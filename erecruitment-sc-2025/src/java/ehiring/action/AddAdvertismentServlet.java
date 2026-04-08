package ehiring.action;

import ehiring.dao.AdvertisementData;
import ehiring.dao.LogManager;
import ehiring.operation.AdvertisementOperation;
import ehiring.operation.StatusChangeOfAdv;
import in.gov.nrscisro.bndauth.BhoonidhiAuthManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class AddAdvertismentServlet extends HttpServlet {

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
	@SuppressWarnings("rawtypes")
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LogManager logMgr;
		String emailLogId = java.net.URLDecoder.decode(request.getHeader("user"));
		logMgr = LogManager.getInstance(emailLogId);
		
		logMgr.accessLog("Inside AddAdvertisment Servlet");
		
//		try {
//			boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
//			if (isMultiPart) {
//				logMgr.accessLog("In multipart code");
//			} else {
//				return;
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
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
			String[] keyVal = pair.split(":");
			logMgr.accessLog(keyVal[0] + "," + keyVal[1]);
			map1.put(keyVal[0].trim(), keyVal[1]);
		}

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
		} catch (Exception e) {
			e.printStackTrace();
		}

		logMgr.accessLog("Map values : " + map1);

		if (map1.get("action").equalsIgnoreCase("addAdvt")) {
			AdvertisementData advData = new AdvertisementData();

			Iterator<Entry<String, String>> hmIterator = map1.entrySet().iterator();

			String advInfo = new String();
			String advNo = "";

			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
			LocalDate localDate = LocalDate.now();
			Date dateNow = Date.valueOf(localDate);
			String advDate = df.format(new java.util.Date(dateNow.getTime()));

			java.text.SimpleDateFormat dftime = new java.text.SimpleDateFormat("dd-MMM-yyyy HH24:mm:ss");
			LocalDate localDateTime = LocalDate.now();
			Date dateNowTime = Date.valueOf(localDate);
			// String advDate = df.format(new java.util.Date(dateNow.getTime()));

			while (hmIterator.hasNext()) {
				Map.Entry mapElement = (Map.Entry) hmIterator.next();
				advInfo = (String) mapElement.getKey();
				logMgr.accessLog("THE STRING is  :" + advInfo);

				String mapValue = (String) map1.get(advInfo).trim();
				// logMgr.accessLog("date is ............:" + dateNow);
				advData.setAdvt_upload_date(dateNow);
				advData.setDate_of_status_update(dateNowTime);

				if (advInfo.equalsIgnoreCase("advtno")) {
					advNo = mapValue;
					advData.setAdvt_no(mapValue);
				}

				if (advInfo.equalsIgnoreCase("valid_from")) {
					System.out.println("DATE IS there....>"+mapValue);
					Date d1 = Date.valueOf(mapValue);
					advData.setValid_from(d1);
				}

				if (advInfo.equalsIgnoreCase("valid_till")) {
					Date d2 = Date.valueOf(mapValue);
					advData.setValid_till(d2);
				}

				if (advInfo.equalsIgnoreCase("remarks")) {
					String strTemp = mapValue;
					advData.setRemarks(strTemp.replace("+", " "));
				}

				if (advInfo.equalsIgnoreCase("ref_date")) {
					Date d1 = Date.valueOf(mapValue);
					advData.setRef_date(d1);
				}

			}

			String pattern = "yyyy-MM-dd";
			String dateInString = new SimpleDateFormat(pattern).format(new java.util.Date());
			String startval = new StatusChangeOfAdv(emailLogId).compareDates(dateInString, advData.getValid_from().toString());

			if (startval.equals("equal")) {
				advData.setStatus("active");
			} else
				advData.setStatus("inactive");

			String action = "error";
			String message = new String();
			AdvertisementOperation advOp = new AdvertisementOperation(emailLogId);
			try {
				logMgr.accessLog("Inside Add Advertisment Servlet try block.....");
				int retVal = advOp.addAdvt(advData);

				if (retVal > 0) {
					logMgr.accessLog("New Advertisement Successfully created");
					action = "success";
					message = "Advertisement No : " + advNo + " dated " + advDate
							+ " sucessfully added. To add posts under this advertisement, please click on the Add Post option under the Advertisement tab.";

				} else if (retVal == -2) {
					message = "Advertisement No : " + advNo
							+ " already exists. Details not added. Please select a different advertisement number";
				} else if (retVal == -3) {
					message = "Advertisement No : " + advNo
							+ " already exists. Details not added. Please select a different advertisement number";
				} else {
					logMgr.accessLog("Advertisement insertion exception");
					message = "Exception ocurred while adding Advertisement No : " + advNo
							+ ". Please contact website administrator";
				}
			} catch (SQLException ex) {
				Logger.getLogger(AddAdvertismentServlet.class.getName()).log(Level.SEVERE, null, ex);
			}

			try {
				JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
				System.out.println("action and message:"+action+","+message);
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
		} else if (map1.get("action").equalsIgnoreCase("editAdvt")) {
			AdvertisementData advData = new AdvertisementData();

			Iterator<Entry<String, String>> hmIterator = map1.entrySet().iterator();

			String advInfo = new String();
			String advNo = "";

			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
			LocalDate localDate = LocalDate.now();
			Date dateNow = Date.valueOf(localDate);
			String advDate = df.format(new java.util.Date(dateNow.getTime()));

			while (hmIterator.hasNext()) {
				Map.Entry mapElement = (Map.Entry) hmIterator.next();
				advInfo = (String) mapElement.getKey();
				logMgr.accessLog("THE STRING is  :" + advInfo);

				String mapValue = (String) map1.get(advInfo).trim();

				advData.setAdvt_upload_date(dateNow);

				if (advInfo.equalsIgnoreCase("advtno")) {
					advNo = mapValue;
					advData.setAdvt_no(mapValue);
				}

				if (advInfo.equalsIgnoreCase("valid_from")) {
					Date d1 = Date.valueOf(mapValue);
					advData.setValid_from(d1);
				}

				if (advInfo.equalsIgnoreCase("valid_till")) {
					Date d2 = Date.valueOf(mapValue);
					advData.setValid_till(d2);
				}

				if (advInfo.equalsIgnoreCase("remarks")) {
					String strTemp = mapValue;
					advData.setRemarks(strTemp.replace("+", " "));
				}
			}

			String message = new String();
			AdvertisementOperation advOp = new AdvertisementOperation(emailLogId);
			try {
				logMgr.accessLog("Inside Edit Advertisment Servlet try block.....");
				int retVal = advOp.editAdvt(advData);

				if (retVal > 0) {
					logMgr.accessLog("Advertisement Updation Successfull");
					message = "Advertisement No : " + advNo + " dated " + advDate + " sucessfully updated.";

				} else if (retVal == -2) {
					message = "Advertisement No : " + advNo
							+ " already exists. Details not added. Please select a different advertisement number";
				} else {
					logMgr.accessLog("Advertisement updation exception");
					message = "Exception ocurred while updating Advertisement No : " + advNo
							+ ". Please contact website administrator";
				}
			} catch (SQLException ex) {
				Logger.getLogger(AddAdvertismentServlet.class.getName()).log(Level.SEVERE, null, ex);
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
			AdvertisementData advData = new AdvertisementData();
			Iterator<Entry<String, String>> hmIterator = map1.entrySet().iterator();

			String advInfo = new String();
			String advNo = "";

			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
			LocalDate localDate = LocalDate.now();
			Date dateNow = Date.valueOf(localDate);
			String date_of_status_update = df.format(new java.util.Date(dateNow.getTime()));

			java.text.SimpleDateFormat dftime = new java.text.SimpleDateFormat("dd-MMM-yyyy HH24:mm:ss");
			LocalDate localDateTime = LocalDate.now();
			Date dateNowTime = Date.valueOf(localDateTime);
			dateNowTime = new Date(System.currentTimeMillis());

			while (hmIterator.hasNext()) {
				Map.Entry mapElement = (Map.Entry) hmIterator.next();
				advInfo = (String) mapElement.getKey();

				advData.setDate_of_status_update(dateNowTime);

				logMgr.accessLog("THE STRING is  :" + advInfo);
				String mapValue = (String) map1.get(advInfo).trim();

				if (advInfo.equalsIgnoreCase("advtno")) {
					advNo = mapValue;
					advData.setAdvt_no(mapValue);
				}
			}

			String action = "error";
			String message = new String();
			AdvertisementOperation advOp = new AdvertisementOperation(emailLogId);

			try {
				int cnt = advOp.getMaxDeleteCnt(advData);
				cnt++;
				logMgr.accessLog("Inside Delete Advertisment Servlet try block....." + cnt);

				advData.setStatus("delete" + cnt);
				int retVal = advOp.deleteAdvt(advData);

				if (retVal > 0) {
					action = "success";
					logMgr.accessLog("Advertisement Deletion Successfull");
					message = "Advertisement No : " + advNo + " sucessfully deleted on " + date_of_status_update;

				}else if(retVal==-99){
					message = "Advertisement No : " + advNo + " is active. So cannot be deleted !!!";
				}else if(retVal==-98){
					message = "Advertisement No : " + advNo + " has active posts. So cannot be deleted !!!";
				}
				else
				{
					logMgr.accessLog("Advertisement deletetion exception");
					message = "Exception ocurred while deleting Advertisement No : " + advNo
							+ ". Please contact website administrator";
				}
			} catch (Exception ex) {
				Logger.getLogger(AddAdvertismentServlet.class.getName()).log(Level.SEVERE, null, ex);
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
	public String getServletInfo

	() {
		return "Short description";
	}// </editor-fold>

}
