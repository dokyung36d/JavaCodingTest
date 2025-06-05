package problem;

import java.util.*;
import java.io.*;


public class 세용액 {
    static int N;
    static int[] numList;

    public static class Node {
        int leftIndex;
        int midIndex;
        int rightIndex;
        long sumValue;

        public Node(int leftIndex, int midIndex, int rightIndex) {
            this.leftIndex = leftIndex;
            this.midIndex = midIndex;
            this.rightIndex = rightIndex;

            this.sumValue = Math.abs((long) numList[leftIndex] + (long) numList[midIndex] + (long) numList[rightIndex]);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        Node bestNode = new Node(0, 1, 2);

        for (int leftIndex = 0; leftIndex < N - 2; leftIndex++) {
            Node node = getBestNode(leftIndex);

            if (node.sumValue < bestNode.sumValue) {
                bestNode = node;
            }
        }

        int[] answerList = new int[3];
        answerList[0] = numList[bestNode.leftIndex];
        answerList[1] = numList[bestNode.midIndex];
        answerList[2] = numList[bestNode.rightIndex];
        Arrays.sort(answerList);

        StringBuilder sb = new StringBuilder();
        sb.append(answerList[0]);
        sb.append(" ");
        sb.append(answerList[1]);
        sb.append(" ");
        sb.append(answerList[2]);

        System.out.println(sb.toString());

    }

    public static Node getBestNode(int leftIndex) {
        long minSum = Long.MAX_VALUE;
        Node bestNode = null;

        int midIndex = leftIndex + 1;
        int rightIndex = N - 1;
        while (midIndex < rightIndex) {
            long sumValue = (long) numList[leftIndex] + (long) numList[midIndex] + (long) numList[rightIndex];

            if (Math.abs(sumValue) < minSum) {
                minSum = Math.abs(sumValue);
                bestNode = new Node(leftIndex, midIndex, rightIndex);
            }

            if (sumValue <= 0) {
                midIndex += 1;
            }

            else {
                rightIndex -= 1;
            }
        }

        return bestNode;
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

        Arrays.sort(numList);
    }
}