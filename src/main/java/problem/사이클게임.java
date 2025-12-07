package problem;

import java.util.*;
import java.io.*;

public class 사이클게임 {
    static int N, M;
    static int[] parentList;
    static int[][] commadList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        for (int i = 0; i < M; i++) {
            int num1Parent = findParent(commadList[i][0]);
            int num2Parent = findParent(commadList[i][1]);

            if (num1Parent == num2Parent) {
                System.out.println(i + 1);
                return;
            }

            union(num1Parent, num2Parent);
        }

        System.out.println(0);
    }

    public static int findParent(int num) {
        if (num == parentList[num]) { return num; }

        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent == num2Parent) { return; }

        parentList[Math.max(num1Parent, num2Parent)] = Math.min(num1Parent, num2Parent);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        parentList = new int[N];
        for (int i = 0; i < N; i++) {
            parentList[i] = i;
        }

        commadList = new int[M][2];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            commadList[i][0] = Integer.parseInt(st.nextToken());
            commadList[i][1] = Integer.parseInt(st.nextToken());
        }
    }
}