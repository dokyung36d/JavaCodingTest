package problem;

import java.util.*;
import java.io.*;

public class RGB거리 {
    static int N;
    static int[][] colorList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int minCost = Integer.MAX_VALUE / 2;


        for (int i = 0; i < 3; i++) {
            minCost = Math.min(minCost, getMinCost(i));
        }

        System.out.println(minCost);
    }

    public static int getMinCost(int startIndex) {
        int[][] dpMatrix = new int[N][3];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dpMatrix[i], Integer.MAX_VALUE / 2);
        }
        dpMatrix[0][startIndex] = colorList[0][startIndex];

        for (int i = 0; i < N - 1; i++) {
            for (int fromColor = 0; fromColor < 3; fromColor++) {
                for (int toColor = 0; toColor < 3; toColor++) {
                    if (fromColor == toColor) { continue; }

                    dpMatrix[i + 1][toColor] = Math.min(dpMatrix[i + 1][toColor],
                            dpMatrix[i][fromColor] + colorList[i + 1][toColor]);
                }
            }
        }

        dpMatrix[N - 1][startIndex] = Integer.MAX_VALUE / 2;
        int minCost = Integer.MAX_VALUE / 2;
        for (int i = 0; i < 3; i++) {
            minCost = Math.min(minCost, dpMatrix[N - 1][i]);
        }


        return minCost;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        colorList = new int[N][3];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int red = Integer.parseInt(st.nextToken());
            int green = Integer.parseInt(st.nextToken());
            int blue = Integer.parseInt(st.nextToken());

            colorList[i][0] = red;
            colorList[i][1] = green;
            colorList[i][2] = blue;
        }
    }
}