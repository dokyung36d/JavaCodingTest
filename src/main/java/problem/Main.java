package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static int[][] wallMatrix;
	static int[][] groupMatrix;
	static Pos[] directions = {new Pos(0, -1), new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0)};

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
			if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= M) { return false; }

			return true;
		}
	}

	public static class Neighbor {
		int smallUniqueNum;
		int bigUniqueNum;

		public Neighbor(int uniqueNum1, int uniqueNum2) {
			this.smallUniqueNum = Math.min(uniqueNum1, uniqueNum2);
			this.bigUniqueNum = Math.max(uniqueNum1, uniqueNum2);
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj) { return true; }
			if (obj == null || this.getClass() != obj.getClass()) { return false; }

			Neighbor anotherNeighbor = (Neighbor) obj;
			if (this.smallUniqueNum == anotherNeighbor.smallUniqueNum && this.bigUniqueNum == anotherNeighbor.bigUniqueNum) {
				return true;
			}

			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.smallUniqueNum, this.bigUniqueNum);
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		groupMatrix = new int[N][M];

		int groupUniqueNum = 1;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (groupMatrix[i][j] != 0) { continue; }

				Deque<Pos> queue = new ArrayDeque<>();
				queue.add(new Pos(i, j));

				while (!queue.isEmpty()) {
					Pos curPos = queue.pollFirst();
					if (groupMatrix[curPos.row][curPos.col] != 0) { continue; }
					groupMatrix[curPos.row][curPos.col] = groupUniqueNum;

					for (int directionIndex = 0; directionIndex < 4; directionIndex++) {
						if ((wallMatrix[curPos.row][curPos.col] & (1 << directionIndex)) != 0) { continue; }
						Pos direction = directions[directionIndex];

						Pos movedPos = curPos.addPos(direction);
						if (!movedPos.isValidIndex()) { continue; }
						if (groupMatrix[movedPos.row][movedPos.col] != 0) { continue; }

						queue.add(movedPos);
					}
				}

				groupUniqueNum += 1;
			}
		}


		Map<Integer, Integer> groupSizeMap = new HashMap<>();
		Map<Neighbor, Integer> neighborMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				groupSizeMap.put(groupMatrix[i][j], groupSizeMap.getOrDefault(groupMatrix[i][j], 0) + 1);

				for (int directionIndex = 0; directionIndex < 4; directionIndex++) {
					if ((wallMatrix[i][j] & (1 << directionIndex)) == 0) { continue; }

					Pos curPos = new Pos(i, j);
					Pos movedPos = curPos.addPos(directions[directionIndex]);
					if (!movedPos.isValidIndex()) { continue; }
					if (groupMatrix[curPos.row][curPos.col] == groupMatrix[movedPos.row][movedPos.col]) { continue; }

					neighborMap.put(new Neighbor(groupMatrix[curPos.row][curPos.col], groupMatrix[movedPos.row][movedPos.col]), 1);
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append(groupSizeMap.keySet().size());
		sb.append("\n");

		int maxGroupSize = 0;
		for (int uniqueNum : groupSizeMap.keySet()) {
			maxGroupSize = Math.max(maxGroupSize, groupSizeMap.get(uniqueNum));
		}
		sb.append(maxGroupSize);
		sb.append("\n");

		int maxSize = 0;
		for (Neighbor neighbor : neighborMap.keySet()) {
			maxSize = Math.max(maxSize, groupSizeMap.get(neighbor.smallUniqueNum) + groupSizeMap.get(neighbor.bigUniqueNum));
		}
		sb.append(maxSize);
		System.out.println(sb.toString());
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		M = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());

		wallMatrix = new int[N][M];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				wallMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
	}
}