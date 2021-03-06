/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cplex;

import grafo.GrafoColorato;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.io.IOException;

/**
 *
 * @author user
 */
public class CPLEXModel {
    
    GrafoColorato grafo;
    
    public CPLEXModel (GrafoColorato pGrafo) {
        this.grafo = pGrafo;
    }
    
    public int esegui() throws IloException, IOException {
        int numColori = 0;
        
        int V = grafo.getNodi().size();
        int E = grafo.getArchi().size();
        int C = grafo.getListaColori().size();

        
        IloCplex modello = new IloCplex();

        //Variabili
        IloIntVar[] c_k = modello.boolVarArray(C);
        IloIntVar[] x_e = modello.boolVarArray(E);
        
        //Funzione obiettivo
        modello.addMinimize(modello.sum(c_k));

        //Vincoli
        
        //Vincolo sul numero di archi scelti affinché il sottografo sia un albero ricoprente
        modello.addEq(modello.sum(x_e), V - 1);

        //Vincolo sulla selezione dei colori
        for (int i = 0; i < E; i++) {
            for (int j = 0; j < grafo.getArchi().get(i).getColori().size(); j++) {
                modello.addGe(c_k[grafo.getArco(i).getColori().get(j)], x_e[i]);
            }
        }
        
        //Vincoli sulla ciclicità
        IloNumVar[] f_ij = modello.numVarArray(E * 2, 0, V - 1);
        
        
        IloLinearNumExpr v3 = modello.linearNumExpr();
        IloLinearNumExpr v4 = modello.linearNumExpr();
        
        for (int i = 0; i < E; i++) {
            if ((grafo.getArco(i).getDa().getChiave()== grafo.getNodo(0).getChiave() 
                    || (grafo.getArco(i).getA().getChiave()== grafo.getNodo(0).getChiave()))) {
                v3.addTerm(f_ij[i], 1);
                v4.addTerm(f_ij[i + E], 1);
            }
        }
        modello.addEq(v3, V - 1);
        modello.addEq(v4, 0);

        
        for (int i = 1; i < V; i++) {
            IloLinearNumExpr v5 = modello.linearNumExpr();
            for (int j = 0; j < E; j++) {
                if (grafo.getArco(j).getDa().getChiave() == i) {
                    v5.addTerm(f_ij[j], 1); //outcome
                    v5.addTerm(f_ij[j + E], -1); //income  
                } else if (grafo.getArco(j).getA().getChiave() == i) {
                    v5.addTerm(f_ij[j], -1);    //income 
                    v5.addTerm(f_ij[j + E], 1); //outcome      
                }
            }
            modello.addEq(v5, -1);
        }
        
        
        //Vincolo di unione tra flussi e archi
        for (int i = 0; i < E; i++) {
            IloLinearIntExpr v6 = modello.linearIntExpr();
            v6.addTerm(x_e[i], V - 1);
            modello.addLe(f_ij[i], v6);
            modello.addLe(f_ij[i + E], v6);
        }

        
        //Execute
        if (modello.solve()) {
            numColori = (int) modello.getObjValue();
            System.out.println("Soluzione: " + (modello.getObjValue()));
            System.out.println("Num Archi: " + x_e.length);
            System.out.println("Lista Archi");
            for(int i=0; i<x_e.length; i++){
                if(modello.getValue(x_e[i])==1){
                    System.out.print(grafo.getArco(i).getDa().getChiave()+ " " + grafo.getArco(i).getA().getChiave() + " Colori:");
                    for (Integer colore : grafo.getArco(i).getColori()) {
                        System.out.print(" " + colore);
                    }
                    System.out.print("\n");
                }
            }
        } else {
            System.out.println("Errore:" + modello.getStatus());
        }

        modello.end();

        return numColori;
    }
    
    
    public int eseguiVersioneFixata() throws IloException, IOException {
        int numColori = 0;
        
        int V = grafo.getNodi().size();
        int E = grafo.getArchi().size();
        int C = grafo.getListaColori().size();

        
        IloCplex modello = new IloCplex();

        //Variabili
        IloIntVar[] c_k = modello.boolVarArray(C);
        IloIntVar[] x_e = modello.boolVarArray(E);
        
        //Funzione obiettivo
        modello.addMinimize(modello.sum(c_k));

        //Vincoli
        
        //Vincolo sul numero di archi scelti affinché il sottografo sia un albero ricoprente
        modello.addEq(modello.sum(x_e), V - 1);

        //Vincolo sulla selezione dei colori
        for (int i = 0; i < E; i++) {
            for (int j = 0; j < grafo.getArco(i).getColori().size(); j++) {
                modello.addGe(c_k[grafo.getArco(i).getColori().get(j)], x_e[i]);
            }
        }
        
        //Vincoli sulla ciclicità
        IloNumVar[] f_ij = modello.numVarArray(E * 2, 0, V);
        
        
        IloLinearNumExpr v3 = modello.linearNumExpr();
        
        for (int i = 0; i < E; i++) {
            if ((grafo.getArco(i).getDa().getChiave()== grafo.getNodo(0).getChiave() 
                    || (grafo.getArco(i).getA().getChiave()== grafo.getNodo(0).getChiave()))) {
                v3.addTerm(f_ij[i], 1);
            }
        }
        modello.addEq(v3, V - 1);

        
        for (int i = 1; i < V; i++) {
            IloLinearNumExpr v4 = modello.linearNumExpr();
            for (int j = 0; j < E; j++) {
                if (grafo.getArco(j).getDa().getChiave() == i) {
                    v4.addTerm(f_ij[j], 1); //outcome
                    v4.addTerm(f_ij[j + E], -1); //income  
                } else if (grafo.getArco(j).getA().getChiave() == i) {
                    v4.addTerm(f_ij[j], -1);    //income 
                    v4.addTerm(f_ij[j + E], 1); //outcome      
                }
            }
            modello.addEq(v4, -1);
        }
        
        
        //Vincolo di unione tra flussi e archi
        for (int i = 0; i < E; i++) {
            IloLinearIntExpr v5 = modello.linearIntExpr();
            v5.addTerm(x_e[i], V);
            modello.addLe(f_ij[i], v5);
            modello.addLe(f_ij[i + E], v5);
        }
        
        //Execute
        if (modello.solve()) {
            numColori = (int) modello.getObjValue();
            System.out.println("Soluzione: " + (modello.getObjValue()));
            System.out.println("Num Archi: " + x_e.length);
            System.out.println("Lista Archi");
            for(int i=0; i<x_e.length; i++){
                if(modello.getValue(x_e[i])==1){
                    System.out.print(grafo.getArco(i).getDa().getChiave()+ " " + grafo.getArco(i).getA().getChiave() + " Colori:");
                    for (Integer colore : grafo.getArco(i).getColori()) {
                        System.out.print(" " + colore);
                    }
                    System.out.print("\n");
                }
            }
        } else {
            System.out.println("Errore:" + modello.getStatus());
        }

        modello.end();

        return numColori;
    }

}
