package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static Map<Integer, List<Integer>> friendMap, enemyMap;
	static int[] parentList;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < enemyMap.get(i).size(); j++) {
				for (int k = j + 1; k < enemyMap.get(i).size(); k++) {
					int enemy1 = enemyMap.get(i).get(j);
					int enemy2 = enemyMap.get(i).get(k);

					friendMap.get(enemy1).add(enemy2);
					friendMap.get(enemy2).add(enemy1);
				}
			}
		}


		for (int i = 0; i < N; i++) {
			for (int friend : friendMap.get(i)) {
				int iParent = findParent(i);
				int friendParent = findParent(friend);

				if (iParent != friendParent) {
					union(iParent, friendParent);
				}
			}
		}


		Map<Integer, Integer> parentMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			int parent = parentList[i];
			if (parentMap.get(parent) == null) {
				parentMap.put(parent, 1);
			}
		}


		System.out.println(parentMap.keySet().size());
	}

	public static void union(int num1, int num2) {
		int num1Parent = findParent(num1);
		int num2Parent = findParent(num2);

		if (num1Parent != num2Parent) {
			parentList[Math.max(num1Parent, num2Parent)] = Math.min(num1Parent, num2Parent);
		}
	}

	public static int findParent(int num) {
		if (parentList[num] == num) { return num; }

		return parentList[num] = findParent(parentList[num]);
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		M = Integer.parseInt(st.nextToken());

		friendMap = new HashMap<>();
		enemyMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			friendMap.put(i, new ArrayList<>());
			enemyMap.put(i, new ArrayList<>());
		}


		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());
			String relationship = st.nextToken();

			int node1 = Integer.parseInt(st.nextToken()) - 1;
			int node2 = Integer.parseInt(st.nextToken()) - 1;

			if (relationship.equals("F")) {
				friendMap.get(node1).add(node2);
				friendMap.get(node2).add(node1);
			}

			else {
				enemyMap.get(node1).add(node2);
				enemyMap.get(node2).add(node1);
			}
		}

		parentList = new int[N];
		for (int i = 0; i < N; i++) {
			parentList[i] = i;
		}
	}
}