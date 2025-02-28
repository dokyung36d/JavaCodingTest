package problem;

import java.util.*;
import java.io.*;


public class WateringTheFields {
    static int N, C;
    static Pos[] posList;
    static int[] parentList;

    public static class Pos {
        int uniqueNum;
        int row;
        int col;

        public Pos(int uniqueNum, int row, int col) {
            this.uniqueNum = uniqueNum;
            this.row = row;
            this.col = col;
        }

        public int calcDistance(Pos anotherPos) {
            return (int) (Math.pow(this.row - anotherPos.row, 2) + Math.pow(this.col - anotherPos.col, 2));
        }
    }

    public static class Node implements Comparable<Node> {
        Pos pos1;
        Pos pos2;
        int distance;

        public Node(Pos pos1, Pos pos2) {
            this.pos1 = pos1;
            this.pos2 = pos2;
            this.distance = pos1.calcDistance(pos2);
        }

        @Override
        public int compareTo(Node anotherNode) {
            return Integer.compare(this.distance, anotherNode.distance);
        }

    }

    public static void main(String[] args) throws Exception {
        init();
        PriorityQueue<Node> queue = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                Node node = new Node(posList[i], posList[j]);
                if (node.distance < C) { continue; }

                queue.add(node);
            }
        }

        int answer = 0;
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            int pos1Parent = findParent(node.pos1.uniqueNum);
            int pos2Parent = findParent(node.pos2.uniqueNum);

            if (pos1Parent != pos2Parent) {
                union(node.pos1.uniqueNum, node.pos2.uniqueNum);
                answer += node.distance;
            }
        }

        int flag = 0;
        for (int i = 0; i < N; i++) {
            findParent(i);
            if (parentList[i] != 0) {
                flag = 1;
                break;
            }
        }

        if (flag == 1) {
            System.out.println(-1);
        }
        else{
            System.out.println(answer);
        }

    }

    public static int findParent(int num) {
        if (parentList[num] == num) {
            return num;
        }

        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent != num2Parent) {
            parentList[Math.max(num1Parent, num2Parent)] = Math.min(num1Parent, num2Parent);
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());


        parentList = new int[N];
        for (int i = 0; i < N; i++) {
            parentList[i] = i;
        }
        posList = new Pos[N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int row = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken());

            posList[i] = new Pos(i, row, col);
        }

    }

}