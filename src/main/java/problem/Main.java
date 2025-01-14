package problem;

import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[] numList;
    static Map<Integer, Integer> closestBuildingMap = new HashMap<>();
    public static void main(String[] args) throws Exception {
        init();
        int[] leftToRightList = new int[N];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = N - 1; i >= 0; i--) {
            int value = 0;
            int curNum = numList[i];

            while (!stack.isEmpty()) {
                int tailNum = stack.pollLast();
                if (tailNum > curNum) {
                    stack.addLast(tailNum);
                    break;
                }

                value += 1;
            }

            leftToRightList[i] = stack.size();
            stack.addLast(curNum);
        }

        int[] rightToLeftList = new int[N];
        stack = new ArrayDeque<>();
        for (int i = 0; i < N; i++) {
            int value = 0;
            int curNum = numList[i];

            while (!stack.isEmpty()) {
                int tailNum = stack.pollLast();
                if (tailNum > curNum) {
                    stack.addLast(tailNum);
                    break;
                }

                value += 1;
            }

            rightToLeftList[i] = stack.size();
            stack.addLast(curNum);
        }


        for (int i = 0; i < N; i++) {
            int value = leftToRightList[i] + rightToLeftList[i];
            if (value == 0) {
                System.out.println(0);
            }


        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        numList = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            numList[i] = Integer.parseInt(st.nextToken());
        }
    }
}