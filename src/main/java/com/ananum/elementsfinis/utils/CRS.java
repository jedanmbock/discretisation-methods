/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ananum.elementsfinis.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JD
 */
public class CRS {
    private int order;
    private List<Double> values = new ArrayList<>();
    private List<Integer> colInd = new ArrayList<>();
    private List<Integer> rowPtr = new ArrayList<>();
    
    public CRS(double[][] matrix){
        int ptr = 0;
        for (double[] row: matrix){
            this.rowPtr.add(ptr);
            for (int i = 0; i<row.length; i++){
                double value = row[i];
                if(value != 0){
                    this.values.add(value);
                    this.colInd.add(i);
                    ptr++;
                }
            }
        }
        this.rowPtr.add(ptr);
        this.order = matrix.length;
    }
    
    public CRS(ArrayList<Double> values, ArrayList<Integer> colInd, ArrayList<Integer> rowPtr){
        this.values = values;
        this.colInd = colInd;
        this.rowPtr = rowPtr;
        this.order = rowPtr.size()-1;
    }
    
    public CRS(){
        
    }
    
    public double get(int row, int col)throws Exception{
        if(row<0 || row>order-1 ||col<0 || col>order-1)
            throw new Exception("Reférence inconnue!!!");
        
        int prec = this.rowPtr.get(row);
        int suiv = this.rowPtr.get(row+1);
        
        for (int cpt = prec; cpt<suiv; cpt++){
            if (this.colInd.get(cpt) == col)
                return this.values.get(cpt);
        }
        return 0;
        
    }
    
    public void set(int row, int col, double value) throws Exception{
        double current = this.get(row, col);
        if(current == 0){
            if(value == 0){
                return;
            }else{
                int prec = this.rowPtr.get(row);
                int suiv = this.rowPtr.get(row+1);

                if (col <= this.colInd.get(suiv-1)){
                    for (int cpt = prec; cpt<suiv; cpt++){
                        if (this.colInd.get(cpt) == col){
                            this.values.set(cpt, value);
                            this.colInd.add(cpt, col);
                            this.rowPtr.set(row+1, suiv+1);
                            break;
                        }else{
                            if(this.colInd.get(cpt)< col && this.colInd.get(cpt+1)> col && cpt<=suiv-1){
                                this.values.add(cpt+1, value);
                                this.colInd.add(cpt+1, col);
                                this.rowPtr.set(row+1, suiv+1);
                            }
                            if(this.colInd.get(cpt)> col){
                                this.values.add(cpt, value);
                                this.colInd.add(cpt, col);
                                for (int i = row+1; i<this.rowPtr.size(); i++)
                                    this.rowPtr.set(i, this.rowPtr.get(i)+1);
                            }
                        }
                    }
                }else{
                    this.values.add(suiv, value);
                    this.colInd.add(suiv, col);
                    for (int i = row+1; i<this.rowPtr.size(); i++)
                        this.rowPtr.set(i, this.rowPtr.get(i)+1);
                }
            }
        }else{
            if(value != 0){
                int prec = this.rowPtr.get(row);
                int suiv = this.rowPtr.get(row+1);

                for (int cpt = prec; cpt<suiv; cpt++){
                    if (this.colInd.get(cpt) == col){
                        this.values.set(cpt, value);
                        break;
                    }
                }
            }else{
                int prec = this.rowPtr.get(row);
                int suiv = this.rowPtr.get(row+1);

                for (int cpt = prec; cpt<suiv; cpt++){
                    if (this.colInd.get(cpt) == col){
                        this.values.remove(cpt);
                        this.colInd.remove(cpt);
                        for (int i = row+1; i<this.rowPtr.size(); i++)
                            this.rowPtr.set(i, this.rowPtr.get(i)-1);
                        break;
                    }
                }
            }
        }
    }
    
    public ArrayList<Double> timesVector(ArrayList<Double> vector){
        ArrayList<Double> result = new ArrayList();
        for (int i = 0; i<order; i++){
            result.add(0.0);
            int prec = this.rowPtr.get(i);
            int suiv = this.rowPtr.get(i+1);
            
            if (suiv > prec){
                for(int j = prec; j<=suiv; j++){
                    result.set(i, result.get(i)+
                            this.values.get(j)*vector.get(this.colInd.get(j)));
                }
            }
        }
        return result;
    }

    public int getOrder() {
        return order;
    }
    
    @Override
    public String toString(){
        return "Values: "+this.values+
                "\nCol_Index: "+this.colInd+
                "\nRow_Ptr: "+this.rowPtr+
                "\n Order: "+this.order;
    }
    
}
