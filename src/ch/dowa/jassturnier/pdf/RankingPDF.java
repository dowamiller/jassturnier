/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.pdf;

import ch.dowa.jassturnier.ResourceLoader;
import static ch.dowa.jassturnier.pdf.PdfUtils.BACKGROUND_HEADER;
import static ch.dowa.jassturnier.pdf.PdfUtils.BACKGROUND_ROW_EVEN;
import static ch.dowa.jassturnier.pdf.PdfUtils.BACKGROUND_ROW_ODD;
import static ch.dowa.jassturnier.pdf.PdfUtils.STANDART_FONT;
import static ch.dowa.jassturnier.pdf.PdfUtils.STANDART_FONT_BOLD;
import static ch.dowa.jassturnier.pdf.PdfUtils.TABEL_WIDTH;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;
import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

/**
 *
 * @author domi_
 */
public class RankingPdf {
    
    public static void exportRanking(HashMap<Integer, String> names,  HashMap<Integer, ArrayList<Long>> points, int gangNr, String year)throws IOException{
        LinkedHashMap<Integer, ArrayList<Long>> sortedPoints; 
        sortedPoints = points
                .entrySet()
                .stream()
                .sorted((Map.Entry<Integer, ArrayList<Long>> e1, Map.Entry<Integer, ArrayList<Long>> e2) -> {
                    ArrayList<Long> pointList1 = e1.getValue();
                    ArrayList<Long> pointList2 = e2.getValue();
                    Long points1 = pointList1.get(pointList1.size() - 1);
                    Long points2 = pointList2.get(pointList2.size() - 1);
                    return points2.compareTo(points1);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(oldValue, newValue) -> {
                    return oldValue;
                }, LinkedHashMap::new));
        sortedPoints.keySet().forEach(id -> System.out.println(names.get(id) + points.get(id).toString()));
        
        String vName = !ResourceLoader.readProperty("VNAME").isEmpty() ? ResourceLoader.readProperty("VNAME") : null;
        String outputFileName;
        outputFileName =  vName != null ? vName.replace(' ', '_') + "_" : "";
        outputFileName += "Jassturnier_" + year + "_" + (gangNr == 4 ? "Schlussrangliste" : "Zwischenrangliste_Gang_" + String.valueOf(gangNr)) + ".pdf";
        String titel = ((vName != null) ? vName + " " : "") 
                + "Jassturnier " + year + " - " 
                + (gangNr == 4 ? "Schlussrangliste" : "Zwischenrangliste Gang " + String.valueOf(gangNr));
        
        Table.TableBuilder tableBuilder = Table.builder();
        tableBuilder = tableBuilder.addColumnOfWidth((float) (TABEL_WIDTH * 0.1));
        tableBuilder = tableBuilder.addColumnOfWidth((float) (TABEL_WIDTH * (1 - (0.1 * (gangNr + 1)))));
        for( int i = 1 ; i <= gangNr ; i++ ){
            tableBuilder = tableBuilder.addColumnOfWidth((float) (TABEL_WIDTH * 0.1));
        } 
        tableBuilder.fontSize(10)
            .font(STANDART_FONT)
            .borderColor(Color.WHITE);
        
        int rang = 1;
        Long lastPoints = 0L;
        int nofEqualPoints = 0;
        
        Row.RowBuilder headerRowBuilder = Row.builder()
            .add(CellText.builder().text("Rang").horizontalAlignment(LEFT).borderWidth(1).build())
            .add(CellText.builder().text("Spieler").horizontalAlignment(LEFT).borderWidth(1).build());
        
        for(int i = 1; i <= gangNr; i++){
            headerRowBuilder = headerRowBuilder.add(CellText.builder().text(String.valueOf(i) + ". Gang").horizontalAlignment(LEFT).borderWidth(1).build());
        }
        
        final Row headerRow = headerRowBuilder.backgroundColor(BACKGROUND_HEADER)
            .textColor(Color.WHITE)
            .font(STANDART_FONT_BOLD).fontSize(12)
            .build();
        
        tableBuilder.addRow(headerRow);
        int j = 0;
        for(Integer index : sortedPoints.keySet()){
            ArrayList<Long> pointList = sortedPoints.get(index);
            Long actPoints = pointList.get(pointList.size() - 1);
            rang = Objects.equals(actPoints, lastPoints) ? rang : rang + nofEqualPoints;
            nofEqualPoints = Objects.equals(actPoints, lastPoints) ? nofEqualPoints + 1 : 1;
            lastPoints = actPoints;
            String playerName = names.get(index);
            Row.RowBuilder rowBuilder = Row.builder();
            rowBuilder = rowBuilder.add(CellText.builder().font(STANDART_FONT_BOLD).text(String.valueOf(rang)).horizontalAlignment(LEFT).borderWidth(1).build())
                .add(CellText.builder().font(STANDART_FONT).text(playerName).horizontalAlignment(LEFT).borderWidth(1).build());
            for( int i = 0; i < pointList.size() ; i++) {
                if(i == pointList.size() - 1){
                    rowBuilder = rowBuilder.add(CellText.builder().font(STANDART_FONT_BOLD).text(String.valueOf(pointList.get(i))).horizontalAlignment(RIGHT).borderWidth(1).build());
                } else {
                    rowBuilder = rowBuilder.add(CellText.builder().font(STANDART_FONT).text(String.valueOf(pointList.get(i))).horizontalAlignment(RIGHT).borderWidth(1).build());
                }
            }
            Row row = rowBuilder.backgroundColor(j % 2 == 0 ? BACKGROUND_ROW_EVEN : BACKGROUND_ROW_ODD)
                .build();
            tableBuilder.addRow(row);
            j++;
        }
        
        PdfUtils.exportTemplateWithTable(tableBuilder, outputFileName, titel);
    }
}
