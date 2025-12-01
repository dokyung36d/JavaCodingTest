package problem;

import java.util.*;
import java.io.*;

public class 도시분할계획 {
    static int N, M;
    static int[] parentList;
    static PriorityQueue<Edge> pq;
    static int numParent;

    public static class Edge implements Comparable<Edge> {
        int num1;
        int num2;
        int cost;

        public Edge(int num1, int num2, int cost) {
            this.num1 = Math.min(num1, num2);
            this.num2 = Math.max(num1, num2);
            this.cost = cost;
        }

        public int compareTo(Edge anotherEdge) {
            return Integer.compare(this.cost, anotherEdge.cost);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int answer = 0;

        while (numParent != 2) {
            Edge edge = pq.poll();

            int num1Parent = findParent(edge.num1);
            int num2Parent = findParent(edge.num2);
            if (num1Parent == num2Parent) { continue; }

            union(edge.num1, edge.num2);
            answer += edge.cost;
        }

        System.out.println(answer);
    }

    public static int findParent(int num) {
        if (parentList[num] == num) { return num; }

        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent == num2Parent) { return; }

        parentList[Math.max(num1Parent, num2Parent)] = Math.min(num1Parent, num2Parent);
        numParent -= 1;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        parentList = new int[N];
        for (int i = 0; i < N; i++) {
            parentList[i] = i;
        }

        numParent = N;

        pq = new PriorityQueue<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int num1 = Integer.parseInt(st.nextToken()) - 1;
            int num2 = Integer.parseInt(st.nextToken()) - 1;
            int cost = Integer.parseInt(st.nextToken());

            pq.add(new Edge(num1, num2, cost));
        }
    }
}