package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class treeAttack {
    public static final int MAX_N = 100;
    public static final int MAX_M = 100;

    public static int n, m;
    public static int[][] attackArr = new int[2][2];

    public static int[] counter = new int[MAX_N];
    public static int answer = 0;



    public static void main_treeattack(String[] args) throws IOException {
        init();

        for (int i = 0; i < 2; i++) {
            int startRow = attackArr[i][0];
            int endRow = attackArr[i][1];

            for (int j = startRow; j<= endRow; j++) {
                if (counter[j] > 0) {
                    counter[j] -= 1;
                    answer -= 1;
                }
            }
        }

        System.out.println(answer);

    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        for (int i = 0; i< n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int  j = 0; j < m; j++) {
                int temp = Integer.parseInt(st.nextToken());

                if (temp == 1) {
                    counter[i] += 1;
                    answer += 1;
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            st = new StringTokenizer(br.readLine());

            attackArr[i][0] = Integer.parseInt(st.nextToken());
            attackArr[i][1] = Integer.parseInt(st.nextToken());

            attackArr[i][0] -= 1;
            attackArr[i][1] -= 1;
        }


    }
}
