/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.control;

import ch.dowa.jassturnier.ResourceLoader;
import ch.dowa.jassturnier.database.SQLDriver;
import ch.dowa.jassturnier.database.SQLQuerryExecutor;
import ch.dowa.jassturnier.objectModel.Gang;
import ch.dowa.jassturnier.objectModel.Spiel;
import ch.dowa.jassturnier.objectModel.Spieler;
import ch.dowa.jassturnier.objectModel.Team;
import ch.dowa.jassturnier.objectModel.Turnier;
import ch.dowa.jassturnier.view.JassturnierGui;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Dominik
 */
public class TurnierController {

    private Turnier actTurnier = null;
    private JassturnierGui myGui;
    private String vName = null;
    private String vLogoPath = null;

    public TurnierController(JassturnierGui gui) {
        myGui = gui;
        SQLDriver.getInstance().setGui(gui);
        vLogoPath = !ResourceLoader.readProperty("LOGOPATH").isEmpty() ? ResourceLoader.readProperty("LOGOPATH") : null;
    }

    public void setActTurnier(Turnier actTurnier) {
        this.actTurnier = actTurnier;
    }

    public Turnier getActTurnier() {
        return actTurnier;
    }
    
    public void setvName(String vName) {
        this.vName = vName;
    }

    public void setvLogoPath(String vLogoPath) {
        this.vLogoPath = vLogoPath;
    }

    public String getvName() {
        return vName;
    }

    public String getvLogoPath() {
        return vLogoPath;
    }

    public static final Turnier createAndSaveNewTurnier(String turnierTitel, int anzGaenge, int anzSpiele , int anzTische) {
        int id = SQLQuerryExecutor.getLastIDofTable("turnier") + 1;
        Turnier t = new Turnier(id, turnierTitel, anzTische, anzGaenge, anzSpiele);
        String[] values = {String.valueOf(id), "'" + turnierTitel + "'", String.valueOf(anzGaenge), String.valueOf(anzSpiele), String.valueOf(anzTische)};
        SQLQuerryExecutor.addValuesToTable("turnier", values);
        return t;
    }

    public void addAndSaveTurnierPlayers(int[] turnierPlayerIDs) {
        for (int id : turnierPlayerIDs) {
            String[] values = {String.valueOf(actTurnier.getId()), String.valueOf(id)};
            SQLQuerryExecutor.addValuesToTable("nehmenteil", values);
            actTurnier.addSpieler(SQLQuerryExecutor.getPlayerWithID(id));
        }
    }

    public void addAndSaveNewTurnierPlayers(int[] turnierPlayerIDs) {
        SQLQuerryExecutor.deletePlayerListfromTurnier(actTurnier.getId());
        actTurnier.removePlayers();
        for (int id : turnierPlayerIDs) {
            String[] values = {String.valueOf(actTurnier.getId()), String.valueOf(id)};
            SQLQuerryExecutor.addValuesToTable("nehmenteil", values);
            actTurnier.addSpieler(SQLQuerryExecutor.getPlayerWithID(id));
        }
    }

    public void startAndSaveNewGang() {
        int newGangID = SQLQuerryExecutor.getLastIDofTable("gang") + 1;
        Gang g = new Gang(newGangID, actTurnier.getAnzahlGaengeAktuell() + 1);
        String[] values = {
            String.valueOf(g.getId()),
            String.valueOf(g.getGangNr()),
            String.valueOf(actTurnier.getId())
        };
        SQLQuerryExecutor.addValuesToTable("gang", values);
        actTurnier.addGang(g);
        creatAndSaveNewRandomTeams(g);
        creatAndSaveNewRandomGames(g);
        myGui.refreshRankingTable();
        myGui.refreshGameTable(g);
    }

    public boolean checkGangFinished(Gang g) {
        for (Spiel s : g.getGames()) {
            if (s.getPunkteTeam1() == 0 && s.getPunkteTeam2() == 0) {
                return false;
            }
        }
        return true;
    }

    private void creatAndSaveNewRandomTeams(Gang g) {
        int newTeamID = SQLQuerryExecutor.getLastIDofTable("team") + 1;
        ArrayList<Spieler> tempPlayerlist = (ArrayList<Spieler>) actTurnier.getSpieler().clone();
        Team t;
        for (int i = 0; i < actTurnier.numberOfPlayers() / 2; i++) {
            t = new Team(newTeamID, tempPlayerlist.remove(randomIntBetween(0, tempPlayerlist.size())), tempPlayerlist.remove(randomIntBetween(0, tempPlayerlist.size())));
            SQLQuerryExecutor.saveNewTeam(t, g);
            g.addTeam(t);
            newTeamID++;
        }
    }

    private void creatAndSaveNewRandomGames(Gang g) {
        int newGameID = SQLQuerryExecutor.getLastIDofTable("spiel") + 1;
        ArrayList<Team> tempTeamlist = (ArrayList<Team>) g.getTeams().clone();
        Spiel s;
        int teamIndex = 0;
        for (int i = 0; i < actTurnier.getAnzahlTische(); i++) {
            s = new Spiel(newGameID, i + 1, g.getTeams().get(teamIndex), g.getTeams().get(teamIndex + 1));
            SQLQuerryExecutor.saveNewGame(s, g);
            g.addGame(s);
            newGameID++;
            teamIndex += 2;
        }

    }

    private int randomIntBetween(int lower, int upper) {
        Random r = new Random();
        return r.nextInt(upper - lower) + lower;
    }

    public void updatePointsFromGame(int gangNr, int tischNr, int newPoints1, int newPoints2) {
        Gang gang = null;
        Spiel spiel = null;
        for (Gang g : actTurnier.getGaenge()) {
            if (g.getGangNr() == gangNr) {
                gang = g;
                break;
            }
        }
        for (Spiel s : gang.getGames()) {
            if (s.getTischNr() == tischNr) {
                spiel = s;
                break;
            }
        }
        spiel.setPunkteTeam1(newPoints1);
        spiel.setPunkteTeam2(newPoints2);
        spiel.getTeam1().setPunkte(newPoints1);
        spiel.getTeam2().setPunkte(newPoints2);
        SQLQuerryExecutor.updateGame(spiel.getId(), newPoints1, newPoints2);
        SQLQuerryExecutor.updateTeam(spiel.getTeam1().getId(), newPoints1);
        SQLQuerryExecutor.updateTeam(spiel.getTeam2().getId(), newPoints2);
        myGui.refreshRankingTable();
    }

    public PlaceMappingType getPlaceMapping(int gangIndex) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> tables = new ArrayList<>();
        ArrayList<String> seats = new ArrayList<>();
        if (actTurnier.getAnzahlGaengeAktuell() > 0) {
            String[] placeLables = new String[4];
            placeLables[0] = ResourceLoader.readProperty("PLACE1");
            placeLables[1] = ResourceLoader.readProperty("PLACE2");
            placeLables[2] = ResourceLoader.readProperty("PLACE3");
            placeLables[3] = ResourceLoader.readProperty("PLACE4");
            ArrayList<Map<String, Object>> result = SQLQuerryExecutor.getAlphabeticalTurnierplayerList(actTurnier.getId());
            Gang g = actTurnier.getGaenge().get(gangIndex);
            for (Map m : result) {
                names.add((String) m.get("NACHNAME") + " " + (String) m.get("VORNAME"));
                int playerID = (Integer) m.get("ID");
                for (Spiel s : g.getGames()) {
                    if (s.getTeam1().getSpieler1().getId() == playerID) {
                        tables.add(String.valueOf(s.getTischNr()));
                        seats.add(placeLables[0]);
                        break;
                    } else if (s.getTeam1().getSpieler2().getId() == playerID) {
                        tables.add(String.valueOf(s.getTischNr()));
                        seats.add(placeLables[2]);
                        break;
                    } else if (s.getTeam2().getSpieler1().getId() == playerID) {
                        tables.add(String.valueOf(s.getTischNr()));
                        seats.add(placeLables[1]);
                        break;
                    } else if (s.getTeam2().getSpieler2().getId() == playerID) {
                        tables.add(String.valueOf(s.getTischNr()));
                        seats.add(placeLables[3]);
                        break;
                    }
                }
            }
        }
        return new PlaceMappingType(names, tables, seats);
    }

    public void loadTurnier(int selectedTurnierID) {
        setUpTurnier(selectedTurnierID);
        setUpPlayers(selectedTurnierID);
        setUpGaenge(selectedTurnierID);
        for (Gang g : actTurnier.getGaenge()) {
            setUpTeams(g);
            setUpGames(g);
        }
    }

    private void setUpTurnier(int selectedTurnierID) {
        actTurnier = SQLQuerryExecutor.getTurnierWithID(selectedTurnierID);
    }

    private void setUpPlayers(int selectedTurnierID) {
        actTurnier.setSpieler(SQLQuerryExecutor.getPlayerWithTurnierID(selectedTurnierID));
    }

    private void setUpGaenge(int selectedTurnierID) {
        actTurnier.setGaenge(SQLQuerryExecutor.getGaengeWithTurnierID(selectedTurnierID));
    }

    private void setUpTeams(Gang gang) {
        gang.setTeams(SQLQuerryExecutor.getTeams(gang.getId(), actTurnier.getSpieler()));
    }

    private void setUpGames(Gang gang) {
        gang.setSpiele(SQLQuerryExecutor.getGames(gang.getId(), gang.getTeams()));
    }

    public void changeName(int playerID, String newName, String newPrename) {
        for (Spieler s : actTurnier.getSpieler()) {
            if (s.getId() == playerID) {
                s.setNachname(newName);
                s.setVorname(newPrename);
                SQLQuerryExecutor.updatePlayerName(playerID, newName, newPrename);
                break;
            }
        }
        loadTurnier(actTurnier.getId());

    }
    
    public void changeDbPath(String newPath) throws Exception{
        if(actTurnier == null){
            SQLDriver.getInstance().setDataBasePath(newPath);
        } else {
            throw new Exception("Der Datenbankpfad kann nicht geändert werden wenn ein Turnier geöffnet ist.");
        }
    }

    public class PlaceMappingType {

        ArrayList<String> names;
        ArrayList<String> tables;
        ArrayList<String> seats;

        public PlaceMappingType(ArrayList<String> names, ArrayList<String> tables, ArrayList<String> seats) {
            this.names = names;
            this.tables = tables;
            this.seats = seats;
        }

        public ArrayList<String> getNames() {
            return names;
        }

        public ArrayList<String> getTables() {
            return tables;
        }
        
        public ArrayList<String> getSeats() {
            return seats;
        }

    }
}
