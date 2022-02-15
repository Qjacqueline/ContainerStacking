package com.jacqueline.containerstackingproblem.config;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class dialogbox {
    public void dialogbox(String title, String content) {
        JFrame frame = new JFrame();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        JOptionPane.showMessageDialog(frame, content, title,
                JOptionPane.ERROR_MESSAGE);
        frame.dispose();
    }
}
