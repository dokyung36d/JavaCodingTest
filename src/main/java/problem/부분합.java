package problem;

import java.util.*;
import java.io.*;


public class 부분합 {
    static int N, S;
    static int[] numList, sumList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }


    public static void solution() {
        int answer = Integer.MAX_VALUE / 2;

        int leftIndex = 0;
        int rightIndex = 1;

        while (rightIndex <= N) {
            int sumValue = sumList[rightIndex] - sumList[leftIndex];
            if (sumValue >= S) {
                answer = Math.min(answer, rightIndex - leftIndex);
            }


            if (sumValue >= S) {
                leftIndex += 1;
            }
            else {
                rightIndex += 1;
            }
        }


        if (answer == Integer.MAX_VALUE / 2) {
            System.out.println(0);
        }
        else {
            System.out.println(answer);
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        S = Integer.parseInt(st.nextToken());


        st = new StringTokenizer(br.readLine());
        sumList = new int[N + 1];
        for (int i = 0; i < N; i++) {
            sumList[i + 1] = sumList[i] + Integer.parseInt(st.nextToken());
        }

    }

}

