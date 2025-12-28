package problem;

import java.util.*;
import java.io.*;



public class One의개수세기 {
    static long a, b;
    static List<Long> dpList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        dpList = new ArrayList<>();
        dpList.add((long) 1);

        while (Math.pow(2, dpList.size()) < b) {
            long dpNum = 2 * dpList.get(dpList.size() - 1) + (long) Math.pow(2, dpList.size());
            dpList.add(dpNum);
        }

        long aNumOne = getNumOne(a);
        long bNumOne = getNumOne(b);
        System.out.println(bNumOne - aNumOne);
    }

    public static long getNumOne(long num) {
        if (num == (long) 1) {
            return 1;
        }


        long numOne = (long) 0;

        int curBit = 63 - Long.numberOfLeadingZeros(num);

        while (num > (long) 0) {
            if (num <= (long) 2) {
                numOne += num;
                break;
            }


            long isOne = (num & ((long) 1 << curBit));
            if (isOne == (long) 0) {
                curBit -= 1;
                continue;
            }

            numOne += dpList.get(curBit - 1);
            num -= ((long) 1 << curBit);
            numOne += (num + 1);
            curBit -= 1;
        }

        return numOne;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        a = Long.parseLong(st.nextToken()) - 1;
        b = Long.parseLong(st.nextToken());
    }
}