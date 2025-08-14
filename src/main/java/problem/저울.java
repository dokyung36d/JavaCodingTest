package problem;

import java.util.*;
import java.io.*;


public class 저울 {
    static int N, M;
    static Map<Edge, Integer> edgeMap;
    static int[][] distanceMatrix;

    public static class Edge {
        int from;
        int to;

        public Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }

        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Edge anotherEdge = (Edge) obj;
            if (this.from == anotherEdge.from && this.to == anotherEdge.to) { return true; }

            return false;
        }

        public int hashCode() {
            return Objects.hash(this.from, this.to);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        for (int middle = 0; middle < N; middle++) {
            for (int from = 0; from < N; from++) {
                for (int to = 0; to < N; to++) {
                    if (distanceMatrix[from][middle] == Integer.MAX_VALUE / 2) { continue; }
                    if (distanceMatrix[middle][to] == Integer.MAX_VALUE / 2) { continue; }
                    if (distanceMatrix[from][middle] != distanceMatrix[middle][to]) { continue; }

                    distanceMatrix[from][to] = distanceMatrix[from][middle];
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            int answer = N - 1;

            for (int j = 0; j < N; j++) {
                if (distanceMatrix[i][j] != Integer.MAX_VALUE / 2) {
                    answer -= 1;
                }
            }

            sb.append(answer + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());

        edgeMap = new HashMap<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int num1 = Integer.parseInt(st.nextToken()) - 1;
            int num2 = Integer.parseInt(st.nextToken()) - 1;

            edgeMap.put(new Edge(num1, num2), 1);
            edgeMap.put(new Edge(num2, num1), -1);
        }


        distanceMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(distanceMatrix[i], Integer.MAX_VALUE / 2);
//            distanceMatrix[i][i] = 0;
        }


        for (Edge edge : edgeMap.keySet()) {
            distanceMatrix[edge.from][edge.to] = edgeMap.get(edge);
        }
    }
}
