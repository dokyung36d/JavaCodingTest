package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int[][] mainMatrix, groupMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static Map<Integer, Integer> groupSizeMap;

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
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= M) { return false; }
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
        int pk = 2;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (mainMatrix[i][j] != 0) { continue; }

                bfs(new Pos(i, j), pk);
                pk += 1;
            }
        }


        int[][] answerMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (mainMatrix[i][j] != 1) { continue; }
                Pos curPos = new Pos(i, j);

                Map<Integer, Integer> nearGroupMap = new HashMap<>();
                for (Pos direction : directions) {
                    Pos movedPos = curPos.addPos(direction);
                    if (!movedPos.isValidIndex()) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

                    nearGroupMap.put(mainMatrix[movedPos.row][movedPos.col], 1);
                }


                int nearGroupSize = 0;
                for (int groupPk : nearGroupMap.keySet()) {
                    nearGroupSize += groupSizeMap.get(groupPk);
                }

                answerMatrix[i][j] = (nearGroupSize + 1) % 10;
            }
        }


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                sb.append(answerMatrix[i][j]);
            }
            sb.append("\n");
        }


        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void bfs(Pos startPos, int pk) {
        Deque<Pos> queue = new ArrayDeque<>();
        queue.add(startPos);

        int groupSize = 0;
        int[][] visited = new int[N][M];
        while (!queue.isEmpty()) {
            Pos curPos = queue.pollFirst();

            if (visited[curPos.row][curPos.col] == 1) { continue; }
            visited[curPos.row][curPos.col] = 1;

            mainMatrix[curPos.row][curPos.col] = pk;
            groupSize += 1;

            for (Pos direction : directions) {
                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }
                if (visited[movedPos.row][movedPos.col] == 1) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] != 0) { continue; }

                queue.add(movedPos);
            }
        }

        groupSizeMap.put(pk, groupSize);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());


        mainMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();

            for (int j = 0; j < M; j++) {
                mainMatrix[i][j] = Character.getNumericValue(string.charAt(j));
            }
        }

        groupSizeMap = new HashMap<>();
    }
}
