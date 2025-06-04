package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class 줄세우기 {
    static int N, M;
    static int[] numPrevList;
    static Map<Integer, List<Integer>> graphMap;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            if (numPrevList[i] == 0) {
                pq.add(i);
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            int curNum = pq.poll();
            sb.append(curNum + 1);
            sb.append(" ");

            for (int nextNum : graphMap.get(curNum)) {
                numPrevList[nextNum] -= 1;

                if (numPrevList[nextNum] == 0) {
                    pq.add(nextNum);
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

        numPrevList = new int[N];
        graphMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }


        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int from = Integer.parseInt(st.nextToken()) - 1;
            int to = Integer.parseInt(st.nextToken()) - 1;

            graphMap.get(from).add(to);
            numPrevList[to] += 1;
        }
    }

}
