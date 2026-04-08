<%@page import="java.sql.*" %>
<%@page import="java.net.*"%>
<%@page import="java.util.*" %>
<%@page import="java.io.*" %>
<%--@page import="com.webproject.model.service.CryptoService" --%>
<%@page import="javax.naming.*" %>


<%@ page errorPage="ExceptionHandler.jsp" %>
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
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<%
String unicid = "";
String operating_mode="";
String merchant_country="";
String merchant_currency="";
String EncryptTrans="";
String merchant_code="";
String Merchant_Key="";
String amount="";
String dataUrl="";

unicid="123456";
operating_mode="DOM";
merchant_country="IN";
merchant_currency="INR";
merchant_code="1000112";
Merchant_Key="A7C9F96EEE0602A61F184F4F1B92F0566B9E61D98059729EAD3229F882E81C3A";
amount="1";
			
//String encmerchantKey = "Go-Live Encrption Key";
//String enckey = "Go-Live Decryption Key";
//String merchantKey	= AES256Bit.decrypt(encmerchantKey, enckey);
//out.println(merchantKey);		
			
dataUrl = ""+merchant_code+"|"+operating_mode+"|"+merchant_country+"|"+merchant_currency+"|"+amount+"|NA|https://www.nrsc.gov.in/erecruitment/redirecturl_test.jsp|https://www.nrsc.gov.in/erecruitment/redirecturl_fail.jsp|SBIEPAY|"+unicid+"|"+unicid+"|NB|ONLINE|ONLINE";
 
dataUrl = dataUrl.replaceAll(" ", ""); 
out.println("<br>dataUrl<br>"+dataUrl);
//EncryptTrans   = AES128Bit.encrypt(dataUrl, merchantKey);
//EncryptTrans   = AES256Bit.encrypt(dataUrl, AES256Bit.readKeyBytes(merchantKey));
        SecretKeySpec key = AES256Bit.readKeyBytes(Merchant_Key);
   
         EncryptTrans = AES256Bit.encrypt(dataUrl, key);

out.println("<br>EncryptTrans<br>"+EncryptTrans);
out.println( "<br>decrypted: <br>"+ AES256Bit.decrypt( EncryptTrans , key));
		
%>
		

		<form name="form1" method="post" id="form1" action="https://test.sbiepay.sbi/secure/AggregatorHostedListener">
		
		
        <input name="EncryptTrans" value="<%=EncryptTrans%>" type="hidden" />
		<input type="hidden" name="merchIdVal" value="<%=merchant_code%>"  />

		<input type="submit" value="Pay / Submit"/>	
		</form>
		
	
