/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.dowa.jassturnier.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;

public class Style {

    public static void headerCellStyle(PdfPCell cell) {

        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        // padding
        cell.setPaddingTop(0f);

        // background color
        cell.setBackgroundColor(new BaseColor(0 + 40, 121 + 40, 182 + 40));

        // border
        cell.setBorder(0);
        cell.setBorderWidthBottom(2f);
        cell.setMinimumHeight(30f);

    }

    public static void rangCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(0f);

        // background color
        cell.setBackgroundColor(BaseColor.WHITE);

        // border
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.5f);
        cell.setBorderWidthTop(0.5f);
        cell.setBorderWidthRight(0.5f);
        cell.setBorderWidthLeft(0.5f);

        // height
        cell.setMinimumHeight(15f);
    }

    public static void nameCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(0f);

        // border
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.5f);
        cell.setBorderWidthTop(0.5f);
        cell.setBorderWidthLeft(0.5f);
        cell.setBorderWidthRight(0.5f);

        // height
        cell.setMinimumHeight(15f);
    }

    public static void integerCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(0f);

        // border
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.5f);
        cell.setBorderWidthTop(0.5f);
        cell.setBorderWidthLeft(0.5f);
        cell.setBorderWidthRight(0.5f);

        // height
        cell.setMinimumHeight(15f);
    }

    public static void lableCellStyle(PdfPCell cell) {
        // alignment
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        // padding
        cell.setPaddingTop(0f);

        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);

        // border
        cell.setBorder(0);
        cell.setBorderWidthBottom(0.5f);
        cell.setBorderWidthTop(0.5f);
        cell.setBorderWidthLeft(0.5f);
        cell.setBorderWidthRight(0.5f);

        // height
        cell.setMinimumHeight(15f);
    }

    public static PdfPCell setAlternateRowColor(PdfPCell c, int rowNr) {
        c.setBackgroundColor(rowNr % 2 == 0 ? BaseColor.WHITE : BaseColor.LIGHT_GRAY);
        return c;
    }

}

