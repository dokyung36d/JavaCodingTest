package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static int[][] mainMatrix;
	static int[][] blocked;
	static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	static Pos[] storePosList;
	static Pos[] directions = {new Pos(-1, 0), new Pos(0, - 1), new Pos(0, 1), new Pos(1, 0)};
	static List<User> userList;
	static int numUserOuted;
	
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
	
	public static class BaseCamp implements Comparable<BaseCamp> {
		Pos baseCampPos;
		Pos storePos;
		List<Pos> path;
		
		
		public BaseCamp(Pos baseCampPos, Pos storePos, List<Pos> path) {
			this.baseCampPos = baseCampPos;
			this.storePos = storePos;
			this.path = path;
		}
		
		@Override
		public int compareTo(BaseCamp anotherBaseCamp) {
			if (this.path.size() != anotherBaseCamp.path.size()) {
				return Integer.compare(this.path.size(),anotherBaseCamp.path.size());
			}
			
			if (this.baseCampPos.row != anotherBaseCamp.baseCampPos.row) {
				return Integer.compare(this.baseCampPos.row, anotherBaseCamp.baseCampPos.row);
			}
			
			return Integer.compare(this.baseCampPos.col, anotherBaseCamp.baseCampPos.col);
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
	
	public static class User {
		Pos curPos;
		Pos storePos;
		
		public User(Pos curPos, Pos storePos) {
			this.curPos = curPos;
			this.storePos = storePos;
		}
		
	}
	
    public static void main(String[] args) throws Exception {
    	init();
    	int answer = solution();
    	
    	System.out.println(answer);
    }
    
    public static int solution() {
    	int time = 0;
    	
    	while (numUserOuted != M) {
        	moveAllUsers();
        	
        	if (time < storePosList.length) {
            	BaseCamp baseCamp  = getNearBaseCamp(storePosList[time]);
            	userList.add(new User(baseCamp.baseCampPos, baseCamp.storePos));
            	blocked[baseCamp.baseCampPos.row][baseCamp.baseCampPos.col] = 1;
        	}
        	
        	time += 1;
    	}
    	
    	return time;
    }
    
    public static void moveAllUsers() {
    	List<User> updatedUserList = new ArrayList<>();
    	List<Pos> blockedPosList = new ArrayList<>();
    	for (int i = 0; i < userList.size(); i++) {
    		User user = userList.get(i);
    		BaseCamp baseCamp = getBaseCamp(user.curPos, user.storePos);
    		Pos movedPos = baseCamp.path.get(0);
    		
    		if (movedPos.equals(user.storePos)) {
    			blockedPosList.add(movedPos);
    			numUserOuted += 1;
    			continue;
    		}
    		
    		updatedUserList.add(new User(movedPos, user.storePos));
    	}
    	
    	
    	for (Pos blockedPos : blockedPosList) {
    		blocked[blockedPos.row][blockedPos.col] = 1;
    	}
    	userList = updatedUserList;
    }
    public static BaseCamp getNearBaseCamp(Pos storePos) {
;    	BaseCamp nearBaseCamp = null;
    	
    	for (int i = 0; i < N; i++) {
    		for (int j = 0; j < N; j++) {
    			if (mainMatrix[i][j] == 0) { continue; }
    			if (blocked[i][j] == 1) { continue; }
    			
    			BaseCamp baseCamp = getBaseCamp(new Pos(i, j), storePos);
    			if (baseCamp == null) {continue; }
    			if (nearBaseCamp == null) { nearBaseCamp = baseCamp; }
    			else if (baseCamp.compareTo(nearBaseCamp) < 0) {
    				nearBaseCamp = baseCamp;
    			}
    		}
    	}
    	
    	return nearBaseCamp;
    }
    
    public static BaseCamp getBaseCamp(Pos startPos, Pos destPos) {
    	int[][] visited = new int[N][N];
    	
    	Deque<Node> queue = new ArrayDeque<>();
    	queue.add(new Node(startPos, new ArrayList<>()));
    	
    	while (!queue.isEmpty()) {
    		Node node = queue.pollFirst();
    		if (visited[node.curPos.row][node.curPos.col] == 1) { continue; }
    		visited[node.curPos.row][node.curPos.col] = 1;
    		
    		if (node.curPos.equals(destPos)) {
    			return new BaseCamp(startPos, destPos, node.path);
    		}
    		
    		for (Pos direction : directions) {
    			Pos movedPos = node.curPos.addPos(direction);
    			if (!movedPos.isValidIndex() || visited[movedPos.row][movedPos.col] == 1) {continue; }
    			if (blocked[movedPos.row][movedPos.col] == 1) { continue; }
    			
    			List<Pos> updatedPath = new ArrayList<>(node.path);
    			updatedPath.add(movedPos);
    			
    			queue.add(new Node(movedPos, updatedPath));
    		}
    	}
    	
    	return null;
    }
    
    public static void init() throws IOException {
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	
    	numUserOuted = 0;
    	
    	N = Integer.parseInt(st.nextToken());
    	M = Integer.parseInt(st.nextToken());
    	
    	mainMatrix = new int[N][N];
    	blocked = new int[N][N];
    	for (int i = 0; i < N; i++) {
    		st = new StringTokenizer(br.readLine());
    		for (int j = 0; j < N; j++) {
    			mainMatrix[i][j] = Integer.parseInt(st.nextToken());
    		}
    	}
    	
    	
    	storePosList = new Pos[M];
    	for (int i = 0; i < M; i++) {
    		st = new StringTokenizer(br.readLine());
    		
    		int row = Integer.parseInt(st.nextToken()) - 1;
    		int col = Integer.parseInt(st.nextToken()) - 1;
    		storePosList[i] = new Pos(row, col);
    	}
    	
    	userList = new ArrayList<>();
    }
}