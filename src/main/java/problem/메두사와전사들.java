package problem;

import java.util.*;
import java.io.*;


public class 메두사와전사들 {
    static int N, M;
    static int[][] roadMatrix, warriorMatrix;
    static Pos medusaStartPos;
    static Pos parkPos;
    static List<Pos> warriorPosList;

    static Pos[] medusaMoveDirections = {new Pos(-1, 0), new Pos(1, 0),
            new Pos(0, -1), new Pos(0, 1)};
    static Pos[] warriorFirstMoveDirections = {new Pos(-1, 0), new Pos(1, 0),
            new Pos(0, -1), new Pos(0, 1)};
    static Pos[] warriorSecondMoveDirections = {new Pos(0, -1), new Pos(0, 1),
            new Pos(-1, 0), new Pos(1, 0)};


    public static class Pos {
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
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
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

    public static class SeeNode {
        int[][] matrix;
        int numWarriorRocked;

        public SeeNode(int[][] matrix, int numWarriorRocked) {
            this.matrix = matrix;
            this.numWarriorRocked = numWarriorRocked;
        }
    }

    public static boolean isRoad(Pos pos) {
        if (roadMatrix[pos.row][pos.col] == 1) {
            return false;
        }
        return true;
    }

    public static class BfsNode {
        Pos curPos;
        int depth;
        List<Pos> pathList;

        public BfsNode(Pos pos, int depth, List<Pos> pathList) {
            this.curPos = pos;
            this.depth = depth;
            this.pathList = pathList;
        }
    }
    public static void main(String args[]) throws Exception {
        init();
        BfsNode bfsNode = getFastestPath();
        if (bfsNode.pathList.size() == 0) {
            System.out.println(-1);
            return;
        }

        for (int i = 0; i < bfsNode.pathList.size() - 1; i++) {
            int totalDistanceWarriorMoved = 0;
            int numRockedWarrior;
            int numWarriorAttackedMedusa = 0;

            Pos medusaPos = bfsNode.pathList.get(i);
            SeeNode bestSeeNode = getBestSeeNode(medusaPos, warriorMatrix);

            numRockedWarrior = bestSeeNode.numWarriorRocked;
            int[][] medusaSightMatrix = bestSeeNode.matrix;

            List<Pos> updatedWarriorPosList = new ArrayList<>();
            int[][] updatedWarriorMatrix = new int[N][N];
            for (int j = 0; j < warriorPosList.size(); j++) {
                Pos warriorPos = warriorPosList.get(j);
                if (warriorPos.equals(medusaPos)) {
                    continue;
                }

                if (medusaSightMatrix[warriorPos.row][warriorPos.col] == 1) {
                    updatedWarriorPosList.add(warriorPos);
                    updatedWarriorMatrix[warriorPos.row][warriorPos.col] += 1;
                    continue;
                }

                Pos firstMovedPos = moveWarrior(medusaPos, warriorPos, medusaSightMatrix, warriorFirstMoveDirections);
                if (!firstMovedPos.equals(warriorPos)) { totalDistanceWarriorMoved += 1; }
                if (firstMovedPos.equals(medusaPos)) {
                    numWarriorAttackedMedusa += 1;
                    continue;
                }

                Pos secondMovedPos = moveWarrior(medusaPos, firstMovedPos, medusaSightMatrix, warriorSecondMoveDirections);
                if (!secondMovedPos.equals(firstMovedPos)) { totalDistanceWarriorMoved += 1; }
                if (secondMovedPos.equals(medusaPos)) {
                    numWarriorAttackedMedusa += 1;
                    continue;
                }

                updatedWarriorPosList.add(secondMovedPos);
                updatedWarriorMatrix[secondMovedPos.row][secondMovedPos.col] += 1;
            }

            warriorPosList = updatedWarriorPosList;
            warriorMatrix = updatedWarriorMatrix;

            System.out.println(totalDistanceWarriorMoved + " " + numRockedWarrior + " " + numWarriorAttackedMedusa);
        }
        System.out.println(0);
    }

    public static Pos moveWarrior(Pos medusaPos, Pos warriorPos, int[][] medusaSightMatrix, Pos[] directionOrder) {
        int minDistance = getDistance(medusaPos, warriorPos);
        Pos minDistancePos = new Pos(warriorPos.row,  warriorPos.col);

        for (Pos direction : directionOrder) {
            Pos movedPos = warriorPos.addPos(direction);
            if (!movedPos.isValidIndex() || medusaSightMatrix[movedPos.row][movedPos.col] == 1) { continue; }

            int movedDistance = getDistance(medusaPos, movedPos);
            if (movedDistance < minDistance) {
                minDistance = movedDistance;
                minDistancePos = movedPos;

                return minDistancePos;
            }
        }

        return minDistancePos;
    }



    public static int getDistance(Pos pos1, Pos pos2) {
        return Math.abs(pos1.row - pos2.row) + Math.abs(pos1.col - pos2.col);
    }

    public static SeeNode getBestSeeNode(Pos medusaPos, int[][] warriorMatrix) {
        SeeNode bestSeeNode = new SeeNode(new int[N][N], 0);

        SeeNode upSeeNode = medusaSeeUp(medusaPos, warriorMatrix);
        if (upSeeNode.numWarriorRocked >= bestSeeNode.numWarriorRocked) {
            bestSeeNode = upSeeNode;
        }

        SeeNode downSeeNode = medusaSeeDown(medusaPos, warriorMatrix);
        if (downSeeNode.numWarriorRocked > bestSeeNode.numWarriorRocked) {
            bestSeeNode = downSeeNode;
        }

        SeeNode leftSeeNode = medusaSeeLeft(medusaPos, warriorMatrix);
        if (leftSeeNode.numWarriorRocked > bestSeeNode.numWarriorRocked) {
            bestSeeNode = leftSeeNode;
        }

        SeeNode rightSeeNode = medusaSeeRight(medusaPos, warriorMatrix);
        if (rightSeeNode.numWarriorRocked > bestSeeNode.numWarriorRocked) {
            bestSeeNode = rightSeeNode;
        }

        return bestSeeNode;
    }

    public static SeeNode medusaSeeDown(Pos medusaPos, int[][] warriorMatrix) {
        List<Pos> rockedSoldierList = new ArrayList<>();
        int[][] matrix = new int[N][N];
        Pos direction = new Pos(1, 0);

        for (int i = medusaPos.row + 1; i < N; i++) {
            int left = Math.max(0, medusaPos.col - (i - medusaPos.row));
            int right = Math.min(N - 1, medusaPos.col + (i - medusaPos.row));

            for (int j = left; j<= right; j++) {
                matrix[i][j] = 1;
            }
        }

        //leftDiagDirection
        for (int i = medusaPos.row + 1; i < N - 1; i++) {
            int left = Math.max(0, medusaPos.col - (i - medusaPos.row));
            int right = medusaPos.col - 1;

            for (int j = left; j <= right; j++) {
                if (warriorMatrix[i][j] >= 1 || matrix[i][j] == 0) {
                    matrix[i + 1][j] = 0;
                    if (j >= 1) {
                        matrix[i + 1][j - 1] = 0;
                    }
                }
            }
        }
        //CenterDirection
        for (int i = medusaPos.row + 1; i < N - 1; i++) {
            if (warriorMatrix[i][medusaPos.col] >= 1 || matrix[i][medusaPos.col] == 0) {
                matrix[i + 1][medusaPos.col] = 0;
            }
        }


        //RightDiagDirection
        for (int i = medusaPos.row + 1; i < N - 1; i++) {
            int left = medusaPos.col + 1;
            int right = Math.min(N - 1, medusaPos.col + (i - medusaPos.row));

            for (int  j = left; j <= right; j++) {
                if (warriorMatrix[i][j] >= 1 || matrix[i][j] == 0) {
                    matrix[i + 1][j] = 0;
                    if (j < N - 1) {
                        matrix[i + 1][j + 1] = 0;
                    }
                }
            }
        }

        int numRockedSoldier = 0;
        for (int i = medusaPos.row + 1; i < N; i++) {
            int left = Math.max(0, medusaPos.col - (i - medusaPos.row));
            int right = Math.min(N - 1, medusaPos.col + (i - medusaPos.row));

            for (int j = left; j<= right; j++) {
                if (matrix[i][j] == 1 && warriorMatrix[i][j] >= 1) {
                    numRockedSoldier += warriorMatrix[i][j];
                }
            }
        }


        return new SeeNode(matrix, numRockedSoldier);
    }

    public static SeeNode medusaSeeUp(Pos medusaPos, int[][] warriorMatrix) {
        List<Pos> rockedSoldierList = new ArrayList<>();
        int[][] matrix = new int[N][N];
        Pos direction = new Pos(-1, 0);

        for (int i = medusaPos.row - 1; i >= 0; i--) {
            int left = Math.max(0, medusaPos.col - (medusaPos.row - i));
            int right = Math.min(N - 1, medusaPos.col + (medusaPos.row - i));

            for (int j = left; j<= right; j++) {
                matrix[i][j] = 1;
            }
        }

        //leftDiagDirection
        for (int i = medusaPos.row - 1; i >= 1; i--) {
            int left = Math.max(0, medusaPos.col - (medusaPos.row - i));
            int right = medusaPos.col - 1;

            for (int j = left; j <= right; j++) {
                if (warriorMatrix[i][j] >= 1 || matrix[i][j] == 0) {
                    matrix[i - 1][j] = 0;
                    if (j >= 1) {
                        matrix[i - 1][j - 1] = 0;
                    }
                }
            }
        }
        //CenterDirection
        for (int i = medusaPos.row - 1; i >= 1; i--) {
            if (warriorMatrix[i][medusaPos.col] >= 1 || matrix[i][medusaPos.col] == 0) {
                matrix[i - 1][medusaPos.col] = 0;
            }
        }


        //RightDiagDirection
        for (int i = medusaPos.row - 1; i >= 1; i--) {
            int left = medusaPos.col + 1;
            int right = Math.min(N - 1, medusaPos.col + (medusaPos.row - i));

            for (int  j = left; j <= right; j++) {
                if (warriorMatrix[i][j] >= 1 || matrix[i][j] == 0) {
                    matrix[i - 1][j] = 0;
                    if (j < N - 1) {
                        matrix[i - 1][j + 1] = 0;
                    }
                }
            }
        }

        int numRockedSoldier = 0;
        for (int i = medusaPos.row - 1; i >= 0; i--) {
            int left = Math.max(0, medusaPos.col - (medusaPos.row - i));
            int right = Math.min(N - 1, medusaPos.col + (medusaPos.row - i));

            for (int j = left; j<= right; j++) {
                if (matrix[i][j] == 1 && warriorMatrix[i][j] >= 1) {
                    numRockedSoldier += warriorMatrix[i][j];
                }
            }
        }


        return new SeeNode(matrix, numRockedSoldier);
    }
    public static SeeNode medusaSeeRight(Pos medusaPos, int[][] warriorMatrix) {
        int[][] matrix = new int[N][N];

        for (int j = medusaPos.col + 1; j <= N - 1; j++) {
            int up = Math.max(0, medusaPos.row - (j - medusaPos.col));
            int down = Math.min(N - 1, medusaPos.row + (j - medusaPos.col));

            for (int i = up; i<= down; i++) {
                matrix[i][j] = 1;
            }
        }


        //UpDirection
        for (int j = medusaPos.col + 1; j < N - 1; j++) {
            int up = Math.max(0, medusaPos.row - (j - medusaPos.col));
            int down = medusaPos.row - 1;

            for (int i = up; i <= down; i++) {
                if (warriorMatrix[i][j] >= 1 || matrix[i][j] == 0) {
                    matrix[i][j + 1] = 0;
                    if (i >= 1) {
                        matrix[i - 1][j + 1] = 0;
                    }
                }
            }
        }

        //RightDirection
        for (int j = medusaPos.col + 1; j < N - 1; j++) {
            if (warriorMatrix[medusaPos.row][j] >= 1 || matrix[medusaPos.row][j] == 0) {
                matrix[medusaPos.row][j + 1] = 0;
            }
        }

        //DownDirection
        for (int j = medusaPos.col + 1; j < N - 1; j++) {
            int up = medusaPos.row + 1;
            int down = Math.min(N - 1, medusaPos.row + (j - medusaPos.col));

            for (int i = up; i <= down; i++) {
                if (warriorMatrix[i][j] >= 1 || matrix[i][j] == 0) {
                    matrix[i][j + 1] = 0;
                    if (i < N - 1) {
                        matrix[i + 1][j + 1] = 0;
                    }
                }
            }
        }

        int numRockedWarrior = 0;
        for (int j = medusaPos.col + 1; j <= N - 1; j++) {
            int up = Math.max(0, medusaPos.row - (j - medusaPos.col));
            int down = Math.min(N - 1, medusaPos.row + (j - medusaPos.col));

            for (int i = up; i<= down; i++) {
                if (matrix[i][j] == 1 && warriorMatrix[i][j] >= 1) {
                    numRockedWarrior += warriorMatrix[i][j];
                }
            }
        }

        return new SeeNode(matrix, numRockedWarrior);
    }

    public static SeeNode medusaSeeLeft(Pos medusaPos, int[][] warriorMatrix) {
        int[][] matrix = new int[N][N];

        for (int j = medusaPos.col - 1; j >= 0; j--) {
            int up = Math.max(0, medusaPos.row - (medusaPos.col - j));
            int down = Math.min(N - 1, medusaPos.row + (medusaPos.col - j));

            for (int i = up; i<= down; i++) {
                matrix[i][j] = 1;
            }
        }


        //UpDirection
        for (int j = medusaPos.col - 1; j >= 1; j--) {
            int up = Math.max(0, medusaPos.row - (medusaPos.col - j));
            int down = medusaPos.row - 1;

            for (int i = up; i <= down; i++) {
                if (warriorMatrix[i][j] >= 1 || matrix[i][j] == 0) {
                    matrix[i][j - 1] = 0;
                    if (i >= 1) {
                        matrix[i - 1][j - 1] = 0;
                    }
                }
            }
        }

        //RightDirection
        for (int j = medusaPos.col - 1; j >= 1; j--) {
            if (warriorMatrix[medusaPos.row][j] >= 1 || matrix[medusaPos.row][j] == 0) {
                matrix[medusaPos.row][j - 1] = 0;
            }
        }

        //DownDirection
        for (int j = medusaPos.col - 1; j >= 1; j--) {
            int up = medusaPos.row + 1;
            int down = Math.min(N - 1, medusaPos.row + (medusaPos.col - j));

            for (int i = up; i <= down; i++) {
                if (warriorMatrix[i][j] >= 1 || matrix[i][j] == 0) {
                    matrix[i][j - 1] = 0;
                    if (i < N - 1) {
                        matrix[i + 1][j - 1] = 0;
                    }
                }
            }
        }

        int numRockedWarrior = 0;
        for (int j = medusaPos.col - 1; j >= 0; j--) {
            int up = Math.max(0, medusaPos.row - (medusaPos.col - j));
            int down = Math.min(N - 1, medusaPos.row + (medusaPos.col - j));

            for (int i = up; i<= down; i++) {
                if (matrix[i][j] == 1 && warriorMatrix[i][j] >= 1) {
                    numRockedWarrior += warriorMatrix[i][j];
                }
            }
        }

        return new SeeNode(matrix, numRockedWarrior);
    }

    public static Pos[] getDiagDirections(Pos direction) {
        Pos[] diagDiretions = new Pos[2];
        if (direction.row == 0) {
            diagDiretions[0] = new Pos(-1, direction.col);
            diagDiretions[1] = new Pos(1, direction.col);
            return diagDiretions;
        }

        diagDiretions[0] = new Pos(direction.row, -1);
        diagDiretions[1] = new Pos(direction.row, 1);
        return diagDiretions;
    }
    public static BfsNode getFastestPath() {
        int[][] visited = new int[N][N];
        Deque<BfsNode> queue = new ArrayDeque<>();
        queue.add(new BfsNode(medusaStartPos, 0, new ArrayList<>()));

        while (!queue.isEmpty()) {
            BfsNode bfsNode = queue.poll();
            if (visited[bfsNode.curPos.row][bfsNode.curPos.col] == 1) { continue; }
            visited[bfsNode.curPos.row][bfsNode.curPos.col] = 1;

            if (bfsNode.curPos.equals(parkPos)) { return bfsNode; }

            for (Pos direction : medusaMoveDirections) {
                Pos movedPos = bfsNode.curPos.addPos(direction);
                if (!movedPos.isValidIndex() || !isRoad(movedPos)) { continue; }
                if (bfsNode.pathList.contains(movedPos)) { continue; }

                List<Pos> copiedPathList = new ArrayList<>(bfsNode.pathList);
                copiedPathList.add(movedPos);
                queue.add(new BfsNode(movedPos, bfsNode.depth + 1, copiedPathList));
            }
        }

        return new BfsNode(new Pos(0, 0), 0, new ArrayList<>());
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        roadMatrix = new int[N][N];
        warriorMatrix = new int[N][N];

        st = new StringTokenizer(br.readLine());
        int medusaRow = Integer.parseInt(st.nextToken());
        int medusaCol = Integer.parseInt(st.nextToken());
        medusaStartPos = new Pos(medusaRow, medusaCol);

        int parkRow = Integer.parseInt(st.nextToken());
        int parkCol = Integer.parseInt(st.nextToken());
        parkPos = new Pos(parkRow, parkCol);

        warriorPosList = new ArrayList<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            int warriorRow = Integer.parseInt(st.nextToken());
            int warriorCol = Integer.parseInt(st.nextToken());

            warriorMatrix[warriorRow][warriorCol] += 1;
            warriorPosList.add(new Pos(warriorRow, warriorCol));
        }


        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                roadMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

    }
}