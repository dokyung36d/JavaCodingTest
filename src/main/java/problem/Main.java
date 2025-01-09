package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.*;
import java.util.StringTokenizer;

public class Main {
    static int R;
    static int C;
    static char[][] matrix;
    static Map<Pos, Integer> fireMap = new HashMap<>();
    static List<Pos> fireOriginPosList = new ArrayList<>();

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

        public boolean checkIndex() {
            if (0 <= this.row && this.row < R && 0 <= this.col && this.col < C) {
                return false;
            }

            return true;
        }

        public boolean checkWall() {
            if (matrix[this.row][this.col] == '#') {
                return false;
            }

            return true;
        }

        public boolean checkFire() {
            if (fireMap.get(this) == 1) {
                return false;
            }

            return true;
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

        int consumedTime = 0;
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        matrix = new char[R][C];
        for (int i = 0; i < R; i++) {
            st = new StringTokenizer(br.readLine());
            String row = st.nextToken();
            for (int j = 0; j < C; j++) {
                matrix[i][j] = row.charAt(j);
            }
        }
    }
}
