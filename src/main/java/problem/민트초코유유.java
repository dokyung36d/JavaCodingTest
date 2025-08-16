package problem;

import java.util.*;
import java.io.*;

public class 민트초코유유 {
    public static int N, T;
    public static int[][] colorMatrix;
    public static int[][] powerMatrix;
    public static int[][] groupMatrix;
    public static Pos[] directions = {new Pos(-1, 0), new Pos(1, 0), new Pos(0, -1), new Pos(0, 1)};
    public static Map<Integer, Group> groupMap;
    public static List<Group> groupList;
    public static Map<Pos, Integer> defeatedMap;

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
            if (powerMatrix[this.row][this.col] != powerMatrix[anotherPos.row][anotherPos.col]) {
                return Integer.compare(-powerMatrix[this.row][this.col], -powerMatrix[anotherPos.row][anotherPos.col]);
            }

            if (this.row != anotherPos.row) {
                return Integer.compare(this.row, anotherPos.row);
            }

            return Integer.compare(this.col, anotherPos.col);
        }
    }

    public static class Group implements Comparable<Group> {
        int uniqueNum;
        List<Pos> personList;
        Pos boss;

        public Group(int uniqueNum) {
            this.uniqueNum = uniqueNum;
            this.personList = new ArrayList<>();
        }

        public void addPos(Pos pos) {
            this.personList.add(pos);
        }

        public void setBoss() {
            Collections.sort(this.personList);

            boss = this.personList.get(0);
            for (int i = 0; i < this.personList.size(); i++) {
                Pos personPos = personList.get(i);
                if (personPos.equals(boss)) { continue; }

                powerMatrix[personPos.row][personPos.col] -= 1;
            }
            powerMatrix[boss.row][boss.col] += (this.personList.size() - 1);
        }

        public void attack() {
            if (defeatedMap.get(boss) != null) { return; }

            Pos direction = directions[powerMatrix[this.boss.row][this.boss.col] % 4];
            int power = powerMatrix[this.boss.row][this.boss.col] - 1;
            powerMatrix[this.boss.row][this.boss.col] = 1;
            Pos curPos = new Pos(this.boss.row, this.boss.col);
            int curColor = colorMatrix[this.boss.row][this.boss.col];

            while (true) {
                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.isValidIndex() || power == 0) { return; }
                if (curColor == colorMatrix[movedPos.row][movedPos.col]) {
                    curPos = movedPos;
                    continue;
                }

                defeatedMap.put(movedPos, 1);

                if (power > powerMatrix[movedPos.row][movedPos.col]) {
                    colorMatrix[movedPos.row][movedPos.col] = curColor;
                    power -= (powerMatrix[movedPos.row][movedPos.col] + 1);
                    powerMatrix[movedPos.row][movedPos.col] += 1;

                    curPos = movedPos;
                    if (power == 0) { return; }
                    continue;
                }


                colorMatrix[movedPos.row][movedPos.col] |= curColor;
                powerMatrix[movedPos.row][movedPos.col] += power;
                return;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Group anotherGroup = (Group) obj;
            if (this.uniqueNum == anotherGroup.uniqueNum) { return true; }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.uniqueNum);
        }

        @Override
        public int compareTo(Group anotherGroup) {
            if (getColorPriority(this.boss) != getColorPriority(anotherGroup.boss)) {
                return Integer.compare(getColorPriority(this.boss), getColorPriority(anotherGroup.boss));
            }

            if (powerMatrix[this.boss.row][this.boss.col] != powerMatrix[anotherGroup.boss.row][anotherGroup.boss.col]) {
                return Integer.compare(-powerMatrix[this.boss.row][this.boss.col], -powerMatrix[anotherGroup.boss.row][anotherGroup.boss.col]);
            }

            if (this.boss.row != anotherGroup.boss.row) {
                return Integer.compare(this.boss.row, anotherGroup.boss.row);
            }

            return Integer.compare(this.boss.col, anotherGroup.boss.col);
        }
    }


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < T; i++) {
            morning();
            setGroupMatrix();
            setGroupMap();
            setGroupBosses();
            defeatedMap = new HashMap<>();
            attackGroups();
            sb.append(getScore() + "\n");

        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void morning() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                powerMatrix[i][j] += 1;
            }
        }
    }

    public static String getScore() {
        StringBuilder sb = new StringBuilder();
        int[] colorOrder = {7, 6, 5, 3, 1, 2, 4};
        Map<Integer, Integer> scoreMap = new HashMap<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int color = colorMatrix[i][j];
                scoreMap.put(color, scoreMap.getOrDefault(color, 0) + powerMatrix[i][j]);
            }
        }

        for (int color : colorOrder) {
            sb.append(scoreMap.getOrDefault(color, 0) + " ");
        }

        return sb.toString().toString().substring(0, sb.length() - 1);
    }

    public static void attackGroups() {
        groupList = new ArrayList<>();
        for (int uniqueNum : groupMap.keySet()) {
            groupList.add(groupMap.get(uniqueNum));
        }


        Collections.sort(groupList);
        for (int i = 0; i < groupList.size(); i++) {
            Group group = groupList.get(i);
            group.attack();
        }
    }


    public static void setGroupMatrix() {
        groupMatrix = new int[N][N];
        int uniqueNum = 1;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (groupMatrix[i][j] != 0) { continue; }

                Deque<Pos> queue = new ArrayDeque<>();
                queue.add(new Pos(i, j));

                while (!queue.isEmpty()) {
                    Pos curPos = queue.pollFirst();
                    if (groupMatrix[curPos.row][curPos.col] != 0) { continue; }
                    groupMatrix[curPos.row][curPos.col] = uniqueNum;

                    for (Pos direction : directions) {
                        Pos movedPos = curPos.addPos(direction);
                        if (!movedPos.isValidIndex()) { continue; }
                        if (groupMatrix[movedPos.row][movedPos.col] != 0) { continue; }
                        if (colorMatrix[curPos.row][curPos.col] != colorMatrix[movedPos.row][movedPos.col]) { continue; }

                        queue.addLast(movedPos);
                    }
                }

                uniqueNum += 1;
            }
        }
    }

    public static void setGroupMap() {
        groupMap = new HashMap<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (groupMap.get(groupMatrix[i][j]) == null) {
                    groupMap.put(groupMatrix[i][j], new Group(groupMatrix[i][j]));
                }

                groupMap.get(groupMatrix[i][j]).addPos(new Pos(i, j));
            }
        }
    }

    public static void setGroupBosses() {
        for (int uniqueNum : groupMap.keySet()) {
            groupMap.get(uniqueNum).setBoss();
        }
    }

    public static int getColorPriority(Pos pos) {
        int color = colorMatrix[pos.row][pos.col];
        if (color == 1 || color == 2 || color == 4) {
            return 0;
        }

        else if (color == 7 ) { return 2;}
        return 1;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());


        colorMatrix = new int[N][N];
        powerMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();
            for (int j = 0; j < N; j++) {
                char color = string.charAt(j);

                if (color == 'T') {
                    colorMatrix[i][j] = 4;
                }
                else if (color == 'C') {
                    colorMatrix[i][j] = 2;
                }
                else {
                    colorMatrix[i][j] = 1;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                powerMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }


}
