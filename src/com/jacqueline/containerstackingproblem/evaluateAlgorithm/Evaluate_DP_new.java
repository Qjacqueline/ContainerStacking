package com.jacqueline.containerstackingproblem.evaluateAlgorithm;

import com.jacqueline.containerstackingproblem.dataProcess.Read_Dynamic_Result;
import com.jacqueline.containerstackingproblem.evaluatingResult.Calculate_Rehandle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Evaluate_DP_new {
    public static int s;
    public static int t;
    public static String[] gn;
    public static int[] gn_num;
    public static Read_Dynamic_Result dynamic_result;
    public static int[] total_rehandle;
    public static int final_total_rehandle;

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
//        dynamic_result.datainput("/Users/jacqueline/Code/ContainerStacking/data/dynamic result/EIP_" + String.valueOf(s) + String.valueOf(t) + "_" + gn.length + ".txt", s, gn);
        dynamic_result.datainput("/Users/jacqueline/Code/ContainerStacking/data/dynamic result/storevalue_" + String.valueOf(s) + String.valueOf(t) + "_" + gn.length + ".txt", s, gn);

        // 可能到达队列
        String[][] string_generate = get_queue();
        // 计算
        Calculate_Rehandle calculate_tier = new Calculate_Rehandle();
        total_rehandle = new int[string_generate.length];
        for (int i = 0; i < total_rehandle.length; i++) {
            total_rehandle[i] = 0;
        }
        final_total_rehandle = 0;
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

            for (int j = 0; j < target.length; j++) {
                int[] cur_state_counting_delta = new int[3]; // 当前队列每个集装箱类别估算的差值
//                cur_queue_type_num_accurate[target[j].length() - 1]++; // 当前集装箱类型计数加1
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
                        Collections.sort(same_nc, Collections.reverseOrder());
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

            }

            total_rehandle[i] = calculate_tier.calculate_LB4(layout, s * t);
            final_total_rehandle = final_total_rehandle + total_rehandle[i];
        }

    }

    public static void main(String[] args) throws IOException {
//        int[][] list_three = new int[][]{{4, 3}, {5, 3}, {4, 4}, {4, 5}, {5, 4}, {5, 5}, {6, 3}, {6, 4}, {6, 5}, {7, 3}, {7, 4}, {7, 5}, {8, 3}, {8, 4}, {8, 5}};
        int[][] list_three = new int[][]{{4, 5}};
        for (int i = 0; i < list_three.length; i++) {
            s = list_three[i][0];
            t = list_three[i][1];
            gn = new String[]{"10", "1"};//{"10", "1"}; //{"100", "10", "1"};
            long begin_time = new Date().getTime();
            Evaluate_DP_new evaluateDpNew = new Evaluate_DP_new();
            evaluateDpNew.calculate_total_rehandle();
            long end_time = new Date().getTime();
            System.out.print(final_total_rehandle * 1.00 / total_rehandle.length + "\t");
            System.out.println(MessageFormat.format("贝位结构:s={0} t={1}\t当前队列长度:{2}\t算法决策所需rehandle:{3}\tPI:{4}\t运行时长:{5}", s, t, total_rehandle.length, final_total_rehandle, (float) final_total_rehandle * 1.00 / total_rehandle.length, (end_time - begin_time)));
        }


    }
}
