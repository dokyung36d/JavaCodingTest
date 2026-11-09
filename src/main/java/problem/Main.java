package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static List<Integer> primeNumList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        List<Integer> sumList = new ArrayList<>();
        sumList.add(0);
        for (int primeNum : primeNumList) {
            sumList.add(sumList.get(sumList.size() - 1) + primeNum);
        }


        int leftIndex = 0;
        int rightIndex = 1;

        int answer = 0;
        while (rightIndex < sumList.size()) {
            int sumValue = sumList.get(rightIndex) - sumList.get(leftIndex);

            if (sumValue == N) {
                answer += 1;
                leftIndex += 1;
                continue;
            }

            if (sumValue > N) {
                leftIndex += 1;
                continue;
            }

            rightIndex += 1;
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());


        primeNumList = new ArrayList<>();
        int[] dpList = new int[N + 1];
        Arrays.fill(dpList, 1);
        dpList[0] = 0;
        dpList[1] = 1;

        for (int i = 2; i < N + 1; i++) {
            if (dpList[i] == 0) { continue; }
            primeNumList.add(i);

            for (int num = 2 * i; num <= N; num += i) {
                dpList[num] = 0;
            }
        }
    }
}
