package problem;

import java.util.*;
import java.io.*;


public class ACMCraft {
    static int N, K, W;
    static long[] costList, reqyuredCost;
    static int[] unstartableList;
    static Map<Integer, List<Integer>> childMap;
    static Map<Integer, Integer> numRestParentsMap;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));


    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int T = Integer.parseInt(st.nextToken());

        for (int i = 0; i < T; i++) {
            init();
            solution();

        }
    }

    public static class Node implements Comparable<Node> {
        int curBuilding;
        long cost;

        public Node(int curBuilding, long cost) {
            this.curBuilding = curBuilding;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Long.compare(this.cost, anotherNode.cost);
        }
    }

    public static void solution() throws Exception {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            if (unstartableList[i] == 0) {
                queue.add(new Node(i, 0));
            }
        }


        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node.curBuilding == W) {
                System.out.println(node.cost + costList[W]);
                return;
            }

            List<Integer> childs = childMap.get(node.curBuilding);
            for (int child : childs) {
                int numRestParents = numRestParentsMap.get(child) - 1;
                if (numRestParents == 0) {
                    queue.add(new Node(child, Math.max(node.cost + costList[node.curBuilding], reqyuredCost[child])));
                }

                else {
                    reqyuredCost[child] = Math.max(node.cost + costList[node.curBuilding], reqyuredCost[child]);
                    numRestParentsMap.put(child, numRestParents);
                }
            }
        }

    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        costList = new long[N];
        unstartableList = new int[N];
        reqyuredCost = new long[N];

        childMap = new HashMap<>();
        numRestParentsMap = new HashMap<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            costList[i] = Integer.parseInt(st.nextToken());
            childMap.put(i, new ArrayList<>());
        }

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int parent = Integer.parseInt(st.nextToken()) - 1;
            int child = Integer.parseInt(st.nextToken()) - 1;

            childMap.get(parent).add(child);

            int numParents = numRestParentsMap.getOrDefault(child, 0);
            numRestParentsMap.put(child, numParents + 1);

            unstartableList[child] = 1;
        }

        st = new StringTokenizer(br.readLine());
        W = Integer.parseInt(st.nextToken()) - 1;

    }
}
