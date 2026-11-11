package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[][] infoMatrix;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] dpMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dpMatrix[i], Integer.MAX_VALUE / 2);
            dpMatrix[i][i] = 0;
        }


        for (int i = 0; i < N - 1; i++) {
            dpMatrix[i][i + 1] = infoMatrix[i][0] * infoMatrix[i][1] * infoMatrix[i + 1][1];
        }

        for (int gap = 2; gap < N; gap++) {
            for (int startIndex = 0; startIndex + gap < N; startIndex++) {
                int endIndex = startIndex + gap;

                for (int offest = 0; offest < gap; offest++) {
                    int midIndex = startIndex + offest;
                    dpMatrix[startIndex][endIndex] = Math.min(dpMatrix[startIndex][endIndex],
                            dpMatrix[startIndex][midIndex] + dpMatrix[midIndex + 1][endIndex] +
                            infoMatrix[startIndex][0] * infoMatrix[midIndex][1] * infoMatrix[endIndex][1]);
                }
            }
        }

        System.out.println(dpMatrix[0][N - 1]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        infoMatrix = new int[N][2];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            infoMatrix[i][0] = Integer.parseInt(st.nextToken());
            infoMatrix[i][1] = Integer.parseInt(st.nextToken());
        }
    }
}
