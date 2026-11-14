package problem;

import java.io.*;
import java.util.*;

public class 배열정렬 {
    static int N, M;
    static List<Integer> originalNumList;
    static int[][] commandList;
    static Map<List<Integer>, Integer> visitedMap;

    public static class Node implements Comparable<Node> {
        List<Integer> numList;
        int totalCost;

        public Node(List<Integer> numList, int totalCost) {
            this.numList = numList;
            this.totalCost = totalCost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.totalCost, anotherNode.totalCost);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(originalNumList, 0));


        while (!pq.isEmpty()) {
            Node node = pq.poll();

            if (visitedMap.get(node.numList) != null) { continue; }
            visitedMap.put(node.numList, 1);

            if (isSorted(node.numList)) {
                System.out.println(node.totalCost);
                return;
            }

            for (int i = 0; i < M; i++) {
                List<Integer> swappedList = switchIndex(node.numList, commandList[i][0], commandList[i][1]);
                if (visitedMap.get(swappedList) != null) { continue; }

                pq.add(new Node(swappedList, node.totalCost + commandList[i][2]));
            }
        }


        System.out.println(-1);
        return;
    }

    public static boolean isSorted(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) {
                return false;
            }
        }

        return true;
    }

    public static List<Integer> switchIndex(List<Integer> list, int index1, int index2) {
        List<Integer> updatedList = new ArrayList<>(list);

        updatedList.set(index1, list.get(index2));
        updatedList.set(index2, list.get(index1));

        return updatedList;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        originalNumList = new ArrayList<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            originalNumList.add(Integer.parseInt(st.nextToken()));
        }


        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());

        commandList = new int[M][3];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            commandList[i][0] = Integer.parseInt(st.nextToken()) - 1;
            commandList[i][1] = Integer.parseInt(st.nextToken()) - 1;
            commandList[i][2] = Integer.parseInt(st.nextToken());
        }

        visitedMap = new HashMap<>();
    }
}