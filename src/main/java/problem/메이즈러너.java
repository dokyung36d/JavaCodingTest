package problem;

import java.util.*;
import java.io.*;

public class 메이즈러너 {
	static int N, M, K;
	static int[][] mainMatrix;
	static List<Pos> runnerPosList;
	static Pos exitPos;
	static Pos[] directions = {new Pos(-1, 0), new Pos(1, 0), new Pos(0, 1), new Pos(0, -1)};
	static int numTotalMove;


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

		public Pos minusPos(Pos direction) {
			return new Pos(this.row - direction.row, this.col - direction.col);
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

		public int calcDistance(Pos anotherPos) {
			return Math.abs(this.row - anotherPos.row) + Math.abs(this.col - anotherPos.col);
		}

		public boolean isInBox(Box box) {
			if (box.topLeftPos.row <= this.row &&
					this.row < box.topLeftPos.row + box.size &&
					box.topLeftPos.col <= this.col &&
					this.col < box.topLeftPos.col + box.size) {
				return true;
			}
			return false;
		}

		public Pos getRotatedPosInBox(Box box) {
			Pos standardPos = this.minusPos(box.topLeftPos);
			Pos rotatedPos = new Pos(standardPos.col, box.size - standardPos.row - 1);

			return rotatedPos.addPos(box.topLeftPos);
		}
	}

	public static class Box {
		Pos topLeftPos;
		int size;

		public Box(Pos topLeftPos, int size) {
			this.topLeftPos = topLeftPos;
			this.size = size;
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {

		for (int i = 0; i < K; i++) {
			allRunnersMove();

			if (runnerPosList.size() == 0) { break; }

			Box bestBox = getBestBox();
			int[][] partMatrix = getPartMainMatrix(bestBox);
			int[][] rotatedMatrix = rotateMatrix(partMatrix);
			applyToMainMatrix(bestBox, rotatedMatrix);
			rotateAllPos(bestBox);

//			System.out.println("hello world");	
		}


		StringBuilder sb = new StringBuilder();
		sb.append(numTotalMove + "\n");
		sb.append((exitPos.row + 1) + " " + (exitPos.col + 1));

		System.out.println(sb.toString());
	}

	public static void rotateAllPos(Box box) {
		exitPos = exitPos.getRotatedPosInBox(box);

		List<Pos> updatedRunnerPosList = new ArrayList<>();
		for (Pos runnerPos : runnerPosList) {
			if (runnerPos.isInBox(box)) {
				updatedRunnerPosList.add(runnerPos.getRotatedPosInBox(box));
			}
			else {
				updatedRunnerPosList.add(runnerPos);
			}
		}

		runnerPosList = updatedRunnerPosList;
	}

	public static int[][] rotateMatrix(int[][] matrix) {
		int[][] rotatedMatrix = new int[matrix.length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				int value = matrix[i][j];
				if (value != 0) {
					value -= 1;
				}
				rotatedMatrix[j][matrix.length - i - 1] = value;
			}
		}

		return rotatedMatrix;
	}

	public static int[][] getPartMainMatrix(Box box) {
		int[][] partMatrix = new int[box.size][box.size];

		for (int i = 0; i < box.size; i++) {
			for (int j = 0; j < box.size; j++) {
				partMatrix[i][j] = mainMatrix[box.topLeftPos.row + i][box.topLeftPos.col + j];
			}
		}

		return partMatrix;
	}

	public static void applyToMainMatrix(Box box, int[][] matrix) {
		for (int i = 0; i < box.size; i++) {
			for (int j = 0; j < box.size; j++) {
				mainMatrix[box.topLeftPos.row + i][box.topLeftPos.col + j] = matrix[i][j];
			}
		}
	}

	public static Box getBestBox() {
		for (int size = 1; size <= N; size++) {
			for (int row = 0; row + size <= N; row++) {
				for (int col = 0; col + size <= N; col++) {
					Box box = new Box(new Pos(row, col), size);
					if (isBoxFit(box)) { return box; }
				}
			}
		}

		return null;
	}

	public static boolean isBoxFit(Box box) {
		if (!exitPos.isInBox(box)) {
			return false;
		}

		for (Pos runnerPos : runnerPosList) {
			if (runnerPos.isInBox(box)) {
				return true;
			}
		}

		return false;
	}

	public static void allRunnersMove() {
		List<Pos> updatedRunnerPosList = new ArrayList<>();
		for (Pos runnerPos : runnerPosList) {
			int prevDistance = runnerPos.calcDistance(exitPos);
			int flag = 0;

			for (Pos direction : directions) {
				Pos movedPos = runnerPos.addPos(direction);
				if (!movedPos.isValidIndex()) { continue; }
				if (mainMatrix[movedPos.row][movedPos.col] != 0) { continue; }

				int distance = movedPos.calcDistance(exitPos);
				if (distance >= prevDistance) { continue; }

				numTotalMove += 1;
				flag = 1;
				if (movedPos.equals(exitPos)) {
					break;
				}
				updatedRunnerPosList.add(movedPos);
				break;
			}

			if (flag == 0) {
				updatedRunnerPosList.add(runnerPos);
			}
		}

		runnerPosList = updatedRunnerPosList;
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());


		mainMatrix = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				mainMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}


		runnerPosList = new ArrayList<>();
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());

			int row = Integer.parseInt(st.nextToken()) - 1;
			int col = Integer.parseInt(st.nextToken()) - 1;

			runnerPosList.add(new Pos(row, col));
		}


		st = new StringTokenizer(br.readLine());
		int row = Integer.parseInt(st.nextToken()) - 1;
		int col = Integer.parseInt(st.nextToken()) - 1;

		exitPos = new Pos(row, col);


		numTotalMove = 0;
	}
}