package problem;

import java.util.*;
import java.io.*;


public class 두배열의합 {
    public static int T, n, m;
    public static int[] nList, mList;

    public static void main(String[] args) throws Exception {
        init();
        Map<Integer, Integer> nListMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n + 1; j++) {
                int startIndex = i;
                int endIndex = j;

                int sumValue = getPartSum(startIndex, endIndex, nList);
                if (nListMap.get(sumValue) == null) {
                    nListMap.put(sumValue, 1);
                }
                else {
                    nListMap.put(sumValue, nListMap.get(sumValue) + 1);
                }
            }
        }

        long answer = 0;
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m + 1; j++) {
                int startIndex = i;
                int endIndex = j;

                int sumValue = getPartSum(startIndex, endIndex, mList);
                int restValue = T - sumValue;
                if (nListMap.get(restValue) != null) {
                    answer += nListMap.get(restValue);
                }
            }
        }
        System.out.println(answer);
    }

    public static int getPartSum(int startIndex, int endIndex, int[] numList) {
        int value = 0;

        for (int i = startIndex; i < endIndex; i++) {
            value += numList[i];
        }

        return value;
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        T = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        nList = new int[n];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            nList[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        m = Integer.parseInt(st.nextToken());
        mList = new int[m];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < m; i++) {
            mList[i] = Integer.parseInt(st.nextToken());
        }
    }
}