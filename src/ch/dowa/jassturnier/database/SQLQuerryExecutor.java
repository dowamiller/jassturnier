/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.database;

import ch.dowa.jassturnier.objectModel.Gang;
import ch.dowa.jassturnier.objectModel.Spiel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ch.dowa.jassturnier.objectModel.Spieler;
import ch.dowa.jassturnier.objectModel.Team;
import ch.dowa.jassturnier.objectModel.Turnier;
import java.util.ArrayList;

/**
 *
 * @author Dominik
 */
public class SQLQuerryExecutor {

    public final static int getLastIDofTable(String table) {
        String querry = "select ID from " + table + " order by ID desc";
        ResultSet resultSet = SQLDriver.getInstance().executeQuerry(querry);
        try {
            if (resultSet.first()) {
                return resultSet.getInt("ID");
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public final static void addValuesToTable(String table, String[] values) {
        String querry = "insert into " + table + " values(";
        for (int i = 0; i < values.length; i++) {
            querry = querry + values[i];
            if (i != values.length - 1) {
                querry = querry + ", ";
            } else {
                querry = querry + ")";
            }
        }
        SQLDriver.getInstance().executeUpdate(querry);
    }

    public final static ResultSet getValuesFromTable(String table) {
        String querry = "select * from " + table + "";
        return SQLDriver.getInstance().executeQuerry(querry);
    }

    public final static Spieler getPlayerWithID(int id) {
        String querry = "select * from spieler where id = " + String.valueOf(id) + "";
        ResultSet resultSet = SQLDriver.getInstance().executeQuerry(querry);
        try {
            resultSet.first();
            return new Spieler(resultSet.getInt("ID"), resultSet.getString("Vorname"), resultSet.getString("Nachname"));
        } catch (SQLException ex) {
            Logger.getLogger(SQLQuerryExecutor.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static ArrayList<Spieler> getPlayerWithTurnierID(int id) {
        ArrayList<Spieler> players = new ArrayList<>();
        ArrayList<Integer> playerIDs = new ArrayList<>();
        String querry = "select * from nehmenteil where Turnier = " + String.valueOf(id) + "";
        ResultSet resultSet = SQLDriver.getInstance().executeQuerry(querry);
        try {
            while (resultSet.next()) {
                playerIDs.add(resultSet.getInt("Spieler"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLQuerryExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (int playerID : playerIDs) {
            players.add(getPlayerWithID(playerID));
        }
        return players;
    }

    public final static Turnier getTurnierWithID(int id) {
        String querry = "select * from turnier where id = " + String.valueOf(id) + "";
        ResultSet resultSet = SQLDriver.getInstance().executeQuerry(querry);
        try {
            resultSet.first();
            return new Turnier(resultSet.getInt("ID"), resultSet.getInt("Jahr"), resultSet.getInt("anzahlTische"));
        } catch (SQLException ex) {
            Logger.getLogger(SQLQuerryExecutor.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static ArrayList<Gang> getGaengeWithTurnierID(int id) {
        ArrayList<Gang> gaenge = new ArrayList<>();
        String querry = "select * from gang where Turnier = " + String.valueOf(id) + "";
        ResultSet resultSet = SQLDriver.getInstance().executeQuerry(querry);
        try {
            while (resultSet.next()) {
                gaenge.add(new Gang(resultSet.getInt("ID"), resultSet.getInt("GangNR")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLQuerryExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gaenge;
    }

    public static ArrayList<Team> getTeams(int id, ArrayList<Spieler> spieler) {
        ArrayList<Team> teams = new ArrayList<>();
        String querry = "select * from team where Gang = " + String.valueOf(id) + "";
        ResultSet resultSet = SQLDriver.getInstance().executeQuerry(querry);
        try {
            while (resultSet.next()) {
                Spieler spieler1 = null;
                Spieler spieler2 = null;
                for (Spieler s : spieler) {
                    if (s.getId() == resultSet.getInt("Spieler1")) {
                        spieler1 = s;
                    } else if (s.getId() == resultSet.getInt("Spieler2")) {
                        spieler2 = s;
                    }
                }
                Team team = new Team(resultSet.getInt("ID"), spieler1, spieler2);
                team.setPunkte(resultSet.getInt("Punkte"));
                teams.add(team);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLQuerryExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return teams;
    }

    public static ArrayList<Spiel> getGames(int id, ArrayList<Team> teams) {
        ArrayList<Spiel> games = new ArrayList<>();
        String querry = "select * from spiel where Gang = " + String.valueOf(id) + "";
        ResultSet resultSet = SQLDriver.getInstance().executeQuerry(querry);
        try {
            while (resultSet.next()) {
                Team team1 = null;
                Team team2 = null;
                for (Team t : teams) {
                    if (t.getId() == resultSet.getInt("Team1")) {
                        team1 = t;
                    } else if (t.getId() == resultSet.getInt("Team2")) {
                        team2 = t;
                    }
                }
                Spiel game = new Spiel(resultSet.getInt("ID"), resultSet.getInt("TischNr"), team1, team2);
                game.setPunkteTeam1(resultSet.getInt("Punkte1"));
                game.setPunkteTeam2(resultSet.getInt("Punkte2"));
                games.add(game);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLQuerryExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return games;
    }

    public final static void saveNewTeam(Team t, Gang g) {
        String[] values = {
            String.valueOf(t.getId()),
            String.valueOf(t.getPunkte()),
            String.valueOf(t.getSpieler1().getId()),
            String.valueOf(t.getSpieler2().getId()),
            String.valueOf(g.getId())
        };
        SQLQuerryExecutor.addValuesToTable("team", values);
    }

    public static void saveNewGame(Spiel s, Gang g) {
        String[] values = {
            String.valueOf(s.getId()),
            String.valueOf(s.getTischNr()),
            String.valueOf(g.getId()),
            String.valueOf(s.getPunkteTeam1()),
            String.valueOf(s.getPunkteTeam2()),
            String.valueOf(s.getTeam1().getId()),
            String.valueOf(s.getTeam2().getId())
        };
        SQLQuerryExecutor.addValuesToTable("spiel", values);
    }

    public final static ResultSet getRanking(int turnierID, int gangNr) {
        String querry = "select s.Vorname, s.Nachname, sum(t.Punkte) "
                + "from spieler s, team t, gang g, turnier tu "
                + "where (s.ID = t.Spieler1 or s.ID = t.Spieler2) "
                + "and t.gang = g.ID and (";
        for (int i = 1; i <= gangNr; i++) {
            if (i != gangNr) {
                querry += "g.gangNr = " + String.valueOf(i) + " or ";
            }else {
                querry += "g.gangNr = " + String.valueOf(i) + ") ";
            }
        }
        querry += "and g.turnier = tu.ID "
                + "and tu.ID = " + String.valueOf(turnierID) + " "
                + "group by s.ID "
                + "order by sum(t.punkte) desc";
        return SQLDriver.getInstance().executeQuerry(querry);
    }

    public final static void updateGame(int gameID, int points1, int points2) {
        String querry = "update spiel "
                + "set Punkte1 = " + String.valueOf(points1) + ", Punkte2 = " + String.valueOf(points2) + " "
                + "where ID = " + String.valueOf(gameID);
        SQLDriver.getInstance().executeUpdate(querry);

    }

    public final static void updateTeam(int teamID, int points) {
        String querry = "update team "
                + "set Punkte = " + String.valueOf(points) + " "
                + "where ID = " + String.valueOf(teamID);
        SQLDriver.getInstance().executeUpdate(querry);
    }

    public static ResultSet getAlphabeticalTurnierplayerList(int turnierID) {
        String querry = "select s.ID, s.Nachname, s.Vorname from spieler s, nehmenteil n "
                + "where s.ID = n.Spieler and n.Turnier = " + String.valueOf(turnierID) + " order by s.Nachname asc, s.Vorname";
        return SQLDriver.getInstance().executeQuerry(querry);
    }

    public static String[] getPlaces() {
        return SQLDriver.readPlaces();
    }

    public final static void deleteTurnier(int turnierID) {
        String querry = "delete from turnier "
                + "where ID = " + String.valueOf(turnierID);
        SQLDriver.getInstance().executeUpdate(querry);
    }

    public final static void deletePlayerListfromTurnier(int turnierID) {
        String querry = "delete from nehmenteil "
                + "where Turnier = " + String.valueOf(turnierID);
        SQLDriver.getInstance().executeUpdate(querry);
    }

    public final static void updateNumbersOfTablesFromTurnier(int nofTables, int turnierID) {
        String querry = "update turnier "
                + "set anzahlTische = " + String.valueOf(nofTables) + " "
                + "where ID = " + String.valueOf(turnierID);
        SQLDriver.getInstance().executeUpdate(querry);
    }

    public static void updatePlayerName(int playerID, String newName, String newPrename) {
        String querry = "update spieler "
                + "set Vorname = \"" + newPrename + "\", Nachname = \"" + newName + "\" "
                + "where ID = " + String.valueOf(playerID);
        SQLDriver.getInstance().executeUpdate(querry);
    }
}
