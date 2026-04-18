package problem;

import java.util.*;
import java.io.*;


public class 아기고래의첫항해 {
    static int N, r, c, d;
    static int[][] mainMatrix, visited;
    static Pos[] directions = {new Pos(0, -1), new Pos(1, 0), new Pos(0, 1), new Pos(-1, 0)};
    static Dolphin dolphin;
    static int numRemain;

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
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) { return false; }

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

        @Override
        public int compareTo(Pos anotherPos) {
            if (this.row != anotherPos.row) {
                return Integer.compare(this.row, anotherPos.row);
            }

            return Integer.compare(this.col, anotherPos.col);
        }
    }

    public static class Node implements Comparable<Node> {
        Pos curPos;
        int depth;
        int prevMoveDirectionIndex;

        public Node(Pos curPos, int depth, int prevMoveDirectionIndex) {
            this.curPos = curPos;
            this.depth = depth;
            this.prevMoveDirectionIndex = prevMoveDirectionIndex;
        }

        @Override
        public int compareTo(Node anotherNode) {
            if (this.depth != anotherNode.depth) {
                return Integer.compare(this.depth, anotherNode.depth);
            }

            return this.curPos.compareTo(anotherNode.curPos);
        }
    }

    public static class Dolphin {
        Pos pos;
        int directionIndex;

        public Dolphin(Pos pos, int directionIndex) {
            this.pos = pos;
            this.directionIndex = directionIndex;
        }

        public boolean firstMove() {
            Pos movedPos;

            movedPos = this.pos.addPos(directions[this.directionIndex]);
            if (canMove(movedPos)) {
                this.pos = movedPos;
                visited[movedPos.row][movedPos.col] = 1;

                return true;
            }


            movedPos = this.pos.addPos(directions[(this.directionIndex + 1) % 4]);
            if (canMove(movedPos)) {
                this.pos = movedPos;
                visited[movedPos.row][movedPos.col] = 1;
                this.directionIndex = (this.directionIndex + 1) % 4;

                return true;
            }


            movedPos = this.pos.addPos(directions[(this.directionIndex + 3) % 4]);
            if (canMove(movedPos)) {
                this.pos = movedPos;
                visited[movedPos.row][movedPos.col] = 1;
                this.directionIndex = (this.directionIndex + 3) % 4;

                return true;
            }


            movedPos = this.pos.addPos(directions[(this.directionIndex + 2) % 4]);
            if (canMove(movedPos)) {
                this.pos = movedPos;
                visited[movedPos.row][movedPos.col] = 1;
                this.directionIndex = (this.directionIndex + 2) % 4;

                return true;
            }

            return false;
        }


        public Pos getSecondMovePos() {
            Deque<Node> queue = new ArrayDeque<>();
            queue.add(new Node(this.pos, 0, 0));

            int[][] visitedWhenMove = new int[N][N];

            Node minNode = new Node(new Pos(0, 0), Integer.MAX_VALUE / 2, 0);
            while (!queue.isEmpty()) {
                Node node = queue.poll();

                if (visitedWhenMove[node.curPos.row][node.curPos.col] == 1) { continue; }
                visitedWhenMove[node.curPos.row][node.curPos.col] = 1;

                if (visited[node.curPos.row][node.curPos.col] == 0 && node.compareTo(minNode) < 0) {
                    minNode = node;
                }


                for (Pos direction : directions) {
                    Pos movedPos = node.curPos.addPos(direction);
                    if (!movedPos.isValidIndex()) { continue; }
                    if (visitedWhenMove[movedPos.row][movedPos.col] == 1) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }
                    if (node.depth + 1 > minNode.depth) { continue; }

                    queue.add(new Node(movedPos, node.depth + 1, 0));

                }
            }


            return minNode.curPos;
        }

        public void secondMove(Pos destPos) {
            Deque<Node> queue = new ArrayDeque<>();
            queue.add(new Node(this.pos, 0, 0));

            int[][] visitedWhenMove = new int[N][N];

            while (!queue.isEmpty()) {
                Node node = queue.pollFirst();

                if (visitedWhenMove[node.curPos.row][node.curPos.col] == 1) { continue; }
                visitedWhenMove[node.curPos.row][node.curPos.col] = 1;

                if (node.curPos.equals(destPos)) {
                    this.pos = node.curPos;
                    this.directionIndex = node.prevMoveDirectionIndex;

                    return;
                }

                for (int i = 0; i < 4; i++) {
                    Pos movedPos = node.curPos.addPos(directions[i]);
                    if (!movedPos.isValidIndex()) { continue; }
                    if (visitedWhenMove[movedPos.row][movedPos.col] == 1) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

                    queue.add(new Node(movedPos, node.depth + 1, i));
                }
            }

        }
    }


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {

        StringBuilder sb = new StringBuilder();

        sb.append(dolphin.pos.row + 1 + " " + (dolphin.pos.col + 1) + "\n");
        for (int i = 0; i < numRemain - 1; i++) {
            boolean moved = dolphin.firstMove();
            if (moved) {
                sb.append(dolphin.pos.row + 1 + " " + (dolphin.pos.col + 1) + "\n");
                visited[dolphin.pos.row][dolphin.pos.col] = 1;
                continue; }

            Pos destPos = dolphin.getSecondMovePos();
            dolphin.secondMove(destPos);
            visited[dolphin.pos.row][dolphin.pos.col] = 1;
            sb.append(dolphin.pos.row + 1 + " " + (dolphin.pos.col + 1) + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static boolean canMove(Pos movedPos) {
        if (!movedPos.isValidIndex()) { return false; }
        if (visited[movedPos.row][movedPos.col] == 1) { return false; }
        if (mainMatrix[movedPos.row][movedPos.col] == 1) { return false; }


        return true;
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
            directionIndex = 3;
        }
        else if (d == 2) {
            directionIndex = 1;
        }
        else if (d == 3) {
            directionIndex = 0;
        }
        else {
            directionIndex = 2;
        }

        dolphin = new Dolphin(new Pos(r, c), directionIndex);


        visited = new int[N][N];
        visited[r][c] = 1;

        mainMatrix = new int[N][N];
        numRemain = 0;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                mainMatrix[i][j] = Integer.parseInt(st.nextToken());

                if (mainMatrix[i][j] == 0) {
                    numRemain += 1;
                }
            }
        }

    }
}
