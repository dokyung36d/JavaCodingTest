package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, M ,K;
	static int[] candyList;
	static Edge[] edgeList;
	static int[] parentList;

	public static class Edge {
		int num1;
		int num2;

		public Edge(int num1, int num2) {
			this.num1 = num1;
			this.num2 = num2;
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		parentList = new int[N];
		for (int i = 0; i < N; i++) {
			parentList[i] = i;
		}

		for (Edge edge : edgeList) {
			int num1Parent = findParent(edge.num1);
			int num2Parent = findParent(edge.num2);

			if (num1Parent == num2Parent) { continue; }
			union(num1Parent, num2Parent);
		}

		Map<Integer, Integer> groupSizeMap = new HashMap<>();
		Map<Integer, Integer> groupValueMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			int parent = findParent(i);

			groupSizeMap.put(parent, groupSizeMap.getOrDefault(parent, 0) + 1);
			groupValueMap.put(parent, groupValueMap.getOrDefault(parent, 0) + candyList[i]);
		}


		List<Integer> parentList = new ArrayList<>(groupSizeMap.keySet());
		int[][] dpMatrix = new int[parentList.size() + 1][K];

		for (int i = 0; i < parentList.size(); i++) {
			int groupSize = groupSizeMap.get(parentList.get(i));
			int groupValue = groupValueMap.get(parentList.get(i));
			for (int j = 0; j < K; j++) {
				dpMatrix[i + 1][j] = dpMatrix[i][j];
				if (groupSize > j) { continue; }

				dpMatrix[i + 1][j] = Math.max(dpMatrix[i + 1][j], dpMatrix[i][j - groupSize] + groupValue);
			}
		}

		System.out.println(dpMatrix[parentList.size()][K - 1]);
	}

	public static int findParent(int num) {
		if (num == parentList[num]) { return num; }

		return parentList[num] = findParent(parentList[num]);
	}

	public static void union(int num1, int num2) {
		int num1Parent = findParent(num1);
		int num2Parent = findParent(num2);

		if (num1Parent == num2Parent) { return; }

		parentList[Math.max(num1Parent, num2Parent)] = Math.min(num1Parent, num2Parent);
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		candyList = new int[N];
		edgeList = new Edge[M];
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			candyList[i] = Integer.parseInt(st.nextToken());
		}

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			int num1 = Integer.parseInt(st.nextToken()) - 1;
			int num2 = Integer.parseInt(st.nextToken()) - 1;

			edgeList[i] = new Edge(num1, num2);
		}

	}

}