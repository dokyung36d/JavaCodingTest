package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N;
    static int[][] infoMatrix;

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
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(0, 1, 0));

        int allVisited = (int) Math.pow(2, N);
        int[][] visitedMatrix = new int[N][allVisited];

        int answer = Integer.MAX_VALUE;
        while (!pq.isEmpty()) {
            Node node = pq.poll();

            if (node.visited == allVisited - 1) {
                if (infoMatrix[node.curPos][0] == 0 ) { continue; }

                answer = Math.min(node.cost + infoMatrix[node.curPos][0], answer);
                continue;
            }

            if (visitedMatrix[node.curPos][node.visited] == 1) { continue; }
            visitedMatrix[node.curPos][node.visited] = 1;

            for (int next = 1; next < N; next++) {
                if ((node.visited & (1 << next)) != 0) { continue; }
                if (infoMatrix[node.curPos][next] == 0) { continue; }

                int updatedVisited = (node.visited | (1 << next));
                if (visitedMatrix[next][updatedVisited] != 0) { continue; }

                pq.add(new Node(next, updatedVisited, node.cost + infoMatrix[node.curPos][next]));
            }
        }


        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        infoMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                infoMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }
}