package problem;

import java.util.*;
import java.io.*;

public class Main {
    static int V, E;
	static Map<Integer, List<Node>> graphMap;

	public static class Node implements Comparable<Node> {
		int destNum;
		int cost;

		public Node(int destNum, int cost) {
			this.destNum = destNum;
			this.cost = cost;
		}

		public int compareTo(Node anotherNode) {
			return Integer.compare(this.cost, anotherNode.cost);
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		int answer = 0;

		int[] visited = new int[V];
		PriorityQueue<Node> pq = new PriorityQueue<>();

		for (Node node : graphMap.get(0)) {
			if (visited[node.destNum] == 1) { continue; }
			pq.add(node);
		}
		visited[0] = 1;

		while (!pq.isEmpty()) {
			Node node = pq.poll();
			if (visited[node.destNum] == 1) { continue; }
			visited[node.destNum] = 1;

			answer += node.cost;

			for (Node nearNode : graphMap.get(node.destNum)) {
				if (visited[nearNode.destNum] == 1) { continue; }

				pq.add(nearNode);
			}
		}

		System.out.println(answer);
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		V = Integer.parseInt(st.nextToken());
		E = Integer.parseInt(st.nextToken());

		graphMap = new HashMap<>();
		for (int i = 0; i < V; i++) {
			graphMap.put(i, new ArrayList<>());
		}

		for (int i = 0; i < E; i++) {
			st = new StringTokenizer(br.readLine());

			int num1 = Integer.parseInt(st.nextToken()) - 1;
			int num2 = Integer.parseInt(st.nextToken()) - 1;
			int cost = Integer.parseInt(st.nextToken());

			graphMap.get(num1).add(new Node(num2, cost));
			graphMap.get(num2).add(new Node(num1, cost));
		}
	}
}