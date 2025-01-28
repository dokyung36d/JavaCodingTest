package problem;

import java.util.*;
import java.io.*;


public class Main {
    static int N;
    static List<Integer> inOrderList;
    static List<Integer> postOrderList;
    static StringBuilder sb;

    public static void main(String[] args) throws Exception {
        init();
        sb = new StringBuilder();
        solution(inOrderList, postOrderList);

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }


    public static void solution(List<Integer> inOrderNums, List<Integer> postOrderNums) throws Exception {
        List<Integer> preOrderNums = new ArrayList<>();

        if (inOrderNums.size() == 0) {
            return;
        }

        if (inOrderNums.size() == 1) {
            sb.append(inOrderNums.get(0) + " ");
            return;
        }

        int rootValue = postOrderNums.get(postOrderNums.size() - 1);

        int leftChildLength = inOrderNums.indexOf(rootValue);
        int rightChildLength = inOrderNums.size() - leftChildLength - 1;

        sb.append(rootValue + " ");
        solution(inOrderNums.subList(0, leftChildLength),
                postOrderNums.subList(0, leftChildLength));

        solution(inOrderNums.subList(leftChildLength + 1, inOrderNums.size()),
                postOrderNums.subList(leftChildLength, inOrderNums.size() - 1));



        return;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        inOrderList = new ArrayList<>();
        postOrderList = new ArrayList<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            inOrderList.add(Integer.parseInt(st.nextToken()));
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            postOrderList.add(Integer.parseInt(st.nextToken()));
        }
    }
}