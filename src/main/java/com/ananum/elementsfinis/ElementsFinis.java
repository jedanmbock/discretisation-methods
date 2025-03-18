/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.ananum.elementsfinis;

import com.ananum.elementsfinis.utils.CRS;
import com.ananum.elementsfinis.utils.Gauss;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author JD
 */
public class ElementsFinis {
    
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
        System.out.println(newValues);
        Gauss resolve = new Gauss();
        return resolve.resolution(matrix, newValues);
    }

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

        System.out.println("Vérification differncesFinies.");
        ArrayList<Double> vecteur = new ArrayList();
        for(int i = 0; i<10; i++){
            vecteur.add(-2.0);
        }
        ArrayList<Double> expected = new ArrayList();
        for(int i = 0; i<10; i++){
            expected.add((i+1)*(i+1)/(11.0*11.0));
        }
        
        
        System.out.println("Obtenus: "+ElementsFinis.differencesFinies(11,vecteur,0,1));
        System.out.println("Attendus: "+expected);
    }
}
