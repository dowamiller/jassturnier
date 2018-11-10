package ch.dowa.jassturnier;


import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

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
        return new InputStreamReader(rl.getClass().getResourceAsStream("sql/"+filename));
    }
}
