package com.jsonbeautifier.beautifier.entry;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.jsonbeautifier.beautifier.component.JsonBeautifier;

public class EntryPoint {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            JsonBeautifier beautifier = new JsonBeautifier();
            beautifier.setSize(1024, 768);
            beautifier.getContentPane().setBackground(Color.BLACK);
            beautifier.setVisible(true);
        });
    }
}
