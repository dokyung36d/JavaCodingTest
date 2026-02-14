package problem;

import java.util.*;
import java.io.*;



public class Main {
    static int V, E;
    static Map<Integer, List<Node>> graphMap;
    static PriorityQueue<Node> pq;

    public static class Node implements Comparable<Node> {
        int node;
        int cost;

        public Node(int node, int cost) {
            this.node = node;
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

        pq = new PriorityQueue<>();
        pq.add(new Node(0, 0));

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (visited[node.node] == 1) { continue; }
            visited[node.node] = 1;

            answer += node.cost;

            for (Node nearNode : graphMap.get(node.node)) {
                if (visited[nearNode.node] == 1) { continue; }

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

            graphMap.get(node1).add(new Node(node2, cost));
            graphMap.get(node2).add(new Node(node1, cost));


        }
    }

}
