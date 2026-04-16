package ehiring.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ehiring.dao.LogManager;
import ehiring.operation.PostOperation;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Servlet implementation class OptionServlet
 */
@WebServlet("/LoadPostNoServlet")
public class LoadPostNoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LogManager logMgr;
		//String emailLogId =java.net.URLDecoder.decode(request.getHeader("user"));
		String emailLogId ="erecruit_admin@nrsc.gov.in";
		logMgr = LogManager.getInstance(emailLogId);
		logMgr.accessLog("IN LoadPostNoServlet");

		logMgr.accessLog("Load Post no Servlet Started");
		try {
			PrintWriter out = response.getWriter();
			String advt = request.getParameter("advt");

			ArrayList arr= new PostOperation(emailLogId).getPostNos(advt);
			if(arr.size()==0)
			{
				out.print("<option disabled>No Data Found</option>");
			}
			else
			{
				for(int i = 0 ; i < arr.size() ; ++i)
				{
					out.print("<option value='" + arr.get(i) + "'>" + arr.get(i) + "</option>");
				}
				
			}
			

		} catch (IOException | SQLException e) {
			e.printStackTrace();
			logMgr.accessLog("Error: " + e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}