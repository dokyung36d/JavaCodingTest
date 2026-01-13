package problem;

import java.util.*;
import java.io.*;



public class 할로윈의양아치 {
    static int N, M, K;
    static int[] numCandyList;
    static int[][] graphMatrix;
    static int[] parentList;
    static Map<Integer, Integer> groupMap, groupSizeMap;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        for (int i = 0; i < M; i++) {
            union(graphMatrix[i][0], graphMatrix[i][1]);
        }

        groupMap = new HashMap<>();
        groupSizeMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int parent = findParent(i);

            groupMap.put(parent, groupMap.getOrDefault(parent, 0) + numCandyList[i]);
            groupSizeMap.put(parent, groupSizeMap.getOrDefault(parent, 0) + 1);
        }

        List<Integer> keyList = new ArrayList<>(groupMap.keySet());
        int[][] dpMatrix = new int[keyList.size() + 1][K];
        for (int i = 0; i < keyList.size(); i++) {
            int uniqueNum = keyList.get(i);

            int numCandy = groupMap.get(uniqueNum);
            int groupSize = groupSizeMap.get(uniqueNum);

            for (int size = 0; size < K; size++) {
                if (size < groupSize) {
                    dpMatrix[i + 1][size] = dpMatrix[i][size];
                    continue;
                }

                dpMatrix[i + 1][size] = Math.max(dpMatrix[i][size],
                        dpMatrix[i][size - groupSize] + numCandy);
            }

        }

        System.out.println(dpMatrix[keyList.size()][K - 1]);
    }

    public static int findParent(int num) {
        if (num == parentList[num]) { return num; }

        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent == num2Parent) { return; }

        int minParent = Math.min(num1Parent, num2Parent);
        int maxParent = Math.max(num1Parent, num2Parent);

        parentList[maxParent] = minParent;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        numCandyList = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numCandyList[i] = Integer.parseInt(st.nextToken());
        }

        graphMatrix = new int[M][2];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            graphMatrix[i][0] = Integer.parseInt(st.nextToken()) - 1;
            graphMatrix[i][1] = Integer.parseInt(st.nextToken()) - 1;
        }

        parentList = new int[N];
        for (int i = 0; i < N; i++) {
            parentList[i] = i;
        }
    }
}