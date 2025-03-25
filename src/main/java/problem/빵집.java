package problem;

import java.util.*;
import java.io.*;


public class 빵집 {
    static int R, C;
    static char[][] mainMatrix;
    static Pos[] directions = {new Pos(-1, 1), new Pos(0, 1), new Pos(1, 1)};
    static int[][] visited;
    static int answer = 0;

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
            if (this.row < 0 || this.row >= R || this.col < 0 || this.col >= C) { return false; }

            return true;
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        answer = 0;
        for (int i = 0; i < R; i++) {
            dfs(new Pos(i, 0));
        }
        System.out.println(answer);

    }

    public static boolean dfs(Pos curPos) {
        if (curPos.col == C - 1) {
            answer += 1;
            return true;
        }

        for (Pos direction : directions) {
            Pos movedPos = curPos.addPos(direction);
            if (!movedPos.isValidIndex()) { continue; }
            if (mainMatrix[movedPos.row][movedPos.col] == 'x') { continue; }
            if (visited[movedPos.row][movedPos.col] == 1) { continue; }

            visited[movedPos.row][movedPos.col] = 1;
            if (dfs(movedPos)) {
                return true;
            }
        }

        return false;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        mainMatrix = new char[R][C];
        visited = new int[R][C];
        for (int i = 0; i < R; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();
            for (int j = 0; j < C; j++) {
                mainMatrix[i][j] = string.charAt(j);
            }
        }
    }
}
