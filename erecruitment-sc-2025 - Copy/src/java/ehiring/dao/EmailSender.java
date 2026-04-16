package ehiring.dao;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import ehiring.action.CurrentDateTime;
import ehiring.properties.LoadProperties;
import java.io.File;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.Session;
import javax.mail.Transport;

public class EmailSender {

    public String filename = new String();

    public static void main(String[] args) {
        EmailSender em = new EmailSender();
        System.out.println("To: "+args[0]+" Msg: "+args[1]);
        em.send(args[0], args[1]); // ChangeId: 2024012302 old em.send("", "");
    }

    public void setFileName(String filename) {
        this.filename = filename;
    }

    public String send(String email, String msg) {
        // email ID of Recipient.
        // String recipient = "saikalpana_t@nrsc.gov.in";
        try {
            String recipient = email;

            // email ID of Sender.
            LoadProperties lp = new LoadProperties();
            String sender = lp.getProperty("EMAIL_SENDER");
            String host = lp.getProperty("EMAIL_HOST");

            //String sender = "sureshbabu_av@nrsc.gov.in";
            // using host as localhost
            //String host = "172.20.1.73";
            // Getting system properties
            Properties properties = System.getProperties();

            // Setting up mail server
            properties.setProperty("mail.smtp.host", host);

            // creating session object to get properties
            Session session = Session.getDefaultInstance(properties);

            // MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From Field: adding senders email to from field.
            message.setFrom(new InternetAddress(sender));

            // Set To Field: adding recipient's email to from field.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Set Subject: subject of the email
            if (msg.indexOf("register") >= 0) {
                message.setSubject("Registration - NRSC e-Recruitment system");
            } else if (msg.indexOf("application") >= 0) {
                message.setSubject("Application - NRSC e-Recruitment system");
            } else if (msg.indexOf("notification") >= 0) { // ChangeId: 2025051602
                message.setSubject("[Important] Notification - NRSC e-Recruitment system"); // ChangeId: 2025051602
            } else {
                message.setSubject("One Time Password (OTP) for NRSC e-Recruitment system");
            }

            String enm = java.net.URLEncoder.encode(email, "UTF-8");

            String signature = "\n\nRegards,\nRecruitment Section,\nNational Remote Sensing Centre (NRSC), ISRO";
            String note = "\n\n*This is a system generated email. Please do not reply to this mail.*";

            // set body of the email.
            if (msg.contains("register")) {
                /*	message.setContent("<HTML>" + msg
                 + ". To activate <a href=\"https://apps.nrsc.gov.in/eRecruitment_NRSC/LoginServlet?enm=" + enm
                 + "\">click here</a> </HTML>", "text/html; charset=utf-8");*/
                String link = "https://apps.nrsc.gov.in/eRecruitment_NRSC/index.html?param=" + new CipherCreator().cipher(enm);

                message.setContent("<HTML>" + msg.replaceAll("\n", "</br>") + ". <a href=\"" + link + "\">Click here</a> to activate.</br></br>If it does not work, copy the link below and paste it in the address bar of the browser.</br>" + link + " <br></br> " + signature.replaceAll("\n", "</br>") + note.replaceAll("\n", "</br>"), "text/html; charset=utf-8");

                // message.setText(msg);
            } else if (msg.contains("application")) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(sender));
                message.setText(msg);

                MimeMultipart multiPart = new MimeMultipart();

                MimeBodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(msg);
                multiPart.addBodyPart(messageBodyPart);

                File fn = new File(this.filename);
                FileDataSource filedatasource = new FileDataSource(fn);
                MimeBodyPart attachment = new MimeBodyPart();
                attachment.setDataHandler(new DataHandler(filedatasource));
                attachment.setDisposition(Part.ATTACHMENT);
                attachment.setFileName(fn.getName());
                multiPart.addBodyPart(attachment);

                message.setContent(multiPart);

//                MimeBodyPart mimebodypart = new MimeBodyPart();
//                File fn = new File(this.filename);
//                FileDataSource filedatasource = new FileDataSource(fn);
//                mimebodypart.setDataHandler(new DataHandler(filedatasource));
//                mimebodypart.setFileName(fn.getName());
//                mimebodypart.setText(msg);
//                MimeMultipart mimemulti = new MimeMultipart();
//                mimemulti.addBodyPart(mimebodypart);
//                message.setContent(mimemulti);
                //message.setText(msg);

            } else if (msg.contains("notification")) { // ChangeId: 2025051602
                message.setText(msg); // ChangeId: 2025051602
            }
            else {
                message.setText("Dear Applicant,\n\nThe One Time Password (OTP) for  Online Recruitment Application " + msg + ".\n\nThis OTP is valid for 10 minutes or 1 successful attempt whichever is earlier.\nPlease note this OTP is valid only for this attempt and cannot be used for any other attempt." + signature + note);
            }
            // Send email.

            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
            System.out.println(CurrentDateTime.dateTime() + ":" + "Before Mail Send..");
            Transport.send(message);
            System.out.println(CurrentDateTime.dateTime() + ":" + "Mail successfully sent");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            return "error";
        } catch (Exception mex) {
            mex.printStackTrace();
            return "error";
        }
        return "success";
    }
    /*
     message.setSubject(subject);
     MimeBodyPart mimebody = new MimeBodyPart();
     mimebody.setText(msg);
     MimeMultipart mimemulti = new MimeMultipart();
     mimemulti.addBodyPart(mimebody);

     for (int i = 0; i < mailAttachment.size(); i++) {
     MimeBodyPart mimebodypart1 = new MimeBodyPart();
     String filename = mailAttachment.elementAt(i).toString();
     File fn = new File(filename);
     FileDataSource filedatasource = new FileDataSource(fn);
     mimebodypart1.setDataHandler(new DataHandler(filedatasource));
     //   System.out.println("SendMailBean::attachment name :" + filename);
     mimebodypart1.setFileName(fn.getName());
     mimemulti.addBodyPart(mimebodypart1);
     }
     message.setContent(mimemulti);
     message.setSentDate(new Date());
     System.out.println("setText:3");
     Transport.send(message);
     System.out.println("setText:4");

     */

    public String sendSMS(long phoneNum, String msg) {
        String resp = new String();
        System.out.println("INSIDE sendSMS() method");
        try {

            LoadProperties lp = new LoadProperties();
            String requestUrl = lp.getProperty("SMS_URL");
            //String requestUrl = "http://192.168.0.199:8080/sms1/smssendnew.jsp?";

            requestUrl += "mnumber=" + phoneNum + "&message=" + msg;
            requestUrl = requestUrl.replace(" ", "%20");
            requestUrl = requestUrl.replace("\n", "%0A"); // ChangeId: 2024011805

            System.out.println(CurrentDateTime.dateTime() + ":" + "......:" + requestUrl);
            URL url = new URL(requestUrl);
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            resp = uc.getResponseMessage();
            uc.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        System.out.println(CurrentDateTime.dateTime() + ":" + "the response is:" + resp);
        if (resp.equalsIgnoreCase("OK")) {
            return "success";
        } else {
            return "error";
        }

    }
}
