package problem;

import java.util.*;
import java.io.*;


public class 트리와쿼리 {
    static int N, R, Q;
    static Map<Integer, List<Integer>> graphMap;
    static Map<Integer, Integer> answerMap;
    static int[] visited;
    static int[] qList;

    public static void main(String[] args) throws Exception {
        init();
        int rootValue = recursive(R);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Q; i++) {
            sb.append(answerMap.get(qList[i]) + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int recursive(int num) {
        int value = 1;

        visited[num] = 1;
        for (int nearNum : graphMap.get(num)) {
            if (visited[nearNum] == 1) { continue; }
            value += recursive(nearNum);
        }


        answerMap.put(num + 1, value);
        return value;
    }

    public static void init() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken()) - 1;
        Q = Integer.parseInt(st.nextToken());

        graphMap = new HashMap<>();
        answerMap = new HashMap<>();

        visited = new int[N];
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int vertex1 = Integer.parseInt(st.nextToken()) - 1;
            int vertex2 = Integer.parseInt(st.nextToken()) - 1;

            graphMap.get(vertex1).add(vertex2);
            graphMap.get(vertex2).add(vertex1);
        }

        qList = new int[Q];
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            qList[i] = Integer.parseInt(st.nextToken());
        }
    }
}