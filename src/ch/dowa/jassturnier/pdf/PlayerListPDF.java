/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.pdf;

import ch.dowa.jassturnier.ResourceLoader;
import static ch.dowa.jassturnier.pdf.PdfUtils.*;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.TableDrawer;
import static org.vandeseer.easytable.settings.HorizontalAlignment.LEFT;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellText;


/**
 *
 * @author domi_
 */
public class PlayerListPdf {

    public static void exportPlayerList(ArrayList<String> playerNames, String year) throws IOException  {
        String vName = !ResourceLoader.readProperty("VNAME").isEmpty() ? ResourceLoader.readProperty("VNAME") : null;
        String outputFileName;
        outputFileName =  vName != null ? vName.replace(' ', '_') + "_" : "";
        outputFileName += "Jassturnier_" + year + "_Spielerliste.pdf";
        String titel = ((vName != null) ? vName + " " : "") + "Jassturnier " + year + " - Spielerliste";

        final Table.TableBuilder tableBuilder = Table.builder()
            .addColumnsOfWidth((float)(TABEL_WIDTH * 0.6),(float) (TABEL_WIDTH * 0.2),(float) (TABEL_WIDTH * 0.2))
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
        for ( String playerName : playerNames) {
            tableBuilder.addRow(Row.builder()
                .add(CellText.builder().text(playerName).horizontalAlignment(LEFT).borderWidth(1).build())
                .add(CellText.builder().text("").horizontalAlignment(LEFT).borderWidth(1).build())
                .add(CellText.builder().text("").horizontalAlignment(LEFT).borderWidth(1).build())
                .backgroundColor(i % 2 == 0 ? BACKGROUND_ROW_EVEN : BACKGROUND_ROW_ODD)
                .build());
                i++;
        }

        PdfUtils.exportTemplateWithTable(tableBuilder, outputFileName, titel);
    }

}
