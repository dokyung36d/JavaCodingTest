package problem;

import java.util.*;
import java.io.*;



public class 카드게임 {
    static int N, M, K;
    static int[] minsuCardList, chulsuCardList;
    static int[] parentList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        Arrays.sort(minsuCardList);


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < K; i++) {
            int cardNum = chulsuCardList[i] + 1;

            int index = Arrays.binarySearch(minsuCardList, cardNum);
            if (index >= 0) {

                int minsuCardNum = minsuCardList[findParent(index)];
                sb.append(minsuCardNum + "\n");

                union(findParent(index), findParent(index) + 1);
            }

            else {
                index = - index - 1;
                int minsuCardNum = minsuCardList[findParent(index)];
                sb.append(minsuCardNum + "\n");

                union(findParent(index), findParent(index) + 1);
            }
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int findParent(int num) {
        if (num == parentList[num]) {
            return num;
        }

        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent != num2Parent) {
            parentList[Math.min(num1Parent, num2Parent)] = Math.max(num1Parent, num2Parent);
        }
    }

    public static void init() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        minsuCardList = new int[M];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < M; i++) {
            minsuCardList[i] = Integer.parseInt(st.nextToken());
        }

        chulsuCardList = new int[K];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < K; i++) {
            chulsuCardList[i] = Integer.parseInt(st.nextToken());
        }


        parentList = new int[M + 1];
        for (int i = 0; i < M + 1; i++) {
            parentList[i] = i;
        }
    }
}