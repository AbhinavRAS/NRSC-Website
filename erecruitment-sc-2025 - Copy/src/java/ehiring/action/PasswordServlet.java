package ehiring.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
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
import ehiring.dao.PasswordUtils;
import ehiring.dao.Registration;
import ehiring.operation.SignUpOperation;
import ehiring.properties.LoadProperties;
import in.gov.nrscisro.bndauth.BhoonidhiAuthManager; // ChangeId: 2023120603

/**
 * Servlet implementation class PasswordServlet
 */
@WebServlet("/PasswordServlet")
public class PasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PasswordServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//		doPost(request, response);
//	}
//
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		LogManager logMgr;
		String emailLogId = java.net.URLDecoder.decode(request.getHeader("user"));
		logMgr = LogManager.getInstance(emailLogId);

		try {
			Registration reg = new Registration();
			logMgr.accessLog("Password Servlet Started");
			String msg = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String postStr = "";
			String action = "Successful";

			if (br != null) {
				postStr = br.readLine();
			}

			logMgr.accessLog("Value passwds  :" + postStr);
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
			String funcType = map1.get("action");

			if (funcType.equalsIgnoreCase("sendotp")) {
				boolean intFlg = true;
				String email = decode(map1.get("email").toString());
				LoadProperties lp = new LoadProperties();
				String internal = lp.getProperty("INTERNAL_USE");
				if (internal.equalsIgnoreCase("NO")) {
					if (email.equalsIgnoreCase("erecruit_admin@nrsc.gov.in")) {
						action = "error";
						msg = "Invalid User";
						intFlg = false;
					}

				} else if (internal.equalsIgnoreCase("YES")) {
					if (!email.equalsIgnoreCase("erecruit_admin@nrsc.gov.in")) {
						action = "error";
						msg = "Invalid User";
						intFlg = false;
					}

				}

				if (intFlg) {
					reg.setEmail(email);
					long mobile = new SignUpOperation(emailLogId).getMobileNo(reg);
					logMgr.accessLog("mobile no is:" + mobile);

					if (mobile == 0) {
						action = "error";
						msg = "Email ID is not yet registered. Please enter correct email/Signup";

					} else {
						PasswordUtils pu = new PasswordUtils();
						String passwd = map1.get("password").toString();
						String oldPasswd = decode(passwd);
						String encryPassWd = pu.ProtectUserPassword(oldPasswd);
						reg.setOldPassword(encryPassWd);
						action = new SignUpOperation(emailLogId).checkPasswd(reg);
						logMgr.accessLog("ACTION IS um :" + action);
						// action="success";
						if (action.equalsIgnoreCase("success")) {
							String otp = pu.generateOTP(); 
                                                        //String otp = "12345678";//OTP disabled for testing
							reg.setOTP(otp);
							action = new SignUpOperation(emailLogId).insertOTP(reg);
							EmailSender eSend = new EmailSender();
							// ChangeId: 2024021201 OTP to Mail commented
                                                        // action = eSend.send(email, "for Login is " + otp);
							eSend.sendSMS(mobile, "OTP for NRSC e-Recruitment system for login is :" + otp);

							if (action.equalsIgnoreCase("error")) {
								msg = "Could not send OTP";
							}
						} else if (action.equalsIgnoreCase("error")) {
							HashMap mp = new SignUpOperation(emailLogId).checkAttempts(reg);
							String attempt = (String) mp.get("attempt");
							System.out.println("ATTEMPTS ARE ...:" + attempt);

							if (attempt.equalsIgnoreCase("allow")) {
								msg = " Please enter the correct password.";
							} else if (attempt.equalsIgnoreCase("locked")) {
								msg = "Your attempts for incorrect password have already got exceeded. Account has been locked. Try after 10 minutes.";
							} else if (attempt.equalsIgnoreCase("increased")) {
								msg = " Please enter the correct password.";
							}

						}
					}
				}

			}
			// sendotp_um
			if (funcType.equalsIgnoreCase("sendotp_um")) {
				String email = decode(map1.get("email").toString());
				reg.setEmail(email);
				String oldPasswd = decode(map1.get("password").toString());
				PasswordUtils pu = new PasswordUtils();
				String encryPassWd = pu.ProtectUserPassword(oldPasswd);
				reg.setOldPassword(encryPassWd);
				action = new SignUpOperation(emailLogId).checkPasswd(reg);
				logMgr.accessLog("ACTION IS um :" + action);

				// ProtectUserPassword
				if (action.equalsIgnoreCase("success")) {
					long mobile = Long.parseLong(map1.get("um_mobile").toString());
					// System.out.println("mobile is :"+mobile);
					reg.setMobile(mobile);
					action = new SignUpOperation(emailLogId).isMobileNoExists(reg);
					// System.out.println("action mobile is :"+action);
					if (action.equalsIgnoreCase("success")) {
						String otp = pu.generateOTP();
						reg.setOTP(otp);
						action = new SignUpOperation(emailLogId).insertOTP(reg);
						EmailSender eSend = new EmailSender();
						action = eSend.send(email, "for Login is " + otp);
						eSend.sendSMS(mobile,
								"OTP for NRSC e-Recruitment system for change of mobile number is :" + otp);

						if (action.equalsIgnoreCase("error")) {
							msg = "Could not send OTP";
						} 
						

					} else if (action.equalsIgnoreCase("exists")) {
						msg = "Mobile number already exists.";
						action = "error";
					} else if (action.equalsIgnoreCase("same")) {
						msg = "You entered old number again. Please check the number. ";
						action = "error";
					}
				} else if (action.equalsIgnoreCase("error")) {
					HashMap mp = new SignUpOperation(emailLogId).checkAttempts(reg);
					String attempt = (String) mp.get("attempt");
					System.out.println("ATTEMPTS ARE ...:" + attempt);

					if (attempt.equalsIgnoreCase("allow")) {
						msg = " Please enter the correct password.";
					} else if (attempt.equalsIgnoreCase("locked")) {
						msg = "Your attempts for incorrect password have already got exceeded. Account has been locked. Try after 10 minutes.";
					} else if (attempt.equalsIgnoreCase("increased")) {
						msg = " Please enter the correct password.";

					}

				}
			}

			if (funcType.equalsIgnoreCase("sendotp_signup")) {
				String email = decode(map1.get("email").toString());
				long mobile = Long.parseLong(decode((map1.get("mobile").toString())));
				reg.setEmail(email);
				reg.setMobile(mobile);
                                                               
				// checkMailMobile
				int retn = new SignUpOperation(emailLogId).checkMailMobile(reg);
				if (retn == -1) {
					msg = "Email ID is already registered. Please enter a different email ID.";
					action = "error";
				} else if (retn == -2) {
					msg = "Mobile Number already registered. Please enter a different mobile number.";
					action = "error";
				} else if (retn == -3) {
					msg = "Please fill the details again.";
					action = "error";
				} else {
					PasswordUtils pu = new PasswordUtils();
					String otp = pu.generateOTP();
					reg.setOTP(otp);
					action = new SignUpOperation(emailLogId).insertSignupOTP(reg);
					EmailSender eSend = new EmailSender();
					action = eSend.send(email, "for Signup is " + otp);
                                        try {
						eSend.sendSMS(mobile, "OTP for NRSC e-Recruitment system for signup is :" + otp);
					} catch (Exception ex) {
					}
					if (action.equalsIgnoreCase("error")) {
						msg = "Could not send OTP";
					} else {
						msg = "OTP has been sent to your email ID and mobile";
					}
				}

			}

			if (funcType.equalsIgnoreCase("sendotp_forgot")) {
				String email = decode(map1.get("email").toString());
				reg.setEmail(email);
				long mobileNo = new SignUpOperation(emailLogId).getMobileNo(reg);
				reg.setMobile(mobileNo);
				logMgr.accessLog("mobile no is:" + mobileNo);

				if (mobileNo == 0) {
					action = "error";
					msg = "Email ID is not yet registered. Please enter correct email/Signup";

				} else// checkMailMobile
				{
					PasswordUtils pu = new PasswordUtils();
					String otp = pu.generateOTP();
					reg.setOTP(otp);
					action = new SignUpOperation(emailLogId).insertOTP(reg);
					EmailSender eSend = new EmailSender();
					action = eSend.send(email, " for Forgot passowrd is " + otp);
					try {
						eSend.sendSMS(mobileNo,
								"OTP for NRSC e-Recruitment system for Forgot password is :" + otp);
					} catch (Exception ex) {

					}
					if (action.equalsIgnoreCase("error")) {
						msg = "Could not send OTP";
					} else {
						msg = "OTP has been sent to your email ID and mobile";
					}
				}

			}

			if (funcType.equalsIgnoreCase("resendotp")) {
				String email = decode(map1.get("email").toString());
				reg.setEmail(email);
				long mobile = new SignUpOperation(emailLogId).getMobileNo(reg);

				PasswordUtils pu = new PasswordUtils();
				boolean flg_otp = new SignUpOperation(emailLogId).OTPValidity(reg);
				if (!flg_otp) {
					action = "error";
					msg = "OTP Expired. Please click on send OTP for new OTP.";
				} else {
					String otp = new SignUpOperation(emailLogId).getCurrentOTP(reg);
					System.out.println("opt : " + otp);
					if (otp.equalsIgnoreCase("-1")) {
						action = "error";
						msg = "Could not get OTP";
					} else if (otp.equals("") || otp.isEmpty()) {
						action = "error";
						msg = "No OTP to send. Please click send OTP";
					} else {
						EmailSender eSend = new EmailSender();
						String actionM = eSend.send(email, "is " + otp);
						String actionS = eSend.sendSMS(mobile, "Resent OTP for NRSC e-Recruitment system is :" + otp);
						//if (actionM.equalsIgnoreCase("success") || actionS.equalsIgnoreCase("success"))
						if (actionM.equalsIgnoreCase("success"))
							action = "success";
					}
				}

			}

			if (funcType.equalsIgnoreCase("changePasswd")) {
				String email = decode(map1.get("email").toString());
				reg.setEmail(email);
				String oldPasswd = decode(map1.get("oldPassword").toString());
				PasswordUtils pu = new PasswordUtils();
				String encryPassWd = pu.ProtectUserPassword(oldPasswd);
				reg.setOldPassword(encryPassWd);
				action = new SignUpOperation(emailLogId).checkPasswd(reg);

				// ProtectUserPassword
				if (action.equalsIgnoreCase("success")) {
					String passwd = map1.get("password").toString();
                                        // Start: ChangeId: 2023120604
                                        if(oldPasswd.equals(passwd)){
                                            action = "error";
                                            msg = "E0001:Your new password cannot be same as the last password!";
                                        }
                                        else{
                                            String repeatPasswd = map1.get("passrepeat").toString();
                                            if (passwd.equals(repeatPasswd)) {
                                                reg.setPassword(passwd);
                                                action = new SignUpOperation(emailLogId).updatePasswd(reg);
                                                // Start: ChangeId: 2023120603
                                                if (action.equalsIgnoreCase("success")) {
                                                    String vtoken = map1.get("vtoken").toString();
                                                    if (! (vtoken == null || vtoken.isEmpty() || vtoken.equalsIgnoreCase("null")) ) { // ChangeId: 2023121203
                                                        BhoonidhiAuthManager bam = new BhoonidhiAuthManager();
                                                        int sts = bam.destroyBhoonidhiSession(vtoken);
                                                        if(sts>0){
                                                            System.out.println("User session "+vtoken+" successfully logged out");
                                                        }
                                                    }
                                                }
                                                // End: ChangeId: 2023120603
                                            }
                                        }
                                        // End: ChangeId: 2023120604
					
				} else if (action.equalsIgnoreCase("error")) {
					msg = "The login credentials (email/password) do not match. Please enter the correct email and password.";
				}

			}

			if (funcType.equalsIgnoreCase("updatePasswd")) {
				String email = decode(map1.get("email"));
				reg.setEmail(email);
				String otp = decode(map1.get("otp"));
				reg.setOTP(otp);
				action = new SignUpOperation(emailLogId).checkOTP(reg);
				if (action.equals("success")) {

					// ProtectUserPassword
					String passwd = java.net.URLDecoder.decode(map1.get("password"), "UTF-8").toString();
					String repeatPasswd = java.net.URLDecoder.decode(map1.get("passrepeat"), "UTF-8").toString();
					if (passwd.equals(repeatPasswd)) {
                                            reg.setPassword(passwd);
                                            action = new SignUpOperation(emailLogId).updatePasswd(reg);
                                            // Start: ChangeId: 2023120603
                                            if (action.equals("success")) {
                                                String vtoken = map1.get("vtoken").toString();
                                                if (! (vtoken == null || vtoken.isEmpty() || vtoken.equalsIgnoreCase("null")) ) { // ChangeId: 2023121203
                                                    BhoonidhiAuthManager bam = new BhoonidhiAuthManager();
                                                    int sts = bam.destroyBhoonidhiSession(vtoken);
                                                    if(sts>0){
                                                        System.out.println("User session "+vtoken+" successfully logged out");
                                                    }
                                                }

                                            }
                                            else if(action.equals("error_samepassword")){
                                                action = "error";
                                                msg = "E0002:Your new password cannot be same as the last password!";
                                            }
                                            else if (action.equalsIgnoreCase("error")) {
                                                msg = "Invalid details. Please re-enter";
                                            }
                                            // End: ChangeId: 2023120603
					}

				} else if (action.equals("error_expired")) {
					msg = "OTP expired. Please refresh page";
				} else {
					action = "error";
					msg = "Invalid OTP, Please do check.";

				}

			}

			if (funcType.equalsIgnoreCase("updateMobile")) {
				String email = decode(map1.get("email"));
				reg.setEmail(email);
				reg.setMobile(Long.parseLong(map1.get("um_mobile")));
				String otp = map1.get("um_otp");
				reg.setOTP(otp);
				action = new SignUpOperation(emailLogId).checkOTP(reg);
				if (action.equals("success")) {
					action = new SignUpOperation(emailLogId).updateMobile(reg);
					if (action.equalsIgnoreCase("error"))
						msg = "Problem in changing Mobile number";
					else if (action.equalsIgnoreCase("same")) {
						action = "error";
						msg = "You have given the existing number, please Provide a new number";
					} else if (action.equalsIgnoreCase("exists")) {
						action = "error";
						msg = "You have given the already registered number, please Provide a new number";
					} else
						msg = "Successfully changed the Mobile number";
				} else {
					action = "error";
					msg = "Invalid OTP, Please check again.";

				}

			}

			JsonArrayBuilder jMetaArray = Json.createArrayBuilder();

			jMetaArray.add(action);
			jMetaArray.add(msg);
			JsonObjectBuilder json = Json.createObjectBuilder();
			json.add("Results", jMetaArray);
			javax.json.JsonObject jsonObj = json.build();
			StringWriter writer = new StringWriter();

			PrintWriter pw = response.getWriter();
			Json.createWriter(writer).write(jsonObj);
			String resultStr2 = writer.toString();
			logMgr.accessLog("jsonResponse : " + resultStr2);
			response.setContentType("application/Json;charset=utf-8\"");
			response.setHeader("Content-Security-Policy", "default-src 'self'");
			pw.write(resultStr2);
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
