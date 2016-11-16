/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.pdf;


import com.itextpdf.text.BaseColor;
import java.io.FileOutputStream;
import java.io.IOException;
 
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.ArrayList;

/**
 *
 * @author Dominik
 */
public class RankingPDF {
      /** The resulting PDF file. */
    private ArrayList<String> names;
    private ArrayList<String> points;
    private String header;
    
    public RankingPDF ( ArrayList<String> names, ArrayList<String> points, String header){
        this.names = names;
        this.header = header;
        this.points = points;
    }
      /**
     * Creates a PDF with five tables.
     * @param    filename the name of the PDF file that will be created.
     * @throws    DocumentException 
     * @throws    IOException
     */
    public void createPdf(String filename)
        throws IOException, DocumentException {
    	// step 1
        Document document = new Document();
         document.setPageSize(PageSize.A4);
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        // step 4
        document.add(createTable());
        // step 5
        document.close();
    }
 
    /**
     * Creates a table; widths are set with setWidths().
     * @return a PdfPTable
     * @throws DocumentException
     */
    public PdfPTable createTable() throws DocumentException {
        // create 6 column table
        PdfPTable table = new PdfPTable(3);
 
        // set the width of the table to 100% of page
        table.setWidthPercentage(60);
 
        // set relative columns width
        table.setWidths(new float[]{1,5,2});
 
        // ----------------Table Header "Title"----------------
        // font
        Font font = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        // create header cell
        PdfPCell cell = new PdfPCell(new Phrase(header,font));
        // set Column span "1 cell = 6 cells width"
        cell.setColspan(3);
        // set style
        Style.headerCellStyle(cell);
        // add to table
        table.addCell(cell);
        table.addCell(createLabelCell("Rang:"));
        table.addCell(createLabelCell("Name:"));
        table.addCell(createLabelCell("Punkte:"));
 
        //-----------------Table Cells Label/Value------------------
 
        for(int i = 0;i<names.size() ;i++){
            table.addCell(Style.setAlternateRowColor(createRangCell(String.valueOf(i + 1)), i));
            table.addCell(Style.setAlternateRowColor(createNameCell(names.get(i)), i));
            table.addCell(Style.setAlternateRowColor(createPointCell(points.get(i)), i));
        }
        return table;
    }
    
        private static PdfPCell createRangCell(String text){
        // font
        Font font = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.DARK_GRAY);
 
        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
 
        // set style
        Style.rangCellStyle(cell);
        return cell;
    }
        
            // create cells
    private static PdfPCell createNameCell(String text){
        // font
        Font font = new Font(FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
 
        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
 
        // set style
        Style.nameCellStyle(cell);
        return cell;
    }
    
    private static PdfPCell createPointCell(String text){
        // font
        Font font = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.DARK_GRAY);
 
        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
 
        // set style
        Style.integerCellStyle(cell);
        return cell;
    }
            
    private static PdfPCell createLabelCell(String text){
        // font
        Font font = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
 
        // create cell
        PdfPCell cell = new PdfPCell(new Phrase(text,font));
 
        // set style
        Style.lableCellStyle(cell);
        return cell;
    }
        
    
}
