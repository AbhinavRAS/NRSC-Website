package ehiring.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ehiring.dao.LogManager;
import ehiring.operation.StateDistrictOperation;

/**
 * Servlet implementation class TestServlet
 */
@WebServlet("/LoadUniversity")
public class LoadUniversity extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoadUniversity() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		 LogManager logMgr;
			String emailLogId =java.net.URLDecoder.decode(request.getHeader("user"));
			logMgr = LogManager.getInstance(emailLogId);
		
		logMgr.accessLog("Inside University");  

		try {
			logMgr.accessLog("Loading University options are ");
			PrintWriter out = response.getWriter();
			ArrayList formattedList = new ArrayList();
			
			formattedList = new StateDistrictOperation(emailLogId).loadUniversity();
			//logMgr.accessLog("inside try of load University name");
			//logMgr.accessLog("formattedList: " + formattedList + "size : " + formattedList.size());
			JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
			for (int j = 0; j < formattedList.size(); j++) {
				jMetaArray.add((String)formattedList.get(j));
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
			logMgr.accessLog("Error: " + e.getMessage());
		}
	}
	
}