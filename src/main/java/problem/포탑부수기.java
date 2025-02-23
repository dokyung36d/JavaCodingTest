package problem;

import java.util.*;
import java.io.*;


public class 포탑부수기 {
    static int N, M, K;
    static Pos[][] posMatrix;
    static Pos[] directions = {new Pos(0, 1, 0), new Pos(1, 0, 0),
            new Pos(0, -1, 0), new Pos(-1, 0, 0)};
    static Pos[] sidePosDirections = {new Pos(-1, -1, 0), new Pos(-1, 0,0), new Pos(-1, 1, 0),
            new Pos(0, -1, 0), new Pos(0, 0, 0), new Pos(0, 1, 0),
            new Pos(1, -1, 0), new Pos(1, 0, 0), new Pos(1, 1, 0)};

    public static class Pos implements Comparable<Pos> {
        int row;
        int col;
        int attackValue;
        int lastAttacked;

        public Pos(int row, int col, int attackValue) {
            this.row = row;
            this.col = col;
            this.attackValue = attackValue;
            this.lastAttacked = 0;
        }

        public Pos addPos(Pos anotherPos) {
            return new Pos((this.row + anotherPos.row + N) % N, (this.col + anotherPos.col + M) % M, this.attackValue);
        }

        public int compareTo(Pos anotherPos) {
            if (this.attackValue != anotherPos.attackValue) {
                return Integer.compare(this.attackValue, anotherPos.attackValue);
            }

            if (this.lastAttacked != anotherPos.lastAttacked) {
                return Integer.compare(-this.lastAttacked, -anotherPos.lastAttacked);
            }

            if ((this.row + this.col) != (anotherPos.row + anotherPos.col)) {
                return Integer.compare(-(this.row + this.col), -(anotherPos.row + anotherPos.col));
            }

            return Integer.compare(-this.col, -anotherPos.col);
        }

        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }
            Pos anotherPos = (Pos) obj;
            if (this.row == anotherPos.row && this.col == anotherPos.col) { return true; }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }
    }

    public static class Node {
        Pos curPos;
        List<Pos> path;

        public Node(Pos curPos, List<Pos> path) {
            this.curPos = curPos;
            this.path = path;
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        for (int k = 0; k < K; k++) {
            if (getNumOfTower() <= 1) { break; }
            Pos attakPos = findAttacker();
            Pos defendPos = findDefender();
            posMatrix[attakPos.row][attakPos.col].attackValue += (N + M);
            List<Pos> path = findFastestPath(attakPos, defendPos);
            if (path.size() == 0) {
                for (Pos sidePosDirection : sidePosDirections) {
                    Pos movedPos = defendPos.addPos(sidePosDirection);
                    if (posMatrix[movedPos.row][movedPos.col].attackValue <= 0) {
                        continue;
                    }

                    path.add(movedPos);
                }
            }

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    Pos pos = posMatrix[i][j];
                    if (pos.attackValue <= 0) {
                        continue;
                    }

                    if (pos.equals(attakPos)) {
                        pos.lastAttacked = k + 1;
                    }
                    else if (pos.equals(defendPos)) {
                        pos.attackValue -= attakPos.attackValue;
                    }
                    else if (path.contains(pos)) {
                        pos.attackValue -= (attakPos.attackValue / 2);
                    }
                    else {
                        pos.attackValue += 1;
                    }
                }
            }
        }

        int maxAttackValue = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (posMatrix[i][j].attackValue > maxAttackValue) {
                    maxAttackValue = posMatrix[i][j].attackValue;
                }
            }
        }

        System.out.println(maxAttackValue);

    }

    public static int getNumOfTower() {
        int numTower = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (posMatrix[i][j].attackValue > 0) {
                    numTower += 1;
                }
            }
        }

        return numTower;
    }

    public static List<Pos> findFastestPath(Pos attackPos, Pos defendPos) {
        Deque<Node> queue = new ArrayDeque<>();

        int[][] visited = new int[N][M];
        queue.add(new Node(attackPos, new ArrayList<>()));

        while (!queue.isEmpty()) {
            Node node = queue.pollFirst();
            if (visited[node.curPos.row][node.curPos.col] == 1) { continue; }
            visited[node.curPos.row][node.curPos.col] = 1;

            for (Pos direction : directions) {
                Pos movedPos = node.curPos.addPos(direction);
                if (posMatrix[movedPos.row][movedPos.col].attackValue <= 0) { continue; }
                if (visited[movedPos.row][movedPos.col] == 1) { continue; }

                List<Pos> copiedPath = new ArrayList<>(node.path);
                copiedPath.add(movedPos);

                if (movedPos.equals(defendPos)) {
                    return copiedPath;
                }

                queue.addLast(new Node(movedPos, copiedPath));
            }
        }

        return new ArrayList<>();
    }

    public static Pos findAttacker() {
        Pos smallestPos = new Pos(0, 0, Integer.MAX_VALUE);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (posMatrix[i][j].attackValue <= 0) { continue; }
                if (smallestPos.compareTo(posMatrix[i][j]) > 0) {
                    smallestPos = posMatrix[i][j];
                }
            }
        }

        return smallestPos;
    }

    public static Pos findDefender() {
        Pos biggestPos = new Pos(0, 0, Integer.MIN_VALUE);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (posMatrix[i][j].attackValue <= 0) { continue; }
                if (biggestPos.compareTo(posMatrix[i][j]) < 0) {
                    biggestPos = posMatrix[i][j];
                }
            }
        }

        return biggestPos;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        posMatrix = new Pos[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                int attackValue = Integer.parseInt(st.nextToken());
                posMatrix[i][j] = new Pos(i, j, attackValue);
            }
        }
    }
}