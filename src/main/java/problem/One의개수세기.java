package problem;

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
            dpList.add((long) Math.pow(2, dpList.size()) + (long) 2 * dpList.get(dpList.size() - 1));
        }


        long bNum = recursive(B);
        long aNum = recursive(A - 1);
        System.out.println((long) recursive(B) - (long) recursive(A - 1));
    }


    public static long recursive(long num) {
        int logValue = (int) (Math.log(num) / Math.log(2));
        if (num == 0) { return 0; }
        if (num == 1) { return 1; }
        if (Math.pow(2, logValue) == num) { return 1 + dpList.get(logValue - 1); }


        return dpList.get(logValue - 1) + (long) recursive((long) (num - (long) Math.pow(2, logValue))) + (long) (num - Math.pow(2, logValue) + 1);
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        A = Long.parseLong(st.nextToken());
        B = Long.parseLong(st.nextToken());
    }
}