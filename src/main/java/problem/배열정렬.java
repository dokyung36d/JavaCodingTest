package problem;

import java.util.*;
import java.io.*;


public class 배열정렬 {
    static int N, M;
    static List<Integer> numList;
    static Command[] commandList;
    static HashMap<List<Integer>, Integer> map = new HashMap<>();
    static HashMap<List<Integer>, Integer> confirmed = new HashMap<>();

    public static class Command {
        int smallIndex;
        int bigIndex;
        int cost;

        public Command(int index1, int index2, int cost) {
            this.smallIndex = index1;
            this.bigIndex = index2;
            this.cost = cost;
        }
    }

    public static class Node implements Comparable<Node> {
        List<Integer> array;
        int totalCost;

        public Node(List<Integer> array, int totalCost) {
            this.array = array;
            this.totalCost = totalCost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.totalCost, anotherNode.totalCost);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        queue.add(new Node(numList, 0));
        map.put(numList, 0);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (isSorted(node.array)) {
                System.out.println(node.totalCost);
                return;
            }

            if (confirmed.get(node.array) == null) {
                confirmed.put(node.array, 1);
            }
            else {
                continue;
            }

            for (int i = 0; i < M; i++) {
                Command command = commandList[i];

                List<Integer> swappedArray = swap(node.array, command.smallIndex, command.bigIndex);
                if (confirmed.get(swappedArray) != null) {
                    continue;
                }

                if (map.get(swappedArray) != null && map.get(swappedArray) <= node.totalCost + command.cost) {
                    continue;
                }
                map.put(swappedArray, node.totalCost + command.cost);
                queue.add(new Node(swappedArray, node.totalCost + command.cost));
            }
        }

        System.out.println(-1);
        return;

    }

    public static boolean isSorted(List<Integer> array) {
        for (int i = 0; i < array.size() - 1; i++) {
            if (array.get(i) > array.get(i + 1)) {
                return false;
            }
        }

        return true;
    }

    public static List<Integer> swap(List<Integer> array, int index1, int index2) {
        List<Integer> copiedArray = new ArrayList<>(array);

        copiedArray.set(index1, array.get(index2));
        copiedArray.set(index2, array.get(index1));

        return copiedArray;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        numList = new ArrayList<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList.add(Integer.parseInt(st.nextToken()));
        }

        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());
        commandList = new Command[M];

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int index1 = Integer.parseInt(st.nextToken()) - 1;
            int index2 = Integer.parseInt(st.nextToken()) - 1;
            int cost = Integer.parseInt(st.nextToken());

            commandList[i] =  new Command(index1, index2, cost);
        }

    }
}