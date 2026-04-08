//package ehiring.action;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.sql.Date;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.json.Json;
//import javax.json.JsonArrayBuilder;
//import javax.json.JsonObjectBuilder;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import ehiring.dao.AdvertisementData;
//import ehiring.operation.AdvertisementOperation;
//
///**
// * Servlet implementation class EditAdvertisementServlet
// */
////@WebServlet("/EditAdvertisementServlet")
//public class EditAdvertisementServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//    /**
//     * @see HttpServlet#HttpServlet()
//     */
//    public EditAdvertisementServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }
//
//	@SuppressWarnings("rawtypes")
//	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		System.out.println("Inside AddAdvertismentServlet Servlet");
//		// To read the inputs sent from front end --> values in map1
//
//		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		String postStr = "";
//
//		if (br != null) {
//			postStr = br.readLine();
//		}
//		System.out.println("Value post  :" + postStr + ":");
//		postStr = postStr.substring(postStr.indexOf("{") + 1, postStr.indexOf("}"));
//		postStr = postStr.replaceAll("\"", "");
//
//		Map<String, String> map1 = new HashMap<String, String>();
//		String[] pairs = postStr.split(",");
//		for (int i = 0; i < pairs.length; i++) {
//			String pair = pairs[i];
//			String[] keyVal = pair.split(":");
//			if (!(keyVal[0].equalsIgnoreCase("upload_adv") || keyVal[0].equalsIgnoreCase("btn"))) 
//			{
//				System.out.println(keyVal[0] + "," + keyVal[1]);
//				map1.put(keyVal[0].trim(), keyVal[1]);
//			}
//		}
//		System.out.println("Map values : " + map1);
//
//		AdvertisementData advData = new AdvertisementData();
//
//		Iterator<Entry<String, String>> hmIterator = map1.entrySet().iterator();
//
//		String advInfo = new String();
//		String advNo = "";
//
//		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MMM-yyyy");
//		LocalDate localDate = LocalDate.now();
//		Date dateNow = Date.valueOf(localDate);
//		String advDate = df.format(new java.util.Date(dateNow.getTime()));
//
//		while (hmIterator.hasNext())
//		{
//			Map.Entry mapElement = (Map.Entry) hmIterator.next();
//			advInfo = (String) mapElement.getKey();
//			System.out.println("THE STRING is  :" + advInfo);
//
//			String mapValue = (String) map1.get(advInfo).trim();
//
//			advData.setAdvt_upload_date(dateNow);
//			
//
//			if (advInfo.equalsIgnoreCase("advtno")) {
//				advNo = mapValue;
//				advData.setAdvt_no(mapValue);
//			}
//
//			if (advInfo.equalsIgnoreCase("valid_from")) {
//				Date d1 = Date.valueOf(mapValue);
//				advData.setValid_from(d1);
//			}
//
//			if (advInfo.equalsIgnoreCase("valid_till")) {
//				Date d2 = Date.valueOf(mapValue);
//				advData.setValid_till(d2);
//			}
//
//			if (advInfo.equalsIgnoreCase("remarks")) {
//				String strTemp = mapValue;
//				advData.setRemarks(strTemp.replace("+", " "));
//			}
//		}
//
//		String message = new String();
//		AdvertisementOperation advOp = new AdvertisementOperation();
//		try 
//		{
//			System.out.println("Inside Edit Advertisment Servlet try block.....");
//			int retVal = advOp.editAdvt(advData);
//			
//			if (retVal > 0) 
//			{
//				System.out.println("Advertisement Updation Successfull");
//				message = "Advertisement No : " + advNo + " dated " + advDate
//						+ " sucessfully added. To add posts under this advertisement, please click on the Add Post option under the Advertisement tab!!";
//
//			}
//			else if (retVal == -2) 
//			{
//				message = "Advertisement No : " + advNo
//						+ " already exists. Details not added. Please select a different advertisement number";
//			}
//			else
//			{
//				System.out.println("Advertisement insertion exception");
//				message = "Exception ocurred while adding Advertisement No : " + advNo
//						+ ". Please contact website administrator";
//			}
//		} 
//		catch (SQLException ex)
//		{
//			Logger.getLogger(EditAdvertisementServlet.class.getName()).log(Level.SEVERE, null, ex);
//		}
//
//		try 
//		{
//			JsonArrayBuilder jMetaArray = Json.createArrayBuilder();
//			jMetaArray.add(message);
//
//			JsonObjectBuilder json = Json.createObjectBuilder();
//			json.add("Results", jMetaArray);
//			javax.json.JsonObject jsonObj = json.build();
//			StringWriter writer = new StringWriter();
//
//			PrintWriter pw = response.getWriter();
//			Json.createWriter(writer).write(jsonObj);
//			String resultStr = writer.toString();
//			System.out.println("jsonResponse : " + resultStr);
//			response.setContentType("application/Json;charset=utf-8\"");
//			pw.write(resultStr);
//			pw.close();
//
//		} 
//		catch (Exception e)
//		{
//			System.out.println("Error in process request:" + e.getMessage());
//			response.setContentType("text/html");
//			response.getWriter().print(e.getMessage());
//		}
//	}
//    
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}
//
//}
