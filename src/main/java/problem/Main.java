package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[] isPrime = new int[N + 1];
        Arrays.fill(isPrime, 1);
        List<Integer> primeList = new ArrayList<>();

        for (int i = 2; i < N + 1; i++) {
            if (isPrime[i] == 0) { continue; }
            primeList.add(i);

            for (int num = i * 2; num < N + 1; num += i) {
                isPrime[num] = 0;
            }
        }


        List<Integer> sumList = new ArrayList<>();
        sumList.add(0);
        for (int i = 0; i < primeList.size(); i++) {
            sumList.add(sumList.get(sumList.size() - 1) + primeList.get(i));
        }


        int answer = 0;

        int leftIndex = 0;
        int rightIndex = 1;
        while (rightIndex < sumList.size()) {
            int sumValue = sumList.get(rightIndex) - sumList.get(leftIndex);

            if (sumValue == N) {
                answer += 1;
                leftIndex += 1;
            }

            if (sumValue < N) {
                rightIndex += 1;
                continue;
            }

            if (sumValue > N) {
                leftIndex += 1;
                continue;
            }
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
    }
}