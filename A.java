import java.util.*;
import java.io.*;

class Main{
	public static void main(String[] args) throws IOException{
	

	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
	
		//try{
		int A,B = 0;
		Integer m = Integer.parseInt(br.readLine().trim());
		
		
		for(int i=0; i<=m; i++){
		try{
		StringTokenizer st = new StringTokenizer(br.readLine());
		//StringTokenizer�� nextToken()�Լ��� ����Ͽ� readLine()�� ���� �Է¹��� �����͸� ���� ������ �����Ͽ� ������� ȣ��
		//�̰� ������ΰ��� �ҷ��ͼ� �ϳ��� �ɰ��ִ°� 

		A = Integer.parseInt(st.nextToken());
		B = Integer.parseInt(st.nextToken());

		bw.write((A+B)+"\n");// print �� ��ſ� ���°�

		br.close();
		bw.flush();
		bw.close();
	
		}catch(IOException ie){
		}catch(NoSuchElementException nee){
		}catch(NumberFormatException nfe){}
		}
	}
}

