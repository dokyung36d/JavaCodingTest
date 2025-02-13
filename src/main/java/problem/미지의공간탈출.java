package problem;

import java.util.*;
import java.io.*;


public class 미지의공간탈출 {
    static int N, M, F;
    static int[][] floorMatrix;
    static int[][] columnMatrix;
    static int[][] timeStrangeMatrix;
    static Pos columnStartPos;
    static Pos columnFloorStartPos;
    static Pos columnDestPos;
    static Pos floorStartPos;
    static Pos floorDestPos;
    static Pos[] directions = {new Pos(0, 1), new Pos(0, -1), new Pos(1, 0), new Pos(-1, 0)};;
    static List<TimeStrange> timeStrangeList;


    public static class TimeStrange {
        Pos pos;
        Pos direction;
        int advanceInterval;
        int timeToAdvance;

        public TimeStrange(Pos pos, Pos direction, int timeInterval, int timeToAdvance) {
            this.pos = pos;
            this.direction = direction;
            this.advanceInterval = timeInterval;
            this.timeToAdvance = timeToAdvance;
        }

        public TimeStrange oneTimePassed() {
            if (this.timeToAdvance == 1) {
                Pos movedPos = pos.moveInFloor(direction);
                return new TimeStrange(movedPos, this.direction, this.advanceInterval, this.advanceInterval);
            }

            return new TimeStrange(pos, direction, advanceInterval, timeToAdvance - 1);
        }
    }


    public static class Pos {
        int row;
        int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Pos moveInFloor(Pos direction) {
            return new Pos(this.row + direction.row, this.col + direction.col);
        }

        public boolean isValidIndexInFloor() {
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
                return false;
            }

            return true;
        }

        public Pos moveInColumn(Pos direction) {
            Pos movedPos = this.moveInFloor(direction);
            if (0 <= movedPos.row  && movedPos.row < M && 0 <= movedPos.col && movedPos.col < M) {
                return new Pos(this.col, this.row);
            }
            if (2 * M <= movedPos.row  && movedPos.row < 3 * M && 2 * M <= movedPos.col && movedPos.col < 3 * M) {
                return new Pos(this.col, this.row);
            }

            if (0 <= movedPos.row  && movedPos.row < M && 2 * M <= movedPos.col && movedPos.col < 3 * M) {
                return new Pos(3 * M  - this.col - 1, 3 * M - this.row - 1);
            }
            if (2 * M <= movedPos.row  && movedPos.row < 3 * M && 0 <= movedPos.col && movedPos.col < M) {
                return new Pos(3 * M  - this.col - 1, 3 * M - this.row - 1);
            }

            return movedPos;
        }

        public boolean isValidIndexInColumn() {
            if (this.row < 0 || this.row >= 3 * M || this.col < 0 || this.col >= 3 * M) {
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

    public static class BfsNode {
        Pos pos;
        int depth;

        public BfsNode(Pos pos, int depth) {
            this.pos = pos;
            this.depth = depth;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        findColumnToFloorPos();
        int totalTime = 0;

        int timeInColumn = bfsInColumn();
        if (timeInColumn == -1) {
            System.out.println(-1);
            return;
        }
        totalTime += timeInColumn;
        totalTime += 1; //from column to floor
        timeStrangeAdvance();
        if (timeStrangeMatrix[floorStartPos.row][floorStartPos.col] == 1) {
            System.out.println(-1);
            return;
        }

        int totalTimeInFloor = bfsInFloor();
        if (totalTimeInFloor == -1) {
            System.out.println(-1);
            return;
        }
        totalTime += totalTimeInFloor;
        System.out.println(totalTime);
    }

    public static int bfsInFloor() {
        int[][] visited = new int[N][N];
        Deque<BfsNode> queue = new ArrayDeque<>();
        queue.add(new BfsNode(floorStartPos, 0));
        int curDepth = 0;

        while (!queue.isEmpty()) {
            BfsNode bfsNode = queue.pollFirst();
            Pos pos = bfsNode.pos;
            if (visited[pos.row][pos.col] == 1 || timeStrangeMatrix[pos.row][pos.col] == 1) { continue; }
            if (bfsNode.depth > curDepth) {
                curDepth = bfsNode.depth;
                timeStrangeAdvance();
            }

            visited[pos.row][pos.col] = 1;
            for (Pos direction : directions) {
                Pos movedPos = pos.moveInFloor(direction);
                if (!movedPos.isValidIndexInFloor() || floorMatrix[movedPos.row][movedPos.col] != 0 ||
                        timeStrangeMatrix[movedPos.row][movedPos.col] == 1 || visited[movedPos.row][movedPos.col] == 1) { continue; }

                if (movedPos.equals(floorDestPos)) {
                    return bfsNode.depth + 1;
                }

                queue.add(new BfsNode(movedPos, bfsNode.depth + 1));
            }
        }

        return -1;
    }

    public static int bfsInColumn() {
        int[][] visited = new int[3 * M][3 * M];
        Deque<BfsNode> queue = new ArrayDeque<>();
        queue.add(new BfsNode(columnStartPos, 0));
        int curDepth = 0;

        while (!queue.isEmpty()) {
            BfsNode bfsNode = queue.pollFirst();
            Pos pos = bfsNode.pos;
            if (visited[pos.row][pos.col] == 1) { continue; }
            if (bfsNode.depth > curDepth) {
                curDepth = bfsNode.depth;
                timeStrangeAdvance();
            }

            visited[pos.row][pos.col] = 1;
            for (Pos direction : directions) {
                Pos movedPos = pos.moveInColumn(direction);
                if (!movedPos.isValidIndexInColumn() || visited[movedPos.row][movedPos.col] == 1 ||
                        columnMatrix[movedPos.row][movedPos.col] == 1) { continue; }
                if (movedPos.equals(columnDestPos)) {
                    timeStrangeAdvance();
                    return bfsNode.depth + 1;
                }

                queue.add(new BfsNode(movedPos, bfsNode.depth + 1));
            }

        }

        return -1;
    }

    public static void timeStrangeAdvance() {
        List<TimeStrange> updatedTimeStrangeList = new ArrayList<>();
        for (int i = 0; i < timeStrangeList.size(); i++) {
            TimeStrange oneTimePassedTimeStrange = timeStrangeList.get(i).oneTimePassed();
            Pos curPos = oneTimePassedTimeStrange.pos;
            if (!curPos.isValidIndexInFloor() || floorMatrix[curPos.row][curPos.col] != 0 || curPos.equals(floorDestPos)) {
                continue;
            }

            timeStrangeMatrix[curPos.row][curPos.col] = 1;
            updatedTimeStrangeList.add(oneTimePassedTimeStrange);
        }

        timeStrangeList = updatedTimeStrangeList;
    }


    public static void findColumnToFloorPos() {
        for (int i = 0; i < M; i++) {
            Pos upPos = new Pos(columnFloorStartPos.row - 1, columnFloorStartPos.col + i);
            if (upPos.isValidIndexInFloor() && floorMatrix[upPos.row][upPos.col] == 0) {
                columnDestPos = new Pos(0, M + i);
                floorStartPos = upPos;
                return;
            }

            Pos downPos = new Pos(columnFloorStartPos.row + M, columnFloorStartPos.col + i);
            if (downPos.isValidIndexInFloor() && floorMatrix[downPos.row][downPos.col] == 0) {
                columnDestPos = new Pos(3 * M - 1, M + i);
                floorStartPos = downPos;
                return;
            }

            Pos leftPos = new Pos(columnFloorStartPos.row + i, columnFloorStartPos.col - 1);
            if (leftPos.isValidIndexInFloor() && floorMatrix[leftPos.row][leftPos.col] == 0) {
                columnDestPos = new Pos(M + i, 0);
                floorStartPos = leftPos;
                return;
            }

            Pos rightPos = new Pos(columnFloorStartPos.row + i, columnFloorStartPos.col + M);
            if (rightPos.isValidIndexInFloor() && floorMatrix[rightPos.row][rightPos.col] == 0) {
                columnDestPos = new Pos(M + i, 3 * M - 1);
                floorStartPos = rightPos;
                return;
            }

        }

        return;
    }

    public static void init() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        F = Integer.parseInt(st.nextToken());

        floorMatrix = new int[N][N];
        columnMatrix = new int[3 * M][3 * M];

        int flag = 0;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                floorMatrix[i][j] = Integer.parseInt(st.nextToken());
                if (flag == 0 && floorMatrix[i][j] == 3) {
                    columnFloorStartPos = new Pos(i, j);
                    flag = 1;
                }

                if (floorMatrix[i][j] == 4) {
                    floorDestPos = new Pos(i, j);
                    floorMatrix[i][j] = 0;
                }
            }
        }

        //동쪽
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                columnMatrix[2 * M - j - 1][2 * M + i] = Integer.parseInt(st.nextToken());
            }
        }

        //서쪽
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                columnMatrix[M + j][M - i - 1] = Integer.parseInt(st.nextToken());
            }
        }

        //남쪽
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                columnMatrix[2 * M + i][M + j] = Integer.parseInt(st.nextToken());
            }
        }

        //북쪽
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                columnMatrix[M - i - 1][2 * M - j - 1] = Integer.parseInt(st.nextToken());
            }
        }

        //위쪽
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                columnMatrix[M + i][M + j] = Integer.parseInt(st.nextToken());
                if (columnMatrix[M + i][M + j] == 2) {
                    columnStartPos = new Pos(M + i, M + j);
                    columnMatrix[M + i][M + j] = 0;
                }
            }
        }


        timeStrangeList = new ArrayList<>();
        for (int i = 0; i < F; i++) {
            st = new StringTokenizer(br.readLine());
            int row = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken());
            int directionIndex = Integer.parseInt(st.nextToken());
            Pos direction = directions[directionIndex];
            int advanceInterval = Integer.parseInt(st.nextToken());

            timeStrangeList.add(new TimeStrange(new Pos(row, col), direction, advanceInterval, advanceInterval));
        }

        timeStrangeMatrix = new int[N][N];
    }
}