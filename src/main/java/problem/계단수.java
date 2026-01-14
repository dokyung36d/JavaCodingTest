package problem;

import java.util.*;
import java.io.*;



public class 계단수 {
    static int N;
    static final int MAX_NUM = 1000000000;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][][] dpMatrix = new int[N][10][1024];
        for (int i = 1; i < 10; i++) {
            dpMatrix[0][i][1 << i] = 1;
        }

        for (int i = 1; i < N; i++) {
            for (int curNum = 0; curNum <= 9; curNum++) {
                for (int visited = 0; visited <= 1023; visited++) {
                    if (curNum == 0) {
                        dpMatrix[i][1][visited | (1 << 1)] += dpMatrix[i - 1][0][visited];
                        dpMatrix[i][1][visited | (1 << 1)] %= MAX_NUM;
                        continue;
                    }

                    if (curNum == 9) {
                        dpMatrix[i][8][visited | (1 << 8)] += dpMatrix[i - 1][9][visited];
                        dpMatrix[i][8][visited | (1 << 8)] %= MAX_NUM;
                        continue;
                    }

                    dpMatrix[i][curNum - 1][visited | 1 << (curNum - 1)] += dpMatrix[i - 1][curNum][visited];
                    dpMatrix[i][curNum - 1][visited | 1 << (curNum - 1)] %= MAX_NUM;

                    dpMatrix[i][curNum + 1][visited | 1 << (curNum + 1)] += dpMatrix[i - 1][curNum][visited];
                    dpMatrix[i][curNum + 1][visited | 1 << (curNum + 1)] %= MAX_NUM;
                }
            }
        }

        int answer = 0;
        for (int i = 0; i < 10; i++) {
            answer += dpMatrix[N - 1][i][1023];
            answer %= MAX_NUM;
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
    }
}