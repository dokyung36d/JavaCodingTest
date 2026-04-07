package problem;

import java.util.*;
import java.io.*;


public class 펠린드롬 {
    static int N, M;
    static int[] numList;
    static int[][] mainMatrix, queryMatrix;


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }


    public static void solution() {
        for (int gap = 2; gap < N; gap++) {
            for (int startIndex = 0; startIndex + gap < N; startIndex++) {
                int endIndex = startIndex + gap;
                if (numList[startIndex] == numList[endIndex] && mainMatrix[startIndex + 1][endIndex - 1] == 1) {
                    mainMatrix[startIndex][endIndex] = 1;
                }
            }
        }


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < M; i++) {
            sb.append(mainMatrix[queryMatrix[i][0]][queryMatrix[i][1]]);
            sb.append("\n");
        }


        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        numList = new int[N];
        mainMatrix = new int[N][N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < N; i++) {
            mainMatrix[i][i] = 1;
        }


        for (int i = 0; i < N - 1; i++) {
            if (numList[i] == numList[i + 1]) {
                mainMatrix[i][i + 1] = 1;
            }
        }


        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());

        queryMatrix = new int[M][2];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            queryMatrix[i][0] = Integer.parseInt(st.nextToken()) - 1;
            queryMatrix[i][1] = Integer.parseInt(st.nextToken()) - 1;
        }
    }

}
