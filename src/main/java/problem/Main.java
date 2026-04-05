package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N, R, Q;
    static int[] queryList;
    static Map<Integer, List<Integer>> graphMap;

    public static class Node {
        int num;
        int numNode;

        public Node(int num, int numNode) {
            this.num = num;
            this.numNode = numNode;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[] visited = new int[N];
        int[] numChildList = new int[N];

        Deque<Integer> queue = new ArrayDeque<>();
        queue.add(R);

        while (!queue.isEmpty()) {
            int num = queue.pollFirst();
            int numChild = 0;
            visited[num] = 1;

            for (int nearNum : graphMap.get(num)) {
                if (visited[nearNum] == 1) { continue; }

                numChild += 1;
                queue.add(nearNum);
            }

            numChildList[num] = numChild;
        }

        queue = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
            if (numChildList[i] == 0) {
                queue.add(i);
            }
        }

        int[] answerList = new int[N];
        visited = new int[N];

        while (!queue.isEmpty()) {
            int num = queue.pollFirst();
            visited[num] = 1;
            answerList[num] += 1;

            for (int parentNum : graphMap.get(num)) {
                if (visited[parentNum] == 1) { continue; }

                answerList[parentNum] += answerList[num];
                numChildList[parentNum] -= 1;

                if (numChildList[parentNum] == 0) {
                    queue.add(parentNum);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Q; i++) {
            sb.append(answerList[queryList[i]] + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken()) - 1;
        Q = Integer.parseInt(st.nextToken());

        graphMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());

            int node1 = Integer.parseInt(st.nextToken()) - 1;
            int node2 = Integer.parseInt(st.nextToken()) - 1;


            graphMap.get(node1).add(node2);
            graphMap.get(node2).add(node1);
        }

        queryList = new int[Q];
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());

            queryList[i] = Integer.parseInt(st.nextToken()) - 1;
        }


    }



}