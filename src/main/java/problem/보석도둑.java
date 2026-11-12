package problem;

import java.io.*;
import java.util.*;

public class 보석도둑 {
    static int N, K;
    static PriorityQueue<Gem> gemPQ;
    static PriorityQueue<PolledGem> polledGemPQ;
    static int[] bagList;

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

        public PolledGem(Gem gem) {
            this.m = gem.m;
            this.v = gem.v;
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
            int bagSize = bagList[i];

            while (!gemPQ.isEmpty()) {
                Gem gem = gemPQ.poll();
                if (gem.m > bagSize) {
                    gemPQ.add(gem);
                    break;
                }

                polledGemPQ.add(new PolledGem(gem));
            }

            if (!polledGemPQ.isEmpty()) {
                PolledGem polledGem = polledGemPQ.poll();
                answer += (long) polledGem.v;
            }
        }
        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());


        gemPQ = new PriorityQueue<>();
        polledGemPQ = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int m = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            gemPQ.add(new Gem(m, k));
        }

        bagList = new int[K];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            bagList[i] = Integer.parseInt(st.nextToken());
        }
        Arrays.sort(bagList);


    }
}