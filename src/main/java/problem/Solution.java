package problem;

class Solution {
    static int N;
    public static void main(String[] args) throws Exception {
        System.out.println(longestPalindrome("ccc"));
    }

    public static String longestPalindrome(String s) {
        N = s.length();
        int[][] dpMatrix = new int[N][N];

        for (int i = 0; i < N; i++) {
            dpMatrix[i][i] = 1;
        }

        int maxLength = 0;
        String answerString = Character.toString(s.charAt(0));
        for (int i = 0; i < N - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                dpMatrix[i][i + 1] = 1;

                maxLength = 1;
                answerString = s.substring(i, i + 2);
            }
        }

        for (int gap = 2; gap < N; gap++) {
            for (int startIndex = 0; startIndex + gap < N; startIndex++) {
                int endIndex = startIndex + gap;

                if (s.charAt(startIndex) == s.charAt(endIndex) && dpMatrix[startIndex + 1][endIndex - 1] == 1) {
                    dpMatrix[startIndex][endIndex] = 1;
                    if (gap > maxLength) {
                        maxLength = gap;
                        answerString = s.substring(startIndex, endIndex + 1);
                    }
                }
            }

        }

        return answerString;
    }
}