package problem;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class parent {
    public static int n, m;
    public static int[][] map_info;
    public static int[][] user_info;


    // 각
    public static Queue<int[][]> queue = new LinkedList<>();

    public static HashMap<Integer, int[]> directionDict = new HashMap<>();

    public static int answer = 0;


    public static void main_parent(String[] args) throws IOException {
        init();
        queue.add(user_info);

        while (true) {
            int[][] cur_poses = queue.poll();



            for (int i = 0; i < m; i ++) {
                ArrayList<Object> info = new ArrayList<>();

                for (int j = 0; j < 4; j++) {
                    int[] moved_pos = move(cur_poses[0], j);
                    info.add(new Object[] {moved_pos, j});
                }
                //info에
            }
        }
    }

    //이후에 같은 위치에 동일한 방향인 경우는 맨 마지막에 처리
    public static ArrayList<Integer> generatePermutationFirst(int[] pos0) throws IOException {
        ArrayList<Integer> firstList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            firstList.add(i);
        }

        return firstList;
    }

    public static void generatePermutationSecond(int[] pos0, int[] pos1) {
        if (pos0 == pos1) {
            ArrayList<int[]> returnList = new ArrayList<>();

//            for (int i = 0; i < 4; i ++) {
//                for (int j = 0; j < 4; j++) {
//                    returnList.add({i, j});
//                }
            }
        }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        map_info = new int[n][n];
        user_info = new int[m][2];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < n; j++) {
                int temp = Integer.parseInt(st.nextToken());
                map_info[i][j] = temp;
            }
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());

            int row = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken());

            user_info[i][0] = row;
            user_info[i][1] = col;
        }


        directionDict.put(0, new int[] {-1, 0});
        directionDict.put(1, new int[] {1, 0});
        directionDict.put(2, new int[] {0, -1});
        directionDict.put(3, new int[] {0, 1});
    }

    public static boolean checkIndex(int[] position) {
        int row = position[0];
        int col = position[1];

        if (row < 0 || row >= n || col < 0 || col > n) {
            return false;
        }

        return true;
    }

    public static int[] move(int[] cur_position, int move_index) {
        int row = cur_position[0];
        int col = cur_position[1];

        int[] delta = directionDict.get(move_index);

        int moved_row = row + delta[0];
        int moved_col = col + delta[1];

        return new int[] {moved_row, moved_col};
    }

    public static int getScore(HashMap<Integer, int[]> posDict) {
        int total_score = 0;

        for (int i = 0; i < m; i ++) {
            int[] pos = posDict.get(i);

            total_score += map_info[pos[0]][pos[1]];
        }

        return total_score;
    }

    public static boolean checkMoveAvaialbe(ArrayList<Object> info) {
        ArrayList<int[]> pair = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            Object[] i_info = (Object[]) info.get(i);
            int[] i_pos = (int[]) i_info[0];
            int i_direction = (int) i_info[1];

            for (int j = i+1; j < m; j++) {
                Object[] j_info = (Object[]) info.get(j);
                int[] j_pos = (int[]) j_info[0];
                int j_direction = (int) j_info[1];


                if (i_pos == j_pos && i_direction == j_direction) {
                    return false;
                }
            }
        }
        return true;
    }
}
