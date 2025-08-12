package problem;

import java.util.*;
import java.io.*;


public class 컬러볼 {
    static int N;
    static Map<Integer, Integer> cumulativeSumMap;
    static PriorityQueue<Node> pq;
    static int totalSize;

    public static class Node implements Comparable<Node> {
        int uniqueNum;
        int color;
        int size;

        public Node(int uniqueNum, int color, int size) {
            this.uniqueNum = uniqueNum;
            this.color = color;
            this.size = size;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(-this.size, -anotherNode.size);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[] answerList = new int[N];

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            List<Node> nodeList = new ArrayList<>();
            nodeList.add(node);
            cumulativeSumMap.put(node.color, cumulativeSumMap.get(node.color) - node.size);
            totalSize -= node.size;

            while (!pq.isEmpty() && pq.peek().size == node.size) {
                Node sameSizeNode = pq.poll();
                nodeList.add(sameSizeNode);
                cumulativeSumMap.put(sameSizeNode.color, cumulativeSumMap.get(sameSizeNode.color) - sameSizeNode.size);
                totalSize -= sameSizeNode.size;
            }

            for (Node polledNode : nodeList) {
                answerList[polledNode.uniqueNum] = getScore(polledNode);
            }
        }


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append(answerList[i] + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int getScore(Node node) {
        int curColorSize = cumulativeSumMap.get(node.color);

        return totalSize - curColorSize;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        pq = new PriorityQueue<>();
        cumulativeSumMap = new HashMap<>();
        totalSize = 0;

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int color = Integer.parseInt(st.nextToken());
            int size = Integer.parseInt(st.nextToken());
            pq.add(new Node(i, color, size));

            cumulativeSumMap.put(color, cumulativeSumMap.getOrDefault(color, 0) + size);
            totalSize += size;

        }
    }
}
