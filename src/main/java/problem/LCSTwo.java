package problem;

import java.io.*;
import java.util.*;

public class LCSTwo {
    static int N, M;
    static String string1, string2;
    static int[][] dpMatrix;
    static Pos upDirection = new Pos(-1, 0);
    static Pos leftDirection = new Pos(0, -1);
    static Pos upLeftDirection = new Pos(-1, -1);

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

        public int getValue() {
            return dpMatrix[this.row][this.col];
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        dpMatrix = new int[N][M];

        int rowFlag = 0;
        for (int i = 0; i < N; i++) {
            if (string1.charAt(i) == string2.charAt(0)) {
                rowFlag = 1;
            }

            if (rowFlag == 1) {
                dpMatrix[i][0] = 1;
            }
        }


        int colFlag = 0;
        for (int j = 0; j < M; j++) {
            if (string1.charAt(0) == string2.charAt(j)) {
                colFlag = 1;
            }

            if (colFlag == 1) {
                dpMatrix[0][j] = 1;
            }
        }



        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (string1.charAt(i) == string2.charAt(j)) {
                    dpMatrix[i][j] = dpMatrix[i - 1][j - 1] + 1;
                }
                else {
                    dpMatrix[i][j] = Math.max(dpMatrix[i - 1][j], dpMatrix[i][j - 1]);
                }
            }
        }


        StringBuilder sb = new StringBuilder();
        Pos curPos = new Pos(N - 1, M - 1);
        while (curPos.row != 0 && curPos.col != 0) {
            Pos leftPos = curPos.addPos(leftDirection);
            if (leftPos.isValidIndex() && curPos.getValue() == leftPos.getValue()) {
                curPos = leftPos;
                continue;
            }

            Pos upPos = curPos.addPos(upDirection);
            if (upPos.isValidIndex() && curPos.getValue() == upPos.getValue()) {
                curPos = upPos;
                continue;
            }

            Pos upLeftPos = curPos.addPos(upLeftDirection);
            if (upLeftPos.isValidIndex() && curPos.getValue() == upLeftPos.getValue() + 1) {
                sb.append(string1.charAt(curPos.row));
                curPos = upLeftPos;
            }
        }


        if (sb.length() != dpMatrix[N - 1][M - 1]) {
            if (curPos.row == 0) {
                sb.append(string1.charAt(0));
            }
            else {
                sb.append(string2.charAt(0));
            }
        }

        System.out.println(dpMatrix[N - 1][M - 1]);
        System.out.println(sb.reverse().toString());
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        string1 = st.nextToken();

        st = new StringTokenizer(br.readLine());
        string2 = st.nextToken();


        N = string1.length();
        M = string2.length();
    }
}