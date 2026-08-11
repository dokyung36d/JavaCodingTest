package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N, K;
    static PriorityQueue<Gem> gemPq;
    static PriorityQueue<PolledGem> polledGemPq;
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
        long answer = (long) 0;

        for (int bag : bagList) {
            while (!gemPq.isEmpty()) {
                Gem gem = gemPq.poll();

                if (gem.m > bag) {
                    gemPq.add(gem);
                    break;
                }

                polledGemPq.add(new PolledGem(gem.m, gem.v));
            }


            if (!polledGemPq.isEmpty()) {
                PolledGem polledGem = polledGemPq.poll();

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

        gemPq = new PriorityQueue<>();
        polledGemPq = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int m = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());


            gemPq.add(new Gem(m, v));
        }


        bagList = new int[K];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            bagList[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(bagList);
    }
}