package problem;

import java.util.*;
import java.io.*;

public class Main {
	static int N, M;
	static Map<Node, Integer> graphMap;
	static int[] numPointedList;

	public static class Node {
		int from;
		int to;

		public Node(int from, int to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.from, this.to);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) { return true; }
			if (obj == null || this.getClass() != obj.getClass()) { return false; }

			Node anotherNode = (Node) obj;
			if (this.from == anotherNode.from && this.to == anotherNode.to) { return true; }
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		StringBuilder sb = new StringBuilder();
		int numPolledSinger = 0;
		Queue<Integer> queue = new ArrayDeque<>();
		int[] polled = new int[N];

		for (int i = 0; i < N; i++) {
			if (numPointedList[i] != 0) { continue; }
			queue.add(i);
			polled[i] = 1;
		}


		while (!queue.isEmpty()) {
			int singer = queue.poll();
			numPolledSinger += 1;

			sb.append(singer + 1);
			sb.append("\n");

			for (int nextSinger = 0; nextSinger < N; nextSinger++) {
				if (polled[nextSinger] == 1) { continue; }
				int numDirected = graphMap.getOrDefault(new Node(singer, nextSinger), 0);
				numPointedList[nextSinger] -= numDirected;

				if (numPointedList[nextSinger] == 0) {
					queue.add(nextSinger);
					polled[nextSinger] = 1;
				}
			}
		}

		if (numPolledSinger == N) {
			System.out.println(sb.toString().substring(0, sb.length() - 1));
		}
		else {
			System.out.println(0);
		}
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		numPointedList = new int[N];
		graphMap = new HashMap<>();

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());

			int numSinger = Integer.parseInt(st.nextToken());
			int[] singerList = new int[numSinger];

			for (int j = 0; j < numSinger; j++) {
				singerList[j] = Integer.parseInt(st.nextToken()) - 1;
			}

			for (int firstSingerIndex = 0; firstSingerIndex < numSinger; firstSingerIndex++) {
				for (int secondSingerIndex = firstSingerIndex + 1; secondSingerIndex < numSinger; secondSingerIndex++) {
					Node node = new Node(singerList[firstSingerIndex], singerList[secondSingerIndex]);

					graphMap.put(node, graphMap.getOrDefault(node, 0) + 1);
					numPointedList[singerList[secondSingerIndex]] += 1;
				}
			}
		}
	}

}
