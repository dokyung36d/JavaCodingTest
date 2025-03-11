package problem;

import java.util.*;
import java.io.*;


public class 괄호추가하기 {
    static int N;
    static int[] numList;
    static char[] operatorList;
    static int[][] dpMaxMatrix;
    static int[][] dpMinMatrix;

    public static void main(String[] args) throws Exception {
        init();

        for (int i = 0; i < 1 + N / 2; i++) {
            Arrays.fill(dpMaxMatrix[i], Integer.MIN_VALUE);
            dpMaxMatrix[i][i] = numList[i];

            Arrays.fill(dpMinMatrix[i], Integer.MAX_VALUE);
            dpMinMatrix[i][i] = numList[i];
        }

        for (int gap = 1; gap <= N / 2; gap++) {
            for (int startIndex = 0; startIndex + gap <= N / 2; startIndex++) {
                int row = startIndex;
                int col = startIndex + gap;

                for (int i = 0; i < gap; i++) {
                    int leftValue = dpMaxMatrix[row][row + i];
                    int rightValue = dpMaxMatrix[row + i + 1][col];
                    char operator = operatorList[row + i];
                    if (operator != '-') {
                        dpMaxMatrix[row][col] = Math.max(dpMaxMatrix[row][col], getOperatorResult(leftValue, rightValue, operator));
                    }
                    else {
                        rightValue = dpMinMatrix[row + i + 1][col];
                        dpMaxMatrix[row][col] = Math.max(dpMaxMatrix[row][col], getOperatorResult(leftValue, rightValue, operator));
                    }

                    if (operator == '*') {
                        leftValue = dpMinMatrix[row][row + i];
                        rightValue = dpMinMatrix[row + i + 1][col];
                        dpMaxMatrix[row][col] = Math.max(dpMaxMatrix[row][col], getOperatorResult(leftValue, rightValue, operator));
                    }


                    leftValue = dpMinMatrix[row][row + i];
                    rightValue = dpMinMatrix[row + i + 1][col];
                    operator = operatorList[row + i];
                    if (operator != '-') {
                        dpMinMatrix[row][col] = Math.min(dpMinMatrix[row][col], getOperatorResult(leftValue, rightValue, operator));
                    }
                    else {
                        rightValue = dpMaxMatrix[row + i + 1][col];
                        dpMinMatrix[row][col] = Math.min(dpMinMatrix[row][col], getOperatorResult(leftValue, rightValue, operator));
                    }

                    if (operator == '*') {
                        leftValue = dpMinMatrix[row][row + i];
                        rightValue = dpMaxMatrix[row + i + 1][col];
                        dpMinMatrix[row][col] = Math.min(dpMinMatrix[row][col], getOperatorResult(leftValue, rightValue, operator));

                        leftValue = dpMaxMatrix[row][row + i];
                        rightValue = dpMinMatrix[row + i + 1][col];
                        dpMinMatrix[row][col] = Math.min(dpMinMatrix[row][col], getOperatorResult(leftValue, rightValue, operator));
                    }
                }
            }
        }

        System.out.println(dpMaxMatrix[0][N / 2]);
    }

    public static int getOperatorResult(int num1, int num2, char operator) {
        if (operator == '+') {
            return num1 + num2;
        }

        if (operator == '-') {
            return num1 - num2;
        }

        return num1 * num2;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        dpMaxMatrix = new int[1 + N / 2][1 + N / 2];
        dpMinMatrix = new int[1 + N / 2][1 + N / 2];

        numList = new int[1 + N / 2];
        operatorList = new char[N / 2];

        st = new StringTokenizer(br.readLine());
        String string = st.nextToken();
        for (int i = 0; i < N; i++) {
            if (i % 2 == 0) {
                numList[i / 2] = Character.getNumericValue(string.charAt(i));
            }
            else {
                operatorList[i / 2] = string.charAt(i);
            }
        }
    }
}