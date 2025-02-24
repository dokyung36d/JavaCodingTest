package problem;

import java.util.*;
import java.io.*;


public class 전깃줄 {
    static int N;
    static Edge[] edgeList;

    public static class Edge implements Comparable<Edge> {
        int from;
        int to;

        public Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public int compareTo(Edge anotherEdge) {
            if (this.from == anotherEdge.from) {
                return Integer.compare(this.to, anotherEdge.to);
            }

            return Integer.compare(this.from, anotherEdge.from);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        Arrays.sort(edgeList);
        List<Integer> dpList = new ArrayList<>();
        dpList.add(0);
        List<Deque<Edge>> dpStackList = new ArrayList<>();
        dpStackList.add(new ArrayDeque<>());

        for (int i = 0; i < N; i++) {
            Edge edge = edgeList[i];
            int insertIndex = Collections.binarySearch(dpList, edge.to);
            if (insertIndex >= 0) { continue; }

            insertIndex = - insertIndex - 1;
            if (insertIndex == dpList.size()) {
                dpList.add(edge.to);
                dpStackList.add(new ArrayDeque<>());
                dpStackList.get(insertIndex).addLast(edge);
            }
            else {
                dpList.set(insertIndex, edge.to);
                dpStackList.get(insertIndex).addLast(edge);
            }

        }

        StringBuilder sb = new StringBuilder();
        int numLine = dpList.size() - 1;
        int numDeleteLine = N - numLine;
        sb.append(numDeleteLine + "\n");

        Map<Integer, Integer> startMap = new HashMap<>();

        int prevLineFrom = dpStackList.get(numLine).getLast().from;
        startMap.put(prevLineFrom, 1);
        for (int i = numLine - 1; i > 0; i--) {
            Deque<Edge> stack = dpStackList.get(i);

            while (!stack.isEmpty()) {
                Edge edge = stack.pollLast();
                if (edge.from < prevLineFrom) {
                    prevLineFrom = edge.from;
                    startMap.put(edge.from, 1);
                    break;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            Edge edge = edgeList[i];
            if (startMap.get(edge.from) != null) { continue; }
            sb.append(edge.from + "\n");
        }
        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        edgeList = new Edge[N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());

            edgeList[i] = new Edge(from, to);
        }
    }
}
