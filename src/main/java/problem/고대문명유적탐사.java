package problem;

import java.util.*;
import java.io.*;


public class 고대문명유적탐사 {
    static int[][] mainMatrix;
    static int K, M;
    static Deque<Integer> treasureQueue;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, - 1)};

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
            if (this.row < 0 || this.row >= 5 || this.col < 0 || this.col >= 5) {
                return false;
            }

            return true;
        }

        @Override
        public int compareTo(Pos anotherPos) {
            if (this.col != anotherPos.col) {
                return Integer.compare(this.col, anotherPos.col);
            }

            return Integer.compare(-this.row, -anotherPos.row);
        }
    }

    public static class Node implements Comparable<Node> {
        PriorityQueue<Pos> poppedTreasurePriorityQueue;
        int numRotated;
        Pos topLeftPos;
        int[][] partMatrix;

        public Node(PriorityQueue<Pos> poppedTreasurePriorityQueue, int numRotated, Pos topLeftPos, int[][] partMatrix) {
            this.poppedTreasurePriorityQueue = poppedTreasurePriorityQueue;
            this.numRotated = numRotated;
            this.topLeftPos = topLeftPos;
            this.partMatrix = partMatrix;
        }

        @Override
        public int compareTo(Node anotherNode) {
            if (this.poppedTreasurePriorityQueue.size() != anotherNode.poppedTreasurePriorityQueue.size()) {
                return Integer.compare(this.poppedTreasurePriorityQueue.size(), anotherNode.poppedTreasurePriorityQueue.size());
            }

            if (this.numRotated != anotherNode.numRotated) {
                return Integer.compare(-this.numRotated, -anotherNode.numRotated);
            }

            if (this.topLeftPos.col != anotherNode.topLeftPos.col) {
                return Integer.compare(-this.topLeftPos.col, -anotherNode.topLeftPos.col);
            }

            return Integer.compare(-this.topLeftPos.row, -anotherNode.topLeftPos.row);
        }
    }

    public static int[][] getPartMatrix(Pos topLeftPos) {
        int[][] partMatrix = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                partMatrix[i][j] = mainMatrix[topLeftPos.row + i][topLeftPos.col + j];
            }
        }

        return partMatrix;
    }

    public static int[][] rotateMatrix(int[][] matrix) {
        int[][] rotatedMatrix = new int[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                rotatedMatrix[j][3 - i - 1] = matrix[i][j];
            }
        }

        return rotatedMatrix;
    }

    public static int[][] applyPartMatrixToMainMatrix(int[][] partMatrix, Pos topLeftPos) {
        int[][] appliedMatrix = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                appliedMatrix[i][j] = mainMatrix[i][j];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                appliedMatrix[topLeftPos.row + i][topLeftPos.col + j] = partMatrix[i][j];
            }
        }

        return appliedMatrix;
    }

    public static void main(String[] args) throws Exception {
        init();

        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < K; k++) {
            int answer = 0;

            Node bestNode = getBestNode();
            if (bestNode.poppedTreasurePriorityQueue.size() < 3) {
                break;
            }

            answer += bestNode.poppedTreasurePriorityQueue.size();
            mainMatrix = applyPartMatrixToMainMatrix(bestNode.partMatrix, bestNode.topLeftPos);
            putTreasureToMainMatrix(bestNode.poppedTreasurePriorityQueue);

            while (true) {
                PriorityQueue<Pos> priorityQueue = searchTreasure(mainMatrix);
                if (priorityQueue.size() < 3) {
                    break;
                }

                answer += priorityQueue.size();
                putTreasureToMainMatrix(priorityQueue);
            }
            sb.append(answer + " ");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void putTreasureToMainMatrix(PriorityQueue<Pos> posPriorityQueue) {
        while (!posPriorityQueue.isEmpty()) {
            Pos pos = posPriorityQueue.poll();
            int value = treasureQueue.pollFirst();

            mainMatrix[pos.row][pos.col] = value;
        }
    }

    public static Node getBestNode() {
        Node bestNode = new Node(new PriorityQueue<>(), 4, new Pos(3, 3), new int[3][3]);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Pos topLeftPos = new Pos(i, j);
                int[][] partMatrix = getPartMatrix(topLeftPos);
                for (int numRotated = 1; numRotated <= 3; numRotated++) {
                    partMatrix = rotateMatrix(partMatrix);
                    int[][] appliedMatrix = applyPartMatrixToMainMatrix(partMatrix, topLeftPos);
                    PriorityQueue<Pos> searchedTreasurePriorityQueue = searchTreasure(appliedMatrix);
                    Node node = new Node(searchedTreasurePriorityQueue, numRotated, topLeftPos, partMatrix);
                    if (node.compareTo(bestNode) == 1) {
                        bestNode = node;
                    }
                }
            }
        }

        return bestNode;
    }

    public static PriorityQueue<Pos> searchTreasure(int[][] matrix) {
        int[][] visited = new int[5][5];
        PriorityQueue<Pos> priorityQueue = new PriorityQueue();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (visited[i][j] == 1) { continue; }

                List<Pos> treasureList = new ArrayList<>();
                Deque<Pos> queue = new ArrayDeque<>();
                queue.addLast(new Pos(i, j));
                while (!queue.isEmpty()) {
                    Pos curPos = queue.pollFirst();
                    if (visited[curPos.row][curPos.col] == 1) { continue; }
                    visited[curPos.row][curPos.col] = 1;
                    treasureList.add(curPos);

                    for (Pos direction : directions) {
                        Pos movedPos = curPos.addPos(direction);
                        if (!movedPos.isValidIndex() || visited[movedPos.row][movedPos.col] == 1) { continue; }
                        if (matrix[curPos.row][curPos.col] != matrix[movedPos.row][movedPos.col]) { continue; }

                        queue.addLast(movedPos);
                    }
                }

                if (treasureList.size() >= 3) {
                    priorityQueue.addAll(treasureList);
                }
            }
        }

        return priorityQueue;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        mainMatrix = new int[5][5];
        for (int i = 0; i < 5; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 5; j++) {
                mainMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        treasureQueue = new ArrayDeque<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            treasureQueue.addLast(Integer.parseInt(st.nextToken()));
        }

    }
}