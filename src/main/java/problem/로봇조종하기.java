package problem;

import java.io.*;
import java.util.*;

class 로봇조종하기 {
    static int N;
    static int M;
    static long[][] matrix;
    static long[][] dpMatrix;

    public static void main(String[] args) throws IOException {
        init();

//		if (N == 1 || M == 1) {
//			
//		}

        dpMatrix[0][0] = matrix[0][0];
        for (int i = 1; i < M; i++) {
            dpMatrix[0][i] = dpMatrix[0][i - 1] + matrix[0][i];
        }


        for (int i = 1; i < N; i++) {
            long[] leftToRightRow = new long[M];
            leftToRightRow[0] = dpMatrix[i - 1][0] + matrix[i][0];

            for (int j = 1; j < M; j++) {
                leftToRightRow[j] = Math.max(leftToRightRow[j - 1] + matrix[i][j],
                        dpMatrix[i - 1][j] + matrix[i][j]);
            }


            long[] rightToLeftRow = new long[M];
            rightToLeftRow[M - 1] = dpMatrix[i - 1][M - 1] + matrix[i][M - 1];
            for (int  j = M - 2; j > -1; j--) {
                rightToLeftRow[j] = Math.max(rightToLeftRow[j + 1] + matrix[i][j],
                        dpMatrix[i - 1][j] + matrix[i][j]);
            }


            for (int j = 0; j < M; j++) {
                dpMatrix[i][j] = Math.max(leftToRightRow[j], rightToLeftRow[j]);
            }
        }

        System.out.println(dpMatrix[N - 1][M - 1]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        matrix = new long[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                matrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        dpMatrix = new long[N][M];
    }
}