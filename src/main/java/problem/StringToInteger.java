package problem;

import java.io.*;
import java.util.*;

class StringToInteger {


    public static void main(String[] args) throws Exception {
        System.out.println(myAtoi(" "));
    }


    public static int myAtoi(String s) {
        int answer = 0;

        //공백 제거
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ' && sb.length() == 0) { continue; }

            sb.append(s.charAt(i));

        }

        s = sb.toString();
        if (s.length() == 0) { return 0; }

        //첫 글자 처리
        boolean isMinus = false;
        if (sb.toString().charAt(0) == '-') {
            isMinus = true;
            s = sb.toString().substring(1, sb.length());
        }
        else if (sb.toString().charAt(0) == '+') {
            s = sb.toString().substring(1, sb.length());
        }
        else {
            s = sb.toString();
        }



        sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i)) == false) {
                break;
            }

            sb.append(s.charAt(i));
        }

        if (sb.length() == 0) {
            return 0;
        }

        if (isMinus) {
            sb.insert(0, "-");
        }


        int num;
        try {
            num = Integer.parseInt(sb.toString());
        } catch (NumberFormatException e) {
            if (isMinus) {
                num = Integer.MIN_VALUE;
            }
            else {
                num = Integer.MAX_VALUE;
            }
        }

        return num;
    }
}
