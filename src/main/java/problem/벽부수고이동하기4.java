package problem;

import java.util.*;
import java.io.*;


public class 벽부수고이동하기4 {
    public static int N, M;
    public static int[][] matrix;
    public static Pos[] directions = {new Pos(-1, 0), new Pos(1, 0),
            new Pos(0, -1), new Pos(0, 1)};
    public static Map<Integer, Integer> areaMap;

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

        int[][] visited = new int[N][M];
        int uniqueNum = 2;


        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Pos pos = new Pos(i, j);
                if (matrix[pos.row][pos.col] != 0) { continue; }
                bfs(pos, uniqueNum);
                uniqueNum += 1;

            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Pos pos = new Pos(i, j);
                if (matrix[pos.row][pos.col] != 1) {
                    sb.append(0);
                    continue;
                }

                Map<Integer, Integer> nearAreaMap = new HashMap<>();
                for (Pos direction : directions) {
                    Pos movedPos = pos.addPos(direction);
                    if (!movedPos.isValidIndex() || matrix[movedPos.row][movedPos.col] == 1) { continue; }

                    int nearAreaUniqueNum = matrix[movedPos.row][movedPos.col];
                    nearAreaMap.put(nearAreaUniqueNum, 1);
                }

                int totalNumArea = 1;
                for (int key : nearAreaMap.keySet()) {
                    totalNumArea += areaMap.get(key);
                }
                sb.append((totalNumArea % 10));
            }
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void bfs(Pos startPos, int uniqueNum) {
        Deque<Pos> queue = new ArrayDeque<>();
        queue.add(startPos);

        int numArea = 0;

        while (!queue.isEmpty()) {
            Pos pos = queue.pollFirst();
            if (matrix[pos.row][pos.col] != 0) { continue; }
            numArea += 1;
            matrix[pos.row][pos.col] = uniqueNum;

            for (Pos direction : directions) {
                Pos movedPos = pos.addPos(direction);
                if (!movedPos.isValidIndex() || matrix[movedPos.row][movedPos.col] != 0) { continue; }
                queue.add(movedPos);
            }
        }

        areaMap.put(uniqueNum, numArea);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        areaMap = new HashMap<>();
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        matrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();

            for (int j = 0; j < M; j++) {
                matrix[i][j] = Character.getNumericValue(string.charAt(j));
            }
        }

    }
}