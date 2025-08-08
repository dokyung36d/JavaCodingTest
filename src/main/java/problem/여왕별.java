package problem;

import java.util.*;
import java.io.*;


public class 여왕별 {
    static int M, N;
    static int[][] mainMatrix;
    static int[][] commandMatrix;
    static Pos[] growOrderList;

    static Pos[] directions = {new Pos(1, 0), new Pos(0, 1), new Pos(1, 1)};

    static Pos downDirection = new Pos(1, 0);
    static Pos rightDirection = new Pos(0, 1);
    static Pos downRightDirection = new Pos(1, 1);


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
            if (this.row < 0 || this.row >= M || this.col < 0 || this.col >= M) {
                return false;
            }

            return true;
        }

        public boolean isInEdge() {
            if (this.row == 0 || this.col == 0) {
                return true;
            }

            return false;
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

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        for (int[] command : commandMatrix) {
            int[][] growthMatrix = initGrowthMatrix(command);
            applyGrowthMatrixToMainMatrix(growthMatrix);
        }

        StringBuilder answerSb = new StringBuilder();
        for (int i = 0; i < M; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < M; j++) {
                sb.append(mainMatrix[i][j] + " ");
            }
            String rowString = sb.toString().substring(0, sb.length() - 1);
            answerSb.append(rowString + "\n");
        }

        System.out.println(answerSb.toString().substring(0, answerSb.length() - 1));
    }

    public static int[][] initGrowthMatrix(int[] command) {
        int[][] growthMatrix = new int[M][M];

        for (int i = command[0]; i < command[0] + command[1]; i++) {
            Pos pos = growOrderList[i];
            growthMatrix[pos.row][pos.col] += 1;

            spread(pos, 1, growthMatrix);
        }

        for (int i = command[0] + command[1]; i < 2 * M - 1; i++) {
            Pos pos = growOrderList[i];
            growthMatrix[pos.row][pos.col] += 2;

            spread(pos, 2, growthMatrix);
        }


        return growthMatrix;
    }

    public static void applyGrowthMatrixToMainMatrix(int[][] growthMatrix) {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                mainMatrix[i][j] += growthMatrix[i][j];
            }
        }
    }

    public static void spread(Pos startPos, int growth, int[][] growthMatrix) {
        Deque<Pos> queue = new ArrayDeque<>();
        queue.add(startPos);

        while (!queue.isEmpty()) {
            Pos pos = queue.pollFirst();

            for (Pos direction : directions) {
                Pos movedPos = pos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }
                if (movedPos.isInEdge()) { continue; }
                if (growthMatrix[movedPos.row][movedPos.col] == growth) { continue; }

                growthMatrix[movedPos.row][movedPos.col] = growth;
                queue.add(movedPos);
            }
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        M = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());

        commandMatrix = new int[N][3];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            commandMatrix[i][0] = Integer.parseInt(st.nextToken());
            commandMatrix[i][1] = Integer.parseInt(st.nextToken());
            commandMatrix[i][2] = Integer.parseInt(st.nextToken());
        }

        growOrderList = new Pos[2 * M - 1];
        for (int i = 0; i < M; i++) {
            growOrderList[i] = new Pos(M - 1 - i, 0);
        }


        for (int i = 1; i < M; i++) {
            growOrderList[M - 1 + i] = new Pos(0, i);
        }


        mainMatrix = new int[M][M];
        for (int i = 0; i < M; i++) {
            Arrays.fill(mainMatrix[i], 1);
        }
    }
}