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
public class Spieler {

    private int id;
    private String vorname;
    private String nachname;

    public Spieler(int ID, String vorname, String nachname) {
        this.id = ID;
        this.nachname = nachname;
        this.vorname = vorname;
    }
    
    
    public int getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }
    
    

}
