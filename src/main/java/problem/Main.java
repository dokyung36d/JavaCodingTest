package problem;

import java.io.*;
import java.util.*;

public class Main {
    static List<Integer> commandList;
    static Map<Node, Integer> scoreMap;


    public static class Node {
        int left;
        int right;

        public Node(int left, int right) {
            this.left = left;
            this.right = right;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Node anotherNode = (Node) obj;
            if (this.left == anotherNode.left && this.right == anotherNode.right) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.left, this.right);
        }

        public int getCost(int from, int to) {
            if (from == 0) { return 2; }
            if (from == to) { return 1; }
            if (Math.abs(from - to) == 2) { return 4; }

            return 3;
        }
    }


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        scoreMap = new HashMap<>();
        scoreMap.put(new Node(0, 0), 0);


        for (int command : commandList) {
            Map<Node, Integer> updatedScoreMap = new HashMap<>();

            for (Node node : scoreMap.keySet()) {
                // Left Move
                if (command != node.right) {
                    int totalCost = scoreMap.get(node) + node.getCost(node.left, command);
                    Node movedNode = new Node(command, node.right);

                    updatedScoreMap.put(movedNode, Math.min(totalCost, updatedScoreMap.getOrDefault(movedNode, Integer.MAX_VALUE / 2)));
                }

                //Right Move
                if (command != node.left) {
                    int totalCost = scoreMap.get(node) + node.getCost(node.right, command);
                    Node movedNode = new Node(node.left, command);

                    updatedScoreMap.put(movedNode, Math.min(totalCost, updatedScoreMap.getOrDefault(movedNode, Integer.MAX_VALUE / 2)));
                }
            }

            scoreMap = updatedScoreMap;
        }


        int answer = Integer.MAX_VALUE / 2;
        for (Node node : scoreMap.keySet()) {
            answer = Math.min(answer, scoreMap.get(node));
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        commandList = new ArrayList<>();
        while (true) {
            int command = Integer.parseInt(st.nextToken());
            if (command == 0) { break; }

            commandList.add(command);
        }
    }
}
