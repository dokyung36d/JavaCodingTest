package problem;

import java.util.*;
import java.io.*;


public class 미친아두이노 {
    static int R, C;
    static char[][] mainMatrix;
    static int[] commandList;
    static Pos mainRobotPos;
    static Map<Pos, Integer> crazyRobotPosMap;
    static Pos[] directions = {new Pos(1, -1), new Pos(1, 0), new Pos(1, 1),
            new Pos(0, -1), new Pos(0, 0), new Pos(0, 1),
            new Pos(-1, -1), new Pos(-1, 0), new Pos(-1, 1)};

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
            if (this.row < 0 || this.row >= R || this.col < 0 || this.col >= C) {
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

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        for (int i = 0; i < commandList.length; i++) {
            Pos mainRobotMovedPos = mainRobotPos.addPos(directions[commandList[i]]);
            if (crazyRobotPosMap.get(mainRobotMovedPos) != null) {
                System.out.println("kraj " + (i + 1));
                return;
            }
            mainRobotPos = mainRobotMovedPos;

            Map<Pos, Integer> updatedCrazyRobotPosMap = new HashMap<>();
            for (Pos crazyRobotPos : crazyRobotPosMap.keySet()) {
                Pos crazyRobotMovedPos = moveCrazyRobot(crazyRobotPos);
                if (crazyRobotMovedPos.equals(mainRobotPos)) {
                    System.out.println("kraj " + (i + 1));
                    return;
                }

                updatedCrazyRobotPosMap.put(crazyRobotMovedPos, updatedCrazyRobotPosMap.getOrDefault(crazyRobotMovedPos, 0) + 1);
            }

            List<Pos> deletePosList = new ArrayList<>();
            for (Pos crazyRobotMovedPos : updatedCrazyRobotPosMap.keySet()) {
                if (updatedCrazyRobotPosMap.get(crazyRobotMovedPos) >= 2) {
                    deletePosList.add(crazyRobotMovedPos);
                }
            }

            for (Pos deletePos : deletePosList) {
                updatedCrazyRobotPosMap.remove(deletePos);
            }

            crazyRobotPosMap = updatedCrazyRobotPosMap;
        }


        char[][] answerMatrix = new char[R][C];
        for (int i = 0; i < R; i++) {
            Arrays.fill(answerMatrix[i], '.');
        }

        answerMatrix[mainRobotPos.row][mainRobotPos.col] = 'I';
        for (Pos crazyRobotPos : crazyRobotPosMap.keySet()) {
            answerMatrix[crazyRobotPos.row][crazyRobotPos.col] = 'R';
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                sb.append(answerMatrix[i][j]);
            }
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static Pos moveCrazyRobot(Pos crazyRobotPos) {
        Pos bestMovedPos = crazyRobotPos;
        int minDistance = crazyRobotPos.calcDistance(mainRobotPos);

        for (Pos direction : directions) {
            Pos movedPos = crazyRobotPos.addPos(direction);
            if (!movedPos.isValidIndex()) { continue; }

            if (movedPos.calcDistance(mainRobotPos) < minDistance) {
                bestMovedPos = movedPos;
                minDistance = movedPos.calcDistance(mainRobotPos);
            }
        }


        return bestMovedPos;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        mainMatrix = new char[R][C];
        crazyRobotPosMap = new HashMap<>();

        for (int i = 0; i < R; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();

            for (int j = 0; j < C; j++) {
                mainMatrix[i][j] = string.charAt(j);

                if (mainMatrix[i][j] == 'I') {
                    mainRobotPos = new Pos(i, j);
                }

                if (mainMatrix[i][j] == 'R') {
                    crazyRobotPosMap.put(new Pos(i, j), crazyRobotPosMap.getOrDefault(new Pos(i, j), 0) + 1);
                }
            }
        }


        st = new StringTokenizer(br.readLine());
        String string = st.nextToken();

        commandList = new int[string.length()];
        for (int i = 0; i < string.length(); i++) {
            commandList[i] = Character.getNumericValue(string.charAt(i)) - 1;
        }

    }
}