package problem;

import java.util.*;
import java.io.*;


public class 새로운게임 {
    static int N, K;
    static int flag;
    static int[][] chessMatrix;
    static Deque<Horse>[][] horseMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};

    public static class Horse {
        int uniqueNum;
        Pos pos;
        int directionIndex;

        public Horse(int uniqueNum, Pos pos, int directionIndex) {
            this.uniqueNum = uniqueNum;
            this.pos = pos;
            this.directionIndex = directionIndex;
        }

        public Pos getMovedPos() {
            Pos movedPos = this.pos.addPos(directions[directionIndex]);

            return movedPos;
        }

        public void reverseDirection() {
            this.directionIndex = (this.directionIndex + 2) % 4;
        }
    }

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
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
                return false;
            }

            return true;
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        for (int turn = 1; turn <= 1000; turn++) {
            for (int uniqueNum = 0; uniqueNum < K; uniqueNum++) {
                Pos pos = findPos(uniqueNum);
                if (pos == null) { continue; }

                Horse horse = horseMatrix[pos.row][pos.col].getFirst();
                Pos movedPos = horse.getMovedPos();
                if (!movedPos.isValidIndex() || chessMatrix[movedPos.row][movedPos.col] == 2) {
                    handleBlueCase(pos, movedPos);
                }
                else if (chessMatrix[movedPos.row][movedPos.col] == 0) {
                    handleWhiteCase(pos, movedPos);
                }
                else if (chessMatrix[movedPos.row][movedPos.col] == 1) {
                    handleRedCase(pos, movedPos);
                }


                if (flag == 1) {
                    System.out.println(turn);
                    return;
                }
            }
        }

        System.out.println(-1);
    }

    public static void handleWhiteCase(Pos pos, Pos movedPos) {
        Deque<Horse> queue = horseMatrix[pos.row][pos.col];

        while (!queue.isEmpty()) {
            Horse horse = queue.pollFirst();
            horse.pos = movedPos;
            horseMatrix[movedPos.row][movedPos.col].addLast(horse);
        }

        if (horseMatrix[movedPos.row][movedPos.col].size() >= 4) {
            flag = 1;
        }
    }

    public static void handleRedCase(Pos pos, Pos movedPos) {
        Deque<Horse> queue = horseMatrix[pos.row][pos.col];

        while (!queue.isEmpty()) {
            Horse horse = queue.pollLast();
            horse.pos = movedPos;
            horseMatrix[movedPos.row][movedPos.col].addLast(horse);
        }

        if (horseMatrix[movedPos.row][movedPos.col].size() >= 4) {
            flag = 1;
        }
    }

    public static void handleBlueCase(Pos pos, Pos movedPos) {
        Deque<Horse> queue = horseMatrix[pos.row][pos.col];
        queue.getFirst().reverseDirection();
        Pos reverseMovedPos = queue.getFirst().getMovedPos();
        if (!reverseMovedPos.isValidIndex() || chessMatrix[reverseMovedPos.row][reverseMovedPos.col] == 2) { return; }

        else if (chessMatrix[reverseMovedPos.row][reverseMovedPos.col] == 0) {
            handleWhiteCase(pos, reverseMovedPos);
        }
        else {
            handleRedCase(pos, reverseMovedPos);
        }



        if (horseMatrix[reverseMovedPos.row][reverseMovedPos.col].size() >= 4) {
            flag = 1;
        }
    }

    public static Pos findPos(int uniqueNum) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (horseMatrix[i][j].isEmpty()) { continue; }
                if (horseMatrix[i][j].getFirst().uniqueNum == uniqueNum) {
                    return new Pos(i, j);
                }
            }
        }

        return null;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        flag = 0;

        chessMatrix = new int[N][N];
        horseMatrix = new ArrayDeque[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                chessMatrix[i][j] = Integer.parseInt(st.nextToken());
                horseMatrix[i][j] = new ArrayDeque<>();
            }
        }

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int row = Integer.parseInt(st.nextToken()) - 1;
            int col = Integer.parseInt(st.nextToken()) - 1;

            int directionIndex = Integer.parseInt(st.nextToken()) - 1;
            if (directionIndex == 0) { directionIndex = 1; }
            else if (directionIndex == 1) { directionIndex = 3; }
            else if (directionIndex == 2) { directionIndex = 0; }
            else if (directionIndex == 3) { directionIndex = 2; }

            Pos pos = new Pos(row, col);
            horseMatrix[pos.row][pos.col].addLast(new Horse(i, pos, directionIndex));
        }

    }


}