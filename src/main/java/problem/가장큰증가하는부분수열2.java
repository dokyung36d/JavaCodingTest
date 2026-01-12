package problem;

import java.util.*;
import java.io.*;



public class 가장큰증가하는부분수열2 {
    static int N;
    static int[] numList;
    static List<Integer> dpList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        dpList.add(numList[0]);

        for (int i = 1; i < N; i++) {
            int num = numList[i];

            int index = Collections.binarySearch(dpList, num);
            if (index >= 0) { continue; }

            int insertIndex = - index - 1;
            if (insertIndex == dpList.size()) {
                dpList.add(num);
                continue;
            }

            dpList.set(insertIndex, num);
        }

        System.out.println(dpList.size());
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

        dpList = new ArrayList<>();
    }
}