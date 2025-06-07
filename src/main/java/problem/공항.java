package problem;

import java.util.*;
import java.io.*;


public class 공항 {
    static int G, P;
    static int[] planeList;
    static int[] parentList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        parentList = new int[G + 1];
        for (int i = 0; i < G + 1; i++) {
            parentList[i] = i;
        }


        int answer = 0;
        for (int i = 0; i < P; i++) {
            int curMaxGate = findParent(planeList[i]);
            if (curMaxGate == 0) { break; }

            answer += 1;
            union(curMaxGate, curMaxGate - 1);
        }

        System.out.println(answer);
    }

    public static int findParent(int curNum) {
        if (curNum == parentList[curNum]) { return curNum; }

        return parentList[curNum] = findParent(parentList[curNum]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent == num2Parent) { return; }

        parentList[Math.max(num1Parent, num2Parent)] = Math.min(num1Parent, num2Parent);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        G = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        P = Integer.parseInt(st.nextToken());

        planeList = new int[P];
        for (int i = 0; i < P; i++) {
            st = new StringTokenizer(br.readLine());

            planeList[i] = Integer.parseInt(st.nextToken());
        }
    }
}