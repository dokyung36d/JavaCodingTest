package problem;

import java.util.*;
import java.io.*;


public class 할로윈의양아치 {
    static int N, M, K;
    static int[] kidsCandyList;
    static int[] parentList;
    static Map<Integer, List<Integer>> graphMap;

    public static void main(String[] args) throws Exception {
        init();

        Map<Integer, Integer> groupNumMemberMap = new HashMap<>();
        Map<Integer, Integer> groupCandyMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int prevGroupCandyNum = groupCandyMap.getOrDefault(parentList[i], 0);
            groupCandyMap.put(parentList[i], prevGroupCandyNum + kidsCandyList[i]);

            int prevGroupNumMember = groupNumMemberMap.getOrDefault(parentList[i], 0);
            groupNumMemberMap.put(parentList[i], prevGroupNumMember + 1);
        }

        List<Integer> groupParentList = new ArrayList<>(groupCandyMap.keySet());
        long[][] dpMatrix = new long[groupParentList.size() + 1][K];
        for (int i = 1; i < groupCandyMap.keySet().size() + 1; i++) {
            for (int j = 0; j < K; j++) {
                dpMatrix[i][j] = dpMatrix[i - 1][j];

                int groupParent = groupParentList.get(i - 1);
                int numGroupMember = groupNumMemberMap.get(groupParent);
                int numGroupCandy = groupCandyMap.get(groupParent);
                if (numGroupMember > j) { continue; }

                dpMatrix[i][j] = Math.max(dpMatrix[i][j], dpMatrix[i - 1][j - numGroupMember] + numGroupCandy);
            }
        }

        System.out.println(dpMatrix[groupParentList.size()][K - 1]);

    }

    public static int findParent(int num) {
        if (parentList[num] == num) {
            return num;
        }

        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent != num2Parent) {
            parentList[Math.max(num1Parent, num2Parent)] = Math.min(num1Parent, num2Parent);
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        kidsCandyList = new int[N];
        parentList = new int[N];

        graphMap = new HashMap<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            kidsCandyList[i] = Integer.parseInt(st.nextToken());
            graphMap.put(i, new ArrayList<>());
            parentList[i] = i;
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int vertex1 = Integer.parseInt(st.nextToken()) - 1;
            int vertex2 = Integer.parseInt(st.nextToken()) - 1;

            union(vertex1, vertex2);
            graphMap.get(vertex1).add(vertex2);
            graphMap.get(vertex2).add(vertex1);
        }

        for (int i = 0; i < N; i++) {
            findParent(i);
        }
    }
}
