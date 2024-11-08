package CodeTreeTour;

import java.util.*;
import java.io.*;

public class Main {
    public static class Node {
        int uniqueNum;
        HashMap<Integer, ArrayList<Integer>> connectedNodeDict = new HashMap<>();

        Node(int uniqueNum) {
            this.uniqueNum = uniqueNum;
        }
    }

    public static class TravelPresent {
        int uniqueNum;
        int revenue;
        int dest;
        int profit;

        TravelPresent(int uniqueNum, int revenue, int dest) {
            this.uniqueNum = uniqueNum;
            this.revenue = revenue;
            this.dest = dest;
        }
    }
    public static int Q;
    public static int startPoint = 0;
    public static HashMap<Integer, Node> graphMap = new HashMap<>();


    public static void main(String[] args) throws IOException {
        init();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for (int i = 0; i < Q; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            int command_kind = Integer.parseInt(st.nextToken());
            if (command_kind == 100) {
                prcoess100(st);
                System.out.println("hello");
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


            graphMap.computeIfAbsent(firstNodeUniqueNum, k -> new Node(firstNodeUniqueNum));
            graphMap.computeIfAbsent(secondNodeUniqueNum, k -> new Node(secondNodeUniqueNum));

            graphMap.get(firstNodeUniqueNum).connectedNodeDict.computeIfAbsent(secondNodeUniqueNum, k -> new ArrayList<>()).add(value);
            graphMap.get(secondNodeUniqueNum).connectedNodeDict.computeIfAbsent(firstNodeUniqueNum, k -> new ArrayList<>()).add(value);


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

        Q = Integer.parseInt(st.nextToken());
    }
    
    public static Integer getBestWay(int dest) {
        //bfs로 풀기
        Queue<Integer> queue = new LinkedList<>();

        return 0;
    }
}
