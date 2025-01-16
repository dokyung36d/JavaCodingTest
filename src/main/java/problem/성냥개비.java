import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class 성냥개비 {
    static int T;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static Map<Integer, Integer> numStickRequiredMap = new HashMap<>();
    static int N;

    public static void main(String[] args) throws Exception {
        init();
        StringTokenizer st = new StringTokenizer(br.readLine());
        T = Integer.parseInt(st.nextToken());
        for (int i = 0; i < T; i++) {
            printSmallestNumber();
            System.out.print(" ");
            printBiggestNumber();
        }


    }
    public static void printBiggestNumber() throws Exception {
        if (N == 2) {
            System.out.println(1);
            return;
        }
        if (N == 3) {
            System.out.println(7);
            return;
        }
        if (N % 2 == 0) {
            System.out.println("1".repeat(N / 2));
            return;
        }

        String answer = "7";
        System.out.println(answer + "1".repeat((N - 3) / 2));


    }


    public static void printSmallestNumber() throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        List<Integer>[] dpList = new ArrayList[101];
        for (int i = 0; i < 101; i++) {
            dpList[i] = new ArrayList<>();
        }

        dpList[2].add(1);
        dpList[3].add(7);
        dpList[4].add(4);
        dpList[5].add(2);
        dpList[6].add(6);
        dpList[7].add(8);
        dpList[8].add(1);
        dpList[8].add(0);
        if (N <= 8) {
            System.out.print(getSmallestCombination(dpList[N]));
            return;
        }

        for (int i = 9; i <= N; i++) {
            for (int key : numStickRequiredMap.keySet()) {
                int newNum = numStickRequiredMap.get(key);
                List<Integer> newCombination = new ArrayList<>(dpList[i - key]);
                newCombination.add(newNum);
                String minNumberString = getSmallestCombination(newCombination);

                if (dpList[i].size() == 0) {
                    dpList[i] = newCombination;
                }
                else {
                    dpList[i] = getSmallestString(dpList[i], newCombination);
                }
            }
        }


        System.out.print(getSmallestCombination(dpList[N]));
        return;
    }

    public static List<Integer> getSmallestString(List<Integer> comb1, List<Integer> comb2) {
        String str1 = getSmallestCombination(comb1);
        String str2 = getSmallestCombination(comb2);

        BigInteger str1BigInteger = new BigInteger(str1);
        BigInteger str2BigInteger = new BigInteger(str2);

        if (str1BigInteger.compareTo(str2BigInteger) == 1) {
            return comb2;
        }

        return comb1;
    }

    public static String getSmallestCombination(List<Integer> numList) {
        StringBuilder sb = new StringBuilder();

        Collections.sort(numList);
        int numZero = 0;
        for (int i = 0; i < numList.size(); i++) {
            if (numList.get(i) != 0) {
                break;
            }
            numZero += 1;
        }
        sb.append(String.valueOf(numList.get(numZero)));
        for (int i = 0; i < numZero; i++) {
            sb.append("0");
        }

        for (int i = numZero + 1; i < numList.size(); i++) {
            sb.append(numList.get(i));
        }


        return sb.toString();
    }

    public static void init() throws Exception {
        numStickRequiredMap.put(2, 1);
        numStickRequiredMap.put(3, 4);
        numStickRequiredMap.put(5, 2);
        numStickRequiredMap.put(6, 0);
        numStickRequiredMap.put(7, 8);
    }
}