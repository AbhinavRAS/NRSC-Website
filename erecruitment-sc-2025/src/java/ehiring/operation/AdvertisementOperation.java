package ehiring.operation;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ehiring.action.CurrentDateTime;
import ehiring.dao.AdvertisementData;
import ehiring.dao.LogManager;
import ehiring.db.DBConnectionManager;

import java.util.Calendar;

public class AdvertisementOperation {

    DBConnectionManager conMgr = null;
    String lPoolName = "recruit";
    LogManager logMgr;
    String emailLogId = new String();

    public AdvertisementOperation(String emailLogId) {
        conMgr = DBConnectionManager.getInstance();
        logMgr = LogManager.getInstance(emailLogId);
        this.emailLogId = emailLogId;
    }

    public int addAdvt(AdvertisementData ad) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        int i = -1;
        try {
            logMgr.accessLog("inside ADD ADVERTISEMENT method............");

            con = conMgr.getConnection(lPoolName);
            con.setAutoCommit(false);

            pstmt = con.prepareStatement("select count(*) from advt_data where advt_no = ? and status = ?");
            pstmt.setString(1, ad.getAdvt_no());
            pstmt.setString(2, ad.getStatus());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    logMgr.accessLog("Advertisement with advt_no: " + ad.getAdvt_no() + " is already active!!");
                    return -3;
                }
            }
            // logMgr.accessLog("date is ............:" +
            // ad.getAdvt_upload_date());
            pstmt.close();

            String qry = "insert into advt_data(advt_no,advt_upload_date,valid_from,valid_till,remarks,advt_id,status,date_of_status_update,reference_date) Values('"
                    + ad.getAdvt_no() + "','" + ad.getAdvt_upload_date() + "','" + ad.getValid_from() + "','"
                    + ad.getValid_till() + "','" + ad.getRemarks() + "','" + generateAdvtID() + "','" + ad.getStatus()
                    + "','" + ad.getDate_of_status_update() + "','" + ad.getRef_date() + "')";
            pstmt = con.prepareStatement(qry);

            logMgr.accessLog("Avt qry:" + qry);

            i = pstmt.executeUpdate();
            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            con.rollback();
            if (e.getMessage().contains("duplicate key value")) {
                return -2;
            }
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return i;
    }

    public String generateAdvtID() throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String advt_id = new String();
        String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));

        try {

            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement(
                    "select substr(advt_id,1) from advt_data where advt_id like '" + currentYear + "-%'  ");
            logMgr.accessLog(" generate advt id is :" + pstmt.toString());

            rs = pstmt.executeQuery();

            int max = 0;

            while (rs.next()) {
                String tempStr = rs.getString(1);
                String[] split = tempStr.split("-");
                int temp = Integer.parseInt(split[1]);
                if (temp > max) {
                    max = temp;
                }
            }

            max++;
            advt_id = currentYear + "-" + max;
            logMgr.accessLog(advt_id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return advt_id;
    }

    public ArrayList<String> loadAdvt() throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList<String> arr = new ArrayList<>();
        try {
            logMgr.accessLog("Inside GET ADVERTISEMENT OPTIONS");

            con = conMgr.getConnection(lPoolName);
            /*
             * pstmt =
             * con.prepareStatement("select advt_no from advt_data where status='active'",
             * ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             */
            pstmt = con.prepareStatement("select distinct advt_no from advt_data order by advt_no",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            logMgr.accessLog("THE  ADS ARE for admin :" + pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                arr.add(rs.getString("advt_no"));
            }
            if (arr.isEmpty()) {
                logMgr.accessLog("No Data Found");
            }
            logMgr.accessLog("Load advt called");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return arr;
    }

    public ArrayList<String> loadAllAdvt() throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList<String> arr = new ArrayList<>();

        try {
            logMgr.accessLog("Inside GET ALL ADVERTISEMENT OPTIONS");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement(
                    "select advt_no from advt_data  where status in ('active','inactive') order by advt_no",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            /*
             * pstmt = con.
             * prepareStatement("select distinct advt_no from post_data where status='active'"
             * , ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             */

            logMgr.accessLog("THE ACTIVE ADS ARE :" + pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                arr.add(rs.getString("advt_no"));
            }
            if (arr.isEmpty()) {
                logMgr.accessLog("No Data Found");
            }
            logMgr.accessLog("Load advt called");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return arr;
    }

    public int editAdvt(AdvertisementData ad) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;

        int i = -1;
        try {
            logMgr.accessLog("inside edit advertisement method");

            con = conMgr.getConnection(lPoolName);

            pstmt = con.prepareStatement(
                    "update advt_data set valid_from = ? , valid_till = ? , remarks =?" + " where advt_no = ?");

            pstmt.setDate(1, ad.getValid_from());
            pstmt.setDate(2, ad.getValid_till());
            pstmt.setString(3, ad.getRemarks());
            pstmt.setString(4, ad.getAdvt_no());
            logMgr.accessLog(ad.getValid_from() + "," + ad.getValid_till());

            i = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("duplicate key value")) {
                return -2;
            }
        } finally {
            try {
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return i;
    }

    public ArrayList<String> displayAdvtData() throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList<String> arr = new ArrayList<String>();

        try {
            logMgr.accessLog("Inside displayAdvtData");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement("select advt_no,advt_upload_date from advt_data order by advt_no",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                arr.add(rs.getString("advt_no"));
                arr.add(rs.getString("advt_upload_date"));
            }
            if (arr.isEmpty()) {
                logMgr.accessLog("No Data Found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return arr;
    }

    public ArrayList<String> deleteUpload(String fieldName, String advtNo, String postNo, String filename) {

        ArrayList<String> arr = new ArrayList<String>();

        try {
            ehiring.operation.RecruitmentDirectoryOperation rdo = new ehiring.operation.RecruitmentDirectoryOperation(
                    emailLogId);
            //System.out.println("FTER SPLIT. delete file1!!1"+emailLogId);
            String UPLOAD_DIR = rdo.getAppIdFolderName("", emailLogId);
            String filePath = UPLOAD_DIR + File.separator + advtNo + File.separator + postNo + File.separator;
            logMgr.accessLog("FTER SPLIT. delete file:" + filePath);
            String emailFolder = emailLogId.replace("@", "_");
            emailFolder = emailFolder.replace(".", "_");
            //logMgr.accessLog("AFTER SPLIT....:" + filePath + emailFolder);
            //logMgr.accessLog("FTER SPLIT. delete file:"+filePath + fieldName + "_" + emailFolder);
            //System.out.println("EMAIL IS "+emailLogId);

            int indexStr = filename.lastIndexOf(".");
            String extnFile = filename.substring(indexStr);

            File f = new File(filePath + fieldName + "_" + emailFolder + extnFile);
            logMgr.accessLog(" delete upload file:" + f.toString());

            if (f.exists()) {
                if (f.delete()) {
                    f = new File(filePath + filename);
                    if (f.exists()) {
                        if (f.delete()) {
                            arr.add("success");
                        } else {
                            arr.add("error");
                        }
                    }
                }
            } else {
                arr.add("error");
            }

        } catch (Exception e) {
            arr.add("error");
            e.printStackTrace();
        }

        return arr;
    }

    /*
     * public int deleteAdvt(AdvertisementData ad) { int i = 0, j = 0, k = 0;
     * Connection con = null; PreparedStatement advtPstmt = null, postPstmt = null,
     * categoryPstmt = null;
     * 
     * try { System.out.println(CurrentDateTime.dateTime()+
     * ":"+"inside delete advertisement method");
     * 
     * con = new DBConnection().getCon();
     * 
     * categoryPstmt = con.prepareStatement(
     * "update category_data set status = ? , date_of_status_update = ? where advt_no = ?"
     * );
     * 
     * categoryPstmt.setString(1, ad.getStatus()); categoryPstmt.setDate(2,
     * ad.getDate_of_status_update()); categoryPstmt.setString(3, ad.getAdvt_no());
     * 
     * i = categoryPstmt.executeUpdate();
     * 
     * if (i > 0) { postPstmt = con.prepareStatement(
     * "update post_data set status = ? , date_of_status_update = ? where advt_no = ?"
     * );
     * 
     * postPstmt.setString(1, ad.getStatus()); postPstmt.setDate(2,
     * ad.getDate_of_status_update()); postPstmt.setString(3, ad.getAdvt_no());
     * 
     * j = postPstmt.executeUpdate(); }
     * 
     * if (j > 0) { advtPstmt = con.prepareStatement(
     * "update advt_data set status = ? , date_of_status_update = ? where advt_no = ?"
     * );
     * 
     * advtPstmt.setString(1, ad.getStatus()); advtPstmt.setDate(2,
     * ad.getDate_of_status_update()); advtPstmt.setString(3, ad.getAdvt_no());
     * 
     * k = advtPstmt.executeUpdate(); } } catch (ClassNotFoundException |
     * SQLException e) { e.printStackTrace(); } return k; }
     */
    public int getMaxDeleteCnt(AdvertisementData ad) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int cnt = 0;
        try {
            logMgr.accessLog("Inside getMaxDeletCnt");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement(
                    "select count(status) from advt_data where advt_no = ? and status like 'delete%'",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, ad.getAdvt_no());

            logMgr.accessLog("DELETE ADVT CNT QRY:" + pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                cnt = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cnt;

    }

    // flushPrevSaveApplication
    public String flushPrevSaveApplication(String reqId) {
        int res = flushRecord(reqId, "personal_data");
        if (res == -1) {
            return "error";
        }
        res = flushRecord(reqId, "x_data");
        if (res == -1) {
            return "error";
        }
        res = flushRecord(reqId, "xii_data");
        if (res == -1) {
            return "error";
        }
        res = flushRecord(reqId, "iti_diploma");
        if (res == -1) {
            return "error";
        }
        res = flushRecord(reqId, "highereducation");
        if (res == -1) {
            return "error";
        }
        res = flushRecord(reqId, "experience");
        if (res == -1) {
            return "error";
        } else {
            return "success";
        }

    }

    public int flushRecord(String regId, String tableName) {
        Connection con = null;
        int rs = -2;
        PreparedStatement pstmt = null;
        int cnt = 0;
        try {
            logMgr.accessLog("Inside getDeletRec");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement("delete from " + tableName + " where registration_id = ? ",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, regId);

            logMgr.accessLog("DELETE ADVT CNT QRY:" + pstmt.toString());
            int res = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;

    }

    public int deleteAdvt(AdvertisementData ad) {
        int i = 0, j = 0, k = 0;
        Connection con = null;

        PreparedStatement advtPstmt = null, postPstmt = null, categoryPstmt = null, pstmt = null;

        try {
            logMgr.accessLog("inside delete advertisement method :" + ad.getDate_of_status_update());

            con = conMgr.getConnection(lPoolName);

            pstmt = con.prepareStatement("select count(*) from advt_data where advt_no = ? and status ='active'");
            pstmt.setString(1, ad.getAdvt_no());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    logMgr.accessLog(
                            "Advertisement with advt_no: " + ad.getAdvt_no() + " is active cannot be deleted!!");
                    return -99;
                }
            }

            rs.close();
            pstmt.close();

            pstmt = con.prepareStatement("select count(*) from post_data where advt_no = ? and status ='active'");
            pstmt.setString(1, ad.getAdvt_no());

            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    logMgr.accessLog(
                            "Advertisement with advt_no: " + ad.getAdvt_no() + " has active posts cannot be deleted!!");
                    return -98;
                }
            }

            rs.close();
            pstmt.close();

            categoryPstmt = con.prepareStatement(
                    "update category_data set status = ? , date_of_status_update = ? where advt_no = ? and status='inactive'");

            categoryPstmt.setString(1, ad.getStatus());
            categoryPstmt.setDate(2, ad.getDate_of_status_update());
            categoryPstmt.setString(3, ad.getAdvt_no());

            i = categoryPstmt.executeUpdate();

            if (i >= 0) {
                postPstmt = con.prepareStatement(
                        "update post_data set status = ? , date_of_status_update = ? where advt_no = ? and status='inactive'");

                postPstmt.setString(1, ad.getStatus());
                postPstmt.setDate(2, ad.getDate_of_status_update());
                postPstmt.setString(3, ad.getAdvt_no());

                i = postPstmt.executeUpdate();
            }

            if (i >= 0) {
                advtPstmt = con.prepareStatement(
                        "update advt_data set status = ? , date_of_status_update = ? where advt_no = ? and status='inactive'");

                advtPstmt.setString(1, ad.getStatus());
                advtPstmt.setDate(2, ad.getDate_of_status_update());
                advtPstmt.setString(3, ad.getAdvt_no());

                logMgr.accessLog(advtPstmt.toString());

                i = advtPstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (advtPstmt != null) {
                    advtPstmt.close();
                }
                if (postPstmt != null) {
                    postPstmt.close();
                }
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    public int applicantAttempts(String advtNo, String postNo, String emailId) {

        int postCnt = getMaxFlushAppCnt(advtNo, postNo, emailId, "POSTED");
        int flushCnt = getMaxFlushAppCnt(advtNo, postNo, emailId, "FLUSHED");
        int totalCnt = postCnt + flushCnt;

        return totalCnt;
    }

    public int flushPrevApplication(String advtNo, String postNo, String emailId, String regId) {
        int i = -1;
        Connection con = null;

        PreparedStatement pstmt = null, persPstmt = null;
        try {
            logMgr.accessLog("inside flushPrevApplication method :" + regId);

            con = conMgr.getConnection(lPoolName);
            boolean flag = false;

            int postCnt = getMaxFlushAppCnt(advtNo, postNo, emailId, "POSTED");
            if (postCnt == 1) {
                int flushCnt = getMaxFlushAppCnt(advtNo, postNo, emailId, "FLUSHED");
                if (flushCnt == 2) {
                    return -3; // max attempts exceeded
                } else if (flushCnt == 0 || flushCnt == 1) {
                    flag = true;
                }
            } else {
                i = 0;
            }

            if (flag) {
                persPstmt = con.prepareStatement(
                        "update personal_data set status = ? where post_no=? and advt_no=? and lower(email)=? and status=?");

                persPstmt.setString(1, "FLUSHED");
                persPstmt.setString(2, postNo);
                persPstmt.setString(3, advtNo);
                persPstmt.setString(4, emailId.toLowerCase());
                persPstmt.setString(5, "POSTED");
                logMgr.accessLog("FLUSH QRY 2:" + persPstmt.toString());
                i = persPstmt.executeUpdate();
                persPstmt.close();

                conMgr.freeConnection(lPoolName, con);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            conMgr.freeConnection(lPoolName, con);
        }
        logMgr.accessLog("FLUSH ....:" + i);
        return i;
    }
    // Start: ChangeId:2023101701 To handle submit from home
    public int SubmitApplicationHome(String advtNo, String postNo, String emailId, String regId) {
        int i = -1;
        Connection con = null;

        PreparedStatement txnPstmt = null, persPstmt = null; // ChangeId: 2023111606  txnPstmt declared
        try {
            logMgr.accessLog("inside SubmitApplicationHome method :" + regId);
            System.out.println("inside SubmitApplicationHome method :" + regId);
            con = conMgr.getConnection(lPoolName);
            
            // Start: ChangeId: 2023111606
            txnPstmt = con.prepareStatement("SELECT SUBSTRING(loba_order_id,0,14), ntrp_order_status " +
                                            "FROM transaction_details " +
                                            "WHERE SUBSTRING(loba_order_id,0,14)=? AND ntrp_order_status IN ('SUCCESS','Success') "+
                                            "AND loba_order_amount=750;"); // 09-05-2025
            txnPstmt.setString(1, regId);
            ResultSet rs = txnPstmt.executeQuery();
            if (rs.next()) { // Successful transaction found, 
                logMgr.accessLog("HOMESUBMIT: Successful transaction found for RegId: " + regId);
                persPstmt = con.prepareStatement(
                    "update personal_data set status = ? where post_no=? and advt_no=? and lower(email)=? and registration_id=?");

                persPstmt.setString(1, "POSTED"); 
                persPstmt.setString(2, postNo);
                persPstmt.setString(3, advtNo);
                persPstmt.setString(4, emailId.toLowerCase());
                persPstmt.setString(5, regId);
                logMgr.accessLog("SUBMIT HOME QRY:" + persPstmt.toString());
                i = persPstmt.executeUpdate();
                persPstmt.close();

                conMgr.freeConnection(lPoolName, con);
            }
            else {
                logMgr.accessLog("HOMESUBMIT: No successful transaction found for RegId: " + regId);
            }
                
            rs.close();
            txnPstmt.close();
            // End: ChangeId: 2023111606
            
            

        } catch (SQLException e) {
            e.printStackTrace();
            conMgr.freeConnection(lPoolName, con);
        }
        logMgr.accessLog("HOME SUBMIT ....:" + i);
        return i;
    }
    // End: ChangeId:2023101701 To handle submit from home
    public int getMaxFlushAppCnt(String advtNo, String postNo, String email, String status) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int cnt = 0;
        try {
            logMgr.accessLog("Inside getMaxDeletCnt");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement(
                    "select count(status) from personal_data where post_no=? and advt_no=? and lower(email)=? and status=?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, postNo);
            pstmt.setString(2, advtNo);
            pstmt.setString(3, email.toLowerCase());
            pstmt.setString(4, status);

            logMgr.accessLog("FLUSH QRY 1:" + pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                cnt = rs.getInt(1);
            }
            rs.close();
            pstmt.close();
            conMgr.freeConnection(lPoolName, con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cnt;

    }

    public String getAdvValidDate(String advtno) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String validTill = new String();

        try {
            logMgr.accessLog("Inside getAdvValidDate");
            con = conMgr.getConnection(lPoolName);
            //valid_from = ? , valid_till
            pstmt = con.prepareStatement("select valid_till from advt_data where advt_no='" + advtno + "'",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            System.out.println("VALID DATE IS :select valid_till from advt_data where advt_no='" + advtno + "'");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                validTill = rs.getString("valid_till");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
                logMgr.accessLog("SOME ISSUE With Close connection");
            }
        }
        return validTill;
    }

}
