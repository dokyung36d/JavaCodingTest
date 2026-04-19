package problem;

import java.util.*;
import java.io.*;



public class 가장큰증가하는부분수열2 {
    static int N;
    static int[] numList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        List<Integer> dpList = new ArrayList<>();
        dpList.add(numList[0]);

        for (int i = 1; i < N; i++) {
            int insertIndex = Collections.binarySearch(dpList, numList[i]);
            if (insertIndex >= 0) { continue; }

            insertIndex = - insertIndex - 1;
            if (insertIndex == dpList.size()) {
                dpList.add(numList[i]);
            }
            else {
                dpList.set(insertIndex, numList[i]);
            }
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
    }
}