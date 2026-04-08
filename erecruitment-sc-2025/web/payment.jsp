<!DOCTYPE html>
<%@page import="java.sql.*" %>
<%@page import="java.net.*"%>
<%@page import="java.util.*" %>
<%@page import="java.io.*" %>
<%--@page import="com.webproject.model.service.CryptoService" --%>
<%@page import="javax.naming.*" %>

 
<%--<%@ page errorPage="ExceptionHandler.jsp" %> ChangeId: 2023111602--%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.regex.Pattern"%> <%--ChangeId: 2023111603--%>
<%@page import="java.util.regex.Matcher"%> <%--ChangeId: 2023111603--%>

<%@page import="javax.servlet.*"%>
<%@page import="javax.servlet.http.*"%> 

<%@page import="java.security.*" %>
<%@page import="java.net.URLDecoder"%>
<%@page import="javax.crypto.Cipher"%>
<%@page import="javax.crypto.spec.IvParameterSpec"%>
<%@page import="javax.crypto.spec.SecretKeySpec"%>
<%@page import="java.util.Base64"%>

<%@page import="sun.misc.BASE64Decoder"%>
<%@page import="sun.misc.BASE64Encoder"%>
<%@page import="java.text.*"%>

<%@page import="com.sbi.payagg.util.*"%>
<%@page import="ehiring.action.DBConnection" %> 
<%@page session="false" %> <%--ChangeId: 2023120707--%>
<html lang="en">
<head>
    <!-- Basic -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">   
   
    <!-- Mobile Metas -->
    <meta name="viewport" content="width=device-width, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
 
     <!-- Site Metas -->
    <title>NRSC epayment Gateway</title>  
    <meta name="keywords" content="">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Site Icons -->
    <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
    <link rel="apple-touch-icon" href="images/apple-touch-icon.png">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <!-- Site CSS -->
    <link rel="stylesheet" href="css/style.css">
    <!-- Responsive CSS -->
    <link rel="stylesheet" href="css/responsive.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="css/custom.css">

    <!-- Modernizer for Portfolio -->
    <script src="js/modernizer.js"></script>

    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
<body styel="display:none">

    <!-- LOADER -->
    <div id="preloader">
        <div class="loader">
			<div class="loader__bar"></div>
			<div class="loader__bar"></div>
			<div class="loader__bar"></div>
			<div class="loader__bar"></div>
			<div class="loader__bar"></div>
			<div class="loader__ball"></div>
		</div>
    </div><!-- end loader -->
    <!-- END LOADER -->
    


<%
String loba_id = "NERECRUIT";
String loba_order_id = "";
String loba_order_amount="2";

String advt_no=request.getParameter("pay_advt_no_gateway"); //ChangeId: 2023120102
String post_no=request.getParameter("pay_post_no_gateway"); //ChangeId: 2023120102
String email_id=request.getParameter("pay_email_gateway"); //ChangeId: 2024030102
String aid=request.getParameter("aid_gateway"); //ChangeId: 2023120101
String loba_other_details = advt_no +"^"+post_no+"^"+aid; //ChangeId: 2023120102


// Start: ChangeId: 2023120501
//String app_url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//String app_url = request.getScheme() + "://apps.nrsc.gov.in"  + ":" + request.getServerPort() + request.getContextPath();
 

loba_order_id=request.getParameter("pay_registrationid_gateway");
// Start: ChangeId: 2023111603
if( loba_order_id == null ){
    response.sendError(400, "No Request parameter passed!" );
}
else{
    Pattern pattern = Pattern.compile("^[0-9]{4}-[0-9]{8}-[0-9]{13}$");  // Sample valid pattern 2023-23129698-1698662407867
    Matcher match = pattern.matcher(loba_order_id);
    if(!match.matches()){
        response.sendError(400, "Invalid Request parameter!" );
    }
}
// End: ChangeId: 2023111603

loba_order_amount="750"; // 09-05-2025

int flag=new DBConnection().insertTrans(loba_id,loba_order_id,Integer.parseInt(loba_order_amount));


String data_url = "loba_id="+loba_id+"|loba_order_id="+loba_order_id+"|loba_order_amount="+loba_order_amount+"|loba_other_details="+loba_other_details;

// Start: ChangeId: 2025042401
String[] loba_order_id_parts = loba_order_id.split("-");
String regId = loba_order_id_parts[0] + "-" + loba_order_id_parts[1];
String user_detail = new DBConnection().getUserDetail(regId);
data_url = data_url +"|"+user_detail;
// End ChangeId: 2025042401        
String lobaSecretKey = "42813CF74357A7C0891D20D0A0B767AF";

String lobaSecretData = ""; 
try {
    // Convert the key to bytes
    byte[] keyBytes = lobaSecretKey.getBytes("UTF-8");

    // Create the AES cipher in ECB mode with PKCS5 padding
    SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    // Encrypt the data
    byte[] encryptedBytes = cipher.doFinal(data_url.getBytes("UTF-8"));

    // Encode the encrypted data to Base64 for safe transmission
    lobaSecretData = Base64.getEncoder().encodeToString(encryptedBytes);

    //out.println("Encrypted Data: " + lobaSecretData);
} catch (Exception e) {
    e.printStackTrace();
}
%>





    <header class="header header_style_01">
        <nav class="megamenu navbar navbar-default">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" ><img src="images/logos/logo.png" style="width:100px;height:100px;" alt="image"></a>	
					   
                </div>
            </div>
        </nav>
    </header>

    <div id="about" class="section wb">
        <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <div class="message-box">
                       
                        <h2><u>e-Recruitment Registration Payment details:</u></h2><br>
		
                        <!-- <h3><b><u>Post:</u> Medical Officer</b></h3><br> -->
                        <h3><b><u>Registration Id:</u> <%=loba_order_id%></b></h3><br>
                        <h3><b><u>Registration Fee:</u>  <%=loba_order_amount%></b></h3><br>
		
                        <form name="form1" method="post" id="form1" action="https://apps.nrsc.gov.in/nbpg/payment.php">
                        <input type="hidden" name="loba_id" value="<%=loba_id%>"  />
                        <input type="hidden" name="loba_data" value="<%=lobaSecretData%>"  />

                        <!--ChangeId: 2024012008, submit button shown, useful if not auto redirected-->
                        <input type="submit" class="btn btn-light btn-radius btn-brd grd1" value="Click here if not auto redirected to bank"/> <!-- ChangeId: 2023110701 To not to show Make Payment button at this page -->
                        </form>
                    </div><!-- end messagebox -->
                </div><!-- end col -->

            </div><!-- end row -->

            <hr class="hr1"> 

        </div><!-- end container -->
    </div><!-- end section -->
	
	



    

 

    <div class="copyrights">
        <div class="container">
            <div class="footer-distributed">
                <div class="footer-left">                   
                    <p class="footer-company-name">All Rights Reserved. &copy; 2023 Design By : NRSC</p>
                </div>

                
            </div>
        </div><!-- end container -->
    </div><!-- end copyrights -->

    <a href="#" id="scroll-to-top" class="dmtop global-radius"><i class="fa fa-angle-up"></i></a>

    <!-- ALL JS FILES -->
    <!--<script src="js/all.js"></script>--> <!--ChangeId: 2023111604 commented-->
    <script src="js/jquery.js"></script>
    <!-- ALL PLUGINS -->
    <script src="js/custom.js"></script>
    <script src="js/portfolio.js"></script>
    <!--<script src="js/hoverdir.js"></script>  ChangeId: 2024011604 removed  -->
    <!--Start: ChangeId: 2023101901-->
    <script nonce="erecruit2024010901"> // ChangeId: 2024010901
        // $("#form1").submit(); // ChangeId: 2023111604 commented
        localStorage.removeItem("csrf-token"); // ChangeId: 2023121202
        localStorage.removeItem("vtoken"); // ChangeId: 2023121202
        document.getElementById("form1").submit(); // ChangeId: 2023111604 added
    </script>
    <!--End: ChangeId: 2023101901-->
</body>
</html>