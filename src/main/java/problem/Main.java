package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static int[][] mainMatrix;
	static Pos[] centerDirections = {new Pos(0, 1), new Pos(1, 1),
									new Pos(1, 0), new Pos(0, 0)};
	static Pos[][] sideDirections = {{new Pos(0, 0), new Pos(1, 1)},
			{new Pos(0, 1), new Pos(1, 0)},
			{new Pos(0, 0), new Pos(1, 1)},
			{new Pos(0, 1), new Pos(1, 0)}};
	static int bestScore;
	static Pos endPos;

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

		public int getValue() {
			return mainMatrix[this.row][this.col];
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		bestScore = 0;
		recursive(new Pos(0, 0), 0, new int[N][M]);

		System.out.println(bestScore);
	}

	public static void recursive(Pos curPos, int score, int[][] visited) {
		if (curPos.row >= N) {
			return;
		}

		Pos nextPos;
		if (curPos.col == M - 1) {
			nextPos = new Pos(curPos.row + 1, 0);
		}
		else {
			nextPos = new Pos(curPos.row, curPos.col + 1);
		}

		for (int i = 0; i < 4; i++) {
			Pos centerPos = curPos.addPos(centerDirections[i]);
			Pos sidePos1 = curPos.addPos(sideDirections[i][0]);
			Pos sidePos2 = curPos.addPos(sideDirections[i][1]);

			if (!centerPos.isValidIndex()) { continue; }
			if (!sidePos1.isValidIndex()) { continue; }
			if (!sidePos2.isValidIndex()) { continue; }

			if (visited[centerPos.row][centerPos.col] == 1) { continue; }
			if (visited[sidePos1.row][sidePos1.col] == 1) { continue; }
			if (visited[sidePos2.row][sidePos2.col] == 1) { continue; }


			int updatedScore = score;
			updatedScore += (centerPos.getValue() * 2);
			updatedScore += sidePos1.getValue();
			updatedScore += sidePos2.getValue();

			bestScore = Math.max(bestScore, updatedScore);

			visited[centerPos.row][centerPos.col] = 1;
			visited[sidePos1.row][sidePos1.col] = 1;
			visited[sidePos2.row][sidePos2.col] = 1;

			recursive(nextPos, updatedScore, visited);

			visited[centerPos.row][centerPos.col] = 0;
			visited[sidePos1.row][sidePos1.col] = 0;
			visited[sidePos2.row][sidePos2.col] = 0;
		}

		recursive(nextPos, score, visited);
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		mainMatrix = new int[N][M];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				mainMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		endPos = new Pos(N - 1, M - 1);
	}
}