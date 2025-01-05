package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class 가장큰증가하는부분수열2 {
    static int N;
    static int[] numList;
    static Map<Integer, Deque<Integer>> dpStackMap;

    public static void main(String[] args) throws IOException {
        init();
        List<Integer> dpList = new ArrayList<>();
        dpList.add(numList[0]);
        dpStackMap.put(1, new ArrayDeque<>());
        dpStackMap.get(1).addLast(0);

        for (int i = 1; i < N; i++) {
            int curNum = numList[i];
            int index = Collections.binarySearch(dpList, curNum);
            if (index >= 0) {
                continue;
            }

            if (curNum > dpList.get(dpList.size() - 1)) {
                dpList.add(curNum);
                dpStackMap.put(dpList.size(), new ArrayDeque<>());
                dpStackMap.get(dpList.size()).addLast(i);
            }
            else {
                int insertIndex = -index - 1;
                dpList.set(insertIndex, numList[i]);
                dpStackMap.get(insertIndex + 1).addLast(i);
            }
        }

        System.out.println(dpList.size());

        if (dpList.size() == 1) {
            System.out.println(dpList.get(0));
            return;
        }

        List<Integer> answerList = new ArrayList<>();
        int curIndex = dpStackMap.get(dpList.size()).pollLast();
        answerList.add(numList[curIndex]);
        for (int i = dpList.size() - 1; i >= 1; i--) {
            while (true) {
                int index = dpStackMap.get(i).pollLast();
                if (index > curIndex) {
                    continue;
                }

                answerList.add(numList[index]);
                curIndex = index;
                break;
            }
        }

        for (int i = answerList.size() - 1; i >= 0; i--) {
            System.out.print(answerList.get(i));
            if (i != 0) {
                System.out.print(" ");
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

        dpStackMap = new HashMap<>();
    }
}