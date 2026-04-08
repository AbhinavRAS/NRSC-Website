/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehiring.action;

import ehiring.dao.LogManager;
import ehiring.operation.AcademicEligibilityOperation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoadAcademicEligibility extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		LogManager logMgr;
		String emailLogId = java.net.URLDecoder.decode(request.getHeader("user"));
		logMgr = LogManager.getInstance(emailLogId);
		
		logMgr.accessLog("IN LoadAcademicEligibility Servlet");

		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String postStr = "";

		logMgr = LogManager.getInstance("erecruit_admin@nrsc.gov.in");
		logMgr.accessLog("LoadAcademicEligibility Started");
		
		boolean flg = false;

		if (br != null) {
			postStr = br.readLine();
		}
		//logMgr.accessLog("Value post  :" + postStr + ":");
		postStr = postStr.substring(postStr.indexOf("{") + 1, postStr.indexOf("}"));
		postStr = postStr.replaceAll("\"", "");

		Map<String, String> map1 = new LinkedHashMap<String, String>();
		String[] pairs = postStr.split(",");
		for (int i = 0; i < pairs.length; i++) {
			String pair = pairs[i];
			logMgr.accessLog("pair is:" + pair);
			String[] keyVal = pair.split(":");
			System.out.println(CurrentDateTime.dateTime()+":"+keyVal[0] + "," + keyVal[1]);
			map1.put(keyVal[0].trim(), keyVal[1]);
		}
		logMgr.accessLog("Map values : " + map1);

		ArrayList formattedList = new ArrayList();
		JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
		
		if (map1.get("name").equalsIgnoreCase("QualDisc")) {
			formattedList = new AcademicEligibilityOperation(emailLogId).getQualDiscipline();
            
            for (int j = 0; j < formattedList.size(); j++) {
                Map map = (HashMap) formattedList.get(j);
                JsonObjectBuilder jsonMapObject = Json.createObjectBuilder();
                for (Object key : map.keySet()) {
                	if(key.toString().equalsIgnoreCase("Qualification")) {
                		jsonMapObject.add(key.toString(), map.get(key.toString()).toString());
                	} else {
                		ArrayList tempArr = (ArrayList)map.get(key.toString());
                		JsonArrayBuilder jMetaSubArray = Json.createArrayBuilder();
                		for (int k = 0; k < tempArr.size(); k++) {
                			jMetaSubArray.add(tempArr.get(k).toString());
                		}
                		jsonMapObject.add(key.toString(), jMetaSubArray);
                	}
                }
                jMetaArray.add(jsonMapObject);
            }
            
		} else if (map1.get("name").equalsIgnoreCase("Specialization")) {
			formattedList = new AcademicEligibilityOperation(emailLogId).getSpecialization(map1.get("id").toString());
            for (int j = 0; j < formattedList.size(); j++) {
                jMetaArray.add(formattedList.get(j).toString());
            }
		}
		
		try {
		//	logMgr.accessLog("formattedList:" + formattedList.size());

            JsonObjectBuilder json = Json.createObjectBuilder();
            json.add("Results", jMetaArray);
            javax.json.JsonObject jsonObj = json.build();
            StringWriter writer = new StringWriter();

            PrintWriter pw = response.getWriter();
            Json.createWriter(writer).write(jsonObj);
            String resultStr = writer.toString();
            logMgr.accessLog("jsonResponse : " + resultStr);
            response.setContentType("application/Json;charset=utf-8\"");
            pw.write(resultStr);
            pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			logMgr.accessLog("Error in process request:" + e.getMessage());
			response.setContentType("text/html");
			response.getWriter().print(e.getMessage());
		}
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
	// + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request  servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException      if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
