package problem;

import java.util.*;
import java.io.*;



public class 수나누기게임 {
    static int N;
    static int[] numList, scoreList;
    static Map<Integer, Integer> numToIndexMap;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {

        for (int i = 0; i < N; i++) {
            int num = numList[i];
            List<Integer> divisorList = getDivisors(num);

            for (int divisor : divisorList) {
                if (numToIndexMap.get(divisor) == null) { continue; }

                scoreList[numToIndexMap.get(divisor)] += 1;
                scoreList[i] -= 1;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append(scoreList[i] + " ");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static List<Integer> getDivisors(int num) {
        Map<Integer, Integer> divisorMap = new HashMap<>();

        for (int i = 1; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                divisorMap.put(i, 1);
                divisorMap.put(num / i, 1);
            }
        }

        return new ArrayList<>(divisorMap.keySet());
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        numList = new int[N];
        scoreList = new int[N];

        numToIndexMap = new HashMap<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());

            numToIndexMap.put(numList[i], i);
        }
    }
}
