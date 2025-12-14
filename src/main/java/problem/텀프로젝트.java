package problem;

import java.util.*;
import java.io.*;

public class 텀프로젝트 {
    static int T;
    static int N;
    static int[] numList, numPointedList;
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
        int answer = 0;
        int[] visited = new int[N];

        for (int i = 0; i < N; i++) {
            if (visited[i] == 1) { continue; }
            if (numPointedList[i] != 0) { continue; }


            int[] visitedInThisCycle = new int[N];
            int visitOrder = 1;
            int curNum = i;
            while (true) {
                visited[curNum] = 1;
                visitedInThisCycle[curNum] = visitOrder;

                int nextNum = numList[curNum];
                visitOrder += 1;

                if (visitedInThisCycle[nextNum] != 0) {
                    int numInCycle = visitOrder - visitedInThisCycle[nextNum];
                    int numVisitedInThisCycle = visitOrder - 1;
                    answer += (numVisitedInThisCycle - numInCycle);

                    break;
                }

                if (visited[nextNum] == 1) {
                    answer += (visitOrder - 1);
                    break;
                }

                curNum = nextNum;

            }
        }
        System.out.println(answer);

    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());

        numList = new int[N];
        numPointedList = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken()) - 1;
            numPointedList[numList[i]] += 1;
        }
    }

}
