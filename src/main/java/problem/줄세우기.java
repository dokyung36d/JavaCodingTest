package problem;

import java.io.*;
import java.util.*;

public class 줄세우기 {
    static int N, M;
    static Map<Integer, List<Integer>> graphMap;
    static int[] numPointedList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
            if (numPointedList[i] != 0) { continue; }
            queue.add(i);
        }


        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            int num = queue.pollFirst();
            sb.append(num + 1);
            sb.append(" ");

            for (int nearNum : graphMap.get(num)) {
                numPointedList[nearNum] -= 1;

                if (numPointedList[nearNum] == 0) {
                    queue.add(nearNum);
                }
            }
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());


        numPointedList = new int[N];
        graphMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }


        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int num1 = Integer.parseInt(st.nextToken()) - 1;
            int num2 = Integer.parseInt(st.nextToken()) - 1;

            graphMap.get(num1).add(num2);
            numPointedList[num2] += 1;
        }
    }
}
