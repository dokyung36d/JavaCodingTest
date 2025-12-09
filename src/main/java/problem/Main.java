package problem;

import java.util.*;
import java.io.*;

public class Main {
	static int N;
	static List<Integer> primeNumList, primeCumulativeSumList;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		setPrimeNumList();

		int leftIndex = 0;
		int rightIndex = 0;

		int answer = 0;
		while (leftIndex <= rightIndex && rightIndex < primeCumulativeSumList.size()) {
			int sumValue = primeCumulativeSumList.get(rightIndex) - primeCumulativeSumList.get(leftIndex);

			if (sumValue == N) {
				answer += 1;
				leftIndex += 1;
				continue;
			}

			if (sumValue < N) {
				rightIndex += 1;
				continue;
			}

			if (sumValue > N) {
				leftIndex += 1;
				continue;
			}
		}
		System.out.println(answer);
	}

	public static void setPrimeNumList() {
		primeNumList = new ArrayList<>();
		primeCumulativeSumList = new ArrayList<>();
		int[] isPrime = new int[N + 1]; // 0 ~ N

		Arrays.fill(isPrime, 1);
		isPrime[0] = 0;
		isPrime[1] = 0;

		for (int i = 0; i < N + 1; i++) {
			if (isPrime[i] == 0) { continue; }

			for (int j = 2 * i; j < N + 1; j += i) {
				isPrime[j] = 0;
			}
		}

		int primeCumulativeSum = 0;
		primeCumulativeSumList.add(primeCumulativeSum);
		for (int i = 0; i < N + 1; i++) {
			if (isPrime[i] == 0 ) { continue; }
			primeNumList.add(i);
			primeCumulativeSumList.add(primeCumulativeSum + i);
			primeCumulativeSum += i;
		}
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
	}
}