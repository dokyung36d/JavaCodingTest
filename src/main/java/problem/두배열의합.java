package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class 두배열의합 {
    static int T, aN, bN;
    static int[] aNList, bNList;
    static int[] aNCumulativeSum, bNCumulativeSum;
    static Map<Integer, Integer> aNSumMap, bNSumMap;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        aNCumulativeSum = setCumulativeSum(aNList);
        bNCumulativeSum = setCumulativeSum(bNList);

        aNSumMap = setSumMap(aNCumulativeSum);
        bNSumMap = setSumMap(bNCumulativeSum);


        long answer = 0;
        for (int aNSumValue : aNSumMap.keySet()) {
            int restSum = T - aNSumValue;

            answer += ((long) aNSumMap.get(aNSumValue)) * ((long) bNSumMap.getOrDefault(restSum, 0));
        }


        System.out.println(answer);
    }

    public static Map<Integer, Integer> setSumMap(int[] cumulativeSum) {
        Map<Integer, Integer> sumMap = new HashMap<>();

        for (int gap = 1; gap < cumulativeSum.length; gap++) {
            for (int start = 0; start + gap < cumulativeSum.length; start++) {
                int sumValue = cumulativeSum[start + gap] - cumulativeSum[start];
                sumMap.put(sumValue, sumMap.getOrDefault(sumValue, 0) + 1);
            }
        }

        return sumMap;
    }

    public static int[] setCumulativeSum(int[] numList) {
        int[] cumulativeSum = new int[numList.length + 1];
        cumulativeSum[0] = 0;

        for (int i = 1; i < numList.length + 1; i++) {
            cumulativeSum[i] = cumulativeSum[i - 1] + numList[i - 1];
        }

        return cumulativeSum;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        T = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        aN = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        aNList = new int[aN];
        for (int i = 0; i < aN; i++) {
            aNList[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        bN = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        bNList = new int[bN];
        for (int i = 0; i < bN; i++) {
            bNList[i] = Integer.parseInt(st.nextToken());
        }


    }


}