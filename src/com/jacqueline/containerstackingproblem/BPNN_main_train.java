package com.jacqueline.containerstackingproblem;

import com.jacqueline.containerstackingproblem.evaluatingResult.Evaluating_BPNN2;

import com.jacqueline.containerstackingproblem.evaluatingResult.Evaluating_BPNN2;

import java.io.IOException;

public class BPNN_main_train {

    public static void main(String[] args) throws IOException {
        int s = 4;
        int t = 3;
        int[] gn = {100, 10, 1};

        int mid = 18;
        double rate = 0.05;
        double mobp = 0.85;
        double[][][] layer_weight = new double[3][][];

        //BPNN
        int[] layernum = new int[]{gn.length * 4 + 1, mid, gn.length * 2 + 1};
        layer_weight[0] = new double[gn.length * 4 + 2][mid];
        layer_weight[1] = new double[mid + 1][gn.length * 2 + 1];

        Evaluating_BPNN2 evaluating_BPNN = new Evaluating_BPNN2(layernum, rate, mobp);
        String[] gn_string = {"100", "10", "1"};
        evaluating_BPNN.train_Modle(layernum, rate, mobp, gn, t,s);


    }
}