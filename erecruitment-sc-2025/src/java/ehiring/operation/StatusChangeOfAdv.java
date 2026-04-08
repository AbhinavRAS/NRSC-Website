package ehiring.operation;

import java.sql.Connection;
import ehiring.action.CurrentDateTime;
import ehiring.dao.LogManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ehiring.action.CurrentDateTime;
import ehiring.db.DBConnectionManager;

public class StatusChangeOfAdv {

    LogManager logMgr;

    public StatusChangeOfAdv(String emailLogId) {
        logMgr = LogManager.getInstance(emailLogId);
    }

    public boolean UpdateAdvt() throws SQLException {
        boolean flg = false;
        //	String value = new String();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt_inner = null;
        DBConnectionManager conMgr = null;
        String lPoolName = "recruit";
        String advtNo, sDate, eDate, status, startval;
        //int i = 0;

        try {
            logMgr.accessLog("Inside Update Status....");
            conMgr = DBConnectionManager.getInstance();
            conn = conMgr.getConnection(lPoolName);
            // logMgr.accessLog("CON IS >> ..."+conn);

            String pattern = "yyyy-MM-dd";
            String dateInString = new SimpleDateFormat(pattern).format(new Date());

            pstmt = conn.prepareStatement("select * from advt_data");

            logMgr.accessLog("Update status QUERY IS :" + pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                advtNo = rs.getString("advt_no");
                sDate = rs.getString("valid_from");
                eDate = rs.getString("valid_till");
                status = rs.getString("status");
                logMgr.accessLog(
                        "advtNo,sDate,eDate:" + advtNo + "," + sDate + "," + eDate + "," + status + "," + dateInString);

                startval = compareDates(dateInString, sDate);
                String valEnd = compareDates(dateInString, eDate);
                logMgr.accessLog("advtNo,sDate,eDate:" + advtNo + "," + sDate + "," + eDate + "," + status + ","
                        + dateInString + "," + startval + "," + valEnd);
                if (startval.equals("after")) {
                    if (status.equals("active")) {
                        pstmt_inner = conn.prepareStatement(
                                "update advt_data set status='inactive' where advt_no='" + advtNo + "'");
                        logMgr.accessLog("Update status QUERY after IS :" + pstmt.toString());
                        int retVal = pstmt_inner.executeUpdate();
                        if (retVal > 0) {
                            flg = true;
                        }
                        pstmt_inner.close();
                        // do inactive posts also.
                    }
                } else if (startval.equals("equal")) {
                    if (status.equals("inactive")) {
                        pstmt_inner = conn.prepareStatement("update advt_data set status='active' where advt_no='" + advtNo + "'");
                        logMgr.accessLog("Update status QUERY after IS :" + pstmt_inner.toString());
                        int retVal = pstmt_inner.executeUpdate();
                        if (retVal > 0) {
                            flg = true;
                        }
                        pstmt_inner.close();
                        if (flg) {
                            pstmt_inner = conn.prepareStatement(
                                    "update post_data set status='active' where advt_no='" + advtNo + "'");
                            logMgr.accessLog("Update status QUERY before IS :" + pstmt.toString());
                            retVal = pstmt_inner.executeUpdate();
                            if (retVal > 0) {
                                flg = true;
                            }

                            pstmt_inner.close();
                            if (flg) {
                                pstmt_inner = conn.prepareStatement(
                                        "update post_data_details set status='active' where advt_no='" + advtNo
                                        + "'");
                                logMgr.accessLog("Update status QUERY before IS :" + pstmt.toString());
                                retVal = pstmt_inner.executeUpdate();
                                if (retVal > 0) {
                                    flg = true;
                                }
                                pstmt_inner.close();
                                if (flg) {
                                    pstmt_inner = conn.prepareStatement(
                                            "update category_data set status='active' where advt_no='" + advtNo
                                            + "'");
                                    logMgr.accessLog("Update status QUERY before IS :" + pstmt.toString());
                                    retVal = pstmt_inner.executeUpdate();
                                    if (retVal > 0) {
                                        flg = true;
                                    }
                                }
                                pstmt_inner.close();
                            }
                        }
                        // do inactive posts also.
                    }
                } else {
                    if (valEnd.equals("before")) {
                        if (status.equals("active")) {
                            //pstmt_inner.close();
                            pstmt_inner = conn.prepareStatement("update advt_data set status='inactive' where advt_no='" + advtNo + "'");
                            logMgr.accessLog("Update status QUERY after IS :" + pstmt_inner.toString());
                            int retVal = pstmt_inner.executeUpdate();
                            if (retVal > 0) {
                                flg = true;
                            }
                            pstmt_inner.close();
                            if (flg) {
                                pstmt_inner = conn.prepareStatement(
                                        "update post_data set status='inactive' where advt_no='" + advtNo + "'");
                                logMgr.accessLog("Update status QUERY before IS :" + pstmt.toString());
                                retVal = pstmt_inner.executeUpdate();
                                if (retVal > 0) {
                                    flg = true;
                                }

                                pstmt_inner.close();
                                if (flg) {
                                    pstmt_inner = conn.prepareStatement(
                                            "update post_data_details set status='inactive' where advt_no='" + advtNo
                                            + "'");
                                    logMgr.accessLog("Update status QUERY before IS :" + pstmt.toString());
                                    retVal = pstmt_inner.executeUpdate();
                                    if (retVal > 0) {
                                        flg = true;
                                    }
                                    pstmt_inner.close();
                                    if (flg) {
                                        pstmt_inner = conn.prepareStatement(
                                                "update category_data set status='inactive' where advt_no='" + advtNo
                                                + "'");
                                        logMgr.accessLog("Update status QUERY before IS :" + pstmt.toString());
                                        retVal = pstmt_inner.executeUpdate();
                                        if (retVal > 0) {
                                            flg = true;
                                        }
                                    }
                                    pstmt_inner.close();
                                }
                            }
                        }//
                    }

                } // else

            } // while

        } // try
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, conn);

            } catch (SQLException e) {
            }
        }
        return flg;
    }

    public String compareDates(String dtRef, String dt) {
        String val = "NA";
        try {
            System.out.println("date1 : " + (dtRef));
            System.out.println("dateRef : " + (dt));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(dt);
            Date dateRef = sdf.parse(dtRef);

			//logMgr.accessLog("date1 : " + sdf.format(date1));
            //logMgr.accessLog("dateRef : " + sdf.format(dateRef));
            System.out.println("date1 : " + sdf.format(date1));
            System.out.println("dateRef : " + sdf.format(dateRef));

            if (date1.after(dateRef)) {
                // logMgr.accessLog("Date1 is after DateRef");
                val = "after";
            }

            if (date1.before(dateRef)) {
                // logMgr.accessLog("Date1 is before DateRef");
                val = "before";
            }

            if (date1.equals(dateRef)) {
                // logMgr.accessLog("Date1 is equal DateRef");
                val = "equal";
            }
        } catch (Exception e) {
            logMgr.accessLog("could not compare dates");
            e.printStackTrace();
        }

        return val;

    }

    public static void main(String args[]) {
        try {
            StatusChangeOfAdv st = new StatusChangeOfAdv("emaillogid");
            st.UpdateAdvt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
