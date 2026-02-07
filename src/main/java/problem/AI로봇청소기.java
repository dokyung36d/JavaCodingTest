package problem;

import java.util.*;
import java.io.*;



public class AI로봇청소기 {
    static int N, K, L;
    static int[][] mainMatrix;
    static Cleaner[] cleanerList;
    static int[][] cleanerPosMatrix;
    static Pos[] directions = {new Pos(0, 1), new Pos(1, 0), new Pos(0,-1), new Pos(-1, 0)};

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

        public void clean() {
            if (mainMatrix[this.row][this.col] < 0) {
                return;
            }

            else if (mainMatrix[this.row][this.col] <= 20) {
                mainMatrix[this.row][this.col] = 0;
                return;
            }

            mainMatrix[this.row][this.col] -= 20;
        }

        public int getNumDustRemoved() {
            if (mainMatrix[this.row][this.col] < 0) {
                return 0;
            }

            else if (mainMatrix[this.row][this.col] <= 20) {
                return mainMatrix[this.row][this.col];
            }

            return 20;
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
            return Objects.hash(this.row * N + this.col);
        }

        @Override
        public int compareTo(Pos anotherPos) {
            if (this.row != anotherPos.row) {
                return Integer.compare(this.row, anotherPos.row);
            }

            return Integer.compare(this.col, anotherPos.col);
        }
    }

    public static class Cleaner {
        int uniqueNum;
        Pos curPos;

        public Cleaner(int uniqueNum, Pos curPos) {
            this.uniqueNum = uniqueNum;
            this.curPos = curPos;
        }


        public void move() {
            if (mainMatrix[curPos.row][curPos.col] > 0) { return; }

            Deque<MoveNode> queue = new ArrayDeque<>();
            queue.add(new MoveNode(curPos, 0));


            MoveNode minMoveNode = new MoveNode(new Pos(0, 0), Integer.MAX_VALUE / 2);

            int[][] visited = new int[N][N];
            visited[curPos.row][curPos.col] = 1;
            while (!queue.isEmpty()) {
                MoveNode moveNode = queue.poll();
                if (moveNode.depth > minMoveNode.depth) {
                    break;
                }

                for (Pos direction : directions) {
                    Pos movedPos = moveNode.dustPos.addPos(direction);
                    if (!movedPos.isValidIndex()) { continue; }
                    if (visited[movedPos.row][movedPos.col] == 1) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] == -1) { continue; }
                    if (cleanerPosMatrix[movedPos.row][movedPos.col] == 1) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] == 0) {
                        queue.addLast(new MoveNode(movedPos, moveNode.depth + 1));
                        visited[movedPos.row][movedPos.col] = 1;
                        continue;
                    }

                    MoveNode moveNode1 = new MoveNode(movedPos, moveNode.depth + 1);
                    if (moveNode1.compareTo(minMoveNode) < 0) {
                        minMoveNode = moveNode1;
                    }
                }
            }


            if (minMoveNode.depth == Integer.MAX_VALUE / 2) { return; }
            cleanerPosMatrix[curPos.row][curPos.col] = 0;

            this.curPos = minMoveNode.dustPos;
            cleanerPosMatrix[this.curPos.row][this.curPos.col] = 1;
        }

        public void clean() {
            CleanNode minCleanNode = new CleanNode(0);
            minCleanNode.numDustRemoved = 0;

            for (int directionIndex = 0; directionIndex < 4; directionIndex++) {
                CleanNode cleanNode = new CleanNode(directionIndex);

                if (cleanNode.compareTo(minCleanNode) > 0) { continue; }
                minCleanNode = cleanNode;
            }


            int reverseDirectionIndex = (minCleanNode.directionIndex + 2) % 4;

            this.curPos.clean();
            for (int directionIndex = 0; directionIndex < 4; directionIndex++) {
                if (directionIndex == reverseDirectionIndex) { continue; }

                Pos movedPos = curPos.addPos(directions[directionIndex]);
                if (!movedPos.isValidIndex()) { continue; }

                movedPos.clean();
            }
        }

        public class MoveNode implements Comparable<MoveNode> {
            int depth;
            Pos dustPos;

            public MoveNode(Pos dustPos, int depth) {
                this.dustPos = dustPos;
                this.depth = depth;
            }

            @Override
            public int compareTo(MoveNode anotherMoveNode) {
                if (this.depth != anotherMoveNode.depth) {
                    return Integer.compare(this.depth, anotherMoveNode.depth);
                }

                return this.dustPos.compareTo(anotherMoveNode.dustPos);
            }
        }

        public class CleanNode implements Comparable<CleanNode> {
            int directionIndex;
            int numDustRemoved;

            public CleanNode(int directionIndex) {
                this.directionIndex = directionIndex;

                numDustRemoved = 0;
                numDustRemoved += curPos.getNumDustRemoved();
                for (int i = 0; i < 4; i++) {
                    Pos movedPos = curPos.addPos(directions[i]);
                    if (!movedPos.isValidIndex()) { continue; }

                    numDustRemoved += movedPos.getNumDustRemoved();
                }

                int reverseDirectionIndex = (directionIndex + 2) % 4;
                Pos reverseMovedPos = curPos.addPos(directions[reverseDirectionIndex]);
                if (reverseMovedPos.isValidIndex()) {
                    numDustRemoved -= reverseMovedPos.getNumDustRemoved();
                }
            }

            @Override
            public int compareTo(CleanNode anotherCleanNode) {
                if (this.numDustRemoved != anotherCleanNode.numDustRemoved) {
                    return Integer.compare(-this.numDustRemoved, -anotherCleanNode.numDustRemoved);
                }

                return Integer.compare(this.directionIndex, anotherCleanNode.directionIndex);
            }
        }

        public class PathNode implements Comparable<PathNode> {
            int depth;
            Pos curPos;

            public PathNode(int depth, Pos curPos) {
                this.depth = depth;
                this.curPos = curPos;
            }

            @Override
            public int compareTo(PathNode anotherPathNode) {
                return Integer.compare(this.depth, anotherPathNode.depth);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        // StringBuilder sb = new StringBuilder();

        for (int turn = 0; turn < L; turn++) {
            for (int i = 0; i < K; i++) {
                Cleaner cleaner = cleanerList[i];
                cleaner.move();
            }

            for (int i = 0; i < K; i++) {
                Cleaner cleaner = cleanerList[i];
                cleaner.clean();
            }

            accumulateDust();
            spread();

            int numDust = getNumDust();
            System.out.println(numDust);
            // sb.append(numDust + "\n");
        }

        // System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void accumulateDust() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] > 0) {
                    mainMatrix[i][j] += 5;
                }
            }
        }
    }

    public static void spread() {
        int[][] spreadMatrix = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] != 0) {
                    continue;
                }

                Pos curPos = new Pos(i, j);

                int numNearDust = 0;
                for (Pos direction : directions) {
                    Pos movedPos = curPos.addPos(direction);
                    if (!movedPos.isValidIndex()) {
                        continue;
                    }
                    if (mainMatrix[movedPos.row][movedPos.col] == -1) {
                        continue;
                    }

                    numNearDust += mainMatrix[movedPos.row][movedPos.col];
                }

                spreadMatrix[curPos.row][curPos.col] = numNearDust / 10;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                mainMatrix[i][j] += spreadMatrix[i][j];
            }
        }
    }

    public static int getNumDust() {
        int numDust = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] <= 0) { continue; }

                numDust += mainMatrix[i][j];
            }
        }

        return numDust;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());

        mainMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                mainMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }


        cleanerList = new Cleaner[K];
        cleanerPosMatrix = new int[N][N];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            int row = Integer.parseInt(st.nextToken()) - 1;
            int col = Integer.parseInt(st.nextToken()) - 1;

            cleanerList[i] = new Cleaner(i + 1, new Pos(row, col));
            cleanerPosMatrix[row][col] = 1;
        }
    }

}
