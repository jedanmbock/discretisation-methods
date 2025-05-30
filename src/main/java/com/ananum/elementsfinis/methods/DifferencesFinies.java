/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.elementsfinis.methods;

import com.ananum.elementsfinis.utils.CRS;
import com.ananum.elementsfinis.utils.Gauss;
import java.util.ArrayList;

/**
 *
 * @author JD
 */
//Classe pour la résolution des équations différentielles par la méthode des différences finies

public class DifferencesFinies {
    // Résolution de -u" = f sur [0,1] avec pas régulier sous les conditions de Dirichlet
    public static ArrayList<Double> differencesFinies(int nbMailles, ArrayList<Double> fValues, double zeroValue, double oneValue){
        double[][] A = new double[nbMailles-1][nbMailles-1];
        
        for (int i = 0; i<nbMailles-1; i++){
            for(int j = 0; j<nbMailles-1; j++){
                if(j == i)
                    A[i][j] = 2.0*(nbMailles*nbMailles);
                if(j == i-1 || j == i+1)
                    A[i][j] = -1.0*(nbMailles*nbMailles);
            }
        }
        
        CRS matrix = new CRS(A);
        ArrayList<Double> newValues = new ArrayList();
        for (int i = 0; i<fValues.size();i++){
            if(i == 0){
                newValues.add(fValues.get(i)+zeroValue*(nbMailles*nbMailles));
            }else{
                if(i == fValues.size()-1){
                    newValues.add(fValues.get(i)+oneValue*(nbMailles*nbMailles));
                }else{
                    newValues.add(fValues.get(i));
                }
            }
        }
        Gauss resolve = new Gauss();
        return resolve.resolution(matrix, newValues);
    }
    
    // Résolution de -u"+u' = f sur [0,1] avec pas régulier sous les conditions de Dirichlet
    public static ArrayList<Double> differencesFinies2(int nbMailles, ArrayList<Double> fValues, double zeroValue, double oneValue){
        double[][] A = new double[nbMailles-1][nbMailles-1];
        double diag = 2.0*(nbMailles*nbMailles);
        double minus = -1.0*(nbMailles*nbMailles)-nbMailles/2.0;
        double plus = -1.0*(nbMailles*nbMailles)+nbMailles/2.0;
        
        for (int i = 0; i<nbMailles-1; i++){
            for(int j = 0; j<nbMailles-1; j++){
                if(j == i)
                    A[i][j] = diag;
                if(j == i-1)
                    A[i][j] = minus;
                if(j == i+1)
                    A[i][j] = plus;
            }
        }
        
        CRS matrix = new CRS(A);
        ArrayList<Double> newValues = new ArrayList();
        for (int i = 0; i<fValues.size();i++){
            if(i == 0){
                newValues.add(fValues.get(i)-zeroValue*minus);
            }else{
                if(i == fValues.size()-1){
                    newValues.add(fValues.get(i)-oneValue*plus);
                }else{
                    newValues.add(fValues.get(i));
                }
            }
        }
        Gauss resolve = new Gauss();
        return resolve.resolution(matrix, newValues);
    }
    
    //Résolution de -div(gradU) = f sur [0;1]x[0;1] sous les conditions de Dirichlet sur un domaine régulier carré
    public static ArrayList<Double> differencesFinies2D(int nbMaillesX, int nbMaillesY, ArrayList<Double> fValues, ArrayList<Double> borderValuesX, ArrayList<Double> borderValuesY){
        double h = 1.0/nbMaillesX, k = 1.0/nbMaillesY;
        int taille = nbMaillesX*nbMaillesY;
        double[][] A = new double[taille][taille];
        double diag = 2*((1.0/(h*h))+(1.0/(k*k))), coefX = -1.0/(h*h), coefY = -1.0/(k*k);
        for(int i = 0; i<taille; i++){
            for(int j = 0; j<taille; j++){
                if(i == j)
                    A[i][j] = diag;
                if(j == i+1 || j == i-1){
                    if(i%(nbMaillesX-1) != 0)
                        A[i][j] = coefX;
                }
                if(j == i-nbMaillesX+1 || j == i+nbMaillesX-1){
                    if(i%(nbMaillesX) != 0)
                        A[i][j] = coefY;
                }
            }
        }
        CRS matrix = new CRS(A);
        ArrayList<Double> newValues = new ArrayList();
        for (int i = 0; i<fValues.size();i++){
            newValues.add(fValues.get(i));
            if(i < nbMaillesX || i > taille-nbMaillesX-1)
                newValues.set(i,newValues.get(i)+coefY*borderValuesX.get(i));
            if(i%nbMaillesX == 0){
                int q = i/nbMaillesX;
                newValues.set(i,newValues.get(i)+coefY*borderValuesY.get(q));
            }
            if(i%(nbMaillesX-1) == 0){
                int q = i/(nbMaillesX-1);
                newValues.set(i,newValues.get(i)+coefY*borderValuesY.get(nbMaillesY+q));
            }
        }
        Gauss resolve = new Gauss();
        return resolve.resolution(matrix, newValues);
    }
}
