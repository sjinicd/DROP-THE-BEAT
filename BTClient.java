import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.text.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.swing.border.*;
import javax.sound.sampled.*;
import java.net.*;

public class BTClient extends JFrame implements ActionListener {
   Container cp;   
   JLabel label_id,chat,myscore,charbar,answer,JLclient1,JLclient2,JLclient3,JLclient4,Jm;
   Label Lclient1, Lclient2, Lclient3, Lclient4;
   JButton B_ready,B_exit,quiz,send;
   JTextArea T_chatPanel, T_mainPanel, song_title;
   JTextField T_chatInput;
   JScrollPane scrP_chat;
   JPanel pn;
   int port = 2222;
   Socket soc;
   String smusic, name, score, idx,charac,characName,sendmsg,line,genre,songNumber,songTTitle;
   boolean gamestart, author;
   int gameNumber;
   Clip clip,clip1;
   ImageIcon[] imgs = new ImageIcon[3];
   ImageIcon[] imgs2 = new ImageIcon[3];
   ImageIcon[] imgs3 = new ImageIcon[15];
   Vector<String> songtitle = new Vector<String>();
   Vector<String> songtxt = new Vector<String>();
   BufferedReader br2;

   public BTClient(){
      init();
      enter();
   }
    void init(){
      cp = getContentPane();
       cp.setLayout(null);
       pn = new JPanel();
       pn.setLayout(null);
       pn.setBounds(0,0,1200,700);
       label_id = new JLabel();
       label_id.setIcon(new ImageIcon("imgs//bg.png"));//배경
      
       B_ready = new JButton(new ImageIcon("imgs//ready-2.png"));//버튼
       B_ready.setBorderPainted(false);
       B_ready.setOpaque(false);
       B_ready.setFocusPainted(false);
      B_ready.setContentAreaFilled(false);
      
       B_exit = new JButton(new ImageIcon("imgs//exit-2.png"));//버튼
       B_exit.setBorderPainted(false);
       B_exit.setOpaque(false);
       B_exit.setFocusPainted(false);
      B_exit.setContentAreaFilled(false);
       B_exit.addActionListener(this);

      send = new JButton(new ImageIcon("imgs//addmusic.png"));//버튼
       send.setOpaque(false);
      send.setBorderPainted(false);
      send.setFocusPainted(false);
       send.setContentAreaFilled(false);
      send.addActionListener(this);
      
       charbar = new JLabel(new ImageIcon("imgs//charBar.png"));//버튼
       charbar.setOpaque(false);
     
       myscore = new JLabel(new ImageIcon("imgs//score.png"));//점수판 이미지
       myscore.setOpaque(false);

	   Jm = new JLabel(new ImageIcon("imgs//cc5-2.png"));
	   Jm.setOpaque(false);
      
       quiz = new JButton(new ImageIcon("imgs//quiz.png"));//버튼
       quiz.setBorderPainted(false);
       quiz.setOpaque(false);
      quiz.setContentAreaFilled(false);
       quiz.setFocusPainted(false);
       quiz.addActionListener(this);
      
       chat = new JLabel(new ImageIcon("imgs//chat.png"));//채팅 판.
       chat.setOpaque(false);
      
       answer = new JLabel(new ImageIcon("imgs//answer.png"));//버튼
       answer.setOpaque(false);

      T_mainPanel = new JTextArea();
      T_mainPanel.setOpaque(false);
      T_mainPanel.setFont(new Font("나눔고딕 ExtraBold", Font.PLAIN, 25));
      T_mainPanel.setEditable(false);
      T_mainPanel.setForeground(Color.WHITE);

      song_title = new JTextArea();
      song_title.setOpaque(false);
      song_title.setFont(new Font("나눔고딕 ExtraBold", Font.PLAIN, 15));
      song_title.setEditable(false);
      song_title.setForeground(Color.WHITE);

      T_chatInput = new JTextField();
      T_chatInput.setOpaque(false);
      T_chatInput.setFont(new Font("나눔바른고딕", Font.BOLD, 25));
      T_chatInput.setText("");
      T_chatInput.setBorder(null);
      T_chatInput.setForeground(Color.WHITE);

      T_chatPanel = new JTextArea();
      T_chatPanel.setFont(new Font("나눔고딕 ExtraBold", Font.PLAIN, 15));
      T_chatPanel.setBorder(null);
      T_chatPanel.setEditable(false);
      T_chatPanel.setForeground(Color.WHITE);

      scrP_chat = new JScrollPane(T_chatPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      scrP_chat.setBounds(70,400,155,130);
      scrP_chat.setBorder(BorderFactory.createEmptyBorder());

       scrP_chat.getViewport().setOpaque(false);
       scrP_chat.setOpaque(false);
       T_chatPanel.setOpaque(false);


      JPanel Pclientlist = new JPanel();
      JLabel Lclientlist = new JLabel(new ImageIcon("imags//imgs.png"));
      Pclientlist.setOpaque(false);
      Lclientlist.setOpaque(false);
      Pclientlist.setBorder(new LineBorder(new Color(127, 219, 254), 4, true));
       Pclientlist.setBounds(50, 20, 220, 260);
       Lclientlist.setBounds(50, 20, 220, 260);
      
      JLclient1 = new JLabel(new ImageIcon("imgs//cc4-mini.png"));
       JLclient1.setBounds(15, 20, 90, 70); 
      Pclientlist.add(JLclient1);
      
      Lclient1 = new Label("[ 닉네임 & 점수 ]");
      Lclient1.setFont(new Font("나눔바른고딕", Font.BOLD, 11));
      Lclient1.setAlignment(Label.CENTER);
       Lclient1.setBounds(10, 90, 100, 30);
      Pclientlist.add(Lclient1);

       JLclient2 = new JLabel(new ImageIcon("imgs//cc5-mini.png"));
       JLclient2.setBounds(115, 20, 90, 70);
       Pclientlist.add(JLclient2);
      
      Lclient2 = new Label("[ 닉네임 & 점수 ]");
      Lclient2.setFont(new Font("나눔바른고딕", Font.BOLD, 11));
      Lclient2.setAlignment(Label.CENTER);
       Lclient2.setBounds(115, 90, 100, 30);
      Pclientlist.add(Lclient2);
      
      JLclient3 = new JLabel(new ImageIcon("imgs//cc6-mini.png"));
       JLclient3.setBounds(15, 140, 90, 70);
      Pclientlist.add(JLclient3);
      
      Lclient3 = new Label("[ 닉네임 & 점수 ]");
      Lclient3.setFont(new Font("나눔바른고딕", Font.BOLD, 11));
      Lclient3.setAlignment(Label.CENTER);
       Lclient3.setBounds(10, 210, 100, 30);
      Pclientlist.add(Lclient3);
      
      JLclient4 = new JLabel(new ImageIcon("imgs//cc7-mini.png"));
       JLclient4.setBounds(115, 140, 90, 70); 
      Pclientlist.add(JLclient4);
      
      Lclient4 = new Label("[ 닉네임 & 점수 ]");
      Lclient4.setFont(new Font("나눔바른고딕", Font.BOLD, 11));
      Lclient4.setAlignment(Label.CENTER);
       Lclient4.setBounds(115, 210, 100, 30);
      Pclientlist.add(Lclient4);
      Pclientlist.setLayout(null);

       label_id.setBounds(0, 0, 1200, 700);
       B_ready.setBounds(760, 0, 200, 71);
       B_exit.setBounds(960, 0, 200, 71);
       charbar.setBounds(40, 20, 4, 260);
	   Jm.setBounds(920,100,230,130);
       myscore.setBounds(920, 385, 230, 245);
       quiz.setBounds(295, 100, 600, 400);
       chat.setBounds(40, 295, 230, 330);
       answer.setBounds(335, 500, 520, 130);
      send.setBounds(935,290,200,71);
      T_mainPanel.setBounds(330,200,520,270);
      song_title.setBounds(920, 260, 230, 50);
      T_chatPanel.setBounds(50,315,200,300);
      T_chatInput.setBounds(370,535,450,80);
      scrP_chat.setBounds(50,315,200,300);

      pn.add(scrP_chat);
      pn.add(T_chatInput);
      pn.add(T_mainPanel);
      pn.add(song_title);
      pn.add(answer);
      pn.add(Pclientlist);
      pn.add(Lclientlist);
      pn.add(B_ready);
       pn.add(B_exit);
      pn.add(send);
       pn.add(charbar);
       pn.add(myscore);
       pn.add(quiz);
       pn.add(chat);
	   pn.add(Jm);
   
      pn.add(label_id);
       cp.add(pn);

      setUI();
    }

    void setUI(){
       setTitle("Drop the Beat");
       setSize(1200,700);
       setVisible(true);
       setResizable(false);
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      imgs[0] = new ImageIcon("imgs//cc4-mini.png");
       imgs[1] = new ImageIcon("imgs//cc5-mini.png");
       imgs[2] = new ImageIcon("imgs//cc6-mini.png");

      imgs2[0] = new ImageIcon("imgs//cc4.png");
      imgs2[1] = new ImageIcon("imgs//cc5.png");
      imgs2[2] = new ImageIcon("imgs//cc6.png");

	  imgs3[0] = new ImageIcon("imgs//예스.jpg");
	  imgs3[1] = new ImageIcon("imgs//방탄.jpg");
	  imgs3[2] = new ImageIcon("imgs//트와이스.jpg");
	  imgs3[3] = new ImageIcon("imgs//가호.jpg");
	  imgs3[4] = new ImageIcon("imgs//red.jpg");
	  imgs3[5] = new ImageIcon("imgs//창모.jpg");
	  imgs3[6] = new ImageIcon("imgs//애쉬.jpg");
	  imgs3[7] = new ImageIcon("imgs//로꼬.jpg");
	  imgs3[8] = new ImageIcon("imgs//zico.jpg");
	  imgs3[9] = new ImageIcon("imgs//머쉬.jpg");
	  imgs3[10] = new ImageIcon("imgs//조항조.jpg");
	  imgs3[11] = new ImageIcon("imgs//김호중.jpg");
	  imgs3[12] = new ImageIcon("imgs//막걸리.jpg");
	  imgs3[13] = new ImageIcon("imgs//나훈아.jpg");
	  imgs3[14] = new ImageIcon("imgs//young.jpg");

    }

    void enter(){
       String nick = Start.Nickname; //여기서 로그인 클래스의 데이터 받아옴. 
      String ip = Start.ip;
      int charac = Start.charac;

       try{
          Socket soc = new Socket(ip,port);
          Speaking speak = new Speaking(soc,nick,charac);
          Listening listen = new Listening(soc); // 음악 틀 준비
         speak.start();
         listen.start();
         B_ready.addActionListener(new Speaking(soc,nick,charac));
         send.addActionListener(new Speaking(soc,nick,charac));
         T_chatInput.addKeyListener(new Speaking(soc,nick,charac));
         //System.out.println("서버연결성공!!!!!!");
        }catch(IOException io){
           JOptionPane.showMessageDialog(null, "서버 접속 실패!\n서버가 닫혀 있는 것 같습니다.", "ERROR", JOptionPane.WARNING_MESSAGE);
          System.exit(0);
       }
   }

   public void actionPerformed(ActionEvent e){

       Object obj = e.getSource();
	   try{
			 File file = new File("sound/ButtonClick.wav");
			 AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
			 clip1 = AudioSystem.getClip();
			 clip1.open(audioInputStream);
		 }catch(Exception e1){ }
       if(obj == B_exit){
		   clip1.start();
           int answer= JOptionPane.showConfirmDialog(null, "정말 종료 하시겠습니까?", "종료창",
                       JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
           if(answer == JOptionPane.YES_OPTION) System.exit(0);
			clip1.close();
	  }

   }

   class Speaking extends Thread implements KeyListener, ActionListener {
      DataOutputStream dos;
      String nick;
      Socket soc;
      int charac;

      Speaking(Socket soc, String nick, int charac){
         this.soc = soc;
         try{
            dos = new DataOutputStream(this.soc.getOutputStream());
            this.nick = nick;
            this.charac = charac;
         }catch(IOException ie){ }
      }
     
      public void run(){
         try{
            dos.writeUTF(nick+"/"+charac);
         }catch(IOException ii){ }
      }
      public void keyReleased(KeyEvent e){
         //P.pln("쳐짐");
         if(e.getKeyCode() == KeyEvent.VK_ENTER){
            String msg = T_chatInput.getText();
            T_chatInput.setText("");
            try{
               dos.writeUTF("@" + Setting.CHAT + nick + " : " + msg);
               dos.flush();
            }catch(IOException io){ }
         }
      }
      public void actionPerformed(ActionEvent e){
		  try{
            File file = new File("sound/ButtonClick.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            clip1 = AudioSystem.getClip();
            clip1.open(audioInputStream);
          }catch(Exception e1){ }
         Object obj = e.getSource();
         if(e.getSource() == B_ready){
			 clip1.start();
            try{
               dos.writeUTF("@" + Setting.CHAT + " * "+ nick + " 님 준비 완료 !!");
               dos.flush();
               dos.writeUTF("@"+Setting.READY);
               dos.flush();
               B_ready.setEnabled(false);
            }catch(IOException io){ }
			 clip1.close();
         }else if(obj == send){
			 clip1.start();
            try{	
               sendmsg= JOptionPane.showInputDialog(null, "어떤 신청곡을 원하시나요?", " ");
               JOptionPane.showMessageDialog(null, "<"+sendmsg+"> 신청곡을 서버에 전송했습니다!\n빠른 시일내에 서비스 하겠습니다.");
               dos.writeUTF("@" + Setting.SEND + sendmsg);
               dos.flush();
            }catch(IOException ie){ }
			clip1.close();
         }
      }
      public void keyTyped(KeyEvent e){}
      public void keyPressed(KeyEvent e){}
   }

   class Listening extends Thread {
      DataInputStream dis;
      DataOutputStream dos;
      Socket soc; 
      Listening(Socket soc){
         this.soc = soc;
         try{
            dis = new DataInputStream(this.soc.getInputStream());
            dos = new DataOutputStream(this.soc.getOutputStream());
         }catch(IOException io){ }
      }
      void txtSet(){
         String song0 ="_ ___ __ _____\n_ __ ___ 골라 YES or YES?";
         String song1 = "Shoes on, get up in the morn' Cup of milk, \nlet's rock and roll King Kong, kick the drum,\nrolling on like a Rolling Stone\nSing song when I'm walking home \nJump up to the top, LeBron \nDing dong, ____ __ __ __ _____ \nIce tea and a game of ping pong";
         String song2 ="마음속으로는 다 알고 있잖아\n____ __ __ _____ .";
         String song3 ="지금부터 시작해봐 앞을 달려\n_______ _______ __ ____\n뭐든 할 수 있어\n끝없이 달려봐 거친 세상 속";
         String song4 ="___ ___ ___ _____\n쉽게 나를 놓을 순 없네";
         String song5 ="어젠 시장님이 만나쟤 motown\n어수룩해 엄마 속에 걱정\n한 톤만큼 쌓고\n_____ ____ __\n여섯이 되었고\n발견했지 우연히 5년 전의 노트";
         String song6 ="매일이 난 너무 두려워\n누가 날 죽일 수도\nWhen open my eyes, 어떤 것도 안 보이는 걸\n__ ____ __ _ ___ __ .\n알지, 안 일어날 걸\n하지만 아무도 몰라";
         String song7 ="Baby, 난 너만 담아\n___ ___ __ ___ _ .";
         String song8 ="__ __ ____\n_ __ __ ____\nTell me what I got to do\n급한 대로 블루투스 켜\n아무 노래나 일단 틀어";
         String song9 ="나는 여기까지 혼자 왔다\n__ __ __ __ __ __\n나는 원해 엄마 고기반찬\n누나는 방문을 열어줘요 당장";
         String song10 ="그래 한때 삶에 무게 견디지 못해\n__ __ __ __ ___ 묻었다";
         String song11 ="___ ___ ___ ___ __";
         String song12 ="__ __ ____ __ ___\n춤을 추던 우리 아버지";
         String song13 ="____ ____\n__ _ __ ____ .";
         String song14 ="____ ___ __ ___\n___ __ ___";

		 String songtitle0 = "트와이스 yes or yes";
         String songtitle1 = "방탄소년단 Dynamite";
         String songtitle2 = "트와이스 i'cant stop";
         String songtitle3 = "가호 running";
         String songtitle4 = "레드벨벳 미래";
         String songtitle5 = "창모 meteo";
         String songtitle6 = "애쉬 아일랜드 paranoid";
         String songtitle7 = "로꼬 감아";
         String songtitle8 = "지코 아무노래";
         String songtitle9 = "머쉬베놈 왔다";
         String songtitle10 = "김호중 태클을 걸지마";
         String songtitle11 = "김호중 고맙소";
         String songtitle12 = "영탁 막걸리 한잔";
         String songtitle13 = "나훈아 어느 60대 노부부 이야기";
         String songtitle14 = "영탁 찐이야";

         songtxt.add(song0);
         songtxt.add(song1);
         songtxt.add(song2);
         songtxt.add(song3);
         songtxt.add(song4);
         songtxt.add(song5);
         songtxt.add(song6);
         songtxt.add(song7);
         songtxt.add(song8);
         songtxt.add(song9);
         songtxt.add(song10);
         songtxt.add(song11);
         songtxt.add(song12);
         songtxt.add(song13);
         songtxt.add(song14);

          songtitle.add(songtitle0);
         songtitle.add(songtitle1);
         songtitle.add(songtitle2);
         songtitle.add(songtitle3);
         songtitle.add(songtitle4);
         songtitle.add(songtitle5);
         songtitle.add(songtitle6);
         songtitle.add(songtitle7);
         songtitle.add(songtitle8);
         songtitle.add(songtitle9);
         songtitle.add(songtitle10);
         songtitle.add(songtitle11);
         songtitle.add(songtitle12);
         songtitle.add(songtitle13);
         songtitle.add(songtitle14);
      }
      public void run(){
         while(dis != null){
            try{
               String msg = dis.readUTF();
               String code = msg.substring(0,3);
               if(code.equals("#"+Setting.RESET)){
                  name = msg.substring(3,msg.indexOf("/"));
                  score = msg.substring(msg.indexOf("/")+1, msg.indexOf(","));
                  charac = msg.substring(msg.indexOf(",")+1,msg.indexOf("_"));
                  characName = msg.substring(msg.indexOf("_")+1,msg.indexOf(" "));
                  idx = msg.substring(msg.indexOf(" ")+1);
                  renewalList(); // 클라이언트 리스트 갱신
               }else if(code.equals("#"+Setting.GAME_START)){
                  gamestart=true;
               }else if(code.equals("#"+Setting.GAME_OVER)){
                  gamestart=false;
                  author = false;
                  B_ready.setEnabled(true);
               }else if(code.equals("#"+Setting.AUTHOR)){
                  P.pln(msg.substring(3));
                  if(Start.Nickname.equals(msg.substring(3))){
                     author = true;
                     String []genres = {"힙합", "댄스", "트로트", "랜덤"};
                     int selected = JOptionPane.showOptionDialog(null, "어떤 장르를 선택하시겠습니까?", "장르 선택", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, genres, genres[0]);
                     if(selected == JOptionPane.CLOSED_OPTION) JOptionPane.showMessageDialog(null, "다시 선택해주세요!");
                     else JOptionPane.showMessageDialog(null, genres[selected]+" 장르를 선택하셨습니다!");
                     String select = genres[selected];
                     try{ // 선택된 장르를 서버에게 보내줌.
                        dos.writeUTF("@"+Setting.CHOOSE+select);
                        dos.flush();
                     }catch(IOException ii){ }
                  }
               }else if (code.equals("#"+Setting.CHAT)){
                  //P.pln("채팅 :"+msg);
                  T_chatPanel.append(msg.substring(3)+"\n");
                  scrP_chat.getVerticalScrollBar().setValue(scrP_chat.getVerticalScrollBar().getMaximum());
               }else if (code.equals("#"+Setting.COUNT)){
                  T_mainPanel.setText(msg.substring(3)+"\n");
                  //song_title.setText(msg.substring(3)+"\n");
                  scrP_chat.getVerticalScrollBar().setValue(scrP_chat.getVerticalScrollBar().getMaximum());
               }else if (code.equals("#"+Setting.MUSIC)){ //랜덤번호 도착 > 재생
                  File folder10 = new File("sound/10");
                  File SongList[] = folder10.listFiles();
                  txtSet(); //댄스0-4, 힙합5-9, 트로트10-14
                  songNumber = msg.substring(msg.indexOf("/")+1);
                  P.pln("쏭넘버:"+songNumber);
                  int songNum = Integer.parseInt(songNumber);
                  int songTTitle = Integer.parseInt(songNumber);
                  P.pln("번호:"+songNum);
				  song_title.setText(songtitle.get(songNum));
                  T_mainPanel.setText(songtxt.get(songNum)); // 문제 화면에도 잘 뜨나?
				  Jm.setIcon(imgs3[songNum]);
                  song_title.setText(songtitle.get(songTTitle));
                  
                  try{ // 해당 번호의 노래를 트는 곳
                     clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));
                     clip.open(AudioSystem.getAudioInputStream(SongList[songNum]));
                     clip.addLineListener(event -> { 
                        if (event.getType() == LineEvent.Type.STOP) clip.close();
                        });
                      clip.start();
                     while(!clip.isRunning()) Thread.sleep(10);
                     while (clip.isRunning()) Thread.sleep(10);
                     clip.close();
                     Thread.sleep(1000);
                     try{ // 노래 1회차 재생 다 됐다고 알려주기.
                        dos.writeUTF("@"+Setting.NEXT);
                        dos.flush();
                     }catch(IOException ii){ }
                  }catch(LineUnavailableException le){
                  }catch(InterruptedException ie){
                  }catch(UnsupportedAudioFileException ue){ }
               }else if (code.equals("#"+Setting.QUIZ)) {
                  T_mainPanel.setText(msg.substring(3)+"\n"); //메인패널에 뜨는 메세지
                  song_title.setText(msg.substring(3)+"\n");

               }
            }catch(IOException ii){
              T_chatPanel.append("[ 서버와의 연결이 끊어졌습니다. 닉네임 중복, 서버 정원 초과, 게임 진행중인 경우 연결이 거부됩니다. ]\n[ 3초 후 프로그램을 종료합니다 .. ]");
               try{
                  Thread.sleep(3000);
                  System.exit(0);
               }catch(InterruptedException it){ }
            }
         }
      }
      public void renewalList(){
         int indexN = Integer.parseInt(idx);
         int charaN = Integer.parseInt(charac);
         if(indexN == 0){
            JLclient1.setIcon(imgs[charaN]);
            Lclient1.setText("[" + name + " / " + "점수: " + score + "]");
            if(name.equals(Start.Nickname)){
            myscore.setIcon(imgs2[charaN]);
            myscore.setText("[" + name + " / " + "점수: " + score + "]");
            myscore.setFont(new Font("나눔바른고딕", Font.BOLD, 16));
            myscore.setForeground(Color.WHITE);
            myscore.setHorizontalAlignment(JLabel.CENTER);
            }
         deleteList();
         }else if(indexN == 1){
            JLclient2.setIcon(imgs[charaN]);
            Lclient2.setText("[ " + name + " / " + "점수: " + score + " ]");
            if(name.equals(Start.Nickname)){
            myscore.setIcon(imgs2[charaN]);
            myscore.setText("[" + name + " / " + "점수: " + score + "]");
            myscore.setFont(new Font("나눔바른고딕", Font.BOLD, 16));
            myscore.setForeground(Color.WHITE);
            myscore.setHorizontalAlignment(JLabel.CENTER);
            }         
            deleteList();
       }else if(indexN == 2){
            JLclient3.setIcon(imgs[charaN]);
         Lclient3.setText("[ " + name + " / " + "점수: " + score + " ]");
            if(name.equals(Start.Nickname)){
            myscore.setIcon(imgs2[charaN]);
            myscore.setText("[" + name + " / " + "점수: " + score + "]");
            myscore.setFont(new Font("나눔바른고딕", Font.BOLD, 16));
            myscore.setForeground(Color.WHITE);
            myscore.setHorizontalAlignment(JLabel.CENTER);
            }         
            deleteList();
       }else if(indexN == 3){
         JLclient4.setIcon(imgs[charaN]);
         Lclient4.setText("[ " + name + " / " + "점수: " + score + " ]");
            if(name.equals(Start.Nickname)){
            myscore.setIcon(imgs2[charaN]);
            myscore.setText("[" + name + " / " + "점수: " + score + "]");
            myscore.setFont(new Font("나눔바른고딕", Font.BOLD, 16));
            myscore.setForeground(Color.WHITE);
            myscore.setHorizontalAlignment(JLabel.CENTER);
            }
            deleteList();
       }
      }
      /* 캐릭터 반복문 이걸로 퉁치기.
      void listset(){
      }*/
      public void deleteList(){
         int indexN = Integer.parseInt(idx);
         int charaN = Integer.parseInt(charac);
         switch(indexN){ 
            case 0: Lclient2.setText("[ 접속대기 / 점수 ]"); Lclient3.setText("[ 접속대기 / 점수 ]"); Lclient4.setText("[ 접속대기 / 점수 ]");
            case 1: Lclient3.setText("[ 접속대기 / 점수 ]"); Lclient4.setText("[ 접속대기 / 점수 ]");
            case 2: Lclient4.setText("[ 접속대기 / 점수 ]");
         }
      }
    }
   public static void main(String[] args){
      BTClient btc = new BTClient();
   }
}