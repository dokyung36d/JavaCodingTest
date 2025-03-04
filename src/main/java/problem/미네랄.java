package problem;

import java.util.*;
import java.io.*;


public class 미네랄 {
    static int R, C, N;
    static char[][] mainMatrix;
    static int[] commandList;
    static Pos[] directions = {new Pos(1, 0), new Pos(0, -1), new Pos(0, 1), new Pos(-1, 0)};

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

        public boolean isValidIndex() {
            if (this.row < 0 || this.row >= R || this.col < 0 || this.col >= C) {
                return false;
            }

            return true;
        }

        public Pos multiply(int num) {
            return new Pos(this.row * num, this.col * num);
        }
    }

    public static void main(String[] args) throws IOException {
        init();
        for (int i = 0; i < N; i++) {
            int attackRow = R - commandList[i];
            Pos attackedPos = getAttackedPos(attackRow, i % 2 == 0);
            if (attackedPos == null) { continue; }

            solution(attackedPos);

        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                sb.append(mainMatrix[i][j]);
            }
            sb.append("\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void solution(Pos attackedPos) {
        mainMatrix[attackedPos.row][attackedPos.col] = '.';
        char[][] updatedMainMatrix = new char[R][C];
        int[][] visited = new int[R][C];

        List<List<Pos>> clusters = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Pos movedPos = attackedPos.addPos(directions[i]);
            if (!movedPos.isValidIndex() || mainMatrix[movedPos.row][movedPos.col] == '.') { continue; }
            if (visited[movedPos.row][movedPos.col] == 1) { continue; }

            List<Pos> cluster = bfs(movedPos);
            clusters.add(cluster);
            for (Pos clusterElementPos : cluster) {
                visited[clusterElementPos.row][clusterElementPos.col] = 1;
            }
        }

        for (int i = 0; i < R; i++) {
            Arrays.fill(updatedMainMatrix[i], '.');
            for (int j = 0; j < C; j++) {
                if (visited[i][j] == 1 || mainMatrix[i][j] == '.') { continue; }
                updatedMainMatrix[i][j] = 'x';
            }
        }

        List<Pos> downCluster = null;
        for (List<Pos> cluster : clusters) {
            int numDown = getNumDown(updatedMainMatrix, cluster);
            if (numDown > 0) {
                downCluster = cluster;
                continue;
            }

            for (Pos clusterElementPos : cluster) {
                updatedMainMatrix[clusterElementPos.row][clusterElementPos.col] = 'x';
            }
        }

        if (downCluster == null) {
            mainMatrix = updatedMainMatrix;
            return;
        }

        Pos downDirection = new Pos(1, 0);
        int numDown = getNumDown(updatedMainMatrix, downCluster);
        for (Pos pos : downCluster) {
            Pos movedPos = pos.addPos(downDirection.multiply(numDown));
            updatedMainMatrix[movedPos.row][movedPos.col] = 'x';
        }
        mainMatrix = updatedMainMatrix;
    }

    public static List<Pos> bfs(Pos startPos) {
        int[][] visited = new int[R][C];
        Deque<Pos> queue = new ArrayDeque<>();
        queue.add(startPos);

        while (!queue.isEmpty()) {
            Pos curPos = queue.pollFirst();
            if (visited[curPos.row][curPos.col] == 1) { continue; }
            visited[curPos.row][curPos.col] = 1;

            for (Pos direction : directions) {
                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.isValidIndex() || visited[movedPos.row][movedPos.col] == 1) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] == '.') { continue; }

                queue.add(movedPos);
            }
        }

        List<Pos> visitedPosList = new ArrayList<>();
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                if (visited[i][j] == 1) {
                    visitedPosList.add(new Pos(i, j));
                }
            }
        }

        return visitedPosList;
    }

    public static int getNumDown(char[][] matrix, List<Pos> cluster) {
        Pos downDirection = new Pos(1,0 );
        int numDown = 0;
        int flag = 0;

        while (true) {
            for (Pos clusterElementPos : cluster) {
                Pos movedPos = clusterElementPos.addPos(downDirection.multiply(numDown));
                if (!movedPos.isValidIndex() || matrix[movedPos.row][movedPos.col] == 'x') {
                    flag = 1;
                    break;
                }
            }

            if (flag == 1) {
                break;
            }
            numDown += 1;
        }

        return numDown - 1;
    }

    public static Pos getAttackedPos(int attackRow, boolean isLeftAttack) {
        Pos attackedPos = null;
        if (isLeftAttack) {
            for (int j = 0; j < C; j++) {
                if (mainMatrix[attackRow][j] == 'x') {
                    attackedPos = new Pos(attackRow, j);
                    break;
                }
            }
        }
        else {
            for (int j = C - 1; j >= 0; j--) {
                if (mainMatrix[attackRow][j] == 'x') {
                    attackedPos = new Pos(attackRow, j);
                    break;
                }
            }
        }

        return attackedPos;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        mainMatrix = new char[R][C];
        for (int i = 0; i < R; i++) {
            st = new StringTokenizer(br.readLine());
            String str = st.nextToken();
            for (int j = 0; j < C; j++) {
                mainMatrix[i][j] = str.charAt(j);
            }
        }

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        commandList = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            commandList[i] = Integer.parseInt(st.nextToken());
        }
    }
}