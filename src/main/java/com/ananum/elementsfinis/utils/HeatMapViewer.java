/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.elementsfinis.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

/**
 *
 * @author JD
 */
public class HeatMapViewer extends JPanel{
    private double[] values;
    private int n, m;
    private String title;
    private double minValue, maxValue;
    private final int LEGEND_WIDTH = 60;

    public HeatMapViewer(double[] values, int n, int m, String title) {
        this.values = values;
        this.n = n;
        this.m = m;
        this.title = title;
        setPreferredSize(new Dimension(600, 600));
        setToolTipText("");
        ToolTipManager.sharedInstance().registerComponent(this);

        minValue = Double.MAX_VALUE;
        maxValue = -Double.MAX_VALUE;
        for (double v : values) {
            if (v < minValue) minValue = v;
            if (v > maxValue) maxValue = v;
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth() - LEGEND_WIDTH;
        int h = getHeight();
        int cellW = w / n;
        int cellH = h / m;

        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                int index = j * n + i;
                float norm = (float) ((values[index] - minValue) / (maxValue - minValue));
                g2.setColor(Color.getHSBColor(0.7f - norm * 0.7f, 1f, 1f));
                g2.fillRect(i * cellW, j * cellH, cellW, cellH);
            }
        }

        // Légende (Colorbar)
        int legendX = w + 10;
        int legendY = 20;
        int legendHeight = h - 40;
        int steps = 10;

        for (int k = 0; k < steps; k++) {
            float frac = (float) k / (steps - 1);
            g2.setColor(Color.getHSBColor(0.7f - frac * 0.7f, 1f, 1f));
            int y = legendY + (legendHeight * (steps - 1 - k)) / steps;
            g2.fillRect(legendX, y, 20, legendHeight / steps);
            double value = minValue + frac * (maxValue - minValue);
            g2.setColor(Color.BLACK);
            g2.drawString(new Float(value).toString(), legendX + 25, y + 10);
        }

        g2.setColor(Color.BLACK);
        g2.drawString(title, 10, 15);
    }

    public String getToolTipText(MouseEvent event) {
        int w = getWidth() - LEGEND_WIDTH;
        int h = getHeight();
        int cellW = w / n;
        int cellH = h / m;
        int i = event.getX() / cellW;
        int j = event.getY() / cellH;
        if (i >= 0 && i < n && j >= 0 && j < m) {
            int index = j * n + i;
            return String.format("(i=%d, j=%d): %.2e", i + 1, j + 1, values[index]);
        }
        return null;
    }
}
