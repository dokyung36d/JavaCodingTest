package problem;

import java.util.*;
import java.io.*;

public class 개구리의여행 {
    static int N, Q;
    static char[][] mainMatrix;
    static Pos[][] commands;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, - 1)};

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

        public Pos multiplyPos(int numMultiply) {
            return new Pos(this.row * numMultiply, this.col * numMultiply);
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

    public static class Frog implements Comparable<Frog> {
        Pos pos;
        int time;
        int power;

        public Frog(Pos pos, int time, int power) {
            this.pos = pos;
            this.time = time;
            this.power = power;
        }

        public Frog powerUp() {
            return new Frog(this.pos, this.time + (int) Math.pow(this.power + 1, 2), this.power + 1);
        }

        public Frog powerDown(int downPower) {
            return new Frog(this.pos, this.time + 1, downPower);
        }

        @Override
        public int compareTo(Frog anotherFrog) {
            return Integer.compare(this.time, anotherFrog.time);
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Q; i++) {
            Pos startPos = commands[i][0];
            Pos destPos = commands[i][1];

            sb.append(solution(startPos, destPos) + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int solution(Pos startPos, Pos destPos) {
        int[][][] visited = new int[N][N][5];
        PriorityQueue<Frog> pq = new PriorityQueue<>();
        pq.add(new Frog(startPos, 0, 1));

        while (!pq.isEmpty()) {
            Frog frog = pq.poll();
            if (visited[frog.pos.row][frog.pos.col][frog.power - 1] == 1) { continue; }
            visited[frog.pos.row][frog.pos.col][frog.power - 1] = 1;
            if (frog.pos.equals(destPos)) { return frog.time; }


            // Jump
            for (Pos direction : directions) {
                if (!isMovePossible(frog, direction)) { continue; }

                Pos movedPos = frog.pos.addPos(direction.multiplyPos(frog.power));
                if (visited[movedPos.row][movedPos.col][frog.power - 1] == 1) { continue; }
                pq.add(new Frog(movedPos, frog.time + 1, frog.power));
            }

            //Power up
            if (frog.power != 5) {
                pq.add(frog.powerUp());
            }

            //Power down
            for (int downPower = 1; downPower < frog.power; downPower++) {
                pq.add(frog.powerDown(downPower));
            }
        }

        return -1;
    }

    public static boolean isMovePossible(Frog frog, Pos direction) {
        for (int i = 1; i < frog.power; i++) {
            Pos delta = direction.multiplyPos(i);
            Pos movedPos = frog.pos.addPos(delta);
            if (!movedPos.isValidIndex()) { return false; }
            if (mainMatrix[movedPos.row][movedPos.col] == '#') { return false; }
        }

        Pos destPos = frog.pos.addPos(direction.multiplyPos(frog.power));
        if (!destPos.isValidIndex()) { return false; }
        if (mainMatrix[destPos.row][destPos.col] == 'S') { return false; }
        if (mainMatrix[destPos.row][destPos.col] == '#') { return false; }


        return true;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());

        mainMatrix = new char[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();
            for (int j = 0; j < N; j++) {
                mainMatrix[i][j] = string.charAt(j);
            }
        }

        st = new StringTokenizer(br.readLine());
        Q = Integer.parseInt(st.nextToken());
        commands = new Pos[Q][2];

        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());

            int row1 = Integer.parseInt(st.nextToken()) - 1;
            int col1 = Integer.parseInt(st.nextToken()) - 1;
            Pos startPos = new Pos(row1, col1);

            int row2 = Integer.parseInt(st.nextToken()) - 1;
            int col2 = Integer.parseInt(st.nextToken()) - 1;
            Pos destPos = new Pos(row2, col2);

            commands[i][0] = startPos;
            commands[i][1] = destPos;
        }
    }

}
