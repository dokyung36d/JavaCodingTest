package problem;

import java.util.*;
import java.io.*;


public class 팰린드롬분할 {
    static int N;
    static String string;
    static Character[] charList;

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
            if (charList[i] == charList[i + 1]) {
                dpMatrix[i][i + 1] = 1;
            }
        }



        for (int gap = 2; gap < N; gap++) {
            for (int startIndex = 0; startIndex + gap < N; startIndex++) {
                int endIndex = startIndex + gap;
                if (charList[startIndex] == charList[endIndex] && dpMatrix[startIndex + 1][endIndex - 1] == 1) {
                    dpMatrix[startIndex][endIndex] = 1;
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

        charList = new Character[N];
        for (int i = 0; i < N; i++) {
            charList[i] = string.charAt(i);
        }
    }
}