package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, K;
	static int[][] colorMatrix;
	static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
	static Deque<Horse>[][] mainMatrix;
	static Map<Integer, Horse> horseMap;
	static int answerFlag;

	public static class Horse {
		int uniqueNum;
		int directionIndex;
		Pos curPos;

		public Horse(int uniqueNum, int directionIndex, Pos curPos) {
			this.uniqueNum = uniqueNum;
			this.directionIndex = directionIndex;
			this.curPos = curPos;
		}
	}

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
		for (int i = 0; i < 1000; i++) {
			for (int uniqueNum = 1; uniqueNum <= K; uniqueNum++) {
				move(uniqueNum);

				if (answerFlag == 1) {
					System.out.println(i + 1);
					return;
				}
			}
		}

		System.out.println(-1);

	}

	public static void move(int uniqueNum) {
		Horse horse = horseMap.get(uniqueNum);
		Pos movedPos = horse.curPos.addPos(directions[horse.directionIndex]);
		if (!movedPos.isValidIndex()) {
			blueMove(horse);
			return;
		}

		int movedColor = colorMatrix[movedPos.row][movedPos.col];
		if (movedColor == 0) {
			whiteMove(horse);
		}
		else if (movedColor == 1) {
			redMove(horse);
		}
		else if (movedColor == 2) {
			blueMove(horse);
		}


	}

	public static void whiteMove(Horse horse) {
		Pos movedPos = horse.curPos.addPos(directions[horse.directionIndex]);

		Deque<Horse> updatedCurPosDeque = new ArrayDeque<>();
		int flag = 0;

		Deque<Horse> curPosDeque = mainMatrix[horse.curPos.row][horse.curPos.col];
		while (!curPosDeque.isEmpty()) {
			Horse polledHorse = curPosDeque.pollFirst();
			if (polledHorse.uniqueNum == horse.uniqueNum) {
				flag = 1;
				mainMatrix[horse.curPos.row][horse.curPos.col] = updatedCurPosDeque;
			}

			if (flag == 1) {
				polledHorse.curPos = movedPos;
				mainMatrix[movedPos.row][movedPos.col].addLast(polledHorse);
			}
			else {
				updatedCurPosDeque.addLast(polledHorse);
			}
		}

		if (mainMatrix[movedPos.row][movedPos.col].size() >= 4) {
			answerFlag = 1;
		}
	}

	public static void redMove(Horse horse) {
		Pos movedPos = horse.curPos.addPos(directions[horse.directionIndex]);

		Deque<Horse> updatedCurPosDeque = new ArrayDeque<>();
		int flag = 0;

		Deque<Horse> curPosDeque = mainMatrix[horse.curPos.row][horse.curPos.col];
		Deque<Horse> movedPosNewDeque = new ArrayDeque<>();
		while (!curPosDeque.isEmpty()) {
			Horse polledHorse = curPosDeque.pollFirst();
			if (polledHorse.uniqueNum == horse.uniqueNum) {
				flag = 1;
				mainMatrix[horse.curPos.row][horse.curPos.col] = updatedCurPosDeque;
			}


			if (flag == 1) {
				polledHorse.curPos = movedPos;
				movedPosNewDeque.addFirst(polledHorse);
			}
			else {
				updatedCurPosDeque.addLast(polledHorse);
			}
		}

		int numIteration = movedPosNewDeque.size();
		for (int i = 0; i < numIteration; i++) {
			mainMatrix[movedPos.row][movedPos.col].addLast(movedPosNewDeque.pollFirst());
		}
		if (mainMatrix[movedPos.row][movedPos.col].size() >= 4) {
			answerFlag = 1;
		}
	}

	public static void blueMove(Horse horse) {
		horse.directionIndex = (horse.directionIndex + 2) % 4;
		Pos movedPos = horse.curPos.addPos(directions[horse.directionIndex]);
		if (!movedPos.isValidIndex()) { return; }

		int color = colorMatrix[movedPos.row][movedPos.col];
		if (color == 2) { return; }
		else if (color == 1) { redMove(horse); }
		else { 	whiteMove(horse); }

	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		colorMatrix = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				colorMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		mainMatrix = new Deque[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				mainMatrix[i][j] = new ArrayDeque<>();
			}
		}

		horseMap = new HashMap<>();

		for (int i = 0; i < K; i++) {
			st = new StringTokenizer(br.readLine());

			int row = Integer.parseInt(st.nextToken()) - 1;
			int col = Integer.parseInt(st.nextToken()) - 1;

			int direction = Integer.parseInt(st.nextToken()) - 1;

			if (direction == 0) {
				direction = 1;
			}
			else if (direction == 1) {
				direction = 3;
			}
			else if (direction == 2) {
				direction = 0;
			}
			else {
				direction = 2;
			}

			Horse horse = new Horse(i + 1, direction, new Pos(row, col));
			horseMap.put(horse.uniqueNum, horse);
			mainMatrix[row][col].addLast(horse);
		}

	}
}