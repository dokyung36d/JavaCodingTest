package problem;

import java.util.*;
import java.io.*;


public class Main {
    static List<Integer> commandList;

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
            if (this.left == anotherNode.left && this.right == anotherNode.right) { return true; }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.left, this.right);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        Map<Node, Integer> costMap = new HashMap<>();
        costMap.put(new Node(0, 0), 0);

        for (int command : commandList) {
            Map<Node, Integer> updatedCostMap = new HashMap<>();

            for (Node node : costMap.keySet()) {
                int prevCost = costMap.get(node);

                if (command != node.right) {
                    int leftMoveCost = calcCost(node.left, command);
                    Node leftMoveNode = new Node(command, node.right);
                    if (updatedCostMap.get(leftMoveNode) == null ||  prevCost + leftMoveCost < updatedCostMap.get(leftMoveNode)) {
                        updatedCostMap.put(leftMoveNode, prevCost + leftMoveCost);
                    }
                }


                if (command != node.left) {
                    int rightMoveCost = calcCost(node.right, command);
                    Node rightMoveNode = new Node(node.left, command);
                    if (updatedCostMap.get(rightMoveNode) == null || prevCost + rightMoveCost < updatedCostMap.get(rightMoveNode)) {
                        updatedCostMap.put(rightMoveNode, prevCost + rightMoveCost);
                    }
                }
            }


            costMap = updatedCostMap;
        }


        int answer = Integer.MAX_VALUE;
        for (Node node : costMap.keySet()) {
            answer = Math.min(answer, costMap.get(node));
        }

        if (answer == Integer.MAX_VALUE) {
            System.out.println(0);
        }
        else {
            System.out.println(answer);
        }
    }


    public static int calcCost(int from, int to) {
        if (from == 0) { return 2; }
        if (from == to) { return 1; }
        if (Math.abs(from - to) == 2) { return 4; }

        return 3;
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