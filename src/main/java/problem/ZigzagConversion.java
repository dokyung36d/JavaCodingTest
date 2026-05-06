package problem;

import java.io.*;
import java.util.*;

class ZigzagConversion {
    static int N;

    public static class Pos implements Comparable<Pos> {
        int row;
        int col;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Pos addPos(Pos direction) {
            return new Pos(this.row + direction.row, this.col + direction.col);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            Pos anotherPos = (Pos) obj;
            if (this.row == anotherPos.row && this.col == anotherPos.col) { return true; }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }

        @Override
        public int compareTo(Pos anotherPos) {
            if (this.row != anotherPos.row) {
                return Integer.compare(this.row, anotherPos.row);
            }

            return Integer.compare(this.col, anotherPos.col);
        }
    }

    public static void main(String[] args) throws Exception {
        String string = "A";
        int numRows = 1;

        System.out.println(convert(string, numRows));
    }

    public static String convert(String s, int numRows) {
        N =s.length();

        List<Pos> directionList = new ArrayList<>();
        for (int i = 0; i < numRows - 1; i++) {
            directionList.add(new Pos(1, 0));
        }
        for (int i = 0; i < numRows - 1; i++) {
            directionList.add(new Pos(-1, 1));
        }

        if (numRows == 1) {
            directionList.add(new Pos(0, 1));
        }

        int directionLength = directionList.size();


        Map<Pos, Integer> posMap = new HashMap<>();

        int curIndex = 0;
        Pos curPos = new Pos(0, 0);
        for (int i = 0; i < N; i++) {
            posMap.put(curPos, i);

            Pos direction = directionList.get(curIndex);
            curPos = curPos.addPos(direction);

            curIndex = (curIndex + 1) % directionLength;
        }


        List<Pos> posList = new ArrayList<>(posMap.keySet());
        Collections.sort(posList);


        StringBuilder sb = new StringBuilder();
        for (Pos pos : posList) {
            sb.append(s.charAt(posMap.get(pos)));
        }

        return sb.toString();
    }
}