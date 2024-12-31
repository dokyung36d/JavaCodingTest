package problem;

import java.util.*;
import java.io.*;

public class 파리퇴치 {
    public static class Pos {
        int row;
        int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Pos addPos(Pos anotherPos) {
            return new Pos(this.row + anotherPos.row, this.col + anotherPos.col);
        }

        public Pos mutiplyPos(int n) {
            return new Pos(this.row * n, this.col * n);
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int T = Integer.parseInt(st.nextToken());
        for (int i = 0; i < T; i++) {
            int answer = solution(br);
            System.out.println("#" + (i + 1) + " " + answer);
        }
    }

    public static int solution(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                matrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int maxValue = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int value = getResult(new Pos(i, j), matrix, N, M);

                if (value > maxValue) {
                    maxValue = value;
                }
            }
        }


        return maxValue;
    }

    public static int getResult(Pos pos, int[][] matrix, int n, int m) throws IOException {
        int[][] firstDirections = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        int[][] secondDirections = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
        int maxValue = 0;

        int value = 0;
        for (int i = 0; i < 4; i++) {
            Pos direction = new Pos(firstDirections[i][0], firstDirections[i][1]);
            for (int j = 1; j < m; j++) {
                Pos multipliedDirection = direction.mutiplyPos(j);
                Pos finalPos = pos.addPos(multipliedDirection);

                if (!checkIndex(finalPos, n)) {
                    continue;
                }
                value += matrix[finalPos.row][finalPos.col];
            }
        }
        if (value > maxValue) {
            maxValue = value;
        }

        value = 0;
        for (int i = 0; i < 4; i++) {
            Pos direction = new Pos(secondDirections[i][0], secondDirections[i][1]);
            for (int j = 1; j < m; j++) {
                Pos multipliedDirection = direction.mutiplyPos(j);
                Pos finalPos = pos.addPos(multipliedDirection);

                if (!checkIndex(finalPos, n)) {
                    continue;
                }
                value += matrix[finalPos.row][finalPos.col];
            }
        }
        if (value > maxValue) {
            maxValue = value;
        }


        return maxValue + matrix[pos.row][pos.col];
    }

    public static boolean checkIndex(Pos pos, int N) throws IOException {
        if (0 <= pos.row && pos.row < N && 0 <= pos.col && pos.col < N) {
            return true;
        }
        return false;
    }
}