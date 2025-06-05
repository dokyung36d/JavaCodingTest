package problem;
import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static int maxCost;
	static int[] memorySizeList, costList;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		int[][] dpMatrix = new int[N + 1][maxCost + 1];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < maxCost + 1; j++) {
				dpMatrix[i + 1][j] = dpMatrix[i][j];
				if (costList[i] > j) { continue; }

				dpMatrix[i + 1][j] = Math.max(dpMatrix[i + 1][j],
						dpMatrix[i][j - costList[i]] + memorySizeList[i]);
			}
		}

		for (int i = 0; i < maxCost + 1; i++) {
			if (dpMatrix[N][i] >= M) {
				System.out.println(i);
				return;
			}
		}
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		memorySizeList = new int[N];
		costList = new int[N];

		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			memorySizeList[i] = Integer.parseInt(st.nextToken());
		}

		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			costList[i] = Integer.parseInt(st.nextToken());
			maxCost += costList[i];
		}
	}
	
}