package problem;

import java.util.*;
import java.io.*;



public class 소수의연속합 {
    static int N;
    static List<Integer> primeNumberList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        setPrimeNumberList();

        List<Integer> sumList = new ArrayList<>();
        sumList.add(0);
        for (int i = 0; i < primeNumberList.size(); i++) {
            sumList.add(sumList.get(sumList.size() - 1) + primeNumberList.get(i));
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
            else {
                rightIndex += 1;
                continue;
            }
        }


        System.out.println(answer);
    }

    public static void setPrimeNumberList() {
        primeNumberList = new ArrayList<>();

        int[] isPrime = new int[N + 1];
        Arrays.fill(isPrime, 1);
        isPrime[0] = 0;
        isPrime[1] = 1;

        for (int i = 2; i < N + 1; i++) {
            if (isPrime[i] == 0) { continue; }
            primeNumberList.add(i);

            for (int j = 2 * i; j < N + 1; j += i) {
                isPrime[j] = 0;
            }
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
    }
}