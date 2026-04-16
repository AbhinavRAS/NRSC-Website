<%@page import="java.sql.*" %>
<%@page import="java.net.*"%>
<%@page import="java.util.*" %>
<%@page import="java.io.*" %>
<%--@page import="com.webproject.model.service.CryptoService" --%>
<%@page import="javax.naming.*" %>

 
<%-- <%@ page errorPage="ExceptionHandler.jsp" %> ChangeId: 2023111602--%>
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
                        <div class="row" style="align-content: center;">
                            
                            
                            
                            
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
<%@ page language = "java" contentType = "text/html; charset = ISO-8859-1"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String encdata = request.getParameter("encData");
	String merchant_code = request.getParameter("merchIdVal");
	//out.println(encdata+"<br>");
    //out.println(merchant_code+"<br>");
    String decrypted="";
    //For Testing Purpose
	//String key="nVg5ssL9Zufvd91lnZhzfTCM/prscXT6SZ7cuUUl3Is=";   
	String key="sxZVFwkgcFu8mW0rhKVlg4CzGJ1vvB07Aa/PFPkGT7I=";

	 
	     //testing
		decrypted   = AES256Bit.decrypt(encdata,  AES256Bit.readKeyBytes(key));
		//out.println("<br>encdata: "+encdata);
	//out.println("<br>decrypted: "+decrypted);
	
			//decrypted   = AES128Bit.decrypt(encdata, Key);
		//	decrypted   = AES256Bit.decrypt(encdata,  AES256Bit.readKeyBytes(key));
		//	out.println(decrypted);
			//out.println("<br>encdata:"+encdata);
			//out.println("<br>merchant_code:"+merchant_code);
//out.println("<br>");
			
			String titlename = "";
			String title = "";
			String fname = "";
			String emails = "";
			String ref3_received = "NA";
			String ref4_received = "NA";
			String ref5_received = "NA";
			String ref6_received = "NA";
			String ref7_received = "NA";
			String ref8_received = "NA";
			String ref9_received = "NA";

			String [] splitParts = decrypted.split("\\|");

			
			String unicid_received = splitParts[0] ;
			unicid_received = unicid_received.trim();
			
			String sbi_ref_no_received = splitParts[1] ;
			sbi_ref_no_received = sbi_ref_no_received.trim();
			
			String status_received = splitParts[2] ;
			status_received = status_received.trim();
			
			String fee_received = splitParts[3] ;
			if(fee_received.equals("NA")){
			fee_received="0";
			}
			fee_received = fee_received.trim();
			
			String currency_received = splitParts[4] ;
			currency_received = currency_received.trim();
			
			String pmode_received = splitParts[5] ;
			pmode_received = pmode_received.trim();
			
			String other_received = splitParts[6] ;
			other_received = other_received.trim();
                        // Start: ChangeId: 2023120101, 2023120501
                        String[] others = other_received.split("\\^");
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
			
			String reason_received = splitParts[7] ;
			reason_received = reason_received.trim();
			
			String bankcode_received = splitParts[8] ;
			bankcode_received = bankcode_received.trim();

			String banrefno_received = splitParts[9] ;
			banrefno_received = banrefno_received.trim();

			String trans_date_received = splitParts[10] ;
			trans_date_received = trans_date_received.trim();

			String country_received = splitParts[11] ;
			country_received = country_received.trim();

			String cin_received = splitParts[12] ;
			cin_received = cin_received.trim();

			String ref1_received = splitParts[13] ;
			ref1_received = ref1_received.trim();

			String ref2_received = splitParts[14] ;
			ref2_received = ref2_received.trim();

			

%>
                            
                            
                            
                            
                            
                            
                     <h2 style="background-color: #f3f7fd;"><u>Payment Transaction details:</u></h2><br>
                      
 
                        
						
						 <!-- <h3 ><b><u>Post:</u> Medical Officer</b></h3><br> -->
						  <h3 style="background-color: #f3f7fd;text-align: left"><b><u>Payment Order Id:</u> <%=unicid_received%></b></h3><br>
						   <h3 style="background-color: #f3f7fd;text-align: left"><b><u>Fee :</u> <%=currency_received+"."%>    <%=fee_received%></b></h3><br>
						   <h3 style="background-color: #f3f7fd;text-align: left"><b><u>Transaction ID:</u>  <%=sbi_ref_no_received%></b></h3><br>
						
						   <h3 style="background-color: #f3f7fd;text-align: left"><b><u>Transaction Status:</u> <font style="color:red;"><%=status_received%></font>  </b></h3><br>
						  <h3 style="background-color: #f3f7fd;text-align: left"><b><u>Transaction Date:</u>  <%=trans_date_received%></b></h3><br>
						   
					
                        <% 
                           
                            
                           //String decrypted= "15345AABC479ECA1R|5718539555712|SUCCESS|750|INR|NB|NA|Y|SBIT|86340|2023-07-28 14:13:28|IN|null|1000112|0.00^0.00|||||||||";
                           
                           int flag=new DBConnection().updateTrans(decrypted);
                            
                            if(flag==1)
                            {
                            	
                            	//out.println("<br>Inserted into DB successfully. ");
                            	
                            }else
                            	//out.println("<br>Not Inserted into DB. ");
                            
                            
                            %>

                     	
 
		 
                            <a href=<%=retURL%> style="align-content: center;" > <!--ChangeId: 2023120101 form changed to anchor-->
                                <input type="Submit"  class="btn btn-warning"  value="Login and Retry Payment"/>
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
