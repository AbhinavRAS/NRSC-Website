package ehiring.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import in.gov.nrscisro.bndauth.BhoonidhiAuthManager;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter("/AuthenticationFilter")
public class AuthenticationFilter implements Filter {

	/**
	 * Default constructor.
	 */
	public AuthenticationFilter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// HttpSession session = req.getSession(false);

		String ipAddress = req.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		System.out.println(CurrentDateTime.dateTime()+":"+"ipAddress.... : " + ipAddress);

		String uri = req.getRequestURI();
		String fileName = uri.replaceAll("/eRecruitment_NRSC", "");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String postStr = "";
		if (br != null) {
			postStr = br.readLine();
		}
		System.out.println(CurrentDateTime.dateTime()+":"+"Value post in filter :" + postStr + ":");
//		if (postStr != null) {
//			postStr = postStr.substring(postStr.indexOf("{") + 1, postStr.indexOf("}"));
//			postStr = postStr.replaceAll("\"", "");
//
//			Map<String, String> map1 = new LinkedHashMap<String, String>();
//			String[] pairs = postStr.split(",");
//			for (int i = 0; i < pairs.length; i++) {
//				String pair = pairs[i];
//				System.out.println(CurrentDateTime.dateTime()+":"+"pair is:" + pair);
//				String[] keyVal = pair.split(":");
//				System.out.println(CurrentDateTime.dateTime()+":"+keyVal[0] + "," + keyVal[1]);
//				map1.put(keyVal[0].trim(), keyVal[1]);
//			}
//			System.out.println(CurrentDateTime.dateTime()+":"+"Map values : " + map1);
//		}
		// HttpSession session = req.getSession(false);
		// System.out.println(CurrentDateTime.dateTime()+":"+"URI IS .....>" + uri + " session is :" + session);

//		if (session == null && !(uri.endsWith("html") || uri.endsWith("LoginServlet"))) {
//		if (session == null) {
		if (postStr == null) {
			System.out.println(CurrentDateTime.dateTime()+":"+"Init call");
			javax.servlet.RequestDispatcher rd = req.getRequestDispatcher(fileName);
			rd.include(req, res);
		} else {
			boolean jwtFlag = false;
			postStr = postStr.substring(postStr.indexOf("{") + 1, postStr.indexOf("}"));
			postStr = postStr.replaceAll("\"", "");

			Map<String, String> map1 = new LinkedHashMap<String, String>();
			String[] pairs = postStr.split(",");
			for (int i = 0; i < pairs.length; i++) {
				String pair = pairs[i];
				System.out.println(CurrentDateTime.dateTime()+":"+"pair is:" + pair);
				String[] keyVal = pair.split(":");
				System.out.println(CurrentDateTime.dateTime()+":"+keyVal[0] + "," + keyVal[1]);
				map1.put(keyVal[0].trim(), keyVal[1]);
			}
			System.out.println(CurrentDateTime.dateTime()+":"+"Map values : " + map1);
			String action = map1.get("action").toString();

			if (action.equals("sendotp")) {
				jwtFlag = true;
			} else if (map1.get("jwtToken") == null) {
				System.out.println(CurrentDateTime.dateTime()+":"+"Token not present, cannot continue. Returning.");
				jwtFlag = false;
			} else {
				String jwt = map1.get("jwtToken").toString();
				System.out.println(CurrentDateTime.dateTime()+":"+"doFilter,,,,,,,,,,,,,,,,,,,,,," + jwt);
				String userId = map1.get("userId").toString();
				jwt = validateSession(userId, ipAddress);
				if (jwt.equalsIgnoreCase("EXCEPTON")) {
					System.out.println(CurrentDateTime.dateTime()+":"+"Invalid token, cannot continue. Returning.");
					jwtFlag = false;
					String msg = "Error occurred. Please try again.";
				}
			}
			if (!jwtFlag) {
				javax.servlet.RequestDispatcher rd = req.getRequestDispatcher("/index.html");
				rd.include(req, res);
			} else {
				System.out.println(CurrentDateTime.dateTime()+":"+"Proceeding 2");
				javax.servlet.RequestDispatcher rd = req.getRequestDispatcher(fileName);
				rd.forward(request, response);
//				// pass the request along the filter chain
//				if (req.getHeader("x-requested-with") != null
//						&& req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
//					System.out.println(CurrentDateTime.dateTime()+":"+"ajax request forwarding");
//				} else {
//					chain.doFilter(req, res);
//				}

			}
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	String validateSession(String userId, String ipAddress) {
		try {
			BhoonidhiAuthManager bam = new BhoonidhiAuthManager();
			String jws = bam.startSession(userId, ipAddress);
			return jws;
		} catch (Exception e) {
			e.printStackTrace();
			return "EXCEPTION";
		}
	}

}
