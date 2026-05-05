package problem;

import java.io.*;
import java.util.*;

class MedianofTwoSortedArrays {
    static int N;

    public static void main(String[] args) throws Exception {
        int[] nums1 = {1};
        int[] nums2 = {1};

        System.out.println(findMedianSortedArrays(nums1, nums2));
    }

    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int totalLength = nums1.length + nums2.length;
        int halfLength = (totalLength + 1) / 2;


        int leftIndex = 0;
        int rightIndex = nums1.length;
        while (leftIndex <= rightIndex) {
            int leftLength = (leftIndex + rightIndex) / 2;
            int rightLength = halfLength - leftLength;



            int nums1LeftMaxNum = (leftLength >= 1) ? nums1[leftLength - 1] : Integer.MIN_VALUE;
            int nums1RightMinNum = (leftLength < nums1.length) ? nums1[leftLength] : Integer.MAX_VALUE;

            int nums2LeftMaxNum = (rightLength >= 1) ? nums2[rightLength - 1] : Integer.MIN_VALUE;
            int nums2RightMinNum = (rightLength < nums2.length) ? nums2[rightLength] : Integer.MAX_VALUE;


            if (nums1RightMinNum < nums2LeftMaxNum) {
                leftIndex = leftLength + 1;
                continue;
            }

            if (nums2RightMinNum < nums1LeftMaxNum) {
                rightIndex = leftLength - 1;
                continue;
            }



            if (totalLength % 2 == 1) {
                return (double) Math.max(nums1LeftMaxNum, nums2LeftMaxNum);
            }



            double answer = (double) (Math.max(nums1LeftMaxNum, nums2LeftMaxNum) + Math.min(nums1RightMinNum, nums2RightMinNum)) / 2;
            return answer;
        }

        return 0;
    }
}