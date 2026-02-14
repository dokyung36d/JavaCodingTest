package problem;

import java.util.*;
import java.io.*;



public class Main {
    static int N;
    static int[][] colorMatrix;
    static int answer;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        answer = Integer.MAX_VALUE / 2;

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

        dpMatrix[0][startIndex] = colorMatrix[0][startIndex];

        for (int i = 1; i < N; i++) {
            for (int prevColor = 0; prevColor < 3; prevColor++) {
                int prevCost = dpMatrix[i - 1][prevColor];
                for (int nextColor = 0; nextColor < 3; nextColor++) {
                    if (prevColor == nextColor) { continue; }

                    dpMatrix[i][nextColor] = Math.min(dpMatrix[i][nextColor], dpMatrix[i - 1][prevColor] + colorMatrix[i][nextColor]);
                }
            }
        }


        int minValue = Integer.MAX_VALUE / 2;
        for (int i = 0; i < 3; i++) {
            if (i == startIndex) { continue; }

            minValue = Math.min(minValue, dpMatrix[N - 1][i]);
        }

        return minValue;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        colorMatrix = new int[N][3];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int red = Integer.parseInt(st.nextToken());
            int green = Integer.parseInt(st.nextToken());
            int blue = Integer.parseInt(st.nextToken());

            colorMatrix[i][0] = red;
            colorMatrix[i][1] = green;
            colorMatrix[i][2] = blue;
        }
    }
}
