package problem;

import java.util.*;
import java.io.*;


public class 마법의숲탐색 {
    static int R, C, K;
    static int[][] mainMatrix;
    static int[][] jumpMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static Pos[] underCheckPosList = {new Pos(2, 0), new Pos(1, -1), new Pos(1, 1)};
    static Pos[] leftCheckPosList = {new Pos(-1, - 1), new Pos(0, -2), new Pos(1, -1),
            new Pos(1, -2), new Pos(2, -1)};
    static Pos[] rightCheckPosList = {new Pos(-1, 1), new Pos(0, 2), new Pos(1, 1),
            new Pos(2, 1), new Pos(1, 2)};
    static GolemInitInfo[] golemInitInfoList;


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
            if (this.row < 0 || this.row >= R || this.col < 0 || this.col >= C) {
                return false;
            }

            return true;
        }

        public boolean isValidCenterPos() {
            if (this.row >= 0 && this.row <= 2) {
                return false;
            }

            return true;
        }
    }

    public static class GolemInitInfo {
        int uniqueNum;
        int startCol;
        int directionIndex;

        public GolemInitInfo(int uniqueNum, int startCol, int directionIndex) {
            this.uniqueNum = uniqueNum;
            this.startCol = startCol;
            this.directionIndex = directionIndex;
        }
    }

    public static class Golem {
        int uniqueNum;
        Pos curPos;
        int directionIndex;

        public Golem(int uniqueNum, Pos pos, int directionIndex) {
            this.uniqueNum = uniqueNum;
            this.curPos = pos;
            this.directionIndex = directionIndex;
        }

        public boolean isMovePossible(Pos[] checkDirectionList) {
            for (Pos checkDirection : checkDirectionList) {
                Pos movedPos = this.curPos.addPos(checkDirection);
                if (!movedPos.isValidIndex() || mainMatrix[movedPos.row][movedPos.col] != 0) {
                    return false;
                }
            }

            return true;
        }

        public Golem goDown() {
            return new Golem(this.uniqueNum, curPos.addPos(new Pos(1, 0)), this.directionIndex);
        }

        public Golem goLeft() {
            return new Golem(this.uniqueNum, curPos.addPos(new Pos(1, -1)), (this.directionIndex + 3) % 4);
        }

        public Golem goRight() {
            return new Golem(this.uniqueNum, curPos.addPos(new Pos(1, 1)), (this.directionIndex + 1) % 4);
        }


        public void applyToMainMatrix() {
            mainMatrix[this.curPos.row][this.curPos.col] = this.uniqueNum;
            for (Pos direction : directions) {
                Pos movedPos = this.curPos.addPos(direction);
                mainMatrix[movedPos.row][movedPos.col] = this.uniqueNum;
            }

            Pos jumpPos = this.curPos.addPos(directions[this.directionIndex]);
            jumpMatrix[jumpPos.row][jumpPos.col] = 1;
        }
    }

    public static Golem fallGolem(GolemInitInfo golemInitInfo) {
        Golem golem = new Golem(golemInitInfo.uniqueNum, new Pos(1, golemInitInfo.startCol), golemInitInfo.directionIndex);

        while (true) {
            if (golem.isMovePossible(underCheckPosList)) {
                golem = golem.goDown();
            }

            else if (golem.isMovePossible(leftCheckPosList)) {
                golem = golem.goLeft();
            }

            else if (golem.isMovePossible(rightCheckPosList)) {
                golem = golem.goRight();
            }

            else {
                break;
            }
        }

        return golem;
    }

    public static void main(String[] args) throws Exception {
        init();

        int answer = 0;

        for (GolemInitInfo golemInitInfo : golemInitInfoList) {
            Golem golem = fallGolem(golemInitInfo);
            golem.applyToMainMatrix();

            if (golem.curPos.row <= 3) {
                initMatrix();
                continue;
            }

            int maxRow = bfs(golem.curPos);
            answer += maxRow;
        }

        System.out.println(answer);

    }

    public static int bfs(Pos startPos) {
        int[][] visited = new int[R][C];

        Deque<Pos> queue = new ArrayDeque<>();
        queue.add(startPos);


        int maxRow = startPos.row;
        while (!queue.isEmpty()) {
            Pos curPos = queue.pollFirst();
            if (visited[curPos.row][curPos.col] == 1) { continue; }
            visited[curPos.row][curPos.col] = 1;

            maxRow = Math.max(maxRow, curPos.row);

            for (Pos direction : directions) {
                Pos movedPos = curPos.addPos(direction);
                if (!movedPos.isValidIndex() || visited[movedPos.row][movedPos.col] == 1) { continue; }
                if (mainMatrix[movedPos.row][movedPos.col] == 0) { continue; }

                if (mainMatrix[curPos.row][curPos.col] == mainMatrix[movedPos.row][movedPos.col]) {
                    queue.add(movedPos);
                }
                else if (jumpMatrix[curPos.row][curPos.col] == 1) {
                    queue.add(movedPos);
                }
            }
        }

        return maxRow - 2;
    }

    public static void initMatrix() {
        mainMatrix = new int[R][C];
        jumpMatrix = new int[R][C];
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        R += 3;
        mainMatrix = new int[R][C];
        jumpMatrix = new int[R][C];

        golemInitInfoList = new GolemInitInfo[K];
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int col = Integer.parseInt(st.nextToken()) - 1;
            int directionIndex = Integer.parseInt(st.nextToken());

            golemInitInfoList[i] = new GolemInitInfo(i + 1, col, directionIndex);
        }

    }
}