package problem;

import java.util.*;
import java.io.*;


public class 피보나치함수 {
    static int N;
    static int[] numList;
    static int[] zeroList;
    static int[] oneList;

    public static void main(String[] args) throws Exception {
        init();
        StringBuilder sb = new StringBuilder();

        zeroList[0] = 1;
        zeroList[1] = 0;
        oneList[0] = 0;
        oneList [1] = 1;
        for (int i = 2; i < 41; i++) {
            zeroList[i] = zeroList[i - 1] + zeroList[i - 2];
            oneList[i] = oneList[i - 1] + oneList[i - 2];
        }

        for (int i = 0; i < N; i++) {
            sb.append(zeroList[numList[i]] + " " + oneList[numList[i]] + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        numList = new int[N];
        zeroList = new int[41];
        oneList = new int[41];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            numList[i] = Integer.parseInt(st.nextToken());
        }
    }
}