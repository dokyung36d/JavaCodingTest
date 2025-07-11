package problem;

import java.util.*;
import java.io.*;


public class 치즈 {
    static int N, M;
    static int numCheese;
    static int[][] mainMatrix;
    static int[][] externalMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};

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
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int time = 0;

        while (true) {
            if (numCheese == 0) {
                break;
            }

            time += 1;

            setExternalMatrix();
            List<Pos> deleteCheeseList = getDeleteCheese();
            for (Pos deleteCheesePos : deleteCheeseList) {
                mainMatrix[deleteCheesePos.row][deleteCheesePos.col] = 0;
                numCheese -= 1;
            }
        }


        System.out.println(time);
    }

    public static List<Pos> getDeleteCheese() {
        List<Pos> deleteCheeseList = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (mainMatrix[i][j] == 0) { continue; }
                if (!checkCheeseDelete(new Pos(i, j))) { continue; }

                deleteCheeseList.add(new Pos(i, j));
            }
        }

        return deleteCheeseList;
    }

    public static boolean checkCheeseDelete(Pos cheesPos) {
        int numAttach = 0;

        for (Pos direction : directions) {
            Pos nearPos = cheesPos.addPos(direction);
            if (!nearPos.isValidIndex()) { continue; }
            if (externalMatrix[nearPos.row][nearPos.col] == 1) {
                numAttach += 1;
            }
        }


        if (numAttach >= 2) {
            return true;
        }

        return false;
    }

    public static void setExternalMatrix() {
        externalMatrix = new int[N][M];
        int[][] visited = new int[N][M];
        Deque<Pos> queue = new ArrayDeque<>();

        queue.add(new Pos(0, 0));
        queue.add(new Pos(0, M - 1));
        queue.add(new Pos(N -1, 0));
        queue.add(new Pos(N -1, M - 1));

        while (!queue.isEmpty()) {
            Pos curPos = queue.pollFirst();
            if (visited[curPos.row][curPos.col] == 1) { continue; }
            visited[curPos.row][curPos.col] = 1;

            externalMatrix[curPos.row][curPos.col] = 1;
            for (Pos direction : directions) {
                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }
                if (visited[movedPos.row][movedPos.col] == 1) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

                queue.add(movedPos);
            }
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        numCheese = 0;

        mainMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < M; j++) {
                mainMatrix[i][j] = Integer.parseInt(st.nextToken());

                if (mainMatrix[i][j] == 1) {
                    numCheese += 1;
                }
            }
        }
    }
}
