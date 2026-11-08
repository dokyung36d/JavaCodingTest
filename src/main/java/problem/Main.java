package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int[] numList;
    static int[][] queryList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] dpMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            dpMatrix[i][i] = 1;
        }

        for (int i = 0; i < N - 1; i++) {
            if (numList[i] == numList[i + 1]) {
                dpMatrix[i][i + 1] = 1;
            }
        }


        for (int gap = 2; gap < N; gap++) {
            for (int startIndex = 0; startIndex + gap < N; startIndex++) {
                int endIndex = startIndex + gap;

                if (numList[startIndex] == numList[endIndex] && dpMatrix[startIndex + 1][endIndex - 1] == 1) {
                    dpMatrix[startIndex][endIndex] = 1;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < M; i++) {
            sb.append(dpMatrix[queryList[i][0]][queryList[i][1]]);
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        numList = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());
        }


        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());

        queryList = new int[M][2];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            queryList[i][0] = Integer.parseInt(st.nextToken()) - 1;
            queryList[i][1] = Integer.parseInt(st.nextToken()) - 1;
        }
    }
}
