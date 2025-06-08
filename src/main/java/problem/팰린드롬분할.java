package problem;

import java.util.*;
import java.io.*;


public class 팰린드롬분할 {
    static String mainString;
    static int N;

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
            if (mainString.charAt(i) == mainString.charAt(i + 1)) {
                dpMatrix[i][i + 1] = 1;
            }
        }

        for (int gap = 2; gap < N; gap++) {
            for (int startIndex = 0; startIndex + gap < N; startIndex++) {
                int endIndex = startIndex + gap;

                if (mainString.charAt(startIndex) == mainString.charAt(endIndex) && dpMatrix[startIndex + 1][endIndex - 1] == 1) {
                    dpMatrix[startIndex][endIndex] = 1;
                }
            }
        }

        int[] dpList = new int[N + 1];
        for (int i = 0; i < N + 1; i++) {
            dpList[i] = i;
        }
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j <= i; j++) {
                int startIndex = j - 1;
                int endIndex = i - 1;

                if (dpMatrix[startIndex][endIndex] == 1) {
                    dpList[i] = Math.min(dpList[i], dpList[j - 1] + 1);
                }

            }
        }
        System.out.println(dpList[N]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        mainString = st.nextToken();
        N = mainString.length();
    }

}