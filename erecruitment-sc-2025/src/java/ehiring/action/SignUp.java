package ehiring.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ehiring.dao.EmailSender;
import ehiring.dao.LogManager;
import ehiring.dao.Registration;
import ehiring.operation.SignUpOperation;
import ehiring.properties.LoadProperties;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/register")
public class SignUp extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LogManager logMgr;
        String emailLogId = java.net.URLDecoder.decode(request.getHeader("user"));
        logMgr = LogManager.getInstance(emailLogId);

        try {

            logMgr.accessLog("Signup Servlet Started");
            String action = new String();
            String messageStr = new String();
            if (true) {
//                action = "error";
//                messageStr = "Sign Up not allowed!!";
//                JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
//
//                jMetaArray.add(action);
//                jMetaArray.add(messageStr);
//                JsonObjectBuilder json = Json.createObjectBuilder();
//                json.add("Results", jMetaArray);
//                javax.json.JsonObject jsonObj = json.build();
//                StringWriter writer = new StringWriter();
//
//                PrintWriter pw = response.getWriter();
//                Json.createWriter(writer).write(jsonObj);
//                String resultStr2 = writer.toString();
//                logMgr.accessLog("jsonResponse : " + resultStr2);
//                response.setContentType("application/Json;charset=utf-8\"");
//                pw.write(resultStr2);
//                pw.close();
//            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
                String postStr = "";

                if (br != null) {
                    postStr = br.readLine();
                }
                // logMgr.accessLog("Value post :" + postStr + ":");
                postStr = postStr.substring(postStr.indexOf("{") + 1, postStr.indexOf("}"));
                postStr = postStr.replaceAll("\"", "");

                Map<String, String> map1 = new LinkedHashMap<String, String>();
                String[] pairs = postStr.split(",");
                for (int i = 0; i < pairs.length; i++) {
                    String pair = pairs[i];
                    logMgr.accessLog("pair is:" + pair);
                    String[] keyVal = pair.split(":");
                    logMgr.accessLog(keyVal[0] + "," + keyVal[1]);
                    map1.put(keyVal[0].trim(), keyVal[1]);
                }
                logMgr.accessLog("Map values : " + map1);
                String email = decode(map1.get("email"));
                long mobile = Long.parseLong(decode(map1.get("mobile")));
			// String pass =java.net.URLDecoder.decode(map1.get("password"),
                // "UTF-8").toString();

                String dob = new String();
                Registration reg = new Registration();
                int i = -1;
                action = new String();
                logMgr.accessLog("SignUp Servlet Started");

                String name = decode(map1.get("name"));
                reg.setName(name);
                reg.setMobile(mobile);
                boolean intFlg = true;

                messageStr = new String();
                LoadProperties lp = new LoadProperties();
                String internal = lp.getProperty("INTERNAL_USE");
                if (internal.equalsIgnoreCase("NO")) { // allowed to signup
                    if (name.startsWith("Administrator") || name.startsWith("admininistrator")) {
                        action = "error";
                        messageStr = "Invalid name";
                        intFlg = false;
                    }

                } else if (internal.equalsIgnoreCase("YES")) {
                    intFlg = false;
                }

                dob = map1.get("dob").toString();
                logMgr.accessLog("DATE OF BIRTH:" + dob);
                int age = getAge(dob);
                if (age >= 18 && age <= 60) {
                    Date dateofbirth = Date.valueOf(dob);
                    reg.setDob(dateofbirth);
                    intFlg = true;
                } else {
                    logMgr.accessLog("DOB NOT ELIGIBLE:" + age);
                    // response.sendRedirect("administrator_profile.html");
                    intFlg = false;
                    action = "error";
                    messageStr = "Your not eligible for applying, your age should be between(18-60).";
                }

                if (intFlg) {
                    reg.setEmail(email);
                    reg.setOTP(decode(map1.get("otp")));

                    action = new SignUpOperation(emailLogId).checkOTP(reg);
                    if (action.equals("success")) {

                        // ProtectUserPassword
                        String passwd = decode(map1.get("password"));
                        String repeatPasswd = decode(map1.get("passrepeat"));
                        if (passwd.equals(repeatPasswd)) {
                            reg.setPassword(passwd);
                            i = new SignUpOperation(emailLogId).register(reg);
                        }

                        logMgr.accessLog("The value of i is:" + i);

                        if (i > 0) {
                            logMgr.accessLog("New Account Successfully created");
                            action = "success";
                            EmailSender eSend = new EmailSender();
						// String message = "Dear Applicant,\n\nYou have successfully registered with
                            // the NRSC e-Recruitment system.The One Time Password (OTP) for NRSC
                            // e-Recruitment system " + msg + ".\n\nThis OTP is valid for 10 minutes or 1
                            // successful attempt whichever is earlier.\nPlease note this OTP is valid only
                            // for this attempt and cannot be used for any other attempt.\n\nWith
                            // Regards,\nRecruitment Section,\nNational Remote Sensing Centre (NRSC), ISRO";
                            String message = "Dear Applicant,\n\nYou have successfully registered with the NRSC e-Recruitment system";
                            String actionM = eSend.send(email, message);
                            String actionS = eSend.sendSMS(mobile, message);
                            //if (actionM.equalsIgnoreCase("success") || actionS.equalsIgnoreCase("success"))
                            if (actionM.equalsIgnoreCase("success")) {
                                action = "success";
                            }
                        } else if (i == -1) {
                            logMgr.accessLog("Account creation  exception");
                            // response.sendRedirect("administrator_profile.html");

                            action = "error";
                            messageStr = "Email already exists.";
                        } else if (i == -2) {
                            logMgr.accessLog("Account Mobile creation  exception");
                            // response.sendRedirect("administrator_profile.html");

                            action = "error";
                            messageStr = "Mobile already exists.";
                        } else {
                            logMgr.accessLog("Account Mobile creation  exception");
                            // response.sendRedirect("administrator_profile.html");

                            action = "error";
                            messageStr = "Account creation problem";
                        }
                    } else {
                        action = "error";
                        messageStr = "Invalid OTP, Please Check.";
                    }
                } // intflag true

                JsonArrayBuilder jMetaArray = Json.createArrayBuilder();

                jMetaArray.add(action);
                jMetaArray.add(messageStr);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getAge(String dob) {
        int age = 0;

        try {
            String monthStart = dob.substring(5, 7);
            if (monthStart.startsWith("0")) {
                monthStart = dob.substring(6, 7);
            }
            String monthEnd = dob.substring(5, 7);
            if (monthEnd.startsWith("0")) {
                monthEnd = dob.substring(6, 7);
            }
            Month mType = Month.of(Integer.parseInt(monthStart));
            LocalDate l = LocalDate.of(Integer.parseInt(dob.substring(0, 4)), mType,
                    Integer.parseInt(dob.substring(8)));
            LocalDate now = LocalDate.now(); // gets localDate
            Period diff = Period.between(l, now); // difference between the dates is calculated
            System.out.println(diff.getYears() + "years" + diff.getMonths() + "months" + diff.getDays() + "days");
            age = diff.getYears();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return age;
    }

    public String decode(String value) {
        try {
            value = java.net.URLDecoder.decode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
