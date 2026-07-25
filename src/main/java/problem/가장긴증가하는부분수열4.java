package problem;

import java.util.*;
import java.io.*;


public class 가장긴증가하는부분수열4 {
    static int N;
    static int[] numList;
    static Map<Integer, Node> indexNodeMap;

    public static class Node implements Comparable<Node> {
        int index;
        int num;
        int prevNumIndex;

        public Node(int index) {
            this.index = index;
            this.num = numList[index];
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.num, anotherNode.num);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        indexNodeMap = new HashMap<>();

        List<Node> dpList = new ArrayList<>();
        dpList.add(new Node(0));
        indexNodeMap.put(0, new Node(0));

        for (int i = 1; i < N; i++) {
            Node node = new Node(i);

            int index = Collections.binarySearch(dpList, node);
            if (index >= 0) { continue; }


            index = - index - 1;
            if (index != 0) {
                node.prevNumIndex = dpList.get(index - 1).index;
            }
            indexNodeMap.put(i, node);

            if (index == dpList.size()) {
                dpList.add(node);
                continue;
            } else {
                dpList.set(index, node);
            }
        }



        System.out.println(dpList.size());


        List<Integer> answerList = new ArrayList<>();
        Node curNode = dpList.get(dpList.size() - 1);
        answerList.add(curNode.num);

        for (int i = 0; i < dpList.size() - 1; i++) {
            int prevIndex = curNode.prevNumIndex;

            curNode = indexNodeMap.get(prevIndex);
            answerList.add(curNode.num);
        }


        StringBuilder sb = new StringBuilder();
        for (int i = answerList.size() - 1; i >= 0; i--) {
            sb.append(answerList.get(i) + " ");
        }
        System.out.println(sb.toString().substring(0, sb.length() - 1));
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