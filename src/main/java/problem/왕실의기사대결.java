package problem;

import java.util.*;
import java.io.*;


public class 왕실의기사대결 {
    static int L, N, Q;
    static int[][] chessMatrix, warriorMatrix;
    static Map<Integer, Warrior> warriorMap;
    static Command[] commandList;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, - 1)};
    
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
    		if (this.row < 0 || this.row >= L || this.col < 0 || this.col >= L) {
    			return false;
    		}
    		
    		return true;
    	}
    }
    
    public static class Warrior {
    	int uniqueNum;
    	Pos topLeftPos;
    	int height;
    	int width;
    	int hp;
    	int originalHP;
    	
    	public Warrior(int uniqueNum, Pos topLeftPos, int height, int width, int hp) {
    		this.uniqueNum = uniqueNum;
    		this.topLeftPos = topLeftPos;
    		this.height = height;
    		this.width = width;
    		this.originalHP = hp;
    		this.hp = hp;
    	}
    	
    	public int[][] applyToMatrix(int[][] matrix) {
    		for (int i = 0; i < height; i++) {
    			for (int j = 0; j < width; j++) {
    				matrix[this.topLeftPos.row + i][this.topLeftPos.col + j] = this.uniqueNum;
    			}
    		}
    		
    		return matrix;
    	}
    	
    	public List<Pos> getSurroundings(int directionIndex) {
    		if (directionIndex == 0) {
    			return getNorthSurroundings();
    		}
    		
    		if (directionIndex == 1) {
    			return getEastSurroundings();
    		}
    		
    		if (directionIndex == 2) {
    			return getSouthSurroundings();
    		}
    		
    		return getWestSurroundings();
    	}
    	
    	public List<Pos> getNorthSurroundings() {
    		List<Pos> northSurroundings = new ArrayList<>();
    		for (int i = 0; i < width; i++) {
    			northSurroundings.add(new Pos(this.topLeftPos.row - 1, this.topLeftPos.col + i));
    		}
    		
    		return northSurroundings;
    	}
    	
    	public List<Pos> getEastSurroundings() {
    		List<Pos> eastSurroundings = new ArrayList<>();
    		for (int i = 0; i < height; i++) {
    			eastSurroundings.add(new Pos(this.topLeftPos.row + i, this.topLeftPos.col + this.width));
    		}
    		
    		return eastSurroundings;
    	}
    	
    	public List<Pos> getSouthSurroundings() {
    		List<Pos> southSurroundings = new ArrayList<>();
    		for (int i = 0; i < this.width; i++) {
    			southSurroundings.add(this.topLeftPos.addPos(new Pos(this.height, i)));
    		}
    		
    		return southSurroundings;
    	}
    	
    	public List<Pos> getWestSurroundings() {
    		List<Pos> westSurroundings = new ArrayList<>();
    		for (int i = 0; i < this.height; i++) {
    			westSurroundings.add(this.topLeftPos.addPos(new Pos(i, -1)));
    		}
    		
    		return westSurroundings;
    	}
    	
    	public int countDamage() {
    		int damage = 0;
    		for (int i = 0; i < this.height; i++) {
    			for (int j = 0; j < this.width; j++) {
    				if (chessMatrix[this.topLeftPos.row + i][this.topLeftPos.col + j] == 1) {
    					damage += 1;
    				}
    			}
    		}
    		
    		return damage;
    	}
    	
    	public boolean isPosIncluded(Pos pos) {
    		if (pos.row >= this.topLeftPos.row && pos.row < this.topLeftPos.row + this.height 
    				&& pos.col >= this.topLeftPos.col && pos.col < this.topLeftPos.col + this.width) {
    			return true;
    		}
    		
    		return false;
    	}
    	
    	public boolean isAtLeastOnePosIncluded(List<Pos> posList) {
    		for (Pos pos : posList) {
    			if (this.isPosIncluded(pos)) {
    				return true;
    			}
    		}
    		
    		return false;
    	}
    	
    }
    
    public static class Command {
    	int uniqueNum;
    	int directionIndex;
    	
    	public Command(int uniqueNum, int directionIndex) {
    		this.uniqueNum = uniqueNum;
    		this.directionIndex = directionIndex;
    	}
    }
    
    
    public static void main(String[] args) throws Exception {
    	init();
    	
    	
    	for (Command command : commandList) {
    		int flag = 0;
    		Map<Integer, Integer> movedMap = new HashMap<>();
    		Deque<Pos> queue = new ArrayDeque<>();
    		
    		movedMap.put(command.uniqueNum, 1);
    		Warrior attackWarrior = warriorMap.get(command.uniqueNum);
    		if (attackWarrior.hp <= 0) { continue; }
    		queue.addAll(attackWarrior.getSurroundings(command.directionIndex));
    		while (!queue.isEmpty()) {
    			Pos curPos = queue.pollFirst();
    			if (!curPos.isValidIndex() || chessMatrix[curPos.row][curPos.col] == 2) {
    				flag = 1;
    				break;
    			}
    			
    			if (warriorMatrix[curPos.row][curPos.col] == 0) { continue; }
    			
    			Warrior attackedWarrior = warriorMap.get(warriorMatrix[curPos.row][curPos.col]);
    			if (movedMap.get(attackedWarrior.uniqueNum) != null) { continue; }
    			movedMap.put(attackedWarrior.uniqueNum, 1);
    			
    			queue.addAll(attackedWarrior.getSurroundings(command.directionIndex));
    		}
    		
    		
    		
    		if (flag == 1) { continue; }
    		
    		int[][] updatedWarriorMatrix = new int[L][L];
    		for (int i = 0; i < N; i++) {
    			int uniqueNum = i + 1;
    			Warrior warrior = warriorMap.get(uniqueNum);
    			
    			if (movedMap.get(uniqueNum) != null) {
    				warrior.topLeftPos = warrior.topLeftPos.addPos(directions[command.directionIndex]);
    				if (uniqueNum != command.uniqueNum) {
    					int damage = warrior.countDamage();
    					warrior.hp -= damage;
    				}
    			}
    			
    			if (warrior.hp > 0) {
    				warrior.applyToMatrix(updatedWarriorMatrix);
    			}
    		}
    		
    		warriorMatrix = updatedWarriorMatrix;
    	}
    	
    	
    	int answer = 0;
    	for (int i = 0; i < N; i++) {
    		int uniqueNum = i + 1;
    		Warrior warrior = warriorMap.get(uniqueNum);
    		if (warrior.hp <= 0) { continue; }
    		
    		answer += (warrior.originalHP - warrior.hp);
    	}
    	
    	System.out.println(answer);
    }
    
    public static void init() throws IOException {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	
    	L = Integer.parseInt(st.nextToken());
    	N = Integer.parseInt(st.nextToken());
    	Q = Integer.parseInt(st.nextToken());
    	
    	chessMatrix = new int[L][L];
    	warriorMatrix = new int[L][L];
    	
    	for (int i = 0; i < L; i++) {
    		st = new StringTokenizer(br.readLine());
    		for (int j = 0; j < L; j++) {
    			chessMatrix[i][j] = Integer.parseInt(st.nextToken());
    			
    		}
    	}
    	
    	warriorMap = new HashMap<>();
    	for (int i = 0; i < N; i++) {
    		st = new StringTokenizer(br.readLine());
    		
    		int row = Integer.parseInt(st.nextToken()) - 1;
    		int col = Integer.parseInt(st.nextToken()) - 1;
    		int height = Integer.parseInt(st.nextToken());
    		int width = Integer.parseInt(st.nextToken());
    		int hp = Integer.parseInt(st.nextToken());
    		
    		Warrior warrior = new Warrior(i + 1, new Pos(row, col), height, width, hp);
    		warrior.applyToMatrix(warriorMatrix);
    		warriorMap.put(i + 1, warrior);
    	}
    	
    	commandList = new Command[Q];
    	for (int i = 0; i < Q; i++) {
    		st = new StringTokenizer(br.readLine());
    		int uniqueNum = Integer.parseInt(st.nextToken());
    		int directionIndex = Integer.parseInt(st.nextToken());
    		
    		commandList[i] = new Command(uniqueNum, directionIndex);
    	}
    	
    }
}