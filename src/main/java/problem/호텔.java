package problem;

import java.util.*;
import java.io.*;


public class 호텔 {
    static int C, N;
    static City[] cityList;

    public static class City {
        int cost;
        int numPeople;

        public City(int cost, int numPeople) {
            this.cost = cost;
            this.numPeople = numPeople;
        }
    }

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
            for (int j = 1; j < C + 1; j++) {
                City city = cityList[i];

                int maxMultiplyNum;
                if (j % city.numPeople == 0) {
                    maxMultiplyNum = j / city.numPeople;
                }
                else {
                    maxMultiplyNum = (j / city.numPeople) + 1;
                }

                for (int multiplyNum = 0; multiplyNum <= maxMultiplyNum; multiplyNum++) {
                    dpMatrix[i + 1][j] = Math.min(dpMatrix[i + 1][j],
                            dpMatrix[i][Math.max(0, j - (city.numPeople * multiplyNum))] + city.cost * multiplyNum);
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

        cityList = new City[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int cost = Integer.parseInt(st.nextToken());
            int numPeople = Integer.parseInt(st.nextToken());
            cityList[i] = new City(cost, numPeople);
        }
    }
}
