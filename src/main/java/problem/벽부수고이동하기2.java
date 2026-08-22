package problem;

import java.util.*;
import java.io.*;


public class 벽부수고이동하기2 {
    static int N, M, K;
    static int[][] mainMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static Pos destPos;

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
            if (this.row == anotherPos.row && this.col == anotherPos.col) {
                return true;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }
    }

    public static class Node {
        Pos curPos;
        int canBreakNum;
        int numMoved;

        public Node(Pos curPos, int canBreakNum, int numMoved) {
            this.curPos = curPos;
            this.canBreakNum = canBreakNum;
            this.numMoved = numMoved;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        Deque<Node> queue = new ArrayDeque<>();
        queue.add(new Node(new Pos(0, 0), K, 0));

        int[][][] visited = new int[N][M][K + 1];

        while (!queue.isEmpty()) {
            Node node = queue.pollFirst();

            if (visited[node.curPos.row][node.curPos.col][node.canBreakNum] == 1) { continue; }
            visited[node.curPos.row][node.curPos.col][node.canBreakNum] = 1;

            if (node.curPos.equals(destPos)) {
                System.out.println(node.numMoved + 1);

                return;
            }


            for (Pos direction : directions) {
                Pos movedPos = node.curPos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }


                if (mainMatrix[movedPos.row][movedPos.col] == 0 && visited[movedPos.row][movedPos.col][node.canBreakNum] == 0) {
                    queue.add(new Node(movedPos, node.canBreakNum, node.numMoved + 1));
                }

                if (mainMatrix[movedPos.row][movedPos.col] == 1 && node.canBreakNum >= 1
                        && visited[movedPos.row][movedPos.col][node.canBreakNum - 1] == 0) {
                    queue.add(new Node(movedPos, node.canBreakNum - 1, node.numMoved + 1));
                }
            }
        }


        System.out.println(-1);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        destPos = new Pos(N - 1, M - 1);


        mainMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();

            for (int j = 0; j < M; j++) {
                mainMatrix[i][j] = Character.getNumericValue(string.charAt(j));
            }
        }
    }
}