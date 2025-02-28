package problem;

import java.util.*;
import java.io.*;


public class 코드트리빵 {
    static int n, m;
    static int[][] baseCampMatrix;
    static int[][] prohibitedMatrix;
    static Deque<Pos> storePosQueue;
    static List<Person> playerInMatrixList;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, -1), new Pos(0, 1), new Pos(1, 0)};

    public static class Pos implements Comparable<Pos> {
        int row;
        int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Pos addPos(Pos anotherPos) {
            return new Pos(this.row + anotherPos.row, this.col + anotherPos.col);
        }

        public boolean isValidIndex() {
            if (this.row < 0 || this.row >= n || this.col < 0 || this.col >= n) {
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

        @Override
        public int compareTo(Pos anotherPos) {
            if (this.row != anotherPos.row) {
                return Integer.compare(this.row, anotherPos.row);
            }

            return Integer.compare(this.col, anotherPos.col);
        }
    }

    public static class Person {
        Pos curPos;
        Pos destPos;

        public Person(Pos curPos, Pos destPos) {
            this.curPos = curPos;
            this.destPos = destPos;
        }

        public Pos getNextPos() {
            List<Pos> path = getFastestPath(curPos, destPos);

            return path.get(0);
        }
    }

    public static class Node {
        Pos pos;
        List<Pos> path;

        public Node(Pos pos, List<Pos> path) {
            this.pos = pos;
            this.path = path;
        }
    }

    public static List<Pos> getFastestPath(Pos startPos, Pos destPos) {
        Deque<Node> queue = new ArrayDeque<>();
        queue.add(new Node(startPos, new ArrayList<>()));
        int[][] visited = new int[n][n];

        while (!queue.isEmpty()) {
            Node node = queue.pollFirst();
            if (visited[node.pos.row][node.pos.col] == 1) { continue; }
            visited[node.pos.row][node.pos.col] = 1;

            for (Pos direction : directions) {
                Pos movedPos = node.pos.addPos(direction);
                if (!movedPos.isValidIndex() || visited[movedPos.row][movedPos.col] == 1) { continue; }
                if (prohibitedMatrix[movedPos.row][movedPos.col] == 1) { continue; }

                List<Pos> copiedPath = new ArrayList<>(node.path);
                copiedPath.add(movedPos);
                if (movedPos.equals(destPos)) {
                    return copiedPath;
                }

                queue.add(new Node(movedPos, copiedPath));
            }
        }

        return new ArrayList<>();
    }

    public static Pos getNearestBaseCamp(Pos destPos) {
        int minDistance = Integer.MAX_VALUE;
        Pos baseCampPos = new Pos(n - 1, n - 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (baseCampMatrix[i][j] == 0) { continue; }

                List<Pos> path = getFastestPath(new Pos(i, j), destPos);
                if (path.size() == 0) { continue; }
                if (path.size() < minDistance) {
                    minDistance = path.size();
                    baseCampPos = new Pos(i, j);
                }
                else if (path.size() == minDistance && new Pos(i, j).compareTo(baseCampPos) < 0) {
                    baseCampPos = new Pos(i, j);
                }
            }
        }

        return baseCampPos;
    }

    public static void main(String[] args) throws Exception {
        init();


        for (int time = 1; time < Integer.MAX_VALUE; time++) {
            allPlayerInMatrixMove();

            if (!storePosQueue.isEmpty()) {
                Pos storePos = storePosQueue.pollFirst();
                Pos baseCampPos = getNearestBaseCamp(storePos);

                playerInMatrixList.add(new Person(baseCampPos, storePos));
                prohibitedMatrix[baseCampPos.row][baseCampPos.col] = 1;
                baseCampMatrix[baseCampPos.row][baseCampPos.col] = 0;
            }

            if (playerInMatrixList.size() == 0) {
                System.out.println(time);
                return;
            }
        }
    }

    public static void allPlayerInMatrixMove() {
        List<Pos> prohibitedPosList = new ArrayList<>();
        List<Person> updatedPlayerInMatrixList = new ArrayList<>();
        for (Person person : playerInMatrixList) {
            List<Pos> path = getFastestPath(person.curPos, person.destPos);
            Pos nextPos = path.get(0);
            if (nextPos.equals(person.destPos)) {
                prohibitedPosList.add(nextPos);
                continue;
            }

            Person movedPerson = new Person(nextPos, person.destPos);
            updatedPlayerInMatrixList.add(movedPerson);
        }


        for (Pos prohibitedPlace : prohibitedPosList) {
            prohibitedMatrix[prohibitedPlace.row][prohibitedPlace.col] = 1;
        }

        playerInMatrixList = updatedPlayerInMatrixList;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        baseCampMatrix = new int[n][n];
        prohibitedMatrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                baseCampMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        storePosQueue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int row = Integer.parseInt(st.nextToken()) - 1;
            int col = Integer.parseInt(st.nextToken()) - 1;

            storePosQueue.add(new Pos(row, col));
        }

        playerInMatrixList = new ArrayList<>();
    }
}