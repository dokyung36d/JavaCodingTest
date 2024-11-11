package problem;

import java.util.*;
import java.io.*;

public class CodeTreeTour {
    public static class Node {
        int uniqueNum;

        HashMap<Integer, Integer> connectedNodeDict = new HashMap<>();

        Node(int uniqueNum, int opposNodeUniqueNum, int value) {
            this.uniqueNum = uniqueNum;
            this.connectedNodeDict.put(opposNodeUniqueNum, value);
        }
    }

    public static class TravelPresent implements Comparable<TravelPresent> {
        int id;
        int dest;
        int profit;
        int revenue;


        TravelPresent(int id, int profit, int dest, int revenue) {
            this.id = id;
            this.profit = profit;
            this.dest = dest;
            this.revenue = revenue;
        }

        @Override
        public int compareTo(TravelPresent anotherPresent) {
            if (this.profit != anotherPresent.profit) {
                return Integer.compare(-this.profit, -anotherPresent.profit);
            }

            return Integer.compare(this.id, anotherPresent.id);
        }

        public void setProfit(int money) {
            int newProfit = this.revenue - money;

            if (newProfit < 0) {
                this.profit = -1;
                return ;
            }

            this.profit = newProfit;

        }
    }

    public static class QueueNode implements Comparable<QueueNode> {

        int curPos;
        int usedMoney;

        QueueNode(int curPos, int usedMoney) {
            this.curPos = curPos;
            this.usedMoney = usedMoney;
        }

        @Override
        public int compareTo(QueueNode anotherNode) {
            return Integer.compare(this.usedMoney, anotherNode.usedMoney);
        }
    }
    public static int Q;
    public static int startPoint = 0;
    public static HashMap<Integer, Node> graphMap = new HashMap<>();
    public static ArrayList<Integer> dijkstra;
    public static PriorityQueue<TravelPresent> travelPresentPriorityQueue = new PriorityQueue<>();
    public static HashMap<Integer, Integer> travelPresentMap = new HashMap<>();
    public static final int initValue = (int) Math.pow(10, 5);

    public static void main(String[] args) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        int i = 0;
//        while (true) {
//            String x = br.readLine();
//            i++;
//            if (x == null) {
//                break;
//            }
//
//            System.out.println(i);
//        }
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        Q = Integer.parseInt(st.nextToken());

        init();
        for (int i = 0; i < Q; i++) {

//            if (line == null) { break; }
            st = new StringTokenizer(br.readLine());


            int command_kind = Integer.parseInt(st.nextToken());
            if (command_kind == 100) {
                prcoess100(st);
            }

            else if (command_kind == 200) {
                prcoess200(st);
            }

            else if (command_kind == 300) {
                prcoess300(st);
            }

            else if (command_kind == 400) {
                prcoess400();
            }

            else if (command_kind == 500) {
                process500(st);
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

            if (firstNodeUniqueNum == secondNodeUniqueNum) {
                continue;
            }


            graphMap.computeIfAbsent(firstNodeUniqueNum, k -> new Node(firstNodeUniqueNum, secondNodeUniqueNum, value));
            graphMap.computeIfAbsent(secondNodeUniqueNum, k -> new Node(secondNodeUniqueNum, firstNodeUniqueNum, value));

            // MAX_VALUE : 100
            graphMap.get(firstNodeUniqueNum).connectedNodeDict.computeIfAbsent(secondNodeUniqueNum, k -> 101);
            graphMap.get(secondNodeUniqueNum).connectedNodeDict.computeIfAbsent(firstNodeUniqueNum, k -> 101);


            int min_value = Math.max(graphMap.get(firstNodeUniqueNum).connectedNodeDict.get(secondNodeUniqueNum),
                    graphMap.get(secondNodeUniqueNum).connectedNodeDict.get(firstNodeUniqueNum));
            if (value < min_value) {
                graphMap.get(firstNodeUniqueNum).connectedNodeDict.put(secondNodeUniqueNum, value);
                graphMap.get(secondNodeUniqueNum).connectedNodeDict.put(firstNodeUniqueNum, value);
            }
        }

        updateDijkstra();
    }
    public static void prcoess200(StringTokenizer st) throws IOException {
        int id = Integer.parseInt(st.nextToken());
        int revenue = Integer.parseInt(st.nextToken());
        int dest = Integer.parseInt(st.nextToken());

        int profit;
        int spentMoney = dijkstra.get(dest);
        profit = revenue - spentMoney;


        TravelPresent newTravelPresent = new TravelPresent(id, profit, dest, revenue);
        travelPresentPriorityQueue.add(newTravelPresent);
        travelPresentMap.put(id, 1);

    }

    public static void prcoess300(StringTokenizer st) throws IOException {
        int removeId = Integer.parseInt(st.nextToken());
        travelPresentMap.remove(removeId);
    }

    public static void prcoess400() throws IOException {
        while (!travelPresentPriorityQueue.isEmpty()) {
            TravelPresent travelPresent = travelPresentPriorityQueue.poll();

            //In case, the present is removed
            if (travelPresentMap.get(travelPresent.id) == null) { continue; }

            if (travelPresent.profit < 0) {
                System.out.println(-1);

                // that present is sold, but need to store in queue in case when start change
                travelPresentPriorityQueue.add(travelPresent);
                return;
            }

            System.out.println(travelPresent.id);
            travelPresentMap.remove(travelPresent.id);
            return ;
        }

        System.out.println(-1);
    }

    public static void process500(StringTokenizer st) throws IOException {
        int newStartPoint = Integer.parseInt(st.nextToken());
        startPoint = newStartPoint;

        updateDijkstra();
        ArrayList<TravelPresent> travelPresents = new ArrayList<>();

        while (!travelPresentPriorityQueue.isEmpty()) {
            TravelPresent travelPresent = travelPresentPriorityQueue.poll();
            travelPresents.add(travelPresent);
        }

        for (TravelPresent travelPresent : travelPresents) {
            int spentMoney = dijkstra.get(travelPresent.dest);
            travelPresent.setProfit(spentMoney);

            travelPresentPriorityQueue.add(travelPresent);
        }

    }

    public static void init() throws IOException {
        dijkstra = new ArrayList<>(Collections.nCopies(2000, initValue));
    }

    public static void updateDijkstra() {
//        int destValue = dijkstra.get(dest);

        //value previous calculated
//        if (destValue != initValue) { return destValue; }
        dijkstra = new ArrayList<>(Collections.nCopies(2000, initValue));

        PriorityQueue<QueueNode> queue = new PriorityQueue<>();
        QueueNode initNode = new QueueNode(startPoint, 0);
        queue.add(initNode);

        dijkstra.set(startPoint, 0);

        if (graphMap.get(startPoint) == null) {
            return;
        }

        ArrayList<Integer> connectedNodesUniqueNum = new ArrayList<>(graphMap.get(startPoint).connectedNodeDict.keySet());

        // at here, should no change dijkstra
//        for (Integer connectedNodeUniqueNum : connectedNodesUniqueNum) {
//            Node node = graphMap.get(connectedNodeUniqueNum);
//            int money = graphMap.get(connectedNodeUniqueNum).connectedNodeDict.get(startPoint);
//
//            QueueNode queueNode = new QueueNode(node.uniqueNum, money);
//            queue.add(queueNode);
//        }

        while (!queue.isEmpty()) {
            QueueNode queueNode = queue.poll();

            //Final change
            if (queueNode.usedMoney < dijkstra.get(queueNode.curPos)) {
                dijkstra.set(queueNode.curPos, queueNode.usedMoney);
            }

            ArrayList<Integer> nearNodes = new ArrayList<>(graphMap.get(queueNode.curPos).connectedNodeDict.keySet());

            for (Integer nearNode : nearNodes) {
                if (dijkstra.get(nearNode) != initValue) { continue; }

                int spentMoney = graphMap.get(queueNode.curPos).connectedNodeDict.get(nearNode);

                QueueNode newQueueNode = new QueueNode(nearNode, queueNode.usedMoney + spentMoney);
                queue.add(newQueueNode);
            }
        }
    }
}