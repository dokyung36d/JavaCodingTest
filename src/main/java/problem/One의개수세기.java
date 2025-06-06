package problem;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

import java.util.*;
import java.io.*;


public class One의개수세기 {
    static long A, B;
    static List<Long> dpList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        dpList = new ArrayList<>();
        dpList.add((long) 1);

        while (Math.pow(2, dpList.size()) < B) {
            dpList.add(dpList.get(dpList.size() - 1) * 2 + (long) Math.pow(2, dpList.size()));
        }

        long bNumOne = getNumOne(B);
        long aNumOne = getNumOne(A - 1);

        System.out.println(bNumOne - aNumOne);
    }

    public static long getNumOne(long num) {
        int numBit = (int) (Math.log(num) / Math.log(2)) + 1;


        long numOne = 0;
        for (int i = numBit - 1; i >= 1; i--) {
            if ((num & (long) 1 << i) == (long) 0) { continue; }

            numOne += dpList.get(i - 1);
            num -= (long) 1 << i;
            numOne += (num + 1);
        }



        return numOne + num;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        A = Long.parseLong(st.nextToken());
        B = Long.parseLong(st.nextToken());
    }
}