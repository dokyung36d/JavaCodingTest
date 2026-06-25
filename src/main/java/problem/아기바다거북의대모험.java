package problem;

import java.util.*;
import java.io.*;


public class 아기바다거북의대모험 {
    static int N, M, K;
    static int[][] mainMatrix;
    static Map<Pos, Turtle> posTurtleMap;
    static Map<Integer, Turtle> turtleMap;
    static Map<Pos, Volcano> posVolcanoMap;
    static Pos[] directions = {new Pos(0, 1), new Pos(1, 0), new Pos(0, -1), new Pos(-1, 0)};
    static Pos destPos;
    static Map<Integer, Integer> turtleOutedMap;
    static Map<Pos, Integer> eruptedMap;
    static int[][] spreadMatrix;

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

    public static class Turtle {
        int uniqueNum;
        Pos curPos;

        public Turtle(int uniqueNum, Pos curPos) {
            this.uniqueNum = uniqueNum;
            this.curPos = curPos;
        }


        public void move() {
            int[][] visited = new int[N][N];

            Deque<Node> queue = new ArrayDeque<>();
            queue.add(new Node(this.curPos, null));

            while (!queue.isEmpty()) {
                Node node = queue.poll();

                if (visited[node.curPos.row][node.curPos.col] == 1) { continue; }
                visited[node.curPos.row][node.curPos.col] = 1;


                if (node.curPos.equals(destPos)) {
                    this.curPos = node.firstMovedPos;
                    return;
                }


                for (Pos direction : directions) {
                    Pos movedPos = node.curPos.addPos(direction);
                    if (!movedPos.isValidIndex()) { continue; }
                    if (visited[movedPos.row][movedPos.col] == 1) { continue; }
                    if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }
                    if (posTurtleMap.get(movedPos) != null) { continue; }

                    if (node.firstMovedPos == null) {
                        queue.add(new Node(movedPos, movedPos));
                    }
                    else {
                        queue.add(new Node(movedPos, node.firstMovedPos));
                    }
                }
            }
        }

    }

    public static class Volcano {
        Pos pos;
        int p;
        int curP;

        public Volcano(Pos pos, int p, int curP) {
            this.pos = pos;
            this.p = p;
            this.curP = curP;
        }


        static class SpreadNode {
            Pos curPos;
            int directionIndex;
            int power;

            public SpreadNode(Pos curPos, int directionIndex, int power) {
                this.curPos = curPos;
                this.directionIndex = directionIndex;
                this.power = power;
            }
        }

        public void spread() {
            spreadMatrix[this.pos.row][this.pos.col] += p;
            this.curP = 0;

            Deque<SpreadNode> queue = new ArrayDeque<>();
            for (int i = 0; i < 4; i++) {
                Pos movedPos = this.pos.addPos(directions[i]);
                if (!movedPos.isValidIndex()) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

                queue.add(new SpreadNode(movedPos, i, this.p / 2));
            }


            while (!queue.isEmpty()) {
                SpreadNode spreadNode = queue.pollFirst();

                spreadMatrix[spreadNode.curPos.row][spreadNode.curPos.col] += spreadNode.power;
                if (spreadNode.power < 2) { continue; }


                Pos movedPos = spreadNode.curPos.addPos(directions[spreadNode.directionIndex]);
                if (!movedPos.isValidIndex()) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

                queue.add(new SpreadNode(movedPos, spreadNode.directionIndex, spreadNode.power / 2));
            }
        }
    }

    public static class Node {
        Pos curPos;
        Pos firstMovedPos;

        public Node(Pos curPos, Pos firstMovedPos) {
            this.curPos = curPos;
            this.firstMovedPos = firstMovedPos;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {

        for (int turn = 1; turn <= 100; turn++) {
            allTurtleMove(turn);
            pressureUp();

            spreadMatrix = new int[N][N];
            eruptedMap = new HashMap<>();
            recursiveErupt();

            rockTurtle();
        }


        for (int i = 0; i < M; i++) {
            if (turtleOutedMap.get(i + 1) == null) {
                turtleOutedMap.put(i + 1, -1);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < M; i++) {
            sb.append(turtleOutedMap.get(i + 1) + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void rockTurtle() {
        for (int i = 0; i < M; i++) {
            if (turtleOutedMap.get(i + 1) != null) { continue; }

            Turtle turtle = turtleMap.get(i + 1);
            if (spreadMatrix[turtle.curPos.row][turtle.curPos.col] >= 20) {
                turtleOutedMap.put(i + 1, -1);
                mainMatrix[turtle.curPos.row][turtle.curPos.col] = 1;
            }
        }
    }

    public static void recursiveErupt() {
        int erupted = 0;

        for (Pos volcanoPos : posVolcanoMap.keySet()) {
            if (eruptedMap.get(volcanoPos) != null) { continue; }

            Volcano volcano = posVolcanoMap.get(volcanoPos);
            if (volcano.curP + spreadMatrix[volcano.pos.row][volcano.pos.col] < volcano.p) { continue; }

            erupted = 1;
            volcano.spread();
            eruptedMap.put(volcano.pos, 1);
        }


        if (erupted == 1) {
            recursiveErupt();
        }
    }

    public static void pressureUp() {
        for (Pos volcanoPos : posVolcanoMap.keySet()) {
            Volcano volcano = posVolcanoMap.get(volcanoPos);
            volcano.curP += 10;

            posVolcanoMap.put(volcanoPos, volcano);
        }
    }

    public static void allTurtleMove(int turn) {

        for (int i = 0; i < M; i++) {
            if (turtleOutedMap.get(i + 1) != null) { continue; }

            Turtle turtle = turtleMap.get(i + 1);

            turtleMap.remove(i + 1);
            posTurtleMap.remove(turtle.curPos);

            turtle.move();

            if (turtle.curPos.equals(destPos)) {
                turtleOutedMap.put(turtle.uniqueNum, turn);
                continue;
            }


            turtleMap.put(turtle.uniqueNum, turtle);
            posTurtleMap.put(turtle.curPos, turtle);
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        destPos = new Pos(N - 1,  N - 1);

        mainMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < N; j++) {
                mainMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        turtleMap = new HashMap<>();
        posTurtleMap = new HashMap<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int row = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken());
            posTurtleMap.put(new Pos(row, col), new Turtle(i + 1, new Pos(row, col)));
            turtleMap.put(i + 1, new Turtle(i + 1, new Pos(row, col)));
        }


        posVolcanoMap = new HashMap<>();
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            int row = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());

            posVolcanoMap.put(new Pos(row, col), new Volcano(new Pos(row, col), p, 0));
        }


        turtleOutedMap = new HashMap<>();
    }
}