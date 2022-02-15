// calculate rehandles
package com.jacqueline.containerstackingproblem.evaluateAlgorithm;

import com.jacqueline.containerstackingproblem.dataProcess.Read_Dynamic_Result;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Evaluate_Counting_Methods {

    public static long duration = 0;
    public static int s;
    public static int t;
    public static String[] gn;
    public static int[] gn_num;
    public static Read_Dynamic_Result dynamic_result;
    public static String method_name;
    public static int[] total_rehandle;
    public static int sum_total_rehandle;

    public String[][] get_queue() throws IOException {
        // 输入可能达到队列 string_ss
        BufferedReader reader = new BufferedReader(new FileReader(
                "/Users/jacqueline/Code/ContainerStacking/data/instances/instance_"
                        + s * t + "_" + gn.length + ".txt"));
        String[] string_ss = new String[10000];
        int index = 0;
        String line = null;
        while ((line = reader.readLine()) != null) {
            string_ss[index] = line;
            index++;
            if (index > 9999)
                break;
        }
        reader.close();

        // 将队列string_ss处理成数字队列string_generate
        String[][] string_generate = new String[string_ss.length][s * t];
        for (int i = 0; i < string_generate.length; i++) {
            for (int j = 0; j < string_generate[i].length; j++) {
                if (gn.length == 2) {
                    gn_num = new int[2];
                    if (string_ss[i].charAt(j) == 'H') {
                        string_generate[i][j] = "10";
                    }
                    if (string_ss[i].charAt(j) == 'L') {
                        string_generate[i][j] = "1";
                    }
                }
                if (gn.length == 3) {
                    gn_num = new int[3];
                    if (string_ss[i].charAt(j) == 'H') {
                        string_generate[i][j] = "100";
                    }
                    if (string_ss[i].charAt(j) == 'M') {
                        string_generate[i][j] = "10";
                    }
                    if (string_ss[i].charAt(j) == 'L') {
                        string_generate[i][j] = "1";
                    }
                }
            }
        }
        return string_generate;
    }

    public int get_state_position_in_dynamic_table(String cur_nc, String[] cur_sc) {
        int target_index = 0;
        // find the target index
        for (int k = 0; k < dynamic_result.dataarray.length; k++) {
            if (cur_nc.equals(dynamic_result.current_state[k][0])) {
                boolean find = true;
                for (int h = 0; h < s; h++) {
                    if (!cur_sc[h].equals(dynamic_result.current_state[k][h + 1]))
                        find = false;
                }
                if (find) {
                    target_index = k;
                    break;
                }
            }
        }
        return target_index;
    }

    public int get_container_position_in_dynamic_table(String target_j) {
        int target_k = 0;
        for (int k = 0; k < gn.length; k++) {
            if (gn[k].equals(target_j)) {
                target_k = k;
                break;
            }
        }
        return target_k;
    }

    public int[] cal_extreme_plan_one(String cur_nc, String[] cur_sc) {
        int[] cur_state_type_num_estimate = new int[3];
        int max_num = s * t / 3;
        for (int i = 0; i < s; i++) {
            String cur_s_sc = cur_sc[i];
            if (cur_s_sc.length() == 1) {
                // 全为 L
                cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + Integer.parseInt(cur_s_sc), max_num);
            } else if (cur_s_sc.length() == 2) {
                // 上方 全L
                cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + t - Integer.parseInt(String.valueOf(cur_nc.charAt(i))) - Integer.parseInt(cur_s_sc) / 10, max_num);
                // 中间 M
                cur_state_type_num_estimate[1] = Math.min(cur_state_type_num_estimate[1] + 1, max_num);
                // 下方 M->L
                int differ = cur_state_type_num_estimate[1] + Integer.parseInt(cur_s_sc) / 10 - 1 - max_num;
                if (differ > 0) {
                    cur_state_type_num_estimate[1] = max_num;
                    cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + differ, max_num);
                } else {
                    cur_state_type_num_estimate[1] += Integer.parseInt(cur_s_sc) / 10 - 1;
                }
            } else if (cur_s_sc.length() == 3) {
                // 上方 M->L
                int differ = cur_state_type_num_estimate[1] + (t - Integer.parseInt(cur_s_sc) / 100 - Integer.parseInt(String.valueOf(cur_nc.charAt(i)))) - max_num;
                if (differ > 0) {
                    cur_state_type_num_estimate[1] = max_num;
                    cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + differ, max_num);
                } else {
                    cur_state_type_num_estimate[1] += t - Integer.parseInt(cur_s_sc) / 100 - Integer.parseInt(String.valueOf(cur_nc.charAt(i)));
                }
                // 中间 H
                cur_state_type_num_estimate[2] = Math.min(cur_state_type_num_estimate[2] + 1, max_num);
                // 下方 H->M->L
                int differ_h = cur_state_type_num_estimate[2] + Integer.parseInt(cur_s_sc) / 100 - 1 - max_num;
                if (differ_h > 0) {
                    cur_state_type_num_estimate[2] = max_num;
                    int differ_m = cur_state_type_num_estimate[1] + differ_h - max_num;
                    if (differ_m > 0) {
                        cur_state_type_num_estimate[1] = max_num;
                        cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + differ_m, max_num);
                    } else {
                        cur_state_type_num_estimate[1] += differ_h;
                    }
                } else {
                    cur_state_type_num_estimate[2] += Integer.parseInt(cur_s_sc) / 100 - 1;
                }
            }
        }
//        for (int i = 0; i < 3; i++) {
//            System.out.print(cur_state_type_num_estimate[i] + "\t");
//        }
        return cur_state_type_num_estimate;
    }

    public int[] cal_extreme_plan_two(String cur_nc, String[] cur_sc) {
        int[] cur_state_type_num_estimate = new int[3];
        int max_num = s * t / 3;
        for (int i = 0; i < s; i++) {
            String cur_s_sc = cur_sc[i];
            if (cur_s_sc.length() == 1) {
                // 全为 L
                cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + Integer.parseInt(cur_s_sc), max_num);
            } else if (cur_s_sc.length() == 2) {
                // 上方 全L
                cur_state_type_num_estimate[0] += t - Integer.parseInt(String.valueOf(cur_nc.charAt(i))) - Integer.parseInt(cur_s_sc) / 10;
                // 中间 M
                cur_state_type_num_estimate[1] = Math.min(cur_state_type_num_estimate[1] + 1, max_num);
                // 下方 L->M
                int differ = cur_state_type_num_estimate[0] + Integer.parseInt(cur_s_sc) / 10 - 1 - max_num;
                if (differ > 0) {
                    cur_state_type_num_estimate[0] = max_num;
                    cur_state_type_num_estimate[1] = Math.min(cur_state_type_num_estimate[1] + differ, max_num);
                } else {
                    cur_state_type_num_estimate[0] += Integer.parseInt(cur_s_sc) / 10 - 1;
                }
            } else if (cur_s_sc.length() == 3) {
                // 上方 L->M
                int differ = cur_state_type_num_estimate[0] + (t - Integer.parseInt(cur_s_sc) / 100 - Integer.parseInt(String.valueOf(cur_nc.charAt(i)))) - max_num;
                if (differ > 0) {
                    cur_state_type_num_estimate[0] = max_num;
                    cur_state_type_num_estimate[1] = Math.min(cur_state_type_num_estimate[1] + differ, max_num);
                } else {
                    cur_state_type_num_estimate[0] += t - Integer.parseInt(cur_s_sc) / 100 - Integer.parseInt(String.valueOf(cur_nc.charAt(i)));
                }
                // 中间 H
                cur_state_type_num_estimate[2] = Math.min(cur_state_type_num_estimate[2] + 1, max_num);
                // 下方 L->M->H
                int differ_l = cur_state_type_num_estimate[0] + Integer.parseInt(cur_s_sc) / 100 - 1 - max_num;
                if (differ_l > 0) {
                    cur_state_type_num_estimate[0] = max_num;
                    int differ_m = cur_state_type_num_estimate[0] + differ_l - max_num;
                    if (differ_m > 0) {
                        cur_state_type_num_estimate[1] = max_num;
                        cur_state_type_num_estimate[2] = Math.min(cur_state_type_num_estimate[2] + differ_m, max_num);
                    } else {
                        cur_state_type_num_estimate[1] += differ_l;
                    }
                } else {
                    cur_state_type_num_estimate[0] += Integer.parseInt(cur_s_sc) / 100 - 1;
                }
            }
        }
//        for (int i = 0; i < 3; i++) {
//            System.out.print(cur_state_type_num_estimate[i] + "\t");
//        }
        return cur_state_type_num_estimate;
    }

    public int[] cal_average_plan(String cur_nc, String[] cur_sc) {
        int[] cur_state_type_num_estimate = new int[3];
        int max_num = s * t / 3;
        for (int i = 0; i < s; i++) {
            String cur_s_sc = cur_sc[i];
            if (cur_s_sc.length() == 1) {
                // 全为 L
                cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + Integer.parseInt(cur_s_sc), max_num);
            } else if (cur_s_sc.length() == 2) {
                // 上方 全L
                cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + t - Integer.parseInt(String.valueOf(cur_nc.charAt(i))) - Integer.parseInt(cur_s_sc) / 10, max_num);
                // 中间 M
                cur_state_type_num_estimate[1] = Math.min(cur_state_type_num_estimate[1] + 1, max_num);
                // 下方 M/L
                int average_num = (Integer.parseInt(cur_s_sc) / 10 - 1) / 2;
                int differ = cur_state_type_num_estimate[1] + average_num - max_num;
                if (differ > 0) {
                    cur_state_type_num_estimate[1] = max_num;
                    cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + differ, max_num);
                } else {
                    cur_state_type_num_estimate[1] += average_num;
                    cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + average_num, max_num);
                }
            } else if (cur_s_sc.length() == 3) {
                // 上方 M/L
                int average_num = (int) Math.ceil(1.0 * (t - Integer.parseInt(cur_s_sc) / 100 - Integer.parseInt(String.valueOf(cur_nc.charAt(i)))) / 2);
                int differ = cur_state_type_num_estimate[1] + average_num - max_num;
                if (differ > 0) {
                    cur_state_type_num_estimate[1] = max_num;
                    cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + differ, max_num);
                } else {
                    cur_state_type_num_estimate[1] += average_num;
                    cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + average_num, max_num);
                }
                // 中间 H
                cur_state_type_num_estimate[2] = Math.min(cur_state_type_num_estimate[2] + 1, max_num);
                // 下方 H->M->L
                int average_num_below = (int) Math.ceil(1.0 * (Integer.parseInt(cur_s_sc) / 100 - 1) / 3);
                int differ_h = cur_state_type_num_estimate[2] + average_num_below - max_num;
                cur_state_type_num_estimate[2] = differ_h > 0 ? max_num : cur_state_type_num_estimate[2] + average_num_below; //
                int cur_num_left = differ_h > 0 ? Integer.parseInt(cur_s_sc) / 100 - 1 - average_num_below + differ_h : Integer.parseInt(cur_s_sc) / 100 - 1 - average_num_below; // 除去H还剩几个箱子
                int cur_num_left_average = (int) Math.ceil(cur_num_left / 2);
                int differ_m = cur_state_type_num_estimate[1] + cur_num_left_average - max_num;
                cur_state_type_num_estimate[1] = differ_m > 0 ? max_num : cur_state_type_num_estimate[1] + cur_num_left_average;
                int cur_num_left_left = differ_m > 0 ? cur_num_left - cur_num_left_average + differ_m : cur_num_left - cur_num_left_average;
                cur_state_type_num_estimate[0] = Math.min(cur_state_type_num_estimate[0] + cur_num_left_left, max_num);
            }
        }
//        for (int i = 0; i < 3; i++) {
//            System.out.print(cur_state_type_num_estimate[i] + "\t");
//        }
        return cur_state_type_num_estimate;
    }

    public int[] choose_counting_plan(String cur_nc, String[] cur_sc) {
        int[] cur_state_type_num_estimate = new int[3];
        if (method_name.equals("Extreme_Plan_one")) {
            cur_state_type_num_estimate = cal_extreme_plan_one(cur_nc, cur_sc);
        } else if (method_name.equals("Extreme_Plan_two")) {
            cur_state_type_num_estimate = cal_extreme_plan_two(cur_nc, cur_sc);
        } else if (method_name.equals("Average_Plan")) {
            cur_state_type_num_estimate = cal_average_plan(cur_nc, cur_sc);
        }
        return cur_state_type_num_estimate;
    }

    public void update_layout(int[][] layout, String pre_nc, String[] pre_sc, String cur_nc, String[] cur_sc, int target_k) {
        int pre_pos = 0;
        int cur_pos = 0;
        boolean[] pre_find = new boolean[s];
        boolean[] cur_find = new boolean[s];
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                if (!cur_find[j]) {
                    if (pre_nc.charAt(i) == cur_nc.charAt(j) && pre_sc[i].equals(cur_sc[j])) {
                        pre_find[i] = true;
                        cur_find[j] = true;
                        break;
                    }
                }
            }
            if (!pre_find[i]) {
                pre_pos = i;
            }
        }
        for (int i = 0; i < s; i++) {
            if (!cur_find[i]) {
                cur_pos = i;
                break;
            }
        }

        int tier = t - Integer.parseInt(String.valueOf(pre_nc.charAt(pre_pos)));
        layout[pre_pos][tier] = target_k;
        // re-sequence layout
        if (pre_pos != cur_pos) {
            int[] stack_a = layout[pre_pos].clone();
            int[] stack_b = layout[cur_pos].clone();
            layout[pre_pos] = stack_b;
            layout[cur_pos] = stack_a;
        }
    }

    public void calculate_total_rehandle() throws IOException {
        // 输入动态规划结果 dataarray
        dynamic_result = new Read_Dynamic_Result();
        dynamic_result.datainput("/Users/jacqueline/Code/ContainerStacking/data/dynamic result/EIP_" + String.valueOf(s) + String.valueOf(t) + "_" + gn.length + ".txt", s, gn);

        // 可能到达队列
        String[][] string_generate = get_queue();
        int total_state_num = string_generate.length * s * t;
        int[] total_counting_delta = new int[3]; // 每个集装箱类别估算的差值
        total_rehandle = new int[string_generate.length];
        sum_total_rehandle = 0;
        FileWriter fw = new FileWriter("/Users/jacqueline/Code/ContainerStacking/data/counting methods result/" + method_name + "_" + s + t + gn_num.length + "_all_prob.txt");
        PrintWriter write = new PrintWriter(fw);
        int total_queue_accurate_state_100 = 0; // 当前队列正确的状态数
        int total_queue_accurate_state_95 = 0; // 当前队列95%正确的状态数
        int total_queue_accurate_state_90 = 0; // 当前队列90%正确的状态数
        int total_queue_accurate_state_80 = 0; // 当前队列80%正确的状态数
        int total_queue_accurate_state_70 = 0; // 当前队列80%正确的状态数
        int total_queue_accurate_state_60 = 0; // 当前队列80%正确的状态数
        int total_queue_accurate_state_50 = 0; // 当前队列50%正确的状态数
        // 循环到达队列
        for (int i = 0; i < string_generate.length; i++) { //string_generate.length
            String[] target = string_generate[i];  // 到达队列

            int[][] layout = new int[s][t];
            // 每一个队列，对应一个layout
            for (int s1 = 0; s1 < s; s1++) {
                for (int t1 = 0; t1 < t; t1++) {
                    layout[s1][t1] = 0;
                }
            }

            // state的初始representation emp_layout + sc_layout int[]
            String emp_layout = new String();
            int[] sc_layout = new int[s];
            for (int k = 0; k < s; k++) {
                emp_layout = emp_layout + String.valueOf(t);
                sc_layout[k] = 0;
            }

            // state的初始representation cur_nc + cur_sc string[]
            String cur_nc = "";
            String[] cur_sc = new String[s];
            for (int k = 0; k < s; k++)
                cur_sc[k] = "";
            for (int k = 0; k < s; k++) {
                cur_nc = cur_nc + String.valueOf(t);
                cur_sc[k] = String.valueOf(0);
            }

            int[] cur_queue_type_num_accurate = new int[3]; // 记录当前达到集装箱各类型数量准确值
            int cur_queue_accurate_state_100 = 0; // 当前队列正确的状态数
            int cur_queue_accurate_state_95 = 0; // 当前队列95%正确的状态数
            int cur_queue_accurate_state_90 = 0; // 当前队列90%正确的状态数
            int cur_queue_accurate_state_80 = 0; // 当前队列80%正确的状态数
            int cur_queue_accurate_state_70 = 0; // 当前队列80%正确的状态数
            int cur_queue_accurate_state_60 = 0; // 当前队列80%正确的状态数
            int cur_queue_accurate_state_50 = 0; // 当前队列50%正确的状态数

            // 循环每一个到达的箱子
            for (int j = 0; j < target.length; j++) {
                int[] cur_state_counting_delta = new int[3]; // 当前队列每个集装箱类别估算的差值
                cur_queue_type_num_accurate[target[j].length() - 1]++; // 当前集装箱类型计数加1
                int target_index = get_state_position_in_dynamic_table(cur_nc, cur_sc); // 当前state在动规表的位置
                int target_k = get_container_position_in_dynamic_table(target[j]); // 当前container类型在动规表的位置
                String pre_nc = cur_nc;
                String[] pre_sc = cur_sc.clone();
                // 下一state的representation
                cur_nc = dynamic_result.next_state[target_index][(s + 1) * target_k + 0];
                for (int h = 0; h < s; h++)
                    cur_sc[h] = dynamic_result.next_state[target_index][(s + 1) * target_k + h + 1];

                // resequence sc
                List<Integer> same_nc = new ArrayList<>();
                int start = 0, end = 0;
                for (int q = 0; q < s; q++) {
                    String cur_s_nc = String.valueOf(cur_nc.charAt(q));
                    int cur_s_sc = Integer.parseInt(cur_sc[q]);
                    String next_s_nc = "-1";
                    int next_s_sc = -1;
                    if (q != s - 1) {
                        next_s_nc = String.valueOf(cur_nc.charAt(q + 1));
                        next_s_sc = Integer.parseInt(cur_sc[q + 1]);
                    }
                    same_nc.add(cur_s_sc);
                    end++;
                    if (!cur_s_nc.equals(next_s_nc)) {
                        Collections.sort(same_nc);
                        int y = 0;
                        for (int w = start; w < end; w++) {
                            cur_sc[w] = String.valueOf(same_nc.get(y));
                            y++;
                        }
                        start = end;
                        same_nc = new ArrayList<>();
                    }
                }

                // 更新layout
                update_layout(layout, pre_nc, pre_sc, cur_nc, cur_sc, Integer.parseInt(target[j]));

                // 估测各类型集装箱数量
                int[] cur_queue_type_num_estimate = choose_counting_plan(cur_nc, cur_sc);

                for (int q = 0; q < 3; q++) {
                    cur_state_counting_delta[q] += Math.abs(cur_queue_type_num_accurate[q] - cur_queue_type_num_estimate[q]);
                }
                double wrong_num = Arrays.stream(cur_state_counting_delta, 0, 3).sum() / 2.0;
                if (wrong_num == 0) {
                    cur_queue_accurate_state_100++;
                }
                if (wrong_num <= (j + 1) * 0.05) {
                    cur_queue_accurate_state_95++;
                }
                if (wrong_num <= (j + 1) * 0.1) {
                    cur_queue_accurate_state_90++;
                }
                if (wrong_num <= (j + 1) * 0.2) {
                    cur_queue_accurate_state_80++;
                }
                if (wrong_num <= (j + 1) * 0.3) {
                    cur_queue_accurate_state_70++;
                }
                if (wrong_num <= (j + 1) * 0.4) {
                    cur_queue_accurate_state_60++;
                }
                if (wrong_num <= (j + 1) * 0.5) {
                    cur_queue_accurate_state_50++;
                }

                // output state
//                for (int n = 0; n < s; n++)
//                    System.out.print(cur_nc.charAt(n) + " ");
//                System.out.print("-");
//                for (int n = 0; n < s; n++)
//                    System.out.print(cur_sc[n] + " ");
//                System.out.println();
            }

            total_queue_accurate_state_100 += cur_queue_accurate_state_100;
            total_queue_accurate_state_95 += cur_queue_accurate_state_95;
            total_queue_accurate_state_90 += cur_queue_accurate_state_90;
            total_queue_accurate_state_80 += cur_queue_accurate_state_80;
            total_queue_accurate_state_70 += cur_queue_accurate_state_70;
            total_queue_accurate_state_60 += cur_queue_accurate_state_60;
            total_queue_accurate_state_50 += cur_queue_accurate_state_50;

//            IntStream.range(0, 3).forEach(q -> total_counting_delta[q] += cur_queue_counting_delta[q]);
//            Calculate_Rehandle calculate_tier = new Calculate_Rehandle();
//            total_rehandle[i] = calculate_tier.calculate_LB4(layout, s * t);
//            sum_total_rehandle += total_rehandle[i];

            // output counting result
//            write.println("估算正确的状态数:" + cur_queue_accurate_state_num + "\t" + "总状态数:" + s * t + "\t" + "准确率:" + cur_queue_accurate_state_num * 1.0 / (s * t));
//            write.println("各状态差值:" + "L-" + cur_queue_counting_delta[0] + "\t" + "M-" + cur_queue_counting_delta[1] + "\t" + "H-" + cur_queue_counting_delta[2]);
//                       write.println("step:" + i + " rehandle:" + total_rehandle[i]);
            write.println("100%\t" + String.format("%.2f", cur_queue_accurate_state_100 * 1.0 / (s * t)) + "\t" + "95%\t" + String.format("%.2f", cur_queue_accurate_state_95 * 1.0 / (s * t)) + "\t" + "90%\t" + String.format("%.2f", cur_queue_accurate_state_90 * 1.0 / (s * t)) + "\t" + "80%\t" + String.format("%.2f", cur_queue_accurate_state_80 * 1.0 / (s * t)) + "\t" + "70%\t" + String.format("%.2f", cur_queue_accurate_state_70 * 1.0 / (s * t)) + "\t" + "60%\t" + String.format("%.2f", cur_queue_accurate_state_60 * 1.0 / (s * t)) + "\t" + "50%\t" + String.format("%.2f", cur_queue_accurate_state_50 * 1.0 / (s * t)) + "\t");

        }

        // output counting result
        write.println("100%\t" + String.format("%.4f", total_queue_accurate_state_100 * 1.0 / total_state_num) + "\t" + "95%\t" + String.format("%.4f", total_queue_accurate_state_95 * 1.0 / total_state_num) + "\t" + "90%\t" + String.format("%.4f", total_queue_accurate_state_90 * 1.0 / total_state_num) + "\t" + "80%\t" + String.format("%.4f", total_queue_accurate_state_80 * 1.0 / total_state_num) + "\t" + "70%\t" + String.format("%.4f", total_queue_accurate_state_70 * 1.0 / total_state_num) + "\t" + "60%\t" + String.format("%.4f", total_queue_accurate_state_60 * 1.0 / total_state_num) + "\t" + "50%\t" + String.format("%.4f", total_queue_accurate_state_50 * 1.0 / total_state_num) + "\t");
//        write.println("估算正确的状态数:" + total_accurate_state_num + "\t" + "总状态数:" + total_state_num + "\t" + "准确率:" + total_accurate_state_num * 1.0 / total_state_num);
//        write.println("各状态差值:" + "L|" + total_counting_delta[0] + "\t" + "M|" + total_counting_delta[1] + "\t" + "H|" + total_counting_delta[2]);
//        write.println("total_rehandle:" + sum_total_rehandle + " LB4:" + sum_total_rehandle * 1.00 / string_generate.length);
        fw.close();
        write.close();


    }

    public static void main(String[] args) throws IOException {
        s = 4;
        t = 5;
        gn = new String[]{"1", "10", "100"};
        gn_num = new int[]{1, 10, 100};
        method_name = "Extreme_Plan_two"; //"Extreme_Plan_one" "Extreme_Plan_two" "Average_Plan"
        Evaluate_Counting_Methods counting_methods = new Evaluate_Counting_Methods();
        counting_methods.calculate_total_rehandle();
//        counting_methods.exchange();
//        String cur_nc = "1111";
//        String[] cur_sc = new String[]{"4", "30", "30", "300"};
//        int[] cur_state_type_num_estimate = counting_methods.cal_average_plan(cur_nc, cur_sc);
//        for (int i = 0; i < 3; i++)
//            System.out.print(cur_state_type_num_estimate[i] + "\t");
    }
}
