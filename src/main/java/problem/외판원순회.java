package problem;

import java.util.*;
import java.io.*;



public class 외판원순회 {
    static int N;
    static int[][] graphMatrix;
    static int allVisited;

    public static class Node implements Comparable<Node> {
        int curPos;
        int cost;
        int visited;

        public Node(int curPos, int cost, int visited) {
            this.curPos = curPos;
            this.cost = cost;
            this.visited = visited;
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
        int answer = Integer.MAX_VALUE;

        int[][] visitedMatrix = new int[N][(int) Math.pow(2, N)];

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(0, 0, 1));

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (visitedMatrix[node.curPos][node.visited] == 1) { continue; }
            visitedMatrix[node.curPos][node.visited] = 1;

            if (node.visited == allVisited) {
                if (graphMatrix[node.curPos][0] == 0) { continue; }

                int totalCost = node.cost + graphMatrix[node.curPos][0];
                answer = Math.min(answer, totalCost);
            }

            for (int nextPos = 0; nextPos < N; nextPos++) {
                if (graphMatrix[node.curPos][nextPos] == 0 ) { continue; }
                if ((node.visited & (1 << nextPos)) != 0) { continue; }
                int updatedVisited = node.visited | (1 << nextPos);
                if (visitedMatrix[nextPos][updatedVisited] == 1) { continue; }

                pq.add(new Node(nextPos, node.cost + graphMatrix[node.curPos][nextPos], updatedVisited));
            }
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        graphMatrix = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                graphMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        allVisited = (int) Math.pow(2, N) - 1;

    }
}