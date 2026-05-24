package problem;

import java.io.*;
import java.util.*;

class ReverseInteger {


    public static void main(String[] args) throws Exception {
        System.out.println(reverse(1534236469));
    }
    public static int reverse(int x) {
        int returnValue = 0;
        boolean isPositive = true;
        if (x < 0) {
            isPositive = false;
        }


        x = Math.abs(x);
        int answer = 0;
        while (x > 0) {
            int originalAnswerValue = answer;
            answer *= 10;
            answer += x % 10;


            int recoveryNum = (answer - x % 10) / 10;
            if (recoveryNum != originalAnswerValue) {
                return 0;
            }

            x /= 10;
        }

        if (isPositive == false) {
            answer *= -1;
        }

        return answer;
    }
}