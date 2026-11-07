package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int[] parentList;
    static PriorityQueue<Node> pq;

    public static class Node implements Comparable<Node> {
        int num1;
        int num2;
        int cost;

        public Node(int num1, int num2, int cost) {
            this.num1 = num1;
            this.num2 = num2;
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
        int numParent = N;

        int totalCost = 0;
        while (numParent > 2) {
            Node node = pq.poll();

            int num1Parent = findParent(node.num1);
            int num2Parent = findParent(node.num2);

            if (num1Parent == num2Parent) { continue; }
            union(node.num1, node.num2);
            numParent -= 1;

            totalCost += node.cost;
        }


        System.out.println(totalCost);
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


        pq = new PriorityQueue<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int num1 = Integer.parseInt(st.nextToken()) - 1;
            int num2 = Integer.parseInt(st.nextToken()) - 1;
            int cost = Integer.parseInt(st.nextToken());

            pq.add(new Node(num1, num2, cost));
        }

    }
}
