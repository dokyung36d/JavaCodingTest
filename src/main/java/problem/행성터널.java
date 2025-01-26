package problem;

import java.util.*;
import java.io.*;


public class 행성터널 {
    static int N;
    static Pos[] posList;
    static int[] parentList;

    public static class Pos {
        int x;
        int y;
        int z;
        int index;

        public Pos(int x, int y, int z, int index) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.index = index;
        }
    }

    public static class GapNode implements Comparable<GapNode> {
        int gap;
        int vertex1;
        int vertex2;

        public GapNode(int gap, int vertex1, int vertex2) {
            this.gap = gap;
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
        }

        @Override
        public int compareTo(GapNode anotherGapNode) {
            return Integer.compare(this.gap, anotherGapNode.gap);
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        List<GapNode> queue = new ArrayList<>();
        Arrays.sort(posList, (pos1, pos2) -> pos1.x - pos2.x);
        for (int i = 0; i < N - 1; i++) {
            queue.add(new GapNode(Math.abs(posList[i + 1].x - posList[i].x), posList[i + 1].index, posList[i].index));
        }

        Arrays.sort(posList, (pos1, pos2) -> pos1.y - pos2.y);
        for (int i = 0; i < N - 1; i++) {
            queue.add(new GapNode(Math.abs(posList[i + 1].y - posList[i].y), posList[i + 1].index, posList[i].index));
        }

        Arrays.sort(posList, (pos1, pos2) -> pos1.z - pos2.z);
        for (int i = 0; i < N - 1; i++) {
            queue.add(new GapNode(Math.abs(posList[i + 1].z - posList[i].z), posList[i + 1].index, posList[i].index));
        }


        int answer = 0;
        int[] visited = new int[N];

        Collections.sort(queue);
        for (int i = 0; i < queue.size(); i++) {
            GapNode gapNode = queue.get(i);

            if (findParent(gapNode.vertex1) != findParent(gapNode.vertex2)) {
                union(gapNode.vertex1, gapNode.vertex2);
                answer += gapNode.gap;
            }
        }

        System.out.println(answer);
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
            parentList[num1Parent] = num2Parent;
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        posList = new Pos[N];

        parentList = new int[N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int z = Integer.parseInt(st.nextToken());
            posList[i] = new Pos(x, y, z, i);

            parentList[i] = i;
        }

    }



}