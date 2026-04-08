/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// ChangeId: 2023111401 New Servlet DownloadFileServlet
package ehiring.action;

import ehiring.properties.LoadProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Trainee
 */
public class DownloadFileServlet extends HttpServlet {
    private final int ARBITARY_SIZE = 1048;
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
        
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("Inside DownloadFileServlet ...");
        
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String postStr = br.readLine();
        System.out.println("DownloadFileServlet postStr:"+postStr);
        postStr = postStr.substring(postStr.indexOf("{") + 1, postStr.indexOf("}"));
        postStr = postStr.replaceAll("\"", "");
        Map<String, String> postMap = new LinkedHashMap<String, String>();
        String[] pairs = postStr.split(",");
        //System.out.println(pairs);
        for (int i = 0; i < pairs.length; i++) {
            System.out.println("Pair "+pairs[i]);
            String pair = pairs[i];
            String[] keyVal = pair.split(":");
            postMap.put(keyVal[0],keyVal[1]);
        }
        String filename = postMap.get("filename");
        String email = postMap.get("email");
        String ext = postMap.get("ext");
        
        System.out.println("DownloadFileServlet postMap:"+postMap);        
        
        response.setContentType("application/"+ext);
        response.setHeader("Content-disposition", "attachment; filename=sample.pdf");

        

        email = email.replace("@","_");
        email = email.replace(".","_");
        
        // Start: ChangeId: 2023111601
        String baseDirRecruitment = "";
        String baseDirAdmin = ""; // ChangeId: 2023120602
        LoadProperties lp = new LoadProperties();
        try {
            baseDirRecruitment = lp.getProperty("RECRUIT_FOLDER_DIR");
            baseDirAdmin = lp.getProperty("ADMIN_FOLDER_DIR"); // ChangeId: 2023120602
        } catch (Exception ex) {
            Logger.getLogger(DownloadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        // End: ChangeId: 2023111601
        
        // Start: ChangeId: 2023120602
        String filepath = "";
        if(filename.startsWith("#")){
            filename = filename.split("#")[1];
            filepath = baseDirAdmin;
        }
        else{
            filepath = baseDirRecruitment + File.separator + email + File.separator + postMap.get("advt_no") + File.separator + postMap.get("post_no");
        }
        // End: ChangeId: 2023120602
        
        File downloadFile = new File(filepath + File.separator + filename);
        System.out.println("DownloadFileServlet Filename: "+downloadFile);
        
        try(FileInputStream inStream = new FileInputStream(downloadFile);
            OutputStream outStream = response.getOutputStream()) { 

            byte[] buffer = new byte[ARBITARY_SIZE];
        
            int numBytesRead;
            while ((numBytesRead = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, numBytesRead);
            }
            outStream.close();
            inStream.close();
        }catch (Exception e) {
            System.out.println(e.getMessage());  
        }
        
        
        
        
        /*try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet DownloadFileServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DownloadFileServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
            out.close();
        }*/
        
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
