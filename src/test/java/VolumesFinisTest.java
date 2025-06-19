
import com.ananum.elementsfinis.methods.DifferencesFinies;
import com.ananum.elementsfinis.methods.VolumesFinis;
import com.ananum.elementsfinis.utils.Function;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.jupiter.api.Test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author JD
 */
public class VolumesFinisTest {
    private int nbMailles = 600;
    private double[] lambdaValues = {1.0,2.0};
    private double[] lambdaLimits = {0.0, 0.5, 1.0};
    private double primeCoef = 0.0;
    private double functCoef = 0.0;
    
    class Uexact3 implements Function{
        @Override
        public double u(double x){
            if(x>=0.0 && x<0.5)
                return x*x;
            return 0.5*x*x;
        }

        @Override
        public double f(double x){
            return -2.0;
        }
    }
    
    public double erreur(double actual, double expected){
        return Math.abs(actual-expected);
    }
    
    public ArrayList<Double> erreurs (ArrayList<Double> actual, ArrayList<Double> expected){
        ArrayList<Double> erreurs = new ArrayList();
        for(int i = 0; i<actual.size(); i++)
            erreurs.add(this.erreur(actual.get(i),expected.get(i)));
        return erreurs;
    }
    
    public double norme(ArrayList<Double> erreurs){
        return erreurs.stream().reduce(0.0, (a, b)->Math.max(a,b));
    }
    
    public double min(ArrayList<Double> erreurs){
        return erreurs.stream().reduce(0.0, (a, b)->Math.min(a, b));
    }
    
    public void saveImage(String name, JFreeChart chart){
        File file = new File(name);

        try {
            if (file.createNewFile()) {
                System.out.println("Fichier créé : " + file.getAbsolutePath());
                ChartUtils.saveChartAsPNG(file, chart, 800, 600);
                System.out.println("Image enregistrée.");
            } else {
                System.out.println("Le fichier existe déjà.");
                ChartUtils.saveChartAsPNG(file, chart, 800, 600);
                System.out.println("Image enregistrée.");
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la création du fichier : " + e.getMessage());
        }
        
        
    }
    
    public ArrayList<Double> computeRegularFValues(Function funct, double[] points){
        ArrayList<Double> fValues = new ArrayList();
        for(int i = 1; i<points.length-1; i++){
            fValues.add(funct.f(points[i]));
        }
        return fValues;
    }
    
    public ArrayList<Double> computeRegularExpectedValues(Function funct, int nb){
        ArrayList<Double> fValues = new ArrayList();
        double pas = 1.0/(nb);
        for(int i = 0; i<nb+1; i++){
            fValues.add(funct.u(i*pas));
        }
        return fValues;
    }
    
    public ArrayList<Double> computeRegularExpectedValues(Function funct, double[] points){
        ArrayList<Double> expected = new ArrayList();
        for(int i = 0; i<points.length; i++){
            expected.add(funct.u(points[i]));
        }
        return expected;
    }
    
    public void drawGraphics(Function funct, int nb, double[] points, int[] nbsMailles, ArrayList<Double> expectedCurve,ArrayList<Double> erreurs, ArrayList<Double> actual, ArrayList<Double> expected, String name, String pathTestResult, String pathGraphics, String pathErrorsGraphics, double[] lambdaValues, double[] lambdaLimits){
        XYSeries seriesErreurs = new XYSeries("erreurs");
        XYSeries seriesActual = new XYSeries("Calculés");
        XYSeries seriesExpected = new XYSeries("Espérés");
        XYSeries seriesErreur = new XYSeries("Erreur");
        for (int i = 0; i<points.length; i++) {
            seriesErreurs.add(points[i], erreurs.get(i));
            seriesActual.add(points[i], actual.get(i));
        }
        double pas = 1.0/(1001);
        for (int i = 0; i<1002; i++) {
            seriesExpected.add(i*pas, expectedCurve.get(i));
        }
        ArrayList<Double> erreursMailles = new ArrayList();
        try{
            FileWriter writer = new FileWriter(pathTestResult);
            for (int i:nbsMailles){
                HashMap<String, double[]> results = VolumesFinis.computeUtils(i, lambdaValues, lambdaLimits);
                double[] pointsI = results.get("Vertices");
                ArrayList<Double> fValuesI = this.computeRegularFValues(funct, pointsI);
                ArrayList<Double> expectedI = this.computeRegularExpectedValues(funct, pointsI);
                VolumesFinis resolve = new VolumesFinis();
                ArrayList<Double> actualI = resolve.volumesFinis(i, fValuesI, this.primeCoef, this.functCoef, funct.u(0), funct.u(1), this.lambdaValues, this.lambdaLimits);
                ArrayList<Double> erreursI = this.erreurs(actualI, expectedI);
                seriesErreur.add(i, this.norme(erreursI));
                writer.write(i+" "+this.norme(erreursI)+"\n");
                erreursMailles.add(this.norme(erreursI));
            }
            int index1 = 0;
            int index2 = erreursMailles.size()-1;
            double convergenceRate = (Math.log(erreursMailles.get(index1)/erreursMailles.get(index2)))/(Math.log(nbsMailles[index2]/nbsMailles[index1]));
            System.out.println("La vitesse de convergence est: "+convergenceRate);
            writer.close();
        }catch(IOException e){
            System.out.println("Erreur lors de la création du fichier de conservation de résultats");
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesActual);
        dataset.addSeries(seriesExpected);
        dataset.addSeries(seriesErreurs);
        
        XYSeriesCollection datasetErreur = new XYSeriesCollection(seriesErreur);
        
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Courbes pour "+name, "X", "Y",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        JFreeChart chartErreur = ChartFactory.createXYLineChart(
                "Evolution des erreurs en fonction du nombre de Mailles pour "+name, "nbMailles", "Erreur",
                datasetErreur, PlotOrientation.VERTICAL, true, true, false);
        
        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.RED);
        chart.getXYPlot().getRenderer().setSeriesPaint(1, Color.BLUE);
        chart.getXYPlot().getRenderer().setSeriesPaint(2, Color.GREEN);
        //chartErreur.getXYPlot().getRangeAxis().setRange(this.min(erreursMailles),this.norme(erreursMailles));
        
        JFrame frame = new JFrame("Graphique");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
        
        JFrame frameErreur = new JFrame("Erreurs");
        frameErreur.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frameErreur.getContentPane().add(new ChartPanel(chartErreur));
        frameErreur.pack();
        frameErreur.setVisible(true);
        this.saveImage(pathGraphics, chart);
        this.saveImage(pathErrorsGraphics, chartErreur);
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    @Test
    public void differencesFiniesTest3(){
        System.out.println("Tests avec f(x) = x^2");
        System.out.println("Cas avec "+this.nbMailles+" points entre 0 et 1");
        int[] nbsMailles = {11, 21, 41, 81, 161, 321, 641, 1281};
        int nbMailles = this.nbMailles;
        Function funct = new Uexact3();
        HashMap<String, double[]> results = VolumesFinis.computeUtils(nbMailles, this.lambdaValues, this.lambdaLimits);
        double[] points = results.get("Vertices");
        ArrayList<Double> fValues = this.computeRegularFValues(funct, points);
        ArrayList<Double> expected = this.computeRegularExpectedValues(funct, points);
        ArrayList<Double> expectedCurve = this.computeRegularExpectedValues(funct, 1001);
        VolumesFinis resolve = new VolumesFinis();
        ArrayList<Double> actual = resolve.volumesFinis(nbMailles, fValues, this.primeCoef, this.functCoef, funct.u(0), funct.u(1), this.lambdaValues, this.lambdaLimits);
        ArrayList<Double> erreurs = this.erreurs(actual, expected);
        System.out.println("Erreur de calcul: "+this.norme(erreurs));
        System.out.println("-------------------------------------------------------------");
        this.drawGraphics(funct, nbMailles, points, nbsMailles, expectedCurve, erreurs, actual, expected,"u(x) = x^2","volumesFinisResults/Test3_u(x)=x^2.txt","volumesFinisResults/Graphique3_u(x)=x^2.png","volumesFinisResults/Erreurs3_u(x)=x^2.png",this.lambdaValues, this.lambdaLimits);
    }
}
