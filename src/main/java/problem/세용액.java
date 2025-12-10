package problem;

import java.util.*;
import java.io.*;

public class 세용액 {
    static int N;
    static int[] numList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        long minSum = Long.MAX_VALUE / 2;
        int[] answerNumList = new int[3];

        for (int i = 0; i < N - 2; i++) {
            int leftIndex = i + 1;
            int rightIndex = N - 1;

            while (leftIndex < rightIndex) {
                long sumValue = (long) numList[i] + (long) numList[leftIndex] + (long) numList[rightIndex];

                if (Math.abs(sumValue) < minSum) {
                    minSum = Math.abs(sumValue);
                    answerNumList[0] = numList[i];
                    answerNumList[1] = numList[leftIndex];
                    answerNumList[2] = numList[rightIndex];
                }

                if (sumValue < 0) {
                    leftIndex += 1;
                }
                else {
                    rightIndex -= 1;
                }
            }
        }

        System.out.println(answerNumList[0] + " " + answerNumList[1] + " " + answerNumList[2]);
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
