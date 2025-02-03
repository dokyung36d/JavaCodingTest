package problem;

import java.util.*;
import java.io.*;


public class 펠린드롬 {
    static int N, M;
    static int[] numList, commandList;

    public static void main(String[] args) throws Exception {
        init();

        int[][] dpMatrix = new int[N][N];

        for (int i = 0; i < N; i++) {
            dpMatrix[i][i] = 1;
        }
        for (int i = 0; i < N - 1; i++) {
            if (numList[i] == numList[i + 1]) {
                dpMatrix[i][i + 1] = 1;
            }
        }

        for (int i = 2; i < N; i++) {
            for (int j = 0; j < N - i; j++) {
                int startIndex = j;
                int endIndex = j + i;

                if (numList[startIndex] == numList[endIndex]) {
                    dpMatrix[j][j + i] = dpMatrix[j + 1][j + i - 1];
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < M; i++) {
            int startIndex = commandList[2 * i];
            int endIndex = commandList[2 * i + 1];

            sb.append(dpMatrix[startIndex][endIndex]);
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
        commandList = new int[2 * M];

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            commandList[2 * i] = Integer.parseInt(st.nextToken()) - 1;
            commandList[2 * i + 1] = Integer.parseInt(st.nextToken()) - 1;
        }
    }
}