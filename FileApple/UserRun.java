package tn.fa.user;

import java.io.*;
import java.lang.Character.UnicodeScript;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.plaf.synth.SynthOptionPaneUI;

//로그인시에 서버와 연결 클라이언트
class UserRun extends Thread{
   
   
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
   
	String ip;
	String name;
	String upPath;
	String downPath;
	int score;
   
   
   
	Socket s;
	DatagramSocket singleMsgDs;
	DatagramPacket singleMsgDp;
	InetAddress singleUserIA;
	String serverIp;//수정할것
   
	int port = 6000;
	int port2 = 6032;	// 서버공지
	int port3 = 6033;	// 1:1 쪽지
   
	InputStream is;
	OutputStream os;
	DataInputStream dis;
	DataOutputStream dos;
	ObjectInputStream ois;
	ObjectOutputStream oos;
   
	Vector<String> showFileList = new Vector<String>();
	ArrayList<String> msgIpNMsg = new ArrayList<String>();
	Hashtable<String, Integer> myFileList = new Hashtable<String, Integer>();
	Hashtable<String, String> usersIpName = new Hashtable<String, String>(); // ip에 대한 닉네임
	Hashtable<String, Hashtable<String, Integer>> usersIpNFileList = new Hashtable<String, Hashtable<String, Integer>>(); // ip에 파일리스트
      
	UserUI window;
	//UserUI mainWindow;
   
	
	//서버와 연결
	void connect() {
		try {
			s = new Socket(serverIp, 6000);
         
			is = s.getInputStream();
			dis = new DataInputStream(is);
			ois = new ObjectInputStream(is);
			os = s.getOutputStream();
		   	dos = new DataOutputStream(os);
		   	oos = new ObjectOutputStream(os);
         
	        /*System.out.println("이름 : ");
	        name = br.readLine().trim();
	        System.out.println("uppath : ");
	        upPath = br.readLine();
	        System.out.println("downpath : ");
	        downPath = br.readLine();*/

		   	readFileList();
		   	sendMyInfo();

		   	getUserInfo();

		   	start();
         
		   	Runnable getUserMsg = ()->{
		   		getSingleMsg();
		   	};
		   	Thread getUMThread = new Thread(getUserMsg);
		   	getUMThread.start();
         
		   	Runnable getNotice = ()->{
		   		getServerMsg();
		   	};
		   	Thread getNThread = new Thread(getNotice);
		   	getNThread.start();
			
		}catch(IOException ie) {
			System.out.println("ioecception");
		}
	}
   
	
	void readFileList() {
		myFileList.clear();
	    
		File userFile = new File(upPath);
		File[] userFileArray = userFile.listFiles();
		for(File fileCheck : userFileArray){
			String fileNameCheck = fileCheck.getName();
			if(fileNameCheck.indexOf(".") != -1) {
				long fileBytes = fileCheck.length();
				int fileBytesInt = (int)fileBytes/1024;
				double ifScore = (double)fileBytes/1024;
				if((ifScore > 0) && (ifScore < 1)) fileBytesInt = 1;
				if(ifScore == 0)fileBytesInt = 0;
				myFileList.put(fileNameCheck, fileBytesInt);
				System.out.println(fileNameCheck + " " + fileBytesInt);
			}
		}
	}
   
	
   //나의 정보를 날려주기
	void sendMyInfo(){
		try {
			ip = dis.readUTF();
			send(name);
			oos.writeObject(myFileList);
		}catch(IOException ie) {}
	}
  
	
	//서버한테 tcp로 문자열 보내기
	void send(String str) {
		try {
			System.out.println(str);
			dos.writeUTF(str);
			dos.flush();
		}catch(IOException ie) {
		}      
	}

	
	//유저들 데이터 가져오기 
	void getUserInfo() {
		System.out.println("getUserInfo");
		try {
			ServerSocket getUIss = new ServerSocket(6401);
			Socket getUIs = getUIss.accept();
	         
			InputStream getUIis = getUIs.getInputStream();
			DataInputStream getUIdis = new DataInputStream(getUIis);
			ObjectInputStream getUIois = new ObjectInputStream(getUIis);
			OutputStream getUIos = getUIs.getOutputStream();
			ObjectOutputStream getUIoos = new ObjectOutputStream(getUIos);
			usersIpName.clear();
			usersIpNFileList.clear();
			showFileList.clear();
			
			try{getUIoos.writeObject(myFileList);}catch(IOException ie) {}
			String getScore = getUIdis.readUTF();
			score = Integer.parseInt(getScore);
			while(true){
	            String temp = getUIdis.readUTF();
	            
	            if(temp.equals("n")) break;
	            	String ip = temp;

	            	String name = getUIdis.readUTF();

	            	Hashtable<String, Integer> usersFileList = (Hashtable<String, Integer>)getUIois.readObject();
      
	            	for(String fileName : usersFileList.keySet()) {
	            		showFileList.add(fileName + "   " + name + "   " + usersFileList.get(fileName));
	            	}
	                     
	            	usersIpName.put(ip, name);
	            	usersIpNFileList.put(ip, usersFileList);
			}
			getUIois.close();
			getUIdis.close();
			getUIis.close();
			getUIs.close();
			getUIss.close();
		}catch(ClassNotFoundException cnfe){	
		}catch(IOException ie) {
		}
	}

	
   //내 아이피, 서버포트번호, 연결을 원하는 아이피, 다운받고싶은 파일이름(1)
   void requestConnect(String wantName, String wantFile){
		try {
			refreshUserInfo();
			boolean userOk = false;
			for(String ip : usersIpName.keySet()) {
				if(usersIpName.get(ip).equals(wantName))userOk = true;
			}
			if(userOk){
				ServerSocket ss;
				int port = 6031;
				Socket s;
				
				ss=new ServerSocket(port);
				
				send("wantFile");
				send(ip);
				send(""+port);
	
				wantName = wantName.trim();
				send(wantName);
	
				wantFile = wantFile.trim();
				send(wantFile);
				s=ss.accept();
				
				InputStream reIs = s.getInputStream();
				DataInputStream reDis = new DataInputStream(reIs);
				
				if(reDis.readUTF().equals("fileOK")) {
					receiveFile(s, wantFile, wantName);
				}else {
					JOptionPane.showMessageDialog(window,"파일이 존재하지 않습니다", "다운로드", JOptionPane.INFORMATION_MESSAGE);
				}
				
				reDis.close();
				reDis.close();
				ss.close();
			}else {

				refreshUserInfo();
				JOptionPane.showMessageDialog(window,"유저가 존재하지 않습니다", "다운로드", JOptionPane.INFORMATION_MESSAGE);
			}
		}catch(IOException ie) {}
	}
   
   
   	void refreshUserInfo() {
   		send("refreshUserInfo");
   		readFileList();
   		getUserInfo();
	}
   
   	
   	// 서버 공지 받기 기능
	void getServerMsg() {
		DatagramSocket userDS = null;
		DatagramPacket userDP = null;
		try{
			userDS = new DatagramSocket(port2);
			byte[] serverMsgArr = new byte[2048];
			userDP = new DatagramPacket(serverMsgArr, serverMsgArr.length);

			while(true){
				userDS.receive(userDP);
				String serverMsg = new String(serverMsgArr, StandardCharsets.UTF_8);
				serverMsg = serverMsg.trim();
				for(int i=0; i<serverMsgArr.length; i++) serverMsgArr[i]=0;
				window.noticeTA.append(serverMsg + "\n");
			}
		}catch(SocketException se){
		}catch(IOException ioe){
		}finally{
			if(userDS != null) userDS.close();
		}
	}
   
	
	// 1:1 쪽지 보내기 기능
	void sendMsgUser(String valName , String singleMsg) {
		try {
			singleMsgDs = new DatagramSocket();
			singleMsg = singleMsg.trim();
			singleMsg = "[" + name + "]: " + singleMsg;

			byte[] singleMsgArray = singleMsg.getBytes();
			
			for(String userValName : usersIpName.keySet()) {
				String versusSen = usersIpName.get(userValName);
				if(valName.equals(versusSen)) {
					singleUserIA = singleUserIA.getByName(userValName);
					singleMsgDp = new DatagramPacket(singleMsgArray, singleMsgArray.length, singleUserIA, port3);
					singleMsgDs.send(singleMsgDp);
					msgIpNMsg.add(userValName + " " + "send" + " " + singleMsg);
				}
			}
		}catch(SocketException se){
		}catch(IOException ioe){
		}finally{
			if(singleMsgDs != null) singleMsgDs.close();
		}
	}
   
	////////////////////////////////////////////////////////////
	// 1:1 쪽지 받기 기능
	void getSingleMsg() {
		DatagramSocket userSingleDS = null;
		DatagramPacket userSingleDP = null;
		try{
			userSingleDS = new DatagramSocket(port3);
			byte[] singleMsgArray = new byte[2048];
			userSingleDP = new DatagramPacket(singleMsgArray, singleMsgArray.length);

			while(true){
				userSingleDS.receive(userSingleDP);
				String singleGetMsg = new String(singleMsgArray, StandardCharsets.UTF_8);
				singleGetMsg = singleGetMsg.trim();

				InetAddress getsingleIA = userSingleDP.getAddress();
				String getSingleIP = getsingleIA.getHostAddress();
				msgIpNMsg.add(getSingleIP + " " + "receive" + " " + singleGetMsg);
				
				refreshUserInfo();
				try {
					Thread.sleep(100);
				}catch(InterruptedException ie) {}
				
				String singleModifiedMsg = "[" + usersIpName.get(getSingleIP) + "] 님에게 메세지 도착";
				window.mailTA.append(singleModifiedMsg + "\n");

				for(int i=0; i<singleMsgArray.length; i++) singleMsgArray[i]=0;
			}
		}catch(SocketException se){
		}catch(IOException ioe){
		}finally{
			if(userSingleDS != null) userSingleDS.close();
		}
	}
	
   
	// 파일 다운로드 해주는 기능
	void receiveFile(Socket s, String wantFile, String wantName) {
		InputStream userIS = null;
		FileInputStream userFIS = null;
		BufferedInputStream userBIS = null;
             
		OutputStream userOS = null;
		FileOutputStream userFOS = null;
		BufferedOutputStream userBOS = null;
		try{
            
			int sizeOfFiles = 1024 * 100;   // 100 KB
			byte[] fileBuffer = new byte[sizeOfFiles];
			int bytesAmount = 0;
            
			userIS = s.getInputStream(); //   소켓에서 읽어오기
			userBIS = new BufferedInputStream(userIS, sizeOfFiles);

			//(2) File과 연결된 스트림 
			wantFile = wantName + "_" + wantFile;
			userFOS = new FileOutputStream(downPath+ "\\"+ wantFile); // 다운받을 폴더로 파일 이름 정해서 가져오기
			userBOS = new BufferedOutputStream(userFOS, sizeOfFiles);
                 
			while ((bytesAmount = userBIS.read(fileBuffer)) != -1) {   // 소켓에서 읽어서 파일 다운로드
				userBOS.write(fileBuffer, 0, bytesAmount);
				userBOS.flush();
			}
		}catch(FileNotFoundException userUploadFNFE) {
			System.out.println("파일경로를 확인하여주세요.");
		}catch(IOException userUploadIOE) {
		}finally{
			try{
				userBIS.close();
				userIS.close();
				userBOS.close();
				userFOS.close();
			}catch(IOException ioe){}
		}
	}
   

	public void run(){
		response();
	}
   
	
	//파일 다운을 요청받으면 소켓연결해서 파일 보내주기(4)//파일리스트 새로고침을 받으면 리스트 보내주기
	void response(){
		try {
			while(true) {
				String action = dis.readUTF();
				
				if(action.equals("wantFile")) {
					String ip = dis.readUTF();
					int port = Integer.parseInt(dis.readUTF());
					String wantFile = dis.readUTF();
					Socket responseS = new Socket(ip, port);
					OutputStream responseOs = responseS.getOutputStream();
					DataOutputStream responseDos = new DataOutputStream(responseOs);
					int i=0;
					for(String file:myFileList.keySet()) {
						if(file.equals(wantFile)) {
							responseDos.writeUTF("fileOK");
							responseDos.flush();
							fileSend(responseS, wantFile);
							break;
						}
						i++;
						//파일이 있으면 잇다고 
					}
					if(myFileList.size() == i) {
						responseDos.writeUTF("notOK");
					}
					responseDos.close();
					responseOs.close();
					responseS.close();
				}else if(action.equals("refreshFileList")) {
					readFileList();
					oos.writeObject(myFileList);
				}else if(action.equals("refreshUserList")) {
					String size = dis.readUTF();
					usersIpName.clear();
					usersIpNFileList.clear();
					if(!size.equals("1"))getUserInfo();
				}
			}
		}catch(IOException ie) {
		}finally {
			System.out.println("서버가 종료되었습니다 2초뒤에 종료됩니다.");
			try {
				send("cutConnect");
				dis.close();
				dos.close();
				oos.close();
				ois.close();
				is.close();
        	 	os.close();
        	 	Thread.sleep(2000);
        	 	System.exit(-1);
         	}catch(IOException ie){
         	}catch(InterruptedException ine) {}
		}
	}
   
	
	// 파일 업로드 해주는 메소드
	void fileSend(Socket s, String wantFile){

		FileInputStream userFIS = null;
		BufferedInputStream userBIS = null;

		OutputStream userOS = null;
		BufferedOutputStream userBOS = null;
       
		int bytesSum = 0;
       
		File userUpload = new File(upPath + "\\" + wantFile);   //업로드 해줄 파일의 경로와 파일명
           
		try{
              
			int sizeOfFiles = 1024 * 100;   // 100 KB
			byte[] fileBuffer = new byte[sizeOfFiles];
			int bytesAmount = 0;
              
			userFIS = new FileInputStream(userUpload);   // 파일을 불러와서 소켓에 넣을 준비
			userBIS = new BufferedInputStream(userFIS);
              
			userOS = s.getOutputStream();   // 소켓으로 파일 업로드
			userBOS = new BufferedOutputStream(userOS, sizeOfFiles);
              
              
			while ((bytesAmount = userBIS.read(fileBuffer)) != -1) {   // 파일 경로에서 읽어온 파일을 소켓을 통해 보냄.
				userBOS.write(fileBuffer, 0, bytesAmount);
				userBOS.flush();
				bytesSum = bytesSum + bytesAmount;
			}
			send("scoreSum");
			send(""+bytesSum);
			refreshUserInfo();
			userBIS.close();
			userFIS.close();
			userBOS.close();
			userOS.close();
		}catch(FileNotFoundException userUploadFNFE) {
			System.out.println("파일경로를 확인하여주세요.");
		}catch(IOException userUploadIOE) {
		}
	}
   
	
	void getName(String userIp, String userName) {
		usersIpName.put(userIp, userName);
	}
      
      
	void getFileList(String userIp, Hashtable<String, Integer> userFileList) {
		usersIpNFileList.put(userIp, userFileList);
	}
}