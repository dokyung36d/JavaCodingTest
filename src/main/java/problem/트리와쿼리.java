package problem;

import java.util.*;
import java.io.*;


public class 트리와쿼리 {
    static int N, R, Q;
    static int[] queryList;
    static int[] visited, answerList;
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
        recursive(R);


        StringBuilder sb = new StringBuilder();
        for (int query : queryList) {
            sb.append(answerList[query] + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int recursive(int num) {
        if (visited[num] == 1) { return answerList[num]; }
        visited[num] = 1;

        answerList[num] += 1;

        for (int nearNum : graphMap.get(num)) {
            if (visited[nearNum] == 1) { continue; }

            answerList[num] += recursive(nearNum);
        }

        return answerList[num];
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

        visited = new int[N];
        answerList = new int[N];
    }



}