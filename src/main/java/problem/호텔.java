package problem;

import java.io.*;
import java.util.*;

public class 호텔 {
    static int N, C;
    static int[][] infoMatrix;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] dpMatrix = new int[N + 1][C + 1];
        for (int i = 0; i < N + 1; i++) {
            Arrays.fill(dpMatrix[i], Integer.MAX_VALUE / 2);
            dpMatrix[i][0] = 0;
        }


        for (int i = 0; i < N; i++) {
            int price = infoMatrix[i][0];
            int numPeople = infoMatrix[i][1];

            for (int j = 1; j < C + 1; j++) {
                int numMultiply;
                if (j % numPeople == 0) {
                    numMultiply = j / numPeople;
                }
                else {
                    numMultiply = j / numPeople + 1;
                }

                dpMatrix[i + 1][j] = dpMatrix[i][j];
                for (int multiply = 1; multiply <= numMultiply; multiply++) {
                    dpMatrix[i + 1][j] = Math.min(dpMatrix[i + 1][j],
                            dpMatrix[i][Math.max(0, j - multiply * numPeople)] + multiply * price);
                }
            }
        }



        System.out.println(dpMatrix[N][C]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        C = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());

        infoMatrix = new int[N][2];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            infoMatrix[i][0] = Integer.parseInt(st.nextToken());
            infoMatrix[i][1] = Integer.parseInt(st.nextToken());
        }
    }
}