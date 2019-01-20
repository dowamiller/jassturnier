package ch.dowa.jassturnier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.io.IOUtils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author domi_
 */
public class ResourceLoader {

    static ResourceLoader rl = new ResourceLoader();

    public static InputStreamReader getSqlFile(String filename) {
        return new InputStreamReader(rl.getClass().getResourceAsStream("sql/" + filename));
    }
    
    public static byte[] getIcon(String filename){
        try {
            return IOUtils.toByteArray(rl.getClass().getResourceAsStream("icons/" + filename));
        } catch (IOException ex) {
            Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String readProperty(String propertyName) {
        String result = null;
        Properties prop = new Properties();
        String fileName = "app.config";
        InputStream in;
        OutputStream out;
        File f;
        try {
            in = new FileInputStream(fileName);
            prop.load(in);
            result = prop.getProperty(propertyName);
        } catch (FileNotFoundException ex) {
            System.out.println("File app.config not found. Create one.");
            prop = getDefaultProperties();
            result = prop.getProperty(propertyName);
            f = new File(fileName);
            try {
                out = new FileOutputStream(f);
                prop.store(out, fileName);
                out.close();
                Files.setAttribute(f.toPath(), "dos:hidden", true);
            } catch (IOException ex1) {
                Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ex) {
            System.out.println("Unable to open File app.config ");
        }
        return result;
    }

    public static Properties readProperties() {
        Properties prop = new Properties();
        String fileName = "app.config";
        InputStream in;
        OutputStream out;
        File f;
        try {
            in = new FileInputStream(fileName);
            prop.load(in);
            in.close();
        } catch (IOException ex) {
            System.out.println("Unable to open File app.config ");
        }
        return prop;
    }

    public static void writeProperties(Properties newProps) {
        String fileName = "app.config";
        OutputStream out;
        File f;
        try {
            f = new File(fileName);
            Files.setAttribute(f.toPath(), "dos:hidden", false);
            out = new FileOutputStream(f);
            newProps.store(out, fileName);
            out.close();
            Files.setAttribute(f.toPath(), "dos:hidden", true);
        } catch (IOException ex) {
            Logger.getLogger(ResourceLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static Properties getDefaultProperties() {
        String db = "jdbc:h2:";
        System.out.println(db);
        Properties prop = new Properties();
        prop.setProperty("DB", db);
        prop.setProperty("DBPATH", "./data/");
        prop.setProperty("PLACE1", "A");
        prop.setProperty("PLACE2", "B");
        prop.setProperty("PLACE3", "C");
        prop.setProperty("PLACE4", "D");
        prop.setProperty("LOGOPATH", "");
        return prop;
    }
}
