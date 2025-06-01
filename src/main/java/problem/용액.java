package problem;

import java.util.*;
import java.io.*;


public class 용액 {
    public static int N;
    public static int[] numList;

    public static void main(String[] args) throws Exception {
        init();
        solution();

    }

    public static void solution() {
        int minPair = 0;
        int maxPair = 1;
        int answerSumValue = Math.abs(numList[minPair] + numList[maxPair]);

        for (int i = 0; i < N - 1; i++) {
            int pairIndex = binarySearch(i);

            int sumValue = Math.abs(numList[i] + numList[pairIndex]);
            if (sumValue < answerSumValue) {
                answerSumValue = sumValue;
                minPair = i;
                maxPair = pairIndex;
            }
        }

        System.out.println(numList[minPair] + " " + numList[maxPair]);
    }

    public static int binarySearch(int curIndex) {
        int curNum = numList[curIndex];

        int leftIndex = curIndex + 1;
        int rightIndex = N - 1;

        int answerIndex = curIndex;
        int minSum = Integer.MAX_VALUE;

        while (leftIndex <= rightIndex) {
            int midIndex = (leftIndex + rightIndex) / 2;
            int midValue = numList[midIndex];

            int sumValue = curNum + midValue;
            if (Math.abs(sumValue) < minSum) {
                minSum = Math.abs(sumValue);
                answerIndex = midIndex;
            }


            if (sumValue == 0) { return midIndex; }
            if (sumValue < 0 ) {
                leftIndex = midIndex + 1;
            }
            else {
                rightIndex = midIndex - 1;
            }
        }

        return answerIndex;
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