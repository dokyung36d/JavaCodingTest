package CodeTreeTour;

import java.io.IOException;
import java.util.*;
import java.io.*;

public class main {

    public static class Node {
        int uniqueNum;
        HashMap<Integer, ArrayList<Integer>> connectedNodeDict = new HashMap<>();

        Node(int uniqueNum) {
            this.uniqueNum = uniqueNum;
        }
    }
    public static int Q;
    public static HashMap<Integer, Node> graphMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for (int i = 0; i < Q; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            int command_kind = Integer.parseInt(st.nextToken());

            if (command_kind == 100) {
                prcoess100(st);
            }

            else if (command_kind == 200) {

            }

            else if (command_kind == 300) {

            }

            else if (command_kind == 400) {

            }
        }

    }
    public static void prcoess100(StringTokenizer st) throws IOException {
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        while (st.hasMoreTokens()) {
            int firstNodeUniqueNum = Integer.parseInt(st.nextToken());
            int secondNodeUniqueNum = Integer.parseInt(st.nextToken());
            int value = Integer.parseInt(st.nextToken());

//            Node firstNode = graphMap.get(firstNodeUniqueNum);
        }


    }
    public static void prcoess200() throws IOException {

    }

    public static void prcoess300() throws IOException {

    }

    public static void prcoess400() throws IOException {

    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int Q = Integer.parseInt(st.nextToken());
    }
}
