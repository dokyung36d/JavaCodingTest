package problem;

import java.util.*;
import java.io.*;


public class 텀프로젝트 {
    static int T;
    static int N;
    static int[] graphList;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        T = Integer.parseInt(st.nextToken());

        for (int i = 0; i < T; i++) {
            init();
            solution();
        }
    }

    public static void solution() {
        int[] visited = new int[N];
        int numCycleNode = 0;

        for (int i = 0; i < N; i++) {
            if (visited[i] == 1) { continue; }

            int visitOrder = 0;
            Map<Integer, Integer> visitOrderMap = new HashMap<>();
            visitOrderMap.put(i, visitOrder);

            int curNum = i;
            while (true) {
                visitOrder++;

                int nextNum = graphList[curNum];
                if (visited[nextNum] == 1) {
                    break;
                }

                if (visitOrderMap.get(nextNum) != null) {
                    numCycleNode += visitOrder - visitOrderMap.get(nextNum);
                    break;
                }


                visitOrderMap.put(nextNum, visitOrder);

                curNum = nextNum;
            }


            for (int visitedNum : visitOrderMap.keySet()) {
                visited[visitedNum] = 1;
            }
        }

        System.out.println(N - numCycleNode);
    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());


        st = new StringTokenizer(br.readLine());
        graphList = new int[N];
        for (int i = 0; i < N; i++) {
            graphList[i] = Integer.parseInt(st.nextToken()) - 1;
        }
    }
}
