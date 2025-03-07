package problem;

import java.util.*;
import java.io.*;


public class 타일링 {
    static int N;
    static int[] dpList;

    public static void main(String[] args) throws Exception {
        init();
        dpList[0] = 1;
        dpList[1] = 1;
        for (int i = 2; i < N + 1; i++) {
            dpList[i] = dpList[i - 1] + dpList[i - 2];
            dpList[i] = dpList[i] % 10007;
        }

        System.out.println(dpList[N]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        dpList = new int[N + 1];
    }
}