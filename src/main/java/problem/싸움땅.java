package problem;

import java.util.*;
import java.io.*;


public class 싸움땅 {
    static int N, M, K;
    static PriorityQueue<Integer>[][] gunMatrix;
    static Pos[] directions = {new Pos(-1, 0), new Pos(0, 1), new Pos(1, 0), new Pos(0, -1)};
    static Person[] personList;
    static int[] scoreList;

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
            if (this.row < 0 || this.row >= N || this.col < 0 || this.col >= N) {
                return false;
            }

            return true;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }
            Pos anotherPos = (Pos) obj;
            if (this.row == anotherPos.row && this.col == anotherPos.col) {
                return true;
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.row, this.col);
        }
    }

    public static class Person implements Comparable<Person> {
        int uniqueNum;
        Pos pos;
        int directionIndex;
        int initValue;
        int gunValue;

        public Person(int uniqueNum, Pos pos, int directionIndex, int initValue) {
            this.uniqueNum = uniqueNum;
            this.pos = pos;
            this.directionIndex = directionIndex;
            this.initValue = initValue;
            this.gunValue = 0;
        }

        public void rotateDirection() {
            this.directionIndex = (this.directionIndex + 1) % 4;
        }

        public void reverseDirection() {
            this.directionIndex = (this.directionIndex + 2) % 4;
        }

        public Pos getMovedPos() {
            return this.pos.addPos(directions[directionIndex]);
        }

        public int getTotalAttackValue() {
            return this.gunValue + this.initValue;
        }

        public void rotateWhenLose() {
            while (true) {
                Pos movedPos = this.getMovedPos();
                if (movedPos.isValidIndex() && findPersonInPos(movedPos) == null) {
                    break;
                }

                this.rotateDirection();
            }
        }

        public Person findPersonInSamePos() {
            for (int i = 0; i < M; i++) {
                if (personList[i].uniqueNum == this.uniqueNum) { continue; }

                if (this.pos.equals(personList[i].pos)) {
                    return personList[i];
                }
            }
            return null;
        }

        @Override
        public int compareTo(Person anotherPerson) {
            if (this.getTotalAttackValue() == anotherPerson.getTotalAttackValue()) {
                return Integer.compare(this.initValue, anotherPerson.initValue);
            }

            return Integer.compare(this.getTotalAttackValue(), anotherPerson.getTotalAttackValue());
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        for (int k = 0; k < K; k++) {
            solution();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < M; i++) {
            sb.append(scoreList[i] + " ");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static void solution() {
        for (int i = 0; i < M; i++) {
            Person person = personList[i];
            Pos movedPos = person.getMovedPos();
            if (!movedPos.isValidIndex()) {
                person.reverseDirection();
                movedPos = person.getMovedPos();
            }
            person.pos = movedPos;

            Person anotherPerson = person.findPersonInSamePos();
            if (anotherPerson == null) {
                gunMatrix[person.pos.row][person.pos.col].add(-person.gunValue);
                person.gunValue = -gunMatrix[person.pos.row][person.pos.col].poll();
                continue;
            }


            if (person.compareTo(anotherPerson) > 0) { //person win
                fight(person, anotherPerson);
            }
            else {
                fight(anotherPerson, person);
            }

        }
    }

    public static void fight(Person winPerson, Person losePerson) {
        scoreList[winPerson.uniqueNum] += winPerson.getTotalAttackValue() - losePerson.getTotalAttackValue();

        gunMatrix[losePerson.pos.row][losePerson.pos.col].add(-losePerson.gunValue);
        losePerson.gunValue = 0;
        losePerson.rotateWhenLose();
        Pos losePersonMovedPos = losePerson.getMovedPos();
        losePerson.pos = losePersonMovedPos;
        gunMatrix[losePersonMovedPos.row][losePersonMovedPos.col].add(0);
        losePerson.gunValue = -gunMatrix[losePersonMovedPos.row][losePersonMovedPos.col].poll();

        gunMatrix[winPerson.pos.row][winPerson.pos.col].add(-winPerson.gunValue);
        winPerson.gunValue = -gunMatrix[winPerson.pos.row][winPerson.pos.col].poll();
    }

    public static Person findPersonInPos(Pos pos) {
        for (int i = 0; i < M; i++) {
            if (personList[i].pos.equals(pos)) {
                return personList[i];
            }
        }

        return null;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        gunMatrix = new PriorityQueue[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                gunMatrix[i][j] = new PriorityQueue<>();
                int gunValue = Integer.parseInt(st.nextToken());
                gunMatrix[i][j].add(-gunValue);
            }
        }

        scoreList = new int[M];
        personList = new Person[M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int row = Integer.parseInt(st.nextToken()) - 1;
            int col = Integer.parseInt(st.nextToken()) - 1;
            int directionIndex = Integer.parseInt(st.nextToken());
            int initValue = Integer.parseInt(st.nextToken());

            personList[i] = new Person(i, new Pos(row, col), directionIndex, initValue);
        }
    }

}
