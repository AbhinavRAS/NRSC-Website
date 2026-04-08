package ehiring.dao;

import java.io.FileOutputStream;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class WaterMark {
	public boolean doWaterMarking(String filePath)
	{
		
		 try {
			 System.out.println("Adding to the order 111:");
	        	final FileOutputStream fileOutputStream = new FileOutputStream(filePath.replace("preview.pdf","preview_new.pdf"));
	        //	 System.out.println("Adding to the order 111:"+filePath.replace("preview.pdf","preview_new.pdf"));
	            final PdfReader pdfReader = new PdfReader(filePath);
	            final int numberOfPages = pdfReader.getNumberOfPages();
	            final PdfStamper pdfStamper = new PdfStamper(pdfReader, fileOutputStream);
	            for (int j = 1; j <= numberOfPages; ++j) {
	            	System.out.println("Adding to the order inside ....:");
	                final PdfContentByte underContent = pdfStamper.getUnderContent(j);
	                final BaseFont font = BaseFont.createFont("Helvetica", "Cp1252", true);
	                final PdfGState gState = new PdfGState();
	                gState.setFillOpacity(0.7f);
	                gState.setBlendMode(PdfGState.BM_HARDLIGHT);
	                underContent.setGState(gState);
	                underContent.beginText();
	                underContent.setFontAndSize(font, 50.0f);
	                underContent.setColorFill(BaseColor.LIGHT_GRAY);
	                underContent.showTextAligned(1, "Registration No:", 300.0f, 400.0f, 45.0f);
	                underContent.endText();
	            }
	            pdfStamper.close();
	            System.out.println("Adding to the order:");
	           
	        }
	        
	        catch (Exception i1) {
	            i1.printStackTrace();
	            return false;
	        }
		return true;
		
	}
	
   
}