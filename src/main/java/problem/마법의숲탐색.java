package problem;

import java.util.*;
import java.io.*;

public class 마법의숲탐색 {
    static int R, C, K;
    static int[][] mainMatrix, jumpMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static Golem[] golemList;

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
            if (this.row < 0 || this.row >= R + 3 || this.col < 0 || this.col >= C) {
                return false;
            }

            return true;
        }

        public boolean canGolemFit() {
            if (!this.isValidIndex()) { return false; }
            if (mainMatrix[this.row][this.col] != 0) { return false; }

            for (Pos direction : directions) {
                Pos movedPos = this.addPos(direction);
                if (!movedPos.isValidIndex()) { return false; }
                if (mainMatrix[movedPos.row][movedPos.col] != 0) { return false; }
            }

            return true;
        }
    }

    public static class Golem {
        Pos centerPos;
        int uniqueNum;
        int directionIndex;

        public Golem(Pos centerPos, int uniqueNum, int directionIndex) {
            this.centerPos = centerPos;
            this.uniqueNum = uniqueNum;
            this.directionIndex = directionIndex;
        }

        public Golem move() {
            Golem downGolem = this.goDown();
            if (downGolem != null) {
                return downGolem;
            }

            Golem leftDownGolem = this.goLeft();
            if (leftDownGolem != null) {
                return leftDownGolem;
            }

            Golem rightDownGolem = this.goRight();
            if (rightDownGolem != null) {
                return rightDownGolem;
            }

            return null;
        }

        public Golem goDown() {
            Pos downPos = this.centerPos.addPos(directions[2]);
            if (!downPos.canGolemFit()) { return null; }

            return new Golem(downPos, this.uniqueNum, this.directionIndex);
        }

        public Golem goLeft() {
            Pos leftPos = this.centerPos.addPos(directions[3]);
            if (!leftPos.canGolemFit()) { return null; }

            Pos leftDownPos = leftPos.addPos(directions[2]);
            if (!leftDownPos.canGolemFit()) { return null; }

            return new Golem(leftDownPos, this.uniqueNum, (this.directionIndex + 3) % 4);
        }

        public Golem goRight() {
            Pos rightPos = this.centerPos.addPos(directions[1]);
            if (!rightPos.canGolemFit()) { return null; }

            Pos rightDownPos = rightPos.addPos(directions[2]);
            if (!rightDownPos.canGolemFit()) { return null; }

            return new Golem(rightDownPos, this.uniqueNum, (this.directionIndex + 1) % 4);
        }

        public void applyToMainMatrix() {
            for (Pos direction : directions) {
                Pos movedPos = this.centerPos.addPos(direction);
                mainMatrix[movedPos.row][movedPos.col] = this.uniqueNum;
            }
            mainMatrix[this.centerPos.row][this.centerPos.col] = this.uniqueNum;

            Pos jumpPos = this.centerPos.addPos(directions[this.directionIndex]);
            jumpMatrix[jumpPos.row][jumpPos.col] = 1;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int answer = 0;

        for (Golem golem : golemList) {
            while (true) {
                Golem movedGolem = golem.move();
                if (movedGolem == null) { break; }

                golem = movedGolem;
            }


            if (golem.centerPos.row <= 3) {
                initMatrix();
                continue;
            }

            golem.applyToMainMatrix();
            answer += getScore(golem);
        }

        System.out.println(answer);
    }

    public static int getScore(Golem golem) {
        int maxRow = 0;

        Pos curPos = golem.centerPos;
        int[][] visited = new int[R + 3][C];
        Deque<Pos> queue = new ArrayDeque<>();
        queue.add(curPos);

        while (!queue.isEmpty()) {
            Pos pos = queue.pollFirst();
            if (visited[pos.row][pos.col] == 1) { continue; }
            visited[pos.row][pos.col] = 1;

            if (pos.row > maxRow) {
                maxRow = pos.row;
            }

            for (Pos direction : directions) {
                Pos movedPos = pos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] == 0) { continue; }

                if (mainMatrix[pos.row][pos.col] == mainMatrix[movedPos.row][movedPos.col] || jumpMatrix[pos.row][pos.col] == 1) {
                    queue.add(movedPos);
                }
            }
        }

        return maxRow - 2;
    }

    public static void initMatrix() {
        mainMatrix = new int[R + 3][C];
        jumpMatrix = new int[R + 3][C];
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        mainMatrix = new int[R + 3][C];
        jumpMatrix = new int[R + 3][C];

        golemList = new Golem[K];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            int col = Integer.parseInt(st.nextToken()) - 1;
            int directionIndex = Integer.parseInt(st.nextToken());

            golemList[i] = new Golem(new Pos(1, col), i + 1, directionIndex);
        }
    }

}