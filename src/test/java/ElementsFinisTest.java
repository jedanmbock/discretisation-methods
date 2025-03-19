
import com.ananum.elementsfinis.ElementsFinis;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
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
public class ElementsFinisTest {
    public double uexact1(double x){
        return 1;
    }
    
    public double funct1(double x){
        return 0;
    }
    
    public double uexact2(double x){
        return x;
    }
    
    public double funct2(double x){
        return 0;
    }
    
    public double uexact3(double x){
        return x*x;
    }
    
    public double funct3(double x){
        return -2;
    }
    
    public double uPasExact1(double x){
        return x*x*x;
    }
    
    public double funct4(double x){
        return 6*x;
    }
    
    public double uPasExact2(double x){
        return Math.sin(Math.PI*x);
    }
    
    public double funct5(double x){
        return -Math.PI * Math.PI *Math.sin(Math.PI*x);
    }
    
    public double erreur(double actual, double expected){
        return Math.abs(actual-expected);
    }
    
    public ArrayList<Double> erreurs(ArrayList<Double> actual, ArrayList<Double> expected){
        ArrayList<Double> erreurs = new ArrayList();
        for(int i = 0; i<actual.size(); i++)
            erreurs.add(this.erreur(actual.get(i),expected.get(i)));
        return erreurs;
    }
    public double norme(ArrayList<Double> erreurs){
        return erreurs.stream().reduce(0.0, (a, b)->Math.max(a,b));
    }
    
    @Test
    public void differencesFiniesTest1(){
        System.out.println("Tests avec f(x) = 1");
        System.out.println("Cas avec 1000 points entre 0 et 1");
        int nbMailles = 151;
        ArrayList<Double> fValues = new ArrayList();
        double pas = 1.0/(nbMailles-1);
        double init =0.0;
        for(int i = 0; i<nbMailles-1; i++){
            init += pas;
            fValues.add(funct3(init));
        }
        ArrayList<Double> expected = new ArrayList();
        init =0.0;
        for(int i = 0; i<nbMailles-1; i++){
            init += pas;
            expected.add(this.uexact3(init));
        }
        ArrayList<Double> actual = ElementsFinis.differencesFinies(nbMailles, fValues, uexact3(0), uexact3(1));
        ArrayList<Double> erreurs = this.erreurs(actual, expected);
        System.out.println("Erreur de calcul: "+this.norme(erreurs));
        
        XYSeries seriesErreurs = new XYSeries("erreurs");
        XYSeries seriesActual = new XYSeries("Calculés");
        XYSeries seriesExpected = new XYSeries("Espérés");
        init = 0.0;
        for (int i = 0; i<nbMailles-1; i++) {
            init += pas;
            seriesErreurs.add(init, erreurs.get(i));
            seriesActual.add(init, actual.get(i));
            seriesExpected.add(init, expected.get(i));
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesActual);
        dataset.addSeries(seriesExpected);
        dataset.addSeries(seriesErreurs);
        
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Courbes", "X", "Y",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        
        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.RED);
        chart.getXYPlot().getRenderer().setSeriesPaint(1, Color.BLUE);
        chart.getXYPlot().getRenderer().setSeriesPaint(2, Color.GREEN);
        JFrame frame = new JFrame("Graphique");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
        try{
            Thread.sleep(100000);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
}
