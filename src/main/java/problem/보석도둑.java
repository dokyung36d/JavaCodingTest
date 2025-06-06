package problem;

import java.util.*;
import java.io.*;


public class 보석도둑 {
    static int N, K;
    static PriorityQueue<Gem> gemPq;
    static PriorityQueue<PolledGem> polledGemPq;
    static int[] bagList;

    public static class Gem implements Comparable<Gem> {
        int weight;
        int value;

        public Gem(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }

        @Override
        public int compareTo(Gem anotherGem) {
            return Integer.compare(this.weight, anotherGem.weight);
        }
    }

    public static class PolledGem implements Comparable<PolledGem> {
        int weight;
        int value;

        public PolledGem(int weight, int value) {
            this.weight = weight;
            this.value = value;
        }

        @Override
        public int compareTo(PolledGem anotherPolledGem) {
            return Integer.compare(-this.value, -anotherPolledGem.value);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        long answer = 0;

        for (int bag : bagList) {
            while (!gemPq.isEmpty()) {
                Gem gem = gemPq.poll();
                if (gem.weight > bag) {
                    gemPq.add(gem);
                    break;
                }

                polledGemPq.add(new PolledGem(gem.weight, gem.value));
            }

            if (polledGemPq.isEmpty()) {
                continue;
            }

            answer += polledGemPq.poll().value;
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

            int weight = Integer.parseInt(st.nextToken());
            int value = Integer.parseInt(st.nextToken());

            gemPq.add(new Gem(weight, value));
        }


        bagList = new int[K];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            bagList[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(bagList);
    }
}
