package problem;

import java.util.*;
import java.io.*;


public class 좌표압축 {
    static int N;
    static int[] numList;

    public static void main(String[] args) throws Exception {
        init();
        Map<Integer, Integer> numMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            if (numMap.get(numList[i]) == null) {
                numMap.put(numList[i], 1);
            }
        }

        Set<Integer> numSet = numMap.keySet();
        List<Integer> numListDuplicatesRemoved = new ArrayList<>();
        for (int num : numSet) {
            numListDuplicatesRemoved.add(num);
        }

        Collections.sort(numListDuplicatesRemoved);
        Map<Integer, Integer> orderMap = new HashMap<>();
        for (int i = 0; i < numListDuplicatesRemoved.size(); i++) {
            orderMap.put(numListDuplicatesRemoved.get(i), i);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            sb.append(orderMap.get(numList[i]) + " ");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
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