package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N, C;
    static int[][] infoMatrix;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] dpMatrix = new int[N + 1][C + 1];

        for (int i = 0; i < N + 1; i++) {
            for (int j = 1; j < C + 1; j++) {
                dpMatrix[i][j] = Integer.MAX_VALUE / 2;
            }
        }


        for (int i = 0; i < N; i++) {
            int cost = infoMatrix[i][0];
            int people = infoMatrix[i][1];

            for (int j = 0; j < C + 1; j++) {
                for (int prevPeople = 0; prevPeople <= j; prevPeople++) {
                    int difference = j - prevPeople;
                    int numMultiply;

                    if (difference == 0) {
                        numMultiply = 0;
                    }
                    else if (difference % people == 0) {
                        numMultiply = difference / people;
                    }
                    else {
                        numMultiply = difference / people + 1;
                    }


                    dpMatrix[i + 1][j] = Math.min(dpMatrix[i + 1][j], dpMatrix[i][prevPeople] + cost * numMultiply);
                }
            }
        }

        System.out.println(dpMatrix[N][C]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        C = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());


        infoMatrix = new int[N][2];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            infoMatrix[i][0] = Integer.parseInt(st.nextToken());
            infoMatrix[i][1] = Integer.parseInt(st.nextToken());


        }
    }
}