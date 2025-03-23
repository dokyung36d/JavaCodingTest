package problem;

import java.util.*;
import java.io.*;


public class 소가길을건너간이유 {
    static int N, R, K;
    static int[][] groupMatrix;
    static Map<Pos, Map<Pos, Integer>> bridgeMap;
    static Pos[] cowPosList;
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
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
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

    public static void main(String[] args) throws IOException {
        init();
        setGroupMatrix();

        int answer = 0;
        for (int i = 0; i < K; i++) {
            for (int j = i + 1; j < K; j++) {
                Pos cow1 = cowPosList[i];
                Pos cow2 = cowPosList[j];

                if (groupMatrix[cow1.row][cow1.col] != groupMatrix[cow2.row][cow2.col]) {
                    answer += 1;
                }
            }
        }

        System.out.println(answer);
    }

    public static void setGroupMatrix() {
        int[][] visited = new int[N][N];

        int uniqueNum = 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (visited[i][j] != 0) { continue; }

                Deque<Pos> queue = new ArrayDeque<>();
                queue.add(new Pos(i, j));

                while (!queue.isEmpty()) {
                    Pos curPos = queue.pollFirst();
                    if (visited[curPos.row][curPos.col] == 1) { continue; }
                    visited[curPos.row][curPos.col] = 1;
                    groupMatrix[curPos.row][curPos.col] = uniqueNum;

                    for (Pos direction : directions) {
                        Pos movedPos = curPos.addPos(direction);
                        if (!movedPos.isValidIndex() || bridgeMap.get(curPos).get(movedPos) != null) { continue; }
                        if (visited[movedPos.row][movedPos.col] == 1) { continue; }

                        queue.add(movedPos);
                    }
                }

                uniqueNum += 1;
            }
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());

        groupMatrix = new int[N][N];

        bridgeMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                bridgeMap.put(new Pos(i, j), new HashMap<>());
            }
        }


        for (int i = 0; i < R; i++) {
            st = new StringTokenizer(br.readLine());
            int row1 = Integer.parseInt(st.nextToken()) - 1;
            int col1 = Integer.parseInt(st.nextToken()) - 1;
            Pos pos1 = new Pos(row1, col1);

            int row2 = Integer.parseInt(st.nextToken()) - 1;
            int col2 = Integer.parseInt(st.nextToken()) - 1;
            Pos pos2 = new Pos(row2, col2);

            bridgeMap.get(pos1).put(pos2, 1);
            bridgeMap.get(pos2).put(pos1, 1);
        }

        cowPosList = new Pos[K];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int row = Integer.parseInt(st.nextToken()) - 1;
            int col = Integer.parseInt(st.nextToken()) - 1;

            cowPosList[i] = new Pos(row, col);
        }
    }
}
