package problem;

import java.util.*;
import java.io.*;


public class 호텔 {
    static int N, C;
    static int[][] cityInfo;

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
            int cost = cityInfo[i][0];
            int customer = cityInfo[i][1];

            for (int j = 1; j < C + 1; j++) {
                int maxNumMultiply;
                if (j % customer == 0) {
                    maxNumMultiply = j / customer;
                }
                else {
                    maxNumMultiply = j / customer + 1;
                }

                for (int numMultiply = 0; numMultiply <= maxNumMultiply; numMultiply++) {
                    dpMatrix[i + 1][j] = Math.min(dpMatrix[i + 1][j],
                            dpMatrix[i][Math.max(0, j - customer * numMultiply)] + cost * numMultiply);
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

        cityInfo = new int[N][2];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int cost = Integer.parseInt(st.nextToken());
            int customer = Integer.parseInt(st.nextToken());

            cityInfo[i][0] = cost;
            cityInfo[i][1] = customer;
        }
    }


}

