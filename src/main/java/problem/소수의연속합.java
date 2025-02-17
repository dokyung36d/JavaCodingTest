package problem;

import java.util.*;
import java.io.*;


public class 소수의연속합 {
    static int N;

    public static void main(String[] args) throws Exception {
        init();
        int[] primeFlagList = new int[N + 1];
        Arrays.fill(primeFlagList, 1);
        primeFlagList[0] = 0;
        primeFlagList[1] = 0;

        for (int i = 2; i < (int) Math.sqrt(N) + 1; i++) {
            if (primeFlagList[i] == 0) { continue; }

            for (int j = i + 1; j < primeFlagList.length; j++) {
                if (j % i == 0) {
                    primeFlagList[j] = 0;
                }
            }
        }



        List<Integer> primeList = new ArrayList<>();
        for (int i = 0; i < primeFlagList.length; i++) {
            if (primeFlagList[i] == 1) {
                primeList.add(i);
            }
        }

        List<Long> primeCumulativeSumList = new ArrayList<>();
        primeCumulativeSumList.add(0l);
        for (int i = 0; i < primeList.size(); i++) {
            long prevCumulativeSum = primeCumulativeSumList.get(i);
            primeCumulativeSumList.add(prevCumulativeSum + primeList.get(i));
        }

        long answer = 0;
        for (int i = 0; i < primeCumulativeSumList.size(); i++) {
            long curSum = primeCumulativeSumList.get(i);
            long rest = N + curSum;
            int searchIndex = Collections.binarySearch(primeCumulativeSumList, rest);
            if (searchIndex >= 0) {
                answer += 1;
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
