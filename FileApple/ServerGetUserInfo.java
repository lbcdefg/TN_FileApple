package tn.fa.server;

import java.io.*;
import java.net.*;
import java.util.*;

//각 유저를 저장할수있는 클래스
public class ServerGetUserInfo extends Thread{
	
	String name;
	String upPath;
	String downPath;
	Hashtable<String, Integer> myFileList = new Hashtable<String, Integer>();
	
	Socket s;
	String ip;
	OutputStream os;
	DataOutputStream dos;
	ObjectOutputStream oos;
	InputStream is;
	DataInputStream dis;
	ObjectInputStream ois;
	   
	ServerMain sm;

	
	
	
	ServerGetUserInfo(Socket temp, ServerMain sm){
		try {
			s = temp;
			ip = temp.getInetAddress().getHostAddress();
			os = temp.getOutputStream();
			dos = new DataOutputStream(os);
			oos = new ObjectOutputStream(os);
			is = temp.getInputStream();
			dis = new DataInputStream(is);
			ois = new ObjectInputStream(is);
			
			this.sm = sm;
			
			
			getUserInfo();
			start();
		}catch(IOException ie) {
		}
	}
	 
	
	//ip랑 파일 리스트 받는곳
	void getUserInfo() {
		System.out.println("getUserInfo");
		try {
			dos.writeUTF(ip);
			dos.flush();
			
			String getName = dis.readUTF();
			getName = getName.trim();
			System.out.println(getName);
			name = getName;
			
			myFileList = (Hashtable<String, Integer>)ois.readObject();
			for(String file:myFileList.keySet()) {
				System.out.println(file + " " + myFileList.get(file));
			}
		}catch(ClassNotFoundException cnce) {
		}catch(IOException ie) {
		}
	}

	
	//내 아이피, 서버포트번호, 연결을 원하는 아이피, 다운받고싶은 파일이름(2)
	public void run(){
		try {
			while(true) {
				System.out.println("!!! Start Sending !!!");
				
				String action = dis.readUTF();

				if(action.equals("wantFile")) {
					String ip = dis.readUTF();
					String port = dis.readUTF();
					String wantName = dis.readUTF();
					String wantFile = dis.readUTF();
					
					sm.requestUser(ip, port, wantName, wantFile, dos);
					
				}else if(action.equals("refreshUserInfo")){
					sm.refreshUsersFileList(this);
		               
				}else if(action.equals("scoreSum")){
					String bytesSum = dis.readUTF();
					int score = Integer.parseInt(bytesSum);
					sm.getScore(ip, score);
				}
				
				System.out.println("!!! End Sending !!!");
			}
		}catch(IOException ie) {
		}finally {
			sm.makeLogTxt();
			sm.deleteUser(this);
			sm.sUi2.servLogTa.append(name+" 님이 퇴장하셨습니다\n");
			sm.nameScoreList();
		}
	}
}
