package problem;

import java.util.*;
import java.io.*;

public class Main {
	static int T;
	static int N, M;
	static int[] nList, mList;
	static long[] nSumList, mSumList;
	static Map<Long, Integer> nSumMap, mSumMap;

	public static void main(String[] args) throws Exception {
		init();
		solution();
	}

	public static void solution() {
		long answer = 0;

		for (long nSum : nSumMap.keySet()) {
			long restNum = T - nSum;

			if (mSumMap.get(restNum) == null) { continue; }

			answer += (long) nSumMap.get(nSum) * mSumMap.get(restNum);
		}

		System.out.println(answer);
	}

	public static void init() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		T = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());

		nList = new int[N];
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < N; i++) {
			nList[i] = Integer.parseInt(st.nextToken());
		}

		nSumList = new long[N + 1];
		for (int i = 0; i < N; i++) {
			nSumList[i + 1] = (long) nSumList[i] + nList[i];
		}


		nSumMap = new HashMap<>();
		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N + 1; j++) {
				long sumValue = nSumList[j] - nSumList[i];
				nSumMap.put(sumValue, nSumMap.getOrDefault(sumValue, 0) + 1);
			}
		}

		st = new StringTokenizer(br.readLine());
		M = Integer.parseInt(st.nextToken());

		mList = new int[M];
		st = new StringTokenizer(br.readLine());
		for (int i = 0; i < M; i++) {
			mList[i] = Integer.parseInt(st.nextToken());
		}

		mSumList = new long[M + 1];
		for (int i = 0; i < M; i++) {
			mSumList[i + 1] = (long) mSumList[i] + mList[i];
		}

		mSumMap = new HashMap<>();
		for (int i = 0; i < M; i++) {
			for (int j = i + 1; j < M + 1; j++) {
				long sumValue = mSumList[j] - mSumList[i];
				mSumMap.put(sumValue, mSumMap.getOrDefault(sumValue, 0) + 1);
			}
		}
	}
}