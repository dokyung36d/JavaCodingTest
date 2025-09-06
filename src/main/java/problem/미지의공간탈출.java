package problem;

import java.util.*;
import java.io.*;

public class 미지의공간탈출 {
	static int N, M, F;
	static int[][] floorMatrix, columnMatrix;
	static Pos[] directions = {new Pos(0, 1), new Pos(0, -1), new Pos(1, 0), new Pos(-1, 0)};
	static Pos columnStartPos, columnDestPos;
	static Pos floorStartPos, floorDestPos;
	static Pos topLeftColumnBottomPos;
	static List<Fire> fireList;

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

		public Pos moveInColumnMatrix(Pos direction) {
			Pos movedPos = this.addPos(direction);

			if (0 <= movedPos.row && movedPos.row < M && 0 <= movedPos.col && movedPos.col < M) {
				return new Pos(this.col, this.row);
			}

			if (2 * M <= movedPos.row && movedPos.row < 3 * M && 2 * M <= movedPos.col && movedPos.col < 3 * M) {
				return new Pos(this.col, this.row);
			}

			if (0 <= movedPos.row && movedPos.row < M && 2 * M <= movedPos.col && movedPos.col < 3 * M) {
				return new Pos(3 * M - this.col - 1, 3 * M - this.row - 1);
			}

			if (2 * M <= movedPos.row && movedPos.row < 3 * M && 0 <= movedPos.col && movedPos.col < M) {
				return new Pos(3 * M - this.col - 1, 3 * M - this.row - 1);
			}

			return movedPos;
		}

		public boolean isValidIndexInFloor() {
			if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
				return false;
			}

			return true;
		}

		public boolean isValidIndexInColumn() {
			if (this.row < 0 || this.row >= 3 * M || this.col < 0 || this.col >= 3 * M) {
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

	public static class Fire {
		Pos curPos;
		int cycle;
		int remainTime;
		int directionIndex;

		public Fire(Pos curPos, int cycle, int remainTime, int directionIndex) {
			this.curPos = curPos;
			this.cycle = cycle;
			this.remainTime = remainTime;
			this.directionIndex = directionIndex;
		}

		public Fire advance() {
			if (this.remainTime == 1) {
				Pos movedPos = this.curPos.addPos(directions[directionIndex]);
				if (!movedPos.isValidIndexInFloor()) { return null; }
				if (floorMatrix[movedPos.row][movedPos.col] == 1) { return null; }
				if (floorMatrix[movedPos.row][movedPos.col] == 3) { return null; }
				if (movedPos.equals(floorDestPos)) { return null; }

				floorMatrix[movedPos.row][movedPos.col] = 2;
				return new Fire(movedPos, cycle, cycle, this.directionIndex);
			}

			return new Fire(this.curPos, this.cycle, this.remainTime - 1, this.directionIndex);
		}
	}

	public static class Node {
		Pos curPos;
		int depth;

		public Node(Pos curPos, int depth) {
			this.curPos = curPos;
			this.depth = depth;
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		Pos pos = new Pos(8, 5);
		Pos movedPos = pos.moveInColumnMatrix(new Pos(0, 1));

		int depthInColumn = bfsInColumn();
		advanceAllFire();
		if (depthInColumn == -1 || floorMatrix[floorStartPos.row][floorStartPos.col] == 2) {
			System.out.println(-1);
			return;
		}
		int depthInFloor = bfsInFloor();
		if (depthInFloor == -1) {
			System.out.println(-1);
			return;
		}

		System.out.println(depthInColumn + 1 + depthInFloor);
	}

	public static int bfsInFloor() {
		Deque<Node> queue = new ArrayDeque<>();
		int[][] visited = new int[N][N];
		int maxDepth = 0;

		queue.add(new Node(floorStartPos, 0));
		while (!queue.isEmpty()) {
			Node node = queue.pollFirst();
			if (visited[node.curPos.row][node.curPos.col] == 1) { continue; }
			visited[node.curPos.row][node.curPos.col] = 1;

			if (node.depth > maxDepth) {
				advanceAllFire();
				maxDepth = node.depth;
			}
			if (floorMatrix[node.curPos.row][node.curPos.col] == 2) { continue; }


			if (node.curPos.equals(floorDestPos)) {
				return node.depth;
			}



			for (Pos direction : directions) {
				Pos movedPos = node.curPos.addPos(direction);
				if (!movedPos.isValidIndexInFloor()) { continue; }
				if (visited[movedPos.row][movedPos.col] == 1) { continue; }
				if (floorMatrix[movedPos.row][movedPos.col] != 0) { continue; }

				queue.addLast(new Node(movedPos, node.depth + 1));
			}
		}

		return -1;
	}

	public static int bfsInColumn() {
		Deque<Node> queue = new ArrayDeque<>();
		int[][] visited = new int[3 * M][3 * M];
		int maxDepth = 0;

		queue.add(new Node(columnStartPos, 0));

		while (!queue.isEmpty()) {
			Node node = queue.pollFirst();
			if (visited[node.curPos.row][node.curPos.col] == 1) { continue; }
			visited[node.curPos.row][node.curPos.col] = 1;

			if (node.depth > maxDepth) {
				advanceAllFire();
				maxDepth = node.depth;
			}

			if (node.curPos.equals(columnDestPos)) {
				return node.depth;
			}


			for (Pos direction : directions) {
				Pos movedPos = node.curPos.moveInColumnMatrix(direction);
				if (!movedPos.isValidIndexInColumn()) { continue; }
				if (visited[movedPos.row][movedPos.col] == 1) { continue; }
				if (columnMatrix[movedPos.row][movedPos.col] == 1)  { continue; }


				queue.addLast(new Node(movedPos, node.depth + 1));
			}

		}

		return -1;
	}

	public static void advanceAllFire() {
		List<Fire> updatedFireList = new ArrayList<>();

		for (Fire fire : fireList) {
			Fire advancedFire = fire.advance();
			if (advancedFire == null) { continue; }

			updatedFireList.add(advancedFire);
		}

		fireList = updatedFireList;
	}

	public static int[][] rotateNTimes(int[][] matrix, int numRotate) {
		for (int i = 0; i < numRotate; i++) {
			matrix = rotateMatrix(matrix);
		}

		return matrix;
	}

	public static int[][] rotateMatrix(int[][] matrix) {
		int[][] rotatedMatrix = new int[matrix.length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				rotatedMatrix[j][matrix.length - i - 1] = matrix[i][j];
			}
		}

		return rotatedMatrix;
	}

	public static void applyToColumnMatrix(Pos topLeftPos, int[][] matrix) {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				Pos curPos = topLeftPos.addPos(new Pos(i, j));
				columnMatrix[curPos.row][curPos.col] = matrix[i][j];
			}
		}
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		F = Integer.parseInt(st.nextToken());

		floorMatrix = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());

			for (int j = 0; j < N; j++) {
				floorMatrix[i][j] = Integer.parseInt(st.nextToken());

				if (floorMatrix[i][j] == 4) {
					floorDestPos = new Pos(i, j);
					floorMatrix[i][j] = 0;
				}

				if (floorMatrix[i][j] == 3 && topLeftColumnBottomPos == null) {
					topLeftColumnBottomPos = new Pos(i, j);
				}
			}
		}

		for (int i = 0; i < M; i++) {
			Pos eastPos = new Pos(topLeftColumnBottomPos.row + i, topLeftColumnBottomPos.col - 1);
			if (floorMatrix[eastPos.row][eastPos.col] == 0) {
				floorStartPos = eastPos;
				columnDestPos = new Pos(M + i, 0);
			}

			Pos westPos = new Pos(topLeftColumnBottomPos.row + i, topLeftColumnBottomPos.col + M);
			if (floorMatrix[westPos.row][westPos.col] == 0) {
				floorStartPos = westPos;
				columnDestPos = new Pos(M + i, 3 * M - 1);
			}

			Pos southPos = new Pos(topLeftColumnBottomPos.row + M, topLeftColumnBottomPos.col + i);
			if (floorMatrix[southPos.row][southPos.col] == 0) {
				floorStartPos = southPos;
				columnDestPos = new Pos(3 * M - 1, M + i);
			}

			Pos northPos = new Pos(topLeftColumnBottomPos.row - 1, topLeftColumnBottomPos.col + i);
			if (floorMatrix[northPos.row][northPos.col] == 0) {
				floorStartPos = northPos;
				columnDestPos = new Pos(0, M + i);
			}
		}



		columnMatrix = new int[3 * M][3 * M];

		int[][] eastMatrix = new int[M][M];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				eastMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		eastMatrix = rotateNTimes(eastMatrix, 3);
		applyToColumnMatrix(new Pos(M, 2 * M), eastMatrix);


		int[][] westMatrix = new int[M][M];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				westMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		westMatrix = rotateNTimes(westMatrix, 1);
		applyToColumnMatrix(new Pos(M, 0), westMatrix);


		int[][] southMatrix = new int[M][M];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				southMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		southMatrix = rotateNTimes(southMatrix, 0);
		applyToColumnMatrix(new Pos(2 * M, M), southMatrix);


		int[][] northMatrix = new int[M][M];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				northMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		northMatrix = rotateNTimes(northMatrix, 2);
		applyToColumnMatrix(new Pos(0, M), northMatrix);

		int[][] centerMatrix = new int[M][M];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				centerMatrix[i][j] = Integer.parseInt(st.nextToken());

				if (centerMatrix[i][j] == 2) {
					columnStartPos = new Pos(M + i, M + j);
					centerMatrix[i][j] = 0;
				}
			}
		}
		centerMatrix = rotateNTimes(centerMatrix, 0);
		applyToColumnMatrix(new Pos(M, M), centerMatrix);


		fireList = new ArrayList<>();
		for (int i = 0; i < F; i++) {
			st = new StringTokenizer(br.readLine());

			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int directionIndex = Integer.parseInt(st.nextToken());
			int cycle = Integer.parseInt(st.nextToken());

			fireList.add(new Fire(new Pos(r, c), cycle, cycle, directionIndex));
			floorMatrix[r][c] = 2;
		}
	}
}