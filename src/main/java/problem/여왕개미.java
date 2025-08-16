package problem;

import java.util.*;
import java.io.*;

public class 여왕개미 {
    static int Q, N;
    static List<House> houseList;
    static int[][] commandList;
    static Map<Integer, Integer> deleteMap;
    static int uniqueNum;

    public static class House implements Comparable<House> {
        int uniqueNum;
        int x;

        public House(int uniqueNum, int x) {
            this.uniqueNum = uniqueNum;
            this.x = x;
        }

        @Override
        public int compareTo(House anotherHouse) {
            return Integer.compare(this.x, anotherHouse.x);
        }

    }

    public static class ReverseHouse implements Comparable<ReverseHouse> {
        int uniqueNum;
        int x;

        public ReverseHouse(int uniqueNum, int x) {
            this.uniqueNum = uniqueNum;
            this.x = x;
        }

        @Override
        public int compareTo(ReverseHouse anotherReverseHouse) {
            return Integer.compare(-this.x, -anotherReverseHouse.x);
        }

    }


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        StringBuilder sb = new StringBuilder();
        for (int[] command : commandList) {
            if (command[0] == 200) {
                houseList.add(new House(uniqueNum, command[1]));
                uniqueNum += 1;
                continue;
            }

            if (command[0] == 300) {
                deleteMap.put(command[1], 1);
                continue;
            }

            sortHouseList();
            int numSearch = command[1];

            int start = 0;
            int end = houseList.get(houseList.size() - 1).x - houseList.get(0).x;
            int minGap = end;
            while (start <= end) {
                int gap = start + (end - start) / 2;
                boolean result = search(numSearch, gap);

                if (result == true) {
                    minGap = Math.min(minGap, gap);
                    end = gap - 1;
                }
                else {
                    start = gap + 1;
                }
            }

            sb.append(minGap +"\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void sortHouseList() {
        List<House> sortedHouseList = new ArrayList<>();
        for (int i = 0; i < houseList.size(); i++) {
            House house = houseList.get(i);
            if (deleteMap.get(house.uniqueNum) != null) { continue; }

            sortedHouseList.add(house);
        }

        Collections.sort(sortedHouseList);
        houseList = sortedHouseList;
    }


    public static boolean search(int numSearch, int gap) {
        House prevHouse = houseList.get(0);
        numSearch -= 1;
        for (int i = 1; i < houseList.size(); i++) {
            House curHouse = houseList.get(i);
            if (curHouse.x - prevHouse.x > gap) {
                prevHouse = curHouse;
                numSearch -= 1;
            }

            if (numSearch < 0) {
                return false;
            }
        }


        return true;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        Q = Integer.parseInt(st.nextToken());

        houseList = new ArrayList<>();
        st = new StringTokenizer(br.readLine());
        int commandNum = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        uniqueNum = 1;
        for (int i = 0; i < N; i++) {
            int x = Integer.parseInt(st.nextToken());
            houseList.add(new House(uniqueNum, x));
            uniqueNum += 1;
        }

        commandList = new int[Q - 1][2];
        for (int i = 0; i < Q - 1; i++) {
            st = new StringTokenizer(br.readLine());

            int command = Integer.parseInt(st.nextToken());
            int num = Integer.parseInt(st.nextToken());
            commandList[i][0] = command;
            commandList[i][1] = num;
        }

        deleteMap = new HashMap<>();

    }

}