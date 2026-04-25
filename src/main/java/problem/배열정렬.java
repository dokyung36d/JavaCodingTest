package problem;

import java.util.*;
import java.io.*;


public class 배열정렬 {
    static int N, M;
    static int[][] commandList;
    static PriorityQueue<Node> pq;
    static List<Integer> sortedList;

    public static class Node implements Comparable<Node> {
        List<Integer> numList;
        int cost;

        public Node(List<Integer> numList, int cost) {
            this.numList = numList;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.cost, anotherNode.cost);
        }
    }

    public static void main(String[] args) throws Exception {

        init();
        solution();
    }

    public static void solution() {
        Map<List<Integer>, Integer> visitedMap = new HashMap<>();


        while (!pq.isEmpty()) {
            Node node = pq.poll();

            if (visitedMap.get(node.numList) != null) { continue; }
            visitedMap.put(node.numList, 1);

            if (node.numList.equals(sortedList)) {
                System.out.println(node.cost);

                return;
            }


            for (int[] command : commandList) {
                List<Integer> swappedList = swap(node.numList, command);

                if (visitedMap.get(swappedList) != null) { continue; }
                pq.add(new Node(swappedList, node.cost + command[2]));
            }
        }

        System.out.println(-1);
    }

    public static List<Integer> swap(List<Integer> list, int[] command) {
        List<Integer> copiedList = new ArrayList<>(list);

        copiedList.set(command[0], list.get(command[1]));
        copiedList.set(command[1], list.get(command[0]));

        return copiedList;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        pq = new PriorityQueue<>();

        N = Integer.parseInt(st.nextToken());
        List<Integer> numList = new ArrayList<>();
        sortedList = new ArrayList<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int num = Integer.parseInt(st.nextToken());

            numList.add(num);
            sortedList.add(num);
        }
        Collections.sort(sortedList);

        pq.add(new Node(numList, 0));


        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());

        commandList = new int[M][3];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int index1 = Integer.parseInt(st.nextToken()) - 1;
            int index2 = Integer.parseInt(st.nextToken()) - 1;
            int cost = Integer.parseInt(st.nextToken());

            commandList[i][0] = index1;
            commandList[i][1] = index2;
            commandList[i][2] = cost;
        }
    }
}