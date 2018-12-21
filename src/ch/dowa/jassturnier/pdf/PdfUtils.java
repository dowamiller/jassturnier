/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.pdf;

import ch.dowa.jassturnier.ResourceLoader;
import java.awt.Color;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.vandeseer.easytable.TableDrawer;
import org.vandeseer.easytable.structure.Table.TableBuilder;

/**
 *
 * @author domi_
 */
public class PdfUtils {
    
    public static final float DOCUMENT_PADDING = 50f;
    public final static Color BACKGROUND_HEADER = new Color(76, 129, 190);
    public final static Color BACKGROUND_ROW_EVEN = new Color(186, 206, 230);
    public final static Color BACKGROUND_ROW_ODD = new Color(218, 230, 242);
    public static final float TABEL_WIDTH = PDRectangle.A4.getWidth()  - 2 * DOCUMENT_PADDING;
    public static final PDFont STANDART_FONT = PDType1Font.HELVETICA;
    public static final PDFont STANDART_FONT_BOLD = PDType1Font.HELVETICA_BOLD;
    public static final float DOC_CONTENT_START_X = DOCUMENT_PADDING;
    public static final float DOC_CONTENT_START_Y = PDRectangle.A4.getHeight() - DOCUMENT_PADDING;
    public static final float DOC_CONTENT_START_FIRST_PAGE_Y = PDRectangle.A4.getHeight() - (2 * DOCUMENT_PADDING);
    public static final float DOC_TITEL_START_X = DOCUMENT_PADDING;
    public static final float DOC_TITEL_START_Y = DOC_CONTENT_START_Y;
    
    
    private final static float IMAGE_MAX_START_X = DOCUMENT_PADDING + (TABEL_WIDTH * 0.7F);
    private final static float IMAGE_MAX_START_Y = PDRectangle.A4.getHeight() - 2* DOCUMENT_PADDING + 10F;
    
    public final static float IMAGE_END_X = PDRectangle.A4.getWidth() - DOCUMENT_PADDING;
    public final static float IMAGE_END_Y = PDRectangle.A4.getHeight() - 20F;
    
    private final static float IMAGE_MAX_HEIGHT = IMAGE_END_Y - IMAGE_MAX_START_Y;
    private final static float IAMGE_MAX_WIDTH = IMAGE_END_X - IMAGE_MAX_START_X;
    private final static float MAX_RATIO = IAMGE_MAX_WIDTH / IMAGE_MAX_HEIGHT;
    
    public static PDRectangle getImageSizePdf(PDImageXObject img){
        
        float actImageWidth = img.getWidth();
        float actImageHeight = img.getHeight();
        float actRatio = actImageWidth / actImageHeight;
                 
        float imageWidth = 0F;
        float imageHeight = 0F;
            
            if (MAX_RATIO < actRatio ) {
                imageWidth = IAMGE_MAX_WIDTH;
                imageHeight = imageWidth / actRatio;
            } else if ( 1 < actRatio && actRatio <= MAX_RATIO){
                imageHeight = IMAGE_MAX_HEIGHT;
                imageWidth = imageHeight * actRatio;
            } else if (actRatio <= 1) {
                imageHeight = IMAGE_MAX_HEIGHT;
                imageWidth = imageHeight * actRatio;
            }
            
        return new PDRectangle(imageWidth, imageHeight);
    }
    
    public static void exportTemplateWithTable(TableBuilder tableBuilder, String fileName, String title) throws IOException {
        String vLogoPath = !ResourceLoader.readProperty("LOGOPATH").isEmpty() ? ResourceLoader.readProperty("LOGOPATH") : null;
                
        final PDDocument document = new PDDocument();
        boolean firstPage = true;
        PDImageXObject image = null;
        float imageStartX = 0F;
        float imageStartY = 0F;
        PDRectangle imgSize = null;
                
        TableDrawer drawer =  TableDrawer.builder()
            .table(tableBuilder.build())
            .startX(DOC_CONTENT_START_X)
            .startY(DOC_CONTENT_START_Y)
            .endY(DOCUMENT_PADDING) 
            .build();

        if(vLogoPath != null){
            image = PDImageXObject.createFromFile(vLogoPath, document);
            imgSize = getImageSizePdf(image);
            imageStartX = IMAGE_END_X - imgSize.getWidth();
            imageStartY = IMAGE_END_Y - imgSize.getHeight(); 
        }
        
        do {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                if (firstPage) {
                    contentStream.beginText();
                    contentStream.setFont(STANDART_FONT_BOLD, 14);
                    contentStream.newLineAtOffset(DOC_TITEL_START_X, DOC_TITEL_START_Y);
                    contentStream.showText(title);
                    contentStream.endText();
                    if (image != null){
                        contentStream.drawImage(image, imageStartX, imageStartY, imgSize.getWidth(), imgSize.getHeight());
                    }
                    drawer.startY(DOC_CONTENT_START_FIRST_PAGE_Y);
                    firstPage = false;
                }
                drawer.contentStream(contentStream).draw();
            }
            drawer.startY(DOC_CONTENT_START_Y);
        } while (!drawer.isFinished());

        document.save(fileName );
        document.close();
    }
    
}
