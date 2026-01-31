package problem;

import java.util.*;
import java.io.*;



public class 배열정렬 {
    static int N, M;
    static List<Integer> numList;
    static Command[] commandList;

    public static class Command {
        int firstIndex;
        int secondIndex;
        int cost;

        public Command(int firstIndex, int secondIndex, int cost) {
            this.firstIndex = firstIndex;
            this.secondIndex = secondIndex;
            this.cost = cost;
        }
    }

    public static class Node implements Comparable<Node> {
        List<Integer> list;
        int cost;

        public Node(List<Integer> list, int cost) {
            this.list = list;
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
        Map<String, Integer> visitedMap = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(numList, 0));

        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (visitedMap.get(node.list.toString()) != null) { continue; }
            visitedMap.put(node.list.toString(), 1);

            if (isSorted(node.list)) {
                System.out.println(node.cost);
                return;
            }

            for (Command command : commandList) {
                List<Integer> swappedList = swapList(node.list, command);
                if (visitedMap.get(swappedList.toString()) != null) { continue; }

                pq.add(new Node(swappedList, node.cost + command.cost));
            }
        }

        System.out.println(-1);
    }

    public static boolean isSorted(List<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i) > list.get(i + 1)) { return false; }
        }

        return true;
    }

    public static List<Integer> swapList(List<Integer> list, Command command) {
        List<Integer> swappedList = new ArrayList<>(list);
        swappedList.set(command.firstIndex, list.get(command.secondIndex));
        swappedList.set(command.secondIndex, list.get(command.firstIndex));

        return swappedList;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        numList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            numList.add(Integer.parseInt(st.nextToken()));
        }


        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());


        commandList = new Command[M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int firstIndex = Integer.parseInt(st.nextToken()) - 1;
            int secondIndex = Integer.parseInt(st.nextToken()) - 1;
            int cost = Integer.parseInt(st.nextToken());

            commandList[i] = new Command(firstIndex, secondIndex, cost);
        }
    }
}