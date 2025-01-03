package problem;

import java.util.*;
import java.io.*;

public class 여행가자 {
    static int N;
    static int M;
    static Map<Integer, City> nodeMap = new HashMap<>();
    static Map<List<Integer>, Integer> visitableMap = new HashMap<>();
    static int[] travelPlan;

    public static class City {
        int unqiueNum;
        Map<Integer, Integer> neighbors = new HashMap<>();

        public City(int uniqueNum) {
            this.unqiueNum = uniqueNum;
        }
    }

    public static void main(String[] args) throws IOException {
        init();
        int flag = 0;

        for (int i = 0; i < M - 1; i++) {
            int startCity = travelPlan[i];
            int destCity = travelPlan[i + 1];

            if (startCity == destCity) {
                continue;
            }

            if (visitableMap.get(Arrays.asList(startCity, destCity)) != null) {
                continue;
            }
            if (dfs(startCity, destCity)) {
                continue;
            }

            flag = 1;
            break;
        }

        if (flag == 0) {
            System.out.println("YES");
        }
        else {
            System.out.println("NO");
        }


    }

    public static boolean dfs(int startCity, int destCity) {
        Deque<Integer> queue = new ArrayDeque<>();
        queue.addLast(startCity);

        Map<Integer, Integer> visited = new HashMap<>();
        visited.put(startCity, 1);

        while (!queue.isEmpty()) {
            int curCity = queue.pollLast();
            Set<Integer> neighborKeys = nodeMap.get(curCity).neighbors.keySet();

            for (Integer neighborKey : neighborKeys) {
                if (visited.get(neighborKey) != null) {
                    continue;
                }
                visited.put(neighborKey, 1);

                visitableMap.put(Arrays.asList(startCity, neighborKey), 1);
                visitableMap.put(Arrays.asList(neighborKey, startCity), 1);

                if (neighborKey == destCity) {
                    return true;
                }

                queue.addLast(neighborKey);
            }
        }

        return false;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        for (int i = 0; i < N; i++) {
            nodeMap.put(i, new City(i));
        }

        st = new StringTokenizer(br.readLine());
        M = Integer.parseInt(st.nextToken());
        travelPlan = new int[M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                int whetherNeighbor = Integer.parseInt(st.nextToken());
                if (whetherNeighbor == 0) {
                    continue;
                }
                nodeMap.get(i).neighbors.put(j, 1);
            }
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            int travelCity = Integer.parseInt(st.nextToken());
            travelPlan[i] = travelCity - 1;
        }

    }
}