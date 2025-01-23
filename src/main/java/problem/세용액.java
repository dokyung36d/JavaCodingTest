package problem;

import java.util.*;
import java.io.*;

public class 세용액 {
    static int N;
    static long[] liquidList;

    public static void main(String[] args) throws Exception {
        init();

        long[] answerList = new long[3];
        Arrays.sort(liquidList);

        int startIndex, midIndex, endIndex;

        long answer = Long.MAX_VALUE;
        long sum;

        for (int i = 0; i < N - 2; i++) {
            startIndex = i;
            midIndex = i + 1;
            endIndex = N - 1;

            while (midIndex < endIndex) {
                sum = liquidList[startIndex] + liquidList[midIndex] + liquidList[endIndex];
                if (Math.abs(sum) < answer) {
                    answer = Math.abs(sum);
                    answerList[0] = liquidList[startIndex];
                    answerList[1] = liquidList[midIndex];
                    answerList[2] = liquidList[endIndex];
                }

                if (sum < 0) {
                    midIndex += 1;
                }
                else {
                    endIndex -= 1;
                }

            }
        }

        Arrays.sort(answerList);
        System.out.print(answerList[0] + " ");
        System.out.print(answerList[1] + " ");
        System.out.print(answerList[2]);

    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        liquidList = new long[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            liquidList[i] = Long.parseLong(st.nextToken());
        }
    }
}