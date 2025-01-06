package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.*;

public class 가장긴증가하는부분수열5 {
    static int N;
    static int[] numList;
    static Map<Integer, ArrayList<Integer>> dpStackMap;

    public static void main(String[] args) throws IOException {
        init();
        List<Integer> dpList = new ArrayList<>();
        dpList.add(numList[0]);
        dpStackMap.put(1, new ArrayList<>());
        dpStackMap.get(1).add(0);

        for (int i = 1; i < N; i++) {
            int curNum = numList[i];
            int index = Collections.binarySearch(dpList, curNum);
            if (index >= 0) {
                continue;
            }

            if (curNum > dpList.get(dpList.size() - 1)) {
                dpList.add(curNum);
                dpStackMap.put(dpList.size(), new ArrayList<>());
                dpStackMap.get(dpList.size()).add(i);
            }
            else {
                int insertIndex = -index - 1;
                dpList.set(insertIndex, curNum);
                dpStackMap.get(insertIndex + 1).add(i);
            }
        }

        System.out.println(dpList.size());

        if (dpList.size() == 1) {
            System.out.println(dpList.get(0));
            return;
        }

        List<Integer> answerList = new ArrayList<>();
        int curIndex = dpStackMap.get(dpList.size()).get(dpStackMap.get(dpList.size()).size() - 1);
        answerList.add(numList[curIndex]);
        for (int i = dpList.size() - 1; i >= 1; i--) {
            int index = Collections.binarySearch(dpStackMap.get(i), curIndex);
            index = - index - 2;

            answerList.add(numList[dpStackMap.get(i).get(index)]);
            curIndex = dpStackMap.get(i).get(index);

        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        for (int i = answerList.size() - 1; i >= 1; i--) {
            bw.write(String.valueOf(answerList.get(i))+ " ");
        }
        bw.write(String.valueOf(answerList.get(0)));
        bw.flush();
        bw.close();
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
