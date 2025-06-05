package problem;
import java.util.*;
import java.io.*;


public class Main {
	static int N, M;
	static Map<Integer, List<Integer>> graphMap;
	static int[] numPointed;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		PriorityQueue<Integer> pq = new PriorityQueue<>();
		for (int i = 0; i < N; i++) {
			if (numPointed[i] == 0) {
				pq.add(i);
			}
		}

		int numOut = 0;

		StringBuilder sb = new StringBuilder();
		while (!pq.isEmpty()) {
			int curNum = pq.poll();
			sb.append(curNum + 1);
			sb.append("\n");
			numOut += 1;

			for (int nearNum : graphMap.get(curNum)) {
				numPointed[nearNum] -= 1;

				if (numPointed[nearNum] == 0) {
					pq.add(nearNum);
				}
			}
		}

		if (numOut != N) {
			System.out.println(0);
			return;
		}
		System.out.println(sb.toString().substring(0, sb.length() - 1));
	}


	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		numPointed = new int[N];
		graphMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			graphMap.put(i, new ArrayList<>());
		}

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());

			int numOrder = Integer.parseInt(st.nextToken());
			int[] orderList = new int[numOrder];
			for (int order = 0; order < numOrder; order++) {
				orderList[order] = Integer.parseInt(st.nextToken()) - 1;
			}

			for (int j = 0; j < numOrder - 1; j++) {
				graphMap.get(orderList[j]).add(orderList[j + 1]);
				numPointed[orderList[j + 1]] += 1;
			}
		}
	}
}