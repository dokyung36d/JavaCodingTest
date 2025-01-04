package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class 가장큰증가하는부분수열 {
    static int N;
    static int[] numList;
    public static void main(String[] args) throws IOException {
        init();
        List<Integer> dpList = new ArrayList<>();
        Map<Integer, Integer> dpMap = new HashMap<>();
        dpList.add(numList[0]);

        int answer = 1;

        for (int i = 1; i < N; i++) {
            int curNum = numList[i];
            int index = Collections.binarySearch(dpList, curNum);
            if (index >= 0) {
                continue;
            }

            if (curNum > dpList.get(dpList.size() - 1)) {
                dpList.add(curNum);
            }
            else {
                int insertIndex = -index - 1;
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
