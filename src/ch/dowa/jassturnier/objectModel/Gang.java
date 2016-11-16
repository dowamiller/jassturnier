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
    private ArrayList<Spiel> games;
    private ArrayList<Team> teams;

    public void setSpiele(ArrayList<Spiel> spiele) {
        this.games = spiele;
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
    }

    public Gang(int id, int gangNr) {
        this.id = id;
        this.gangNr = gangNr;
        this.games = new ArrayList<>();
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
        games.add(s);
    }

    public ArrayList<Spiel> getGames() {
        return games; //To change body of generated methods, choose Tools | Templates.
    }
}
