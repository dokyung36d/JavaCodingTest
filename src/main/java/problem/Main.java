package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int V, E;
	static int[] parentList;
	static PriorityQueue<Edge> pq;

	public static class Edge implements Comparable<Edge> {
		int node1;
		int node2;
		int cost;

		public Edge(int node1, int node2, int cost) {
			this.node1 = node1;
			this.node2 = node2;
			this.cost = cost;
		}

		@Override
		public int compareTo(Edge anotherEdge) {
			return Integer.compare(this.cost, anotherEdge.cost);
		}

	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		parentList = new int[V];
		for (int i = 0; i < V; i++) {
			parentList[i] = i;
		}

		int totalCost = 0;

		while (!pq.isEmpty()) {
			Edge edge = pq.poll();

			int node1Parent = findParent(edge.node1);
			int node2Parent = findParent(edge.node2);
			if (node1Parent == node2Parent) { continue; }

			union(edge.node1, edge.node2);
			totalCost += edge.cost;
		}

		System.out.println(totalCost);
	}

	public static void union(int node1, int node2) {
		int node1Parent = findParent(node1);
		int node2Parent = findParent(node2);

		if (node1Parent == node2Parent) { return; }

		parentList[Math.min(node1Parent, node2Parent)] = Math.max(node1Parent, node2Parent);
	}

	public static int findParent(int curNode) {
		if (curNode == parentList[curNode]) { return curNode; }

		return parentList[curNode] = findParent(parentList[curNode]);
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		V = Integer.parseInt(st.nextToken());
		E = Integer.parseInt(st.nextToken());

		pq = new PriorityQueue<>();
		for (int i = 0; i < E; i++) {
			st = new StringTokenizer(br.readLine());
			int node1 = Integer.parseInt(st.nextToken()) - 1;
			int node2 = Integer.parseInt(st.nextToken()) - 1;
			int cost = Integer.parseInt(st.nextToken());

			pq.add(new Edge(node1, node2, cost));
		}
	}
}