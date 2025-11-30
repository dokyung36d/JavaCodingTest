package problem;

import java.util.*;
import java.io.*;

public class Main {
    static int N, R, Q;
    static Map<Integer, List<Integer>> graphMap;
    static int[] visited, numNodeList;
    static int[] queryList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        recursive(R);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Q; i++) {
            sb.append(numNodeList[queryList[i]]);
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int recursive(int num) {
        if (visited[num] == 1) { return numNodeList[num]; }

        visited[num] = 1;
        int numNode = 1;

        for (int nearNode : graphMap.get(num)) {
            if (visited[nearNode] == 1) { continue; }

            numNode += recursive(nearNode);
        }

        numNodeList[num] = numNode;
        return numNodeList[num];
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken()) - 1;
        Q = Integer.parseInt(st.nextToken());

        visited = new int[N];
        numNodeList = new int[N];
        queryList = new int[Q];

        graphMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());

            int num1 = Integer.parseInt(st.nextToken()) - 1;
            int num2 = Integer.parseInt(st.nextToken()) - 1;

            graphMap.get(num1).add(num2);
            graphMap.get(num2).add(num1);
        }


        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            queryList[i] = Integer.parseInt(st.nextToken()) - 1;
        }
    }
}