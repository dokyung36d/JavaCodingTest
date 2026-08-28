package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N, M, K;
    static int[] parentList;
    static int[] numCandyList;
    static int[][] relationMatrix;


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        for (int i = 0; i < M; i++) {
            union(relationMatrix[i][0], relationMatrix[i][1]);
        }

        Map<Integer, Integer> groupSizeMap = new HashMap<>();
        Map<Integer, Integer> groupCandyMap = new HashMap<>();

        for (int i = 0; i < N; i++) {
            int parent = findParent(i);

            groupSizeMap.put(parent, groupSizeMap.getOrDefault(parent, 0) + 1);
            groupCandyMap.put(parent, groupCandyMap.getOrDefault(parent, 0) + numCandyList[i]);
        }

        int numGroup = groupSizeMap.keySet().size();
        int[][] dpMatrix = new int[numGroup + 1][K];

        List<Integer> parentList = new ArrayList<>(groupSizeMap.keySet());

        for (int i = 1; i <= numGroup; i++) {
            int parent = parentList.get(i - 1);

            int groupSize = groupSizeMap.get(parent);
            int groupNumCandy = groupCandyMap.get(parent);

            for (int j = 1; j < K; j++) {
                if (groupSize > j) { continue; }
                dpMatrix[i][j] = Math.max(dpMatrix[i - 1][j], dpMatrix[i - 1][j - groupSize] + groupNumCandy);
            }
        }

        System.out.println("hello world");
    }

    public static int findParent(int num) {
        if (num == parentList[num]) { return num; }

        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent == num2Parent) { return; }

        parentList[Math.max(num1Parent, num2Parent)] = Math.min(num1Parent, num2Parent);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        parentList = new int[N];
        for (int i = 0; i < N; i++) {
            parentList[i] = i;
        }

        numCandyList = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numCandyList[i] = Integer.parseInt(st.nextToken());
        }

        relationMatrix = new int[M][2];
        for (int i = 0; i < M; i++) {
             st = new StringTokenizer(br.readLine());

             relationMatrix[i][0] = Integer.parseInt(st.nextToken()) - 1;
             relationMatrix[i][1] = Integer.parseInt(st.nextToken()) - 1;
        }
    }
}