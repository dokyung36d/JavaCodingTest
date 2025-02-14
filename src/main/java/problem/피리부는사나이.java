package problem;

import java.util.*;
import java.io.*;


public class 피리부는사나이 {
    public static int N, M;
    static Pos[][] nextPosMatrix;
    static Pos[][] parentMatrix;

    public static class Pos {
        int row;
        int col;

        public Pos(int row, int col) {
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
    }

    public static void main(String[] args) throws Exception {
        init();

        int[][] visited = new int[N][M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (visited[i][j] == 1) {
                    continue;
                }
                visited[i][j] = 1;
                Pos curPos = new Pos(i, j);

                while (true) {
                    Pos nextPos = nextPosMatrix[curPos.row][curPos.col];
                    if (nextPos == null) { break; }

                    visited[nextPos.row][nextPos.col] = 1;
                    Pos curPosParent = findParent(curPos);
                    Pos nextPosParent = findParent(nextPos);
                    if (curPosParent.equals(nextPosParent)) { break; }

                    union(curPosParent, nextPosParent);
                    curPos = nextPos;
                }
            }
        }

        Map<Pos, Integer> parentMap = new HashMap<>();
        int answer = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Pos parent = findParent(parentMatrix[i][j]);
                if (parentMap.get(parent) == null) {
                    answer += 1;
                    parentMap.put(parent, 1);
                }
            }
        }

        System.out.println(answer);
    }

    public static Pos findParent(Pos pos) {
        if (parentMatrix[pos.row][pos.col].equals(pos)) {
            return pos;
        }

        return parentMatrix[pos.row][pos.col] = findParent(parentMatrix[pos.row][pos.col]);
    }

    public static void union(Pos pos1, Pos pos2) {
        Pos pos1Parent = findParent(pos1);
        Pos pos2Parent = findParent(pos2);

        if (!pos1Parent.equals(pos2Parent)) {
            parentMatrix[pos2Parent.row][pos2Parent.col] = pos1Parent;
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        nextPosMatrix = new Pos[N][M];
        parentMatrix = new Pos[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            String commands = st.nextToken();
            for (int j = 0; j < M; j++) {
                Pos curPos = new Pos(i, j);
                parentMatrix[i][j] = curPos;
                char command = commands.charAt(j);

                Pos direction;
                if (command == 'D') {
                    direction = new Pos(1, 0);
                }

                else if (command == 'U') {
                    direction = new Pos(-1, 0);

                }
                else if (command == 'L') {
                    direction = new Pos(0, -1);
                }
                else {
                    direction = new Pos(0, 1);
                }


                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.isValidIndex()) {
                    nextPosMatrix[curPos.row][curPos.col] = null;
                }
                else {
                    nextPosMatrix[curPos.row][curPos.col] = movedPos;
                }

            }
        }
    }
}