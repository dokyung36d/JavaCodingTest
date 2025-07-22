package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N;
	static int[] bossList, isParent;
	static Map<Integer, List<Integer>> childMap;

	public static class Node implements Comparable<Node> {
		int curNum;
		int time;

		public Node(int curNum, int time) {
			this.curNum = curNum;
			this.time = time;
		}

		public int compareTo(Node anotherNode) {
			return Integer.compare(-this.time, -anotherNode.time);
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		if (N == 1) {
			System.out.println(0);
			return;
		}
		boolean result = doesChildHaveChild(1);
		int answer = recursive(0);
		System.out.println(answer);
	}

	public static int recursive(int parentNum) {
		if (!doesChildHaveChild(parentNum)) {
			return childMap.get(parentNum).size();
		}

		int maxTime = 0;
		PriorityQueue<Node> pq = new PriorityQueue<>();
		for (int child : childMap.get(parentNum)) {
			pq.add(new Node(child, recursive(child)));
		}

		int calledTime = 0;
		while (!pq.isEmpty()) {
			Node node = pq.poll();
			calledTime += 1;

			maxTime = Math.max(maxTime, calledTime + node.time);

		}


		return maxTime;
	}

	public static boolean doesChildHaveChild(int parentNum) {
		List<Integer> childList = childMap.get(parentNum);

		for (int child : childList) {
			if (isParent[child] == 1) {
				return true;
			}
		}

		return false;
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		bossList = new int[N];
		isParent = new int[N];

		childMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			childMap.put(i, new ArrayList<>());
		}

		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			bossList[i] = Integer.parseInt(st.nextToken());

			if (i != 0) {
				isParent[bossList[i]] = 1;
				childMap.get(bossList[i]).add(i);
			}
		}
	}
}