/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.pdf;

import ch.dowa.jassturnier.ResourceLoader;
import ch.dowa.jassturnier.objectModel.Gang;
import ch.dowa.jassturnier.objectModel.Spiel;
import static ch.dowa.jassturnier.pdf.PdfGenerator.STANDART_FONT;
import static ch.dowa.jassturnier.pdf.PdfGenerator.STANDART_FONT_BOLD;
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import static org.vandeseer.easytable.settings.HorizontalAlignment.CENTER;
import static org.vandeseer.easytable.settings.VerticalAlignment.MIDDLE;
import org.vandeseer.easytable.structure.Row;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.cell.CellImage;
import org.vandeseer.easytable.structure.cell.CellText;

/**
 *
 * @author domi_
 */
public class TabelSheetsPdf {

    public static void exportTabelSheets(Gang actGang, String turnierTitel, int numberOfGames) throws IOException {
        PdfGenerator gen = new PdfGenerator(PDRectangle.A5);
        String outputFileName;
        outputFileName = turnierTitel.replace(' ', '_') + "_Spielblaetter_Gang_" + actGang.getGangNr() + ".pdf";
        HashMap<String, Table.TableBuilder> tableBuildersMap = new HashMap();
        byte[] arrowIconBytes = ResourceLoader.getIcon("arrowIcon.png");
        PDImageXObject arrowIcon = PDImageXObject.createFromByteArray(new PDDocument(), arrowIconBytes, "arrowIcon");

        float thinBorderWidth = 0.3f;
        float thickBorderWidth = 1.3f;
        float rowHeight = 20f;

        String[] placeLables = new String[4];
        placeLables[0] = ResourceLoader.readProperty("PLACE1");
        placeLables[1] = ResourceLoader.readProperty("PLACE2");
        placeLables[2] = ResourceLoader.readProperty("PLACE3");
        placeLables[3] = ResourceLoader.readProperty("PLACE4");

        for (Spiel s : actGang.getGames()) {
            String titel = "Gang " + String.valueOf(actGang.getGangNr()) + " - Tischnummer " + String.valueOf(s.getTischNr());
            final Table.TableBuilder tableBuilder = Table.builder()
                    .addColumnsOfWidth(
                            gen.tabelWidth() / 10f,
                            gen.tabelWidth() / 10f,
                            gen.tabelWidth() / 10f,
                            gen.tabelWidth() / 10f,
                            gen.tabelWidth() / 10f,
                            gen.tabelWidth() / 10f,
                            gen.tabelWidth() / 10f,
                            gen.tabelWidth() / 10f,
                            gen.tabelWidth() / 10f,
                            gen.tabelWidth() / 10f)
                    .fontSize(10)
                    .font(STANDART_FONT)
                    .borderColor(Color.BLACK)
                    .borderWidth(0)
                    .textColor(Color.BLACK);

            // 1. Zeile
            tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text("").build())
                    .add(CellText.builder()
                            .text(placeLables[0] + ": " + s.getTeam1().getSpieler1().getVorname() + " " + s.getTeam1().getSpieler1().getNachname())
                            .borderWidth(thickBorderWidth)
                            .borderWidthBottom(thinBorderWidth)
                            .span(4)
                            .build())
                    .add(CellText.builder()
                            .text(placeLables[1] + ": " + s.getTeam2().getSpieler1().getVorname() + " " + s.getTeam2().getSpieler1().getNachname())
                            .borderWidth(thickBorderWidth)
                            .borderWidthBottom(thinBorderWidth)
                            .span(4)
                            .build())
                    .add(CellText.builder().text("").build())
                    .font(STANDART_FONT)
                    .height(rowHeight)
                    .build()
            );
            // 2. Zeile
            tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text("").build())
                    .add(CellText.builder()
                            .text(placeLables[2] + ": " + s.getTeam1().getSpieler2().getVorname() + " " + s.getTeam1().getSpieler2().getNachname())
                            .borderWidth(thickBorderWidth)
                            .borderWidthBottom(thinBorderWidth)
                            .borderWidthTop(thinBorderWidth)
                            .span(4)
                            .build())
                    .add(CellText.builder()
                            .text(placeLables[3] + ": " + s.getTeam2().getSpieler2().getVorname() + " " + s.getTeam2().getSpieler2().getNachname())
                            .borderWidth(thickBorderWidth)
                            .borderWidthTop(thinBorderWidth)
                            .borderWidthBottom(thinBorderWidth)
                            .span(4)
                            .build())
                    .add(CellText.builder().text("").build())
                    .font(STANDART_FONT)
                    .height(rowHeight)
                    .build()
            );

            for (int i = 1; i <= numberOfGames; i++) {
                if (i == 1) {
                    tableBuilder.addRow(Row.builder()
                            .add(CellText.builder().text(String.valueOf(i) + ".")
                                    .borderWidth(thickBorderWidth)
                                    .borderWidthBottom(thinBorderWidth)
                                    .horizontalAlignment(CENTER)
                                    .verticalAlignment(MIDDLE)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthLeft(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthRight(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthLeft(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthRight(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("157")
                                    .borderWidth(thickBorderWidth)
                                    .borderWidthBottom(thinBorderWidth)
                                    .horizontalAlignment(CENTER)
                                    .verticalAlignment(MIDDLE)
                                    .build())
                            .font(STANDART_FONT)
                            .height(rowHeight)
                            .build()
                    );
                } else if (i == numberOfGames) {
                    tableBuilder.addRow(Row.builder()
                            .add(CellText.builder().text(String.valueOf(i) + ".")
                                    .borderWidth(thickBorderWidth)
                                    .borderWidthTop(thinBorderWidth)
                                    .horizontalAlignment(CENTER)
                                    .verticalAlignment(MIDDLE)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthLeft(thickBorderWidth)
                                    .borderWidthBottom(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthBottom(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthBottom(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthRight(thickBorderWidth)
                                    .borderWidthBottom(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthLeft(thickBorderWidth)
                                    .borderWidthBottom(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthBottom(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthBottom(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthRight(thickBorderWidth)
                                    .borderWidthBottom(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("157")
                                    .borderWidth(thickBorderWidth)
                                    .borderWidthTop(thinBorderWidth)
                                    .horizontalAlignment(CENTER)
                                    .verticalAlignment(MIDDLE)
                                    .build())
                            .font(STANDART_FONT)
                            .height(rowHeight)
                            .build()
                    );
                } else {
                    tableBuilder.addRow(Row.builder()
                            .add(CellText.builder().text(String.valueOf(i) + ".")
                                    .borderWidth(thickBorderWidth)
                                    .borderWidthTop(thinBorderWidth)
                                    .borderWidthBottom(thinBorderWidth)
                                    .horizontalAlignment(CENTER)
                                    .verticalAlignment(MIDDLE)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthLeft(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthRight(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthLeft(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .build())
                            .add(CellText.builder().text("")
                                    .borderWidth(thinBorderWidth)
                                    .borderWidthRight(thickBorderWidth)
                                    .build())
                            .add(CellText.builder().text("157")
                                    .borderWidth(thickBorderWidth)
                                    .borderWidthTop(thinBorderWidth)
                                    .borderWidthBottom(thinBorderWidth)
                                    .horizontalAlignment(CENTER)
                                    .verticalAlignment(MIDDLE)
                                    .build())
                            .font(STANDART_FONT)
                            .height(rowHeight)
                            .build()
                    );
                }
            }

            tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text("").build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("").build())
                    .font(STANDART_FONT)
                    .height(rowHeight)
                    .build());

            tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text("+")
                            .horizontalAlignment(CENTER)
                            .verticalAlignment(MIDDLE)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("")
                            .borderWidth(thickBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("").build())
                    .add(CellImage.builder().image(arrowIcon)
                            .horizontalAlignment(CENTER)
                            .verticalAlignment(MIDDLE)
                            .maxHeight(rowHeight * 0.6f)
                            .span(2)
                            .build())
                    .add(CellText.builder().text("").build())
                    .add(CellText.builder().text("").build())
                    .font(STANDART_FONT)
                    .height(rowHeight)
                    .build());

            String pointsTotal = "   " + String.valueOf(numberOfGames * 157);

            tableBuilder.addRow(Row.builder()
                    .add(CellText.builder().text("=")
                            .horizontalAlignment(CENTER)
                            .verticalAlignment(MIDDLE)
                            .build())
                    .add(CellText.builder().text(pointsTotal.substring(pointsTotal.length() - 4, pointsTotal.length() - 3))
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .horizontalAlignment(CENTER)
                            .verticalAlignment(MIDDLE)
                            .build())
                    .add(CellText.builder().text(pointsTotal.substring(pointsTotal.length() - 3, pointsTotal.length() - 2))
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .horizontalAlignment(CENTER)
                            .verticalAlignment(MIDDLE)
                            .build())
                    .add(CellText.builder().text(pointsTotal.substring(pointsTotal.length() - 2, pointsTotal.length() - 1))
                            .borderWidth(thickBorderWidth)
                            .borderWidthRight(thinBorderWidth)
                            .borderWidthLeft(thinBorderWidth)
                            .horizontalAlignment(CENTER)
                            .verticalAlignment(MIDDLE)
                            .build())
                    .add(CellText.builder().text(pointsTotal.substring(pointsTotal.length() - 1, pointsTotal.length()))
                            .borderWidth(thickBorderWidth)
                            .horizontalAlignment(CENTER)
                            .verticalAlignment(MIDDLE)
                            .borderWidthLeft(thinBorderWidth)
                            .build())
                    .add(CellText.builder().text("").build())
                    .add(CellText.builder().text("").build())
                    .add(CellText.builder().text("").build())
                    .add(CellText.builder().text("").build())
                    .add(CellText.builder().text("").build())
                    .font(STANDART_FONT_BOLD)
                    .height(rowHeight)
                    .build());

            tableBuildersMap.put(titel, tableBuilder);
        }

        gen.exportTemplateWithTableMultiPage(tableBuildersMap, outputFileName);
    }
}
