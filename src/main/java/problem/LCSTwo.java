package problem;

import java.io.*;
import java.nio.Buffer;
import java.util.*;

public class LCSTwo {
    static String str1;
    static String str2;
    static int[][] dpMatrix;

    public static class Pos {
        int row;
        int col;
        int value;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public boolean checkValidIndex() {
            if (this.row < 0 || this.col < 0) {
                return false;
            }

            return true;
        }

        public int getValue() {
            return dpMatrix[this.row][this.col];
        }

        public Pos addPos(Pos anotherPos) {
            return new Pos(this.row + anotherPos.row, this.col + anotherPos.col);
        }
    }


    public static void main(String[] args) throws Exception {
        init();

        int str1Length = str1.length();
        int str2Length = str2.length();
        dpMatrix = new int[str1Length][str2Length];

        int flag = 0;
        for (int i = 0; i < str1Length; i++) {
            if (str1.charAt(i) == str2.charAt(0)) {
                flag = 1;
                dpMatrix[i][0] = 1;
                continue;
            }

            if (flag == 1) {
                dpMatrix[i][0] = 1;
            }
            else {
                dpMatrix[i][0] = 0;
            }
        }

        flag = 0;
        for (int i = 0; i < str2Length; i++) {
            if (str2.charAt(i) == str1.charAt(0)) {
                flag = 1;
                dpMatrix[0][i] = 1;
                continue;
            }

            if (flag == 1) {
                dpMatrix[0][i] = 1;
            }
            else {
                dpMatrix[0][i] = 0;
            }
        }

        for (int i = 1; i < str1Length; i++) {
            for (int j = 1; j < str2Length; j++) {
                if (str1.charAt(i) == str2.charAt(j)) {
                    dpMatrix[i][j] = dpMatrix[i - 1][j - 1] + 1;
                }

                else {
                    dpMatrix[i][j] = Math.max(dpMatrix[i - 1][j], dpMatrix[i][j - 1]);
                }
            }
        }
        System.out.println(dpMatrix[str1Length - 1][str2Length - 1]);
        System.out.println(getLongestString());

    }

    public static String getLongestString() throws Exception {
        StringBuilder sb = new StringBuilder();

        Pos pos = new Pos(str1.length() - 1, str2.length() - 1);

        while (pos.row >= 1 && pos.col >= 1) {
            Pos leftPos = pos.addPos(new Pos(0, -1));
            Pos upPos = pos.addPos(new Pos(-1, 0));
            Pos upLeftPos = pos.addPos(new Pos(-1, -1));

            if (str1.charAt(pos.row) == str2.charAt(pos.col)) {
                sb.append(str1.charAt(pos.row));
                pos = upLeftPos;
            }
            else {
                if (upPos.getValue() >= leftPos.getValue()) {
                    pos = upPos;
                }
                else {
                    pos = leftPos;
                }
            }
        }

        if (pos.getValue() == 0) {
            return sb.reverse().toString();
        }

        if (pos.row == 0) {
            sb.append(str1.charAt(0));
            return sb.reverse().toString();
        }


        sb.append(str2.charAt(0));
        return sb.reverse().toString();

//            if (pos.row >= 1 && pos.col >= 1) {
//                if (leftPos.value == upLeftPos.value && upPos.value == upLeftPos.value) {
//                    pos = upLeftPos;
//                }
//
//                else if (leftPos.value == pos.value && upPos.value < pos.value && upLeftPos.value < pos.value) {
//                    pos = leftPos;
//                }
//
//                else if (upPos.value == pos.value && leftPos.value < pos.value && upLeftPos.value < pos.value) {
//                    pos = upPos;
//                }
//            }

//            else if (pos.row >= 1 && pos.col < 0) {
//
//            }
//
//            else if (pos.row < 0 && pos.col >= 1) {
//
//            }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        str1 = st.nextToken();

        st = new StringTokenizer(br.readLine());
        str2 = st.nextToken();
    }
}