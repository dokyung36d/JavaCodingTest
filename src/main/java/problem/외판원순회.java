package problem;

import java.io.*;
import java.util.*;

public class 외판원순회 {
    static int N;
    static int[][] costMatrix;
    static int allVisited ;
    static Map<Node, Integer> visitedMap;

    public static class Node implements Comparable<Node> {
        int curCity;
        int visited;
        int totalCost;

        public Node(int curCity, int visited, int totalCost) {
            this.curCity = curCity;
            this.visited = visited;
            this.totalCost = totalCost;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Node anotherNode = (Node) obj;
            if (this.curCity == anotherNode.curCity && this.visited == anotherNode.visited) { return true; }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.curCity, this.visited);
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.totalCost, anotherNode.totalCost);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int answer = Integer.MAX_VALUE / 2;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(0, 1, 0));

        visitedMap = new HashMap<>();
        while (!pq.isEmpty()) {
            Node node = pq.poll();

            if (visitedMap.get(node) != null) { continue; }
            visitedMap.put(node, 1);

            if (node.visited == allVisited && costMatrix[node.curCity][0] != 0) {
                answer = Math.min(answer, node.totalCost + costMatrix[node.curCity][0]);
                continue;
            }


            for (int i = 0; i < N; i++) {
                if (costMatrix[node.curCity][i] == 0) { continue; }
                if ((node.visited & (1 << i)) != 0) { continue; }

                int updatedVisited = (node.visited | (1 << i));
                pq.add(new Node(i, updatedVisited, node.totalCost + costMatrix[node.curCity][i]));
            }
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        allVisited = (int) Math.pow(2, N) - 1;

        costMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                costMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }
}