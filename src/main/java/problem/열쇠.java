package problem;

import java.util.*;
import java.io.*;

public class 열쇠 {
    static int T;
    static char[][] matrix;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static String doorString = "QWERTYUIOPASDFGHJKLZXCVBNM";
    static String keyString = "qwertyuiopasdfghjklzxcvbnm";
    static Map<Character, List<Pos>> doorMap;
    static Map<Character, List<Pos>> blockedPosMap;
    static Pos[] directions = {new Pos(-1, 0), new Pos(1, 0), new Pos(0, -1), new Pos(0, 1)};
    static int row, col;
    static Deque<Pos> queue = new ArrayDeque<>();
    static int[][] visited;


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

        public boolean checkPosAvail() {
            if (this.row < 0 || this.row >= matrix.length || this.col < 0 || this.col >= matrix[0].length) {
                return false;
            }

            return true;
        }

        public boolean existWall() {
            if (matrix[this.row][this.col] == '*') {
                return true;
            }

            return false;
        }




        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj != null || this.getClass() != obj.getClass()) { return false; }
            Pos anotherPos = (Pos) obj;

            if (this.row == anotherPos.row && this.col == anotherPos.col) {
                return true;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        T = Integer.parseInt(st.nextToken());
        for (int i = 0; i < T; i++) {
            init();
            solution();
        }
    }

    public static void solution() throws Exception {
        gatherStartPos();

        int answer = 0;
        while (!queue.isEmpty()) {
            Pos curPos = queue.pollLast();

            if (visited[curPos.row][curPos.col] == 1) {
                continue;
            }

            char posValue = matrix[curPos.row][curPos.col];
            if (Character.isLetter(posValue) && Character.isUpperCase(posValue)) {
                blockedPosMap.get(posValue).add(new Pos(curPos.row, curPos.col));
                continue;
            }

            else if (Character.isLetter(posValue) && Character.isLowerCase(posValue)) {
                matrix[curPos.row][curPos.col] = '.';
                openDoorsByKey(posValue);
                addClosedDoorToQueue(posValue);
                visited[curPos.row][curPos.col] = 1;
            }

            else if (matrix[curPos.row][curPos.col] == '$') {
                matrix[curPos.row][curPos.col] = '.';
                answer += 1;
                visited[curPos.row][curPos.col] = 1;
            }
            else if (matrix[curPos.row][curPos.col] == '.') {
                visited[curPos.row][curPos.col] = 1;
            }


            for (Pos direction : directions) {
                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.checkPosAvail() || movedPos.existWall()) {
                    continue;
                }

                if (visited[movedPos.row][movedPos.col] == 1) {
                    continue;
                }

                queue.add(movedPos);
            }
        }
        System.out.println(answer);
        return;
    }

    public static void addClosedDoorToQueue(char key) {
        char door = Character.toUpperCase(key);
        for (Pos closedDoorPos : blockedPosMap.get(door)) {
            queue.add(closedDoorPos);
        }
    }

    public static void gatherStartPos() {
        for (int j = 0; j < col; j++) {
            if (matrix[0][j] != '*' && !isUpperLetter(matrix[0][j])) {
                queue.add(new Pos(0, j));
            }
            if (isUpperLetter(matrix[0][j])) {
                blockedPosMap.get(matrix[0][j]).add(new Pos(0, j));
            }

            if (matrix[row - 1][j] != '*' && !isUpperLetter(matrix[row - 1][j])) {
                queue.add(new Pos(row - 1, j));
            }
            if (isUpperLetter(matrix[row - 1][j])) {
                blockedPosMap.get(matrix[row - 1][j]).add(new Pos(row - 1, j));
            }

        }

        for (int i = 1; i < row - 1; i++) {
            if (matrix[i][0] != '*' && !isUpperLetter(matrix[i][0])) {
                queue.add(new Pos(i, 0));
            }
            if (isUpperLetter(matrix[i][0])) {
                blockedPosMap.get(matrix[i][0]).add(new Pos(i, 0));
            }

            if (matrix[i][col - 1] != '*' && !isUpperLetter(matrix[i][col - 1])) {
                queue.add(new Pos(i, col - 1));
            }
            if (isUpperLetter(matrix[i][col - 1])) {
                blockedPosMap.get(matrix[i][col - 1]).add(new Pos(i, col - 1));
            }
        }
    }

    public static void openDoorsByKey(char key) {
        char destDoor = Character.toUpperCase(key);
        for (Pos doorPos : doorMap.get(destDoor)) {
            matrix[doorPos.row][doorPos.col] = '.';
        }

        doorMap.put(destDoor, new ArrayList<>());
    }

    public static boolean isUpperLetter(char x) {
        if (Character.isLetter(x) && Character.isUpperCase(x)) {
            return true;
        }

        return false;
    }


    public static void init() throws IOException {
        doorMap = new HashMap<>();
        blockedPosMap = new HashMap<>();
        for (int i = 0; i < doorString.length(); i++) {
            doorMap.put(doorString.charAt(i), new ArrayList<>());
            blockedPosMap.put(doorString.charAt(i), new ArrayList<>());
        }

        StringTokenizer st = new StringTokenizer(br.readLine());
        row = Integer.parseInt(st.nextToken());
        col = Integer.parseInt(st.nextToken());

        queue = new ArrayDeque<>();
        matrix = new char[row][col];
        visited = new int[row][col];

        for (int i = 0; i < row; i++) {
            st = new StringTokenizer(br.readLine());
            String newRow = st.nextToken();
            for (int j = 0; j < col; j++) {
                matrix[i][j] = newRow.charAt(j);
                if (Character.isLetter(matrix[i][j]) && !Character.isLowerCase(matrix[i][j])) {
                    doorMap.get(matrix[i][j]).add(new Pos(i, j));
                }
            }
        }

        st = new StringTokenizer(br.readLine());
        String availableKeyString = st.nextToken();
        if (!availableKeyString.equals("0")) {
            for (int i = 0; i < availableKeyString.length(); i++) {
                openDoorsByKey(availableKeyString.charAt(i));
            }
        }
    }

}