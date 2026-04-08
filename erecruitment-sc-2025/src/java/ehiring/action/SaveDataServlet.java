package ehiring.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ehiring.dao.LogManager;
import ehiring.dao.Temp_Data;
import ehiring.operation.SaveOperation;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/SaveDataServlet")
public class SaveDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SaveDataServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@SuppressWarnings("rawtypes")
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LogManager logMgr;
		String emailLogId = java.net.URLDecoder.decode(request.getHeader("user"));
		logMgr = LogManager.getInstance(emailLogId);
	
		int personalResult = -1, xResult = -1, xiiResult = -1, ugResult = -1, pgResult = -1, phdResult = -1,
				pdResult = -1, expResult = 0;
		String[] applicant_eligibility;
		boolean insertFlag = false;
		logMgr.accessLog("Inside AddAdvertisment Servlet");

		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String postStr = "";

		if (br != null) {
			postStr = br.readLine();
		}
	//	logMgr.accessLog("Value post  :" + postStr + ":");
		postStr = postStr.substring(postStr.indexOf("{") + 1, postStr.indexOf("}"));
		postStr = postStr.replaceAll("\"", "");

		Map<String, String> map1 = new LinkedHashMap<String, String>();
		String[] pairs = postStr.split(",");
		for (int i = 0; i < pairs.length; i++) {
			String pair = pairs[i];
			String[] keyVal = pair.split(":");
			logMgr.accessLog(keyVal[0] + "," + keyVal[1]);
			map1.put(keyVal[0].trim(), keyVal[1]);
		}
		logMgr.accessLog("Map values : " + map1);

		if (map1.get("action").equalsIgnoreCase("savepersonal")) {
			Temp_Data tempData = new Temp_Data();
			Iterator<Entry<String, String>> hmIterator1 = map1.entrySet().iterator();

			String applicantInfo = new String();
			while (hmIterator1.hasNext()) {
				Map.Entry mapElement = (Map.Entry) hmIterator1.next();
				applicantInfo = (String) mapElement.getKey();
				logMgr.accessLog("THE p_STRING is  :" + applicantInfo);

				String mapValue = (String) map1.get(applicantInfo).trim();

				if (applicantInfo.equalsIgnoreCase("advt_no")) {
					tempData.setAdvt_no(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("post_no")) {
					tempData.setPost_no(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("email")) {
					tempData.setEmail(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("salutation")) {
					tempData.setSalutation(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("first_name")) {
					tempData.setFirst_name(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("last_name")) {
					tempData.setLast_name(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("gender")) {
					tempData.setGender(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("marital_status")) {
					tempData.setMarital_status(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("dob")) {
					tempData.setDob(Date.valueOf(mapValue));
				}
				if (applicantInfo.equalsIgnoreCase("place_of_birth")) {
					tempData.setPlace_of_birth(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("nationality")) {
					tempData.setNationality(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("category")) {
					if (mapValue != null)
						tempData.setCategory(mapValue);

					if (mapValue.equalsIgnoreCase("pwd")){
						tempData.setCategory_pwd(map1.get("category_pwd"));
                                                // Start: ChangeId: 2025041501
                                                tempData.setCategoryPwdScribe(map1.get("category_pwd_scribe"));
                                                tempData.setCategoryPwdCertificate(map1.get("category_pwd_certificate"));
                                                // End: ChangeId: 2025041501
                                                
                                                //ChangeId: 2025050807
                                                tempData.setCategoryPwdCompTime(map1.get("category_pwd_comptime"));
                                        }
                                                
					else if (mapValue.equalsIgnoreCase("exservice")) {
						tempData.setCategory_exservice_from(Date.valueOf(map1.get("category_exservice_from")));
						tempData.setCategory_exservice_to(Date.valueOf(map1.get("category_exservice_to")));
					} else if (mapValue.equalsIgnoreCase("merit sports")) {
						tempData.setCategory_merit_sportname(map1.get("category_merit_sportname"));
						tempData.setCategory_merit_sportlevel(map1.get("category_merit_sportlevel"));
					} else if (mapValue.equalsIgnoreCase("ews")) {
						tempData.setCategory_ews(map1.get("category_ews"));
					}
				}
				if (applicantInfo.equalsIgnoreCase("house_no")) {
					tempData.setHouse_no(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("locality")) {
					tempData.setLocality(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("state")) {
					tempData.setState(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("district")) {
					tempData.setDistrict(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("town")) {
					tempData.setTown(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pincode")) {
					tempData.setPincode(Integer.parseInt(mapValue));
				}
				if (applicantInfo.equalsIgnoreCase("p_house_no")) {
					tempData.setP_house_no(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("p_locality")) {
					tempData.setP_locality(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("p_state")) {
					tempData.setP_state(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("p_district")) {
					tempData.setP_district(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("p_town")) {
					tempData.setP_town(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("p_pincode")) {
					tempData.setP_pincode(Integer.parseInt(mapValue));
				}
				if (applicantInfo.equalsIgnoreCase("contact_no")) {
					tempData.setContact_no(Long.parseLong(mapValue));
				}
                                // PPEG-HRD: Start AADHAAR
                                if (applicantInfo.equalsIgnoreCase("aadhaar")) {
					tempData.setAadhaar(Long.parseLong(mapValue));
				}
                                // PPEG-HRD: End AADHAAR
				if (applicantInfo.equalsIgnoreCase("alternate_contact")) {
					tempData.setAlternate_contact(Long.parseLong(mapValue));
				}
				if (applicantInfo.equalsIgnoreCase("nearest_railway_station")) {
					tempData.setNearest_railway_station(mapValue);
				}
                                
                                //Start: ChangeId:2025041601
                                if (applicantInfo.equalsIgnoreCase("bank_acc_beneficiary")) {
					tempData.setBank_Acc_Beneficiary(mapValue);
				}
                                if (applicantInfo.equalsIgnoreCase("bank_acc_doc")) {
					tempData.setBank_Acc_Doc(mapValue);
				}
                                //End: ChangeId:2025041601
                                
                                // Start: ChangeId: 2023111001
                                if (applicantInfo.equalsIgnoreCase("bank_acc_no")) {
					tempData.setBank_Acc_No(mapValue);
				}
                                if (applicantInfo.equalsIgnoreCase("bank_ifsc_code")) {
					tempData.setBank_IFSC_Code(mapValue);
				}
                                // Start: ChangeId: 2023111001
                                
                                // Start: ChangeId:2023111101
                                if (applicantInfo.equalsIgnoreCase("cgov_serv")) {
                                    logMgr.accessLog("HRD_DEBUG SERV"+applicantInfo);
                                    tempData.setCGov_Serv(mapValue);
				}
                                if (applicantInfo.equalsIgnoreCase("cgov_serv_doj")) {
                                    logMgr.accessLog("HRD_DEBUG SERV_DOJ"+applicantInfo);
                                    tempData.setCGov_Serv_DOJ(Date.valueOf(map1.get("cgov_serv_doj")));
				}
                                // End: ChangeId:2023111101
			}
			try {
				personalResult = new SaveOperation(emailLogId).savePersonalData(tempData);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (personalResult > 0)
				insertFlag = true;
			if (insertFlag) {
				logMgr.accessLog("Personal Data saved.");
			} else {
				logMgr.accessLog("Exception in saving data.");
			}
			try {
				JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
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

			} catch (Exception e) {
				e.printStackTrace();
				logMgr.accessLog("Error in process request:" + e.getMessage());
				response.setContentType("text/html");
				response.getWriter().print(e.getMessage());
			}
		}

		else if (map1.get("action").equalsIgnoreCase("saveEducation")) {
			Temp_Data tempData = new Temp_Data();

			Iterator<Entry<String, String>> hmIterator1 = map1.entrySet().iterator();
			String applicantInfo = new String();
			while (hmIterator1.hasNext()) {
				Map.Entry mapElement = (Map.Entry) hmIterator1.next();
				applicantInfo = (String) mapElement.getKey();
				logMgr.accessLog("THE p_STRING is  :" + applicantInfo);

				String mapValue = (String) map1.get(applicantInfo).trim();
				List elgArray = new ArrayList();

				if (applicantInfo.equalsIgnoreCase("eligibility")) {
					logMgr.accessLog("eligib is: " + mapValue);
					applicant_eligibility = mapValue.split("\\|");
					logMgr.accessLog("applicant_eligibility is : " + applicant_eligibility);
					elgArray = Arrays.asList(applicant_eligibility);
					logMgr.accessLog("elgArray is : " + elgArray);
				}

				if (applicantInfo.equalsIgnoreCase("x_edu_board")) {
					tempData.setX_edu_board(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("x_school")) {
					tempData.setX_school(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("x_year_of_passing")) {
					tempData.setX_year_of_passing(Integer.parseInt(mapValue));
				}
				if (applicantInfo.equalsIgnoreCase("x_division")) {
					tempData.setX_division(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("x_percentage_cgpa")) {
                                         
					if (mapValue != null)
						tempData.setX_percentage_cgpa(mapValue);
                                        // Start: ChangeId 2023121901
                                        /*
					if (mapValue.equalsIgnoreCase("CGPA")) {
						tempData.setX_cgpa_obt(Float.parseFloat(map1.get("x_cgpa_obt")));
						tempData.setX_cgpa_max(map1.get("x_cgpa_max"));
						tempData.setX_percentage(Float.parseFloat(map1.get("x_percentage")));
					} else if (mapValue.equalsIgnoreCase("percentage")) {
						tempData.setX_percentage(Float.parseFloat(map1.get("x_percentage")));
					}
                                        */
                                        tempData.setX_cgpa_obt(Float.parseFloat(map1.get("x_cgpa_obt")));
                                        tempData.setX_cgpa_max(map1.get("x_cgpa_max"));
                                        tempData.setX_cgpa_to_perc(Float.parseFloat(map1.get("x_cgpa_to_perc")));
                                        tempData.setX_percentage(Float.parseFloat(map1.get("x_percentage")));
                                        // End: ChangeId 2023121901
				}

				if (applicantInfo.equalsIgnoreCase("xii_edu_board")) {
					tempData.setXii_edu_board(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("xii_school")) {
					tempData.setXii_school(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("xii_year_of_passing")) {
					int xii_year_of_passing = Integer.parseInt(mapValue);
					tempData.setXii_year_of_passing(xii_year_of_passing);
				}
				if (applicantInfo.equalsIgnoreCase("xii_specialization")) {
					tempData.setXii_specialization(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("xii_division")) {
					tempData.setXii_division(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("xii_percentage_cgpa")) {
                                        if (mapValue != null)
						tempData.setXii_percentage_cgpa(mapValue);
                                        // Start: ChangeId 2023122001
                                        /*
					if (mapValue.equalsIgnoreCase("CGPA")) {
						tempData.setXii_cgpa_obt(Float.parseFloat(map1.get("xii_cgpa_obt")));
						tempData.setXii_cgpa_max(map1.get("xii_cgpa_max"));
						tempData.setXii_percentage(Float.parseFloat(map1.get("xii_percentage")));
					} else if (mapValue.equalsIgnoreCase("percentage")) {
						tempData.setX_percentage(Float.parseFloat(map1.get("xii_percentage")));
					}
                                        */
                                        tempData.setXii_cgpa_obt(Float.parseFloat(map1.get("xii_cgpa_obt")));
                                        tempData.setXii_cgpa_max(map1.get("xii_cgpa_max"));
                                        tempData.setXii_cgpa_to_perc(Float.parseFloat(map1.get("xii_cgpa_to_perc")));
                                        tempData.setXii_percentage(Float.parseFloat(map1.get("xii_percentage")));
                                        // End: ChangeId 2023122001
					
				}
				if (applicantInfo.equalsIgnoreCase("ug_university")) {
					tempData.setUg_university(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("ug_college")) {
					tempData.setUg_college(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("ug_year_of_passing")) {
					int ug_year_of_passing = Integer.parseInt(mapValue);
					tempData.setUg_year_of_passing(ug_year_of_passing);
				}
				if (applicantInfo.equalsIgnoreCase("ug_qualification")) {
					tempData.setUg_qualification(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("ug_discipline")) {
					tempData.setUg_discipline(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("ug_division")) {
					tempData.setUg_division(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("ug_percentage_cgpa")) {
					if (mapValue != null)
						tempData.setUg_percentage_cgpa(mapValue);
					if (mapValue.equalsIgnoreCase("CGPA")) {
						tempData.setUg_cgpa_obt(Float.parseFloat(map1.get("ug_cgpa_obt")));
						tempData.setUg_cgpa_max(map1.get("ug_cgpa_max"));
						tempData.setUg_percentage(Float.parseFloat(map1.get("ug_percentage")));
					} else if (mapValue.equalsIgnoreCase("percentage")) {
						tempData.setUg_percentage(Float.parseFloat(map1.get("ug_percentage")));
					}
				}

				if (applicantInfo.equalsIgnoreCase("pg_university")) {
					tempData.setPg_university(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pg_college")) {
					tempData.setPg_college(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pg_year_of_passing")) {
					int pg_year_of_passing = Integer.parseInt(mapValue);
					tempData.setPg_year_of_passing(pg_year_of_passing);
				}
				if (applicantInfo.equalsIgnoreCase("pg_qualification")) {
					tempData.setPg_qualification(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pg_discipline")) {
					tempData.setPg_discipline(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pg_division")) {
					tempData.setPg_division(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pg_percentage_cgpa")) {
					if (mapValue != null)
						tempData.setPg_percentage_cgpa(mapValue);
					if (mapValue.equalsIgnoreCase("CGPA")) {
						tempData.setPg_cgpa_obt(Float.parseFloat(map1.get("pg_cgpa_obt")));
						tempData.setPg_cgpa_max(map1.get("pg_cgpa_max"));
						tempData.setPg_percentage(Float.parseFloat(map1.get("pg_percentage")));
					} else if (mapValue.equalsIgnoreCase("percentage")) {
						tempData.setPg_percentage(Float.parseFloat(map1.get("pg_percentage")));
					}
				}

				if (applicantInfo.equalsIgnoreCase("phd_university")) {
					tempData.setPhd_university(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("phd_college")) {
					tempData.setPhd_college(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("phd_year_of_passing")) {
					int phd_year_of_passing = Integer.parseInt(mapValue);
					tempData.setPhd_year_of_passing(phd_year_of_passing);
				}
				if (applicantInfo.equalsIgnoreCase("phd_qualification")) {
					tempData.setPhd_qualification(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("phd_discipline")) {
					tempData.setPhd_discipline(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("phd_division")) {
					tempData.setPhd_division(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("phd_percentage_cgpa")) {
					if (mapValue != null)
						tempData.setPhd_percentage_cgpa(mapValue);
					if (mapValue.equalsIgnoreCase("CGPA")) {
						tempData.setPhd_cgpa_obt(Float.parseFloat(map1.get("phd_cgpa_obt")));
						tempData.setPhd_cgpa_max(map1.get("phd_cgpa_max"));
						tempData.setPhd_percentage(Float.parseFloat(map1.get("phd_percentage")));
					} else if (mapValue.equalsIgnoreCase("percentage")) {
						tempData.setPhd_percentage(Float.parseFloat(map1.get("phd_percentage")));
					}
				}
				if (applicantInfo.equalsIgnoreCase("pd_university")) {
					tempData.setPd_university(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pd_college")) {
					tempData.setPd_college(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pd_year_of_passing")) {
					int pd_year_of_passing = Integer.parseInt(mapValue);
					tempData.setPd_year_of_passing(pd_year_of_passing);
				}
				if (applicantInfo.equalsIgnoreCase("pd_qualification")) {
					tempData.setPd_qualification(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pd_discipline")) {
					tempData.setPd_discipline(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pd_division")) {
					tempData.setPd_division(mapValue);
				}
				if (applicantInfo.equalsIgnoreCase("pd_percentage_cgpa")) {
					if (mapValue != null)
						tempData.setPd_percentage_cgpa(mapValue);
					if (mapValue.equalsIgnoreCase("CGPA")) {
						tempData.setPd_cgpa_obt(Float.parseFloat(map1.get("pd_cgpa_obt")));
						tempData.setPd_cgpa_max(map1.get("pd_cgpa_max"));
						tempData.setPd_percentage(Float.parseFloat(map1.get("pd_percentage")));
					} else if (mapValue.equalsIgnoreCase("percentage")) {
						tempData.setPd_percentage(Float.parseFloat(map1.get("pd_percentage")));
					}
				}

				try {
					if (elgArray.contains("X")) {
						xResult = new SaveOperation(emailLogId).saveEducationData(tempData);
						if (xResult <= 0)
							insertFlag = false;
					}
					if (elgArray.contains("XII")) {
						xiiResult = new SaveOperation(emailLogId).saveEducationData(tempData);
						if (xiiResult <= 0)
							insertFlag = false;
					}
					if (elgArray.contains("Graduate")) {
						ugResult = new SaveOperation(emailLogId).saveEducationData(tempData);
						if (ugResult <= 0)
							insertFlag = false;
					}

					if (elgArray.contains("Postgraduate")) {
						pgResult = new SaveOperation(emailLogId).saveEducationData(tempData);
						if (pgResult <= 0)
							insertFlag = false;
					}
					if (elgArray.contains("PhD")) {
						phdResult = new SaveOperation(emailLogId).saveEducationData(tempData);
						if (phdResult <= 0)
							insertFlag = false;
					}
					if (elgArray.contains("PostDoctoral")) {
						pdResult = new SaveOperation(emailLogId).saveEducationData(tempData);
						if (pdResult <= 0)
							insertFlag = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (xResult > 0)
				insertFlag = true;
			if (insertFlag) {
				logMgr.accessLog("X Data saved.");
			} else {
				logMgr.accessLog("Exception in saving X data.");
			}
			if (xiiResult > 0)
				insertFlag = true;
			if (insertFlag) {
				logMgr.accessLog("XII Data saved.");
			} else {
				logMgr.accessLog("Exception in saving XII data.");
			}
			if (ugResult > 0)
				insertFlag = true;
			if (insertFlag) {
				logMgr.accessLog("ug Data saved.");
			} else {
				logMgr.accessLog("Exception in saving ug data.");
			}
			if (pgResult > 0)
				insertFlag = true;
			if (insertFlag) {
				logMgr.accessLog("pg Data saved.");
			} else {
				logMgr.accessLog("Exception in saving pg data.");
			}
			if (phdResult > 0)
				insertFlag = true;
			if (insertFlag) {
				logMgr.accessLog("phd Data saved.");
			} else {
				logMgr.accessLog("Exception in saving phd data.");
			}
			if (pdResult > 0)
				insertFlag = true;
			if (insertFlag) {
				logMgr.accessLog("pd Data saved.");
			} else {
				logMgr.accessLog("Exception in saving pd data.");
			}

			try {
				JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
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

			} catch (Exception e) {
				e.printStackTrace();
				logMgr.accessLog("Error in process request:" + e.getMessage());
				response.setContentType("text/html");
				response.getWriter().print(e.getMessage());
			}
		} else if (map1.get("action").equalsIgnoreCase("saveExperience")) {
			Temp_Data tempData = new Temp_Data();

			Iterator<Entry<String, String>> hmIterator1 = map1.entrySet().iterator();
			String applicantInfo = new String();
			while (hmIterator1.hasNext()) {
				Map.Entry mapElement = (Map.Entry) hmIterator1.next();
				applicantInfo = (String) mapElement.getKey();
				logMgr.accessLog("THE p_STRING is  :" + applicantInfo);

				String mapValue = (String) map1.get(applicantInfo).trim();
				if (applicantInfo.equalsIgnoreCase("experience")) {
					if (mapValue != null) {
						tempData.setExperience(mapValue);
					}
					if (mapValue.equalsIgnoreCase("yes")) {
						tempData.setExp_years(Integer.parseInt(map1.get("exp_years")));
						tempData.setExp_months(Integer.parseInt(map1.get("exp_months")));
						tempData.setEmp_name(map1.get("emp_name"));
						tempData.setEmp_address(map1.get("emp_address"));
						tempData.setTime_from(Date.valueOf(map1.get("time_from")));
						tempData.setTime_to(Date.valueOf(map1.get("time_to")));
					}
				}
			}
			try {
				expResult = new SaveOperation(emailLogId).saveExperienceData(tempData);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (expResult > 0)
				insertFlag = true;
			if (insertFlag) {
				logMgr.accessLog("Experience Data saved.");
			} else {
				logMgr.accessLog("Exception in saving experience data.");
			}
			try {
				JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
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

			} catch (Exception e) {
				e.printStackTrace();
				logMgr.accessLog("Error in process request:" + e.getMessage());
				response.setContentType("text/html");
				response.getWriter().print(e.getMessage());
			}
		}
	}
}