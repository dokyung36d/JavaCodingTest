package problem;

import java.util.*;
import java.io.*;



public class Main {
    static int N, K, W;
    static int[] costList;
    static Map<Integer, List<Integer>> graphMap;
    static int[] numPointedList;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static class Node implements Comparable<Node> {
        int num;
        int cost;

        public Node(int num, int cost) {
            this.num = num;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.cost, anotherNode.cost);
        }
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int T = Integer.parseInt(st.nextToken());


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < T; i++) {
            init();

            sb.append(solution() + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int solution() {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            if (numPointedList[i] != 0) { continue; }

            pq.add(new Node(i, costList[i]));
        }


        while (!pq.isEmpty()) {
            Node node = pq.poll();
            if (node.num == W) {
                return node.cost;
            }

            for (int nearNum : graphMap.get(node.num)) {
                numPointedList[nearNum] -= 1;
                if (numPointedList[nearNum] == 0) {
                    pq.add(new Node(nearNum, node.cost + costList[nearNum]));
                }
            }
        }

        return 0;
    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        costList = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            costList[i] = Integer.parseInt(st.nextToken());
        }


        graphMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }
        numPointedList = new int[N];

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            int from = Integer.parseInt(st.nextToken()) - 1;
            int to = Integer.parseInt(st.nextToken()) - 1;

            graphMap.get(from).add(to);
            numPointedList[to] += 1;
        }


        st = new StringTokenizer(br.readLine());
        W = Integer.parseInt(st.nextToken()) - 1;
    }
}
