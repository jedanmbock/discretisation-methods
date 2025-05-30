/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.elementsfinis.methods;

/**
 *
 * @author JD
 */
import java.util.ArrayList;

public class FiniteDifferenceSolver {
    private int n;
    private double h;

    public FiniteDifferenceSolver(int n) {
        this.n = n;
        this.h = 1.0 / (n + 1);
    }

    public double[] solve() {
        int N = n * n;
        double[][] A = new double[N][N];
        double[] b = new double[N];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                int idx = j * n + i;
                A[idx][idx] = 4;
                if (i > 0) A[idx][idx - 1] = -1;
                else       b[idx] += exactSolution((i) * h, (j + 1) * h); // gauche
                if (i < n - 1) A[idx][idx + 1] = -1;
                else           b[idx] += exactSolution((i + 2) * h, (j + 1) * h); // droite
                if (j > 0) A[idx][idx - n] = -1;
                else       b[idx] += exactSolution((i + 1) * h, (j) * h); // bas
                if (j < n - 1) A[idx][idx + n] = -1;
                else           b[idx] += exactSolution((i + 1) * h, (j + 2) * h); // haut

                b[idx] += fValue((i+1)*h,(j+1)*h) * h * h; // f(x,y) = -4 donc RHS = -4 * h^2, 
            }
        }

        return gaussianElimination(A, b);
    }

    public double[] computeExact() {
        double[] u = new double[n * n];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                double x = (i + 1) * h;
                double y = (j + 1) * h;
                u[j * n + i] = exactSolution(x, y);
            }
        }
        return u;
    }

    private double exactSolution(double x, double y) {
        return x * x + y * y;
    }
    
    private double fValue(double x, double y){
        return -4;
    }

    private double[] gaussianElimination(double[][] A, double[] b) {
        int N = b.length;
        for (int p = 0; p < N; p++) {
            int max = p;
            for (int i = p + 1; i < N; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) max = i;
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double t = b[p]; b[p] = b[max]; b[max] = t;

            for (int i = p + 1; i < N; i++) {
                double alpha = A[i][p] / A[p][p];
                b[i] -= alpha * b[p];
                for (int j = p; j < N; j++) A[i][j] -= alpha * A[p][j];
            }
        }

        double[] x = new double[N];
        for (int i = N - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < N; j++) sum += A[i][j] * x[j];
            x[i] = (b[i] - sum) / A[i][i];
        }
        return x;
    }
}

