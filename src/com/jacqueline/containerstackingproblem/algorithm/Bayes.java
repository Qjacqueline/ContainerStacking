package com.jacqueline.containerstackingproblem.algorithm;

import com.jacqueline.containerstackingproblem.config.MyProperties;

import java.io.File;
import java.util.*;


public class Bayes {

    private static String root_gn2 = MyProperties.Neuron_root_gn2;
    private static String root_gn3 = MyProperties.Neuron_root_gn3;
    public static Vector<String> trainData = new Vector<>(); //读入数据
    public static Vector<String> testData = new Vector<>(); //读入数据

    public static int gn_num; //gn种类
    public static int input_len; //输入数据长度
    public static int category_num; //总类别数

    static int[] category_1_element_count; //存储类别1的统计数据
    static int[] category_2_element_count; //存储类别2的统计数据
    static int[] category_3_element_count; //存储类别3的统计数据
    static int[] category_4_element_count; //存储类别4的统计数据
    static int[] category_5_element_count; //存储类别5的统计数据
    static int[] category_6_element_count; //存储类别6的统计数据
    static int[] category_7_element_count; //存储类别7的统计数据
    static double p_y1;
    static double p_y2;
    static double p_y3;
    static double p_y4;
    static double p_y5;
    static double p_y6;
    static double p_y7;

    public static int[] each_category_num_predict; //每个类别的预测结果数量
    public static int[] each_category_num_actual; //每个类别的实际结果数量
    public static double correct; // testData中正确预测的数据
    public static int zeroNum; // testData中的无效数据

    public Bayes(int gn_numm) {
        gn_num = gn_numm;
        input_len = gn_num == 2 ? 10 : 14;
        category_num = gn_num == 2 ? 5 : 7;
    }

    public static boolean loadTrainData(String url) {//加载测试的数据文件
        try {
            Scanner in = new Scanner(new File(url));//读入文件
            in.nextLine();
            while (in.hasNextLine()) {
                String str = in.nextLine();//将文件的每一行存到str的临时变量中
                trainData.add(str);//将每一个样本点的数据追加到Vector 中
            }
            return true;
        } catch (Exception e) { //如果出错返回false
            return false;
        }
    }

    public static boolean loadTestData(String url) {//加载测试的数据文件
        try {
            Scanner in = new Scanner(new File(url));//读入文件
            in.nextLine();
            while (in.hasNextLine()) {
                String str = in.nextLine();//将文件的每一行存到str的临时变量中
                testData.add(str);//将每一个样本点的数据追加到Vector 中
            }
            return true;
        } catch (Exception e) { //如果出错返回false
            return false;
        }
    }

    public static void pretreatment(Vector<String> indata) {   //数据预处理，将原始数据中的每一个属性值提取出来存放到Vector<double[]>  data中
        int i = 0;
        String t;

        category_1_element_count = new int[input_len];
        category_2_element_count = new int[input_len];
        category_3_element_count = new int[input_len];
        category_4_element_count = new int[input_len];
        category_5_element_count = new int[input_len];
        category_6_element_count = new int[input_len];
        category_7_element_count = new int[input_len];

        while (i < indata.size()) { //取出indata中的每一行值
            t = indata.get(i);
            String[] sourceStrArray = t.split("\t", input_len + 1);//使用字符串分割函数提取出各属性值
            switch (sourceStrArray[input_len - 1]) {
                case "1": {
                    for (int j = 0; j < input_len - 1; j++) {
                        if (sourceStrArray[j].equals("1")) {
                            category_1_element_count[j]++;
                        }
                    }
                    category_1_element_count[input_len - 1]++;
                    break;
                }
                case "2": {
                    for (int j = 0; j < input_len - 1; j++) {
                        if (sourceStrArray[j].equals("1")) {
                            category_2_element_count[j]++;
                        }
                    }
                    category_2_element_count[input_len - 1]++;
                    break;
                }
                case "3": {
                    for (int j = 0; j < input_len - 1; j++) {
                        if (sourceStrArray[j].equals("1")) {
                            category_3_element_count[j]++;
                        }
                    }
                    category_3_element_count[input_len - 1]++;
                    break;
                }
                case "4": {
                    for (int j = 0; j < input_len - 1; j++) {
                        if (sourceStrArray[j].equals("1")) {
                            category_4_element_count[j]++;
                        }
                    }
                    category_4_element_count[input_len - 1]++;
                    break;
                }
                case "5": {
                    for (int j = 0; j < input_len - 1; j++) {
                        if (sourceStrArray[j].equals("1")) {
                            category_5_element_count[j]++;
                        }
                    }
                    category_5_element_count[input_len - 1]++;
                    break;
                }
                case "6": {
                    for (int j = 0; j < input_len - 1; j++) {
                        if (sourceStrArray[j].equals("1")) {
                            category_6_element_count[j]++;
                        }
                    }
                    category_6_element_count[input_len - 1]++;
                    break;
                }
                case "7": {
                    for (int j = 0; j < input_len - 1; j++) {
                        if (sourceStrArray[j].equals("1")) {
                            category_7_element_count[j]++;
                        }
                    }
                    category_7_element_count[input_len - 1]++;
                    break;
                }
            }
            i++;
        }
        if (gn_num == 2) {
            p_y1 = (double) category_1_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（1）
            p_y2 = (double) category_2_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（2）
            p_y3 = (double) category_3_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（3）
            p_y4 = (double) category_4_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（4）
            p_y5 = (double) category_5_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（5）
        } else {
            p_y1 = (double) category_1_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（1）
            p_y2 = (double) category_2_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（2）
            p_y3 = (double) category_3_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（3）
            p_y4 = (double) category_4_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（4）
            p_y5 = (double) category_5_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（5）
            p_y6 = (double) category_6_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（6）
            p_y7 = (double) category_7_element_count[input_len - 1] / (double) (trainData.size());//表示概率p（7）
        }

    }

    public static double bayes_cal(int[] x, int[] category) {
        double[] ai_y = new double[input_len - 1];
        int[] sum_ai = new int[input_len - 1];
        for (int i = 0; i < input_len - 1; i++) {
            if (x[i] == 1) {
                sum_ai[i] += category[i];
            } else {
                sum_ai[i] += category[input_len - 1] - category[i];
            }

        }
        for (int i = 0; i < input_len - 1; i++) {
            ai_y[i] = (double) sum_ai[i] / (double) category[input_len - 1];
        }

        return multiply(ai_y);
    }

    public static double multiply(double[] factors) {
        double result = 1.0;
        for (double f : factors)
            result *= f;
        return result;
    }

    public static int process_one_data(int[] test_input) {
        List<Double> x_in;
        x_in = new ArrayList<>();
        if (gn_num == 2) {
            x_in.add(bayes_cal(test_input, category_1_element_count) * p_y1);
            x_in.add(bayes_cal(test_input, category_2_element_count) * p_y2);
            x_in.add(bayes_cal(test_input, category_3_element_count) * p_y3);
            x_in.add(0.0);
            x_in.add(bayes_cal(test_input, category_5_element_count) * p_y5);
        } else {
            x_in.add(bayes_cal(test_input, category_1_element_count) * p_y1);
            x_in.add(bayes_cal(test_input, category_2_element_count) * p_y2);
            x_in.add(bayes_cal(test_input, category_3_element_count) * p_y3);
            x_in.add(bayes_cal(test_input, category_4_element_count) * p_y4);
            x_in.add(bayes_cal(test_input, category_5_element_count) * p_y5);
            x_in.add(0.0);
            x_in.add(bayes_cal(test_input, category_7_element_count) * p_y7);
        }
        int outIndex = x_in.indexOf(Collections.max(x_in));
        return outIndex;
    }

    public static void readData() {
        if (gn_num == 2) {
            loadTrainData(root_gn2 + "43.txt");
            loadTrainData(root_gn2 + "44.txt");
//            loadTestData(root_gn2 + "43.txt");
//            loadTestData(root_gn2 + "44.txt");
//            loadTestData(root_gn2 + "45.txt");
//            loadTestData(root_gn2 + "53.txt");
//            loadTestData(root_gn2 + "55.txt");
//            loadTestData(root_gn2 + "63.txt");
//            loadTestData(root_gn2 + "64.txt");
//            loadTestData(root_gn2 + "65.txt");
//            loadTestData(root_gn2 + "73.txt");
//            loadTestData(root_gn2 + "74.txt");
        } else {
            loadTrainData(root_gn3 + "43.txt");
            loadTrainData(root_gn3 + "44.txt");
//            loadTestData(root_gn3 + "43.txt");
//            loadTestData(root_gn3 + "44.txt");
//            loadTestData(root_gn3 + "45.txt");
//            loadTestData(root_gn3 + "53.txt");
//            loadTestData(root_gn3 + "54.txt");
//            loadTestData(root_gn3 + "55.txt");
//            loadTestData(root_gn3 + "63.txt");
//            loadTestData(root_gn3 + "64.txt");
//            loadTestData(root_gn3 + "73.txt");
        }
    }

    public static void calculateResult() {
        each_category_num_predict = new int[category_num];
        each_category_num_actual = new int[category_num];
        correct = 0;
        zeroNum = 0;
        for (int r = 0; r < testData.size(); r++) {
            String[] sourceStrArray = testData.get(r).split("\t", input_len + 1);//使用字符串分割函数提取出各属性值
            int[] test_input = new int[input_len - 1]; // 用于记录test data的信息
            for (int i = 0; i < input_len - 1; i++) {
                test_input[i] = Integer.parseInt(sourceStrArray[i]); //读取数字放入数组的第i个元素
            }
            if (sourceStrArray[input_len - 1].equals("")) {
                zeroNum++;
            } else {
                int output_type = Integer.valueOf(sourceStrArray[input_len - 1]);
                each_category_num_actual[output_type - 1]++;

                int outIndex = process_one_data(test_input);
                each_category_num_predict[outIndex]++;

                correct = output_type == outIndex + 1 ? correct + 1 : correct;
            }
        }
    }

    public static void printResult() {
        if (gn_num == 2) {
            System.out.println("使用训练样本进行分类器检验得到结果统计如下：");
            System.out.println("1类有：" + each_category_num_predict[0] + "    实际有1类样本" + each_category_num_actual[0] + "个");
            System.out.println("2类有：" + each_category_num_predict[1] + "    实际有2类样本" + each_category_num_actual[1] + "个");
            System.out.println("3类有：" + each_category_num_predict[2] + "      实际有3类样本" + each_category_num_actual[2] + "个");
            System.out.println("4类有：" + each_category_num_predict[3] + "    实际有4类样本" + each_category_num_actual[3] + "个");
            System.out.println("5类有：" + each_category_num_predict[4] + "    实际有5类样本" + each_category_num_actual[4] + "个");
            System.out.println("分类的正确率为" + correct * 1.0 / (testData.size() - zeroNum) * 100 + "%");
        } else {
            System.out.println("使用训练样本进行分类器检验得到结果统计如下：");
            System.out.println("1类有：" + each_category_num_predict[0] + "    实际有1类样本" + each_category_num_actual[0] + "个");
            System.out.println("2类有：" + each_category_num_predict[1] + "    实际有2类样本" + each_category_num_actual[1] + "个");
            System.out.println("3类有：" + each_category_num_predict[2] + "      实际有3类样本" + each_category_num_actual[2] + "个");
            System.out.println("4类有：" + each_category_num_predict[3] + "    实际有4类样本" + each_category_num_actual[3] + "个");
            System.out.println("5类有：" + each_category_num_predict[4] + "    实际有5类样本" + each_category_num_actual[4] + "个");
            System.out.println("6类有：" + each_category_num_predict[5] + "    实际有4类样本" + each_category_num_actual[5] + "个");
            System.out.println("7类有：" + each_category_num_predict[6] + "    实际有5类样本" + each_category_num_actual[6] + "个");
            System.out.println("分类的正确率为" + correct * 1.0 / (testData.size() - zeroNum) * 100 + "%");

        }
    }

    public static void main(String[] args) {
        Bayes bayes = new Bayes(3);
        bayes.readData();
        bayes.pretreatment(trainData);
        bayes.calculateResult();
        bayes.printResult();
    }
}

