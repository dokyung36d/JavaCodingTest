package problem;

import java.util.*;
import java.io.*;


public class 최소스패닝트리 {
    static int V, E;
    static Map<Integer, List<Node>> graphMap;

    public static class Node implements Comparable<Node> {
        int from;
        int to;
        int cost;

        public Node(int from, int to, int cost) {
            this.from = from;
            this.to = to;
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
        int answer = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Node node : graphMap.get(0)) {
            pq.add(node);
        }
        visited[0] = 1;

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (visited[node.to] == 1) { continue; }
            visited[node.to] += 1;

            answer += node.cost;

            for (Node nearNode : graphMap.get(node.to)) {
                if (visited[nearNode.to] == 1) { continue; }

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

            int node1 = Integer.parseInt(st.nextToken()) - 1;
            int node2 = Integer.parseInt(st.nextToken()) - 1;
            int cost = Integer.parseInt(st.nextToken());

            graphMap.get(node1).add(new Node(node1, node2, cost));
            graphMap.get(node2).add(new Node(node2, node1, cost));
        }
    }

}