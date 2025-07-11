package problem;

import java.util.*;
import java.io.*;


public class 트리 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int N;
    static int[] mainPreOrderList, mainInOrderList;

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int T = Integer.parseInt(st.nextToken());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < T; i++) {
            init();
            String answer = solution();
            sb.append(answer);
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static String solution() {
        return recursive(mainPreOrderList, mainInOrderList);
    }

    public static String recursive(int[] preOrderList, int[] inOrderList) {
        if (preOrderList.length == 0) {
            return "";
        }

        if (preOrderList.length == 1) {
            return String.valueOf(preOrderList[0]);
        }

//		if (preOrderList.length == 2) {
//			StringBuilder sb = new StringBuilder();
//			sb.append(inOrderList[0]);
//			sb.append(inOrderList[1]);
//
//			return sb.toString();
//		}

        int parentNode = preOrderList[0];
        int leftTreeLength = getIndexOfValue(inOrderList, parentNode);
        int rightTreeLength = preOrderList.length - leftTreeLength - 1;

        StringBuilder sb = new StringBuilder();

        int[] leftTreePreOrderList = getPartList(preOrderList, 1, leftTreeLength + 1);
        int[] leftTreeInOrderList = getPartList(inOrderList, 0, leftTreeLength);


        int[] rightTreePreOrderList = getPartList(preOrderList, leftTreeLength + 1, preOrderList.length);
        int[] rightTreeInOrderList = getPartList(inOrderList, leftTreeLength + 1, preOrderList.length);

        String leftTreeString = recursive(leftTreePreOrderList, leftTreeInOrderList);
        String rightTreeString = recursive(rightTreePreOrderList, rightTreeInOrderList);

        sb.append(leftTreeString);
        if (!leftTreeString.equals("")) {
            sb.append(" ");
        }
        sb.append(rightTreeString);
        if (!rightTreeString.equals("")) {
            sb.append(" ");
        }
        sb.append(parentNode);

        return sb.toString();
    }


    public static int[] getPartList(int[] numList, int startIndex, int endIndex) {
        int[] partNumList = new int[endIndex - startIndex];
        for (int i = startIndex; i < endIndex; i++) {
            partNumList[i - startIndex] = numList[i];
        }


        return partNumList;
    }

    public static int getIndexOfValue(int[] numList, int value) {
        for (int i = 0; i < numList.length; i++) {
            if (numList[i] == value) {
                return i;
            }
        }

        return -1;
    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());

        mainPreOrderList = new int[N];
        mainInOrderList = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            mainPreOrderList[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            mainInOrderList[i] = Integer.parseInt(st.nextToken());
        }

    }
}