// calculate rehandles
package com.jacqueline.containerstackingproblem.evaluatingResult;

import com.jacqueline.containerstackingproblem.config.dialogbox;
import com.jacqueline.containerstackingproblem.dataProcess.Read_Dynamic_Result;

import java.io.*;
import java.util.Date;

public class Calculate_Rehandle {
    int n_blocking = 0;
    int[] gn_num;
    long duration = 0;

    void calculate_total_rehandle(int s, int t, String[] gn, double[] ratio) throws IOException {
        Read_Dynamic_Result dataarray = new Read_Dynamic_Result();
        dataarray.datainput("/Users/jacqueline/Code/ContainerStacking/data/dynamic result/EIP_" + String.valueOf(s) + String.valueOf(t) + "_" + gn.length + ".txt", s, gn);

        BufferedReader reader = new BufferedReader(new FileReader(
                "/Users/jacqueline/Desktop/Tsing/Research/ContainerAllocation/毕业论文整理new/storage/data/instances/instance_"
                        + s * t + "_3.txt"));

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

        String[][] string_generate = new String[string_ss.length][s * t];// 鐢熸垚鐨勫疄楠岄槦鍒�

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

        // String[][] string_generate = dataarray.generate_string(s, t, gn,ratio) ;
        double[] total_rehandle_tier = new double[string_generate.length];// 层高差
        double[] total_rehandle_LB1 = new double[string_generate.length];
        double[] total_rehandle_LB3 = new double[string_generate.length];
        double[] total_rehandle_LB4 = new double[string_generate.length];
        for (int i = 0; i < total_rehandle_tier.length; i++) {
            total_rehandle_tier[i] = 0.0;
            total_rehandle_LB1[i] = 0.0;
            total_rehandle_LB3[i] = 0.0;
            total_rehandle_LB4[i] = 0.0;
        }
        double final_total_rehandle_tier = 0.0;
        double final_total_rehandle_LB1 = 0.0;
        double final_total_rehandle_LB3 = 0.0;
        double final_total_rehandle_LB4 = 0.0;

        for (int i = 0; i < string_generate.length; i++) {// 每一个队列，对应一个layout
            // if(i>10000) {
            // break;
            // }
            Date myDate = new Date();
            long begin = myDate.getTime();
            int[][] layout = new int[s][t];
            for (int s1 = 0; s1 < s; s1++) {
                for (int t1 = 0; t1 < t; t1++) {
                    layout[s1][t1] = 0;
                }
            }
            String emp_layout = new String();// layout中的空箱数
            int[] sc_layout = new int[s];// layout中的贝位表示
            for (int k = 0; k < s; k++) {
                emp_layout = emp_layout + String.valueOf(t);
                sc_layout[k] = 0;
            }
            String[] target = string_generate[i];// 到达队列
            String cur_nc = "";// 空箱数量
            String[] cur_sc = new String[s];// 贝位表示状态
            for (int k = 0; k < s; k++)
                cur_sc[k] = "";
            // the initial state
            for (int k = 0; k < s; k++) {
                cur_nc = cur_nc + String.valueOf(t);
                cur_sc[k] = String.valueOf(0);
            }
            for (int j = 0; j < target.length; j++) {// 每一个到达的箱子
//            	System.out.println(target[j]);
                int target_index = 0;
                // find the target index
                for (int k = 0; k < dataarray.size; k++) {
                    if (cur_nc.equals(dataarray.current_state[k][0])) {// nc
                        boolean find = true;
                        for (int h = 0; h < s; h++) {
                            if (!cur_sc[h].equals(dataarray.current_state[k][h + 1]))
                                find = false;
                        }
                        if (find) {
                            target_index = k;// 鎵�鍦ㄥ摢涓�琛�
                            break;
                        }
                    }
                }
                // find the position of the target weight group
                int target_k = 0;
                for (int k = 0; k < gn.length; k++) {
                    if (gn[k].equals(target[j])) {
                        target_k = k;
                        break;
                    }
                }
                // System.out.println("dataarray_size:"+dataarray.size);
                cur_nc = dataarray.next_state[target_index][(s + 1) * target_k + 0];
                for (int h = 0; h < s; h++) {
                    cur_sc[h] = dataarray.next_state[target_index][(s + 1) * target_k + h + 1];
//                  System.out.println(cur_sc[h]);
                }
                int l1 = 0;
                for (l1 = 0; l1 < s; l1++) {
                    if (emp_layout.charAt(l1) > '0') {
                        String emp_layout1 = emp_layout;
                        int[] sc_layout1 = new int[s];
                        for (int h = 0; h < s; h++) {
                            sc_layout1[h] = sc_layout[h];
                        }
                        layout[l1][t - Integer.parseInt(String.valueOf(emp_layout.charAt(l1)))] = Integer
                                .parseInt(target[j]);
                        // adjust nc
                        String first_part = "";
                        String third_part = "";
                        String second_part = "";
                        if (l1 > 0)
                            first_part = emp_layout.substring(0, l1 - 1 - 0 + 1);
                        if (l1 < emp_layout.length() - 1)
                            third_part = emp_layout.substring(l1 + 1,
                                    emp_layout.length() - 1 - (l1 + 1) + 1 + (l1 + 1));
                        int left_empty = Integer.parseInt(String.valueOf(emp_layout.charAt(l1))) - 1;
                        second_part = String.valueOf(left_empty);
                        emp_layout = first_part + second_part + third_part;

                        // adjust sc
//				  if (left_empty == 0) {
//					sc_layout[l1] = t;
//					} else {
                        int div = sc_layout[l1] / Integer.parseInt(String.valueOf(target[j]));

                        if (div == 0) {
                            sc_layout[l1] = Integer.parseInt(String.valueOf(target[j])) * (t - left_empty);
                        } else {
                            if (left_empty + div + 1 <= t) {
                                sc_layout[l1] = Integer.parseInt(String.valueOf(target[j])) * (t - left_empty);
                            }
                        }
//				   }
                        // resequence nc & sc
                        int[] emp_layout2 = new int[s];
                        for (int a = 0; a < s; a++) {
                            emp_layout2[a] = Integer.parseInt(String.valueOf(emp_layout.charAt(a)));
                        }

                        int[] sc_layout2 = new int[s];
                        for (int a = 0; a < s; a++) {
                            sc_layout2[a] = sc_layout[a];
                        }
//				  System.out.println("emp_layout:"+emp_layout);
                        for (int k = 0; k < emp_layout2.length - 1; k++) {
                            for (int m = 0; m < emp_layout2.length - 1 - k; m++) {
                                if (emp_layout2[m] < emp_layout2[m + 1]) {
                                    int temp_nc = emp_layout2[m];
                                    emp_layout2[m] = emp_layout2[m + 1];
                                    emp_layout2[m + 1] = temp_nc;
                                    int temp_sc = sc_layout2[m];
                                    sc_layout2[m] = sc_layout2[m + 1];
                                    sc_layout2[m + 1] = temp_sc;
                                }
                            }
                        }
                        // re-sequence sc
                        boolean needed = true;
                        while (needed) {
                            needed = false;
                            for (int k = 0; k < emp_layout2.length - 1; k++) {
                                int first = emp_layout2[k];
                                int second = emp_layout2[k + 1];
                                int first1 = sc_layout2[k];
                                int second1 = sc_layout2[k + 1];
                                if (first == second && first1 < second1) {
                                    needed = true;
                                    int temp_sc = sc_layout2[k + 1];
                                    sc_layout2[k + 1] = sc_layout2[k];
                                    sc_layout2[k] = temp_sc;
                                }
                            }
                        }
//				for(int n=0;n<s;n++)
//					   System.out.println("nc_layout2["+n+"]:"+emp_layout2[n]);
//				for(int n=0;n<s;n++)
//				       System.out.println("sc_layout2["+n+"]:"+sc_layout2[n]);
                        int b = 0;
                        for (b = 0; b < s; b++) {
                            // if(cur_nc!=null && !cur_nc.equals("")) {
                            if ((emp_layout2[b] != Integer.parseInt(String.valueOf(cur_nc.charAt(b))))
                                    || (sc_layout2[b] != Integer.parseInt(String.valueOf(cur_sc[b])))) {
                                layout[l1][t - Integer.parseInt(String.valueOf(emp_layout1.charAt(l1)))] = 0;
                                emp_layout = emp_layout1;
                                for (int h = 0; h < s; h++) {
                                    sc_layout[h] = sc_layout1[h];
                                }
                                break;
                            }
                            // }
                        }
                        if (b == s) {// 全部相等，即为要找的位置
                            // System.out.println(b);

                            break;
                        }
                    }
                }
                if (l1 == s) {
                    System.out.println("Something is wrong with l1");
                    break;
                }

            }
            /*
             * for(int s0=0;s0<s;s0++) { for(int t0=0;t0<t;t0++) {
             * System.out.println("layout["+s0+"]["+t0+"]="+layout[s0][t0]); } }
             */
            // total_rehandle_tier[i]=calculate_tier_difference2(layout);
//              total_rehandle_LB1[i]=calculate_LB1(layout);
            Date mDate = new Date();
            long end = mDate.getTime();
            duration = duration + end - begin;
            total_rehandle_LB3[i] = calculate_LB3(layout, s * t);
//              total_rehandle_LB4[i]=calculate_LB4(layout,s*t);
//              System.out.println("total rehandle LB3:"+total_rehandle_LB3[i]);
//              System.out.println("total rehandle LB4:"+total_rehandle_LB4[i]);
//              if(total_rehandle_LB3[i]!=total_rehandle_LB4[i]) {
//              	System.out.println("wrong:"+i);
            // break;
//              }
            // final_total_rehandle_tier=final_total_rehandle_tier+total_rehandle_tier[i];
            // final_total_rehandle_LB1=final_total_rehandle_LB1+total_rehandle_LB1[i];
            final_total_rehandle_LB3 = final_total_rehandle_LB3 + total_rehandle_LB3[i];
//              final_total_rehandle_LB4=final_total_rehandle_LB4+total_rehandle_LB4[i];
        }

        System.out.println("s:" + s + "  t:" + t);
        // System.out.println("total rehandle tier
        // difference2:"+final_total_rehandle_tier);
        // System.out.println("total rehandle LB1:"+final_total_rehandle_LB1);
        System.out.println("total rehandle LB3:" + final_total_rehandle_LB3);
//        System.out.println("total rehandle LB4:"+final_total_rehandle_LB4);
        System.out.println("time:" + duration);

        try {

            FileWriter fw = new FileWriter(
                    "total_rehandle_" + String.valueOf(s) + String.valueOf(t) + "_LB3.txt", false);
            PrintWriter write = new PrintWriter(fw);
            write.println(s + "\t" + t + "\t" + string_generate.length + "\t" + final_total_rehandle_LB3 + "\t"
                    + duration + "\t");
            fw.close();
            write.close();
        } catch (IOException f) {
            dialogbox dialog = new dialogbox();
            dialog.dialogbox("error", f.toString());
        }

    }

    int calculate_tier_difference(int[][] layout) {
        int result = 0;
        int s = layout.length;
        int t = layout[0].length;
        for (int i = 0; i < s; i++) {
            for (int j = 1; j < t; j++) {
                int diff = 0;
                for (int k = j - 1; k >= 0; k--) {
                    if (layout[i][k] > layout[i][j]) {
                        if (layout[i][k] != layout[i][k + 1])
                            diff = j - k;
                        else
                            diff = j - (k + 1);
                    }
                }
                result = result + diff;

            }
        }
        return result;
    }

    int calculate_tier_difference2(int[][] layout) {
        int result = 0;
        int s = layout.length;
        int t = layout[0].length;
        for (int i = 0; i < s; i++) {
            for (int j = 1; j < t; j++) {
                int diff = 0;
                for (int k = j - 1; k >= 0; k--) {
                    if (layout[i][k] > layout[i][j]) {

                        diff = j - k;

                    }
                }
                result = result + diff;

            }
        }
        return result;
    }

    int calculate_LB1(int[][] layout) {
        int LB1 = 0;

        int s = layout.length;
        int t = layout[0].length;
        for (int i = 0; i < s; i++) {
            for (int j = 1; j < t; j++) {
                int flag = 0;
                for (int k = j - 1; k >= 0; k--) {
                    if (layout[i][k] > layout[i][j])
                        flag = 1;
                }
                if (flag == 1)
                    LB1++;
            }
        }
        return LB1;
    }

    public int calculate_LB3(int[][] layout, int containernum) {
        int LB3 = 0;
        int s = layout.length;
        int t = layout[0].length;
        int[][] layout2 = new int[s][t];
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < t; j++) {
                layout2[i][j] = layout[i][j];
            }
        }
        // LB1
        for (int i = 0; i < s; i++) {
            for (int j = 1; j < t; j++) {
                int flag = 0;
                for (int k = j - 1; k >= 0; k--) {
                    if (layout[i][k] > layout[i][j])
                        flag = 1;
                }
                if (flag == 1)
                    LB3++;
            }
        }
        // LB2
        while (!isLayoutNull(layout2)) {
            int targetone = FindTargetOne(layout2);
            int[][] layout3 = new int[s][t];
            int[] targetoneLocation = getLocation(targetone, layout2);
            for (int i = 0; i < s; i++) {
                for (int j = 0; j < t; j++) {
                    if ((layout2[i][j] == targetone) && (i != targetoneLocation[0])) {
                        for (int k = j; k < t; k++)
                            layout3[i][k] = 0;
                    } else if ((layout2[i][j] == targetone) && (i == targetoneLocation[0])
                            && (j > targetoneLocation[1])) {
                        layout3[i][j] = layout2[i][j];
                        for (int k = j; k < t; k++)
                            layout3[i][k] = 0;
                    } else {
                        layout3[i][j] = layout2[i][j];
                    }
                }

            }
            LB3 = LB3 + get2LB(targetone, containernum, layout3);
            // 移除目标集装箱及其上的blockingcontainer
            int stackheight = getStackHeight(layout2, targetoneLocation[0]);
            int count = 0;
            for (int j = targetoneLocation[1]; j < stackheight; j++) {
                layout2[targetoneLocation[0]][j] = 0;
                // count++;
            }
            // System.out.println("count:"+count);
            // System.out.println("LB: "+LB);

        }
        LB3 = LB3 + StillLeftContainernum(layout2);
        return LB3;
    }

    public int calculate_LB4(int[][] layout, int containernum) {
        int LB4 = calculate_LB1(layout);
        int s = layout.length;
        int t = layout[0].length;
        // System.out.println(LB4);
        // LB2
        while (!isLayoutNull(layout)) {
            int targetone = FindTargetOne(layout);
            int[][] layout2 = new int[s][t];
            int[] targetoneLocation = getLocation(targetone, layout);
            for (int i = 0; i < s; i++) {
                for (int j = 0; j < t; j++) {
                    if ((layout[i][j] == targetone) && (i != targetoneLocation[0])) {
                        for (int k = j; k < t; k++)
                            layout2[i][k] = 0;
                    } else if ((layout[i][j] == targetone) && (i == targetoneLocation[0])
                            && (j > targetoneLocation[1])) {
                        for (int k = j; k < t; k++)
                            layout2[i][k] = 0;
                    } else {
                        layout2[i][j] = layout[i][j];
                    }
                }

            }
            /*
             * for(int i=0;i<s;i++) { for(int j=0;j<t;j++) {
             * System.out.println("layout2["+i+"]["+j+"]:"+layout2[i][j]); } }
             */
            // System.out.println("x:"+targetoneLocation[0]);
            // System.out.println("y:"+targetoneLocation[1]);
            // System.out.println("getLB4:"+getLB4(targetone, layout2));
            LB4 = LB4 + getLB4(targetone, layout2);

            // 移除目标集装箱及其上的blockingcontainer
            int stackheight = getStackHeight(layout, targetoneLocation[0]);
            int count = 0;
            for (int j = targetoneLocation[1]; j < stackheight; j++) {
                layout[targetoneLocation[0]][j] = 0;
                // count++;
            }
            // System.out.println("count:"+count);
            // System.out.println("LB: "+LB);

        }
        LB4 = LB4 + StillLeftContainernum(layout);
        return LB4;
    }

    public int getLB4(int targetcontainerid, int[][] layout) {

        int LB4 = 0;
        int s = layout.length;
        int t = layout[0].length;
        int[] targetonelocation = getLocation(targetcontainerid, layout);
        int[] U = new int[s];
        int r = 0;// r个可放的stack
        for (int i = 0; i < s; i++) {
            if ((i != targetonelocation[0]) && (getStackHeight(layout, i) < t)) {
                // if(i!=targetonelocation[0]) {
                r++;
                U[r] = i;// 对应的stack
            }
        }
        int[] u = new int[r + 1];
        u[0] = -1;
        for (int i = 1; i < r + 1; i++) {
            u[i] = U[i];
        }
        int[] Q = new int[s];
        for (int i = 0; i < s; i++) {
            Q[i] = NewFindStackMaxId(layout, i, t); // 找出该stack最大的集装箱类型
        }
        Q[targetonelocation[0]] = -10000;
//    	System.out.println("Q[0]:"+Q[0]);
//    	for(int k=1;k<r+1;k++) {
//    		System.out.println("Qu"+k+":"+Q[u[k]]);
//    	}
        int[] u_mark = new int[r + 1];
        u_mark[0] = -1;
        for (int w = 1; w < r + 1; w++) {
            int k = 2;
            int maxQ = Q[u[1]];
            u_mark[w] = u[1];
            for (k = 2; k < r + 1; k++) {
                if (maxQ < Q[u[k]]) {
                    maxQ = Q[u[k]];
                    u_mark[w] = u[k];
                }
            }
            for (int a = 1; a < r + 1; a++) {
                if (u_mark[w] == u[a])
                    u[a] = targetonelocation[0];
            }
//    	    System.out.println("k:"+k);

        }

        // for(int i=1;i<r+1;i++){
        // System.out.println("u_mark"+i+":"+u_mark[i]);
        // }
        int[] q = new int[r + 1];
        q[0] = Integer.MAX_VALUE;
        for (int k = 1; k < r + 1; k++) {
            q[k] = Q[u_mark[k]];
        }
        int m = 0;
        int[] v = new int[t + 1];
        v[0] = -1;
        for (int j = 1; j < t + 1; j++)
            v[j] = -1;
        int ts = targetonelocation[0];// 目标堆垛stack number
        int Ns = getStackHeight(layout, targetonelocation[0]);// 目标堆垛层高
        for (int j = targetonelocation[1] + 1; j < Ns; j++) {
            if (layout[ts][j] >= q[r]) {
                m++;// 可能不会压箱的container个数
                v[m] = j;// 不会压箱的container所在的原始层高
            }
        }
        // System.out.println("m:"+m);
        if (m <= 1)
            n_blocking = 0;
        else {
            n_blocking = m - 1;
            Comp(m, v, 0, q, layout, ts);// 放置第m个可能不会压箱的container
        }
        return LB4 = n_blocking + (Ns - 1 - targetonelocation[1] - m);
    }

    public void Comp(int j, int[] v, int n, int[] q, int[][] layout, int ts) {
        if (j >= 0) {
            int k = 0;
            int r = q.length - 1;
            if (j == 0)
                n_blocking = n; // 当前压箱数

            else {
                // System.out.println("j:"+j);
                if (layout[ts][v[j]] >= q[r]) {
                    int min_gap = layout[ts][v[j]] - q[r];
                    for (int i = r; i > 0; i--) {
                        if (min_gap < layout[ts][v[j]] - q[i] && (layout[ts][v[j]] >= q[i])) {
                            min_gap = layout[ts][v[j]] - q[i];
                            k = i;
                        }
                    }
                    q[k] = layout[ts][v[j]];
                } else
                    k = r + 1;
            }

            if (k <= r)
                Comp(j - 1, v, n, q, layout, ts);
            if (k > 1 && n + 1 < n_blocking) {
                Comp(j - 1, v, n + 1, q, layout, ts);
            }
        }
    }

    public int StillLeftContainernum(int[][] layout) {// layout中还剩多少个container
        int stillnum = 0;
        int s = layout.length;
        int t = layout[0].length;
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < t; j++) {
                if (layout[i][j] != 0) {
                    stillnum++;
                }
            }
        }
        return stillnum;
    }

    public boolean isLayoutNull(int[][] layout) {// 给定一个layout，判断layout是否为空，如果为空则返回true，否则为false
        int flag = 0;
        int s = layout.length;
        int t = layout[0].length;
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < t; j++) {
                if (layout[i][j] != 0) {
                    flag = 1;
                    break;
                }
            }
        }
        if (flag == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int FindTargetOne(int[][] layout) {// 找到targetcontiainer的containerid
        int targetone = (int) Double.MIN_VALUE;
        int s = layout.length;
        int t = layout[0].length;
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < t; j++) {
                if (layout[i][j] > targetone) {
                    targetone = layout[i][j];
                }
            }
        }
        return targetone;
    }

    public int get2LB(int targetcontainerid, int originalcontainernum, int[][] layout) {// 给定目标集装箱id，得到LB2
        int LB2 = 0;
        int[] targetonelocation = getLocation(targetcontainerid, layout);
        int height = getStackHeight(layout, targetonelocation[0]);
        for (int j = height - 1; j > targetonelocation[1]; j--) {// 对压着集装箱
            int blockingcontainer = layout[targetonelocation[0]][j];
            if (blockingcontainer != 0 && blockingcontainer != targetcontainerid
                    && checkBlockingBigger(layout, targetonelocation[0], blockingcontainer, originalcontainernum)) {
                LB2++;
            }
        }
        return LB2;
    }

    public int[] getLocation(int contianerid, int[][] layout) {// 给定contianerid给出它在layout中的位置（行列），0起头
        int[] location = new int[2];
        int s = layout.length;
        int t = layout[0].length;
        for (int i = 0; i < s; i++) {
            for (int j = t - 1; j >= 0; j--) {
                if (layout[i][j] == contianerid) {
                    location[0] = i;
                    location[1] = j;
                    break;
                }
            }
        }
        return location;
    }

    public boolean checkBlockingBigger(int[][] layout, int targetstackno, int blockingcontainer,
                                       int originalcontainernum) {// 给定目标集装箱所在堆垛以及blockingcontainer，判断blockcon是否大于所有堆垛最小id
        int flag = 1;
        int s = layout.length;
        int t = layout[0].length;
        for (int i = 0; i < s; i++) {
            if (i != targetstackno && NewFindStackMaxId(layout, i, originalcontainernum) <= blockingcontainer
                    && getStackHeight(layout, i) < t) {// 存在非blocking的stack位置
                flag = 0;
                break;
            }
        }
        if (flag == 0) {
            return false;
        } else {
            return true;
        }

    }

    public int NewFindStackMaxId(int[][] layout, int stackno, int originalconnum) {// 给定stackno，给出该stack中id最大的id，stackno从0开始，高度为0则为connum+1
        int maxid = (int) Double.MIN_VALUE;
        int s = layout.length;
        int t = layout[0].length;
        if (getStackHeight(layout, stackno) == 0) {
            maxid = 0;
        } else {
            for (int j = t - 1; j >= 0; j--) {
                if (layout[stackno][j] != 0 && layout[stackno][j] > maxid) {
                    maxid = layout[stackno][j];
                }
            }
        }
        return maxid;
    }

    public int getStackHeight(int[][] layout, int stackno) {// 给定stack编号给出该stack有多少个集装箱
        int s = layout.length;
        int t = layout[0].length;
        int height = 0;
        for (int j = 0; j < t; j++) {
            if (layout[stackno][j] != 0) {
                height++;
            }
        }
        return height;
    }

}
