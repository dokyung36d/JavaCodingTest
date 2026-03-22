package problem;

import java.util.*;
import java.io.*;


public class AI로봇청소기 {
    static int N, K, L;
    static int[][] mainMatrix;
    static Map<Integer, Cleaner> cleanerMap;
    static Map<Pos, Cleaner> posCleanerMap;
    static Pos[] directions = {new Pos(0, 1), new Pos(1, 0), new Pos(0, -1), new Pos(-1, 0)};

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
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
                return false;
            }

            return true;
        }

        public int calcDistance(Pos anotherPos) {
            return Math.abs(this.row - anotherPos.row) + Math.abs(this.col - anotherPos.col);
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

    public static class MoveNode implements Comparable<MoveNode> {
        int distance;
        Pos destPos;

        public MoveNode(int distance, Pos destPos) {
            this.distance = distance;
            this.destPos = destPos;
        }

        @Override
        public int compareTo(MoveNode anotherMoveNode) {
            if (this.distance != anotherMoveNode.distance) {
                return Integer.compare(this.distance, anotherMoveNode.distance);
            }

            if (this.destPos.row != anotherMoveNode.destPos.row) {
                return Integer.compare(this.destPos.row, anotherMoveNode.destPos.row);
            }

            return Integer.compare(this.destPos.col, anotherMoveNode.destPos.col);
        }
    }

    public static class Cleaner {
        int pk;
        Pos pos;

        public Cleaner(int pk, Pos pos) {
            this.pk = pk;
            this.pos = pos;
        }
    }


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        List<Integer> uniqueNumList = new ArrayList<>(cleanerMap.keySet());
        Collections.sort(uniqueNumList);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < L; i++) {
            for (int uniqueNum : uniqueNumList) {
                Cleaner cleaner = cleanerMap.get(uniqueNum);

                Pos movedPos = getClosestPos(uniqueNum);
                updateCleanerPos(uniqueNum, movedPos);
            }

            for (int uniqueNum : uniqueNumList) {
                doBestClean(uniqueNum);
            }

            accumulateDust();
            spreadDust();

            sb.append(getNumDust() + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static Pos getClosestPos(int uniqueNum) {
        Cleaner cleaner = cleanerMap.get(uniqueNum);

        Deque<MoveNode> queue = new ArrayDeque<>();
        queue.add(new MoveNode(0, cleaner.pos));

        MoveNode answerMoveNode = new MoveNode(Integer.MAX_VALUE / 2, cleaner.pos);

        int[][] visited = new int[N][N];
        while (!queue.isEmpty()) {
            MoveNode moveNode = queue.poll();
            if (visited[moveNode.destPos.row][moveNode.destPos.col] == 1) { continue; }
            visited[moveNode.destPos.row][moveNode.destPos.col] = 1;

            if (moveNode.compareTo(answerMoveNode) < 0 && mainMatrix[moveNode.destPos.row][moveNode.destPos.col] > 0) {
                answerMoveNode = moveNode;
            }

            for (Pos direction : directions) {
                Pos movedPos = moveNode.destPos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }
                if (visited[movedPos.row][movedPos.col] != 0) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] == -1) { continue; }
                if (posCleanerMap.get(movedPos) != null) { continue; }
                if (moveNode.distance + 1 > answerMoveNode.distance) { continue; }

                queue.add(new MoveNode(moveNode.distance + 1, movedPos));
            }
        }

        return answerMoveNode.destPos;
    }

    public static void doBestClean(int uniqueNum) {
        class CleanNode {
            int directionIndex;
            int numClean;

            CleanNode(int directionIndex, int numClean) {
                this.directionIndex = directionIndex;
                this.numClean = numClean;
            }
        }

        Cleaner cleaner = cleanerMap.get(uniqueNum);
        CleanNode bestCleanNode = new CleanNode(0, 0);

        for (int i = 0; i < 4; i++) {
            int cleanNum = getCleanNum(cleaner.pos, i);

            if (cleanNum > bestCleanNode.numClean) {
                bestCleanNode = new CleanNode(i, cleanNum);
            }
        }

        clean(cleaner.pos, bestCleanNode.directionIndex);
    }

    public static int getCleanNum(Pos centerPos, int directionIndex) {
        int reverseDirectionIndex = (directionIndex + 2) % 4;

        int numClean = 0;
        for (int i = 0; i < 4; i++) {
            if (i == reverseDirectionIndex) { continue; }

            Pos movedPos = centerPos.addPos(directions[i]);
            if (!movedPos.isValidIndex()) { continue; }
            if (mainMatrix[movedPos.row][movedPos.col] == -1) { continue; }
            if (mainMatrix[movedPos.row][movedPos.col] == 0) { continue; }

            numClean += Math.min(20, mainMatrix[movedPos.row][movedPos.col]);
        }
        numClean += Math.min(20, mainMatrix[centerPos.row][centerPos.col]);


        return numClean;
    }

    public static void clean(Pos centerPos, int directionIndex) {
        int reverseDirectionIndex = (directionIndex + 2) % 4;

        for (int i = 0; i < 4; i++) {
            if (i == reverseDirectionIndex) { continue; }

            Pos movedPos = centerPos.addPos(directions[i]);
            if (!movedPos.isValidIndex()) { continue; }
            if (mainMatrix[movedPos.row][movedPos.col] == -1) { continue; }

            if (mainMatrix[movedPos.row][movedPos.col] <= 20) {
                mainMatrix[movedPos.row][movedPos.col] = 0;
            }
            else {
                mainMatrix[movedPos.row][movedPos.col] -= 20;
            }
        }

        if (mainMatrix[centerPos.row][centerPos.col] <= 20) {
            mainMatrix[centerPos.row][centerPos.col] = 0;
        }

        else {
            mainMatrix[centerPos.row][centerPos.col] -= 20;
        }
    }

    public static void accumulateDust() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] <= 0) { continue; }

                mainMatrix[i][j] += 5;
            }
        }
    }

    public static void spreadDust() {
        int[][] spreadMatrix = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] != 0) { continue; }

                for (Pos direction : directions) {
                    Pos movedPos = new Pos(i, j).addPos(direction);

                    if (!movedPos.isValidIndex()) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] <= 0) { continue; }

                    spreadMatrix[i][j] += mainMatrix[movedPos.row][movedPos.col];
                }
            }
        }


        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] != 0) { continue; }

                mainMatrix[i][j] += (spreadMatrix[i][j] / 10);
            }
        }
    }

    public static void updateCleanerPos(int uniqueNum, Pos movedPos) {
        posCleanerMap.remove(cleanerMap.get(uniqueNum).pos);
        posCleanerMap.put(movedPos, cleanerMap.get(uniqueNum));

        cleanerMap.put(uniqueNum, new Cleaner(uniqueNum, movedPos));
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

        cleanerMap = new HashMap<>();
        posCleanerMap = new HashMap<>();
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            int row = Integer.parseInt(st.nextToken()) - 1;
            int col = Integer.parseInt(st.nextToken()) - 1;

            cleanerMap.put(i + 1, new Cleaner(i + 1, new Pos(row, col)));
            posCleanerMap.put(new Pos(row, col), new Cleaner(i + 1, new Pos(row, col)));
        }
    }

}
