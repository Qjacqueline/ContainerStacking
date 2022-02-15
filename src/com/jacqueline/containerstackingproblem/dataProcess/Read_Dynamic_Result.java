//  Queue generate and read
// 读store_value_st_new2.txt文件 st_43，44，45，53，54，55，63，64，65，73，74，75
// 数据内容：当前state 当前value + 若到达箱为100 下一状态value+所在层高 + 若到达箱为10 下一状态value+所在层高 + 若到达箱为1 下一状态value+所在层高
package com.jacqueline.containerstackingproblem.dataProcess;

public class Read_Dynamic_Result {
    public String[][] current_state;
    public double[] current_value;
    public String[][] next_state;
    public String[][] next_weight_group;
    public double[][] next_value;
    public int[][] tier_no;
    public int size;
    public String[][] dataarray;

    public void datainput(String filename, int s, String[] gn) {
        Input input = new Input();
        dataarray = input.readdata(filename);

        size = dataarray.length;
        current_state = new String[dataarray.length][1 + s];
        current_value = new double[dataarray.length];
        next_state = new String[dataarray.length][(1 + s) * gn.length];
        next_weight_group = new String[dataarray.length][gn.length];
        next_value = new double[dataarray.length][gn.length];
        tier_no = new int[dataarray.length][gn.length];

        for (int i = 0; i < dataarray.length; i++) {
            current_state[i][0] = dataarray[i][0];
            for (int j = 0; j < s; j++)
                current_state[i][j + 1] = dataarray[i][j + 1];
            current_value[i] = Double.valueOf(dataarray[i][1 + s]);
            for (int k = 0; k < gn.length; k++) {
                next_weight_group[i][k] = dataarray[i][(1 + 1 + s + 1 + 1) * k + (2 + s)];
                next_state[i][(s + 1) * k + 0] = dataarray[i][(1 + 1 + s + 1 + 1) * k + (3 + s)];
                for (int j = 0; j < s; j++)
                    next_state[i][(s + 1) * k + j + 1] = dataarray[i][(1 + 1 + s + 1 + 1) * k + (4 + s) + j];
                next_value[i][k] = Double.valueOf(dataarray[i][(1 + 1 + s + 1 + 1) * k + (5 + s) + s - 1]);
                tier_no[i][k] = Integer.valueOf(dataarray[i][(1 + 1 + s + 1 + 1) * k + (6 + s) + s - 1]);
            }
        }
    }

}
