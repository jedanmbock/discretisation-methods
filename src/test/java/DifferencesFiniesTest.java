
import com.ananum.elementsfinis.methods.DifferencesFinies;
import com.ananum.elementsfinis.utils.Function;
import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
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
//Test pour -u" = f
public class DifferencesFiniesTest {
    int nbMailles = 6;
    class Param{
        public String title, functName, pathTestResult, pathGraphics, pathErrorsGraphics;
        public Function function;

        public Param(String title, String functName, String pathTestResult, String pathGraphics, String pathErrorsGraphics, Function function) {
            this.title = title;
            this.functName = functName;
            this.pathTestResult = pathTestResult;
            this.pathGraphics = pathGraphics;
            this.pathErrorsGraphics = pathErrorsGraphics;
            this.function = function;
        }
    }
    
    class Uexact1 implements Function{
        @Override
        public double u(double x){
            return 1;
        }
        
        @Override
        public double f(double x){
            return 0;
        }
    }
    
    class Uexact2 implements Function{
        @Override
        public double u(double x){
            return x;
        }

        @Override
        public double f(double x){
            return 0;
        }
    }
    
    class Uexact3 implements Function{
        @Override
        public double u(double x){
            return x*x;
        }

        @Override
        public double f(double x){
            return -2;
        }
    }
    
    class UPasExact1 implements Function{
        @Override
        public double u(double x){
            return 10.0*x*x*x;
        }

        @Override
        public double f(double x){
            return -60.0*x;
        }
    }
    
    class UPasExact2 implements Function{
        @Override
        public double u(double x){
             return Math.sin(Math.PI*x);
        }

        @Override
        public double f(double x){
            return Math.PI * Math.PI *Math.sin(Math.PI*x);
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
    
    public ArrayList<Double> computeRegularFValues(Function funct, int nb){
        ArrayList<Double> fValues = new ArrayList();
        double pas = 1.0/(nb);
        double init =0.0;
        for(int i = 0; i<nb-1; i++){
            init += pas;
            fValues.add(funct.f(init));
        }
        return fValues;
    }
    
    public ArrayList<Double> computeRegularExpectedValues(Function funct, int nb){
        ArrayList<Double> expected = new ArrayList();
        double pas = 1.0/(nb);
        double init =0.0;
        for(int i = 0; i<nb-1; i++){
            init += pas;
            expected.add(funct.u(init));
        }
        return expected;
    }
    
    public void drawGraphics(Function funct, int nb, int equation, int[] nbsMailles, ArrayList<Double> expectedCurve,ArrayList<Double> erreurs, ArrayList<Double> actual, ArrayList<Double> expected, String name, String pathTestResult, String pathGraphics, String pathErrorsGraphics){
        XYSeries seriesErreurs = new XYSeries("erreurs");
        XYSeries seriesActual = new XYSeries("Calculés");
        XYSeries seriesExpected = new XYSeries("Espérés");
        XYSeries seriesErreur = new XYSeries("Erreur");
        double init = 0.0;
        double pas = 1.0/(nb);
        for (int i = 0; i<nb-1; i++) {
            init += pas;
            seriesErreurs.add(init, erreurs.get(i));
            seriesActual.add(init, actual.get(i));
        }
        init = 0.0;
        pas = 1.0/(521);
        for (int i = 0; i<520; i++) {
            init += pas;
            seriesExpected.add(init, expectedCurve.get(i));
        }
        ArrayList<Double> erreursMailles = new ArrayList();
        try{
            FileWriter writer = new FileWriter(pathTestResult);
            for (int i:nbsMailles){
                ArrayList<Double> fValuesI = this.computeRegularFValues(funct, i);
                ArrayList<Double> expectedI = this.computeRegularExpectedValues(funct, i);
                ArrayList<Double> actualI;
                if(equation == 1)
                    actualI = DifferencesFinies.differencesFinies(i, fValuesI, funct.u(0), funct.u(1));
                else
                    actualI = DifferencesFinies.differencesFinies2(i, fValuesI, funct.u(0), funct.u(1));
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
    
    public void differencesFiniesTest(String title, Function function, String functName, String pathTestResult, String pathGraphics, String pathErrorsGraphics, int equation){
        System.out.println(title);
        System.out.println("Cas avec "+this.nbMailles+" points entre 0 et 1");
        int[] nbsMailles = {11, 21, 41, 81, 161, 321};
        int nbMailles = this.nbMailles;
        Function funct = function;
        ArrayList<Double> fValues = this.computeRegularFValues(funct, nbMailles);
        ArrayList<Double> expectedCurve = this.computeRegularExpectedValues(funct, 521);
        ArrayList<Double> expected = this.computeRegularExpectedValues(funct, nbMailles);
        ArrayList<Double> actual;
        if (equation == 1)
            actual = DifferencesFinies.differencesFinies(nbMailles, fValues, funct.u(0), funct.u(1));
        else
            actual = DifferencesFinies.differencesFinies2(nbMailles, fValues, funct.u(0), funct.u(1));
        ArrayList<Double> erreurs = this.erreurs(actual, expected);
        System.out.println("Erreur de calcul: "+this.norme(erreurs));
        System.out.println("-------------------------------------------------------------");
        this.drawGraphics(funct, nbMailles, equation, nbsMailles, expectedCurve, erreurs, actual, expected,functName,pathTestResult, pathGraphics,pathErrorsGraphics);
    }
    
    @Test
    public void testEntry(){
        Scanner clavier = new Scanner(System.in);
        int equationChoice = 1, typeChoice = 2, functionChoice = 5;
        String tete = "differencesFiniesResults/equation";
        Param param1 = new Param("Tests avec u(x)=1","u(x)=1","Test1_u(x)=1.txt", "Graphique1_u(x)=1.png","Erreurs1_u(x)=1.png",new Uexact1());
        Param param2 = new Param("Tests avec u(x)=x","u(x)=x","Test2_u(x)=x.txt","Graphique2_u(x)=x.png","Erreurs2_u(x)=x.png",new Uexact2());
        Param param3 = new Param("Tests avec u(x)=x^2", "u(x)=x^2","Test3_u(x)=x^2.txt","Graphique3_u(x)=x^2.png","Erreurs3_u(x)=x^2.png",new Uexact3());
        Param param4 = new Param("Tests avec u(x)=x^3", "u(x)=x^3","Test4_u(x)=x^3.txt","Graphique4_u(x)=x^3.png","Erreurs4_u(x)=x^3.png",new UPasExact1());
        Param param5 = new Param("Tests avec u(x)=sin(pi*x)", "u(x)=sin(pi*x)", "Test5_u(x)=sin(pi x).txt","Graphique5_u(x)=sin(pi x).png","Erreurs5_u(x)=sin(pi x).png",new UPasExact2());
        System.out.println("Tests sur les différences finies");
        
            System.out.println("Choisir l'équation: ");
            System.out.println("1 pour -u\"=f");
            System.out.println("2 pour -u\"+u'=f");
            //equationChoice = clavier.nextInt();
            System.out.println(equationChoice);
        
        tete += equationChoice+"/";
        
            System.out.println("Choisir le test: ");
            System.out.println("1 pour toutes les fonctions");
            System.out.println("2 pour une fonction en particulier");
            //typeChoice = clavier.nextInt();
        
        Param[] functions = {param1, param2, param3, param4, param5};
        if(typeChoice == 2){
            
                System.out.println("Choisir la fonction: ");
                System.out.println("1 pour u(x)=1");
                System.out.println("2 pour u(x)=x");
                System.out.println("3 pour u(x)=x^2");
                System.out.println("4 pour u(x)=x^3");
                System.out.println("5 pour u(x)=sin(pi*x)");
                //functionChoice = clavier.nextInt();
            this.differencesFiniesTest(functions[functionChoice-1].title, functions[functionChoice-1].function, functions[functionChoice-1].functName, tete+functions[functionChoice-1].pathTestResult, tete+functions[functionChoice-1].pathGraphics, tete+functions[functionChoice-1].pathErrorsGraphics, equationChoice);
        }else{
            for(Param p: functions){
                this.differencesFiniesTest(p.title, p.function, p.functName, tete+p.pathTestResult, tete+p.pathGraphics, tete+p.pathErrorsGraphics, equationChoice);
            }
        }     
    }
}