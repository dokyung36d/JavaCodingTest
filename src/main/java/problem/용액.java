package problem;

import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;


public class 용액 {
    static int N;
    static int[] numList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int leftIndex = 0;
        int rightIndex = N - 1;

        int leftAnswer = numList[leftIndex];
        int rightAnswer = numList[rightIndex];

        int minSumValue = Integer.MAX_VALUE;

        while (leftIndex < rightIndex) {
            int leftNum = numList[leftIndex];
            int rightNum = numList[rightIndex];

            int sumValue = leftNum + rightNum;
            if (Math.abs(sumValue) < minSumValue) {
                minSumValue = Math.abs(sumValue);

                leftAnswer = numList[leftIndex];
                rightAnswer = numList[rightIndex];
            }


            if (sumValue >= 0) {
                rightIndex -= 1;
            }
            else {
                leftIndex += 1;
            }
        }


        System.out.println(leftAnswer + " " + rightAnswer);
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