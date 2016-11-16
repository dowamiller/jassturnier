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
public class Spiel {

    private int id;
    private int tischNr;
    private int punkteTeam1;
    private int punkteTeam2;
    private Team team1;
    private Team team2;

    public Spiel(int id, int tischNr, Team team1, Team team2) {
        this.id = id;
        this.tischNr = tischNr;
        this.punkteTeam1 = 0;
        this.punkteTeam2 = 0;
        this.team1 = team1;
        this.team2 = team2;
    }

    public int getId() {
        return id;
    }

    public int getTischNr() {
        return tischNr;
    }

    public Team getTeam1() {
        return team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public int getPunkteTeam1() {
        return punkteTeam1;
    }

    public void setPunkteTeam1(int punkteTeam1) {
        this.punkteTeam1 = punkteTeam1;
    }

    public int getPunkteTeam2() {
        return punkteTeam2;
    }

    public void setPunkteTeam2(int punkteTeam2) {
        this.punkteTeam2 = punkteTeam2;
    }
}
