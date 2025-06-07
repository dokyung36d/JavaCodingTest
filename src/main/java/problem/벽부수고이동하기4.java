package problem;

import java.util.*;
import java.io.*;


public class 벽부수고이동하기4 {
    static int N, M;
    static int[][] mainMatrix, groupMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1),
            new Pos(1, 0), new Pos(0, -1)};
    static Map<Integer, Integer> groupSizeMap;

    public static class Pos {
        int row;
        int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Pos addPos(Pos direction) {
            return new Pos(this.row + direction.row, this.col + direction.col);
        }

        public boolean isValidIndex() {
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= M) {
                return false;
            }

            return true;
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        groupMatrix = new int[N][M];
        int uniqueNum = 2;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (mainMatrix[i][j] == 1) { continue; }

                fillGroupMatrix(new Pos(i, j), uniqueNum);
                uniqueNum += 1;
            }
        }

        int[][] answerMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (mainMatrix[i][j] != 1) { continue; }

                answerMatrix[i][j] = (getNumMovePossible(new Pos(i, j)) + 1) % 10;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                sb.append(answerMatrix[i][j]);
            }
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int getNumMovePossible(Pos pos) {
        Map<Integer, Integer> nearGroupMap = new HashMap<>();

//		if (mainMatrix[pos.row][pos.col] == 1) {
//			return -1;
//		}
        for (Pos direction : directions) {
            Pos movedPos = pos.addPos(direction);
            if (!movedPos.isValidIndex()) { continue; }
            if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

            nearGroupMap.put(mainMatrix[movedPos.row][movedPos.col], 1);
        }

        int answer = 0;
        for (int groupUniqueNum : nearGroupMap.keySet()) {
            answer += groupSizeMap.get(groupUniqueNum);
        }

        return answer;
    }

    public static void fillGroupMatrix(Pos startPos, int uniqueNum) {
        Deque<Pos> queue = new ArrayDeque<>();
        queue.add(startPos);

        int size = 0;
        while (!queue.isEmpty()) {
            Pos curPos = queue.pollFirst();
            if (mainMatrix[curPos.row][curPos.col] != 0) { continue; }
            mainMatrix[curPos.row][curPos.col] = uniqueNum;
            size += 1;

            for (Pos direction : directions) {
                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] == 1) { continue; }

                queue.add(movedPos);
            }
        }

        groupSizeMap.put(uniqueNum, size);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        mainMatrix = new int[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();
            for (int j = 0; j < M; j++) {
                mainMatrix[i][j] = string.charAt(j) - '0';
            }
        }

        groupSizeMap = new HashMap<>();
    }
}