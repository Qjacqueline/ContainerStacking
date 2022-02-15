package com.jacqueline.containerstackingproblem.algorithm;

import com.jacqueline.containerstackingproblem.config.MyProperties;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

public class Dynamic_Programming_BM {

    int totalStateSize;
    double finalObjectiveValue;
    String[][] followingState;
    String[] arrivingWeightGroup;
    double[] currentRehandleTimes;
    int[] tierNoChosen;
    int[] stateNumberChosen;
    String[] stateWeightChosen;

    /**
     * 给定之前的状态，为当前状态找到最适合的位置，并计算目标值
     *
     * @param t
     * @param nc
     * @param sc
     * @param gn
     * @param ncPrevious
     * @param scPrevious
     * @param vaPrevious
     * @param ratio
     * @return
     */
    double calculateObjectiveValue(int t, String nc, String sc, String[] gn, String[] ncPrevious, String[] scPrevious,
                                   double[] vaPrevious, double[] ratio) {
        followingState = new String[gn.length][2];
        arrivingWeightGroup = new String[gn.length];
        currentRehandleTimes = new double[gn.length];
        tierNoChosen = new int[gn.length];
        stateNumberChosen = new int[gn.length];
        stateWeightChosen = new String[gn.length];

        double[] objectiveValue = new double[gn.length];
        for (int i = 0; i < gn.length; i++)
            objectiveValue[i] = Double.MAX_VALUE;

        // 分每一种重量权重进行讨论
        for (int i = 0; i < gn.length; i++) {
            // 每一个可放置的stack都要试一遍
            for (int j = 0; j < nc.length(); j++) {
                if (nc.charAt(j) > '0') {
                    String ncTemp = nc;
                    String scTemp = sc;
                    int tierNoTemp = Integer.parseInt(String.valueOf(nc.charAt(j)));
                    String stateWeightChosenTemp = String.valueOf(sc.charAt(j));
                    int rehandleTimes = 0;
                    // gn.length represents "*"
                    // 此时代表空位置
                    int positionOfStateWeightChosen = gn.length;
                    for (int ii = 0; ii < gn.length; ii++)
                        if (scTemp.charAt(j) == gn[ii].charAt(0))
                            positionOfStateWeightChosen = ii;

                    // 判断到达的是否更重
                    boolean isArrivalHeavier = false;
                    // i：代表到达的集装箱重量，positionOfStateWeightChosen:代表当前stack上的重量
                    // 数值越小，权重越大
                    if (positionOfStateWeightChosen > i)
                        isArrivalHeavier = true;

                    // the original method -- the optimistic case 1
                    int rehandleTimesOfMethod1 = 0;
                    // 到达的集装箱更重
                    if (positionOfStateWeightChosen >= i)
                        rehandleTimesOfMethod1 = 0;
                        // 到达的集装箱更轻
                    else
                        rehandleTimesOfMethod1 = 1;

                    rehandleTimes = rehandleTimesOfMethod1;

                    // the revised method -- the pessimistic case 2
                    // 如果某stack表示为H时，认为只有一个H，而且位于最底层，其它都是比H来的轻
                    int rehandleTimesOfMethod2 = 0;
                    if (gn.length == 2) {
                        if (i == 1) {
                            if (positionOfStateWeightChosen >= 1) {
                                rehandleTimesOfMethod2 = 0;
                            } else {
                                rehandleTimesOfMethod2 = t - tierNoTemp;
                            }
                        }
                        if (i == 0) {
                            if (positionOfStateWeightChosen >= 1) {
                                rehandleTimesOfMethod2 = 0;
                            } else {
                                rehandleTimesOfMethod2 = t - tierNoTemp - 1;
                            }
                        }
                    }

                    if (gn.length == 3) {
                        if (i == 2) { // 到达的箱子比较轻
                            if (positionOfStateWeightChosen >= 2) {
                                rehandleTimesOfMethod2 = 0;
                            } else {
                                rehandleTimesOfMethod2 = t - tierNoTemp;
                            }
                        }
                        if (i == 1) {
                            if (positionOfStateWeightChosen >= 2) {
                                rehandleTimesOfMethod2 = 0;
                            }
                            if (positionOfStateWeightChosen == 1) {
                                rehandleTimesOfMethod2 = t - tierNoTemp - 1;
                            }
                            if (positionOfStateWeightChosen == 0) {
                                rehandleTimesOfMethod2 = t - tierNoTemp;
                            }
                        }
                        if (i == 0) {
                            if (positionOfStateWeightChosen >= 2) {
                                rehandleTimesOfMethod2 = 0;
                            }
                            if (positionOfStateWeightChosen == 1) {
                                rehandleTimesOfMethod2 = t - tierNoTemp - 1;
                            }
                            if (positionOfStateWeightChosen == 0) {
                                rehandleTimesOfMethod2 = t - tierNoTemp - 1;
                            }
                        }
                    }
                    rehandleTimes = rehandleTimesOfMethod2;

                    // method 3 intermediate case //注意前两个都不能屏蔽，才起效果。
                    int rehandleTimesOfMethod3 = 0;
                    rehandleTimesOfMethod3 = Math.round((rehandleTimesOfMethod1 + rehandleTimesOfMethod2) / 2);
                    rehandleTimes = rehandleTimesOfMethod3;

                    // adjust nc
                    String firstPart = "";
                    String thirdPart = "";
                    String secondPart = "";
                    if (j > 0)
                        firstPart = ncTemp.substring(0, j - 1 - 0 + 1);
                    if (j < nc.length() - 1)
                        thirdPart = ncTemp.substring(j + 1, ncTemp.length() - 1 - (j + 1) + 1 + (j + 1));
                    int leftEmptyNumber = Integer.parseInt(String.valueOf(ncTemp.charAt(j))) - 1;
                    secondPart = String.valueOf(leftEmptyNumber);
                    ncTemp = firstPart + secondPart + thirdPart;

                    // adjust sc
                    String firstPart1 = "";
                    String thirdPart1 = "";
                    String secondPart1 = "";
                    if (leftEmptyNumber == 0) {
                        if (j > 0)
                            firstPart1 = scTemp.substring(0, j - 1 - 0 + 1);
                        if (j < sc.length() - 1)
                            thirdPart1 = scTemp.substring(j + 1, scTemp.length() - 1 - (j + 1) + 1 + (j + 1));
                        secondPart1 = "0";
                        scTemp = firstPart1 + secondPart1 + thirdPart1;
                    } else {
                        if (isArrivalHeavier) { // 代表到达的集装箱更重，此时要更新重量权重
                            if (j > 0)
                                firstPart1 = scTemp.substring(0, j - 1 - 0 + 1);
                            if (j < sc.length() - 1)
                                thirdPart1 = scTemp.substring(j + 1, scTemp.length() - 1 - (j + 1) + 1 + (j + 1));
                            secondPart1 = gn[i];
                            scTemp = firstPart1 + secondPart1 + thirdPart1;
                        }
                    }
                    // re-sequence nc and sc
                    for (int k = 0; k < ncTemp.length() - 1; k++) {
                        int first = Integer.parseInt(String.valueOf(ncTemp.charAt(k)));
                        int second = Integer.parseInt(String.valueOf(ncTemp.charAt(k + 1)));
                        String firstPart2 = "";
                        String fourthPart2 = "";
                        String secondPart2 = "";
                        String thirdPart2 = "";
                        String firstPart3 = "";
                        String fourthPart3 = "";
                        String secondPart3 = "";
                        String thirdPart3 = "";
                        if (first < second) {
                            if (k > 0)
                                firstPart2 = ncTemp.substring(0, k - 1 - 0 + 1);
                            if (k < nc.length() - 2)
                                fourthPart2 = ncTemp.substring(k + 2, ncTemp.length() - 1 - (k + 2) + 1 + (k + 2));
                            secondPart2 = String.valueOf(second);
                            thirdPart2 = String.valueOf(first);
                            ncTemp = firstPart2 + secondPart2 + thirdPart2 + fourthPart2;

                            if (k > 0)
                                firstPart3 = scTemp.substring(0, k - 1 - 0 + 1);
                            if (k < sc.length() - 2)
                                fourthPart3 = scTemp.substring(k + 2, scTemp.length() - 1 - (k + 2) + 1 + (k + 2));
                            secondPart3 = String.valueOf(scTemp.charAt(k + 1));
                            thirdPart3 = String.valueOf(scTemp.charAt(k));
                            scTemp = firstPart3 + secondPart3 + thirdPart3 + fourthPart3;
                        }
                    }
                    // re-sequence sc
                    boolean isRequenceNeeded = true;
                    while (isRequenceNeeded) {
                        isRequenceNeeded = false;
                        for (int k = 0; k < ncTemp.length() - 1; k++) {
                            int first = Integer.parseInt(String.valueOf(ncTemp.charAt(k)));
                            int second = Integer.parseInt(String.valueOf(ncTemp.charAt(k + 1)));
                            int first1 = -1;
                            int second1 = -1;
                            for (int h = 0; h < gn.length; h++) {
                                if (gn[h].equals(String.valueOf(scTemp.charAt(k))))
                                    first1 = h; // first1 和 second1 谁大，就意味着谁轻
                                if (gn[h].equals(String.valueOf(scTemp.charAt(k + 1))))
                                    second1 = h;
                            }

                            String firstPart3 = "";
                            String fourthPart3 = "";
                            String secondPart3 = "";
                            String thirdPart3 = "";
                            if (first == second && first1 > second1) {
                                isRequenceNeeded = true;
                                if (k > 0)
                                    firstPart3 = scTemp.substring(0, k - 1 - 0 + 1);
                                if (k < sc.length() - 2)
                                    fourthPart3 = scTemp.substring(k + 2, scTemp.length() - 1 - (k + 2) + 1 + (k + 2));
                                secondPart3 = String.valueOf(scTemp.charAt(k + 1));
                                thirdPart3 = String.valueOf(scTemp.charAt(k));
                                scTemp = firstPart3 + secondPart3 + thirdPart3 + fourthPart3;
                            }
                        }
                    }

                    // match the states;
                    double objectiveValueOfFollowingState = 0.0;
                    for (int k = 0; k < ncPrevious.length; k++) {
                        if (ncTemp.equals(ncPrevious[k]) && scTemp.equals(scPrevious[k])) {
                            objectiveValueOfFollowingState = vaPrevious[k];
                            break;
                        }
                    }

                    // calculate objective value
                    double objectiveValueTemp = objectiveValueOfFollowingState + rehandleTimes;
                    if (objectiveValue[i] > objectiveValueTemp) {
                        objectiveValue[i] = objectiveValueTemp;
                        followingState[i][0] = ncTemp;
                        followingState[i][1] = scTemp;
                        arrivingWeightGroup[i] = gn[i];
                        currentRehandleTimes[i] = rehandleTimes;
                        // tier NO是从上到下有1增长到t
                        tierNoChosen[i] = tierNoTemp;
                        stateNumberChosen[i] = tierNoTemp;
                        stateWeightChosen[i] = stateWeightChosenTemp;
                    }
                }
            }
        }
        double totalObjectiveValue = 0.0;
        for (int i = 0; i < objectiveValue.length; i++)
            totalObjectiveValue = totalObjectiveValue + objectiveValue[i] * ratio[i];
        return totalObjectiveValue;
    }

    int calculateTotalWeightPermutationSize(int s, int t, int n, String[] gn, String[] result) {
        int totalNumber = 0;
        for (int i = 0; i < result.length; i++) {
            totalNumber = totalNumber + calculateWeightPermutationSize(result[i], t, gn);
        }
        return totalNumber;
    }

    /**
     * 列出空箱数量等于n的所有空箱排列组合 第一步，先计算总的维度 第二步，给每一个单元赋值
     *
     * @param s
     * @param t
     * @param n
     * @return
     */
    String[] calculateEmptyPermutation(int s, int t, int n) {
        int total_number = 0;
        if (s == 2) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--) {
                    if (i + ii == n)
                        total_number++;
                }
        }
        if (s == 3) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--) {
                        if (i + ii + j == n)
                            total_number++;
                    }
        }
        if (s == 4) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--) {
                            if (i + ii + j + jj == n)
                                total_number++;
                        }
        }
        if (s == 5) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--)
                            for (int k = jj; k >= 0; k--) {
                                if (i + ii + j + jj + k == n)
                                    total_number++;
                            }
        }
        if (s == 6) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--)
                            for (int k = jj; k >= 0; k--)
                                for (int kk = k; kk >= 0; kk--) {
                                    if (i + ii + j + jj + k + kk == n)
                                        total_number++;
                                }
        }
        if (s == 7) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--)
                            for (int k = jj; k >= 0; k--)
                                for (int kk = k; kk >= 0; kk--)
                                    for (int h = kk; h >= 0; h--) {
                                        if (i + ii + j + jj + k + kk + h == n)
                                            total_number++;
                                    }
        }
        if (s == 8) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--)
                            for (int k = jj; k >= 0; k--)
                                for (int kk = k; kk >= 0; kk--)
                                    for (int h = kk; h >= 0; h--)
                                        for (int hh = h; hh >= 0; hh--) {
                                            if (i + ii + j + jj + k + kk + h + hh == n)
                                                total_number++;
                                        }
        }

        String[] result = new String[total_number];
        int index = 0;

        if (s == 2) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    if (i + ii == n) {
                        result[index] = String.valueOf(i) + String.valueOf(ii);
                        index++;
                    }
        }
        if (s == 3) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--) {
                        if (i + ii + j == n) {
                            result[index] = String.valueOf(i) + String.valueOf(ii) + String.valueOf(j);
                            index++;
                        }
                    }
        }
        if (s == 4) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--) {
                            if (i + ii + j + jj == n) {
                                result[index] = String.valueOf(i) + String.valueOf(ii) + String.valueOf(j)
                                        + String.valueOf(jj);
                                index++;
                            }
                        }
        }
        if (s == 5) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--)
                            for (int k = jj; k >= 0; k--) {
                                if (i + ii + j + jj + k == n) {
                                    result[index] = String.valueOf(i) + String.valueOf(ii) + String.valueOf(j)
                                            + String.valueOf(jj) + String.valueOf(k);
                                    index++;
                                }
                            }
        }
        if (s == 6) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--)
                            for (int k = jj; k >= 0; k--)
                                for (int kk = k; kk >= 0; kk--) {
                                    if (i + ii + j + jj + k + kk == n) {
                                        result[index] = String.valueOf(i) + String.valueOf(ii) + String.valueOf(j)
                                                + String.valueOf(jj) + String.valueOf(k) + String.valueOf(kk);
                                        index++;
                                    }
                                }
        }
        if (s == 7) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--)
                            for (int k = jj; k >= 0; k--)
                                for (int kk = k; kk >= 0; kk--)
                                    for (int h = kk; h >= 0; h--) {
                                        if (i + ii + j + jj + k + kk + h == n) {
                                            result[index] = String.valueOf(i) + String.valueOf(ii) + String.valueOf(j)
                                                    + String.valueOf(jj) + String.valueOf(k) + String.valueOf(kk)
                                                    + String.valueOf(h);
                                            index++;
                                        }
                                    }
        }
        if (s == 8) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--)
                            for (int k = jj; k >= 0; k--)
                                for (int kk = k; kk >= 0; kk--)
                                    for (int h = kk; h >= 0; h--)
                                        for (int hh = h; hh >= 0; hh--) {
                                            if (i + ii + j + jj + k + kk + h + hh == n) {
                                                result[index] = String.valueOf(i) + String.valueOf(ii)
                                                        + String.valueOf(j) + String.valueOf(jj) + String.valueOf(k)
                                                        + String.valueOf(kk) + String.valueOf(h) + String.valueOf(hh);
                                                index++;
                                            }
                                        }
        }

        for (int i = 0; i < total_number; i++)
            System.out.println(result[i]);
        return result;
    }

    /**
     * 计算给定一个空想排列，计算对应的重量排列个数 当该垛一个集装箱都没有，此时的重量状态为*
     * 当该垛有集装箱但至少有一个位置，此时的重量状态size为gn的size 重量状态也按照由重往轻进行排序
     *
     * @param nc
     * @param t
     * @param gn
     * @return
     */
    int calculateWeightPermutationSize(String nc, int t, String[] gn) {
        int total_column = 0; // calculate the number of stacks on which there
        // are empty slots
        for (int i = 0; i < nc.length(); i++) {
            // System.out.println(nc.charAt(i));
            if (nc.charAt(i) > '0')
                total_column++;
        }
        int index = 0;

        if (total_column == 1) {
            for (int i = 0; i < gn.length; i++) {
                if (nc.charAt(0) == String.valueOf(t).charAt(0)) // 空垛
                    i = gn.length;
                index++;
            }
        }

        if (total_column == 2) {
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    if (nc.charAt(0) == String.valueOf(t).charAt(0))
                        i = gn.length;
                    if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                        ii = gn.length;
                    }

                    index++;
                }
            }
        }

        if (total_column == 3) {
            // int index=0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                            i = gn.length;
                        }
                        if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                            ii = gn.length;
                        }
                        if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                            j = gn.length;
                        }

                        index++;
                    }
                }
            }
        }

        if (total_column == 4) {
            // int index=0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        int start_point4 = 0;
                        if (nc.charAt(2) == nc.charAt(3))
                            start_point4 = j;
                        else
                            start_point4 = 0;
                        for (int jj = start_point4; jj < gn.length; jj++) {
                            if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                                i = gn.length;
                            }
                            if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                                ii = gn.length;
                            }
                            if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                                j = gn.length;
                            }
                            if (nc.charAt(3) == String.valueOf(t).charAt(0)) {
                                jj = gn.length;
                            }

                            index++;
                        }
                    }
                }
            }
        }

        if (total_column == 5) {
            // int index=0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        int start_point4 = 0;
                        if (nc.charAt(2) == nc.charAt(3))
                            start_point4 = j;
                        else
                            start_point4 = 0;
                        for (int jj = start_point4; jj < gn.length; jj++) {
                            int start_point5 = 0;
                            if (nc.charAt(3) == nc.charAt(4))
                                start_point5 = jj;
                            else
                                start_point5 = 0;
                            for (int k = start_point5; k < gn.length; k++) {
                                if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                                    i = gn.length;
                                }
                                if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                                    ii = gn.length;
                                }
                                if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                                    j = gn.length;
                                }
                                if (nc.charAt(3) == String.valueOf(t).charAt(0)) {
                                    jj = gn.length;
                                }
                                if (nc.charAt(4) == String.valueOf(t).charAt(0)) {
                                    k = gn.length;
                                }

                                index++;
                            }
                        }
                    }
                }
            }
        }

        if (total_column == 6) {
            // int index=0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        int start_point4 = 0;
                        if (nc.charAt(2) == nc.charAt(3))
                            start_point4 = j;
                        else
                            start_point4 = 0;
                        for (int jj = start_point4; jj < gn.length; jj++) {
                            int start_point5 = 0;
                            if (nc.charAt(3) == nc.charAt(4))
                                start_point5 = jj;
                            else
                                start_point5 = 0;
                            for (int k = start_point5; k < gn.length; k++) {
                                int start_point6 = 0;
                                if (nc.charAt(4) == nc.charAt(5))
                                    start_point6 = k;
                                else
                                    start_point6 = 0;
                                for (int kk = start_point6; kk < gn.length; kk++) {
                                    if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                                        i = gn.length;
                                    }
                                    if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                                        ii = gn.length;
                                    }
                                    if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                                        j = gn.length;
                                    }
                                    if (nc.charAt(3) == String.valueOf(t).charAt(0)) {
                                        jj = gn.length;
                                    }
                                    if (nc.charAt(4) == String.valueOf(t).charAt(0)) {
                                        k = gn.length;
                                    }
                                    if (nc.charAt(5) == String.valueOf(t).charAt(0)) {
                                        kk = gn.length;
                                    }

                                    index++;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (total_column == 7) {
            // int index=0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        int start_point4 = 0;
                        if (nc.charAt(2) == nc.charAt(3))
                            start_point4 = j;
                        else
                            start_point4 = 0;
                        for (int jj = start_point4; jj < gn.length; jj++) {
                            int start_point5 = 0;
                            if (nc.charAt(3) == nc.charAt(4))
                                start_point5 = jj;
                            else
                                start_point5 = 0;
                            for (int k = start_point5; k < gn.length; k++) {
                                int start_point6 = 0;
                                if (nc.charAt(4) == nc.charAt(5))
                                    start_point6 = k;
                                else
                                    start_point6 = 0;
                                for (int kk = start_point6; kk < gn.length; kk++) {
                                    int start_point7 = 0;
                                    if (nc.charAt(5) == nc.charAt(6))
                                        start_point7 = kk;
                                    else
                                        start_point7 = 0;
                                    for (int h = start_point7; h < gn.length; h++) {
                                        if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                                            i = gn.length;
                                        }
                                        if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                                            ii = gn.length;
                                        }
                                        if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                                            j = gn.length;
                                        }
                                        if (nc.charAt(3) == String.valueOf(t).charAt(0)) {
                                            jj = gn.length;
                                        }
                                        if (nc.charAt(4) == String.valueOf(t).charAt(0)) {
                                            k = gn.length;
                                        }
                                        if (nc.charAt(5) == String.valueOf(t).charAt(0)) {
                                            kk = gn.length;
                                        }
                                        if (nc.charAt(6) == String.valueOf(t).charAt(0)) {
                                            h = gn.length;
                                        }

                                        index++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (total_column == 8) {
            // int index=0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        int start_point4 = 0;
                        if (nc.charAt(2) == nc.charAt(3))
                            start_point4 = j;
                        else
                            start_point4 = 0;
                        for (int jj = start_point4; jj < gn.length; jj++) {
                            int start_point5 = 0;
                            if (nc.charAt(3) == nc.charAt(4))
                                start_point5 = jj;
                            else
                                start_point5 = 0;
                            for (int k = start_point5; k < gn.length; k++) {
                                int start_point6 = 0;
                                if (nc.charAt(4) == nc.charAt(5))
                                    start_point6 = k;
                                else
                                    start_point6 = 0;
                                for (int kk = start_point6; kk < gn.length; kk++) {
                                    int start_point7 = 0;
                                    if (nc.charAt(5) == nc.charAt(6))
                                        start_point7 = kk;
                                    else
                                        start_point7 = 0;
                                    for (int h = start_point7; h < gn.length; h++) {
                                        int start_point8 = 0;
                                        if (nc.charAt(6) == nc.charAt(7))
                                            start_point8 = h;
                                        else
                                            start_point8 = 0;
                                        for (int hh = start_point8; hh < gn.length; hh++) {
                                            if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                                                i = gn.length;
                                            }
                                            if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                                                ii = gn.length;
                                            }
                                            if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                                                j = gn.length;
                                            }
                                            if (nc.charAt(3) == String.valueOf(t).charAt(0)) {
                                                jj = gn.length;
                                            }
                                            if (nc.charAt(4) == String.valueOf(t).charAt(0)) {
                                                k = gn.length;
                                            }
                                            if (nc.charAt(5) == String.valueOf(t).charAt(0)) {
                                                kk = gn.length;
                                            }
                                            if (nc.charAt(6) == String.valueOf(t).charAt(0)) {
                                                h = gn.length;
                                            }
                                            if (nc.charAt(7) == String.valueOf(t).charAt(0)) {
                                                hh = gn.length;
                                            }

                                            index++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return index;

    }

    String[] calculateWeightPermutation(String nc, int t, String[] gn) {

        String[] ss = new String[calculateWeightPermutationSize(nc, t, gn)];
        int total_column = 0; // calculate the number of stacks on which there
        // are empty slots
        for (int i = 0; i < nc.length(); i++) {
            // System.out.println(nc.charAt(i));
            if (nc.charAt(i) > '0')
                total_column++;
        }
        // System.out.println(total_column);
        String tail = ""; // the states on the full stack are "0000"
        for (int j = 0; j < nc.length() - total_column; j++)
            tail = tail + "0";
        // System.out.println(tail);

        if (total_column == 1) {
            int index = 0;
            for (int i = 0; i < gn.length; i++) {
                String first = "";
                if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                    first = "*";
                    i = gn.length;
                } else
                    first = gn[i];
                ss[index] = first + tail;
                index++;
            }
        }

        if (total_column == 2) {
            int index = 0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    String first = "";
                    if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                        first = "*";
                        i = gn.length;
                    } else
                        first = gn[i];
                    String second = "";
                    if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                        second = "*";
                        ii = gn.length;
                    } else
                        second = gn[ii];

                    ss[index] = first + second + tail;
                    index++;
                }
            }
        }

        if (total_column == 3) {
            int index = 0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        String first = "";
                        if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                            first = "*";
                            i = gn.length;
                        } else
                            first = gn[i];
                        String second = "";
                        if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                            second = "*";
                            ii = gn.length;
                        } else
                            second = gn[ii];
                        String third = "";
                        if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                            third = "*";
                            j = gn.length;
                        } else
                            third = gn[j];

                        ss[index] = first + second + third + tail;
                        index++;
                    }
                }
            }
        }

        if (total_column == 4) {
            int index = 0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        int start_point4 = 0;
                        if (nc.charAt(2) == nc.charAt(3))
                            start_point4 = j;
                        else
                            start_point4 = 0;
                        for (int jj = start_point4; jj < gn.length; jj++) {
                            String first = "";
                            if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                                first = "*";
                                i = gn.length;
                            } else
                                first = gn[i];
                            String second = "";
                            if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                                second = "*";
                                ii = gn.length;
                            } else
                                second = gn[ii];
                            String third = "";
                            if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                                third = "*";
                                j = gn.length;
                            } else
                                third = gn[j];
                            String fourth = "";
                            if (nc.charAt(3) == String.valueOf(t).charAt(0)) {
                                fourth = "*";
                                jj = gn.length;
                            } else
                                fourth = gn[jj];

                            ss[index] = first + second + third + fourth + tail;
                            index++;
                        }
                    }
                }
            }
        }

        if (total_column == 5) {
            int index = 0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        int start_point4 = 0;
                        if (nc.charAt(2) == nc.charAt(3))
                            start_point4 = j;
                        else
                            start_point4 = 0;
                        for (int jj = start_point4; jj < gn.length; jj++) {
                            int start_point5 = 0;
                            if (nc.charAt(3) == nc.charAt(4))
                                start_point5 = jj;
                            else
                                start_point5 = 0;
                            for (int k = start_point5; k < gn.length; k++) {
                                String first = "";
                                if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                                    first = "*";
                                    i = gn.length;
                                } else
                                    first = gn[i];
                                String second = "";
                                if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                                    second = "*";
                                    ii = gn.length;
                                } else
                                    second = gn[ii];
                                String third = "";
                                if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                                    third = "*";
                                    j = gn.length;
                                } else
                                    third = gn[j];
                                String fourth = "";
                                if (nc.charAt(3) == String.valueOf(t).charAt(0)) {
                                    fourth = "*";
                                    jj = gn.length;
                                } else
                                    fourth = gn[jj];
                                String fifth = "";
                                if (nc.charAt(4) == String.valueOf(t).charAt(0)) {
                                    fifth = "*";
                                    k = gn.length;
                                } else
                                    fifth = gn[k];

                                ss[index] = first + second + third + fourth + fifth + tail;
                                index++;
                            }
                        }
                    }
                }
            }
        }

        if (total_column == 6) {
            int index = 0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        int start_point4 = 0;
                        if (nc.charAt(2) == nc.charAt(3))
                            start_point4 = j;
                        else
                            start_point4 = 0;
                        for (int jj = start_point4; jj < gn.length; jj++) {
                            int start_point5 = 0;
                            if (nc.charAt(3) == nc.charAt(4))
                                start_point5 = jj;
                            else
                                start_point5 = 0;
                            for (int k = start_point5; k < gn.length; k++) {
                                int start_point6 = 0;
                                if (nc.charAt(4) == nc.charAt(5))
                                    start_point6 = k;
                                else
                                    start_point6 = 0;
                                for (int kk = start_point6; kk < gn.length; kk++) {
                                    String first = "";
                                    if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                                        first = "*";
                                        i = gn.length;
                                    } else
                                        first = gn[i];
                                    String second = "";
                                    if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                                        second = "*";
                                        ii = gn.length;
                                    } else
                                        second = gn[ii];
                                    String third = "";
                                    if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                                        third = "*";
                                        j = gn.length;
                                    } else
                                        third = gn[j];
                                    String fourth = "";
                                    if (nc.charAt(3) == String.valueOf(t).charAt(0)) {
                                        fourth = "*";
                                        jj = gn.length;
                                    } else
                                        fourth = gn[jj];
                                    String fifth = "";
                                    if (nc.charAt(4) == String.valueOf(t).charAt(0)) {
                                        fifth = "*";
                                        k = gn.length;
                                    } else
                                        fifth = gn[k];
                                    String sixth = "";
                                    if (nc.charAt(5) == String.valueOf(t).charAt(0)) {
                                        sixth = "*";
                                        kk = gn.length;
                                    } else
                                        sixth = gn[kk];

                                    ss[index] = first + second + third + fourth + fifth + sixth + tail;
                                    index++;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (total_column == 7) {
            int index = 0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        int start_point4 = 0;
                        if (nc.charAt(2) == nc.charAt(3))
                            start_point4 = j;
                        else
                            start_point4 = 0;
                        for (int jj = start_point4; jj < gn.length; jj++) {
                            int start_point5 = 0;
                            if (nc.charAt(3) == nc.charAt(4))
                                start_point5 = jj;
                            else
                                start_point5 = 0;
                            for (int k = start_point5; k < gn.length; k++) {
                                int start_point6 = 0;
                                if (nc.charAt(4) == nc.charAt(5))
                                    start_point6 = k;
                                else
                                    start_point6 = 0;
                                for (int kk = start_point6; kk < gn.length; kk++) {
                                    int start_point7 = 0;
                                    if (nc.charAt(5) == nc.charAt(6))
                                        start_point7 = kk;
                                    else
                                        start_point7 = 0;
                                    for (int h = start_point7; h < gn.length; h++) {
                                        String first = "";
                                        if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                                            first = "*";
                                            i = gn.length;
                                        } else
                                            first = gn[i];
                                        String second = "";
                                        if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                                            second = "*";
                                            ii = gn.length;
                                        } else
                                            second = gn[ii];
                                        String third = "";
                                        if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                                            third = "*";
                                            j = gn.length;
                                        } else
                                            third = gn[j];
                                        String fourth = "";
                                        if (nc.charAt(3) == String.valueOf(t).charAt(0)) {
                                            fourth = "*";
                                            jj = gn.length;
                                        } else
                                            fourth = gn[jj];
                                        String fifth = "";
                                        if (nc.charAt(4) == String.valueOf(t).charAt(0)) {
                                            fifth = "*";
                                            k = gn.length;
                                        } else
                                            fifth = gn[k];
                                        String sixth = "";
                                        if (nc.charAt(5) == String.valueOf(t).charAt(0)) {
                                            sixth = "*";
                                            kk = gn.length;
                                        } else
                                            sixth = gn[kk];
                                        String seventh = "";
                                        if (nc.charAt(6) == String.valueOf(t).charAt(0)) {
                                            seventh = "*";
                                            h = gn.length;
                                        } else
                                            seventh = gn[h];

                                        ss[index] = first + second + third + fourth + fifth + sixth + seventh + tail;
                                        index++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (total_column == 8) {
            int index = 0;
            for (int i = 0; i < gn.length; i++) {
                int start_point2 = 0;
                if (nc.charAt(0) == nc.charAt(1))
                    start_point2 = i;
                else
                    start_point2 = 0;
                for (int ii = start_point2; ii < gn.length; ii++) {
                    int start_point3 = 0;
                    if (nc.charAt(1) == nc.charAt(2))
                        start_point3 = ii;
                    else
                        start_point3 = 0;
                    for (int j = start_point3; j < gn.length; j++) {
                        int start_point4 = 0;
                        if (nc.charAt(2) == nc.charAt(3))
                            start_point4 = j;
                        else
                            start_point4 = 0;
                        for (int jj = start_point4; jj < gn.length; jj++) {
                            int start_point5 = 0;
                            if (nc.charAt(3) == nc.charAt(4))
                                start_point5 = jj;
                            else
                                start_point5 = 0;
                            for (int k = start_point5; k < gn.length; k++) {
                                int start_point6 = 0;
                                if (nc.charAt(4) == nc.charAt(5))
                                    start_point6 = k;
                                else
                                    start_point6 = 0;
                                for (int kk = start_point6; kk < gn.length; kk++) {
                                    int start_point7 = 0;
                                    if (nc.charAt(5) == nc.charAt(6))
                                        start_point7 = kk;
                                    else
                                        start_point7 = 0;
                                    for (int h = start_point7; h < gn.length; h++) {
                                        int start_point8 = 0;
                                        if (nc.charAt(6) == nc.charAt(7))
                                            start_point8 = h;
                                        else
                                            start_point8 = 0;
                                        for (int hh = start_point8; hh < gn.length; hh++) {
                                            String first = "";
                                            if (nc.charAt(0) == String.valueOf(t).charAt(0)) {
                                                first = "*";
                                                i = gn.length;
                                            } else
                                                first = gn[i];
                                            String second = "";
                                            if (nc.charAt(1) == String.valueOf(t).charAt(0)) {
                                                second = "*";
                                                ii = gn.length;
                                            } else
                                                second = gn[ii];
                                            String third = "";
                                            if (nc.charAt(2) == String.valueOf(t).charAt(0)) {
                                                third = "*";
                                                j = gn.length;
                                            } else
                                                third = gn[j];
                                            String fourth = "";
                                            if (nc.charAt(3) == String.valueOf(t).charAt(0)) {
                                                fourth = "*";
                                                jj = gn.length;
                                            } else
                                                fourth = gn[jj];
                                            String fifth = "";
                                            if (nc.charAt(4) == String.valueOf(t).charAt(0)) {
                                                fifth = "*";
                                                k = gn.length;
                                            } else
                                                fifth = gn[k];
                                            String sixth = "";
                                            if (nc.charAt(5) == String.valueOf(t).charAt(0)) {
                                                sixth = "*";
                                                kk = gn.length;
                                            } else
                                                sixth = gn[kk];
                                            String seventh = "";
                                            if (nc.charAt(6) == String.valueOf(t).charAt(0)) {
                                                seventh = "*";
                                                h = gn.length;
                                            } else
                                                seventh = gn[h];
                                            String eighth = "";
                                            if (nc.charAt(7) == String.valueOf(t).charAt(0)) {
                                                eighth = "*";
                                                hh = gn.length;
                                            } else
                                                eighth = gn[hh];

                                            ss[index] = first + second + third + fourth + fifth + sixth + seventh
                                                    + eighth + tail;
                                            index++;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < ss.length; i++)
            System.out.println(ss[i]);

        return ss;

    }


    /**
     * 生成放到一个stack中的翻到次数
     *
     * @param t
     * @param gn
     */
    void generateRehandleTimeOfAStack(int t, String[] gn, double[] ratio, double pessimisticCoe) {

        int previousM = 0;
        String[] previousWeight = new String[gn.length];
        double[][] previousValueForEachWeight = new double[gn.length][gn.length];
        double[] previousValue = new double[gn.length];

        int currentM = 0;
        String[] currentWeight = new String[gn.length];
        double[][] currentValueForEachWeight = new double[gn.length][gn.length];
        double[] currentValue = new double[gn.length];

        // 用于存储数据
        ArrayList values = new ArrayList();
        // 记录初始值
        values.add(0);
        values.add("0");
        for (int arrival = 0; arrival < gn.length; arrival++) {
            values.add(0.0);
        }
        values.add(0.0);

        for (int m = 1; m <= t; m++) {
            currentM = m;
            if (m < t) {
                for (int w = 0; w < gn.length; w++) {
                    currentWeight[w] = gn[w];
                    for (int arrival = 0; arrival < gn.length; arrival++) {
                        // 刚到达的更轻
                        if (arrival > w) {
                            int index = -2;
                            for (int j = 0; j < previousWeight.length; j++) {
                                if (previousWeight[j] == gn[w]) {
                                    index = j;
                                    break;
                                }
                            }
                            if (m == 1 && m < t) {
                                currentValueForEachWeight[w][arrival] = 1 + pessimisticCoe * (t - m - 1);
                            }
                            if (m > 1 && m < t) {
                                currentValueForEachWeight[w][arrival] = 1 + pessimisticCoe * (t - m - 1)
                                        + previousValue[index];
                            }
                        } else {
                            int index = -2;
                            for (int j = 0; j < previousWeight.length; j++) {
                                if (previousWeight[j] == gn[arrival]) {
                                    index = j;
                                    break;
                                }
                            }
                            if (m == 1 && m < t) {
                                currentValueForEachWeight[w][arrival] = 0;
                            }
                            if (m > 1 && m < t) {
                                currentValueForEachWeight[w][arrival] = previousValue[index];
                            }
                        }

                    }
                    values.add(currentM);
                    values.add(currentWeight[w]);
                    double temp = 0.0;
                    for (int arrival = 0; arrival < gn.length; arrival++) {
                        values.add(currentValueForEachWeight[w][arrival]);
                        temp = temp + currentValueForEachWeight[w][arrival] * ratio[arrival];
                    }
                    currentValue[w] = temp;
                    values.add(currentValue[w]);
                }
                // 为下一次计算准备数据
                for (int w = 0; w < gn.length; w++) {
                    previousWeight[w] = currentWeight[w];
                    for (int arrival = 0; arrival < gn.length; arrival++) {
                        previousValueForEachWeight[w][arrival] = currentValueForEachWeight[w][arrival];
                    }
                    previousValue[w] = currentValue[w];
                }

            } else {
                values.add(currentM);
                values.add("*");
                double temp = 0.0;
                for (int arrival = 0; arrival < gn.length; arrival++) {
                    values.add(previousValue[arrival]);
                    temp = temp + previousValue[arrival] * ratio[arrival];
                }
                values.add(temp);
            }

        }

        int length = gn.length + 3;
        int size = values.size() / length;
        int[] emptyNumber = new int[size];
        String[] weightGroup = new String[size];
        double[][] value = new double[size][gn.length + 1];
        for (int i = 0; i < size; i++) {
            emptyNumber[i] = (int) values.get(i * length);
            weightGroup[i] = (String) values.get(i * length + 1);
            System.out.print(emptyNumber[i] + "  " + weightGroup[i] + "  ");
            for (int j = 0; j < gn.length + 1; j++) {
                value[i][j] = (double) values.get(i * length + 2 + j);
                System.out.print(value[i][j] + "  ");
            }
            System.out.println("  ");
        }

    }

    /**
     * 执行动态规划计算最优存储位置
     *
     * @param s
     * @param t
     * @param gn
     * @param ratio
     * @param input
     */
    void executeDynamicProgramming(int s, int t, String[] gn, double[] ratio, boolean input) throws IOException {
        // s: the number of stacks
        // t: the number of tiers
        // g: the number of weight groups
        // n：空的场地位置的个数
        int n = 0;
        String[] ncPrevious;
        String[] scPrevious;
        double[] vaPrevious;
        int tnPrevious;

        // ncCurrent，scCurrent，vaCurrent是后推式动态规划中当前步的结果，即当前需要计算的结果
        String[] ncCurrent;
        String[] scCurrent;
        double[] vaCurrent;
        int tnCurrent;

        tnPrevious = 1;
        ncPrevious = new String[tnPrevious];
        scPrevious = new String[tnPrevious];
        vaPrevious = new double[tnPrevious];

        // 设定初始值：nc_pre，sc_pre，va_pre是后推式动态规划中后一步的结果，即已知的结果
        String temp = "";
        for (int j = 0; j < s; j++)
            temp = temp + "0";
        ncPrevious[0] = temp;
        scPrevious[0] = temp;
        vaPrevious[0] = 0.0;
        totalStateSize = 0;

        MyProperties properties = new MyProperties();


        FileWriter fileWriter = new FileWriter(MyProperties.dynamic_result + "store_value_BM_" + String.valueOf(s) + String.valueOf(t)
                + "_" + String.valueOf(gn.length) + ".txt", false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        while (n < s * t) {
            n = n + 1;
            // the total dimension for the case when the number of empty
            // slots equals n
            String[] nc = calculateEmptyPermutation(s, t, n);
            tnCurrent = calculateTotalWeightPermutationSize(s, t, n, gn, nc);
            totalStateSize = totalStateSize + tnCurrent;
            ncCurrent = new String[tnCurrent];
            scCurrent = new String[tnCurrent];
            vaCurrent = new double[tnCurrent];
            int index = 0;
            for (int i = 0; i < nc.length; i++) {
                String[] sc = calculateWeightPermutation(nc[i], t, gn); // 获取对应于一种空箱状态的，所有重量状态
                for (int j = 0; j < sc.length; j++) {
                    ncCurrent[index] = nc[i];
                    scCurrent[index] = sc[j];
                    vaCurrent[index] = calculateObjectiveValue(t, nc[i], sc[j], gn, ncPrevious, scPrevious,
                            vaPrevious, ratio);
                    printWriter.print(ncCurrent[index] + scCurrent[index] + "\t");
                    for (int k = 0; k < gn.length; k++)
                        printWriter.print(followingState[k][0] + followingState[k][1] + "\t" + stateNumberChosen[k]
                                + "\t" + stateWeightChosen[k] + "\t");
                    printWriter.println();
                    if (n == s * t)
                        finalObjectiveValue = vaCurrent[index];
                    index++;
                }
            }

            tnPrevious = tnCurrent;
            ncPrevious = new String[tnPrevious];
            scPrevious = new String[tnPrevious];
            vaPrevious = new double[tnPrevious];
            for (int i = 0; i < tnPrevious; i++) {
                ncPrevious[i] = ncCurrent[i];
                scPrevious[i] = scCurrent[i];
                vaPrevious[i] = vaCurrent[i];
            }

            for (int i = 0; i < ncCurrent.length; i++) {
                System.out.println(i + "  " + ncCurrent[i] + "  " + scCurrent[i] + " " + vaCurrent[i]);
            }

        }
        fileWriter.close();
        printWriter.close();


    }

    public static void main(String[] args) throws IOException {
        Dynamic_Programming_BM dynamicProgramming = new Dynamic_Programming_BM();
        double[] ratio = {0.5, 0.5};
        String[] gn = {"H", "L"};

        // 两个权重的情形
        long beginTime, endTime, duration;

        FileWriter fileWriter = new FileWriter(MyProperties.dynamic_result + "BM_LB1.txt", false);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        for (int s = 4; s < 8; s++) {
            for (int t = 3; t < 5; t++) {
                if (s * t <= 12) {
                    Date myDate = new Date();
                    beginTime = myDate.getTime();
//                     dynamicProgramming.generateRehandleTimeOfAStack(t, gn, ratio, 1);
                    dynamicProgramming.executeDynamicProgramming(s, t, gn, ratio, true);
                    Date myDate2 = new Date();
                    endTime = myDate2.getTime();
                    duration = endTime - beginTime;
//                     printWriter.println(s + "\t" + t + "\t" + 2 + "\t" + dynamicProgramming.totalStateSize + "\t"
//                             + dynamicProgramming.finalObjectiveValue + "\t" + duration);
                }

            }
        }
        fileWriter.close();
        printWriter.close();
    }

}
