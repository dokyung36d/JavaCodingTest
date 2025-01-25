package problem;

import java.util.*;
import java.io.*;

public class 비숍 {
    static int N;
    static int[][] matrix;
    static List<Pos> evenAvailPosList;
    static List<Pos> oddAvailPosList;
    static int numAvailPos;
    static int answer;
    static int oddAnswer;

    public static class Pos {
        int row;
        int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    public static class Node {
        List<Pos> bishopPosList;
        int searchIndex;

        public Node(List<Pos> bishopPosList, int searchIndex) {
            this.bishopPosList = new ArrayList<>(bishopPosList);
            this.searchIndex = searchIndex;
        }

    }

    public static boolean checkPosInDiag(Pos pos1, Pos pos2) {
        int rowDelta = pos1.row - pos2.row;
        int colDelta = pos1.col - pos2.col;

        if (Math.abs(rowDelta) == Math.abs(colDelta)) {
            return true;
        }

        return false;
    }

    public static boolean compatibleWithBishopList(Pos pos, List<Pos> bishopList) {
        for (int i = 0; i < bishopList.size(); i++) {
            if (checkPosInDiag(pos, bishopList.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) throws Exception {
        init();

        answer = 0;
        answer += dfs(new ArrayList<>(), oddAvailPosList, 0);
        answer += dfs(new ArrayList<>(), evenAvailPosList, 0);
        System.out.println(answer);
    }

    public static int dfs(List<Pos> bishopPosList, List<Pos> availBishopPosList, int curSearchIndex) throws Exception {
        if (curSearchIndex == availBishopPosList.size()) {
            return bishopPosList.size();
        }


        Pos nextBishopPos = availBishopPosList.get(curSearchIndex);
        if (compatibleWithBishopList(nextBishopPos, bishopPosList)) {
            bishopPosList.add(nextBishopPos);
            if (answer - bishopPosList.size() > numAvailPos - curSearchIndex + 1) {
                bishopPosList.remove(bishopPosList.size() - 1);
                return dfs(bishopPosList, availBishopPosList, curSearchIndex + 1);
            }

            int plusedResult = dfs(bishopPosList, availBishopPosList, curSearchIndex + 1);
            bishopPosList.remove(bishopPosList.size() - 1);

            int notPlusedResult = dfs(bishopPosList, availBishopPosList, curSearchIndex + 1);
            return Math.max(plusedResult, notPlusedResult);
        }

        return dfs(bishopPosList, availBishopPosList, curSearchIndex + 1);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        matrix = new int[N][N];
        oddAvailPosList = new ArrayList<>();
        evenAvailPosList = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                matrix[i][j] = Integer.parseInt(st.nextToken());

                if (matrix[i][j] == 1) {
                    if ((i + j) % 2 == 1) {
                        oddAvailPosList.add(new Pos(i, j));
                    }
                    else {
                        evenAvailPosList.add(new Pos(i, j));
                    }
                }
            }
        }

        numAvailPos = oddAvailPosList.size() + evenAvailPosList.size();
    }

}