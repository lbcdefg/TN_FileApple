package tn.fa.server;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;

class ServerUI implements ActionListener{
	JFrame servFrame1;
	JTextField servInTf;
	JPanel servInP;
	JLabel servInL, servIconL, servIconL1;
	JButton servInB , servBrowseB;
	String folderPath = "";
	JFileChooser chooser;
	FileNameExtensionFilter filter;

	JFrame servFrame2;
	JTextField textField;
	JPanel servMainP;
	JLabel servLogL,servNotL,servPntL,servMaIconL1, serMaIconL2, servMaBgL;
	JTextArea servLogTa,servNotTa,servPntTa;
	JScrollPane servNotSp1,servLogSp2,servPntSp3;
	   
	static ServerMain sgu;

	static ServerUI window2;
	/**
	* Launch the application.
	*/
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
					window2 = new ServerUI();
					window2.servFrame1.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
   

	/**
	* Create the application.
	*/
	ServerUI() {
		initialize();
		initialize2();
	}

	/**
	* Initialize the contents of the frame.
	*/
	String browse() {
		chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		chooser.setCurrentDirectory(new File("/"));
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setDialogTitle("타이틀");
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
	void initialize() {
		servFrame1 = new JFrame();
		servFrame1.setTitle("FileApple Server ver 1.0");
		Toolkit tk1 = Toolkit.getDefaultToolkit();
		Image i1 = tk1.getImage(".\\imgs\\icon.png");
		servFrame1.setIconImage(i1);
		servFrame1.setBounds(750, 350, 450, 300);
		servFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		servFrame1.getContentPane().setLayout(null);
		servFrame1.setResizable(false);
      
		servInP = new JPanel();
		servInP.setBackground(new Color(255, 220, 100));
		servInP.setForeground(new Color(255, 220, 100));
		servInP.setBounds(0, 0, 444, 271);
		servFrame1.getContentPane().add(servInP);
		servInP.setLayout(null);
      
		servInL = new JLabel("로그폴더");
		servInL.setFont(new Font("나눔스퀘어 네오 OTF Bold", Font.PLAIN, 14));
		servInL.setBounds(88, 153, 105, 34);
		servInP.add(servInL);
      
		servIconL = new JLabel("");
		servIconL.setIcon(new ImageIcon(".\\imgs\\icon.png"));
		servIconL.setBounds(110, 23, 178, 142);
		servInP.add(servIconL);
      
		servIconL1 = new JLabel("");
		servIconL1.setIcon(new ImageIcon(".\\imgs\\FilealppleSmall.png"));
		servIconL1.setBounds(165, 76, 286, 57);
		servInP.add(servIconL1);
      
		servInTf = new JTextField();
		servInTf.setBounds(150, 160, 146, 21);
		servInP.add(servInTf);
		servInTf.setColumns(10);
      
		servInB = new JButton("접속");
		servInB.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 12));
		servInB.setBackground(new Color(255, 255, 255));
		servInB.setBounds(189, 197, 60, 21);
		servInB.addActionListener(this);
		servInP.add(servInB);
      
		servBrowseB = new JButton("browse");
		servBrowseB.setBackground(new Color(255, 255, 255));
		servBrowseB.setBounds(308, 160, 23, 20);
		servBrowseB.addActionListener(this);
		servInP.add(servBrowseB);
	}
	void initialize2() {
		servFrame2 = new JFrame();
		servFrame2.setTitle("FileApple Server ver 1.0");
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image i = tk.getImage(".\\imgs\\icon.png");
		servFrame2.setIconImage(i);
		servFrame2.setBounds(100, 100, 773, 843);
		servFrame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		servFrame2.getContentPane().setLayout(null);
		servFrame2.setResizable(false);

		servMainP = new JPanel();
		servMainP.setBackground(new Color(255, 220, 100));
		servMainP.setBounds(0, 0, 767, 814);
		servFrame2.getContentPane().add(servMainP);
		servMainP.setLayout(null);
      
		servLogL = new JLabel("접속로그");
		servLogL.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 21));
		servLogL.setBounds(34, 98, 174, 51);
		servMainP.add(servLogL);
      
		servMaIconL1 = new JLabel("");
		servMaIconL1.setIcon(new ImageIcon(".\\imgs\\Fileappl1.png"));
		servMaIconL1.setBounds(113, 52, 286, 57);
		servMainP.add(servMaIconL1);
      
		servLogSp2 = new JScrollPane();
		servLogSp2.setBounds(34, 140, 329, 419);
		servMainP.add(servLogSp2);
      
		servLogTa = new JTextArea();
		servLogTa.setWrapStyleWord(true);
		servLogTa.setLineWrap(true);
		servLogSp2.setViewportView(servLogTa);
		servLogTa.setEditable(false);
      
		servNotSp1 = new JScrollPane();
		servNotSp1.setBounds(34, 606, 329, 134);
		servMainP.add(servNotSp1);
      
		servNotTa = new JTextArea();
		servNotTa.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 13));
		servNotTa.setLineWrap(true);
		servNotSp1.setViewportView(servNotTa);
		servNotTa.setEditable(false);
      
		textField = new JTextField();
		textField.setToolTipText("");
		textField.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 12));
		textField.setBounds(34, 750, 329, 30);
		textField.addActionListener(this);
		servMainP.add(textField);
		textField.setColumns(10);
      
		servNotL = new JLabel("서버공지");
		servNotL.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 21));
		servNotL.setBounds(34, 560, 174, 51);
		servMainP.add(servNotL);
      
		serMaIconL2 = new JLabel("");
		serMaIconL2.setIcon(new ImageIcon(".\\imgs\\icon.png"));
		serMaIconL2.setBounds(55, 3, 136, 127);
		servMainP.add(serMaIconL2);
      
		servPntSp3 = new JScrollPane();
		servPntSp3.setBounds(400, 140, 320, 640);
		servMainP.add(servPntSp3);
      
		servPntTa = new JTextArea();
		servPntTa.setEditable(false);
		servPntSp3.setViewportView(servPntTa);
      
		JLabel lblNewLabel = new JLabel("       IP              Name                   Point");
		lblNewLabel.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 15));
		servPntSp3.setColumnHeaderView(lblNewLabel);
      
		servPntL = new JLabel("사용자 포인트");
		servPntL.setFont(new Font("나눔스퀘어 네오 Bold", Font.PLAIN, 21));
		servPntL.setBounds(400, 98, 174, 51);
		servMainP.add(servPntL);
      
		servMaBgL = new JLabel("");
		servMaBgL.setHorizontalAlignment(SwingConstants.CENTER);
		servInP.setBackground(new Color(255, 220, 100));
		servInP.setForeground(new Color(255, 220, 100));
      //servMaBgL.setIcon(new ImageIcon("C:\\박종서\\java\\Advanced\\eclipse\\workspace\\ServerMain\\ServerMain\\src\\tn\\fa\\Background.png"));
		servMaBgL.setBounds(0, 0, 757, 804);
		servMainP.add(servMaBgL);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == servBrowseB) {
			browse();
			//JOptionPane.showMessageDialog(null, folderPath);
			servInTf.setText(folderPath);
		}
		if(obj == servInB) {
			if (servInTf.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "아무것도 입력하지 않았어요~");
			}else {
				String getT = servInTf.getText();
				servInTf.setText("");
				JOptionPane.showMessageDialog(null, "환영합니다!","WELCOME TO FILEAPPLE",JOptionPane.PLAIN_MESSAGE);

				sgu = new ServerMain();
				sgu.sUi1 = window2;
				sgu.logFilePath = getT;
				sgu.start();
	         
				ServerUI window = new ServerUI();
	         
				sgu.sUi2 = window;
				//uc.logFilePath = servInTf.getText();	         
				window.servFrame2.setVisible(true);
				servFrame1.dispose();
			}
		}
		//JTextField jtfield = (JTextField)e.getSource();
		if((Object)textField==obj) {
			if(textField.getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "아무것도 입력하지 않았어요~");
			}else{
				String getT = textField.getText();
				textField.setText("");
				servNotTa.append(getT + "\n");
				sgu.sendMsgAll(getT);
			}
		}
	}
}