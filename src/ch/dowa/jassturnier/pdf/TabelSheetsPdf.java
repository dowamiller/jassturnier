/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.pdf;

import ch.dowa.jassturnier.ResourceLoader;
import ch.dowa.jassturnier.objectModel.Gang;
import ch.dowa.jassturnier.objectModel.Spiel;
import static ch.dowa.jassturnier.pdf.PdfGenerator.BACKGROUND_HEADER;
import static ch.dowa.jassturnier.pdf.PdfGenerator.BACKGROUND_ROW_EVEN;
import static ch.dowa.jassturnier.pdf.PdfGenerator.BACKGROUND_ROW_ODD;
import static ch.dowa.jassturnier.pdf.PdfGenerator.STANDART_FONT;
import static ch.dowa.jassturnier.pdf.PdfGenerator.STANDART_FONT_BOLD;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;

/**
 *
 * @author domi_
 */
public class TabelSheetsPdf {
    
    public static void exportTabelSheets(Gang actGang, String turnierTitel) throws IOException  {
        PdfGenerator gen = new PdfGenerator(PDRectangle.A5);
        String outputFileName;
        outputFileName =  turnierTitel.replace(' ', '_') + "_Spielblaetter_Gang_" + actGang.getGangNr() + ".pdf";
        HashMap<String,Table.TableBuilder> tableBuildersMap = new HashMap();
        
        for (Spiel s : actGang.getGames()){
            String titel = "Gang " + String.valueOf(actGang.getGangNr()) + " - Tischnummer " + String.valueOf(s.getTischNr());
            final Table.TableBuilder tableBuilder = Table.builder()
                .addColumnsOfWidth((float)(gen.tabelWidth() * 0.6),(float) (gen.tabelWidth() * 0.2),(float) (gen.tabelWidth() * 0.2))
                .fontSize(10)
                .font(STANDART_FONT)
                .borderColor(Color.WHITE);

            final Row headerRow = Row.builder()
                    .add(CellText.builder().text("Spieler").horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(CellText.builder().text("anwesend").horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(CellText.builder().text("bezahlt").horizontalAlignment(LEFT).borderWidth(1).build())
                    .backgroundColor(BACKGROUND_HEADER)
                    .textColor(Color.WHITE)
                    .font(STANDART_FONT_BOLD).fontSize(12)
                    .build();

            tableBuilder.addRow(headerRow);
            int i = 0;
            String[] playerNames = {s.getTeam1().getSpieler1().getNachname(), 
                                    s.getTeam1().getSpieler2().getNachname(),
                                    s.getTeam2().getSpieler1().getNachname(),
                                    s.getTeam2().getSpieler1().getNachname()};
            for ( String playerName : playerNames) {
                tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text(playerName).horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(CellText.builder().text("").horizontalAlignment(LEFT).borderWidth(1).build())
                    .add(CellText.builder().text("").horizontalAlignment(LEFT).borderWidth(1).build())
                    .backgroundColor(i % 2 == 0 ? BACKGROUND_ROW_EVEN : BACKGROUND_ROW_ODD)
                    .build());
                    i++;
            }
            tableBuildersMap.put(titel, tableBuilder);
        }

        gen.exportTemplateWithTableMultiPage(tableBuildersMap, outputFileName);
    }
}
