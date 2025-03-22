package problem;

import java.util.*;
import java.io.*;


public class 미지의공간탈출 {
    static int N, M, F;
    static int[][] floorMatrix, columnMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static Pos columnMatrixStartPos, columnMatrixExitPos;
    static Pos floorMatrixStartPos, floorMatrixExitPos;
    static List<TimeStrange> timeStrangeList;

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
            if (this.row == anotherPos.row && this.col == anotherPos.col) { return true; }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }

        public Pos addPos(Pos direction) {
            return new Pos(this.row + direction.row, this.col + direction.col);
        }

        public Pos moveInColumnMatrix(Pos direction) {
            Pos movedPos = addPos(direction);
            if (0 <= movedPos.row && movedPos.row < M && 0 <= movedPos.col && movedPos.col < M) {
                return new Pos(this.col, this.row);
            }

            if (2 * M <= movedPos.row && movedPos.row < 3 * M && 2 * M <= movedPos.col && movedPos.col < 3 * M) {
                return new Pos(this.col, this.row);
            }

            if (0 <= movedPos.row && movedPos.row < M && 2 * M <= movedPos.col && movedPos.col < 3 * M) {
                return new Pos(3 * M - this.col - 1, 3 * M - this.row - 1);
            }

            if (2 * M <= movedPos.row && movedPos.row < 3 * M && 0 <= movedPos.col && movedPos.col < M) {
                return new Pos(3 * M - this.col - 1, 3 * M - this.row - 1);
            }


            return movedPos;
        }

        public boolean isValidIndexInFloorMatrix() {
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
                return false;
            }

            return true;
        }

        public boolean isValidIndexInColumnMatrix() {
            if (this.row < 0 || this.row >= 3 * M || this.col < 0 || this.col  >= 3 * M) {
                return false;
            }

            return true;
        }
    }

    public static class TimeStrange {
        Pos curPos;
        int directionIndex;
        int period;
        int remainTime;

        public TimeStrange(Pos curPos, int directionIndex,  int period, int remainTime) {
            this.curPos = curPos;
            this.directionIndex = directionIndex;
            this.period = period;
            this.remainTime = remainTime;
        }

        public TimeStrange passTime() {
            if (remainTime == 1) {
                Pos movedPos = curPos.addPos(directions[directionIndex]);
                return new TimeStrange(movedPos, this.directionIndex, this.period, this.period);
            }

            return new TimeStrange(this.curPos, this.directionIndex, this.period, this.remainTime - 1);
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

        Pos pos = new Pos(5, 2);
        Pos movedPos = pos.moveInColumnMatrix(directions[1]);

        findStartAndExitPos();
        int columnDepth = bfsInColumnMatrix();
        if (columnDepth == -1) {
            System.out.println(-1);
            return;
        }
        columnDepth += 1;
        passAllTimeStrange();
        if (floorMatrix[floorMatrixStartPos.row][floorMatrixStartPos.col] == 1) {
            System.out.println(-1);
            return;
        }

        int floorDepth = bfsInFloorMatrix();
        if (floorDepth == -1) {
            System.out.println(-1);
            return;
        }
        System.out.println(columnDepth + floorDepth);
    }

    public static int bfsInFloorMatrix() {
        int[][] visited = new int[N][N];
        Deque<BfsNode> queue = new ArrayDeque<>();

        int maxDepth = -1;
        queue.add(new BfsNode(floorMatrixStartPos, 0));

        while (!queue.isEmpty()) {
            BfsNode node = queue.pollFirst();
            if (visited[node.pos.row][node.pos.col] == 1) { continue; }
            visited[node.pos.row][node.pos.col] = 1;

            if (node.depth > maxDepth) {
                passAllTimeStrange();
                maxDepth = node.depth;
            }

            for (Pos direction : directions) {
                Pos movedPos = node.pos.addPos(direction);
                if (!movedPos.isValidIndexInFloorMatrix() || visited[movedPos.row][movedPos.col] == 1) { continue; }
                if (floorMatrix[movedPos.row][movedPos.col] != 0) { continue; }

                if (movedPos.equals(floorMatrixExitPos)) {
                    return node.depth + 1;
                }

                queue.add(new BfsNode(movedPos, node.depth + 1));
            }
        }

        return -1;
    }

    public static int bfsInColumnMatrix() {
        int[][] visited = new int[3 * M][3 * M];
        Deque<BfsNode> queue = new ArrayDeque<>();
        queue.add(new BfsNode(columnMatrixStartPos, 0));

        int maxDepth = -1;
        while (!queue.isEmpty()) {
            BfsNode node = queue.pollFirst();
            if (visited[node.pos.row][node.pos.col] == 1) { continue; }
            visited[node.pos.row][node.pos.col] = 1;
            if (node.depth > maxDepth) {
                passAllTimeStrange();
                maxDepth = node.depth;
            }

            for (Pos direction : directions) {
                Pos movedPos = node.pos.moveInColumnMatrix(direction);
                if (!movedPos.isValidIndexInColumnMatrix() || visited[movedPos.row][movedPos.col] == 1) {
                    continue;
                }
                if (columnMatrix[movedPos.row][movedPos.col] == 1) { continue; }

                if (movedPos.equals(columnMatrixExitPos)) {
                    return node.depth + 1;
                }

                queue.add(new BfsNode(movedPos, node.depth + 1));
            }
        }

        return -1;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        F = Integer.parseInt(st.nextToken());

        floorMatrix = new int[N][N];
        columnMatrix = new int[3 * M][3 * M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                floorMatrix[i][j] = Integer.parseInt(st.nextToken());
                if (floorMatrix[i][j] == 4) {
                    floorMatrixExitPos = new Pos(i, j);
                    floorMatrix[i][j] = 0;
                }
            }
        }


        int[][] eastMatrix = new int[M][M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                eastMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        eastMatrix = rotateNTimes(eastMatrix, 3);
        columnMatrix = applySmallMatrixToBigMatrix(columnMatrix, eastMatrix, new Pos(M, 2 * M));


        int[][] westMatrix = new int[M][M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                westMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        westMatrix = rotateNTimes(westMatrix, 1);
        columnMatrix = applySmallMatrixToBigMatrix(columnMatrix, westMatrix, new Pos(M, 0));


        int[][] southMatrix = new int[M][M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                southMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        southMatrix = rotateNTimes(southMatrix, 0);
        columnMatrix = applySmallMatrixToBigMatrix(columnMatrix, southMatrix, new Pos(2 * M, M));


        int[][] northMatrix = new int[M][M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                northMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        northMatrix = rotateNTimes(northMatrix, 2);
        columnMatrix = applySmallMatrixToBigMatrix(columnMatrix, northMatrix, new Pos(0, M));


        int[][] centerMatrix = new int[M][M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                centerMatrix[i][j] = Integer.parseInt(st.nextToken());
                if (centerMatrix[i][j] == 2) {
                    columnMatrixStartPos = new Pos(M + i, M + j);
                    centerMatrix[i][j] = 0;
                }
            }
        }

        columnMatrix = applySmallMatrixToBigMatrix(columnMatrix, centerMatrix, new Pos(M, M));


        timeStrangeList = new ArrayList<>();
        for (int i = 0; i < F; i++) {
            st = new StringTokenizer(br.readLine());

            int row = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken());
            int directionIndex = Integer.parseInt(st.nextToken());
            int period = Integer.parseInt(st.nextToken());

            if (directionIndex == 0) {
                directionIndex = 1;
            }
            else if (directionIndex == 1) {
                directionIndex = 3;
            }
            else if (directionIndex == 3) {
                directionIndex = 0;
            }

            floorMatrix[row][col] = 1;
            timeStrangeList.add(new TimeStrange(new Pos(row, col), directionIndex, period, period));
        }
    }

    public static void passAllTimeStrange() {
        List<TimeStrange> updatedTimeStrangeList = new ArrayList<>();
        for (int i = 0; i < timeStrangeList.size(); i++) {
            TimeStrange timeStrange = timeStrangeList.get(i);
            TimeStrange updatedTimeStrange = timeStrange.passTime();
            if (timeStrange.curPos.equals(updatedTimeStrange.curPos)) {
                updatedTimeStrangeList.add(updatedTimeStrange);
                continue;
            }


            if (!updatedTimeStrange.curPos.isValidIndexInFloorMatrix()) { continue; }
            if (floorMatrix[updatedTimeStrange.curPos.row][updatedTimeStrange.curPos.col] == 0 &&
                    !updatedTimeStrange.curPos.equals(floorMatrixExitPos)) {
                floorMatrix[updatedTimeStrange.curPos.row][updatedTimeStrange.curPos.col] = 1;
                updatedTimeStrangeList.add(updatedTimeStrange);
            }
        }

        timeStrangeList =  updatedTimeStrangeList;
    }

    public static void findStartAndExitPos() {
        Pos columnTopLeftPosInFloorMatrix = new Pos(0, 0);

        int flag = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (floorMatrix[i][j] == 3) {
                    columnTopLeftPosInFloorMatrix = new Pos(i, j);
                    flag = 1;
                    break;
                }
            }
            if (flag == 1) {
                break;
            }
        }


        for (int i = 0; i < M; i++) {
            //위쪽에 있을 경우
            if (floorMatrix[columnTopLeftPosInFloorMatrix.row - 1][columnTopLeftPosInFloorMatrix.col + i] == 0) {
                floorMatrixStartPos = new Pos(columnTopLeftPosInFloorMatrix.row - 1, columnTopLeftPosInFloorMatrix.col + i);
                columnMatrixExitPos = new Pos(0, M + i);
                break;
            }

            //아래쪽에 있을 경우
            if (floorMatrix[columnTopLeftPosInFloorMatrix.row + M][columnTopLeftPosInFloorMatrix.col + i] == 0) {
                floorMatrixStartPos = new Pos(columnTopLeftPosInFloorMatrix.row + M, columnTopLeftPosInFloorMatrix.col + i);
                columnMatrixExitPos = new Pos(3 * M - 1, M + i);
                break;
            }

            //왼쪽에 있을 경우
            if (floorMatrix[columnTopLeftPosInFloorMatrix.row + i][columnTopLeftPosInFloorMatrix.col - 1] == 0) {
                floorMatrixStartPos = new Pos(columnTopLeftPosInFloorMatrix.row + i, columnTopLeftPosInFloorMatrix.col - 1);
                columnMatrixExitPos = new Pos(M + i, 0);
                break;
            }

            //오른쪽에 있을 경우
            if (floorMatrix[columnTopLeftPosInFloorMatrix.row + i][columnTopLeftPosInFloorMatrix.col + M] == 0) {
                floorMatrixStartPos = new Pos(columnTopLeftPosInFloorMatrix.row + i, columnTopLeftPosInFloorMatrix.col + M);
                columnMatrixExitPos = new Pos(M + i, 3 * M - 1);
                break;
            }
        }
    }

    public static int[][] rotateNTimes(int[][] originalMatrix, int n) {
        for (int i = 0; i < n; i++) {
            originalMatrix = rotateMatrix(originalMatrix);
        }

        return originalMatrix;
    }

    public static int[][] rotateMatrix(int[][] originalMatrix) {
        int[][] rotatedMatrix = new int[originalMatrix.length][originalMatrix.length];

        for (int i = 0; i < originalMatrix.length; i++) {
            for (int j = 0; j < originalMatrix.length; j++) {
                rotatedMatrix[j][originalMatrix.length - i - 1] = originalMatrix[i][j];
            }
        }

        return rotatedMatrix;
    }


    public static int[][] applySmallMatrixToBigMatrix(int[][] bigMatrix, int[][] smallMatrix, Pos topLeftPos) {
        for (int i = 0; i < smallMatrix.length; i++) {
            for (int j = 0; j < smallMatrix[0].length; j++) {
                Pos direction = new Pos(i, j);
                Pos curPos = topLeftPos.addPos(direction);
                bigMatrix[curPos.row][curPos.col] = smallMatrix[direction.row][direction.col];
            }
        }

        return bigMatrix;
    }
}