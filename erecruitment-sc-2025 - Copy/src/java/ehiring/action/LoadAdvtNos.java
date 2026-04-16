package ehiring.action;

import ehiring.dao.AdvertisementData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import ehiring.dao.EmailSender;
import ehiring.dao.Experience_Data;
import ehiring.dao.IntCourse_Data;
import ehiring.dao.LogManager;
import ehiring.dao.NET_Data;
import ehiring.dao.PersonalData;
import ehiring.dao.RegistrationRelation;
import ehiring.dao.WaterMark;
import ehiring.dao.XII_Data;
import ehiring.dao.X_Data;
import ehiring.dao.GeneratePdf;
import ehiring.dao.MedicalCouncilDetails;
import ehiring.dao.diploma_iti_data;
import ehiring.db.DBConnectionManager;
import ehiring.operation.AdvertisementOperation;
import ehiring.operation.Diploma_ITI_Operation;
import ehiring.operation.ExperienceOperation;
import ehiring.operation.IntCourseOperation;
import ehiring.operation.MedicalCouncilOperation;
import ehiring.operation.NETOperation;
import ehiring.operation.PersonalOperation;
import ehiring.operation.PostOperation;
import ehiring.operation.RegistrationRelationOperation;
import ehiring.operation.StatusChangeOfAdv;
import ehiring.operation.XOperation;
import ehiring.operation.XiiOperation;
import ehiring.properties.LoadProperties;
import in.gov.nrscisro.bndauth.BhoonidhiAuthManager;
import java.util.Calendar;

/**
 * Servlet implementation class LOadAdvtNos
 */
@WebServlet("/LoadAdvtNos")
public class LoadAdvtNos extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public DBConnectionManager conMgr = null;
    public String lPoolName = "recruit";
    public int days = 0, months = 0, years = 0;
    int totYears = 0, totMonths = 0, totDays = 0;
    String totExp = "0";
    LogManager logMgr;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoadAdvtNos() {
        super();
        conMgr = DBConnectionManager.getInstance();

        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("rawtypes")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {

        String userIpAddress = request.getRemoteAddr();
        String emailLogId = java.net.URLDecoder.decode(request.getHeader("user"));
        logMgr = LogManager.getInstance(emailLogId + ":" + userIpAddress);
        logMgr.accessLog("LoadAdvtNos Started");

        logMgr.accessLog("ENTERED SERVLET........................");
        int regrelResult = 0, personalResult = -1, xResult = -1, xiiResult = -1, ugResult = -1, pgResult = -1,
                phdResult = -1, pdResult = -1, expResult = 0, netResult = -1, medNurseResult = -1, medDoctResult = -1;
        String registration_id = "-", advt_no = "", post_no = "", post_name = "";
        Pattern usrNamePtrn = Pattern.compile("^[a-z0-9_-]{6,14}$");
        String[] applicant_eligibility;
        boolean insertFlag = true;
        String message = new String();
        boolean medNurseFlag = false;
        boolean medDoctFlag = false;
        // String eMail = (String) request.getSession().getAttribute("User");
        // Start: ChangeId: 2023121902
        HashMap<String,HashMap> minMarks = new HashMap();
        
        //"B.Sc":{"MinPercentage":65,"MinCGPA":6.84},
        HashMap<String, String> hmBSc = new HashMap();
        hmBSc.put("MinPercentage","60"); // ChangeId: 2024012004
        hmBSc.put("MinCGPA","6.5"); // ChangeId: 2024012004
        minMarks.put("B.Sc",hmBSc);
        
        //"B.Arch":{"MinPercentage":65,"MinCGPA":6.84},
        HashMap<String, String> hmBArch = new HashMap();
        hmBArch.put("MinPercentage","65");
        hmBArch.put("MinCGPA","6.84");
        minMarks.put("B.Arch",hmBArch);
        
        //"B.Tech/B.E":{"MinPercentage":65,"MinCGPA":6.84},
        HashMap<String, String> hmBTechBE = new HashMap();
        hmBTechBE.put("MinPercentage","65");
        hmBTechBE.put("MinCGPA","6.84");
        minMarks.put("B.Tech/B.E",hmBTechBE);
        
        //"M.Sc":{"MinPercentage":60,"MinCGPA":6.5},
        HashMap<String, String> hmMSc = new HashMap();
        hmMSc.put("MinPercentage","65"); // ChangeId: 2024012004
        hmMSc.put("MinCGPA","6.84"); // ChangeId: 2024012004
        minMarks.put("M.Sc",hmMSc);
        
        //"M.Tech/M.E":{"MinPercentage":60,"MinCGPA":6.5},
        HashMap<String, String> hmMTechME = new HashMap();
        hmMTechME.put("MinPercentage","60");
        hmMTechME.put("MinCGPA","6.5");
        minMarks.put("M.Tech/M.E",hmMTechME);
        
        //"MSc-Tech":{"MinPercentage":60,"MinCGPA":6.5},
        HashMap<String, String> hmMScTech = new HashMap();
        hmMScTech.put("MinPercentage","65"); // ChangeId: 2024012004
        hmMScTech.put("MinCGPA","6.84"); // ChangeId: 2024012004
        minMarks.put("MSc-Tech",hmMScTech);
        
        //"M.Tech":{"MinPercentage":60,"MinCGPA":6.5},
        HashMap<String, String> hmMTech = new HashMap();
        hmMTech.put("MinPercentage","60");
        hmMTech.put("MinCGPA","6.5");
        minMarks.put("M.Tech",hmMTech);
        
        // Start: ChangeId: 2023122704
        //"MBBS":{"MinPercentage":10,"MinCGPA":1.0}, 
        HashMap<String, String> hmMBBS = new HashMap();
        hmMBBS.put("MinPercentage","10");
        hmMBBS.put("MinCGPA","1.0");
        minMarks.put("MBBS",hmMBBS);
        // End: ChangeId: 2023122704
        
        // End: ChangeId: 2023121902
        
        
        List qualArr = new ArrayList();
        List typeArr = new ArrayList();
        List univArr = new ArrayList();
        List collegeArr = new ArrayList();
        List discpArr = new ArrayList();
        List yearPassArr = new ArrayList();
        List diviArr = new ArrayList();
        List perCGArr = new ArrayList();
        List cgpaUnivFlagArr = new ArrayList();
        List cgpaObtArr = new ArrayList();
        List cgpaMaxArr = new ArrayList();
        List percentArr = new ArrayList();
        List specArr = new ArrayList();
        List markSheetArr = new ArrayList();
        List degCertArr = new ArrayList();
        List absArr = new ArrayList();
        List topicArr = new ArrayList();
        LinkedHashMap pdfMap = new LinkedHashMap();
        String emailMsg = new String();
        // String buttonType = new String();
        boolean previewFlag = false;
        boolean saveFlag = false;

        LinkedHashMap persMap = new LinkedHashMap();
        LinkedHashMap bankMap = new LinkedHashMap(); // ChangeId: 2023111001
        LinkedHashMap permAddrMap = new LinkedHashMap();
        LinkedHashMap presAddrMap = new LinkedHashMap();
        LinkedHashMap expMap = new LinkedHashMap();
        LinkedHashMap netMap = new LinkedHashMap();
        LinkedHashMap remarksMap = new LinkedHashMap();
        ArrayList expArr = new ArrayList();
        ArrayList hEdArr = new ArrayList();
        LinkedHashMap hEdMap = new LinkedHashMap();
        LinkedHashMap xMap = new LinkedHashMap();
        LinkedHashMap xiiMap = new LinkedHashMap();
        LinkedHashMap itiMap = new LinkedHashMap();
        LinkedHashMap dipMap = new LinkedHashMap();
        LinkedHashMap mainExpMap = new LinkedHashMap();
        LinkedHashMap medNurseMap = new LinkedHashMap();
        LinkedHashMap medDoctMap = new LinkedHashMap();

        boolean XIIFlag = false;
        boolean ITIFlag = false;
        boolean DIPFlag = false;

        diploma_iti_data itiData = new diploma_iti_data();
        diploma_iti_data dipData = new diploma_iti_data();
        PersonalData personalData = new PersonalData();
        NET_Data netData = new NET_Data();
        X_Data xData = new X_Data();
        XII_Data xiiData = new XII_Data();
        Experience_Data expData = new Experience_Data();
        MedicalCouncilDetails mcNursedata = new MedicalCouncilDetails();
        MedicalCouncilDetails mcDoctdata = new MedicalCouncilDetails();

        List expYears = new ArrayList();
        List expMonths = new ArrayList();
        List expName = new ArrayList();
        List expAddress = new ArrayList();
        List expDesign = new ArrayList();
        List expNature = new ArrayList();
        List expWork = new ArrayList();
        List expPayDrawn = new ArrayList(); // PPEG-HRD: PayDrawn
        List expReason = new ArrayList(); // PPEG-HRD: ReasonForLeaving
        List expFrom = new ArrayList();
        List expTo = new ArrayList();
        List expGovt = new ArrayList();
        List expCert = new ArrayList();

        String signatureExtn = "";
        String photoExtn = "";
        String fileExtn = ""; // HRD ChangeId:2023080801

        Connection con = conMgr.getConnection(lPoolName);
        con.setAutoCommit(false);

        logMgr.accessLog("Inside LoadAdvtNos Servlet");

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String postStr = "";

        boolean flg = false;

        if (br != null) {
            postStr = br.readLine();
        }
        // Value post :" + postStr +
        // ":");
        postStr = postStr.substring(postStr.indexOf("{") + 1, postStr.indexOf("}"));
        postStr = postStr.replaceAll("\"", "");

        Map<String, String> map1 = new LinkedHashMap<String, String>();
        String[] pairs = postStr.split(",");
        for (int i = 0; i < pairs.length; i++) {
            //	System.out.println("STARTED....");
            String pair = pairs[i];
            logMgr.accessLog("pair is:" + pair);
            System.out.println("pair is there fore ....:"+pair);
            String[] keyVal = pair.split(":");
            String fieldName = keyVal[0].trim();
            if (fieldName.equalsIgnoreCase("doneXII")) {
                if (keyVal[1].equalsIgnoreCase("yes")) {
                    XIIFlag = true;
                }
            }

            if (fieldName.equalsIgnoreCase("doneITI")) {
                if (keyVal[1].equalsIgnoreCase("yes")) {
                    ITIFlag = true;
                }
                //System.out.println("IIIIII:"+ITIFlag);
            }

            if (fieldName.equalsIgnoreCase("doneDIP")) {
                if (keyVal[1].equalsIgnoreCase("yes")) {
                    DIPFlag = true;
                }
            }

            if (XIIFlag == false) {
                if (fieldName.startsWith("xii")) {
                    continue;
                }
            }
            if (ITIFlag == false) {
                if (fieldName.startsWith("iti")) {
                    //System.out.println("STARTED.. ITI.."+fieldName);
                    continue;
                }
            }
            if (DIPFlag == false) {
                if (fieldName.startsWith("dip")) {
                    continue;
                }
            }

            //logMgr.accessLog(keyVal[0] + "," + keyVal[1]);
            map1.put(fieldName, keyVal[1]);
        }
        // logMgr.accessLog( "Map values : " + map1);

        ArrayList formattedList = null;
        String resultStr = "";
        //System.out.println(CurrentDateTime.dateTime() + ":" + "ENTERED SERVLET........................" + map1.get("action"));

        String vtoken = java.net.URLDecoder.decode(map1.get("vtoken").toString(), "UTF-8");
        try {
            BhoonidhiAuthManager bam = new BhoonidhiAuthManager();

            String valid = bam.validateBhoonidhiJWT(vtoken);
            if (valid == "NOT_VALID") {
                logMgr.accessLog("JWT NOT VALID");
                JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
                jMetaArray.add("invalid_session");
                JsonObjectBuilder json = Json.createObjectBuilder();
                json.add("Results", jMetaArray);
                javax.json.JsonObject jsonObj = json.build();
                StringWriter writer = new StringWriter();
                Json.createWriter(writer).write(jsonObj);
                resultStr = writer.toString();
                logMgr.accessLog("jsonResponse : " + resultStr);

                PrintWriter pw = response.getWriter();
                response.setContentType("application/Json;charset=utf-8\"");
                pw.write(resultStr);
                con.close();
                pw.close();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * Code to load the advt no dropbox at the admin side
         */
        if (map1.get("action").equalsIgnoreCase("advtNos")) {
            formattedList = new AdvertisementOperation(emailLogId).loadAdvt();
            JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
            for (int j = 0; j < formattedList.size(); j++) {
                jMetaArray.add(formattedList.get(j).toString());
            }
            JsonObjectBuilder json = Json.createObjectBuilder();
            json.add("Results", jMetaArray);
            javax.json.JsonObject jsonObj = json.build();
            StringWriter writer = new StringWriter();

            Json.createWriter(writer).write(jsonObj);
            resultStr = writer.toString();
            flg = true;
        }

        /*
         * Code to delete the previous Upload
         */
        if (map1.get("action").equalsIgnoreCase("deletePrevUpload")) {
            System.out.println("ENTERED deletePrevUpload:....");
            String fieldName = map1.get("fieldName").toString();
            String filename = map1.get("filename").toString();
            String advtNo = map1.get("advtNo").toString();
            String postNo = map1.get("postNo").toString();
            //System.out.println("ENTERED deletePrevUpload:...." + fieldName + filename + advtNo + postNo);
            formattedList = new AdvertisementOperation(emailLogId).deleteUpload(fieldName, advtNo, postNo, filename);
            JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
            for (int j = 0; j < formattedList.size(); j++) {
                jMetaArray.add(formattedList.get(j).toString());
            }
            JsonObjectBuilder json = Json.createObjectBuilder();
            json.add("Results", jMetaArray);
            javax.json.JsonObject jsonObj = json.build();
            StringWriter writer = new StringWriter();

            Json.createWriter(writer).write(jsonObj);
            resultStr = writer.toString();
            flg = true;
        } /*
         * Code to load the advt no dropbox at the admin side
         */ else if (map1.get("action").equalsIgnoreCase("advtNosAll")) {
            formattedList = new AdvertisementOperation(emailLogId).loadAllAdvt();
            JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
            for (int j = 0; j < formattedList.size(); j++) {
                jMetaArray.add(formattedList.get(j).toString());
            }
            JsonObjectBuilder json = Json.createObjectBuilder();
            json.add("Results", jMetaArray);
            javax.json.JsonObject jsonObj = json.build();
            StringWriter writer = new StringWriter();

            Json.createWriter(writer).write(jsonObj);
            resultStr = writer.toString();
            flg = true;
        } /*
         * Code to display table at the admin side
         */ else if (map1.get("action").equalsIgnoreCase("postDetails")) {

            formattedList = new PostOperation(emailLogId).getAdvtData(map1.get("advtNo").toString(),
                    map1.get("postNo").toString());
            List<JSONObject> jsonList = new ArrayList<JSONObject>();
            for (int i = 0; i < formattedList.size(); i++) {
                LinkedHashMap data = (LinkedHashMap) formattedList.get(i);
                // LinkedHashMap.,:"+data.toString());
                JSONObject obj = new JSONObject(data);
                jsonList.add(obj);
                logMgr.accessLog("jsonList:" + jsonList.toString());
            }
            JSONArray test = new JSONArray(jsonList);
            JSONObject finalMap = null;
            try {
                finalMap = new JSONObject().put("Results", test);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            resultStr = finalMap.toString();
            flg = true;
        } /*
         * Code to display table at the admin side
         */ else if (map1.get("action").equalsIgnoreCase("downloadAppAll")) {
            logMgr.accessLog("map ....fgf .strings:" + map1.toString());
            String uploadFileContextPath = request.getServletContext().getRealPath("");
            // String uploadDir = upload_dir;// (String) ((HttpSession)
            // request.getSession()).getAttribute("uploadDir");
            String filename = map1.get("advt_No").toString() + "_" + map1.get("post_No").toString() + "_"
                    + "Report.xls";
            String filenm_path = uploadFileContextPath + "/" + filename;
            logMgr.accessLog("FILE NM  therefore IS....789 :" + filenm_path);

            String contextPath = request.getServletContext().getContextPath() + File.separator + filename;
            String msgs = new PostOperation(emailLogId).DownloadApplicantData(map1.get("advt_No").toString(),
                    map1.get("post_No").toString(), filenm_path);
            JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
            if (msgs.indexOf("none") >= 0) {
                jMetaArray.add(msgs);
            } else {
                jMetaArray.add(contextPath);
            }
            JsonObjectBuilder json = Json.createObjectBuilder();
            json.add("Results", jMetaArray);
            javax.json.JsonObject jsonObj = json.build();
            StringWriter writer = new StringWriter();

            PrintWriter pw = response.getWriter();
            Json.createWriter(writer).write(jsonObj);
            String resultStr2 = writer.toString();
            logMgr.accessLog("jsonResponse : " + resultStr2);
            response.setContentType("application/Json;charset=utf-8\"");
            pw.write(resultStr2);
            con.close();
            pw.close();
            flg = true;
            return;
        } else if (map1.get("action").equalsIgnoreCase("downloadCert")) {
            logMgr.accessLog("map ....fgf .strings:" + map1.toString());
            advt_no = map1.get("advt_No").toString();
            post_no = map1.get("post_No").toString();
            String email = map1.get("email").toString();
            String regId = map1.get("regId").toString();

            email = email.replace("@", "_");
            email = email.replace(".", "_");

            try {
                LoadProperties lp = new LoadProperties();
                String appDir = lp.getProperty("RECRUIT_FOLDER_DIR");
                String filePath = appDir + File.separator + email + File.separator + advt_no + File.separator + post_no
                        + File.separator;
                String zipName = appDir + File.separator + regId;
                // String filenm_path = uploadFileContextPath + "/" + filename;
                logMgr.accessLog("FILE NM  therefore IS....456 :" + filePath);

                String contextPath = request.getServletContext().getContextPath() + File.separator + filePath;
                String msgs = new PostOperation(emailLogId).DownloadApplicantCert(advt_no, post_no, email, regId,
                        filePath, zipName);
                JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
                if (msgs.indexOf("none") >= 0) {
                    jMetaArray.add(msgs);
                } else {
                    jMetaArray.add(contextPath);
                }
                JsonObjectBuilder json = Json.createObjectBuilder();
                json.add("Results", jMetaArray);
                javax.json.JsonObject jsonObj = json.build();
                StringWriter writer = new StringWriter();

                PrintWriter pw = response.getWriter();
                Json.createWriter(writer).write(jsonObj);
                String resultStr2 = writer.toString();
                logMgr.accessLog("jsonResponse : " + resultStr2);
                response.setContentType("application/Json;charset=utf-8\"");
                pw.write(resultStr2);
                con.close();
                pw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            flg = true;
            return;
        } /*
         * Code to display table at the admin side
         */ else if (map1.get("action").equalsIgnoreCase("reportDetails")) {

            formattedList = new PostOperation(emailLogId).getApplicantData(map1.get("advtNo").toString(),
                    map1.get("postNo").toString());
            List<JSONObject> jsonList = new ArrayList<JSONObject>();
            for (int i = 0; i < formattedList.size(); i++) {
                LinkedHashMap data = (LinkedHashMap) formattedList.get(i);
                // LinkedHashMap.,:"+data.toString());
                JSONObject obj = new JSONObject(data);
                jsonList.add(obj);
                logMgr.accessLog("jsonList:" + jsonList.toString());
            }
            JSONArray test = new JSONArray(jsonList);
            JSONObject finalMap = null;
            try {
                finalMap = new JSONObject().put("Results", test);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            resultStr = finalMap.toString();
            flg = true;
        } /*
         * Code to resubmit
         */ else if (map1.get("action").equalsIgnoreCase("resubmit")) {
            logMgr.accessLog("ENTERED FOR RESUBMIT .....");
            String regId = map1.get("regId").toString();
            String advtNo = map1.get("advt_no").toString();
            String postNo = map1.get("post_no").toString();
            String emailId = map1.get("email").toString();
            int retVal = new AdvertisementOperation(emailLogId).flushPrevApplication(advtNo, postNo, emailId, regId);
            String msg = "success";
            if (retVal == -3) {
                msg = "Maximum Attempts exceeded. Your latest application is not deleted and will be considered for the post.";
            } else if (retVal < 0) {
                msg = "Problem in flushing the application";
            }

            JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
            jMetaArray.add(msg);
            JsonObjectBuilder json = Json.createObjectBuilder();
            json.add("Results", jMetaArray);
            javax.json.JsonObject jsonObj = json.build();
            StringWriter writer = new StringWriter();

            Json.createWriter(writer).write(jsonObj);
            resultStr = writer.toString();
            flg = true;
        } /* Start: ChangeId:2023101701 To process submit from home
         * Code to resubmit
         */ else if (map1.get("action").equalsIgnoreCase("submitApplicantHome")) {
            logMgr.accessLog("ENTERED FOR HOME-SUBMIT .....");
            String regId = map1.get("regId").toString();
            String advtNo = map1.get("advt_no").toString();
            String postNo = map1.get("post_no").toString();
            String emailId = map1.get("email").toString();
            
            int retVal = new AdvertisementOperation(emailLogId).SubmitApplicationHome(advtNo, postNo, emailId, regId);
            String msg = "Your application is successfully submitted";
            if (retVal < 0) {
                msg = "Problem in submitting the application";
            }

            JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
            jMetaArray.add(msg);
            JsonObjectBuilder json = Json.createObjectBuilder();
            json.add("Results", jMetaArray);
            javax.json.JsonObject jsonObj = json.build();
            StringWriter writer = new StringWriter();

            Json.createWriter(writer).write(jsonObj);
            resultStr = writer.toString();
            flg = true;
            
            // Start: ChangeId: 2323110204
            if(retVal>=0){
                LoadProperties lp = new LoadProperties();
                String appDir;
                try {
                    String eMail_dir = emailId;
                    eMail_dir = eMail_dir.replace("@", "_");
                    eMail_dir = eMail_dir.replace(".", "_");
                    appDir = lp.getProperty("RECRUIT_FOLDER_DIR");
                    String filePath = appDir + File.separator + eMail_dir + File.separator + advtNo + File.separator + postNo
                        + File.separator;
                    String filename = advtNo + "_" + postNo + "_" + eMail_dir
                                + ".pdf"; // ChangeId: 2025042201
                    //String zipName = appDir + File.separator + regId;
                    message = "Your application has been successfully submitted under Advt No: " + advtNo
                            + " for Post No: " + postNo + ". Your Registration ID is: " + regId + ".";

                    String signature = "\n\nRegards,\nRecruitment Section,\nNational Remote Sensing Centre (NRSC), ISRO";
                    String note = "\n\n*This is a system generated email. Please do not reply to this mail.*";
                    emailMsg = "Dear Applicant," 
                            + "\n\nYour application is successfully submitted" 
                            + " for NRSC Advertisement-" + advtNo + ", Post No: " + postNo
                            + ".\n\nYour Registration ID is " + regId + "." + signature + note;

                    //Email
                    EmailSender eSend = new EmailSender();
                    eSend.setFileName(filePath + filename);
                    String action = eSend.send(emailId, emailMsg);
                    //String sms = message + " - Recruitment Section,NRSC.";
                    //eSend.sendSMS(personalData.getContact_no(), sms);
                } catch (Exception ex) {
                    Logger.getLogger(LoadAdvtNos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            // End: ChangeId: 2323110204
        }
         /*
         * Code to display table at the applicant side applied posts
         */ else if (map1.get("action").equalsIgnoreCase("ApplicantAppliedPost")) {

            String eMail = map1.get("user_id").toString();
            formattedList = new PostOperation(emailLogId).getAllApplicantPosts(eMail);

            java.util.LinkedList<JSONObject> jsonList = new java.util.LinkedList<JSONObject>();
            for (int i = 0; i < formattedList.size(); i++) {
                LinkedHashMap data = (LinkedHashMap) formattedList.get(i);
                JSONObject obj = new JSONObject(data);
                jsonList.add(obj);
            }

            JSONArray test = new JSONArray(jsonList);

            JSONObject finalMap = null;
            try {
                finalMap = new JSONObject().put("Results", test);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            logMgr.accessLog(test.toString());
            resultStr = finalMap.toString();
            flg = true;
        } else if (map1.get("action").equalsIgnoreCase("SavedApplicantData")) {

            String eMail = map1.get("email").toString();
            formattedList = new PostOperation(emailLogId).getApplicantSaveData(eMail);

            java.util.LinkedList<JSONObject> jsonList = new java.util.LinkedList<JSONObject>();
            if (formattedList.size() > 0) {
                LinkedHashMap data = (LinkedHashMap) formattedList.get(0);
                JSONObject obj = new JSONObject(data);
                jsonList.add(obj);
            }
            JSONArray test = new JSONArray(jsonList);

            JSONObject finalMap = null;
            try {
                finalMap = new JSONObject().put("Results", test);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            logMgr.accessLog(test.toString());
            resultStr = finalMap.toString();
            flg = true;
        } /*
         * Code to display table at the applicant side
         */ else if (map1.get("action").equalsIgnoreCase("ApplicantpostDetails")) {

            String eMail = map1.get("user_id").toString();
            String aid = map1.get("aid").toString(); // ChangeId: 2023120101
            formattedList = new PostOperation(emailLogId).getAllAdverstimentPost(eMail,aid); // ChangeId: 2023120101 aid is passed

            java.util.LinkedList<JSONObject> jsonList = new java.util.LinkedList<JSONObject>();
            for (int i = 0; i < formattedList.size(); i++) {
                LinkedHashMap data = (LinkedHashMap) formattedList.get(i);
                JSONObject obj = new JSONObject(data);
                jsonList.add(obj);
            }

            JSONArray test = new JSONArray(jsonList);

            JSONObject finalMap = null;
            try {
                finalMap = new JSONObject().put("Results", test);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            logMgr.accessLog(test.toString());
            resultStr = finalMap.toString();
            flg = true;
        } /*
         * Report Generation : 'ALL'
         */ else if (map1.get("action").equalsIgnoreCase("displayAll")) {
            formattedList = (ArrayList) new PostOperation(emailLogId).generateAll(
                    map1.get("advt_no_drop_report").toString(), map1.get("post_no_drop_report").toString());
            java.util.LinkedList<JSONObject> jsonList = new java.util.LinkedList<JSONObject>();
            for (int i = 0; i < formattedList.size(); i++) {
                LinkedHashMap data = (LinkedHashMap) formattedList.get(i);
                JSONObject obj = new JSONObject(data);
                jsonList.add(obj);
            }

            JSONArray test = new JSONArray(jsonList);

            JSONObject finalMap = null;
            try {
                finalMap = new JSONObject().put("Results", test);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            logMgr.accessLog(test.toString());
            resultStr = finalMap.toString();
            flg = true;
        } /*
         * Application status tab at applicant side
         */ else if (map1.get("action").equalsIgnoreCase("applicationStatus")) {
            String email = map1.get("email").toString();
            formattedList = new PostOperation(emailLogId).getApplicantStatus(email);
            java.util.LinkedList<JSONObject> jsonList = new java.util.LinkedList<JSONObject>();
            for (int i = 0; i < formattedList.size(); i++) {
                LinkedHashMap data = (LinkedHashMap) formattedList.get(i);
                JSONObject obj = new JSONObject(data);
                jsonList.add(obj);
            }

            JSONArray test = new JSONArray(jsonList);

            JSONObject finalMap = null;
            try {
                finalMap = new JSONObject().put("Results", test);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            logMgr.accessLog(test.toString());
            resultStr = finalMap.toString();
            flg = true;
        }

        /*
         * Finally displaying the results from the chosen action
         */
        if (flg) {
            try {
                // formattedList:" +
                // formattedList.size() + ": " +
                // formattedList);
                // jsonResponse : " +
                // resultStr);
                response.setContentType("application/Json;charset=utf-8");
                PrintWriter pw = response.getWriter();
                pw.write(resultStr);
                pw.close();

            } catch (Exception e) {
                e.printStackTrace();
                response.setContentType("text/html");
                response.getWriter().print(e.getMessage());
            }
        }

        /*
         * Code to submit the application from the applicant side.
         */
        if (map1.get("action").equalsIgnoreCase("submitApplicant")) {
            System.out.println("ENTERED submitApplicant:" + emailLogId);
            List elgArray = new ArrayList();
            try {
                String pattern = "yyyy-MM-dd";
                String dateInString = new SimpleDateFormat(pattern).format(new java.util.Date());
                AdvertisementOperation advData = new AdvertisementOperation(emailLogId);
                String validDate = new StatusChangeOfAdv(emailLogId).compareDates(advData.getAdvValidDate(map1.get("advt_no").toString()), dateInString);
                System.out.println(dateInString + "," + advData.getAdvValidDate(map1.get("advt_no").toString()));

                if (validDate.equals("after") || validDate.equals("NA")) {
                    // con.rollback();
                    if (validDate.equals("after")) {
                        System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                        message = "Sorry!!: Cannot Apply as the advertisement end date has exceeded";
                    } else if (validDate.equals("NA")) {
                        System.out.println("Error in comparing the dates");
                        message = "Sorry!! Cannot Apply as Please contact System Administrator";
                    }
                    JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
                    jMetaArray.add("error"); // buttonType
                    jMetaArray.add(message);

                    JsonObjectBuilder json = Json.createObjectBuilder();
                    json.add("Results", jMetaArray);
                    javax.json.JsonObject jsonObj = json.build();
                    StringWriter writer = new StringWriter();

                    PrintWriter pw = response.getWriter();
                    Json.createWriter(writer).write(jsonObj);
                    String resultStr2 = writer.toString();
                    logMgr.accessLog("jsonResponse : " + resultStr2);
                    response.setContentType("application/Json;charset=utf-8\"");
                    pw.write(resultStr2);
                    pw.close();
                }

                System.out.println("Move further..." + validDate);

                int count_attempts = 0;
                String upload_dir = map1.get("upload_dir").toString();
                String eMail = map1.get("user_id").toString();
                String mailAddr = eMail;
                String buttonType = map1.get("buttonType").toString();

                String advt_no_flush = map1.get("advt_no").toString();
                String post_no_flush = map1.get("post_no").toString();

                advt_no = map1.get("advt_no").toString();
                post_no = map1.get("post_no").toString();
                post_name = map1.get("post_name").toString();

                boolean toContinue = false;

                int retVal = new AdvertisementOperation(emailLogId).flushPrevApplication(advt_no_flush, post_no_flush,
                        eMail, "");
                String msg = "success";
                if (retVal == -3) {
                    msg = "Maximum Attempts exceeded. Your latest application is not deleted and will be considered for the post.";
                } else if (retVal < 0) {
                    msg = "Problem in flushing the application";
                } else {
                    toContinue = true;
                }
                days = 0;
                months = 0;
                years = 0;
                totYears = 0;
                totMonths = 0;
                totDays = 0;
                totExp = "0";

                if (!toContinue) {
                    JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
                    jMetaArray.add("error:" + msg);
                    JsonObjectBuilder json = Json.createObjectBuilder();
                    json.add("Results", jMetaArray);
                    javax.json.JsonObject jsonObj = json.build();
                    StringWriter writer = new StringWriter();

                    PrintWriter pw = response.getWriter();
                    Json.createWriter(writer).write(jsonObj);
                    resultStr = writer.toString();
                    logMgr.accessLog("jsonResponse : " + resultStr);
                    response.setContentType("application/Json;charset=utf-8\"");
                    pw.write(resultStr);
                    con.close();
                    pw.close();
                    return;
                } else {
                    //try {
                    logMgr.accessLog("Submit application process started.....");

                    RegistrationRelation regRel = new RegistrationRelation();
                    Iterator<Entry<String, String>> hmIterator = map1.entrySet().iterator();
                    String regRelInfo = new String();

                    while (hmIterator.hasNext()) {
                        Map.Entry mapElement = (Map.Entry) hmIterator.next();
                        regRelInfo = (String) mapElement.getKey();
                        //logMgr.accessLog("THE reg_relSTRING is  :" + regRelInfo);

                        String mapValue = (String) map1.get(regRelInfo).trim();

                        if (regRelInfo.equalsIgnoreCase("email")) {
                            regRel.setEmail(mapValue);
                        }
                    }

                    logMgr.accessLog("Button Type: " + buttonType);
                    if (buttonType.equalsIgnoreCase("preview")) {
                        previewFlag = true;
                    }
                    if (buttonType.equalsIgnoreCase("save")) {
                        saveFlag = true;
                    }

                    String eMail_dir = eMail;
                    eMail_dir = eMail_dir.replace("@", "_");
                    eMail_dir = eMail_dir.replace(".", "_");

                    LoadProperties lp = new LoadProperties();
                    String appDir = lp.getProperty("RECRUIT_FOLDER_DIR");
                    String adminDir = lp.getProperty("ADMIN_FOLDER_DIR");
                    String filePath = appDir + File.separator + eMail_dir + File.separator + advt_no + File.separator + post_no + File.separator;

                    Iterator<Entry<String, String>> hmIterator1 = map1.entrySet().iterator();

                    String applicantInfo = new String();
                    logMgr.accessLog("Starting with personal data.......");
                    while (hmIterator1.hasNext()) {
                        Map.Entry mapElement = (Map.Entry) hmIterator1.next();
                        applicantInfo = (String) mapElement.getKey();
                        // THE p_STRING is :" +
                        // applicantInfo);

                        String mapValue = (String) map1.get(applicantInfo).trim();

                        //  personalData.setRegistration_id(registration_id);
                        if (applicantInfo.equalsIgnoreCase("advt_no")) {
                            advt_no = mapValue;
                            personalData.setAdvt_no(mapValue);
                            // persMap.put("AdvtNo", mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("post_no")) {
                            post_no = mapValue;
                            personalData.setPost_no(mapValue);
                            // persMap.put("PostNo", mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("tnc")) {
                            if (!previewFlag) {
                                if (mapValue.equalsIgnoreCase("No")) {
                                    message = "Please accept the terms and conditions";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);

                                    return;
                                }
                            }
                        }

                        if (applicantInfo.equalsIgnoreCase("edu_others")) {
                            mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            personalData.setRemarks(mapValue);// DatabaseUtils.sqlEscapeString(userString);
                            remarksMap.put("Details", mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("net")) {
                            mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            if (mapValue.equals("NA")) {
                                //personalData.setServiceman_cert(mapValue);
                            } else {
                                netData.setRegistration_id(registration_id);
                                netData.setAdvt_no(advt_no);
                                netData.setPost_no(post_no);
                                StringTokenizer stNet = new StringTokenizer(mapValue, "_");
                                netData.setNet_name(stNet.nextToken());
                                netMap.put("Examination", netData.getNet_name());
                                String netDocName = stNet.nextToken();

                                {
                                    boolean validValFlag = true;
                                    validValFlag = isFileAvaiable(netDocName, filePath);
                                    if (!validValFlag) {
                                        message = "Please Upload NET Certificate in jpg/pdf format";
                                        validValFlag = false;
                                    } else {
                                        validValFlag = isCertValidPattern(netDocName);
                                        if (!validValFlag) {
                                            message = "Special characters in filename not allowed in uploading NET Certificate";
                                            validValFlag = false;
                                        } else {
                                            fileExtn = getFileExtension(netDocName); // HRD ChangeId:2023080801
                                            if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                    || fileExtn.equalsIgnoreCase("pdf")) {
                                                validValFlag = true;
                                            } else {
                                                message = "Please enter NET Certificate in jpg/pdf format";
                                                validValFlag = false;
                                            }
                                        }
                                    }

                                    if (validValFlag) {
                                        netData.setNet_doc(netDocName);
                                    } else {
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }
                                }

                                // netMap.put("Doc Name", netData.getNet_doc());
                            }
                        }
                        System.out.println(CurrentDateTime.dateTime() + "insertflag.123.......>>>>>>>>>:" + insertFlag + "insertFlag 13456.");
                        Pattern matchPattern; // ChangeId: 2023111001
                        if (applicantInfo.equalsIgnoreCase("salutation")) {
                            usrNamePtrn = Pattern.compile("[A-za-z]{2,10}[.]{1}");

                            /*
                             * if (validatePatern(mapValue, usrNamePtrn)) {
                             * personalData.setSalutation(mapValue); } else { message =
                             * "Invalid Salutation"; buttonType = "error"; getErrMsg(message, buttonType,
                             * response, con);
                             * 
                             * return; }
                             */
                            personalData.setSalutation(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("first_name")) {
                            logMgr.accessLog("First name is :" + mapValue);
                            mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            logMgr.accessLog("First name is after:" + mapValue);
                            if (mapValue.equals("NA")) {
                                personalData.setFirst_name("");
                            } else {
                                usrNamePtrn = Pattern.compile("[a-z A-Z. ]{0,60}"); //ChangeId: 2025052003
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    personalData.setFirst_name(mapValue);
                                    persMap.put("Name", personalData.getFirst_name());
                                } else {
                                    message = "Invalid First Name";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }

                        if (applicantInfo.equalsIgnoreCase("middle_name")) {
                            // logMgr.accessLog( "middle_name name is :" + mapValue);
                            mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            /*
                             * logMgr.accessLog( "middle_name name is after:" + mapValue); if
                             * (mapValue.equals("NA")) { personalData.setMiddle_name(""); } else {
                             * usrNamePtrn = Pattern.compile("[a-z A-Z. ]{0,50}"); if
                             * (validatePatern(mapValue, usrNamePtrn)) {
                             * personalData.setMiddle_name(mapValue); } else { message =
                             * "Invalid Middle Name"; buttonType = "error"; getErrMsg(message, buttonType,
                             * response, con); return; } }
                             */
                            personalData.setMiddle_name(" ");
                        }

                        if (applicantInfo.equalsIgnoreCase("last_name")) {
                            mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            personalData.setLast_name(" ");
                            /*
                             * if (mapValue.equals("NA")) { personalData.setLast_name(""); } else {
                             * usrNamePtrn = Pattern.compile("[a-z A-Z. ]{0,50}"); if
                             * (validatePatern(mapValue, usrNamePtrn)) {
                             * personalData.setLast_name(mapValue);
                             * 
                             * } else { message = "Invalid Last Name"; getErrMsg(message, buttonType,
                             * response, con); buttonType = "error"; return;
                             * 
                             * } String fullName = personalData.getSalutation() + " " +
                             * personalData.getFirst_name() + " " + personalData.getMiddle_name() + " " +
                             * personalData.getLast_name(); persMap.put("Name", fullName);
                             */

                        }

                        if (applicantInfo.equalsIgnoreCase("mother_name")) {
                            logMgr.accessLog("First name is :" + mapValue);
                            mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            logMgr.accessLog("First name is after:" + mapValue);
                            usrNamePtrn = Pattern.compile("[a-z A-Z. ]{0,50}");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setMotherName(mapValue);
                                persMap.put("Mother Name", mapValue);
                            } else {
                                message = "Invalid Mothers Name";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }
                        }

                        if (applicantInfo.equalsIgnoreCase("father_name")) {
                            mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            usrNamePtrn = Pattern.compile("[a-z A-Z. ]{0,50}");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setFatherName(mapValue);
                                persMap.put("Father Name", mapValue);
                            } else {
                                message = "Invalid Fathers Name";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }
                        }

                        if (applicantInfo.equalsIgnoreCase("gender")) {
                            usrNamePtrn = Pattern.compile("[a-zA-Z]{3,20}"); // ChangeId: 2023121502 max length 10 -> 20
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setGender(mapValue);
                                persMap.put("Gender", mapValue);
                            } else {
                                message = "Invalid Gender";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("marital_status")) {
                            if (mapValue.equals("NA")) {
                                personalData.setPlace_of_birth("");
                            } else {
                                usrNamePtrn = Pattern.compile("[a-zA-Z]{3,20}");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    personalData.setMarital_status(mapValue);
                                    persMap.put("Marital Status", mapValue);
                                } else {
                                    message = "Invalid Marital Status";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }

                        if (applicantInfo.equalsIgnoreCase("dob")) {
                            // 1998-03-04 [0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9]));
                            usrNamePtrn = Pattern.compile("[0-9]{4}-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setDob(Date.valueOf(mapValue));
                                persMap.put("Date Of Birth", getStrDate(mapValue));

                            } else {
                                message = "Invalid Date Of Birth " + mapValue;
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("place_of_birth")) {
                            if (mapValue.equals("NA")) {
                                personalData.setPlace_of_birth("");
                            } else {
                                usrNamePtrn = Pattern.compile("[a-zA-Z. ]{3,30}");
                                if (validatePatern(mapValue, usrNamePtrn)) {

                                    personalData.setPlace_of_birth(mapValue);
                                    persMap.put("Place Of Birth", mapValue);
                                } else {
                                    message = "Invalid Place of Birth";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("nationality")) {
                            usrNamePtrn = Pattern.compile("[a-zA-Z]{2,10}");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setNationality(mapValue);
                                persMap.put("Nationality", mapValue);
                            } else {
                                message = "Invalid Nationality";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("category")) {
                            System.out.println(CurrentDateTime.dateTime() + ":"
                                    + "TEH CATEGORY IS	....................................." + mapValue);
                            personalData.setCategory(mapValue);
                            persMap.put("Category", mapValue);
                            /*
                             * if (!mapValue.equalsIgnoreCase("NA")) { usrNamePtrn =
                             * Pattern.compile("[a-zA-Z]{2,20}[_][1-9][0-9][_][1-9][0-9]"); if
                             * (validatePatern(mapValue, usrNamePtrn)) { personalData.setCategory(mapValue);
                             * persMap.put("Category", mapValue); } else { message = "Invalid Category " +
                             * mapValue; buttonType = "error"; getErrMsg(message, buttonType, response,
                             * con); return; }
                             * 
                             * }
                             */
                        }

                        if (applicantInfo.equalsIgnoreCase("reservation_proof")) {
                            if (mapValue.equals("NA")) {
                                personalData.setReservation_cert(mapValue);
                            } else {
                                boolean validValFlag = true;
                                validValFlag = isFileAvaiable(mapValue, filePath);
                                if (!validValFlag) {
                                    message = "Please Upload Reservation Certificate in jpg/pdf format";
                                    validValFlag = false;
                                } else {
                                    validValFlag = isCertValidPattern(mapValue);
                                    if (!validValFlag) {
                                        message = "Special characters in filename not allowed in uploading Reservation Certificate";
                                        validValFlag = false;
                                    } else {
                                        fileExtn = getFileExtension(mapValue); // HRD ChangeId:2023080801
                                        if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                || fileExtn.equalsIgnoreCase("pdf")) {
                                            validValFlag = true;
                                        } else {
                                            message = "Please enter Reservation Certificate in jpg/pdf format";
                                            validValFlag = false;
                                        }
                                    }
                                }

                                if (validValFlag) {
                                    personalData.setReservation_cert(mapValue);
                                    persMap.put("Reservation Certificate", mapValue);
                                } else {
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }

                        if (applicantInfo.indexOf("pwd") >= 0) {
                            logMgr.accessLog("map value is ....>" + mapValue);
                            //if (!mapValue.equalsIgnoreCase("NO")) // ChangeId: 2023111402
                            {
                                String pwdVal = map1.get("category_pwd");
                                personalData.setCategory_pwd(pwdVal);
                                // persMap.put("PWD", mapValue);
                                persMap.put("PWD", pwdVal);
                                
                                /*Start: ChangeId:2025050701*/
                                if(!pwdVal.equalsIgnoreCase("No")){
                                    /*Start: ChangeId:2025041501*/
                                    String pwdScribeVal = map1.get("category_pwd_scribe");
                                    personalData.setCategoryPwdScribe(pwdScribeVal);
                                    persMap.put("PWD Scribe", pwdScribeVal);
                                    /*End: ChangeId:2025041501*/
                                    
                                    /*Start: ChangeId:2025050807*/
                                    String pwdCompTimeVal = map1.get("category_pwd_comptime");
                                    personalData.setCategoryPwdCompTime(pwdCompTimeVal);
                                    persMap.put("PWD Compensatory Time", pwdCompTimeVal);
                                    /*End: ChangeId:2025050807*/
                                }
                                /*End: ChangeId:2025050701*/
                                
                                
                            }
                        }

                        if (applicantInfo.equalsIgnoreCase("category_pwd_certificate")) {
                            System.out.println("Disability Certificate: "+mapValue);
                            if (mapValue.equals("NA")) {
                                personalData.setDisability_cert(mapValue);
                                persMap.put("PWD Certificate", mapValue);
                            } else {

                                boolean validValFlag = true;
                                validValFlag = isFileAvaiable(mapValue, filePath);
                                if (!validValFlag) {
                                    message = "Please Upload Disability Certificate in jpg/pdf format";
                                    validValFlag = false;
                                } else {
                                    validValFlag = isCertValidPattern(mapValue);
                                    if (!validValFlag) {
                                        message = "Special characters in filename not allowed in uploading Disability Certificate";
                                        validValFlag = false;
                                    } else {
                                        fileExtn = getFileExtension(mapValue); // HRD ChangeId:2023080801
                                        if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                || fileExtn.equalsIgnoreCase("pdf")) {
                                            validValFlag = true;
                                        } else {
                                            message = "Please enter Disability Certificate in jpg/pdf format";
                                            validValFlag = false;
                                        }
                                    }
                                }

                                if (validValFlag) {
                                    personalData.setDisability_cert(mapValue);
                                    persMap.put("PWD Certificate", mapValue);
                                } else {
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }
                        //Start: ChangeId:2023111101
                        if (applicantInfo.indexOf("cgov_serv") >= 0) {
                            // exservice IS
                            // ................."+ mapValue);
                            //persMap.put("Central Gov Servant ", mapValue);
                            if (mapValue.equalsIgnoreCase("Yes") || mapValue.equalsIgnoreCase("No") ) { // ChangeId: 2023110902
                                String doj = map1.get("cgov_serv_doj");
                                //String to = map1.get("category_exservice_to");
                                //System.out.println("FROM AND TO IS ....." + from + "," + to);
                                //personalData.setCategory_exservice("Yes"); // ChangeId: 2023110902
                                personalData.setCGov_Serv(mapValue); // ChangeId: 2023110902
                                persMap.put("Central Gov Servant ", mapValue);
                                if (!doj.equalsIgnoreCase("NA")) {
                                    personalData.setCGov_Serv_DOJ(Date.valueOf(doj));
                                    persMap.put("Central Gov Service DOJ ", getStrDate(doj));
                                }
                                //if (!to.equalsIgnoreCase("NA")) {
                                //    personalData.setCategory_exservice_to(Date.valueOf(to));
                                //    persMap.put("ExService To", getStrDate(to));
                                //}
                                // persMap.put("ExService", mapValue);
                            }
                            
                        }
                        //End: ChangeId:2023111101
                        
                        // Start: ChangeId: 2025050703
                        if (applicantInfo.equalsIgnoreCase("category_exservice")) {
                            persMap.put("Ex-Serviceman", mapValue);
                        }
                        // End: ChangeId: 2025050703
                        if (applicantInfo.indexOf("exservice") >= 0) {
                            // exservice IS
                            // ................."+ mapValue);
                            if (mapValue.equalsIgnoreCase("Yes") || mapValue.equalsIgnoreCase("No") ) { // ChangeId: 2023110902
                                String from = map1.get("category_exservice_from");
                                String to = map1.get("category_exservice_to");
                                //System.out.println("FROM AND TO IS ....." + from + "," + to);
                                //personalData.setCategory_exservice("Yes"); // ChangeId: 2023110902
                                personalData.setCategory_exservice(mapValue); // ChangeId: 2023110902
                                if (!from.equalsIgnoreCase("NA")) {
                                    personalData.setCategory_exservice_from(Date.valueOf(from));
                                    if(mapValue.equalsIgnoreCase("Yes"))
                                        persMap.put("Ex-Serviceman From ", getStrDate(from));
                                }
                                if (!to.equalsIgnoreCase("NA")) {
                                    personalData.setCategory_exservice_to(Date.valueOf(to));
                                    if(mapValue.equalsIgnoreCase("Yes"))
                                        persMap.put("Ex-Serviceman To", getStrDate(to));

                                }
                                // persMap.put("ExService", mapValue);
                            }
                        }
                            
                        if (applicantInfo.equalsIgnoreCase("serviceman_proof")) {
                            if (mapValue.equals("NA")) {
                                personalData.setServiceman_cert(mapValue);
                            } else {

                                boolean validValFlag = true;
                                validValFlag = isFileAvaiable(mapValue, filePath);
                                if (!validValFlag) {
                                    message = "Please Upload ExService Man Certificate in jpg/pdf format";
                                    validValFlag = false;
                                } else {
                                    validValFlag = isCertValidPattern(mapValue);
                                    if (!validValFlag) {
                                        message = "Special characters in filename not allowed in uploading ExService Man Certificate";
                                        validValFlag = false;
                                    } else {
                                        fileExtn = getFileExtension(mapValue); // HRD ChangeId:2023080801
                                        if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                || fileExtn.equalsIgnoreCase("pdf")) {
                                            validValFlag = true;
                                        } else {
                                            message = "Please enter ExService Man Certificate in jpg/pdf format";
                                            validValFlag = false;
                                        }
                                    }
                                }

                                if (validValFlag) {
                                    personalData.setServiceman_cert(mapValue);
                                    persMap.put("Ex-Serviceman Certificate", mapValue);
                                } else {
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }

                        if (applicantInfo.indexOf("merit_sport") >= 0) {
                            personalData.setCategory_merit(map1.get("category_merit_sport")); // ChangeId:2023083002 newly added
                            if (mapValue.equalsIgnoreCase("Yes")) {
                                personalData.setCategory_merit_sportname(map1.get("category_merit_sportname"));
                                personalData.setCategory_merit_sportlevel(map1.get("category_merit_sportlevel"));
                                // persMap.put("MeritSports", mapValue);
                                persMap.put("Sport Name", personalData.getCategory_merit_sportname());
                                persMap.put("Sport Level", personalData.getCategory_merit_sportlevel());
                            }
                        }
                        if (applicantInfo.indexOf("ews") >= 0) {
                            if (mapValue.equalsIgnoreCase("Yes") || mapValue.equalsIgnoreCase("No")) { // ChangeId:2023083002 'No' also saved to DB
                                personalData.setCategory_ews(map1.get("category_ews"));
                                // persMap.put("EWS", mapValue);
                                persMap.put("EWS", map1.get("category_ews"));
                            }
                        }

                        if (applicantInfo.equalsIgnoreCase("ews_cerfitificate")) {
                            if (mapValue.equals("NA")) {
                                personalData.setEws_cert(mapValue);
                            } else {

                                boolean validValFlag = true;
                                validValFlag = isFileAvaiable(mapValue, filePath);
                                if (!validValFlag) {
                                    message = "Please Upload EWS Certificate in jpg/pdf format";
                                    validValFlag = false;
                                } else {
                                    validValFlag = isCertValidPattern(mapValue);
                                    if (!validValFlag) {
                                        message = "Special characters in filename not allowed in uploading EWS Certificate";
                                        validValFlag = false;
                                    } else {
                                        fileExtn = getFileExtension(mapValue); // HRD ChangeId:2023080801
                                        if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                || fileExtn.equalsIgnoreCase("pdf")) {
                                            validValFlag = true;
                                        } else {
                                            message = "Please enter EWS Certificate in jpg/pdf format";
                                            validValFlag = false;
                                        }
                                    }
                                }

                                if (validValFlag) {
                                    personalData.setEws_cert(mapValue);
                                    persMap.put("EWS Certificate", mapValue);
                                } else {
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }

                        if (applicantInfo.equalsIgnoreCase("house_no")) {
                            mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            if (mapValue.equals("NA")) {
                                personalData.setHouse_no("");
                            } else {
                                usrNamePtrn = Pattern.compile("([a-z A-Z 0-9,.\\-\\/]{1,50})");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    personalData.setHouse_no(mapValue);
                                    presAddrMap.put("House No", mapValue);
                                } else {
                                    message = "Invalid House No";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("locality")) {
                            mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            if (mapValue.equals("NA")) {
                                personalData.setLocality("");
                            } else {
                                usrNamePtrn = Pattern.compile("([a-z A-Z 0-9,.\\-\\/]{1,50})");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    personalData.setLocality(mapValue);
                                    presAddrMap.put("Locality", mapValue);
                                } else {
                                    message = "Invalid Locality";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("town")) {
                            if (mapValue.equals("NA")) {
                                personalData.setTown("");
                            }
                            usrNamePtrn = Pattern.compile("[a-zA-Z .]{1,30}");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setTown(mapValue);
                                presAddrMap.put("Town", mapValue);
                            } else {
                                message = "Invalid Town";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("state")) {
                            usrNamePtrn = Pattern.compile("[a-zA-Z .]{1,30}");
                            if (validatePatern(mapValue, usrNamePtrn)) {

                                personalData.setState(mapValue);
                                presAddrMap.put("State", mapValue);
                            } else {
                                message = "Invalid State";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("district")) {
                            usrNamePtrn = Pattern.compile("[a-zA-Z 0-9 ]{1,30}");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setDistrict(mapValue);
                                presAddrMap.put("District", mapValue);
                            } else {
                                message = "Invalid District";
                                getErrMsg(message, buttonType, response, con);
                                buttonType = "error";
                                return;
                            }

                        }

                        if (applicantInfo.equalsIgnoreCase("photograph")) {
                            if (mapValue.equals("NA")) {
                                personalData.setPhoto(mapValue);
                            } else {
                                photoExtn = getFileExtension(mapValue);
                                if (photoExtn.equalsIgnoreCase("jpg") || photoExtn.equalsIgnoreCase("jpeg")) {
                                    personalData.setPhoto(mapValue);
                                } else {
                                    message = "Please enter Photo in jpg format";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }

                        if (applicantInfo.equalsIgnoreCase("signature")) {
                            if (mapValue.equals("NA")) {
                                personalData.setSignature(mapValue);
                            } else {
                                signatureExtn = getFileExtension(mapValue);
                                if (signatureExtn.equalsIgnoreCase("jpg") || signatureExtn.equalsIgnoreCase("jpeg")) {
                                    personalData.setSignature(mapValue);
                                } else {
                                    message = "Please enter signature in jpg format";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }
                        }

                        if (applicantInfo.equalsIgnoreCase("pincode")) {
                            if (mapValue.equals("NA")) {
                                personalData.setPincode(-1);
                            } else {
                                usrNamePtrn = Pattern.compile("[1-9]{1}[0-9]{1,5}");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    personalData.setPincode(Integer.parseInt(mapValue));
                                    presAddrMap.put("Pincode", mapValue);
                                } else {
                                    message = "Invalid Pincode";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("p_house_no")) {
                            if (mapValue.equals("NA")) {
                                personalData.setP_house_no("");
                            } else {
                                mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                                usrNamePtrn = Pattern.compile("([a-z A-Z 0-9,.\\-\\/]{1,50})");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    personalData.setP_house_no(mapValue);
                                    permAddrMap.put("House No", mapValue);
                                } else {
                                    message = "Invalid Permanent House No";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }
                        }
                        if (applicantInfo.equalsIgnoreCase("p_locality")) {
                            if (mapValue.equals("NA")) {
                                personalData.setP_locality("");
                            } else {
                                mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                                usrNamePtrn = Pattern.compile("([a-z A-Z 0-9,.\\-\\/]{1,50})");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    personalData.setP_locality(mapValue);
                                    permAddrMap.put("Locality", mapValue);
                                } else {
                                    message = "Invalid Permanent Locality";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("p_town")) {
                            if (mapValue.equals("NA")) {
                                personalData.setP_town("");
                            } else {
                                usrNamePtrn = Pattern.compile("[a-zA-Z .]{1,30}");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    personalData.setP_town(mapValue);
                                    permAddrMap.put("Town", mapValue);
                                } else {
                                    message = "Invalid  Permanent Town";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("p_state")) {
                            usrNamePtrn = Pattern.compile("[a-zA-Z .]{1,30}");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setP_state(mapValue);
                                permAddrMap.put("State", mapValue);
                            } else {
                                message = "Invalid Permanent State";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("p_district")) {
                            usrNamePtrn = Pattern.compile("[a-zA-Z 0-9]{1,30}");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setP_district(mapValue);
                                permAddrMap.put("District", mapValue);
                            } else {
                                message = "Invalid Permanent District";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }

                        }

                        if (applicantInfo.equalsIgnoreCase("p_pincode")) {
                            if (mapValue.equals("NA")) {
                                mapValue = "-1";
                            }
                            if (saveFlag) {
                                personalData.setP_pincode(Integer.parseInt(mapValue));
                                permAddrMap.put("Pincode", mapValue);
                            } else {
                                usrNamePtrn = Pattern.compile("[1-9]{1}[0-9]{5}");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    personalData.setP_pincode(Integer.parseInt(mapValue));
                                    permAddrMap.put("Pincode", mapValue);
                                } else {
                                    message = "Invalid Permanent Pincode";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("contact_no")) {
                            usrNamePtrn = Pattern.compile("[6-9]{1}[0-9]{9}");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setContact_no(Long.parseLong(mapValue));
                                persMap.put("Contact No", mapValue);
                                persMap.put("Email", eMail);
                            } else {
                                message = "Invalid contact number";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }
                        }
                        if (applicantInfo.equalsIgnoreCase("alternate_contact")) {

                            if (mapValue.equalsIgnoreCase("NA")) {
                                mapValue = "0";
                            }
                            personalData.setAlternate_contact(Long.parseLong(mapValue));
                            if (mapValue.equalsIgnoreCase("0")) {
                                persMap.put("Alternate Contact No", "-");
                            } else {
                                persMap.put("Alternate Contact No", mapValue);
                            }

                            /*
                             * usrNamePtrn = Pattern.compile("[6-9]{1}[0-9]{9}"); if
                             * (validatePatern(mapValue, usrNamePtrn)) { } else { message =
                             * "Invalid alternate contact number"; buttonType="error"; getErrMsg(message,
                             * buttonType, response, con); return; }
                             */
                        }
                        // PPEG-HRD: Start AADHAAR
                        if (applicantInfo.equalsIgnoreCase("aadhaar")) {

                            if (mapValue.equalsIgnoreCase("NA")) {
                                mapValue = "0";
                            }
                            personalData.setAadhaar(Long.parseLong(mapValue));
                            if (mapValue.equalsIgnoreCase("0")) {
                                persMap.put("Aadhaar No", "-");
                            } else {
                                persMap.put("Aadhaar No", mapValue);
                            }
                        }
                        // PPEG-HRD: End AADHAAR
                        
                        if (applicantInfo.equalsIgnoreCase("nearest_railway_station")) {
                            usrNamePtrn = Pattern.compile("[a-zA-Z .]{1,30}");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                personalData.setNearest_railway_station(mapValue);
                                persMap.put("Nearest Railway Station", mapValue);
                            } else {
                                message = "Invalid Near Railway Station";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }

                        }
                        
                        //Start: ChangeId:2025041601
                        if (applicantInfo.equalsIgnoreCase("bank_acc_beneficiary")) {
                            matchPattern = Pattern.compile("^[A-Za-z. ]{1,60}$"); //ChangeId: 2025052003
                            if (validatePatern(mapValue, matchPattern)) {
                                personalData.setBank_Acc_Beneficiary(mapValue);
                                bankMap.put("Bank Account Beneficiary", mapValue);
                            } else {
                                message = "Invalid Bank Account Beneficiary";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }
                        }
                        //End: ChangeId:2025041601
                        
                        // Start: ChangeId: 2023111001
                        if (applicantInfo.equalsIgnoreCase("bank_acc_no")) {
                            matchPattern = Pattern.compile("^[0-9]{9,18}$");
                            if (validatePatern(mapValue, matchPattern)) {
                                personalData.setBank_Acc_No(mapValue);
                                bankMap.put("Bank Account No", mapValue);
                            } else {
                                message = "Invalid Bank Account Number";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }
                        }
                        if (applicantInfo.equalsIgnoreCase("bank_ifsc_code")) {
                            matchPattern = Pattern.compile("^[a-zA-Z]{4}[0]{1}[a-zA-Z0-9]{6}$");
                            if (validatePatern(mapValue, matchPattern)) {
                                personalData.setBank_IFSC_Code(mapValue);
                                bankMap.put("Bank IFSC Code", mapValue);
                            } else {
                                message = "Invalid Bank IFSC Code";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }
                        }
                        // End: ChangeId: 2023111001
                        
                        //Start: ChangeId:2025041601
                        if (applicantInfo.equalsIgnoreCase("bank_acc_doc")) {
                            if (saveFlag) {
                                personalData.setBank_Acc_Doc(mapValue);
                                bankMap.put("Bank Account Document", mapValue);
                            } else {
                                if (!mapValue.equalsIgnoreCase("NA")) {

                                    boolean validValFlag = true;
                                    validValFlag = isFileAvaiable(mapValue, filePath);
                                    if (!validValFlag) {
                                        message = "Please upload Bank Account Document in jpg/pdf format";
                                        validValFlag = false;
                                    } else {
                                        validValFlag = isCertValidPattern(mapValue);
                                        if (!validValFlag) {
                                            message = "Special characters in filename not allowed in uploading Bank account document";
                                            validValFlag = false;
                                        } else {
                                            fileExtn = getFileExtension(mapValue); // HRD ChangeId:2023080801
                                            if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                    || fileExtn.equalsIgnoreCase("pdf")) {
                                                validValFlag = true;
                                            } else {
                                                message = "Please enter Bank Account Document in jpg/pdf format";
                                                validValFlag = false;
                                            }
                                        }
                                    }

                                    if (validValFlag) {
                                        personalData.setBank_Acc_Doc(mapValue);
                                        bankMap.put("Bank Account Document", mapValue);
                                    } else {
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }

                                } else {
                                    message = "Upload Bank Account Document";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }
                        //End: ChangeId:2025041601

                        if (applicantInfo.equalsIgnoreCase("zone")) {
                            matchPattern = Pattern.compile("[-@/a-zA-Z .]{1,}"); // ChangeId: 2023112001; - and @ are included, 2023121603 / included
                            //System.out.println("permap zone value before :" + mapValue);
                            if (validatePatern(mapValue, matchPattern)) {
                                //System.out.println("permap zone value:" + mapValue);
                                personalData.setZone(mapValue);
                                // Start: ChangeId: 2023121301
                                String[] locations = mapValue.split("@");
                                if (locations.length == 3) {
                                    persMap.put("Exam-centre Pref 1", locations[0]);
                                    persMap.put("Exam-centre Pref 2", locations[1]);
                                    persMap.put("Exam-centre Pref 3", locations[2]);
                                }
                                else{
                                    message = "Insufficient Exam-centre preferences";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                }
                                // End: ChangeId: 2023121301
                            } else {
                                message = "Invalid Exam-centre preference";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }

                        }

                        if (applicantInfo.equalsIgnoreCase("eligibility")) {
                            logMgr.accessLog("eligib is.......: " + mapValue);
                            applicant_eligibility = mapValue.split("\\|");
                            logMgr.accessLog("applicant_eligibility is : " + applicant_eligibility);
                            elgArray = Arrays.asList(applicant_eligibility);
                            System.out.println(
                                    CurrentDateTime.dateTime() + ":" + "elgArray is before : " + elgArray.toString());
                            /*
                             * if (elgArray.indexOf("Graduate") >= 0) { elgArray.add("X");
                             * elgArray.add("XII"); }
                             */
                            System.out.println(
                                    CurrentDateTime.dateTime() + ":" + "elgArray is after : " + elgArray.toString());
                        }

                        if (applicantInfo.equalsIgnoreCase("x_edu_board")) {
                            logMgr.accessLog("mao Value:" + mapValue);
                            usrNamePtrn = Pattern.compile("[a-zA-Z .]{1,75}(.*?)");
                            if (validatePatern(mapValue, usrNamePtrn)) {
                                xData.setX_edu_board(mapValue);
                                xMap.put("Education Board", mapValue);
                            } else {
                                message = "Invalid  Xth Board";
                                buttonType = "error";
                                getErrMsg(message, buttonType, response, con);
                                return;
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("x_school")) {
                            mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            xData.setX_school(mapValue);
                            xMap.put("School Name", mapValue);
                            /*
                             * usrNamePtrn = Pattern.compile("[a-z A-Z.']{1,75} (.*?)"); if
                             * (validatePatern(mapValue, usrNamePtrn)) { xData.setX_school(mapValue);
                             * xMap.put("School", mapValue); } else { message = "Invalid  Xth School";
                             * buttonType = "error"; getErrMsg(message, buttonType, response, con); return;
                             * }
                             */

                        }
                        if (applicantInfo.equalsIgnoreCase("x_year_of_passing")) {
                            if (mapValue.equals("NA")) {
                                mapValue = "-1";
                            }
                            if (saveFlag) {
                                xData.setX_year_of_passing(Integer.parseInt(mapValue));
                                xMap.put("Year of Passing", mapValue);
                            } else {
                                usrNamePtrn = Pattern.compile("[1-2]{1}[0-9]{3}");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    xData.setX_year_of_passing(Integer.parseInt(mapValue));
                                    xMap.put("Year of Passing", mapValue);
                                } else {
                                    message = "Invalid X Year Of Passing";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }
                        }
                        if (applicantInfo.equalsIgnoreCase("x_division")) {
                            if (saveFlag) {
                                xData.setX_division(mapValue);
                                xMap.put("Division", mapValue);
                            } else {
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    xData.setX_division(mapValue);
                                    xMap.put("Division", mapValue);
                                } else {
                                    message = "Invalid X division";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }
                        }
                        if (applicantInfo.equalsIgnoreCase("x_dc")) {
                            if (saveFlag) {
                                xData.setMarksheet(mapValue);
                                xMap.put("Marks Sheet-Filename", mapValue);
                            } else {
                                if (!mapValue.equalsIgnoreCase("NA")) {

                                    boolean validValFlag = true;
                                    validValFlag = isFileAvaiable(mapValue, filePath);
                                    if (!validValFlag) {
                                        message = "Please X Marks Sheet in jpg/pdf format";
                                        validValFlag = false;
                                    } else {
                                        validValFlag = isCertValidPattern(mapValue);
                                        if (!validValFlag) {
                                            message = "Special characters in filename not allowed in uploading X Marks Sheet";
                                            validValFlag = false;
                                        } else {
                                            fileExtn = getFileExtension(mapValue); // HRD ChangeId:2023080801
                                            if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                    || fileExtn.equalsIgnoreCase("pdf")) {
                                                validValFlag = true;
                                            } else {
                                                message = "Please enter X Marks Sheet in jpg/pdf format";
                                                validValFlag = false;
                                            }
                                        }
                                    }

                                    if (validValFlag) {
                                        xData.setMarksheet(mapValue);
                                        xMap.put("Marks Sheet-Filename", mapValue);
                                    } else {
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }

                                } else {
                                    message = "Upload X Marks Sheet";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                        }
                        if (applicantInfo.equalsIgnoreCase("x_percentage_cgpa")) {
                            // Start: ChangeId: 2023121901
                            /*
                            if (!mapValue.equalsIgnoreCase("NA")) {
                                xData.setX_percentage_cgpa(mapValue);
                                xMap.put("Percentage/CGPA", mapValue);
                            }
                            */
                            xData.setX_percentage_cgpa(mapValue);
                            
                            if (!map1.get("x_percentage").equals("-1") && !map1.get("x_percentage").equals("NA")) { // ChangeId: 2023121901
                                if (saveFlag) {
                                    usrNamePtrn = Pattern.compile("^-?\\d*\\.{0,1}\\d+$");
                                } else {
                                    usrNamePtrn = Pattern.compile("[0-9]+(\\.[0-9]{0,2})?%?");
                                }
                                if (validatePatern(map1.get("x_percentage"), usrNamePtrn)) {
                                    xData.setX_percentage(Float.parseFloat(map1.get("x_percentage")));
                                    xMap.put("Percentage", xData.getX_percentage()); // ChangeId: 2023121901
                                } else {
                                    message = "Invalid Percentage X Board: " + mapValue;
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }else{
                                xData.setX_percentage(-1);
                                xMap.put("Percentage", "-"); // ChangeId: 2023121901
                            }
                            // End: ChangeId: 2023121901
                            //if (mapValue.equalsIgnoreCase("CGPA")) { // ChangeId: 2023121901
                            if (!map1.get("x_cgpa_obt").equals("-1") && !map1.get("x_cgpa_obt").equals("NA")) { // ChangeId: 2023121901
                                xData.setX_cgpa_obt(Float.parseFloat(map1.get("x_cgpa_obt")));
                                xData.setX_cgpa_max(map1.get("x_cgpa_max"));
                                xData.setX_cgpa_to_perc(Float.parseFloat(map1.get("x_cgpa_to_perc"))); // ChangeId: 2023121901 x_cgpa_to_perc
                                if (saveFlag) {
                                    usrNamePtrn = Pattern.compile("^-?\\d*\\.{0,1}\\d+$");
                                } else {
                                    usrNamePtrn = Pattern.compile("[0-9]+(\\.[0-9]{0,2})?%?");
                                }
                                if (validatePatern(xData.getX_cgpa_obt() + "", usrNamePtrn)) {
                                    xMap.put("CGPA Obtained", xData.getX_cgpa_obt());
                                    xMap.put("Maximum CGPA", xData.getX_cgpa_max());
                                    // Start: ChangeId: 2023121901
                                    if( Float.parseFloat(map1.get("x_cgpa_max"))>=0 && Float.parseFloat(map1.get("x_cgpa_max"))<10.0 ){
                                        xMap.put("CGPA to Percentage", xData.getX_cgpa_to_perc());
                                    }
                                    // End: ChangeId: 2023121901
                                } else {
                                    message = "Invalid CGPA X Board " + mapValue + "," + xData.getX_cgpa_obt();
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }

                            }else{
                                xData.setX_cgpa_obt(-1);
                                xData.setX_cgpa_max("");
                                xData.setX_cgpa_to_perc(-1); // ChangeId: 2023121901 x_cgpa_to_perc
                                xMap.put("CGPA Obtained", "-");
                                xMap.put("Maximum CGPA", "-");
                                xMap.put("CGPA to Percentage", "-");
                            }
                            
                            //else if (mapValue.equalsIgnoreCase("percentage")) { // ChangeId: 2023121901
                            
                            // Start: ChangeId: 2023121901
                            //String perc = xData.getX_percentage() + "";
                            //if (perc.equals("-1.0")) {
                            //    perc = "-";
                            // }

                            //xMap.put("Percentage", perc); 
                            // End: ChangeId: 2023121901
                        }
                        
                        if (XIIFlag) {
                            if (applicantInfo.equalsIgnoreCase("xii_edu_board")) {
                                xiiData.setXii_edu_board(mapValue);
                                xiiMap.put("Education Board", mapValue);
                                // Education board ..." +
                                // mapValue);
							/*
                                 * usrNamePtrn = Pattern.compile("[a-zA-Z .]{1,75} (.*?)"); if
                                 * (validatePatern(mapValue, usrNamePtrn)) { xiiData.setXii_edu_board(mapValue);
                                 * xiiMap.put("Board", mapValue); } else { message = "Invalid XIIth Board";
                                 * buttonType = "error"; getErrMsg(message, buttonType, response, con); return;
                                 * }
                                 */

                            }
                            if (applicantInfo.equalsIgnoreCase("xii_school")) {
                                mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                                usrNamePtrn = Pattern.compile("[a-z A-Z.']{1,75}");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    xiiData.setXii_school(mapValue);
                                    xiiMap.put("School Name", mapValue);
                                } else {
                                    message = "Invalid  XIIth School";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }

                            }
                            if (applicantInfo.equalsIgnoreCase("xii_year_of_passing")) {
                                if (saveFlag) {
                                    int xii_year_of_passing = Integer.parseInt(mapValue);
                                    xiiData.setXii_year_of_passing(xii_year_of_passing);
                                    xiiMap.put("Year of Passing", mapValue);
                                } else {
                                    usrNamePtrn = Pattern.compile("[1-2]{1}[0-9]{3}");
                                    if (validatePatern(mapValue, usrNamePtrn)) {
                                        int xii_year_of_passing = Integer.parseInt(mapValue);
                                        xiiData.setXii_year_of_passing(xii_year_of_passing);
                                        xiiMap.put("Year of Passing", mapValue);
                                    } else {
                                        message = "Invalid XII Year Of Passing:" + saveFlag;
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }
                                }

                            }
                            if (applicantInfo.equalsIgnoreCase("xii_specialization")) {
                                mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                                // usrNamePtrn = Pattern.compile("[a-z A-Z.']{1,50}");
                                xiiData.setXii_specialization(mapValue);
                                xiiMap.put("Specialization", mapValue);
                            }
                            if (applicantInfo.equalsIgnoreCase("xii_division")) {
                                xiiData.setXii_division(mapValue);
                                xiiMap.put("Division", mapValue);
                            }
                            if (applicantInfo.equalsIgnoreCase("xii_dc")) {
                                if (saveFlag) {
                                    xiiData.setMarksheet(mapValue);
                                    xiiMap.put("Marks Sheet-Filename", mapValue);
                                } else {
                                    if (!mapValue.equalsIgnoreCase("NA")) {

                                        boolean validValFlag = true;
                                        validValFlag = isFileAvaiable(mapValue, filePath);
                                        if (!validValFlag) {
                                            message = "Please XII Marks Sheet in jpg/pdf format";
                                            validValFlag = false;
                                        } else {
                                            validValFlag = isCertValidPattern(mapValue);
                                            if (!validValFlag) {
                                                message = "Special characters in filename not allowed in uploading XII Marks Sheet";
                                                validValFlag = false;
                                            } else {
                                                fileExtn = getFileExtension(mapValue); // HRD ChangeId:2023080801
                                                if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                        || fileExtn.equalsIgnoreCase("pdf")) {
                                                    validValFlag = true;
                                                } else {
                                                    message = "Please enter XII Marks Sheet in jpg/pdf format";
                                                    validValFlag = false;
                                                }
                                            }
                                        }

                                        if (validValFlag) {
                                            xiiData.setMarksheet(mapValue);
                                            xiiMap.put("Marks Sheet-Filename", mapValue);
                                        } else {
                                            buttonType = "error";
                                            getErrMsg(message, buttonType, response, con);
                                            return;
                                        }

                                    } else {
                                        message = "Invalid XII Marks Sheet";
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }
                                }

                            }
                            /*
                            if (applicantInfo.equalsIgnoreCase("xii_percentage_cgpa")) {
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    xiiData.setXii_percentage_cgpa(mapValue);
                                    xiiMap.put("Percentage/CGPA", mapValue);
                                }
                                if (mapValue.equalsIgnoreCase("CGPA")) {
                                    xiiData.setXii_percentage_cgpa(mapValue);
                                    xiiData.setXii_cgpa_obt(Float.parseFloat(map1.get("xii_cgpa_obt")));
                                    xiiData.setXii_cgpa_max(map1.get("xii_cgpa_max"));
                                    xiiData.setXii_percentage(Float.parseFloat(map1.get("xii_percentage")));
                                    if (saveFlag) {
                                        usrNamePtrn = Pattern.compile("^-?\\d*\\.{0,1}\\d+$");
                                    } else {
                                        usrNamePtrn = Pattern.compile("[0-9]+(\\.[0-9]{0,2})?%?");
                                    }
                                    if (validatePatern(xiiData.getXii_cgpa_obt() + "", usrNamePtrn)) {
                                        xiiMap.put("CGPA Obtained", xiiData.getXii_cgpa_obt());
                                        xiiMap.put("Maximum CGPA", xiiData.getXii_cgpa_max());
                                        // xiiMap.put("Percentage", xiiData.getXii_percentage());

                                    } else {
                                        message = "Invalid  CGPA XII Board";
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }

                                } else if (mapValue.equalsIgnoreCase("percentage")) {
                                    xiiData.setXii_percentage_cgpa(mapValue);
                                    xiiData.setXii_percentage(Float.parseFloat(map1.get("xii_percentage")));
                                    // xiiMap.put("Percentage", xiiData.getXii_percentage());
                                }

                                String perc = xiiData.getXii_percentage() + "";
                                if (perc.equals("-1.0")) {
                                    perc = "-";
                                }

                                xiiMap.put("Percentage", perc);
                            }
                            */
                            if (applicantInfo.equalsIgnoreCase("xii_percentage_cgpa")) {
                                // Start: ChangeId: 2023122001
                                /*
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    xiiData.setXii_percentage_cgpa(mapValue);
                                    xiiMap.put("Percentage/CGPA", mapValue);
                                }
                                */
                                xiiData.setXii_percentage_cgpa(mapValue);
                                if (!map1.get("xii_percentage").equals("-1") && !map1.get("xii_percentage").equals("NA")) { // ChangeId: 2023122001
                                    if (saveFlag) {
                                        usrNamePtrn = Pattern.compile("^-?\\d*\\.{0,1}\\d+$");
                                    } else {
                                        usrNamePtrn = Pattern.compile("[0-9]+(\\.[0-9]{0,2})?%?");
                                    }
                                    if (validatePatern(map1.get("xii_percentage"), usrNamePtrn)) {
                                        xiiData.setXii_percentage(Float.parseFloat(map1.get("xii_percentage")));
                                        xiiMap.put("Percentage", xiiData.getXii_percentage()); // ChangeId: 2023122001
                                    } else {
                                        message = "Invalid  Percentage XII Board: " + mapValue;
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }
                                }else{
                                    xiiData.setXii_percentage(-1);
                                    xiiMap.put("Percentage", "-"); // ChangeId: 2023122001
                                }
                                // End: ChangeId: 2023122001
                                //if (mapValue.equalsIgnoreCase("CGPA")) { // ChangeId: 2023122001
                                if (!map1.get("xii_cgpa_obt").equals("-1") && !map1.get("xii_cgpa_obt").equals("NA")) { // ChangeId: 2023122001
                                    xiiData.setXii_cgpa_obt(Float.parseFloat(map1.get("xii_cgpa_obt")));
                                    xiiData.setXii_cgpa_max(map1.get("xii_cgpa_max"));
                                    xiiData.setXii_cgpa_to_perc(Float.parseFloat(map1.get("xii_cgpa_to_perc"))); // ChangeId: 2023122001 xii_cgpa_to_perc
                                    if (saveFlag) {
                                        usrNamePtrn = Pattern.compile("^-?\\d*\\.{0,1}\\d+$");
                                    } else {
                                        usrNamePtrn = Pattern.compile("[0-9]+(\\.[0-9]{0,2})?%?");
                                    }
                                    if (validatePatern(xiiData.getXii_cgpa_obt() + "", usrNamePtrn)) {
                                        xiiMap.put("CGPA Obtained", xiiData.getXii_cgpa_obt());
                                        xiiMap.put("Maximum CGPA", xiiData.getXii_cgpa_max());
                                        // Start: ChangeId: 2023122001
                                        if( Float.parseFloat(map1.get("xii_cgpa_max"))>=0 && Float.parseFloat(map1.get("xii_cgpa_max"))<10.0 ){
                                            xiiMap.put("CGPA to Percentage", xiiData.getXii_cgpa_to_perc());
                                        }
                                        // End: ChangeId: 2023122001
                                    } else {
                                        message = "Invalid CGPA XII Board " + mapValue + "," + xiiData.getXii_cgpa_obt();
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }

                                }else{
                                    xiiData.setXii_cgpa_obt(-1);
                                    xiiData.setXii_cgpa_max("");
                                    xiiData.setXii_cgpa_to_perc(-1); // ChangeId: 2023122001 xii_cgpa_to_perc
                                    xiiMap.put("CGPA Obtained", "-");
                                    xiiMap.put("Maximum CGPA", "-");
                                    xiiMap.put("CGPA to Percentage", "-");
                                } //else if (mapValue.equalsIgnoreCase("percentage")) { // ChangeId: 2023122001
                                
                                // Start: ChangeId: 2023122001
                                //String perc = xiiData.getXii_percentage() + "";
                                //if (perc.equals("-1.0")) {
                                //    perc = "-";
                                // }

                                //xiiMap.put("Percentage", perc); 
                                // End: ChangeId: 2023122001
                            }

                        }

                        // FOR ITI
                        if (ITIFlag) {
                            if (applicantInfo.equalsIgnoreCase("iti_type")) {
                                itiData.setQualType(mapValue);
                                itiMap.put("ITI", mapValue);
                            }
                            if (applicantInfo.equalsIgnoreCase("iti_college")) {
                                mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                                usrNamePtrn = Pattern.compile("[a-z A-Z.']{1,50}");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    itiData.setCollege(mapValue);
                                    itiMap.put("College", mapValue);
                                } else {
                                    message = "Invalid ITI College";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }

                            }
                            if (applicantInfo.equalsIgnoreCase("iti_specialization")) {
                                mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                                // usrNamePtrn = Pattern.compile("[a-z A-Z.']{1,50}");
                                itiData.setSpecialization(mapValue);
                                itiMap.put("Specialization", mapValue);
                            }
                            if (applicantInfo.equalsIgnoreCase("iti_year_of_passing")) {
                                if (saveFlag) {
                                    int xii_year_of_passing = Integer.parseInt(mapValue);
                                    int iti_year_of_passing = Integer.parseInt(mapValue);
                                    itiData.setYear_of_passing(iti_year_of_passing);
                                    itiMap.put("Year of Passing", iti_year_of_passing);
                                } else {
                                    usrNamePtrn = Pattern.compile("[1-2]{1}[0-9]{3}");
                                    if (validatePatern(mapValue, usrNamePtrn)) {
                                        int xii_year_of_passing = Integer.parseInt(mapValue);
                                        int iti_year_of_passing = Integer.parseInt(mapValue);
                                        itiData.setYear_of_passing(iti_year_of_passing);
                                        itiMap.put("Year of Passing", iti_year_of_passing);
                                    } else {
                                        message = "Invalid  ITI Year Of Passing";
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }
                                }
                            }

                            if (applicantInfo.equalsIgnoreCase("iti_division")) {
                                itiData.setDivision(mapValue);
                                itiMap.put("Division", mapValue);

                            }
                            if (applicantInfo.equalsIgnoreCase("iti_dc")) {
                                if (saveFlag) {
                                    itiData.setDegCert(mapValue);
                                    itiMap.put("Certificate-Filename", mapValue);
                                } else {
                                    if (!mapValue.equalsIgnoreCase("NA")) {

                                        boolean validValFlag = true;
                                        validValFlag = isFileAvaiable(mapValue, filePath);
                                        if (!validValFlag) {
                                            message = "Please iti degree certificate in jpg/pdf format";
                                            validValFlag = false;
                                        } else {
                                            validValFlag = isCertValidPattern(mapValue);
                                            if (!validValFlag) {
                                                message = "Special characters in filename not allowed in uploading iti degree certificate";
                                                validValFlag = false;
                                            } else {
                                                fileExtn = getFileExtension(mapValue); // HRD ChangeId:2023080801
                                                if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                        || fileExtn.equalsIgnoreCase("pdf")) {
                                                    validValFlag = true;
                                                } else {
                                                    message = "Please enter iti degree certificate in jpg/pdf format";
                                                    validValFlag = false;
                                                }
                                            }
                                        }

                                        if (validValFlag) {
                                            itiData.setDegCert(mapValue);
                                            itiMap.put("Certificate-Filename", mapValue);
                                        } else {
                                            buttonType = "error";
                                            getErrMsg(message, buttonType, response, con);
                                            return;
                                        }

                                    } else {
                                        message = "Upload iti degree certificate";
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }
                                }

                            }
                            if (applicantInfo.equalsIgnoreCase("iti_marksheet")) {
                                boolean validValFlag = true;
                                validValFlag = isFileAvaiable(mapValue, filePath);
                                if (!validValFlag) {
                                    message = "Please iti Marks Sheet in jpg/pdf format";
                                    validValFlag = false;
                                } else {
                                    validValFlag = isCertValidPattern(mapValue);
                                    if (!validValFlag) {
                                        message = "Special characters in filename not allowed in uploading iti Marks Sheet";
                                        validValFlag = false;
                                    } else {
                                        fileExtn = getFileExtension(mapValue); // HRD ChangeId:2023080801
                                        if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                || fileExtn.equalsIgnoreCase("pdf")) {
                                            validValFlag = true;
                                        } else {
                                            message = "Please enter iti Marks Sheet in jpg/pdf format";
                                            validValFlag = false;
                                        }
                                    }
                                }

                                if (validValFlag) {
                                    itiData.setMarksheet(mapValue);
                                    itiMap.put("Marks Sheet-Filename", mapValue);
                                } else {
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }

                            }
                            if (applicantInfo.equalsIgnoreCase("iti_percentage_cgpa")) {
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    itiData.setPercentage_cgpa(mapValue);
                                    itiMap.put("Percentage/CGPA", mapValue);
                                }
                                if (mapValue.equalsIgnoreCase("CGPA")) {
                                    itiData.setCgpa_obt(Float.parseFloat(map1.get("iti_cgpa_obt")));
                                    itiData.setCgpa_max(map1.get("iti_cgpa_max"));
                                    itiData.setPercentage(Float.parseFloat(map1.get("iti_percentage")));
                                    if (saveFlag) {
                                        usrNamePtrn = Pattern.compile("^-?\\d*\\.{0,1}\\d+$");
                                    } else {
                                        usrNamePtrn = Pattern.compile("[0-9]+(\\.[0-9]{0,2})?%?");
                                    }
                                    if (validatePatern(xData.getX_cgpa_obt() + "", usrNamePtrn)) {
                                        itiMap.put("CGPA Obtained", itiData.getCgpa_obt());
                                        itiMap.put("Maximum CGPA", itiData.getCgpa_max());
                                        // itiMap.put("Percentage", itiData.getPercentage());

                                    } else {
                                        message = "Invalid  CGPA  ITI";
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }

                                } else if (mapValue.equalsIgnoreCase("percentage")) {
                                    itiData.setPercentage(Float.parseFloat(map1.get("iti_percentage")));
                                    // itiMap.put("Percentage", itiData.getPercentage());
                                }

                                String perc = itiData.getPercentage() + "";
                                if (perc.equals("-1.0")) {
                                    perc = "-";
                                }

                                itiMap.put("Percentage", perc);
                            }
                        }
                        /**
                         * *****************************
                         */
                        // FOR DIPLOMA 
                        if (DIPFlag) {
                            if (applicantInfo.equalsIgnoreCase("dip_type")) {
                                dipData.setQualType(mapValue);
                                dipMap.put("Diploma", mapValue);
                            }
                            if (applicantInfo.equalsIgnoreCase("dip_college")) {
                                mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                                usrNamePtrn = Pattern.compile("[a-z A-Z.']{1,50}");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    dipData.setCollege(mapValue);
                                    dipMap.put("College", mapValue);
                                } else {
                                    message = "Invalid Diploma College";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }

                            }
                            if (applicantInfo.equalsIgnoreCase("dip_specialization")) {
                                mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                                // usrNamePtrn = Pattern.compile("[a-z A-Z.']{1,50}");
                                dipData.setSpecialization(mapValue);
                                dipMap.put("Specialization", mapValue);
                            }
                            if (applicantInfo.equalsIgnoreCase("dip_year_of_passing")) {
                                if (saveFlag) {
                                    int xii_year_of_passing = Integer.parseInt(mapValue);
                                    int iti_year_of_passing = Integer.parseInt(mapValue);
                                    dipData.setYear_of_passing(iti_year_of_passing);
                                    dipMap.put("Year of Passing", iti_year_of_passing);
                                } else {
                                    usrNamePtrn = Pattern.compile("[1-2]{1}[0-9]{3}");
                                    if (validatePatern(mapValue, usrNamePtrn)) {
                                        int xii_year_of_passing = Integer.parseInt(mapValue);
                                        int iti_year_of_passing = Integer.parseInt(mapValue);
                                        dipData.setYear_of_passing(iti_year_of_passing);
                                        dipMap.put("Year of Passing", iti_year_of_passing);
                                    } else {
                                        message = "Invalid  Diploma Year Of Passing";
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }
                                }
                            }

                            if (applicantInfo.equalsIgnoreCase("dip_division")) {
                                dipData.setDivision(mapValue);
                                dipMap.put("Division", mapValue);

                            }
                            if (applicantInfo.equalsIgnoreCase("dip_dc")) {
                                if (saveFlag) {
                                    dipData.setDegCert(mapValue);
                                    dipMap.put("Certificate-Filename", mapValue);
                                } else {
                                    if (!mapValue.equalsIgnoreCase("NA")) {
                                        dipData.setDegCert(mapValue);
                                        dipMap.put("Certificate-Filename", mapValue);
                                    } else {
                                        message = "Invalid dip Marks Sheet";
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }
                                }

                            }
                            if (applicantInfo.equalsIgnoreCase("dip_marksheet")) {

                                boolean validValFlag = true;
                                validValFlag = isFileAvaiable(mapValue, filePath);
                                if (!validValFlag) {
                                    message = "Please diploma Marks Sheet in jpg/pdf format";
                                    validValFlag = false;
                                } else {
                                    validValFlag = isCertValidPattern(mapValue);
                                    if (!validValFlag) {
                                        message = "Special characters in filename not allowed in uploading diploma Marks Sheet";
                                        validValFlag = false;
                                    } else {
                                        fileExtn = getFileExtension(mapValue); // HRD ChangeId:2023080801
                                        if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                || fileExtn.equalsIgnoreCase("pdf")) {
                                            validValFlag = true;
                                        } else {
                                            message = "Please enter diploma Marks Sheet in jpg/pdf format";
                                            validValFlag = false;
                                        }
                                    }
                                }

                                if (validValFlag) {
                                    dipData.setMarksheet(mapValue);
                                    dipMap.put("Marks Sheet-Filename", mapValue);
                                } else {
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }

                            }
                            if (applicantInfo.equalsIgnoreCase("dip_percentage_cgpa")) {
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    dipData.setPercentage_cgpa(mapValue);
                                    dipMap.put("Percentage/CGPA", mapValue);
                                }
                                if (mapValue.equalsIgnoreCase("CGPA")) {
                                    dipData.setCgpa_obt(Float.parseFloat(map1.get("dip_cgpa_obt")));
                                    dipData.setCgpa_max(map1.get("dip_cgpa_max"));
                                    dipData.setPercentage(Float.parseFloat(map1.get("dip_percentage")));
                                    if (saveFlag) {
                                        usrNamePtrn = Pattern.compile("^-?\\d*\\.{0,1}\\d+$");
                                    } else {
                                        usrNamePtrn = Pattern.compile("[0-9]+(\\.[0-9]{0,2})?%?");
                                    }
                                    if (validatePatern(xData.getX_cgpa_obt() + "", usrNamePtrn)) {
                                        dipMap.put("CGPA Obtained", itiData.getCgpa_obt());
                                        dipMap.put("Maximum CGPA", itiData.getCgpa_max());
                                        // itiMap.put("Percentage", itiData.getPercentage());

                                    } else {
                                        message = "Invalid  CGPA  Diploma ";
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }

                                } else if (mapValue.equalsIgnoreCase("percentage")) {
                                    dipData.setPercentage(Float.parseFloat(map1.get("dip_percentage")));
                                    // itiMap.put("Percentage", itiData.getPercentage());
                                }

                                String perc = dipData.getPercentage() + "";
                                if (perc.equals("-1.0")) {
                                    perc = "-";
                                }

                                dipMap.put("Percentage", perc);
                            }
                        }

                        /**
                         * *************************
                         */

                        /*
                         * eligibility_id | eligibility_value ----------------+-------------------- 1 |
                         * X 2 | XII 3 | Graduate 4 | Postgraduate 5 | PhD 6 | PostDoctoral 7 | ITI 8 |
                         * DIPLOMA 9 | Intergrated Course
                         */
                        if (applicantInfo.equalsIgnoreCase("qualification")) {
                            qualArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("elg")) {
                            typeArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("university")) {
                            univArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("college")) {
                            // mapValue = java.net.URLDecoder.decode(mapValue, "UTF-8");
                            collegeArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("discipline")) {
                            discpArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("year_of_passing")) {

                            yearPassArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("division")) {
                            logMgr.accessLog("qual is.......: " + mapValue);
                            diviArr = gethigherEdArray(mapValue);
                            logMgr.accessLog("qualArray is : " + diviArr.toString());
                        }

                        if (applicantInfo.equalsIgnoreCase("percentage_cgpa")) {
                            perCGArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("c_univ")) {
                            cgpaUnivFlagArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("cgpa_obt")) {
                            logMgr.accessLog("qual is.......: " + mapValue);
                            cgpaObtArr = gethigherEdArray(mapValue);
                            // System.out.println(CurrentDateTime.dateTime() + ":" + "qualArray is : " +
                            // cgpaObtArr.toString());
                        }

                        if (applicantInfo.equalsIgnoreCase("cgpa_max")) {
                            cgpaMaxArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("percentage")) {
                            percentArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("specialization")) {
                            specArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("marksheet")) {
                            markSheetArr = gethigherEdArray(mapValue);
                        }
                        if (applicantInfo.equalsIgnoreCase("dc")) {
                            degCertArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("topic")) {
                            topicArr = gethigherEdArray(mapValue);
                        }

                        if (applicantInfo.equalsIgnoreCase("abs")) { // abstract
                            absArr = gethigherEdArray(mapValue);
                        }
                        System.out.println("INSERT FLAGbefore x ...............:" + insertFlag);

                        System.out.println("INSERT FLAG ..after diploma.............:" + insertFlag);

                        System.out.println(" med.getCouncilRegNo()..... before:" + mcNursedata.getCouncilRegNo() + "," + advt_no + "," + post_no);
                        //MEDICAL STREAM INPUTS

                        {
                            if (applicantInfo.equalsIgnoreCase("nurse_reg_no")) {
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    usrNamePtrn = Pattern.compile("([a-z A-Z 0-9\\-\\/]{1,50})");
                                    if (validatePatern(mapValue, usrNamePtrn)) {
                                        mcNursedata.setCouncilRegNo(mapValue);
                                        medNurseMap.put("Registration Council Number", mapValue);
                                        medNurseFlag = true;
                                    } else {
                                        message = "Invalid registration number for post:" + post_no;
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }

                                }
                            }

                            if (applicantInfo.equalsIgnoreCase("nurse_council")) {
                                //   System.out.println("IapplicantInfo ........nurse_council.......:" + applicantInfo.toString() + "," + mapValue);
                                usrNamePtrn = Pattern.compile("([a-z A-Z 0-9\\-\\/]{1,50})");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    mcNursedata.setNameCouncil(mapValue);
                                    medNurseMap.put("Council Name", mapValue);
                                } else {
                                    message = "Invalid council name for  post:" + post_no;
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                            if (applicantInfo.equalsIgnoreCase("nurse_reg_date")) {
                                //  System.out.println("IapplicantInfo ........nurse_reg_date.......:" + applicantInfo.toString() + "," + mapValue);
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    mcNursedata.setMedRegDate(Date.valueOf(mapValue));
                                    medNurseMap.put("Registration Date", mapValue);
                                }
                            }

                            if (applicantInfo.equalsIgnoreCase("nurse_valid_date")) {
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    mcNursedata.setValidityDate(Date.valueOf(mapValue));
                                    medNurseMap.put("Validity Date", mapValue);
                                }
                            }

                        }

                        {
                            //   System.out.println(" med.getCouncilRegNo()118888812222:" + mcNursedata.getCouncilRegNo() + "," + advt_no + "," + post_no + "," + medDoctFlag + "," + previewFlag + "," + insertFlag);
                            if (applicantInfo.equalsIgnoreCase("doc_reg_no")) {
                                //   System.out.println("IapplicantInfo ..........inside .....:" + applicantInfo.toString() + "," + mapValue);
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    usrNamePtrn = Pattern.compile("([a-z A-Z 0-9\\-\\/]{1,50})");
                                    if (validatePatern(mapValue, usrNamePtrn)) {
                                        // System.out.println("IapplicantInfo ........validate.......:" + applicantInfo.toString() + "," + mapValue);
                                        mcDoctdata.setCouncilRegNo(mapValue);
                                        medDoctMap.put("Registration Council Number", mapValue);
                                        medDoctFlag = true;
                                    } else {
                                        message = "Invalid registration number for post:" + post_no;
                                        buttonType = "error";
                                        getErrMsg(message, buttonType, response, con);
                                        return;
                                    }
                                }

                            }

                            if (applicantInfo.equalsIgnoreCase("doc_council")) {
                                System.out.println("IapplicantInfo ........nurse_council.......:" + applicantInfo.toString() + "," + mapValue);

                                usrNamePtrn = Pattern.compile("([a-z A-Z 0-9\\-\\/]{1,50})");
                                if (validatePatern(mapValue, usrNamePtrn)) {
                                    mcDoctdata.setNameCouncil(mapValue);
                                    medDoctMap.put("Council Name", mapValue);
                                } else {
                                    message = "Invalid council name for  post:" + post_no;
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }

                            if (applicantInfo.equalsIgnoreCase("doc_reg_date")) {
                                // System.out.println("IapplicantInfo ........nurse_reg_date.......:" + applicantInfo.toString() + "," + mapValue);
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    mcDoctdata.setMedRegDate(Date.valueOf(mapValue));
                                    medDoctMap.put("Registration Date", mapValue);
                                }

                            }

                            if (applicantInfo.equalsIgnoreCase("doc_valid_date")) {
                                if (!mapValue.equalsIgnoreCase("NA")) {
                                    mcDoctdata.setValidityDate(Date.valueOf(mapValue));
                                    medDoctMap.put("Validity Date", mapValue);
                                }
                            }

                        }
                        System.out.println(" med.medDoctMap():" + medDoctMap.toString() + "," + advt_no + "," + post_no);

                        //REGISTRATION ID CREATION
                        if (!previewFlag) {
                            logMgr.accessLog("ENTERE..........");

                            if (!saveFlag) {

                                regRel.setEmail(eMail);;
                                regRel.setAdvt_no(advt_no);
                                regRel.setPost_no(post_no);
                                //   registration_id = new RegistrationRelationOperation(emailLogId).generateRegistration_id();
                                registration_id = new RegistrationRelationOperation(emailLogId).getUniqueRegistration(regRel, con);
                                System.out.println("registration_id:" + registration_id);
                                if (registration_id.isEmpty() || registration_id.equals("-") || registration_id.equals(".") || registration_id.equals("-1")) {
                                    message = "Problem in Submitting the application. Contact System Administrator";
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                                regRel.setRegistration_id(registration_id);
                                System.out.println(
                                        CurrentDateTime.dateTime() + ":" + "reg relation id1555......." + registration_id);

                                regrelResult = new RegistrationRelationOperation(emailLogId).establishRelationship(regRel, con);
                                logMgr.accessLog("reg relation id3......." + regRel.getRegistration_id());

                                if (regrelResult > 0) {
                                    insertFlag = true;
                                } else {
                                    insertFlag = false;
                                    buttonType = "error";
                                    System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                    message = "Error in submitting your application, please contact Administrator.";
                                    if (saveFlag) {
                                        message = "The draft could not be saved 1.";
                                    }
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }

                                logMgr.accessLog("reg relation done.......");
                            } else {
                                String emailId = regRel.getEmail();
                                registration_id = "NA_" + emailId;
                                String retMsg = new AdvertisementOperation(emailLogId)
                                        .flushPrevSaveApplication(registration_id);

                                if (retMsg.equalsIgnoreCase("error")) {
                                    message = "Could not flush the data.Error in submitting your application, please contact Administrator.";
                                    JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
                                    jMetaArray.add("error"); // buttonType
                                    jMetaArray.add(message);

                                    JsonObjectBuilder json = Json.createObjectBuilder();
                                    json.add("Results", jMetaArray);
                                    javax.json.JsonObject jsonObj = json.build();
                                    StringWriter writer = new StringWriter();

                                    PrintWriter pw = response.getWriter();
                                    Json.createWriter(writer).write(jsonObj);
                                    String resultStr2 = writer.toString();
                                    logMgr.accessLog("jsonResponse : " + resultStr2);
                                    response.setContentType("application/Json;charset=utf-8\"");
                                    pw.write(resultStr2);
                                    pw.close();
                                }

                            }
                        }

                        if (insertFlag) {
                            if (applicantInfo.equalsIgnoreCase("experience")) {
                                // if (mapValue.equalsIgnoreCase("NA"))
                                expData.setExperience(mapValue);
                                expData.setRegistration_id(registration_id);
                                expData.setAdvt_no(advt_no);
                                expData.setPost_no(post_no);
                                expData.setEmail(eMail);

                                if (mapValue.equalsIgnoreCase("yes")) {

                                    String qualValue = map1.get("exp_years");
                                    expData.setExp_years(Integer.parseInt(qualValue));
                                    // mainExpMap.put("Years", qualValue);

                                    qualValue = map1.get("exp_months");
                                    expData.setExp_months(Integer.parseInt((qualValue)));
                                    // mainExpMap.put("Months", qualValue);

                                    expName = gethigherEdArray(map1.get("emp_name"));
                                    expAddress = gethigherEdArray(map1.get("emp_address"));
                                    expDesign = gethigherEdArray(map1.get("emp_desig"));
                                    expWork = gethigherEdArray(map1.get("emp_work"));
                                    expPayDrawn = gethigherEdArray(map1.get("emp_paydrawn")); // PPEG-HRD: PayDrawn
                                    expReason = gethigherEdArray(map1.get("emp_reason")); // PPEG-HRD: ReasonForLeaving
                                    expFrom = gethigherEdArray(map1.get("time_from"));
                                    expTo = gethigherEdArray(map1.get("time_to"));
                                    expGovt = gethigherEdArray(map1.get("govtExp"));
                                    expCert = gethigherEdArray(map1.get("exp_cer"));

                                    // setDesignation //govtExp
                                    int exp_in_days = 0; // ChangeId:2023083003
                                    totYears = 0;
                                    totMonths = 0;
                                    totDays = 0;
                                    for (int ex = 0; ex < expName.size(); ++ex) {
                                        expData.setSlno(ex + 1);
                                        expMap = new LinkedHashMap();
                                        String exFrom = (String) expFrom.get(ex);
                                        String exTo = (String) expTo.get(ex);
                                        expData.setEmp_name((String) expName.get(ex));
                                        expMap.put("Employer Name", decodeStr(expData.getEmp_name()));
                                        expData.setEmp_address((String) expAddress.get(ex));
                                        expMap.put("Employer Address", decodeStr(expData.getEmp_address()));
                                        expData.setDesignation((String) expDesign.get(ex));
                                        expMap.put("Designation", decodeStr(expData.getDesignation()));
                                        expData.setEmp_work(((String) expWork.get(ex)));
                                        expMap.put("Nature of Work", decodeStr(expData.getEmp_work()));

                                        // Start PPEG-HRD: PayDrawn
                                        expData.setEmp_paydrawn(Integer.parseInt((String) expPayDrawn.get(ex)));
                                        expMap.put("Pay Drawn", expData.getEmp_paydrawn());
                                        // End PPEG-HRD: PayDrawn

                                        // Start PPEG-HRD: ReasonForLeaving
                                        expData.setEmp_reason(((String) expReason.get(ex)));
                                        expMap.put("Reason for Leaving", decodeStr(expData.getEmp_reason()));
                                        // End PPEG-HRD: ReasonForLeaving

                                        expData.setTime_from(Date.valueOf(exFrom));
                                        expMap.put(" From", getStrDate(exFrom));
                                        expData.setTime_to(Date.valueOf(exTo));
                                        expMap.put(" To", getStrDate(exTo));

                                        getDiffTime(exFrom, exTo);
                                        expData.setExp_years(years);
                                        totYears += years;
                                        expData.setExp_months(months);
                                        totMonths += months;
                                        expData.setExp_days(days);
                                        totDays += days;
                                        exp_in_days += ( days + 1 ) + ( months * 30 ) + ( years * 365 ); // ChangeId:2023083003
                                        expData.setExpGovt((String) expGovt.get(ex));
                                        expMap.put("Govt Experience ", expData.getExpGovt());

                                        String expCertificate = (String) expCert.get(ex);
                                        expData.setExpCert(expCertificate);

                                        {
                                            boolean validValFlag = true;
                                            validValFlag = isFileAvaiable(expCertificate, filePath);
                                            if (!validValFlag) {
                                                message = "Please experience certificate in jpg/pdf format";
                                                validValFlag = false;
                                            } else {
                                                validValFlag = isCertValidPattern(expCertificate);
                                                if (!validValFlag) {
                                                    message = "Special characters in filename not allowed in uploading experience certificate ";
                                                    validValFlag = false;
                                                } else {
                                                    fileExtn = getFileExtension(expCertificate); // HRD ChangeId:2023080801
                                                    if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                            || fileExtn.equalsIgnoreCase("pdf")) {
                                                        validValFlag = true;
                                                    } else {
                                                        message = "Please upload experience certificate in jpg/pdf format";
                                                        validValFlag = false;
                                                    }
                                                }
                                            }

                                            if (validValFlag) {
                                                expMap.put("Experience Certificate", expData.getExpCert());
                                            } else {
                                                buttonType = "error";
                                                getErrMsg(message, buttonType, response, con);
                                                return;
                                            }

                                        }

                                        expArr.add(expMap);

                                        if (!previewFlag) {
                                            expResult = new ExperienceOperation(emailLogId).expData(expData, con);
                                            if (expResult > 0) {
                                                insertFlag = true;
                                            } else {
                                                insertFlag = false;
                                                con.rollback();
                                                buttonType = "error";
                                                System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                                message = "Error in submitting your application, please contact Administrator.";
                                                if (saveFlag) {
                                                    message = "The draft could not be saved 7.";
                                                }
                                                getErrMsg(message, buttonType, response, con);
                                                //break;
                                                return;
                                                //break;
                                            }
                                        }

                                    } //
                                    if (post_name.startsWith("Medical Officer")) {
                                        //if (totYears < 2) { // ChangeId:2023083003
                                        if (exp_in_days < 730 ) { // ChangeId:2023083003
                                            insertFlag = false;
                                            con.rollback();
                                            buttonType = "error";
                                            System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                            message = "Error in submitting your application, please contact Administrator.";
                                            if (saveFlag) {
                                                message = "The draft could not be saved 7.";
                                            }
                                            getErrMsg(message, buttonType, response, con);
                                            return;
                                        }
                                    }
                                    System.out.println(
                                            CurrentDateTime.dateTime() + ":" + "EXP DATA ARR is :" + expArr.toString());
                                    mainExpMap.put("expData", expArr);

                                }
                            }

                        }//experience
                    }

                    if (insertFlag) {
                        totExp = getTotalExp(totYears, totMonths, totDays);
                        personalData.setTotalExp(totExp);
                        // Higher Education
                        IntCourse_Data intg = new IntCourse_Data();
                        ArrayList qualTypeSeq = new ArrayList(); //
                        qualTypeSeq = getSequenceQualType(typeArr);

                        logMgr.accessLog("qualArr: " + qualArr.toString());

                        for (int seq = 0; seq < qualTypeSeq.size(); ++seq) {
                            intg = new IntCourse_Data();
                            int i = (int) qualTypeSeq.get(seq);
                            hEdMap = new LinkedHashMap();
                            // String qualValue = (String) qualArr.get(i); //
                            // java.net.URLDecoder.decode((String) qualArr.get(i),// "UTF-8");
                            logMgr.accessLog("QUAL VALUE IS :" + typeArr.toString());
                            String qualType = (String) typeArr.get(i);// qualValue.split("\\_")[0];
                            String qualSplit = (String) qualArr.get(i);// qualValue.split("\\_")[1];
                            // if (qualSplit.startsWith("Integrated M"))
                            // qualType = "Postgraduate";
                            hEdMap.put("Type", qualType);
                            hEdMap.put("Qualification", qualSplit);
                            intg.setsNo(i + 1);
                            intg.setUniversity(decodeStr((String) univArr.get(i)));
                            hEdMap.put("University", intg.getUniversity());
                            intg.setCollege(decodeStr((String) collegeArr.get(i)));
                            hEdMap.put("College", intg.getCollege());
                            int yr_pass = Integer.parseInt((String) yearPassArr.get(i));
                            hEdMap.put("Year of Passing", yr_pass);
                            intg.setYear_of_passing(yr_pass);
                            intg.setQualification((String) qualSplit);
                            intg.setDiscipline(decodeStr((String) discpArr.get(i)));
                            hEdMap.put("Department", intg.getDiscipline());
                            intg.setDivision((String) diviArr.get(i));
                            hEdMap.put("Division", intg.getDivision()); //
                            intg.setSpecialization(decodeStr((String) specArr.get(i)));
                            hEdMap.put("Specialization", intg.getSpecialization());
                            String hgMarkSheet = (String) markSheetArr.get(i);

                            {
                                boolean validValFlag = true;
                                validValFlag = isFileAvaiable(hgMarkSheet, filePath);
                                if (!validValFlag) {
                                    message = "Please upload Marks Sheet certificate in jpg/pdf format";
                                    validValFlag = false;
                                } else {
                                    validValFlag = isCertValidPattern(hgMarkSheet);
                                    if (!validValFlag) {
                                        message = "Special characters in filename not allowed in uploading Marks Sheet certificate ";
                                        validValFlag = false;
                                    } else {
                                        fileExtn = getFileExtension(hgMarkSheet); // HRD ChangeId:2023080801
                                        if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                || fileExtn.equalsIgnoreCase("pdf")) {
                                            validValFlag = true;
                                        } else {
                                            message = "Please upload Marks Sheet in jpg/pdf format";
                                            validValFlag = false;
                                        }
                                    }
                                }

                                if (validValFlag) {
                                    intg.setMarksheet(hgMarkSheet);
                                    hEdMap.put("Marks Sheet-Filename", intg.getMarksheet());
                                } else {
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }

                            }

                            String hgDegcert = (String) degCertArr.get(i);
                            {
                                boolean validValFlag = true;
                                validValFlag = isFileAvaiable(hgDegcert, filePath);
                                if (!validValFlag) {
                                    message = "Please upload Degree certificate in jpg/pdf format";
                                    validValFlag = false;
                                } else {
                                    validValFlag = isCertValidPattern(hgDegcert);
                                    if (!validValFlag) {
                                        message = "Special characters in filename not allowed in uploading Degree certificate ";
                                        validValFlag = false;
                                    } else {
                                        fileExtn = getFileExtension(hgDegcert); // HRD ChangeId:2023080801
                                        if (fileExtn.equalsIgnoreCase("jpg") || fileExtn.equalsIgnoreCase("jpeg")
                                                || fileExtn.equalsIgnoreCase("pdf")) {
                                            validValFlag = true;
                                        } else {
                                            message = "Please upload Degree in jpg/pdf format";
                                            validValFlag = false;
                                        }
                                    }
                                }

                                if (validValFlag) {
                                    intg.setMarksheet(hgMarkSheet);
                                    intg.setDegCert(hgDegcert);
                                    hEdMap.put("Certificate-Filename", intg.getDegCert());
                                } else {
                                    buttonType = "error";
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }

                            }

                            intg.setQualType(qualType);
                            intg.setPhdTopic((String) topicArr.get(i));
                            intg.setCgpa_univ_flag((String) cgpaUnivFlagArr.get(i));
                            if (!intg.getPhdTopic().equals("-")) {
                                hEdMap.put("Thesis", intg.getPhdTopic());
                            }
                            String absStr = (String) absArr.get(i);
                            intg.setPhdAbstract(absStr);
                            logMgr.accessLog("absStr:" + absStr);
                            if (!intg.getPhdAbstract().equals("-") && !intg.getPhdAbstract().equals("-na-")
                                    && !intg.getPhdAbstract().equals("NA")) {
                                hEdMap.put("Abstract/Thesis Filename", intg.getPhdAbstract());
                            }
                            
                            int validMark = 0;
                            if ( !((String) percentArr.get(i)).equals("NA") && !((String) percentArr.get(i)).equals("-1") ){
                                // Start: ChangeId 2023121902, 2023122702
                                float min = 10.0F;
                                // ChangeId: 2024012005 bypass percentage/cgpa validation for non scientist post
                                if( post_name.contains("Scientist") && (minMarks.get(qualSplit)).get("MinPercentage") != null && !(minMarks.get(qualSplit)).get("MinPercentage").equals("") ){
                                    min = Float.parseFloat((String)((minMarks.get(qualSplit)).get("MinPercentage")));
                                }
                                if( Float.parseFloat((String) percentArr.get(i)) 
                                        >= min )
                                {
                                    intg.setPercentage(Float.parseFloat((String) percentArr.get(i)));
                                    hEdMap.put("Percentage", intg.getPercentage());
                                    validMark++;
                                }
                                // End: ChangeId 2023121902, 2023122702
                            }
                            else
                            {
                                intg.setPercentage(-1);
                                hEdMap.put("Percentage", "-");
                            }
                            //if (percentType.equalsIgnoreCase("CGPA")) { // ChangeId: 2023122101
                            if ( !((String) cgpaObtArr.get(i)).equals("NA") 
                                    && !((String) cgpaObtArr.get(i)).equals("-1")
                                    && !((String) cgpaObtArr.get(i)).equals("-")){
                                // Start: ChangeId 2023121902, 2023122702
                                float min = 1.0F;
                                // ChangeId: 2024012005 bypass percentage/cgpa validation for non scientist post
                                if( post_name.contains("Scientist") && (minMarks.get(qualSplit)).get("MinCGPA") != null && !(minMarks.get(qualSplit)).get("MinCGPA").equals("") ){
                                    min = Float.parseFloat((String)((minMarks.get(qualSplit)).get("MinCGPA")));
                                }
                                if( Float.parseFloat((String) cgpaObtArr.get(i)) 
                                        >= min )
                                {
                                    intg.setCgpa_obt(Float.parseFloat((String) cgpaObtArr.get(i)));
                                    intg.setCgpa_max((String) cgpaMaxArr.get(i));
                                    //intg.setPercentage(Float.parseFloat((String) percentArr.get(i)));

                                    hEdMap.put("CGPA Obtained", intg.getCgpa_obt());
                                    hEdMap.put("Maximum CGPA", intg.getCgpa_max());
                                    //hEdMap.put("University approved conversion Factor Available", intg.getCgpa_univ_flag());
                                    // hEdMap.put("Percentage", intg.getPercentage());
                                    validMark++;
                                }
                                // End: ChangeId 2023121902, 2023122702
                            } 
                            else{
                                intg.setCgpa_obt(-1);
                                intg.setCgpa_max("");
                                //intg.setPercentage(Float.parseFloat((String) percentArr.get(i)));

                                hEdMap.put("CGPA Obtained", "-");
                                hEdMap.put("Maximum CGPA", "-");
                            }
                                
                            
                            String percentType = (String) perCGArr.get(i);
                            if (!percentType.equalsIgnoreCase("NA") 
                                    && !percentType.equalsIgnoreCase("-1")
                                    && !percentType.equalsIgnoreCase("-") ) { // ChangeId: 2023122101
                                // Start: ChangeId 2023121902, 2023122702
                                float min = 10.0F;
                                // ChangeId: 2024012005 bypass percentage/cgpa validation for non scientist post
                                if( post_name.contains("Scientist") && (minMarks.get(qualSplit)).get("MinPercentage") != null && !(minMarks.get(qualSplit)).get("MinPercentage").equals("") ){
                                    min = Float.parseFloat((String)((minMarks.get(qualSplit)).get("MinPercentage")));
                                }
                                if( Float.parseFloat((String) perCGArr.get(i)) 
                                        >= min )
                                {
                                    intg.setPercentage_cgpa(percentType);
                                    hEdMap.put("CGPA to Percentage", intg.getPercentage_cgpa()); // ChangeId: 2023122101 CGPA/Percentage to 'CGPA to Percentage'
                                    validMark++;
                                }
                                // End: ChangeId 2023121902, 2023122702
                            }
                            else{
                                intg.setPercentage_cgpa("-1");
                                hEdMap.put("CGPA to Percentage", "-");
                            }
                            
                            // Start: ChangeId 2023121902
                            if(validMark == 0){
                                getErrMsg("No valid marks for "+qualSplit, "error", response, con);
                                System.out.println("ERROR: Marks validation not satisfied for "+qualSplit);
                                return;
                            }
                            else{
                                System.out.println("INFO: Marks validation satisfied for "+qualSplit);
                            }
                            // End: ChangeId 2023121902
                            // else if (percentType.equalsIgnoreCase("percentage")) { // ChangeId: 2023122101
                            
                            //String perc = intg.getPercentage() + "";
                            //if (perc.equals("-1.0") || perc.equals("0.0") || perc.equals("-1")) {
                            //    perc = "NA";
                            //}

                            //hEdMap.put("Percentage", perc);

                            hEdArr.add(hEdMap);
                            if (!previewFlag) {
                                intg.setRegistration_id(registration_id);
                                intg.setAdvt_no(advt_no);
                                intg.setPost_no(post_no);
                                intg.setEmail(eMail);
                                int intgResult = new IntCourseOperation(emailLogId).IntgData(intg, con);
                                if (intgResult <= 0) {
                                    insertFlag = false;
                                    con.rollback();
                                    buttonType = "error";
                                    System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                    message = "Error in submitting your application, please contact Administrator.";
                                    if (saveFlag) {
                                        message = "The draft could not be saved 8.";
                                    }
                                    getErrMsg(message, buttonType, response, con);
                                    //break;
                                    return;
                                } else {
                                    insertFlag = true;
                                }

                            }

                        }
                    }

                    if (netMap.size() > 0) {
                        if (!previewFlag) {
                            if (insertFlag) {
                                netResult = new NETOperation(emailLogId).insNETData(netData, con);
                                if (netResult > 0) {
                                    insertFlag = true;
                                } else {
                                    insertFlag = false;
                                    con.rollback();
                                    buttonType = "error";
                                    System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                    message = "Error in submitting your application, please contact Administrator.";
                                    if (saveFlag) {
                                        message = "The draft could not be saved 2.";
                                    }
                                    getErrMsg(message, buttonType, response, con);
                                    return;
                                }
                            }//
                        }
                    }

                    if (insertFlag) {
                        if (elgArray.contains("X")) {
                            xData.setRegistration_id(registration_id);
                            xData.setAdvt_no(advt_no);
                            xData.setPost_no(post_no);
                            xData.setEmail(eMail);
                            //      System.out.println("INSERT FLAGbefore x ...............:" + insertFlag + "," + previewFlag);

                            if (!previewFlag) {
                                xResult = new XOperation(emailLogId).xData(xData, con);
                                if (xResult > 0) {
                                    insertFlag = true;
                                } else {
                                    insertFlag = false;
                                    con.rollback();
                                    buttonType = "error";
                                    System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                    message = "Error in submitting your application, please contact Administrator.";
                                    if (saveFlag) {
                                        message = "The draft could not be saved 3.";
                                    }
                                    getErrMsg(message, buttonType, response, con);
                                    //break;
                                    return;
                                }
                            }
                        }
                    }

                    System.out.println("xResult is :" + xResult);
                    if (insertFlag) {
                        if (XIIFlag) {
                            if (elgArray.contains("XII")) {
                                xiiData.setRegistration_id(registration_id);
                                xiiData.setAdvt_no(advt_no);
                                xiiData.setPost_no(post_no);
                                xiiData.setEmail(eMail);
                                if (!previewFlag) {
                                    xiiResult = new XiiOperation(emailLogId).xiiData(xiiData, con);
                                    if (xiiResult > 0) {
                                        insertFlag = true;
                                    } else {
                                        insertFlag = false;
                                        con.rollback();
                                        buttonType = "error";
                                        System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                        message = "Error in submitting your application, please contact Administrator.";
                                        if (saveFlag) {
                                            message = "The draft could not be saved 4.";
                                        }
                                        getErrMsg(message, buttonType, response, con);
                                        //break;
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("INSERT FLAG ..after xii.............:" + insertFlag);
                    if (insertFlag) {
                        if (ITIFlag) {
                            if (elgArray.contains("ITI")) {
                                itiData.setRegistration_id(registration_id);
                                itiData.setAdvt_no(advt_no);
                                itiData.setPost_no(post_no);
                                itiData.setEmail(eMail);
                                if (!previewFlag && !saveFlag) {
                                    int itiResult = new Diploma_ITI_Operation(emailLogId).IntgData(itiData, con);
                                    if (itiResult > 0) {
                                        insertFlag = true;
                                    } else {
                                        insertFlag = false;
                                        con.rollback();
                                        buttonType = "error";
                                        System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                        message = "Error in submitting your application, please contact Administrator.";
                                        if (saveFlag) {
                                            message = "The draft could not be saved 5.";
                                        }
                                        getErrMsg(message, buttonType, response, con);
                                        //break;
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    System.out.println(" DIPLOMA:" + advt_no + "," + post_no);

                    if (insertFlag) {
                        if (DIPFlag) {
                            if (elgArray.contains("DIPLOMA")) {
                                dipData.setRegistration_id(registration_id);
                                dipData.setAdvt_no(advt_no);
                                dipData.setPost_no(post_no);
                                dipData.setEmail(eMail);
                                if (!previewFlag && !saveFlag) {
                                    int itiResult = new Diploma_ITI_Operation(emailLogId).IntgData(dipData, con);
                                    if (itiResult <= 0) {
                                        insertFlag = false;
                                        con.rollback();
                                        buttonType = "error";
                                        System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                        message = "Error in submitting your application, please contact Administrator.";
                                        if (saveFlag) {
                                            message = "The draft could not be saved 6.";
                                        }
                                        getErrMsg(message, buttonType, response, con);
                                        //break;
                                        return;
                                    } else {
                                        insertFlag = true;
                                    }
                                }
                            }
                        }
                    }

                    // System.out.println(" med.getCouncilRegNo()11888881:" + mcNursedata.getCouncilRegNo() + "," + advt_no + "," + post_no + "," + medNurseFlag + "," + previewFlag + "," + insertFlag);
                    System.out.println("HRD-DEBUG: Before Nurse");
                    if (post_name.startsWith("Nurse")) {
                        System.out.println("HRD-DEBUG: Startswith Nurse");
                        if (!previewFlag && !saveFlag) {
                            if (insertFlag) {
                                mcNursedata.setRegistration_id(registration_id);
                                mcNursedata.setAdvt_no(advt_no);
                                mcNursedata.setPost_no(post_no);
                                mcNursedata.setEmail(emailLogId);
                                //  System.out.println(" med.getCouncilRegNo()112221:" + mcNursedata.getCouncilRegNo() + "," + advt_no + "," + post_no);
                                medNurseResult = new MedicalCouncilOperation(emailLogId).medData(mcNursedata, con);
                                //   System.out.println(" med.getCouncilRegNo()1sfter1:" + mcNursedata.getCouncilRegNo() + "," + advt_no + "," + post_no);
                                if (medNurseResult > 0) {
                                    insertFlag = true; System.out.println("HRD-DEBUG: Nurse Insertflag is TRUE");
                                } else {
                                    insertFlag = false;
                                    con.rollback();
                                    buttonType = "error";
                                    System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                    message = "Error in submitting your application, please contact Administrator.";
//                                    if (saveFlag) {
//                                        message = "The draft could not be saved 9.";
//                                    }
                                    getErrMsg(message, buttonType, response, con);
                                    //break;
                                    return;
                                }
                            }

                        }
                    }

                    System.out.println(CurrentDateTime.dateTime() + "insertflag.45.......>>>>>>>>>:" + insertFlag + "insertFlag 13456.");
                    if (post_name.startsWith("Medical Officer")) {
                        if (!previewFlag && !saveFlag) {
                            if (insertFlag) {
                                mcDoctdata.setRegistration_id(registration_id);
                                mcDoctdata.setAdvt_no(advt_no);
                                mcDoctdata.setPost_no(post_no);
                                mcDoctdata.setEmail(emailLogId);
                                System.out.println(" med.getCouncilRegNo()beifere :" + mcDoctdata.getCouncilRegNo() + "," + advt_no + "," + post_no + "," + medDoctResult);
                                medDoctResult = new MedicalCouncilOperation(emailLogId).medData(mcDoctdata, con);
                                //       System.out.println(" med.getCouncilRegNo() after 333333:" + mcDoctdata.getCouncilRegNo() + "," + advt_no + "," + post_no + "," + medDoctResult);
                                if (medDoctResult > 0) {
                                    insertFlag = true;
                                } else {
                                    insertFlag = false;
                                    con.rollback();
                                    buttonType = "error";
                                    System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                    message = "Error in submitting your application, please contact Administrator.";
//                                    if (saveFlag) {
//                                        message = "The draft could not be saved 10.";
//                                    }
                                    getErrMsg(message, buttonType, response, con);
                                    //break;
                                    return;
                                }
                            }
                        }
                    }

                    // String message = new String();
                    logMgr.accessLog("PERS MAP IS :" + persMap.toString());
                    pdfMap.put("Personal", persMap);
                    pdfMap.put("Bank Account",bankMap); // ChangeId: 2023111001
                    pdfMap.put("Present", presAddrMap);
                    pdfMap.put("Permanent", permAddrMap);
                    pdfMap.put("X", xMap);
                    if (xiiMap.size() > 0) {
                        pdfMap.put("XII", xiiMap);
                    }
                    if (itiMap.size() > 0) {
                        pdfMap.put("ITI", itiMap);
                    }

                    if (dipMap.size() > 0) {
                        System.out.println("dipMap:" + dipMap.toString());
                        pdfMap.put("diploma", dipMap);
                    }
                    pdfMap.put("High", hEdArr);
                    if (expArr.size() > 0) {
                        pdfMap.put("Experience", mainExpMap);
                    }

                    if (netMap.size() > 0) {
                        pdfMap.put("NET", netMap);
                    }
                    // if (medMap.size() > 0)
                    if (post_name.startsWith("Nurse")) {
                        // System.out.println("medMap:" + medMap.toString());
                        pdfMap.put("Medical", medNurseMap);
                    }

                    if (post_name.startsWith("Medical Officer")) {
                        // System.out.println("medMap:" + medMap.toString());
                        pdfMap.put("Medical", medDoctMap);
                    }

                    pdfMap.put("Others", remarksMap);

                    // pdfMap.put("personal", personalData);
                    personalData.setRegistration_id(registration_id);
                    personalData.seteMail(eMail);
                    System.out.println(
                            CurrentDateTime.dateTime() + ":" + "FOR GENERATION PDF IS :" + personalData.getAdvt_no());

                    String zipName = adminDir + File.separator + advt_no + File.separator + post_no + File.separator;
                    // String filenm_path = uploadFileContextPath + "/" + filename;
                    logMgr.accessLog("FILE NM  therefore IS....123 :" + filePath + ", zipName is :" + zipName);

                    String realPath = request.getServletContext().getRealPath("");
                    String uploadFileContextPath = realPath.replace("eRecruitment_NRSC", "");
                    String uploadDir = filePath;
                    // Start: ChangeId: 2025042201
                    //String filename = personalData.getAdvt_no() + "_" + personalData.getPost_no() + "_" + eMail_dir + "_preview.pdf";
                    String filename = personalData.getAdvt_no() + "_" + personalData.getPost_no() + "_" + eMail_dir;
                    //+ "_preview.pdf";
                    if(previewFlag){
                        filename = filename + "_preview.pdf";
                    }
                    else{
                        filename = filename + ".pdf";
                    }
                    // End: ChangeId: 2025042201
                    String app_Path = uploadDir + File.separator;
                    String filenm_path = app_Path + filename;
                    logMgr.accessLog("FILE NM  ther:" + uploadFileContextPath + "..efore IS.... :" + filenm_path
                            + "...app path:" + app_Path);
                    if (!saveFlag) {
                        String logoSPath = app_Path + "signature_" + eMail_dir + "." + signatureExtn;
                        logMgr.accessLog("logoSPath:" + logoSPath);
                        if (new File(logoSPath).exists()) {
                            String logoPgPath = app_Path + "photograph_" + eMail_dir + "." + photoExtn;
                            logMgr.accessLog("logPgPath:" + logoPgPath);
                            if (new File(logoPgPath).exists()) {
                                FileOutputStream fos = new FileOutputStream(new File(filenm_path));
                                ByteArrayOutputStream baos = GeneratePdf.getPdfFile(pdfMap, personalData.getAdvt_no(),
                                        personalData.getPost_no(), realPath, eMail_dir, app_Path, registration_id, logoSPath,
                                        logoPgPath, previewFlag);
                                baos.writeTo(fos);
                                fos.close();
                                baos.close();
                                personalData.setPreview_file(uploadDir + filename);
                                // Start: ChangeId: 2023110402 Copy preview file to server path
                                /*
                                String destpath = "/home/nrsc/apache-tomcat-8.0.52/webapps/eRecruitment_NRSC/downloads/" + filename;
                                System.out.println("HRD-COPY-SOURCE: "+ filenm_path);
                                System.out.println("HRD-COPY-DEST: "+ destpath);
                                File source = new File(filenm_path);
                                File dest = new File(destpath);
                                FileUtils.copyFile(source, dest);
                                System.out.println("HRD-COPY-DONE: "+filename);*/
                                // File: ChangeId: 2023110402 Copy preview file to server path
                                
                            } else {
                                message = "Error: The Photograph jpg doesnot exists. Please upload the photograph as a jpg file again.";
                            }
                        } else {
                            message = "Error: The Signature jpg doesnot exists. Please upload the signature as a jpg file again.";
                        }

                    }

                    // WaterMark wm = new WaterMark();
                    // wm.doWaterMarking(filenm_path);
                    if (previewFlag) {
                        if (message.indexOf("Error") < 0) {
                            message = filename;
                        }
                    } else {
                        if (saveFlag) {
                            personalData.setStatus("SAVE");
                        } else {
                            //personalData.setStatus("POSTED"); // PPEG-HRD ChangeID:2023081001 PaymentChange
                            personalData.setStatus("PAYMENT"); // PPEG-HRD ChangeID:2023081001 PaymentChange
                            PostOperation po = new PostOperation(emailLogId);

                            String msgs = po.DownloadApplicantCert(advt_no, post_no, eMail_dir, registration_id, filePath,
                                    zipName);
                            if (msgs.equals("none")) {
                                message = "Your files are not saved properly";
                            }
                        }

                        System.out.println(CurrentDateTime.dateTime() + "insertflag.23.......>>>>>>>>>:" + insertFlag + "insertFlag 13456.");
                        personalResult = new PersonalOperation().personalData(personalData, con);
                        if (insertFlag) {
                            if (personalResult <= 0) {
                                insertFlag = false;
                                buttonType = "error";
                                System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                                message = "Error in submitting your application, please contact Administrator.";
                                if (saveFlag) {
                                    message = "The draft could not be saved 11.";
                                }
                                getErrMsg(message, buttonType, response, con);
                                return;
                            } else {
                                insertFlag = true;
                            }
                        }

                        System.out.println(CurrentDateTime.dateTime() + "insertflag........>>>>>>>>>:" + insertFlag + "insertFlag 13456.");
                        if (insertFlag) {

                            con.commit();
                            if (saveFlag) {
                                message = "The draft is successfully saved. Please continue filling the form.";
                            } else {
                                count_attempts = new AdvertisementOperation(emailLogId).applicantAttempts(advt_no, post_no, personalData.geteMail());

                                if (count_attempts >= 1) {
                                    System.out.println(CurrentDateTime.dateTime() + ":" + "Application successfully submitted.");

                                    File source = new File(filenm_path);
                                    File dest = new File(zipName + File.separator + registration_id + "_preview.pdf");
                                    FileUtils.copyFile(source, dest);

                                    if (buttonType.equals("submit") || buttonType.equals("submitApplicantHome")) { // ChangeId: 2023110203
                                        message = "Your application has been successfully submitted under Advt No: " + advt_no
                                                + " for Post No: " + post_no + ". Your Registration ID is: " + registration_id + ".";

                                        String signature = "\n\nRegards,\nRecruitment Section,\nNational Remote Sensing Centre (NRSC), ISRO";
                                        String note = "\n\n*This is a system generated email. Please do not reply to this mail.*";
                                        emailMsg = "Dear " + personalData.getSalutation() + " " + personalData.getFirst_name() + " "
                                                + personalData.getMiddle_name() + " " + personalData.getLast_name() + ","
                                                + "\n\nYour application is successfully submitted on " + GeneratePdf.getCurrentTime()
                                                + " for NRSC Advertisement-" + advt_no + ", Post No: " + post_no
                                                + ".\n\nYour Registration ID is " + registration_id + "." + signature + note;

                                        //Email
                                        EmailSender eSend = new EmailSender();
                                        eSend.setFileName(uploadDir + filename);
                                        String action = eSend.send(mailAddr, emailMsg);
                                        //String sms = message + " - Recruitment Section,NRSC.";
                                        //eSend.sendSMS(personalData.getContact_no(), sms); // ChangeId: 2023110203
                                    }
                                }
                            }//
                        } else {
                            con.rollback();
                            buttonType = "error";
                            System.out.println(CurrentDateTime.dateTime() + ":" + "Exception in submitting application.");
                            message = "Error in submitting your application, please contact Administrator.";
                            if (saveFlag) {
                                message = "The draft could not be saved 12.";
                            }
                            getErrMsg(message, buttonType, response, con);
                            return;
                        }
                    }

                    LocalDate localDate = LocalDate.now();
                    Date dateToday = Date.valueOf(localDate);

                    JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
                    jMetaArray.add(buttonType); // buttonType
                    jMetaArray.add(message);
                    jMetaArray.add(registration_id);
                    jMetaArray.add(uploadDir + filename);// if preview - pdf path, if submit - message
                    jMetaArray.add(getStrDate(dateToday.toString()));
                    jMetaArray.add(count_attempts);

                    con.close();

                    JsonObjectBuilder json = Json.createObjectBuilder();
                    json.add("Results", jMetaArray);
                    javax.json.JsonObject jsonObj = json.build();
                    StringWriter writer = new StringWriter();

                    PrintWriter pw = response.getWriter();
                    Json.createWriter(writer).write(jsonObj);
                    String resultStr2 = writer.toString();
                    logMgr.accessLog("jsonResponse : " + resultStr2);
                    response.setContentType("application/Json;charset=utf-8\"");
                    pw.write(resultStr2);
                    pw.close();
                    logMgr.accessLog("SAVE FLAG/PREVIEW FLAG:" + saveFlag + "," + previewFlag);
                    /*} catch (Exception e) {
                     con.rollback();
                     e.printStackTrace();
                     logMgr.accessLog("Error in process request:" + e.getMessage());
                     response.setContentType("text/html");
                     response.getWriter().print(e.getMessage());
                     }*/
                }
            } catch (Exception e) {
                con.rollback();
                con.close();
                e.printStackTrace();
                logMgr.accessLog("Error in process request:" + e.getMessage());
                JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
                jMetaArray.add("error");
                jMetaArray.add("Error : " + e.getMessage());

                JsonObjectBuilder json = Json.createObjectBuilder();
                json.add("Results", jMetaArray);
                javax.json.JsonObject jsonObj = json.build();
                StringWriter writer = new StringWriter();

                PrintWriter pw = response.getWriter();
                Json.createWriter(writer).write(jsonObj);
                String resultStr2 = writer.toString();
                logMgr.accessLog("jsonResponse : " + resultStr2);
                response.setContentType("application/Json;charset=utf-8\"");
                pw.write(resultStr2);
                pw.close();

                response.setContentType("text/html");
                response.getWriter().print(e.getMessage());
            }
        }

    }

    public boolean validatePatern(String userName, Pattern ptrn) {

        Matcher mtch = ptrn.matcher(userName);
        if (mtch.matches()) {
            return true;
        }
        return false;
    }

    public List gethigherEdArray(String mval) {
        String[] higherEd = mval.split("\\|");
        logMgr.accessLog("applicant_qual is : " + higherEd.toString());
        List qualArray = Arrays.asList(higherEd);
        logMgr.accessLog("qual Array is:" + qualArray.toString());
        return qualArray;
    }

    public void getErrMsg(String message, String buttonType, HttpServletResponse response, Connection con) {
        try {
            JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
            jMetaArray.add(buttonType); // buttonType
            jMetaArray.add(message); // if preview - pdf path, if submit - message
            JsonObjectBuilder json = Json.createObjectBuilder();
            json.add("Results", jMetaArray);
            javax.json.JsonObject jsonObj = json.build();
            StringWriter writer = new StringWriter();

            PrintWriter pw = response.getWriter();
            Json.createWriter(writer).write(jsonObj);
            String resultStr2 = writer.toString();
            logMgr.accessLog("jsonResponse : " + resultStr2);
            response.setContentType("application/Json;charset=utf-8\"");
            pw.write(resultStr2);
            con.close();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String decodeStr(String data) {
        String decodeData = new String();
        try {
            decodeData = java.net.URLDecoder.decode(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodeData;
    }

    public String getStrDate(String dt) {
        // String strDate = "12/12/07";
        logMgr.accessLog("Intial date is : " + dt);

        try {
            // create SimpleDateFormat object with source string date format
            SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd");

            // parse the string into Date object
            java.util.Date date = sdfSource.parse(dt);

            // create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("dd-MMM-yyyy");

            // parse the date into another format
            dt = sdfDestination.format(date);

            System.out.println(
                    CurrentDateTime.dateTime() + ":" + "Date is converted from yyyy-MM-dd format to dd-MM-yyyy");
            logMgr.accessLog("Converted date is : " + dt);

        } catch (Exception pe) {
            logMgr.accessLog("Parse Exception : " + pe);
        }

        return dt;
    }

    public void setDays(int value) {
        this.days = value;
    }

    public void setMonths(int value) {
        this.months = value;
    }

    public void setYears(int value) {
        this.years = value;
    }

    public int getDays() {
        return days;
    }

    public int getMonths() {
        return months;
    }

    public int getYears() {
        return years;
    }

    public void getDiffTime(String dateStart, String dateEnd) {
        try {

//			System.out.println("dateStart=" + dateStart);
//			System.out.println("dateEnd=" + dateEnd);
            String monthStart = dateStart.substring(5, 7);
            if (monthStart.startsWith("0")) {
                monthStart = dateStart.substring(6, 7);
            }
            String monthEnd = dateEnd.substring(5, 7);
            if (monthEnd.startsWith("0")) {
                monthEnd = dateEnd.substring(6, 7);
            }
            Month mType = Month.of(Integer.parseInt(monthStart));
            LocalDate date1 = LocalDate.of(Integer.parseInt(dateStart.substring(0, 4)), mType,
                    Integer.parseInt(dateStart.substring(8)));
            mType = Month.of(Integer.parseInt(monthEnd));
            LocalDate date2 = LocalDate.of(Integer.parseInt(dateEnd.substring(0, 4)), mType,
                    Integer.parseInt(dateEnd.substring(8)));
            System.out.println("Date 1 = " + date1);
            System.out.println("Date 2 = " + date2);
            Period p = Period.between(date1, date2);
//			System.out.println("Period = " + p);
//			System.out.println("Years (Difference) = " + p.getYears());
//			System.out.println("Month (Difference) = " + p.getMonths());
//			System.out.println("Days (Difference) = " + p.getDays());

            days = p.getDays();
            months = p.getMonths();
            years = p.getYears();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getTotalExp(int yrs, int mn, int dys) {
        totExp = "0";
        Period period = Period.ofMonths(mn).normalized();
        yrs += period.getYears();
        mn = period.getMonths();

        period = Period.ofDays(dys).normalized();
        yrs += period.getYears();
        mn += period.getMonths();
        dys = period.getDays();

        period = Period.ofMonths(mn).normalized();
        yrs += period.getYears();
        mn = period.getMonths();

        totExp = yrs + ":years ";
        if (mn > 0) {
            totExp += mn + ":months ";
        }
        if (dys > 0) {
            totExp += dys + ":days ";
        }

        System.out.println("TOTAL exp IS:" + totExp);
        return totExp;
    }

    public ArrayList getSequenceQualType(List typeArr) {
        ArrayList qualTypeSeq = new ArrayList();
        for (int i = 0; i < typeArr.size(); ++i) {
            String qualType = (String) typeArr.get(i); // Graduate Postgraduate PhD PostDoctoral
            if (qualType.equals("Graduate")) {
                qualTypeSeq.add(i);
            }
        }
        for (int i = 0; i < typeArr.size(); ++i) {
            String qualType = (String) typeArr.get(i); // Graduate Postgraduate PhD PostDoctoral
            if (qualType.equals("Postgraduate")) {
                qualTypeSeq.add(i);
            }
        }

        for (int i = 0; i < typeArr.size(); ++i) {
            String qualType = (String) typeArr.get(i); // Graduate Postgraduate PhD PostDoctoral
            if (qualType.equals("PhD")) {
                qualTypeSeq.add(i);
            }
        }
        for (int i = 0; i < typeArr.size(); ++i) {
            String qualType = (String) typeArr.get(i); // Graduate Postgraduate PhD PostDoctoral
            if (qualType.equals("PostDoctoral")) {
                qualTypeSeq.add(i);
            }
        }
        return qualTypeSeq;
    }

    private static String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private boolean isFileAvaiable(String fileName, String dirpath) {
        if (new File(dirpath + File.separator + fileName).exists()) {
            return true;
        } else {
            return false;
        }

    }

    /*^ : start of string
     [ : beginning of character group
     a-z : any lowercase letter
     A-Z : any uppercase letter
     0-9 : any digit
     _ : underscore
     ] : end of character group
     + : one or more of the given characters
     $ : end of string*/
    private boolean isCertValidPattern(String fileName) {
        //Pattern usrNamePtrn = Pattern.compile("([a-z A-Z 0-9\\_\\. {1,50})");
        Pattern usrNamePtrn = Pattern.compile("^[-a-zA-Z0-9_.]+$"); // ChangeId: 2023112801 hyphen included
        if (validatePatern(fileName, usrNamePtrn)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LoadAdvtNos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LoadAdvtNos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
