package problem;

import java.util.*;
import java.io.*;


public class 계단수 {
    static int N;
    static final int DIVISOR = 1000000000;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][][] dpTensor = new int[N + 1][10][1024];
        for (int i = 1; i < 10; i++) {
            dpTensor[0][i][1 << i] = 1;
        }

        for (int row = 0; row < N; row++) {
            for (int lastNum = 0; lastNum < 10; lastNum++) {
                for (int bit = 0; bit < 1024; bit++) {
                    if (lastNum == 0) {
                        dpTensor[row + 1][1][bit | (1 << 1)] += dpTensor[row][0][bit];
                        dpTensor[row + 1][1][bit | (1 << 1)] %= DIVISOR;
                        continue;
                    }

                    if (lastNum == 9) {
                        dpTensor[row + 1][8][bit | (1 << 8)] += dpTensor[row][9][bit];
                        dpTensor[row + 1][8][bit | (1 << 8)] %= DIVISOR;
                        continue;
                    }

                    dpTensor[row + 1][lastNum - 1][bit | (1 << lastNum - 1)] += dpTensor[row][lastNum][bit];
                    dpTensor[row + 1][lastNum - 1][bit | (1 << lastNum - 1)] %= DIVISOR;

                    dpTensor[row + 1][lastNum + 1][bit | (1 << lastNum + 1)] += dpTensor[row][lastNum][bit];
                    dpTensor[row + 1][lastNum + 1][bit | (1 << lastNum + 1)] %= DIVISOR;
                }

            }
        }

        int answer = 0;
        for (int i = 0; i < 10; i++) {
            answer += dpTensor[N - 1][i][1023];
            answer %= DIVISOR;
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
    }

}