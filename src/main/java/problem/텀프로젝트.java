package problem;

import java.util.*;
import java.io.*;

import java.util.*;
import java.io.*;


public class 텀프로젝트 {
    static int T, n;
    static int[] numList;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());

        T = Integer.parseInt(st.nextToken());
        for (int i = 0; i < T; i++) {
            init();
            solution();
        }
    }

    public static void solution() {
        int[] visited = new int[n];

        int numPersonInCycle = 0;

        for (int i = 0; i < n; i++) {
            if (visited[i] == 1) { continue; }

            Map<Integer, Integer> visitOrderMap = new HashMap<>();
            int visitOrder = 1;
            visitOrderMap.put(i, 1);

            int curPerson = i;
            visited[curPerson] = 1;
            while (true) {
                int nextPerson = numList[curPerson];
                if (visitOrderMap.get(nextPerson) != null) { // When Cycle is established
                    numPersonInCycle += visitOrder - visitOrderMap.get(nextPerson) + 1;
                    break;
                }

                if (visited[nextPerson] == 1) {
                    break;
                }

                visited[nextPerson] = 1;
                visitOrder += 1;
                visitOrderMap.put(nextPerson, visitOrder);
                curPerson = nextPerson;
            }
        }

        System.out.println(n - numPersonInCycle);
    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());

        numList = new int[n];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            numList[i] = Integer.parseInt(st.nextToken()) - 1;
        }
    }

}