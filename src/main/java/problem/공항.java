package problem;

import java.util.*;
import java.io.*;


public class 공항 {
    static int G, P;
    static int[] airplaneList;
    static int[] parentList;

    public static void main(String[] args) throws Exception {
        init();

        for (int i = 0; i < P; i++) {
            int parent = findParent(airplaneList[i]);

            if (parent == 0) {
                System.out.println(i);
                return;
            }

            union(parent, parent - 1);
        }
        System.out.println(P);
    }

    public static int findParent(int num) {
        if (parentList[num] == num) {
            return num;
        }

        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent != num2Parent) {
            parentList[Math.max(num1Parent, num1Parent)] = Math.min(num1Parent, num2Parent);
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        G = Integer.parseInt(st.nextToken());

        parentList = new int[G + 1];
        for (int i = 0; i < G + 1; i++) {
            parentList[i] = i;
        }

        st = new StringTokenizer(br.readLine());
        P = Integer.parseInt(st.nextToken());

        airplaneList = new int[P];
        for (int i = 0; i < P; i++) {
            st = new StringTokenizer(br.readLine());
            airplaneList[i] = Integer.parseInt(st.nextToken());
        }
    }
}
