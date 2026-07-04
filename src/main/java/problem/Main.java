package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N, M;
    static PriorityQueue<Node> pq;
    static int[] parentList;

    public static class Node implements Comparable<Node> {
        int num1;
        int num2;
        int cost;

        public Node(int num1, int num2, int cost) {
            this.num1 = Math.min(num1, num2);
            this.num2 = Math.max(num1, num2);
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
        int answer = 0;

        int numParent = N;

        while (numParent != 2) {
            Node node = pq.poll();

            int num1Parent = findParent(node.num1);
            int num2Parent = findParent(node.num2);

            if (num1Parent == num2Parent) { continue; }

            answer += node.cost;
            numParent -= 1;
            union(num1Parent, num2Parent);
        }


        System.out.println(answer);
    }

    public static int findParent(int num) {
        if (num == parentList[num]) { return num; }

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