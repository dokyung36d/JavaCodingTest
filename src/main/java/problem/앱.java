package problem;

import java.util.*;
import java.io.*;


public class 앱 {
    static int N, M;
    static int[] memoryList, costList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] dpMatrix = new int[N + 1][M + 1];

        for (int i = 0; i < N + 1; i++) {
            Arrays.fill(dpMatrix[i], Integer.MAX_VALUE / 2);
            dpMatrix[i][0] = 0;
        }


        for (int i = 0; i < N; i++) {
            int memory = memoryList[i];
            int cost =  costList[i];

            for (int j = 0; j < M + 1; j++) {
                dpMatrix[i + 1][j] = dpMatrix[i][j];

                dpMatrix[i + 1][j] = Math.min(dpMatrix[i + 1][j], dpMatrix[i][Math.max(j - memory, 0)] + cost);
            }
        }

        System.out.println(dpMatrix[N][M]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        memoryList = new int[N];
        costList = new int[N];


        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            memoryList[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            costList[i] = Integer.parseInt(st.nextToken());
        }
    }
}