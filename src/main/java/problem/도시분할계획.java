package problem;

import java.util.*;
import java.io.*;



public class 도시분할계획 {
    static int N, M;
    static PriorityQueue<Node> pq;
    static int[] parentList;

    public static class Node implements Comparable<Node> {
        int home1;
        int home2;
        int cost;

        public Node(int home1, int home2, int cost) {
            this.home1 = home1;
            this.home2 = home2;
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
        int numGroup = N;

        int answer = 0;
        while (numGroup > 2) {
            Node node = pq.poll();

            int home1Parent = findParent(node.home1);
            int home2Parent = findParent(node.home2);

            if (home1Parent == home2Parent) { continue; }

            union(home1Parent, home2Parent);
            numGroup -= 1;

            answer += node.cost;
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

        if (num1Parent == num2Parent) {
            return;
        }

        parentList[Math.min(num1Parent, num2Parent)] = Math.max(num1Parent, num2Parent);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        pq = new PriorityQueue<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int home1 = Integer.parseInt(st.nextToken()) - 1;
            int home2 = Integer.parseInt(st.nextToken()) - 1;
            int cost = Integer.parseInt(st.nextToken());

            pq.add(new Node(home1, home2, cost));
        }

        parentList = new int[N];
        for (int i = 0; i < N; i++) {
            parentList[i] = i;
        }
    }

}