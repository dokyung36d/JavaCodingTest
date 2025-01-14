package problem;

import java.io.*;
import java.util.*;

public class 하늘에서별똥별이 {
    static int N, M, L, K;
    static Map<Pos, Integer> starMap = new HashMap<>();
    static Pos[] starList;

    public static class Pos {
        int row;
        int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }
            Pos anotherPos = (Pos) obj;
            if (this.row == anotherPos.row  && this.col == anotherPos.col) {
                return true;
            }

            return false;

        }
    }

    public static void main(String[] args) throws Exception {
        int answer = 0;
        init();

        for (int i = 0; i < K; i++) {
            for (int j = 0; j < K; j++) {
                int startRow = starList[i].row;
                int startCol = starList[j].col;
                Pos standardPos = new Pos(startRow, startCol);
                int value = 0;

                for (int v = 0; v < K; v++) {
                    value += checkBelong(standardPos, starList[v]);
                }

                if (value > answer) {
                    answer = value;
                }
            }
        }

        System.out.println(K - answer);
    }


    public static int checkBelong(Pos standardPoint, Pos anotherStar) {
        if (standardPoint.row <= anotherStar.row && anotherStar.row <= standardPoint.row + L
                && standardPoint.col <= anotherStar.col && anotherStar.col <= standardPoint.col + L) {
            return 1;
        }

        return 0;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        starList = new Pos[K];

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            int col = Integer.parseInt(st.nextToken());
            int row = Integer.parseInt(st.nextToken());
            starMap.put(new Pos(row, col), 1);
            starList[i] = new Pos(row, col);
        }
    }
}