package problem;

import java.io.*;
import java.util.*;

class LongestSubstringWithoutRepeatingCharacters {
    static int N;
    static Map<Character, Integer> dpMap;
    public static void main(String[] args) throws Exception {
        System.out.println(lengthOfLongestSubstring("pwwkew"));
    }


    public static int lengthOfLongestSubstring(String s) {
        N = s.length();
        dpMap = new HashMap<>();

        int answer = 0;

        int startIndex = -1;
        for (int i = 0; i < N; i++) {
            char curChar = s.charAt(i);

            if (dpMap.get(curChar) == null) {
                dpMap.put(curChar, i);
                answer = Math.max(answer, i - startIndex);

                continue;
            }

            startIndex = Math.max(startIndex, dpMap.get(curChar));
            dpMap.put(curChar, i);
            answer = Math.max(answer, i - startIndex);
        }


        return answer;
    }
}