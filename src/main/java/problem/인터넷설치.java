package problem;

import java.util.*;
import java.io.*;


public class 인터넷설치 {
    static int N, P, K;
    static Map<Integer, List<Edge>> graphMap;

    public static class Edge {
        int from;
        int to;
        int cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    public static class QueueNode implements Comparable<QueueNode> {
        int nodeUniqueNum;
        int numExceedsCost;

        public QueueNode(int nodeUniqueNum, int numExceedsCost) {
            this.nodeUniqueNum = nodeUniqueNum;
            this.numExceedsCost = numExceedsCost;
        }


        @Override
        public int compareTo(QueueNode anotherQueueNode) {
            return Integer.compare(this.numExceedsCost, anotherQueueNode.numExceedsCost);
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        int answer = Integer.MAX_VALUE / 2;

        int start = 0;
        int end = 1000001;

        while (start <= end) {
            int mid = (start + end) / 2;

            if (isCostPossible(mid)) {
                answer = Math.min(answer, mid);
                end = mid - 1;
            }
            else {
                start = mid + 1;
            }
        }

        if (answer == Integer.MAX_VALUE / 2) {
            System.out.println(-1);
            return;
        }

        System.out.println(answer);
    }

    public static boolean isCostPossible(int cost) {
        PriorityQueue<QueueNode> priorityQueue = new PriorityQueue<>();
        int[] visited = new int[N];

        priorityQueue.add(new QueueNode(0, 0));
        while (!priorityQueue.isEmpty()) {
            QueueNode queueNode = priorityQueue.poll();
            if (visited[queueNode.nodeUniqueNum] == 1) { continue; }
            visited[queueNode.nodeUniqueNum] = 1;
            if (queueNode.nodeUniqueNum == N - 1) {
                return true;
            }


            for (Edge edge : graphMap.get(queueNode.nodeUniqueNum)) {
                if (visited[edge.to] == 1) { continue; }

                int numExceedsCost = queueNode.numExceedsCost;
                if (edge.cost > cost) { numExceedsCost += 1; }
                if (numExceedsCost > K) { continue; }
                priorityQueue.add(new QueueNode(edge.to, numExceedsCost));
            }
        }

        return false;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        graphMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }


        for (int i = 0; i < P; i++) {
            st = new StringTokenizer(br.readLine());
            int node1 = Integer.parseInt(st.nextToken()) - 1;
            int node2 = Integer.parseInt(st.nextToken()) - 1;
            int cost = Integer.parseInt(st.nextToken());

            graphMap.get(node1).add(new Edge(node1, node2, cost));
            graphMap.get(node2).add(new Edge(node2, node1, cost));
        }
    }
}