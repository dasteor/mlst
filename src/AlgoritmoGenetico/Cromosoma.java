/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AlgoritmoGenetico;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Daniel
 */
public class Cromosoma extends ArrayList<Integer> {
    
    private int valoreDiFitness;
    private int valoreDiFitnessDiBackup;
    
    public Cromosoma () { 
        super();
        this.valoreDiFitness = 0;
        this.valoreDiFitnessDiBackup = 0;
    }
    
    public int getValoreFunzioneDiFitness () {
        return this.valoreDiFitness;
    }
    
    public void setValoreFunzioneDiFitness (int ff) {
        this.valoreDiFitness = ff;
        this.valoreDiFitnessDiBackup = ff;
    }
    
    public void incrementaValoreFunzioneDiFitness (int incremento) {
        this.valoreDiFitness += incremento;
    }
    
    public void ripristinaValoreFunzioneDiFitness () {
        this.valoreDiFitness = this.valoreDiFitnessDiBackup;
    }
    
    
}