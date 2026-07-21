package problem;

import java.util.*;
import java.io.*;


public class Main {
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


        int numOut = 0;
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            int curNum = queue.pollFirst();

            sb.append(curNum + 1);
            sb.append("\n");

            numOut += 1;


            for (int nearNum : graphMap.get(curNum)) {
                numPointedList[nearNum] -= 1;

                if (numPointedList[nearNum] == 0) {
                    queue.add(nearNum);
                }
            }
        }


        if (numOut == N) {
            System.out.println(sb.toString().substring(0, sb.length() - 1));
        }
        else {
            System.out.println(0);
        }
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

            int length = Integer.parseInt(st.nextToken());
            int[] orderList = new int[length];

            for (int j = 0; j < length; j++) {
                orderList[j] = Integer.parseInt(st.nextToken()) - 1;
            }


            for (int from = 0; from < length; from++) {
                for (int to = from + 1; to < length; to++) {
                    graphMap.get(orderList[from]).add(orderList[to]);
                    numPointedList[orderList[to]] += 1;
                }
            }
        }
    }
}