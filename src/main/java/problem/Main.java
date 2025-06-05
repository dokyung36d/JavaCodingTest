package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N;
	static Matrix[] matrixList;

	public static class Matrix {
		int row;
		int col;

		public Matrix(int row, int col) {
			this.row = row;
			this.col = col;
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		int[][] dpMatrix = new int[N][N];
		for (int i = 0; i < N; i++) {
			Arrays.fill(dpMatrix[i], Integer.MAX_VALUE);
			dpMatrix[i][i] = 0;
		}

		for (int gap = 1; gap < N; gap++) {
			for (int start = 0; start + gap < N; start++) {
				for (int mid = 0; mid < gap; mid++) {
					int numOperation = dpMatrix[start][start + mid] + dpMatrix[start + mid + 1][start + gap]
							+ matrixList[start].row * matrixList[start + mid].col * matrixList[start + gap].col;

					dpMatrix[start][start + gap] = Math.min(dpMatrix[start][start + gap], numOperation);
				}
			}
		}

		System.out.println(dpMatrix[0][N - 1]);
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		matrixList = new Matrix[N];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());

			int row = Integer.parseInt(st.nextToken());
			int col = Integer.parseInt(st.nextToken());

			matrixList[i] = new Matrix(row, col);
		}
	}



}