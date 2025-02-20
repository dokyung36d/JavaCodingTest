package problem;

import java.util.*;
import java.io.*;


public class 왕실의기사대결 {
    static int L, N, Q;
    static int[][] floorMatrix;
    static int[][] warriorMatrix;
    static Map<Integer, Warrior> warriorMap;
    static Map<Integer, Integer> originalHPMap;
    static Command[] commandList;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    //위, 오른, 아래, 왼

    public static class Command {
        int uniqueNum;
        int directionIndex;

        public Command(int uniqueNum, int directionIndex) {
            this.uniqueNum = uniqueNum;
            this.directionIndex = directionIndex;
        }
    }

    public static class Warrior {
        int uniqueNum;
        Pos topLeftPos;
        int h;
        int w;
        int hp;

        public Warrior(int uniqueNum, Pos topLeftPos, int h, int w, int hp) {
            this.uniqueNum = uniqueNum;
            this.topLeftPos = topLeftPos;
            this.h = h;
            this.w = w;
            this.hp = hp;
        }

        public int getDamage() {
            int damage = 0;
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    damage += floorMatrix[topLeftPos.row + i][topLeftPos.col + j];
                }
            }
            this.hp -= damage;
            return damage;
        }

        public void applyToMatrix(int[][] matrix) {
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    matrix[topLeftPos.row + i][topLeftPos.col + j] = this.uniqueNum;
                }
            }
        }

        public List<Pos> getSurroundingsByDirectionIndex(int directionIndex) {
            if (directionIndex == 0) {
                return getNorthSurroundings();
            }

            else if (directionIndex == 1) {
                return getEastSurroundings();
            }

            else if (directionIndex == 2) {
                return getSouthSurroundings();
            }

            return getWestSurroundings();
        }

        public List<Pos> getNorthSurroundings() {
            List<Pos> northSurroundings = new ArrayList<>();
            Pos northDirection = directions[0];
            for (int i = 0; i < w; i++) {
                Pos northSurrounding = new Pos(this.topLeftPos.row, this.topLeftPos.col + i).addPos(northDirection);
                northSurroundings.add(northSurrounding);
            }

            return northSurroundings;
        }

        public List<Pos> getEastSurroundings() {
            List<Pos> eastSurroundings = new ArrayList<>();
            Pos eastDirection = directions[1];
            for (int i = 0; i < h; i++) {
                Pos eastSurrounding = new Pos(this.topLeftPos.row + i, this.topLeftPos.col + w);
                eastSurroundings.add(eastSurrounding);
            }

            return eastSurroundings;
        }

        public List<Pos> getSouthSurroundings() {
            List<Pos> southSurroundings = new ArrayList<>();
            Pos southDirection  = directions[2];
            for (int i = 0; i < w; i++) {
                Pos southSurrounding = new Pos(this.topLeftPos.row + h, this.topLeftPos.col + i);
                southSurroundings.add(southSurrounding);
            }

            return southSurroundings;
        }

        public List<Pos> getWestSurroundings() {
            List<Pos> westSurroundings = new ArrayList<>();
            Pos westDirection = directions[3];
            for (int i = 0; i < h; i++) {
                Pos westSurrounding = new Pos(this.topLeftPos.row + i, this.topLeftPos.col - 1);
                westSurroundings.add(westSurrounding);
            }

            return westSurroundings;
        }
    }

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
            if (this.row < 0 || this.row >= L || this.col < 0 || this.col >= L) {
                return false;
            }

            return true;
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        for (int i = 0; i < Q; i++) {
            Command command = commandList[i];

            int warriorUniqueNum = command.uniqueNum;
            int directionIndex = command.directionIndex;
            if (warriorMap.get(warriorUniqueNum).hp <= 0) { continue; }

            Deque<Pos> queue = new ArrayDeque<>();
            queue.addAll(warriorMap.get(warriorUniqueNum).getSurroundingsByDirectionIndex(directionIndex));

            int wallFlag = 0;
            Map<Integer, Integer> warriorMovedMap = new HashMap<>();
            while (!queue.isEmpty()) {
                Pos pos = queue.pollFirst();
                if (!pos.isValidIndex() || floorMatrix[pos.row][pos.col] == 2) {
                    wallFlag = 1;
                    break;
                }


                int hittedWarriorUniqueNum = warriorMatrix[pos.row][pos.col];
                if (hittedWarriorUniqueNum == 0) { continue; }
                if (warriorMovedMap.get(hittedWarriorUniqueNum) != null) { continue; }

                warriorMovedMap.put(hittedWarriorUniqueNum, 1);
                Warrior hittedWarrior = warriorMap.get(hittedWarriorUniqueNum);
                queue.addAll(hittedWarrior.getSurroundingsByDirectionIndex(directionIndex));
            }


            if (wallFlag == 1) { continue; }

            int[][] updatedWarriorMatrix = new int[L][L];
            for (int j = 1; j < N + 1; j++) {
                Warrior warrior = warriorMap.get(j);
                if (warrior.hp <= 0) { continue; }
                if (warrior.uniqueNum == warriorUniqueNum || warriorMovedMap.get(warrior.uniqueNum) != null) {
                    warrior.topLeftPos = warrior.topLeftPos.addPos(directions[directionIndex]);
                }

                if (warriorMovedMap.get(warrior.uniqueNum) != null) {
                    warrior.getDamage();
                }

                warriorMap.put(warrior.uniqueNum, warrior);

                if (warrior.hp <= 0) { continue; }
                warrior.applyToMatrix(updatedWarriorMatrix);
            }

            warriorMatrix = updatedWarriorMatrix;

        }

        int totalDamage = 0;
        for (int i = 1; i < N + 1; i++) {
            Warrior warrior = warriorMap.get(i);
            if (warrior.hp <= 0) { continue; }

            totalDamage += (originalHPMap.get(i) - warrior.hp);
        }

        System.out.println(totalDamage);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        floorMatrix = new int[L][L];
        warriorMatrix = new int[L][L];
        for (int i = 0; i < L; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0 ; j < L; j++) {
                floorMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        warriorMap = new HashMap<>();
        originalHPMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken()) - 1;
            int c = Integer.parseInt(st.nextToken()) - 1;
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            Warrior warrior = new Warrior(i + 1, new Pos(r, c), h, w, k);
            originalHPMap.put(i + 1, k);
            warrior.applyToMatrix(warriorMatrix);
            warriorMap.put(i + 1, warrior);
        }

        commandList = new Command[Q];
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int uniqueNum = Integer.parseInt(st.nextToken());
            int directionIndex = Integer.parseInt(st.nextToken());
            Command command = new Command(uniqueNum, directionIndex);
            commandList[i] = command;
        }
    }
}