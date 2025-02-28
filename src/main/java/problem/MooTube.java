package problem;

import java.util.*;
import java.io.*;


public class MooTube {
    static int N, Q;
    static Map<Integer, List<Edge>> graphMap;
    static Question[] questions;

    public static class Question {
        int k;
        int v;

        public Question(int k, int v) {
            this.k = k;
            this.v = v;
        }
    }

    public static class Edge {
        int destVertex;
        int usado;

        public Edge(int destVertex, int usado) {
            this.destVertex = destVertex;
            this.usado = usado;
        }
    }

    public static void main(String[] args) throws Exception {
        init();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Q; i++) {
            int answer = solution(questions[i]);
            sb.append(answer + "\n");
        }

        System.out.println(sb.toString().substring(0, sb.length() - 1));
    }

    public static int solution(Question question) {
        int answer = 0;
        int[] visited = new int[N];
        Deque<Edge> queue = new ArrayDeque<>();
        for (Edge edge : graphMap.get(question.v)) {
            if (edge.usado < question.k) { continue; }

            answer += 1;
            visited[edge.destVertex] = 1;
            queue.add(edge);
        }

        visited[question.v] = 1;
        while (!queue.isEmpty()) {
            int curNode = queue.pollFirst().destVertex;

            for (Edge edge : graphMap.get(curNode)) {
                if (visited[edge.destVertex] == 1) { continue; }
                if (edge.usado < question.k) { continue; }

                visited[edge.destVertex] = 1;
                answer += 1;
                queue.add(edge);
            }
        }

        return answer;
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        graphMap = new HashMap<>();
        for (int i = 0; i < N; i++) {
            graphMap.put(i, new ArrayList<>());
        }

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int vertex1 = Integer.parseInt(st.nextToken()) - 1;
            int vertex2 = Integer.parseInt(st.nextToken()) - 1;
            int usado = Integer.parseInt(st.nextToken());

            graphMap.get(vertex1).add(new Edge(vertex2, usado));
            graphMap.get(vertex2).add(new Edge(vertex1, usado));
        }


        questions = new Question[Q];
        for (int i = 0; i < Q; i++) {
            st = new StringTokenizer(br.readLine());
            int k = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken()) - 1;
            questions[i] = new Question(k, v);
        }
    }
}