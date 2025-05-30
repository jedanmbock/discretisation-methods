/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.elementsfinis.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author JD
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class HeatmapPanel extends JPanel {
    private final int n;
    private final ArrayList<Double> data;
    private double min, max;
    private int hoverI = -1, hoverJ = -1;
    private static final int CELL_SIZE = 20;
    private static final int LEGEND_WIDTH = 60;
    private final int numLevels = 6;
    private final DecimalFormat df = new DecimalFormat("0.000");

    public HeatmapPanel(int n, ArrayList<Double> data) {
        this.n = n;
        this.data = data;
        computeMinMax();
        setPreferredSize(new Dimension(n * CELL_SIZE + LEGEND_WIDTH, n * CELL_SIZE));
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int x = e.getX(), y = e.getY();
                if (x < n * CELL_SIZE && y < n * CELL_SIZE) {
                    hoverI = x / CELL_SIZE;
                    hoverJ = y / CELL_SIZE;
                    repaint();
                } else {
                    hoverI = hoverJ = -1;
                }
            }
        });
    }

    private void computeMinMax() {
        min = Double.POSITIVE_INFINITY;
        max = Double.NEGATIVE_INFINITY;
        for (double val : data) {
            if (val < min) min = val;
            if (val > max) max = val;
        }
    }

    private Color getColor(double value) {
        float ratio = (float) ((value - min) / (max - min));
        return Color.getHSBColor(0.66f - 0.66f * ratio, 1f, 1f); // blue to red
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Heatmap
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                int index = j * n + i;
                double val = data.get(index);
                g.setColor(getColor(val));
                g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                g.setColor(Color.GRAY);
                g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }

        // Hover tooltip
        if (hoverI >= 0 && hoverJ >= 0 && hoverI < n && hoverJ < n) {
            int index = hoverJ * n + hoverI;
            double val = data.get(index);
            g.setColor(Color.BLACK);
            g.drawString("(" + hoverI + "," + hoverJ + "): " + df.format(val),
                    10, getHeight() - 10);
        }

        // Legend
        drawLegend(g);
    }

    private void drawLegend(Graphics g) {
        int xStart = n * CELL_SIZE + 10;
        int yStart = 10;
        int legendHeight = getHeight() - 20;
        int boxHeight = legendHeight / numLevels;

        for (int i = 0; i < numLevels; i++) {
            double val1 = min + i * (max - min) / numLevels;
            double val2 = min + (i + 1) * (max - min) / numLevels;
            g.setColor(getColor((val1 + val2) / 2));
            g.fillRect(xStart, yStart + i * boxHeight, 20, boxHeight);
            g.setColor(Color.BLACK);
            g.drawRect(xStart, yStart + i * boxHeight, 20, boxHeight);
            g.drawString(df.format(val2), xStart + 25, yStart + i * boxHeight + 12);
        }
        g.drawString("Légende", xStart, yStart - 5);
    }
}
