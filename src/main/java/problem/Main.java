package problem;

import java.util.*;
import java.io.*;

public class Main {
	static int N, M, K;
	static int[][] mainMatrix;
	static Tower[][] towerMatrix;
	static Tower minTower, maxTower;
	static Pos[] directions = {new Pos(0, 1), new Pos(1, 0), new Pos(0, -1), new Pos(-1, 0)};
	static int[][] attackMatrix;
	
	public static class Pos {
		int row;
		int col;
		
		public Pos(int row, int col) {
			this.row = row;
			this.col = col;
		}
		
		public Pos addPos(Pos direction) {
			return new Pos((this.row + direction.row + N) % N, (this.col + direction.col + M) % M);
		}
		
		public int getPower() {
			return mainMatrix[this.row][this.col];
		}
		
		public int getSum() {
			return this.row + this.col;
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
	
	public static class Tower implements Comparable<Tower> {
		Pos pos;
		int lastAttacked;
		
		
		public Tower(Pos pos, int lastAttacked) {
			this.pos = pos;
			this.lastAttacked = lastAttacked;
		}
		
		@Override
		public int compareTo(Tower anotherTower) {
			if (this.pos.getPower() != anotherTower.pos.getPower()) {
				return Integer.compare(this.pos.getPower(), anotherTower.pos.getPower());
			}
			
			if (this.lastAttacked != anotherTower.lastAttacked) {
				return Integer.compare(-this.lastAttacked, -anotherTower.lastAttacked);
			}
			
			if (this.pos.getSum() != anotherTower.pos.getSum()) {
				return Integer.compare(-this.pos.getSum(), -anotherTower.pos.getSum());
			}
			
			return Integer.compare(-this.pos.col, -anotherTower.pos.col);
		}
	}
	
	public static class Node {
		Pos pos;
		List<Pos> pathList;
		
		public Node(Pos pos, List<Pos> pathList) {
			this.pos = pos;
			this.pathList = pathList;
		}
	}
	
	public static void main(String[] args) throws Exception {
		init();
		solution();
	}
	
	public static void solution() {
		for (int turn = 1; turn <= K; turn++) {
			int numTower = getTowerNum();
			if (numTower <= 1) { break; }
			
			setMinMaxTower();
			towerMatrix[minTower.pos.row][minTower.pos.col].lastAttacked = turn;
			mainMatrix[minTower.pos.row][minTower.pos.col] += (N + M);
			
			
			List<Pos> path = getPath();
			if (path != null) {
				lazerAttack(minTower.pos, path);
				setAttackMatrix(path);
			}
			else {
				List<Pos> attackList = cannonAttack();
				setAttackMatrix(attackList);
			}
			
			repair();
		}
		
		System.out.println(getMaxValue());
	}
	
	public static int getTowerNum() {
		int numTower = 0;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (mainMatrix[i][j] == 0) { continue; }
				
				numTower += 1;
			}
		}
		
		return numTower;
	}
	
	public static int getMaxValue() {
		int maxValue = 0;
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				maxValue = Math.max(maxValue, mainMatrix[i][j]);
			}
		}
		
		return maxValue;
	}
	
	public static void lazerAttack(Pos startPos, List<Pos> pathList) {
		int power = mainMatrix[startPos.row][startPos.col];
		
		for (int i = 0; i < pathList.size() - 1; i++) {
			Pos pos = pathList.get(i);
			mainMatrix[pos.row][pos.col] -= (power / 2);
		}
		
		Pos destPos = pathList.get(pathList.size() - 1);
		mainMatrix[destPos.row][destPos.col] -= power;
		
		convertMinusToZero();
	}
	
	public static List<Pos> cannonAttack() {
		int power = mainMatrix[minTower.pos.row][minTower.pos.col];
		
		Pos destPos = maxTower.pos;
		List<Pos> attackedList = new ArrayList<>();
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) { continue; }
				
				Pos movedPos = destPos.addPos(new Pos(i, j));
				if (movedPos.equals(minTower.pos)) { continue; }
				if (mainMatrix[movedPos.row][movedPos.col] == 0) { continue; }
				mainMatrix[movedPos.row][movedPos.col] -= (power / 2);
				attackedList.add(movedPos);
			}
		}
		
		mainMatrix[destPos.row][destPos.col] -= power;
		attackedList.add(destPos);
		convertMinusToZero();
		
		return attackedList;
	}
	
	public static void repair() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (mainMatrix[i][j] == 0) { continue; }
				if (attackMatrix[i][j] == 1) { continue; }
				
				mainMatrix[i][j] += 1;		
			}
		}
	}
	
	public static void setAttackMatrix(List<Pos> attackedList) {
		attackMatrix = new int[N][M];
		
		attackMatrix[minTower.pos.row][minTower.pos.col] = 1;
		attackMatrix[maxTower.pos.row][maxTower.pos.col] = 1;
		
		for (Pos pos : attackedList) {
			attackMatrix[pos.row][pos.col] = 1;
		}
	}
	
	public static void convertMinusToZero() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				mainMatrix[i][j] = Math.max(0,  mainMatrix[i][j]);
			}
		}
	}
	
	public static List<Pos> getPath() {
		int[][] visited = new int[N][M];
		
		Pos startPos = minTower.pos;
		Deque<Node> queue = new ArrayDeque<>();
		queue.addLast(new Node(startPos, new ArrayList<>()));
		
		Pos destPos = maxTower.pos;
		
		while (!queue.isEmpty()) {
			Node node = queue.pollFirst();
			if (visited[node.pos.row][node.pos.col] == 1) { continue; }
			visited[node.pos.row][node.pos.col] = 1;
			
			if (node.pos.equals(destPos)) { return node.pathList; }
			
			for (Pos direction : directions) {
				Pos movedPos = node.pos.addPos(direction);
				if (visited[movedPos.row][movedPos.col] == 1) { continue; }
				if (mainMatrix[movedPos.row][movedPos.col] == 0) { continue; }
				
				List<Pos> updatedPosList = new ArrayList<>(node.pathList);
				updatedPosList.add(movedPos);
				
				queue.addLast(new Node(movedPos, updatedPosList));
			}
		}
		
		return null;
	}
	
	
	
	public static void setMinMaxTower() {
		minTower = null;
		maxTower = null;
		
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (mainMatrix[i][j] == 0) { continue; }
				Tower tower = towerMatrix[i][j];
				
				if (minTower == null || tower.compareTo(minTower) < 0) {
					minTower = tower;
				}
				
				if (maxTower == null || tower.compareTo(maxTower) > 0) {
					maxTower = tower;
				}
			}
		}
	}
	
	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());
		
		mainMatrix = new int[N][M];
		towerMatrix = new Tower[N][M];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				mainMatrix[i][j] = Integer.parseInt(st.nextToken());
				towerMatrix[i][j] = new Tower(new Pos(i, j), 0);
			}
		}
	}
}