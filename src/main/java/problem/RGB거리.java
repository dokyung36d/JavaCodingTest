package problem;

import java.util.*;
import java.io.*;


public class RGB거리 {
    static int N;
    static int[][] infoMatrix;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int answer = Integer.MAX_VALUE / 2;
        for (int i = 0; i < 3; i++) {
            answer = Math.min(answer, dp(i));
        }

        System.out.println(answer);
    }

    public static int dp(int startIndex) {
        int[][] dpMatrix = new int[N][3];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dpMatrix[i], Integer.MAX_VALUE / 2);
        }


        dpMatrix[0][startIndex] = infoMatrix[0][startIndex];
        for (int i = 1; i < N; i++) {
            for (int curIndex = 0; curIndex < 3; curIndex++) {
                for (int prevIndex = 0; prevIndex < 3; prevIndex++) {
                    if (curIndex == prevIndex) { continue; }

                    dpMatrix[i][curIndex] = Math.min(dpMatrix[i][curIndex], dpMatrix[i - 1][prevIndex] + infoMatrix[i][curIndex]);
                }
            }
        }


        int answer = Integer.MAX_VALUE / 2;
        for (int i = 0; i < 3; i++) {
            if (i == startIndex) { continue; }

            answer = Math.min(answer, dpMatrix[N - 1][i]);
        }
        return answer;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        infoMatrix = new int[N][3];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            infoMatrix[i][0] = Integer.parseInt(st.nextToken());
            infoMatrix[i][1] = Integer.parseInt(st.nextToken());
            infoMatrix[i][2] = Integer.parseInt(st.nextToken());
        }
    }
}