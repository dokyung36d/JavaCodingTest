package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N, M;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static int[][] directionMatrix;
    static Pos[][] parentMatrix;

    public static class Pos implements Comparable<Pos> {
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
            if (this.row == anotherPos.row && this.col == anotherPos.col) {
                return true;
            }

            return false;
        }

        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }

        @Override
        public int compareTo(Pos anotherPos) {
            if (this.row != anotherPos.row) {
                return Integer.compare(this.row, anotherPos.row);
            }

            return Integer.compare(this.col, anotherPos.col);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][] visited = new int[N][M];


        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (visited[i][j] == 1) { continue; }

                Pos curPos = new Pos(i, j);
                Pos nextPos = curPos.addPos(directions[directionMatrix[curPos.row][curPos.col]]);

                union(curPos, nextPos);
            }
        }


        Map<Pos, Integer> parentMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                parentMap.put(findParent(new Pos(i, j)), 1);
            }
        }

        System.out.println(parentMap.keySet().size());
    }

    public static Pos findParent(Pos curPos) {
        if (parentMatrix[curPos.row][curPos.col].equals(curPos)) { return curPos; }

        return parentMatrix[curPos.row][curPos.col] = findParent(parentMatrix[curPos.row][curPos.col]);
    }

    public static void union(Pos pos1, Pos pos2) {
        Pos pos1Parent = findParent(pos1);
        Pos pos2Parent = findParent(pos2);

        if (pos1Parent.equals(pos2Parent)) { return; }

        if (pos1Parent.compareTo(pos2Parent) < 0) {
            parentMatrix[pos2Parent.row][pos2Parent.col] = pos1Parent;
        }
        else {
            parentMatrix[pos1Parent.row][pos1Parent.col] = pos2Parent;
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        directionMatrix = new int[N][M];
        parentMatrix = new Pos[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                parentMatrix[i][j] = new Pos(i, j);
            }
        }


        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            String string = st.nextToken();
            for (int j = 0; j < M; j++) {
                char direction = string.charAt(j);

                if (direction == 'U') {
                    directionMatrix[i][j] = 0;
                }

                else if (direction == 'R') {
                    directionMatrix[i][j] = 1;
                }

                else if (direction == 'D') {
                    directionMatrix[i][j] = 2;
                }

                else {
                    directionMatrix[i][j] = 3;
                }
            }
        }
    }
}