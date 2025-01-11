package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.*;
import java.util.StringTokenizer;

public class 불 {
    static int R;
    static int C;
    static char[][] matrix;
    static int[][] fireMatrix;
    static int[][] jihoonMatrix;
    static Deque<Pos> fireQueue = new ArrayDeque<>();
    static List<Pos> fireOriginPosList = new ArrayList<>();
    static Pos jihoonPos;
    static int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static class Pos {
        int row;
        int col;
        int depth;

        public Pos(int row, int col, int depth) {
            this.row = row;
            this.col = col;
            this.depth = depth;
        }

        public Pos addPos(Pos anotherPos) {
            return new Pos(this.row + anotherPos.row, this.col + anotherPos.col, this.depth + 1);
        }

        public boolean checkValidIndex() {
            if (0 <= this.row && this.row < R && 0 <= this.col && this.col < C) {
                return true;
            }

            return false;
        }

        public boolean existWall() {
            if (matrix[this.row][this.col] == '#') {
                return true;
            }

            return false;
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
            if (this.row == anotherPos.row && this.col == anotherPos.col) {
                return true;
            }

            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        init();
        bfsJihoon();
        bfsFire();

        Deque<Pos> queue = new ArrayDeque<>();
        int[][] visited = new int[R][C];
        queue.addLast(jihoonPos);
        visited[jihoonPos.row][jihoonPos.col] = 1;

        while (!queue.isEmpty()) {
            Pos curPos = queue.pollFirst();
            for (int i = 0; i < 4; i++) {
                Pos direction = new Pos(directions[i][0], directions[i][1], 0);
                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.checkValidIndex()) {
                    System.out.println(movedPos.depth - 1);
                    return;
                }

                if (!checkJihoonArrivesEarilerThanFire(movedPos)) {
                    continue;
                }
                if (movedPos.existWall()) {
                    continue;
                }
                if (visited[movedPos.row][movedPos.col] == 1) {
                    continue;
                }

                visited[movedPos.row][movedPos.col] = 1;
                queue.addLast(movedPos);

            }
        }

        System.out.println("IMPOSSIBLE");
        return;
    }

    public static boolean checkJihoonArrivesEarilerThanFire(Pos pos) {
        if (fireMatrix[pos.row][pos.col] == 0) {
            return true;
        }
        if (jihoonMatrix[pos.row][pos.col] < fireMatrix[pos.row][pos.col]) {
            return true;
        }

        return false;
    }

    public static void bfsJihoon() throws IOException {
        Deque<Pos> jihoonPosDeque = new ArrayDeque<>();
        jihoonPosDeque.addLast(jihoonPos);
        int[][] visited = new int[R][C];
        visited[jihoonPos.row][jihoonPos.col] = 1;
        jihoonMatrix[jihoonPos.row][jihoonPos.col] = jihoonPos.depth;

        while (!jihoonPosDeque.isEmpty()) {
            Pos curPos = jihoonPosDeque.pollFirst();
            for (int i = 0; i < 4; i++) {
                Pos direction = new Pos(directions[i][0], directions[i][1], 0);
                Pos movedPos = curPos.addPos(direction);

                if (!movedPos.checkValidIndex()) {
                    continue;
                }
                if (visited[movedPos.row][movedPos.col] == 1) {
                    continue;
                }
                if (movedPos.existWall()) {
                    continue;
                }

                visited[movedPos.row][movedPos.col] = 1;
                jihoonMatrix[movedPos.row][movedPos.col] = movedPos.depth;
                jihoonPosDeque.addLast(movedPos);
            }
        }
    }

    public static void bfsFire() throws IOException {
        Deque<Pos> fireDeque = new ArrayDeque<>();
        int[][] visited = new int[R][C];

        for (int i = 0; i < fireOriginPosList.size(); i++ ) {
            Pos fireOriginPos = fireOriginPosList.get(i);
            visited[fireOriginPos.row][fireOriginPos.col] = 1;
            fireDeque.addLast(fireOriginPos);
            fireMatrix[fireOriginPos.row][fireOriginPos.col] = fireOriginPos.depth;
        }

        while (!fireDeque.isEmpty()) {
            Pos firePos = fireDeque.pollFirst();
            for (int i = 0; i < 4; i++) {
                Pos direction = new Pos(directions[i][0], directions[i][1], 0);
                Pos movedPos = firePos.addPos(direction);
                if (!movedPos.checkValidIndex()) {
                    continue;
                }
                if (visited[movedPos.row][movedPos.col] == 1) {
                    continue;
                }
                if (movedPos.existWall()) {
                    continue;
                }

                visited[movedPos.row][movedPos.col] = 1;
                fireMatrix[movedPos.row][movedPos.col] = movedPos.depth;
                fireDeque.addLast(movedPos);
            }
        }
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        matrix = new char[R][C];
        jihoonMatrix = new int[R][C];
        fireMatrix = new int[R][C];
        for (int i = 0; i < R; i++) {
            st = new StringTokenizer(br.readLine());
            String row = st.nextToken();
            for (int j = 0; j < C; j++) {
                matrix[i][j] = row.charAt(j);

                if (matrix[i][j] == 'F') {
                    fireOriginPosList.add(new Pos(i, j, 1));
                }

                if (matrix[i][j] == 'J') {
                    jihoonPos = new Pos(i, j, 1);
                }
            }
        }
    }
}
