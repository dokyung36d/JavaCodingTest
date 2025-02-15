package problem;

import java.util.*;
import java.io.*;


public class 수나누기게임 {
    static int N;
    static int[] numList, sortedNumList;


    public static void main(String[] args) throws Exception {
        init();
        Map<Integer, Integer> scoreMap = new HashMap<>();
        Map<Integer, Integer> numMap = new HashMap<>();

        Arrays.sort(sortedNumList);
        for (int i = 0; i < N; i++) {
            scoreMap.put(numList[i], 0);
            numMap.put(numList[i], 1);
        }

        for (int i = 0; i < N; i++) {
            List<Integer> divisors = findDivisors(sortedNumList[i]);

            for (int divisor : divisors) {
                if (numMap.get(divisor) == null) { continue; }

                scoreMap.put(divisor, scoreMap.getOrDefault(divisor, 0) + 1);
                scoreMap.put(sortedNumList[i], scoreMap.getOrDefault(sortedNumList[i], 0) - 1);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            int num = numList[i];
            sb.append(scoreMap.get(num) +  " ");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }




    public static List<Integer> findDivisors(int num) {
        Map<Integer, Integer> divisorMap = new HashMap<>();
        for (int i = 1; i < Math.sqrt(num) + 1; i++) {
            if (num % i == 0) {
                divisorMap.put(i, 1);

                if (i != 1) {
                    divisorMap.put(num / i, 1);
                }
            }
        }

        return new ArrayList<>(divisorMap.keySet());
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        numList = new int[N];
        sortedNumList = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());
            sortedNumList[i] = numList[i];
        }
    }
}