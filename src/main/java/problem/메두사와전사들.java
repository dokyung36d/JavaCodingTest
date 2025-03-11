package problem;

import java.util.*;
import java.io.*;


public class 메두사와전사들 {
    static int N, M;
    static Pos medusaHomePos, parkPos;
    static Pos[] medusaDirections = {new Pos(-1, 0), new Pos(1, 0), new Pos(0, -1), new Pos(0, 1)};
    static Pos[] warriorFirstDirections = {new Pos(-1, 0), new Pos(1, 0), new Pos(0, -1), new Pos(0, 1)};
    static Pos[] warriorSecondDirections = {new Pos(0, -1), new Pos(0, 1), new Pos(-1, 0), new Pos(1, 0)};
    static int[][] roadMatrix;
    static List<Warrior> warriorList;
    static Map<Pos, Integer> warriorPosMap;
    static int totalNumMoved, totalNumWarriorRocked, totalNumWarriorAttackedMedusa;

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

    public static class Node {
        Pos pos;
        List<Pos> path;

        public Node(Pos pos, List<Pos> path) {
            this.pos = pos;
            this.path = path;
        }
    }

    public static class Warrior {
        int uniqueNum;
        Pos pos;

        public Warrior(int uniqueNum, Pos pos) {
            this.uniqueNum = uniqueNum;
            this.pos = pos;
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        warriorPosMap = getWarriorPosMap();
        List<Pos> path = getFastestPath();
        if (path.size() == 0) {
            System.out.println(-1);
            return;
        }

        for (Pos curMedusaPos : path) {
            if (curMedusaPos.equals(parkPos)) {
                break;
            }
            totalNumMoved = 0;
            totalNumWarriorRocked = 0;
            totalNumWarriorAttackedMedusa = 0;

            int[][] bestSeeMatrix = getBestSeeMatrix(curMedusaPos);
            totalNumWarriorRocked = getNumWarriorRocked(bestSeeMatrix);

            moveWarrior(bestSeeMatrix, curMedusaPos, warriorFirstDirections);
            moveWarrior(bestSeeMatrix, curMedusaPos, warriorSecondDirections);

            warriorPosMap = getWarriorPosMap();


            System.out.println(totalNumMoved + " " + totalNumWarriorRocked + " " + totalNumWarriorAttackedMedusa);
        }

        System.out.println(0);
    }

    public static void moveWarrior(int[][] seeMatrix, Pos medusaPos, Pos[] directionOrder) {
        List<Warrior> updatedWarriorList = new ArrayList<>();
        for (Warrior warrior : warriorList) {
            if (warrior.pos.equals(medusaPos)) {
                continue;
            }
            if (seeMatrix[warrior.pos.row][warrior.pos.col] == 1) {
                updatedWarriorList.add(warrior);
                continue;
            }

            int prevDistance = warrior.pos.calcDistance(medusaPos);
            int flag = 0;
            for (Pos direction : directionOrder) {
                Pos movedPos = warrior.pos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }
                if (seeMatrix[movedPos.row][movedPos.col] == 1) { continue; }
                if (movedPos.calcDistance(medusaPos) >= prevDistance) { continue; }

                flag = 1;
                totalNumMoved += 1;
                if (movedPos.equals(medusaPos)) {
                    totalNumWarriorAttackedMedusa += 1;
                    break;
                }
                updatedWarriorList.add(new Warrior(warrior.uniqueNum, movedPos));
                break;
            }

            if (flag == 0) {
                updatedWarriorList.add(warrior);
            }
        }

        warriorList = updatedWarriorList;
    }

    public static int[][] getBestSeeMatrix(Pos medusaPos) {
        int maxWarriorRocked = -1;
        int[][] bestSeeMatrix = new int[N][N];

        int[][] seeUpMatrix = seeUp(medusaPos);
        int seeUpNumRocked = getNumWarriorRocked(seeUpMatrix);
        if (seeUpNumRocked > maxWarriorRocked) {
            bestSeeMatrix = seeUpMatrix;
            maxWarriorRocked = seeUpNumRocked;
        }


        int[][] seeDownMatrix = seeDown(medusaPos);
        int seeDownNumRocked = getNumWarriorRocked(seeDownMatrix);
        if (seeDownNumRocked > maxWarriorRocked) {
            bestSeeMatrix = seeDownMatrix;
            maxWarriorRocked = seeDownNumRocked;
        }


        int[][] seeLeftMatrix = seeLeft(medusaPos);
        int seeLeftNumRocked = getNumWarriorRocked(seeLeftMatrix);
        if (seeLeftNumRocked > maxWarriorRocked) {
            bestSeeMatrix = seeLeftMatrix;
            maxWarriorRocked = seeLeftNumRocked;
        }


        int[][] seeRightMatrix = seeRight(medusaPos);
        int seeRightNumRocked = getNumWarriorRocked(seeRightMatrix);
        if (seeRightNumRocked > maxWarriorRocked) {
            bestSeeMatrix = seeRightMatrix;
            maxWarriorRocked = seeRightNumRocked;
        }


        return bestSeeMatrix;
    }

    public static int getNumWarriorRocked(int[][] seeMatrix) {
        int numRocked = 0;

        for (Warrior warrior : warriorList) {
            if (seeMatrix[warrior.pos.row][warrior.pos.col] == 1) {
                numRocked += 1;
            }
        }

        return numRocked;
    }

    public static int[][] seeLeft(Pos curMedusaPos) {
        int[][] seeMatrix = new int[N][N];

        for (int j = curMedusaPos.col - 1; j >= 0; j--) {
            int upRow = Math.max(0, curMedusaPos.row - (curMedusaPos.col - j));
            int downRow = Math.min(N - 1, curMedusaPos.row + (curMedusaPos.col - j));

            for (int i = upRow; i <= downRow; i++) {
                seeMatrix[i][j] = 1;
            }
        }

        for (int j = curMedusaPos.col - 1; j >= 1; j--) {
            if (seeMatrix[curMedusaPos.row][j] == 0 || warriorPosMap.get(new Pos(curMedusaPos.row, j)) != null) {
                seeMatrix[curMedusaPos.row][j - 1] = 0;
            }
        }

        for (int j = curMedusaPos.col - 1; j >= 1; j--) {
            int upRow = Math.max(0, curMedusaPos.row - (curMedusaPos.col - j));

            for (int i = upRow; i < curMedusaPos.row; i++) {
                if (seeMatrix[i][j] == 0 || warriorPosMap.get(new Pos(i, j)) != null) {
                    seeMatrix[i][j - 1] = 0;
                    if (i >= 1) {
                        seeMatrix[i - 1][j - 1] = 0;
                    }
                }
            }
        }

        for (int j = curMedusaPos.col - 1; j >= 1; j--) {
            int downRow = Math.min(N - 1, curMedusaPos.row + (curMedusaPos.col - j));

            for (int i = curMedusaPos.row + 1; i <= downRow; i++) {
                if (seeMatrix[i][j] == 0 || warriorPosMap.get(new Pos(i, j)) != null) {
                    seeMatrix[i][j - 1] = 0;
                    if (i < N - 1) {
                        seeMatrix[i + 1][j - 1] = 0;
                    }
                }
            }
        }

        return seeMatrix;
    }

    public static int[][] seeRight(Pos curMedusaPos) {
        int[][] seeMatrix = new int[N][N];
        for (int j = curMedusaPos.col + 1; j < N; j++) {
            int upRow = Math.max(0, curMedusaPos.row - (j - curMedusaPos.col));
            int downRow = Math.min(N - 1, curMedusaPos.row + (j - curMedusaPos.col));

            for (int i = upRow; i <= downRow; i++) {
                seeMatrix[i][j] = 1;
            }
        }

        for (int j = curMedusaPos.col + 1; j < N - 1; j++) {
            if (seeMatrix[curMedusaPos.row][j] == 0 || warriorPosMap.get(new Pos(curMedusaPos.row, j)) != null) {
                seeMatrix[curMedusaPos.row][j + 1] = 0;
            }
        }

        for (int j = curMedusaPos.col + 1; j < N - 1; j++) {
            int upRow = Math.max(0, curMedusaPos.row - (j - curMedusaPos.col));

            for (int i = upRow; i < curMedusaPos.row; i++) {
                if (seeMatrix[i][j] == 0 || warriorPosMap.get(new Pos(i, j)) != null) {
                    seeMatrix[i][j + 1] = 0;
                    if (i >= 1) {
                        seeMatrix[i - 1][j + 1] = 0;
                    }
                }
            }
        }

        for (int j = curMedusaPos.col + 1; j < N - 1; j++) {
            int downRow = Math.min(N - 1, curMedusaPos.row + (j - curMedusaPos.col));

            for (int i = curMedusaPos.row + 1; i <= downRow; i++) {
                if (seeMatrix[i][j] == 0 || warriorPosMap.get(new Pos(i, j)) != null) {
                    seeMatrix[i][j + 1] = 0;
                    if (i < N - 1) {
                        seeMatrix[i + 1][j + 1] = 0;
                    }
                }
            }
        }

        return seeMatrix;
    }

    public static int[][] seeUp(Pos curMedusaPos) {
        int[][] seeMatrix = new int[N][N];
        for (int i = curMedusaPos.row - 1; i >= 0; i--) {
            int leftCol = Math.max(0, curMedusaPos.col - (curMedusaPos.row - i));
            int rightCol = Math.min(N - 1, curMedusaPos.col + (curMedusaPos.row - i));

            for (int j = leftCol; j <= rightCol; j++) {
                seeMatrix[i][j] = 1;
            }
        }

        for (int i = curMedusaPos.row - 1; i >= 1; i--) {
            if (seeMatrix[i][curMedusaPos.col] == 0 || warriorPosMap.get(new Pos(i, curMedusaPos.col)) != null) {
                seeMatrix[i - 1][curMedusaPos.col] = 0;
            }
        }

        for (int i = curMedusaPos.row - 1; i >= 1; i--) {
            int leftCol = Math.max(0, curMedusaPos.col - (curMedusaPos.row - i));

            for (int j = leftCol; j < curMedusaPos.col; j++) {
                if (seeMatrix[i][j] == 0 || warriorPosMap.get(new Pos(i, j)) != null) {
                    seeMatrix[i - 1][j] = 0;
                    if (j >= 1) {
                        seeMatrix[i - 1][j - 1] = 0;
                    }
                }
            }
        }

        for (int i = curMedusaPos.row - 1; i >= 1; i--) {
            int rightCol = Math.min(N - 1, curMedusaPos.col + (curMedusaPos.row - i));

            for (int j = curMedusaPos.col + 1; j <= rightCol; j++) {
                if (seeMatrix[i][j] == 0 || warriorPosMap.get(new Pos(i, j)) != null) {
                    seeMatrix[i - 1][j] = 0;
                    if (j < N - 1) {
                        seeMatrix[i - 1][j + 1] = 0;
                    }
                }
            }
        }

        return seeMatrix;
    }

    public static int[][] seeDown(Pos curMedusaPos) {
        int[][] seeMatrix = new int[N][N];
        for (int i = curMedusaPos.row + 1; i < N; i++) {
            int leftCol = Math.max(0, curMedusaPos.col - (i - curMedusaPos.row));
            int rightCol = Math.min(N - 1, curMedusaPos.col + (i - curMedusaPos.row));

            for (int j = leftCol; j <= rightCol; j++) {
                seeMatrix[i][j] = 1;
            }
        }


        for (int i = curMedusaPos.row + 1; i < N - 1; i++) {
            if (seeMatrix[i][curMedusaPos.col] == 0 || warriorPosMap.get(new Pos(i, curMedusaPos.col)) != null) {
                seeMatrix[i + 1][curMedusaPos.col] = 0;
            }
        }

        for (int i = curMedusaPos.row + 1; i < N - 1; i++) {
            int leftCol = Math.max(0, curMedusaPos.col - (i - curMedusaPos.row));

            for (int j = leftCol; j < curMedusaPos.col; j++) {
                if (seeMatrix[i][j] == 0 || warriorPosMap.get(new Pos(i, j)) != null) {
                    seeMatrix[i + 1][j] = 0;
                    if (j >= 1) {
                        seeMatrix[i + 1][j - 1] = 0;
                    }
                }
            }
        }

        for (int i = curMedusaPos.row + 1; i < N - 1; i++) {
            int rightCol = Math.min(N - 1, curMedusaPos.col + (i - curMedusaPos.row));
            for (int j = curMedusaPos.col + 1; j <= rightCol; j++) {
                if (seeMatrix[i][j] == 0 || warriorPosMap.get(new Pos(i, j)) != null) {
                    seeMatrix[i + 1][j] = 0;
                    if (j < N - 1) {
                        seeMatrix[i + 1][j + 1] = 0;
                    }
                }
            }
        }

        return seeMatrix;
    }

    public static List<Pos> getFastestPath() {
        Deque<Node> queue = new ArrayDeque<>();
        int[][] visited = new int[N][N];
        queue.add(new Node(medusaHomePos, new ArrayList<>()));

        while (!queue.isEmpty()) {
            Node node = queue.pollFirst();
            if (visited[node.pos.row][node.pos.col] == 1) { continue; }
            visited[node.pos.row][node.pos.col] = 1;

            for (Pos direction : medusaDirections) {
                Pos movedPos = node.pos.addPos(direction);
                if (!movedPos.isValidIndex() || roadMatrix[movedPos.row][movedPos.col] == 1) { continue; }
                if (visited[movedPos.row][movedPos.col] == 1) { continue; }

                List<Pos> copiedPath = new ArrayList<>(node.path);
                copiedPath.add(movedPos);

                if (movedPos.equals(parkPos)) {
                    return copiedPath;
                }
                queue.add(new Node(movedPos, copiedPath));
            }
        }

        return new ArrayList<>();
    }

    public static Map<Pos, Integer> getWarriorPosMap() {
        Map<Pos, Integer> warriorPosMap = new HashMap<>();
        for (int i = 0; i < warriorList.size(); i++) {
            warriorPosMap.put(warriorList.get(i).pos, 1);
        }

        return warriorPosMap;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());


        st = new StringTokenizer(br.readLine());
        int medusaHomeRow = Integer.parseInt(st.nextToken());
        int medusaHomeCol = Integer.parseInt(st.nextToken());
        medusaHomePos = new Pos(medusaHomeRow, medusaHomeCol);

        int parkRow = Integer.parseInt(st.nextToken());
        int parkCol = Integer.parseInt(st.nextToken());
        parkPos = new Pos(parkRow, parkCol);

        warriorList = new ArrayList<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            int warriorRow = Integer.parseInt(st.nextToken());
            int warriorCol = Integer.parseInt(st.nextToken());

            warriorList.add(new Warrior(i, new Pos(warriorRow, warriorCol)));
        }

        roadMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                roadMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }
}