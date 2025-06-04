package problem;

import java.util.*;
import java.io.*;


public class ACMCraft {
    static int T, N, K;
    static int[] timeList;
    static int destBuilding;
    static Map<Integer, List<Integer>> graphMap;
    static int[] numPrevJobList;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static class Node implements Comparable<Node> {
        int uniqueNum;
        int endTime;

        public Node(int uniqueNum, int endTime) {
            this.uniqueNum = uniqueNum;
            this.endTime = endTime;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.endTime, anotherNode.endTime);
        }
    }

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        T = Integer.parseInt(st.nextToken());

        for (int i = 0; i < T; i++) {
            init();
            solution();
        }
    }

    public static void solution() {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            if (numPrevJobList[i] != 0) { continue; }

            if (i == destBuilding) {
                System.out.println(timeList[i]);
                return;
            }

            pq.add(new Node(i, timeList[i]));
        }

        int totalTime = 0;
        while (!pq.isEmpty()) {
            Node node = pq.poll();

            totalTime = Math.max(totalTime, node.endTime);
            for (int nearNode : graphMap.get(node.uniqueNum)) {
                numPrevJobList[nearNode] -= 1;

                if (numPrevJobList[nearNode] == 0) {
                    pq.add(new Node(nearNode, node.endTime + timeList[nearNode]));

                    if (nearNode == destBuilding) {
                        System.out.println(node.endTime + timeList[nearNode]);
                        return;
                    }
                }
            }
        }

        System.out.println(totalTime);
    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        timeList = new int[N];
        graphMap = new HashMap<>();
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            timeList[i] = Integer.parseInt(st.nextToken());

            graphMap.put(i, new ArrayList<>());
        }

        numPrevJobList = new int[N];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            int from = Integer.parseInt(st.nextToken()) - 1;
            int to = Integer.parseInt(st.nextToken()) - 1;

            graphMap.get(from).add(to);
            numPrevJobList[to] += 1;
        }

        st = new StringTokenizer(br.readLine());
        destBuilding = Integer.parseInt(st.nextToken()) - 1;
    }
}
