package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, - 1)};
	static Map<Pos, List<Pos>> switchMap;

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
			if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
				return false;
			}

			return true;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) { return true; }
			if (obj == null || this.getClass() != obj.getClass()) { return false; }

			Pos anotherPos = (Pos) obj;
			if (this.row == anotherPos.row && this.col == anotherPos.col) { return true; }

			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.row, this.col);
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		int[][] visited = new int[N][N];

		int[][] visitableMatrix = new int[N][N];
		int[][] switchOnMatrix = new int[N][N];
		switchOnMatrix[0][0] = 1;

		Deque<Pos> queue = new ArrayDeque<>();
		queue.add(new Pos(0, 0));

		while (!queue.isEmpty()) {
			Pos curPos = queue.pollFirst();
			if (visited[curPos.row][curPos.col] == 1) { continue; }

			visited[curPos.row][curPos.col] = 1;

			for (Pos direction : directions) {
				Pos movedPos = curPos.addPos(direction);
				if (!movedPos.isValidIndex()) { continue; }
				if (visited[movedPos.row][movedPos.col] == 1) { continue; }

				visitableMatrix[movedPos.row][movedPos.col] = 1;
				if (switchOnMatrix[movedPos.row][movedPos.col] == 1) {
					queue.add(movedPos);
				}
			}


			for (Pos switchPos : switchMap.get(curPos)) {
				switchOnMatrix[switchPos.row][switchPos.col] = 1;
				if (visited[switchPos.row][switchPos.col] == 1) { continue; }

				if (visitableMatrix[switchPos.row][switchPos.col] == 1) {
					queue.add(switchPos);
				}
			}

		}


		int numSwitchOn = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (switchOnMatrix[i][j] == 1) {
					numSwitchOn += 1;
				}
			}
		}

		System.out.println(numSwitchOn);
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		switchMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				switchMap.put(new Pos(i, j), new ArrayList<>());
			}
		}

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());

			int fromRow = Integer.parseInt(st.nextToken()) - 1;
			int fromCol = Integer.parseInt(st.nextToken()) - 1;

			int toRow = Integer.parseInt(st.nextToken()) - 1;
			int toCol = Integer.parseInt(st.nextToken()) - 1;

			switchMap.get(new Pos(fromRow, fromCol)).add(new Pos(toRow, toCol));
		}
	}
}