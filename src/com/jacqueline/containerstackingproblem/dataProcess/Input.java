package com.jacqueline.containerstackingproblem.dataProcess;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Input {
    public String[][] readdata(String filename) {
        String[] content = null;
        int linenu = 0;
        int totalline = 0;
        String line = null;
        // 先统计文件有多少行?
        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                totalline = totalline + 1;
            }
            fr.close();
            br.close();
        } catch (IOException ae) {
            System.out.println("报错");
        }

        String[][] res = new String[totalline][];
        // 逐行保存到数据里边去
        try {
            FileReader fr1 = new FileReader(filename);
            BufferedReader br1 = new BufferedReader(fr1);
            while ((line = br1.readLine()) != null) {
                content = line.split("\t");
                res[linenu] = content;
                linenu = linenu + 1;
            }
            fr1.close();
            br1.close();

        } catch (IOException ae) {
            System.out.println("报错");
        }
        return res;
    }
}
