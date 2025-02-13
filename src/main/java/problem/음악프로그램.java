package problem;

import java.util.*;
import java.io.*;


public class 음악프로그램 {
    static int N, M;
    static Map<Integer, List<Integer>> graphMap;
    static int[] numPointed;
    public static void main(String[] args) throws Exception {
        init();

        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
            if (numPointed[i] == 0) {
                queue.addLast(i);
            }
        }

        List<Integer> answerList = new ArrayList<>();
        while (!queue.isEmpty()) {
            int curNode = queue.pollFirst();
            answerList.add(curNode);

            for (int i = 0; i < graphMap.get(curNode).size(); i++) {
                int destNode = graphMap.get(curNode).get(i);

                numPointed[destNode] -= 1;
                if (numPointed[destNode] == 0) {
                    queue.add(destNode);
                }
            }
        }


        if (answerList.size() != N) {
            System.out.println(0);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append(answerList.get(i) + 1);
            sb.append("\n");
        }
        System.out.println(sb.toString().substring(0, sb.length() - 1));


    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        numPointed = new int[N];
        graphMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int numOrder = Integer.parseInt(st.nextToken());
            int[] orders = new int[numOrder];
            for (int j = 0; j < numOrder; j++) {
                orders[j] = Integer.parseInt(st.nextToken()) - 1;
            }

            for (int j = 0; j < numOrder; j++) {
                for (int k = j + 1; k < numOrder; k++) {
                    graphMap.get(orders[j]).add(orders[k]);
                    numPointed[orders[k]] += 1;
                }
            }
        }
    }
}