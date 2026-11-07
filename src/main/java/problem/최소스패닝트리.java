package problem;

import java.io.*;
import java.util.*;

public class 최소스패닝트리 {
    static int V, E;
    static Map<Integer, List<Node>> graphMap;

    public static class Node implements Comparable<Node> {
        int dest;
        int cost;

        public Node(int dest, int cost) {
            this.dest = dest;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.cost, anotherNode.cost);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[] visited = new int[V];

        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Node node : graphMap.get(0)) {
            pq.add(node);
        }

        visited[0] = 1;
        int totalCost = 0;
        while (!pq.isEmpty()) {
            Node node = pq.poll();

            if (visited[node.dest] == 1) { continue; }
            visited[node.dest] = 1;
            totalCost += node.cost;

            for (Node nearNode : graphMap.get(node.dest)) {
                if (visited[nearNode.dest] == 1) { continue; }
                pq.add(nearNode);
            }
        }


        System.out.println(totalCost);
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
