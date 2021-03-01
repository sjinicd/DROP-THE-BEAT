import java.awt.*;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.text.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.border.*;
import javax.sound.sampled.*; 

public class Start extends JFrame implements ActionListener {
   Container cp;
   JPanel p1;
   JButton jba,rd1,rd2,rd3; // 입장 버튼
   JLabel lid,lip,bg,bg2,login; // id입력하는 창
   JTextField tfid,tfip; // id입력하는 창
   ImageIcon jbai, bgi,bgi2; // 배경, 입장 이미지
   ImageIcon chac1,chac2,chac3;
   String c1,c11,c2,c22,c3,c33;
   Clip clip;
   public static String ip, Nickname;
   public static int charac=5;

   void init(){
	  playMusic();
      cp = getContentPane();
	  cp.setLayout(null);
      p1 = new JPanel();
	  p1.setLayout(null);
	  p1.setBounds(0,-30,1200,700);
	  bg = new JLabel();
	  bg2 = new JLabel();
	  login = new JLabel(new ImageIcon("imgs//loginp.png"));
	  Font font = new Font("나눔고딕 ExtraBold", Font.PLAIN, 27);
	  jba = new JButton();
	  JLabel lab1 = new JLabel("클래스 선택");
	  
	  c1="imgs/cc4.png";
	  c11="imgs/cc4-2.png";
	  c2="imgs/cc5.png";
	  c22="imgs/cc5-2.png";
	  c3="imgs/cc6.png";
	  c33="imgs/cc6-2.png";
	  chac1 = new ImageIcon(c1);
	  chac2 = new ImageIcon(c2);
	  chac3 = new ImageIcon(c3);

	  rd1 = new JButton(chac1);
	  rd2 = new JButton(chac2);
	  rd3 = new JButton(chac3);

      rd1.setFocusPainted(false);
	  rd1.setBorderPainted(false);
	  rd1.setContentAreaFilled(false);
	  rd1.setBounds(400,220,150,150);
	  rd1.addActionListener(this);
	  
	  rd2.setBounds(510,220,150,150);
	  rd2.setContentAreaFilled(false);
	  rd2.setFocusPainted(false);
	  rd2.setBorderPainted(false);
	  rd2.addActionListener(this);

	  rd3.setBounds(620,220,150,150);
	  rd3.setContentAreaFilled(false);
	  rd3.setFocusPainted(false);
	  rd3.setBorderPainted(false);
	  rd3.addActionListener(this);

	  jba.setBorderPainted(false);
	  jba.setContentAreaFilled(false);
	  jba.setFocusPainted(false);
      jba.setBounds(460,540,250,89);	  
	  jba.addActionListener(this);

	  login.setBounds(415,380,350,133);
	  login.setOpaque(false);
      
      tfid = new JTextField();
      tfid.setText("");
	  tfid.setBounds(543,402,200,60);
      tfid.addActionListener(this);
	  tfid.setOpaque(false);
	  tfid.setBorder(BorderFactory.createEmptyBorder());
	  tfid.setFont(new Font("나눔고딕 ExtraBold", Font.PLAIN, 20));
	  tfid.setForeground(Color.WHITE);
	  tfid.addActionListener(this);
	  
      tfip = new JTextField();
      tfip.setText("127.0.0.1");
      tfip.setBounds(543,449,200,60);
	  tfip.setColumns(10);
	  tfip.setOpaque(false);
	  tfip.addActionListener(this);
	  tfip.setBorder(BorderFactory.createEmptyBorder());
	  tfip.setFont(new Font("나눔고딕 ExtraBold", Font.PLAIN, 20));
	  tfip.setForeground(Color.WHITE);
	  tfip.addActionListener(this);

	  p1.add(rd1);
	  p1.add(rd2);
	  p1.add(rd3);
	  p1.add(tfid);
	  p1.add(tfip);
	  p1.add(jba);
	  p1.add(login);
	  p1.add(bg2);
	  
	  cp.add(p1);
	  cp.add(lab1);
	  
	  setUI();
	  setImg();
   }

   void setUI(){
	  setTitle("Drop the Beat");
	  setSize(1200,700);
      setVisible(true);
      setResizable(false);
	  setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  bg2.setSize(1200,700); 
	}

	void setImg(){
		bgi = new ImageIcon(getClass().getResource("imgs/login.png"));
		bgi2 = new ImageIcon(getClass().getResource("imgs/move.gif"));
		jbai = new ImageIcon(getClass().getResource("imgs/enter3.png"));
		jba.setIcon(jbai);
		bg2.setIcon(bgi2);
	}

	void playMusic(){
		try{
		   File file = new File("sound/bgmusic.wav");
		   AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
		   clip = AudioSystem.getClip();
		   clip.open(audioInputStream);
		   clip.loop(Clip.LOOP_CONTINUOUSLY);
		   clip.start();
	    }catch(Exception e){ }
	}

   public void actionPerformed(ActionEvent e){
	  ImageIcon iperrorimg = new ImageIcon("Imgs//sc3.png");
      Object obj = e.getSource();
	  int notChoose = 5;
      tfid.requestFocus();
      if(obj == jba){
         if(tfid.getText().equals("") || tfip.getText().equals(""))  JOptionPane.showMessageDialog(null, "아이디를 입력해주세요", "로그인 에러", JOptionPane.QUESTION_MESSAGE); 
         else if(tfid.getText().trim().length()>6){
            JOptionPane.showMessageDialog(null, "ID는 최대 6자까지 입력해주세요.", "로그인 에러", JOptionPane.   QUESTION_MESSAGE); //퀘스턴 메세지 옆에 이미지 파일 넣기
            tfid.setText("");
            tfid.requestFocus();
		 }else if(charac == notChoose) JOptionPane.showMessageDialog(null, "캐릭터를 선택해주세요.", "캐릭터 선택", JOptionPane.   QUESTION_MESSAGE);
         else if(charac != notChoose){
            Nickname = tfid.getText().trim();
            String temp = tfip.getText();
            if(temp.matches("(^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$)")) ip = temp;
            JOptionPane.showMessageDialog(null, " 로그인 성공! ", "Drop the Beat LOGIN", JOptionPane.INFORMATION_MESSAGE);
            jba.setEnabled(false);
            tfid.setEnabled(false);
            tfip.setEnabled(false);
            setVisible(false);
			clip.loop(0);
			clip.close();
			BTClient b = new BTClient();
		 }else JOptionPane.showMessageDialog(null, "IP 주소를 정확하게 입력해주세요! ", "ERROR!", JOptionPane.WARNING_MESSAGE, iperrorimg);//ip주소 에러났을때 에러화면
	  }else if(obj == rd1){
		charac = 0;
        rd1.setEnabled(false);
        rd2.setEnabled(true);
        rd3.setEnabled(true);
     }else if(obj == rd2){
        charac = 1;
        rd2.setEnabled(false);
        rd1.setEnabled(true);
        rd3.setEnabled(true);
     }else if(obj == rd3){
        charac = 2;
        rd3.setEnabled(false);
        rd1.setEnabled(true);
        rd2.setEnabled(true);
     }
   }
   public static void main(String [] args){
	  Start s = new Start();
      s.init();
   }
}