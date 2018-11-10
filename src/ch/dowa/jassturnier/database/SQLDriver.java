/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.database;

import ch.dowa.jassturnier.ResourceLoader;
import ch.dowa.jassturnier.view.JassturnierGui;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


/**
 *
 * @author Dominik
 */
public class SQLDriver {

    private static SQLDriver instance = null;
    private String url;
    private String user;
    private String password;
    private JassturnierGui gui;

    public void setGui(JassturnierGui gui) {
        this.gui = gui;
    }

    private SQLDriver() {
        url = readProperties()[0] + "Jassturnier";
        user = readProperties()[1];
        password =  readProperties()[2];
        setUpTablesIfNecessairy();
        insertNamesIfNecessairy();
    }

    public static final SQLDriver getInstance() {
        if (instance == null) {
            instance = new SQLDriver();
        }
        return instance;
    }

//    public final boolean connect(String url, String user, String password) {
//        boolean faild = true;
//        try {
//            connection = DriverManager.getConnection(url+ "heroku_1231147e1ab43d6?reconnect=true", user, password);
//            faild = false;
//        } catch (SQLException e) {
//            Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, e);
//        }
//        return faild;
//    }

    public final ArrayList<Map<String, Object>> executeQuerry(String querry) {
        ResultSet result = null;
        Connection con = null;
        Statement myStmt = null;
        HashMap<String, Object> map;
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<Map<String, Object>> results = new ArrayList<>();
        try {
            
            con = DriverManager.getConnection(url);
            myStmt = con.createStatement();
            result = myStmt.executeQuery(querry);
            ResultSetMetaData columns = result.getMetaData();
            for (int i = 0; i < columns.getColumnCount(); i++) {
                columnNames.add(columns.getColumnName(i+1));
            }
            while (result.next()) {
                map = new HashMap();
                for (String columnName : columnNames){
                    map.put(columnName, result.getObject(columnName));
                }
                results.add(map);
            }
        }
        catch (SQLException sqle) {
            JOptionPane.showMessageDialog(gui, "Die Datenbank abfrage konnt nicht ausgeführt werden");
            Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, sqle);
        } finally {
            try {    
                if (result != null) {
                    result.close();
                }
                if (myStmt != null) {
                    myStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } 
            catch(SQLException sqle) {
                Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, sqle);
            }
        }

        return results;
    }

    public final int executeUpdate(String querry) {
        int result = 0;
        Connection con = null;
        Statement myStmt = null;
        try {
            con = DriverManager.getConnection(url);
            con.setAutoCommit(false);
            myStmt = con.createStatement();
            result = myStmt.executeUpdate(querry);
            con.commit();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(gui, "Die Datenbank konnte nicht aktualisiert werden");
            try {
                if (con != null) {
                    con.rollback();
                    Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, e);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally {
            try {    
                if (myStmt != null) {
                    myStmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } 
            catch(SQLException sqle) {
                Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, sqle);
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
    
    private void setUpTablesIfNecessairy() {
        ArrayList<String> createQuerrys = readCreateQuerrys();
        createQuerrys.forEach((s) -> {
            executeUpdate(s);
        });
    }
    

    private void insertNamesIfNecessairy() {
       String selectQuerry = "select count(*) from spieler";
       ArrayList<Map<String, Object>> result = executeQuerry(selectQuerry);
       if ((Long) result.get(0).get("COUNT(*)") == 0){
           ArrayList<String> updateQuerrys = createUpdateQuerrys(readNames());
           updateQuerrys.forEach((s) -> {
               executeUpdate(s);
           });
       }
    }
        
    private ArrayList<String> readCreateQuerrys() {
        ArrayList<String> querrys = new ArrayList<>();
        InputStreamReader inputStream = null;
        try {
            inputStream = ResourceLoader.getSqlFile("createquerry.sql");
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
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return querrys;
    }

    private ArrayList<String> readNames() {
        ArrayList<String> names = new ArrayList<>();
        InputStreamReader inputStream = null;
        try {
            inputStream = new InputStreamReader(new FileInputStream("spieler.txt"), StandardCharsets.UTF_8.name());
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
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(SQLDriver.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            querry = "insert into spieler values(" + String.valueOf(i) + ", '" + nameParts[1] + "', '" + nameParts[0] + "')";
            querrys.add(querry);
            i++;
        }
        return querrys;
    }

}
