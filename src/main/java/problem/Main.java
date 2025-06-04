package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int N;
	static List<Integer> primeNumList, cumulativeSum;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		setPrimeNumList();
		setCumulativeSum();

		int left = 0;
		int right = 1;

		int answer = 0;
		while (left <= right && right < cumulativeSum.size()) {
			int sumValue = cumulativeSum.get(right) - cumulativeSum.get(left);

			if (sumValue == N) {
				answer += 1;
				left += 1;
			}

			else if (sumValue < N) {
				right += 1;
			}

			else if (sumValue > N) {
				left += 1;
			}
		}

		System.out.println(answer);
	}

	public static void setPrimeNumList() {
		int[] isPrime = new int[N + 1];
		Arrays.fill(isPrime, 1);

		isPrime[0] = 0;
		isPrime[1] = 0;

		for (int num = 2; num < N + 1; num++) {
			if (isPrime[num] == 0) {
				continue;
			}

			for (int i = num * 2; i < N + 1; i += num) {
				isPrime[i] = 0;
			}
		}

		primeNumList = new ArrayList<>();
		for (int i = 0; i < N + 1; i++) {
			if (isPrime[i] == 1) {
				primeNumList.add(i);
			}
		}
	}

	public static void setCumulativeSum() {
		cumulativeSum = new ArrayList<>();
		cumulativeSum.add(0);
		for (int i = 0; i < primeNumList.size(); i++) {
			cumulativeSum.add(cumulativeSum.get(cumulativeSum.size() - 1) + primeNumList.get(i));
		}


	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());

	}
}