/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.pdf;

import ch.dowa.jassturnier.ResourceLoader;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table;
import org.vandeseer.easytable.structure.Table.TableBuilder;

/**
 *
 * @author domi_
 */
public class PdfGenerator {

    public final static Color BACKGROUND_HEADER = new Color(76, 129, 190);
    public final static Color BACKGROUND_ROW_EVEN = new Color(186, 206, 230);
    public final static Color BACKGROUND_ROW_ODD = new Color(218, 230, 242);
    public static final PDFont STANDART_FONT = PDType1Font.HELVETICA;
    public static final PDFont STANDART_FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    
    public PdfGenerator (PDRectangle format){
        pageFormat = format;
        documentPadding = format.getWidth() / 12F;
    }
    
    private final PDRectangle pageFormat;
    private final float documentPadding;


    public float tabelWidth() {
        return pageFormat.getWidth()  - 2 * documentPadding;
    }

    private float docContentStartX() {
        return documentPadding;
    }

    private float docContentStartY() {
        return pageFormat.getHeight() - documentPadding;
    }
    
    private float docContentSartFirsPageY() {
        return pageFormat.getHeight() - (2 * documentPadding);
    }

    private float docTitelStartX() {
        return documentPadding;
    }

    private float docTitelStartY() {
        return docContentStartY();
    }

    private float imgEndX() {
        return pageFormat.getWidth() - documentPadding;
    }

    private float imgEndY() {
        return pageFormat.getHeight() - 20F;
    }
    
    private float imgMaxStartX() {
        return documentPadding + (tabelWidth() * 0.7F);
    }
    
    private float imgMaxStartY() {
        return pageFormat.getHeight() - 2 * documentPadding + 10F;
    }
    
    private float imgMaxHeight() {
        return imgEndY() - imgMaxStartY();
    } 

    private float imgMaxWidth() {
        return imgEndX() - imgMaxStartX();
    }
    
    private float maxRatio() {
        return imgMaxWidth() / imgMaxHeight();
    }
 
    
    public PDRectangle getImageSizePdf(PDImageXObject img){
        
        float actImageWidth = img.getWidth();
        float actImageHeight = img.getHeight();
        float actRatio = actImageWidth / actImageHeight;
                 
        float imageWidth = 0F;
        float imageHeight = 0F;
            
            if (maxRatio() < actRatio ) {
                imageWidth = imgMaxWidth();
                imageHeight = imageWidth / actRatio;
            } else if ( 1 < actRatio && actRatio <= maxRatio()){
                imageHeight = imgMaxHeight();
                imageWidth = imageHeight * actRatio;
            } else if (actRatio <= 1) {
                imageHeight = imgMaxHeight();
                imageWidth = imageHeight * actRatio;
            }
            
        return new PDRectangle(imageWidth, imageHeight);
    }
    
    public  void exportTemplateWithTable(TableBuilder tableBuilder, String fileName, String title) throws IOException {
        String vLogoPath = !ResourceLoader.readProperty("LOGOPATH").isEmpty() ? ResourceLoader.readProperty("LOGOPATH") : null;
                
        final PDDocument document = new PDDocument();
        boolean firstPage = true;
        PDImageXObject image = null;
        float imageStartX = 0F;
        float imageStartY = 0F;
        PDRectangle imgSize = null;
                
        TableDrawer drawer =  TableDrawer.builder()
            .table(tableBuilder.build())
            .startX(docContentStartX())
            .startY(docContentStartY())
            .endY(documentPadding) 
            .build();

        if(vLogoPath != null){
            image = PDImageXObject.createFromFile(vLogoPath, document);
            imgSize = getImageSizePdf(image);
            imageStartX = imgEndX() - imgSize.getWidth();
            imageStartY = imgEndY() - imgSize.getHeight(); 
        }
        
        do {
            PDPage page = new PDPage(pageFormat);
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                if (firstPage) {
                    contentStream.beginText();
                    contentStream.setFont(STANDART_FONT_BOLD, 14);
                    contentStream.newLineAtOffset(docTitelStartX(), docTitelStartY());
                    contentStream.showText(title);
                    contentStream.endText();
                    if (image != null){
                        contentStream.drawImage(image, imageStartX, imageStartY, imgSize.getWidth(), imgSize.getHeight());
                    }
                    drawer.startY(docContentSartFirsPageY());
                    firstPage = false;
                }
                drawer.contentStream(contentStream).draw();
            }
            drawer.startY(docContentStartY());
        } while (!drawer.isFinished());

        document.save(fileName );
        document.close();
    }
    
    public  void exportTemplateWithTableMultiPage(HashMap<String,Table.TableBuilder> tableBuildersMap , String fileName) throws IOException {
        String vLogoPath = !ResourceLoader.readProperty("LOGOPATH").isEmpty() ? ResourceLoader.readProperty("LOGOPATH") : null;
        PDDocument mainDoc = new PDDocument();
        PDFMergerUtility merger = new PDFMergerUtility();
        for (Map.Entry<String, TableBuilder> entry : tableBuildersMap.entrySet()) {
            String title = entry.getKey();
            TableBuilder tableBuilder = entry.getValue();

            final PDDocument documentPage = new PDDocument();
            boolean firstPage = true;
            PDImageXObject image = null;
            float imageStartX = 0F;
            float imageStartY = 0F;
            PDRectangle imgSize = null;

            TableDrawer drawer =  TableDrawer.builder()
                .table(tableBuilder.build())
                .startX(docContentStartX())
                .startY(docContentStartY())
                .endY(documentPadding) 
                .build();

            if(vLogoPath != null){
                image = PDImageXObject.createFromFile(vLogoPath, documentPage);
                imgSize = getImageSizePdf(image);
                imageStartX = imgEndX() - imgSize.getWidth();
                imageStartY = imgEndY() - imgSize.getHeight(); 
            }

            do {
                PDPage page = new PDPage(pageFormat);
                documentPage.addPage(page);
                try (PDPageContentStream contentStream = new PDPageContentStream(documentPage, page)) {
                    if (firstPage) {
                        contentStream.beginText();
                        contentStream.setFont(STANDART_FONT_BOLD, 14);
                        contentStream.newLineAtOffset(docTitelStartX(), docTitelStartY());
                        contentStream.showText(title);
                        contentStream.endText();
                        if (image != null){
                            contentStream.drawImage(image, imageStartX, imageStartY, imgSize.getWidth(), imgSize.getHeight());
                        }
                        drawer.startY(docContentSartFirsPageY());
                        firstPage = false;
                    }
                    drawer.contentStream(contentStream).draw();
                }
                drawer.startY(docContentStartY());
            } while (!drawer.isFinished());
            
            merger.appendDocument(mainDoc, documentPage);
        }
        
        mainDoc.save(fileName);
        mainDoc.close();
    }
    
}
