package problem;

import java.util.*;
import java.io.*;


public class 미지의공간탈출 {
	static int N, M, F;
	static int[][] floorMatrix, columnMatrix;
	static Pos floorStartPos, floorDestPos;
	static Pos columnStartPos, columnDestPos;
	static List<TimeStrange> timeStrangeList;
	static Pos[] directions = { new Pos(0, 1), new Pos(0, -1), new Pos(1, 0), new Pos(-1, 0)};
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
		
		public Pos moveInColumn(Pos direction) {
			Pos movedPos = this.addPos(direction);
			if (!movedPos.isValidIndex(3 * M)) { return this; }
			
			if (movedPos.isInRectangle(new Pos(0, 0)) || movedPos.isInRectangle(new Pos(2 * M, 2 * M))) {
				return new Pos(this.col, this.row);
			}
			
			if (movedPos.isInRectangle(new Pos(2 * M, 0)) || movedPos.isInRectangle(new Pos(0, 2 * M))) {
				return new Pos(3 * M - 1 - this.col, 3 * M - 1 - this.row);
			}
			
			return movedPos;
			
		}
		
		public boolean isInRectangle(Pos topLeftPos) {
			if (topLeftPos.row <= this.row && this.row < topLeftPos.row + M && 
					topLeftPos.col <= this.col && this.col < topLeftPos.col + M) {
				return true;
			}
			
			return false;
		}
		
		public boolean isValidIndex(int size) {
			if (this.row < 0 || this.row >= size || this.col < 0 || this.col >= size) {
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
	
	public static class TimeStrange {
		Pos curPos;
		int directionIndex;
		int advanceValue;
		int remainAdvanceTime;
		
		public TimeStrange(Pos curPos, int directionIndex, int advanceValue, int remainAdvanceTime) {
			this.curPos = curPos;
			this.directionIndex = directionIndex;
			this.advanceValue = advanceValue;
			this.remainAdvanceTime = remainAdvanceTime;
		}
		
		public TimeStrange advance() {
			if (remainAdvanceTime == 1) {
				Pos movedPos = this.curPos.addPos(directions[directionIndex]);
				return new TimeStrange(movedPos, directionIndex, this.advanceValue, this.advanceValue);
			}
			
			return new TimeStrange(this.curPos, this.directionIndex, this.advanceValue, this.remainAdvanceTime - 1);
			
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
		Pos curPos = new Pos(0, 5);
		Pos movedPos = curPos.moveInColumn(new Pos(0, 1));
		
		int depthInColumn = bfsInColumn();
		timeFlows();
		if (depthInColumn == 0 || floorMatrix[floorStartPos.row][floorStartPos.col] != 0) {
			System.out.println(-1);
			return;
		}
		
		int depthInFloor = bfsInFloor();
		if (depthInFloor == 0) {
			System.out.println(-1);
			return;
		}
		
		
		System.out.println(depthInColumn + 1 + depthInFloor);
	}
	
	public static int bfsInFloor() {
		int[][] visited = new int[N][N];
		Deque<Node> queue = new ArrayDeque<>();
		queue.add(new Node(floorStartPos, 0));
		
		int maxDepth = 0;
		while (!queue.isEmpty()) {
			Node node = queue.pollFirst();
			
			if (visited[node.curPos.row][node.curPos.col] == 1) { continue; }
			visited[node.curPos.row][node.curPos.col] = 1;
			
			if (node.depth > maxDepth) {
				timeFlows();
				maxDepth = node.depth;
			}
			
			if (floorMatrix[node.curPos.row][node.curPos.col] != 0) { continue; }
			
			if (node.curPos.equals(floorDestPos)) {
				return node.depth;
			}
			
			for (Pos direction : directions) {
				Pos movedPos = node.curPos.addPos(direction);
				if (!movedPos.isValidIndex(N)) { continue; }
				if (visited[movedPos.row][movedPos.col] == 1) { continue; }
				if (floorMatrix[movedPos.row][movedPos.col] != 0) { continue; }
				
				queue.add(new Node(movedPos, node.depth + 1));
				
			}
			
		}
		
		return 0;
	}
	
	public static int bfsInColumn() {
		int[][] visited = new int[3 * M][3 * M];
		Deque<Node> queue = new ArrayDeque<>();
		queue.add(new Node(columnStartPos, 0));
		
		int maxDepth = 0;
		while (!queue.isEmpty()) {
			Node node = queue.pollFirst();
			if (visited[node.curPos.row][node.curPos.col] == 1) { continue; }
			visited[node.curPos.row][node.curPos.col] = 1;
			
			if (node.depth > maxDepth) {
				timeFlows();
				maxDepth = node.depth;
			}
			
			if (node.curPos.equals(columnDestPos)) {
				return node.depth;
			}
			
			for (Pos direction : directions) {
				Pos movedPos = node.curPos.moveInColumn(direction);
				if (visited[movedPos.row][movedPos.col] == 1) { continue; }
				if (columnMatrix[movedPos.row][movedPos.col] == 1) { 
					continue;
				}
				
				queue.add(new Node(movedPos, node.depth + 1));
			}
		}
		
		
		return 0;
	}
	
	public static void timeFlows() {
		List<TimeStrange> updatedTimeStrangeList = new ArrayList<>();
		for (TimeStrange timeStrange : timeStrangeList) {
			TimeStrange flowedTimeStrange = timeStrange.advance();
			
			Pos curPos = flowedTimeStrange.curPos;
			if (!curPos.isValidIndex(N)) { continue; }
			if (floorMatrix[curPos.row][curPos.col] == 1) { continue; }
			if (floorMatrix[curPos.row][curPos.col] == 3) { continue; }
			if (curPos.equals(floorDestPos)) { continue; }
			
			floorMatrix[curPos.row][curPos.col] = 2;
			updatedTimeStrangeList.add(flowedTimeStrange);
		}
		
		timeStrangeList = updatedTimeStrangeList;
	}
	
	public static int[][] rotateMatrix(int[][] matrix) {
		int size = matrix.length;
		int[][] rotatedMatrix = new int[size][size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				rotatedMatrix[j][size - 1 - i] = matrix[i][j];
			}
		}
		
		return rotatedMatrix;
	}
	
	public static int[][] rotateNtimes(int rotateNum, int[][] matrix) {
		if (rotateNum == 0) {
			return matrix;
		}
		
		
		for (int i = 0; i < rotateNum; i++) {
			matrix = rotateMatrix(matrix);
		}
		
		return matrix;
	}
	
	public static int[][] applyPartMatrixToMatrix(int[][] matrix, int[][] partMatrix, Pos topLeftPos) {
		int size = partMatrix.length;
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Pos curPos = topLeftPos.addPos(new Pos(i, j));
				matrix[curPos.row][curPos.col] = partMatrix[i][j];
			}
		}
		
		return matrix;
	}
	
	
	public static void init() throws IOException {
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		F = Integer.parseInt(st.nextToken());
		
		floorMatrix = new int[N][N];
		columnMatrix = new int[3 * M][3 * M];
		
		int flag = 0;
		Pos columnTopLeftPos = null;
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < N; j++) {
				floorMatrix[i][j] = Integer.parseInt(st.nextToken());
				
				if (floorMatrix[i][j] == 4) {
					floorDestPos = new Pos(i, j);
					floorMatrix[i][j] = 0;
				}
				if (floorMatrix[i][j] == 3 && flag == 0) {
					columnTopLeftPos = new Pos(i, j);
					flag = 1;
				}
			}
		}
		
		
		floorStartPos = null;
		columnDestPos = null;
		for (int i = 0; i < M; i++) {
			//Up
			Pos upPos = new Pos(columnTopLeftPos.row - 1, columnTopLeftPos.col + i);
			if (floorMatrix[upPos.row][upPos.col] == 0) {
				floorStartPos = upPos;
				columnDestPos = new Pos(0, M + i);
				break;
			}
			
			//Down
			
			Pos downPos = new Pos(columnTopLeftPos.row + M, columnTopLeftPos.col + i);
			if (floorMatrix[downPos.row][downPos.col] == 0) {
				floorStartPos = downPos;
				columnDestPos = new Pos(3 * M - 1, M + i);
			}
			
			//Left
			Pos leftPos = columnTopLeftPos.addPos(new Pos(i, -1));
			if (floorMatrix[leftPos.row][leftPos.col] == 0) {
				floorStartPos = leftPos;
				columnDestPos = new Pos(M + i, 0);
			}
			
			//Right
			Pos rightPos = columnTopLeftPos.addPos(new Pos(i, M));
			if (floorMatrix[rightPos.row][rightPos.col] == 0) {
				floorStartPos = rightPos;
				columnDestPos = new Pos(M + i, 3 * M - 1);
			}
		}
		
		
		//East
		int[][] eastMatrix = new int[M][M];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				eastMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		int[][] eastRotatedMatrix = rotateNtimes(3, eastMatrix);
		Pos eastTopLeftPos = new Pos(M, 2 * M);
		columnMatrix = applyPartMatrixToMatrix(columnMatrix, eastRotatedMatrix, eastTopLeftPos);
		
		
		//West
		int[][] westMatrix = new int[M][M];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				westMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		int[][] westRotatedMatrix = rotateNtimes(1, westMatrix);
		Pos westTopLeftPos = new Pos(M ,0);
		columnMatrix = applyPartMatrixToMatrix(columnMatrix, westRotatedMatrix, westTopLeftPos);
	
		
		//South
		int[][] southMatrix = new int[M][M];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				southMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		Pos southTopLeftPos = new Pos(2 * M, M);
		columnMatrix = applyPartMatrixToMatrix(columnMatrix, southMatrix, southTopLeftPos);
		
		
		//North
		int[][] northMatrix = new int[M][M];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				northMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		
		int[][] northRotatedMatrix = rotateNtimes(2, northMatrix);
		Pos northTopLeftPos = new Pos(0, M);
		columnMatrix = applyPartMatrixToMatrix(columnMatrix, northRotatedMatrix, northTopLeftPos);
		
		
		//Center
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
		
		Pos centerTopLeftPos = new Pos(M, M);
		columnMatrix = applyPartMatrixToMatrix(columnMatrix, centerMatrix, centerTopLeftPos);
		
			
		
		timeStrangeList = new ArrayList<>();
		for (int i = 0; i < F; i++) {
			st = new StringTokenizer(br.readLine());
			
			int row = Integer.parseInt(st.nextToken());
			int col = Integer.parseInt(st.nextToken());
			int directionIndex = Integer.parseInt(st.nextToken());
			int advanceValue = Integer.parseInt(st.nextToken());
			
			floorMatrix[row][col] = 2;
			timeStrangeList.add(new TimeStrange(new Pos(row, col), directionIndex, advanceValue, advanceValue));
		}
	}
}