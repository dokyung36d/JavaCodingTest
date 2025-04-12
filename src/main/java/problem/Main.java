package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int K, M;
	static int[][] mainMatrix;
	static Deque<Integer> treasureQueue;
	static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
	public static class Pos implements Comparable<Pos> {
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
			if (this.row < 0 || this.row >= 5 || this.col < 0 || this.col >= 5) {
				return false;
			}
			
			return true;
		}
		
		
		@Override
		public int compareTo(Pos anotherPos) {
			if (this.col != anotherPos.col) {
				return Integer.compare(this.col, anotherPos.col);
			}
			
			return Integer.compare(-this.row, -anotherPos.row);
		}
	}
	
	public static void main(String[] args) throws Exception {
		init();
		solution();
		
	}
	
	public static class Node implements Comparable<Node>{
		PriorityQueue<Pos> pq;
		int numRotated;
		Pos topLeftPos;
		int[][] partMatrix;
		
		public Node(PriorityQueue<Pos> pq, int numRotated, Pos topLeftPos, int[][] partMatrix) {
			this.pq = pq;
			this.numRotated = numRotated;
			this.topLeftPos = topLeftPos;
			this.partMatrix = partMatrix;
		}
		
		@Override
		public int compareTo(Node anotherNode) {
			if (this.pq.size() != anotherNode.pq.size()) {
				return Integer.compare(-this.pq.size(), -anotherNode.pq.size());
			}
			
			if (this.numRotated != anotherNode.numRotated) {
				return Integer.compare(this.numRotated, anotherNode.numRotated);
			}
			
			if (this.topLeftPos.col != anotherNode.topLeftPos.col) {
				return Integer.compare(this.topLeftPos.col, anotherNode.topLeftPos.col);
			}
			
			return Integer.compare(this.topLeftPos.row, this.topLeftPos.row);
		}
	}
	
	public static void solution() {
		int[][] partMatrix = getPartMatrix(new Pos(1, 1));
		int[][] rotatedPartMatrix = rotateNtimes(partMatrix, 1);
		int[][] appliedMainMatrix = getAppliedPartMatrixToMainMatrix(new Pos(1, 1),
				rotatedPartMatrix);
		
		PriorityQueue<Pos> pq = bfs(appliedMainMatrix);
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < K; i++) {
			int answer = 0;
			
			Node bestNode = getBestNode();
			if (bestNode.pq.size() == 0) { break; }
			answer += bestNode.pq.size();
			
			mainMatrix = getAppliedPartMatrixToMainMatrix(bestNode.topLeftPos, bestNode.partMatrix);
			fillTreasure(bestNode.pq);
			while (true) {
				pq = bfs(mainMatrix);
				if (pq.size() == 0) { break; }
				
				answer += pq.size();
				fillTreasure(pq);
			}
			
			sb.append(answer + " ");
		}
		
		System.out.println(sb.toString().substring(0, sb.length() - 1));
	}
	
	public static void fillTreasure(PriorityQueue<Pos> pq) {
		while (!pq.isEmpty()) {
			Pos pos = pq.poll();
			int treasureValue = treasureQueue.pollFirst();
			
			mainMatrix[pos.row][pos.col] = treasureValue;
		}
	}

	
	public static Node getBestNode() {
		Node bestNode = new Node(new PriorityQueue<>(), 4, new Pos(5, 5), new int[3][3]);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				Pos topLeftPos = new Pos(i, j);
				int[][] partMatrix = getPartMatrix(topLeftPos);
				
				for (int numRotate = 1; numRotate <= 3; numRotate++) {
					int[][] rotatedMatrix = rotateNtimes(partMatrix, numRotate);
					int[][] appliedMatrix = getAppliedPartMatrixToMainMatrix(topLeftPos,
							rotatedMatrix);
					
					PriorityQueue<Pos> pq = bfs(appliedMatrix);
					Node node = new Node(pq, numRotate, topLeftPos, rotatedMatrix);
					if (node.compareTo(bestNode) < 0) {
						bestNode = node;
					}
				}
			}
		}
		
		return bestNode;
	}
	
	public static PriorityQueue<Pos> bfs(int[][] matrix) {
		PriorityQueue<Pos> pq = new PriorityQueue<>();
		int[][] visited = new int[5][5];
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (visited[i][j] == 1) { continue; }
				
				List<Pos> areaPosList = new ArrayList<>();
				Deque<Pos> queue = new ArrayDeque<>();
				queue.add(new Pos(i, j));
				while (!queue.isEmpty()) {
					Pos curPos = queue.pollFirst();
					if (visited[curPos.row][curPos.col] == 1) { continue; }
					visited[curPos.row][curPos.col] = 1;
					areaPosList.add(curPos);
					
					for (Pos direction : directions) {
						Pos movedPos = curPos.addPos(direction);
						if (!movedPos.isValidIndex()) { continue; }
						if (visited[movedPos.row][movedPos.col] == 1) { continue; }
						if (matrix[curPos.row][curPos.col] !=
								matrix[movedPos.row][movedPos.col]) { continue; }
						
						queue.add(movedPos);
					}
				}
				
				if (areaPosList.size() >= 3) {
					pq.addAll(areaPosList);
				}
			}
		}
		
		return pq;
	}
	
	public static int[][] getAppliedPartMatrixToMainMatrix(Pos topLeftPos, int[][] partMatrix) {
		int[][] copiedMainMatrix = copyMatrix(mainMatrix);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				copiedMainMatrix[topLeftPos.row + i][topLeftPos.col + j] = partMatrix[i][j];
			}
		}
		
		return copiedMainMatrix;
	}
	
	public static int[][] copyMatrix(int[][] matrix) {
		int size = matrix.length;
		int[][] copiedMatrix = new int[size][size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				copiedMatrix[i][j] = matrix[i][j];
			}
		}
		
		return copiedMatrix;
	}
	
	
	public static int[][] getPartMatrix(Pos topLeftPos) {
		int[][] partMatrix = new int[3][3];
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				partMatrix[i][j] = mainMatrix[topLeftPos.row + i][topLeftPos.col + j];
			}
		}
		
		return partMatrix;
	}
	
	public static int[][] rotateNtimes(int[][] matrix, int numRotate) {
		if (numRotate == 0) {
			return matrix;
		}
		
		for (int i = 0; i < numRotate; i++) {
			matrix = rotateMatrix(matrix);
		}
		
		return matrix;
	}
	
	public static int[][] rotateMatrix(int[][] matrix) {
		int size = matrix.length;
		int[][] rotatedMatrix = new int[size][size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				rotatedMatrix[j][size - i - 1] = matrix[i][j];
			}
		}
		
		return rotatedMatrix;
	}
	
	public static void init() throws IOException {
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		K = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		
		mainMatrix = new int[5][5];
		for (int i = 0; i < 5; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < 5; j++) {
				mainMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		
		treasureQueue = new ArrayDeque<>();
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			treasureQueue.addLast(Integer.parseInt(st.nextToken()));
		}
		
	}
}