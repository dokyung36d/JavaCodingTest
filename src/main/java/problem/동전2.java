package problem;

import java.util.*;
import java.io.*;


public class 동전2 {
    static int N, K;
    static int[] coinList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] dpMatrix = new int[N + 1][K + 1];
        for (int i = 0; i < N + 1; i++) {
            Arrays.fill(dpMatrix[i], Integer.MAX_VALUE / 2);
            dpMatrix[i][0] = 0;
        }


        for (int i = 0; i < N; i++) {
            int coinValue = coinList[i];
            for (int j = 1; j < K + 1; j++) {
                for (int numCoin = 0; coinValue * numCoin <= j; numCoin++) {
                    int cost = numCoin * coinValue;

                    dpMatrix[i + 1][j] = Math.min(dpMatrix[i + 1][j],
                            dpMatrix[i][j - cost] + numCoin);
                }
            }
        }


        if (dpMatrix[N][K] == Integer.MAX_VALUE / 2) {
            System.out.println(-1);
        }
        else {
            System.out.println(dpMatrix[N][K]);
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());


        coinList = new int[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            coinList[i] = Integer.parseInt(st.nextToken());
        }
    }
}