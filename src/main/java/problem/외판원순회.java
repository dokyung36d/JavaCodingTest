package problem;

import java.util.*;
import java.io.*;


public class 외판원순회 {
    static int N;
    static int[][] adjacencyMatrix;

    public static class Node implements Comparable<Node> {
        int curNum;
        int visited;
        int cost;

        public Node(int curNum, int visited, int cost) {
            this.curNum = curNum;
            this.visited = visited;
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
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (int i = 1; i < N; i++) {
            if (adjacencyMatrix[0][i] != 0) {
                int visited = 1 | (1 << i);
                pq.add(new Node(i, visited, adjacencyMatrix[0][i]));
            }
        }

        int answer = Integer.MAX_VALUE / 2;
        int fullyVisited = (int) Math.pow(2, N) - 1;
        int[][] visitedMatrix = new int[N][(int) Math.pow(2, N)];
        while (!pq.isEmpty()) {
            Node node = pq.poll();

            if (visitedMatrix[node.curNum][node.visited] == 1) { continue; }
            visitedMatrix[node.curNum][node.visited] = 1;

            if (node.visited == fullyVisited && adjacencyMatrix[node.curNum][0] != 0) {
                answer = Math.min(answer, node.cost + adjacencyMatrix[node.curNum][0]);
            }

            for (int i = 0; i < N; i++) {
                if ((node.visited & (1 << i)) != 0) { continue; }
                if (adjacencyMatrix[node.curNum][i] == 0) { continue; }

                int updatedVisited = node.visited | (1 << i);
                if (visitedMatrix[i][updatedVisited] == 1) { continue; }

                pq.add(new Node(i, updatedVisited, node.cost + adjacencyMatrix[node.curNum][i]));
            }
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());

        adjacencyMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                adjacencyMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }
}