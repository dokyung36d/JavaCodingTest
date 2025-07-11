package problem;

import java.util.*;
import java.io.*;


public class Main {
	static int numPizzaOrder;
	static int N, M;
	static int[] nPizzaSizeList, mPizzaSizeList;
	static int[] nCumulativeSumList, mCumulativeSumList;
	static Map<Integer, Integer> mPizzaSizeMap, nPizzaSizeMap;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		for (int i = 0; i < M; i++) {
			for (int gap = 1; gap < M; gap++) {
				int sumValue = mCumulativeSumList[i + gap] - mCumulativeSumList[i];
				mPizzaSizeMap.put(sumValue, mPizzaSizeMap.getOrDefault(sumValue, 0) + 1);
			}
		}
		mPizzaSizeMap.put(0, 1);
		mPizzaSizeMap.put(mCumulativeSumList[M], 1);


		for (int i = 0; i < N; i++) {
			for (int gap = 1; gap < N; gap++) {
				int sumValue = nCumulativeSumList[i + gap] - nCumulativeSumList[i];
				nPizzaSizeMap.put(sumValue, nPizzaSizeMap.getOrDefault(sumValue, 0) + 1);
			}
		}
		nPizzaSizeMap.put(0, 1);
		nPizzaSizeMap.put(nCumulativeSumList[N], 1);


		int answer = 0;
		for (int mSumValue : mPizzaSizeMap.keySet()) {
			int restSumValue = numPizzaOrder - mSumValue;
			if (nPizzaSizeMap.get(restSumValue) == null) { continue; }

			answer += mPizzaSizeMap.get(mSumValue) * nPizzaSizeMap.get(restSumValue);
		}

		System.out.println(answer);
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		numPizzaOrder = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		M = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());

		mPizzaSizeList = new int[2 * M];
		nPizzaSizeList = new int[2 * N];

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());

			mPizzaSizeList[i] = Integer.parseInt(st.nextToken());
			mPizzaSizeList[i + M] = mPizzaSizeList[i];
		}

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());

			nPizzaSizeList[i] = Integer.parseInt(st.nextToken());
			nPizzaSizeList[i + N] = nPizzaSizeList[i];
		}


		mCumulativeSumList = new int[2 * M + 1];
		nCumulativeSumList = new int[2 * N + 1];
		for (int i = 0; i < 2 * M; i++) {
			mCumulativeSumList[i + 1] = mCumulativeSumList[i] + mPizzaSizeList[i];
		}

		for (int i = 0; i < 2 * N; i++) {
			nCumulativeSumList[i + 1] = nCumulativeSumList[i] + nPizzaSizeList[i];
		}


		nPizzaSizeMap = new HashMap<>();
		mPizzaSizeMap = new HashMap<>();


	}
}