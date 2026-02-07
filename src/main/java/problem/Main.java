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
        int leftAnswer = 0;
        int rightAnswer = 0;
        int minSum = Integer.MAX_VALUE;

        int leftIndex = 0;
        int rightIndex = N - 1;

        while (leftIndex < rightIndex) {
            int sum = numList[leftIndex] + numList[rightIndex];

            if (Math.abs(sum) < minSum) {
                leftAnswer = numList[leftIndex];
                rightAnswer = numList[rightIndex];

                minSum = Math.abs(sum);
            }

            if (sum < 0) {
                leftIndex += 1;
                continue;
            }

            if (sum > 0) {
                rightIndex -= 1;
                continue;
            }

            if (sum == 0) {
                break;
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
    }

}
