package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int V, E, K;
	static Map<Integer,List<Edge>> graphMap;
	static long[] costList;

	public static class Edge {
		int from;
		int to;
		int cost;

		public Edge(int from, int to, int cost) {
			this.from = from;
			this.to = to;
			this.cost = cost;
		}

	}


	public static class Node implements Comparable<Node> {
		int curNode;
		long totalCost;

		public Node(int curNode, long totalCost) {
			this.curNode = curNode;
			this.totalCost = totalCost;
		}

		public int compareTo(Node anotherNode) {
			return Long.compare(this.totalCost, anotherNode.totalCost);
		}
	}


	public static void main(String[] args) throws Exception {
		init();
		solution();

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < V; i++) {
			if (costList[i] == Long.MAX_VALUE) {
				sb.append("INF");
			}
			else {
				sb.append(costList[i]);
			}

			sb.append("\n");
		}

		System.out.println(sb.toString().substring(0, sb.length() - 1));
	}

	public static void solution() {
		PriorityQueue<Node> pq = new PriorityQueue<>();
		pq.add(new Node(K, (long) 0));
		int[] visited = new int[V];

		while (!pq.isEmpty()) {
			Node node = pq.poll();

			if (visited[node.curNode] == 1) { continue; }
			visited[node.curNode] = 1;
			costList[node.curNode] = node.totalCost;

			for (Edge edge : graphMap.get(node.curNode)) {
				if (visited[edge.to] == 1) { continue; }

				pq.add(new Node(edge.to, node.totalCost + (long) edge.cost));
			}
		}
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		V = Integer.parseInt(st.nextToken());
		E = Integer.parseInt(st.nextToken());

		costList = new long[V];
		Arrays.fill(costList, Long.MAX_VALUE);
		graphMap = new HashMap<>();
		for (int i = 0; i < V; i++) {
			graphMap.put(i, new ArrayList<>());
		}

		st = new StringTokenizer(br.readLine());
		K = Integer.parseInt(st.nextToken()) - 1;

		for (int i = 0; i < E; i++) {
			st = new StringTokenizer(br.readLine());

			int from = Integer.parseInt(st.nextToken()) - 1;
			int to = Integer.parseInt(st.nextToken()) - 1;
			int cost = Integer.parseInt(st.nextToken());

			graphMap.get(from).add(new Edge(from, to, cost));
		}
	}
}