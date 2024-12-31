package problem;

import java.util.*;
import java.io.*;

public class 장훈이의높은선반 {
    static class Node {
        int curIndex;
        int totalHeight;

        public Node(int curIndex, int firstHeight) {
            this.curIndex = curIndex;
            this.totalHeight = firstHeight;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int T = Integer.parseInt(st.nextToken());
        for (int i = 0; i < T; i++) {
            int answer = solution(br);
            System.out.println("#" + (i + 1) + " " + answer);
        }
    }

    public static int solution(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int[] heightList = new int[N];
        for (int i = 0; i < N; i++) {
            heightList[i] = Integer.parseInt(st.nextToken());
        }
        Deque<Node> stack = new ArrayDeque<>();
        stack.addLast(new Node(0, 0));
        stack.addLast(new Node(0, heightList[0]));

        int minValue = (int) Math.pow(10, 5);
        while (!stack.isEmpty()) {
            Node node = stack.pollFirst();
            if (node.totalHeight >= M) {
                if (node.totalHeight - M < minValue) {
                    minValue = node.totalHeight - M;
                }
                continue;
            }

            if (node.curIndex == N - 1) {
                continue;
            }

            Node nodeHeightNotAdded = new Node(node.curIndex + 1, node.totalHeight);
            stack.addLast(nodeHeightNotAdded);

            Node nodeHeightAdded = new Node(node.curIndex + 1, node.totalHeight + heightList[node.curIndex + 1]);
            if (nodeHeightAdded.totalHeight - M > minValue) {
                continue;
            }
            stack.addLast(nodeHeightAdded);

        }

        return minValue;
    }
}