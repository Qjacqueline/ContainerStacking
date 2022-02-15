// 评估训练好的网络 BPNN层高无关型
package com.jacqueline.containerstackingproblem.evaluatingResult;

import com.jacqueline.containerstackingproblem.algorithm.BPNNAlgorithm;
import com.jacqueline.containerstackingproblem.algorithm.Dynamic_programming_New2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

//heaviest+tier gap
public class Evaluating_BPNN2 {
    int final_total_rahandle;
    int num;
    int num_test;
    double[][] layer;
    //	double[][][] layer_weight;
    double mobp;
    int[] layernum;
    double rate;
    double accuracy_train;
    double accuracy_test;
    double Error_sum;
    public BPNNAlgorithm bpnnAlgorithm;

    public Evaluating_BPNN2(int[] layernum, double rate, double mobp) {
        bpnnAlgorithm = new BPNNAlgorithm(layernum, rate, mobp);
    }

    // BPNN层高无关
    public BPNNAlgorithm train_Modle(int[] layernum, double rate, double mobp, int[] gn, int t, int s)
            throws IOException {
        this.layernum = layernum;
        this.rate = rate;
        this.mobp = mobp;

        BPNN_Prepare3 bPrepare = new BPNN_Prepare3();//train set
        BPNN_Prepare3 bPrepare2 = new BPNN_Prepare3();// test set
        bPrepare.datainput("/Users/jacqueline/Desktop/Tsing/Research/ContainerAllocation/毕业论文整理 3/storage/data/G=3_new2019/train/train" + s + t + ".txt", gn, t);
        bPrepare2.datainput("/Users/jacqueline/Desktop/Tsing/Research/ContainerAllocation/毕业论文整理 3/storage/data/G=3_new2019/test/test" + s + t + ".txt", gn, t);

        int num = 0;
        int num_test = 0;
        accuracy_train = 0.0;
        accuracy_test = 0.0;
        Error_sum = 0.0;
        int length = 12000;
        double[] error = new double[length];
        for (int n = 0; n < length; n++) {
            double sum = 0.0;
//			for (int i = 0; i < bPrepare.data.length; i++) {
//				bpnnAlgorithm.train(bPrepare.data[i], bPrepare.target[i]);

//		    }

        }

        /*
         * try { FileWriter fw = new
         * FileWriter("/Users/yuanguoping/Desktop/storage/data/NN/ceng_three_2_new.txt",
         * false); PrintWriter write = new PrintWriter(fw); for(int
         * j=0;j<bpnnAlgorithm.layer_weight[0].length;j++) { for(int
         * k=0;k<bpnnAlgorithm.layer_weight[0][j].length;k++) {
         * write.print(bpnnAlgorithm.layer_weight[0][j][k]+"\t"); } write.println(); }
         * write.println(); for(int j=0;j<bpnnAlgorithm.layer_weight[1].length;j++) {
         * for(int k=0;k<bpnnAlgorithm.layer_weight[1][j].length;k++) {
         * write.print(bpnnAlgorithm.layer_weight[1][j][k]+"\t"); } write.println(); }
         * fw.close(); write.close(); }catch(IOException f){ dialogbox dialog=new
         * dialogbox(); dialog.dialogbox("error",f.toString()); }
         *
         * for(int j=0;j<bpnnAlgorithm.layer_weight[0].length;j++) { for(int
         * k=0;k<bpnnAlgorithm.layer_weight[0][j].length;k++) {
         * System.out.println(bpnnAlgorithm.layer_weight[0][j][k]); } }
         */
        int mid = 10;
        double[][][] layer_weight = new double[3][][];
        layer_weight[0] = new double[gn.length * 4 + 2][mid];// 第一层神经元数量+1
        layer_weight[1] = new double[mid + 1][gn.length * 2 + 1];
        int i = 0;
        int j = 0;
        int m = 0;
        int k = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new FileReader("/Users/yuanguoping/Desktop/storage/data/NN/ceng_three_2_new.txt"));
            String line = null;
            String[] sp;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("")) {
                    i++;
                    continue;
                }
                sp = line.trim().split("\t");

                if (i == 0) {
                    for (int x = 0; x < sp.length; x++) {
                        layer_weight[i][j][x] = Double.parseDouble(sp[x]);
                    }
                    j++;
                }
                if (i == 1) {
                    for (int x = 0; x < sp.length; x++) {
                        layer_weight[i][m][x] = Double.parseDouble(sp[x]);
                    }
                    m++;
                }
                if (i == 2) {
                    for (int x = 0; x < sp.length; x++) {
                        layer_weight[i][k][x] = Double.parseDouble(sp[x]);
                    }
                    k++;
                }
            }
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        bpnnAlgorithm.layer_weight = layer_weight;
        // accuracy
        /*
         * int sum1 = bPrepare.data.length; for (int a = 0; a < bPrepare.data.length;
         * a++) { double[] result = bpnnAlgorithm.computeOut(bPrepare.data[a]); if
         * (Arrays.equals(bPrepare.target[j], bpnnAlgorithm.adjust(result))) { num++; }
         * }
         *
         * accuracy_train = (double) num / sum1;
         * System.out.println("Accuracy of train dataset:" + accuracy_train);
         */
        int sum_test = bPrepare2.data.length;
        for (int y = 0; y < bPrepare2.data.length; y++) {
            double[] result_test = bpnnAlgorithm.computeOut(bPrepare2.data[y]);

            if (Arrays.equals(bPrepare2.target[y], bpnnAlgorithm.adjust(result_test))) {
                num_test++;
            }
            /*
             * System.out.println("y:"+y); for(int c=0;c<result_test.length;c++) {
             * System.out.print(bPrepare2.target[y][c]+" "); } System.out.println();
             *
             * for(int c=0;c<result_test.length;c++) {
             * System.out.print(bpnnAlgorithm.adjust(result_test)[c]+" "); }
             * System.out.println();
             */
        }
        accuracy_test = (double) num_test / sum_test;
        System.out.println("Accuracy of test dataset:" + accuracy_test);

        // writerTrain.close();
        // writerTest.close();
        return bpnnAlgorithm;

    }

    public void evaluating_BPNN_function(int s, int t, String[] gn, double[] ratio, BPNNAlgorithm bpnnAlgorithm)
            throws IOException {
        //// 读取所有可能到达序列并处理 ////
        Calculate_Rehandle calculate_tier = new Calculate_Rehandle();
        Date myDate = new Date();
        long begin_time = myDate.getTime();
        BufferedReader reader = new BufferedReader(new FileReader("/Users/jacqueline/Desktop/Tsing/Research/ContainerAllocation/毕业论文整理new/storage/data/instances/instance_" + s * t + "_3.txt"));// instance_35_2.txt 所有可能达到序列
        String[] string_ss = new String[10000];
        int index1 = 0;
        String line = null;
        while ((line = reader.readLine()) != null) {
            string_ss[index1] = line;
            index1++;
            if (index1 > 9999)
                break;
        }
        reader.close();
        String[][] string_generate = new String[string_ss.length][s * t];
        for (int i = 0; i < string_generate.length; i++) {
            for (int j = 0; j < string_generate[i].length; j++) {
                if (gn.length == 2) {
                    if (string_ss[i].charAt(j) == 'H') {
                        string_generate[i][j] = "10";
                    }
                    if (string_ss[i].charAt(j) == 'L') {
                        string_generate[i][j] = "1";
                    }
                }
                if (gn.length == 3) {
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

        // 计算
        int[] total_rehandle = new int[string_generate.length];
        for (int i = 0; i < total_rehandle.length; i++) {
            total_rehandle[i] = 0;
        }
        final_total_rahandle = 0;


        for (int i = 0; i < string_generate.length; i++) { // for循环每条可能的序列
            String[] target = string_generate[i];
            String cur_nc = "";
            String[] cur_sc = new String[s];
            for (int k = 0; k < s; k++) {
                cur_nc = cur_nc + String.valueOf(t);
                cur_sc[k] = String.valueOf(0);
            }
            int[][] layout = new int[s][t];
            for (int s1 = 0; s1 < s; s1++) {
                for (int t1 = 0; t1 < t; t1++) {
                    layout[s1][t1] = 0;
                }
            }
            for (int j = 0; j < target.length; j++) { // 每生成的一条队列中的每一个container
                // 当前container type对应数字
                int target_k = 0;
                for (int k = 0; k < gn.length; k++) {
                    if (target[j].equals(gn[k])) {
                        target_k = k;
                        break;
                    }
                }
                int[] BP_input = new int[gn.length * 4 + 1];
                for (int x = 0; x < BP_input.length; x++) {
                    BP_input[x] = 0;
                }
                for (int nc = 0; nc < cur_nc.length(); nc++) {
                    int emp = (int) cur_nc.charAt(nc) - (int) ('0');
                    int ti = (int) cur_sc[nc].charAt(0) - (int) ('0');
                    if (String.valueOf(cur_nc.charAt(nc)).equals(String.valueOf(t))) {
                        BP_input[gn.length * 2] = 1;// 将堆垛表示的状态转变成神经网络的输入状态
                    }
                    if (gn.length == 2) {
                        if (cur_nc.charAt(nc) != '0') {
                            if (t - emp == ti) {
                                if (Integer.parseInt(String.valueOf(cur_sc[nc])) >= 10) {
                                    BP_input[0] = 1;
                                } else {
                                    BP_input[2] = 1;
                                }
                            } else {
                                if (Integer.parseInt(String.valueOf(cur_sc[nc])) >= 10) {
                                    BP_input[1] = 1;
                                } else {
                                    BP_input[3] = 1;
                                }
                            }
                        }
                    }
                    if (gn.length == 3) {
                        if (cur_nc.charAt(nc) != '0') {
                            if (t - emp == ti) {
                                if (Integer.parseInt(String.valueOf(cur_sc[nc])) >= 100) {
                                    BP_input[0] = 1;
                                } else if (Integer.parseInt(String.valueOf(cur_sc[nc])) >= 10) {
                                    BP_input[2] = 1;
                                } else {
                                    BP_input[4] = 1;
                                }
                            } else {
                                if (Integer.parseInt(String.valueOf(cur_sc[nc])) >= 100) {
                                    BP_input[1] = 1;
                                } else if (Integer.parseInt(String.valueOf(cur_sc[nc])) >= 10) {
                                    BP_input[3] = 1;
                                } else {
                                    BP_input[5] = 1;
                                }

                            }
                        }
                    }

                }
                // 调用动态规划里的container ratio计算方法
                Dynamic_programming_New2 dynamic = new Dynamic_programming_New2();
                int sc[] = new int[cur_nc.length()];
                for (int k = 0; k < cur_nc.length(); k++) {
                    sc[k] = Integer.parseInt(String.valueOf(cur_sc[k]));
                }
                int[] int_gn = new int[gn.length];
                if (gn.length == 2) {
                    int_gn[0] = 10;
                    int_gn[1] = 1;
                }
                if (gn.length == 3) {
                    int_gn[0] = 100;
                    int_gn[1] = 10;
                    int_gn[2] = 1;
                }
                double[] ratio_adjust = dynamic.calculate_Containers_ratio(cur_nc, sc, int_gn, t);
                for (int k = 0; k < ratio_adjust.length; k++) {
                    if (ratio_adjust[k] > 0)
                        BP_input[gn.length * 2 + 1 + k] = 1;
                }
                BP_input[3 * gn.length + target_k + 1] = 1;
                System.out.println("BP_input:"); // 神经网络输入
                for (int a = 0; a < BP_input.length; a++) {
                    System.out.print(BP_input[a] + " ");
                }
                double[] result_BPNN = bpnnAlgorithm.computeOut(BP_input);
                int[] result_BPNN_adjust = bpnnAlgorithm.adjust(result_BPNN);
//				System.out.println("result_BPNN_adjust:");
//				for(int a=0;a<result_BPNN_adjust.length;a++) {
//					System.out.print(result_BPNN_adjust[a]+" ");
//				}
                int BPNN_index = 0;
                for (int l = 0; l < result_BPNN_adjust.length; l++) {
                    if (result_BPNN_adjust[l] == 1) {
                        BPNN_index = l;
                        break;
                    }
                }
//				System.out.println("BPNN_index:"+BPNN_index);
                // 把神经网络的结果距最重箱层高差+最重箱记录在outcome中
                int[] outcome = new int[2];
                switch (gn.length) {
                    case 2:
                        switch (BPNN_index) {
                            case 0:
                                outcome[0] = 1;
                                outcome[1] = 10;
                                break;
                            case 1:
                                outcome[0] = 2;
                                outcome[1] = 10;
                                break;
                            case 2:
                                outcome[0] = 1;
                                outcome[1] = 1;
                                break;
                            case 3:
                                outcome[0] = 2;
                                outcome[1] = 1;
                                break;
                            case 4:
                                outcome[0] = 3;
                                outcome[1] = 0;
                                break;
                            default:
                                break;
                        }
                        break;
                    case 3:
                        switch (BPNN_index) {
                            case 0:
                                outcome[0] = 1;
                                outcome[1] = 100;
                                break;
                            case 1:
                                outcome[0] = 2;
                                outcome[1] = 100;
                                break;
                            case 2:
                                outcome[0] = 1;
                                outcome[1] = 10;
                                break;
                            case 3:
                                outcome[0] = 2;
                                outcome[1] = 10;
                                break;
                            case 4:
                                outcome[0] = 1;
                                outcome[1] = 1;
                                break;
                            case 5:
                                outcome[0] = 2;
                                outcome[1] = 1;
                                break;
                            case 6:
                                outcome[0] = 3;
                                outcome[1] = 0;
                                break;
                            default:
                                break;
                        }
                    default:
                        break;
                }
//				System.out.println("outcome[0]:"+outcome[0]);
                String nc_pre = cur_nc;
                String[] sc_pre = cur_sc;
                String[][] state_update = new String[3][];
                state_update[0] = new String[1];
                state_update[1] = new String[cur_sc.length];
                state_update[2] = new String[1];
                state_update = state_update(nc_pre, sc_pre, outcome, gn, target_k, t);
//				System.out.println("nc_pre:"+nc_pre);
//				System.out.println("sc_pre:");
//				for(int a=0;a<sc_pre.length;a++) {
//					System.out.print(sc_pre[a]+" ");
//				}
                cur_nc = state_update[0][0]; // 当前的空箱状态
//				System.out.println("sc_pre after:");
//				for(int a=0;a<sc_pre.length;a++) {
//					System.out.print(sc_pre[a]+" ");
//				}
                cur_sc = state_update[1]; // 当前的堆垛状态
//				System.out.println("sc_pre:"+sc_pre[0]);
                int index = Integer.parseInt(state_update[2][0]);
                int tier = t - Integer.parseInt(String.valueOf(cur_nc.charAt(index)));
//				System.out.println("cur_nc after:"+cur_nc);
//				System.out.println("cur_sc after:");
                for (int a = 0; a < cur_sc.length; a++) {
//				   System.out.print(cur_sc[a]+" ");
                }
//				System.out.println();
//				System.out.println("index:" +index+"   current tier:"+tier);

                layout[index][tier - 1] = Integer.parseInt(target[j]);
            }
            total_rehandle[i] = calculate_tier.calculate_LB3(layout, s * t);
            final_total_rahandle = final_total_rahandle + total_rehandle[i];
            for (int a = t - 1; a >= 0; a--) {
                for (int b = 0; b < s; b++) {
//					System.out.print(layout[b][a]+" ");
                }
//				System.out.println();
            }
        }

        Date myDate2 = new Date();
        long end_time = myDate2.getTime();
        long duration = end_time - begin_time;
        System.out.println("duichang:" + string_generate.length + "  BP_rehandle:" + final_total_rahandle + " duration:"
                + duration);
    }

    // decoding：退化成神经网络的表达形式：输入前状态，神经网络结果，输出堆放到哪个堆垛以及更新状态
    public static String[][] state_update(String nc_pre, String[] sc_pre, int[] outcome, String[] gn, int k, int t) {
//		System.out.println("enter");
        int[] tier_b = new int[sc_pre.length]; // 最重箱所在层高，若满设为0
        int[] gn_b = new int[sc_pre.length];// 最重箱类型
        for (int i = 0; i < sc_pre.length; i++) {
            tier_b[i] = 10000;
            gn_b[i] = 10000;
            if (Integer.parseInt(String.valueOf(nc_pre.charAt(i))) > 0) {
                tier_b[i] = (int) sc_pre[i].charAt(0) - (int) ('0');
                if (Integer.parseInt(sc_pre[i]) >= 100) {
                    gn_b[i] = 100;
                } else if (Integer.parseInt(sc_pre[i]) >= 10) {
                    gn_b[i] = 10;
                } else if (Integer.parseInt(sc_pre[i]) >= 1) {
                    gn_b[i] = 1;
                } else {
                    gn_b[i] = 0;
                }
            } else {
                gn_b[i] = Integer.parseInt(sc_pre[i]);
            }
        }

        int len = 0;
        for (int i = 0; i < nc_pre.length(); i++) {
            if (outcome[0] == 1) { // 差=1
                if (t - Integer.parseInt(String.valueOf(nc_pre.charAt(i))) + 1 - tier_b[i] == 1 && gn_b[i] == outcome[1])
                    len++;// 找有哪几个堆垛符合神经网络的结果
            }
            if (outcome[0] == 2) { // 差>1
                if (t - Integer.parseInt(String.valueOf(nc_pre.charAt(i))) + 1 - tier_b[i] > 1 && gn_b[i] == outcome[1])
                    len++;
            }
            if (outcome[0] == 3) { // Empty
                if (tier_b[i] == 0 && gn_b[i] == outcome[1])
                    len++;
            }

        }
//		System.out.println("outcome[1]:"+location[1]);
//		System.out.println("len:"+len);
        int[] NN_index = new int[len];
        for (int c = 0; c < nc_pre.length(); c++) {
//	    	System.out.println("tier_b"+tier_b[c]);
//	    	System.out.println("gn_b:"+gn_b[c]);
        }
        int j = 0;

        switch (outcome[0]) {
            case 1:
                for (int i = 0; i < nc_pre.length(); i++) {
                    if (t - Integer.parseInt(String.valueOf(nc_pre.charAt(i))) + 1 - tier_b[i] == 1 && gn_b[i] == outcome[1]) {
                        NN_index[j] = i;
                        j = j + 1;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < nc_pre.length(); i++) {
                    if (t - Integer.parseInt(String.valueOf(nc_pre.charAt(i))) + 1 - tier_b[i] > 1 && gn_b[i] == outcome[1]) {
                        NN_index[j] = i;
                        j = j + 1;
                    }
                }
                break;
            case 3:
                for (int i = 0; i < nc_pre.length(); i++) {
                    if (tier_b[i] == 0 && gn_b[i] == outcome[1]) {
                        NN_index[j] = i;
                        j = j + 1;
                    }
                }
                break;
            default:
                break;
        }

        for (int c = 0; c < len; c++) {
//			System.out.println("NN_index:"+NN_index[c]);
        }
        int[] a_array = new int[nc_pre.length()];// 把字符串变成了数组
        for (int l = 0; l < nc_pre.length(); l++) {
            a_array[l] = Integer.parseInt(String.valueOf(nc_pre.charAt(l)));
        }
        // 把到达集装箱由0，1，2转变成100，10，1
        int k_gn = -1;
        if (gn.length == 2) {
            switch (k) {
                case 0:
                    k_gn = 10;
                    break;
                case 1:
                    k_gn = 1;
                    break;
            }
        }
        if (gn.length == 3) {
            switch (k) {
                case 0:
                    k_gn = 100;
                    break;
                case 1:
                    k_gn = 10;
                    break;
                case 2:
                    k_gn = 1;
                    break;
            }
        }
        int index = -1;
        int max_emp = 0;// 空位数
        int min_emp = 100;
        int min_gap = 100;
        int max_stack = 0;
        switch (len) {
            case 0: // 到达的集装箱类型比堆垛类型重，则选择第一个满足条件的堆垛; 若到达的集装箱类型比堆垛类型轻，则选择最重箱所在层高最高的堆垛。
                index = FindIndex(a_array, sc_pre, gn, k_gn, t);
                break;
            case 1: // 选择唯一堆垛。
                index = NN_index[0];
                break;
            default: // 由多个满足条件，到达的集装箱类型比堆垛类型重，则选择第一个满足条件的堆垛; 若到达的集装箱类型比堆垛类型轻，则选择最重箱所在层高最高的堆垛。
//			index=FindIndex(a_array, b, gn, k_gn, t);
                switch (outcome[0]) {
                    case 3:
                        index = NN_index[0];
                        break;
                    case 1:
                        if (gn.length == 2) {
                            for (int z = 0; z < NN_index.length; z++) {
                                if (k_gn == 10) {
                                    if (min_emp > a_array[NN_index[z]]) {
                                        min_emp = a_array[NN_index[z]];
                                        index = NN_index[z];
                                    }
                                } else {
                                    if (max_emp < a_array[NN_index[z]]) {
                                        max_emp = a_array[NN_index[z]];
                                        index = NN_index[z];
                                    }
                                }
                            }
                        }
                        if (gn.length == 3) {
                            for (int z = 0; z < NN_index.length; z++) {
                                if (k_gn >= 10) {
                                    if (min_emp > a_array[NN_index[z]]) {
                                        min_emp = a_array[NN_index[z]];
                                        index = NN_index[z];
                                    }
                                } else {
                                    if (max_emp < a_array[NN_index[z]]) {
                                        max_emp = a_array[NN_index[z]];
                                        index = NN_index[z];
                                    }
                                }
                            }
                        }

                        break;

                    case 2:
                        // 选择离最重箱层高尽可能近的，H选空箱数量小的，L选空箱数量大的，M选离最离最重箱尽可能近的
                        if (gn.length == 2) {

                            if (k_gn == 10) {
                                for (int z = 0; z < NN_index.length; z++) {
                                    if (min_emp > a_array[NN_index[z]]) {
                                        min_emp = a_array[NN_index[z]];
                                        index = NN_index[z];
                                    }
                                }
                            } else {
                                for (int z = 0; z < NN_index.length; z++) {
                                    if (max_emp < a_array[NN_index[z]]) {
                                        max_emp = a_array[NN_index[z]];
                                        index = NN_index[z];
                                    }
                                }
                            }
                        }
                        if (gn.length == 3) {
                            if (k_gn == 100) {
                                for (int z = 0; z < NN_index.length; z++) {
                                    if (min_emp > a_array[NN_index[z]]) {
                                        min_emp = a_array[NN_index[z]];
                                        index = NN_index[z];
                                    }
                                }
                            } else if (k_gn == 10) {
                                index = NN_index[0];
                                for (int z = 0; z < NN_index.length; z++) {
                                    if (min_gap > (t - a_array[NN_index[z]] + 1 - tier_b[NN_index[z]])) {
                                        min_gap = t - a_array[NN_index[z]] + 1 - tier_b[NN_index[z]];
                                        index = NN_index[z];
                                    }
                                }
                            } else {
                                for (int z = 0; z < NN_index.length; z++) {
                                    if (max_emp < a_array[NN_index[z]]) {
                                        max_emp = a_array[NN_index[z]];
                                        index = NN_index[z];
                                    }
                                }
                            }
                        }
                        break;
                }
                break;
        }
//		System.out.println("array_index:"+index);
        a_array[index] = a_array[index] - 1;
        String nc = "";
        String[] sc = new String[sc_pre.length];
        for (int p = 0; p < a_array.length; p++) {
            nc = nc + String.valueOf(a_array[p]);
            sc[p] = sc_pre[p];
        }
        if (k_gn >= gn_b[index]) {
            gn_b[index] = k_gn;
            sc[index] = String.valueOf((t - a_array[index]) * k_gn);
        }
        String[][] result = new String[3][];
//		System.out.println("nc:"+nc);
        result[0] = new String[1];
        result[1] = new String[nc_pre.length()];
        result[2] = new String[1];
        result[0][0] = nc;
        for (int p = 0; p < nc_pre.length(); p++) {
            result[1][p] = sc[p];
        }
        result[2][0] = String.valueOf(index);
        return result;
    }

    public static int FindIndex(int[] nc, String[] sc, String[] gn, int k, int t) {
        // 根据惩罚值计算最好的堆垛
        double belta = 0.2;
        double alpha = 1;
        double gamma = 1;
        double min_value = 1000;
        double[] pun_sc = new double[sc.length];// 记录惩罚值
        for (int i = 0; i < sc.length; i++) {
            pun_sc[i] = 10000;
        }
//		int nc_len=0;//找到空箱数不为0的堆垛的状态
        int index = -1;
        /*
         * for (int i =0;i<nc.length;i++) { if (nc[i]!=0) nc_len++; }
         */
        int[] result = new int[gn.length - 1];
        int maxWeight = -1;
        for (int i = 0; i < nc.length; i++) {
            if (nc[i] != 0) {
                if (gn.length == 2) {
                    result[0] = Integer.parseInt(sc[i]) / 10;
                    if (result[0] != 0) {// 鏈塎
                        maxWeight = 0;
                    } else {
                        maxWeight = 1;// 鍏ㄦ槸L(1)鐨勬椂鍊欐垨绌�
                    }
                    if (k == 10) {
                        pun_sc[i] = Math.max(belta * maxWeight * (nc[i] - 1), 0);
                        // pun_sc[i]=0;
                        // rehandle=0;
                    }
                    if (k == 1) {
                        if (maxWeight == 0) {
                            pun_sc[i] = alpha * Math.max((t - nc[i] + 1), 0)
                                    + gamma * Math.max((t - nc[i] + 1 - result[0]), 0);
//						pun_sc[i]=1;
                            // rehandle=alpha*Math.max((t-nc_int+1), 0);
                            // rehandle=1;
                        } else {
                            pun_sc[i] = 0;
                        }
                    }
                }

                if (gn.length == 3) {
                    result[0] = Integer.parseInt(String.valueOf(sc[i])) / 100;// 鏈夋病鏈塇
                    result[1] = (Integer.parseInt(String.valueOf(sc[i])) / 10) % 10;// 鏈夋病鏈塎
                    if (result[0] != 0) {// 鏈塇
                        maxWeight = 0;
                    } else if (result[1] != 0) {// 鏈塎
                        maxWeight = 1;
                    } else {
                        maxWeight = 2;
                    }
                    if (k == 100) {
                        pun_sc[i] = Math.max(belta * maxWeight * (nc[i] - 1), 0);
                        // pun_sc[i]=0;
                        // rehandle=0;
                    }
                    if (k == 10) {
                        if (maxWeight == 0) {// 鏈塇
                            pun_sc[i] = alpha * Math.max((t - nc[i] + 1), 0)
                                    + gamma * Math.max((t - nc[i] + 1 - result[0]), 0);
//						pun_sc[i]=1;
                            // rehandle=alpha*Math.max((t-nc_int+1), 0);
                            // rehandle=1;
                        }
                        if (maxWeight == 1) {
                            pun_sc[i] = 0;
                        }
                        if (maxWeight == 2) {
                            pun_sc[i] = Math.max(belta * (nc[i] - 1), 0);
//						pun_sc[i]=0;
                            // rehandle=0;
                        }
                    }
                    if (k == 1) {
                        if (maxWeight == 2) {
                            pun_sc[i] = 0;
                        } else if (maxWeight == 1) {
                            pun_sc[i] = Math.max((t - nc[i] + 1), 0) + gamma * Math.max((t - nc[i] + 1 - result[1]), 0);
//						pun_sc[i]=1;
                            // rehandle=alpha*Math.max((t-nc_int+1), 0);
                            // rehandle=1;
                        } else {
                            pun_sc[i] = Math.max((t - nc[i] + 1), 0) + gamma * Math.max((t - nc[i] + 1 - result[0]), 0);
//						pun_sc[i]=1;
                            // rehandle=alpha*Math.max((t-nc_int+1), 0);
                            // rehandle=1;
                        }
                    }

                }
            }
        }
        for (int j = 0; j < nc.length; j++) {
            if (nc[j] != 0) {
                if (min_value >= pun_sc[j]) {
                    min_value = pun_sc[j];
                    index = j;
                }
            }
        }

        return index;

    }

}
