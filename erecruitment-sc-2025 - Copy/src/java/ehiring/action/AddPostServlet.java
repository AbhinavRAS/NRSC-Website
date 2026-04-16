package ehiring.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ehiring.dao.CategoryData;
import ehiring.dao.LogManager;
import ehiring.dao.PostData;
import ehiring.operation.PostOperation;
import ehiring.operation.StatusChangeOfAdv;
import in.gov.nrscisro.bndauth.BhoonidhiAuthManager;

/**
 * Servlet implementation class AddPostServlet
 */
@WebServlet("/AddPostServlet")
public class AddPostServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	LogManager logMgr;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddPostServlet() {
		super();
	}

	@SuppressWarnings("rawtypes")
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String emailLogId = java.net.URLDecoder.decode(request.getHeader("user"));
		logMgr = LogManager.getInstance(emailLogId);
		logMgr.accessLog("AddPostServlet Started");
		
		// To read the inputs sent from front end --> values in map1

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
			logMgr.accessLog("Printing each name:" + keyVal[0] );
			logMgr.accessLog("Printing each value:" + keyVal[1] );
			
			logMgr.accessLog("Printing each value:" + keyVal[0] + "," + keyVal[1]);
			map1.put(keyVal[0].trim(), keyVal[1]);
		}
		logMgr.accessLog("Map values : " + map1);
		
		
		String vtoken = java.net.URLDecoder.decode(map1.get("vtoken").toString(), "UTF-8");
		try {
		BhoonidhiAuthManager bam = new BhoonidhiAuthManager();
		
		String valid = bam.validateBhoonidhiJWT(vtoken);
        if (valid == "NOT_VALID") {
            logMgr.accessLog("TOKEN NOT VALID");
			JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
			jMetaArray.add("invalid_session");
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
			return;
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		

		if (map1.get("action").equalsIgnoreCase("addPost")) {
			PostData postData = new PostData();
			CategoryData categorydata = new CategoryData();
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
			LocalDate localDate = LocalDate.now();
			Date dateNow = Date.valueOf(localDate);
			
			java.text.SimpleDateFormat dftime = new java.text.SimpleDateFormat("dd-MMM-yyyy HH24:mm:ss");
			LocalDate localDateTime = LocalDate.now();
			Date dateNowTime = Date.valueOf(localDateTime);
			// String advDate = df.format(new java.util.Date(dateNow.getTime()));

			String date_of_status_update = df.format(new java.util.Date(dateNow.getTime()));

			
			postData.setDate_of_status_update(dateNowTime);

			Iterator<Entry<String, String>> hmIterator = map1.entrySet().iterator();
			String postInfo = new String();

			String postNo = "", advNo = "", postName = "";
			while (hmIterator.hasNext()) {
				Map.Entry mapElement = (Map.Entry) hmIterator.next();
				postInfo = (String) mapElement.getKey();
				logMgr.accessLog("THE STRING is  :" + postInfo);

				String mapValue = (String) map1.get(postInfo).trim();
				
				logMgr.accessLog("THE STRING is  :" + postInfo+","+mapValue);

				if (postInfo.equalsIgnoreCase("advt_no_drop_add_post")) {
					advNo = mapValue;
					postData.setAdvt_no(mapValue);
				}
				if (postInfo.equalsIgnoreCase("post_name_addPost")) {
					postName = mapValue;
					postData.setPost_name(mapValue);
				}
				if (postInfo.equalsIgnoreCase("post_no")) {
					postNo = mapValue;
					postData.setPost_no(mapValue);
				}
				if (postInfo.equalsIgnoreCase("no_of_vacancies")) {
					postData.setNo_of_vacancies(Integer.parseInt(mapValue));
				}
				if (postInfo.equalsIgnoreCase("eligibility_details")) {
					postData.setElgDetails(mapValue);
				}
				if (postInfo.equalsIgnoreCase("desirable_qualification_addPost")) {
					postData.setDesirable_qualification(mapValue);
				}
				if (postInfo.equalsIgnoreCase("net")) {
					postData.setNet(mapValue);
				}
				if (postInfo.equalsIgnoreCase("post_exp")) {
					postData.setExperience(mapValue);
				}
				if (postInfo.equalsIgnoreCase("post_remarks")) {
					postData.setRemarks(mapValue);
				}
				if (postInfo.equalsIgnoreCase("post_sel")) {
					postData.setSelect_process(mapValue);
				}
				
				if (postInfo.equalsIgnoreCase("category")) {
					String[] tempArr = mapValue.split("\\|");
					LinkedHashMap<String, ArrayList> catMap = new LinkedHashMap<String, ArrayList>();
					for (String tempStr : tempArr) {
						String[] catStr = tempStr.split("#");
						ArrayList<String> arr = new ArrayList<String>();
						arr.add(catStr[1]);
						arr.add(catStr[2]);
						catMap.put(catStr[0], arr);
					}
					categorydata.setCategoryMap(catMap);
				}
			}
			
		
			String action = "error";
			String message = new String();
			
			try {
				PostOperation postOp = new PostOperation(emailLogId);
				String status_advt =postOp.getStatusOfAdvt(advNo);
				postData.setStatus(status_advt);
				categorydata.setStatus(status_advt);

				logMgr.accessLog("Inside add post servlet try block.....");
				int retVal = postOp.addPost(postData, categorydata);
				if (retVal > 0) {
					action = "success";
					logMgr.accessLog("New Post Successfully created");
					message = "Post No : " + postNo + " for the post : " + postName
							+ " added successfully in Advertisement No : " + advNo;
					// response.sendRedirect("administrator_profile.html");
				} else if (retVal == -1 || retVal == 0) {
					message = "Post No : " + postNo + " for the post : " + postName + "in Advertisement No : " + advNo
							+ " with given eligibiltiy already exists. Details not added. Please select a different post name or a different post number";
				} else {
					logMgr.accessLog("Advertisement insertion exception");
					message = "Exception ocurred while adding Post No : " + postNo + " for the post : " + postName
							+ "in Advertisement No : " + advNo + ". Please contact website administrator";
					PrintWriter out = response.getWriter();
					out.print("<h1 style='text-align:center;'>Exception in adding Post</h1>");
					request.getRequestDispatcher("administrator_profile.html").include(request, response);
				}
			} catch (SQLException ex) {
				Logger.getLogger(AddPostServlet.class.getName()).log(Level.SEVERE, null, ex);
			}

			try {
				JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
				jMetaArray.add(action);
				jMetaArray.add(message);

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
				logMgr.accessLog("Error in process request:" + e.getMessage());
				response.setContentType("text/html");
				response.getWriter().print(e.getMessage());
			}
		} else if (map1.get("action").equalsIgnoreCase("editPost")) {
			PostData postData = new PostData();
			CategoryData categorydata = new CategoryData();

			Iterator<Entry<String, String>> hmIterator = map1.entrySet().iterator();
			String postInfo = new String();

			String postNo = "", advNo = "", postName = "";

			while (hmIterator.hasNext()) {
				Map.Entry mapElement = (Map.Entry) hmIterator.next();
				postInfo = (String) mapElement.getKey();
				logMgr.accessLog("THE STRING is  :" + postInfo);

				String mapValue = (String) map1.get(postInfo).trim();

				if (postInfo.equalsIgnoreCase("advt_no_drop_edit_ad")) {
					advNo = mapValue;
					postData.setAdvt_no(mapValue);
				}
				if (postInfo.equalsIgnoreCase("post_no_drop_edit")) {
					postNo = mapValue;
					postData.setPost_no(mapValue);
				}
				if (postInfo.equalsIgnoreCase("post_name_edit")) {
					postName = mapValue;
					postData.setPost_name(mapValue);
				}
				if (postInfo.equalsIgnoreCase("no_of_vacancies")) {
					postData.setNo_of_vacancies(Integer.parseInt(mapValue));
				}
				if (postInfo.equalsIgnoreCase("eligibility_editPost")) {
					postData.setEligibility(mapValue);
				}
				if (postInfo.equalsIgnoreCase("essential_qualification_editPost")) {
					postData.setEssential_qualification(mapValue);
				}
				if (postInfo.equalsIgnoreCase("discipline_editPost")) {
					postData.setDiscipline(mapValue);
				}
				if (postInfo.equalsIgnoreCase("specialization_editPost")) {
					postData.setSpecialization(mapValue);
				}
				if (postInfo.equalsIgnoreCase("desirable_qualification_editPost")) {
					postData.setDesirable_qualification(mapValue);
				}
				if (postInfo.equalsIgnoreCase("category")) {
					String[] tempArr = mapValue.split("\\|");
					LinkedHashMap<String, ArrayList> catMap = new LinkedHashMap<String, ArrayList>();
					for (String tempStr : tempArr) {
						String[] catStr = tempStr.split("#");
						ArrayList<String> arr = new ArrayList<String>();
						arr.add(catStr[1]);
						arr.add(catStr[2]);
						catMap.put(catStr[0], arr);
					}
					categorydata.setCategoryMap(catMap);
				}
			}

			String message = new String();
			PostOperation postOp = new PostOperation(emailLogId);
			try {
				logMgr.accessLog("Inside edit post servlet try block.....");
				int retVal = postOp.editPost(postData, categorydata);

				if (retVal > 0) {
					logMgr.accessLog("Post Updation Successfull");
					message = "Post No : " + postNo + " successfully updated in Advertisement No : " + advNo;
				} else {
					logMgr.accessLog("Post updation exception");
					message = "Exception ocurred while updating Post No : " + postNo + " for the post : " + postName
							+ "in Advertisement No : " + advNo + ". Please contact website administrator";
				}
			} catch (SQLException ex) {
				Logger.getLogger(AddPostServlet.class.getName()).log(Level.SEVERE, null, ex);
			}

			try {
				JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
				jMetaArray.add(message);

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
				logMgr.accessLog("Error in process request:" + e.getMessage());
				response.setContentType("text/html");
				response.getWriter().print(e.getMessage());
			}

		} else {
			PostData postData = new PostData();
			Iterator<Entry<String, String>> hmIterator = map1.entrySet().iterator();

			String advInfo = new String();
			String advNo = "", postNo = "";

			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
			LocalDate localDate = LocalDate.now();
			Date dateNow = Date.valueOf(localDate);
			
			java.text.SimpleDateFormat dftime = new java.text.SimpleDateFormat("dd-MMM-yyyy HH24:mm:ss");
			LocalDate localDateTime = LocalDate.now();
			Date dateNowTime = Date.valueOf(localDate);
			// String advDate = df.format(new java.util.Date(dateNow.getTime()));

			String date_of_status_update = df.format(new java.util.Date(dateNow.getTime()));

			while (hmIterator.hasNext()) {
				Map.Entry mapElement = (Map.Entry) hmIterator.next();
				advInfo = (String) mapElement.getKey();

				//postData.setStatus("delete");
				postData.setDate_of_status_update(dateNowTime);

				logMgr.accessLog("THE STRING is  :" + advInfo);
				String mapValue = (String) map1.get(advInfo).trim();

				if (advInfo.equalsIgnoreCase("advt_no_drop_delete_ad_post")) {
					advNo = mapValue;
					postData.setAdvt_no(mapValue);
				}
				if (advInfo.equalsIgnoreCase("post_no_drop_delete")) {
					postNo = mapValue;
					postData.setPost_no(mapValue);
				}
			}

			String message = new String();
			PostOperation postOp = new PostOperation(emailLogId);
			try {
				int cnt = postOp.getMaxDeleteCnt(postData);
				cnt++;
				postData.setStatus("delete"+cnt);
				logMgr.accessLog("Inside Delete Post Servlet try block.....");
				
				int retVal = postOp.deletePost(postData);

				if (retVal > 0) {
					logMgr.accessLog("Post Deletion Successfull");
					message = "Post No. " + postNo + " sucessfully deleted from Advertisement No : " + advNo + " on "
							+ date_of_status_update;

				}else if(retVal==-99){
					message = "In Advertisement No : " + advNo + " the Post No:"+ postNo+" is active. So cannot be deleted !!!";
				} 
				else {
					logMgr.accessLog("Post deletetion exception");
					message = "Exception ocurred while deleting Post No : " + postNo + " from Advertisement No : "
							+ advNo + ". Please contact website administrator";
				}
			} catch (Exception ex) {
				Logger.getLogger(AddPostServlet.class.getName()).log(Level.SEVERE, null, ex);
			}

			try {
				JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
				jMetaArray.add(message);

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
				logMgr.accessLog("Error in process request:" + e.getMessage());
				response.setContentType("text/html");
				response.getWriter().print(e.getMessage());
			}
		}
	}

	// BufferedReader br = new BufferedReader(new
	// InputStreamReader(request.getInputStream()));
	// String postStr = "";
	//
	// if (br != null) {
	// postStr = br.readLine();
	// }
	// logMgr.accessLog("Value post :" + postStr + ":");
	// if (postStr.contains("{") && postStr.contains("}"))
	// logMgr.accessLog(CurrentDateTime.dateTime()+":"+postStr + "contains {}");
	// postStr = postStr.substring(postStr.indexOf("{") + 1, postStr.indexOf("}"));
	// logMgr.accessLog(CurrentDateTime.dateTime()+":"+postStr);
	// postStr = postStr.replaceAll("\"", " ");
	//
	// logMgr.accessLog(CurrentDateTime.dateTime()+":"+postStr);
	// logMgr.accessLog(CurrentDateTime.dateTime()+":"+postStr.split("&").length);
	//
	// Map<String, String> map1 = new LinkedHashMap<>();
	// String[] pairs = postStr.split("&");
	//
	// for (String pair : pairs) {
	// String[] keyVal = pair.split("=");
	// if (!(keyVal[0].equalsIgnoreCase("upload_adv") ||
	// keyVal[0].equalsIgnoreCase("btn"))) {
	// logMgr.accessLog(CurrentDateTime.dateTime()+":"+keyVal[0] + "," + keyVal[1]);
	// map1.put(keyVal[0].trim(), keyVal[1]);
	// }
	//
	// }
	//

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}
}
