package problem;

import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class 고층건물 {
    static int N;
    static int[] buildingList;
    public static void main(String[] args) throws IOException {
        init();
        int answer = 0;

        for (int i = 0; i < N; i++) {
            int candidate = 0;
            for (int j = 0; j < N; j++) {
                if (i == j) {
                    continue;
                }
                if (viewAvailable(i, j)) {
                    candidate += 1;
                }
            }

            if (candidate > answer) {
                answer = candidate;
            }
        }

        System.out.println(answer);
    }

    public static boolean viewAvailable(int viewPos, int destPos) throws IOException {
        int viewBuildingHeight = buildingList[viewPos];
        int destBuildingHeight = buildingList[destPos];

        for (int i = Math.min(viewPos, destPos) + 1; i < Math.max(viewPos, destPos); i++) {
            int viewGap = Math.abs(i - viewPos);
            int destGap = Math.abs(i - destPos);

            BigInteger standardValue1 = new BigInteger(String.valueOf(destBuildingHeight));
            standardValue1 = standardValue1.multiply(new BigInteger(String.valueOf(viewGap)));
            BigInteger standardValue2 = new BigInteger(String.valueOf(viewBuildingHeight));
            standardValue2 = standardValue2.multiply(new BigInteger(String.valueOf(destGap)));
            BigInteger standardValue = standardValue1.add(standardValue2);

            BigInteger curBuildingHeight = new BigInteger(String.valueOf(buildingList[i]));
            curBuildingHeight = curBuildingHeight.multiply(new BigInteger(String.valueOf(viewGap + destGap)));
            int compareResult = curBuildingHeight.compareTo(standardValue);
            if (compareResult == 1 || compareResult == 0) {
                return false;
            }
        }

        return true;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        buildingList = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            buildingList[i] = Integer.parseInt(st.nextToken());
        }
    }

}