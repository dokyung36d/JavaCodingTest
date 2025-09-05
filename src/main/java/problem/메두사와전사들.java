package problem;

import java.util.*;
import java.io.*;

public class 메두사와전사들 {
	static int N, M;
	static int numWarriorMoved, totalNumWarriorRocked, numWarriorAttacked;
	static Pos medusaHomePos, parkPos;
	static List<Pos> warriorPosList;
	static int[][] mainMatrix;
	static Map<Pos, Integer> warriorPosMap;
	static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, - 1)};
	static Pos[] sideDirections = {new Pos(-1, 1), new Pos(1, 1), new Pos(1, -1), new Pos(-1, - 1)};
	static Pos[] seeDirections = {directions[0], directions[2], directions[3], directions[1]};
	static Pos[][] seeSideDirections = {{sideDirections[0], sideDirections[3]}, {sideDirections[1], sideDirections[2]},
			{sideDirections[2], sideDirections[3]}, {sideDirections[0], sideDirections[1]}};
	static Pos[] firstDirections = {directions[0], directions[2], directions[3], directions[1]};
	static Pos[] secondDirections = {directions[3], directions[1], directions[0], directions[2]};

	public static class BFSNode {
		Pos curPos;
		List<Pos> path;

		public BFSNode(Pos curPos, List<Pos> path) {
			this.curPos = curPos;
			this.path = path;
		}
	}

	public static class SeeNode {
		Pos curPos;
		List<Pos> directions;

		public SeeNode(Pos curPos, List<Pos> directions) {
			this.curPos = curPos;
			this.directions = directions;
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
			if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) { return false; }

			return true;
		}

		public boolean isEdge() {
			if (this.row == 0 || this.row == N - 1 || this.col == 0 || this.col == N - 1) { return true; }

			return false;
		}

		public int calcDistance(Pos anotherPos) {
			return Math.abs(this.row - anotherPos.row) + Math.abs(this.col - anotherPos.col);
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
		List<Pos> path = getShortestPath();
		if (path == null) {
			System.out.println(-1);
			return;
		}


		for (Pos medusaPos : path) {
			if (medusaPos.equals(parkPos)) {
				System.out.println(0);
				return;
			}

			numWarriorMoved = 0;
			totalNumWarriorRocked = 0;
			numWarriorAttacked = 0;

			removeWarriorsInMedusaPos(medusaPos);
			updateWarriorPosMap();

			int[][] seeMatrix = getBestSeeMatrix(medusaPos);
			totalNumWarriorRocked = getNumWarriorRocked(seeMatrix);
			warriorsMove(medusaPos, firstDirections, seeMatrix);
			warriorsMove(medusaPos, secondDirections, seeMatrix);

			updateWarriorPosMap();
			System.out.println(numWarriorMoved + " " + totalNumWarriorRocked + " " + numWarriorAttacked);
		}
		System.out.println("hello world");
	}

	public static void removeWarriorsInMedusaPos(Pos medusaPos) {
		List<Pos> updatedWarriorPosList = new ArrayList<>();
		for (Pos warriorPos : warriorPosList) {
			if (warriorPos.equals(medusaPos)) { continue; }

			updatedWarriorPosList.add(warriorPos);
		}

		warriorPosList = updatedWarriorPosList;
	}

	public static void updateWarriorPosMap() {
		warriorPosMap = new HashMap<>();
		for (Pos warriorPos : warriorPosList) {
			warriorPosMap.put(warriorPos, 1);
		}
	}

	public static void warriorsMove(Pos medusaPos, Pos[] seeOrders, int[][] seeMatrix) {
		List<Pos> updatedWarriorPosList = new ArrayList<>();
		for (Pos warriorPos : warriorPosList) {
			int prevDistance = medusaPos.calcDistance(warriorPos);
			if (seeMatrix[warriorPos.row][warriorPos.col] == 1) {
				updatedWarriorPosList.add(warriorPos);
				continue;
			}


			int flag = 0;
			for (Pos direction : seeOrders) {
				Pos movedPos = warriorPos.addPos(direction);
				if (!movedPos.isValidIndex()) { continue; }
				if (seeMatrix[movedPos.row][movedPos.col] == 1) { continue; }
				int distance = movedPos.calcDistance(medusaPos);
				if (distance >= prevDistance) { continue; }

				numWarriorMoved += 1;
				flag = 1;
				if (movedPos.equals(medusaPos)) {
					numWarriorAttacked += 1;
					break;
				}

				updatedWarriorPosList.add(movedPos);
				break;
			}

			if (flag == 0) {
				updatedWarriorPosList.add(warriorPos);
			}

		}

		warriorPosList = updatedWarriorPosList;
	}

	public static int[][] getBestSeeMatrix(Pos medusaPos) {
		int[][] bestSeeMatrix = medusaSee(medusaPos, 0);
		int maxNumRocked = getNumWarriorRocked(bestSeeMatrix);

		for (int i = 1; i < 4; i++) {
			int[][] seeMatrix = medusaSee(medusaPos, i);
			int numRocked = getNumWarriorRocked(seeMatrix);

			if (numRocked > maxNumRocked) {
				maxNumRocked = numRocked;
				bestSeeMatrix = seeMatrix;
			}
		}


		return bestSeeMatrix;
	}

	public static int getNumWarriorRocked(int[][] seeMatrix) {
		int numRocked = 0;

		for (Pos warriorPos : warriorPosList) {
			if (seeMatrix[warriorPos.row][warriorPos.col] == 1) {
				numRocked += 1;
			}
		}

		return numRocked;
	}

	public static int[][] medusaSee(Pos medusaPos, int directionIndex) {
		int[][] seeMatrix = new int[N][N];
		//0, 1, 2로 구별
		// 0 : 태초 상태, 1 : 메두사 시선 범위, 2 : 전사한테 가려진 곳.

		Deque<SeeNode> queue = new ArrayDeque<>();

		//시작점을 메두사가 아니라 한칸 떨어진 곳에서 시작해야 함.
		Pos mainDirection = seeDirections[directionIndex];
		List<Pos> mainDirections = new ArrayList<>();
		mainDirections.add(mainDirection);
		Pos mainStartPos = medusaPos.addPos(mainDirection);
		if (mainStartPos.isValidIndex()) {
			queue.add(new SeeNode(medusaPos.addPos(mainDirection), mainDirections));
			seeMatrix[mainStartPos.row][mainStartPos.col] = 1;
		}

		Pos side1Direction = seeSideDirections[directionIndex][0];
		List<Pos> side1Directions = new ArrayList<>();
		side1Directions.add(mainDirection);
		side1Directions.add(side1Direction);
		Pos side1StartPos = medusaPos.addPos(side1Direction);
		if (side1StartPos.isValidIndex()) {
			queue.add(new SeeNode(medusaPos.addPos(side1Direction), side1Directions));
			seeMatrix[side1StartPos.row][side1StartPos.col] = 1;
		}

		Pos side2Direction = seeSideDirections[directionIndex][1];
		List<Pos> side2Directions = new ArrayList<>();
		side2Directions.add(mainDirection);
		side2Directions.add(side2Direction);
		Pos side2StartPos = medusaPos.addPos(side2Direction);
		if (side2StartPos.isValidIndex()) {
			queue.add(new SeeNode(medusaPos.addPos(side2Direction), side2Directions));
			seeMatrix[side2StartPos.row][side2StartPos.col] = 1;
		}


		int[][] visited = new int[N][N];
		while (!queue.isEmpty()) {
			SeeNode seeNode = queue.pollFirst();
			if (visited[seeNode.curPos.row][seeNode.curPos.col] == 1) { continue; }
			visited[seeNode.curPos.row][seeNode.curPos.col] = 1;

			int status;
			if (seeMatrix[seeNode.curPos.row][seeNode.curPos.col] == 2 || warriorPosMap.get(seeNode.curPos) != null) {
				status = 2;
			}
			else {
				status = 1;
			}


			for (Pos direction : seeNode.directions) {
				Pos movedPos = seeNode.curPos.addPos(direction);
				if (!movedPos.isValidIndex()) { continue; }
				if (seeMatrix[movedPos.row][movedPos.col] == 2) { continue; }

				seeMatrix[movedPos.row][movedPos.col] = status;

				if (visited[movedPos.row][movedPos.col] == 1) { continue; }
				queue.addLast(new SeeNode(movedPos, seeNode.directions));
			}
		}

		return seeMatrix;
	}

	public static List<Pos> getShortestPath() {
		Deque<BFSNode> queue = new ArrayDeque<>();

		queue.add(new BFSNode(medusaHomePos, new ArrayList<>()));
		int[][] visited = new int[N][N];

		while (!queue.isEmpty()) {
			BFSNode bfsNode = queue.pollFirst();

			if (bfsNode.curPos.equals(parkPos)) {
				return bfsNode.path;
			}

			if (visited[bfsNode.curPos.row][bfsNode.curPos.col] == 1) { continue; }
			visited[bfsNode.curPos.row][bfsNode.curPos.col] = 1;

			for (Pos direction : seeDirections) {
				Pos movedPos = bfsNode.curPos.addPos(direction);
				if (!movedPos.isValidIndex()) { continue; }
				if (visited[movedPos.row][movedPos.col] == 1) { continue; }
				if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

				List<Pos> updatedPath = new ArrayList<>(bfsNode.path);
				updatedPath.add(movedPos);
				queue.add(new BFSNode(movedPos, updatedPath));
			}
		}

		return null;
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		int medusaHomeRow = Integer.parseInt(st.nextToken());
		int medusaHomeCol = Integer.parseInt(st.nextToken());
		medusaHomePos = new Pos(medusaHomeRow, medusaHomeCol);

		int parkPosRow = Integer.parseInt(st.nextToken());
		int parkPosCol = Integer.parseInt(st.nextToken());
		parkPos = new Pos(parkPosRow, parkPosCol);


		warriorPosList = new ArrayList<>();
		warriorPosMap = new HashMap<>();
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			int warriorRow = Integer.parseInt(st.nextToken());
			int warriorCol = Integer.parseInt(st.nextToken());

			Pos warriorPos = new Pos(warriorRow, warriorCol);
			warriorPosMap.put(warriorPos, 1);
			warriorPosList.add(warriorPos);
		}

		mainMatrix = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());

			for (int j = 0; j < N; j++) {
				mainMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
	}

}