package problem;

import java.util.*;
import java.io.*;


public class 수나누기게임 {
    static int N;
    static int[] numList;
    static Map<Integer, Integer> numMap, answerMap;
    static int maxNum;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[] dpMatrix = new int[maxNum + 1];

        for (int num : numList) {
            for (int multipliedNum = 2 * num; multipliedNum <= maxNum; multipliedNum += num) {
                if (numMap.get(multipliedNum) == null) { continue; }

                answerMap.put(num, answerMap.get(num) + 1);
                answerMap.put(multipliedNum, answerMap.get(multipliedNum) - 1);
            }
        }


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append(answerMap.get(numList[i]));
            sb.append(" ");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        numList = new int[N];
        numMap = new HashMap<>();
        answerMap = new HashMap<>();

        st = new StringTokenizer(br.readLine());
        maxNum = 0;
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());

            numMap.put(numList[i], 1);
            answerMap.put(numList[i], 0);
            maxNum = Math.max(maxNum, numList[i]);
        }
    }
}