package problem;

import java.util.*;
import java.io.*;


public class 펠린드롬 {
    static int N;
    static int[] numList;
    static int M;
    static Query[] queryList;

    public static class Query {
        int from;
        int to;

        public Query(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }
    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] dpMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            dpMatrix[i][i] = 1;
        }

        for (int i = 0; i < N - 1; i++) {
            if (numList[i] == numList[i + 1]) {
                dpMatrix[i][i + 1] = 1;
            }
        }

        for (int gap = 2; gap <= N - 1; gap++) {
            for (int start = 0; start + gap < N; start++) {
                int end = start + gap;
                if (numList[start] == numList[end] && dpMatrix[start + 1][end - 1] == 1) {
                    dpMatrix[start][end] = 1;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < M; i++) {
            Query query = queryList[i];
            sb.append(dpMatrix[query.from][query.to]);
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        numList = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());
        queryList = new Query[M];

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int from = Integer.parseInt(st.nextToken()) - 1;
            int to = Integer.parseInt(st.nextToken()) - 1;
            queryList[i] = new Query(from, to);
        }
    }
}