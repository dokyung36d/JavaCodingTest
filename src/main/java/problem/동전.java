package problem;

import java.util.*;
import java.io.*;


public class 동전 {
    static int N, K;
    static int[] coinList;

    public static void main(String[] args) throws Exception {
        init();

        int[] dpList = new int[K + 1];
        Arrays.fill(dpList, Integer.MAX_VALUE / 2);
        dpList[0] = 0;

        for (int i = 0; i < N; i++) {
            int coin = coinList[i];
            for (int j = 0; j < K + 1; j++) {
                if (coin > j) { continue; }

                dpList[j] = Math.min(dpList[j - coin] + 1, dpList[j]);
            }
        }

        if (dpList[K] == Integer.MAX_VALUE / 2) {
            System.out.println(-1);
        }
        else {
            System.out.println(dpList[K]);
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

        Arrays.sort(coinList);
    }
}
