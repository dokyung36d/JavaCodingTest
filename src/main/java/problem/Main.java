package problem;


import java.nio.file.Path;
import java.util.*;
import java.io.*;



public class Main {
    static int C, N;
    static int[][] cityMatrix;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] dpMatrix = new int[N + 1][C + 1];
        for (int i = 0; i < N + 1; i++) {
            Arrays.fill(dpMatrix[i], Integer.MAX_VALUE / 2);
            dpMatrix[i][0] = 0;
        }

        for (int i = 0; i < N; i++) {
            int cost = cityMatrix[i][0];
            int people = cityMatrix[i][1];

            for (int j = 1; j < C + 1; j++) {

                for (int curPeople = 0; curPeople <= j; curPeople++) {
                    int multiply;
                    if (curPeople % people == 0) {
                        multiply = curPeople / people;
                    }
                    else {
                        multiply = curPeople / people + 1;
                    }

                    dpMatrix[i + 1][j] = Math.min(dpMatrix[i + 1][j], dpMatrix[i][j - curPeople] + cost * multiply);
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

        cityMatrix = new int[N][2];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int cost = Integer.parseInt(st.nextToken());
            int people = Integer.parseInt(st.nextToken());

            cityMatrix[i][0] = cost;
            cityMatrix[i][1] = people;
        }
    }

}
