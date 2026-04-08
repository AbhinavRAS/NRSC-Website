package ehiring.dao;

import com.itextpdf.text.*;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ehiring.operation.RecruitmentDirectoryOperation;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class GeneratePdf {

    public static int colorFlg = 0;
    public static boolean centreFlag = false;

    static LogManager logMgr;

    public GeneratePdf(String emailLogId) {
        logMgr = LogManager.getInstance(emailLogId);
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateTimeInIST = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        // Setting the time zone
        dateTimeInIST.setTimeZone(TimeZone.getTimeZone("IST"));
        // logMgr.accessLog(dateTimeInIST.format(new Date()));
        String currTime = dateTimeInIST.format(new Date());
        // logMgr.accessLog("FOR SMSM DATE..............." + currTime);

        return currTime;
    }

    public static ByteArrayOutputStream getPdfFile(HashMap m, String advtNo, String postNo, String context,
            String eMail, String appPath, String registration_id, String logoSPath, String logoPgPath,
            boolean previewFlag) {
        logMgr = LogManager.getInstance(eMail);
        logMgr.accessLog("ENTERED Generate is PDF .....:" + m.toString());

        Document document = new Document();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, bout);

			// add header and footer
            // writer.setPageEvent(new WatermarkPageEvent());
            // PdfWriter.getInstance(document, bout);
            document.open();

            RecruitmentDirectoryOperation rd = new RecruitmentDirectoryOperation(eMail);
            String logoPath = rd.getLogo(context);
            logMgr.accessLog("the img is :" + logoPath);

            Image img_header = Image.getInstance(logoPath);
            img_header.scaleToFit(325, 300);
            img_header.setAlignment(Image.MIDDLE);
            // img_header.setAbsolutePosition(70, 750);
            document.add(img_header);

            Font textFont = new Font(FontFamily.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK);
            Paragraph p_time = new Paragraph(getCurrentTime(), textFont);
            p_time.setAlignment(Element.ALIGN_RIGHT);
            document.add(p_time);

            String formName = "Application Form";
            if (previewFlag) {
                formName = "Application Form(Preview)";
            }
            textFont = new Font(FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK);
            p_time = new Paragraph(formName, textFont);
            p_time.setAlignment(Element.ALIGN_CENTER);
            document.add(p_time);

			// document.add(Chunk.NEWLINE);
            Image img = Image.getInstance(logoPgPath);
            logMgr.accessLog("the photo path is :" + logoPgPath);
            img.scaleToFit(75, 75);
            img.setAlignment(Image.ALIGN_CENTER);
            document.add(img);

            /*
             * logoPath = appPath + "signature_" + eMail + ".jpg";
             * logMgr.accessLog("the signature path is :" + logoPath); img =
             * Image.getInstance(logoPath); img.scaleToFit(75, 75);
             * img.setAlignment(Image.ALIGN_CENTER); document.add(img);
             */
			// document.add(Chunk.NEWLINE);
			// table.setWidthPercentage(40);
            // table.setWidths(new int[] { 3, 3 });
            // table.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPTable table = null;

            /**
             * **********************
             */
            table = new PdfPTable(2);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            centreFlag = true;
            table.addCell(getPdfCell("Registration Number"));
            table.addCell(getPdfCell(registration_id));

            /**
             * **************************
             */
            table.setSpacingAfter(10f);
            document.add(table);

            table = new PdfPTable(2);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(getPdfHeadCell("Advertisement Reference"));
            table.addCell(getPdfHeadCell("Post No"));

            centreFlag = true;
            table.addCell(getPdfCell(advtNo));
            table.addCell(getPdfCell(postNo));
            table.setSpacingAfter(10f);
            document.add(table);

            centreFlag = false;

            table = new PdfPTable(1);
            table.addCell("");
            table.addCell("");
            document.add(table);

            boolean higherEdFlg = false;

            Set setMain = m.entrySet();
            Iterator iteratorMain = setMain.iterator();
            while (iteratorMain.hasNext()) {
                Map.Entry mentryMain = (Map.Entry) iteratorMain.next();
				// System.out.print("MAIN is .. key is: " + mentryMain.getKey() + " & Value is:
                // " + mentryMain);
                logMgr.accessLog("MAIN is ...:" + mentryMain.getValue().toString());
                if (mentryMain.getKey().equals("Personal")) {
                    document.add(getSingleRow("Personal Details"));
                    higherEdFlg = false;
                } else if (mentryMain.getKey().equals("Present")) {
                    document.add(getSingleRow("Present Address"));
                    higherEdFlg = false;
                // Start: ChangeId: 2023111001
                } else if (mentryMain.getKey().equals("Bank Account")) { 
                    document.add(getSingleRow("Bank Account Details"));
                    higherEdFlg = false;
                // End: ChangeId: 2023111001
                } else if (mentryMain.getKey().equals("Permanent")) {
                    document.add(getSingleRow("Permanent Address"));
                    higherEdFlg = false;
                } else if (mentryMain.getKey().equals("X")) {
                    document.add(getSingleRow("Secondary Education (Xth Class)"));
                    higherEdFlg = false;
                } else if (mentryMain.getKey().equals("XII")) {
                    document.add(getSingleRow("Senior Secondary Education (XIIth Class)"));
                    higherEdFlg = false;
                } else if (mentryMain.getKey().equals("ITI")) {
                    document.add(getSingleRow("ITI"));
                    higherEdFlg = false;
                } else if (mentryMain.getKey().equals("diploma")) {
                    document.add(getSingleRow("Diploma"));
                    higherEdFlg = false;
                } else if (mentryMain.getKey().equals("High")) {
                    //document.add(getSingleRow("Higher Education"));
                    higherEdFlg = true;
                } else if (mentryMain.getKey().equals("Experience")) {
                    document.add(getSingleRow("Experience"));
                    higherEdFlg = false;
                } else if (mentryMain.getKey().equals("NET")) {
                    document.add(getSingleRow("National Examination Test)"));
                    higherEdFlg = false;
                } else if (mentryMain.getKey().equals("Medical")) {
                    document.add(getSingleRow("Medical Council Details"));
                    higherEdFlg = false;
                } else if (mentryMain.getKey().equals("Others")) {
                    document.add(getSingleRow("Other information (like Certifications, Additional courses)"));
                    higherEdFlg = false;
                }

                logMgr.accessLog("higherEdFlg IS gggfff **************************" + higherEdFlg);

                if (higherEdFlg) {
                    ArrayList arrList = (ArrayList) mentryMain.getValue();
                    if (arrList.size() == 0) {
                        document.add(addNATable());
                    } else {
                        getArrayListDoc(document, arrList);
                    }

                } else {
                    logMgr.accessLog("WELCOME IS ***********************GET KEY***" + mentryMain.getKey());

                    HashMap det = (HashMap) mentryMain.getValue();
                    if (det.size() == 0) {
                        document.add(addNATable());
                    } else {
                        getMapDoc(document, det);
                    }

                }

            } // for loop

            document.add(Chunk.NEWLINE);

			// String declare = "I hereby declare that the details furnished above are true
            // and correct to the best of my knowledge and I undertake to inform you of any
            // changes therein, immediately. In case any of the above information is found
            // to be false or untrue or misleading or misrepresenting, I am aware that I may
            // be held liable for it.";
            String declare = "I affirm that the information given in this online recruitment application is true and correct to the best of my knowledge."
                    + " I also fully understand that if at any stage it is noticed that any attempt has been made by me to willfully conceal "
                    + "and misrepresent the facts, my candidature shall be summarily rejected. I also declare that I meet the eligibility "
                    + "conditions(Education Qualification, Experience etc.,) and also accept the terms & conditions stipulated in the detailed advertisement.";

            textFont = new Font(FontFamily.TIMES_ROMAN, 10, Font.BOLDITALIC, BaseColor.BLACK);
            Paragraph p_declaration = new Paragraph(declare, textFont);
            p_declaration.setAlignment(Element.ALIGN_JUSTIFIED_ALL);
            document.add(p_declaration);

            logMgr.accessLog("the signature path is :" + logoSPath);
            img = Image.getInstance(logoSPath);
            img.scaleToFit(75, 75);
            img.setAlignment(Image.ALIGN_RIGHT);

            document.add(img);

            textFont = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLACK);
            Paragraph candSign = new Paragraph("(Applicant Signature)", textFont);
            candSign.setAlignment(Element.ALIGN_RIGHT);
            document.add(candSign);

            document.close();

            /**
             * ********************************
             */
            int pageCount = writer.getPageNumber() - 1;
            byte[] pdfAsBytes = bout.toByteArray();
            Font smallFont = FontFactory.getFont("Arial", 9, Font.NORMAL);

            Font FONT = new Font(Font.FontFamily.HELVETICA, 34, Font.BOLD, new GrayColor(0.5f));
            Phrase p = new Phrase("PREVIEW", FONT);

			// image watermark
            float w = img_header.getScaledWidth();
            float h = img_header.getScaledHeight();
            Rectangle pagesize;
            float x, y;

            // add footer
            PdfReader reader = new PdfReader(pdfAsBytes);
            // ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DataOutputStream output = new DataOutputStream(outputStream);
            document = new Document();
            document.open();
            PdfStamper stamper = new PdfStamper(reader, output);
            PdfContentByte content = stamper.getOverContent(1);

            for (int i = 1; i <= pageCount; i++) {
				// content = stamper.getOverContent(i);
                // content.addImage(img_header);

                content = stamper.getOverContent(i);
                content.saveState();

                pagesize = reader.getPageSizeWithRotation(i);
                x = (pagesize.getLeft() + pagesize.getRight()) / 2;
                y = (pagesize.getTop() + pagesize.getBottom()) / 2;

                // set transparency
                PdfGState state = new PdfGState();
                state.setFillOpacity(0.2f);
                content.setGState(state);

                if (previewFlag) {

                    // add watermark text and image
                    ColumnText.showTextAligned(content, Element.ALIGN_CENTER, p, x, y, 45f);

                } else {

                    img_header.setRotationDegrees(45f);
                    img_header.setRotation(45f);
                    content.addImage(img_header, w, 0, 0, h, x - (w / 2), y - (h / 2));
                }

                content.restoreState();

                ColumnText.showTextAligned(stamper.getOverContent(i), Element.ALIGN_CENTER,
                        new Phrase(i + "/" + pageCount, smallFont), 550, 30, 0);
            }
            stamper.close();

            /**
             * ******************************
             */
        } catch (Exception ex) {

            Logger.getLogger(GeneratePdf.class.getName()).log(Level.SEVERE, null, ex);
        }
		// logMgr.accessLog("BOUT SIZE is :" + bout.size());
        // return bout;
        return outputStream;
    }

    public static Document getArrayListDoc(Document doc, ArrayList arrList) {
        try {
            PdfPTable table = null;
            logMgr.accessLog("the higher  ARRAY ed map is :" + arrList.toString());
            ArrayList qualHeading = new ArrayList();
            for (int hed = 0; hed < arrList.size(); ++hed) {
                String qualString = new String();
                HashMap det = (HashMap) arrList.get(hed);
                Set set = det.entrySet();
                Iterator iterator = set.iterator();
                int cnt = 0;

                while (iterator.hasNext()) {
                    Map.Entry mentry = (Map.Entry) iterator.next();
                    System.out.print("key is: " + mentry.getKey() + " & Value is: ");
                    logMgr.accessLog(mentry.getValue().toString());
                    String key = (String) mentry.getKey();
                    String val = mentry.getValue() + "";
                    /*
                     * if (key.equalsIgnoreCase("Qualification")) { StringTokenizer stz = new
                     * StringTokenizer(val.trim(), "_"); qualString = stz.nextToken(); //
                     * Graduate_B.Sc qualHeading.add(qualString); val = stz.nextToken(); }
                     */

                    // Higher Education
                    if (key.equalsIgnoreCase("Type")) {
                        qualString = val;
                        qualHeading.add(qualString);
                        if (cnt == 0) {
                            doc.add(getSingleRow(qualString));

                            /*
                             * if(!qualHeading.contains(qualString)) doc.add(getSingleRow(qualString)); else
                             * { table = new PdfPTable(2); table.addCell(getPdfCell("     "));
                             * table.addCell(getPdfCell("     ")); doc.add(table); }
                             */
                        }

                    } else {
                        table = new PdfPTable(2);
                        table.addCell(getPdfCell(key));
                        table.addCell(getPdfCell(val));
                        doc.add(table);
                        cnt++;
                    }

                    // Experience
                    if (key.equals("Name")) {
                        // if (hed > 0)
                        {
                            table = new PdfPTable(2);
                            table.addCell(getPdfCell("     "));
                            table.addCell(getPdfCell("     "));
                            doc.add(table);
                        }
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static Document getMapDoc(Document doc, HashMap det) {
        try {
            PdfPTable table = null;

            Set set = det.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                System.out.print("key is: " + mentry.getKey() + " & Value is: " + mentry.getValue());
                String key = (String) mentry.getKey();
                if (key.equalsIgnoreCase("expData")) {
                    getArrayListDoc(doc, (ArrayList) mentry.getValue());
                    // doc.add(getSingleRow("Total Period Experience"));
                } else {

                    String val = mentry.getValue() + "";
                    /*
                     * if (key.equalsIgnoreCase("category")) { StringTokenizer stz = new
                     * StringTokenizer(val.trim(), "_"); val = stz.nextToken(); }
                     */
                    table = new PdfPTable(2);
                    table.addCell(getPdfCell(key));
                    table.addCell(getPdfCell(val));
                    doc.add(table);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static PdfPCell getPdfHeadCell(String headerName) {
        logMgr.accessLog("HEADER NAME is .....:" + headerName);
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        headFont.setColor(BaseColor.WHITE);
        PdfPCell hcell = new PdfPCell(new Phrase(headerName, headFont));
        BaseColor myColor = WebColors.getRGBColor("#4F81BD");
		// if (headerName.trim().equals("more..")) {
        // hcell = new PdfPCell(new Phrase(" ", headFont));
        // myColor = WebColors.getRGBColor("#EFE0A3");
        // }

        hcell.setBackgroundColor(myColor);
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return hcell;
    }

    public static PdfPCell getPdfCell(String value) {
        if (colorFlg == 4) {
            colorFlg = 0;
        }
        PdfPCell cell = new PdfPCell(new Phrase(value));
        BaseColor myColor = WebColors.getRGBColor("#D0D8E8");
        if (colorFlg == 2 || colorFlg == 3) {
            myColor = WebColors.getRGBColor("#E9EDF4");
        }

        cell.setBackgroundColor(myColor);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        if (centreFlag) {
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        }
        colorFlg++;
        return cell;
    }

    public static PdfPTable getSingleRow(String heading) {
        PdfPTable tbl = new PdfPTable(1);
        tbl.addCell(getPdfHeadCell(heading));
        return tbl;
    }

    public static PdfPTable getSingleSubRow(String heading) {
        PdfPTable tbl = new PdfPTable(1);

        tbl.addCell(getPdfHeadCell(heading));
        return tbl;
    }

    public Paragraph getPara(String value) {
        Paragraph para = new Paragraph();
        para.add(value);
		// try {
        // Text text1 = new Text(value);
        // text1.setFont(PdfFontFactory.createFont(FontConstants.TIMES_ITALIC));
        // // text1.setBackgroundColor(Color.makeColor(colorSpace))
        // // text1.setFontColor(Color.BLUE);
        // para.add(text1);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        return para;
    }

    public static Paragraph addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add("");
        }
        return paragraph;
    }

    public Document getDocument(HashMap det, Document doc) {
        Document docm = new Document();
        // HashMap det = (HashMap) m.get(i);
        try {
            Set set = det.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry) iterator.next();
                System.out.print("key is: " + mentry.getKey() + " & Value is: ");
                logMgr.accessLog(mentry.getValue().toString());

                PdfPTable tabl = new PdfPTable(2);
                tabl.addCell(getPdfCell((String) mentry.getKey()));
                tabl.addCell(getPdfCell((String) mentry.getValue()));
                docm.add(tabl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return docm;
    }

    public static PdfPCell getCellNoBorder(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static PdfPTable addNATable() {
        PdfPTable table = new PdfPTable(2);
        PdfPCell cell = new PdfPCell(new Phrase("-NA-"));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        table.addCell(cell);

        return table;
    }

    public static void main(String args[]) {
        SimpleDateFormat dateTimeInIST = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        // Setting the time zone
        dateTimeInIST.setTimeZone(TimeZone.getTimeZone("IST"));
        logMgr.accessLog(dateTimeInIST.format(new Date()));
        String currTime = dateTimeInIST.format(new Date());
        logMgr.accessLog(currTime);

    }

}
