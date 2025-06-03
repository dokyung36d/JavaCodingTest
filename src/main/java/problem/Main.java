package problem;

import java.nio.Buffer;
import java.util.*;
import java.io.*;


public class Main {
	static int N;
	static int[][] colorMatrix;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		int minCost = Integer.MAX_VALUE / 2;

		for (int i = 0; i < 3; i++) {
			minCost = Math.min(minCost, getDPResult(i));
		}

		System.out.println(minCost);
	}

	public static int getDPResult(int startIndex) {
		int[][] dpMatrix = new int[N][3];

		for (int i = 0; i < N; i++) {
			Arrays.fill(dpMatrix[i], Integer.MAX_VALUE / 2);
		}
		dpMatrix[0][startIndex] = colorMatrix[0][startIndex];

		for (int i = 0; i < N - 1; i++) {
			for (int j = 0; j < 3; j++) {
				dpMatrix[i + 1][((j + 3) - 1) % 3] = Math.min(dpMatrix[i + 1][((j + 3) - 1) % 3],
						dpMatrix[i][j] + colorMatrix[i + 1][((j + 3) - 1) % 3]);

				dpMatrix[i + 1][(j + 1) % 3] = Math.min(dpMatrix[i + 1][(j + 1) % 3],
						dpMatrix[i][j] + colorMatrix[i + 1][(j + 1) % 3]);
			}
		}

		dpMatrix[N - 1][startIndex] = Integer.MAX_VALUE / 2;
		int minValue = Integer.MAX_VALUE / 2;

		for (int i = 0; i < 3; i++) {
			minValue = Math.min(minValue, dpMatrix[N - 1][i]);
		}

		return minValue;
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		colorMatrix = new int[N][3];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());

			int redCost = Integer.parseInt(st.nextToken());
			int greenCost = Integer.parseInt(st.nextToken());
			int blueCost = Integer.parseInt(st.nextToken());

			colorMatrix[i][0] = redCost;
			colorMatrix[i][1] = greenCost;
			colorMatrix[i][2] = blueCost;
		}
	}
	
}