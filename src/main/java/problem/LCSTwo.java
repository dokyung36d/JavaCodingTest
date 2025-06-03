package problem;

import java.util.*;
import java.io.*;


public class LCSTwo {
    static String string1, string2;
    static Pos upLeftDirection = new Pos(-1, -1);
    static Pos upDirection = new Pos(-1, 0);
    static Pos leftDirection = new Pos(0, -1);
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

        public int getValue() {
            return dpMatrix[this.row][this.col];
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int M = string1.length();
        int N = string2.length();
        dpMatrix = new int[N][M];


        int rowFlag = 0;
        int colFlag = 0;
        for (int i = 0; i < N; i++) {
            if (string1.charAt(0) == string2.charAt(i)) {
                rowFlag = 1;
            }
            if (rowFlag == 1) {
                dpMatrix[i][0] = 1;
            }
        }

        for (int i = 0; i < M; i++) {
            if (string1.charAt(i) == string2.charAt(0)) {
                colFlag = 1;
            }
            if (colFlag == 1) {
                dpMatrix[0][i] = 1;
            }
        }



        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (string2.charAt(i) == string1.charAt(j)) {
                    dpMatrix[i][j] = dpMatrix[i - 1][j - 1] + 1;
                }

                else {
                    dpMatrix[i][j] = Math.max(dpMatrix[i - 1][j],
                            dpMatrix[i][j - 1]);
                }
            }
        }


        StringBuilder sb = new StringBuilder();
        Pos curPos = new Pos(N - 1, M - 1);
        while (curPos.row >= 1 && curPos.col >= 1) {
            Pos upLeftPos = curPos.addPos(upLeftDirection);
            Pos upPos = curPos.addPos(upDirection);
            Pos leftPos = curPos.addPos(leftDirection);

            if (curPos.getValue() == upLeftPos.getValue()) {
                curPos = upLeftPos;
                continue;
            }

            if (curPos.getValue() == leftPos.getValue()) {
                curPos = leftPos;
                continue;
            }

            if (curPos.getValue() == upPos.getValue()) {
                curPos = upPos;
                continue;
            }

            sb.append(string2.charAt(curPos.row));
            curPos = upLeftPos;
        }


        if (curPos.getValue() == 0) {

        }

        else if (curPos.row == 0) {
            sb.append(string2.charAt(0));
        }
        else {
            sb.append(string1.charAt(0));
        }

        if (dpMatrix[N - 1][M - 1] == 0) {
            System.out.println(0);
            return;
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
    }
}