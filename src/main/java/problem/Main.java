package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static int[][] mainMatrix;
	static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};

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
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		int answer = 0;


		for (int wallHeight = 9; wallHeight >= 1; wallHeight--) {
			List<Pos> poolPosList = getPoolPosList(wallHeight);

			for (Pos poolPos : poolPosList) {
				answer += wallHeight - mainMatrix[poolPos.row][poolPos.col];
				mainMatrix[poolPos.row][poolPos.col] = wallHeight;
			}
		}


		System.out.println(answer);
	}

	public static List<Pos> getPoolPosList(int wallHeight) {
		int[][] poolMatrix = new int[N][M];
		int[][] wallMatrix = new int[N][M];
		int[][] edgeMatrix = new int[N][M];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (mainMatrix[i][j] >= wallHeight) {
					wallMatrix[i][j] = 1;
				}
			}
		}

		Deque<Pos> edgeQueue = new ArrayDeque<>();
		for (int i = 0; i < N; i++) {
			if (wallMatrix[i][0] == 0) {
				edgeQueue.add(new Pos(i, 0));
			}

			if (wallMatrix[i][M - 1] == 0) {
				edgeQueue.add(new Pos(i, M - 1));
			}
		}

		for (int j = 1; j < M - 1; j++) {
			if (wallMatrix[0][j] == 0) {
				edgeQueue.add(new Pos(0, j));
			}

			if (wallMatrix[N - 1][j] == 0) {
				edgeQueue.add(new Pos(N - 1, j));
			}
		}

		while (!edgeQueue.isEmpty()) {
			Pos curPos = edgeQueue.pollFirst();
			if (edgeMatrix[curPos.row][curPos.col] == 1) { continue; }
			edgeMatrix[curPos.row][curPos.col] = 1;

			for (Pos direction : directions) {
				Pos movedPos = curPos.addPos(direction);
				if (!movedPos.isValidIndex()) { continue; }
				if (wallMatrix[movedPos.row][movedPos.col] == 1) { continue; }
				if (edgeMatrix[movedPos.row][movedPos.col] == 1) { continue; }

				edgeQueue.add(movedPos);
			}
		}

		List<Pos> poolPosList = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (wallMatrix[i][j] == 1) { continue; }
				if (edgeMatrix[i][j] == 1) { continue; }

				poolMatrix[i][j] = 1;
				poolPosList.add(new Pos(i, j));
			}
		}



		return poolPosList;
	}


	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		mainMatrix = new int[N][M];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			String string = st.nextToken();
			for (int j = 0; j < M; j++) {
				mainMatrix[i][j] = Character.getNumericValue(string.charAt(j));
			}
		}
	}
}