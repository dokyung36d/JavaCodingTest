package problem;

import java.util.*;
import java.io.*;

public class DanceDanceRevolution {
    static List<Integer> commandList;

    public static class Node {
        int left;
        int right;

        public Node(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.left, this.right);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Node anotherNode = (Node) obj;
            if (this.left == anotherNode.left && this.right == anotherNode.right) { return true; }
            return false;
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
                int leftCost = getCost(node.left, command);
                Node leftMovedNode = new Node(command, node.right);
                updatedCostMap.put(leftMovedNode, Math.min(updatedCostMap.getOrDefault(leftMovedNode, Integer.MAX_VALUE / 2), costMap.get(node) + leftCost));


                int rightCost = getCost(node.right, command);
                Node rightMovedNode = new Node(node.left, command);
                updatedCostMap.put(rightMovedNode, Math.min(updatedCostMap.getOrDefault(rightMovedNode, Integer.MAX_VALUE / 2), costMap.get(node) + rightCost));
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
        if (from == 0) {
            return 2;
        }

        if (from == to) {
            return 1;
        }

        if (Math.abs(from - to) == 2) {
            return 4;
        }

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