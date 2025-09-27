package problem;

import java.util.*;
import java.io.*;

public class 미생물연구 {
    static int N, Q;
    static int[][] mainMatrix;
    static Map<Integer, Group> groupMap;
    static Group[] groupList;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};

    public static class XYPos {
        int x;
        int y;

        public XYPos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Pos convertToPos() {
            return new Pos(N - this.y - 1, this.x);
        }
    }


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

        public Pos minusPos(Pos direction) {
            return new Pos(this.row - direction.row, this.col - direction.col);
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
                return Integer.compare(-this.row, -anotherPos.row);
            }

            return Integer.compare(this.col, anotherPos.col);
        }
    }

    public static class Group implements Comparable<Group> {
        int pk;
        Pos downLeftPos;
        List<Pos> posList;

        public Group(int pk, Pos downLeftPos, List<Pos> posList) {
            this.pk = pk;
            this.downLeftPos = downLeftPos;
            this.posList = posList;
        }

        public Group(int pk, Pos downLeftPos, Pos topRightPos) {
            this.pk = pk;
            this.downLeftPos = downLeftPos;
            this.posList = new ArrayList<>();

            for (int i = downLeftPos.row; i >= topRightPos.row; i--) {
                for (int j = downLeftPos.col; j <= topRightPos.col; j++) {
                    posList.add(new Pos(i, j));
                }
            }
        }

        public Group(int pk) {
            this.pk = pk;
            this.posList = new ArrayList<>();
        }

        @Override
        public int compareTo(Group anotherGroup) {
            if (this.posList.size() != anotherGroup.posList.size()) {
                return Integer.compare(-this.posList.size(), -anotherGroup.posList.size());
            }

            return Integer.compare(this.pk, anotherGroup.pk);
        }

        public void applyToMatrix(int[][] matrix) {
            for (Pos pos : this.posList) {
                matrix[pos.row][pos.col] = this.pk;
            }
        }


        public boolean canFit(Pos movedDownLeftPos, int[][] matrix) {
            Pos direction = movedDownLeftPos.minusPos(this.downLeftPos);

            for (Pos pos : this.posList) {
                Pos movedPos = pos.addPos(direction);
                if (!movedPos.isValidIndex()) { return false; }
                if (matrix[movedPos.row][movedPos.col] != 0) { return false; }
            }

            return true;
        }

        public void move(Pos movedDownLeftPos) {
            Pos direction = movedDownLeftPos.minusPos(this.downLeftPos);

            List<Pos> updatedPosList = new ArrayList<>();
            for (Pos pos : this.posList) {
                Pos movedPos = pos.addPos(direction);
                updatedPosList.add(movedPos);
            }

            this.downLeftPos = movedDownLeftPos;
            this.posList = updatedPosList;
        }

        public void addPos(Pos pos) {
            this.posList.add(pos);
        }

        public void setDownLeftPos() {
            this.downLeftPos = this.posList.get(0);

            for (int i = 1; i < this.posList.size(); i++) {
                Pos pos = posList.get(i);
                if (pos.compareTo(this.downLeftPos) < 0) {
                    this.downLeftPos = pos;
                }
            }
        }
    }

    public static class Node {
        int pk1;
        int pk2;

        public Node(int pk1, int pk2) {
            this.pk1 = Math.min(pk1, pk2);
            this.pk2 = Math.max(pk1, pk2);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Node anotherNode = (Node) obj;
            if (this.pk1 == anotherNode.pk1 && this.pk2 == anotherNode.pk2) {
                return true;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.pk1, this.pk2);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        StringBuilder sb = new StringBuilder();

        for (Group group : groupList) {
            group.applyToMatrix(mainMatrix);
            updateGroupMap();

            checkGroupsDivided();
            updateGroupMap();

            int[][] updatedMainMatrix = new int[N][N];

            List<Group> curGroupList = new ArrayList<>();
            for (int pk : groupMap.keySet()) {
                curGroupList.add(groupMap.get(pk));
            }
            Collections.sort(curGroupList);

            for (Group curGroup : curGroupList) {
                Pos updatedDownLeftPos = getBestDownLeftPos(curGroup, updatedMainMatrix);
                if (updatedDownLeftPos == null) { continue; }

                curGroup.move(updatedDownLeftPos);
                curGroup.applyToMatrix(updatedMainMatrix);
            }


            mainMatrix = updatedMainMatrix;
            sb.append(getScore() + "\n");
        }


        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static Pos getBestDownLeftPos(Group group, int[][] matrix) {
        for (int j = 0; j < N; j++) {
            for (int i = N - 1; i >= 0; i--) {
                Pos downLeftPos = new Pos(i, j);

                if (group.canFit(downLeftPos, matrix)) { return downLeftPos; }
            }
        }

        return null;
    }

    public static void checkGroupsDivided() {
        for (int pk : groupMap.keySet()) {
            if (checkGroupDivided(pk)) {
                removeGroupFromMainMatrix(pk);
            }
        }
    }

    public static boolean checkGroupDivided(int pk) {
        Group group = groupMap.get(pk);
        Pos pos = group.downLeftPos;
        int groupSize = group.posList.size();

        int[][] visited = new int[N][N];
        Deque<Pos> queue = new ArrayDeque<>();
        queue.add(pos);

        int size = 0;
        while (!queue.isEmpty()) {
            Pos curPos = queue.pollFirst();

            if (visited[curPos.row][curPos.col] == 1) { continue; }
            visited[curPos.row][curPos.col] = 1;

            size += 1;

            for (Pos direction : directions) {
                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] != pk) { continue; }
                if (visited[movedPos.row][movedPos.col] == 1) { continue; }

                queue.addLast(movedPos);
            }
        }

        if (size == groupSize) {
            return false;
        }

        return true;
    }

    public static void removeGroupFromMainMatrix(int pk) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] == pk) {
                    mainMatrix[i][j] = 0;
                }
            }
        }
    }

    public static void updateGroupMap() {
        groupMap = new HashMap<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] == 0) { continue; }

                int pk = mainMatrix[i][j];
                if (groupMap.get(pk) == null) {
                    groupMap.put(pk, new Group(pk));
                }

                groupMap.get(pk).addPos(new Pos(i, j));
            }
        }


        for (int pk : groupMap.keySet()) {
            groupMap.get(pk).setDownLeftPos();
        }
    }

    public static int getScore() {
        Map<Node, Integer> nodeMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (mainMatrix[i][j] == 0) { continue; }
                Pos pos = new Pos(i, j);

                for (Pos direction : directions) {
                    Pos movedPos = pos.addPos(direction);
                    if (!movedPos.isValidIndex()) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] == 0) { continue; }
                    if (mainMatrix[i][j] == mainMatrix[movedPos.row][movedPos.col]) { continue; }

                    nodeMap.put(new Node(mainMatrix[i][j], mainMatrix[movedPos.row][movedPos.col]), 1);
                }
            }
        }


        int score = 0;
        for (Node node : nodeMap.keySet()) {
            score += (groupMap.get(node.pk1).posList.size() * groupMap.get(node.pk2).posList.size());
        }

        return score;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        mainMatrix = new int[N][N];

        groupMap = new HashMap<>();
        groupList = new Group[Q];


        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());

            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());

            int x2 = Integer.parseInt(st.nextToken()) - 1;
            int y2 = Integer.parseInt(st.nextToken()) - 1;

            Pos downLeftPos = new XYPos(x1, y1).convertToPos();
            Pos topRightPos = new XYPos(x2, y2).convertToPos();

            Group group = new Group(i + 1, downLeftPos, topRightPos);
            groupList[i] = group;
        }

    }
}