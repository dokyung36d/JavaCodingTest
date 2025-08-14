package problem;

import java.util.*;
import java.io.*;


public class 택배 {
    static int N, C, M;
    static PriorityQueue<Node> pq;
    static int[] capacityList;

    public static class Node implements Comparable<Node> {
        int start;
        int end;
        int size;

        public Node(int start, int end, int size) {
            this.start = start;
            this.end = end;
            this.size = size;
        }

        @Override
        public int compareTo(Node anotherNode) {
            if (this.end == anotherNode.end) {
                return Integer.compare(-this.start, -anotherNode.start);
            }

            return Integer.compare(this.end, anotherNode.end);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int answer = 0;
        while (!pq.isEmpty()) {
            Node node = pq.poll();

            int maxSize = node.size;
            for (int i = node.start; i < node.end; i++) {
                maxSize = Math.min(capacityList[i], maxSize);
            }

            for (int i = node.start; i < node.end; i++) {
                capacityList[i] -= maxSize;
            }

            answer += maxSize;
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());

        pq = new PriorityQueue<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int start = Integer.parseInt(st.nextToken()) - 1;
            int end = Integer.parseInt(st.nextToken()) - 1;
            int size = Integer.parseInt(st.nextToken());

            pq.add(new Node(start, end, size));
        }

        capacityList = new int[N];
        Arrays.fill(capacityList, C);
    }

}