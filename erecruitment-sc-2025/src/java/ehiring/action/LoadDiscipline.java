/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehiring.action;

import ehiring.dao.LogManager;
import ehiring.operation.DisciplineOperation;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoadDiscipline extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	LogManager logMgr;

	/**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String emailLogId = java.net.URLDecoder.decode(request.getHeader("user"));
		logMgr = LogManager.getInstance(emailLogId);
		logMgr.accessLog("LoadDiscipline Servlet Started");

        logMgr.accessLog("IN LoadDiscipline Servlet");

        ArrayList<String> formattedList = new DisciplineOperation(emailLogId).loadDisciplineOptions();

        try {
//            logMgr.accessLog("formattedList:" + formattedList.size() + ": " + formattedList);
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
            //logMgr.accessLog("jsonResponse : " + resultStr);
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
