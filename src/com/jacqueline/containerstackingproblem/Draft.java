package com.jacqueline.containerstackingproblem;

public class Draft {
    public static void change(int[][] a) {
        a[0][0] = 1;
    }

    public static void main(String[] args) {
        String aa = "aba";
        String bb = aa;
        bb.replace('b', 'c');
        System.out.println(aa);
        int[][] a = new int[3][3];
        int[][] b = a;
        change(b);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }
        boolean[] pre_find = new boolean[3];
        for (int j = 0; j < 3; j++) {
            System.out.print(pre_find[j] + "\t");
        }
    }
}


