package problem;

import java.io.*;
import java.util.*;

public class 파티 {
    static int N;
    static int M;
    static int X;
    static Map<Integer, City> graphMap = new HashMap<>();
    static Map<Integer, City> reverseGraphMap = new HashMap<>();

    static class Node implements Comparable<Node> {
        int startCity;
        int curCity;
        int totalCost;

        public Node(int startCity, int curCity,int totalCost) {
            this.startCity = startCity;
            this.curCity = curCity;
            this.totalCost = totalCost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.totalCost, anotherNode.totalCost);
        }
    }


    static class City {
        Map<Integer, Integer> nearCityCostMap;

        public City() {
            nearCityCostMap = new HashMap<>();
        }

        public void addNearCity(int destCity, int cost) {
            this.nearCityCostMap.put(destCity, cost);
        }
    }
    public static void main(String[] args) throws Exception {
        init();

        int[] firstVisited = getTimeToVisitParty();
        int[] secondVisited = getTimeToComeBack();

        int answer = 0;
        for (int i = 0; i < N + 1; i ++) {
            int candidate = firstVisited[i] + secondVisited[i];
            if (candidate > answer) {
                answer = candidate;
            }
        }

        System.out.println(answer);
    }

    public static int[] getTimeToVisitParty() throws Exception {
        int[] visited = new int[N + 1]; // first element should ignore
        visited[X] = 1;

        PriorityQueue<Node> heapq = new PriorityQueue<>();
        for (int key : reverseGraphMap.get(X).nearCityCostMap.keySet()) {
            heapq.add(new Node(X, key, reverseGraphMap.get(X).nearCityCostMap.get(key)));
        }

        while (!heapq.isEmpty()) {
            Node node = heapq.poll();
            if (visited[node.curCity] != 0) {
                continue;
            }

            visited[node.curCity] = node.totalCost;
            for (int nearCity : reverseGraphMap.get(node.curCity).nearCityCostMap.keySet()) {
                if (visited[nearCity] != 0) {
                    continue;
                }

                Node newNode = new Node(X, nearCity, node.totalCost + reverseGraphMap.get(node.curCity).nearCityCostMap.get(nearCity));
                heapq.add(newNode);
            }

        }

        visited[X] = 0;
        return visited;
    }

    public static int[] getTimeToComeBack() throws Exception {
        int[] visited = new int[N + 1];
        visited[X] = 1;

        PriorityQueue<Node> heapq = new PriorityQueue<Node>();
        for (int key : graphMap.get(X).nearCityCostMap.keySet()) {
            heapq.add(new Node(X, key, graphMap.get(X).nearCityCostMap.get(key)));
        }

        while (!heapq.isEmpty()) {
            Node node = heapq.poll();
            if (visited[node.curCity] != 0) {
                continue;
            }

            visited[node.curCity] = node.totalCost;
            for (int nearCity : graphMap.get(node.curCity).nearCityCostMap.keySet()) {
                if (visited[nearCity] != 0) {
                    continue;
                }

                Node newNode = new Node(X, nearCity, node.totalCost + graphMap.get(node.curCity).nearCityCostMap.get(nearCity));
                heapq.add(newNode);
            }
        }

        visited[X] = 0;
        return visited;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        X = Integer.parseInt(st.nextToken());

        for (int i = 0; i < N; i++) {
            graphMap.put(i + 1, new City());
            reverseGraphMap.put(i + 1, new City());
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int startCity = Integer.parseInt(st.nextToken());
            int destCity = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            graphMap.get(startCity).addNearCity(destCity, cost);
            reverseGraphMap.get(destCity).addNearCity(startCity, cost);
        }
    }
}