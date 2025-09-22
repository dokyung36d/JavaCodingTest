package problem;

import java.util.*;
import java.io.*;

public class Main {
	static int L, N, Q;
	static int[][] mainMatrix, soldierMatrix;
	static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
	static Map<Integer, Soldier> soldierMap;
	static int[][] commands;
	static int[] initPList;
	
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
	
	public static class Soldier {
		int pk;
		Pos topLeftPos;
		int h;
		int w;
		int p;
		
		public Soldier(int pk, Pos topLeftPos, int h, int w, int p) {
			this.pk = pk;
			this.topLeftPos = topLeftPos;
			this.h = h;
			this.w = w;
			this.p = p;
		}
		
		public void applyToMatrix(int[][] matrix) {
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					matrix[topLeftPos.row + i][topLeftPos.col + j] = this.pk;
				}
			}
		}
		
		public List<Pos> getSides(int directionIndex) {
			List<Pos> sideList = new ArrayList<>();
			
			if (directionIndex == 0) {
				for (int j = 0; j < w; j++) {
					sideList.add(new Pos(topLeftPos.row - 1, topLeftPos.col + j));
				}
			}
			
			if (directionIndex == 1) {
				for (int i = 0; i < h; i++) {
					sideList.add(topLeftPos.addPos(new Pos(i, this.w)));
				}
			}
			
			if (directionIndex == 2) {
				for (int j = 0; j < w; j++) {
					sideList.add(topLeftPos.addPos(new Pos(this.h, j)));
				}
			}
			
			if (directionIndex == 3) {
				for (int i = 0; i < h; i++) {
					sideList.add(topLeftPos.addPos(new Pos(i, -1)));
				}
			}
			
			return sideList;
		}
		
		public int getDamage() {
			int damage = 0;
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					if (mainMatrix[this.topLeftPos.row + i][this.topLeftPos.col + j] == 1) {
						damage += 1;
					}
				}
			}
			
			return damage;
		}
	}
	
	public static void main(String[] args) throws Exception {
		init();
		solution();
	}
	
	public static void solution() {
		for (int i = 0; i < Q; i++) {
			attack(commands[i]);
		}
		
		
		int answer = 0;
		for (int i = 0; i < N; i++) {
			int pk = i + 1;
			if (soldierMap.get(pk) == null) { continue; }
			
			answer += initPList[i] - soldierMap.get(pk).p;
		}
		
		System.out.println(answer);
		
	}
	
	public static void attack(int[] command) {
		int attackPk = command[0];
		int directionIndex = command[1];
		
		int[][] updatedSoliderMatrix = new int[L][L];
		int[] attackedList = new int[N];
	
		Deque<Integer> queue = new ArrayDeque<>();
		queue.add(attackPk);
		
		if (soldierMap.get(attackPk) == null) { return; }
		
		while (!queue.isEmpty()) {
			int pk = queue.pollFirst();
			attackedList[pk - 1] = 1;
			
			Soldier soldier = soldierMap.get(pk);
			List<Pos> sidePosList = soldier.getSides(directionIndex);
			
			for (Pos pos : sidePosList) {
				if (!pos.isValidIndex()) { return; }
				if (mainMatrix[pos.row][pos.col] == 2) { return; }
				if (soldierMatrix[pos.row][pos.col] == 0) { continue; }
				if (attackedList[soldierMatrix[pos.row][pos.col] - 1] != 0) { continue; }
				
				queue.addLast(soldierMatrix[pos.row][pos.col]);		
			}
		}
		
		
		for (int i = 0; i < N; i++) {
			if (soldierMap.get(i + 1) == null) { continue; }
			
			if (i + 1 == attackPk) {
				Soldier attackSoldier = soldierMap.get(attackPk);
				attackSoldier.topLeftPos = attackSoldier.topLeftPos.addPos(directions[directionIndex]);
				
				soldierMap.put(attackPk, attackSoldier);
				attackSoldier.applyToMatrix(updatedSoliderMatrix);
			}
			
			else if (attackedList[i] == 1) {
				Soldier attackedSoldier = soldierMap.get(i + 1);
				attackedSoldier.topLeftPos = attackedSoldier.topLeftPos.addPos(directions[directionIndex]);
				
				int damage = attackedSoldier.getDamage();
				attackedSoldier.p -= damage;
				
				if (attackedSoldier.p <= 0) {
					soldierMap.remove(attackedSoldier.pk);
					continue;
				}
				
				soldierMap.put(attackedSoldier.pk, attackedSoldier);
				attackedSoldier.applyToMatrix(updatedSoliderMatrix);
			}
			
			else {
				Soldier soldier = soldierMap.get(i + 1);
				soldier.applyToMatrix(updatedSoliderMatrix);
			}
		}
		
		soldierMatrix = updatedSoliderMatrix;
	}
	
	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());
		
		mainMatrix = new int[L][L];
		soldierMatrix = new int[L][L];
		for (int i = 0; i < L; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < L; j++) {
				mainMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		
		soldierMap = new HashMap<>();
		initPList = new int[N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			
			int row = Integer.parseInt(st.nextToken()) - 1;
			int col = Integer.parseInt(st.nextToken()) - 1;
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int p = Integer.parseInt(st.nextToken());
			
			Soldier soldier = new Soldier(i + 1, new Pos(row, col), h, w, p);
			soldierMap.put(i + 1, soldier);
			soldier.applyToMatrix(soldierMatrix);
			initPList[i] = p;
		}
		
		
		commands = new int[Q][2];
		for (int i = 0; i < Q; i++) {
			st = new StringTokenizer(br.readLine());
			
			int pk = Integer.parseInt(st.nextToken());
			int directionIndex = Integer.parseInt(st.nextToken());
			
			commands[i][0] = pk;
			commands[i][1] = directionIndex;
		}
	}

}