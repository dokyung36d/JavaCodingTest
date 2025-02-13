package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N, M;
    static Map<Integer, List<Integer>> graphMap;
    static int[] numParentsList;

    public static void main(String[] args) throws Exception {
        init();
        StringBuilder sb = new StringBuilder();

        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
            if (numParentsList[i] == 0) {
                queue.add(i);
            }
        }


        while (!queue.isEmpty()) {
            int num = queue.pollFirst();
            sb.append(num + 1 + " ");

            for (int i = 0; i < graphMap.get(num).size(); i++) {
                int child = graphMap.get(num).get(i);
                numParentsList[child] -= 1;

                if (numParentsList[child] == 0) {
                    queue.add(child);
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

        numParentsList = new int[N];
        graphMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int parent = Integer.parseInt(st.nextToken()) - 1;
            int child = Integer.parseInt(st.nextToken()) - 1;

            graphMap.get(parent).add(child);
            numParentsList[child] += 1;
        }

    }
}