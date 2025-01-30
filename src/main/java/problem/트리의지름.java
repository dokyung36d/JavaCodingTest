package problem;

import java.util.*;
import java.io.*;


public class 트리의지름 {
    public static int N;
    public static  Map<Integer, List<Edge>> graphMap;

    public static class Edge {
        int destVertex;
        int cost;

        public Edge(int destVertex, int cost) {
            this.destVertex = destVertex;
            this.cost = cost;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj != null && obj.getClass() != this.getClass()) { return false; }

            Edge anotherEdge = (Edge) obj;
            if (this.destVertex == anotherEdge.destVertex) {
                return true;
            }

            return false;
        }
    }

    public static class Node implements Comparable<Node> {
        Edge edge;
        int totalCost;

        public Node(Edge edge, int totalCost) {
            this.edge = edge;
            this.totalCost = totalCost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.totalCost, anotherNode.totalCost);
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        List<Integer> firstDijkstraResult = dijkstra(0);
        int firstVertex = firstDijkstraResult.get(0);

        List<Integer> secondDijkstraResult = dijkstra(firstVertex);
        System.out.println(secondDijkstraResult.get(1));
    }

    public static List<Integer> dijkstra(int startVertex) {
        int[] confirmed = new int[N];
        int[] distances = new int[N];
        PriorityQueue<Node> queue = new PriorityQueue<>();
        for (int i = 0; i < graphMap.get(startVertex).size(); i++) {
            Edge edge = graphMap.get(startVertex).get(i);
            queue.add(new Node(edge, edge.cost));
        }
        confirmed[startVertex] = 1;

        int maxVertex = startVertex;
        int maxCost = 0;

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (confirmed[node.edge.destVertex] == 1) {
                continue;
            }

            confirmed[node.edge.destVertex] = 1;
            distances[node.edge.destVertex] = node.totalCost;
            if (node.totalCost > maxCost) {
                maxCost = node.totalCost;
                maxVertex = node.edge.destVertex;
            }

            for (int i = 0; i < graphMap.get(node.edge.destVertex).size(); i++) {
                Edge edge = graphMap.get(node.edge.destVertex).get(i);
                if (confirmed[edge.destVertex] == 1) {
                    continue;
                }

                queue.add(new Node(edge, node.totalCost + edge.cost));
            }
        }

        List<Integer> returnList = new ArrayList<>();
        returnList.add(maxVertex);
        returnList.add(maxCost);

        return returnList;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());

        graphMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int startVertex = Integer.parseInt(st.nextToken());
            startVertex -= 1;
            graphMap.put(startVertex, new ArrayList<>());
            while (true) {
                int destVertex = Integer.parseInt(st.nextToken());
                if (destVertex == -1) {
                    break;
                }
                destVertex -= 1;
                int cost = Integer.parseInt(st.nextToken());

                graphMap.get(startVertex).add(new Edge(destVertex, cost));
            }
        }
    }
}
