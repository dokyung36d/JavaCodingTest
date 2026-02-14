package problem;

import java.util.*;
import java.io.*;



public class Main {
    static int N, S;
    static int[] numList;
    static int[] sumList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int leftIndex = 0;
        int rightIndex = 1;

        int answer = Integer.MAX_VALUE / 2;
        while (rightIndex <= N) {
            int sumValue = sumList[rightIndex] - sumList[leftIndex];
            if (sumValue >= S) {
                answer = Math.min(answer, rightIndex - leftIndex);
                leftIndex += 1;
                continue;
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

        numList = new int[N];
        sumList = new int[N + 1];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());

            sumList[i + 1] = sumList[i] + numList[i];
        }
    }
}
