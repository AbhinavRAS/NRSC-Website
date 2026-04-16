package ehiring.action;

 

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;


public class Double_verification { 
    public static void main(String[] args) throws SQLException, IOException, Exception {
        Double_verification d = new  Double_verification();
            try{
                disableSSLVerification();
            } catch (IOException e) {
                System.out.println("IOException"+e.getMessage()); 
        //      GenericExceptionLog.exceptionJava(e, "Exception Occured in :: urlConnection() for "+gatewayUrl, "AggGatewayURLConnection");
            }
        String loba_order_id = args[0];
        String loba_id = args[1];

        String response = d.urlConnection("https://172.19.2.154/nbpg/get_status.php", loba_id, loba_order_id);
        if(!response.equals("No record found")){
            DBConnection db = new DBConnection(); 
            db.updateDoubleVerification(response);
        }
        
    }//main end

public  String urlConnection(String gatewayUrl,String loba_id,String loba_order_id) {

    String responseCode = "";
    InputStream stream = null;
    InputStreamReader isr = null;
    BufferedReader reader = null;

    try {

        HashMap<String, String> params = new HashMap<String,String>();

        params.put("loba_id", loba_id);
        params.put("loba_order_id", loba_order_id);

        URL url = new URL(gatewayUrl);


        HttpURLConnection  httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setDoInput(true); // true indicates the server returns response
        StringBuffer requestParams = new StringBuffer();

        if (params != null && params.size() > 0) {
            httpConn.setDoOutput(true); // true indicates POST request

            // creates the params string, encode them using URLEncoder

            Iterator<String> paramIterator = params.keySet().iterator();

            while (paramIterator.hasNext()) {
                String key = paramIterator.next();
                String value = params.get(key);
                requestParams.append(URLEncoder.encode(key, "UTF-8"));
                requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
                requestParams.append("&");
            }
            
            // sends POST data
            OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
            writer.write(requestParams.toString());
            writer.flush();

            //Response Code
            int responseMsg = httpConn.getResponseCode();
                 
            // Reading Response

            stream = httpConn.getInputStream();
            isr = new InputStreamReader(stream);
            reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line = null ;

            while((line = reader.readLine()) != null) {
                   sb.append(line).append("\n");
            }

            stream.close();

            responseCode = sb.toString() ;
            responseCode = responseCode.trim();
            System.out.println("responseCode:"+responseCode);
        }

    } catch (MalformedURLException e) {
        System.out.println("MalformedURLException "+e.getMessage());
//      GenericExceptionLog.exceptionJava(e, "Exception Occured in :: urlConnection() for "+gatewayUrl, "AggGatewayURLConnection");
    } catch (ProtocolException e) {
        System.out.println("ProtocolException: "+e.getMessage());
//      GenericExceptionLog.exceptionJava(e, "Exception Occured in :: urlConnection() for "+gatewayUrl, "AggGatewayURLConnection");
    } catch (IOException e) {
        System.out.println("IOException"+e.getMessage());
//      GenericExceptionLog.exceptionJava(e, "Exception Occured in :: urlConnection() for "+gatewayUrl, "AggGatewayURLConnection");
    } finally {

        try {
            if (reader != null) {
                   reader.close();
                   reader = null;
            }

            if (isr != null) {
                   isr.close();
                   isr = null;
            }

            if (stream != null) {
                   stream.close();
                   stream = null;
            }

        } catch (Exception e) {
            System.out.println("Exception: "+e.getMessage());
//          GenericExceptionLog.exceptionJava(e, "Exception Occured in :: urlConnection() for "+gatewayUrl, "AggGatewayURLConnection");
        }
    }
    return responseCode;
 }

private static void disableSSLVerification() throws Exception {
        // Create a TrustManager that accepts all certificates
        TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;  // No certificate authorities are accepted
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // No client certificate validation
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // No server certificate validation
                }
            }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCertificates, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Install the all-trusting hostname verifier
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}


