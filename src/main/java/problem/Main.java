package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N, M, K;
    static Tower[][] mainMatrix;
    static Pos[] directions = {new Pos(0, 1), new Pos(1, 0), new Pos(0, -1), new Pos(-1, 0)};
    
    public static class Pos {
    	int row;
    	int col;
    	
    	public Pos(int row, int col) {
    		this.row = row;
    		this.col = col;
    	}
    	
    	public Pos addPos(Pos direction) {
    		return new Pos((this.row + direction.row + N) % N, 
    				(this.col + direction.col + M) % M);
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
    	Pos curPos;
    	int lastAttackedTime;
    	int attackValue;
    	
    	public Tower(Pos pos, int lastAttackedTime, int attackValue) {
    		this.curPos = pos;
    		this.lastAttackedTime = lastAttackedTime;
    		this.attackValue = attackValue;
    	}
    	
 
    	@Override
    	public int compareTo(Tower anotherTower) {
//    		int attackValue = mainMatrix[curPos.row][curPos.col];
//    		int anotherTowerAttackValue = mainMatrix[anotherTower.curPos.row][anotherTower.curPos.col];
    		if (this.attackValue != anotherTower.attackValue) {
    			return Integer.compare(attackValue, anotherTower.attackValue);
    		}
    		
    		if (this.lastAttackedTime != anotherTower.lastAttackedTime) {
    			return Integer.compare(-this.lastAttackedTime, -anotherTower.lastAttackedTime);
    		}
    		
    		
    		int sumValue = this.curPos.row + this.curPos.col;
    		int anotherTowerSumValue = anotherTower.curPos.row + anotherTower.curPos.col;
    		if (sumValue != anotherTowerSumValue) {
    			return Integer.compare(-sumValue, -anotherTowerSumValue);
    		}
    		
    		return Integer.compare(-this.curPos.col, -anotherTower.curPos.col);
    	}
    }
    
    public static class Node {
    	Pos pos;
    	List<Pos> path;
    	
    	public Node(Pos pos, List<Pos> path) {
    		this.pos = pos;
    		this.path = path;
    	}
    }
    
    public static void main(String[] args) throws Exception {
    	init();
    	
    	
    	for (int i = 0; i < K; i++) {
	    	int numTower = countTower();
	    	if (numTower == 1) { break; }
	    	
	    	Tower weakTower = findWeakTower();
	    	Tower strongTower = findStrongTower();
	    	mainMatrix[weakTower.curPos.row][weakTower.curPos.col].attackValue += (N + M);
	    	mainMatrix[weakTower.curPos.row][weakTower.curPos.col].lastAttackedTime = i + 1;
	    	
	    	List<Pos> path = findPath(weakTower.curPos, strongTower.curPos);
	    	if (path.size() != 0) {
	    		lazerAttack(weakTower.curPos, strongTower.curPos, path);
	    	}
	    	else {
	    		path = cannonAttack(weakTower.curPos, strongTower.curPos);
	    	}
	    	heal(weakTower.curPos, path);
    	}
    	
    	
    	int answer = 0;
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < M; j++) {
    			answer = Math.max(answer, mainMatrix[i][j].attackValue);
    		}
    	}
    	
    	System.out.println(answer);
    }
    
    public static int countTower() {
    	int numTower = 0;
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < M; j++) {
    			if (mainMatrix[i][j].attackValue > 0) {
    				numTower += 1;
    			}
    		}
    	}
    	
    	return numTower;
    }
    
    public static void heal(Pos startPos, List<Pos> attackedPosList) {
    	Map<Pos, Integer> attackedMap = new HashMap<>();
    	attackedMap.put(startPos, 1);
    	for (Pos pos : attackedPosList) {
    		attackedMap.put(pos, 1);
    	}
    	
    	
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < M; j++) {
    			if (mainMatrix[i][j].attackValue <= 0) { continue; }
    			if (attackedMap.get(new Pos(i, j)) != null) { continue; }
    			
    			mainMatrix[i][j].attackValue += 1;
    		}
    	}
    }
    
    public static List<Pos> findPath(Pos startPos, Pos destPos) {
    	Deque<Node> queue = new ArrayDeque<>();
    	
    	queue.add(new Node(startPos, new ArrayList<>()));
    	int[][] visited = new int[N][M];
    	
    	while (!queue.isEmpty()) {
    		Node node = queue.pollFirst();
    		if (visited[node.pos.row][node.pos.col] == 1) { continue; }
    		visited[node.pos.row][node.pos.col] = 1;
    		
    		if (destPos.equals(node.pos)) {
    			return node.path;
    		}
    		
    		
    		for (Pos direction : directions) {
    			Pos movedPos = node.pos.addPos(direction);
    			if (mainMatrix[movedPos.row][movedPos.col].attackValue <= 0) { continue; }
    			if (visited[movedPos.row][movedPos.col] == 1) { continue; }
    			
    			List<Pos> updatedPath = new ArrayList<>(node.path);
    			updatedPath.add(movedPos);
    			queue.add(new Node(movedPos, updatedPath));
    		}
    	}
    	
    	return new ArrayList<>();
    	
    }
    
    public static void lazerAttack(Pos startPos, Pos destPos, List<Pos> path) {
    	int attackPower = mainMatrix[startPos.row][startPos.col].attackValue;
    	
    	for (Pos pos : path) {
    		if (pos.equals(destPos)) { continue; }
    		
    		mainMatrix[pos.row][pos.col].attackValue -= (attackPower / 2);
    	}
    
    	mainMatrix[destPos.row][destPos.col].attackValue -= attackPower;
    	
    }
    
    public static List<Pos> cannonAttack(Pos attackPos, Pos destPos) {
    	List<Pos> attackedPosList = new ArrayList<>();
    	
    	int attackPower = mainMatrix[attackPos.row][attackPos.col].attackValue;
    	for (int i = -1; i <= 1; i++) {
    		for (int j = -1; j <= 1; j++) {
    			if (i == 0 && j == 0) { continue;}
    			Pos targetPos = destPos.addPos(new Pos(i, j));
    			if (targetPos.equals(attackPos)) { continue; }
    			if (mainMatrix[targetPos.row][targetPos.col].attackValue <= 0) { continue; }
    			
    			attackedPosList.add(targetPos);
    			mainMatrix[targetPos.row][targetPos.col].attackValue -= (attackPower / 2);
    		}
    	}
    	
    	attackedPosList.add(destPos);
    	mainMatrix[destPos.row][destPos.col].attackValue -= attackPower;
    	
    	return attackedPosList;
    }
    
    public static Tower findWeakTower() {
    	Tower weakTower = new Tower(new Pos(-1, -1), -1, Integer.MAX_VALUE / 2);
    	
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < M; j++) {
    			if (mainMatrix[i][j].attackValue <= 0) { continue; }
    			
    			if (weakTower.compareTo(mainMatrix[i][j]) == 1) {
    				weakTower = mainMatrix[i][j];
    				
    			}
    		}
    	}
    	
    	return weakTower;
    }
    
    public static Tower findStrongTower() {
    	Tower strongTower = new Tower(new Pos(N, N), Integer.MAX_VALUE / 2, -1);
    	
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < M; j++) {
    			if (mainMatrix[i][j].attackValue <= 0) { continue; }
    			
    			if (strongTower.compareTo(mainMatrix[i][j]) == -1) {
    				strongTower = mainMatrix[i][j];
    			}
    		}
    	}
    	
    	return strongTower;
    }
    
    public static void init() throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	
    	N = Integer.parseInt(st.nextToken());
    	M = Integer.parseInt(st.nextToken());
    	K = Integer.parseInt(st.nextToken());
    	
    	mainMatrix = new Tower[N][M];
    	for (int i = 0; i < N; i++) {
    		st = new StringTokenizer(br.readLine());
    		for (int j = 0; j < M; j++) {
    			int attackValue = Integer.parseInt(st.nextToken());
    			mainMatrix[i][j] = new Tower(new Pos(i, j), 0, attackValue);
    		}
    	}
    	
    	
    }
}