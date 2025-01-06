package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.*;

public class 피보나치수6 {
    static long n;
    static Map<Long, long[][]> matrixExponentialMap;
    static long[][] fibonacciMatrix = {{(long) 1, (long) 1}, {(long) 1, (long) 0}};

    public static void main(String[] args) throws IOException {
        init();
        matrixExponentialMap.put((long) 1, fibonacciMatrix);

        if (n == 1 || n == 2) {
            System.out.println(1);
            return;
        }
        n -= 1;

        long curMaxExponential = (long) 1;
        while (curMaxExponential < n) {
            long[][] prevMatrixExponentialResult = matrixExponentialMap.get(curMaxExponential);
            long[][] matrixExponentialResult = matrixMultiplication(prevMatrixExponentialResult, prevMatrixExponentialResult);

            matrixExponentialMap.put(2 * curMaxExponential, matrixExponentialResult);
            curMaxExponential *= 2;
        }


        long[][] fianlMatrix = {{(long) 1, (long) 0}, {(long) 0, (long) 1}};
        while (n > 0) {
            long curRequiredExponential = (long) (Math.log(n) / Math.log(2));
            long[][] curRequiredExponentialMatrix = matrixExponentialMap.get((long) Math.pow(2, curRequiredExponential));
            fianlMatrix = matrixMultiplication(fianlMatrix, curRequiredExponentialMatrix);

            n -= (long) Math.pow(2, curRequiredExponential);
        }

        System.out.println(fianlMatrix[0][0]);

    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Long.parseLong(st.nextToken());

        matrixExponentialMap = new HashMap<>();
    }

    public static long[][] matrixMultiplication(long[][] firstMatrix, long[][] secondMatrix) throws IOException {
        int firstMatrixNumRow = firstMatrix.length;
        int firstMatrixNumCol = firstMatrix[0].length;

        int secondMatrixNumRow = secondMatrix.length;
        int secondMatrixNumCol = secondMatrix[0].length;

        long[][] resultMatrix = new long[firstMatrixNumRow][secondMatrixNumCol];
        for (int i = 0; i < firstMatrixNumRow; i++) {
            for (int j = 0; j < secondMatrixNumCol; j ++) {
                long value = (long) 0;

                for (int k = 0; k < firstMatrixNumCol; k++) {
                    value = (value + firstMatrix[i][k] * secondMatrix[k][j]) % ((long) 1000000007);
                }

                resultMatrix[i][j] = value;
            }
        }

        return resultMatrix;
    }
}