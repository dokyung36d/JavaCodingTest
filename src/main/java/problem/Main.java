package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N, M, K;
    static int[] numCandyList, parentList;
    static int[][] friendMatrix;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        for (int i = 0; i < M; i++) {
            union(friendMatrix[i][0], friendMatrix[i][1]);
        }


        Map<Integer, Integer> costMap = new HashMap<>();
        Map<Integer, Integer> groupSizeMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            int parent = findParent(i);

            costMap.put(parent, costMap.getOrDefault(parent, 0) + numCandyList[i]);
            groupSizeMap.put(parent, groupSizeMap.getOrDefault(parent, 0) + 1);
        }


        List<Integer> parentList = new ArrayList<>(costMap.keySet());
        int[][] dpMatrix = new int[parentList.size() + 1][K];


        for (int i = 0; i < parentList.size(); i++) {
            int numChild = groupSizeMap.get(parentList.get(i));
            int cost = costMap.get(parentList.get(i));

            for (int j = 0; j < K; j++) {
                if (numChild > j) {
                    dpMatrix[i + 1][j] = dpMatrix[i][j];
                    continue; }

                dpMatrix[i + 1][j] = Math.max(dpMatrix[i][j], dpMatrix[i][j - numChild] + cost);
            }
        }

        System.out.println(dpMatrix[parentList.size()][K - 1]);
    }

    public static int findParent(int num) {
        if (num == parentList[num]) { return num; }

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


        numCandyList = new int[N];
        parentList = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numCandyList[i] = Integer.parseInt(st.nextToken());

            parentList[i] = i;
        }


        friendMatrix = new int[M][2];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int num1 = Integer.parseInt(st.nextToken()) - 1;
            int num2 = Integer.parseInt(st.nextToken()) - 1;

            friendMatrix[i][0] = num1;
            friendMatrix[i][1] = num2;
        }
    }
}