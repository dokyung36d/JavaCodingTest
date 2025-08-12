package problem;

import java.util.*;
import java.io.*;


public class 거짓말 {
    static int N, M;
    static Map<Integer, List<Integer>> humanPartyMap, partyHumanMap, partyPartyGraphMap;
    static Deque<Integer> truthHumanQueue;

    public static class PartyEdge {
        int party1;
        int party2;

        public PartyEdge(int party1, int party2) {
            this.party1 = Math.min(party1, party2);
            this.party2 = Math.max(party1, party2);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) { return true; }
            if (obj == null || this.getClass() != obj.getClass()) { return false; }

            PartyEdge anotherPartyEdge = (PartyEdge) obj;
            if (this.party1 == anotherPartyEdge.party1 && this.party2 == anotherPartyEdge.party2) { return true; }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.party1, this.party2);
        }
    }

    public static void main(String[] args) throws Exception {
        init();
        solution();
    }

    public static void solution() {
        Deque<Integer> truthPartyQueue = new ArrayDeque<>();
        int[] isTruthGroup = new int[M];

        while (!truthHumanQueue.isEmpty()) {
            int truthHuman = truthHumanQueue.poll();

            for (int party : humanPartyMap.get(truthHuman)) {
                truthPartyQueue.add(party);
            }
        }


        while (!truthPartyQueue.isEmpty()) {
            int truthParty = truthPartyQueue.poll();
            if (isTruthGroup[truthParty] == 1) { continue; }
            isTruthGroup[truthParty] = 1;

            for (int nearParty : partyPartyGraphMap.get(truthParty)) {
                if (isTruthGroup[nearParty] == 1) { continue; }
                truthPartyQueue.add(nearParty);
            }
        }

        int answer = 0;
        for (int i = 0; i < M; i++) {
            if (isTruthGroup[i] == 0) {
                answer += 1;
            }
        }

        System.out.println(answer);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        humanPartyMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            humanPartyMap.put(i, new ArrayList<>());
        }

        partyHumanMap = new HashMap<>();
        for (int i = 0; i < M; i++) {
            partyHumanMap.put(i, new ArrayList<>());
        }

        partyPartyGraphMap = new HashMap<>();
        for (int i = 0; i < M; i++) {
            partyPartyGraphMap.put(i, new ArrayList<>());
        }


        truthHumanQueue = new ArrayDeque<>();
        st = new StringTokenizer(br.readLine());
        int numTruthPeople = Integer.parseInt(st.nextToken());
        for (int i = 0; i < numTruthPeople; i++) {
            truthHumanQueue.add(Integer.parseInt(st.nextToken()) - 1);
        }


        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int numPartyPerson = Integer.parseInt(st.nextToken());
            for (int j = 0; j < numPartyPerson; j++) {
                int person = Integer.parseInt(st.nextToken()) - 1;
                partyHumanMap.get(i).add(person);
                humanPartyMap.get(person).add(i);
            }
        }

        Map<PartyEdge, Integer> partyEdgeMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            for (int party1Index = 0; party1Index < humanPartyMap.get(i).size(); party1Index++) {
                for (int party2Index = party1Index + 1; party2Index < humanPartyMap.get(i).size(); party2Index++) {
                    int party1 = humanPartyMap.get(i).get(party1Index);
                    int party2 = humanPartyMap.get(i).get(party2Index);

                    partyEdgeMap.put(new PartyEdge(party1, party2), 1);
                }
            }
        }


        for (PartyEdge partyEdge : partyEdgeMap.keySet()) {
            partyPartyGraphMap.get(partyEdge.party1).add(partyEdge.party2);
            partyPartyGraphMap.get(partyEdge.party2).add(partyEdge.party1);
        }

    }
}