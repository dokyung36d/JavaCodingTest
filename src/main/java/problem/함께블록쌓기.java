package problem;

import java.util.*;
import java.io.*;


public class 함께블록쌓기 {
    static int N, M, H;
    static Map<Integer, List<Integer>> giftMap;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] dpMatrix = new int[N + 1][H + 1];
        dpMatrix[0][0] = 1;

        for (int i = 1; i <= N; i++) {
            dpMatrix[i] = Arrays.copyOf(dpMatrix[i - 1], H + 1);
            for (int j = 1; j <= H; j++) {
                for (int gift : giftMap.get(i)) {
                    if (gift > j) { continue; }
                    dpMatrix[i][j] += dpMatrix[i - 1][j - gift];
                }

                dpMatrix[i][j] = dpMatrix[i][j] % 10007;
            }

        }

        System.out.println(dpMatrix[N][H]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());

        giftMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            giftMap.put(i + 1, new ArrayList<>());
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            while (st.hasMoreTokens()) {
                int blockHeight = Integer.parseInt(st.nextToken());
                giftMap.get(i + 1).add(blockHeight);
            }
        }

    }
}