package problem;

import java.util.*;
import java.io.*;


public class 나무자르기 {
    static int N, M;
    static int minHeight, maxHeight;
    static int[] treeList;

    public static void main(String[] args) throws Exception {
        init();

        int answer = 0;
        while (minHeight <= maxHeight) {
            int midHeight = (minHeight + maxHeight) / 2;
            if (isPossible(midHeight)) {
                answer = Math.max(answer, midHeight);
                minHeight = midHeight + 1;
            }
            else {
                maxHeight = midHeight - 1;
            }
        }

        System.out.println(answer);
    }

    public static boolean isPossible(int height) {
        int totalValue = M;
        for (int i = 0; i < N; i++) {
            if (treeList[i] < height) { continue; }

            totalValue -= (treeList[i] - height);
            if (totalValue <= 0) {
                return true;

            }
        }

        return false;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        minHeight = 0;
        maxHeight = 0;

        treeList = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int height = Integer.parseInt(st.nextToken());
            if (height > maxHeight) {
                maxHeight = height;
            }
            treeList[i] = height;

        }
    }

}