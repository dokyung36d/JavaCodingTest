package problem;

import java.util.*;
import java.io.*;


public class 팰린드롬분할 {
    static String string;

    public static void main(String[] args) throws Exception {
        init();

        int N = string.length();
        int[][] dpMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            dpMatrix[i][i] = 1;
        }
        for (int i = 0; i < N - 1; i++) {
            if (string.charAt(i) == string.charAt(i + 1)) {
                dpMatrix[i][i + 1] = 1;
            }
        }

        for (int gap = 2; gap < N; gap++) {
            for (int startIndex = 0; startIndex + gap < N; startIndex++) {
                int endIndex = startIndex + gap;
                if (string.charAt(startIndex) == string.charAt(endIndex)) {
                    dpMatrix[startIndex][endIndex] = dpMatrix[startIndex + 1][endIndex - 1];
                }
            }
        }

        int[] dpList = new int[N + 1];
        for (int i = 0; i < N + 1; i++) {
            dpList[i] = i;
        }

        for (int i = 1; i < N; i++) {
            for (int j = 0; j <= i; j++) {
                int startIndex = j;
                int endIndex = i - 1;
                if (dpMatrix[startIndex][endIndex] != 1) { continue; }

                dpList[i] = Math.min(dpList[i], dpList[j] + 1);
            }
        }

        for (int j = 0; j < N; j++) {
            int startIndex = j;
            int endIndex = N - 1;

            if (dpMatrix[startIndex][endIndex] != 1) { continue; }
            dpList[N] = Math.min(dpList[N], dpList[startIndex] + 1);
        }

        System.out.println(dpList[N]);

    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        string = st.nextToken();
    }
}