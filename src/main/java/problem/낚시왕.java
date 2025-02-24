package problem;

import java.util.*;
import java.io.*;


public class 낚시왕 {
    static int R, C, M;
    static Shark[][] sharkMatrix;
    static Map<Pos, Shark> sharkMap;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1),
            new Pos(1, 0), new Pos(0, -1)};

    public static class Pos {
        int row;
        int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public boolean isValidIndex() {
            if (this.row < 0 || this.row >= R || this.col < 0 || this.col >= C) {
                return false;
            }

            return true;
        }

        public Pos addPos(Pos direction) {
            return new Pos(this.row + direction.row, this.col + direction.col);
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

    //Idea : 각 시점의 위치를 전부 계산해둠.
    public static class Shark implements Comparable<Shark> {
        Pos pos;
        int directionIndex;
        int speed;
        int size;


        public Shark(Pos pos, int directionIndex, int speed, int size) {
            this.pos = pos;
            this.directionIndex = directionIndex;
            this.speed = speed;
            this.size = size;
        }

        public int compareTo(Shark anotherShark) {
            return Integer.compare(this.size, anotherShark.size);
        }

        public Shark advance() {
            Pos curPos = this.pos;
            if (directions[directionIndex].row == 0) {
                this.speed = (this.speed) % (2 * C  - 2);
            }
            else {
                this.speed = (this.speed) % (2 * R - 2);
            }
            for (int i = 0; i < speed; i++) {
                Pos movedPos = curPos.addPos(directions[directionIndex]);
                if (!movedPos.isValidIndex()) {
                    directionIndex = (directionIndex + 2) % 4;
                    movedPos = curPos.addPos(directions[directionIndex]);
                }

                curPos = movedPos;
            }

            return new Shark(curPos, directionIndex, speed, size);
        }

    }


    public static void main(String[] args) throws Exception {
        init();
        int answer = 0;

        for (int j = 0; j < C; j++) {
            for (int i = 0; i < R; i++) {
                if (sharkMap.get(new Pos(i, j)) == null) { continue; }

                answer += sharkMap.get(new Pos(i, j)).size;
                sharkMap.remove(new Pos(i, j));
                break;
            }
            sharkMap = advanceAllSharks();
        }
        System.out.println(answer);
    }

    public static Map<Pos, Shark> advanceAllSharks() {
        Map<Pos, Shark> advancedSharkMap = new HashMap<>();
        for (Pos curSharkPos : sharkMap.keySet()) {
            Shark shark = sharkMap.get(curSharkPos);
            Shark movedShark = shark.advance();
            if (advancedSharkMap.get(movedShark.pos) == null) {
                advancedSharkMap.put(movedShark.pos, movedShark);
                continue;
            }

            if (movedShark.compareTo(advancedSharkMap.get(movedShark.pos)) > 0) {
                advancedSharkMap.put(movedShark.pos, movedShark);
            }

        }

        return advancedSharkMap;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        sharkMatrix = new Shark[R][C];
        sharkMap = new HashMap<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int row = Integer.parseInt(st.nextToken()) - 1;
            int col = Integer.parseInt(st.nextToken()) - 1;
            int speed = Integer.parseInt(st.nextToken());
            int directinoIndex = Integer.parseInt(st.nextToken()) - 1;
            int size = Integer.parseInt(st.nextToken());

            if (directinoIndex == 2) {
                directinoIndex = 1;
            }
            else if (directinoIndex == 1) {
                directinoIndex = 2;
            }

            sharkMatrix[row][col] = new Shark(new Pos(row, col), directinoIndex, speed, size);
            sharkMap.put(new Pos(row, col), new Shark(new Pos(row, col), directinoIndex, speed, size));
        }

    }
}