package problem;

import java.util.*;
import java.io.*;


public class 미생물연구 {
    static int N, Q;
    static RCPos[] rcDirections = {new RCPos(-1, 0), new RCPos(0, 1),
            new RCPos(1, 0), new RCPos(0, - 1)};
    static XYPos[] xyDirections = {new XYPos(-1, 0), new XYPos(0, 1),
            new XYPos(1, 0), new XYPos(0, - 1)};
    static int[][] mainMatrix;
    static XYPos[][] commands;
    static Map<Integer, CellGroup> cellGroupMap;

    public static class RCPos implements Comparable<RCPos> {
        int row;
        int col;

        public RCPos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public RCPos addRCPos(RCPos direction) {
            return new RCPos(this.row + direction.row, this.col + direction.col);
        }

        public boolean isValidIndex() {
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
                return false;
            }

            return true;
        }

        public XYPos convertToXYPos() {
            return new XYPos(N - this.col - 1, this.row);
        }

        @Override
        public int compareTo(RCPos anotherRCPos) {
            if (this.row == anotherRCPos.row) {
                return Integer.compare(this.col, anotherRCPos.col);
            }

            return Integer.compare(-this.row, -anotherRCPos.row);
        }
    }

    public static class XYPos {
        int x;
        int y;

        public XYPos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public XYPos addXYPos(XYPos direction) {
            return new XYPos(this.x + direction.x, this.y + direction.y);
        }

        public boolean isValidIndex() {
            if (this.x < 0 || this.x >= N || this.y < 0 || this.y >= N) {
                return false;
            }

            return true;
        }

        public RCPos convertToRCPos() {
            return new RCPos(N - this.y - 1, this.x);
        }
    }

    public static class Node {
        int num1;
        int num2;

        public Node(int num1, int num2) {
            this.num1 = Math.min(num1, num2);
            this.num2 = Math.max(num1, num2);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Node anotherNode = (Node) obj;
            if (this.num1 == anotherNode.num1 && this.num2 == anotherNode.num2) { return true; }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.num1, this.num2);
        }
    }

    public static class CellGroup implements Comparable<CellGroup> {
        int uniqueNum;
        Map<RCPos, Integer> rcPosMap;
        RCPos downLeftRCPos;

        public CellGroup(int uniqueNum, List<RCPos> rcPosList) {
            this.uniqueNum = uniqueNum;

            rcPosMap = new HashMap<>();
            for (RCPos rcPos: rcPosList) {
                rcPosMap.put(rcPos, 1);

                if (downLeftRCPos == null) {
                    downLeftRCPos = rcPos;
                }
                else if (downLeftRCPos.compareTo(rcPos) > 0){
                    downLeftRCPos = rcPos;
                }
            }
        }

        @Override
        public int compareTo(CellGroup anotherCellGroup) {
            if (this.rcPosMap.keySet().size() == anotherCellGroup.rcPosMap.keySet().size()) {
                return Integer.compare(this.uniqueNum, anotherCellGroup.uniqueNum);
            }

            return Integer.compare(-this.rcPosMap.keySet().size(),
                    -anotherCellGroup.rcPosMap.keySet().size());
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Q; i++) {
            XYPos[] command = commands[i];

            int uniqueNum = i + 1;
            XYPos downLeftXYPos = command[0];
            XYPos upRightPos = command[1];

            fillMainMatrix(uniqueNum, downLeftXYPos, upRightPos);
            setCellGroupMap();
            checkCellGroupsDivided();
            updateMainMatrix();
            setCellGroupMap();

            int score = getScore();

            sb.append(score + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int getScore() {
        int score = 0;
        Map<Node, Integer> nearMap = new HashMap<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] == 0) { continue; }
                RCPos rcPos = new RCPos(i, j);

                for (RCPos direction : rcDirections) {
                    RCPos movedPos = rcPos.addRCPos(direction);
                    if (!movedPos.isValidIndex()) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] == 0) { continue; }
                    if (mainMatrix[i][j] == mainMatrix[movedPos.row][movedPos.col]) { continue; }

                    nearMap.put(new Node(mainMatrix[i][j], mainMatrix[movedPos.row][movedPos.col]), 1);
                }
            }
        }

        for (Node node :  nearMap.keySet()) {
            score += (cellGroupMap.get(node.num1).rcPosMap.keySet().size() * cellGroupMap.get(node.num2).rcPosMap.keySet().size());
        }

        return score;
    }

    public static void updateMainMatrix() {
        int[][] updatedMainMatrix = new int[N][N];

        PriorityQueue<CellGroup> pq = new PriorityQueue<>();
        for (int i = 0; i < Q; i++) {
            pq.add(cellGroupMap.get(i + 1));
        }

        while (!pq.isEmpty()) {
            CellGroup cellGroup = pq.poll();
            moveCellGroup(cellGroup, updatedMainMatrix);
        }

        mainMatrix = updatedMainMatrix;
    }


    public static void moveCellGroup(CellGroup cellGroup, int[][] matrix) {
        List<RCPos> rcPosList = new ArrayList<>(cellGroup.rcPosMap.keySet());
        if (rcPosList.size() == 0) { return; }

        for (int col = 0; col < N; col++) {
            for (int row = N - 1; row >= 0; row--) {
                RCPos direction = new RCPos(row - cellGroup.downLeftRCPos.row, col - cellGroup.downLeftRCPos.col);
                if (!canCellGroupMove(rcPosList, direction, matrix)) { continue; }
                applyCellGroupMoveToMatrix(cellGroup.uniqueNum, rcPosList, direction, matrix);
                return;

            }
        }

        cellGroupMap.put(cellGroup.uniqueNum, new CellGroup(cellGroup.uniqueNum, new ArrayList<>()));
    }

    public static boolean canCellGroupMove(List<RCPos> rcPosList, RCPos direction, int[][] matrix) {
        for (RCPos rcPos : rcPosList) {
            RCPos movedRCPos = rcPos.addRCPos(direction);
            if (!movedRCPos.isValidIndex()) { return false; }
            if (matrix[movedRCPos.row][movedRCPos.col] != 0) { return false; }
        }

        return true;
    }

    public static void applyCellGroupMoveToMatrix(int uniqueNum, List<RCPos> rcPosList, RCPos direction, int[][] matrix) {
        for (RCPos rcPos : rcPosList) {
            RCPos movedRCPos = rcPos.addRCPos(direction);
            matrix[movedRCPos.row][movedRCPos.col] = uniqueNum;
        }
    }

    public static void checkCellGroupsDivided() {
        for (int i = 0; i < Q; i++) {
            checkCellGroupDivided(i + 1);
        }
    }

    public static void checkCellGroupDivided(int uniqueNum) {
        CellGroup cellGroup = cellGroupMap.get(uniqueNum);
        int groupSize = cellGroup.rcPosMap.keySet().size();
        if (groupSize == 0) { return; }

        RCPos startPos = new ArrayList<>(cellGroup.rcPosMap.keySet()).get(0);
        Deque<RCPos> queue = new ArrayDeque<>();
        queue.addLast(startPos);

        int[][] visited = new int[N][N];
        int numVisitPos = 0;
        while (!queue.isEmpty()) {
            RCPos rcPos = queue.pollFirst();
            if (visited[rcPos.row][rcPos.col] == 1) { continue; }
            visited[rcPos.row][rcPos.col] = 1;
            numVisitPos += 1;

            for (RCPos direction : rcDirections) {
                RCPos movedRCPos = rcPos.addRCPos(direction);
                if (!movedRCPos.isValidIndex()) { continue; }
                if (visited[movedRCPos.row][movedRCPos.col] == 1) { continue; }
                if (mainMatrix[movedRCPos.row][movedRCPos.col] != uniqueNum) { continue; }

                queue.add(movedRCPos);
            }
        }

        if (numVisitPos != groupSize) {
            cellGroupMap.put(uniqueNum, new CellGroup(uniqueNum, new ArrayList<>()));
        }
    }

    public static void fillMainMatrix(int uniqueNum, XYPos downLeftXYPos, XYPos upRightXYPos) {
        RCPos downLeftRCPos = downLeftXYPos.convertToRCPos();
        RCPos upRightRCPos = upRightXYPos.convertToRCPos();

        for (int row = upRightRCPos.row; row <= downLeftRCPos.row; row++) {
            for (int col = downLeftRCPos.col; col <= upRightRCPos.col; col++) {
                if (mainMatrix[row][col] != 0) {
                    cellGroupMap.get(mainMatrix[row][col]).rcPosMap.remove(new RCPos(row, col));
                }

                cellGroupMap.get(uniqueNum).rcPosMap.put(new RCPos(row,col), 1);
                mainMatrix[row][col] = uniqueNum;
            }
        }

    }

    public static void setCellGroupMap() {
        Map<Integer, List<RCPos>> groupPosMap = new HashMap<>();
        for (int i = 0; i < Q; i++) {
            groupPosMap.put(i + 1, new ArrayList<>());
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] == 0) { continue; }
                groupPosMap.get(mainMatrix[i][j]).add(new RCPos(i, j));
            }
        }


        for (int i = 0; i < Q; i++) {
            cellGroupMap.put(i + 1, new CellGroup(i + 1, groupPosMap.get(i + 1)));
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        commands = new XYPos[Q][2];
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());

            int downLeftX = Integer.parseInt(st.nextToken());
            int downLeftY = Integer.parseInt(st.nextToken());
            XYPos downLeftXYPos = new XYPos(downLeftX, downLeftY);

            int upRightX = Integer.parseInt(st.nextToken()) - 1;
            int upRightY = Integer.parseInt(st.nextToken()) - 1;
            XYPos upRightXYPos = new XYPos(upRightX, upRightY);

            commands[i][0] = downLeftXYPos;
            commands[i][1] = upRightXYPos;
        }

        cellGroupMap = new HashMap<>();
        for (int i = 0; i < Q; i++) {
            cellGroupMap.put(i + 1, new CellGroup(i + 1, new ArrayList<>()));
        }
        mainMatrix = new int[N][N];
    }
}