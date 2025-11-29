package problem;

import java.util.*;
import java.io.*;

public class 용액 {
    static int N;
    static int[] numList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int answerLeftNum = numList[0];
        int answerRightNum = numList[1];
        int minSumValue = Integer.MAX_VALUE;

        int leftIndex = 0;
        int rightIndex = N - 1;

        while (leftIndex < rightIndex) {
            int leftNum = numList[leftIndex];
            int rightNum = numList[rightIndex];

            int sumValue = leftNum + rightNum;

            if (sumValue == 0) {
                System.out.println(leftNum + " " + rightNum);
                return;
            }
            if (Math.abs(sumValue) < minSumValue) {
                minSumValue = Math.abs(sumValue);

                answerLeftNum = leftNum;
                answerRightNum = rightNum;
            }

            if (sumValue < 0) {
                leftIndex += 1;
            }
            if (sumValue > 0) {
                rightIndex -= 1;
            }
        }

        System.out.println(answerLeftNum + " " + answerRightNum);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        numList = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(numList);
    }
}