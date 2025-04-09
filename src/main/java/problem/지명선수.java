package problem;

import java.util.*;
import java.io.*;


public class 지명선수 {
	static int T;
	static int N;
	static int[] aList, bList;
	static Deque<Integer> aQueue, bQueue; 
	static int[] outed;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws Exception {
		StringTokenizer st = new StringTokenizer(br.readLine());
		T = Integer.parseInt(st.nextToken());
		
		for (int i =  0; i < T; i++) {
			init();
			String answer = solution();
			System.out.println(answer);
		}
	}
	
	public static String solution() {		
		for (int i = 0; i < N; i++) {
			while (!aQueue.isEmpty()) {
				int player = aQueue.pollFirst();
				if (outed[player] != 0) { continue; }
				
				outed[player] = 1;
				break;
			}
			
			while (!bQueue.isEmpty()) {
				int player = bQueue.pollFirst();
				if (outed[player] != 0) { continue; }
				
				outed[player] = 2;
				break;
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			if (outed[i] == 1) {
				sb.append("A");
			}
			else {
				sb.append("B");
			}
		}
		
		return sb.toString();
	}
	
	public static void init() throws IOException {
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		
		aList = new int[N];
		bList = new int[N];
		
		outed = new int[N];
		
		aQueue = new ArrayDeque<>();
		bQueue = new ArrayDeque<>();
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			aList[i] = Integer.parseInt(st.nextToken()) - 1;
			aQueue.addLast(aList[i]);
		}
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			bList[i] = Integer.parseInt(st.nextToken()) - 1;
			bQueue.addLast(bList[i]);
		}
	}
}