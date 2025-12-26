package problem;

import java.util.*;
import java.io.*;

public class Main {
	static int N, M;
    static Pos[][] parentMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static int[][] directionIndexMatrix;

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
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Pos curPos = new Pos(i, j);

                Pos direction = directions[directionIndexMatrix[curPos.row][curPos.col]];
                Pos directedPos = curPos.addPos(direction);

                union(curPos, directedPos);
            }
        }

        int answer = 0;
        Map<Pos, Integer> parentMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Pos parent = findParent(new Pos(i, j));

                parentMap.put(parent, 1);
            }
        }

        System.out.println(parentMap.keySet().size());
    }

    public static Pos findParent(Pos pos) {
        if (pos.equals(parentMatrix[pos.row][pos.col])) { return pos; }

        return parentMatrix[pos.row][pos.col] = findParent(parentMatrix[pos.row][pos.col]);
    }

    public static void union(Pos pos1, Pos pos2) {
        Pos pos1Parent = findParent(pos1);
        Pos pos2Parent = findParent(pos2);

        if (pos1Parent.equals(pos2Parent)) { return; }
        parentMatrix[pos1Parent.row][pos1Parent.col] = pos2Parent;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        parentMatrix = new Pos[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                parentMatrix[i][j] = new Pos(i, j);
            }
        }

        directionIndexMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();

            for (int j = 0; j < M; j++) {
                char direction  = string.charAt(j);

                if (direction == 'U') {
                    directionIndexMatrix[i][j] = 0;
                }

                else if (direction == 'R') {
                    directionIndexMatrix[i][j] = 1;
                }

                else if (direction == 'D') {
                    directionIndexMatrix[i][j] = 2;
                }

                else {
                    directionIndexMatrix[i][j] = 3;
                }
            }
        }


    }
}
