package problem;

import java.util.*;
import java.io.*;

public class 수영대회결승전 {
    public static class Node {
        int depth;
        int row;
        int col;


        public Node(int depth, int row, int col) {
            this.depth = depth;
            this.row = row;
            this.col = col;
        }

        public Node addNode(Node anotherNode) {
            return new Node(this.depth + 1, this.row + anotherNode.row, this.col + anotherNode.col);
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
        int[][] matrix = new int[N][N];
        Map<List<Integer>, Integer> visited = new HashMap<>();

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                matrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        int[] startPos = new int[2];
        int[] destPos = new int[2];
        st = new StringTokenizer(br.readLine());
        for (int i =0 ; i < 2; i++) {
            startPos[i] = Integer.parseInt(st.nextToken());
        }
        st = new StringTokenizer(br.readLine());
        for (int i =0 ; i < 2; i++) {
            destPos[i] = Integer.parseInt(st.nextToken());
        }
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        Deque<Node> queue = new ArrayDeque<>();
        queue.add(new Node(0,startPos[0], startPos[1]));

        while (!queue.isEmpty()) {
            Node node = queue.pollFirst();
            //System.out.println(Arrays.asList(node.row, node.col));

            for (int[] direction : directions) {
                Node movedNode = node.addNode(new Node(0, direction[0], direction[1]));
                if (!checkIndex(movedNode, N)) {
                    continue;
                }

                if (visited.get(Arrays.asList(movedNode.row, movedNode.col)) != null) {
                    continue;
                }

                if (matrix[movedNode.row][movedNode.col] == 1) {
                    continue;
                }

                if (matrix[movedNode.row][movedNode.col] == 2 && movedNode.depth % 3 != 0) {
                    queue.addLast(new Node(node.depth + 1, node.row, node.col));
                    continue;
                }

                if (movedNode.row == destPos[0] && movedNode.col == destPos[1]) {
                    return movedNode.depth;
                }
                queue.addLast(movedNode);
                visited.put(Arrays.asList(movedNode.row, movedNode.col), 1);
            }
        }


        return -1;
    }

    public static boolean checkIndex(Node node, int N) {
        if (0 <= node.row && node.row < N && 0 <= node.col && node.col < N) {
            return true;
        }

        return false;
    }
}