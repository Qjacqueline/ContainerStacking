// EIP model
package com.jacqueline.containerstackingproblem;

import com.jacqueline.containerstackingproblem.config.dialogbox;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Dynamic_programming_New2 {
    int state_size;
    double final_value;
    String[] next_state_number;
    int[][] next_state_weight;
    int[] next_weight_group;
    double[] next_value;
    int[] tier_no;
    int[][] layout;

    double calculate_rehandle(char nc, int sc, int gn, int t, int[] gn_group) {
        double rehandle = 0;
        double belta = 0.2;
        double alpha = 1;
        double gamma = 1;
        int nc_int = Integer.parseInt(String.valueOf(nc));
        int[] result = new int[gn_group.length - 1];
        int maxWeight = -1;
        if (gn_group.length == 2) {
            result[0] = sc / 10;
            if (result[0] != 0) {//鏈塎
                maxWeight = 0;
            } else {
                maxWeight = 1;//鍏ㄦ槸L(1)鐨勬椂鍊欐垨绌�
            }
            if (gn == 10) {
                rehandle = Math.max(belta * maxWeight * (nc_int - 1), 0);
                //	rehandle=0;
            }
            if (gn == 1) {
                if (maxWeight == 0) {
                    rehandle = alpha * Math.max((t - nc_int + 1), 0) + gamma * Math.max((t - nc_int + 1 - result[0]), 0);
                    //rehandle=alpha*Math.max((t-nc_int+1), 0);
                    //	rehandle=1;
                } else {
                    rehandle = 0;
                }
            }

        }
        if (gn_group.length == 3) {
            result[0] = sc / 100;//鏈夋病鏈塇
            result[1] = (sc / 10) % 10;//鏈夋病鏈塎
            if (result[0] != 0) {//鏈塇
                maxWeight = 0;
            } else if (result[1] != 0) {//鏈塎
                maxWeight = 1;
            } else {
                maxWeight = 2;
            }
            if (gn == 100) {
                rehandle = Math.max(belta * maxWeight * (nc_int - 1), 0);
                //	    rehandle=0;
            }
            if (gn == 10) {
                if (maxWeight == 0) {//鏈塇
                    rehandle = alpha * Math.max((t - nc_int + 1), 0) + gamma * Math.max((t - nc_int + 1 - result[0]), 0);
                    //rehandle=alpha*Math.max((t-nc_int+1), 0);
                    //	rehandle=1;
                }
                if (maxWeight == 1) {
                    rehandle = 0;
                }
                if (maxWeight == 2) {
                    rehandle = Math.max(belta * (nc_int - 1), 0);
                    //	rehandle=0;
                }
            }
            if (gn == 1) {
                if (maxWeight == 2) {
                    rehandle = 0;
                } else if (maxWeight == 1) {
                    rehandle = Math.max((t - nc_int + 1), 0) + gamma * Math.max((t - nc_int + 1 - result[1]), 0);
                    //rehandle=alpha*Math.max((t-nc_int+1), 0);
                    //	rehandle=1;
                } else {
                    rehandle = Math.max((t - nc_int + 1), 0) + gamma * Math.max((t - nc_int + 1 - result[0]), 0);
                    //rehandle=alpha*Math.max((t-nc_int+1), 0);
                    //	rehandle=1;
                }
            }
        }
        //if (rehandle<0)
        //System.out.println("wrong");
        return rehandle;
    }

    double calculate_value(int t, String nc, int[] sc, int[] gn, String[] nc_pre, int[][] sc_pre, double[] va_pre,
                           double[] ratio) {
        next_state_number = new String[gn.length];
        next_state_weight = new int[gn.length][sc.length];
        next_weight_group = new int[gn.length];
        next_value = new double[gn.length];
        tier_no = new int[gn.length];
        double[] objective_value = new double[gn.length];

        for (int i = 0; i < gn.length; i++)
            objective_value[i] = Double.MAX_VALUE;
        for (int i = 0; i < gn.length; i++) { // gn.length kinds of arrival
            //zheli yao xiugai
            if (!(calculate_Containers(nc, sc, gn, t)[i] < (Math.ceil(sc.length * t * ratio[i])))) {
                continue;
            }
            //

            for (int j = 0; j < nc.length(); j++) { // there are at most nc.length possible locations

                if (nc.charAt(j) > '0') {
                    String nc_temp = nc;
                    int[] sc_temp = new int[sc.length];
                    for (int k = 0; k < sc.length; k++)
                        sc_temp[k] = sc[k];
                    int tier_no_temp = Integer.parseInt(String.valueOf(nc.charAt(j)));
                    double rehandle_times = calculate_rehandle(nc_temp.charAt(j), sc_temp[j], gn[i], t, gn);
                    //当前是否会压箱
                    // adjust nc
                    String first_part = "";
                    String third_part = "";
                    String second_part = "";
                    if (j > 0)
                        first_part = nc_temp.substring(0, j - 1 - 0 + 1);
                    if (j < nc.length() - 1)
                        third_part = nc_temp.substring(j + 1, nc_temp.length() - 1 - (j + 1) + 1 + (j + 1));
                    int left_empty = Integer.parseInt(String.valueOf(nc_temp.charAt(j))) - 1;
                    second_part = String.valueOf(left_empty);
                    nc_temp = first_part + second_part + third_part;
                    // adjust sc
//						if (left_empty == 0) {
//							sc_temp[j] = t;
//						} else {
                    int div = sc_temp[j] / gn[i];
//						System.out.println(div);
                    if (div == 0) {
                        sc_temp[j] = gn[i] * (t - left_empty);
                    } else {
                        if (left_empty + div + 1 <= t) {
                            sc_temp[j] = gn[i] * (t - left_empty);
                        }
//						 }
                    }

                    // re-sequence nc and sc
                    for (int k = 0; k < nc_temp.length() - 1; k++) {
                        int first = Integer.parseInt(String.valueOf(nc_temp.charAt(k)));
                        int second = Integer.parseInt(String.valueOf(nc_temp.charAt(k + 1)));
                        String first_part2 = "";
                        String fourth_part2 = "";
                        String second_part2 = "";
                        String third_part2 = "";
                        if (first < second) {//鍓嶇┖绠辨暟<鍚庣┖绠辨暟锛岃浜ゆ崲
                            if (k > 0)
                                first_part2 = nc_temp.substring(0, k - 1 - 0 + 1);//0鍒発涔嬮棿鐨勯儴鍒�
                            if (k < nc.length() - 2)
                                fourth_part2 = nc_temp.substring(k + 2,
                                        nc_temp.length() - 1 - (k + 2) + 1 + (k + 2));
                            second_part2 = String.valueOf(second);
                            third_part2 = String.valueOf(first);
                            nc_temp = first_part2 + second_part2 + third_part2 + fourth_part2;
                            // re-sequence sc
                            int temp_sc = sc_temp[k + 1];
                            sc_temp[k + 1] = sc_temp[k];
                            sc_temp[k] = temp_sc;//浜ゆ崲sc鐨勫��
                        }
                    }
                    // re-sequence sc
                    boolean needed = true;
                    while (needed) {
                        needed = false;
                        for (int k = 0; k < nc_temp.length() - 1; k++) {
                            int first = Integer.parseInt(String.valueOf(nc_temp.charAt(k)));
                            int second = Integer.parseInt(String.valueOf(nc_temp.charAt(k + 1)));
                            int first1 = sc_temp[k];
                            int second1 = sc_temp[k + 1];
                            if (first == second && first1 < second1) {
                                needed = true;
                                int temp_sc = sc_temp[k + 1];
                                sc_temp[k + 1] = sc_temp[k];
                                sc_temp[k] = temp_sc;
                            }
                        }
                    }

                    int start_index = 0;
                    for (int k = 0; k < nc_pre.length; k++) {
                        if (nc_temp.equals(nc_pre[k])) {
                            start_index = k;
                            break;
                        }
                    }//绌虹鏁颁笌鍓嶄竴涓姸鎬佺浉鍚�

/*						int start = start_index;
						int interval = 100;
						while (start < nc_pre.length) {
							if (nc_temp.equals(nc_pre[start])) {
								boolean small = false;
								for (int m = 0; m < sc_temp.length; m++) {
									if (sc_temp[m] < sc_pre[start][m]) {
										boolean allequal = true;
										for (int k = 0; k < m; k++) {
											if (sc_temp[k] != sc_pre[start][k]) {
												allequal = false;
												break;
											}
										}
										if (allequal) {
											small = true;
											break;
										}
									}
								}

								if (small) {
									int next_start = start + interval;
									if (next_start < nc_pre.length) {
										if (nc_temp.compareTo(nc_pre[next_start]) > 0) {
											break;
										}
										if (nc_temp.equals(nc_pre[next_start])) {
											boolean small2 = false;
											for (int m = 0; m < sc_temp.length; m++) {
												if (sc_temp[m] < sc_pre[next_start][m]) {
													boolean allequal2 = true;
													for (int k = 0; k < m; k++) {
														if (sc_temp[k] != sc_pre[next_start][k]) {
															allequal2 = false;
															break;
														}
													}
													if (allequal2) {
														small2 = true;
														break;
													}
												}
											}
											if (small2) {
												start = start + interval + 1;
											}
											if (!small2) {
												break;
											}
										}
									} else {
										break;
									}
								} else {
									break;
								}
							}
						}
						boolean find = false;
						double following_value = 0.0;
						for (int k = 0; k < nc_pre.length; k++) {
							if (nc_temp.equals(nc_pre[k])) {
								boolean match = true;
								for (int m = 0; m < sc_temp.length; m++)
									if (sc_temp[m] != sc_pre[k][m]) {
										match = false;
										break;
									}
								if (match) {
									find=true;
									following_value = va_pre[k];
									break;
								}
							}
						}
	//					if(find)
	//					   System.out.println("findfindfindfind");
	//					else
	//					   System.out.println("nofindnofindnofindnofind");
	*/                    // match the states;

                    double following_value = 0.0;
                    for (int k = 0; k < nc_pre.length; k++) {
                        if (nc_temp.equals(nc_pre[k])) {
                            boolean match = true;
                            for (int m = 0;
                                 m < sc_temp.length; m++)
                                if (sc_temp[m] != sc_pre[k][m]) {
                                    match = false;
                                    break;
                                }
                            if (match) {
                                following_value = va_pre[k];
                                break;
                            }
                        }
                    }

                    // calculate objective value

                    double value_temp = following_value + rehandle_times;
                    if (objective_value[i] > value_temp) {

                        objective_value[i] = value_temp;
                        next_state_number[i] = nc_temp;
                        for (int m = 0; m < sc_temp.length; m++)
                            next_state_weight[i][m] = sc_temp[m];
                        next_weight_group[i] = gn[i];
                        next_value[i] = rehandle_times;
                        tier_no[i] = tier_no_temp;
                    }

                }
            }
        }
        //       System.out.println("nc:"+nc);
        //       for(int k=0;k<nc.length();k++) {
        //           System.out.println("sc:"+sc[k]);
        //        }
        double total_objective_value = 0.0;

        double[] ratio_adjust = calculate_Containers_ratio(nc, sc, gn, t);

        //tiao zheng gai lv

        for (int i = 0; i < objective_value.length; i++) {
            //	System.out.println("鏉╂瑦妲稿В鏂剧伐閿涳拷"+ratio[i]);
            total_objective_value = total_objective_value + objective_value[i] * ratio_adjust[i];
        }


        return total_objective_value;
    }

    //閻樿埖锟戒胶鈹栭梻瀵告畱鐠侊紕鐣�
    int dimension_size(int s, int t, int n, int[] gn, String[] result) {
        int total_number = 0;
        for (int i = 0; i < result.length; i++) {
            total_number = total_number + permutation_size(result[i], t, gn);
        }
        return total_number;
    }

    //solution_space閺傝纭堕懢宄板絿閻ㄥ嫭妲竢esult閺佹壆绮嶉敍灞藉祮閻樿埖锟戒胶娈戦崜宥呭磹濞堢绱欐俊鍌︾窗2211閿涘本鏆熺�涙銆冪粈铏光敄濡茶姤鏆熼敍锟�
    String[] solution_space(int s, int t, int n) {
        ArrayList value = new ArrayList();
        int total_number = 0;
        /* if(s==2){ for(int i=t; i>=0; i--) for(int ii=i; ii>=0;
         * ii--){ if(i+ii==n) total_number++; } } if(s==3){ for(int i=t; i>=0; i--)
         * for(int ii=i; ii>=0; ii--) for(int j=ii; j>=0; j--){ if(i+ii+j==n)
         * total_number++; } } if(s==4){ for(int i=t; i>=0; i--) for(int ii=i; ii>=0;
         * ii--) for(int j=ii; j>=0; j--) for(int jj=j; jj>=0; jj--){ if(i+ii+j+jj==n)
         * total_number++; } } if(s==5){ for(int i=t; i>=0; i--) for(int ii=i; ii>=0;
         * ii--) for(int j=ii; j>=0; j--) for(int jj=j; jj>=0; jj--) for(int k=jj; k>=0;
         * k--){ if(i+ii+j+jj+k==n) total_number++; } } if(s==6){ for(int i=t; i>=0;
         * i--) for(int ii=i; ii>=0; ii--) for(int j=ii; j>=0; j--) for(int jj=j; jj>=0;
         * jj--) for(int k=jj; k>=0; k--) for(int kk=k; kk>=0; kk--){
         * if(i+ii+j+jj+k+kk==n) total_number++; } } if(s==7){ for(int i=t; i>=0; i--)
         * for(int ii=i; ii>=0; ii--) for(int j=ii; j>=0; j--) for(int jj=j; jj>=0;
         * jj--) for(int k=jj; k>=0; k--) for(int kk=k; kk>=0; kk--) for(int h=kk; h>=0;
         * h--){ if(i+ii+j+jj+k+kk+h==n) total_number++; } } //
         */
//        if (s == 8) {
//            for (int i = t;
//                 i >= 0; i--)
//                for (int ii = i; ii >= 0; ii--)
//                    for (int j = ii; j >= 0; j--)
//                        for (int jj = j;
//                             jj >= 0; jj--)
//                            for (int k = jj; k >= 0; k--)
//                                for (int kk = k; kk >= 0; kk--)
//                                    for (int
//                                         h = kk; h >= 0; h--)
//                                        for (int hh = h; hh >= 0; hh--) {
//                                            if (i + ii + j + jj + k + kk + h + hh == n)
//                                                total_number++;
//                                        }
//        }
//        String[] result = new String[total_number];
//        int index = 0;

        if (s == 2) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    if (i + ii == n) {
                        value.add(String.valueOf(i) + String.valueOf(ii));
                        // result[index]=String.valueOf(i)+String.valueOf(ii);
                        // index++;
                    }
        }
        if (s == 3) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--) {
                        if (i + ii + j == n) {
                            value.add(String.valueOf(i) + String.valueOf(ii) + String.valueOf(j));
                            // result[index]=String.valueOf(i)+String.valueOf(ii)+String.valueOf(j);
                            // index++;
                        }
                    }
        }
        if (s == 4) {
            for (int i = t; i >= 0; i--)
                for (int ii = i; ii >= 0; ii--)
                    for (int j = ii; j >= 0; j--)
                        for (int jj = j; jj >= 0; jj--) {
                            if (i + ii + j + jj == n) {
                                value.add(String.valueOf(i) + String.valueOf(ii) + String.valueOf(j)
                                        + String.valueOf(jj));
                                // result[index]=String.valueOf(i)+String.valueOf(ii)+String.valueOf(j)+String.valueOf(jj);
                                // index++;
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
                                    value.add(String.valueOf(i) + String.valueOf(ii) + String.valueOf(j)
                                            + String.valueOf(jj) + String.valueOf(k));
                                    // result[index]=String.valueOf(i)+String.valueOf(ii)+String.valueOf(j)+String.valueOf(jj)+String.valueOf(k);
                                    // index++;
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
                                        value.add(String.valueOf(i) + String.valueOf(ii) + String.valueOf(j)
                                                + String.valueOf(jj) + String.valueOf(k) + String.valueOf(kk));
                                        // result[index]=String.valueOf(i)+String.valueOf(ii)+String.valueOf(j)+String.valueOf(jj)+String.valueOf(k)+String.valueOf(kk);
                                        // index++;
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
                                            value.add(String.valueOf(i) + String.valueOf(ii) + String.valueOf(j)
                                                    + String.valueOf(jj) + String.valueOf(k) + String.valueOf(kk)
                                                    + String.valueOf(h));
                                            // result[index]=String.valueOf(i)+String.valueOf(ii)+String.valueOf(j)+String.valueOf(jj)+String.valueOf(k)+String.valueOf(kk)+String.valueOf(h);
                                            // index++;
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
                                                value.add(String.valueOf(i) + String.valueOf(ii) + String.valueOf(j)
                                                        + String.valueOf(jj) + String.valueOf(k) + String.valueOf(kk)
                                                        + String.valueOf(h) + String.valueOf(hh));
//                                                result[index] = String.valueOf(i) + String.valueOf(ii) + String.valueOf(j) + String.valueOf(jj) + String.valueOf(k) + String.valueOf(kk) + String.valueOf(h) + String.valueOf(hh);
//                                                index++;
                                            }
                                        }
        }
        String[] result = new String[value.size()];
        for (int i = 0; i < value.size(); i++)
            result[i] = (String) value.get(i);

//		for (int i = 0; i < result.length; i++)
//			System.out.println(result[i]);
        return result;
    }

    // permutation_size閺傝纭舵稉鏄忣洣閼惧嘲褰囬惃鍕Ц鐠囶櫞c娑撳婀侀崙鐘殿潚閻樿埖锟戒緤绱欐俊锟�200,閺堝00閸滃00娑撱倗顫掗敍锟�
    int permutation_size(String nc, int t, int[] gn) {
        int total_column = 0;  //calculate the number of stacks on which there are empty slots
        for (int i = 0; i < nc.length(); i++) {
            //System.out.println(nc.charAt(i));
            if (nc.charAt(i) > '0')
                total_column++;
        }
        int index = 0;
        int[] columnall = column_state(t, gn);
        ArrayList value = new ArrayList();

        if (total_column == 1) {
            int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
            index = column_state(already_have1, gn).length;
        }

        if (total_column == 2) {
            int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0))); //Integer.parseInt(String.valueOf(nc.charAt(0))): the number of empty slot in a stack
            int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1))); // already_have1: the number of containers stacking on the stack.
            int column_state_size1 = column_state(already_have1, gn).length;
            int column_state_size2 = column_state(already_have2, gn).length;
            for (int i = 0; i < column_state_size1; i++) {
                int start_position2;
                if (already_have2 == already_have1)
                    start_position2 = i;
                else
                    start_position2 = 0;
                for (int j = start_position2; j < column_state_size2; j++)
                    index++;
            }
        }

        if (total_column == 3) {
            int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0))); //Integer.parseInt(String.valueOf(nc.charAt(0))): the number of empty slot in a stack
            int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1))); // already_have1: the number of containers stacking on the stack.
            int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
            int column_state_size1 = column_state(already_have1, gn).length;
            int column_state_size2 = column_state(already_have2, gn).length;
            int column_state_size3 = column_state(already_have3, gn).length;
            for (int i = 0; i < column_state_size1; i++) {
                int start_position2;
                if (already_have2 == already_have1)
                    start_position2 = i;
                else
                    start_position2 = 0;
                for (int ii = start_position2; ii < column_state_size2; ii++) {
                    int start_position3;
                    if (already_have3 == already_have2)
                        start_position3 = ii;
                    else
                        start_position3 = 0;
                    for (int j = start_position3; j < column_state_size3; j++)
                        index++;
                }
            }
        }

        if (total_column == 4) {
            int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0))); //Integer.parseInt(String.valueOf(nc.charAt(0))): the number of empty slot in a stack
            int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1))); // already_have1: the number of containers stacking on the stack.
            int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
            int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
            int column_state_size1 = column_state(already_have1, gn).length;
            int column_state_size2 = column_state(already_have2, gn).length;
            int column_state_size3 = column_state(already_have3, gn).length;
            int column_state_size4 = column_state(already_have4, gn).length;
            for (int i = 0; i < column_state_size1; i++) {
                int start_position2;
                if (already_have2 == already_have1)
                    start_position2 = i;
                else
                    start_position2 = 0;
                for (int ii = start_position2; ii < column_state_size2; ii++) {
                    int start_position3;
                    if (already_have3 == already_have2)
                        start_position3 = ii;
                    else
                        start_position3 = 0;
                    for (int j = start_position3; j < column_state_size3; j++) {
                        int start_position4;
                        if (already_have4 == already_have3)
                            start_position4 = j;
                        else
                            start_position4 = 0;
                        for (int jj = start_position4; jj < column_state_size4; jj++)
                            index++;
                    }
                }
            }
        }

        if (total_column == 5) {
            int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0))); //Integer.parseInt(String.valueOf(nc.charAt(0))): the number of empty slot in a stack
            int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1))); // already_have1: the number of containers stacking on the stack.
            int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
            int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
            int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
            int column_state_size1 = column_state(already_have1, gn).length;
            int column_state_size2 = column_state(already_have2, gn).length;
            int column_state_size3 = column_state(already_have3, gn).length;
            int column_state_size4 = column_state(already_have4, gn).length;
            int column_state_size5 = column_state(already_have5, gn).length;
            for (int i = 0; i < column_state_size1; i++) {
                int start_position2;
                if (already_have2 == already_have1)
                    start_position2 = i;
                else
                    start_position2 = 0;
                for (int ii = start_position2; ii < column_state_size2; ii++) {
                    int start_position3;
                    if (already_have3 == already_have2)
                        start_position3 = ii;
                    else
                        start_position3 = 0;
                    for (int j = start_position3; j < column_state_size3; j++) {
                        int start_position4;
                        if (already_have4 == already_have3)
                            start_position4 = j;
                        else
                            start_position4 = 0;
                        for (int jj = start_position4; jj < column_state_size4; jj++) {
                            int start_position5;
                            if (already_have5 == already_have4)
                                start_position5 = jj;
                            else
                                start_position5 = 0;
                            for (int k = start_position5; k < column_state_size5; k++)
                                index++;
                        }
                    }
                }
            }
        }

        if (total_column == 6) {
            int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0))); //Integer.parseInt(String.valueOf(nc.charAt(0))): the number of empty slot in a stack
            int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1))); // already_have1: the number of containers stacking on the stack.
            int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
            int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
            int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
            int already_have6 = t - Integer.parseInt(String.valueOf(nc.charAt(5)));
            int column_state_size1 = column_state(already_have1, gn).length;
            int column_state_size2 = column_state(already_have2, gn).length;
            int column_state_size3 = column_state(already_have3, gn).length;
            int column_state_size4 = column_state(already_have4, gn).length;
            int column_state_size5 = column_state(already_have5, gn).length;
            int column_state_size6 = column_state(already_have6, gn).length;
            for (int i = 0; i < column_state_size1; i++) {
                int start_position2;
                if (already_have2 == already_have1)
                    start_position2 = i;
                else
                    start_position2 = 0;
                for (int ii = start_position2; ii < column_state_size2; ii++) {
                    int start_position3;
                    if (already_have3 == already_have2)
                        start_position3 = ii;
                    else
                        start_position3 = 0;
                    for (int j = start_position3; j < column_state_size3; j++) {
                        int start_position4;
                        if (already_have4 == already_have3)
                            start_position4 = j;
                        else
                            start_position4 = 0;
                        for (int jj = start_position4; jj < column_state_size4; jj++) {
                            int start_position5;
                            if (already_have5 == already_have4)
                                start_position5 = jj;
                            else
                                start_position5 = 0;
                            for (int k = start_position5; k < column_state_size5; k++) {
                                int start_position6;
                                if (already_have6 == already_have5)
                                    start_position6 = k;
                                else
                                    start_position6 = 0;
                                for (int kk = start_position6; kk < column_state_size6; kk++)
                                    index++;
                            }
                        }
                    }
                }
            }
        }

        if (total_column == 7) {
            int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0))); //Integer.parseInt(String.valueOf(nc.charAt(0))): the number of empty slot in a stack
            int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1))); // already_have1: the number of containers stacking on the stack.
            int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
            int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
            int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
            int already_have6 = t - Integer.parseInt(String.valueOf(nc.charAt(5)));
            int already_have7 = t - Integer.parseInt(String.valueOf(nc.charAt(6)));
            int column_state_size1 = column_state(already_have1, gn).length;
            int column_state_size2 = column_state(already_have2, gn).length;
            int column_state_size3 = column_state(already_have3, gn).length;
            int column_state_size4 = column_state(already_have4, gn).length;
            int column_state_size5 = column_state(already_have5, gn).length;
            int column_state_size6 = column_state(already_have6, gn).length;
            int column_state_size7 = column_state(already_have7, gn).length;
            for (int i = 0; i < column_state_size1; i++) {
                int start_position2;
                if (already_have2 == already_have1)
                    start_position2 = i;
                else
                    start_position2 = 0;
                for (int ii = start_position2; ii < column_state_size2; ii++) {
                    int start_position3;
                    if (already_have3 == already_have2)
                        start_position3 = ii;
                    else
                        start_position3 = 0;
                    for (int j = start_position3; j < column_state_size3; j++) {
                        int start_position4;
                        if (already_have4 == already_have3)
                            start_position4 = j;
                        else
                            start_position4 = 0;
                        for (int jj = start_position4; jj < column_state_size4; jj++) {
                            int start_position5;
                            if (already_have5 == already_have4)
                                start_position5 = jj;
                            else
                                start_position5 = 0;
                            for (int k = start_position5; k < column_state_size5; k++) {
                                int start_position6;
                                if (already_have6 == already_have5)
                                    start_position6 = k;
                                else
                                    start_position6 = 0;
                                for (int kk = start_position6; kk < column_state_size6; kk++) {
                                    int start_position7;
                                    if (already_have7 == already_have6)
                                        start_position7 = kk;
                                    else
                                        start_position7 = 0;
                                    for (int m = start_position7; m < column_state_size7; m++)
                                        index++;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (total_column == 8) {
            int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0))); //Integer.parseInt(String.valueOf(nc.charAt(0))): the number of empty slot in a stack
            int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1))); // already_have1: the number of containers stacking on the stack.
            int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
            int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
            int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
            int already_have6 = t - Integer.parseInt(String.valueOf(nc.charAt(5)));
            int already_have7 = t - Integer.parseInt(String.valueOf(nc.charAt(6)));
            int already_have8 = t - Integer.parseInt(String.valueOf(nc.charAt(7)));
            int column_state_size1 = column_state(already_have1, gn).length;
            int column_state_size2 = column_state(already_have2, gn).length;
            int column_state_size3 = column_state(already_have3, gn).length;
            int column_state_size4 = column_state(already_have4, gn).length;
            int column_state_size5 = column_state(already_have5, gn).length;
            int column_state_size6 = column_state(already_have6, gn).length;
            int column_state_size7 = column_state(already_have7, gn).length;
            int column_state_size8 = column_state(already_have8, gn).length;
            for (int i = 0; i < column_state_size1; i++) {
                int start_position2;
                if (already_have2 == already_have1)
                    start_position2 = i;
                else
                    start_position2 = 0;
                for (int ii = start_position2; ii < column_state_size2; ii++) {
                    int start_position3;
                    if (already_have3 == already_have2)
                        start_position3 = ii;
                    else
                        start_position3 = 0;
                    for (int j = start_position3; j < column_state_size3; j++) {
                        int start_position4;
                        if (already_have4 == already_have3)
                            start_position4 = j;
                        else
                            start_position4 = 0;
                        for (int jj = start_position4; jj < column_state_size4; jj++) {
                            int start_position5;
                            if (already_have5 == already_have4)
                                start_position5 = jj;
                            else
                                start_position5 = 0;
                            for (int k = start_position5; k < column_state_size5; k++) {
                                int start_position6;
                                if (already_have6 == already_have5)
                                    start_position6 = k;
                                else
                                    start_position6 = 0;
                                for (int kk = start_position6; kk < column_state_size6; kk++) {
                                    int start_position7;
                                    if (already_have7 == already_have6)
                                        start_position7 = kk;
                                    else
                                        start_position7 = 0;
                                    for (int m = start_position7; m < column_state_size7; m++) {
                                        int start_position8;
                                        if (already_have8 == already_have7)
                                            start_position8 = m;
                                        else
                                            start_position8 = 0;
                                        for (int mm = start_position8; mm < column_state_size8; mm++)
                                            index++;
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

    // 濮濄倖鏌熷▔鏇炲灟娑撴槒顩﹂弰顖濆箯閸欐牜娈戦崗铚傜秼閻ㄥ嫬鐖㈤崹娑氬Ц閹拷
    int[][] permutation(String nc, int t, int[] gn) {//閽堝涓�涓猲c
        int total_column = 0;  //calculate the number of stacks on which there are empty slots
        for (int i = 0; i < nc.length(); i++) {
            //System.out.println(nc.charAt(i));
            if (nc.charAt(i) > '0')
                total_column++;
        }

//        int permutation_size = permutation_size(nc, t, gn);


        int[] columnall = column_state(t, gn);
        ArrayList value = new ArrayList();
        switch (nc.length()) {
            case 3:
                if (total_column == 1) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int[] column1;
                    column1 = column_state(already_have1, gn);
                    for (int i = 0; i < column1.length; i++) {
                        int start_position2 = 0;
                        for (int j = start_position2; j < columnall.length; j++) {
                            int start_position3 = j;
                            for (int k = start_position3; k < columnall.length; k++) {
                                value.add(column1[i]);
                                value.add(columnall[j]);
                                value.add(columnall[k]);
                            }
                        }
                    }
                }

                if (total_column == 2) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3 = 0;
                            for (int jj = start_position3; jj < columnall.length; jj++) {
                                value.add(column1[i]);
                                value.add(column2[j]);
                                value.add(columnall[jj]);
                            }
                        }
                    }
                }

                if (total_column == 3) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                value.add(column1[i]);
                                value.add(column2[ii]);
                                value.add(column3[j]);
                            }
                        }
                    }
                }
                break;
            case 4:
                if (total_column == 1) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int[] column1;
                    column1 = column_state(already_have1, gn);
                    for (int i = 0; i < column1.length; i++) {
                        int start_position2 = 0;
                        for (int j = start_position2; j < columnall.length; j++) {
                            int start_position3 = j;
                            for (int k = start_position3; k < columnall.length; k++) {
                                int start_position4 = k;
                                for (int ii = start_position4; ii < columnall.length; ii++) {
                                    value.add(column1[i]);
                                    value.add(columnall[j]);
                                    value.add(columnall[k]);
                                    value.add(columnall[ii]);
                                }
                            }
                        }
                    }
                }

                if (total_column == 2) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3 = 0;
                            for (int k = start_position3; k < columnall.length; k++) {
                                int start_positon4 = k;
                                for (int ii = start_positon4; ii < columnall.length; ii++) {
                                    value.add(column1[i]);
                                    value.add(column2[j]);
                                    value.add(columnall[k]);
                                    value.add(columnall[ii]);
                                }
                            }
                        }
                    }
                }

                if (total_column == 3) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = j;
                            else
                                start_position3 = 0;
                            for (int k = start_position3; k < column_state_size3; k++) {
                                int start_positon4 = 0;
                                for (int ii = start_positon4; ii < columnall.length; ii++) {
                                    value.add(column1[i]);
                                    value.add(column2[j]);
                                    value.add(column3[k]);
                                    value.add(columnall[ii]);
                                }
                            }
                        }
                    }
                }

                if (total_column == 4) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    value.add(column1[i]);
                                    value.add(column2[ii]);
                                    value.add(column3[j]);
                                    value.add(column4[jj]);
                                }
                            }
                        }
                    }
                }
                break;
            case 5:
                if (total_column == 1) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int[] column1;
                    column1 = column_state(already_have1, gn);
                    for (int i = 0; i < column1.length; i++) {
                        int start_position2 = 0;
                        for (int j = start_position2; j < columnall.length; j++) {
                            int start_position3 = j;
                            for (int k = start_position3; k < columnall.length; k++) {
                                int start_position4 = k;
                                for (int ii = start_position4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        value.add(column1[i]);
                                        value.add(columnall[j]);
                                        value.add(columnall[k]);
                                        value.add(columnall[ii]);
                                        value.add(columnall[jj]);
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 2) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3 = 0;
                            for (int k = start_position3; k < columnall.length; k++) {
                                int start_positon4 = k;
                                for (int ii = start_positon4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        value.add(column1[i]);
                                        value.add(column2[j]);
                                        value.add(columnall[k]);
                                        value.add(columnall[ii]);
                                        value.add(columnall[jj]);
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 3) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = j;
                            else
                                start_position3 = 0;
                            for (int k = start_position3; k < column_state_size3; k++) {
                                int start_positon4 = 0;
                                for (int ii = start_positon4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        value.add(column1[i]);
                                        value.add(column2[j]);
                                        value.add(column3[k]);
                                        value.add(columnall[ii]);
                                        value.add(columnall[jj]);
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 4) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = j;
                            else
                                start_position3 = 0;
                            for (int k = start_position3; k < column_state_size3; k++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = k;
                                else
                                    start_position4 = 0;
                                for (int ii = start_position4; ii < column_state_size4; ii++) {
                                    int start_position5 = 0;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        value.add(column1[i]);
                                        value.add(column2[j]);
                                        value.add(column3[k]);
                                        value.add(column4[ii]);
                                        value.add(columnall[jj]);
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 5) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int[] column5 = column_state(already_have5, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    int column_state_size5 = column5.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    int start_position5;
                                    if (already_have5 == already_have4)
                                        start_position5 = jj;
                                    else
                                        start_position5 = 0;
                                    for (int k = start_position5; k < column_state_size5; k++) {
                                        value.add(column1[i]);
                                        value.add(column2[ii]);
                                        value.add(column3[j]);
                                        value.add(column4[jj]);
                                        value.add(column5[k]);

                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case 6:
                if (total_column == 1) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int[] column1;
                    column1 = column_state(already_have1, gn);
                    for (int i = 0; i < column1.length; i++) {
                        int start_position2 = 0;
                        for (int j = start_position2; j < columnall.length; j++) {
                            int start_position3 = j;
                            for (int k = start_position3; k < columnall.length; k++) {
                                int start_position4 = k;
                                for (int ii = start_position4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            value.add(column1[i]);
                                            value.add(columnall[j]);
                                            value.add(columnall[k]);
                                            value.add(columnall[ii]);
                                            value.add(columnall[jj]);
                                            value.add(columnall[kk]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 2) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3 = 0;
                            for (int k = start_position3; k < columnall.length; k++) {
                                int start_positon4 = k;
                                for (int ii = start_positon4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            value.add(column1[i]);
                                            value.add(column2[j]);
                                            value.add(columnall[k]);
                                            value.add(columnall[ii]);
                                            value.add(columnall[jj]);
                                            value.add(columnall[kk]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 3) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = j;
                            else
                                start_position3 = 0;
                            for (int k = start_position3; k < column_state_size3; k++) {
                                int start_positon4 = 0;
                                for (int ii = start_positon4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            value.add(column1[i]);
                                            value.add(column2[j]);
                                            value.add(column3[k]);
                                            value.add(columnall[ii]);
                                            value.add(columnall[jj]);
                                            value.add(columnall[kk]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 4) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = j;
                            else
                                start_position3 = 0;
                            for (int k = start_position3; k < column_state_size3; k++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = k;
                                else
                                    start_position4 = 0;
                                for (int ii = start_position4; ii < column_state_size4; ii++) {
                                    int start_position5 = 0;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            value.add(column1[i]);
                                            value.add(column2[j]);
                                            value.add(column3[k]);
                                            value.add(column4[ii]);
                                            value.add(columnall[jj]);
                                            value.add(columnall[kk]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 5) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int[] column5 = column_state(already_have5, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    int column_state_size5 = column5.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    int start_position5;
                                    if (already_have5 == already_have4)
                                        start_position5 = jj;
                                    else
                                        start_position5 = 0;
                                    for (int k = start_position5; k < column_state_size5; k++) {
                                        int start_position6 = 0;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            value.add(column1[i]);
                                            value.add(column2[ii]);
                                            value.add(column3[j]);
                                            value.add(column4[jj]);
                                            value.add(column5[k]);
                                            value.add(columnall[kk]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 6) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0))); // Integer.parseInt(String.valueOf(nc.charAt(0))):
                    // the number of empty slot in a
                    // stack
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1))); // already_have1: the number of
                    // containers stacking on the stack.
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
                    int already_have6 = t - Integer.parseInt(String.valueOf(nc.charAt(5)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int[] column5 = column_state(already_have5, gn);
                    int[] column6 = column_state(already_have6, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    int column_state_size5 = column5.length;
                    int column_state_size6 = column6.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    int start_position5;
                                    if (already_have5 == already_have4)
                                        start_position5 = jj;
                                    else
                                        start_position5 = 0;
                                    for (int k = start_position5; k < column_state_size5; k++) {
                                        int start_position6;
                                        if (already_have6 == already_have5)
                                            start_position6 = k;
                                        else
                                            start_position6 = 0;
                                        for (int kk = start_position6; kk < column_state_size6; kk++) {
                                            value.add(column1[i]);
                                            value.add(column2[ii]);
                                            value.add(column3[j]);
                                            value.add(column4[jj]);
                                            value.add(column5[k]);
                                            value.add(column6[kk]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case 7:
                if (total_column == 1) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int[] column1;
                    column1 = column_state(already_have1, gn);
                    for (int i = 0; i < column1.length; i++) {
                        int start_position2 = 0;
                        for (int j = start_position2; j < columnall.length; j++) {
                            int start_position3 = j;
                            for (int k = start_position3; k < columnall.length; k++) {
                                int start_position4 = k;
                                for (int ii = start_position4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            int start_position7 = kk;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                value.add(column1[i]);
                                                value.add(columnall[j]);
                                                value.add(columnall[k]);
                                                value.add(columnall[ii]);
                                                value.add(columnall[jj]);
                                                value.add(columnall[kk]);
                                                value.add(columnall[pp]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 2) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3 = 0;
                            for (int k = start_position3; k < columnall.length; k++) {
                                int start_positon4 = k;
                                for (int ii = start_positon4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            int start_position7 = kk;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                value.add(column1[i]);
                                                value.add(column2[j]);
                                                value.add(columnall[k]);
                                                value.add(columnall[ii]);
                                                value.add(columnall[jj]);
                                                value.add(columnall[kk]);
                                                value.add(columnall[pp]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 3) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = j;
                            else
                                start_position3 = 0;
                            for (int k = start_position3; k < column_state_size3; k++) {
                                int start_positon4 = 0;
                                for (int ii = start_positon4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            int start_position7 = kk;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                value.add(column1[i]);
                                                value.add(column2[j]);
                                                value.add(column3[k]);
                                                value.add(columnall[ii]);
                                                value.add(columnall[jj]);
                                                value.add(columnall[kk]);
                                                value.add(columnall[pp]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 4) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = j;
                            else
                                start_position3 = 0;
                            for (int k = start_position3; k < column_state_size3; k++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = k;
                                else
                                    start_position4 = 0;
                                for (int ii = start_position4; ii < column_state_size4; ii++) {
                                    int start_position5 = 0;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            int start_position7 = kk;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                value.add(column1[i]);
                                                value.add(column2[j]);
                                                value.add(column3[k]);
                                                value.add(column4[ii]);
                                                value.add(columnall[jj]);
                                                value.add(columnall[kk]);
                                                value.add(columnall[pp]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 5) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int[] column5 = column_state(already_have5, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    int column_state_size5 = column5.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    int start_position5;
                                    if (already_have5 == already_have4)
                                        start_position5 = jj;
                                    else
                                        start_position5 = 0;
                                    for (int k = start_position5; k < column_state_size5; k++) {
                                        int start_position6 = 0;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            int start_position7 = kk;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                value.add(column1[i]);
                                                value.add(column2[ii]);
                                                value.add(column3[j]);
                                                value.add(column4[jj]);
                                                value.add(column5[k]);
                                                value.add(columnall[kk]);
                                                value.add(columnall[pp]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 6) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0))); // Integer.parseInt(String.valueOf(nc.charAt(0))):
                    // the number of empty slot in a
                    // stack
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1))); // already_have1: the number of
                    // containers stacking on the stack.
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
                    int already_have6 = t - Integer.parseInt(String.valueOf(nc.charAt(5)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int[] column5 = column_state(already_have5, gn);
                    int[] column6 = column_state(already_have6, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    int column_state_size5 = column5.length;
                    int column_state_size6 = column6.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    int start_position5;
                                    if (already_have5 == already_have4)
                                        start_position5 = jj;
                                    else
                                        start_position5 = 0;
                                    for (int k = start_position5; k < column_state_size5; k++) {
                                        int start_position6;
                                        if (already_have6 == already_have5)
                                            start_position6 = k;
                                        else
                                            start_position6 = 0;
                                        for (int kk = start_position6; kk < column_state_size6; kk++) {
                                            int start_position7 = 0;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                value.add(column1[i]);
                                                value.add(column2[ii]);
                                                value.add(column3[j]);
                                                value.add(column4[jj]);
                                                value.add(column5[k]);
                                                value.add(column6[kk]);
                                                value.add(columnall[pp]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 7) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
                    int already_have6 = t - Integer.parseInt(String.valueOf(nc.charAt(5)));
                    int already_have7 = t - Integer.parseInt(String.valueOf(nc.charAt(6)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int[] column5 = column_state(already_have5, gn);
                    int[] column6 = column_state(already_have6, gn);
                    int[] column7 = column_state(already_have7, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    int column_state_size5 = column5.length;
                    int column_state_size6 = column6.length;
                    int column_state_size7 = column7.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    int start_position5;
                                    if (already_have5 == already_have4)
                                        start_position5 = jj;
                                    else
                                        start_position5 = 0;
                                    for (int k = start_position5; k < column_state_size5; k++) {
                                        int start_position6;
                                        if (already_have6 == already_have5)
                                            start_position6 = k;
                                        else
                                            start_position6 = 0;
                                        for (int kk = start_position6; kk < column_state_size6; kk++) {
                                            int start_position7;
                                            if (already_have7 == already_have6)
                                                start_position7 = kk;
                                            else
                                                start_position7 = 0;
                                            for (int m = start_position7; m < column_state_size7; m++) {
                                                value.add(column1[i]);
                                                value.add(column2[ii]);
                                                value.add(column3[j]);
                                                value.add(column4[jj]);
                                                value.add(column5[k]);
                                                value.add(column6[kk]);
                                                value.add(column7[m]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case 8:
                if (total_column == 1) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int[] column1;
                    column1 = column_state(already_have1, gn);
                    for (int i = 0; i < column1.length; i++) {
                        int start_position2 = 0;
                        for (int j = start_position2; j < columnall.length; j++) {
                            int start_position3 = j;
                            for (int k = start_position3; k < columnall.length; k++) {
                                int start_position4 = k;
                                for (int ii = start_position4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            int start_position7 = kk;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                int start_position8 = pp;
                                                for (int ll = start_position8; ll < columnall.length; ll++) {
                                                    value.add(column1[i]);
                                                    value.add(columnall[j]);
                                                    value.add(columnall[k]);
                                                    value.add(columnall[ii]);
                                                    value.add(columnall[jj]);
                                                    value.add(columnall[kk]);
                                                    value.add(columnall[pp]);
                                                    value.add(columnall[ll]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 2) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3 = 0;
                            for (int k = start_position3; k < columnall.length; k++) {
                                int start_positon4 = k;
                                for (int ii = start_positon4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            int start_position7 = kk;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                int start_position8 = pp;
                                                for (int ll = start_position8; ll < columnall.length; ll++) {
                                                    value.add(column1[i]);
                                                    value.add(columnall[j]);
                                                    value.add(columnall[k]);
                                                    value.add(columnall[ii]);
                                                    value.add(columnall[jj]);
                                                    value.add(columnall[kk]);
                                                    value.add(columnall[pp]);
                                                    value.add(columnall[ll]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 3) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = j;
                            else
                                start_position3 = 0;
                            for (int k = start_position3; k < column_state_size3; k++) {
                                int start_positon4 = 0;
                                for (int ii = start_positon4; ii < columnall.length; ii++) {
                                    int start_position5 = ii;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            int start_position7 = kk;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                int start_position8 = pp;
                                                for (int ll = start_position8; ll < columnall.length; ll++) {
                                                    value.add(column1[i]);
                                                    value.add(columnall[j]);
                                                    value.add(columnall[k]);
                                                    value.add(columnall[ii]);
                                                    value.add(columnall[jj]);
                                                    value.add(columnall[kk]);
                                                    value.add(columnall[pp]);
                                                    value.add(columnall[ll]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 4) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int j = start_position2; j < column_state_size2; j++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = j;
                            else
                                start_position3 = 0;
                            for (int k = start_position3; k < column_state_size3; k++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = k;
                                else
                                    start_position4 = 0;
                                for (int ii = start_position4; ii < column_state_size4; ii++) {
                                    int start_position5 = 0;
                                    for (int jj = start_position5; jj < columnall.length; jj++) {
                                        int start_position6 = jj;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            int start_position7 = kk;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                int start_position8 = pp;
                                                for (int ll = start_position8; ll < columnall.length; ll++) {
                                                    value.add(column1[i]);
                                                    value.add(columnall[j]);
                                                    value.add(columnall[k]);
                                                    value.add(columnall[ii]);
                                                    value.add(columnall[jj]);
                                                    value.add(columnall[kk]);
                                                    value.add(columnall[pp]);
                                                    value.add(columnall[ll]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 5) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int[] column5 = column_state(already_have5, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    int column_state_size5 = column5.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    int start_position5;
                                    if (already_have5 == already_have4)
                                        start_position5 = jj;
                                    else
                                        start_position5 = 0;
                                    for (int k = start_position5; k < column_state_size5; k++) {
                                        int start_position6 = 0;
                                        for (int kk = start_position6; kk < columnall.length; kk++) {
                                            int start_position7 = kk;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                int start_position8 = pp;
                                                for (int ll = start_position8; ll < columnall.length; ll++) {
                                                    value.add(column1[i]);
                                                    value.add(columnall[j]);
                                                    value.add(columnall[k]);
                                                    value.add(columnall[ii]);
                                                    value.add(columnall[jj]);
                                                    value.add(columnall[kk]);
                                                    value.add(columnall[pp]);
                                                    value.add(columnall[ll]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 6) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0))); // Integer.parseInt(String.valueOf(nc.charAt(0))):
                    // the number of empty slot in a
                    // stack
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1))); // already_have1: the number of
                    // containers stacking on the stack.
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
                    int already_have6 = t - Integer.parseInt(String.valueOf(nc.charAt(5)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int[] column5 = column_state(already_have5, gn);
                    int[] column6 = column_state(already_have6, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    int column_state_size5 = column5.length;
                    int column_state_size6 = column6.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    int start_position5;
                                    if (already_have5 == already_have4)
                                        start_position5 = jj;
                                    else
                                        start_position5 = 0;
                                    for (int k = start_position5; k < column_state_size5; k++) {
                                        int start_position6;
                                        if (already_have6 == already_have5)
                                            start_position6 = k;
                                        else
                                            start_position6 = 0;
                                        for (int kk = start_position6; kk < column_state_size6; kk++) {
                                            int start_position7 = 0;
                                            for (int pp = start_position7; pp < columnall.length; pp++) {
                                                int start_position8 = pp;
                                                for (int ll = start_position8; ll < columnall.length; ll++) {
                                                    value.add(column1[i]);
                                                    value.add(columnall[j]);
                                                    value.add(columnall[k]);
                                                    value.add(columnall[ii]);
                                                    value.add(columnall[jj]);
                                                    value.add(columnall[kk]);
                                                    value.add(columnall[pp]);
                                                    value.add(columnall[ll]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 7) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
                    int already_have6 = t - Integer.parseInt(String.valueOf(nc.charAt(5)));
                    int already_have7 = t - Integer.parseInt(String.valueOf(nc.charAt(6)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int[] column5 = column_state(already_have5, gn);
                    int[] column6 = column_state(already_have6, gn);
                    int[] column7 = column_state(already_have7, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    int column_state_size5 = column5.length;
                    int column_state_size6 = column6.length;
                    int column_state_size7 = column7.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    int start_position5;
                                    if (already_have5 == already_have4)
                                        start_position5 = jj;
                                    else
                                        start_position5 = 0;
                                    for (int k = start_position5; k < column_state_size5; k++) {
                                        int start_position6;
                                        if (already_have6 == already_have5)
                                            start_position6 = k;
                                        else
                                            start_position6 = 0;
                                        for (int kk = start_position6; kk < column_state_size6; kk++) {
                                            int start_position7;
                                            if (already_have7 == already_have6)
                                                start_position7 = kk;
                                            else
                                                start_position7 = 0;
                                            for (int m = start_position7; m < column_state_size7; m++) {
                                                int start_position8 = m;
                                                for (int ll = start_position8; ll < columnall.length; ll++) {
                                                    value.add(column1[i]);
                                                    value.add(columnall[j]);
                                                    value.add(columnall[k]);
                                                    value.add(columnall[ii]);
                                                    value.add(columnall[jj]);
                                                    value.add(columnall[kk]);
                                                    value.add(columnall[m]);
                                                    value.add(columnall[ll]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (total_column == 8) {
                    int already_have1 = t - Integer.parseInt(String.valueOf(nc.charAt(0)));
                    int already_have2 = t - Integer.parseInt(String.valueOf(nc.charAt(1)));
                    int already_have3 = t - Integer.parseInt(String.valueOf(nc.charAt(2)));
                    int already_have4 = t - Integer.parseInt(String.valueOf(nc.charAt(3)));
                    int already_have5 = t - Integer.parseInt(String.valueOf(nc.charAt(4)));
                    int already_have6 = t - Integer.parseInt(String.valueOf(nc.charAt(5)));
                    int already_have7 = t - Integer.parseInt(String.valueOf(nc.charAt(6)));
                    int already_have8 = t - Integer.parseInt(String.valueOf(nc.charAt(6)));
                    int[] column1 = column_state(already_have1, gn);
                    int[] column2 = column_state(already_have2, gn);
                    int[] column3 = column_state(already_have3, gn);
                    int[] column4 = column_state(already_have4, gn);
                    int[] column5 = column_state(already_have5, gn);
                    int[] column6 = column_state(already_have6, gn);
                    int[] column7 = column_state(already_have7, gn);
                    int[] column8 = column_state(already_have8, gn);
                    int column_state_size1 = column1.length;
                    int column_state_size2 = column2.length;
                    int column_state_size3 = column3.length;
                    int column_state_size4 = column4.length;
                    int column_state_size5 = column5.length;
                    int column_state_size6 = column6.length;
                    int column_state_size7 = column7.length;
                    int column_state_size8 = column8.length;
                    for (int i = 0; i < column_state_size1; i++) {
                        int start_position2;
                        if (already_have2 == already_have1)
                            start_position2 = i;
                        else
                            start_position2 = 0;
                        for (int ii = start_position2; ii < column_state_size2; ii++) {
                            int start_position3;
                            if (already_have3 == already_have2)
                                start_position3 = ii;
                            else
                                start_position3 = 0;
                            for (int j = start_position3; j < column_state_size3; j++) {
                                int start_position4;
                                if (already_have4 == already_have3)
                                    start_position4 = j;
                                else
                                    start_position4 = 0;
                                for (int jj = start_position4; jj < column_state_size4; jj++) {
                                    int start_position5;
                                    if (already_have5 == already_have4)
                                        start_position5 = jj;
                                    else
                                        start_position5 = 0;
                                    for (int k = start_position5; k < column_state_size5; k++) {
                                        int start_position6;
                                        if (already_have6 == already_have5)
                                            start_position6 = k;
                                        else
                                            start_position6 = 0;
                                        for (int kk = start_position6; kk < column_state_size6; kk++) {
                                            int start_position7;
                                            if (already_have7 == already_have6)
                                                start_position7 = kk;
                                            else
                                                start_position7 = 0;
                                            for (int m = start_position7; m < column_state_size7; m++) {
                                                int start_position8;
                                                if (already_have8 == already_have7)
                                                    start_position8 = m;
                                                else
                                                    start_position8 = 0;
                                                for (int ll = start_position8; ll < columnall.length; ll++) {
                                                    value.add(column1[i]);
                                                    value.add(columnall[j]);
                                                    value.add(columnall[k]);
                                                    value.add(columnall[ii]);
                                                    value.add(columnall[jj]);
                                                    value.add(columnall[kk]);
                                                    value.add(columnall[m]);
                                                    value.add(columnall[ll]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        int[][] sc = new int[value.size() / nc.length()][nc.length()];
        for (int i = 0; i < value.size() / nc.length(); i++) {
            for (int j = 0; j < nc.length(); j++) {
                sc[i][j] = (Integer) value.get(i * nc.length() + j);
            }
        }
        int num = 0;
        for (int i = 0; i < sc.length; i++) {
            if (calculate_ContainersType(nc, sc[i], gn, t)) {
                num++;
            }
        }
        int[][] sc_right = new int[num][];
        int index = 0;
        for (int i = 0; i < sc.length; i++) {
            if (calculate_ContainersType(nc, sc[i], gn, t)) {
                sc_right[index] = sc[i];
                index++;
            }
        }
        return sc_right;


        //      return sc;

    }

    int[] column_state(int column_number, int[] gn) { //stack height, type of containers

        ArrayList value = new ArrayList();
        // Vector value = new Vector();
        /*
         * int total_number =0; if(gn.length==2){ for(int i=column_number; i>=0; i--)
         * for(int ii=column_number; ii>=0; ii--){ if(i+ii==column_number)
         * total_number++; } } if(gn.length==3){ for(int i=column_number; i>=0; i--)
         * for(int ii=column_number; ii>=0; ii--) for(int j=column_number; j>=0; j--){
         * if(i+ii+j==column_number) total_number++; } } if(gn.length==4){ for(int
         * i=column_number; i>=0; i--) for(int ii=column_number; ii>=0; ii--) for(int
         * j=column_number; j>=0; j--) for(int jj=column_number; jj>=0; jj--){
         * if(i+ii+j+jj==column_number) total_number++; } }
         *
         * int[] result = new int[total_number]; int index=0;
         */
        if (column_number == 0) {
            value.add(0);
        } else {
            if (gn.length == 2) {// there are types of containers,H and L
                for (int i = 0; i < gn.length - 1; i++) {
                    for (int j = column_number; j >= 1; j--) {
                        value.add(j * gn[i]);
                    }
                }
                value.add(column_number * gn[1]);


            }
            if (gn.length == 3) {
                for (int i = 0; i < gn.length - 1; i++) {
                    for (int j = column_number; j >= 1; j--) {
                        value.add(j * gn[i]);
                    }
                }
                value.add(column_number * gn[2]);

            }
            if (gn.length == 4) {
                for (int i = 0; i < gn.length - 1; i++) {
                    for (int j = column_number; j >= 1; j--) {
                        value.add(j * gn[i]);
                    }
                }
                value.add(column_number * gn[3]);

            }
        }

        int[] result = new int[value.size()];
        for (int i = 0; i < value.size(); i++)
            result[i] = (Integer) value.get(i);

        return result;

    }

    void dynamic_programming(int s, int t, int[] gn, double[] ratio) {
        // s: the number of stacks
        // t: the number of tiers
        // g: the number of weight groups
        int n = 0;
        String[] nc_pre;
        int[][] sc_pre;
        double[] va_pre;
        int tn_pre;
        // String[] nc_cur;
        //   int[][] sc_cur;
        //   double[] va_cur;
        //  int tn_cur;

        tn_pre = 1;//shangyige dimension size
        nc_pre = new String[tn_pre];
        sc_pre = new int[tn_pre][s];
        va_pre = new double[tn_pre];

        String temp = "";
        for (int j = 0; j < s; j++)
            temp = temp + "0";
        nc_pre[0] = temp;
        for (int j = 0; j < s; j++)
            sc_pre[0][j] = t;
        va_pre[0] = 0.0;
        int state_size = 0;
        while (n < s * t) {
            n = n + 1;
            //the total dimensiion for the case when the number of empty slots equals n
            String[] nc = solution_space(s, t, n);
            ArrayList values = new ArrayList();
            for (int i = 0; i < nc.length; i++) {
                int[][] sc = permutation(nc[i], t, gn);
                for (int j = 0; j < sc.length; j++) {
                    values.add(nc[i]);
                    for (int k = 0; k < sc[j].length; k++)
                        values.add(sc[j][k]);
                    double finalvalue = calculate_value(t, nc[i], sc[j], gn, nc_pre, sc_pre, va_pre, ratio);
                    values.add(finalvalue);

                    if (n == s * t)
                        final_value = finalvalue;
                }
            }


            tn_pre = values.size() / (s + 2);
            state_size = state_size + tn_pre;
            nc_pre = new String[tn_pre];
            sc_pre = new int[tn_pre][s];
            va_pre = new double[tn_pre];
            for (int i = 0; i < tn_pre; i++) {
                nc_pre[i] = (String) values.get(i * (s + 2));
                for (int k = 1; k <= s; k++)
                    sc_pre[i][k - 1] = (Integer) values.get(i * (s + 2) + k);
                va_pre[i] = (Double) values.get(i * (s + 2) + s + 1);
            }

            for (int i = 0; i < nc_pre.length; i++) {
                System.out.println(i + "  " + nc_pre[i] + "  " + sc_pre[i][0] + " " + sc_pre[i][1] + " " + sc_pre[i][2] + " " + sc_pre[i][3] + " " + va_pre[i]);
            }
        }


    }

    void dynamic_programming(int s, int t, int[] gn, double[] ratio, boolean input) {
        // s: the number of stacks
        // t: the number of tiers
        // g: the number of weight groups
        int n = 0;
        String[] nc_pre;
        int[][] sc_pre;
        double[] va_pre;
        int tn_pre;
        //       String[] nc_cur;
        //       String[] sc_cur;
        //       double[] va_cur;
        //       int tn_cur;

        tn_pre = 1;
        nc_pre = new String[tn_pre];
        sc_pre = new int[tn_pre][s];
        va_pre = new double[tn_pre];

        String temp = "";
        for (int j = 0; j < s; j++)
            temp = temp + "0";
        nc_pre[0] = temp;
        for (int j = 0; j < s; j++)
            sc_pre[0][j] = t;
        va_pre[0] = 0.0;
        int state_size = 0;


        try {
            FileWriter fw = new FileWriter("/Users/jacqueline/Code/ContainerStacking/data/dynamic result/EIP_" + String.valueOf(s) + String.valueOf(t) + "_" + gn.length + ".txt");
            PrintWriter write = new PrintWriter(fw);
            while (n <= s * t) { //s * t
                n = n + 1;
                //the total dimensiion for the case when the number of empty slots equals n
                String[] nc = solution_space(s, t, n);
                ArrayList values = new ArrayList();
                for (int i = 0; i < nc.length; i++) {
                    int[][] sc = permutation(nc[i], t, gn);//那些L个数>8的被排除
                    for (int j = 0; j < sc.length; j++) {
                        values.add(nc[i]);
                        for (int k = 0; k < sc[j].length; k++)
                            values.add(sc[j][k]);
                        double finalvalue = calculate_value(t, nc[i], sc[j], gn, nc_pre, sc_pre, va_pre, ratio);
                        values.add(finalvalue);
                        if (n == s * t)
                            final_value = finalvalue;
                        write.print(nc[i] + "\t");
                        for (int m = 0; m < s; m++)
                            write.print(sc[j][m] + "\t");
                        write.print(finalvalue + "\t");
                        for (int k = 0; k < gn.length; k++) {
                            write.print(next_weight_group[k] + "\t" + next_state_number[k] + "\t");
                            for (int m = 0; m < s; m++)
                                write.print(next_state_weight[k][m] + "\t");
                            write.print(next_value[k] + "\t");
                            write.print(tier_no[k] + "\t");
                        }
                        write.println();
                    }
                }
                tn_pre = values.size() / (s + 2);
                state_size = state_size + tn_pre;
                nc_pre = new String[tn_pre];
                sc_pre = new int[tn_pre][s];
                va_pre = new double[tn_pre];
                for (int i = 0; i < tn_pre; i++) {
                    nc_pre[i] = (String) values.get(i * (s + 2));
                    for (int k = 1; k <= s; k++)
                        sc_pre[i][k - 1] = (Integer) values.get(i * (s + 2) + k);
                    va_pre[i] = (Double) values.get(i * (s + 2) + s + 1);
                }
            }
            fw.close();
            write.close();
        } catch (IOException f) {
            dialogbox dialog = new dialogbox();
            dialog.dialogbox("error", f.toString());
        }
    }

    public static int[] calculate_Containers(String nc, int[] sc, int[] gn, int t) {
        int[] result = new int[gn.length];
        if (gn.length == 2) {
            double[] ratio = new double[]{0.5, 0.5};
            for (int i = 0; i < sc.length; i++) {
                result[1] += sc[i] % 10;//L的个数
                if (sc[i] >= 10 && sc[i] / 10 + Integer.parseInt(String.valueOf(nc.charAt(i))) < t)
                    result[1] += t - sc[i] / 10 - Integer.parseInt(String.valueOf(nc.charAt(i)));//现有的L个数

            }
        }
        if (gn.length == 3) {
            double[] ratio = new double[]{(double) 1 / 3, (double) 1 / 3, (double) 1 / 3};
            result[0] = 0;//记录H的个数
            result[1] = 0;//记录M与L共同的个数
            for (int i = 0; i < sc.length; i++) {
                if (sc[i] >= 100) {
                    result[0]++;
                    if (sc[i] / 100 + Integer.parseInt(String.valueOf(nc.charAt(i))) < t)
                        result[1] += t - sc[i] / 100 - Integer.parseInt(String.valueOf(nc.charAt(i)));
                } else if (sc[i] >= 10) {
                    result[1] += t - Integer.parseInt(String.valueOf(nc.charAt(i)));
                    if (sc[i] / 10 + Integer.parseInt(String.valueOf(nc.charAt(i))) < t)
                        result[2] += t - sc[i] / 10 - Integer.parseInt(String.valueOf(nc.charAt(i)));

                } else {
                    result[2] += sc[i] % 10;
                    result[1] += sc[i] % 10;
                }
            }
            if (result[1] >= sc.length * t * (ratio[1] + ratio[2])) {
                result[2] = sc.length * t;
            } else {
                result[1] = 0;
            }
        }
        return result;
    }

    public static double[] calculate_Containers_ratio(String nc, int[] sc, int[] gn, int t) {
        double[] result = new double[gn.length];
        int[] num = new int[gn.length];//记录现有的HML个数
        double[] ratio_adjust = new double[gn.length];
        int empty_containers = 0;
        for (int i = 0; i < nc.length(); i++) {
            empty_containers += Integer.parseInt(String.valueOf(nc.charAt(i)));

        }
//		System.out.println("empty_containers:"+empty_containers);
        for (int i = 0; i < gn.length; i++) {
            result[i] = 0;
            num[i] = 0;
        }

        if (gn.length == 2) {
            double[] ratio = {0.5, 0.5};

            for (int i = 0; i < sc.length; i++) {
                if (sc[i] >= 10) {
                    num[0]++; //现有的H个数

                } else {
                    num[1] += sc[i];
                }
                if (sc[i] >= 10 && sc[i] / 10 + Integer.parseInt(String.valueOf(nc.charAt(i))) < t)
                    num[1] += t - sc[i] / 10 - Integer.parseInt(String.valueOf(nc.charAt(i)));//现有的L个数
                //  num[1]++;
            }

            if (num[1] >= Math.ceil(sc.length * t * ratio[1])) {
                result[0] = empty_containers;
                result[1] = 0;

            } else {
                result[0] = 0.5 * empty_containers;
                result[1] = 0.5 * empty_containers;
            }
//			System.out.println("result[0]:"+result[0]);
//			System.out.println("result[1]:"+result[1]);
            for (int i = 0; i < gn.length; i++) {
                ratio_adjust[i] = result[i] / empty_containers;
//				System.out.println("ratio_adjust:"+ratio_adjust[i]);
            }
        }
        if (gn.length == 3) {
            double[] ratio = {(double) 1 / 3, (double) 1 / 3, (double) 1 / 3};

            for (int i = 0; i < sc.length; i++) {
                if (sc[i] >= 100) {
                    num[0]++;
                    if (sc[i] / 100 + Integer.parseInt(String.valueOf(nc.charAt(i))) < t)
                        num[1] += t - sc[i] / 100 - Integer.parseInt(String.valueOf(nc.charAt(i)));
                } else if (sc[i] >= 10) {
                    num[1] += t - Integer.parseInt(String.valueOf(nc.charAt(i)));
                    if (sc[i] / 10 + Integer.parseInt(String.valueOf(nc.charAt(i))) < t)
                        num[2] += t - sc[i] / 10 - Integer.parseInt(String.valueOf(nc.charAt(i)));

                } else {
                    num[2] += sc[i];
                    num[1] += sc[i];
                }
            }
            if (num[0] >= Math.ceil(sc.length * t * ratio[0])) {
                result[0] = 0;
                result[1] = 0.5 * empty_containers;
                result[2] = 0.5 * empty_containers;
                if (num[2] >= Math.ceil(sc.length * t * ratio[2])) {
                    result[2] = 0;
                }
                result[1] = empty_containers - result[0] - result[1];
            } else {
                if (num[1] >= sc.length * t * (ratio[1] + ratio[2])) {
                    result[2] = 0;
                    result[1] = 0;
                    result[0] = empty_containers;
                } else {
                    if (num[2] >= Math.ceil(sc.length * t * ratio[2])) {
                        result[2] = 0;
                        result[1] = 0.5 * empty_containers;
                        result[0] = 0.5 * empty_containers;
                    } else {
                        result[0] = ratio[0] * empty_containers;
                        result[1] = ratio[1] * empty_containers;
                        result[2] = ratio[2] * empty_containers;
                    }
                }
            }

//				System.out.println("result[0]:"+result[0]);
//				System.out.println("result[1]:"+result[1]);
//				System.out.println("result[2]:"+result[2]);
            for (int i = 0; i < gn.length; i++) {
                ratio_adjust[i] = result[i] / empty_containers;
//					System.out.println("ratio_adjust:"+ratio_adjust[i]);
            }
        }

        return ratio_adjust;
    }
    //  public static double[] calculate_Containers_ratio3(String nc, int[] sc, int[] gn,int t) {

    //  }
    public static double[] calculate_Containers_ratio2(String nc, int[] sc, int[] gn, int t) {
        double[] result = new double[gn.length];
        double[] num = new double[gn.length];//记录现有的HML个数
        double[] ratio_adjust = new double[gn.length];
        double empty_containers = 0;
        for (int i = 0; i < nc.length(); i++) {
            empty_containers += Integer.parseInt(String.valueOf(nc.charAt(i)));
        }
        for (int i = 0; i < gn.length; i++) {
            result[i] = 0;
            num[i] = 0;
        }

        if (gn.length == 2) {
            double[] ratio = {0.5, 0.5};
            int num_min = 0;
            int num_max = 0;
            int num_min1 = 0;
            int num_max1 = 0;
            int num_min2 = 0;
            int num_max2 = 0;
            int num_min3 = 0;
            int num_max3 = 0;
            for (int i = 0; i < sc.length; i++) {
                if (sc[i] >= 10) {
                    num[0]++; //现有的H个数
                    num_max += sc[i] / 10;
                    num_max1 += sc[i] / 10;
                } else
                    num[1] += sc[i] % 10;
                if (sc[i] >= 10 && sc[i] / 10 + Integer.parseInt(String.valueOf(nc.charAt(i))) < t)
                    num[1] += t - sc[i] / 10 - Integer.parseInt(String.valueOf(nc.charAt(i)));//现有的L个数
                //  num[1]++;
            }
            if ((sc.length * t) % 2 == 0) {
                num_min = (int) Math.max(Math.ceil(sc.length * t * ratio[0]) - empty_containers, (int) num[0]);
                System.out.println("num_min:" + num_min);
                if (num_max > Math.ceil(sc.length * t * ratio[0]))
                    num_max = (int) Math.ceil(sc.length * t * ratio[0]);
                num_max = Math.min((int) (sc.length * t - empty_containers - num[1]), num_max);
                System.out.println("num_max:" + num_max);
                double sum = 0;
                for (int i = (int) Math.ceil(sc.length * t * ratio[0]) - num_max; i <= (int) Math.ceil(sc.length * t * ratio[0]) - num_min; i++)
                    //for(int i=num_min;i<=num_max;i++)
                    sum += i;
                System.out.println("sum:" + sum);

                result[0] = (double) (sum / (num_max - num_min + 1));//现在有的该类型集装箱的个数
                result[1] = empty_containers - result[0];
            } else {
                if (num[1] >= Math.ceil(sc.length * t * ratio[1])) {
                    result[0] = empty_containers;
                    result[1] = 0;
                } else {
                    //H<L, L=11

                    num_min1 = (int) Math.max(Math.ceil(sc.length * t * ratio[0]) - 1 - empty_containers, (int) num[0]);//num[0]<=7
                    System.out.println("num_min1:" + num_min1);
                    if (num_max1 > Math.ceil(sc.length * t * ratio[0]) - 1)
                        num_max1 = (int) Math.ceil(sc.length * t * ratio[0]) - 1;
                    num_max1 = Math.min((int) (sc.length * t - empty_containers - num[1]), num_max1);//最大为10
                    System.out.println("num_max1:" + num_max1);
                    double sum1 = 0;
                    for (int i = (int) Math.ceil(sc.length * t * ratio[0]) - 1 - num_max1; i <= (int) Math.ceil(sc.length * t * ratio[0]) - 1 - num_min1; i++)
                        sum1 += i;
                    System.out.println("sum1:" + sum1);
                    //result[1]
                    num_min3 = (int) Math.max(Math.ceil(sc.length * t * ratio[1]) - empty_containers, (int) num[1]);
                    System.out.println("num_min3:" + num_min3);
                    num_max3 = (int) Math.min(sc.length * t - empty_containers - num[0], Math.ceil(sc.length * t * ratio[1]));
                    System.out.println("num_max3:" + num_max3);
                    double sum3 = 0;
                    for (int i = (int) Math.ceil(sc.length * t * ratio[1]) - num_max3; i <= (int) Math.ceil(sc.length * t * ratio[1]) - num_min3; i++)
                        sum3 += i;
                    System.out.println("sum3:" + sum3);

                    //H>L,H=11
                    num_min = (int) Math.max(Math.ceil(sc.length * t * ratio[0]) - empty_containers, (int) num[0]);
                    System.out.println("num_min:" + num_min);
                    if (num_max > Math.ceil(sc.length * t * ratio[0]))
                        num_max = (int) Math.ceil(sc.length * t * ratio[0]);
                    num_max = (int) Math.min(sc.length * t - empty_containers - num[1], num_max);
                    System.out.println("num_max:" + num_max);
                    double sum = 0;
                    for (int i = (int) Math.ceil(sc.length * t * ratio[0]) - num_max; i <= (int) Math.ceil(sc.length * t * ratio[0]) - num_min; i++)
                        sum += i;
                    System.out.println("sum:" + sum);

                    num_min2 = (int) Math.max(Math.ceil(sc.length * t * ratio[1]) - 1 - empty_containers, (int) num[1]);
                    System.out.println("num_min2:" + num_min2);
                    num_max2 = (int) Math.min(sc.length * t - empty_containers - num[0], Math.ceil(sc.length * t * ratio[1]) - 1);
                    System.out.println("num_max2:" + num_max2);
                    double sum2 = 0;
                    for (int i = (int) Math.ceil(sc.length * t * ratio[1]) - 1 - num_max2; i <= (int) Math.ceil(sc.length * t * ratio[1]) - 1 - num_min2; i++)
                        sum2 += i;
                    System.out.println("sum2:" + sum2);

                    result[0] = (double) (0.5 * sum1 / (num_max1 - num_min1 + 1) + 0.5 * sum / (num_max - num_min + 1));//将来可能有的该类型集装箱的个数
                    result[1] = (double) (0.5 * sum3 / (num_max3 - num_min3 + 1) + 0.5 * sum2 / (num_max2 - num_min2 + 1));

                }
            }
            System.out.println("result[0]:" + result[0]);
            System.out.println("result[1]:" + result[1]);
            for (int i = 0; i < gn.length; i++) {
                if (empty_containers != 0) {
                    ratio_adjust[i] = Math.max(result[i] / empty_containers, 0);
                    System.out.println("ratio_adjust[" + i + "]:" + ratio_adjust[i]);
                }
            }
        }
        if (gn.length == 3) {

            for (int i = 0; i < sc.length; i++) {
                if (sc[i] >= 100) {
                    result[0]++;
                } else if (sc[i] >= 10) {
                    result[1]++;
                } else {
                    result[1] += sc[i];
                }
            }
        }
        double probability = 0;
        for (int ii = 0; ii < gn.length; ii++) {
            probability += ratio_adjust[ii];
        }
        for (int ii = 0; ii < gn.length; ii++) {
            ratio_adjust[ii] = ratio_adjust[ii] / probability;
        }
        return ratio_adjust;
    }

    public static boolean calculate_ContainersType(String nc, int[] sc, int[] gn, int t) {
        int[] result = new int[gn.length];
        boolean signal = false;
        int empty_containers = 0;
        for (int i = 0; i < nc.length(); i++) {
            empty_containers += Integer.parseInt(String.valueOf(nc.charAt(i)));
        }
        if (gn.length == 2) {
            double[] ratio = new double[]{0.5, 0.5};
            for (int i = 0; i < sc.length; i++) {

                result[1] += sc[i] % 10;
                if (sc[i] >= 10 && sc[i] / 10 + Integer.parseInt(String.valueOf(nc.charAt(i))) < t)
                    result[1] += t - sc[i] / 10 - Integer.parseInt(String.valueOf(nc.charAt(i)));
            }
            if (result[1] < sc.length * t * ratio[1] + 1) {
                signal = true;
            }
        }
        if (gn.length == 3) {
            double[] ratio = new double[]{0.33, 0.33, 0.33};
            for (int i = 0; i < sc.length; i++) {
                if (sc[i] >= 100) {
                    result[0]++;
                    if (sc[i] / 100 + Integer.parseInt(String.valueOf(nc.charAt(i))) < t)
                        result[1] += t - sc[i] / 100 - Integer.parseInt(String.valueOf(nc.charAt(i)));
                } else if (sc[i] >= 10) {
                    result[1] += t - Integer.parseInt(String.valueOf(nc.charAt(i)));
                    if (sc[i] / 10 + Integer.parseInt(String.valueOf(nc.charAt(i))) < t)
                        result[2] += t - sc[i] / 10 - Integer.parseInt(String.valueOf(nc.charAt(i)));


                } else {
                    result[2] += sc[i];
                    result[1] += sc[i];
                }

            }
            if (result[0] < sc.length * t * result[0] + 1 && result[1] < sc.length * t * (ratio[1] + ratio[2]) + 1 && result[2] < sc.length * t * ratio[2] + 1) {
                signal = true;
            }
        }
        return signal;
    }

    public static void main(String[] args) {
//        int[] gn = new int[]{1, 10, 100};
//        double[] ratio = new double[]{0.33, 0.33, 0.33};
//        double[] ratio2 = new double[]{1.0 / 3, 1.0 / 3, 1.0 / 3};
        int[] gn = new int[]{1, 10};
        double[] ratio2 = new double[]{0.5, 0.5};
        Dynamic_programming_New2 dynamic_programming_new2 = new Dynamic_programming_New2();
        dynamic_programming_new2.dynamic_programming(4, 5, gn, ratio2, true);
    }

}

