//读取BPNN训练好的数据，测试模型效果
//要改line73输入神经网络参数文件+前置s,t参数+layer设置（BPNN/BPNNt）
package com.jacqueline.containerstackingproblem;

import com.jacqueline.containerstackingproblem.algorithm.BPNNAlgorithm;
import com.jacqueline.containerstackingproblem.evaluatingResult.Evaluating_BPNN2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//{t=3,G=2}    {7, 10, 5}  |3，3，2| |4，3，2| |5，3，2| ->|6，3，2| |7，3，2|
//{t=4,G=2}    {9, 12, 7}  |3，4，2| |4，4，2| |5，4，2| ->|6，4，2| |7，4，2|
//{t=5,G=2}    {11, 20, 9} |3，5，2| |4，5，2| |5，5，2| ->|6，5，2| |7，5，2|
//{t=3,G=3}    {10, 18, 7} |3，3，3| |4，3，3| |5，3，3| ->|6，3，3| |7，3，3|
//{t=4,G=3}    {13, 23, 10}  |3，4，3| |4，4，3|  ->|6，4，3| |7，4，3|
//{t=5,G=3}    {16, 25, 13}  |3，5，3| |4，5，3|  ->|6，5，3| |7，5，3|

public class BPNN_main_apply {
    public static void main(String[] args) throws IOException {
        int s = 4;
        int t = 3;
        int t2 = 4;

//        int[] gn = { 10, 1 };
//        String[] gn_string = { "10", "1" };
//        double[] ratio = { 0.5, 0.5 };
        int[] gn = {100, 10, 1};
        String[] gn_string = {"100", "10", "1"};
        double[] ratio = {0.33, 0.33, 0.33};

        int mid = 18;
        double rate = 0.05;
        double mobp = 0.85;
        double[][][] layer_weight = new double[3][][];

        //BPNN
        int[] layernum = new int[]{gn.length * 4 + 1, mid, gn.length * 2 + 1};
        layer_weight[0] = new double[gn.length * 4 + 2][mid];
        layer_weight[1] = new double[mid + 1][gn.length * 2 + 1];
        //BPNNt
//		 int[] layernum=new int[] {gn.length*(t+1)+1,mid,gn.length*(t-1)+1};
//		 layer_weight[0] = new double[gn.length*(t+1)+2][mid];
//		 layer_weight[1] = new double[mid+1][gn.length*(t)+1];


        // 读取已经训练好的网络参数
        int i = 0;
        int j = 0;
        int m = 0;
        int k = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(//"/Users/jacqueline/Desktop/Tsing/Research/ContainerAllocation/毕业论文整理 3/storage/data/ceng_s"+String.valueOf(s)+"_g" + String.valueOf(gn.length) + ".txt"));
                    "/Users/jacqueline/Desktop/Tsing/Research/ContainerAllocation/毕业论文整理 3/storage/data/NN/ceng_s"+String.valueOf(s)+"_g" + String.valueOf(gn.length) + "_new.txt")); // ceng几对应s=几 G=2
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

        for (int k1 = 0; k1 < layer_weight.length - 1; k1++) {
            for (int k2 = 0; k2 < layer_weight[k1].length; k2++) {
                for (int k3 = 0; k3 < layer_weight[k1][k2].length; k3++) {
                    System.out.println("k1:" + k1 + "  k2:" + k2 + "  k3:" + k3);
                    System.out.println(layer_weight[k1][k2][k3]);
                }
            }
        }

        Evaluating_BPNN2 evaluating_BPNN = new Evaluating_BPNN2(layernum, rate, mobp);
        // evaluating_BPNN.train_Modle(layernum, rate, mobp, gn, t, s);
        BPNNAlgorithm bpnnAlgorithm = evaluating_BPNN.bpnnAlgorithm;
        bpnnAlgorithm.layer_weight = layer_weight;

        evaluating_BPNN.evaluating_BPNN_function(s, t, gn_string, ratio, bpnnAlgorithm);

    }

}
