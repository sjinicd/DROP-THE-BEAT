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

      SerStatus = new JLabel(" ���� ����� ! ");
      SerStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
      SerStatus.setPreferredSize(new Dimension(70,70));
      SerStatus.setFont(new Font("������� ExtraBold", Font.PLAIN, 15));
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

      SerStart = new JButton("���� � ����");
      SerStart.setHorizontalTextPosition(SwingConstants.CENTER);
      SerStart.setPreferredSize(new Dimension(120, 40));
      SerStart.setFocusPainted(false);
      SerStart.setAlignmentX(Component.CENTER_ALIGNMENT);
      SerStart.setForeground(Color.WHITE);
      SerStart.setBackground(Color.DARK_GRAY);
      SerStart.setBorder(null);
      p_button.add(SerStart);
      SerStart.addActionListener(this);

      SerClose = new JButton("���� � ����");
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
         //P.pln("���� � ����");
         new Thread(){
            public void run() {
               try{
                  Collections.synchronizedMap(clist);
                  Collections.synchronizedMap(cScore);
                  Collections.synchronizedMap(cCharac);
                  Sers = new ServerSocket(port);
                  SerStatus.setText(" [ Server Start ] ");
                  SerLog.append(" ���� ���� ����. " + "\n");
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
         int SerSelect = JOptionPane.showConfirmDialog(null, "������ �����Ͻðڽ��ϱ�?","Beat Server", JOptionPane.OK_CANCEL_OPTION);
         try{
            if(SerSelect == JOptionPane.YES_OPTION){
               Sers.close();
               SerLog.setText(" [ Server Closed ] ");
               SerLog.append(" ���� ����. "+"\n");
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
             broadcast("#" + Setting.CHAT+" * "+name+" ��, �����߽��ϴ�.\n * ������ : "+clist.size()+" �� / 4��");
             SerLog.append("���� ������, �� "+clist.size()+" ��\n");
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
             broadcast("#"+Setting.CHAT+" * "+name+" ��, �����߽��ϴ�. \n * ������ : "+clist.size()+" �� / 4��");
             SerLog.append(" * ���� ���� ��� ( ��"+clist.size()+"�� ������ ) \n");
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
          File dir = new File("��û�� ���.txt");
          if(!dir.exists()) dir.mkdir();
          FileWriter fw = null;
          PrintWriter pw = null;
          try{
            File SongRequest = new File(dir, "��û�� ���.txt");
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
             P.pln(ready+" �� �غ�Ϸ�");
             broadcast("#" + Setting.READY+msg.substring(3));
             if(ready > 1 && ready == clist.size()){
                try{
                   broadcast("#" + Setting.COUNT+"��� �غ�Ǿ����ϴ� !");
                   Thread.sleep(2000);
                   for(int i =5; i>0; i--){
                      broadcast("#" + Setting.COUNT+i+" �� �� ������ �����մϴ�!");
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
                broadcast("#" + Setting.QUIZ+author+" ���� �帣�� �����մϴ�.");
             }
          }else if(code.equals("@" + Setting.CHOOSE)){
             genre = msg.substring(3);
             P.pln("������ �帣"+genre);
             randomSongNumber(genre);//�帣�� �ش��ϴ� ���� �������� �̾���.
          }else if(code.equals("@" + Setting.SEND)){
             String songMsg = msg.substring(3);
             readmsg(songMsg);
          }else if(code.equals("@" + Setting.NEXT)){ // �뷡 ��� �� ���ƿ��� �޼ҵ�
             clientPlay++; //Ŭ���̾�Ʈ�ʿ��� �� ������ ����,
             try{
                Thread.sleep(500);
             }catch(InterruptedException ie){ }
             //replay();
          }
       }
       public void randomSongNumber(String genre){
          playNumber=0;
          clientPlay=0;
          P.pln((playNumber+1)+"ȸ ��� ��.");
          Random r = new Random();
          MusicStart = true;
          gameNumber++;
          playNumber=0;
          if(gameNumber > 5){ //5�� �ϸ� ����.
             broadcast("#"+Setting.GAME_OVER);
             broadcast("#"+Setting.QUIZ+" * ������ ����Ǿ����ϴ�! ");
             MusicStart = false;
             gameNumber = 0;
             playNumber = 0;
             ready=0;
             genre="";
          }
          try{
            FileReader fr = new FileReader("txt\\lines.txt");
            br = new BufferedReader(fr);
            for(int i = 0; i<14; i++) lines[i] = br.readLine(); //�����ȣ�Ű���.
          }catch(FileNotFoundException fe){
          }catch(IOException ie){ }
          randomN = r.nextInt(4);
          P.pln("�帣:"+genre+"/ ���������ѹ� : "+randomN);
          int dance =0;
          int hiphop =5;
          int trot = 10;
          if(genre.equals("��")) randomN = randomN+dance;
          else if(genre.equals("����")) randomN = randomN+hiphop;
          else if(genre.equals("Ʈ��Ʈ")) randomN = randomN+trot;
          else if(genre.equals("����")) randomN = r.nextInt(14);
          
          P.pln("�帣:"+genre+"/ ���͸���ģ�����ѹ� : "+randomN);
          broadcast("#" + Setting.MUSIC+"/"+randomN);
          P.pln("����:" + lines[randomN]);
	   }
      //--------------------------------------------------------------------//
         
             
       synchronized void replay(){
          playNumber++;
          //broadcast("#" + Setting.MUSIC + "/" + randomN);
          P.pln("clientPlay:"+clientPlay);
          P.pln("PlayNumber:"+playNumber);
          if(playNumber > 3) { //3ȸ�̻� ����Ǹ�, pass
             broadcast("#"+Setting.CHAT+" �ƽ��׿�! PASS!");
             randomSongNumber(genre);
          }else if(playNumber <= 3){ //�ƴϸ� 2�� �� �ٽ����
             broadcast("#" + Setting.MUSIC + "/" + randomN);
             clientPlay=0;
          }
      }
       public void answerChecking(String msg){
          String nick = msg.substring(0,msg.indexOf(" "));
          String answer = msg.substring(msg.indexOf(":")+2);
          answer = answer.trim();
          P.pln("����Ȯ����~:"+nick+"answer:"+answer);
          //if(answer.equals(lines[ran]) && MusicStart == true){ //���� ������.
          if(answer.equals(lines[randomN]) && MusicStart == true){
             broadcast("#" + Setting.CHAT+"< "+nick+"> �� ����!");
             cScore.put(nick,cScore.get(nick)+1);
             resetList();
             //randomSongNumber(genre);// �ٽ� ���
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