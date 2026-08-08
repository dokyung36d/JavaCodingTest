package problem;

import java.util.*;
import java.io.*;


public class Main {
    static String str1, str2;
    static int N, M;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static int[][] dpMatrix;

    public static class Pos {
        int row;
        int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Pos addPos(Pos direction) {
            return new Pos(this.row + direction.row, this.col + direction.col);
        }

        public boolean isValidIndex() {
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= M) {
                return false;
            }

            return true;
        }

        public Pos getLeftPos() {
            return new Pos(this.row, this.col - 1);
        }

        public Pos getUpPos() {
            return new Pos(this.row - 1, this.col);
        }

        public Pos getUpLeftPos() {
            return new Pos(this.row - 1, this.col - 1);
        }

        public int getDpValue() {
            return dpMatrix[this.row][this.col];
        }
    }


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        dpMatrix = new int[N][M];

        int flag = 0;
        for (int i = 0; i < M; i++) {
            if (flag == 1)  {
                dpMatrix[0][i] = 1;
                continue;
            }

            if (str1.charAt(0) == str2.charAt(i)) {
                flag = 1;
                dpMatrix[0][i] = 1;
            }
        }


        flag = 0;
        for (int i = 0; i < N; i++) {
            if (flag == 1) {
                dpMatrix[i][0] = 1;
                continue;
            }

            if (str1.charAt(i) == str2.charAt(0)) {
                flag = 1;
                dpMatrix[i][0] = 1;
            }
        }


        for (int i = 1; i < M; i++) {
            for (int j = 1; j < N; j++) {
                dpMatrix[j][i] = Math.max(dpMatrix[j][i - 1], dpMatrix[j - 1][i]);
                if (str1.charAt(j) == str2.charAt(i)) {
                    dpMatrix[j][i] = Math.max(dpMatrix[j - 1][i - 1] + 1, dpMatrix[j][i]);
                }


            }
        }


        StringBuilder sb = new StringBuilder();
        Pos curPos = new Pos(N - 1, M - 1);
        while (curPos.row >= 1 && curPos.col >= 1) {
            Pos leftPos = curPos.getLeftPos();
            Pos upPos = curPos.getUpPos();

            Pos upLeftPos = curPos.getUpLeftPos();

            if (leftPos.getDpValue() == curPos.getDpValue()) {
                curPos = leftPos;
                continue;
            }

            if (upPos.getDpValue() == curPos.getDpValue()) {
                curPos = upPos;
                continue;
            }


            sb.append(str1.charAt(curPos.row));
            curPos = upLeftPos;
        }


        if (sb.length() != dpMatrix[N - 1][M - 1] && curPos.row == 0) {
            sb.append(str1.charAt(0));
        }
        else if (sb.length() != dpMatrix[N - 1][M - 1] && curPos.col == 0) {
            sb.append(str2.charAt(0));
        }

        System.out.println(dpMatrix[N - 1][M - 1] + "\n" + sb.reverse().toString());


    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        str1 = st.nextToken();
        N = str1.length();


        st = new StringTokenizer(br.readLine());
        str2 = st.nextToken();
        M = str2.length();
    }
}