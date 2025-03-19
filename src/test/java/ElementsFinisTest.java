
import com.ananum.elementsfinis.ElementsFinis;
import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
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
        System.out.println("Tests avec u(x) = 1");
        System.out.println("Cas avec 320 points entre 0 et 1");
        int[] nbsMailles = {10, 20, 40, 80, 160, 320};
        int nbMailles = 321;
        ArrayList<Double> fValues = new ArrayList();
        double pas = 1.0/(nbMailles-1);
        double init =0.0;
        for(int i = 0; i<nbMailles-1; i++){
            init += pas;
            fValues.add(funct1(init));
        }
        ArrayList<Double> expected = new ArrayList();
        init =0.0;
        for(int i = 0; i<nbMailles-1; i++){
            init += pas;
            expected.add(this.uexact1(init));
        }
        ArrayList<Double> actual = ElementsFinis.differencesFinies(nbMailles, fValues, uexact1(0), uexact1(1));
        ArrayList<Double> erreurs = this.erreurs(actual, expected);
        System.out.println("Erreur de calcul: "+this.norme(erreurs));
        System.out.println("-------------------------------------------------------------");
        
        XYSeries seriesErreurs = new XYSeries("erreurs");
        XYSeries seriesActual = new XYSeries("Calculés");
        XYSeries seriesExpected = new XYSeries("Espérés");
        XYSeries seriesErreur = new XYSeries("Erreur");
        init = 0.0;
        for (int i = 0; i<nbMailles-1; i++) {
            init += pas;
            seriesErreurs.add(init, erreurs.get(i));
            seriesActual.add(init, actual.get(i));
            seriesExpected.add(init, expected.get(i));
        }
        try{
            FileWriter writer = new FileWriter("Test1_u(x)=1.txt");
            for (int i:nbsMailles){
                ArrayList<Double> fValuesI = new ArrayList();
                double pasI = 1.0/(i-1);
                double initI =0.0;
                for(int j = 0; j<i-1; j++){
                    initI += pas;
                    fValuesI.add(funct1(initI));
                }
                ArrayList<Double> expectedI = new ArrayList();
                initI =0.0;
                for(int j = 0; j<i-1; j++){
                    initI += pasI;
                    expectedI.add(this.uexact1(initI));
                }
                ArrayList<Double> actualI = ElementsFinis.differencesFinies(i, fValuesI, uexact1(0), uexact1(1));
                ArrayList<Double> erreursI = this.erreurs(actualI, expectedI);
                seriesErreur.add(i, this.norme(erreursI));
                writer.write(i+" "+this.norme(erreursI)+"\n");
            }
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
                "Courbes pour u(x) = 1", "X", "Y",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        JFreeChart chartErreur = ChartFactory.createXYLineChart(
                "Evolution des erreurs en fonction du nombre de Mailles pour u(x) = 1", "nbMailles", "Erreur",
                datasetErreur, PlotOrientation.VERTICAL, true, true, false);
        
        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.RED);
        chart.getXYPlot().getRenderer().setSeriesPaint(1, Color.BLUE);
        chart.getXYPlot().getRenderer().setSeriesPaint(2, Color.GREEN);
        
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
        try{
            Thread.sleep(30000);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
    @Test
    public void differencesFiniesTest2(){
        System.out.println("Tests avec u(x) = x");
        System.out.println("Cas avec 320 points entre 0 et 1");
        int[] nbsMailles = {10, 20, 40, 80, 160, 320};
        int nbMailles = 321;
        ArrayList<Double> fValues = new ArrayList();
        double pas = 1.0/(nbMailles-1);
        double init =0.0;
        for(int i = 0; i<nbMailles-1; i++){
            init += pas;
            fValues.add(funct2(init));
        }
        ArrayList<Double> expected = new ArrayList();
        init =0.0;
        for(int i = 0; i<nbMailles-1; i++){
            init += pas;
            expected.add(this.uexact2(init));
        }
        ArrayList<Double> actual = ElementsFinis.differencesFinies(nbMailles, fValues, uexact2(0), uexact2(1));
        ArrayList<Double> erreurs = this.erreurs(actual, expected);
        System.out.println("Erreur de calcul: "+this.norme(erreurs));
        System.out.println("-------------------------------------------------------------");
        
        XYSeries seriesErreurs = new XYSeries("erreurs");
        XYSeries seriesActual = new XYSeries("Calculés");
        XYSeries seriesExpected = new XYSeries("Espérés");
        XYSeries seriesErreur = new XYSeries("Erreur");
        init = 0.0;
        for (int i = 0; i<nbMailles-1; i++) {
            init += pas;
            seriesErreurs.add(init, erreurs.get(i));
            seriesActual.add(init, actual.get(i));
            seriesExpected.add(init, expected.get(i));
        }
        
        try{
            FileWriter writer = new FileWriter("Test2_u(x)=x.txt");
            for (int i:nbsMailles){
                ArrayList<Double> fValuesI = new ArrayList();
                double pasI = 1.0/(i-1);
                double initI =0.0;
                for(int j = 0; j<i-1; j++){
                    initI += pas;
                    fValuesI.add(funct2(initI));
                }
                ArrayList<Double> expectedI = new ArrayList();
                initI =0.0;
                for(int j = 0; j<i-1; j++){
                    initI += pasI;
                    expectedI.add(this.uexact2(initI));
                }
                ArrayList<Double> actualI = ElementsFinis.differencesFinies(i, fValuesI, uexact2(0), uexact2(1));
                ArrayList<Double> erreursI = this.erreurs(actualI, expectedI);
                seriesErreur.add(i, this.norme(erreursI));
                writer.write(i+" "+this.norme(erreursI)+"\n");
            }
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
                "Courbes pour u(x)=x", "X", "Y",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        JFreeChart chartErreur = ChartFactory.createXYLineChart(
                "Evolution des erreurs en fonction du nombre de Mailles pour u(x)=x", "nbMailles", "Erreur",
                datasetErreur, PlotOrientation.VERTICAL, true, true, false);
        
        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.RED);
        chart.getXYPlot().getRenderer().setSeriesPaint(1, Color.BLUE);
        chart.getXYPlot().getRenderer().setSeriesPaint(2, Color.GREEN);
        
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
        try{
            Thread.sleep(30000);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
    @Test
    public void differencesFiniesTest3(){
        System.out.println("Tests avec f(x) = x^2");
        System.out.println("Cas avec 320 points entre 0 et 1");
        int[] nbsMailles = {10, 20, 40, 80, 160, 320};
        int nbMailles = 321;
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
        System.out.println("-------------------------------------------------------------");
        
        XYSeries seriesErreurs = new XYSeries("erreurs");
        XYSeries seriesActual = new XYSeries("Calculés");
        XYSeries seriesExpected = new XYSeries("Espérés");
        XYSeries seriesErreur = new XYSeries("Erreur");
        init = 0.0;
        for (int i = 0; i<nbMailles-1; i++) {
            init += pas;
            seriesErreurs.add(init, erreurs.get(i));
            seriesActual.add(init, actual.get(i));
            seriesExpected.add(init, expected.get(i));
        }
        
        try{
            FileWriter writer = new FileWriter("Test3_u(x)=x^2.txt");
            for (int i:nbsMailles){
                ArrayList<Double> fValuesI = new ArrayList();
                double pasI = 1.0/(i-1);
                double initI =0.0;
                for(int j = 0; j<i-1; j++){
                    initI += pas;
                    fValuesI.add(funct3(initI));
                }
                ArrayList<Double> expectedI = new ArrayList();
                initI =0.0;
                for(int j = 0; j<i-1; j++){
                    initI += pasI;
                    expectedI.add(this.uexact3(initI));
                }
                ArrayList<Double> actualI = ElementsFinis.differencesFinies(i, fValuesI, uexact3(0), uexact3(1));
                ArrayList<Double> erreursI = this.erreurs(actualI, expectedI);
                seriesErreur.add(i, this.norme(erreursI));
                writer.write(i+" "+this.norme(erreursI)+"\n");
            }
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
                "Courbes pour u(x)=x^2", "X", "Y",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        JFreeChart chartErreur = ChartFactory.createXYLineChart(
                "Evolution des erreurs en fonction du nombre de Mailles pour u(x)=x^2", "nbMailles", "Erreur",
                datasetErreur, PlotOrientation.VERTICAL, true, true, false);
        
        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.RED);
        chart.getXYPlot().getRenderer().setSeriesPaint(1, Color.BLUE);
        chart.getXYPlot().getRenderer().setSeriesPaint(2, Color.GREEN);
        
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
        try{
            Thread.sleep(30000);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
    @Test
    public void differencesFiniesTest4(){
        System.out.println("Tests avec f(x) = x^3");
        System.out.println("Cas avec 320 points entre 0 et 1");
        int[] nbsMailles = {10, 20, 40, 80, 160, 320};
        int nbMailles = 321;
        ArrayList<Double> fValues = new ArrayList();
        double pas = 1.0/(nbMailles-1);
        double init =0.0;
        for(int i = 0; i<nbMailles-1; i++){
            init += pas;
            fValues.add(funct4(init));
        }
        ArrayList<Double> expected = new ArrayList();
        init =0.0;
        for(int i = 0; i<nbMailles-1; i++){
            init += pas;
            expected.add(this.uPasExact1(init));
        }
        ArrayList<Double> actual = ElementsFinis.differencesFinies(nbMailles, fValues, uPasExact1(0), uPasExact1(1));
        ArrayList<Double> erreurs = this.erreurs(actual, expected);
        System.out.println("Erreur de calcul: "+this.norme(erreurs));
        System.out.println("-------------------------------------------------------------");
        
        XYSeries seriesErreurs = new XYSeries("erreurs");
        XYSeries seriesActual = new XYSeries("Calculés");
        XYSeries seriesExpected = new XYSeries("Espérés");
        XYSeries seriesErreur = new XYSeries("Erreur");
        init = 0.0;
        for (int i = 0; i<nbMailles-1; i++) {
            init += pas;
            seriesErreurs.add(init, erreurs.get(i));
            seriesActual.add(init, actual.get(i));
            seriesExpected.add(init, expected.get(i));
        }
        
        try{
            FileWriter writer = new FileWriter("Test4_u(x)=x^3.txt");
            for (int i:nbsMailles){
                ArrayList<Double> fValuesI = new ArrayList();
                double pasI = 1.0/(i-1);
                double initI =0.0;
                for(int j = 0; j<i-1; j++){
                    initI += pas;
                    fValuesI.add(funct4(initI));
                }
                ArrayList<Double> expectedI = new ArrayList();
                initI =0.0;
                for(int j = 0; j<i-1; j++){
                    initI += pasI;
                    expectedI.add(this.uPasExact1(initI));
                }
                ArrayList<Double> actualI = ElementsFinis.differencesFinies(i, fValuesI, uPasExact1(0), uPasExact1(1));
                ArrayList<Double> erreursI = this.erreurs(actualI, expectedI);
                seriesErreur.add(i, this.norme(erreursI));
                writer.write(i+" "+this.norme(erreursI)+"\n");
            }
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
                "Courbes pour u(x)=x^3", "X", "Y",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        JFreeChart chartErreur = ChartFactory.createXYLineChart(
                "Evolution des erreurs en fonction du nombre de Mailles pour u(x)=x^3", "nbMailles", "Erreur",
                datasetErreur, PlotOrientation.VERTICAL, true, true, false);
        
        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.RED);
        chart.getXYPlot().getRenderer().setSeriesPaint(1, Color.BLUE);
        chart.getXYPlot().getRenderer().setSeriesPaint(2, Color.GREEN);
        
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
        try{
            Thread.sleep(30000);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
    
    @Test
    public void differencesFiniesTest5(){
        System.out.println("Tests avec f(x) = sin(pi*x)");
        System.out.println("Cas avec 320 points entre 0 et 1");
        int[] nbsMailles = {10, 20, 40, 80, 160, 320};
        int nbMailles = 321;
        ArrayList<Double> fValues = new ArrayList();
        double pas = 1.0/(nbMailles-1);
        double init =0.0;
        for(int i = 0; i<nbMailles-1; i++){
            init += pas;
            fValues.add(funct5(init));
        }
        ArrayList<Double> expected = new ArrayList();
        init =0.0;
        for(int i = 0; i<nbMailles-1; i++){
            init += pas;
            expected.add(this.uPasExact2(init));
        }
        ArrayList<Double> actual = ElementsFinis.differencesFinies(nbMailles, fValues, uPasExact2(0), uPasExact2(1));
        ArrayList<Double> erreurs = this.erreurs(actual, expected);
        System.out.println("Erreur de calcul: "+this.norme(erreurs));
        System.out.println("---------------------------------------------");
        
        XYSeries seriesErreurs = new XYSeries("erreurs");
        XYSeries seriesActual = new XYSeries("Calculés");
        XYSeries seriesExpected = new XYSeries("Espérés");
        XYSeries seriesErreur = new XYSeries("Erreur");
        init = 0.0;
        for (int i = 0; i<nbMailles-1; i++) {
            init += pas;
            seriesErreurs.add(init, erreurs.get(i));
            seriesActual.add(init, actual.get(i));
            seriesExpected.add(init, expected.get(i));
        }
        
        try{
            FileWriter writer = new FileWriter("Test4_u(x)=sin(pi x).txt");
            for (int i:nbsMailles){
                ArrayList<Double> fValuesI = new ArrayList();
                double pasI = 1.0/(i-1);
                double initI =0.0;
                for(int j = 0; j<i-1; j++){
                    initI += pas;
                    fValuesI.add(funct5(initI));
                }
                ArrayList<Double> expectedI = new ArrayList();
                initI =0.0;
                for(int j = 0; j<i-1; j++){
                    initI += pasI;
                    expectedI.add(this.uPasExact2(initI));
                }
                ArrayList<Double> actualI = ElementsFinis.differencesFinies(i, fValuesI, uPasExact2(0), uPasExact2(1));
                ArrayList<Double> erreursI = this.erreurs(actualI, expectedI);
                seriesErreur.add(i, this.norme(erreursI));
                writer.write(i+" "+this.norme(erreursI)+"\n");
            }
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
                "Courbes pour u(x) = sin(pi*x)", "X", "Y",
                dataset, PlotOrientation.VERTICAL, true, true, false);
        JFreeChart chartErreur = ChartFactory.createXYLineChart(
                "Evolution des erreurs en fonction du nombre de Mailles pour u(x) = sin(pi*x)", "nbMailles", "Erreur",
                datasetErreur, PlotOrientation.VERTICAL, true, true, false);
        
        chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.RED);
        chart.getXYPlot().getRenderer().setSeriesPaint(1, Color.BLUE);
        chart.getXYPlot().getRenderer().setSeriesPaint(2, Color.GREEN);
        
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
        try{
            Thread.sleep(30000);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        
    }
}
