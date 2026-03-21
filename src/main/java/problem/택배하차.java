package problem;

import java.util.*;
import java.io.*;


public class 택배하차 {
    static int N, M;
    static int[][] mainMatrix;
    static Map<Integer, Box> boxMap;

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
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
                return false;
            }

            return true;
        }
    }

    public static class Box {
        int uniqueNum;
        Pos topLeftPos;
        int w;
        int h;

        public Box(int uniqueNum, Pos topLeftPos, int h, int w) {
            this.uniqueNum = uniqueNum;
            this.topLeftPos = topLeftPos;
            this.w = w;
            this.h = h;
        }

        public List<Pos> getDownPos() {
            List<Pos> downPosList = new ArrayList<>();
            for (int i = 0; i < w; i++) {
                Pos downPos = topLeftPos.addPos(new Pos(h, i));

                downPosList.add(downPos);
            }


            return downPosList;
        }

        public boolean fallOnce() {
            List<Pos> downPosList = getDownPos();

            for (Pos downPos : downPosList) {
                if (!downPos.isValidIndex()) { return false; }
                if (mainMatrix[downPos.row][downPos.col] != 0) { return false; }
            }

            this.topLeftPos = topLeftPos.addPos(new Pos(1, 0));
            return true;
        }

        public void fall() {
            while (true) {
                boolean result = fallOnce();

                if (result == false) { return; }
            }
        }


        public boolean isLeftExitPossible() {
            for (int j = 0; j < this.topLeftPos.col; j++) {
                for (int i = this.topLeftPos.row; i < this.topLeftPos.row + h; i++) {
                    if (mainMatrix[i][j] != 0) { return false; }
                }
            }

            return true;
        }


        public boolean isRightExitPossible() {
            Pos topRightPos = this.topLeftPos.addPos(new Pos(0, w));

            for (int j = topRightPos.col; j < N; j++) {
                for (int i = topRightPos.row; i < topRightPos.row + h; i++) {
                    if (mainMatrix[i][j] != 0) { return false; }
                }
            }

            return true;
        }

        public void applyToMatrix(int[][] matrix) {
            for (int i = 0; i < this.h; i++) {
                for (int j = 0; j < this.w; j++) {
                    matrix[topLeftPos.row + i][topLeftPos.col + j] = this.uniqueNum;
                }
            }
        }


        public void removeFromMatrix(int[][] matrix) {
            for (int i = 0; i < this.h; i++) {
                for (int j = 0; j < this.w; j++) {
                    matrix[topLeftPos.row + i][topLeftPos.col + j] = 0;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        List<Integer> answerList = new ArrayList<>();

        while (true) {
            int leftOutNum = leftPick();
            if (leftOutNum == 0) { break; }
            answerList.add(leftOutNum);

            int rightOutNum = rightPick();
            if (rightOutNum == 0) { break; }
            answerList.add(rightOutNum);


        }

        StringBuilder sb = new StringBuilder();
        for (int num : answerList) {
            sb.append(num + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int leftPick() {
        List<Integer> keyList = new ArrayList<>(boxMap.keySet());
        Collections.sort(keyList);

        int outBoxUniqueNum = 0;

        if (keyList.isEmpty()) {
            return 0;
        }

        for (int key : keyList) {
            Box box = boxMap.get(key);
            if (!box.isLeftExitPossible()) { continue; }

            outBoxUniqueNum = key;
            boxMap.remove(key);

            box.removeFromMatrix(mainMatrix);
            break;
        }

        fallAllBox();
        return outBoxUniqueNum;
    }

    public static int rightPick() {
        List<Integer> keyList = new ArrayList<>(boxMap.keySet());
        Collections.sort(keyList);

        int outBoxUniqueNum = 0;

        if (keyList.isEmpty()) {
            return 0;
        }

        for (int uniqueNum : keyList) {
            Box box = boxMap.get(uniqueNum);
            if (!box.isRightExitPossible()) { continue; }

            outBoxUniqueNum = uniqueNum;
            boxMap.remove(outBoxUniqueNum);

            box.removeFromMatrix(mainMatrix);
            break;
        }

        fallAllBox();
        return outBoxUniqueNum;
    }

    public static void fallAllBox() {
        Map<Integer, Integer> fallMap = new HashMap<>();

        for (int i = N - 1; i >= 0; i--) {
            for (int j = 0; j < N; j++) {
                int uniqueNum = mainMatrix[i][j];
                if (uniqueNum == 0) { continue; }
                if (fallMap.get(uniqueNum) != null) { continue; }

                boxMap.get(uniqueNum).removeFromMatrix(mainMatrix);
                boxMap.get(uniqueNum).fall();
                boxMap.get(uniqueNum).applyToMatrix(mainMatrix);
                fallMap.put(uniqueNum, 1);
            }
        }
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        mainMatrix = new int[N][N];
        boxMap = new HashMap<>();


        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int k = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken()) - 1;

            Box box = new Box(k, new Pos(-1, c), h, w);
            box.fall();
            box.applyToMatrix(mainMatrix);

            boxMap.put(k, box);

        }
    }
}