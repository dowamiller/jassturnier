/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dominik
 */
public class SQLDriver {

    private Connection connection = null;
    private static SQLDriver instance = null;

    private SQLDriver() {
        readProperties();
        connect(readProperties()[0], readProperties()[1], readProperties()[2]);

    }

    public static final SQLDriver getInstance() {
        if (instance == null) {
            instance = new SQLDriver();
        }
        return instance;
    }

    public final void connect(String url, String user, String password) {
        boolean faild = true;
        try {
            connection = DriverManager.getConnection(url + "jassturnier", user, password);
            faild = false;
        } catch (Exception e) {
            //Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, e);
        }
        if (faild) {
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException ex) {
                Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, ex);
            }
            creatDatabaseAndConnect(url, user, password);
        }
    }

    public final ResultSet executeQuerry(String querry) {
        ResultSet result = null;
        if (connection != null) {
            try {
                Statement myStmt = connection.createStatement();
                result = myStmt.executeQuery(querry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public final int executeUpdate(String querry) {
        int result = 0;
        if (connection != null) {
            try {
                Statement myStmt = connection.createStatement();
                result = myStmt.executeUpdate(querry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * Reads Properties from the propertie File
     */
    private static String[] readProperties() {
        String[] result = new String[3];
        Properties prop = new Properties();
        String fileName = "app.config";
        try {
            InputStream stream = new FileInputStream(fileName);
            prop.load(stream);
            result[0] = prop.getProperty("MySQLServerUrl");
            result[1] = prop.getProperty("User");
            result[2] = prop.getProperty("Password");
        } catch (FileNotFoundException ex) {
            System.out.println("File app.config not found");
        } catch (IOException ex) {
            System.out.println("Unable to open File app.config ");
        }
        return result;
    }

    public static String[] readPlaces() {
        String[] places = new String[4];
        Properties prop = new Properties();
        String fileName = "app.config";
        try {
            InputStream stream = new FileInputStream(fileName);
            prop.load(stream);
            places[0] = prop.getProperty("Place1");
            places[1] = prop.getProperty("Place2");
            places[2] = prop.getProperty("Place3");
            places[3] = prop.getProperty("Place4");
        } catch (FileNotFoundException ex) {
            System.out.println("File app.config not found");
        } catch (IOException ex) {
            System.out.println("Unable to open File app.config ");
        }
        return places;
    }

    private void creatDatabaseAndConnect(String url, String user, String password) {
        executeUpdate("create database Jassturnier");
        connect(url, user, password);
        ArrayList<String> createQuerrys = readCreateQuerrys();
        for (String s : createQuerrys) {
            executeUpdate(s);
        }
        ArrayList<String> updateQuerrys = createUpdateQuerrys(readNames());
        for (String s : updateQuerrys) {
            executeUpdate(s);
        }
    }

    private ArrayList<String> readCreateQuerrys() {
        ArrayList<String> querrys = new ArrayList<>();
        FileReader inputStream = null;
        try {
            inputStream = new FileReader("createquerry.sql");
            int c;
            StringBuilder builder = new StringBuilder();
            while ((c = inputStream.read()) != -1) {
                char ch = (char) c;
                if (ch != '\n' && ch != '\t') {
                    if (ch == ';') {
                        querrys.add(builder.toString());
                        builder.setLength(0);
                    } else {
                        builder.append(ch);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return querrys;
    }

    private ArrayList<String> readNames() {
        ArrayList<String> names = new ArrayList<>();
        FileReader inputStream = null;
        try {
            inputStream = new FileReader("spieler.txt");
            int c;
            StringBuilder builder = new StringBuilder();
            while ((c = inputStream.read()) != -1) {
                char ch = (char) c;
                if (ch != '\n' && ch != '\t' && ch != '\r') {
                    if (ch == ';') {
                        names.add(builder.toString());
                        builder.setLength(0);
                    } else {
                        builder.append(ch);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, ex);
        }
        Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    private ArrayList<String> createUpdateQuerrys(ArrayList<String> names) {
        String querry = null;
        int i = 1;
        ArrayList<String> querrys = new ArrayList<>();
        for (String s : names) {
            String[] nameParts = s.split(" ");
            querry = "insert into spieler values(" + String.valueOf(i) + ", \"" + nameParts[1] + "\", \"" + nameParts[0] + "\")";
            querrys.add(querry);
            i++;
        }
        return querrys;
    }
}
