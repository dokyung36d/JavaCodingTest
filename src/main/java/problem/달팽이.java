package problem;

import java.util.*;
import java.io.*;


public class 달팽이 {
	static int T;
	static int N;
	static Pos[] directions = {new Pos(0, 1), new Pos(1, 0), new Pos(0, -1), new Pos(-1, 0)};
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
	}
	
	public static void main(String[] args) throws Exception {
		StringTokenizer st = new StringTokenizer(br.readLine());
		T = Integer.parseInt(st.nextToken());
		
		
		for (int i = 0; i < T; i++) {
			init();
			int[][] snail = solution();
			
			StringBuilder sb = new StringBuilder();
			sb.append("#" + (i + 1) + "\n");
			for (int row = 0; row < N; row++) {
				for (int col = 0; col < N; col++) {
					sb.append(snail[row][col] + " ");
				}
				sb.deleteCharAt(sb.length() - 1);
				sb.append("\n");
			}
			
			System.out.append(sb.toString().subSequence(0, sb.length() - 1));
			
		}
	}
	
	public static int[][] solution() {
		int[][] snail = new int[N][N];
		
		Pos curPos = new Pos(0, 0);
		int directionIndex = 0;
		int value = 1;
		
		for (int i = 0; i < N * N; i++) {
			snail[curPos.row][curPos.col] = value;
			
			value += 1;
			Pos nextPos = curPos.addPos(directions[directionIndex]);
			if (!nextPos.isValidIndex() || snail[nextPos.row][nextPos.col] != 0) {
				directionIndex = (directionIndex + 1) % 4;
				nextPos = curPos.addPos(directions[directionIndex]);
			}
			
			curPos = nextPos;
		}
		
		return snail;
	}
	
	public static void init() throws IOException {
		StringTokenizer st = new StringTokenizer(br.readLine());
		
		N = Integer.parseInt(st.nextToken());
	}
}