package problem;

import java.util.*;
import java.io.*;


public class Gaaaaaaaaaarden {
    static int N, M;
    static int G, R;
    static int[][] mainMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static int maxNumFlower;

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

    public static class Node {
        Pos curPos;
        int numGreen;
        int numRed;
        int[][] matrix;

        public Node(Pos curPos, int numGreen, int numRed, int[][] matrix) {
            this.curPos = curPos;
            this.numGreen = numGreen;
            this.numRed = numRed;
            this.matrix = matrix;
        }
    }

    public static class FluidNode {
        Pos pos;
        int color;

        public FluidNode(Pos pos, int color) {
            this.pos = pos;
            this.color = color;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        recursive(new Node(new Pos(0, 0), G, R, new int[N][M]));

        System.out.println(maxNumFlower);
    }

    public static void recursive(Node node) {
        if (node.numGreen == 0 && node.numRed == 0) {
            bfs(node.matrix);
            return;
        }

        if (node.curPos.row >= N) { return; }

        Pos nextPos = getNextPos(node.curPos);

        // 그냥 넘어가는 경우
        recursive(new Node(nextPos, node.numGreen, node.numRed, node.matrix));
        if (mainMatrix[node.curPos.row][node.curPos.col] != 2) { return; }

        if (node.numGreen >= 1) {
            node.matrix[node.curPos.row][node.curPos.col] = 1;
            Node updatedNode = new Node(nextPos, node.numGreen - 1, node.numRed, node.matrix);
            recursive(updatedNode);

            node.matrix[node.curPos.row][node.curPos.col] = 0;
        }


        if (node.numRed >= 1) {
            node.matrix[node.curPos.row][node.curPos.col] = 2;
            Node updatedNode = new Node(nextPos, node.numGreen, node.numRed - 1, node.matrix);
            recursive(updatedNode);

            node.matrix[node.curPos.row][node.curPos.col] = 0;
        }
    }

    public static void bfs(int[][] matrix) {
        int[][] visited = new int[N][M];
        FluidNode[][] bfsMatrix = new FluidNode[N][M];

        Deque<FluidNode> queue = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (matrix[i][j] != 0) {
                    queue.add(new FluidNode(new Pos(i, j), matrix[i][j]));
                    visited[i][j] = 1;
                }
            }
        }

        int numFlower = 0;

        while (!queue.isEmpty()) {
            Map<Pos, Integer> movedPosMap = new HashMap<>();
            while (!queue.isEmpty()) {
                FluidNode fluidNode = queue.pollFirst();

                for (Pos direction : directions) {
                    Pos movedPos = fluidNode.pos.addPos(direction);
                    if (!movedPos.isValidIndex()) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] == 0) { continue; }
                    if (visited[movedPos.row][movedPos.col] == 1) { continue; }

                    movedPosMap.put(movedPos, movedPosMap.getOrDefault(movedPos, 0) | (1 << (fluidNode.color - 1)));
                }
            }

            Deque<FluidNode> updatedQueue = new ArrayDeque<>();
            for (Pos pos : movedPosMap.keySet()) {
                visited[pos.row][pos.col] = 1;
                if (movedPosMap.get(pos) == 3) {
                    numFlower += 1;
                    continue;
                }

                updatedQueue.add(new FluidNode(pos, movedPosMap.get(pos)));
            }

            queue = updatedQueue;
        }

        maxNumFlower = Math.max(maxNumFlower, numFlower);
    }

    public static Pos getNextPos(Pos pos) {
        Pos nextPos;
        if (pos.col == M - 1) {
            nextPos = new Pos(pos.row + 1, 0);
        }
        else {
            nextPos = new Pos(pos.row, pos.col + 1);
        }


        return nextPos;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        G = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());

        mainMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                mainMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        maxNumFlower = 0;
    }
}