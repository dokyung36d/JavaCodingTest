package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N;
    static int[] numList;
    static final int MAX_NUM = 1000001;


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        Map<Integer, Integer> numToIndexMap = new HashMap<>();

        for (int i = 0; i < N; i++) {
            numToIndexMap.put(numList[i], i);
        }


        int[] scoreList = new int[MAX_NUM];

        for (int i = 0; i < N; i++) {
            for (int multipliedNum = 2 * numList[i]; multipliedNum < MAX_NUM; multipliedNum += numList[i]) {
                if (numToIndexMap.get(multipliedNum) == null) { continue; }

                scoreList[numList[i]] += 1;
                scoreList[multipliedNum] -= 1;
            }
        }


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append(scoreList[numList[i]]);
            sb.append(" ");
        }


        System.out.println(sb.toString().substring(0, sb.length() - 1));
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