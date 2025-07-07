package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N;
	static char[][] mainMatrix;
	static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
	static List<Pos> teacherPosList;

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

		public Pos multiplyPos(int numMultiply) {
			return new Pos(this.row * numMultiply, this.col * numMultiply);
		}

		public boolean isValidIndex() {
			if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
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
		boolean result = recursive(3, mainMatrix);

		if (result == true) {
			System.out.println("YES");
		}
		else {
			System.out.println("NO");
		}
	}

	public static boolean recursive(int numRemainWall, char[][] matrix) {
		if (numRemainWall == 0) {
			if (checkHideFromTeachers(matrix)) {
				return true;
			}

			return false;
		}


		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (matrix[i][j] != 'X') { continue; }

				matrix[i][j] = 'O';
				boolean result = recursive(numRemainWall - 1, matrix);
				matrix[i][j] = 'X';

				if (result == true) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean checkHideFromTeachers(char[][] matrix) {
		for (Pos teacherPos : teacherPosList) {
			if (checkHideFromTeacher(matrix, teacherPos) == false) {
				return false;
			}
		}

		return true;
	}

	public static boolean checkHideFromTeacher(char[][] matrix, Pos teacherPos) {
		for (Pos direction : directions) {
			int multiplyNum = 1;

			while (true) {
				Pos seePos = teacherPos.addPos(direction.multiplyPos(multiplyNum));
				if (!seePos.isValidIndex()) { break; }
				if (matrix[seePos.row][seePos.col] == 'O') { break; }
				if (matrix[seePos.row][seePos.col] == 'T') { break; }

				if (matrix[seePos.row][seePos.col] == 'S') { return false; }

				multiplyNum += 1;
 			}
		}

		return true;
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		mainMatrix = new char[N][N];

		teacherPosList = new ArrayList<>();
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				mainMatrix[i][j] = st.nextToken().charAt(0);

				if (mainMatrix[i][j] == 'T') {
					teacherPosList.add(new Pos(i, j));
				}
			}
		}
	}
}