package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static PriorityQueue<Node> pq;

    public static class Node implements Comparable<Node> {
        int startTime;
        int endTime;

        public Node(int startTime, int endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public int compareTo(Node anotherNode) {
            if (this.endTime != anotherNode.endTime) {
                return Integer.compare(this.endTime, anotherNode.endTime);
            }

            return Integer.compare(this.startTime, anotherNode.startTime);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int answer = 0;
        int curTime = -1;

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (node.startTime < curTime) { continue; }

            answer += 1;
            curTime = node.endTime;
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        pq = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int startTime = Integer.parseInt(st.nextToken());
            int endTime = Integer.parseInt(st.nextToken());

            pq.add(new Node(startTime, endTime));
        }
    }
}
