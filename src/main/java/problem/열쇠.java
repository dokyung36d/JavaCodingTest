package problem;

import java.util.*;
import java.io.*;



public class 열쇠 {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int N, M;
    static char[][] mainMatrix;
    static Map<Character, Integer> keyMap;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};


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

        public boolean isEdge() {
            if (this.row == 0 || this.row == N - 1 || this.col == 0 || this.col == M - 1) {
                return true;
            }

            return false;
        }

        public char getValue() {
            return mainMatrix[this.row][this.col];
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true;}
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Pos anotherPos = (Pos) obj;
            if (this.row == anotherPos.row && this.col == anotherPos.col) { return true; }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }
    }


    public static void main(String[] args) throws Exception {
        StringTokenizer st = new StringTokenizer(br.readLine());
        int T = Integer.parseInt(st.nextToken());


        for (int i = 0; i < T; i++) {
            init();
            solution();
        }
    }

    public static void solution() {
        int answer = 0;
        Map<Character, List<Pos>> blockedListMap = new HashMap<>();
        String alphabets = "qwertyuiopasdfghjklzxcvbnm";
        for (int i = 0; i < alphabets.length(); i++) {
            blockedListMap.put(alphabets.charAt(i), new ArrayList<>());
        }


        Queue<Pos> queue = new ArrayDeque<>();
        List<Pos> edgePosList = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            edgePosList.add(new Pos(0, i));
            edgePosList.add(new Pos(N - 1, i));
        }

        for (int i = 1; i < N - 1; i++) {
            edgePosList.add(new Pos(i, 0));
            edgePosList.add(new Pos(i, M - 1));
        }


        for (Pos pos : edgePosList) {
            char value = mainMatrix[pos.row][pos.col];

            if (value != '*' && !Character.isUpperCase(value)) {
                queue.add(pos);
                continue;
            }

            if (Character.isUpperCase(value) && keyMap.get(Character.toLowerCase(value)) == null) {
                blockedListMap.get(Character.toLowerCase(value)).add(pos);
            }

            else if (Character.isUpperCase(value) && keyMap.get(Character.toLowerCase(value)) != null) {
                queue.add(pos);
            }
        }


        int[][] visited = new int[N][M];
        while (!queue.isEmpty()) {
            Pos curPos = queue.poll();
            if (visited[curPos.row][curPos.col] == 1) { continue; }
            visited[curPos.row][curPos.col] = 1;

            if (curPos.getValue() == '$') {
                answer += 1;
            }

            else if (Character.isLowerCase(curPos.getValue())) {
                keyMap.put(curPos.getValue(), 1);
                queue.addAll(blockedListMap.get(curPos.getValue()));

                blockedListMap.put(curPos.getValue(), new ArrayList<>());
            }

            else if (Character.isUpperCase(curPos.getValue()) && keyMap.get(Character.toLowerCase(curPos.getValue())) == null) {
                blockedListMap.get(Character.toLowerCase(curPos.getValue())).add(curPos);
            }

            for (Pos direction : directions) {
                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.isValidIndex()) { continue; }
                if (movedPos.getValue() == '*') { continue; }
                if (visited[movedPos.row][movedPos.col] == 1) { continue; }

                if (Character.isUpperCase(movedPos.getValue()) && keyMap.get(Character.toLowerCase(movedPos.getValue())) == null) {
                    blockedListMap.get(Character.toLowerCase(movedPos.getValue())).add(movedPos);
                    continue;
                }

                queue.add(movedPos);
            }
        }

        System.out.println(answer);
    }


    public static void init() throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        mainMatrix = new char[N][M];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String string = st.nextToken();

            for (int j = 0; j < M; j++) {
                mainMatrix[i][j] = string.charAt(j);
            }
        }

        keyMap = new HashMap<>();
        st = new StringTokenizer(br.readLine());
        String string = st.nextToken();
        for (int i = 0; i < string.length(); i++) {
            char key = string.charAt(i);
            keyMap.put(key, 1);
        }



    }
}