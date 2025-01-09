package problem;

import java.io.*;
import java.util.*;

class 보석도둑
{
    public static int N;
    public static int K;
    public static Queue<Gift> giftQueue;
    public static int[] bagList;
    public static Queue<AvailableGift> giftAvailableQueue;

    public static class Gift implements Comparable<Gift> {
        int M;
        int V;

        public Gift(int M, int V) {
            this.M = M;
            this.V = V;
        }

        @Override
        public int compareTo(Gift anotherGift) {
            return Integer.compare(this.M, anotherGift.M);
        }
    }

    public static class AvailableGift implements Comparable<AvailableGift> {
        int M;
        int V;

        public AvailableGift(Gift gift) {
            this.M = gift.M;
            this.V = gift.V;
        }

        @Override
        public int compareTo(AvailableGift anotherAvailableGift) {
            return Integer.compare(-this.V, -anotherAvailableGift.V);
        }
    }

    public static void main(String args[]) throws IOException
    {
        init();
        long answer = 0;

        for (int i = 0; i < K; i++) {
            int bagSize = bagList[i];

            while (!giftQueue.isEmpty()) {
                Gift polledGift = giftQueue.poll();
                if (polledGift.M > bagSize) {
                    giftQueue.add(polledGift);
                    break;
                }

                giftAvailableQueue.add(new AvailableGift(polledGift));
            }

            if (giftAvailableQueue.isEmpty()) {
                continue;
            }


            AvailableGift polledAvailableGift = giftAvailableQueue.poll();
            answer += (long) polledAvailableGift.V;
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        giftQueue = new PriorityQueue<>();
        bagList = new int[K];


        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int giftM = Integer.parseInt(st.nextToken());
            int giftV = Integer.parseInt(st.nextToken());

            giftQueue.add(new Gift(giftM, giftV));
        }

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            bagList[i] = Integer.parseInt(st.nextToken());
        }
        Arrays.sort(bagList);

        giftAvailableQueue = new PriorityQueue<>();
    }
}
