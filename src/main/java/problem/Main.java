package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N, r, c, d;
    static int[][] mainMatrix, visited;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static int[] moveDirectionIndices = {3, 2, 1, 0};
    static Whale whale;
    static int numSea;

    public static class Pos implements Comparable<Pos> {
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

        public boolean canMove() {
            if (!isValidIndex()) { return false; }
            if (visited[this.row][this.col] == 1) { return false; }
            if (mainMatrix[this.row][this.col] == 1) { return false; }

            return true;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true;}
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

        @Override
        public int compareTo(Pos anotherPos) {
            if (this.row != anotherPos.row) {
                return Integer.compare(this.row, anotherPos.row);
            }

            return Integer.compare(this.col, anotherPos.col);
        }
    }

    public static class Node implements Comparable<Node> {
        Pos pos;
        int depth;
        int directionIndex;

        public Node(Pos pos, int depth, int directionIndex) {
            this.pos = pos;
            this.depth = depth;
            this.directionIndex = directionIndex;
        }


        @Override
        public int compareTo(Node anotherNode) {
            if (this.depth != anotherNode.depth) {
                return Integer.compare(this.depth, anotherNode.depth);
            }

            return this.pos.compareTo(anotherNode.pos);
        }
    }

    public static class Whale {
        Pos curPos;
        int directionIndex;

        public Whale(Pos curPos, int directionIndex) {
            this.curPos = curPos;
            this.directionIndex = directionIndex;
        }

        public boolean canNearMove() {
            for (Pos direction : directions) {
                Pos movedPos = this.curPos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }

                if (visited[movedPos.row][movedPos.col] == 1) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

                return true;
            }

            return false;
        }

        public void move() {
            if (canNearMove()) {
                firstMove();
            }
            else {
                secondMove();
            }
        }

        public void firstMove() {
            Pos movedPos;

            movedPos = this.curPos.addPos(directions[directionIndex]);
            if (movedPos.canMove()) {
                this.curPos = movedPos;

                return;
            }


            movedPos = this.curPos.addPos(directions[(directionIndex + 3) % 4]);
            if (movedPos.canMove()) {
                this.curPos = movedPos;
                this.directionIndex = (directionIndex + 3) % 4;

                return;
            }


            movedPos = this.curPos.addPos(directions[(directionIndex + 1) % 4]);
            if (movedPos.canMove()) {
                this.curPos = movedPos;
                this.directionIndex = (directionIndex + 1) % 4;

                return;
            }

            movedPos = this.curPos.addPos(directions[(directionIndex + 2) % 4]);
            if (movedPos.canMove()) {
                this.curPos = movedPos;
                this.directionIndex = (directionIndex + 2) % 4;

                return;
            }
        }


        public void secondMove() {
            Deque<Node> queue = new ArrayDeque<>();
            queue.add(new Node(this.curPos, 0, 0));

            int[][] moved = new int[N][N];

            Node minNode = new Node(new Pos(0, 0), Integer.MAX_VALUE / 2, 0);

            while (!queue.isEmpty()) {
                Node node = queue.pollFirst();
                if (moved[node.pos.row][node.pos.col] == 1) { continue; }
                moved[node.pos.row][node.pos.col] = 1;

                if (node.compareTo(minNode) < 0 && visited[node.pos.row][node.pos.col] == 0) {
                    minNode = node;
                }

                for (int moveDirectionIndex : moveDirectionIndices) {
                    Pos direction = directions[moveDirectionIndex];

                    Pos movedPos = node.pos.addPos(direction);
                    if (!movedPos.isValidIndex()) { continue; }
                    if (moved[movedPos.row][movedPos.col] == 1) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }


                    queue.add(new Node(movedPos, node.depth + 1, moveDirectionIndex));
                }
            }


            this.curPos = minNode.pos;
            this.directionIndex = minNode.directionIndex;
        }
    }


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        StringBuilder sb = new StringBuilder();
        sb.append(whale.curPos.row + 1 + " " + (whale.curPos.col + 1));
        sb.append("\n");

        for (int i = 0; i < numSea; i++) {
            whale.move();
            visited[whale.curPos.row][whale.curPos.col] = 1;

            sb.append(whale.curPos.row + 1 + " " + (whale.curPos.col + 1));
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken()) - 1;
        c = Integer.parseInt(st.nextToken()) - 1;
        d = Integer.parseInt(st.nextToken());

        int directionIndex;
        if (d == 1) {
            directionIndex = 0;
        }
        else if (d == 2) {
            directionIndex = 2;
        }
        else if (d == 3) {
            directionIndex = 3;
        }
        else {
            directionIndex = 1;
        }


        Pos startPos = new Pos(r, c);
        whale = new Whale(startPos, directionIndex);

        visited = new int[N][N];
        mainMatrix = new int[N][N];

        numSea = 0;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                mainMatrix[i][j] = Integer.parseInt(st.nextToken());

                if (mainMatrix[i][j] == 0) {
                    numSea += 1;
                }
            }
        }

        numSea -= 1;
        visited[r][c] = 1;
    }
}