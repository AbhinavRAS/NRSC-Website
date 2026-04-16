<%@page import="java.sql.*" %>
<%@page import="java.net.*"%>
<%@page import="java.util.*" %>
<%@page import="java.io.*" %>
<%--@page import="com.webproject.model.service.CryptoService" --%>
<%@page import="javax.naming.*" %>

 
<%--<%@ page errorPage="ExceptionHandler.jsp" %> ChangeId: 2023111602--%>
<%@page import="java.util.Properties"%>


<%@page import="javax.servlet.*"%>
<%@page import="javax.servlet.http.*"%> 

<%@page import="java.security.*" %>
<%@page import="java.net.URLDecoder"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="javax.crypto.spec.IvParameterSpec"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>

<%@page import="sun.misc.BASE64Decoder"%>
<%@page import="sun.misc.BASE64Encoder"%>
<%@page import="java.text.*"%>

<%@page import="com.sbi.payagg.util.*"%>

<%@page import="ehiring.action.DBConnection" %>  
<%@page session="false" %> <%--ChangeId: 2023120707--%>



<html>
    <head>
        <meta charset="ISO-8859-1">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- <meta http-equiv="Content-Security-Policy" content="default-src 'unsafe-inline' 'self' http://localhost:8081; script-src 'self' 'unsafe-inline' 'unsafe-eval' "> -->
        <title>eRecruitment@NRSC/ISRO</title>

        <link rel="icon" href="images/isroLogo.jpg" type="image/gif"
              sizes="16x16">
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css">
        <link rel="stylesheet" type="text/css"
              href="vendor/animsition/css/animsition.min.css">
        <link rel="stylesheet" type="text/css"
              href="vendor/select2/select2.min.css">
        <link rel="stylesheet" type="text/css" href="css/util.css">
        <link rel="stylesheet" type="text/css" href="css/main.css">
        <link href="css/bootstrap-datetimepicker.min.css" rel="stylesheet"
              media="screen" />

        <script src="js/jquery.min.js"></script>
        <!-- <script src="js/jquery.js"></script> ChangeId: 2024011602 -->
        <!-- <script src="js/jquery_main.js"></script> -->
        <script src="js/bootstrap.min.js"></script> 
        <script src="js/main.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker.js"></script>
        <script src="vendor/animsition/js/animsition.min.js"></script>
        <script src="vendor/bootstrap/js/popper.js"></script>
        <script src="vendor/select2/select2.min.js"></script>
        <script src="vendor/daterangepicker/moment.min.js"></script>
        <script src="vendor/countdowntime/countdowntime.js"></script>
        <!--ChangeId: 2024011601 activateAccount and getAppType removed -->
        
    </head>
    <body>
        <div class="container">
            <input type="hidden" name="UPLOAD_DIR" id="UPLOAD_DIR"> 
            <input type="hidden" value="false" id="validFlag"> 
            <input type="hidden" name="email_id" id="email_id" value="">
            <input type="hidden" name="app_contact" id="app_contact"> 
            <input type="hidden" name="app_dob" id="app_dob"> 
            <input type="hidden" name="main_advt_no" value="NA" id="main_advt_no">
            <input type="hidden" name="main_post_no" value="NA" id="main_post_no">
            <div class="region region-header">
                <div id="block-logoblock"
                     class="block block-block-content block-block-content558b88c7-3bf2-4708-a259-db5a2953e7e0">
                    <div
                        class="clearfix text-formatted field field--name-body field--type-text-with-summary field--label-hidden field__item">
                        <header role="banner">
                            <img src="images/erecruit_header.jpg" alt="header"
                                 style="width: 100%;">
                        </header>
                    </div>
                </div>
            </div>

            <div class="row"
                 style="background-color: black; border-color: black; font-weight: bold; text-align: center; margin-bottom: 0px; min-height: 25px;">
                <div class="col-md-4"></div>
                <div class="col-md-4" id="header_strip">
                    <font color="#f3f7fd" style="font-size: 17px;">NRSC <font
                        color="#ffbebe">e</font>Recruitment Application
                    </font>
                </div>
                <div class="col-md-2"></div>

                <ul class="nav navbar-nav navbar-right"
                    style="font-weight: bold; margin-top: 0px; margin-right: 0px;">
                    <li class="dropdown"><a class="dropdown-toggle"
                                            data-toggle="dropdown" href="#" style="padding: 2px 15px;">Help<span
                                class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <!--ChangeId: 2023120705 rel="noopener noreferrer" added -->
                            <li><a href="#" rel="noopener noreferrer"
                                   onclick="downloadGeneralInstruction('#erecruitment_general_instructions.pdf')" 
                                   style="padding: 0px 0px;">General Instructions</a> <!--ChangeId: 2023120602--></li>
                        </ul></li>
                </ul>

            </div>
            <marquee>
                <font color="red"><b>This web application is only for
                    online applying against vacant post. Results will be intimated
                    through NRSC website nrsc.gov.in. For further updates, contact us.</b></font>
            </marquee>

            <!-- 		<nav class="navbar navbar-inverse" -->
            <!-- 			style="background-color: black; border-color: black; font-weight: bold; text-align: center; margin-bottom: 0px; min-height: 30px;" -->
            <!-- 			id="header_strip"> -->
            <!-- 			<a style="color: #f3f7fd;">Welcome to the e-Recruitment portal of -->
            <!-- 				NRSC</a> -->
            <!-- 		</nav> -->

            <noscript>
            <!-- 			<div style="border: 1px solid purple; padding: 50px"> -->
            <div style="border: 1px solid purple; margin-top: 50px;">
                <span style="color: red">JavaScript is disabled! For full
                    functionality, of this site it is necessary to enable JavaScript.
                    Go through <a href="https://enablejavascript.co/">https://enablejavascript.co/</a>
                    for how to enable JavaScript in your web browser
                </span>
            </div>
            </noscript>

            <div id="mainDiv" style="min-height: 83vh; background-color: #f3f7fd;">
                <!--  Login div starts -->
                <div class="row" style="margin-top: 0px;" id="div_login">
                    <div class="col-md-2 hidden-sm hidden-xs"></div>
                    <div class="col-md-8">
                        <div class="row">
                            
                            
                            
                            
<%@ page import = "java.io.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%@ page import="javax.naming.*" %>

<%@page import="java.util.Properties"%>

 
<%@page import="javax.servlet.*"%>
<%@page import="javax.servlet.http.*"%>
<%--@page import="com.webproject.model.service.CryptoService" --%>

<%@page import="com.sbi.payagg.util.*"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Date,java.text.SimpleDateFormat"%>
<%@page language = "java" contentType = "text/html; charset = ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    String loba_id = request.getParameter("loba_id");
    String loba_data = request.getParameter("loba_data");
    
    String lobaSecretKey="42813CF74357A7C0891D20D0A0B767AF";
    String loba_data_plain = "";
    
    try {
        // Decode the Base64 encrypted data
        byte[] encryptedBytes = Base64.getDecoder().decode(loba_data);

        // Prepare the key and cipher
        SecretKeySpec secretKey = new SecretKeySpec(lobaSecretKey.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Decrypt the data
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        loba_data_plain = new String(decryptedBytes, "UTF-8");

        //out.println("Decrypted Data: " + loba_data_plain);
    } catch (Exception e) {
        e.printStackTrace();
    }

    Map<String, String> response_data_map = new HashMap<>();
    String[] pairs = loba_data_plain.split("\\|");
    for (String pair : pairs) {
        String[] keyValue = pair.split("=", 2); // use limit=2 in case value has '='
        if (keyValue.length == 2) {
            response_data_map.put(keyValue[0], keyValue[1]);
        }
    }
    
    //String[] loba_data_arr = loba_data_plain.split("\\|");
    if( ! (response_data_map.get("loba_id").equals("NERECRUIT")) ){
        out.println("LOBA ID Mismatch...");
        return;
    }
    String loba_order_id = response_data_map.get("loba_order_id");
    String ntrp_order_status = response_data_map.get("ntrp_order_status").toUpperCase();
    String ntrp_txn_amount = response_data_map.get("ntrp_txn_amount");
    String ntrp_txn_ref = response_data_map.get("ntrp_txn_ref");
    String ntrp_txn_time = response_data_map.get("ntrp_txn_time");
    String loba_other_detail = response_data_map.get("loba_other_detail");
    
              
    // Start: ChangeId: 2023120101, 2023120501
    String[] others = loba_other_detail.split("\\^");
    String aid = "";
    //String retURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    String retURL = request.getScheme() + "://apps.nrsc.gov.in"  + ":" + request.getServerPort() + request.getContextPath();
    if(others != null && others.length>=3){
        aid = others[2];
        if(aid != ""){
            retURL = retURL+"?aid="+aid;
        }
    }
    // End: ChangeId: 2023120101
    String stsColor = "";
    String btnText = "";
    if(ntrp_order_status.equals("SUCCESS")){
        stsColor = "green";
        btnText = "Login and Submit the application";
    }
    else if(ntrp_order_status.equals("FAIL") || ntrp_order_status.equals("INCOMPLETE")){
        stsColor = "red";
        btnText = "Login and Retry payment";
    }
    else{
        stsColor = "red";
        btnText = "Login and Retry payment";
    }
%>
                           
        <h2 style="background-color: #f3f7fd;"><u>Payment Transaction details:</u></h2><br>

        <!-- <h3 ><b><u>Post:</u> Medical Officer</b></h3><br> -->
        <h3 style="background-color: #f3f7fd;text-align: left"><b><u>Payment Order Id:</u> <%=loba_order_id%></b></h3><br>
        <h3 style="background-color: #f3f7fd;text-align: left"><b><u>Fee Received:</u> INR <%=ntrp_txn_amount%>  </b></h3><br>
        <h3 style="background-color: #f3f7fd;text-align: left"><b><u>Transaction ID:</u>  <%=ntrp_txn_ref%></b></h3><br>
        <h3 style="background-color: #f3f7fd;text-align: left"><b><u>Transaction Status:</u> <font style="color:<%=stsColor%>;"><%=ntrp_order_status%></font>  </b></h3><br>
        <h3 style="background-color: #f3f7fd;text-align: left"><b><u>Transaction Date:</u> <%=ntrp_txn_time%> </b></h3><br>

					
<% 
    String decrypted= "" + loba_order_id + "|" + ntrp_order_status + "|" + ntrp_txn_amount + "|" + ntrp_txn_ref + "|" + ntrp_txn_time + "|" + loba_other_detail;
    //out.println(decrypted);
    int flag=new DBConnection().updateTrans(decrypted);
    if(flag==1)
    {
        //out.println("<br>Inserted into DB successfully. ");
    }else
    {
        //out.println("<br>Not Inserted into DB. ");
    }
    
    
%>
                            <a href=<%=retURL%> style="align-content: center;" > <!--ChangeId: 2023120101 form changed to anchor-->
                                <input type="Submit"  class="btn btn-warning"  value="<%=btnText%>"/>
                            </a>
                        </div>
                     
                    </div>
                    <div class="col-md-2 hidden-sm hidden-xs"></div>
                </div>
                <!--  Login div ends -->
            </div>

            <div class="row"
                 style="background-color: #3d7de0; border-color: #3d7de0; font-weight: bold; text-align: center; margin-bottom: 0px; min-height: 25px; margin-top: 0px;">
                <div class="col-md-5">
                    <font color="#f3f7fd">Contact: recruit[at]nrsc[dot]gov[dot]in</font> <!--ChangeId: 2024011803, 2025050702-->
                </div>
                <!-- <div class="col-md-1"></div> -->
                <div class="col-md-7">
                    <font color="#f3f7fd">Browser versions supported: Firefox
                    109+, Chrome 100+, Edge 88+, Opera 64+</font>
                </div>
            </div>

            <div class="row"
                 style="background-color: black; border-color: black; font-weight: bold; text-align: center; margin-bottom: 0px; min-height: 25px; margin-top: 0px;">
                <div class="col-md-3"></div>
                <div class="col-md-6">
                    <font color="#f3f7fd">Copyright &copy; National Remote
                    Sensing Centre, ISRO, DOS (2022)</font>
                </div>
                <div class="col-md-3"></div>
            </div>
        </div>
        
    </body>
</html>
