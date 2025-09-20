package problem;

import java.util.*;
import java.io.*;

public class 루돌프의반란 {
    static int N, M, P, C, D;
    static Pos deerPos;
    static int[][] mainMatrix;
    static Pos[] deerDirections = {new Pos(-1, -1), new Pos(-1, 0), new Pos(-1, 1),
            new Pos(0, -1), new Pos(0, 1),
            new Pos(1, -1), new Pos(1, 0), new Pos(1, 1)};
    static Pos[] santaDirections = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0),
            new Pos(0, -1)};

    static Map<Integer, Santa> santaMap;
    static Map<Integer, Integer> santaOutedMap;
    static int[] santaDowned;

    static int[] scoreList;


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

        public Pos minusPos(Pos anotherPos) {
            return new Pos(this.row - anotherPos.row, this.col - anotherPos.col);
        }

        public Pos multiplyPos(int num) {
            return new Pos(this.row * num, this.col * num);
        }

        public Pos reversePos() {
            return new Pos(-this.row, -this.col);
        }

        public boolean isValidIndex() {
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
                return false;
            }

            return true;
        }


        public int calcDistance(Pos anotherPos) {
            return (int) (Math.pow(this.row - anotherPos.row, 2) +
                    Math.pow(this.col - anotherPos.col, 2));
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
            if (this.calcDistance(deerPos) != anotherPos.calcDistance(deerPos)) {
                return Integer.compare(this.calcDistance(deerPos),
                        anotherPos.calcDistance(deerPos));
            }

            if (this.row != anotherPos.row) {
                return Integer.compare(-this.row, -anotherPos.row);
            }

            return Integer.compare(-this.col, -anotherPos.col);
        }
    }

    public static class Santa implements Comparable<Santa> {
        int uniqueNum;
        Pos curPos;

        public Santa(int uniqueNum, Pos curPos) {
            this.uniqueNum = uniqueNum;
            this.curPos = curPos;
        }


        public int compareTo(Santa anotherSanta) {
            return Integer.compare(this.uniqueNum, anotherSanta.uniqueNum);
        }

    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {

        for (int turn = 0; turn < M; turn++) {
            List<Integer> uniqueNumList = new ArrayList<>(santaMap.keySet());
            if (uniqueNumList.size() == 0) { break; }


            Pos deerMovedPos = getDeerMovedPos();
            Pos direction = deerMovedPos.minusPos(deerPos);
            deerPos = deerMovedPos;
            if (mainMatrix[deerMovedPos.row][deerMovedPos.col] != 0) {
                attack(deerMovedPos, C, direction, 2);
            }


            for (int uniqueNum = 1; uniqueNum <= P; uniqueNum++) {
                if (santaOutedMap.get(uniqueNum) == 1) { continue; }
                if (santaDowned[uniqueNum - 1] != 0) {
                    santaDowned[uniqueNum - 1] -= 1;
                    continue;
                }

                Pos origPos = santaMap.get(uniqueNum).curPos;
                Pos movedPos = santaMove(uniqueNum);
                Pos santaDirection = movedPos.minusPos(origPos);
                if (movedPos.equals(deerPos)) {
                    attack(origPos, D, santaDirection.reversePos(), 1);
                    continue;
                }


                updateSantaInfo(uniqueNum, movedPos);
            }


            for (int uniqueNum : santaMap.keySet()) {
                scoreList[uniqueNum - 1] += 1;
            }
        }


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < P; i++) {
            sb.append(scoreList[i] + " ");
        }
        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void dfs(Pos pos, Pos direction) {
        int uniqueNum = mainMatrix[pos.row][pos.col];
        Pos movedPos = pos.addPos(direction);
        if (!movedPos.isValidIndex()) {
            removeOutedSantaInfo(uniqueNum);
            return;
        }

        if (mainMatrix[movedPos.row][movedPos.col] != 0) {
            dfs(movedPos, direction);
        }

        updateSantaInfo(uniqueNum, movedPos);
        ;	}

    public static void attack(Pos santaPos, int power, Pos direction, int numRest) {
        int attackedSantaUniqueNum = mainMatrix[santaPos.row][santaPos.col];
        santaDowned[attackedSantaUniqueNum - 1] = numRest;
        Santa attackedSanta = santaMap.get(attackedSantaUniqueNum);

        scoreList[attackedSantaUniqueNum - 1] += power;

        Pos poweredDirection = direction.multiplyPos(power);
        Pos movedPos = deerPos.addPos(poweredDirection);
        if (!movedPos.isValidIndex()) {
            removeOutedSantaInfo(attackedSantaUniqueNum);
            return;
        }


        if (mainMatrix[movedPos.row][movedPos.col] != 0 &&
                !movedPos.equals(attackedSanta.curPos)) {
            dfs(movedPos, direction);
        }

        updateSantaInfo(attackedSantaUniqueNum, movedPos);
    }

    public static Pos santaMove(int uniqueNum) {
        Pos santaPos = santaMap.get(uniqueNum).curPos;

        int minDistance = santaPos.calcDistance(deerPos);
        Pos minPos = santaPos;

        for (Pos direction : santaDirections) {
            Pos movedPos = santaPos.addPos(direction);
            if (!movedPos.isValidIndex()) { continue; }
            if (mainMatrix[movedPos.row][movedPos.col] != 0) { continue; }

            int distance = movedPos.calcDistance(deerPos);
            if (distance < minDistance) {
                minDistance = distance;
                minPos = movedPos;
            }
        }

        return minPos;
    }


    public static Pos getDeerMovedPos() {
        List<Pos> santaPosList = new ArrayList<>();
        for (int uniqueNum : santaMap.keySet()) {
            santaPosList.add(santaMap.get(uniqueNum).curPos);
        }
        Collections.sort(santaPosList);
        Pos bestDirection = null;

        Pos closestSantaPos = santaPosList.get(0);
        int minDistance = deerPos.calcDistance(closestSantaPos);

        for (Pos direction : deerDirections) {
            Pos movedPos = deerPos.addPos(direction);
            if (!movedPos.isValidIndex()) { continue; }

            int distance = movedPos.calcDistance(closestSantaPos);
            if (distance < minDistance) {
                minDistance = distance;
                bestDirection = direction;
            }
        }

        return deerPos.addPos(bestDirection);
    }

    public static void removeOutedSantaInfo(int uniqueNum) {
        Santa santa = santaMap.get(uniqueNum);

        mainMatrix[santa.curPos.row][santa.curPos.col] = 0;
        santaMap.remove(uniqueNum);
        santaOutedMap.put(uniqueNum, 1);
    }

    public static void updateSantaInfo(int uniqueNum, Pos movedPos) {
        Pos origPos = santaMap.get(uniqueNum).curPos;

        mainMatrix[origPos.row][origPos.col] = 0;
        mainMatrix[movedPos.row][movedPos.col] = uniqueNum;
        santaMap.put(uniqueNum, new Santa(uniqueNum, movedPos));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());

        mainMatrix = new int[N][N];

        st = new StringTokenizer(br.readLine());
        int deerRow = Integer.parseInt(st.nextToken()) - 1;
        int deerCol = Integer.parseInt(st.nextToken()) - 1;
        deerPos = new Pos(deerRow, deerCol);

        santaMap = new HashMap<>();
        santaOutedMap = new HashMap<>();
        santaDowned = new int[P];
        for (int i = 0; i < P; i++) {
            st = new StringTokenizer(br.readLine());

            int uniqueNum = Integer.parseInt(st.nextToken());
            int santaRow = Integer.parseInt(st.nextToken()) - 1;
            int santaCol = Integer.parseInt(st.nextToken()) - 1;

            mainMatrix[santaRow][santaCol] = uniqueNum;
            santaMap.put(uniqueNum, new Santa(uniqueNum, new Pos(santaRow, santaCol)));
            santaOutedMap.put(uniqueNum, 0);
        }

        scoreList = new int[P];
    }

}
