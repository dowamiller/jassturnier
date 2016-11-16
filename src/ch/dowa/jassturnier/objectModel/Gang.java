/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.objectModel;

import java.util.ArrayList;

/**
 *
 * @author Dominik
 */
public class Gang {

    private int id;
    private int gangNr;
    private ArrayList<Spiel> spiele;
    private ArrayList<Team> teams;

    public void setSpiele(ArrayList<Spiel> spiele) {
        this.spiele = spiele;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public Gang(int id, int gangNr) {
        this.id = id;
        this.gangNr = gangNr;
        this.spiele = new ArrayList<>();
        this.teams = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public int getGangNr() {
        return gangNr;
    }
    
    public void addTeam(Team t){
        teams.add(t);
    }

    public ArrayList<Team> getTeams() {
        return teams; //To change body of generated methods, choose Tools | Templates.
    }

    public void addGame(Spiel s) {
        spiele.add(s);
    }

    public ArrayList<Spiel> getGames() {
        return spiele; //To change body of generated methods, choose Tools | Templates.
    }
}
