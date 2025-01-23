package problem;

import java.util.*;
import java.io.*;

public class 텀프로젝트 {
    static int T;
    static int N;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int[] infoList;

    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        T = Integer.parseInt(st.nextToken());
        for (int i = 0; i < T; i++) {
            init();
            solution();
        }

    }

    public static void solution() throws Exception {
        int answer = 0;
        int[] outed = new int[N];

        for (int i = 0; i < N; i++) {
            if (outed[i] == 1) {
                continue;
            }

            int curPerson = i;
            Map<Integer, Integer> visitedOrderMap = new HashMap<>();
            int visitedOrder = 0;
            visitedOrderMap.put(curPerson, visitedOrder++);

            while (true) {
                int nextPerson = infoList[curPerson];
                if (outed[nextPerson] == 1) {
                    answer += visitedOrderMap.keySet().size();
                    break;
                }
                if (visitedOrderMap.get(nextPerson) == null) {
                    visitedOrderMap.put(nextPerson, visitedOrder++);
                    curPerson = nextPerson;
                    continue;
                }

                answer += visitedOrderMap.get(nextPerson);
                break;
            }

            for(int key : visitedOrderMap.keySet()) {
                outed[key] = 1;
            }
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        infoList = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            infoList[i] = Integer.parseInt(st.nextToken()) - 1;
        }
    }
}