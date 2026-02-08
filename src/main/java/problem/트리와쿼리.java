package problem;

import java.util.*;
import java.io.*;



public class Main {
    static int N, R, Q;
    static int[] numPointedList, queryList, answerList, visitedList;
    static Map<Integer, List<Integer>> graphMap;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        StringBuilder sb = new StringBuilder();

        recursive(R);

        for (int query : queryList) {
            int answer = recursive(query);
            sb.append(answer + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int recursive(int num) {
        if (answerList[num] != 0) {
            return answerList[num];
        }

        visitedList[num] = 1;
        int answer = 1;

        for (int nearNode : graphMap.get(num)) {
            if (visitedList[nearNode] == 1) { continue; }

            answer += recursive(nearNode);
        }

        answerList[num] = answer;
        return answer;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken()) - 1;
        Q = Integer.parseInt(st.nextToken());

        numPointedList = new int[N];
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

            numPointedList[node1] += 1;
            numPointedList[node2] += 1;
        }

        queryList = new int[Q];
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());

            queryList[i] = Integer.parseInt(st.nextToken()) - 1;
        }

        answerList = new int[N];
        visitedList = new int[N];
    }

}
