package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[] numList;

    public static class Node implements Comparable<Node> {
        int leftIndex;
        int rightIndex;

        public Node(int leftIndex, int rightIndex) {
            this.leftIndex = leftIndex;
            this.rightIndex = rightIndex;
        }

        public int getSumValue() {
            return numList[this.leftIndex] + numList[this.rightIndex];
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(Math.abs(this.getSumValue()), Math.abs(anotherNode.getSumValue()));
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int leftIndex = 0;
        int rightIndex = N - 1;

        Node bestNode = null;

        while (leftIndex < rightIndex) {
            Node node = new Node(leftIndex, rightIndex);

            if (bestNode == null || node.compareTo(bestNode) < 0) {
                bestNode = node;
            }

            if (node.getSumValue() == 0) {
                System.out.println(numList[node.leftIndex] + " " + numList[node.rightIndex]);
                return;
            }

            if (node.getSumValue() < 0) {
                leftIndex += 1;
            }
            else {
                rightIndex -= 1;
            }
        }

        System.out.println(numList[bestNode.leftIndex] + " " + numList[bestNode.rightIndex]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        numList = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());
        }
    }
}
