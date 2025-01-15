package problem;

import java.io.*;
import java.util.*;

public class 탑보기 {
    static int N;
    static Building[] buildingList;
    static Map<Integer, Integer> closestBuildingMap = new HashMap<>();
    static int[] closestBuildingList;

    public static class Building implements Comparable<Building> {
        int height;
        int pos;
        Building closestBuilding;

        public Building(int height, int pos) {
            this.height = height;
            this.pos = pos;
        }

        @Override
        public int compareTo(Building anotherBuilding) {
            if (this.height == anotherBuilding.height) {
                return Integer.compare(this.pos,  anotherBuilding.pos);
            }

            return Integer.compare(this.height, anotherBuilding.height);
        }

        public void applyClosestBuilding(Building newBuilding) {
            if (closestBuilding == null) {
                this.closestBuilding = newBuilding;
                return;
            }
            int baseGap = Math.abs(this.pos - closestBuilding.pos);
            int newGap = Math.abs(this.pos - newBuilding.pos);

            if (newGap < baseGap) {
                this.closestBuilding = newBuilding;
                return;
            }

            if (newGap == baseGap) {
                if (newBuilding.pos < this.closestBuilding.pos) {
                    this.closestBuilding = newBuilding;
                }
            }

        }
    }


    public static void main(String[] args) throws Exception {
        init();
        int[] leftToRightList = new int[N];
        Deque<Building> stack = new ArrayDeque<>();
        for (int i = N - 1; i >= 0; i--) {
            int value = 0;
            Building curBuilding = buildingList[i];

            while (!stack.isEmpty()) {
                Building tailBuilding = stack.pollLast();
                if (tailBuilding.height > curBuilding.height) {
                    stack.addLast(tailBuilding);

                    curBuilding.applyClosestBuilding(tailBuilding);
                    break;
                }

                value += 1;
            }

            leftToRightList[i] = stack.size();
            stack.addLast(curBuilding);
        }

        int[] rightToLeftList = new int[N];
        stack = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
            int value = 0;
            Building curBuilding = buildingList[i];

            while (!stack.isEmpty()) {
                Building tailBuilding = stack.pollLast();
                if (tailBuilding.height > curBuilding.height) {
                    stack.addLast(tailBuilding);

                    curBuilding.applyClosestBuilding(tailBuilding);
                    break;
                }

                value += 1;
            }

            rightToLeftList[i] = stack.size();
            stack.addLast(curBuilding);
        }


        for (int i = 0; i < N; i++) {
            int value = leftToRightList[i] + rightToLeftList[i];
            if (value == 0) {
                System.out.println(0);
                continue;
            }
            System.out.print(value);
            System.out.print(" ");
            System.out.println(buildingList[i].closestBuilding.pos + 1);




        }
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        buildingList = new Building[N];
        closestBuildingList = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int height = Integer.parseInt(st.nextToken());
            buildingList[i] = new Building(height, i);
            closestBuildingList[i] = Integer.MAX_VALUE;
        }
    }
}