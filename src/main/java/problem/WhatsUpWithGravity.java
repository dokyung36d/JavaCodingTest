package problem;

import java.util.*;
import java.io.*;


public class WhatsUpWithGravity {
    static int N, M;
    static char[][] originalMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static Pos startPos, destPos;


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

    public static class Node implements Comparable<Node> {
        Pos pos;
        int flipped;
        int numFlipped;

        public Node(Pos pos, int flipped, int numFlipped) {
            this.pos = pos;
            this.flipped = flipped;
            this.numFlipped = numFlipped;
        }

        public boolean isLeftMovePossible() {
            Pos movedPos = this.pos.addPos(directions[3]);
            if (!movedPos.isValidIndex()) { return false; }
            if (originalMatrix[movedPos.row][movedPos.col] == '#') {
                return false;
            }

            Pos floorCellPos = getFloorCellPos(movedPos);
            if (floorCellPos == null) { return false; }

            return true;
        }

        public boolean isRightMovePossible() {
            Pos movedPos = this.pos.addPos(directions[1]);
            if (!movedPos.isValidIndex()) { return false; }
            if (originalMatrix[movedPos.row][movedPos.col] == '#') {
                return false;
            }

            Pos floorCellPos = getFloorCellPos(movedPos);
            if (floorCellPos == null) { return false; }

            return true;
        }

        public boolean isFlipPosiible() {
            if (flipped == 0) {
                for (int i = pos.row - 1; i >= 0; i--) {
                    if (originalMatrix[i][pos.col] == '#') {
                        return true;
                    }
                }
            }

            else {
                for (int i = pos.row + 1; i < N; i++) {
                    if (originalMatrix[i][pos.col] == '#') {
                        return true;
                    }
                }
            }

            return false;
        }

        public Node getFlippedNode() {
            if (flipped == 0) {
                for (int i = pos.row - 1; i >= 0; i--) {
                    if (originalMatrix[i][pos.col] == '#') {
                        return new Node(new Pos(i + 1, pos.col), 1, this.numFlipped + 1);
                    }
                }
            }

            else {
                for (int i = pos.row + 1; i < N; i++) {
                    if (originalMatrix[i][pos.col] == '#') {
                        return new Node(new Pos(i - 1, pos.col), 0, this.numFlipped + 1);
                    }
                }
            }

            return null;
        }


        public Pos getFloorCellPos(Pos curPos) {
            if (flipped == 0) {
                for (int i = curPos.row + 1; i < N; i++) {
                    if (originalMatrix[i][curPos.col] == '#') {
                        return new Pos(i, curPos.col);
                    }
                }
            }

            else {
                for (int i = curPos.row - 1; i >= 0; i--) {
                    if (originalMatrix[i][curPos.col] == '#') {
                        return new Pos(i, curPos.col);
                    }
                }
            }

            return null;
        }

        public int compareTo(Node anotherNode) {
            return Integer.compare(this.numFlipped, anotherNode.numFlipped);
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        int[][][] visited = new int[N][M][2];

        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(startPos, 0, 0));

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (visited[node.pos.row][node.pos.col][node.flipped] == 1) { continue; }
            visited[node.pos.row][node.pos.col][node.flipped] = 1;

            if (destPos.equals(node.pos)) {
                System.out.println(node.numFlipped);
                return;
            }

            //Fall Down
            if (node.flipped == 0) {
                for (int i = node.pos.row + 1; i < N; i++) {
                    if (destPos.equals(new Pos(i, node.pos.col))) {
                        System.out.println(node.numFlipped);
                        return;
                    }

                    if (originalMatrix[i][node.pos.col] == '.') {
                        node.pos = new Pos(i, node.pos.col);
                        continue;
                    }
                    break;
                }

                if (node.pos.row == N - 1) { continue; }
            }

            else {
                for (int i = node.pos.row - 1; i >= 0; i--) {
                    if (destPos.equals(new Pos(i, node.pos.col))) {
                        System.out.println(node.numFlipped);
                        return;
                    }

                    if (originalMatrix[i][node.pos.col] == '.') {
                        node.pos = new Pos(i, node.pos.col);
                        continue;
                    }
                    break;
                }

                if (node.pos.row == 0) { continue; }
            }


            Pos leftMovedPos = node.pos.addPos(directions[3]);
            if (leftMovedPos.isValidIndex() && visited[leftMovedPos.row][leftMovedPos.col][node.flipped] == 0 &&
                    originalMatrix[leftMovedPos.row][leftMovedPos.col] == '.') {
                pq.add(new Node(leftMovedPos, node.flipped, node.numFlipped));
            }

            Pos rightMovedPos = node.pos.addPos(directions[1]);
            if (rightMovedPos.isValidIndex() && visited[rightMovedPos.row][rightMovedPos.col][node.flipped] == 0 &&
                    originalMatrix[rightMovedPos.row][rightMovedPos.col] == '.') {
                pq.add(new Node(rightMovedPos, node.flipped, node.numFlipped));
            }


            //Flip
            pq.add(new Node(node.pos,1 - node.flipped, node.numFlipped + 1));


        }
        System.out.println(-1);

    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        originalMatrix = new char[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();
            for (int j = 0; j < M; j++) {
                originalMatrix[i][j] = string.charAt(j);


                if (originalMatrix[i][j] == 'C') {
                    startPos = new Pos(i, j);
                    originalMatrix[i][j] = '.';
                }

                if (originalMatrix[i][j] == 'D') {
                    destPos= new Pos(i, j);

                    originalMatrix[i][j] = '.';
                }
            }
        }
    }
}