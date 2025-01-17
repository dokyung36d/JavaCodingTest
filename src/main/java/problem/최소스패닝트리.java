package problem;

import java.awt.*;
import java.io.*;
import java.nio.Buffer;
import java.util.*;
import java.util.List;

public class 최소스패닝트리 {
    static int V, E;
    static Map<Integer, List<Node>> graphMap;
    static int minCost;
    static int[] minVertexes;
    static int[] visited;

    public static class Node implements Comparable<Node> {
        int destVertex;
        int edgeCost;

        public Node(int destVertex, int edgeCost) {
            this.destVertex = destVertex;
            this.edgeCost = edgeCost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.edgeCost, anotherNode.edgeCost);
        }

    }

    public static void main(String[] args) throws Exception {
        init();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        int answer = 0;
        answer += minCost;
        queue.addAll(graphMap.get(minVertexes[0]));
        queue.addAll(graphMap.get(minVertexes[1]));

        visited[minVertexes[0]] = 1;
        visited[minVertexes[1]] = 1;

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (visited[node.destVertex] == 1) {
                continue;
            }
            visited[node.destVertex] = 1;
            answer += node.edgeCost;

            for (int i = 0; i < graphMap.get(node.destVertex).size(); i++) {
                int dest = graphMap.get(node.destVertex).get(i).destVertex;
                if (visited[dest] == 1) {
                    continue;
                }
                queue.add(graphMap.get(node.destVertex).get(i));
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
        for (int i = 0; i <= V; i++) {
            graphMap.put(i + 1, new ArrayList<>());
        }

        visited = new int[V + 1];

        minCost = 1000001;
        minVertexes = new int[2];

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            int vertex1 = Integer.parseInt(st.nextToken());
            int vertex2 = Integer.parseInt(st.nextToken());
            int edgeCost = Integer.parseInt(st.nextToken());

            graphMap.get(vertex1).add(new Node(vertex2, edgeCost));
            graphMap.get(vertex2).add(new Node(vertex1, edgeCost));

            if (edgeCost < minCost) {
                minCost = edgeCost;

                minVertexes[0] = vertex1;
                minVertexes[1] = vertex2;
            }
        }
    }
}