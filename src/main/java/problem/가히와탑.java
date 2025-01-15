package problem;

import java.io.*;
import java.util.*;

public class 가히와탑 {
    static int N, a, b;
    public static void main(String[] args) throws Exception {
        init();
        int leftMaxNum = a;
        int rightMaxNum = b;
        int maxNum = Math.max(leftMaxNum, rightMaxNum);
        int numRequired = a + b - 1;
        if (numRequired > N) {
            System.out.println(-1);
            return;
        }

        List<Integer> answerList = new ArrayList<>();

        for (int i = 0; i < leftMaxNum - 1; i++) {
            answerList.add(i + 1);
        }

        answerList.add(maxNum);

        for (int i = rightMaxNum - 1; i >= 1; i--) {
            answerList.add(i);
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        bw.write(String.valueOf(answerList.get(0)));
        bw.write(" ");

        for (int i = 0; i < N - numRequired; i++) {
            bw.write("1 ");
        }

        for (int i = 1; i < answerList.size(); i++) {
            bw.write(String.valueOf(answerList.get(i)));
            bw.write(" ");
        }

        bw.flush();
        bw.close();

    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        a = Integer.parseInt(st.nextToken());
        b = Integer.parseInt(st.nextToken());

    }
}