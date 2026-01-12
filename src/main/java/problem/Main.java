package problem;

import java.util.*;
import java.io.*;



public class Main {
	static int N, M;
	static int[][] mainMatrix;
	static int[][] dpMatrix;
	static Map<Integer, Integer> groupSizeMap;
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
		setDpMatrix();


		int[][] answerMatrix = new int[N][M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (mainMatrix[i][j] == 0) { continue; }

				Map<Integer, Integer> nearGroupMap = new HashMap<>();
				for (Pos direction : directions) {
					Pos movedPos = new Pos(i, j).addPos(direction);
					if (!movedPos.isValidIndex()) { continue; }
					if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

					nearGroupMap.put(dpMatrix[movedPos.row][movedPos.col], 1);
				}

				int totalSize = 0;
				for (int uniqueNum : nearGroupMap.keySet()) {
					totalSize += groupSizeMap.get(uniqueNum);
				}
				totalSize += 1;
				answerMatrix[i][j] = totalSize % 10;
			}
		}


		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				sb.append(answerMatrix[i][j]);
			}
			sb.append("\n");
		}
		System.out.println(sb.toString().substring(0, sb.length() - 1));
	}

	public static void setDpMatrix() {
		dpMatrix = new int[N][M];
		groupSizeMap = new HashMap<>();

		int[][] visited = new int[N][M];
		int uniqueNum = 1;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (mainMatrix[i][j] == 1) { continue; }
				if (visited[i][j] == 1) { continue; }

				int groupSize = 0;

				Deque<Pos> queue = new ArrayDeque<>();
				queue.add(new Pos(i, j));

				while (!queue.isEmpty()) {
					Pos curPos = queue.poll();
					if (visited[curPos.row][curPos.col] == 1) { continue; }
					visited[curPos.row][curPos.col] = 1;
					dpMatrix[curPos.row][curPos.col] = uniqueNum;
					groupSize += 1;

					for (Pos direction : directions) {
						Pos movedPos = curPos.addPos(direction);
						if (!movedPos.isValidIndex()) { continue; }
						if (visited[movedPos.row][movedPos.col] == 1) { continue; }
						if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

						queue.addLast(movedPos);
					}
				}

				groupSizeMap.put(uniqueNum, groupSize);
				uniqueNum += 1;
			}
		}
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
