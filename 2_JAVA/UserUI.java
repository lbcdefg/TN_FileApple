package tn.fa.user;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.lang.reflect.Field;
import java.nio.charset.Charset;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

class UserUI extends JFrame implements ActionListener{
///////////////////////////유저 접속창 변수
	JPanel userCp;   
	JTextField nameTf, serverIpTf, uploadTf, downloadTf;
	JButton downloadPathB, uploadPathB , enterB;
	////////////////////////유저메인창 변수
	String folderPath;
	String name;
	JFileChooser chooser;
	FileNameExtensionFilter filter;
	JPanel contentPane;
	JFrame frame1;
	JButton refreshB, clearB;   
	static JTextArea mailTA;
	///////////////////////벡터선언
	Vector<String> dirData = new Vector<String>();
	Vector<String> fListData = new Vector<String>();
	Vector<String> uListData = new Vector<String>();
	///////////////////////dir변수
	JFrame dirF;
	static JList<String> dirList, fList, uList;
	JPanel panel;
	JLabel dirL1, dirL2;
	int i=0;
	JTextField letterTf;
	static JTextArea letterTa;
	JLabel letterL;
	JScrollPane dirTaScp;
	JScrollPane dirTfScp, dirListScp, mailScroll;
	//////////ask변수
	JFrame askF;
	JPanel askP;
	JLabel askL;
	JButton yesB, noB, startB;
	/////////////로딩변수
	JFrame jFrame;
	
	static String valName;
	static JTextArea noticeTA;
	static JLabel nameL;
	static JLabel pointL;
    
	static UserRun ur;
    
	static UserUI frame;
	static int scoreTotal;
	static String scoreTotalStr;

   public static void main(String[] args) {
	   System.setProperty("file.encoding","UTF-8");
	   try{
		   Field charset = Charset.class.getDeclaredField("defaultCharset");
		   charset.setAccessible(true);
		   charset.set(null,null);
	   }
	   catch(Exception e){
	   }
	   
	   EventQueue.invokeLater(new Runnable() {
		   public void run() {
			   try {
				   frame = new UserUI();
				   frame.setVisible(true);
			   } catch (Exception ee) {
				   ee.printStackTrace();
			   }
		   }
	   });
   }
   
   UserUI(){
      init();
      DirInit();
      ask();
   }
   
   String browse() {
	      chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	      chooser.setCurrentDirectory(new File("/"));
	      chooser.setAcceptAllFileFilterUsed(true);
	      chooser.setDialogTitle("경로 탐색");
	      chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	      filter = new FileNameExtensionFilter("Binary File", "cd11");
	      chooser.setFileFilter(filter);
	      int returnVal = chooser.showOpenDialog(null); 
	         if(returnVal == JFileChooser.APPROVE_OPTION) { // 열기를 클릭 
	            folderPath = chooser.getSelectedFile().toString();
	         }else if(returnVal == JFileChooser.CANCEL_OPTION){ // 취소를 클릭
	         System.out.println("cancel"); 
	         folderPath = "";
	         }              
	         return folderPath;
	   }

   void init() {
      setTitle("FileApple ver 1.0");
      setResizable(false);
      
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(300, 150, 430, 430);
      userCp = new JPanel();
      Toolkit tk1 = Toolkit.getDefaultToolkit();
      Image i1 = tk1.getImage(".\\imgs\\icon.png");
      setIconImage(i1);
      userCp.setBackground(new Color(255, 220, 100));
      userCp.setBorder(new EmptyBorder(5, 5, 5, 5));

      setContentPane(userCp);
      userCp.setLayout(null);
      
      nameTf = new JTextField();
      nameTf.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 16));
      nameTf.setBounds(165, 155, 160, 27);
      userCp.add(nameTf);
      
      serverIpTf = new JTextField();
      serverIpTf.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 16));
      serverIpTf.setBounds(165, 195, 160, 27);
      userCp.add(serverIpTf);
      
      uploadTf = new JTextField();
      uploadTf.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 16));
      uploadTf.setBounds(165, 235, 160, 27);
      userCp.add(uploadTf);
      
      downloadTf = new JTextField();
      downloadTf.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 16));
      downloadTf.setBounds(165, 275, 160, 27);
      userCp.add(downloadTf);
      
      JLabel nameL = new JLabel("이름");
      nameL.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 15));
      nameL.setBounds(30, 155, 125, 30);
      userCp.add(nameL);
      
      JLabel serveripL = new JLabel("서버 아이피");
      serveripL.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 15));
      serveripL.setBounds(30, 195, 125, 30);
      userCp.add(serveripL);
      
      JLabel uploadpL = new JLabel("업로드 파일경로");
      uploadpL.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 15));
      uploadpL.setBounds(30, 235, 125, 30);
      userCp.add(uploadpL);
      
      JLabel downloadpL = new JLabel("다운로드파일경로");
      downloadpL.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 15));
      downloadpL.setBounds(30, 275, 125, 30);
      userCp.add(downloadpL);
      
      uploadPathB = new JButton("");
      uploadPathB.setForeground(new Color(255, 220, 100));
      uploadPathB.setIcon(new ImageIcon(".\\imgs\\browse.png"));
      uploadPathB.setBackground(new Color(250, 214, 78));
      uploadPathB.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 11));
      uploadPathB.setBounds(325, 237, 35, 27);
      uploadPathB.addActionListener(this);
      uploadPathB.setBorderPainted(false);
      uploadPathB.setFocusPainted(false);
      userCp.add(uploadPathB);
      
      downloadPathB = new JButton("");
      downloadPathB.setForeground(new Color(255, 220, 100));
      downloadPathB.setBackground(new Color(250, 214, 78));
      downloadPathB.setIcon(new ImageIcon(".\\imgs\\browse.png"));
      downloadPathB.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 11));
      downloadPathB.setBounds(325, 277, 35, 27);
      downloadPathB.setBorderPainted(false);
      downloadPathB.setFocusPainted(false);
      downloadPathB.addActionListener(this);
      userCp.add(downloadPathB);
      
      enterB = new JButton("접속");
      enterB.setBackground(Color.WHITE);
      enterB.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 20));
      enterB.setBounds(165, 320, 100, 50);
      enterB.addActionListener(this);
      userCp.add(enterB);
      
      JLabel mainIcon = new JLabel("");
      mainIcon.setIcon(new ImageIcon(".\\imgs\\icon.png"));
      mainIcon.setBounds(46, 20, 172, 135);
      userCp.add(mainIcon);
      
      JLabel lblNewLabel_1 = new JLabel("");
      lblNewLabel_1.setIcon(new ImageIcon(".\\imgs\\fa2.png"));
      lblNewLabel_1.setBounds(141, 44, 344, 101);
      userCp.add(lblNewLabel_1);
      
      JLabel userInBg = new JLabel();
      userInBg.setIcon(new ImageIcon(".\\imgs\\ClientInBg.jpg"));
      userInBg.setBounds(0, 0, 443, 424);
      userCp.add(userInBg);
   }
   
   
   void init2(){
	   setTitle("FileApple ver 1.0");
	   setResizable(false);
	   Toolkit tk2 = Toolkit.getDefaultToolkit();
	   Image i2 = tk2.getImage(".\\imgs\\icon.png");
	   setIconImage(i2);
	      
	   setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   setBounds(100, 100, 800, 650);
	   contentPane = new JPanel();
	   contentPane.setBackground(new Color(255, 220, 100));
	   contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

	   setContentPane(contentPane);
	   contentPane.setLayout(null);
	      
	   JLabel filelistL = new JLabel("파일리스트");
	   filelistL.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 20));
	   filelistL.setBounds(284, -15, 149, 74);
	   contentPane.add(filelistL);
	   
	   JScrollPane noticeScroll = new JScrollPane();
	   noticeScroll.setBounds(284, 451, 490, 124);
	   contentPane.add(noticeScroll);
	   
	   noticeTA = new JTextArea();
	   noticeTA.setEditable(false);
	   noticeTA.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 15));
	   noticeTA.setBounds(284, 451, 490, 124);
	   noticeTA.setLineWrap(true);
	   noticeTA.setWrapStyleWord(true);
	   noticeScroll.setViewportView(noticeTA);
       

	   /**
       서버가 클라이언트한테 공지
        1.서버가 서버창 텍스트필드에 작성한다
        2. 작성내용을 read해서 udp를 통해 write Utf로 소켓에 담아 보낸다
        4. 클라이언트는 readUtf를 통해 받는다
	    */
      
      
	   ///////////////////파일리스트 벡터값

   
	   fList = new JList<String>(fListData);
       fList.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 14));
       fList.setBounds(284, 41, 490, 380);
       fList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       fList.setValueIsAdjusting(true);
       fList.setSelectionBackground(new Color(191,255,100));
       fList.addMouseListener(new mouseAdaptor());
       fList.setLayout(null);
       //fList.setFixedCellWidth(1500);
       fList.setFixedCellHeight(30);
       JScrollPane f = new JScrollPane();
       f.setBounds(284, 41, 490, 380);
       contentPane.add(f);
       f.setViewportView(fList);
       
  
       JLabel cListL = new JLabel("접속자리스트");
       cListL.setBounds(15, 180, 178, 34);
       contentPane.add(cListL);
       cListL.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 20));
      
       /////////////////////접속자리스트벡터값


       uList = new JList<String>(uListData);
       uList.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 14));
       uList.setBounds(15, 217, 257, 204);
       uList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       uList.setValueIsAdjusting(true);
       uList.setSelectionBackground(new Color(191,255,100));
       uList.addMouseListener(new mouseAdaptor2());
       uList.setLayout(null);
       uList.setFixedCellWidth(470);
       uList.setFixedCellHeight(30);
       JScrollPane s = new JScrollPane();

       s.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       s.setBounds(15,217,257,204);
       contentPane.add(s);
       s.setViewportView(uList);
     
       JLabel Co = new JLabel("Tropical Night Co.");
       Co.setBackground(new Color(255, 255, 255));
       Co.setFont(new Font("나눔스퀘어 네오 ExtraBold", Font.BOLD, 11));
       Co.setBounds(650, 582, 140, 23);
       contentPane.add(Co);

     
       refreshB = new JButton("새로고침");
       refreshB.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 13));
       refreshB.setBackground(Color.WHITE);
       refreshB.setBounds(679, 10, 95, 23);
       contentPane.add(refreshB);
       refreshB.addActionListener(this);
       
       /*JPanel mailP = new JPanel();
       mailP.setBounds(15, 472, 257, 131);
       contentPane.add(mailP);*/
       mailScroll = new JScrollPane();
       mailScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       mailScroll.setBounds(15, 451, 257, 124);
       
       contentPane.add(mailScroll);
       
       mailTA = new JTextArea();
       mailTA.setEditable(false);
       mailTA.setLineWrap(true);
       mailTA.setWrapStyleWord(true);
       mailTA.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 15));
       mailTA.setBounds(15, 451, 257, 124);
     
       mailScroll.setViewportView(mailTA);

       
       dirTfScp = new JScrollPane();
       dirTfScp.setBounds(30, 350, 265, 31);
       panel.add(dirTfScp);
       
       JLabel mailL = new JLabel("쪽지함");
       mailL.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 20));
       mailL.setBounds(15, 400, 149, 74);
       contentPane.add(mailL);
        
       JLabel noticeL = new JLabel("서버공지");
       noticeL.setBounds(284, 425, 95, 23);
       contentPane.add(noticeL);
       noticeL.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 20));
        
       JLabel clientIcon = new JLabel("");
       clientIcon.setIcon(new ImageIcon(".\\imgs\\movingpine.gif"));
       clientIcon.setBounds(45, 80, 109, 108);
       contentPane.add(clientIcon);

       JLabel clientIconBg = new JLabel("");
       clientIconBg.setIcon(new ImageIcon(".\\imgs\\tn.png"));
       clientIconBg.setBounds(-20, 20, 200, 200);
       contentPane.add(clientIconBg);
       
       //"NAME" 대신에 name 변수 넣어야함

  
       nameL.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 20));
       nameL.setBounds(136, 92, 135, 31);
       contentPane.add(nameL);
       
       //"보유포인트" 대신에 point 변수 넣어야함
       
       pointL.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 16));
       pointL.setBounds(136, 133, 129, 47);
       contentPane.add(pointL);
       
       JLabel lblNewLabel = new JLabel("");
       lblNewLabel.setBounds(25, -5, 300, 88);
       contentPane.add(lblNewLabel);
       lblNewLabel.setIcon(new ImageIcon(".\\imgs\\fa2.png"));

       JLabel userBg = new JLabel();
       userBg.setIcon(new ImageIcon(".\\imgs\\ClientBg.jpg"));
       userBg.setBounds(0, -120, 936, 873);
       contentPane.add(userBg);
 
       ur.refreshUserInfo();
       fListData.clear();
       for(String getUsers : ur.showFileList) {
    	   fListData.addElement(getUsers);
       }
       fList.setListData(fListData);
     
       uListData.clear();
       for(String getUsersIp : ur.usersIpName.keySet()) {
    	   if(!(ur.usersIpName.get(getUsersIp)).equals(name)){
    		   uListData.addElement(ur.usersIpName.get(getUsersIp));
    	   }else if((ur.usersIpName.get(getUsersIp)).equals(name)) {   		
    	   }
       }
       uList.setListData(uListData);
   }
   
   	
   void DirInit() {
	   dirF = new JFrame();
	   dirF.setTitle("FileApple ver 1.0");
	   dirF.setBounds(100, 100, 600, 437);
	   dirF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	   dirF.getContentPane().setLayout(null);
	   dirF.setResizable(false);
	      
	   panel = new JPanel();
	   panel.setBackground(new Color(255, 220, 100));
	   panel.setBounds(0, 0, 700, 452);
	   dirF.getContentPane().add(panel);
	   panel.setLayout(null);
	      
	   dirListScp = new JScrollPane();

       dirListScp.setBounds(312, 92, 247, 289);
       panel.add(dirListScp);
      
       /////////////////////////디렉토리 벡터값

     
       dirList = new JList<String>(dirData);
       dirList.setBounds(312, 92, 247, 289);
       dirList.getPreferredScrollableViewportSize();
       dirList.setFont(new Font("나눔스퀘어 네오 Bold",Font.PLAIN, 13));
       dirList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       dirList.setValueIsAdjusting(true);
       dirList.getScrollableTracksViewportHeight();
       dirList.setSelectionBackground(Color.LIGHT_GRAY);
       dirList.addMouseListener(new mouseAdaptor3());
       dirListScp.setViewportView(dirList);

       dirL1 = new JLabel();
       dirL1.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 18));
       dirL1.setBounds(161, 10, 280, 43);
       panel.add(dirL1);

       dirL2 = new JLabel("파일리스트");
       dirL2.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 17));
       dirL2.setBounds(312, 46, 90, 61);
       panel.add(dirL2);
       
       dirTaScp = new JScrollPane();
    
       dirTaScp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       dirTaScp.setBounds(30, 92, 265, 256);
       panel.add(dirTaScp);

       letterTa = new JTextArea();
       letterTa.setEditable(false);
       letterTa.setLineWrap(true);
       letterTa.setFont(new Font("나눔스퀘어 네오 Bold", Font.BOLD, 15));
       letterTa.setWrapStyleWord(true);
       dirTaScp.setViewportView(letterTa);

       letterTf = new JTextField();
       letterTf.setBounds(30, 350, 265, 31);
       panel.add(letterTf);
       letterTf.addActionListener(this);
       
       letterL = new JLabel("쪽지함");
       letterL.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 17));
       letterL.setBounds(30, 45, 71, 63);
       panel.add(letterL);
   }

   void ask() {
	      askF = new JFrame();
	      askF.setTitle("FileApple ver 1.0");
	      askF.setBounds(100, 100, 307, 171);
	      askF.setLocationRelativeTo(null);
	      askF.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	      askF.getContentPane().setLayout(null);
	      
	      askP = new JPanel();
	      askP.setBackground(new Color(255, 220, 100));
	      askP.setBounds(0, 0, 291, 132);
	      askF.getContentPane().add(askP);
	      askP.setLayout(null);
	      
	      askL = new JLabel("다운 받으시겠습니까?");
	      askL.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 18));
	      askL.setBounds(65, 10, 172, 55);
	      askP.add(askL);
	      
	      yesB = new JButton("네");
	      yesB.setBackground(new Color(255, 255, 255));
	      yesB.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 12));
	      yesB.setBounds(47, 77, 75, 34);
	      yesB.addActionListener(this);
	      askP.add(yesB);
	      
	      noB = new JButton("아니요");
	      noB.setBackground(new Color(255, 255, 255));
	      noB.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 12));
	      noB.setBounds(169, 77, 75, 34);
	      noB.addActionListener(this);
	      askP.add(noB);
	   }
	   /////////////////////////로딩프로그래스 일단 대기
	 /*  void loading(){
	      JFrame jFrame = new JFrame();
	       jFrame.setTitle("FileApple ver 1.0");
	       jFrame.getContentPane().setBackground(new Color(255, 220, 100));
	       jFrame.setLocationRelativeTo(null);
	       jFrame.getContentPane().setLayout(null);
	       
	       JLabel lblNewLabel_1 = new JLabel("");
	       lblNewLabel_1.setIcon(new ImageIcon("C:\\Users\\kosmo\\Desktop\\Woo\\eclipse\\workspace\\FineApple\\tn\\fa\\server\\imgs\\runningpine.gif"));
	       lblNewLabel_1.setBounds(58, 15, 119, 88);
	       jFrame.getContentPane().add(lblNewLabel_1);
	       
	       JLabel lblNewLabel = new JLabel("다운로드중...");
	       lblNewLabel.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 17));
	       lblNewLabel.setBounds(170, 50, 111, 34);
	       jFrame.getContentPane().add(lblNewLabel);
	       
	       jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	       jFrame.setSize(382, 157);
	       jFrame.setVisible(true);

	           
	           // update progressbar staring from 0 to 100

           
           
   }*/
   
   ///////////////////////파일리스트 마우스어댑터
 class mouseAdaptor extends MouseAdapter{
     public void mouseClicked(MouseEvent e) {
    	 String cutSentence1 = "";
    	 String cutSentence2 = "";
    	 String cutSentence3 = "";
    	 String cutExtension = "";
    	 String cutName = "";
    	 
    	 if (e.getClickCount() == 2) {
        	Object val = (Object)fList.getSelectedValue();
           
        	String data = val.toString();
        	cutSentence1 = data;
            int idx1 = cutSentence1.indexOf(".");
           	cutSentence1 = cutSentence1.substring(0, idx1);	// "."점까지가 Sen1
           	cutSentence1 = cutSentence1.trim();
           	
          	cutSentence2 = data.substring(idx1); // "."점 뒤부터 Sen2
          	cutSentence2 = cutSentence2.trim();

          	cutSentence3 = cutSentence2;
          	int idx2 = cutSentence2.indexOf(" ");
          	cutExtension = cutSentence2.substring(0, idx2); // "."점 뒤부터 확장자 부분만 잘라서 Ext
          	cutExtension = cutExtension.trim();

          	cutSentence3 = cutSentence3.substring(idx2); // 이름 있는 부분부터 그 뒤까지
          	cutSentence3 = cutSentence3.trim();
          	
           	int idx3 = cutSentence3.indexOf(" ");
           	cutName = cutSentence3.substring(0, idx3); // 이름부분 잘라서 Name
           	cutName = cutName.trim();
      		
           	String filename = cutSentence1 + cutExtension;
           	String warningExt = cutExtension.substring(1, cutExtension.length());
           	
      		if(warningExt.equalsIgnoreCase("exe")) {
      			int anwser = JOptionPane.showConfirmDialog(null, "확장자가 EXE 파일입니다.. 받으시겠습니까?", "경고 창", JOptionPane.YES_NO_OPTION);
      			if(anwser == JOptionPane.YES_OPTION) {
      				ur.requestConnect(cutName, filename);
      	        }
      		}else {
      			ur.requestConnect(cutName, filename);
      		}
        }
     }
  }


 class mouseAdaptor2 extends MouseAdapter{
    public void mouseClicked(MouseEvent e) {
    	String cutSentence = "";
    	String cutSentence1 = "";
    	String cutSentence2 = "";
    	String cutSentence3 = "";

    	String ipVers = "";
    	String ipVers1 = "";
    	String ipVers2 = "";
    	Object val = (Object)uList.getSelectedValue();

    	if ((e.getClickCount() == 2) && (val != null)) {
            
            valName = (String)val;
            
            dirData.clear();
            
            
            for(String getUsersFile : ur.showFileList) {
            	
            	cutSentence = getUsersFile;
            	int idx1 = cutSentence.indexOf(".");
            	cutSentence = cutSentence.substring(idx1);
            	cutSentence = cutSentence.trim();
            	int idx2 = cutSentence.indexOf(" ");
            	
            	cutSentence = cutSentence.substring(idx2);
            	cutSentence = cutSentence.trim();
            	int idx3 = cutSentence.indexOf(" ");
            	cutSentence = cutSentence.substring(0, idx3);
            	cutSentence = cutSentence.trim();
            	
            	if(valName.equals(cutSentence)) {
            		dirData.addElement(getUsersFile);
            	}
            }
            
        	for(String ipVersus : ur.usersIpName.keySet()) {
        		String nameVer = ur.usersIpName.get(ipVersus);
        		if(nameVer.equals(valName)) {
        			ipVers = ipVersus;
        		}
        	}

            UserUI window1 = new UserUI();
            window1.dirF.setVisible(true);
            window1.dirL1.setText("[ " + valName + " ] 의 토리 쉐어하우스");
            window1.letterTa.setText("");
            for(String singleMsg : ur.msgIpNMsg) {
            	cutSentence1 = singleMsg;
            	cutSentence1 = cutSentence1.trim();
            	cutSentence2 = cutSentence1;
            	
            	int idxM1 = cutSentence1.indexOf(" ");
            	ipVers1 = cutSentence1.substring(0, idxM1);	// IP 자르기
            	ipVers1 = ipVers1.trim();

            	cutSentence2 = cutSentence2.substring(idxM1);
            	cutSentence2 = cutSentence2.trim();
            	cutSentence3 = cutSentence2;
            	int idxM2 = cutSentence2.indexOf(" ");
            	ipVers2 = cutSentence2.substring(0, idxM2);	// send/receive 구분자
            	ipVers2 = ipVers2.trim();
            	
            	cutSentence3 = cutSentence2.substring(idxM2);	// 메세지 내용
            	cutSentence3 = cutSentence3.trim();
            	
            	if(ipVers1.equals(ipVers)) {
            		if(ipVers2.equals("receive")) {
            			window1.letterTa.append(cutSentence3 + "\n");
            		}else if(ipVers2.equals("send")) {
            			window1.letterTa.append(cutSentence3 + "\n");
            		}
            	}

            }
            
            window1.dirList.setListData(dirData);
       }
    }
 }
    
 class mouseAdaptor3 extends MouseAdapter{
     public void mouseClicked(MouseEvent e) {
    	 String cutSentence1 = "";
    	 String cutSentence2 = "";
    	 String cutSentence3 = "";
    	 String cutExtension = "";
    	 String cutName = "";
    	 
    	 if (e.getClickCount() == 2) {
           //int row = list.getSelectedIndex();
        	Object getL = (Object)dirList.getSelectedValue();
           
           	String getLStr = getL.toString();
           	cutSentence1 = getLStr;
            int idx1 = cutSentence1.indexOf(".");
           	cutSentence1 = cutSentence1.substring(0, idx1);	// "."점까지가 Sen1
           	cutSentence1 = cutSentence1.trim();
           	
          	cutSentence2 = getLStr.substring(idx1); // "."점 뒤부터 Sen2
          	cutSentence2 = cutSentence2.trim();

          	cutSentence3 = cutSentence2;
          	int idx2 = cutSentence2.indexOf(" ");
          	cutExtension = cutSentence2.substring(0, idx2); // "."점 뒤부터 확장자 부분만 잘라서 Ext
          	cutExtension = cutExtension.trim();

          	cutSentence3 = cutSentence3.substring(idx2); // 이름 있는 부분부터 그 뒤까지
          	cutSentence3 = cutSentence3.trim();

           	int idx3 = cutSentence3.indexOf(" ");
           	cutName = cutSentence3.substring(0, idx3); // 이름부분 잘라서 Name
           	cutName = cutName.trim();
      		
           	String filename = cutSentence1 + cutExtension;
      		
          	ur.requestConnect(cutName, filename);
        }
     }
  }
   
   @Override
   	public void actionPerformed(ActionEvent e) {
	   Object obj = e.getSource();
         if(obj == uploadPathB) {
           uploadTf.setText(browse());
         }
         if(obj == downloadPathB) {
           downloadTf.setText(browse());
         }
         if(obj == enterB) {
             name = nameTf.getText();
             name = name.replaceAll(" ", "");
             String serverIp = serverIpTf.getText();
             String downloadPath = downloadTf.getText();
             String uploadPath = uploadTf.getText();
             File df = new File(downloadPath); // 다운로드 파일경로 검증
             File uf = new File(uploadPath); // 다운로드 파일경로 검증
             
          
          try
            {
             Socket serverS = new Socket(serverIp, 7000);
             
             InputStream uiis = serverS.getInputStream();
             ObjectInputStream uiois = new ObjectInputStream(uiis);

             Hashtable<String, String> serverInfoIpName = (Hashtable<String, String>)uiois.readObject();
    
             if(name.isEmpty() || serverIp.isEmpty() || downloadPath.isEmpty()|| uploadPath.isEmpty()) {
            	 JOptionPane.showMessageDialog(null, "전부 입력해주세요!");
             }else if((!(df.exists())) || (!(uf.exists()))) {
            	 JOptionPane.showMessageDialog(null, "폴더경로가 맞지 않습니다.", "경고 창", JOptionPane.PLAIN_MESSAGE);
             }else if(name.length() > 6) {
            	 nameTf.setText("");
            	 JOptionPane.showMessageDialog(null, "닉네임 6글자 이내로 제한", "경고 창", JOptionPane.PLAIN_MESSAGE);
             }else if(serverInfoIpName.size() != 0) {
            	 boolean nickCheck = false;
            	 for(String versusName : serverInfoIpName.keySet()) {
            		 if(name.equals(serverInfoIpName.get(versusName))) {
            			 nickCheck = true;
            		 }
            	 }   
                 if(nickCheck) {
                	 nameTf.setText("");
                	 JOptionPane.showMessageDialog(null, "닉네임 중복", "경고 창", JOptionPane.PLAIN_MESSAGE);
                 }else {
                 
                	 JOptionPane.showMessageDialog(null, "접속완료!", "Confirm!!", JOptionPane.PLAIN_MESSAGE);
                	 ur = new UserRun();
                	 ur.name = name;
                	 ur.serverIp = serverIp;
                	 ur.downPath = downloadPath;
                	 ur.upPath = uploadPath;
                	 ur.connect();
                	 serverS.close();
    
                	 UserUI frame1 = new UserUI();
                	 frame1.setVisible(true);
                	 frame1.dispose();
                   
                	 nameL = new JLabel(name);
                	 pointL = new JLabel("0 Point");
                	 init2();
                 }  
             }else{
            	 ur = new UserRun();
            	 ur.name = name;
            	 ur.serverIp = serverIp;
            	 ur.downPath = downloadPath;
            	 ur.upPath = uploadPath;
            	 ur.connect();
            	 serverS.close();

            	 UserUI frame1 = new UserUI();
            	 frame1.setVisible(true);
            	 frame1.dispose();
            	 
            	 nameL = new JLabel(name);
            	 pointL = new JLabel("0 Point");
            	 init2();
             }
          }catch(SocketException se) {
          }catch(IOException ioe) {   
          }catch(ClassNotFoundException e1) {
          }
         }
         if(obj == refreshB) {
            ur.refreshUserInfo();
            fListData.clear();
            for(String getUsers : ur.showFileList) {
            	fListData.addElement(getUsers);
            }
            fList.setListData(fListData);
            
            uListData.clear();
            for(String getUsersIp : ur.usersIpName.keySet()) {
            	if(!(ur.usersIpName.get(getUsersIp)).equals(name)){
            		uListData.addElement(ur.usersIpName.get(getUsersIp));
            	}
            }
            uList.setListData(uListData);
            
            String totalScore = String.valueOf(ur.score);
            
            pointL.setText(totalScore + " P");
             //filelistT의 vector data를 get(); 하는 메소드를 새로 만들어서 여기서 호출해야 할 것 같음!
          	}

         
       if(letterTf==obj) {
          if(letterTf.getText().length() == 0) {
              JOptionPane.showMessageDialog(null, "아무것도 입력하지 않았어요~");
          }else {
             String getT = letterTf.getText() + "\n";
             letterTf.setText("");
             letterTa.append(getT);
             ur.sendMsgUser(valName, getT);
          }
       }
       if(obj == yesB) {
         //         JOptionPane.showMessageDialog(askF,"다운!");
          askF.dispose();
      //    new Loading();
          
       }
       else if(obj ==noB){
          //JOptionPane.showMessageDialog(askF,"취소!");
          askF.dispose();
          
       }
             /*if(obj==startB) {
                 for (int i = 0; i <= MAXIMUM; i++) {
                         final int currentNumber = i;
                         try {
                             SwingUtilities.invokeLater(new Runnable() {
                                 public void run() {
                                     progressBar.setValue(currentNumber);
                                 }
                             });
                             java.lang.Thread.sleep(10);
                         } catch (InterruptedException event) {
                             JOptionPane.showMessageDialog(jFrame, event.getMessage());
                         }
                     }
                 jFrame.dispose();
             }*/
             
             
    } 
    
 }