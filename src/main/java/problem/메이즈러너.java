package problem;

import java.util.*;
import java.io.*;


public class 메이즈러너 {
    static int N, M, K;
    static int[][] mainMatrix;
    static int[][] playerMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(1,0), new Pos(0, -1), new Pos(0, 1)};

    public static class Rectangle {
        Pos topLeftPos;
        int length;

        public Rectangle(Pos topLeftPos, int length) {
            this.topLeftPos = topLeftPos;
            this.length = length;
        }
    }

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
            if (obj == null || obj.getClass() != this.getClass()) { return false; }
            Pos anotherPos = (Pos) obj;
            if (this.row == anotherPos.row && this.col == anotherPos.col) { return true; }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }

        public boolean isValidIndex() {
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
                return false;
            }

            return true;
        }

        public Pos addPos(Pos anotherPos) {
            return new Pos(this.row + anotherPos.row, this.col + anotherPos.col);
        }

        public int calcDistance(Pos anotherPos) {
            return Math.abs(this.row - anotherPos.row) + Math.abs(this.col - anotherPos.col);
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        int totalMoved = 0;

        for (int i = 0; i < K; i++) {
            totalMoved += allPlayerMove();
            if (countNumPlayer() == 0) {
                break;
            }
            Rectangle rectangle = findBestRectangle();

            int[][] subMainMatrix = getSubMatrix(rectangle, mainMatrix);
            int[][] subPlayerMatrix = getSubMatrix(rectangle, playerMatrix);
            int[][] rotatedMainMatrix = rotateMatrix(subMainMatrix);
            dimishWall(rotatedMainMatrix);
            int[][] rotatedPlayerMatrix = rotateMatrix(subPlayerMatrix);

            applySubMatrixToMatrix(rectangle, rotatedMainMatrix, mainMatrix);
            applySubMatrixToMatrix(rectangle, rotatedPlayerMatrix, playerMatrix);

        }

        System.out.println(totalMoved);
        Pos exitPos = findExitPos();
        System.out.println((exitPos.row + 1) + " " + (exitPos.col + 1));

    }

    public static int countNumPlayer() {
        int numPlayer = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (playerMatrix[i][j] != 0) {
                    numPlayer += playerMatrix[i][j];
                }
            }
        }

        return numPlayer;
    }

//    public static void removePlayerPosFromMainMatrix() {
//        List<Pos> updatedPlayerPosList = new ArrayList<>();
//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                if (mainMatrix[i][j] == -1) {
//                    updatedPlayerPosList.add(new Pos(i, j));
//                    mainMatrix[i][j] = 0;
//                }
//            }
//        }
//
//        playerPosList = updatedPlayerPosList;
//    }

//    public static void applyPlayerPosListToMainMatrix() {
//        for (Pos playerPos : playerPosList) {
//            mainMatrix[playerPos.row][playerPos.col] = -1;
//        }
//    }

    public static void applySubMatrixToMatrix(Rectangle rectangle, int[][] subMatrix, int[][] matrix) {
        for (int i = 0; i < rectangle.length; i++) {
            for (int j = 0; j < rectangle.length; j++) {
                Pos pos = rectangle.topLeftPos.addPos(new Pos(i, j));
                matrix[pos.row][pos.col] = subMatrix[i][j];
            }
        }
    }

    public static int[][] getSubMatrix(Rectangle rectangle, int[][] matrix) {
        int[][] subMatrix = new int[rectangle.length][rectangle.length];
        for (int i = 0; i < rectangle.length; i++) {
            for (int j = 0; j < rectangle.length; j++) {
                Pos pos = rectangle.topLeftPos.addPos(new Pos(i, j));
                subMatrix[i][j] = matrix[pos.row][pos.col];
            }
        }

        return subMatrix;
    }

    public static int[][] dimishWall(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] >= 1) {
                    matrix[i][j] -= 1;
                }
            }
        }

        return matrix;
    }

    public static int[][] rotateMatrix(int[][] matrix) {
        int[][] rotatedMatrix = new int[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                rotatedMatrix[j][matrix.length - i - 1] = matrix[i][j];
            }
        }

        return rotatedMatrix;
    }

    public static Rectangle findBestRectangle() {
        for (int length = 0; length <= N; length++) {
            for (int row = 0; row + length - 1 < N; row++) {
                for (int col = 0; col + length - 1 < N; col++) {
                    Rectangle rectangle = new Rectangle(new Pos(row, col), length);
                    if (isValidRectangle(rectangle)) { return rectangle; }
                }
            }
        }

        return new Rectangle(new Pos(0, 0), N);
    }

    public static boolean isValidRectangle(Rectangle rectangle) {
        Pos exitPos = findExitPos();
        if (!isPosInRectange(exitPos, rectangle)) { return false; }

        if (!isPlayerInRectangle(rectangle)) { return false; }

        return true;
    }

    public static boolean isPlayerInRectangle(Rectangle rectangle) {
        for (int i = 0; i < rectangle.length; i++) {
            for (int j = 0; j < rectangle.length; j++) {
                Pos pos = rectangle.topLeftPos.addPos(new Pos(i, j));
                if (playerMatrix[pos.row][pos.col] >= 1) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isPosInRectange(Pos pos, Rectangle rectangle) {
        if (rectangle.topLeftPos.row <= pos.row && pos.row < rectangle.topLeftPos.row + rectangle.length &&
                rectangle.topLeftPos.col <= pos.col && pos.col < rectangle.topLeftPos.col + rectangle.length) {
            return true;
        }

        return false;
    }

    public static Pos findExitPos() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] == -2) {
                    return new Pos(i, j);
                }
            }
        }

        return null;
    }

    public static int allPlayerMove() {
        Pos exitPos = findExitPos();

        int[][] updatedPlayerMatrix = new int[N][N];
        int numMoved = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (playerMatrix[i][j] == 0) { continue; }

                Pos playerPos = new Pos(i, j);
                Pos movedPos = playerMove(playerPos, exitPos);
                if (!movedPos.equals(playerPos)) {
                    numMoved += playerMatrix[i][j];
                }

                if (movedPos.equals(exitPos)) {
                    continue;
                }
                updatedPlayerMatrix[movedPos.row][movedPos.col] += playerMatrix[i][j];
            }
        }

        playerMatrix = updatedPlayerMatrix;
        return numMoved;
    }

    public static Pos playerMove(Pos playerPos, Pos exitPos) {
        int prevDistance = playerPos.calcDistance(exitPos);

        for (Pos direction : directions) {
            Pos movedPos = playerPos.addPos(direction);
            if (!movedPos.isValidIndex()) { continue; }
            if (mainMatrix[movedPos.row][movedPos.col] > 0) { continue; }
            if (movedPos.calcDistance(exitPos) < prevDistance) {
                return movedPos;
            }
        }

        return playerPos;
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        mainMatrix = new int[N][N];
        playerMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                mainMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int row = Integer.parseInt(st.nextToken()) - 1;
            int col = Integer.parseInt(st.nextToken()) - 1;

            playerMatrix[row][col] += 1;
        }

        st = new StringTokenizer(br.readLine());
        int row = Integer.parseInt(st.nextToken()) - 1;
        int col = Integer.parseInt(st.nextToken()) - 1;

        mainMatrix[row][col] = -2;


    }
}