package problem;

import java.util.*;
import java.io.*;



public class 낚시왕 {
    static int R, C, M;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static Shark[][] mainMatrix;
    static int answer;

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
            if (this.row < 0 || this.row >= R || this.col < 0 || this.col >= C) {
                return false;
            }

            return true;
        }
    }

    public static class Shark {
        Pos curPos;
        int directionIndex;
        int speed;
        int size;

        public Shark(Pos curPos, int directionIndex, int speed, int size) {
            this.curPos = curPos;
            this.directionIndex = directionIndex;
            this.speed = speed;
            this.size = size;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        answer = 0;

        for (int i = 0; i < C; i++) {
            catchShark(i);
            moveAllSharks();
        }


        System.out.println(answer);
    }

    public static void moveAllSharks() {
        Shark[][] updatedMainMatrix = new Shark[R][C];

        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                if (mainMatrix[i][j] == null) { continue; }

                Shark movedShark = moveShark(mainMatrix[i][j]);
                if (updatedMainMatrix[movedShark.curPos.row][movedShark.curPos.col] == null) {
                    updatedMainMatrix[movedShark.curPos.row][movedShark.curPos.col] = movedShark;
                    continue;
                }

                if (updatedMainMatrix[movedShark.curPos.row][movedShark.curPos.col] != null && movedShark.size > updatedMainMatrix[movedShark.curPos.row][movedShark.curPos.col].size) {
                    updatedMainMatrix[movedShark.curPos.row][movedShark.curPos.col] = movedShark;
                    continue;
                }
            }
        }

        mainMatrix = updatedMainMatrix;
    }

    public static Shark moveShark(Shark shark) {
        for (int i = 0; i < shark.speed; i++) {
            Pos movedPos = shark.curPos.addPos(directions[shark.directionIndex]);
            if (!movedPos.isValidIndex()) {
                shark.directionIndex = (shark.directionIndex + 2) % 4;
                movedPos = shark.curPos.addPos(directions[shark.directionIndex]);
                shark.curPos = movedPos;
                continue;
            }

            shark.curPos = movedPos;
        }

        return shark;
    }

    public static void catchShark(int col) {
        for (int i = 0; i < R; i++) {
            if (mainMatrix[i][col] == null) { continue; }

            answer += mainMatrix[i][col].size;
            mainMatrix[i][col] = null;

            return;
        }
    }
    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        mainMatrix = new Shark[R][C];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int row = Integer.parseInt(st.nextToken()) - 1;
            int col = Integer.parseInt(st.nextToken()) - 1;

            int speed = Integer.parseInt(st.nextToken());
            int direction = Integer.parseInt(st.nextToken());
            int directionIndex;
            if (direction == 1) {
                directionIndex = 0;
            }
            else if (direction == 2) {
                directionIndex = 2;
            }
            else if (direction == 3) {
                directionIndex = 1;
            }
            else {
                directionIndex = 3;
            }

            int size = Integer.parseInt(st.nextToken());


            if (directionIndex == 0 || directionIndex == 2) {
                speed = speed % (2 * R - 2);
            }
            else {
                speed = speed % (2 * C - 2);
            }

            mainMatrix[row][col] = new Shark(new Pos(row, col), directionIndex, speed, size);
        }
    }
}