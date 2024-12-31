package problem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class 두개의숫자열 {
    public static int T;
    public static int[][] AList;
    public static int[][] BList;

    public static void main(String[] args) throws IOException {
        init();

        for (int i = 0; i < T; i++) {
            int answer = solution(AList[i], BList[i]);
            System.out.println("#" + (i + 1) + " " + answer);
        }
    }

    public static int solution(int[] list1, int[] list2) throws IOException {
        int answer = 0;

        int list1Length = list1.length;
        int list2Length = list2.length;
        int minLength = Math.min(list1Length, list2Length);

        if (list1Length < list2Length) {
            for (int i = 0; i < list2Length - list1Length + 1; i++) {
                int candidate = 0;
                for (int j = 0; j < list1Length; j++) {
                    candidate += list1[j] * list2[i + j];
                }

                if (candidate > answer) {
                    answer = candidate;
                }
            }
        }

        else if (list1Length > list2Length) {
            for (int i = 0; i < list1Length - list2Length + 1; i++) {
                int candidate = 0;
                for (int j = 0; j < list2Length; j++) {
                    candidate += list1[i + j] * list2[j];
                }

                if (candidate > answer) {
                    answer = candidate;
                }
            }
        }

        else {
            int candidate = 0;
            for (int i = 0; i < list1Length; i++) {
                candidate += list1[i] * list2[i];
            }
            return candidate;
        }
        return answer;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        T = Integer.parseInt(st.nextToken());

        AList = new int[T][];
        BList = new int[T][];

        for (int i = 0; i < T; i++) {
            st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());

            int[] newAList = new int[N];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                newAList[j] = Integer.parseInt(st.nextToken());
            }
            AList[i] = newAList;

            int[] newBList = new int[M];
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                newBList[j] = Integer.parseInt(st.nextToken());
            }
            BList[i] = newBList;

        }
    }

}
