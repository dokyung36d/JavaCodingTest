package problem;

import java.io.*;
import java.util.*;

public class 동전분배 {
    static int N;
    static int[] coinList;
    static HashMap<Integer, Integer> numCoinMap;
    static int totalMoney;
    static int halfMoney;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 3; i++) {
            init();
            solution();
        }
    }

    public static void solution() throws Exception {
        int flag = 0;
        int[][] dpMatrix = new int[N + 1][halfMoney + 1];
        dpMatrix[0][0] = 1;
        if (totalMoney % 2 == 1) {
            System.out.println(0);
            return;
        }

        for (int i = 1; i < N + 1; i++) {
            int curCoinValue = coinList[i - 1];
            int curCoinNum = numCoinMap.get(curCoinValue);

            for (int j = 0; j < halfMoney + 1; j++) {
                for (int k = 0; k <= curCoinNum; k++) {
                    int prevTotalMoney = j - curCoinValue * k;
                    if (prevTotalMoney < 0) {
                        continue;
                    }

                    if (dpMatrix[i - 1][prevTotalMoney] == 1) {
                        dpMatrix[i][j] = 1;
                        break;
                    }
                }
            }
            if (dpMatrix[i][halfMoney] == 1) {
                System.out.println(1);
                return;
            }
        }

        System.out.println(0);
        return;
    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        coinList = new int[N];
        numCoinMap = new HashMap<>();

        totalMoney = 0;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int coinValue = Integer.parseInt(st.nextToken());
            int numCoin = Integer.parseInt(st.nextToken());

            coinList[i] = coinValue;
            numCoinMap.put(coinValue, numCoin);

            totalMoney += (coinValue * numCoin);
        }

        halfMoney = totalMoney / 2;
    }
}