package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N;
	static int[] numList;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		int[] indexList= new int[1000001];
		int[] scoreList = new int[N];
		Map<Integer, Integer> scoreMap = new HashMap<>();

		for (int i = 0; i < N; i++) {
			indexList[numList[i]] = i + 1;
		}

		for (int i = 0; i < N; i++) {
			for (int curNum = numList[i] * 2; curNum < 1000001; curNum += numList[i]) {
				if (indexList[curNum] == 0) { continue; }

				scoreList[i] += 1;
				scoreList[indexList[curNum] - 1] -= 1;
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			sb.append(scoreList[i]);
			sb.append(" ");
		}

		System.out.println(sb.toString().substring(0, sb.length() - 1));
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		numList = new int[N];

		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			numList[i] = Integer.parseInt(st.nextToken());
		}
	}
}