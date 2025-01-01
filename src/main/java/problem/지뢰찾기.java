package problem;

import java.util.*;
import java.io.*;

public class 지뢰찾기 {
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

        @Override
        public int hashCode() {
            return Arrays.asList(this.row, this.col).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Pos anotherPos = (Pos) obj;
            if (this.row == anotherPos.row && this.col == anotherPos.col) {
                return true;
            }

            return false;
        }
    }
    public static Map<Pos, Integer> visited;
    public static char[][] matrix;


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int T = Integer.parseInt(st.nextToken());
        for (int i = 0; i < T; i++) {
            init();
            int answer = solution(br);
            System.out.println("#" + (i + 1) + " " + answer);
        }
    }

    public static void init() throws IOException {
        visited = new HashMap<>();
    }

    public static int solution(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());

        matrix = new char[N][N];
        for (int i = 0; i < N; i++) {
            String input = br.readLine();
            for (int j = 0; j < N; j++) {
                matrix[i][j] = input.charAt(j);
            }
        }
        Map<List<Integer>, Integer> outedMap = new HashMap<>();
        ArrayList<Pos> zeroNearBombPosList = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (calculateNearBomb(new Pos(i, j)) == 0) {
                    zeroNearBombPosList.add(new Pos(i, j));
                }
            }
        }
        int answer = 0;
        for (int i = 0; i < zeroNearBombPosList.size(); i++) {
            Pos zeroNearBombPos = zeroNearBombPosList.get(i);
            if (visited.get(zeroNearBombPos) != null) {
                continue;
            }
            bfs(zeroNearBombPos);
            answer += 1;
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (matrix[i][j] == '*') {
                    continue;
                }

                if (visited.get(new Pos(i, j)) == null) {
                    answer += 1;
                }
            }
        }

        return answer;
    }

    public static void bfs(Pos startPos) {
        Deque<Pos> queue = new ArrayDeque<>();
        queue.addLast(startPos);
        visited.put(startPos, 1);

        while (!queue.isEmpty()) {
            Pos pos = queue.pollFirst();
            int numNearBomb = calculateNearBomb(pos);
            if (numNearBomb != 0) {
                continue;
            }

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    Pos direction = new Pos(i, j);
                    Pos movedPos = pos.addPos(direction);
                    if (!checkIndex(movedPos, matrix.length)) {
                        continue;
                    }
                    if (matrix[movedPos.row][movedPos.col] == '*') {
                        continue;
                    }
                    if (visited.get(movedPos) != null) {
                        continue;
                    }
                    visited.put(movedPos, 1);

                    if (calculateNearBomb(movedPos) != 0) {
                        continue;
                    }

                    queue.addLast(movedPos);
                }
            }
        }
    }

    public static int calculateNearBomb(Pos pos) {
        int numBomb = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Pos movedPos = pos.addPos(new Pos(i, j));
                if (!checkIndex(movedPos, matrix.length)) {
                    continue;
                }

                if (matrix[movedPos.row][movedPos.col] == '*') {
                    numBomb += 1;
                    continue;
                }
            }
        }

        return numBomb;
    }

    public static boolean checkIndex(Pos pos, int N) {
        if (0 <= pos.row && pos.row < N && 0 <= pos.col && pos.col < N) {
            return true;
        }

        return false;
    }
}