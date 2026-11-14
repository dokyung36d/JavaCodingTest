package problem;

import java.io.*;
import java.util.*;

public class 공항 {
    static int G, P;
    static int[] parentList, planeList;

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        for (int i = 0; i < P; i++) {
            int plane = planeList[i];
            int parent = findParent(plane);

            if (parent == 0) {
                System.out.println(i);
                return;
            }


            union(parent, findParent(parent - 1));
        }

        System.out.println(P);
    }

    public static int findParent(int num) {
        if (parentList[num] == num) { return num; }

        return parentList[num] = findParent(parentList[num]);
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

        parentList = new int[G + 1];
        for (int i = 0; i < G + 1; i++) {
            parentList[i] = i;
        }
    }
}
