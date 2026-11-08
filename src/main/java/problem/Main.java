package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N, S;
    static List<Integer> sumList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int leftIndex = 0;
        int rightIndex = 1;

        int answer = Integer.MAX_VALUE / 2;

        while (rightIndex < sumList.size()) {
            int sumValue = sumList.get(rightIndex) - sumList.get(leftIndex);

            if (sumValue >= S) {
                answer = Math.min(answer, rightIndex - leftIndex);
            }


            if (sumValue < S) {
                rightIndex += 1;
            }
            else {
                leftIndex += 1;
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

        sumList = new ArrayList<>();
        sumList.add(0);


        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int num = Integer.parseInt(st.nextToken());

            sumList.add(sumList.get(sumList.size() - 1) + num);
        }
    }
}
