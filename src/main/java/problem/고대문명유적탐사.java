package problem;

import java.util.*;
import java.io.*;


public class 고대문명유적탐사 {
    public static int K, M;
    public static int[][] matrix;
    public static Deque<Integer> treasureQueue;
    public static Pos[] directions = {new Pos(-1, 0), new Pos(1, 0), new Pos(0, -1), new Pos(0, 1)};

    public static class MatrixNode implements Comparable<MatrixNode> {
        List<Pos> poppedTreasureList;
        int numRotated;
        Pos topLeftPos;

        public MatrixNode(List<Pos> poppedTreasureList, int numRotated, Pos topLeftPos) {
            this.poppedTreasureList = poppedTreasureList;
            this.numRotated = numRotated;
            this.topLeftPos = topLeftPos;
        }

        @Override
        public int compareTo(MatrixNode anotherMatrixNode) {
            if (this.poppedTreasureList.size() != anotherMatrixNode.poppedTreasureList.size()) {
                return Integer.compare(-this.poppedTreasureList.size(), -anotherMatrixNode.poppedTreasureList.size());
            }

            if (this.numRotated != anotherMatrixNode.numRotated) {
                return Integer.compare(this.numRotated, anotherMatrixNode.numRotated);
            }

            if (this.topLeftPos.col != anotherMatrixNode.topLeftPos.col) {
                return Integer.compare(this.topLeftPos.col, anotherMatrixNode.topLeftPos.col);
            }

            return Integer.compare(this.topLeftPos.row, anotherMatrixNode.topLeftPos.row);
        }
    }

    public static class Treasure implements Comparable<Treasure> {
        Pos pos;

        public Treasure(Pos pos) {
            this.pos = pos;
        }

        @Override
        public int compareTo(Treasure anotherTreasure) {
            if (this.pos.col == anotherTreasure.pos.col) {
                return Integer.compare(-this.pos.row, -anotherTreasure.pos.row);
            }

            return Integer.compare(this.pos.col, anotherTreasure.pos.col);
        }
    }

    public static class Pos {
        int row;
        int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Pos addPos(Pos anotherPos) {
            return new Pos(this.row + anotherPos.row, this.col + anotherPos.col);
        }

        public boolean isValidIndex() {
            if (this.row < 0 || this.row >= 5 || this.col < 0 || this.col >= 5) {
                return false;
            }

            return true;
        }

        public boolean isSubMatrixTopLeftPosPossible() {
            if (this.row < 0 || this.row >= 4 || this.col < 0 || this.col >= 4) {
                return false;
            }

            return true;
        }
    }



    public static void main(String[] args) throws Exception {
        init();

//        int[][] matrix = {{7, 5, 1, 7, 7}, {6, 7, 6, 7, 6}, {6, 6, 7, 6, 4}, {7, 6, 3, 2, 1}, {5, 4, 3, 2, 7}};
//        List<Pos> posList = bfs(matrix);
//        System.out.println("hello world");

        StringBuilder sb = new StringBuilder();

        for (int k = 0; k < K; k++) {
            int value = 0;
            MatrixNode bestMatrixNode = getBestMatrixNode();
            if (bestMatrixNode.poppedTreasureList.size() == 0) { break; }

            value += bestMatrixNode.poppedTreasureList.size();

            int[][] subMatrix = getSubMatrix(bestMatrixNode.topLeftPos);
            for (int i = 0; i < bestMatrixNode.numRotated; i++) {
                subMatrix = rotateMatrix(subMatrix);
            }
            matrix = applySubMatrixToMatrix(bestMatrixNode.topLeftPos, subMatrix);
            applyPoppedTreasureToMatrix(bestMatrixNode.poppedTreasureList);

            while (true) {
                List<Pos> poppedTreasureList = bfs(matrix);
                if (poppedTreasureList.size() == 0) { break; }

                value += poppedTreasureList.size();
                applyPoppedTreasureToMatrix(poppedTreasureList);
            }

            sb.append(value + " ");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void applyPoppedTreasureToMatrix(List<Pos> poppedTreasureList) {
        PriorityQueue<Treasure> pq = new PriorityQueue<>();
        for (Pos poppedTreasure : poppedTreasureList) {
            pq.add(new Treasure(poppedTreasure));
        }

        while (!pq.isEmpty()) {
            Treasure treasure = pq.poll();
            matrix[treasure.pos.row][treasure.pos.col] = treasureQueue.pollFirst();
        }
    }

    public static MatrixNode getBestMatrixNode() {
        MatrixNode bestMatrixNode = new MatrixNode(new ArrayList<>(), 0, new Pos(0, 0));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Pos topLeftPos = new Pos(i, j);
                int[][] subMatrix = getSubMatrix(topLeftPos);

                int[][] onceRotatedMatrix = rotateMatrix(subMatrix);
                int[][] onceRotatedAppliedMatrix = applySubMatrixToMatrix(topLeftPos, onceRotatedMatrix);
                List<Pos> onceRotatedPoppedTreasureList = bfs(onceRotatedAppliedMatrix);
                MatrixNode onceRotatedMatrixNode = new MatrixNode(onceRotatedPoppedTreasureList, 1, topLeftPos);
                if (onceRotatedMatrixNode.compareTo(bestMatrixNode) < 0) {
                    bestMatrixNode = onceRotatedMatrixNode;
                }

                int[][] twiceRotatedMatrix = rotateMatrix(onceRotatedMatrix);
                int[][] twiceRotatedAppliedMatrix = applySubMatrixToMatrix(topLeftPos, twiceRotatedMatrix);
                List<Pos> twiceRotatedPoppedTreasureList = bfs(twiceRotatedAppliedMatrix);
                MatrixNode twiceRotatedMatrixNode = new MatrixNode(twiceRotatedPoppedTreasureList, 2, topLeftPos);
                if (twiceRotatedMatrixNode.compareTo(bestMatrixNode) < 0) {
                    bestMatrixNode = twiceRotatedMatrixNode;
                }

                int[][] threeRotatedMatrix = rotateMatrix(twiceRotatedMatrix);
                int[][] threeRotatedAppliedMatrix = applySubMatrixToMatrix(topLeftPos, threeRotatedMatrix);
                List<Pos> threeRotatedPoppedTreasureList = bfs(threeRotatedAppliedMatrix);
                MatrixNode threeRotatedMatrixNode = new MatrixNode(threeRotatedPoppedTreasureList, 3, topLeftPos);
                if (threeRotatedMatrixNode.compareTo(bestMatrixNode) < 0) {
                    bestMatrixNode = threeRotatedMatrixNode;
                }
            }
        }

        return bestMatrixNode;
    }

    public static List<Pos> bfs(int[][] rotatedMatrix) {
        int[][] visited = new int[5][5];
        List<Pos> poppedTreasureList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Pos pos = new Pos(i, j);
                if (visited[pos.row][pos.col] == 1) { continue; }

                int treasureValue = rotatedMatrix[pos.row][pos.col];

                Deque<Pos> queue = new ArrayDeque<>();
                queue.add(pos);
                List<Pos> adjacentTreasureList = new ArrayList<>();

                while (!queue.isEmpty()) {
                    Pos curPos = queue.pollFirst();
                    if (visited[curPos.row][curPos.col] == 1) { continue; }

                    adjacentTreasureList.add(curPos);
                    visited[curPos.row][curPos.col] = 1;
                    for (Pos direction : directions) {
                        Pos movedPos = curPos.addPos(direction);
                        if (!movedPos.isValidIndex() || visited[movedPos.row][movedPos.col] == 1) { continue; }
                        if (rotatedMatrix[movedPos.row][movedPos.col] != treasureValue) { continue; }

                        queue.add(movedPos);
                    }
                }

                if (adjacentTreasureList.size() < 3) { continue; }
                for (Pos adjacentTreasure : adjacentTreasureList) {
                    poppedTreasureList.add(adjacentTreasure);
                }
            }
        }

        return poppedTreasureList;
    }

    public static int[][] applySubMatrixToMatrix(Pos topLeftPos, int[][] subMatrix) {
        int[][] appliedMatrix = new int[5][5];

        int[][] copiedMatrix = copyMatrix(matrix);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Pos direction = new Pos(i, j);
                copiedMatrix[topLeftPos.row + direction.row][topLeftPos.col + direction.col] = subMatrix[direction.row][direction.col];
            }
        }

        return copiedMatrix;
    }

    public static int[][] copyMatrix(int[][] originalMatrix) {
        int[][] copiedMatrix = new int[5][5];
        for (int i = 0; i < originalMatrix.length; i++) {
            for (int j = 0; j < originalMatrix.length; j++) {
                copiedMatrix[i][j] = originalMatrix[i][j];
            }
        }

        return copiedMatrix;
    }

    public static int[][] getSubMatrix(Pos topLeftPos) {
        int[][] subMatrix = new int[3][3];

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                Pos direction = new Pos(i, j);
                subMatrix[i][j] = matrix[topLeftPos.row + direction.row][topLeftPos.col + direction.col];
            }
        }

        return subMatrix;
    }

    public static int[][] rotateMatrix(int[][] subMatrix) {
        int[][] rotatedMatrix = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rotatedMatrix[j][3 - i - 1] = subMatrix[i][j];
            }
        }

        return rotatedMatrix;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        matrix = new int[5][5];
        for (int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        treasureQueue = new ArrayDeque<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            int treasure = Integer.parseInt(st.nextToken());
            treasureQueue.addLast(treasure);
        }
    }
}