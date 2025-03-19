/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.elementsfinis.utils;

import java.util.ArrayList;

/**
 *
 * @author JD
 */
public class Gauss {
    
    public double abs(double nombre){
        if (nombre < 0)
            return -nombre;
        return nombre;
    }
    
    public ArrayList<Double> resolution(CRS matrix, ArrayList<Double> vecteur){
        //transformation de la matrice en une matrice sup-trangulaire
        ArrayList perm = this.elimination(matrix, vecteur);
        
        //Calcul de l'inconnue avec remontée
        ArrayList<Double> resultat = this.remontee((CRS)perm.get(0), (ArrayList<Double>)perm.get(1));
        
        return resultat;
    }
    
    public ArrayList<Double> elimination(CRS matrix, ArrayList<Double> vecteur){
        //Objectif transformation de matrix en matrice sup triangulaire
        ArrayList permutation = new ArrayList();
        try{
            for(int i = 0; i<matrix.getOrder()-1; i++){
                //Recherche du pivot partiel en (i;i)
                permutation = this.pivotPartiel(matrix, vecteur, i);

                //Elimination dans la colonne i de la matrice
                CRS newMatrix = (CRS)permutation.get(0);
                ArrayList<Double> newVector = (ArrayList<Double>)permutation.get(1);
                for (int k = i+1; k<matrix.getOrder(); k++){
                    double coef = newMatrix.get(k, i)/newMatrix.get(i, i);
                    for (int j = i; j<matrix.getOrder(); j++){
                        newMatrix.set(k, j, newMatrix.get(k, j)-coef*newMatrix.get(i,j));
                    }
                    newVector.set(k, newVector.get(k)-coef*newVector.get(i));
                }
                matrix = newMatrix;
                vecteur = newVector;
            }
            return permutation;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public ArrayList pivotPartiel(CRS matrix, ArrayList<Double> vecteur, int col){
        //Objectif: trouver le pivot idéal à l'application de l'élimination
        
        ArrayList permutation = new ArrayList();
        int indice_max = col;
        try{
            for (int i = col+1; i<matrix.getOrder(); i++){
                if(abs(matrix.get(i, col))>abs(matrix.get(indice_max, col)))
                    indice_max = i;
                if(indice_max != col){
                    for (int j = col; j<matrix.getOrder(); j++){
                        double aux = matrix.get(indice_max, j);
                        matrix.set(indice_max, j, matrix.get(col, j));
                        matrix.set(col, j, aux);
                    }
                    double aux = vecteur.get(indice_max);
                    vecteur.set(indice_max, vecteur.get(col));
                    vecteur.set(col, aux);
                }
            }
            permutation.add(matrix);
            permutation.add(vecteur);
            return permutation;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public ArrayList<Double> remontee(CRS matrix, ArrayList<Double> vecteur){
        ArrayList<Double> resultat = new ArrayList();
        for (double nombre: vecteur){
            resultat.add(0.0);
        }
        try{
            resultat.set(vecteur.size()-1, vecteur.get(vecteur.size()-1)/matrix.get(vecteur.size()-1, vecteur.size()-1));
            for (int i = vecteur.size()-2; -1<i; i--){
                double temp = 0;
                for (int j = i+1; j<vecteur.size(); j++){
                    temp += matrix.get(i, j)*resultat.get(j);
                }
                resultat.set(i,(vecteur.get(i)-temp)/matrix.get(i, i));
            }
            return resultat;
        }catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        
        
        
    }
}
