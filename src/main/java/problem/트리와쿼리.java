package problem;

import java.util.*;
import java.io.*;


public class 트리와쿼리 {
    static int N, R, Q;
    static Map<Integer, List<Integer>> treeMap;
    static int[] queryList;
    static int[] dpList, visited;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        dpList = new int[N];
        visited = new int[N];
        Arrays.fill(dpList, Integer.MAX_VALUE);

        recursive(R);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Q; i++) {
            sb.append(dpList[queryList[i]]);
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int recursive(int curNode) {
        if (dpList[curNode] != Integer.MAX_VALUE) { return dpList[curNode]; }

        visited[curNode] = 1;

        int numLeafNode = 1;
        int flag = 0;
        for (int nearNode : treeMap.get(curNode)) {
            if (dpList[nearNode] != Integer.MAX_VALUE) {
                continue;
            }

            if (visited[nearNode] == 1) { continue; }

            flag = 1;
            numLeafNode += recursive(nearNode);
        }

        dpList[curNode] = numLeafNode;
        return dpList[curNode];
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken()) - 1;
        Q = Integer.parseInt(st.nextToken());

        treeMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            treeMap.put(i, new ArrayList<>());
        }

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());

            int node1 = Integer.parseInt(st.nextToken()) - 1;
            int node2 = Integer.parseInt(st.nextToken()) - 1;

            treeMap.get(node1).add(node2);
            treeMap.get(node2).add(node1);
        }

        queryList = new int[Q];
        for (int i = 0;  i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            queryList[i] = Integer.parseInt(st.nextToken()) - 1;
        }

    }
}