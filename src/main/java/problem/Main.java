package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static List<Integer> originalList;
	static Command[] commandList;

	public static class Command {
		int firstIndex;
		int secondIndex;
		int cost;

		public Command(int firstIndex, int secondIndex, int cost) {
			this.firstIndex = firstIndex;
			this.secondIndex = secondIndex;
			this.cost = cost;
		}
	}

	public static class Node implements Comparable<Node> {
		List<Integer> numList;
		int cost;

		public Node(List<Integer> numList, int cost) {
			this.numList = numList;
			this.cost = cost;
		}

		@Override
		public int compareTo(Node anotherNode) {
			return Integer.compare(this.cost, anotherNode.cost);
		}
	}

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		Map<String, Integer> stringMap = new HashMap<>();

		PriorityQueue<Node> pq = new PriorityQueue<>();
		pq.add(new Node(originalList, 0));

		while (!pq.isEmpty()) {
			Node node = pq.poll();

			String stringValue = node.numList.toString().intern();
			if (stringMap.get(stringValue) != null) { continue; }
			stringMap.put(stringValue, 1);

			if (isListAscending(node.numList)) {
				System.out.println(node.cost);
				return;
			}

			for (Command command : commandList) {
				List<Integer> swappedNumList = swapList(node.numList, command);

				if (stringMap.get(swappedNumList.toString().intern()) != null) { continue; }
				pq.add(new Node(swappedNumList, node.cost + command.cost));
			}
		}

		System.out.println(-1);
	}

	public static List<Integer> swapList(List<Integer> numList, Command command) {
		List<Integer> copiedList = new ArrayList<>(numList);

		int firstValue = numList.get(command.firstIndex);
		int secondValue = numList.get(command.secondIndex);

		copiedList.set(command.firstIndex, secondValue);
		copiedList.set(command.secondIndex, firstValue);

		return copiedList;
	}

	public static boolean isListAscending(List<Integer> numList) {
		for (int i = 0; i < numList.size() - 1; i++) {
			if (numList.get(i) > numList.get(i + 1)) {
				return false;
			}
		}

		return true;
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());

		originalList = new ArrayList<>();
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			originalList.add(Integer.parseInt(st.nextToken()));
		}

		st = new StringTokenizer(br.readLine());
		M = Integer.parseInt(st.nextToken());

		commandList = new Command[M];
		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());

			int firstIndex = Integer.parseInt(st.nextToken()) - 1;
			int secondIndex = Integer.parseInt(st.nextToken()) - 1;
			int cost = Integer.parseInt(st.nextToken());

			commandList[i] = new Command(firstIndex, secondIndex, cost);
		}
	}


}