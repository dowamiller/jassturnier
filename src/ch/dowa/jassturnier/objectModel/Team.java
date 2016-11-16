/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.objectModel;

/**
 *
 * @author Dominik
 */
public class Team {
        private int id;
        private int punkte;
        private Spieler spieler1;
        private Spieler spieler2;

    public Team(int id, Spieler spieler1, Spieler spieler2) {
        this.id = id;
        this.punkte = 0;
        this.spieler1 = spieler1;
        this.spieler2 = spieler2;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    public int getId() {
        return id;
    }

    public Spieler getSpieler1() {
        return spieler1;
    }

    public Spieler getSpieler2() {
        return spieler2;
    }
}
