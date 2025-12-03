package problem;

import java.util.*;
import java.io.*;

public class Main {
    static int N, M;
	static String str1, str2;
	static int[][] dpMatrix;

	public static class Pos {
		int row;
		int col;

		public Pos(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public Pos addPos(Pos direction) {
			return new Pos(this.row + direction.row, this.col + direction.col);
		}

		public boolean isValidIndex() {
			if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= M) {
				return false;
			}

			return true;
		}

		public int getDPValue() {
			return dpMatrix[this.row][this.col];
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		int rowFlag = 0;
		for (int i = 0; i < N; i++) {
			if (rowFlag == 1 || str1.charAt(i) == str2.charAt(0)) {
				dpMatrix[i][0] = 1;
				rowFlag = 1;
			}
		}

		int colFlag = 0;
		for (int j = 0; j < M; j++) {
			if (colFlag == 1 || str2.charAt(j) == str1.charAt(0)) {
				dpMatrix[0][j] = 1;
				colFlag = 1;
			}
		}


		for (int i = 1; i < N; i++) {
			for (int j = 1; j < M; j++) {
				if (str1.charAt(i) == str2.charAt(j)) {
					dpMatrix[i][j] = dpMatrix[i - 1][j - 1] + 1;
					continue;
				}

				dpMatrix[i][j] = Math.max(dpMatrix[i - 1][j], dpMatrix[i][j - 1]);
			}
		}

		if (dpMatrix[N - 1][M - 1] == 0) {
			System.out.println(0);
			return;
		}

		System.out.println(dpMatrix[N - 1][M - 1]);


		StringBuilder sb = new StringBuilder();
		Pos curPos = new Pos(N - 1, M - 1);
		while (curPos.row != 0 && curPos.col != 0) {
			Pos leftPos = curPos.addPos(new Pos(0, -1));
			Pos upPos = curPos.addPos(new Pos(-1, 0));
			Pos upLeftPos = curPos.addPos(new Pos(-1, -1));

			if (curPos.getDPValue() == upLeftPos.getDPValue()) {
				curPos = upLeftPos;
				continue;
			}

			if (curPos.getDPValue() == leftPos.getDPValue()) {
				curPos = leftPos;
				continue;
			}

			if (curPos.getDPValue() == upPos.getDPValue()) {
				curPos = upPos;
				continue;
			}

			sb.append(str1.charAt(curPos.row));
			curPos = upLeftPos;
		}

		if (curPos.getDPValue() == 0) {

		}
		else if (curPos.row == 0) {
			sb.append(str1.charAt(0));
		}
		else {
			sb.append(str2.charAt(0));
		}

		System.out.println(sb.reverse().toString());
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine());
		str1 = st.nextToken();

		st = new StringTokenizer(br.readLine());
		str2 = st.nextToken();

		N = str1.length();
		M = str2.length();

		dpMatrix = new int[N][M];
	}
}