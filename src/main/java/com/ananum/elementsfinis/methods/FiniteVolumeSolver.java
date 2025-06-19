/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.elementsfinis.methods;

/**
 *
 * @author JD
 */
public class FiniteVolumeSolver {
    private int n, m;
    private double h, k;

    public FiniteVolumeSolver(int n, int m) {
        this.n = n;
        this.m = m;
        this.h = 1.0 / (n + 1);
        this.k = 1.0/ (m + 1);
    }

    public double[] solve() {
        int N = n * m;
        double[][] A = new double[N][N];
        double[] b = new double[N];
        double ih = 1.0/(h*h), ik = 1.0/(k*k);

        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                
                int idx = j * n + i;
                A[idx][idx] = 2.0*(ih+ik);
                if (i > 0) A[idx][idx - 1] = -ih;
                else       b[idx] += exactSolution((i) * h, (j + 1) * k); // gauche
                if (i < n - 1) A[idx][idx + 1] = -ih;
                else           b[idx] += exactSolution((i + 2) * h, (j + 1) * k); // droite
                if (j > 0) A[idx][idx - n] = -ik;
                else       b[idx] += exactSolution((i + 1) * h, (j) * k); // bas
                if (j < n - 1) A[idx][idx + n] = -ik;
                else           b[idx] += exactSolution((i + 1) * h, (j + 2) * k); // haut

                b[idx] += fValue((i+1)*h,(j+1)*k);  
            }
        }

        return gaussianElimination(A, b);
    }
    
     public double[] computeExact() {
        double[] u = new double[n * m];
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                double x = (i + 1) * h;
                double y = (j + 1) * k;
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
