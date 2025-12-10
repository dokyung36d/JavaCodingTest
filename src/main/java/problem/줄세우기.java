package problem;

import java.util.*;
import java.io.*;

public class 줄세우기 {
    static int N, M;
    static int[] numPointedList;
    static Map<Integer, List<Integer>> graphMap;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        StringBuilder sb = new StringBuilder();

        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
            if (numPointedList[i] != 0) { continue; }

            queue.add(i);
        }


        while (!queue.isEmpty()) {
            int num = queue.pollFirst();
            sb.append(num + 1);
            sb.append(" ");

            for (int nearNum : graphMap.get(num)) {
                numPointedList[nearNum] -= 1;
                if (numPointedList[nearNum] != 0) { continue; }

                queue.add(nearNum);
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

            int from = Integer.parseInt(st.nextToken()) - 1;
            int to = Integer.parseInt(st.nextToken()) - 1;

            numPointedList[to] += 1;
            graphMap.get(from).add(to);
        }
    }
}