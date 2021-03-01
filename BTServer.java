import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class BTServer extends JFrame implements ActionListener {
   Container cp;
   JPanel p_main, p_log, p_button;
   JScrollPane scrP;
   JTextArea SerLog;
   JLabel SerStatus;
   JButton SerStart, SerClose;
   ServerSocket Sers;
   Socket soc;
   int ready, charac, score, playNumber, gameNumber, randomN, clientPlay;
   int port = 2222;
   String ans,genre;
   String[] lines = new String[14];
   String[] linetitle = new String[14];
   BufferedReader br;
   FileReader answerfr;
   boolean MusicStart;
   public static final int MAX_CLIENT = 4;

   LinkedHashMap<String,DataOutputStream> clist = new LinkedHashMap<String,DataOutputStream>();
   LinkedHashMap<String,Integer> cScore = new LinkedHashMap<String,Integer>();
   LinkedHashMap<String,Integer> cCharac = new LinkedHashMap<String,Integer>();

   public void init(){
      cp = getContentPane();
      setTitle(" < BEAT Server > ");
      setResizable(false);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100,100,300,500);
      setLocationRelativeTo(null);
      cp.setLayout(new GridLayout(0, 1, 10, 0));

      p_main = new JPanel();
      cp.add(p_main);
      p_main.setLayout(new BoxLayout(p_main, BoxLayout.Y_AXIS));

      SerStatus = new JLabel(" 서버 대기중 ! ");
      SerStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
      SerStatus.setPreferredSize(new Dimension(70,70));
      SerStatus.setFont(new Font("나눔고딕 ExtraBold", Font.PLAIN, 15));
      p_main.add(SerStatus);
      SerStatus.setHorizontalTextPosition(SwingConstants.CENTER);

      scrP = new JScrollPane();
      scrP.setBorder(new LineBorder(Color.DARK_GRAY));
      scrP.setPreferredSize(new Dimension(70,150));
      p_main.add(scrP);

      SerLog = new JTextArea();
      SerLog.setBorder(new LineBorder(Color.DARK_GRAY));
      scrP.setViewportView(SerLog);

      p_button = new JPanel();
      p_button.setPreferredSize(new Dimension(80, 80));
      p_button.setAutoscrolls(true);
      p_main.add(p_button);
      p_button.setLayout(new FlowLayout(FlowLayout.CENTER,25,25));

      SerStart = new JButton("서버 운영 시작");
      SerStart.setHorizontalTextPosition(SwingConstants.CENTER);
      SerStart.setPreferredSize(new Dimension(120, 40));
      SerStart.setFocusPainted(false);
      SerStart.setAlignmentX(Component.CENTER_ALIGNMENT);
      SerStart.setForeground(Color.WHITE);
      SerStart.setBackground(Color.DARK_GRAY);
      SerStart.setBorder(null);
      p_button.add(SerStart);
      SerStart.addActionListener(this);

      SerClose = new JButton("서버 운영 종료");
      SerClose.setHorizontalTextPosition(SwingConstants.CENTER);
      SerClose.setPreferredSize(new Dimension(120, 40));
      SerClose.setFocusPainted(false);
      SerClose.setAlignmentX(Component.CENTER_ALIGNMENT);
      SerClose.setForeground(Color.WHITE);
      SerClose.setBackground(Color.DARK_GRAY);
      SerClose.setBorder(null);
      p_button.add(SerClose);
      SerClose.addActionListener(this);
      SerClose.setEnabled(false);
   }
   public void actionPerformed(ActionEvent e){
      if(e.getSource() == SerStart){
         //P.pln("서버 운영 시작");
         new Thread(){
            public void run() {
               try{
                  Collections.synchronizedMap(clist);
                  Collections.synchronizedMap(cScore);
                  Collections.synchronizedMap(cCharac);
                  Sers = new ServerSocket(port);
                  SerStatus.setText(" [ Server Start ] ");
                  SerLog.append(" 서버 연결 시작. " + "\n");
                  SerStart.setEnabled(false);
                  SerClose.setEnabled(true);
                  while(true){
                     soc = Sers.accept();
                     if((clist.size()+1)>MAX_CLIENT || MusicStart == true) soc.close();
                     else{
                        Thread gm = new Gmanager(soc);
                        gm.start();
                     }
                  }
               }catch(IOException ie){ }
            }
         }.start();
      }else if(e.getSource() == SerClose){
         int SerSelect = JOptionPane.showConfirmDialog(null, "서버를 종료하시겠습니까?","Beat Server", JOptionPane.OK_CANCEL_OPTION);
         try{
            if(SerSelect == JOptionPane.YES_OPTION){
               Sers.close();
               SerLog.setText(" [ Server Closed ] ");
               SerLog.append(" 서버 종료. "+"\n");
               SerStart.setEnabled(true);
               SerClose.setEnabled(false);
            }
         }catch(IOException ie){ }
      }
   }
   public void broadcast(String msg){
      Iterator<String> list = clist.keySet().iterator();
      while(list.hasNext()){
         try{
            DataOutputStream dos = clist.get(list.next());
            dos.writeUTF(msg);
            dos.flush();
         }catch(IOException ie){}
      }
   }
   public class Gmanager extends Thread {
       Socket soc;
       DataInputStream dis;
       DataOutputStream dos;

       public Gmanager(Socket soc){
          this.soc = soc;
          try{
             dis = new DataInputStream(this.soc.getInputStream());
             dos = new DataOutputStream(this.soc.getOutputStream());
          }catch(IOException io){}
       }

       public void run(){
          String inform = "";
          String name = "";
          try{
             inform = dis.readUTF();
             name = inform.substring(0,inform.indexOf("/"));
             String chaN = inform.substring(inform.indexOf("/")+1);
             chaN = chaN.trim();
             charac = Integer.parseInt(chaN);
             if(!clist.containsKey(name)){
                clist.put(name,dos);
                cScore.put(name,score);
                cCharac.put(name,charac);
             }else if(clist.containsKey(name))  soc.close();
             broadcast("#" + Setting.CHAT+" * "+name+" 님, 입장했습니다.\n * 접속자 : "+clist.size()+" 명 / 4명");
             SerLog.append("현재 접속자, 총 "+clist.size()+" 명\n");
             Iterator<String> nlisting = clist.keySet().iterator();
             while(nlisting.hasNext()) SerLog.append(nlisting.next()+"\n");
             scrP.getVerticalScrollBar().setValue(scrP.getVerticalScrollBar().getMaximum());
             resetList();
             while(dis!=null){
                String msg = dis.readUTF();
                msgChecking(msg);
             }
          }catch(IOException ie){
             clist.remove(name);
             cScore.remove(name);
             cCharac.remove(name);
             closeAll();
             if(clist.isEmpty() == true){
                try{
                   Sers.close();
                   System.exit(0);
                }catch(IOException ii){}
             }
             broadcast("#"+Setting.CHAT+" * "+name+" 님, 퇴장했습니다. \n * 접속자 : "+clist.size()+" 명 / 4명");
             SerLog.append(" * 현재 접속 명단 ( 총"+clist.size()+"명 접속중 ) \n");
             Iterator<String> nlisting = clist.keySet().iterator();
             while(nlisting.hasNext()) SerLog.append(nlisting.next()+"\n");
             scrP.getVerticalScrollBar().setValue(scrP.getVerticalScrollBar().getMaximum());
             resetList();
             ready=0;
             MusicStart=false;
             broadcast("#"+Setting.GAME_OVER);
          }
       }
   
       public void closeAll(){
           try{
              if(dos != null) dos.close();
              if(dis != null) dis.close();
              if(soc != null) soc.close();
           }catch(IOException ie){}
       }

       public void resetList(){
          String[] keys = new String[cScore.size()];
          int[] values = new int[cScore.size()];
          String[] characNum = new String[cCharac.size()];
          int[] charackey = new int[cCharac.size()]; 
          int idx = 0;
          for(Map.Entry<String,Integer> mEntry : cScore.entrySet()){
             keys[idx] = mEntry.getKey();
             values[idx] = mEntry.getValue();
             idx++;
          }
          idx = 0;
          for(Map.Entry<String,Integer> cEntry : cCharac.entrySet()){
             characNum[idx] = cEntry.getKey();
             charackey[idx] = cEntry.getValue();
             idx++;
          }
          for(int i =0; i<clist.size(); i++) {
             broadcast("#" + Setting.RESET + keys[i] + "/" +values[i] + "," + charackey[i]+"_"+characNum[i]+" "+i);
          }
       }
       void readmsg(String songMsg){
          File dir = new File("신청곡 목록.txt");
          if(!dir.exists()) dir.mkdir();
          FileWriter fw = null;
          PrintWriter pw = null;
          try{
            File SongRequest = new File(dir, "신청곡 목록.txt");
            fw = new FileWriter(SongRequest, true);
            pw = new PrintWriter(fw, true);
            pw.print(songMsg+"\n");
            if(pw != null) pw.close();
            if(fw != null) fw.close();
          }catch(IOException ie){ }
         }

       public void msgChecking(String msg){
          String code = msg.substring(0,3);
          if(code.equals("@" + Setting.CHAT)){
             answerChecking(msg.substring(3).trim());
             broadcast("#" + Setting.CHAT+msg.substring(3));
          }else if(code.equals("@" + Setting.READY)){
             ready++;
             P.pln(ready+" 명 준비완료");
             broadcast("#" + Setting.READY+msg.substring(3));
             if(ready > 1 && ready == clist.size()){
                try{
                   broadcast("#" + Setting.COUNT+"모두 준비되었습니다 !");
                   Thread.sleep(2000);
                   for(int i =5; i>0; i--){
                      broadcast("#" + Setting.COUNT+i+" 초 후 게임을 시작합니다!");
                      Thread.sleep(1000);
                   }
                }catch(InterruptedException ie){ }
                Vector<String> randomAuthor = new Vector<String>();
                Iterator<String> authorName = clist.keySet().iterator();
                while(authorName.hasNext()) randomAuthor.add(authorName.next());
                Random r = new Random();
                int randomNum = r.nextInt((randomAuthor.size()));
                String author = randomAuthor.get(randomNum);
                broadcast("#" + Setting.AUTHOR+author);
                broadcast("#" + Setting.QUIZ+author+" 님이 장르를 선택합니다.");
             }
          }else if(code.equals("@" + Setting.CHOOSE)){
             genre = msg.substring(3);
             P.pln("도착한 장르"+genre);
             randomSongNumber(genre);//장르에 해당하는 숫자 랜덤으로 뽑아줌.
          }else if(code.equals("@" + Setting.SEND)){
             String songMsg = msg.substring(3);
             readmsg(songMsg);
          }else if(code.equals("@" + Setting.NEXT)){ // 노래 재생 후 돌아오는 메소드
             clientPlay++; //클라이언트쪽에서 다 연락이 오면,
             try{
                Thread.sleep(500);
             }catch(InterruptedException ie){ }
             //replay();
          }
       }
       public void randomSongNumber(String genre){
          playNumber=0;
          clientPlay=0;
          P.pln((playNumber+1)+"회 재생 중.");
          Random r = new Random();
          MusicStart = true;
          gameNumber++;
          playNumber=0;
          if(gameNumber > 5){ //5판 하면 끝남.
             broadcast("#"+Setting.GAME_OVER);
             broadcast("#"+Setting.QUIZ+" * 게임이 종료되었습니다! ");
             MusicStart = false;
             gameNumber = 0;
             playNumber = 0;
             ready=0;
             genre="";
          }
          try{
            FileReader fr = new FileReader("txt\\lines.txt");
            br = new BufferedReader(fr);
            for(int i = 0; i<14; i++) lines[i] = br.readLine(); //정답번호매겨짐.
          }catch(FileNotFoundException fe){
          }catch(IOException ie){ }
          randomN = r.nextInt(4);
          P.pln("장르:"+genre+"/ 뽑힌랜덤넘버 : "+randomN);
          int dance =0;
          int hiphop =5;
          int trot = 10;
          if(genre.equals("댄스")) randomN = randomN+dance;
          else if(genre.equals("힙합")) randomN = randomN+hiphop;
          else if(genre.equals("트로트")) randomN = randomN+trot;
          else if(genre.equals("랜덤")) randomN = r.nextInt(14);
          
          P.pln("장르:"+genre+"/ 필터링거친랜덤넘버 : "+randomN);
          broadcast("#" + Setting.MUSIC+"/"+randomN);
          P.pln("정답:" + lines[randomN]);
	   }
      //--------------------------------------------------------------------//
         
             
       synchronized void replay(){
          playNumber++;
          //broadcast("#" + Setting.MUSIC + "/" + randomN);
          P.pln("clientPlay:"+clientPlay);
          P.pln("PlayNumber:"+playNumber);
          if(playNumber > 3) { //3회이상 재생되면, pass
             broadcast("#"+Setting.CHAT+" 아쉽네요! PASS!");
             randomSongNumber(genre);
          }else if(playNumber <= 3){ //아니면 2초 후 다시재생
             broadcast("#" + Setting.MUSIC + "/" + randomN);
             clientPlay=0;
          }
      }
       public void answerChecking(String msg){
          String nick = msg.substring(0,msg.indexOf(" "));
          String answer = msg.substring(msg.indexOf(":")+2);
          answer = answer.trim();
          P.pln("정답확인중~:"+nick+"answer:"+answer);
          //if(answer.equals(lines[ran]) && MusicStart == true){ //정답 맞으면.
          if(answer.equals(lines[randomN]) && MusicStart == true){
             broadcast("#" + Setting.CHAT+"< "+nick+"> 님 정답!");
             cScore.put(nick,cScore.get(nick)+1);
             resetList();
             //randomSongNumber(genre);// 다시 재생
          }
       }
   }

   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable(){
         public void run(){
            try{
               BTServer bt = new BTServer();
               bt.init();
               bt.setVisible(true);
            }catch(Exception e){}
         }
      });
   }
}