/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.ananum.elementsfinis;

import com.ananum.elementsfinis.methods.VolumesFinis;
import com.ananum.elementsfinis.utils.CRS;
import com.ananum.elementsfinis.utils.Gauss;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

/**
 *
 * @author JD
 */
public class ElementsFinis {

    public static void main(String[] args) {
//        System.out.println("Vérifiction CRS");
//        double[][] matrix = {{0.0, 3.0, 0.0},{2.0, 0.0, 5.0},{0.0, 3.0, 2.0}};
//        CRS M = new CRS(matrix);
//        System.out.println(new CRS(matrix));
//        try{
//            System.out.println(M.get(2, 1)+" "+M.get(2, 2)+" "+M.get(0, 0));
//            M.set(0, 1, 0.0);
//            System.out.println("New Value: \n"+M);
//        }catch(Exception e){
//            System.out.println(e.getMessage());
//        }
        
//        System.out.println("Vérification Gauss");
//        double[][] matrix = {{1.0, 1.0, 1.0},{1.0, -1.0, 1.0},{1.0, 1.0, -1.0}};
//        ArrayList<Double> vecteur = new ArrayList(Arrays.asList(5.0,5.0,5.0));
//        Gauss resolve = new Gauss();
//        System.out.println(new CRS(matrix));
//        System.out.println(resolve.resolution(new CRS(matrix), vecteur));

//        System.out.println("Vérification differncesFinies.");
//        ArrayList<Double> vecteur = new ArrayList();
//        for(int i = 0; i<10; i++){
//            vecteur.add(-2.0);
//        }
//        ArrayList<Double> expected = new ArrayList();
//        for(int i = 0; i<10; i++){
//            expected.add((i+1)*(i+1)/(11.0*11.0));
//        }
//        
//        
//        System.out.println("Obtenus: "+ElementsFinis.differencesFinies(11,vecteur,0,1));
//        System.out.println("Attendus: "+expected);

        //Test JMathPlot
//        Plot2DPanel plot = new Plot2DPanel();
//        
//        double[] x = new double[21];
//        double[] y = new double[21];
//        for (int i = 0; i < x.length; i++) {
//            x[i] = i - 10;
//            y[i] = x[i] * x[i];
//        }
//
//        plot.addLinePlot("y = x^2", x, y);
//
//        JFrame frame = new JFrame("JMathPlot");
//        frame.setContentPane(plot);
//        frame.setSize(600, 400);
//        frame.setVisible(true);
//        XYSeries series = new XYSeries("y = x^2");
//        for (double x = -10; x <= 10; x += 0.5) {
//            series.add(x, x * x);
//        }
//
//        XYSeriesCollection dataset = new XYSeriesCollection(series);
//        JFreeChart chart = ChartFactory.createXYLineChart(
//                "Courbe Quadratique", "X", "Y",
//                dataset, PlotOrientation.VERTICAL, true, true, false);
//
//        JFrame frame = new JFrame("Graphique");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(new ChartPanel(chart));
//        frame.pack();
//        frame.setVisible(true);

        //Test VolumesFinis.computeUtils
//        double[] lambda = {1.0, 2.0};
//        double[] limits = {0.0, 0.5, 1.0};
//        HashMap<String, double[]> results = VolumesFinis.computeUtils(4,lambda , limits);
//        for(Map.Entry<String, double[]> entry: results.entrySet()){
//            System.out.println(entry.getKey()+" : "+ Arrays.toString(entry.getValue()));
//        }
        int equationChoice;
        Scanner clavier = new Scanner(System.in);
        do{
            System.out.println("Choisir l'équation: ");
            System.out.println("1 pour -u\"=f");
            System.out.println("2 pour -u\"+u'=f");
            equationChoice = clavier.nextInt();
            System.out.println(equationChoice);
        }while(equationChoice != 1 && equationChoice != 2);
    }
}
