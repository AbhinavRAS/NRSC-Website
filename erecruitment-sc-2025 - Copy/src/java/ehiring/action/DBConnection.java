package ehiring.action;
 
import static com.sun.xml.bind.util.CalendarConv.formatter;
import ehiring.properties.LoadProperties;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;  // Import the Map interface
import java.util.HashMap;  // Import the HashMap class

public class DBConnection {
    // Start: ChangeId: 2023111605
    private String db_url;
    private String db_uname;
    private String db_pword;
    private int db_maxconn;
    private Connection db_conn;
    // End: ChangeId: 2023111605
   public static void main(String args[])  throws Exception {
       // Start: ChangeId: 2023111605
       
        Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
               .getConnection("jdbc:postgresql://172.19.2.60:5432/erecruit", // CRITICAL ChangeId: 2023110101: Hard coding to be removed in future
               "nrsc", "tiger");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            //System.exit(0); ChangeId: 2023111302
        }
        System.out.println("Opened database successfully");
       
       DBConnection dbc = new DBConnection(); 
       //dbc.updateTrans("15345AABC479ECA1R|5718539555712|SUCCESS|750|INR|NB|NA|Y|SBIT|86340|2023-07-28 14:13:28|IN|null|1000112|0.00^0.00|||||||||");
       // End: ChangeId: 2023111605
   }
   
   // Start: ChangeId: 2023111605
   public DBConnection(){
        System.out.println("DBConnection: Constructor called");
        LoadProperties lp = new LoadProperties();
        try {
            db_url = lp.getProperty("recruit.url");
            db_uname = lp.getProperty("recruit.user");
            db_pword = lp.getProperty("recruit.password");
            db_maxconn = parseInt(lp.getProperty("recruit.maxconn"));
            
            Class.forName("org.postgresql.Driver");
            db_conn = DriverManager.getConnection(db_url,db_uname,db_pword); 
            
            System.out.println("DBConnection: Connection established "+db_conn);
        } catch (Exception ex) {
            Logger.getLogger(DownloadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   // End: ChangeId: 2023111605
   
   public int updateTrans(String s) throws SQLException
   {
        int flag=0;

        String [] splitParts = s.split("\\|");

	System.out.println("updateTrans(): s=" + s);
        
        String loba_order_id = splitParts[0] ;
        loba_order_id = loba_order_id.trim();

        String ntrp_order_status = splitParts[1];
        ntrp_order_status = ntrp_order_status.trim();

        String ntrp_txn_amount = splitParts[2] ;
        ntrp_txn_amount = ntrp_txn_amount.trim();
        if(ntrp_txn_amount.equals("NA")){
            ntrp_txn_amount="0";
        }

        String ntrp_txn_ref = splitParts[3] ;
        ntrp_txn_ref = ntrp_txn_ref.trim();

        String ntrp_txn_time = splitParts[4] ;
        ntrp_txn_time = ntrp_txn_time.trim();

        String loba_other_detail = splitParts[5] ;
        loba_other_detail = loba_other_detail.trim();

            Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");

            // End: ChangeId: 2023111605

            db_conn.setAutoCommit(false);

            // Start: ChangeId: 2023121402
            java.sql.Timestamp timestamp;
            if(!ntrp_txn_time.equals("NA")){
                DateTimeFormatter[] formatters = new DateTimeFormatter[]{
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),  // Format 1
                    DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")   // Format 2
                };
                timestamp = null;
                for (DateTimeFormatter formatter : formatters) {
                    try {
                        // Try to parse the datetime string using the current formatter
                        LocalDateTime localDateTime = LocalDateTime.parse(ntrp_txn_time, formatter);
                        timestamp = Timestamp.valueOf(localDateTime);  // Convert to Timestamp and return
                    } catch (DateTimeParseException e) {
                        // Continue to the next formatter if parsing fails
                        continue;
                    }
                }
                //SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.ENGLISH);
                //java.util.Date javadate = formatter.parse(ntrp_txn_time);
                //timestamp = new java.sql.Timestamp(javadate.getTime());
            }
            else{
                timestamp = null;
            }
            System.out.println("updateTrans(): timestamp=" + timestamp);
            
            PreparedStatement pstmt = null;
            pstmt = db_conn.prepareStatement(
               "UPDATE transaction_details SET ntrp_order_status=?, ntrp_txn_amount=?, ntrp_txn_ref=?, "+
       " ntrp_resp_bank_txn_date=?, loba_other_detail=?" + " WHERE loba_order_id = ?");

            pstmt.setString(1, ntrp_order_status);
            pstmt.setString(2, ntrp_txn_amount);
            pstmt.setString(3, ntrp_txn_ref);
            pstmt.setTimestamp(4, timestamp);
            pstmt.setString(5, loba_other_detail);
            pstmt.setString(6, loba_order_id);

            int txnstatusflag = 0;

            int txnflag=pstmt.executeUpdate();

            String personal_data_sts ="";
            // Start: ChangeId: 2025051402
            switch(ntrp_order_status){
                case "SUCCESS":
                case "Success":
                    personal_data_sts ="PAID";
                    break;
                case "PENDING":
                case "Pending":
                case "PAYMENT INITIATED":
                case "Payment initiated": // ChangeId: 2025051601
                    personal_data_sts ="PENDING";
                    break;
                default:
                    personal_data_sts ="PAYMENTFAILED"; // ChangeId: 2025051601
                    break;
            }
            // End: ChangeId: 2025051402



            stmt = db_conn.createStatement();
            String  merch_order_number[]=loba_order_id.split("-");
            txnstatusflag = stmt.executeUpdate("UPDATE personal_data set status='"+personal_data_sts+"' where registration_id= "+"'"+merch_order_number[0]+"-"+merch_order_number[1]+"'");
            // End: ChangeId: 2023121402
            db_conn.commit();
            if ( txnflag == 1 ) 
                flag=1;

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            flag=0;
        }
        finally
        {
            stmt.close();
            db_conn.close();
        }

	return flag;  
   }
   
// Start: ChangeId: 2023121402
public int insertTrans(String loba_id, String loba_order_id, int loba_order_amount) throws SQLException
{
    int flag=0;
    Statement stmt = null;

    try {
        Class.forName("org.postgresql.Driver");

        db_conn.setAutoCommit(false);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Calendar cal = Calendar.getInstance();
        java.sql.Date date = new java.sql.Date(cal.getTimeInMillis());
        System.out.println(formatter.format(date));

        int txnstatusflag = 0;
        int txnflag=0;
        PreparedStatement pstmt = null;
        pstmt = db_conn.prepareStatement(
           "insert into  transaction_details (loba_order_id, ntrp_txn_ref, loba_order_amount,"+
           "loba_id, ntrp_order_status, ntrp_resp_bank_txn_date) VALUES(?,?,?,?,?,?);");

        pstmt.setString(1, loba_order_id);
        pstmt.setString(2, "NA");
        pstmt.setInt(3, loba_order_amount);
        pstmt.setString(4, loba_id);
        pstmt.setString(5, "FORWARDED");
        pstmt.setDate(6, date);
        pstmt.executeUpdate();


        stmt = db_conn.createStatement();
        String  merch_order_number[]=loba_order_id.split("-");
        txnstatusflag = stmt.executeUpdate("UPDATE personal_data set status='FORWARDED' where registration_id= "+"'"+merch_order_number[0]+"-"+merch_order_number[1]+"'");

        db_conn.commit();
        if ( txnflag == 1 ) 
            flag=1;

    } catch (Exception e) {
        System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        flag=0;
    }
    finally
    {
        stmt.close();
        db_conn.close();
    }

    return flag;  
}
// End: ChangeId: 2023121402
   
   
   
   
   public int updateDoubleVerification(String response) throws SQLException, IOException
           
   {
        int flag=0;
        
        //Sample: NERECRUIT|NRSC-RMT-1-2025-DEV^CS01^|2025-24630591-1744038989325|2025-04-07 15:18:21|Success|0704250133072|NERECRUIT#2025-24630591-1744038989325|750|750|2025-04-07 20:47:00|IN|INR|CC|NA|877725934590261600|NA|NA|NA|NA|20099707042500132881||23111
        //String [] splitParts = response.split("\\|");
        Map<String, String> response_data_map = new HashMap<>();
        String[] pairs = response.split("\\|");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2); // use limit=2 in case value has '='
            if (keyValue.length == 2) {
                response_data_map.put(keyValue[0], keyValue[1]);
            }
        }
        
        String loba_order_id, 
                ntrp_txn_ref, 
                ntrp_order_status, 
                loba_order_amount, 
                ntrp_currency_code, 
                ntrp_payment_mode, 
                loba_other_detail, 
                ntrp_error_code, 
                ntrp_bank_code, 
                ntrp_resp_bank_txn_no, 
                ntrp_txn_time, 
                ntrp_country_code, 
                ntrp_cin_no_details, 
                loba_id, 
                ntrp_txn_amount; 

        loba_order_id = response_data_map.get("loba_order_id"); 
        ntrp_txn_ref = response_data_map.get("ntrp_txn_ref"); 
        ntrp_order_status = response_data_map.get("ntrp_order_status"); //.toUpperCase(); // ChangeId: 2025051401
        loba_order_amount = response_data_map.get("loba_order_amount"); 
        ntrp_currency_code = response_data_map.get("ntrp_currency_code"); 
        ntrp_payment_mode = response_data_map.get("ntrp_payment_mode"); 
        loba_other_detail = response_data_map.get("loba_other_detail"); 
        ntrp_error_code = response_data_map.get("ntrp_error_code"); 
        ntrp_bank_code = response_data_map.get("ntrp_bank_code"); 
        ntrp_resp_bank_txn_no = response_data_map.get("ntrp_resp_bank_txn_no"); 
        ntrp_txn_time = response_data_map.get("ntrp_txn_time"); 
        ntrp_country_code = response_data_map.get("ntrp_country_code"); 
        ntrp_cin_no_details = response_data_map.get("ntrp_cin_no_details"); 
        loba_id = response_data_map.get("loba_id"); 
        ntrp_txn_amount = response_data_map.get("ntrp_txn_amount"); 
                
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");

            db_conn.setAutoCommit(false);

            java.sql.Timestamp timestamp = null;
            if(!ntrp_txn_time.equals("")){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                java.util.Date javadate = formatter.parse(ntrp_txn_time);
                timestamp = new java.sql.Timestamp(javadate.getTime());
            }
            
            PreparedStatement pstmt = null;
            pstmt = db_conn.prepareStatement(
               "UPDATE transaction_details SET ntrp_txn_ref=?, ntrp_order_status=?, "+
                    "loba_order_amount=?, ntrp_currency_code=?, ntrp_payment_mode=?, "+
                    "loba_other_detail=?, ntrp_error_code=?, ntrp_bank_code=?, "+
                    "ntrp_resp_bank_txn_no=?, ntrp_resp_bank_txn_date=?, ntrp_country_code=?, "+
                    "ntrp_cin_no_details=?, loba_id=?, ntrp_txn_amount=?" + " WHERE loba_order_id = ?");

            pstmt.setString(1, ntrp_txn_ref);
            pstmt.setString(2, ntrp_order_status);
            pstmt.setInt(3, Integer.parseInt(loba_order_amount));
            pstmt.setString(4, ntrp_currency_code);
            pstmt.setString(5, ntrp_payment_mode);
            pstmt.setString(6, loba_other_detail);
            pstmt.setString(7, ntrp_error_code);
            pstmt.setString(8, ntrp_bank_code);
            pstmt.setString(9, ntrp_resp_bank_txn_no);
            pstmt.setTimestamp(10, timestamp);
            pstmt.setString(11, ntrp_country_code);
            pstmt.setString(12, ntrp_cin_no_details);
            pstmt.setString(13, loba_id);
            pstmt.setString(14, ntrp_txn_amount);
            pstmt.setString(15, loba_order_id);
            
            //System.out.println("SQL stmnt"+sql);

            int txnstatusflag = 0;
            //int txnflag=stmt.executeUpdate(sql);
            int txnflag=pstmt.executeUpdate();

            String personal_data_sts ="";
            switch(ntrp_order_status){
                case "SUCCESS":
                case "Success":
                    personal_data_sts ="PAID";
                    break;
                case "PENDING":
                case "Pending":
                case "PAYMENT INITIATED":
                case "Payment initiated": // ChangeId: 2025051601
                    personal_data_sts ="PENDING"; 
                    break;
                default:
                    personal_data_sts ="PAYMENTFAILED"; // ChangeId: 2025051601
                    break;
            }
                    

                    
            stmt = db_conn.createStatement();
            String  merch_order_number[]=loba_order_id.split("-");
            // ChangeId: 2025051302 AND status NOT IN ('PAID','POSTED') added
            txnstatusflag = stmt.executeUpdate("UPDATE personal_data SET status='"+personal_data_sts+"' WHERE registration_id= "+"'"+merch_order_number[0]+"-"+merch_order_number[1]+"' AND status NOT IN ('PAID','POSTED')");
            //stmt.executeUpdate();
            // End: ChangeId: 2023121402
            
            db_conn.commit();

            System.out.println("Personal_data txn status Updated");
        } catch (Exception e) {
           System.err.println( e.getClass().getName()+": "+ e.getMessage() );
           //System.exit(0); // ChangeId: 2023111302

           flag=0;
            System.out.println("error flag"+flag);
            /*printWriter.println("Error in updating OrderID: "+unicid_received +" details");*/
        }
        finally
        {
            stmt.close();
            db_conn.close();
        }
              
        System.out.println("final flag"+flag);

        return flag;   
    }
   
   // Start: ChangeId: 2025042401
   public String getUserDetail(String regId) throws SQLException
   {
	System.out.println("getUserDetail(): regId = "+regId);
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        String resultStr = "";
        
        try {
            Class.forName("org.postgresql.Driver");
           
            
            pstmt = db_conn.prepareStatement(
               "SELECT house_no, locality, town, district, state, pincode, "
                +"p_house_no, p_locality, p_town, p_district, p_state, p_pincode, "
                +"email, contact_no, first_name "
                +"FROM personal_data WHERE registration_id=?", ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);

            pstmt.setString(1, regId);
            
            System.out.println("getUserDetail(): pstmt = "+pstmt.toString());
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                
                resultStr = 
                        "mobile="+rs.getString("contact_no")+"|"+
                        "name="+rs.getString("first_name")+"|"+
                        "email_id="+rs.getString("email")+"|"+
                        
                        "shipping_add="+rs.getString("house_no")+"|"+
                        "shipping_add2="+rs.getString("locality")+"|"+
                        "shipping_pincode="+rs.getString("pincode")+"|"+
                        "shipping_city="+rs.getString("town")+"|"+
                        "shipping_district="+rs.getString("district")+"|"+
                        "shipping_state="+rs.getString("state")+"|"+
                        
                        "billing_add="+rs.getString("p_house_no")+"|"+
                        "billing_add2="+rs.getString("p_locality")+"|"+
                        "billing_pincode="+rs.getString("p_pincode")+"|"+
                        "billing_city="+rs.getString("p_town")+"|"+
                        "billing_district="+rs.getString("p_district")+"|"+
                        "billing_state="+rs.getString("p_state");
            }
            rs.close();
            pstmt.close();
            db_conn.close();

        } catch (Exception e) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
        }
        finally
        {
            rs.close();
            pstmt.close();
            db_conn.close();
        }
        System.out.println("getUserDetail(): ResultStr = "+resultStr);
	return resultStr;  
   }
   // End: ChangeId: 2025042401
}
