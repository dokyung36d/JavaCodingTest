package problem;

import java.util.*;
import java.io.*;


public class 계단오르기 {
    static int N;
    static int[] stairList;

    public static void main(String[] args) throws Exception {
        init();

        if (N == 1) {
            System.out.println(stairList[0]);
            return;
        }

        if (N == 2) {
            System.out.println(stairList[0] + stairList[1]);
            return;
        }
        int[] firstDPList = new int[N];
        int[] secondDPList = new int[N];

        firstDPList[0] = stairList[0];
        firstDPList[1] = stairList[1];
        secondDPList[1] = stairList[0] + stairList[1];

        for (int i = 2; i < N; i++) {
            int value = stairList[i];
            firstDPList[i] = Math.max(firstDPList[i - 2] + value, secondDPList[i - 2] + value);
            secondDPList[i] = firstDPList[i - 1] + value;
        }

        System.out.println(Math.max(firstDPList[N - 1], secondDPList[N - 1]));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        stairList = new int[N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            stairList[i] = Integer.parseInt(st.nextToken());
        }
    }
}