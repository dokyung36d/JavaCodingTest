package problem;

import java.io.*;
import java.util.*;

public class LCS {
    static String firstString;
    static String secondString;
    static int[][] dpMatrix;
    static int firstStringlength;
    static int secondStringLength;
    public static void main(String[] args) throws IOException {
        init();

        int firstStringOneStartIndex = firstString.indexOf(secondString.charAt(0));
        int secondStringOneStartIndex = secondString.indexOf(firstString.charAt(0));

        if (firstStringOneStartIndex != -1) {
            for (int i = firstStringOneStartIndex; i < firstStringlength; i++) {
                dpMatrix[i][0] = 1;
            }
        }
        if (secondStringOneStartIndex != -1) {
            for (int i = secondStringOneStartIndex; i < secondStringLength; i++) {
                dpMatrix[0][i] = 1;
            }
        }


        for (int i = 1; i < firstStringlength; i++) {
            for (int j = 1; j < secondStringLength; j++) {
                if (firstString.charAt(i) != secondString.charAt(j)) {
                    dpMatrix[i][j] = Math.max(dpMatrix[i - 1][j], dpMatrix[i][j - 1]);
                }
                else {
                    dpMatrix[i][j] = dpMatrix[i - 1][j - 1] + 1;
                }
            }
        }

        System.out.println(dpMatrix[firstStringlength - 1][secondStringLength - 1]);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        firstString = st.nextToken();
        firstStringlength = firstString.length();

        st = new StringTokenizer(br.readLine());
        secondString = st.nextToken();
        secondStringLength = secondString.length();
        dpMatrix = new int[firstStringlength][secondStringLength];
    }
}