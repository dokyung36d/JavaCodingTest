package problem;

import java.util.*;
import java.io.*;


public class 최빈수구하기 {
	static int T;
	static int testNum;
	static int[] scoreList;
	static Map<Integer, Integer> scoreMap;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws Exception {
		StringTokenizer st = new StringTokenizer(br.readLine());
		T = Integer.parseInt(st.nextToken());
		
		for (int i = 0; i < T; i++) {
			init();
			int answer = solution();
			System.out.println("#" + testNum + " " + answer);
		}
	}
	
	public static int solution() {
		int answerScore = -1;
		int maxNumAppeared = -1;
		
		for (int score : scoreMap.keySet()) {
			int numAppeared = scoreMap.get(score);
			if (numAppeared >= maxNumAppeared && score > answerScore) {
				maxNumAppeared = numAppeared;
				answerScore = score;
			}
		}
		
		return answerScore;
	}
	
	public static void init() throws IOException {
		scoreList = new int[1000];
		scoreMap = new HashMap<>();
		StringTokenizer st = new StringTokenizer(br.readLine());
		testNum = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < 1000; i++) {
			int score = Integer.parseInt(st.nextToken());
			
			int numPrevAppeared = scoreMap.getOrDefault(score, 0);
			scoreMap.put(score, numPrevAppeared + 1);
		}
	}
}