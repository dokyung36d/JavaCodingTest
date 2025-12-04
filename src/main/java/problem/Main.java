package problem;

import java.util.*;
import java.io.*;

public class Main {
	static int N, M;
	static int[] numList;
	static int[][] dpMatrix;
	static int[][] queryList;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		for (int i = 0; i < N; i++) {
			dpMatrix[i][i] = 1;
		}

		for (int i = 0; i < N - 1; i++) {
			if (numList[i] == numList[i + 1]) {
				dpMatrix[i][i + 1] = 1;
			}
		}

		for (int gap = 1; gap < N; gap++) {
			for (int from = 0; from + gap < N; from++) {
				int to = from + gap;

				if (dpMatrix[from + 1][to - 1] == 1 && numList[from] == numList[to]) {
					dpMatrix[from][to] = 1;
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < M; i++) {
			sb.append(dpMatrix[queryList[i][0]][queryList[i][1]]);
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
		dpMatrix = new int[N][N];

		st = new StringTokenizer(br.readLine());
		M = Integer.parseInt(st.nextToken());

		queryList = new int[M][2];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());

			int from = Integer.parseInt(st.nextToken()) - 1;
			int to = Integer.parseInt(st.nextToken()) - 1;
			queryList[i][0] = from;
			queryList[i][1] = to;
		}
	}
}