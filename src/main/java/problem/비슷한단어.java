package  problem;

import java.util.*;
import java.io.*;
import java.util.List;

public class 비슷한단어 {
    static int N;
    static String[] wordList;
    static Map<String, String> partStringMap = new HashMap<>();
    static Map<String, Integer> stringIndexMap = new HashMap<>();

    static class TreeNode {
        char curAlphabet;
        Map<Character, TreeNode> childNodeMap;

        public TreeNode(char curAlphabet) {
            this.curAlphabet = curAlphabet;
            this.childNodeMap = new HashMap<>();
        }
    }

    static class ReturnNode {
        int record;
        String pairString;

        public ReturnNode(int record, String pairString) {
            this.record = record;
            this.pairString = pairString;
        }
    }
    public static void main(String[] args) throws IOException {
        init();
//        Arrays.sort(wordList);

        int bestRecord = 0;
        List<String> pairString = Arrays.asList(wordList[0], wordList[1]);
        for (int i = 0; i < N; i++) {
            String curString = wordList[i];
            stringIndexMap.put(curString, i);
            ReturnNode resultNode = processString(curString);

            if (resultNode.record > bestRecord) {
                bestRecord = resultNode.record;
                pairString = Arrays.asList(resultNode.pairString, curString);
            }
            else if (resultNode.record == bestRecord &&
                    stringIndexMap.get(resultNode.pairString) != null &&
                    stringIndexMap.get(resultNode.pairString) < stringIndexMap.get(pairString.get(0))) {
                pairString = Arrays.asList(resultNode.pairString, curString);
            }
        }

        System.out.println(pairString.get(0));
        System.out.println(pairString.get(1));
    }

    public static ReturnNode processString(String curString) {
        int record = 0;
        String pairString = null;
        for (int i = 0; i < curString.length() + 1; i++) {
            String partString = curString.substring(0, i);
            if (partStringMap.get(partString) != null) {
                record += 1;
                pairString = partStringMap.get(partString);
                continue;
            }
            partStringMap.put(partString, curString);
        }

        return new ReturnNode(record, pairString);
    }

    public static void init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());

        wordList = new String[N];
        for (int i = 0; i < N; i++) {
            wordList[i] = br.readLine();
        }
    }
}