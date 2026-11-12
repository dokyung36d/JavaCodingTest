package problem;

import java.io.*;
import java.util.*;

public class Main {
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
            dpList.add((long) 2 * dpList.get(dpList.size() - 1) + (long) Math.pow(2, dpList.size()));
        }

        long answer = recursive(B) - recursive(A - 1);
        System.out.println(answer);
    }

    public static long recursive(long num) {
        if (num == 0) { return 0; }
        if (num == 1) { return 1; }

        long answer = 0;


        int logValue = getLog2Value(num);
        long expNum = (long) Math.pow(2, logValue);
        answer += dpList.get(logValue - 1);
        answer += (num - expNum + 1);
        answer += recursive(num - expNum);

        return answer;
    }

    public static int getLog2Value(long num) {
        return (int) (Math.log(num) /  Math.log(2));
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        A = Long.parseLong(st.nextToken());
        B = Long.parseLong(st.nextToken());
    }
}
