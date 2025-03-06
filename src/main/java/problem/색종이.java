package problem;

import java.util.*;
import java.io.*;


public class 색종이 {
    static int N;
    static int[][] mainMatrix;
    static int numWhite;
    static int numBlue;

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
    }

    public static class Paper {
        Pos topLeftPos;
        int size;

        public Paper(Pos topLeftPos, int size) {
            this.topLeftPos = topLeftPos;
            this.size = size;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        recursive(new Paper(new Pos(0,0), N));


        System.out.println(numWhite);
        System.out.println(numBlue);
    }

    public static int recursive(Paper paper) {
        if (paper.size == 1) {
            if (mainMatrix[paper.topLeftPos.row][paper.topLeftPos.col] == 0) {
                numWhite += 1;
            }
            else {
                numBlue += 1;
            }
            return 1;
        }
        if (isAllSame(paper)) { return 1; }

        int total = 0;
        total += recursive(new Paper(paper.topLeftPos, paper.size / 2));
        total += recursive(new Paper(paper.topLeftPos.addPos(new Pos(0, paper.size / 2)), paper.size / 2));
        total += recursive(new Paper(paper.topLeftPos.addPos(new Pos(paper.size / 2, 0)), paper.size / 2));
        total += recursive(new Paper(paper.topLeftPos.addPos(new Pos(paper.size / 2, paper.size / 2)), paper.size / 2));

        return total;
    }

    public static boolean isAllSame(Paper paper) {
        int whiteFlag = 0;
        int blueFlag = 0;

        for (int i = 0; i < paper.size; i++) {
            for (int j = 0; j < paper.size; j++) {
                Pos pos = paper.topLeftPos.addPos(new Pos(i, j));
                if (mainMatrix[pos.row][pos.col] == 0) { whiteFlag = 1; }
                else { blueFlag = 1; }

                if (whiteFlag == 1 && blueFlag == 1) {
                    return false;
                }
            }
        }

        if (whiteFlag == 1) { numWhite += 1; }
        if (blueFlag == 1) { numBlue += 1;}

        return true;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        mainMatrix = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                mainMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        numWhite = 0;
        numBlue = 0;
    }
}
