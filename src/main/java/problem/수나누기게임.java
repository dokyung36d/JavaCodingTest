package problem;

import java.util.*;
import java.io.*;


import java.util.*;
import java.io.*;

public class 수나누기게임 {
    static int N;
    static int[] numList;
    static Map<Integer, Integer> numToIndexMap;
    static final int MAX_NUM = 1000001;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[] scoreList = new int[N];

        for (int i = 0; i < N; i++) {
            int num = numList[i];

            for (int multiplyNum = num; multiplyNum < MAX_NUM; multiplyNum += num) {
                if (numToIndexMap.get(multiplyNum) == null) { continue; }

                scoreList[i] += 1;
                scoreList[numToIndexMap.get(multiplyNum)] -= 1;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append(scoreList[i]);
            sb.append(" ");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        numList = new int[N];
        numToIndexMap = new HashMap<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());
            numToIndexMap.put(numList[i], i);
        }
    }
}