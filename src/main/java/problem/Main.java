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

            // Left Move
            for (Node node : costMap.keySet()) {
                if (command == node.right) { continue; }

                int cost = getCost(node.left, command);


                Node updatedNode = new Node(command, node.right);
                int prevMinCost = updatedCostMap.getOrDefault(updatedNode, Integer.MAX_VALUE / 2);

                updatedCostMap.put(updatedNode, Math.min(prevMinCost, costMap.get(node) + cost));
            }


            // Right Move
            for (Node node : costMap.keySet()) {
                if (command == node.left) { continue; }

                int cost = getCost(node.right, command);


                Node updatedNode = new Node(node.left, command);
                int prevMinCost = updatedCostMap.getOrDefault(updatedNode, Integer.MAX_VALUE / 2);

                updatedCostMap.put(updatedNode, Math.min(prevMinCost, costMap.get(node) + cost));
            }


            costMap = updatedCostMap;
        }


        int answer = Integer.MAX_VALUE / 2;
        for (Node node : costMap.keySet()) {
            answer = Math.min(answer, costMap.get(node));
        }


        System.out.println(answer);
    }


    public static int getCost(int from, int to) {
        if (from == 0) { return 2; }
        if (Math.abs(from - to) == 2) { return 4; }
        if (from == to) { return 1; }

        return 3;
    }


    public static void init() throws IOException {
        commandList = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(bufferedReader.readLine());

        while (true) {
            int command = Integer.parseInt(st.nextToken());
            if (command == 0) { break; }

            commandList.add(command);
        }
    }

}