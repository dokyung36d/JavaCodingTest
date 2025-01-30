package problem;

import java.util.*;
import java.io.*;


public class 부분수열의합 {
    static int N, S;
    static List<Integer> firstArray, secondArray;

    public static void main(String[] args) throws Exception {
        init();

        Map<Long, Long> firstDict = new HashMap<>();
        firstDict.put((long) 0, (long) 1);
        for (int i = 0; i < firstArray.size(); i++) {
            int value = firstArray.get(i);

            Map<Long, Long> updatedDict = new HashMap<>();
            for (long key : firstDict.keySet()) {
                long prevNumCases = firstDict.get(key);

                if (updatedDict.get(key) == null) {
                    updatedDict.put(key, prevNumCases);
                }
                else {
                    updatedDict.put(key, updatedDict.get(key) + prevNumCases);
                }


                long updatedValue = value + key;
                if (updatedDict.get(updatedValue) == null) {
                    updatedDict.put(updatedValue, prevNumCases);
                }
                else {
                    updatedDict.put(updatedValue, updatedDict.get(updatedValue) + prevNumCases);
                }
            }
            firstDict = updatedDict;
        }


        Map<Long, Long> secondDict = new HashMap<>();
        secondDict.put((long) 0, (long) 1);
        for (int i = 0; i < secondArray.size(); i++) {
            int value = secondArray.get(i);

            Map<Long, Long> updatedDict = new HashMap<>();
            for (long key : secondDict.keySet()) {
                long prevNumCases = secondDict.get(key);
                if (updatedDict.get(key) == null) {
                    updatedDict.put(key, prevNumCases);
                }
                else {
                    updatedDict.put(key, updatedDict.get(key) + prevNumCases);
                }

                long updatedValue = value + key;

                if (updatedDict.get(updatedValue) == null) {
                    updatedDict.put(updatedValue, prevNumCases);
                }
                else {
                    updatedDict.put(updatedValue, updatedDict.get(updatedValue) + prevNumCases);
                }
            }
            secondDict = updatedDict;
        }

        long answer = 0;
        for (long key : firstDict.keySet()) {
            long restValue = S - key;

            if (secondDict.get(restValue) != null) {
                answer += firstDict.get(key) * secondDict.get(restValue);
            }
        }

        if (S == 0) {
            System.out.println(answer - 1);
        }
        else {
            System.out.println(answer);
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        S = Integer.parseInt(st.nextToken());

        firstArray = new ArrayList<>();
        secondArray = new ArrayList<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            if (i < N / 2) {
                firstArray.add(Integer.parseInt(st.nextToken()));
            }
            else {
                secondArray.add(Integer.parseInt(st.nextToken()));
            }
        }
    }
}
