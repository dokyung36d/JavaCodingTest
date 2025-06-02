package problem;

import java.util.*;
import java.io.*;


public class 도시분할계획 {
    static int N, M;
    static PriorityQueue<Edge> pq;
    static int[] parentList;

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
        parentList = new int[N];
        for (int i = 0; i < N; i++) {
            parentList[i] = i;
        }

        int numCluster = N;
        int totalCost = 0;

        while (numCluster > 2) {
            Edge edge = pq.poll();

            int node1Parent = findParent(edge.node1);
            int node2Parent = findParent(edge.node2);
            if (node1Parent == node2Parent) { continue; }

            union(edge.node1, edge.node2);
            numCluster -= 1;
            totalCost += edge.cost;
        }

        System.out.println(totalCost);
    }

    public static void union(int node1, int node2) {
        int node1Parent = findParent(node1);
        int node2Parent = findParent(node2);

        if (node1Parent == node2Parent) { return; }

        parentList[Math.max(node1Parent, node2Parent)] = Math.min(node1Parent,node2Parent);
    }

    public static int findParent(int curNode) {
        if (parentList[curNode] == curNode) { return curNode; }

        return parentList[curNode] = findParent(parentList[curNode]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        pq = new PriorityQueue<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int node1 = Integer.parseInt(st.nextToken()) - 1;
            int node2 = Integer.parseInt(st.nextToken()) - 1;
            int cost = Integer.parseInt(st.nextToken());

            pq.add(new Edge(node1, node2, cost));
        }
    }
}