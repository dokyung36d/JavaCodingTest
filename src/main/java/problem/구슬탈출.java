package problem;

import java.util.*;
import java.io.*;



public class 구슬탈출 {
    static int N, M;
    static char[][] mainMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static Pos redStartPos, blueStartPos, destPos;
    static Map<Status, Integer> visitedMap;


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

        public char getValue() {
            return mainMatrix[this.row][this.col];
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
            if (this.row == anotherPos.row && this.col == anotherPos.col) { return true; }

            return false;
        }
    }

    public static class Status {
        Pos redPos;
        Pos bluePos;

        public Status(Pos redPos, Pos bluePos) {
            this.redPos = redPos;
            this.bluePos = bluePos;
        }

        public Status getMovedStatus(int directionIndex) {
            if (directionIndex == 0) {
                return moveUp();
            }

            else if (directionIndex == 1) {
                return moveRight();
            }

            else if (directionIndex == 2) {
                return moveDown();
            }

            return moveLeft();
        }

        public Status moveUp() {
            Pos movedRedPos;
            Pos movedBluePos;

            if (redPos.row <= bluePos.row) {
                movedRedPos = move(redPos, directions[0], bluePos);
                movedBluePos = move(bluePos, directions[0], movedRedPos);
            }

            else {
                movedBluePos = move(bluePos, directions[0], redPos);
                movedRedPos = move(redPos, directions[0], movedBluePos);
            }

            return new Status(movedRedPos, movedBluePos);
        }

        public Status moveRight() {
            Pos movedRedPos;
            Pos movedBluePos;

            if (redPos.col >= bluePos.col) {
                movedRedPos = move(redPos, directions[1], bluePos);
                movedBluePos = move(bluePos, directions[1], movedRedPos);
            }

            else {
                movedBluePos = move(bluePos, directions[1], redPos);
                movedRedPos = move(redPos, directions[1], movedBluePos);
            }

            return new Status(movedRedPos, movedBluePos);
        }

        public Status moveDown() {
            Pos movedRedPos;
            Pos movedBluePos;

            if (redPos.row >= bluePos.row) {
                movedRedPos = move(redPos, directions[2], bluePos);
                movedBluePos = move(bluePos, directions[2], movedRedPos);
            }

            else {
                movedBluePos = move(bluePos, directions[2], redPos);
                movedRedPos = move(redPos, directions[2], movedBluePos);
            }

            return new Status(movedRedPos, movedBluePos);
        }

        public Status moveLeft() {
            Pos movedRedPos;
            Pos movedBluePos;

            if (redPos.col <= bluePos.col) {
                movedRedPos = move(redPos, directions[3], bluePos);
                movedBluePos = move(bluePos, directions[3], movedRedPos);
            }

            else {
                movedBluePos = move(bluePos, directions[3], redPos);
                movedRedPos = move(redPos, directions[3], movedBluePos);
            }

            return new Status(movedRedPos, movedBluePos);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.redPos, this.bluePos);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Status anotherStatus = (Status) obj;
            if (this.redPos.equals(anotherStatus.redPos) && this.bluePos.equals(anotherStatus.bluePos)) {
                return true;
            }

            return false;
        }
    }

    public static class Node {
        Status status;
        int depth;

        public Node(Status status, int depth) {
            this.status = status;
            this.depth = depth;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        Queue<Node> queue = new ArrayDeque<>();
        queue.add(new Node(new Status(redStartPos, blueStartPos), 0));

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (visitedMap.get(node.status) != null) { continue; }
            visitedMap.put(node.status, 1);
            if (node.depth == 10) { break; }

            for (int directionIndex = 0; directionIndex < 4; directionIndex++) {
                Status movedStatus = node.status.getMovedStatus(directionIndex);
                if (visitedMap.get(movedStatus) != null) { continue; }
                if (movedStatus.bluePos.getValue() == 'O') { continue; }
                if (movedStatus.redPos.getValue() == 'O' && movedStatus.bluePos.addPos(directions[directionIndex]).getValue() == 'O') {
                    continue;
                }
                if (movedStatus.redPos.getValue() == 'O' && movedStatus.bluePos.addPos(directions[directionIndex]).getValue() != 'O') {
                    System.out.println(node.depth + 1);
                    return;
                }

                queue.add(new Node(movedStatus, node.depth + 1));
            }
        }

        System.out.println(-1);
    }

    public static Pos move(Pos curPos, Pos direction, Pos anotherColorPos) {
        while (true) {
            Pos movedPos = curPos.addPos(direction);
            if (!movedPos.isValidIndex()) { break; }
            if (movedPos.equals(anotherColorPos)) { break; }
            if (movedPos.getValue() == '#') { break; }
            if (movedPos.getValue() == '.') {
                curPos = movedPos;
                continue;
            }
            if (movedPos.getValue() == 'O') {
                curPos = movedPos;
                break;
            }

            break;
        }

        return curPos;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        mainMatrix = new char[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();

            for (int j = 0; j < M; j++) {
                mainMatrix[i][j] = string.charAt(j);

                if (mainMatrix[i][j] == 'R') {
                    redStartPos = new Pos(i, j);
                    mainMatrix[i][j] = '.';
                }
                else if (mainMatrix[i][j] == 'B') {
                    blueStartPos = new Pos(i, j);
                    mainMatrix[i][j] = '.';
                }

                else if (mainMatrix[i][j] == 'O') {
                    destPos = new Pos(i, j);
                }
            }
        }

        visitedMap = new HashMap<>();
    }
}
