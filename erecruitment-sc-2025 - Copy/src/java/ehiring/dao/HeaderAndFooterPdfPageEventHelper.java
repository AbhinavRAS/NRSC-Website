package ehiring.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
*
*  How To Set Header and Footer in pdf Using Itext Example -
*  Using iText library - core java tutorial
*
*/

class HeaderAndFooterPdfPageEventHelper extends PdfPageEventHelper {
 public void onStartPage(PdfWriter pdfWriter, Document document) {
     System.out.println("onStartPage() method > Writing header in file");
     Rectangle rect = pdfWriter.getBoxSize("rectangle");
     
     // TOP LEFT
     ColumnText.showTextAligned(pdfWriter.getDirectContent(),
              Element.ALIGN_CENTER, new Phrase("TOP LEFT"), rect.getLeft(),
              rect.getTop(), 0);

     // TOP MEDIUM
     ColumnText.showTextAligned(pdfWriter.getDirectContent(),
              Element.ALIGN_CENTER, new Phrase("TOP MEDIUM"),
              rect.getRight() / 2, rect.getTop(), 0);

     // TOP RIGHT
     ColumnText.showTextAligned(pdfWriter.getDirectContent(),
              Element.ALIGN_CENTER, new Phrase("TOP RIGHT"), rect.getRight(),
              rect.getTop(), 0);
 }

 public void onEndPage(PdfWriter pdfWriter, Document document) {
     System.out.println("onEndPage() method > Writing footer in file");
     Rectangle rect = pdfWriter.getBoxSize("rectangle");
     // BOTTOM LEFT
     ColumnText.showTextAligned(pdfWriter.getDirectContent(),
              Element.ALIGN_CENTER, new Phrase("BOTTOM LEFT"),
              rect.getLeft()+15, rect.getBottom(), 0);

     // BOTTOM MEDIUM
     ColumnText.showTextAligned(pdfWriter.getDirectContent(),
              Element.ALIGN_CENTER, new Phrase("BOTTOM MEDIUM"),
              rect.getRight() / 2, rect.getBottom(), 0);

     // BOTTOM RIGHT
     ColumnText.showTextAligned(pdfWriter.getDirectContent(),
              Element.ALIGN_CENTER, new Phrase("BOTTOM RIGHT"),
              rect.getRight()-10, rect.getBottom(), 0);
 }
}