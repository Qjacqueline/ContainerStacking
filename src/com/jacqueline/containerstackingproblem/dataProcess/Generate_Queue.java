package com.jacqueline.containerstackingproblem.dataProcess;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;

public class Generate_Queue {


    public String[] permutation(String S) {
        int length = 1;
        for (int i = 1; i <= S.length(); i++) {
            length *= i;
        }
        //准备结果集
        LinkedList<String> result = new LinkedList<>();
        //递归
        backcheck(S.toCharArray(), 0, result);
        //返回结果

        return result.toArray(new String[0]);
    }

    public void backcheck(char[] nums, int index, LinkedList<String> result) {
        //设置递归出口
        if (index == nums.length) {
            //这时的nums是一种排列，需要添加到结果集中
            if (result.size() >= 1000000)
                return;
            result.add(new String(nums));
        } else {
            //对于本位置遍历所有的可能性
            //创建一个set来避免重复判断，
            HashSet<Character> set = new HashSet<>();
            for (int i = index; i < nums.length; i++) {
                //每次开始递归之前判断本元素是否已经遍历
                if (result.size() >= 1000000)
                    return;
                if (!set.contains(nums[i])) {
                    //加入set标志本数已经遍历过
                    set.add(nums[i]);
                    //替换本位置表示本位置的数确定下来
                    swap(nums, index, i);
                    //接着下一个位置的判断
                    backcheck(nums, index + 1, result);
                    //判断结束后需要复位接着本位置下一个结果的判断
                    swap(nums, index, i);
                }
            }
        }
    }

    public void swap(char nums[], int i, int j) {
        if (i != j) {
            char temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
    }


    public static void main(String[] args) throws IOException {
        int[][] list_two = new int[][]{{7, 3}};
//        int[][] list_two = new int[][]{{4, 3}, {4, 4}, {4, 5}, {5, 3}, {5, 4}, {5, 5}, {6, 3}, {6, 4}, {6, 5}, {7, 3}, {7, 4}, {7, 5}, {8, 3}, {8, 4}, {8, 5}};
        int[][] list_three = new int[][]{{4, 3}, {4, 4}, {4, 5}, {5, 3}, {5, 4}, {5, 5}, {6, 3}, {7, 3}, {5, 5}, {6, 4}, {6, 5}, {7, 4}};
        for (int j = 0; j < list_two.length; j++) { //list_two.length
            int s = list_two[j][0], t = list_two[j][1], gn_num = 3;
//            int s = 3, t = 5,      int gn_num = 2;
//        for (int i = 0; i < list_three.length; i++) {
//            int s = list_three[i][0], t = list_three[i][1], gn_num = 3;
            String a = "";
            if (gn_num == 2) {
                int first_part = s * t / 2;
                int second_part = s * t - first_part;
                for (int i = 0; i < first_part; i++) {
                    a = a + "H";
                }
                for (int i = first_part; i < s * t; i++) {
                    a = a + "L";
                }

            }
            if (gn_num == 3) {
                int first_part = s * t / 3;
                int second_part = s * t * 2 / 3;
                for (int i = 0; i < first_part; i++) {
                    a = a + "H";
                }
                for (int i = first_part; i < second_part; i++) {
                    a = a + "M";
                }
                for (int i = second_part; i < s * t; i++) {
                    a = a + "L";
                }
            }
            FileWriter fw = new FileWriter("/Users/jacqueline/Code/ContainerStacking/data/instances/instance_" + s * t + "_" + gn_num + ".txt");
            PrintWriter write = new PrintWriter(fw);
            Generate_Queue q = new Generate_Queue();
            String[] res = q.permutation(a);
            for (int i = 0; i < res.length; i++) {
                write.println(res[i]);
            }
            System.out.println(res.length);
            fw.close();
            write.close();
        }
    }
}

