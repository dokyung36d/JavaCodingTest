package problem;

import java.util.*;
import java.io.*;

public class Main {
    static int N, C;
    static int[][] infoList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] dpMatrix = new int[N + 1][C + 1];
        for (int i = 0; i <= N; i++) {
            Arrays.fill(dpMatrix[i], Integer.MAX_VALUE / 2);
            dpMatrix[i][0] = 0;
        }

        for (int i = 1; i <= N; i++) {
            int cost = infoList[i - 1][0];
            int customer = infoList[i - 1][1];

            for (int j = 1; j <= C; j++) {
                int maxNumMultiply;
                if (j % customer == 0) {
                    maxNumMultiply = j / customer;
                }
                else {
                    maxNumMultiply = (j / customer) + 1;
                }

                for (int numMultiply = 0; numMultiply <= maxNumMultiply; numMultiply++) {
                    dpMatrix[i][j] = Math.min(dpMatrix[i][j],
                            dpMatrix[i - 1][Math.max(0, j - customer * numMultiply)] + cost * numMultiply);
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

        infoList = new int[N][2];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int cost = Integer.parseInt(st.nextToken());
            int customer = Integer.parseInt(st.nextToken());

            infoList[i][0] = cost;
            infoList[i][1] = customer;
        }
    }
}