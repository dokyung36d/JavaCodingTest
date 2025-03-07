package problem;

import java.util.*;
import java.io.*;


public class 가장긴감소하는부분수열 {
    static int N;
    static int[] numList;

    public static void main(String[] args) throws Exception {
        init();
        List<Integer> dpList = new ArrayList<>();
        dpList.add(-numList[0]);

        for (int i = 1; i < N; i++) {
            int index = Collections.binarySearch(dpList, -numList[i]);
            if (index >= 0) { continue; }
            index = - index - 1;

            if (index == dpList.size()) {
                dpList.add(-numList[i]);
            }
            else if (-numList[i] < dpList.get(index)) {
                dpList.set(index, -numList[i]);
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