package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static Pos homePos, parkPos;
	static List<Pos> warriorPosList;
	static Map<Pos, Integer> warriorPosMap;
	static int[][] floorMatrix;
	static Pos[] medusaDirections = {new Pos(-1, 0), new Pos(1, 0), new Pos(0, -1), new Pos(0, 1)};
	static int numWarriorMoved, numWarriorRockedByMedusa, numWarriorAttackedMedusa;
	static Pos[] firstDirections = {new Pos(-1, 0), new Pos(1, 0), new Pos(0, -1), new Pos(0, 1)};
	static Pos[] secondDirections = {new Pos(0, - 1), new Pos(0, 1), new Pos(-1, 0), new Pos(1, 0)};
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
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
	
	public static class Node {
		Pos curPos;
		List<Pos> path;
		
		public Node(Pos curPos, List<Pos> path) {
			this.curPos = curPos;
			this.path = path;
		}
	}
	
	public static class SeeNode implements Comparable<SeeNode> {
		int[][] seeMatrix;
		int numRockedWarrior;
		
		public SeeNode(int[][] seeMatrix, int numRockedWarrior) {
			this.seeMatrix = seeMatrix;
			this.numRockedWarrior = numRockedWarrior;
		}
		
		@Override
		public int compareTo(SeeNode anotherSeeNode) {
			return Integer.compare(this.numRockedWarrior, anotherSeeNode.numRockedWarrior);
		}
	}
	
	public static void main(String[] args) throws Exception {
		init();
		solution();
		
	}
	
	public static void solution() {
		List<Pos> path = getMedusaPath();
		if (path.size() == 0) {
			System.out.println(-1);
			return;
		}
		for (int i = 0; i < path.size() - 1; i++) {
			numWarriorMoved = 0;
			numWarriorRockedByMedusa = 0;
			numWarriorAttackedMedusa = 0;
			
			updateWarriorPosMap();
			SeeNode seeNode = getBestSeeNode(path.get(i));
			moveWarriors(firstDirections, path.get(i), seeNode.seeMatrix);
			moveWarriors(secondDirections, path.get(i), seeNode.seeMatrix);
			
			StringBuilder sb = new StringBuilder();
			sb.append(numWarriorMoved + " ");
			sb.append(seeNode.numRockedWarrior + " ");
			sb.append(numWarriorAttackedMedusa);
			
			System.out.println(sb.toString());
		}
	
		System.out.println(0);
	}
	
	public static void moveWarriors(Pos[] directions, Pos medusaPos, int[][] seeMatrix) {
		List<Pos> updatedUserPosList = new ArrayList<>();
		for (Pos warriorPos : warriorPosList) {
			if (warriorPos.equals(medusaPos)) {
				continue;
			}
			
			if (seeMatrix[warriorPos.row][warriorPos.col] == 1) {
				updatedUserPosList.add(warriorPos);
				continue;
			}
			
			int flag = 0;
			int prevDistance = warriorPos.calcDistance(medusaPos);
			for (Pos direction : directions) {
				Pos movedPos = warriorPos.addPos(direction);
				if (!movedPos.isValidIndex() || seeMatrix[movedPos.row][movedPos.col] == 1) { continue; }
				int distance = movedPos.calcDistance(medusaPos);
				if (distance >= prevDistance) { continue; }
				
				numWarriorMoved += 1;
				flag = 1;
				if (movedPos.equals(medusaPos)) {
					numWarriorAttackedMedusa += 1;
					break;
				}
				
				updatedUserPosList.add(movedPos);
				break;
			}
			
			if (flag == 0) {
				updatedUserPosList.add(warriorPos);
			}
		}
		
		warriorPosList = updatedUserPosList;
	}
 	
	public static SeeNode getBestSeeNode(Pos medusaPos) {
		SeeNode bestSeeNode = seeUp(medusaPos);
		
		SeeNode seeDownNode = seeDown(medusaPos);
		if (seeDownNode.numRockedWarrior > bestSeeNode.numRockedWarrior) {
			bestSeeNode = seeDownNode;
		}
		
		SeeNode seeLeftNode = seeLeft(medusaPos);
		if (seeLeftNode.numRockedWarrior > bestSeeNode.numRockedWarrior) {
			bestSeeNode = seeLeftNode;
		}
		
		SeeNode seeRightNode = seeRight(medusaPos);
		if (seeRightNode.numRockedWarrior > bestSeeNode.numRockedWarrior) {
			bestSeeNode = seeRightNode;
		}
		return bestSeeNode;
	}
	
	public static SeeNode seeRight(Pos medusaPos) {
		int[][] seeMatrix = new int[N][N];
		for (int j = 1; medusaPos.col + j < N; j++) {
			int upRow = Math.max(0, medusaPos.row - j);
			int downRow = Math.min(N - 1, medusaPos.row + j);
			
			for (int i = upRow; i <= downRow; i++) {
				Pos curPos = new Pos(i, medusaPos.col + j);
				seeMatrix[curPos.row][curPos.col] = 1;
			}
		}
		
		
		//Up
		for (int j = 1; medusaPos.col + j < N - 1; j++) {
			int upRow = Math.max(0,  medusaPos.row - j);
			for (int i = upRow; i < medusaPos.row; i++) {
				Pos curPos = new Pos(i, medusaPos.col + j);
				if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
					seeMatrix[curPos.row][curPos.col + 1] = 0;
					if (curPos.row >= 1) {
						seeMatrix[curPos.row - 1][curPos.col + 1] = 0;
					}
				}
			}
		}
		
		
		//Center
		for (int j = 1; medusaPos.col + j < N - 1; j++) {
			Pos curPos = new Pos(medusaPos.row, medusaPos.col + j);
			if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
				seeMatrix[curPos.row][curPos.col + 1] = 0;
			}
		}
		
		
		//Down
		for (int j = 1; medusaPos.col + j < N - 1; j++) {
			int downRow = Math.min(N - 1, medusaPos.row + j);
			for (int i = medusaPos.row + 1; i <= downRow; i++) {
				Pos curPos = new Pos(i, medusaPos.col + j);
				if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
					seeMatrix[curPos.row][curPos.col + 1] = 0;
					if (curPos.row < N - 1) {
						seeMatrix[curPos.row + 1][curPos.col + 1] = 0;
					}
				}
			}
		}
		
		int numRockedWarrior = countRockedWarrior(seeMatrix);
		return new SeeNode(seeMatrix, numRockedWarrior);
	}
	
	public static SeeNode seeLeft(Pos medusaPos) {
		int[][] seeMatrix = new int[N][N];
		for (int j = 1; medusaPos.col - j >= 0; j++) {
			int upRow = Math.max(0, medusaPos.row - j);
			int downRow = Math.min(N - 1, medusaPos.row + j);
			
			for (int i = upRow; i <= downRow; i++) {
				Pos curPos = new Pos(i, medusaPos.col - j);
				seeMatrix[curPos.row][curPos.col] = 1;
			}
		}
		
		
		//Up
		for (int j = 1; medusaPos.col - j >= 1; j++) {
			int upRow = Math.max(0, medusaPos.row - j);
			for (int i = upRow; i <= medusaPos.row - 1; i++) {
				Pos curPos = new Pos(i, medusaPos.col - j);
				if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
					seeMatrix[curPos.row][curPos.col - 1] = 0;
					if (curPos.row >= 1) {
						seeMatrix[curPos.row - 1][curPos.col - 1] = 0;
					}
				}
			}
		}
		
		//Center
		for (int j = 1; medusaPos.col - j >= 1; j++) {
			Pos curPos = new Pos(medusaPos.row, medusaPos.col - j);
			if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
				seeMatrix[curPos.row][curPos.col - 1] = 0;
			}
		}
		
		//Down
		for (int j = 1; medusaPos.col - j >= 1; j++) {
			int downRow = Math.min(N - 1, medusaPos.row + j);
			for (int i = medusaPos.row + 1; i <= downRow; i++) {
				Pos curPos = new Pos(i, medusaPos.col - j);
				if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
					seeMatrix[curPos.row][curPos.col - 1] = 0;
					if (curPos.row < N - 1) {
						seeMatrix[curPos.row + 1][curPos.col - 1] = 0;
					}
				}
			}
		}
		
		int numRockedWarrior = countRockedWarrior(seeMatrix);
		return new SeeNode(seeMatrix, numRockedWarrior);
	}
	
	public static SeeNode seeUp(Pos medusaPos) {
		int[][] seeMatrix = new int[N][N];
		for (int i = 1; medusaPos.row - i >= 0; i++) {
			int leftCol = Math.max(0, medusaPos.col - i);
			int rightCol = Math.min(N - 1, medusaPos.col + i);
			
			for (int j = leftCol; j <= rightCol; j++) {
				seeMatrix[medusaPos.row - i][j] = 1;
			}
		}
		
		//Left
		for (int i = 1; medusaPos.row - i >= 1; i++) {
			int leftCol = Math.max(0, medusaPos.col - i);
			for (int j = leftCol; j < medusaPos.col; j++) {
				Pos curPos = new Pos(medusaPos.row - i, j);
				if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
					seeMatrix[curPos.row - 1][curPos.col] = 0;
					if (curPos.col >= 1) {
						seeMatrix[curPos.row - 1][curPos.col - 1] = 0;
					}
				}
			}
		}
		
		
		//Center
		for (int i = 1; medusaPos.row - i >= 1; i++) {
			Pos curPos = new Pos(medusaPos.row - i, medusaPos.col);
			if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
				seeMatrix[curPos.row - 1][curPos.col] = 0;
			}
		}
		
		//Right
		for (int i = 1; medusaPos.row - i >= 1; i++) {
			int rightCol = Math.min(N - 1, medusaPos.col + i);
			for (int j = medusaPos.col + 1; j <= rightCol; j++) {
				Pos curPos = new Pos(medusaPos.row - i, j);
				if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
					seeMatrix[curPos.row - 1][curPos.col] = 0;
					if (curPos.col < N - 1) {
						seeMatrix[curPos.row - 1][curPos.col + 1] = 0;
					}
				}
				
			}
		}
		
		int numRockedWarrior = countRockedWarrior(seeMatrix);
		return new SeeNode(seeMatrix, numRockedWarrior);
	}

	
	public static SeeNode seeDown(Pos medusaPos) {
		int[][] seeMatrix = new int[N][N];
		for (int i = 1; i + medusaPos.row < N; i++) {
			int leftCol = Math.max(0, medusaPos.col - i);
			int rightCol = Math.min(N - 1, medusaPos.col + i);
			for (int j = leftCol; j <= rightCol; j++) {
				seeMatrix[medusaPos.row + i][j] = 1;
			}
		}
		
		
		//Left
		for (int i = 1; i + medusaPos.row < N - 1; i++) {
			int leftCol = Math.max(0, medusaPos.col - i);
			for (int j = leftCol; j < medusaPos.col; j++) {
				Pos curPos = new Pos(i + medusaPos.row, j);
				if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
					seeMatrix[curPos.row + 1][curPos.col] = 0;
					if (curPos.col >= 1) {
						seeMatrix[curPos.row + 1][curPos.col - 1] = 0;
					}
				}
			}
		}
		
		
		//Center
		for (int i = 1; i + medusaPos.row < N - 1; i++) {
			Pos curPos = new Pos(i + medusaPos.row, medusaPos.col);
			if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
				seeMatrix[curPos.row + 1][curPos.col] = 0;
			}
		}
		
		
		//Right
		for (int i = 1; i + medusaPos.row < N - 1; i++) {
			int rightCol = Math.min(N - 1, medusaPos.col + i);
			for (int j = medusaPos.col + 1; j <= rightCol; j++) {
				Pos curPos = new Pos(i + medusaPos.row, j);
				if (seeMatrix[curPos.row][curPos.col] == 0 || warriorPosMap.get(curPos) != null) {
					seeMatrix[curPos.row + 1][curPos.col] = 0;
					if (curPos.col < N - 1) {
						seeMatrix[curPos.row + 1][curPos.col + 1] = 0;
					}
				}
			}
		}
		
		int numRockedWarrior = countRockedWarrior(seeMatrix);
		return new SeeNode(seeMatrix, numRockedWarrior);
	}
	
	public static int countRockedWarrior(int[][] seeMatrix) {
		int numRockedWarrior = 0;
		for (Pos warriorPos : warriorPosList) {
			if (seeMatrix[warriorPos.row][warriorPos.col] == 1) {
				numRockedWarrior += 1;
			}
		}
		
		return numRockedWarrior;
	}
	
	public static void updateWarriorPosMap() {
		Map<Pos, Integer> updatedWarriorPosMap = new HashMap<>();
		
		for (Pos warriorPos : warriorPosList) {
			updatedWarriorPosMap.put(warriorPos, 1);
		}
		
		warriorPosMap = updatedWarriorPosMap;
	}
	
	public static List<Pos> getMedusaPath() {
		int[][] visited = new int[N][N];
		
		Deque<Node> queue = new ArrayDeque<>();
		queue.add(new Node(homePos, new ArrayList<>()));
		
		while (!queue.isEmpty()) {
			Node node = queue.pollFirst();
			if (visited[node.curPos.row][node.curPos.col] == 1) { continue; }
			visited[node.curPos.row][node.curPos.col] = 1;
			
			if (node.curPos.equals(parkPos)) { return node.path; }
			
			for (Pos direction : medusaDirections) {
				Pos movedPos = node.curPos.addPos(direction);
				if (!movedPos.isValidIndex() || visited[movedPos.row][movedPos.col] == 1) { continue; }
				if (floorMatrix[movedPos.row][movedPos.col] == 1) { continue; }
				
				List<Pos> updatedPath = new ArrayList<>(node.path);
				updatedPath.add(movedPos);
				
				queue.add(new Node(movedPos, updatedPath));
			}
		}
		
		return new ArrayList<>();
	}
	
	public static void init() throws IOException {
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		st = new StringTokenizer(br.readLine());
		int homeRow = Integer.parseInt(st.nextToken());
		int homeCol = Integer.parseInt(st.nextToken());
		homePos = new Pos(homeRow, homeCol);
		
		int parkRow = Integer.parseInt(st.nextToken());
		int parkCol = Integer.parseInt(st.nextToken());
		parkPos = new Pos(parkRow, parkCol);
		
		
		warriorPosList = new ArrayList<>();
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			int warriorRow = Integer.parseInt(st.nextToken());
			int warriorCol = Integer.parseInt(st.nextToken());
			
			warriorPosList.add(new Pos(warriorRow, warriorCol));
		}
		
		
		floorMatrix = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				floorMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
	}
}