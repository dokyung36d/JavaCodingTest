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
            int curNum = numList[i];
            int index = Collections.binarySearch(dpList, curNum);
            if (index >= 0) { continue; }

            index = - index - 1;
            if (index ==  dpList.size()) {
                dpList.add(curNum);
                continue;
            }
            dpList.set(index, curNum);
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