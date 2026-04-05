package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N;
    static int[] numList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int answerLeftNum = 0;
        int answerRightNum = 0;

        int leftIndex = 0;
        int rightIndex = N - 1;

        int minSumValue = Integer.MAX_VALUE;

        while (leftIndex < rightIndex) {
            int sumValue = numList[leftIndex] + numList[rightIndex];

            if (Math.abs(sumValue) < minSumValue) {
                minSumValue = Math.abs(sumValue);

                answerLeftNum = numList[leftIndex];
                answerRightNum = numList[rightIndex];
            }


            if (sumValue < 0) {
                leftIndex += 1;
            }

            else {
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