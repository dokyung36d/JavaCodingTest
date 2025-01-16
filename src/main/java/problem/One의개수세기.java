package problem;

import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class One의개수세기 {
    static BigInteger A, B;
    static List<BigInteger> numOneInGapList = new ArrayList<>();
    static List<BigInteger> numOneTotalList = new ArrayList<>();
    static BigInteger base = new BigInteger("2");

    public static void main(String[] args) throws Exception {
        init();

        numOneInGapList.add(new BigInteger("0"));
        numOneTotalList.add(new BigInteger("0"));

        numOneInGapList.add(new BigInteger("1"));
        numOneTotalList.add(new BigInteger("1"));

        while (base.pow(numOneTotalList.size()).compareTo(B) != 1) {
            BigInteger numOneInGap = numOneTotalList.get(numOneTotalList.size() - 1).add(base.pow(numOneTotalList.size() - 1));
            BigInteger numOneTotal = numOneInGap.add(numOneTotalList.get(numOneTotalList.size() - 1));

            numOneInGapList.add(numOneInGap);
            numOneTotalList.add(numOneTotal);
        }

        BigInteger biggerValue = getNumOne(B);
        BigInteger smallerValue = getNumOne(A.subtract(new BigInteger("1")));
        System.out.println(biggerValue.subtract(smallerValue));
    }

    public static BigInteger getNumOne(BigInteger num) {
        BigInteger answer = new BigInteger("0");

        while (num.compareTo(new BigInteger("0")) == 1) {
            int logValue = getLogValue(num);
            BigInteger numExponential = base.pow(logValue);

            answer = answer.add(numOneTotalList.get(logValue));
            answer = answer.add(num.subtract(numExponential));
            answer = answer.add(new BigInteger("1"));

            num = num.subtract(numExponential);
        }

        return answer;
    }

    public static int getLogValue(BigInteger num) {
        return num.bitLength() - 1;
    }


    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        A = new BigInteger(st.nextToken());
        B = new BigInteger(st.nextToken());
    }
}