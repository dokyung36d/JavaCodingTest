package problem;

import java.util.*;
import java.io.*;


public class 메이즈러너 {
    static int N, M, K;
    static int[][] wallMatrix;
    static Pos exitPos;
    static List<Pos> userPosList;
    static Pos[] directions = {new Pos(-1, 0), new Pos(1, 0), new Pos(0, -1), new Pos(0, 1)};
    static int numMoved;
    
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
    	
    	public Pos minusPos(Pos direction) {
    		return new Pos(this.row - direction.row, this.col - direction.col);
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
    
    public static class Rectangle {
    	Pos topLeftPos;
    	int size;
    	
    	public Rectangle(Pos topLeftPos, int size) {
    		this.topLeftPos = topLeftPos;
    		this.size = size;
    	}
    }
    
    public static void main(String[] args) throws Exception {
    	init();
    	solution();
    
    	System.out.println(numMoved);
    	System.out.println((exitPos.row + 1) + " " + (exitPos.col + 1));
    }
    
    public static void solution() {
    	
    	for (int i = 0; i < K; i++) {    		
	    	moveUsers();
    		if (userPosList.size() == 0) { break; }
    		
	    	Rectangle rectangle = getSmallestRectangle();
	    	int[][] partMatrix = getPartMatrix(rectangle.topLeftPos, rectangle.size);
	    	int[][] rotatedMatrix = rotateMatrix(partMatrix);
	    	applyToWallMatrix(rectangle.topLeftPos, rotatedMatrix);
	    	rotateUserAndExit(rectangle);
    	}
    }
    
    public static void moveUsers() {
    	List<Pos> updatedUserPosList = new ArrayList<>();
    	
    	for (Pos userPos : userPosList) {
    		int flag = 0;
    		int prevDistance = userPos.calcDistance(exitPos);
    		
    		for (Pos direction : directions) {
    			Pos movedPos = userPos.addPos(direction);
    			if (!movedPos.isValidIndex()) { continue; }
    			if (wallMatrix[movedPos.row][movedPos.col] != 0) { continue; }
    			int distance = movedPos.calcDistance(exitPos);
    			if (distance >= prevDistance) { continue; }
    			
    			
    			numMoved += 1;
    			flag = 1;
    			if (!movedPos.equals(exitPos)) {
    				updatedUserPosList.add(movedPos);
    			}
    			break;
    		}
    		
    		if (flag == 0) {
    			updatedUserPosList.add(userPos);
    		}
    	}
    	
    	userPosList = updatedUserPosList;
    }
    
    public static void rotateUserAndExit(Rectangle rectangle) {
    	exitPos = rotatePos(exitPos, rectangle);
    	
    	for (int i = 0; i < userPosList.size(); i++) {
    		Pos userPos = userPosList.get(i);
    		if (!isPosIncludedInRectangle(userPos, rectangle)) { continue; }
    		
    		userPosList.set(i, rotatePos(userPos, rectangle));
    	}
    }
    
    public static Pos rotatePos(Pos pos, Rectangle rectangle) {
    	Pos posInPartMatrix = pos.minusPos(rectangle.topLeftPos);
    	Pos rotatedPosInPartMatrix = new Pos(posInPartMatrix.col, 
    			rectangle.size - 1 - posInPartMatrix.row);
    	
    	Pos rotatedPos = rotatedPosInPartMatrix.addPos(rectangle.topLeftPos);
    	
    	return rotatedPos;
    }
    
    public static Rectangle getSmallestRectangle() {
    	for (int size = 2; size <= N; size++) {
    		for (int row = 0; row + size <= N; row++) {
    			for (int col = 0; col + size <= N; col++) {
    				Pos topLeftPos = new Pos(row, col);
    				Rectangle rectangle = new Rectangle(topLeftPos, size);
    				if (isRectangleOkay(rectangle)) {
    					return rectangle;
    				}
    			}
    		}
    	}
    	
    	return null;
    }
    
    public static boolean isPosIncludedInRectangle(Pos pos, Rectangle rectangle) {
    	if (rectangle.topLeftPos.row <= pos.row && 
    			pos.row < rectangle.topLeftPos.row +rectangle.size && 
    			rectangle.topLeftPos.col <= pos.col && 
    			pos.col < rectangle.topLeftPos.col + rectangle.size) {
    		return true;
    	}
    	
    	return false;
    }
    
    public static boolean isRectangleOkay(Rectangle rectangle) {
    	if (!isPosIncludedInRectangle(exitPos, rectangle)) {
    		return false;
    	}
    	
    	for (Pos userPos : userPosList) {
    		if (isPosIncludedInRectangle(userPos, rectangle)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public static int[][] getPartMatrix(Pos topLeftPos, int size) {
    	int[][] partMatrix = new int[size][size];
    	for (int i = 0; i < size; i++) {
    		for (int j = 0; j < size; j++) {
    			partMatrix[i][j] = wallMatrix[topLeftPos.row + i][topLeftPos.col + j];
    		}
    	}
    	
    	return partMatrix;
    }
    
    public static int[][] rotateMatrix(int[][] matrix) {
    	int row = matrix.length;
    	int col = matrix[0].length;
    	
    	int[][] rotatedMatrix = new int[row][col];
    	for (int i = 0; i < row; i++) {
    		for (int j = 0; j < col; j++) {
    			int value = matrix[i][j];
    			if (value >= 1) {
    				value -= 1;
    			}
    			rotatedMatrix[j][row - i - 1] = value;
    		}
    	}
    	
    	return rotatedMatrix;
    }
    
    public static void applyToWallMatrix(Pos topLeftPos, int[][] rotatedMatrix) {
    	for (int i = 0; i < rotatedMatrix.length; i++) {
    		for (int j = 0; j < rotatedMatrix.length; j++) {
    			Pos pos = topLeftPos.addPos(new Pos(i, j));
    			wallMatrix[pos.row][pos.col] = rotatedMatrix[i][j];
    		}
    	}
    }
    
    public static void init() throws IOException {
    	numMoved = 0;
    	
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	StringTokenizer st = new StringTokenizer(br.readLine());
    	
    	N = Integer.parseInt(st.nextToken());
    	M = Integer.parseInt(st.nextToken());
    	K = Integer.parseInt(st.nextToken());
    	
    	wallMatrix = new int[N][N];
    	for (int i = 0; i < N; i++) {
    		st = new StringTokenizer(br.readLine());
    		for (int j = 0; j < N; j++) {
    			wallMatrix[i][j] = Integer.parseInt(st.nextToken());
    		}
    	}
    	
    	userPosList = new ArrayList<>();
    	for (int i = 0; i < M; i++) {
    		st = new StringTokenizer(br.readLine());
    		
    		int row = Integer.parseInt(st.nextToken()) - 1;
    		int col = Integer.parseInt(st.nextToken()) - 1;
    		userPosList.add(new Pos(row, col));
    	}
    	
    	st = new StringTokenizer(br.readLine());
		int row = Integer.parseInt(st.nextToken()) - 1;
		int col = Integer.parseInt(st.nextToken()) - 1;
		exitPos = new Pos(row, col);
    	
    }
}