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
		//StringTokenizer의 nextToken()함수를 사용하여 readLine()를 통해 입력받은 데이터를 공백 단위로 구분하여 순서대로 호출
		//이게 리드라인값을 불러와서 하나씩 쪼개주는거 

		A = Integer.parseInt(st.nextToken());
		B = Integer.parseInt(st.nextToken());

		bw.write((A+B)+"\n");// print 값 대신에 쓰는거

		br.close();
		bw.flush();
		bw.close();
	
		}catch(IOException ie){
		}catch(NoSuchElementException nee){
		}catch(NumberFormatException nfe){}
		}
	}
}

