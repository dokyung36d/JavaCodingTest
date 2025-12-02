package problem;

import java.util.*;
import java.io.*;

public class Main {
    static int N, S;
	static int[] numList, sumList;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		int answer = Integer.MAX_VALUE / 2;

		int leftIndex = 0;
		int rightIndex = 1;

		while (rightIndex < N + 1) {
			int sumValue = sumList[rightIndex] - sumList[leftIndex];

			if (sumValue < S) {
				rightIndex += 1;
				continue;
			}

			if (rightIndex - leftIndex < answer) {
				answer = rightIndex - leftIndex;
			}
			leftIndex += 1;
		}

		if (answer == Integer.MAX_VALUE / 2) {
			answer = 0;
		}
		System.out.println(answer);
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		S = Integer.parseInt(st.nextToken());

		numList = new int[N];
		sumList = new int[N + 1];

		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			int num = Integer.parseInt(st.nextToken());
			numList[i] = num;
			sumList[i + 1] = num + sumList[i];
		}
	}
}