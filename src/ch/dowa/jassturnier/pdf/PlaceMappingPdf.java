/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.pdf;

import ch.dowa.jassturnier.ResourceLoader;
import ch.dowa.jassturnier.control.TurnierController;
import static ch.dowa.jassturnier.pdf.PdfGenerator.BACKGROUND_HEADER;
import static ch.dowa.jassturnier.pdf.PdfGenerator.BACKGROUND_ROW_EVEN;
import static ch.dowa.jassturnier.pdf.PdfGenerator.BACKGROUND_ROW_ODD;
import static ch.dowa.jassturnier.pdf.PdfGenerator.STANDART_FONT;
import static ch.dowa.jassturnier.pdf.PdfGenerator.STANDART_FONT_BOLD;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;
import static org.vandeseer.easytable.settings.HorizontalAlignment.RIGHT;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

/**
 *
 * @author domi_
 */
public class PlaceMappingPdf {
    
    public static void exportPlaceMapping(TurnierController.PlaceMappingType placeMapping, int gangNr, String turnierTitel) throws IOException {
        PdfGenerator gen = new PdfGenerator(PDRectangle.A4);        
        String outputFileName;
        outputFileName = turnierTitel.replace(' ', '_') + "_Platzzuweisung_Gang_" + String.valueOf(gangNr) + ".pdf";
        String titel = turnierTitel + " - Platzzuweisung Gang " + String.valueOf(gangNr);
        
        final Table.TableBuilder tableBuilder = Table.builder()
            .addColumnsOfWidth((float)(gen.tabelWidth() * 0.6),(float) (gen.tabelWidth() * 0.2),(float) (gen.tabelWidth() * 0.2))
            .fontSize(10)
            .font(STANDART_FONT)
            .borderColor(Color.WHITE);
        
        final Row headerRow = Row.builder()
            .add(CellText.builder().text("Spieler").horizontalAlignment(LEFT).borderWidth(1).build())
            .add(CellText.builder().text("Tisch").horizontalAlignment(LEFT).borderWidth(1).build())
            .add(CellText.builder().text("Platz").horizontalAlignment(LEFT).borderWidth(1).build())
            .backgroundColor(BACKGROUND_HEADER)
            .textColor(Color.WHITE)
            .font(STANDART_FONT_BOLD).fontSize(12)
            .build();
             
        tableBuilder.addRow(headerRow);
        
        ArrayList<String> names = placeMapping.getNames();
        ArrayList<String> tables = placeMapping.getTables();
        ArrayList<String> seats = placeMapping.getSeats();
               
        for (int i = 0 ; i < placeMapping.getNames().size() ; i++) {
            tableBuilder.addRow(Row.builder()
                .add(CellText.builder().font(STANDART_FONT).text(names.get(i)).horizontalAlignment(LEFT).borderWidth(1).build())
                .add(CellText.builder().font(STANDART_FONT_BOLD).text(tables.get(i)).horizontalAlignment(RIGHT).borderWidth(1).build())
                .add(CellText.builder().font(STANDART_FONT_BOLD).text(seats.get(i)).horizontalAlignment(RIGHT).borderWidth(1).build())
                .backgroundColor(i % 2 == 0 ? BACKGROUND_ROW_EVEN : BACKGROUND_ROW_ODD)
                .build());

        }
        
        gen.exportTemplateWithTable(tableBuilder, outputFileName, titel);
    }
    
}
