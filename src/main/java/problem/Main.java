package problem;

import java.util.*;
import java.io.*;

public class Main {
    static int N, K, W;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static Map<Integer, List<Integer>> pointMap;
    static int[] timeList, numPointedList;

    public static class Node implements Comparable<Node> {
        int curNum;
        int cost;

        public Node(int curNum, int cost) {
            this.curNum = curNum;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.cost, anotherNode.cost);
        }
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int T = Integer.parseInt(st.nextToken());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < T; i++) {
            init();
            sb.append(solution());
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int solution() {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            if (numPointedList[i] != 0) { continue; }

            pq.add(new Node(i, timeList[i]));
        }

        int answer = 0;

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            answer = Math.max(answer, node.cost);
            if (node.curNum == W) {
                return node.cost;
            }

            for (int nextNum : pointMap.get(node.curNum)) {
                numPointedList[nextNum] -= 1;
                if (numPointedList[nextNum] == 0) {
                    pq.add(new Node(nextNum, node.cost + timeList[nextNum]));
                }
            }
        }

        return answer;
    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        timeList = new int[N];
        numPointedList = new int[N];

        pointMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            pointMap.put(i, new ArrayList<>());
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            timeList[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            int from = Integer.parseInt(st.nextToken()) - 1;
            int to = Integer.parseInt(st.nextToken()) - 1;

            numPointedList[to] += 1;
            pointMap.get(from).add(to);
        }

        st = new StringTokenizer(br.readLine());
        W = Integer.parseInt(st.nextToken()) - 1;
    }
}