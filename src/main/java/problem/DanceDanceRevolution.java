package problem;

import java.util.*;
import java.io.*;

public class DanceDanceRevolution {
    static List<Integer> numList = new ArrayList<>();
    static int[][][] dpMatrix;

    public static void main(String[] args) throws Exception {
        init();

        dpMatrix[0][0][0] = 0;
        for (int i = 0; i < numList.size(); i++) {
            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    int cost = getCost(numList.get(i), j);
                    dpMatrix[i + 1][numList.get(i)][k] = Math.min(dpMatrix[i + 1][numList.get(i)][k],
                            dpMatrix[i][j][k] + cost);
                }
            }

            for (int j = 0; j < 5; j++) {
                for (int k = 0; k < 5; k++) {
                    int cost = getCost(numList.get(i), k);
                    dpMatrix[i + 1][j][numList.get(i)] = Math.min(dpMatrix[i + 1][j][numList.get(i)],
                            dpMatrix[i][j][k] + cost);
                }
            }
        }


        int answer = 5 * 100000;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (dpMatrix[numList.size()][i][j] < answer) {
                    answer = dpMatrix[numList.size()][i][j];
                }
            }
        }

        System.out.println(answer);
    }

    public static int getCost(int num1, int num2) {
        if (num1 == 0 || num2 == 0) {
            return 2;
        }

        if (num1 == num2) {
            return 1;
        }

        if (Math.abs(num1 - num2) == 2) {
            return 4;
        }

        return 3;
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        while (true) {
            int num = Integer.parseInt(st.nextToken());
            if (num == 0) {
                break;
            }

            numList.add(num);
        }

        int maxValue = 5 * 100000;
        dpMatrix = new int[numList.size() + 1][5][5];
        for (int i = 0; i < numList.size() + 1; i++) {
            for (int j = 0; j < 5; j++) {
                Arrays.fill(dpMatrix[i][j], maxValue);
            }
        }
    }
}