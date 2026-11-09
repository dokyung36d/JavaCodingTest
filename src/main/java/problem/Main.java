package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static Map<Integer, Integer> scoreMap;
    static Map<Integer, Integer> numToIndexMap, indexToNumMap;
    static final int MAX_NUM = 1000001;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        for (int num : scoreMap.keySet()) {
            for (int multiplied = 2 * num; multiplied < MAX_NUM; multiplied += num) {
                if (scoreMap.get(multiplied) != null) {
                    scoreMap.put(multiplied, scoreMap.get(multiplied) - 1);
                    scoreMap.put(num, scoreMap.get(num) + 1);
                }
            }
        }


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            int num = indexToNumMap.get(i);
            sb.append(scoreMap.get(num));
            sb.append(" ");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        scoreMap = new HashMap<>();
        numToIndexMap = new HashMap<>();
        indexToNumMap = new HashMap<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int num = Integer.parseInt(st.nextToken());

            scoreMap.put(num, 0);
            numToIndexMap.put(num, i);
            indexToNumMap.put(i, num);
        }
    }
}
