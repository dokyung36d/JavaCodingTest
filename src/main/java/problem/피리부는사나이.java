package problem;

import java.util.*;
import java.io.*;


public class 피리부는사나이 {
    static int N, M;
    static int[][] directionIndexMatrix;
    static Pos[][] parentMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1),
            new Pos(1, 0), new Pos(0, -1)};


    public static class Pos implements Comparable<Pos> {
        int row;
        int col;

        public Pos (int row, int col) {
            this.row = row;
            this.col = col;
        }

        public boolean isValidIndex() {
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= M) {
                return false;
            }

            return true;
        }

        public Pos addPos(Pos direction) {
            return new Pos(this.row + direction.row, this.col + direction.col);
        }

        @Override
        public int compareTo(Pos anotherPos) {
            if (this.row == anotherPos.row) {
                return Integer.compare(this.col, anotherPos.col);
            }

            return Integer.compare(this.row, anotherPos.row);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Pos anotherPos = (Pos) obj;
            if (this.row == anotherPos.row && this.col == anotherPos.col) { return true; }

            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        parentMatrix = new Pos[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                parentMatrix[i][j] = new Pos(i, j);
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Pos curPos = new Pos(i, j);
                Pos nextPos = curPos.addPos(directions[directionIndexMatrix[i][j]]);
                if (!nextPos.isValidIndex()) { continue; }

                union(curPos, nextPos);
            }
        }

        Map<Pos, Integer> parentMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Pos parentPos = findParent(new Pos(i, j));
                parentMap.put(parentPos, 1);
            }
        }

        System.out.println(parentMap.keySet().size());
    }

    public static Pos findParent(Pos curPos) {
        if (curPos.equals(parentMatrix[curPos.row][curPos.col])) { return curPos; }

        Pos parentPos = parentMatrix[curPos.row][curPos.col];
        return parentMatrix[parentPos.row][parentPos.col] = findParent(parentPos);
    }

    public static void union(Pos pos1, Pos pos2) {
        Pos pos1Parent = findParent(pos1);
        Pos pos2Parent = findParent(pos2);

        if (pos1Parent.equals(pos2Parent)) { return; }

        if (pos1Parent.compareTo(pos2Parent) < 0) {
            parentMatrix[pos1Parent.row][pos1Parent.col] = pos2Parent;
        }
        else {
            parentMatrix[pos2Parent.row][pos2Parent.col] = pos1Parent;
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        directionIndexMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String directions = st.nextToken();
            for (int j = 0; j < M; j++) {
                char direction = directions.charAt(j);
                int directionIndex;
                if (direction == 'U') {
                    directionIndex = 0;
                }
                else if (direction == 'R') {
                    directionIndex = 1;
                }
                else if (direction == 'D') {
                    directionIndex = 2;
                }
                else {
                    directionIndex = 3;
                }

                directionIndexMatrix[i][j] = directionIndex;
            }
        }
    }



}