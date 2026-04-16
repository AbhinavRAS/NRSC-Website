package ehiring.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ehiring.dao.Registration;
import ehiring.operation.LoginOperation;
import ehiring.operation.SignUpOperation;
import ehiring.properties.LoadProperties;
import in.gov.nrscisro.bndauth.BhoonidhiAuthManager;
import ehiring.dao.CipherCreator;
import ehiring.dao.LogManager;

/**
 * Servlet implementation class Register
 */
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    LogManager logMgr;

    /**
     * Default constructor.
     */
    public LoginServlet() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       System.out.println("In doGet of LoginServlet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            String emailLogId = java.net.URLDecoder.decode(request.getHeader("user"));
            System.out.println("emailLogId:" + emailLogId);
            logMgr = LogManager.getInstance(emailLogId);
            logMgr.accessLog("Login Servlet Started");

            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
			// System.out.println(CurrentDateTime.dateTime()+":"+"ipAddress : " +
            // ipAddress);

            logMgr.accessLog("Login Servlet Started");
            JsonArrayBuilder jMetaArray = Json.createArrayBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String postStr = "";

            if (br != null) {
                postStr = br.readLine();
            }
			// System.out.println(CurrentDateTime.dateTime()+":"+"Value post :" + postStr +
            // ":");
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
            boolean intFlag = true;
            //String userId = new String();
            if (map1.get("action").equalsIgnoreCase("getUserType")) {
                LoadProperties lp = new LoadProperties();
                String internal = lp.getProperty("INTERNAL_USE");
                System.out.println("internal use is ...." + internal);
                System.out.println(internal.equalsIgnoreCase("YES"));
                intFlag = false;
                if (internal.equalsIgnoreCase("NO")) {
                    jMetaArray.add("applicant");
                } else if (internal.equalsIgnoreCase("YES")) {
                    System.out.println("internal use is ...." + internal);
                    jMetaArray.add("admin");
                }
            }

            if (intFlag) {
                if (map1.get("action").equalsIgnoreCase("getToken")) {
                    String session_manual = UUID.randomUUID().toString();
                    // Generate new token for user -> SessionID + timestamp
                    String token = session_manual + System.currentTimeMillis();

                    jMetaArray.add("success"); // action string - success / error
                    jMetaArray.add(token);

                } else if (map1.get("action").equalsIgnoreCase("activate")) {
                    // TODO Auto-generated method stub
                    String msg = "Your account has been activated";
                    String action = new String();
                    String userId = map1.get("email");
                    userId = new CipherCreator().decipher(userId);
                    userId = java.net.URLDecoder.decode(userId, "UTF-8");
                    if (!userId.equals("")) {
                        String status = new LoginOperation(emailLogId).activateUser(userId);
                        action = status;
                        if (action.startsWith("error")) {
                            action = "error";
                            msg = status.replace("error ", "");
                        }
                    } else {
                        action = "error";
                        msg = "Activation email is not available";
                    }

                    jMetaArray.add(action); // action string - success / error
                    jMetaArray.add(msg); // retStr - admin / applicant / no login found

                } else if (map1.get("action").equalsIgnoreCase("login")) {
                    String email = java.net.URLDecoder.decode(map1.get("email"), "UTF-8");
                    String pass = java.net.URLDecoder.decode(map1.get("password"), "UTF-8");
                    String otp = java.net.URLDecoder.decode(map1.get("otp"), "UTF-8");

                    BhoonidhiAuthManager bam = new BhoonidhiAuthManager();
                    String jws = bam.startSession(email, ipAddress);
                    if (jws.equalsIgnoreCase("EXCEPTON")) {
                        String msg = "Error occurred. Please try again.";
                        jMetaArray.add("error");
                        jMetaArray.add(msg);
                    } else {
                        String retStr = "Login not found. Please check username and password.";
                        String userName = "";
                        String UPLOAD_DIR = "";
                        String dob = "";

                        Registration reg = new Registration();
                        reg.setEmail(email);
                        reg.setPassword(pass);
                        reg.setOTP(otp);

                        String session_manual = UUID.randomUUID().toString();
                        // Generate new token for user -> SessionID + timestamp
                        String token = session_manual + System.currentTimeMillis();

                        String value = new String();
                        HashMap mp = new LoginOperation(emailLogId).login(reg);
                        value = (String) mp.get("typeOfUser");
                        logMgr.accessLog("Value : " + value);
                        String actStr = "error";

                        if (value.equalsIgnoreCase("invalid")) {
                            actStr = "error";
                            retStr = "Check Login/Password";
                        } else if (value.equalsIgnoreCase("inactive")) {
                            actStr = "error";
                            retStr = "Your account is inactive. Please check your email to activate your account.";
                        } else if (value.equalsIgnoreCase("error")) {
                            actStr = "error";
                            retStr = "Invalid OTP, please enter the correct OTP.";
                        } else if (value.equalsIgnoreCase("attempt")) {
                            actStr = "error";
                            retStr = "Account is locked after 3 wrong password attempts, please try after 10 minutes.";
                        }
                        String mobile = new String();
                        if (value.equalsIgnoreCase("administrator")) {
                            actStr = "success";
                            retStr = "admin";
                            userName = "Administrator";
                            ehiring.operation.RecruitmentDirectoryOperation rdo = new ehiring.operation.RecruitmentDirectoryOperation(emailLogId);
                            String contextName = request.getServletContext().getContextPath();
                            if (rdo.createAdminFolders()) {
                                UPLOAD_DIR = rdo.getDirpath() + java.io.File.separator;
                            }
                        } else if (value.equalsIgnoreCase("applicant")) {
                            actStr = "success";
                            retStr = "applicant";
                            userName = (String) mp.get("userName");
                            dob = (String) mp.get("dob");
                            mobile = mp.get("mobile").toString();

                            reg.setEmail(email);
                            reg.setOTP("");
                            String action_Str = new SignUpOperation(emailLogId).insertOTP(reg);
                            // userName = "Applicant";

                            ehiring.operation.RecruitmentDirectoryOperation rdo = new ehiring.operation.RecruitmentDirectoryOperation(emailLogId);
                            String contextName = request.getServletContext().getContextPath();
                            UPLOAD_DIR = rdo.getAppIdFolderName(contextName, email);
                        }

                        jMetaArray.add(actStr); // action string - success / error
                        jMetaArray.add(retStr); // retStr - admin / applicant / no login found
                        jMetaArray.add(userName);
                        jMetaArray.add(UPLOAD_DIR);
                        jMetaArray.add(email);
                        jMetaArray.add(dob);
                        jMetaArray.add(token);
                        jMetaArray.add(mobile);
                        jMetaArray.add(jws);
                    }

                }
            } // int flag

            logMgr.accessLog("jMetaArray : " + jMetaArray);
            JsonObjectBuilder json = Json.createObjectBuilder();
            json.add("Results", jMetaArray);
            javax.json.JsonObject jsonObj = json.build();
            StringWriter writer = new StringWriter();

            PrintWriter pw = response.getWriter();
            Json.createWriter(writer).write(jsonObj);
            String resultStr2 = writer.toString();
            System.out.println("ResultString");
            System.out.println(resultStr2);
            logMgr.accessLog("jsonResponse : " + resultStr2);
            response.setContentType("application/Json;charset=utf-8\"");
            pw.write(resultStr2);
            pw.close();
        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
