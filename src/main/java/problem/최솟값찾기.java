package problem;

import java.util.*;
import java.io.*;

public class 최솟값찾기 {
    static int N, L;
    static int[] numList;

    public static class Node implements Comparable<Node> {
        int value;
        int index;

        public Node(int value, int index) {
            this.value = value;
            this.index = index;
        }

        @Override
        public int compareTo(Node anotherNode) {
            if (this.value == anotherNode.value) {
                return Integer.compare(this.index, anotherNode.index);
            }

            return Integer.compare(this.value, anotherNode.value);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        StringBuilder sb = new StringBuilder();


        PriorityQueue<Node> queue = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            queue.add(new Node(numList[i], i));

            while (true) {
                Node node = queue.poll();
                if (node.index < i - L + 1) {
                    continue;
                }

                sb.append(node.value + " ");
                queue.add(node);
                break;
            }
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));

    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());

        numList = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());
        }

    }
}