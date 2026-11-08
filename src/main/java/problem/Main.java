package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static PriorityQueue<Node> nodePQ;

    public static class Node implements Comparable<Node> {
        int pk;
        int startTime;
        int endTime;

        public Node(int pk, int startTime, int endTime) {
            this.pk = pk;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.startTime, anotherNode.startTime);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        PriorityQueue<Integer> classRoomPQ = new PriorityQueue<>();
        classRoomPQ.add(0);


        while (!nodePQ.isEmpty()) {
            Node node = nodePQ.poll();

            int lastClassRoomEndTime = classRoomPQ.poll();

            if (node.startTime < lastClassRoomEndTime) {
                classRoomPQ.add(lastClassRoomEndTime);
                classRoomPQ.add(node.endTime); //강의실 추가배정
            }
            else {
                classRoomPQ.add(node.endTime);
            }

        }


        System.out.println(classRoomPQ.size());
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());


        nodePQ = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int pk = Integer.parseInt(st.nextToken());
            int startTime = Integer.parseInt(st.nextToken());
            int endTime = Integer.parseInt(st.nextToken());

            nodePQ.add(new Node(pk, startTime, endTime));
        }
    }
}
