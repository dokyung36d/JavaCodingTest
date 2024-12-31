package problem;

import java.util.*;
import java.io.*;

public class 숫자배열회전 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int T = Integer.parseInt(st.nextToken());
        for (int i = 0; i < T; i++) {
            System.out.println("#" + (i + 1));
            solution(br);
        }
    }

    public static void solution(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int[][] matrix = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int[] newRow = new int[N];

            for (int j = 0; j < N; j++) {
                newRow[j] = Integer.parseInt(st.nextToken());
            }
            matrix[i] = newRow;
        }
        int[][] rotated90Matrix = rotateMatrix(matrix);
        int[][] rotated180Matrix = rotateMatrix(rotated90Matrix);
        int[][] rotated270Matrix = rotateMatrix(rotated180Matrix);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(Integer.toString(rotated90Matrix[i][j]));
            }
            System.out.print(" ");
            for (int j = 0; j < N; j++) {
                System.out.print(Integer.toString(rotated180Matrix[i][j]));
            }
            System.out.print(" ");
            for (int j = 0; j < N; j++) {
                System.out.print(Integer.toString(rotated270Matrix[i][j]));
            }
            System.out.println("");
        }
    }

    public static int[][] rotateMatrix(int[][] matrix) throws IOException {
        int N = matrix.length;
        int[][] rotatedMatrix = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                rotatedMatrix[j][N - 1- i] = matrix[i][j];
            }
        }

        return rotatedMatrix;
    }
}
