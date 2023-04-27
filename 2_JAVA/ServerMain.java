package tn.fa.server;

import java.io.*;
import java.net.*;
import java.util.*;

//유저들을 소캣으로 연결할수잇는
public class ServerMain extends Thread{
	
	ServerSocket ss;
	int port = 6000;
	
	ServerSocket ssC;
	int portC = 7000;
	
	String userName;
	int userScore;
	String userIp;
	
	DatagramSocket serverMsgDs;
	DatagramPacket serverMsgDp;
	InetAddress userIA;
	int port2 = 6032;
	
	String serverMsg;

	FileWriter userLogScoreFW;
	PrintWriter userLogScorePW;
	FileReader userLogFR;
	BufferedReader userLogBR;
	String logFilePath;

	String logScoreFile = "log_Ip_Score.txt";
	
	BufferedReader serverMsgBr = new BufferedReader(new InputStreamReader(System.in));
	
	ArrayList<ServerGetUserInfo> usersList = new ArrayList<ServerGetUserInfo>();
	Hashtable<String, String> usersIpNName = new Hashtable<String, String>(); // ip에 대한 이름
	Hashtable<String, Integer> usersIpNScore = new Hashtable<String, Integer>(); // ip에 대한 score
	Hashtable<String, Boolean> usersIpNIsFirst = new Hashtable<String, Boolean>(); // ip에 대한 처음접속인지 확인
	
	ServerUI sUi1;
	ServerUI sUi2;
	
	
	
	public void run() {
		connect();
	}
	
	
	//항시대기로 유저 온라인 시켜주기
	synchronized void connect() {
	      try{
	         
	         getFirstScore();

	         Runnable nameCR = () ->{
	            try {
	               ssC=new ServerSocket(portC);
	               while(true) {
						Socket tempC = ssC.accept();
						OutputStream osC = tempC.getOutputStream();
						ObjectOutputStream oosC = new ObjectOutputStream(osC);
						oosC.writeObject(usersIpNName);
						oosC.close();
						osC.close();
					}
				}catch (IOException ie){
				}
			};
			Thread nameCT = new Thread(nameCR);
			nameCT.start();
			
			
			ss=new ServerSocket(port);
			while(true){
				System.out.println(port + "번에서 TCP서버 대기중..");
				Socket temp = ss.accept();
				ServerGetUserInfo user = new ServerGetUserInfo(temp, this);
				getUserFirst(user.ip, user.name);

				sUi2.servLogTa.append(user.name+" 님이 입장하셨습니다\n");
				sUi2.servLogTa.setLineWrap(true);

				usersList.add(user);
				sendUsers(user);
				nameScoreList();
			}
		}catch(IOException ie){
			System.out.println(port + "번 포트는 이미 사용중입니다.");
		}finally{
			try{
				for(ServerGetUserInfo user:usersList) {
					user.dis.close();
					user.dos.close();
					user.ois.close();
					user.oos.close();
					user.is.close();
					user.os.close();
				}
				ss.close();
			}catch(IOException ie){
			}
		}
	}

	// 실제로 접속하여 정보 받아올 경우 만들어지는 해시 테이블
	void getUserFirst(String userIp, String userName) {
	      userScore = 0;   // 첫 접속시 score값 0 부여

	      usersIpNName.put(userIp, userName);
	      
	      if(!usersIpNScore.containsKey(userIp))
	    	  usersIpNScore.put(userIp, userScore);

	      
	      usersIpNIsFirst.put(userIp, true);
	      System.out.println(userIp);   // 검증
	      System.out.println(userName);   // 검증
	      nameScoreList();
	}
	
	
	// 처음에 로그파일에서 Ip랑 Score 가져와서 일단 Hashtable에 넣기
	void getFirstScore() {
		try {
			String logSentenceRAW = "";
	         
			File userScoreFile = new File(logFilePath+"\\"+logScoreFile);
	         
			if(userScoreFile.exists()) { // 파일 있을 때
				userLogFR = new FileReader(logFilePath+"\\"+logScoreFile);   //파일 읽어오기
				userLogBR = new BufferedReader(userLogFR);
	            
	            
				while((logSentenceRAW = userLogBR.readLine()) != null) {   // Score Log 파일 읽어서 Score 누적하여 HashTable 업데이트
					logSentenceRAW = logSentenceRAW.trim();
					int scoreIdx = logSentenceRAW.indexOf(" ");
					String userLogIpVS = logSentenceRAW.substring(0, scoreIdx);
	               
					String substringScore = logSentenceRAW.substring(scoreIdx);
					substringScore = substringScore.trim();
					int rawScore = Integer.parseInt(substringScore);

	               
					usersIpNScore.put(userLogIpVS, rawScore);
				}
			}
		}catch(IOException userLogIOE) {
		}finally {
			try{
				if(userLogBR != null) userLogBR.close();
	            if(userLogFR != null) userLogFR.close();
	            System.out.println(logFilePath);
			}catch(IOException ioe){}
		}
	}
	
	
	//유저가 새로고침을 원할시 파일리스트를 새로고침
	void refreshUsersFileList(ServerGetUserInfo wantUser) {
		sendUsers(wantUser);	
	}
	
	
	//유저가 나갈시 나머지 유저들에게 새로고침
	void deleteUser(ServerGetUserInfo delUser){
		System.out.println("User 총원(나가기 전): " + usersList.size());

		usersIpNName.remove(delUser.ip);
		usersList.remove(delUser);
		
		System.out.println("User 총원(나간 후): " + usersList.size());
		/*
	   	System.out.println(usersList.size());
		for(String ip : usersIpNName.keySet()) {
			System.out.println(ip);
		}
	      
		//refreshFileList(delUser); 
		for(ServerGetUserInfo user: usersList) {
			for(ServerGetUserInfo userData: usersList) {
				send("refreshusersList", user.dos);
				send(usersList.size()+"", user.dos);
				if(!user.ip.equals(delUser.ip)){
	   				sendUsers(user);
	    		}
			}
		}*/
	}

	
	//파일리스트 새로고침
	void refreshFileList(ServerGetUserInfo wantUser) {
		try {
			for(ServerGetUserInfo user: usersList){
				if(!user.ip.equals(wantUser.ip)) {
					send("refreshFileList", user.dos);
					user.myFileList.clear();
					user.myFileList = (Hashtable<String, Integer>)user.ois.readObject();
				}
			}          
		}catch(ClassNotFoundException cnfe){
		}catch(IOException ie) {}
	}

	
	//유저 정보를 클라이언트에게 보내기
	void sendUsers(ServerGetUserInfo user) {
		try {
			Socket sends = new Socket(user.ip, 6401);
			OutputStream sendos = sends.getOutputStream();
			DataOutputStream senddos = new DataOutputStream(sendos);
			ObjectOutputStream sendoos = new ObjectOutputStream(sendos);
			InputStream sendis = sends.getInputStream();
			ObjectInputStream sendois = new ObjectInputStream(sendis);

			try{user.myFileList = (Hashtable<String, Integer>)sendois.readObject();}catch(ClassNotFoundException cnfe) {
				System.out.println("!!!!!!! myFileList에 데이터 못 보냄 !!!!!!!!");
			}
			
			for(String filename : user.myFileList.keySet()) {
				System.out.println(filename);
			}
			send(""+usersIpNScore.get(user.ip), senddos);
			//send(""+(usersList.size()-1), senddos);
			for(ServerGetUserInfo ui : usersList){
				System.out.println("ui.ip : " + ui.ip);
				if(!ui.ip.equals(user.ip)) {
					send(ui.ip, senddos);
					send(ui.name, senddos);
					for(String key : ui.myFileList.keySet()){
						System.out.println(key);
					}
					sendoos.writeObject(ui.myFileList);
				}
			}
			System.out.println("sendUserIp : " + user.ip);
	         //System.out.println(user.dos.writeu);
	         //Thread.sleep(3000);
			send("n", senddos);
		}catch(IOException ie) {
		//}catch (InterruptedException e) {
		}
	}

	
	void send(String str, DataOutputStream dos) {
		try {
			dos.writeUTF(str);
			dos.flush();
		}catch(IOException ie) {
		}
		
	}
	
	
	
	void sendMsgAll(String serverMsg) {
		try{
			serverMsgDs = new DatagramSocket();
			serverMsg = serverMsg.trim();
			serverMsg = "[Server]: " + serverMsg;
			byte[] serverMsgArray = serverMsg.getBytes();
			for(String userLogIp : usersIpNName.keySet()) {
				userIA = userIA.getByName(userLogIp);
				serverMsgDp = new DatagramPacket(serverMsgArray, serverMsgArray.length, userIA, port2);
				serverMsgDs.send(serverMsgDp);
			}
		}catch(SocketException se){
		}catch(IOException ioe){
		}finally{
			if(serverMsgDs != null) serverMsgDs.close();
		}
	}
	
	
	//소켓연결을 요청함(3)
	void requestUser(String ip, String port, String wantName, String wantFile, DataOutputStream dos) {
		int i=0;
		for(ServerGetUserInfo user: usersList) {
			if(user.name.equals(wantName)) {
				//send("userOk", dos);
				send("wantFile", user.dos);
				send(ip, user.dos);
				send(port, user.dos);
				send(wantFile, user.dos);
				break;
			}
			i++;
		}
		if(usersList.size() == i) {
			send("notOk", dos);
		}
	}
	
	
	void getScore(String userIp, int plusUserScore) {	// 위 로직으로 hashtable에 덮어쓰기 진행

		int sumScore = 0;
			
		plusUserScore = plusUserScore/1024;
		if(plusUserScore<1) plusUserScore=1;

		sumScore = plusUserScore + usersIpNScore.get(userIp);	//	Score 누적 (메소드가 받아온값<추가> + Hash저장값<이전까지 추가된 sum값>)
		usersIpNScore.put(userIp, sumScore);

		nameScoreList();
		makeLogTxt();
	}

	
	void nameScoreList() {
		sUi2.servPntTa.setText("");
		for(String userLogIp1 : usersIpNName.keySet()) {
			for(String userLogIp2 : usersIpNScore.keySet()) {
				if(userLogIp1.equals(userLogIp2)) {
					String sentenceForList = userLogIp1 + "\t" + usersIpNName.get(userLogIp1) + "     \t" + usersIpNScore.get(userLogIp2);
					sUi2.servPntTa.append(sentenceForList + "\n");
				}
			}
		}
	}
	
	
	// [Ip, Score]에 대한 로그파일 생성(txt)
	void makeLogTxt() {
		try
		{	
			userLogScoreFW = new FileWriter(logFilePath+"\\"+logScoreFile);
			userLogScorePW = new PrintWriter(userLogScoreFW);
			
			for(String userLogIp : usersIpNScore.keySet()) {
				String logSentenceScore = userLogIp + " " + usersIpNScore.get(userLogIp);
				userLogScorePW.println(logSentenceScore);
			}
			
		}catch(IOException userLogIOE){
		}finally{
			try{
				//if(userLogNamePW != null) userLogNamePW.close();
				if(userLogScorePW != null) userLogScorePW.close();
				//if(userLogNameFW != null) userLogNameFW.close();
				if(userLogScoreFW != null) userLogScoreFW.close();
			}catch(IOException ioe){}
		}
		}
}
