//package ehiring.action;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.sql.SQLException;
//import java.util.ArrayList;
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
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import ehiring.dao.CategoryData;
//import ehiring.dao.PostData;
//import ehiring.operation.PostOperation;
//
///**
// * Servlet implementation class EditPostServlet
// */
//@WebServlet("/EditPostServlet")
//public class EditPostServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	/**
//	 * @see HttpServlet#HttpServlet()
//	 */
//	public EditPostServlet() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
//
//	@SuppressWarnings("rawtypes")
//	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		System.out.println("Inside Edit Post Servlet");
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
//
//			if (!(keyVal[0].equalsIgnoreCase("upload_adv") || keyVal[0].equalsIgnoreCase("btn"))) {
//				System.out.println("Printing each value:" + keyVal[0] + "," + keyVal[1]);
//				map1.put(keyVal[0].trim(), keyVal[1]);
//			}
//		}
//		System.out.println("Map values : " + map1);
//
//		PostData postData = new PostData();
//		CategoryData categorydata = new CategoryData();
//
//		Iterator<Entry<String, String>> hmIterator = map1.entrySet().iterator();
//		String postInfo = new String();
//
//		String postNo = "", advNo = "", postName = "";
//
//		while (hmIterator.hasNext()) {
//			Map.Entry mapElement = (Map.Entry) hmIterator.next();
//			postInfo = (String) mapElement.getKey();
//			System.out.println("THE STRING is  :" + postInfo);
//
//			String mapValue = (String) map1.get(postInfo).trim();
//
//			if (postInfo.equalsIgnoreCase("advt_no_drop_edit_ad")) {
//				advNo = mapValue;
//				postData.setAdvt_no(mapValue);
//			}
//			if (postInfo.equalsIgnoreCase("post_no_drop_edit")) {
//				postNo = mapValue;
//				postData.setPost_no(mapValue);
//			}
//			if (postInfo.equalsIgnoreCase("post_name_edit")) {
//				postName = mapValue;
//				postData.setPost_name(mapValue);
//			}
//			if (postInfo.equalsIgnoreCase("no_of_vacancies")) {
//				postData.setNo_of_vacancies(Integer.parseInt(mapValue));
//			}
//			if (postInfo.equalsIgnoreCase("eligibility_editPost")) {
//				postData.setEligibility(mapValue);
//			}
//			if (postInfo.equalsIgnoreCase("essential_qualification_editPost")) {
//				postData.setEssential_qualification(mapValue);
//			}
//			if (postInfo.equalsIgnoreCase("discipline_editPost")) {
//				postData.setDiscipline(mapValue);
//			}
//			if (postInfo.equalsIgnoreCase("specialization_editPost")) {
//				postData.setSpecialization(mapValue);
//			}
//			if (postInfo.equalsIgnoreCase("desirable_qualification_editPost")) {
//				postData.setDesirable_qualification(mapValue);
//			}
//			if (postInfo.equalsIgnoreCase("category")) {
//				String[] tempArr = mapValue.split("\\|");
//				HashMap<String, ArrayList> catMap = new HashMap<String, ArrayList>();
//				for (String tempStr : tempArr) {
//					String[] catStr = tempStr.split("#");
//					ArrayList<String> arr = new ArrayList<String>();
//					arr.add(catStr[1]);
//					arr.add(catStr[2]);
//					catMap.put(catStr[0], arr);
//				}
//				categorydata.setCategoryMap(catMap);
//			}
//		}
//
//		String message = new String();
//		PostOperation postOp = new PostOperation();
//		try {
//			System.out.println("Inside edit post servlet try block.....");
//			int retVal = postOp.editPost(postData, categorydata);
//
//			if (retVal > 0) {
//				System.out.println("Post Updation Successfull");
//				message = "Post No : " + postNo + " for the post : " + postName
//						+ "added successfully in Advertisement No : " + advNo;
//			}
////			else if (retVal == -2)
////			{
////				message = "Post No : " + postNo + " for the post : " + postName + "in Advertisement No : " + advNo
////						+ " already exists. Details not added. Please select a different post name or a different post number";
////			} 
//			else {
//				System.out.println("Advertisement insertion exception");
//				message = "Exception ocurred while adding Post No : " + postNo + " for the post : " + postName
//						+ "in Advertisement No : " + advNo + ". Please contact website administrator";
//			}
//		} catch (SQLException ex) {
//			Logger.getLogger(EditPostServlet.class.getName()).log(Level.SEVERE, null, ex);
//		}
//
//		try {
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
//		} catch (Exception e) {
//			System.out.println("Error in process request:" + e.getMessage());
//			response.setContentType("text/html");
//			response.getWriter().print(e.getMessage());
//		}
//	}
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
//	 *      response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
//	 *      response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		doGet(request, response);
//	}
//
//}
