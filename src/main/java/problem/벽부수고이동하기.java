package problem;

import java.util.*;
import java.io.*;


public class 벽부수고이동하기 {
    static int N, M;
    static int[][] mainMatrix;
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
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= M) { return false; }
            return true;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true;}
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

    public static class Node {
        Pos curPos;
        int canBreak;
        int distance;


        public Node(Pos curPos, int canBreak, int distance) {
            this.curPos = curPos;
            this.canBreak = canBreak;
            this.distance = distance;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int[][][] visited = new int[N][M][2];
        int[][] distanceMatrix = new int[N][M];

        Deque<Node> queue = new ArrayDeque<>();
        queue.add(new Node(new Pos(0, 0), 1, 0));

        Pos destPos = new Pos(N - 1, M - 1);
        while (!queue.isEmpty()) {
            Node node = queue.poll();

            if (node.curPos.equals(destPos)) {
                System.out.println(node.distance + 1);
                return;
            }

            if (visited[node.curPos.row][node.curPos.col][node.canBreak] == 1) { continue; }
            visited[node.curPos.row][node.curPos.col][node.canBreak] = 1;
            distanceMatrix[node.curPos.row][node.curPos.col] = node.distance;

            for (Pos direction : directions) {
                Pos movedPos = node.curPos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }
                if (visited[movedPos.row][movedPos.col][node.canBreak] == 1) { continue; }

                if (mainMatrix[movedPos.row][movedPos.col] == 0) {
                    queue.add(new Node(movedPos, node.canBreak, node.distance + 1));
                }

                if (mainMatrix[movedPos.row][movedPos.col] == 1 && node.canBreak == 1) {
                    queue.add(new Node(movedPos, 0, node.distance + 1));
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


        mainMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();

            for (int j = 0; j < M; j++) {
                char charNum = string.charAt(j);
                int num = Character.getNumericValue(charNum);

                mainMatrix[i][j] = num;
            }
        }
    }
}
