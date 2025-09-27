package problem;

import java.util.*;
import java.io.*;

public class Main {
	static int N, T;
	static int[][] colorMatrix, powerMatrix, groupMatrix;
	static Pos[] directions = {new Pos(-1, 0), new Pos(1, 0), new Pos(0, -1), new Pos(0, 1)};
	static Map<Integer, Group> groupMap;
	static int[][] attackedMatrix;
	static Map<Integer, Integer> scoreMap;
	
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
			if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
				return false;
			}
			
			return true;
		}
		
		public int getColor() {
			return colorMatrix[this.row][this.col];
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
		
		@Override
		public int compareTo(Pos anotherPos) {
			if (powerMatrix[this.row][this.col] != powerMatrix[anotherPos.row][anotherPos.col]) {
				return Integer.compare(-powerMatrix[this.row][this.col], -powerMatrix[anotherPos.row][anotherPos.col]);
			}
			
			if (this.row != anotherPos.row) {
				return Integer.compare(this.row, anotherPos.row);
			}
			
			return Integer.compare(this.col, anotherPos.col);
		}
	}
	
	static class Group implements Comparable<Group> {
		int pk;
		List<Pos> posList;
		Pos boss;
		
		public Group(int pk, List<Pos> posList) {
			this.pk = pk;
			this.posList = posList;
		}
		
		public void addPos(Pos pos) {
			this.posList.add(pos);
		}
		
		public void setBoss() {
			this.boss = posList.get(0);
			
			for (int i = 1; i < posList.size(); i++) {
				Pos pos = posList.get(i);
				if (pos.compareTo(boss) < 0) {
					this.boss = pos;
				}
			}
		}
		
		public void gatherPower() {
			for (Pos pos : this.posList) {
				if (pos.equals(this.boss)) {continue; }
				
				powerMatrix[pos.row][pos.col] -= 1;
				powerMatrix[this.boss.row][this.boss.col] += 1;
			}
		}
		
		public void attack() {
			int directionIndex = powerMatrix[this.boss.row][this.boss.col] % 4;
			int power = powerMatrix[this.boss.row][this.boss.col] - 1;
			powerMatrix[this.boss.row][this.boss.col] = 1;
			
			
			int curColor = this.boss.getColor();
			Pos curPos = new Pos(boss.row, boss.col);
			while (true) {
				if (power == 0) { break; }
				
				Pos movedPos = curPos.addPos(directions[directionIndex]);
				if (!movedPos.isValidIndex()) { break; }
				
				if (movedPos.getColor() == curColor) {
					curPos = movedPos;
					continue;
				}
				
				
				attackedMatrix[movedPos.row][movedPos.col] = 1;
				
				if (power > powerMatrix[movedPos.row][movedPos.col]) {
					colorMatrix[movedPos.row][movedPos.col] = curColor;
					power -= (powerMatrix[movedPos.row][movedPos.col] + 1);
					powerMatrix[movedPos.row][movedPos.col] += 1;
					
					curPos = movedPos;
					if (power == 0) { break; }
					continue;
				}
				
				else {
					colorMatrix[movedPos.row][movedPos.col] |= curColor;
					powerMatrix[movedPos.row][movedPos.col] += power;
					break;
				}
			}
		}
		
		@Override
		public int compareTo(Group anotherGroup) {
			if (getColorPriority(this.boss.getColor()) != getColorPriority(anotherGroup.boss.getColor())) {
				return Integer.compare(getColorPriority(this.boss.getColor()), getColorPriority(anotherGroup.boss.getColor()));
			}
			
			return this.boss.compareTo(anotherGroup.boss);
		}
	}
	
	public static void main(String[] args) throws Exception {
		init();
		solution();
	}
	
	public static void solution() {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < T; i++) {	
			morning();
			setGroupMatrix();
			setGroupMap();
			
			List<Group> groupList = new ArrayList<>();
			for (int pk : groupMap.keySet()) {
				Group group = groupMap.get(pk);
				groupList.add(group);
			}
			Collections.sort(groupList);
			
			attackedMatrix = new int[N][N];
			for (Group group : groupList) {
				if (attackedMatrix[group.boss.row][group.boss.col] == 1) { continue; }
				
				group.attack();
			} 
			
			setScoreMap();
			sb.append(scoreMap.getOrDefault(7, 0) + " ");
			sb.append(scoreMap.getOrDefault(6, 0) + " ");
			sb.append(scoreMap.getOrDefault(5, 0) + " ");
			sb.append(scoreMap.getOrDefault(3, 0) + " ");
			sb.append(scoreMap.getOrDefault(1, 0) + " ");
			sb.append(scoreMap.getOrDefault(2, 0) + " ");
			sb.append(scoreMap.getOrDefault(4, 0) + "\n");
		}
		
		System.out.println(sb.toString().substring(0, sb.length() - 1));
	}
	
	public static void setScoreMap() {
		scoreMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				int color = colorMatrix[i][j];
				
				scoreMap.put(color, scoreMap.getOrDefault(color, 0) + powerMatrix[i][j]);
			}
		}
	}
	
	public static void setGroupMap() {
		groupMap = new HashMap<>();
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				int pk =  groupMatrix[i][j];
				
				if (groupMap.get(pk) == null) {
					groupMap.put(pk, new Group(pk, new ArrayList<>()));
				}
				
				groupMap.get(pk).addPos(new Pos(i, j));
			}
		}
		
		
		for (int pk : groupMap.keySet()) {
			groupMap.get(pk).setBoss();
			groupMap.get(pk).gatherPower();
		}
		
	}
	
	public static int getColorPriority(int color) {
		if (color == 4 || color == 2 || color == 1) {
			return 0;
		}
		
		if (color == 7) {
			return 2;
		}
		
		return 1;
	}
	
	public static void setGroupMatrix() {
		int pk = 0;
		groupMatrix = new int[N][N];
				
		int[][] visited = new int[N][N];
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (visited[i][j] == 1) { continue; }
				
				pk += 1;
				Deque<Pos> queue = new ArrayDeque<>();
				queue.add(new Pos(i, j));
				int curColor = colorMatrix[i][j];
				
				while (!queue.isEmpty()) {
					Pos pos = queue.pollFirst();
					if (visited[pos.row][pos.col] == 1) { continue; }
					visited[pos.row][pos.col] = 1;
					groupMatrix[pos.row][pos.col] = pk;
				
					
					for (Pos direction : directions) {
						Pos movedPos = pos.addPos(direction);
						if (!movedPos.isValidIndex()) { continue; }
						if (visited[movedPos.row][movedPos.col] == 1) { continue; }
						if (groupMatrix[movedPos.row][movedPos.col] != 0) { continue; }
						if (colorMatrix[movedPos.row][movedPos.col] != curColor) { continue; }
						
						queue.addLast(movedPos);
					}
					
				}
			}
		}
	}
	
	public static void morning() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				powerMatrix[i][j] += 1;
			}
		}
	}
	
	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
		T = Integer.parseInt(st.nextToken());
		
		colorMatrix = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			
			String string = st.nextToken();
			for (int j = 0; j < N; j++) {
				char value = string.charAt(j);
				int numValue;
				
				if (value == 'T') {
					numValue = 4;
				}
				else if (value == 'C') {
					numValue = 2;
				}
				else {
					numValue = 1;
				}
				
				colorMatrix[i][j] = numValue;
			}
		}
		
		
		powerMatrix = new int[N][N];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			
			for (int j = 0; j < N; j++) {
				powerMatrix[i][j] = Integer.parseInt(st.nextToken());
			}
		}
	}
}