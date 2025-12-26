package problem;

import java.util.*;
import java.io.*;

public class Main {
	static int N, M;
    static int[] numPointedList;
    static Map<Integer, List<Integer>> graphMap;
    static PriorityQueue<Integer> pq;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < N; i++) {
            if (numPointedList[i] != 0) { continue; }
            pq.add(i);
        }

        while (!pq.isEmpty()) {
            int num = pq.poll();
            sb.append(num + 1 + " ");

            for (int next : graphMap.get(num)) {
                numPointedList[next] -= 1;

                if (numPointedList[next] == 0) {
                    pq.add(next);
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

            int from = Integer.parseInt(st.nextToken()) - 1;
            int to = Integer.parseInt(st.nextToken()) - 1;

            numPointedList[to] += 1;
            graphMap.get(from).add(to);
        }

        pq = new PriorityQueue<>();
    }
}
