package problem;

import java.util.*;
import java.io.*;


public class DanceDanceRevolution {
    static List<Integer> commandList;

    public static class State {
        int left;
        int right;

        public State(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.left, this.right);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }
            State anotherState = (State) obj;

            if (this.left == anotherState.left && this.right == anotherState.right) {
                return true;
            }

            return false;
        }


    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        Map<State, Integer> dpMap = new HashMap<>();
        dpMap.put(new State(0, 0), 0);

        for (int command : commandList) {
            dpMap = setUpdatedDpMap(command, dpMap);
        }

        int answer = Integer.MAX_VALUE / 2;
        for (State state : dpMap.keySet()) {
            answer = Math.min(answer, dpMap.get(state));
        }

        System.out.println(answer);
    }

    public static Map<State, Integer> setUpdatedDpMap(int command, Map<State, Integer> dpMap) {
        Map<State, Integer> updatedDpMap = new HashMap<>();
        for (State state : dpMap.keySet()) {
            int prevCost = dpMap.getOrDefault(state, Integer.MAX_VALUE / 2);

            if (state.left != command) {
                int cost = getCost(state.right, command);
                State updatedState = new State(state.left, command);

                int totalCost = Math.min(prevCost + cost, updatedDpMap.getOrDefault(updatedState, Integer.MAX_VALUE / 2));
                updatedDpMap.put(updatedState, totalCost);
            }

            if (state.right != command) {
                int cost = getCost(state.left, command);
                State updatedState = new State(command, state.right);

                int totalCost = Math.min(prevCost + cost, updatedDpMap.getOrDefault(updatedState, Integer.MAX_VALUE / 2));
                updatedDpMap.put(updatedState, totalCost);
            }
        }

        return updatedDpMap;
    }

    public static int getCost(int from, int to) {
        if (from == 0) { return 2; }
        if (from == to) { return 1; }
        if (Math.abs(from - to) == 2) { return 4; }
        return 3;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        commandList = new ArrayList<>();
        while (true) {
            int command = Integer.parseInt(st.nextToken());
            if (command == 0) { break; }

            commandList.add(command);
        }
    }
}