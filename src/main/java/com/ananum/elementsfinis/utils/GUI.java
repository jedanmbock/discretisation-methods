/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.elementsfinis.utils;

/**
 *
 * @author JD
 */
import com.ananum.elementsfinis.methods.FiniteDifferenceSolver;
import javax.swing.*;

public class GUI {
   public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int n = 50; // nombre de points intérieurs
            FiniteDifferenceSolver solver = new FiniteDifferenceSolver(n);
            double[] uNumerical = solver.solve();
            double[] uExact = solver.computeExact();
            double[] error = new double[n * n];

            for (int i = 0; i < n * n; i++) {
                error[i] = Math.abs(uNumerical[i] - uExact[i]);
            }

            createFrame("Solution exacte", uExact, n);
            createFrame("Solution numérique", uNumerical, n);
            createFrame("Erreur absolue", error, n);
        });
    }

    private static void createFrame(String title, double[] data, int n) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new HeatMapViewer(data, n, title));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}

