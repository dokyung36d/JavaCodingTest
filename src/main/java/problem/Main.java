package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N;
    static int[] numList;


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        List<Integer> dpList = new ArrayList<>();
        int[] lengthList = new int[N];


        for (int i = 0; i < N; i++) {
            int num = numList[i];

            int index = Collections.binarySearch(dpList, num);
            if (index >= 0) {
                lengthList[i] = index + 1;
                continue;
            }


            index = - index - 1;
            if (index == dpList.size()) {
                lengthList[i] = dpList.size() + 1;
                dpList.add(num);

                continue;
            }


            dpList.set(index, num);
            lengthList[i] = index + 1;

        }


        StringBuilder sb = new StringBuilder();
        int curLength = dpList.size();
        List<Integer> answerList = new ArrayList<>();
        for (int i = N - 1; i >= 0; i--) {
            if (lengthList[i] == curLength) {
                answerList.add(numList[i]);
                curLength -= 1;
            }
        }


        for (int i = answerList.size() - 1; i >= 0; i--) {
            sb.append(answerList.get(i) + " ");
        }

        System.out.println(answerList.size());
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