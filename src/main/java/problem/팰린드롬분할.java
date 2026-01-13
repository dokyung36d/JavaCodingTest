package problem;

import java.util.*;
import java.io.*;



public class 팰린드롬분할 {
    static String string;
    static int N;
    static int[][] dpMatrix;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        dpMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            dpMatrix[i][i] = 1;
        }

        for (int i = 0; i < N - 1; i++) {
            if (string.charAt(i) == string.charAt(i + 1)) {
                dpMatrix[i][i + 1] = 1;
            }
        }

        for (int gap = 2; gap < N; gap++) {
            for (int start = 0; start + gap < N; start++) {
                int end = start + gap;

                if (string.charAt(start) == string.charAt(end) && dpMatrix[start + 1][end - 1] == 1) {
                    dpMatrix[start][end] = 1;
                }
            }
        }

        int[] dpList = new int[N + 1];
        for (int i = 0; i < N + 1; i++) {
            dpList[i] = i;
        }

        for (int endIndex = 0; endIndex < N; endIndex++) {
            for (int startIndex = 0; startIndex <= endIndex; startIndex++) {
                if (dpMatrix[startIndex][endIndex] == 0) { continue; }

                dpList[endIndex + 1] = Math.min(dpList[endIndex + 1], dpList[startIndex] + 1);
            }
        }

        System.out.println(dpList[N]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        string = st.nextToken();
        N = string.length();
    }
}