package ehiring.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ehiring.dao.LogManager;
import ehiring.operation.StateDistrictOperation;

/**
 * Servlet implementation class LoadDistrictName
 */
@WebServlet("/LoadStates")
public class LoadStates extends HttpServlet {
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

		logMgr.accessLog("IN Load State Servlet");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String postStr = "";

		if (br != null) {
			postStr = br.readLine();
		}
		// logMgr.accessLog("Value post :" + postStr + ":");
		postStr = postStr.substring(postStr.indexOf("{") + 1, postStr.indexOf("}"));
		postStr = postStr.replaceAll("\"", "");

		Map<String, String> map1 = new HashMap<String, String>();
		String[] pairs = postStr.split(",");
		for (int i = 0; i < pairs.length; i++) {
			String pair = pairs[i];
			String[] keyVal = pair.split(":");
			logMgr.accessLog(keyVal[0] + "," + keyVal[1]);
			map1.put(keyVal[0].trim(), keyVal[1]);
		}

		logMgr.accessLog("Map values : " + map1);

		ArrayList<String> formattedList = new StateDistrictOperation(emailLogId).loadStateNameOptions();

		try {
			logMgr.accessLog("inside try of load state name");
			// logMgr.accessLog("formattedList: " + formattedList + "size : " +
			// formattedList.size());
			JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
			for (int j = 0; j < formattedList.size(); j++) {
				jMetaArray.add(formattedList.get(j));
			}

			JsonObjectBuilder json = Json.createObjectBuilder();
			json.add("Results", jMetaArray);
			javax.json.JsonObject jsonObj = json.build();
			StringWriter writer = new StringWriter();

			PrintWriter pw = response.getWriter();
			Json.createWriter(writer).write(jsonObj);
			String resultStr = writer.toString();
			// logMgr.accessLog("jsonResponse : " + resultStr);
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