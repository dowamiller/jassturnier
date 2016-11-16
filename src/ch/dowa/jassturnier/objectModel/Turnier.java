/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.objectModel;

import java.util.ArrayList;
import ch.dowa.jassturnier.database.SQLQuerryExecutor;

/**
 *
 * @author Dominik
 */
public class Turnier {
    
    private int id;
    private int anzahlTische;
    private int jahr;
    private ArrayList<Spieler> spieler;
    private ArrayList<Gang> gaenge;

    public void setSpieler(ArrayList<Spieler> spieler) {
        this.spieler = spieler;
    }

    public void setGaenge(ArrayList<Gang> gaenge) {
        this.gaenge = gaenge;
    }

    
    public Turnier(int ID, int Jahr, int anzTische ){
        this.id = ID;
        this.jahr = Jahr;
        this.anzahlTische = anzTische;
        this.spieler = new ArrayList<>();
        this.gaenge = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getAnzahlTische() {
        return anzahlTische;
    }

    public int getJahr() {
        return jahr;
    }
    
    public void addSpieler (Spieler s){
        this.spieler.add(s);
    }
    
    public void addGang(Gang g){
        this.gaenge.add(g);
    }
    
    public int numberOfGaenge(){
        return gaenge.size();
    }
    
    public int numberOfPlayers(){
        return spieler.size();
    }

    public ArrayList<Spieler> getSpieler() {
        return spieler;
    }
        public ArrayList<Gang> getGaenge() {
        return gaenge;
    }
        
    public void removePlayers(){
        spieler = new ArrayList<>();
    }
    
}
