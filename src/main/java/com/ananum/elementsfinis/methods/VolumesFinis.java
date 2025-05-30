/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.elementsfinis.methods;

import com.ananum.elementsfinis.utils.CRS;
import com.ananum.elementsfinis.utils.Gauss;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author JD
 */
//Classe pour la résolution des équations différentielles par la méthode des volumes finis
public class VolumesFinis {
    public static HashMap<String, double[]> computeUtils(int nbMailles, double[] lambdaValues, double[] lambdaLimits){
        boolean flag = nbMailles<lambdaValues.length;
        HashMap<String, double[]> results = new HashMap();
        if(flag){
            System.out.println("Le nombre de Mailles sera augmenté.");
            nbMailles = lambdaValues.length;
        }
        double[] nodes = new double[nbMailles+1];
        double[] lambdas = new double[nbMailles];
        double[] vertices = new double[nbMailles+2];
        if(nbMailles == lambdaValues.length){
            for (int i = 0; i<nbMailles; i++){
                lambdas[i] = lambdaValues[i];
                nodes[i] = lambdaLimits[i];
                if(i == nbMailles-1)
                    nodes[i+1] = lambdaLimits[i+1];
                if(i>0)
                    vertices[i] = (nodes[i]+nodes[i-1])/2;
            }
            vertices[nbMailles] = (nodes[nbMailles]+nodes[nbMailles-1])/2;            
        }else{
            int maille = 0;
            for (int i = 0; i<lambdaValues.length; i++){
                if(maille < nbMailles){
                    int ratio = (nbMailles-maille)/(lambdaValues.length-i);
                    double pas = (lambdaLimits[i+1]-lambdaLimits[i])/ratio;
                    for(int j = 0; j<ratio; j++){
                        lambdas[maille] = lambdaValues[i];
                        nodes[maille] = lambdaLimits[i]+j*pas;
                        nodes[maille+1] = lambdaLimits[i]+(j+1)*pas;
                        if(i+j>0){
                            vertices[maille] = (nodes[maille]+nodes[maille-1])/2;
                            vertices[maille+1] = (nodes[maille+1]+nodes[maille])/2;
                        }
                        maille++;
                    }
                }else{
                    break;
                }
            }    
        }
        vertices[0] = nodes[0];
        vertices[nbMailles+1] = nodes[nbMailles];
        results.put("Nodes", nodes);
        results.put("Vertices", vertices);
        results.put("Lambdas", lambdas);

        for(Map.Entry<String, double[]> entry: results.entrySet()){
            System.out.println(entry.getKey()+" : "+ Arrays.toString(entry.getValue()));
        }
        return results;
    }
    
    //Calcul du facteur de diffusion
    private double[] computeDiffusionFactors(double[] lambdas, double[] nodes, double[] vertices){
        double[] factors = new double[nodes.length];
        for(int i = 1; i<factors.length-1; i++){
            factors[i] = (lambdas[i]*lambdas[i-1])/(lambdas[i-1]*(vertices[i+1]-nodes[i])+lambdas[i]*(nodes[i]-vertices[i]));
        }
        factors[0] = (lambdas[0])/((vertices[1]-nodes[0]));
        factors[nodes.length-1] = (lambdas[nodes.length-2])/(nodes[nodes.length-1]-vertices[nodes.length-1]);
        return factors;
    }
    
    //Résolution de -(lu')'+au'+bu = f sur [0,1], l constant par intervalle, a,b des réels avec décentrage amont
    public ArrayList<Double> volumesFinis(int nbMailles, ArrayList<Double> fValues, double primeCoef, double functCoef, double zeroValue, double oneValue, double[] lambdaValues, double[] lambdaLimits){
        HashMap<String, double[]> results = VolumesFinis.computeUtils(nbMailles,lambdaValues , lambdaLimits);
        double[] lambdas = results.get("Lambdas");
        double[] nodes = results.get("Nodes");
        double[] vertices = results.get("Vertices");
        double[] factors = this.computeDiffusionFactors(lambdas, nodes, vertices);
        double[][] A = new double[nbMailles][nbMailles];
        
        for (int i = 0; i<nbMailles; i++){
            for(int j = 0; j<nbMailles; j++){
                if(j == i)
                    A[i][j] = factors[j]+factors[j+1]+ primeCoef + functCoef*(nodes[i+1]-nodes[i]);
                if(j == i-1)
                    A[i][j] = -1.0*(factors[i]+primeCoef);
                if(j == i+1)
                    A[i][j] = -1.0*(factors[i+1]);
            }
        }
        
        CRS matrix = new CRS(A);
        ArrayList<Double> newValues = new ArrayList();
        for (int i = 0; i<fValues.size();i++){
            double h = nodes[i+1]-nodes[i];
            if(i == 0){
                newValues.add(h*fValues.get(i)+zeroValue*(factors[i]+primeCoef));
            }else{
                if(i == fValues.size()-1){
                    newValues.add(h*fValues.get(i)+oneValue*(factors[i+1]));
                }else{
                    newValues.add(h*fValues.get(i));
                }
            }
        }
        Gauss resolve = new Gauss();
        ArrayList<Double> value = resolve.resolution(matrix, newValues);
        value.add(0,zeroValue);
        value.add(oneValue);
        return value;
    }
    
    //Résolution de -(lu')'+au'+bu = f sur [0,1], l constant par intervalle, a,b des réels avec décentrage amont
    public ArrayList<Double> volumesFinisSansAmont(int nbMailles, ArrayList<Double> fValues, double primeCoef, double functCoef, double zeroValue, double oneValue, double[] lambdaValues, double[] lambdaLimits){
        HashMap<String, double[]> results = VolumesFinis.computeUtils(nbMailles,lambdaValues , lambdaLimits);
        double[] lambdas = results.get("Lambdas");
        double[] nodes = results.get("Nodes");
        double[] vertices = results.get("Vertices");
        double[] factors = this.computeDiffusionFactors(lambdas, nodes, vertices);
        double[][] A = new double[nbMailles][nbMailles];
        
        for (int i = 0; i<nbMailles; i++){
            for(int j = 0; j<nbMailles; j++){
                if(j == i)
                    A[i][j] = factors[j]+factors[j+1]+ primeCoef*(factors[j+1]*((vertices[i+1]-nodes[i])/lambdas[i])-factors[j]*((nodes[i]-vertices[i])/lambdas[i])) + functCoef*(nodes[i+1]-nodes[i]);
                if(j == i-1)
                    A[i][j] = -1.0*factors[i]*(1+primeCoef*((vertices[i+1]-nodes[i])/lambdas[i]));
                if(j == i+1)
                    A[i][j] = -1.0*(factors[i+1]*(1-primeCoef*((nodes[i+1]-vertices[i+1])/lambdas[i])));
            }
        }
        
        CRS matrix = new CRS(A);
        ArrayList<Double> newValues = new ArrayList();
        for (int i = 0; i<fValues.size();i++){
            double h = nodes[i+1]-nodes[i];
            if(i == 0){
                newValues.add(h*fValues.get(i)+zeroValue*(factors[i]+primeCoef));
            }else{
                if(i == fValues.size()-1){
                    newValues.add(h*fValues.get(i)+oneValue*(factors[i+1]));
                }else{
                    newValues.add(h*fValues.get(i));
                }
            }
        }
        Gauss resolve = new Gauss();
        ArrayList<Double> value = resolve.resolution(matrix, newValues);
        value.add(0,zeroValue);
        value.add(oneValue);
        return value;
    }
}
