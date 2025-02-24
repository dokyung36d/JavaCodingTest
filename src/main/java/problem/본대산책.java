package problem;

import java.util.*;
import java.io.*;


public class 본대산책 {
    static int D;
    static int MAX_VALUE = 1000000007;
    static long[][] baseMatrix = {
            { 0, 1, 0, 1, 0, 0, 0, 0 },
            { 1, 0, 1, 1, 0, 0, 0, 0 },
            { 0, 1, 0, 1, 1, 1, 0, 0 },
            { 1, 1, 1, 0, 0, 1, 0, 0 },
            { 0, 0, 1, 0, 0, 1, 1, 0 },
            { 0, 0, 1, 1, 1, 0, 0, 1 },
            { 0, 0, 0, 0, 1, 0, 0, 1 },
            { 0, 0, 0, 0, 0, 1, 1, 0 }
    };
    static long[][] initMatrix;

    public static void main(String[] args) throws Exception {
        init();
        List<long[][]> matrixList = new ArrayList<>();
        matrixList.add(baseMatrix);

        while (Math.pow(2, matrixList.size()) < D) {
            long[][] prevMatrix = matrixList.get(matrixList.size() - 1);
            long[][] squareMatrix = multiplyMatrix(prevMatrix, prevMatrix);
            matrixList.add(squareMatrix);
        }

        long[][] answerMatrix = initMatrix;

        while (D > 0) {
            int log2Value = (int) (Math.log(D) / Math.log(2));

            answerMatrix = multiplyMatrix(answerMatrix, matrixList.get(log2Value));
            D -= (int) Math.pow(2, log2Value);
        }

        System.out.println(answerMatrix[0][0]);

    }

    public static long[][] multiplyMatrix(long[][] matrix1, long[][] matrix2) {
        int N = matrix1.length;
        int M = matrix1[0].length;
        int K = matrix2[0].length;

        long[][] returnMatrix = new long[N][K];

        for (int n = 0; n < N; n++) {
            for (int k = 0; k < K; k++) {
                long value = 0;

                for (int m = 0; m < M; m++) {
                    value += matrix1[n][m] * matrix2[m][k];
                    value %= MAX_VALUE;
                }

                returnMatrix[n][k] = value;
            }
        }

        return returnMatrix;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        D = Integer.parseInt(st.nextToken());

        initMatrix = new long[8][8];
        for (int i = 0; i < 8; i++) {
            initMatrix[i][i] = 1;
        }
    }
}
