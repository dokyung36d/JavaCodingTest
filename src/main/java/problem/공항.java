package problem;

import java.util.*;
import java.io.*;



public class 공항 {
    static int G, P;
    static int[] gateList;
    static int[] parentList;


    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        int answer = 0;

        for (int i = 0; i < P; i++) {
            int gate = gateList[i];

            int parent = findParent(gate);
            if (parent == 0) { break; }

            union(parent, parent - 1);
            answer += 1;
        }

        System.out.println(answer);
    }

    public static int findParent(int num) {
        if (num == parentList[num]) { return num; }

        return parentList[num] = findParent(parentList[num]);
    }

    public static void union(int num1, int num2) {
        int num1Parent = findParent(num1);
        int num2Parent = findParent(num2);

        if (num1Parent == num2Parent) { return; }

        int bigNumGate = Math.max(num1Parent, num2Parent);
        int smallNumGate = Math.min(num1Parent, num2Parent);

        parentList[bigNumGate] = smallNumGate;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        G = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        P = Integer.parseInt(st.nextToken());

        gateList = new int[P];
        for (int i = 0; i < P; i++) {
            st = new StringTokenizer(br.readLine());

            int gate = Integer.parseInt(st.nextToken());
            gateList[i] = gate;
        }


        parentList = new int[G + 1];
        for (int i = 0; i < G + 1; i++) {
            parentList[i] = i;
        }
    }
}
