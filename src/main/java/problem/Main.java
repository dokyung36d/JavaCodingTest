package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N;
	static List<Integer> origianlInOrderList, originalPostOrderList;
	static StringBuilder sb;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		sb = new StringBuilder();

		recursive(origianlInOrderList, originalPostOrderList);
		System.out.println(sb.toString().substring(0, sb.length() - 1));
	}

	public static void recursive(List<Integer> inOrderList, List<Integer> postOrderList) {
		if (inOrderList.size() == 1) {
			sb.append(inOrderList.get(0) + " ");
			return;
		}
		else if (inOrderList.size() == 0) {
			return;
		}


		int operator = postOrderList.get(postOrderList.size() - 1);
		sb.append(operator + " ");
		int operatoIndex = inOrderList.indexOf(operator);

		recursive(inOrderList.subList(0, operatoIndex), postOrderList.subList(0, operatoIndex));
		recursive(inOrderList.subList(operatoIndex + 1, inOrderList.size()),
				postOrderList.subList(operatoIndex, postOrderList.size() - 1));
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());

		origianlInOrderList = new ArrayList<>();
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			origianlInOrderList.add(Integer.parseInt(st.nextToken()));
		}

		originalPostOrderList = new ArrayList<>();
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			originalPostOrderList.add(Integer.parseInt(st.nextToken()));
		}
	}
}