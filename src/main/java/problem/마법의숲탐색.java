package problem;

import java.util.*;
import java.io.*;


public class 마법의숲탐색 {
    static int R, C, K;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    //북 -> 동 -> 남 -> 서
    static int[][] matrix;
    static int[][] jumpMatrix;
    //matrix에서 0은 빈칸을 의미
    static Golem[] golemInitPosList;

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

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }
            Pos anotherPos = (Pos) obj;
            if (this.row == anotherPos.row && this.col == anotherPos.col) { return true; }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }
    }

    public static class Golem {
        Pos centerPos;
        int directionIndex;

        public Golem(Pos centerPos, int directionIndex) {
            this.centerPos = centerPos;
            this.directionIndex = directionIndex;
        }

        public int soulMove() {
            int[][] visited = new int[R][C];
            Deque<Pos> queue = new ArrayDeque<>();
            queue.add(centerPos);

            int maxRow = centerPos.row;
            Pos maxPos = centerPos;
            while (!queue.isEmpty()) {
                Pos pos = queue.pollFirst();
                if (visited[pos.row][pos.col] == 1) { continue; }

                visited[pos.row][pos.col] = 1;
                for (Pos direction : directions) {
                    Pos movedPos = pos.addPos(direction);

                    if (!movedPos.isValidIndex()) { continue; }
                    if (matrix[movedPos.row][movedPos.col] == 0) { continue; }
                    if (jumpMatrix[pos.row][pos.col] == 0 && matrix[pos.row][pos.col] != matrix[movedPos.row][movedPos.col]) { continue; }

                    if (movedPos.row > maxRow) {
                        maxRow = movedPos.row;
                        maxPos = movedPos;
                    }
                    queue.add(movedPos);
                }
            }

            return maxRow - 2;
        }

        public void applyToMatrix(int uniqueNum) {
            int[] dRow = {-1, 0, 1, 0, 0}; //북 -> 동 -> 남 -> 서 -> 가운데
            int[] dCol = {0, 1, 0, -1, 0};

            for (int i = 0; i < dRow.length; i++) {
                matrix[centerPos.row + dRow[i]][centerPos.col + dCol[i]] = uniqueNum;
            }

            Pos jumpDirection = directions[directionIndex];
            jumpMatrix[centerPos.row + jumpDirection.row][centerPos.col + jumpDirection.col] = 1;
        }

        public Golem move() {
            if (isGoDownPossible()) {
                return goDown();
            }

            if (isGoLeftPossible()) {
                return goLeft();
            }

            if (isGoRightPossible()) {
                return goRight();
            }

            return this;
        }

        public boolean isGoDownPossible() {
            Pos[] nextPosList = {new Pos(centerPos.row + 2, centerPos.col),
                    new Pos(centerPos.row + 1, centerPos.col - 1),
                    new Pos(centerPos.row + 1, centerPos.col + 1)};

            for (Pos nextPos : nextPosList) {
                if (!nextPos.isValidIndex()) { return false; }
                if (matrix[nextPos.row][nextPos.col] != 0) { return false; }
            }

            return true;
        }

        public Golem goDown() {
            return new Golem(new Pos(centerPos.row + 1, centerPos.col), directionIndex);
        }

        public boolean isGoLeftPossible() {
            Pos[] nextPosList = {new Pos(centerPos.row - 1, centerPos.col - 1),
                    new Pos(centerPos.row, centerPos.col - 2),
                    new Pos(centerPos.row + 1, centerPos.col - 1),
                    new Pos(centerPos.row + 1, centerPos.col - 2),
                    new Pos(centerPos.row + 2, centerPos.col - 1)};

            for (Pos nextPos : nextPosList) {
                if (!nextPos.isValidIndex()) { return false; }
                if (matrix[nextPos.row][nextPos.col] != 0) { return false; }
            }

            return true;
        }

        public Golem goLeft() {
            return new Golem(new Pos(centerPos.row + 1, centerPos.col - 1), (directionIndex + 3) % 4);
        }

        public boolean isGoRightPossible() {
            Pos[] nextPosList = {new Pos(centerPos.row - 1, centerPos.col + 1),
                    new Pos(centerPos.row, centerPos.col + 2),
                    new Pos(centerPos.row + 1, centerPos.col + 1),
                    new Pos(centerPos.row + 2, centerPos.col + 1),
                    new Pos(centerPos.row + 1, centerPos.col + 2)};

            for (Pos nextPos : nextPosList) {
                if (!nextPos.isValidIndex()) { return false; }
                if (matrix[nextPos.row][nextPos.col] != 0) { return false; }
            }

            return true;
        }

        public Golem goRight() {
            return new Golem(new Pos(centerPos.row + 1, centerPos.col + 1), (directionIndex + 1) % 4);
        }
    }


    public static void main(String[] args) throws Exception {
        init();
        int answer = 0;
        for (int i = 0; i < K; i++) {
            Golem golem = golemInitPosList[i];
            while (true) {
                Golem movedGolem = golem.move();
                if (golem.centerPos.equals(movedGolem.centerPos)) { break; }

                golem = movedGolem;
            }

            golem.applyToMatrix(i + 1);
            if (golem.centerPos.row < 4) {
                matrix = new int[R][C];
                jumpMatrix = new int[R][C];
                continue;
            }
            answer += golem.soulMove();
        }

        System.out.println(answer);
    }

    public static void init() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        R += 3;
        matrix = new int[R][C];
        jumpMatrix = new int[R][C];

        golemInitPosList = new Golem[K];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int column = Integer.parseInt(st.nextToken()) - 1;
            int directionIndex = Integer.parseInt(st.nextToken());

            golemInitPosList[i] = new Golem(new Pos(1, column), directionIndex);
        }
    }
}
