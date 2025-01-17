package problem;

import java.util.*;
import java.io.*;

public class 사이클게임 {
    static int n;
    static int m;
    static int[] parentList;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        init();
        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int firstVertex = Integer.parseInt(st.nextToken());
            int secondVertex = Integer.parseInt(st.nextToken());

            int parentVertex = Math.min(firstVertex, secondVertex);
            int childVertex = Math.max(firstVertex, secondVertex);

            if (findRootParent(childVertex) == findRootParent(parentVertex)) {
                System.out.println(i + 1);
                return;
            }

            union(parentVertex, childVertex);
        }
        System.out.println(0);
    }

    public static void union(int vertex1, int vertex2) {
        int root1 = findRootParent(vertex1);
        int root2 = findRootParent(vertex2);


        if (root1 != root2) {
            parentList[root2] = root1;
        }
    }

    public static int findRootParent(int vertex) {
        if (parentList[vertex] == vertex) {
            return vertex;
        }

        return parentList[vertex] = findRootParent(parentList[vertex]);
    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        parentList = new int[n];
        for (int i = 0; i < n; i++) {
            parentList[i] = i;
        }
    }
}