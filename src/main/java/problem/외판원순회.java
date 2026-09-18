package problem;

import java.util.*;
import java.io.*;


public class 외판원순회 {
    static int N;
    static int[][] graphMatrix;
    static int allVisited;
    static int[][] visitedMatrix;

    public static class Node implements Comparable<Node> {
        int curPos;
        int visited;
        int cost;

        public Node(int curPos, int visited, int cost) {
            this.curPos = curPos;
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
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(0, 1, 0));

        int answer = Integer.MAX_VALUE / 2;
        while (!queue.isEmpty()) {
            Node node = queue.poll();

            if (node.visited == allVisited && graphMatrix[node.curPos][0] != 0) {
                answer = Math.min(answer, node.cost + graphMatrix[node.curPos][0]);
                continue;
            }

            if (visitedMatrix[node.curPos][node.visited] == 1) { continue; }
            visitedMatrix[node.curPos][node.visited] = 1;

            for (int i = 0; i < N; i++) {
                if (graphMatrix[node.curPos][i] == 0) { continue; }

                int updatedVisited = node.visited | (1 << i);
                if (node.visited == updatedVisited) { continue; }

                if (visitedMatrix[i][updatedVisited] == 1) { continue; }


                queue.add(new Node(i, updatedVisited, node.cost + graphMatrix[node.curPos][i]));
            }
        }


        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        allVisited = (int) Math.pow(2, N) - 1;

        graphMatrix = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                graphMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }


        visitedMatrix = new int[N][(int) Math.pow(2, N)];
    }
}