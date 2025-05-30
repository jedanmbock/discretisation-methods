/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.elementsfinis.utils;

/**
 *
 * @author JD
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.DecimalFormat;

public class HeatmapWithZoom extends JPanel {
    private final int rows = 50;
    private final int cols = 50;
    private final double[][] data = new double[rows][cols];
    private final int cellSize = 10;
    private final int margin = 40;
    private final int legendWidth = 20;

    private final double minValue, maxValue;

    // Pour le zoom
    private int mouseX = -1, mouseY = -1;

    public HeatmapWithZoom() {
        double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data[i][j] = Math.sin(i * 0.1) * Math.cos(j * 0.1);
                if (data[i][j] < min) min = data[i][j];
                if (data[i][j] > max) max = data[i][j];
            }
        }
        minValue = min;
        maxValue = max;

        setToolTipText("");
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                int col = (mouseX - margin) / cellSize;
                int row = (mouseY - margin) / cellSize;
                if (col >= 0 && col < cols && row >= 0 && row < rows) {
                    double value = data[row][col];
                    setToolTipText(String.format("x=%d, y=%d, val=%.3f", col, row, value));
                } else {
                    setToolTipText(null);
                }
                repaint();
            }
        });

        setPreferredSize(new Dimension(margin + cols * cellSize + legendWidth + 150, margin + rows * cellSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessin de la heatmap
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g.setColor(getColorForValue(data[i][j]));
                int x = margin + j * cellSize;
                int y = margin + i * cellSize;
                g.fillRect(x, y, cellSize, cellSize);
            }
        }

        // Axes
        g.setColor(Color.BLACK);
        for (int i = 0; i <= rows; i += 10) {
            int y = margin + i * cellSize;
            g.drawString(Integer.toString(i), 5, y + 5);
        }
        for (int j = 0; j <= cols; j += 10) {
            int x = margin + j * cellSize;
            g.drawString(Integer.toString(j), x - 5, margin - 5);
        }

        // Légende
        int legendX = margin + cols * cellSize + 10;
        int legendY = margin;
        int legendHeight = rows * cellSize;
        for (int i = 0; i < legendHeight; i++) {
            float ratio = 1f - (float) i / legendHeight;
            double value = minValue + ratio * (maxValue - minValue);
            g.setColor(getColorForValue(value));
            g.fillRect(legendX, legendY + i, legendWidth, 1);
        }

        g.setColor(Color.BLACK);
        g.drawRect(legendX, legendY, legendWidth, legendHeight);
        DecimalFormat df = new DecimalFormat("#.##");
        g.drawString(df.format(maxValue), legendX + legendWidth + 5, legendY + 10);
        g.drawString(df.format(minValue), legendX + legendWidth + 5, legendY + legendHeight - 2);

        // Affichage du zoom local
        drawZoom(g);
    }

    private void drawZoom(Graphics g) {
        if (mouseX < margin || mouseY < margin) return;

        int col = (mouseX - margin) / cellSize;
        int row = (mouseY - margin) / cellSize;
        if (col < 0 || col >= cols || row < 0 || row >= rows) return;

        int zoomSize = 5;
        int zoomCellSize = 20;
        int zoomX = getWidth() - zoomCellSize * zoomSize - 30;
        int zoomY = 50;

        g.setColor(Color.BLACK);
        g.drawRect(zoomX - 1, zoomY - 1, zoomCellSize * zoomSize + 2, zoomCellSize * zoomSize + 2);
        g.drawString("Zoom", zoomX, zoomY - 10);

        for (int i = -zoomSize / 2; i <= zoomSize / 2; i++) {
            for (int j = -zoomSize / 2; j <= zoomSize / 2; j++) {
                int r = row + i;
                int c = col + j;
                if (r >= 0 && r < rows && c >= 0 && c < cols) {
                    double value = data[r][c];
                    Color color = getColorForValue(value);
                    int x = zoomX + (j + zoomSize / 2) * zoomCellSize;
                    int y = zoomY + (i + zoomSize / 2) * zoomCellSize;

                    g.setColor(color);
                    g.fillRect(x, y, zoomCellSize, zoomCellSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, zoomCellSize, zoomCellSize);
                    g.setFont(new Font("Arial", Font.PLAIN, 10));
                    g.drawString(String.format("%.2f", value), x + 3, y + 12);
                }
            }
        }
    }

    private Color getColorForValue(double value) {
        float norm = (float) ((value - minValue) / (maxValue - minValue));
        return Color.getHSBColor((1 - norm) * 0.7f, 1f, 1f);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Heatmap 2D avec Zoom");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new HeatmapWithZoom());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

