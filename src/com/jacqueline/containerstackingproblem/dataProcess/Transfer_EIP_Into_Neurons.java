package com.jacqueline.containerstackingproblem.dataProcess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Transfer_EIP_Into_Neurons {
    public int[][] data; // 输入神经元
    public int[][] target; // 输出神经元
    String config = "/Users/jacqueline/Code/ContainerStacking/data/G=";

    // copy from BPNN_Prepare3
    public void transferIntoBPNNNeurons(String name, int[] gn, int s, int t, boolean write) {
        Input in = new Input();
        String[][] dataarray = in.readdata(name);
        data = new int[dataarray.length * gn.length][gn.length * 4 + 1]; // 有三种可能到达的集装箱类型（先H再M再L） * 输入神经元编码下的状态
        target = new int[dataarray.length * gn.length][gn.length * 2 + 1];
        for (int x = 0; x < data.length; x++) {
            for (int y = 0; y < data[x].length; y++) {
                data[x][y] = 0;
            }
        }
        for (int x = 0; x < target.length; x++) {
            for (int y = 0; y < target[x].length; y++) {
                target[x][y] = 0;
            }
        }
        for (int i = 0; i < dataarray.length; i++) {
            // 循环到达集装箱类型H，M，L
            for (int m = 0; m < gn.length; m++) {
                // 定义输入前7个 堆垛状态描述神经元
                if (Integer.parseInt(String.valueOf((dataarray[i][2 + dataarray[i][0].length() + m * (4 + dataarray[i][0].length())].charAt(0)))) != 0) { // 若存在该类型，即数量没有超
                    int count = 0;
                    for (int j = 0; j < dataarray[i][0].length(); j++) {
                        if (dataarray[i][0].charAt(j) == '0')
                            break;
                        else
                            count++; // 没堆满的堆垛个数
                    }
                    for (int j = 0; j < count; j++) {
                        int emp = Integer.parseInt(String.valueOf(dataarray[i][0].charAt(j))); // 缺的集装箱数量
                        int tier = (int) dataarray[i][j + 1].charAt(0) - (int) ('0'); // 最重箱所在层高
                        if (emp == t) {
                            data[i + m * dataarray.length][gn.length * 2] = 1;
                        } else {
                            if (gn.length == 2) {
                                if (t - emp == tier) { // 先判断最重箱与top的差距
                                    if (Integer.parseInt(String.valueOf(dataarray[i][j + 1])) >= 10) { // 再判断最重箱类型
                                        data[i + m * dataarray.length][0] = 1;
                                    } else {
                                        data[i + m * dataarray.length][2] = 1;
                                    }
                                } else {
                                    if (Integer.parseInt(String.valueOf(dataarray[i][j + 1])) >= 10) {
                                        data[i + m * dataarray.length][1] = 1;
                                    } else {
                                        data[i + m * dataarray.length][3] = 1;
                                    }
                                }
                            }

                        }
                        if (gn.length == 3) {
                            if (t - emp == tier) {
                                if (Integer.parseInt(String.valueOf(dataarray[i][j + 1])) >= 100) {
                                    data[i + m * dataarray.length][0] = 1;
                                } else if (Integer.parseInt(String.valueOf(dataarray[i][j + 1])) >= 10) {
                                    data[i + m * dataarray.length][2] = 1;
                                } else {
                                    data[i + m * dataarray.length][4] = 1;
                                }
                            } else {
                                if (Integer.parseInt(String.valueOf(dataarray[i][j + 1])) >= 100) {
                                    data[i + m * dataarray.length][1] = 1;
                                } else if (Integer.parseInt(String.valueOf(dataarray[i][j + 1])) >= 10) {
                                    data[i + m * dataarray.length][3] = 1;
                                } else {
                                    data[i + m * dataarray.length][5] = 1;
                                }

                            }
                        }
                    }
                    // 定义输入8-10 到达集装箱概率
                    for (int mm = 0; mm < gn.length; mm++) {
                        if (Integer.parseInt(String.valueOf((dataarray[i][2 + dataarray[i][0].length() + mm * (4 + dataarray[i][0].length())].charAt(0)))) != 0) // EIP输出结果若该达到类型集装箱处不为0则说明达到概率大于0
                            data[i + m * dataarray.length][gn.length * 2 + 1 + mm] = 1;
                    }
                    // 定义输入11-13 当前到达集装箱类型
                    data[i + m * dataarray.length][gn.length * 3 + 1 + m] = 1;
                    // 定义输出神经元
                    String[] res1 = new String[dataarray[i][0].length()];
                    String[] res2 = new String[dataarray[i][0].length()];
                    for (int n = 0; n < dataarray[i][0].length(); n++) {
                        res1[n] = dataarray[i][n + 1];
                        res2[n] = dataarray[i][n + 2 + dataarray[i][0].length() + m * (4 + dataarray[i][0].length()) + 2];
                    }
                    String tier = dataarray[i][2 + dataarray[i][0].length() + m * (4 + dataarray[i][0].length()) + 3 + dataarray[i][0].length()]; // 放置堆垛位置
                    int stackNo = stateSC(dataarray[i][0], res1, res2, tier);
                    int emp = Integer.parseInt(String.valueOf(dataarray[i][0].charAt(stackNo)));
                    int ti = (int) dataarray[i][stackNo + 1].charAt(0) - (int) ('0'); // 最重箱在第几层
                    if (emp == t) {
                        target[i + m * dataarray.length][gn.length * 2] = 1;
                    } else {
                        if (gn.length == 2) {
                            if (t - emp == ti) {
                                if (Integer.parseInt(String.valueOf(dataarray[i][stackNo + 1])) >= 10) {
                                    target[i + m * dataarray.length][0] = 1;
                                } else {
                                    target[i + m * dataarray.length][2] = 1;
                                }
                            } else {
                                if (Integer.parseInt(String.valueOf(dataarray[i][stackNo + 1])) >= 10) {
                                    target[i + m * dataarray.length][1] = 1;
                                } else {
                                    target[i + m * dataarray.length][3] = 1;
                                }

                            }

                        }
                        if (gn.length == 3) {
                            if (t - emp == ti) {
                                if (Integer.parseInt(String.valueOf(dataarray[i][stackNo + 1])) >= 100) {
                                    target[i + m * dataarray.length][0] = 1;
                                } else if (Integer.parseInt(String.valueOf(dataarray[i][stackNo + 1])) >= 10) {
                                    target[i + m * dataarray.length][2] = 1;
                                } else {
                                    target[i + m * dataarray.length][4] = 1;
                                }
                            } else {
                                if (Integer.parseInt(String.valueOf(dataarray[i][stackNo + 1])) >= 100) {
                                    target[i + m * dataarray.length][1] = 1;
                                } else if (Integer.parseInt(String.valueOf(dataarray[i][stackNo + 1])) >= 10) {
                                    target[i + m * dataarray.length][3] = 1;
                                } else {
                                    target[i + m * dataarray.length][5] = 1;
                                }
                            }
                        }

                    }
                }
            }
        }
        if (write) {
            try {
                File writeName = new File(config + gn.length + "_NUMBER/" + s + t + ".txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
                writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
                try (FileWriter writer = new FileWriter(writeName);
                     BufferedWriter out = new BufferedWriter(writer)
                ) {
                    if (gn.length == 2) {
                        out.write("M=1\tM>1\tL=1\tL>1\tempty\tp_M\tp_L\ta_M\ta_L\ttype\n");
                    } else {
                        out.write("H=1\tH>1\tM=1\tM>1\tL=1\tL>1\tempty\tp_H\tp_M\tp_L\ta_H\ta_M\ta_L\ttype\n");
                    }
                    for (int i = 0; i < data.length; i++) {
                        for (int j = 0; j < data[i].length; j++) {
                            out.write(data[i][j] + "\t");
                        }
                        for (int j = 0; j < target[i].length; j++) {
                            if (target[i][j] == 1) {
                                out.write((j + 1) + "\t");
                            }
                        }
                        out.write("\n");
                    }
                    out.flush(); // 把缓存区内容压入文件
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static int stateSC(String str, String[] res1, String[] res2, String s) {
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            if (String.valueOf(str.charAt(i)).equals(s)) {
                len++;
            }
        }

//        System.out.println("Str:" + str);
//        System.out.println("res1:");
//        for (int k = 0; k < res1.length; k++) {
//            System.out.print(res1[k]);
//            System.out.print("\t");
//        }
//        System.out.println("res2:");
//        for (int k = 0; k < res2.length; k++) {
//            System.out.print(res2[k]);
//            System.out.print("\t");
//        }
        int[] getIndexs = new int[len];
//        System.out.println("len:" + len);
        int j = 0;
        for (int i = 0; i < str.length(); i++) {
            if (String.valueOf(str.charAt(i)).equals(s)) {
                getIndexs[j] = i;//���õ�stack�ı��
                //			System.out.println("getIndexs["+j+"]:"+getIndexs[j]);
                j++;
            }
        }
        int markNo = 0;
        if (len == 1) {
//            System.out.println("get[markNo]:" + getIndexs[markNo]);
            return getIndexs[markNo];
        } else {
            for (markNo = 0; markNo < getIndexs.length; markNo++) {
                if (!res1[getIndexs[markNo]].equals(res2[getIndexs[markNo]])) {

                    break;
                }
            }
            if (markNo == getIndexs.length) {
                markNo = getIndexs.length - 1;
            }
//		    System.out.println("markNo:"+markNo);
//            System.out.println("get[markNo]:" + getIndexs[markNo]);
            return getIndexs[markNo];
        }

    }

    public void transferIntoBPNNtNeurons(String name, int[] gn, int t) {
    }

    public static void main(String[] args) throws IOException {

//        int[][] threeGroupList = {{4, 3}, {4, 4}, {4, 5}, {5, 3}, {5, 4}, {5, 5}, {6, 3}, {6, 4}, {7, 3}, {7, 4}};
//        int[][] threeGroupList = {{6, 5}};
//        for (int i = 0; i < threeGroupList.length; i++) {
//            int s = threeGroupList[i][0];
//            int t = threeGroupList[i][1];
//            int[] gn = {100, 10, 1};  //  int[] gn = {10, 1};
//            String path = "/Users/jacqueline/Desktop/Tsing/Research/ContainerAllocation/毕业论文整理 3/storage/data/G=3_new2019/test/test" + s + t + ".txt";
//            Transfer_EIP_Into_Neurons transferr = new Transfer_EIP_Into_Neurons();
//            transferr.transferIntoBPNNNeurons(path, gn, s, t, true);
//        }
//        int[][] twoGroupList = {{4, 3}, {4, 4}, {4, 5}, {5, 3}, {5, 4}, {5, 5}, {6, 3}, {6, 4}, {7, 3}, {7, 4}};
        int[][] twoGroupList = {{7, 5}};
        for (int i = 0; i < twoGroupList.length; i++) {
            int s = twoGroupList[i][0];
            int t = twoGroupList[i][1];
            int[] gn = {10, 1};  //  int[] gn = {10, 1};
            String path = "/Users/jacqueline/Code/ContainerStacking/data/dynamic result/EIP_" + s + t + "_" + gn.length + ".txt";
            Transfer_EIP_Into_Neurons transferr = new Transfer_EIP_Into_Neurons();
            transferr.transferIntoBPNNNeurons(path, gn, s, t, true);
        }
    }
}