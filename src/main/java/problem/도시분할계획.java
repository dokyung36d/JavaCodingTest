package problem;

import java.util.*;
import java.io.*;


public class 도시분할계획 {
    static int N, M;
    static PriorityQueue<Edge> pq;
    static int[] parentList;
    static int numParents;

    public static class Edge implements Comparable<Edge> {
        int vertex1;
        int vertex2;
        int cost;

        public Edge(int vertex1, int vertex2, int cost) {
            this.vertex1 = Math.min(vertex1, vertex2);
            this.vertex2 = Math.max(vertex1, vertex2);
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge anotherEdge) {
            return Integer.compare(this.cost, anotherEdge.cost);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        int answer = 0;

        while (numParents > 2) {
            Edge edge = pq.poll();

            int vertex1Parent = findParent(edge.vertex1);
            int vertex2Parent = findParent(edge.vertex2);
            if (vertex1Parent != vertex2Parent) {
                union(vertex1Parent, vertex2Parent);
                numParents -= 1;
                answer += edge.cost;
            }
        }

        System.out.println(answer);
    }

    public static int findParent(int num) {
        if (parentList[num] == num) {
            return num;
        }

        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent != num2Parent) {
            parentList[num1Parent] = num2Parent;
        }
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        parentList = new int[N];
        numParents = N;
        for (int i = 0; i < N; i++) {
            parentList[i] = i;
        }

        pq = new PriorityQueue<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int vertex1 = Integer.parseInt(st.nextToken()) - 1;
            int vertex2 = Integer.parseInt(st.nextToken()) - 1;
            int cost = Integer.parseInt(st.nextToken());
            pq.add(new Edge(vertex1, vertex2, cost));
        }
    }
}