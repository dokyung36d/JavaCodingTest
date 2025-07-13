package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static char[][] mainMatrix;
	static Pos startPos, destPos;
	static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};

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
			if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= M) {
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

	public static class Node implements Comparable<Node> {
		Pos curPos;
		int directionIndex;
		int numRotate;

		public Node(Pos curPos, int directionIndex, int numRotate) {
			this.curPos = curPos;
			this.directionIndex = directionIndex;
			this.numRotate = numRotate;
		}

		public List<Node> getNearNode() {
			List<Node> nearNodeList = new ArrayList<>();

			nearNodeList.add(new Node(this.curPos.addPos(directions[directionIndex]), this.directionIndex, this.numRotate));

			int leftDirectionIndex = (this.directionIndex + 1) % 4;
			nearNodeList.add(new Node(this.curPos.addPos(directions[leftDirectionIndex]), leftDirectionIndex, this.numRotate + 1));

			int rightDirectionIndex = (this.directionIndex + 3) % 4;
			nearNodeList.add(new Node(this.curPos.addPos(directions[rightDirectionIndex]), rightDirectionIndex, this.numRotate + 1));

			return nearNodeList;
		}

		@Override
		public int compareTo(Node anotherNode) {
			return Integer.compare(this.numRotate, anotherNode.numRotate);
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		int[][][] visited = new int[N][M][4];

		PriorityQueue<Node> pq = new PriorityQueue<>();
		for (int i = 0; i < 4; i++) {
			pq.add(new Node(startPos, i, 0));
		}

		while (!pq.isEmpty()) {
			Node node = pq.poll();
			if (visited[node.curPos.row][node.curPos.col][node.directionIndex] == 1) { continue; }
			visited[node.curPos.row][node.curPos.col][node.directionIndex] = 1;

			if (node.curPos.equals(destPos)) {
				System.out.println(node.numRotate);
				return;
			}

			List<Node> nearNodeList = node.getNearNode();
			for (Node nearNode : nearNodeList) {
				if (!nearNode.curPos.isValidIndex()) { continue; }
				if (mainMatrix[nearNode.curPos.row][nearNode.curPos.col] == '*') { continue; }
				if (visited[nearNode.curPos.row][nearNode.curPos.col][nearNode.directionIndex] == 1) { continue; }

				pq.add(nearNode);
			}
		}

		return;
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		M = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());

		mainMatrix = new char[N][M];
		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			String string = st.nextToken();
			for (int j = 0; j < M; j++) {
				mainMatrix[i][j] = string.charAt(j);

				if (mainMatrix[i][j] == 'C' && startPos == null) {
					startPos = new Pos(i, j);
					mainMatrix[i][j] = '.';
				}

				if (mainMatrix[i][j] == 'C' && startPos != null) {
					destPos = new Pos(i, j);
					mainMatrix[i][j] = '.';
				}
			}
		}
	}
}