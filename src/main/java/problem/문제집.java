package problem;

import java.util.*;
import java.io.*;


public class 문제집 {
    static int N, M;
    static Map<Integer, List<Integer>> graphMap;
    static int[] numParentList;

    public static class Node implements Comparable<Node> {
        int num;

        public Node(int num) {
            this.num = num;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.num, anotherNode.num);
        }

    }

    public static void main(String[] args) throws Exception {
        init();
        PriorityQueue<Node> queue = new PriorityQueue<>();

        for (int i = 0; i < N; i++) {
            if (numParentList[i] == 0) {
                queue.add(new Node(i));
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            sb.append(node.num + 1 + " ");

            for (int i = 0; i < graphMap.get(node.num).size(); i++) {
                int childNum = graphMap.get(node.num).get(i);
                numParentList[childNum] -= 1;
                if (numParentList[childNum] == 0) {
                    queue.add(new Node(childNum));
                }

            }
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        graphMap = new HashMap<>();
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        numParentList = new int[N];
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int headNum = Integer.parseInt(st.nextToken()) - 1;
            int tailNum = Integer.parseInt(st.nextToken()) - 1;

            graphMap.get(headNum).add(tailNum);
            numParentList[tailNum] += 1;
        }

    }

}