import java.util.*;
import java.io.*;

public class 앱 {
    static int N, M;
    static List<Integer> byteList = new ArrayList<>();
    static List<Integer> costList = new ArrayList<>();
    static int maxCost;

    public static class Node implements Comparable<problem.Main.Node> {
        int byteRequired;
        int cost;

        public Node(int byteRequired, int cost) {
            this.byteRequired = byteRequired;
            this.cost = cost;
        }

        public problem.Main.Node addNode(problem.Main.Node anotherNode) {
            return new problem.Main.Node(this.byteRequired + anotherNode.byteRequired, this.cost + anotherNode.cost);
        }

        @Override
        public int compareTo(problem.Main.Node anotherNode) {
            return Integer.compare(this.byteRequired, anotherNode.byteRequired);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        if (M <= 0) {
            System.out.println(0);
            return;
        }

        int[][] dpMatrix = new int[byteList.size() + 1][maxCost + 1];
        for (int i = 1; i < byteList.size() + 1; i++) {
            int byteRequired = byteList.get(i - 1);
            int cost = costList.get(i - 1);

            for (int j = 1; j < maxCost + 1; j++) {
                if (j < cost) {
                    dpMatrix[i][j] = dpMatrix[i - 1][j];
                    continue;
                }
                dpMatrix[i][j] = Math.max(dpMatrix[i - 1][j], dpMatrix[i - 1][j - cost] + byteRequired);
            }
        }


        for (int i = 0; i < maxCost + 1; i++) {
            if (dpMatrix[byteList.size()][i] >= M) {
                System.out.println(i);
                return;
            }
        }
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N;  i++) {
            byteList.add(Integer.parseInt(st.nextToken()));
        }

        maxCost = 0;
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            costList.add(Integer.parseInt(st.nextToken()));
            maxCost += costList.get(costList.size() - 1);
        }

        for (int i = N - 1; i >= 0; i--) {
            if (costList.get(i) == 0) {
                M -= byteList.get(i);

                byteList.remove(i);
                costList.remove(i);
            }
        }

    }
}