package problem;

import java.util.*;
import java.io.*;


public class 우주탐사선 {
    static int N, K;
    static int[][] graphMatrix;

    public static class Node implements Comparable<Node> {
        int curPos;
        int visited;
        int totalCost;

        public Node(int curPos, int visited, int totalCost) {
            this.curPos = curPos;
            this.visited = visited;
            this.totalCost = totalCost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.totalCost, anotherNode.totalCost);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        int allVisited = (1 << N) - 1;
        PriorityQueue<Node> pq = new PriorityQueue<>();
        int[][] dpMatrix = new int[N][(1 << N)];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dpMatrix[i], Integer.MAX_VALUE / 2);
        }


        int answer = Integer.MAX_VALUE / 2;
        pq.add(new Node(K, 1 << K, 0));
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (node.visited == allVisited) {
                answer = Math.min(answer, node.totalCost);
                continue;
            }

            for (int nearPlanet = 0; nearPlanet < N; nearPlanet++) {
                if (node.curPos == nearPlanet) { continue; }

                int updatedVisited = node.visited | (1 << nearPlanet);
                int updatedCost = node.totalCost + graphMatrix[node.curPos][nearPlanet];


                if (updatedCost < dpMatrix[nearPlanet][updatedVisited]) {
                    dpMatrix[nearPlanet][updatedVisited] = updatedCost;
                    pq.add(new Node(nearPlanet, updatedVisited, updatedCost));
                }
            }
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        graphMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                graphMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }
}