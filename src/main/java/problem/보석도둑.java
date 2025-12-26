package problem;

import java.util.*;
import java.io.*;

public class 보석도둑 {
    static int N, K;
    static PriorityQueue<Gem> gemPQ;
    static PriorityQueue<PolledGem> polledGemPQ;
    static int[] bagSizeList;

    public static class Gem implements Comparable<Gem> {
        int m;
        int v;

        public Gem(int m, int v) {
            this.m = m;
            this.v = v;
        }

        @Override
        public int compareTo(Gem anotherGem) {
            return Integer.compare(this.m, anotherGem.m);
        }
    }

    public static class PolledGem implements Comparable<PolledGem> {
        int m;
        int v;

        public PolledGem(int m, int v) {
            this.m = m;
            this.v = v;
        }

        @Override
        public int compareTo(PolledGem anotherPolledGem) {
            return Integer.compare(-this.v, -anotherPolledGem.v);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        long answer = 0;
        for (int i = 0; i < K; i++) {
            int bagSize = bagSizeList[i];

            while (true) {
                if (gemPQ.isEmpty()) { break; }

                Gem gem = gemPQ.poll();
                if (gem.m > bagSize) {
                    gemPQ.add(gem);
                    break;
                }

                polledGemPQ.add(new PolledGem(gem.m, gem.v));
            }

            if (polledGemPQ.isEmpty()) { continue; }

            PolledGem polledGem = polledGemPQ.poll();
            answer += (long) polledGem.v;
        }

        System.out.println(answer);
    }

    public static void init() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        gemPQ = new PriorityQueue<>();
        polledGemPQ = new PriorityQueue<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int m = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());

            Gem gem = new Gem(m, v);
            gemPQ.add(gem);
        }

        bagSizeList = new int[K];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            bagSizeList[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(bagSizeList);
    }
}
