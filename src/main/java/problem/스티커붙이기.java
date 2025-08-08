package problem;

import java.util.*;
import java.io.*;


public class 스티커붙이기 {
    static int N, M, K;
    static int[][] mainMatrix;
    static List<Integer[][]> stickerList;

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
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= M) {
                return false;
            }

            return true;
        }

        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Pos anotherPos = (Pos) obj;
            if (this.row == anotherPos.row && this.col == anotherPos.col) { return true; }

            return false;
        }

        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        for (int i = 0; i < K; i++) {
            tryStickerToMainMatrix(stickerList.get(i));

        }


        int answer = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (mainMatrix[i][j] == 1) {
                    answer += 1;
                }
            }
        }


        System.out.println(answer);
    }

    public static void tryStickerToMainMatrix(Integer[][] matrix) {
        for (int numRotate = 0; numRotate < 4; numRotate++) {
            Integer[][] rotatedMatrix = rotateNTimes(numRotate, matrix);

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    Pos topLeftPos = new Pos(i, j);
                    if (!isFit(topLeftPos, rotatedMatrix)) { continue; }

                    applyStickerToMainMatrix(topLeftPos, rotatedMatrix);
                    return;
                }
            }
        }
    }

    public static void applyStickerToMainMatrix(Pos topLeftPos, Integer[][] matrix) {
        int row = matrix.length;
        int col = matrix[0].length;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                Pos curPos = topLeftPos.addPos(new Pos(i, j));
                if (matrix[i][j] == 0) { continue; }

                mainMatrix[curPos.row][curPos.col] = matrix[i][j];
            }
        }
    }

    public static boolean isFit(Pos startPos, Integer[][] matrix) {
        int matrixRow = matrix.length;
        int matrixCol = matrix[0].length;

        for (int i = 0; i < matrixRow; i++) {
            for (int j = 0; j < matrixCol; j++) {
                Pos curPos = startPos.addPos(new Pos(i, j));

                if (matrix[i][j] == 0) { continue; }
                if (!curPos.isValidIndex()) {
                    return false;
                }
                if (mainMatrix[curPos.row][curPos.col] == 1) {
                    return false;
                }

            }
        }


        return true;
    }

    public static Integer[][] rotateNTimes(int numRotate, Integer[][] matrix) {
        for (int i = 0; i < numRotate; i++) {
            matrix = rotateMatrix(matrix);
        }

        return matrix;
    }

    public static Integer[][] rotateMatrix(Integer[][] matrix) {
        int matrixRow = matrix.length;
        int matrixCol = matrix[0].length;

        Integer[][] rotatedMatrix = new Integer[matrixCol][matrixRow];
        for (int i = 0; i < matrixRow; i++) {
            for (int j = 0; j < matrixCol; j++) {
                rotatedMatrix[j][matrixRow - i - 1] = matrix[i][j];
            }
        }

        return rotatedMatrix;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        stickerList = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            int row = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken());

            Integer[][] matrix = new Integer[row][col];
            for (int j = 0; j < row; j++) {
                st = new StringTokenizer(br.readLine());
                for (int k = 0; k < col; k++) {
                    matrix[j][k] = Integer.parseInt(st.nextToken());
                }
            }

            stickerList.add(matrix);
        }


        mainMatrix = new int[N][M];
    }
}
