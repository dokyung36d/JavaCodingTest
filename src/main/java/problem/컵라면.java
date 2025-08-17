package problem;

import java.util.*;
import java.io.*;

public class 컵라면 {
    static int N;
    static PriorityQueue<CupNoodle> pq;
    static int[] parentList;

    public static class CupNoodle implements Comparable<CupNoodle> {
        int deadLine;
        int numNoodle;

        public CupNoodle(int deadLine, int numNoodle) {
            this.deadLine = deadLine;
            this.numNoodle = numNoodle;
        }

        @Override
        public int compareTo(CupNoodle anotherCupNoodle) {
            return Integer.compare(-this.numNoodle, -anotherCupNoodle.numNoodle);
        }
    }


    public static void main(String[] args)  throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int answer = 0;

        while (!pq.isEmpty()) {
            CupNoodle cupNoodle = pq.poll();

            int parent = findParent(cupNoodle.deadLine);
            if (parent == 0) { continue; }
            union(parent, parent - 1);
            answer += cupNoodle.numNoodle;
        }

        System.out.println(answer);
    }

    public static int findParent(int num) {
        if (num == parentList[num]) { return num; }
        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent == num2Parent) { return; }
        parentList[Math.max(num1Parent, num2Parent)] = Math.min(num1Parent, num2Parent);
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        pq = new PriorityQueue<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int deadLine = Integer.parseInt(st.nextToken());
            int numNoodle = Integer.parseInt(st.nextToken());
            pq.add(new CupNoodle(deadLine, numNoodle));
        }


        parentList = new int[N + 1];
        for (int i = 0; i < N + 1; i++) {
            parentList[i] = i;
        }
    }

}