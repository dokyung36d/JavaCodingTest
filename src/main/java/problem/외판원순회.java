package problem;

import java.util.*;
import java.io.*;

public class 외판원순회 {
    static int N;
    static int[][] graphMatrix;
    public static void main(String[] args) throws Exception {
        init();

        int[][] dpMatrix = new int[N][(int) Math.pow(2, N)];
        for (int i = 1; i < N; i++) {
            dpMatrix[i][(int) Math.pow(2, N - 1)] = graphMatrix[0][i];
        }
        int answer = Integer.MAX_VALUE;
        String visitedAll = "1".repeat(N);

        for (int i = (int) Math.pow(2, N - 1); i < (int) Math.pow(2, N); i++) { // 이전에 방문한 곳을 의미
            for (int j = 0; j < N; j++) { // 현재 위치한 장소
                if (dpMatrix[j][i] == 0) { //이전에 아무도 방문하지 않으면 무시
                    continue;
                }
                String visitedBitString = changeIntToStr(i);
                String updatedVisitedBitString = visitedBitString.substring(0, j) + "1" + visitedBitString.substring(j + 1);
                if (updatedVisitedBitString.equals(visitedAll)) {
                    if (graphMatrix[j][0] == 0) {
                        continue;
                    }
                    int totalCost = dpMatrix[j][i] + graphMatrix[j][0];
                    answer = (int) Math.min(answer, totalCost);
                }

                for (int k = 0; k < N; k++) { //다음에 방문할 장소
                    if (visitedBitString.charAt(k) == '1') {
                        continue;
                    }

                    if (graphMatrix[j][k] == 0) {
                        continue;
                    }

                    int newCost = dpMatrix[j][i] + graphMatrix[j][k];
                    int updatedVisitedInt = changeStrToInt(updatedVisitedBitString);

                    if (dpMatrix[k][updatedVisitedInt] == 0) {
                        dpMatrix[k][updatedVisitedInt] = newCost;
                    }
                    else {
                        dpMatrix[k][updatedVisitedInt] = (int) Math.min(dpMatrix[k][updatedVisitedInt], newCost);
                    }
                }
            }
        }

        System.out.println(answer);
    }

    public static String changeIntToStr(int num) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            if (num % 2 == 1) {
                sb.append("1");
            }
            else {
                sb.append("0");
            }

            num /= 2;
        }

        return sb.reverse().toString();
    }


    public static int changeStrToInt(String str) {
        int returnValue = 0;

        str = new StringBuilder(str).reverse().toString();
        for (int i = 0; i < N; i++) {
            if (str.charAt(i) == '1') {
                returnValue += (int) Math.pow(2, i);
            }
        }

        return returnValue;
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        graphMatrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                graphMatrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
    }
}