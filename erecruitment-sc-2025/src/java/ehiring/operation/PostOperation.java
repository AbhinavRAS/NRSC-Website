package ehiring.operation;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ehiring.action.WriteToExcel;
import ehiring.action.ZipDirectory;
import ehiring.dao.CategoryData;
import ehiring.dao.LogManager;
import ehiring.dao.PostData;
import ehiring.db.DBConnectionManager;

import java.sql.ResultSetMetaData;
import java.util.Collections;
import java.util.Enumeration;

public class PostOperation {
    //static ArrayList<HashMap<String, String>> rs_post_data = null;
    //static boolean rs_post_data_valid = false;
    public DBConnectionManager conMgr = null;
    public String lPoolName = "recruit";
    LogManager logMgr;
    String emailLogId = new String();

    public PostOperation(String emailLogId) {
        conMgr = DBConnectionManager.getInstance();
        logMgr = LogManager.getInstance(emailLogId);
        this.emailLogId = emailLogId;
    }

    public ArrayList getPostNos(String advt) throws SQLException {
        ArrayList arr = new ArrayList();
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement(
                    "select * from post_data where status in ('active','inactive')  and  advt_no=? order by post_no",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, advt);
            logMgr.accessLog("pstmt for post nos:" + pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                arr.add(rs.getString("post_no"));
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

    public String getUserType(String userId) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String userType = new String();

        try {
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement(
                    "select user_type from registration where name=? ",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, userId);
            logMgr.accessLog("pstmt for user_type:" + pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userType = rs.getString("user_type");
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
        return userType;
    }

    public String getStatusOfAdvt(String advt_no) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String userType = new String();

        try {
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement(
                    "select status from advt_data where advt_no=? ",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, advt_no);
            logMgr.accessLog("status of advt :" + pstmt.toString());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userType = rs.getString("status");
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
        return userType;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public int addPost(PostData pd, CategoryData categorydata) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String categoryName = new String();
        LinkedHashMap<String, ArrayList> categoryMap;
        ArrayList<String> arr = new ArrayList<>();
        int i = 0, j = 0, minAge, maxAge;
        AcademicEligibilityOperation acd = new AcademicEligibilityOperation(emailLogId);
        String details = pd.getElgDetails();
        StringTokenizer st = new StringTokenizer(details, "|");

        logMgr.accessLog("Inside addpost method ........");
        try {
            con = conMgr.getConnection(lPoolName);
            con.setAutoCommit(false);
            int count = 1;

            while (st.hasMoreTokens()) {
                String eachRecord = st.nextToken();
                logMgr.accessLog("THE DETAILS IS:" + eachRecord.toString());
                { // EACH RECORD
                    StringTokenizer stn = new StringTokenizer(eachRecord, "#");
                    // String elgId = stn.nextToken();
                    String qual = stn.nextToken();
                    pd.setEssential_qualification(qual);
                    pd.setEligibility(acd.getEligibilityFromQual(qual));
                    pstmt = con.prepareStatement(
                            "select count(*) from post_data where post_no=? and advt_no=? and status=? ");
                    pstmt.setString(1, pd.getPost_no());
                    pstmt.setString(2, pd.getAdvt_no());
                    pstmt.setString(3, pd.getStatus());
                    // pstmt.setString(4, pd.getEligibility());
                    logMgr.accessLog("addPost is ....." + pstmt.toString());

                    rs = pstmt.executeQuery();

                    logMgr.accessLog("addPost is rs.next ....." + rs.next());
                    if (rs.next()) {
                        logMgr.accessLog("addPost is .rs.get(1)...." + rs.getInt(1));
                        if (rs.getInt(1) > 0) {
                            logMgr.accessLog("Post No. : " + pd.getPost_no() + " in Advt No. : " + pd.getAdvt_no()
                                    + " in Eligibility No. : " + pd.getEligibility() + " is already available.");
                            return -1;
                        }
                    }

                    pd.setMandate(stn.nextToken());
                    pd.setDiscipline(stn.nextToken());
                    String specialization = new String();
                    specialization = stn.nextToken();

                    // specialization = specialization.replaceAll("@", "|");
                    pd.setSpecialization(specialization);

                }
                pstmt.close();

                if (count == 1) {

                    pstmt = con.prepareStatement("insert into post_data(post_no,advt_no,post_name,no_of_vacancies,"
                            + "	desirable_qualification,status,net,date_of_status_update,experience,remarks,select_process)"
                            + " Values(?,?,?,?,?,?,?,?,?,?,?)");
                    logMgr.accessLog("pstmt postdata:" + pstmt.toString());

                    pstmt.setString(1, pd.getPost_no());
                    pstmt.setString(2, pd.getAdvt_no());
                    pstmt.setString(3, pd.getPost_name());
                    pstmt.setInt(4, pd.getNo_of_vacancies());
                    pstmt.setString(5, pd.getDesirable_qualification());
                    pstmt.setString(6, pd.getStatus());
                    pstmt.setString(7, pd.getNet());
                    pstmt.setDate(8, pd.getDate_of_status_update());
                    pstmt.setString(9, pd.getExperience());
                    pstmt.setString(10, pd.getRemarks());
                    pstmt.setString(11, pd.getSelect_process());
                    i = pstmt.executeUpdate();
                    logMgr.accessLog("pstmt i values:" + i);
                    pstmt.close();
                }

                // /**
                // * *
                // */
                // pstmt = con.prepareStatement("select count(*) from post_data where post_no='"
                // + pd.getPost_no()
                // + "' and advt_no='" + pd.getAdvt_no() + "'");
                // logMgr.accessLog(" stmt is for:" + pstmt.toString());
                // rs = pstmt.executeQuery();
                //
                // if (rs.next()) {
                // logMgr.accessLog("Post No. : " + pd.getPost_no() + " in Advt No. : " +
                // pd.getAdvt_no()
                // + " in Eligibility No. : " + pd.getEligibility() + " is already available.");
                //
                // }
                // /**
                // * ***
                // */
                if (i > 0) {
                    ArrayList arr_Elg = new ArrayList();

                    if (pd.getEligibility().indexOf("Integrated") >= 0) {

                        if (pd.getEssential_qualification().indexOf("M.") >= 0) {
                            arr_Elg.add("Graduate");
                            arr_Elg.add("Postgraduate");
                        } else if (pd.getEssential_qualification().indexOf("Phd") >= 0) {
                            arr_Elg.add("Postgraduate");
                            arr_Elg.add("PhD");
                        }
                    } else {
                        arr_Elg.add(pd.getEligibility());
                    }

                    logMgr.accessLog("INTEGRATED .......arrayList:" + arr_Elg.toString());

                    for (int elg = 0; elg < arr_Elg.size(); ++elg) {
                        pstmt = con.prepareStatement("insert into post_data_details(post_no,advt_no,eligibility_no,"
                                + "eligibility,essential_qualification,discipline,specialization,mandate,status,date_of_status_update)"
                                // + " Values(?,?,?,?,?,?,?,?)");
                                + " Values ('" + pd.getPost_no() + "', '" + pd.getAdvt_no() + "'," + count + ",'"
                                + arr_Elg.get(elg) + "','" + pd.getEssential_qualification() + "','"
                                + pd.getDiscipline() + "','" + pd.getSpecialization() + "','" + pd.getMandate() + "','"
                                + pd.getStatus() + "','" + pd.getDate_of_status_update() + "')");
                        logMgr.accessLog("pstmt postdata details:" + count + ", stmt is for:" + pstmt.toString());

                        i = pstmt.executeUpdate();
                        count++;
                    }

                }
                // count++;

            }

            if (i > 0) {
                categoryMap = categorydata.getCategoryMap();
                logMgr.accessLog("Category values...." + categoryMap.toString());
                Iterator<Entry<String, ArrayList>> hmIterator = categoryMap.entrySet().iterator();

                while (hmIterator.hasNext()) {
                    Map.Entry mapElement = (Map.Entry) hmIterator.next();
                    categoryName = (String) mapElement.getKey();
                    logMgr.accessLog("THE STRING is categoryName :" + categoryName);
                    arr = (ArrayList<String>) categoryMap.get(categoryName);

                    PreparedStatement ps = con.prepareStatement("insert into category_data(advt_no,post_no,category,"
                            + "age_limit_lower,age_limit_upper,status,date_of_status_update) Values(?,?,?,?,?,?,?)");

                    minAge = Integer.parseInt(arr.get(0).toString());
                    maxAge = Integer.parseInt(arr.get(1).toString());

                    ps.setString(1, pd.getAdvt_no());
                    ps.setString(2, pd.getPost_no());
                    ps.setString(3, categoryName);
                    ps.setInt(4, minAge);
                    ps.setInt(5, maxAge);
                    ps.setString(6, pd.getStatus());
                    ps.setDate(7, pd.getDate_of_status_update());

                    ps.setString(6, pd.getStatus());

                    i = ps.executeUpdate();
                    ps.close();

                }

            }
            // pstmt.close();
            con.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            logMgr.accessLog(" THE VALUE OF I IS :" + i);
            con.rollback();
            return i;
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        // con.close();
        return i;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public int editPost(PostData pd, CategoryData categorydata) throws SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        String categoryName = new String();
        LinkedHashMap<String, ArrayList> categoryMap;
        ArrayList<String> arr = new ArrayList<>();

        int i = 0, j = 0, minAge, maxAge;
        try {

            logMgr.accessLog("Inside editPost method ........");
            con = conMgr.getConnection(lPoolName);

            pstmt = con.prepareStatement("update post_data set post_name = ? and no_of_vacancies = ? and"
                    + "	eligibility = ? and essential_qualification = ? and discipline = ? and"
                    + " specialization = ? and desirable_qualification = ? where advt_no = ? and post_no = ?");

            pstmt.setString(1, pd.getPost_name());
            pstmt.setInt(2, pd.getNo_of_vacancies());
            pstmt.setString(3, pd.getEligibility());
            pstmt.setString(4, pd.getEssential_qualification());
            pstmt.setString(5, pd.getDiscipline());
            pstmt.setString(6, pd.getSpecialization());
            pstmt.setString(7, pd.getDesirable_qualification());
            pstmt.setString(8, pd.getAdvt_no());
            pstmt.setString(9, pd.getPost_no());

            i = pstmt.executeUpdate();

            if (i > 0) {
                categoryMap = categorydata.getCategoryMap();
                logMgr.accessLog("Category values...." + categoryMap.toString());

                Iterator<Entry<String, ArrayList>> hmIterator = categoryMap.entrySet().iterator();

                while (hmIterator.hasNext()) {
                    Map.Entry mapElement = (Map.Entry) hmIterator.next();
                    categoryName = (String) mapElement.getKey();
                    logMgr.accessLog("THE STRING is categoryName :" + categoryName);
                    arr = (ArrayList<String>) categoryMap.get(categoryName);

                    PreparedStatement ps = con.prepareStatement("update category_data set category = ? "
                            + " and age_limit_lower = ? and age_limit_upper = ? where advt_no = ? and "
                            + " post_no = ?");

                    minAge = Integer.parseInt(arr.get(0).toString());
                    maxAge = Integer.parseInt(arr.get(1).toString());

                    ps.setString(1, categoryName);
                    ps.setInt(2, minAge);
                    ps.setInt(3, maxAge);
                    ps.setString(4, pd.getAdvt_no());
                    ps.setString(5, pd.getPost_no());

                    j = ps.executeUpdate();
                    ps.close();

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {

                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (SQLException e) {
            }
        }
        return j;
    }

    public int getAllHighEdQualForPost(String advt_no, String post_no) {
        int count = 0;
        Connection con_inner = null;
        ResultSet rs_inner = null;
        PreparedStatement pstmt_inner = null;

        try {
            con_inner = conMgr.getConnection(lPoolName);
            String qry_inner = "select count(*) from post_data_details where advt_no='" + advt_no + "' and post_no='"
                    + post_no + "' and  essential_qualification not in ('X','XII')";
            logMgr.accessLog("qry inner in getAllHighEdQualForPost :" + qry_inner);
            pstmt_inner = con_inner.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs_inner = pstmt_inner.executeQuery();
            while (rs_inner.next()) {
                count = rs_inner.getInt(1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                pstmt_inner.close();
                conMgr.freeConnection(lPoolName, con_inner);
            } catch (SQLException e) {
            }
        }
        return count;
    }

    public int getMaxHighEdQualForPost(String advt_no, String post_no) {
        int count = 0;
        Connection con_inner = null;
        ResultSet rs_inner = null;
        PreparedStatement pstmt_inner = null;
        ArrayList arr = new ArrayList();

        try {
            con_inner = conMgr.getConnection(lPoolName);
            String qry_inner = "select eligibility from post_data_details where advt_no='" + advt_no + "' and post_no='"
                    + post_no + "' and  essential_qualification not in ('X','XII')";
            logMgr.accessLog("qry inner in getAllHighEdQualForPost :" + qry_inner);
            pstmt_inner = con_inner.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs_inner = pstmt_inner.executeQuery();
            while (rs_inner.next()) {
                String elg = rs_inner.getString("eligibility");
                arr.add(elg);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                pstmt_inner.close();
                conMgr.freeConnection(lPoolName, con_inner);
            } catch (SQLException e) {
            }
        }
        logMgr.accessLog("ARRAY TO STRING :" + arr.toString());
        logMgr.accessLog("ARRAY Size :" + arr.size());
        if (arr.size() == 0) {
            return arr.size();
        } else if (arr.contains("PostDoctoral")) {
            return 4;
        } else if (arr.contains("PhD")) {
            return 3;
        } else if (arr.contains("Postgraduate")) {
            return 2;
        } //else if (arr.contains("Graduate"))
        else {
            return 1;
        }

    }

    @SuppressWarnings({"rawtypes", "unused", "unchecked"})
    public ArrayList<LinkedHashMap<String, Object>> getAdvtData(String advt_no, String post_no) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String net = new String();
        ArrayList<LinkedHashMap<String, Object>> arr = new ArrayList<LinkedHashMap<String, Object>>();
        String startDt = new String();
        String endDt = new String();

        Connection con_inner = null;
        ResultSet rs_inner = null;
        PreparedStatement pstmt_inner = null;

        try {
            con = conMgr.getConnection(lPoolName);
            con_inner = conMgr.getConnection(lPoolName);

            // String qry = "select * from advt_data where status in ('active','inactive')
            // and advt_no='" + advt_no + "'";
            String qry = "select * from advt_data where advt_no='" + advt_no + "'";
            logMgr.accessLog("qry in getRequired .... AdverstimentPost :" + qry);
            pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                startDt = rs.getString("valid_from");
                endDt = rs.getString("valid_till");
            }

            rs.close();
            pstmt.close();
            con.close();

            con = conMgr.getConnection(lPoolName);

            qry = "select * from post_data where status in ('active','inactive') and advt_no='" + advt_no
                    + "' and post_no='" + post_no + "'";
            logMgr.accessLog("qry in getRequired .... AdverstimentPost :" + qry);
            pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                advt_no = rs.getString("advt_no");
                post_no = rs.getString("post_no");
                net = rs.getString("net");

                /*
                 * String qry_inner =
                 * "select distinct(essential_qualification),discipline,specialization,mandate from post_data_details where advt_no='"
                 *///
				/*String qry_inner = "SELECT  * FROM post_data_details  JOIN (VALUES ('X',1),('XII',2),('ITI',3),('DIPLOMA',4),('Graduate',5),('Postgraduate',6),('PhD',7),('PostDoctoral',8)) as x(eligibility,ordering) ON post_data_details.eligibility=x.eligibility "
                 + " where advt_no='" + advt_no + "' and post_no='" + post_no
                 + "' and status in ('active','inactive') ";*/
                String qry_inner = "SELECT  * FROM post_data_details where advt_no='" + advt_no + "' and post_no='" + post_no
                        + "' and status in ('active','inactive') ORDER BY idx(array['X','XII','Graduate','Postgraduate','PhD','PostDoctoral'],post_data_details.eligibility)";

                logMgr.accessLog("qry inner in .... getAllAdverstimentPost :" + qry_inner);
                pstmt_inner = con_inner.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                rs_inner = pstmt_inner.executeQuery();
                ArrayList<HashMap<String, String>> arr_inner = new ArrayList<HashMap<String, String>>();
                while (rs_inner.next()) {
                    HashMap<String, String> mp_inner = new HashMap<String, String>();
                    String essenQual = rs_inner.getString("essential_qualification");
                    if (essenQual.startsWith("Integrated")) {
                        if (rs_inner.getString("eligibility").equalsIgnoreCase("Graduate")) {
                            continue;
                        }
                    }
                    // mp_inner.put("eligibility", rs_inner.getString("eligibility"));
                    mp_inner.put("essential_qualification", essenQual);
                    mp_inner.put("discipline", rs_inner.getString("discipline"));
                    mp_inner.put("specialization", rs_inner.getString("specialization"));
                    mp_inner.put("mandatory", rs_inner.getString("mandate"));
                    arr_inner.add(mp_inner);
                }
                // logMgr.accessLog("arr_inner:" + arr_inner);
                rs_inner.close();
                pstmt_inner.close();

                qry_inner = "select * from category_data where advt_no='" + advt_no + "' and post_no='" + post_no
                        + "' and status in ('active','inactive') ";
                // logMgr.accessLog("qry inner category in getAllAdverstimentPost :" +
                // qry_inner);
                pstmt_inner = con_inner.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                rs_inner = pstmt_inner.executeQuery();
                ArrayList<HashMap<String, String>> arr_categ = new ArrayList<HashMap<String, String>>();
                while (rs_inner.next()) {
                    HashMap<String, String> mp_inner = new HashMap<String, String>();
                    mp_inner.put("category", rs_inner.getString("category"));
                    mp_inner.put("age_limit_lower", rs_inner.getString("age_limit_lower"));
                    mp_inner.put("age_limit_upper", rs_inner.getString("age_limit_upper"));
                    mp_inner.put("status", rs_inner.getString("status"));
                    arr_categ.add(mp_inner);
                }
                // logMgr.accessLog("arr_categ category:" + arr_categ.toString());
                rs_inner.close();
                pstmt_inner.close();

                LinkedHashMap<String, Object> mp = new LinkedHashMap<String, Object>();
                mp.put("advt_no", advt_no);
                mp.put("post_no", post_no);
                mp.put("post_name", rs.getString("post_name"));
                mp.put("no_of_vacancies", rs.getString("no_of_vacancies"));
                mp.put("status", rs.getString("status"));
                mp.put("experience", rs.getString("experience"));
                mp.put("select_process", rs.getString("select_process"));
                mp.put("remarks", rs.getString("remarks"));
                mp.put("desirable_qual", rs.getString("desirable_qualification"));
                mp.put("net", rs.getString("net"));// National Eligibility test
                mp.put("start", this.formatDate("yyyy-MM-dd", startDt));// valid from
                mp.put("end", this.formatDate("yyyy-MM-dd", endDt));// valid till
                mp.put("Eligibility", arr_inner);
                mp.put("Category", arr_categ);
                arr.add(mp);
            }

            rs.close();
            pstmt.close();
            // con_inner.close();
            // con.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

                // pstmt.close();
                conMgr.freeConnection(lPoolName, con);
                conMgr.freeConnection(lPoolName, con_inner);
            } catch (Exception e) {
            }
        }
        // logMgr.accessLog("arr....."+arr.toString());
        return arr;
    }

    // *****//
    @SuppressWarnings({"rawtypes", "unused", "unchecked"})
    public ArrayList<LinkedHashMap<String, Object>> getApplicantData(String advt_no, String post_no) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        ArrayList<LinkedHashMap<String, Object>> arr = new ArrayList<LinkedHashMap<String, Object>>();

        ArrayList per_Arr = new ArrayList();
        ArrayList x_Arr = new ArrayList();
        ArrayList xii_Arr = new ArrayList();
        ArrayList iti_Arr = new ArrayList();
        ArrayList higher_Arr = new ArrayList();
        ArrayList exp_Arr = new ArrayList();
        ArrayList net_Arr = new ArrayList();

        try {
            logMgr.accessLog("Inside GET get Required ....AdverstimentPost OPTIONS");
            con = conMgr.getConnection(lPoolName);

            // int highEdCnt = getAllHighEdQualForPost(advt_no, post_no);
            int highEdCnt = getMaxHighEdQualForPost(advt_no, post_no);
            boolean govtExpFlag = false;

            String qry = "select * from personal_data where advt_no='" + advt_no + "' and post_no='" + post_no
                    + "' and status='POSTED'";
            logMgr.accessLog("qry in getRequired .... getApplicantData :" + qry);
            pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            // int columnsNumber = rsmd.getColumnCount();
            // LinkedHashMap mp = new LinkedHashMap();

            while (rs.next()) {
                String reqId = rs.getString("registration_id");
                String emailId = rs.getString("email");
                // logMgr.accessLog("emailId_repl is with first :"+emailId);
                String emailId_repl = emailId.replace(".", "_");
                // logMgr.accessLog("emailId_repl is with dot :"+emailId_repl);
                emailId_repl = emailId_repl.replace("@", "_");
                // logMgr.accessLog("emailId_repl is @ :"+emailId_repl);
                String[] emailUserName = emailId.split("@");
                String app_user = emailUserName[0];
                // logMgr.accessLog("app_user :"+app_user);

                int itiSize = 0;

                logMgr.accessLog("AFTER SPLIT... report...:" + emailUserName[0]);
                LinkedHashMap mp = new LinkedHashMap();
                logMgr.accessLog("REGISTRATION ID IS ..................................:" + reqId);

                per_Arr = getApplicantTable(reqId, "personal_data", advt_no, post_no);
                x_Arr = getApplicantTable(reqId, "x_data", advt_no, post_no);
                xii_Arr = getApplicantTable(reqId, "xii_data", advt_no, post_no);
                iti_Arr = getApplicantTable(reqId, "iti_diploma", advt_no, post_no);
                higher_Arr = getApplicantTable(reqId, "highereducation", advt_no, post_no);
                exp_Arr = getApplicantTable(reqId, "experience", advt_no, post_no);
                net_Arr = getApplicantTable(reqId, "nationalexamtest", advt_no, post_no);

                String govtExp = "NA";
                govtExpFlag = true;
                for (int i = 0; i < exp_Arr.size(); ++i) {
                    HashMap exp_mp = (HashMap) exp_Arr.get(i);
                    logMgr.accessLog(" the govt ddd.. before ..>" + exp_mp.get("exp_govt") + "," + reqId);

                    if ((exp_mp.get("exp_govt")).toString().equalsIgnoreCase("Yes")) {
                        govtExp = "Yes";
                        //govtExpFlag=true;
                        break;
                    } else {
                        govtExp = "No";
                    }
                }

                mp.put("applicant", app_user);
                mp.put("email_replaced", emailId_repl);

                mp.put("HighEdCnt", highEdCnt);
                mp.put("ITISize", iti_Arr.size());
                mp.put("Personal", per_Arr);
                mp.put("X", x_Arr);
                mp.put("XII", xii_Arr);
                mp.put("ITI", iti_Arr);
                mp.put("Higher", higher_Arr);
                mp.put("NationalExam", net_Arr);
                mp.put("Experience", exp_Arr);
                logMgr.accessLog(" the govt ddd. after ...>" + govtExp + "," + reqId);
                mp.put("GovtExperience", govtExp);
                mp.put("GovtExpFlag", govtExpFlag);

                logMgr.accessLog("MP VALUE IS ....:" + mp.toString());

                arr.add(mp);

                per_Arr = new ArrayList();
                x_Arr = new ArrayList();
                xii_Arr = new ArrayList();
                iti_Arr = new ArrayList();
                higher_Arr = new ArrayList();
                exp_Arr = new ArrayList();
                net_Arr = new ArrayList();
            }

            rs.close();
            pstmt.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    // *****//
    @SuppressWarnings({"rawtypes", "unused", "unchecked"})
    public String DownloadApplicantData(String advt_no, String post_no, String flName) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        ArrayList arr = new ArrayList();

        ArrayList per_Arr = new ArrayList();
        ArrayList x_Arr = new ArrayList();
        ArrayList xii_Arr = new ArrayList();
        ArrayList iti_Arr = new ArrayList();
        ArrayList higher_Arr = new ArrayList();
        ArrayList exp_Arr = new ArrayList();
        ArrayList net_Arr = new ArrayList();

        try {
            logMgr.accessLog("Inside GET get Required ....AdverstimentPost OPTIONS");
            con = conMgr.getConnection(lPoolName);

            int highEdCnt = getAllHighEdQualForPost(advt_no, post_no);

            String qry = "select * from personal_data where advt_no='" + advt_no + "' and post_no='" + post_no
                    + "'  and status='POSTED' ";
            logMgr.accessLog("qry in getRequired .... getApplicantData :" + qry);
            pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            // int columnsNumber = rsmd.getColumnCount();
            // LinkedHashMap mp = new LinkedHashMap();

            ArrayList colNames = new ArrayList();
            colNames.addAll(getColumnNames("personal_data"));

            int cnt = 0;
            while (rs.next()) {
                String reqId = rs.getString("registration_id");
                String emailId = rs.getString("email");
                String[] emailUserName = emailId.split("@");
                String app_user = emailUserName[0];
                logMgr.accessLog("AFTER SPLIT... report...:" + emailUserName[0]);
                LinkedHashMap mp = new LinkedHashMap();
                logMgr.accessLog("REGISTRATION ID IS ..................................:" + reqId);

                per_Arr = getApplicantTableValues(reqId, "personal_data", advt_no, post_no);
                x_Arr = getApplicantTableValues(reqId, "x_data", advt_no, post_no);
                xii_Arr = getApplicantTableValues(reqId, "xii_data", advt_no, post_no);
                iti_Arr = getApplicantTableValues(reqId, "iti_diploma", advt_no, post_no);
                higher_Arr = getApplicantTableValues(reqId, "highereducation", advt_no, post_no);
                exp_Arr = getApplicantTableValues(reqId, "experience", advt_no, post_no);
                net_Arr = getApplicantTableValues(reqId, "nationalexamtest", advt_no, post_no);

                // mp.put("applicant", app_user);
                // mp.put("HighEdCnt", highEdCnt);
                // mp.put("Personal", per_Arr);
                // mp.put("X", x_Arr);
                // mp.put("ITI", iti_Arr);
                // mp.put("Higher", higher_Arr);
                // mp.put("Experience", exp_Arr);
                ArrayList arrInd = new ArrayList();
                arrInd.addAll(per_Arr);
                if (x_Arr.size() > 0) {
                    if (cnt == 0) {
                        colNames.addAll(getColumnNames("x_data"));
                    }
                    arrInd.addAll(x_Arr);
                }

                if (xii_Arr.size() > 0) {
                    if (cnt == 0) {
                        colNames.addAll(getColumnNames("xii_data"));
                    }
                    arrInd.addAll(xii_Arr);
                }

                if (iti_Arr.size() > 0) {
                    if (cnt == 0) {
                        colNames.addAll(getColumnNames("iti_diploma"));
                    }
                    arrInd.addAll(iti_Arr);
                }

                if (higher_Arr.size() > 0) {
                    if (cnt == 0) {
                        for (int h = 0; h < highEdCnt; ++h) {
                            colNames.addAll(getColumnNames("highereducation"));
                        }

                    }
                    arrInd.addAll(higher_Arr);
                }

                if (exp_Arr.size() > 0) {
                    if (cnt == 0) {
                        for (int h = 0; h < 3; ++h) {
                            colNames.addAll(getColumnNames("experience"));
                        }
                    }
                    arrInd.addAll(exp_Arr);
                }
                if (net_Arr.size() > 0) {
                    if (cnt == 0) {
                        for (int h = 0; h < 3; ++h) {
                            colNames.addAll(getColumnNames("nationalexamtest"));
                        }
                    }
                    arrInd.addAll(net_Arr);
                }

                if (cnt == 0) {
                    arr.add(colNames);
                }

                logMgr.accessLog("HELLLLLL:" + arrInd.toString());

                arr.add(arrInd);
                cnt++;
            }

            rs.close();
            pstmt.close();
            con.close();

            logMgr.accessLog(".......................:" + arr.toString() + "," + arr.size());
            if (arr.size() > 0) {
                WriteToExcel wte = new WriteToExcel();
                wte.writeFileApplicant(arr, flName);
            } else {
                return "none";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Successfully Downloaded";
    }

    // *****//
    @SuppressWarnings({"rawtypes", "unused", "unchecked"})
    public String DownloadApplicantCert(String advt_no, String post_no, String email, String regId, String filePath,
            String zipName) {
        ZipDirectory zd = new ZipDirectory(email);
        String msg = new String();
        try {
            File directoryToZip = new File(filePath);
            File zipFilePath = new File(zipName);
            if (!zipFilePath.exists()) {
                zipFilePath.mkdirs();
            }

            List<File> fileList = new ArrayList<File>();
            logMgr.accessLog("---Getting references to all files in: " + directoryToZip.toString() + "......"
                    + filePath.toString());
            if (zd.getAllFiles(directoryToZip, fileList, email)) {
                logMgr.accessLog("---Creating zip file");
                msg = zd.writeZipFile(zipFilePath, regId, fileList);
                logMgr.accessLog("---Done");
            } else {
                return "none";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ArrayList<LinkedHashMap> getApplicantAdvtData() {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList<LinkedHashMap> arr = new ArrayList<LinkedHashMap>();

        try {
            logMgr.accessLog("Inside getApplicantAdvtData method ........");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement("select * from post_data where status = 'active' order by advt_no, post_data");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LinkedHashMap map = new LinkedHashMap();
                map.put("Advt No.", rs.getString("advt_no").replaceAll("\\|", ", "));
                map.put("Post No.", rs.getString("post_no").replaceAll("\\|", ", "));
                map.put("net", rs.getString("net").replaceAll("\\|", ", "));
                map.put("Post Name", rs.getString("post_name").replaceAll("\\|", ", "));
                map.put("Vacancies", rs.getString("no_of_vacancies").replaceAll("\\|", ", "));
                map.put("Essential Qualification", rs.getString("essential_qualification").replaceAll("\\|", ", "));
                map.put("Discipline", rs.getString("discipline").replaceAll("\\|", ", "));
                map.put("Specialization", rs.getString("specialization").replaceAll("\\|", ", "));
                map.put("Academic Eligibility", rs.getString("eligibility").replaceAll("\\|", ", "));
                arr.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    public ArrayList<LinkedHashMap<String, Object>> getAllAdverstimentPost(String eMail, String aid) { // ChangeId: 2023120101
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        ArrayList<LinkedHashMap<String, Object>> arr = new ArrayList<LinkedHashMap<String, Object>>();
        String advt_no = new String();
        String post_no = new String();
        String net = new String();
        String post_spec = new String(); // ChangeId: 2024010101

        Connection con_inner = null;
        ResultSet rs_inner = null;
        PreparedStatement pstmt_inner = null;
        
        // Start: ChangeId: 2023120101
        LinkedHashMap<String, String> mapAdvtId = new LinkedHashMap<String, String>();
        mapAdvtId.put("fb0438c8598802e496f2388f65b3b20b","('NRSC-RMT-4-2023')");
        mapAdvtId.put("3e186d4cea36196afac3be9977169e09","('NRSC-RMT-5-2023')");
        mapAdvtId.put("91bbfc83c8f96dfad15703bc699189c7","('NRSC-RMT-4-2023','NRSC-RMT-5-2023')");
        advt_no = mapAdvtId.get(aid); // ChangeId: 2023120101
        // End: ChangeId: 2023120101

        try {
            con = conMgr.getConnection(lPoolName);
            logMgr.accessLog("Inside GET getAllAdverstimentPost OPTIONS");
            con_inner = conMgr.getConnection(lPoolName);
            /* // Start: ChangeId: 2025041901
            // Start: ChangeId: 2023120401
            String qry = "";
            if( advt_no != null ){
                qry = "select * from post_data where status='active' and advt_no in "+advt_no+" order by advt_no,post_no"; // ChangeId: 2023120101
            }
            else{
                qry = "select * from post_data where status='active' order by advt_no,post_no"; // ChangeId: 2023120101
                // ChangeId: 2024012202
                //qry = "select * from post_data inner join personal_data on post_data.post_no = personal_data.post_no and post_data.advt_no = personal_data.advt_no where personal_data.status in ('POSTED','PAYMENT','PAID','PENDING','PAYMENTFAILED','FORWARDED') and email='"+eMail+"' order by post_data.advt_no,post_data.post_no";
            }    
            // End: ChangeId: 2023120401
            // End: ChangeId: 2025041901 */
            
            // Start: ChangeId: 2025041901
            String qry = "";
            if( advt_no != null ){
                qry = "select * from post_data where status='active' and advt_no in ? order by advt_no,post_no"; // ChangeId: 2023120101
                pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                pstmt.setString(1, advt_no);
            }
            else{
                qry = "select * from post_data where status='active' order by advt_no,post_no"; // ChangeId: 2023120101
                pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            }    
            // End: ChangeId: 2025041901
            
            logMgr.accessLog("qry in getAllAdverstimentPost :" + qry);
            /*
            if(rs_post_data_valid == false)
            {
                rs_post_data = new ArrayList<HashMap<String, String>>();
                //pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                //rs = pstmt.executeQuery();
                System.out.println("Loading post_data from DB");
            }
            else
            {
                System.out.println("Reusing post_data result");
            }
            */
            
            rs = pstmt.executeQuery();
            //int rowidx=0;
            //Enumeration<HashMap<String, String>> e
            //= Collections.enumeration(rs_post_data);
            //HashMap<String, String> reuse;
            //while ( (rs_post_data_valid == true && e.hasMoreElements() ) || ( rs_post_data_valid == false && rs.next() )) {
            while ( rs.next() ) {
                advt_no = rs.getString("advt_no");
                post_no = rs.getString("post_no");
                net = rs.getString("net");
                post_spec = rs.getString("post_spec"); // ChangeId: 2024010101
                
                /*
                if(rs_post_data_valid == false)
                {
                    advt_no = rs.getString("advt_no");
                    post_no = rs.getString("post_no");
                    net = rs.getString("net");
                    post_spec = rs.getString("post_spec"); // ChangeId: 2024010101
                    
                    HashMap<String, String> post_data_field = new HashMap<String, String>();
                    post_data_field.put("advt_no",advt_no);
                    post_data_field.put("post_no",post_no);
                    post_data_field.put("net",net);
                    post_data_field.put("post_spec",post_spec);
                    rs_post_data.add(post_data_field);
                }
                else
                {
                    HashMap<String, String> post_data_field = e.nextElement();
                    advt_no = post_data_field.get("advt_no");
                    post_no = post_data_field.get("post_no");
                    net = post_data_field.get("net");
                    post_spec = post_data_field.get("post_spec");
                }
                */
                
                /* ChangeId: 2025041901
                String qry_inner = "select * from post_data_details where advt_no='" + advt_no + "' and post_no='"
                        + post_no + "' and status='active'";
                */
                String qry_inner = "select * from post_data_details where advt_no=? and post_no=? and status='active'"; // ChangeId: 2025041901
                // logMgr.accessLog("qry inner in getAllAdverstimentPost :" + qry_inner);
                pstmt_inner = con.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                pstmt_inner.setString(1, advt_no); // ChangeId: 2025041901
                pstmt_inner.setString(2, post_no); // ChangeId: 2025041901
                rs_inner = pstmt_inner.executeQuery();
                ArrayList<HashMap<String, String>> arr_inner = new ArrayList<HashMap<String, String>>();
                while (rs_inner.next()) {
                    HashMap<String, String> mp_inner = new HashMap<String, String>();
                    mp_inner.put("eligibility", rs_inner.getString("eligibility"));
                    mp_inner.put("essential_qualification", rs_inner.getString("essential_qualification"));
                    mp_inner.put("discipline", rs_inner.getString("discipline"));
                    mp_inner.put("specialization", rs_inner.getString("specialization"));
                    mp_inner.put("mandatory", rs_inner.getString("mandate"));

                    arr_inner.add(mp_inner);
                }
                // logMgr.accessLog("arr_inner:" + arr_inner);
                rs_inner.close();
                pstmt_inner.close();

                ArrayList<HashMap<String, String>> cat_arr = new ArrayList<HashMap<String, String>>();
                /* ChangeId: 2025041901
                qry_inner = "select * from category_data where advt_no='" + advt_no + "' and post_no='" + post_no
                        + "' and status='active'";
                */
                // logMgr.accessLog("qry category_data in getAllAdverstimentPost :" +
                // qry_inner);
                qry_inner = "select * from category_data where advt_no=? and post_no=? and status='active'"; // ChangeId: 2025041901
                pstmt_inner = con.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                pstmt_inner.setString(1, advt_no); // ChangeId: 2025041901
                pstmt_inner.setString(2, post_no); // ChangeId: 2025041901
                rs_inner = pstmt_inner.executeQuery();
                while (rs_inner.next()) {
                    HashMap<String, String> mp_inner = new HashMap<String, String>();
                    mp_inner.put("category", rs_inner.getString("category"));
                    mp_inner.put("age_limit_lower", rs_inner.getString("age_limit_lower"));
                    mp_inner.put("age_limit_upper", rs_inner.getString("age_limit_upper"));
                    cat_arr.add(mp_inner);
                }
                // logMgr.accessLog("cat_arr:" + cat_arr.toString());
                rs_inner.close();
                pstmt_inner.close();

                String reg_id = "-";
                String preview_file = "-";
                String applied_date = "-";
                String reference_date = "-";
                String application_status = ""; //ChangeID:2023081001 PaymentChange
                
                /* ChangeId: 2025041901
                qry_inner = "select registration_id,preview_file,date_of_application,status from personal_data where advt_no='"
                        + advt_no + "' and post_no='" + post_no + "' and lower(email)='" + eMail.toLowerCase() + "' and status in ('POSTED','PAYMENT','PAID','PENDING','PAYMENTFAILED','FORWARDED')"; // ChangeId: 2023121402
                */
                // ChangeId: 2025041901
                qry_inner = "select registration_id,preview_file,date_of_application,status from personal_data where advt_no=? and post_no=? and lower(email)=? and status in ('POSTED','PAYMENT','PAID','PENDING','PAYMENTFAILED','FORWARDED','PAYMNTFAILED')"; // ChangeId: 2023121402, 2025052001
                
                // logMgr.accessLog("qry registration_id..done. in getAllAdverstimentPost :" +
                // qry_inner);
                pstmt_inner = con.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                pstmt_inner.setString(1, advt_no); // ChangeId: 2025041901
                pstmt_inner.setString(2, post_no); // ChangeId: 2025041901
                pstmt_inner.setString(3, eMail.toLowerCase()); // ChangeId: 2025041901
                rs_inner = pstmt_inner.executeQuery();
                while (rs_inner.next()) {
                    reg_id = rs_inner.getString("registration_id");
                    preview_file = rs_inner.getString("preview_file");
                    applied_date = rs_inner.getString("date_of_application");
                    applied_date = formatDate("yyyy-MM-dd", applied_date);
                    System.out.println("DEBUG-HRD"+rs_inner.getString("status"));
                    application_status = rs_inner.getString("status"); //ChangeID:2023081001 PaymentChange
                }
                // logMgr.accessLog("preview_file:" + preview_file + ",reg_id:" + reg_id);
                rs_inner.close();
                pstmt_inner.close();

                /* // ChangeId: 2025041901
                qry_inner = "select reference_date from advt_data where advt_no='" + advt_no + "' and status='active'";
                */
                qry_inner = "select reference_date from advt_data where advt_no=? and status='active'"; // ChangeId: 2025041901
                // logMgr.accessLog("qry registration_id..done. in getAllAdverstimentPost :" +
                // qry_inner);
                pstmt_inner = con.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                pstmt_inner.setString(1, advt_no); // ChangeId: 2025041901
                rs_inner = pstmt_inner.executeQuery();
                while (rs_inner.next()) {
                    reference_date = rs_inner.getString("reference_date");
                }
                // logMgr.accessLog("reference_date:" + reference_date);
                rs_inner.close();
                pstmt_inner.close();

                String advt_date = "";
                
                /* // ChangeId: 2025041901
                qry = "select valid_from from advt_data where status='active' and advt_no='" + advt_no + "'";
                */
                qry = "select valid_from from advt_data where status='active' and advt_no=?"; // ChangeId: 2025041901
                // logMgr.accessLog("qry in getAllAdverstimentPost date :" + qry);
                pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                pstmt.setString(1, advt_no); // ChangeId: 2025041901
                ResultSet rs_dt = pstmt.executeQuery();
                while (rs_dt.next()) {
                    advt_date = rs_dt.getString("valid_from");

                }
                rs_dt.close();

                LinkedHashMap<String, Object> mp = new LinkedHashMap<String, Object>();
                int count_attempts = new AdvertisementOperation(emailLogId).applicantAttempts(advt_no, post_no, eMail);
                mp.put("advt_no", advt_no);
                mp.put("post_no", post_no);
                mp.put("no_of_attempts", count_attempts);
                mp.put("net", net);
                mp.put("post_spec", post_spec); // ChangeId: 2024010101
                mp.put("advt_date", advt_date);
                mp.put("post_name", rs.getString("post_name"));
                mp.put("no_of_vacancies", rs.getString("no_of_vacancies"));
                mp.put("experience", rs.getString("experience"));
                mp.put("select_process", rs.getString("select_process"));
                mp.put("status", rs.getString("status"));
                mp.put("desirable_qual", rs.getString("desirable_qualification"));
                mp.put("net", rs.getString("net"));// National Eligibility test
                mp.put("Eligibility", arr_inner);
                mp.put("Category", cat_arr);
                mp.put("Reg_Id", reg_id);
                mp.put("Applied_Date", applied_date);
                mp.put("Preview_File", preview_file);
                mp.put("Reference_Date", reference_date);
                mp.put("application_status", application_status); //ChangeID:2023081001 PaymentChange

                arr.add(mp);
                // logMgr.accessLog("FOR REDFERENCE DATE: " + mp.toString());
            }
            //rs_post_data_valid  = true;
            // rs.close();
            // pstmt.close();
            // con.close();
            // con_inner.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rs_inner != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (pstmt_inner != null) {
                    pstmt_inner.close();
                }
                conMgr.freeConnection(lPoolName, con_inner);
                conMgr.freeConnection(lPoolName, con);
            } catch (Exception e) {
            }
        }
        return arr;
    }

    public ArrayList<LinkedHashMap<String, Object>> getAdvtPostDetails(String advt_no, String post_no) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        ArrayList<LinkedHashMap<String, Object>> arr = new ArrayList<LinkedHashMap<String, Object>>();

        Connection con_inner = null;
        ResultSet rs_inner = null;
        PreparedStatement pstmt_inner = null;

        try {
            con = conMgr.getConnection(lPoolName);
            logMgr.accessLog("Inside GET getAllAdverstimentPost OPTIONS");
            con_inner = conMgr.getConnection(lPoolName);
            ;
            {
                /* // ChangeId: 2025041901
                String qry_inner = "select * from post_data_details where advt_no='" + advt_no + "' and post_no='"
                        + post_no + "'";
                */
                String qry_inner = "SELECT * FROM post_data_details WHERE advt_no=? AND post_no=?"; // ChangeId: 2025041901
                // logMgr.accessLog("qry inner in getAllAdverstimentPost :" + qry_inner);
                pstmt_inner = con.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                pstmt_inner.setString(1, advt_no); // ChangeId: 2025041901
                pstmt_inner.setString(2, post_no); // ChangeId: 2025041901
                rs_inner = pstmt_inner.executeQuery();
                ArrayList<HashMap<String, String>> arr_inner = new ArrayList<HashMap<String, String>>();
                while (rs_inner.next()) {
                    HashMap<String, String> mp_inner = new HashMap<String, String>();
                    mp_inner.put("eligibility", rs_inner.getString("eligibility"));
                    mp_inner.put("essential_qualification", rs_inner.getString("essential_qualification"));
                    mp_inner.put("discipline", rs_inner.getString("discipline"));
                    mp_inner.put("specialization", rs_inner.getString("specialization"));
                    mp_inner.put("mandatory", rs_inner.getString("mandate"));
                    arr_inner.add(mp_inner);
                }
                // logMgr.accessLog("arr_inner:" + arr_inner.toString());
                rs_inner.close();
                pstmt_inner.close();

                /* // ChangeId: 2025041901
                qry_inner = "select * from category_data where advt_no='" + advt_no + "' and post_no='" + post_no + "'";
                */
                qry_inner = "select * from category_data where advt_no=? and post_no=?"; // ChangeId: 2025041901
                logMgr.accessLog("qry inner category in getAllAdverstimentPost :" + qry_inner);
                pstmt_inner = con.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                pstmt_inner.setString(1, advt_no); // ChangeId: 2025041901
                pstmt_inner.setString(2, post_no); // ChangeId: 2025041901
                rs_inner = pstmt_inner.executeQuery();
                ArrayList<HashMap<String, String>> arr_categ = new ArrayList<HashMap<String, String>>();
                while (rs_inner.next()) {
                    HashMap<String, String> mp_inner = new HashMap<String, String>();
                    mp_inner.put("category", rs_inner.getString("category"));
                    mp_inner.put("age_limit_lower", rs_inner.getString("essential_qualification"));
                    mp_inner.put("age_limit_upper", rs_inner.getString("discipline"));
                    mp_inner.put("status", rs_inner.getString("status"));
                    arr_categ.add(mp_inner);
                }
                logMgr.accessLog("arr_inner category:" + arr_inner.toString());
                rs_inner.close();
                pstmt_inner.close();

                LinkedHashMap<String, Object> mp = new LinkedHashMap<String, Object>();
                mp.put("advt_no", advt_no);
                mp.put("post_no", post_no);
                mp.put("post_name", rs.getString("post_name"));
                mp.put("no_of_vacancies", rs.getString("no_of_vacancies"));
                mp.put("status", rs.getString("status"));
                mp.put("desirable_qual", rs.getString("desirable_qualification"));
                mp.put("net", rs.getString("net"));// National Eligibility test
                mp.put("Eligibility", arr_inner);
                mp.put("Category", arr_categ);
                arr.add(mp);
            }
            // con_inner.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pstmt_inner.close();
                conMgr.freeConnection(lPoolName, con_inner);
                conMgr.freeConnection(lPoolName, con);
            } catch (Exception e) {
            }
        }
        return arr;
    }

    public int getMaxDeleteCnt(PostData pd) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        int cnt = 0;

        try {
            logMgr.accessLog("Inside getMaxDeletCnt");
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement(
                    "select count(status) from post_data where advt_no = ? and post_no = ? and status like 'delete%' ",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, pd.getAdvt_no());
            pstmt.setString(2, pd.getPost_no());

            logMgr.accessLog("DELETE post CNT QRY:" + pstmt.toString());
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
            }
        }
        return cnt;

    }

    public int deletePost(PostData pd) {
        Connection con = null;
        PreparedStatement postPstmt = null, categoryPstmt = null, pstmt = null;
        int i = 0, j = 0;

        try {
            con = conMgr.getConnection(lPoolName);
            logMgr.accessLog("inside delete post method......");

            pstmt = con.prepareStatement("select count(*) from post_data where advt_no = ? and post_no = ? and status ='active'");
            pstmt.setString(1, pd.getAdvt_no());
            pstmt.setString(2, pd.getPost_no());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    logMgr.accessLog("Advertisement with advt_no/post_no: " + pd.getAdvt_no() + "," + pd.getPost_no() + " is active cannot be deleted!!");
                    return -99;
                }
            }

            rs.close();
            pstmt.close();

            categoryPstmt = con.prepareStatement(
                    "update category_data set status = ? , date_of_status_update = ? where advt_no = ? and post_no = ? and status='inactive'");

            categoryPstmt.setString(1, pd.getStatus());
            categoryPstmt.setDate(2, pd.getDate_of_status_update());
            categoryPstmt.setString(3, pd.getAdvt_no());
            categoryPstmt.setString(4, pd.getPost_no());
            logMgr.accessLog("category:..:" + categoryPstmt.toString());

            i = categoryPstmt.executeUpdate();

            logMgr.accessLog("value of i is : " + i);

            if (i > 0) {
                postPstmt = con.prepareStatement(
                        "update post_data_details set status = ? , date_of_status_update = ? where advt_no = ? and post_no = ? and status='inactive'");

                postPstmt.setString(1, pd.getStatus());
                postPstmt.setDate(2, pd.getDate_of_status_update());
                postPstmt.setString(3, pd.getAdvt_no());
                postPstmt.setString(4, pd.getPost_no());

                i = postPstmt.executeUpdate();

                logMgr.accessLog("value of j is : " + j);
            }
            postPstmt.close();
            if (i > 0) {
                postPstmt = con.prepareStatement(
                        "update post_data set status = ? , date_of_status_update = ? where advt_no = ? and post_no = ? and status='inactive'");

                postPstmt.setString(1, pd.getStatus());
                postPstmt.setDate(2, pd.getDate_of_status_update());
                postPstmt.setString(3, pd.getAdvt_no());
                postPstmt.setString(4, pd.getPost_no());

                j = postPstmt.executeUpdate();

                logMgr.accessLog("value of j is : " + j);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return i;
        } finally {
            try {
                categoryPstmt.close();
                postPstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (Exception e) {
            }
        }
        return i;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ArrayList<LinkedHashMap> getApplicantStatus(String email) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        ArrayList<LinkedHashMap> arr = new ArrayList<LinkedHashMap>();

        try {
            con = conMgr.getConnection(lPoolName);
            logMgr.accessLog("Inside getApplicantStatus method ........");
            pstmt = con.prepareStatement(
                    "select * from personal_data where lower(email) = '" + email.toLowerCase() + "' order by advt_no , post_no  ");

            logMgr.accessLog("getApplicantStatus:.....:" + pstmt.toString());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                LinkedHashMap map = new LinkedHashMap();
                map.put("Registration ID", rs.getString("registration_id").replaceAll("\\|", ", "));
                map.put("Name", rs.getString("first_name").replaceAll("\\|", ", "));
                map.put("Advt No.", rs.getString("advt_no").replaceAll("\\|", ", "));
                map.put("Post No.", rs.getString("post_no").replaceAll("\\|", ", "));
                map.put("Application Date", rs.getString("date_of_application").replaceAll("\\|", ", "));
                arr.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (Exception e) {
            }
        }
        return arr;
    }

    public ResultSet generateAll(String advt_no, String post_no) throws SQLException {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        try {
            con = conMgr.getConnection(lPoolName);
            pstmt = con.prepareStatement("select valid_from, valid_till from advt_data where advt_no=?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, advt_no);

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("Excel Sheet");
            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("Valid From");
            rowhead.createCell((short) 1).setCellValue("Valid Till");

            int index = 1;
            rs = pstmt.executeQuery();
            while (rs.next()) {

                HSSFRow row = sheet.createRow((short) index);
                row.createCell((short) 0).setCellValue(rs.getString(1));
                row.createCell((short) 1).setCellValue(rs.getString(2));
                index++;
            }
            pstmt.close();
            pstmt = con.prepareStatement(
                    "select no_of_vacancies,essential_qualification,discipline,specialization from post_data where advt_no=? and post_no=?",
                    ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pstmt.setString(1, advt_no);
            pstmt.setString(2, post_no);

            rowhead.createCell((short) 2).setCellValue("No of vacancies");
            rowhead.createCell((short) 3).setCellValue("Essential Qualification");
            rowhead.createCell((short) 4).setCellValue("Discipline");
            rowhead.createCell((short) 5).setCellValue("Specialization");

            rs = pstmt.executeQuery();

            while (rs.next()) {
                HSSFRow row = sheet.createRow((short) index);
                row.createCell((short) 0).setCellValue(rs.getString(1));
                row.createCell((short) 1).setCellValue(rs.getString(2));
                row.createCell((short) 2).setCellValue(rs.getString(3));
                row.createCell((short) 3).setCellValue(rs.getString(4));
                index++;
            }

            FileOutputStream fileOut = new FileOutputStream("f:\\excelFile.xls");
            wb.write(fileOut);
            fileOut.close();
            wb.close();
            logMgr.accessLog("Data is saved in excel file.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con);
            } catch (Exception e) {
            }
        }
        return rs;
    }

    public ArrayList getApplicantTable(String reqId, String tableName, String advt_no, String post_no) {
        Connection con_inner = null;
        ResultSet rs_inner = null;
        PreparedStatement pstmt_inner = null;
        ArrayList aList = new ArrayList();

        try {
            con_inner = conMgr.getConnection(lPoolName);
            String qry_inner = "select * from " + tableName;

            qry_inner += " where registration_id='" + reqId + "' ";
            if (!advt_no.equals("NA")) {
                qry_inner += " and advt_no='" + advt_no + "' and post_no='" + post_no + "' ";
            }
            if (tableName.indexOf("highereducation") >= 0) {
                //qry_inner += " JOIN (VALUES ('X',1),('XII',2),('ITI',3),('DIPLOMA',4),('Graduate',5),('Postgraduate',6),('PhD',7),('PostDoctoral',8)) as x(qualification_type,ordering) ON highereducation.qualification_type=x.qualification_type ";
                qry_inner += " ORDER BY idx(array['X','XII','Graduate','Postgraduate','PhD','PostDoctoral'],highereducation.qualification_type)";
            }
            logMgr.accessLog("qry inner in getAllAdverstimentPost :" + qry_inner);
            pstmt_inner = con_inner.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs_inner = pstmt_inner.executeQuery();
            ResultSetMetaData rsmd_inner = rs_inner.getMetaData();
            int columnsNumber_inner = rsmd_inner.getColumnCount();
            while (rs_inner.next()) {
                HashMap<String, String> mp_inner = new HashMap<String, String>();
                for (int i = 1; i <= columnsNumber_inner; i++) {
//					if (i > 1) {
//						System.out.print(",  ");
//					}
                    String columnValue = rs_inner.getString(i);
                    String columnName = rsmd_inner.getColumnName(i);
                    if (columnName.equals("dob")) {
                        columnValue = this.formatDate("yyyy-MM-dd", columnValue);
                    }
                    mp_inner.put(columnName, columnValue);
                    //	System.out.print(columnValue + " " + columnName);
                }

                aList.add(mp_inner);
            }
            if (tableName.indexOf("x_data") >= 0) {
                logMgr.accessLog("arr xth data:" + aList.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs_inner.close();
                pstmt_inner.close();
                conMgr.freeConnection(lPoolName, con_inner);
            } catch (Exception e) {
            }
        }
        return aList;
    }

    public ArrayList getApplicantTableValues(String reqId, String tableName, String advt_no, String post_no) {
        Connection con_inner = null;
        ResultSet rs_inner = null;
        PreparedStatement pstmt_inner = null;
        ArrayList aList = new ArrayList();

        try {
            con_inner = conMgr.getConnection(lPoolName);
            /* // ChangeId: 2025041901
            String qry_inner = "select * from " + tableName + " where registration_id='" + reqId + "' and advt_no='" + advt_no + "' and post_no='" + post_no + "'";
            */
            // ChangeId: 2025041901
            String qry_inner = "select * from " + tableName + " where registration_id=? and advt_no=? and post_no=?";
            // logMgr.accessLog("qry inner in getAllAdverstimentPost :" + qry_inner);
            System.out.println("qry inner in getAllAdverstimentPost :" + qry_inner);
            pstmt_inner = con_inner.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            pstmt_inner.setString(1, reqId); // ChangeId: 2025041901
            pstmt_inner.setString(2, advt_no); // ChangeId: 2025041901
            pstmt_inner.setString(3, post_no); // ChangeId: 2025041901
            rs_inner = pstmt_inner.executeQuery();
            ArrayList<HashMap<String, String>> arr_X = new ArrayList<HashMap<String, String>>();
            ResultSetMetaData rsmd_inner = rs_inner.getMetaData();
            int columnsNumber_inner = rsmd_inner.getColumnCount();
            while (rs_inner.next()) {
                for (int i = 1; i <= columnsNumber_inner; i++) {
                    if (i > 1) {
                        System.out.print(",  ");
                    }
                    String columnValue = rs_inner.getString(i);
                    String columnName = rsmd_inner.getColumnName(i);
                    if (columnName.equals("dob")) {
                        columnValue = this.formatDate("yyyy-MM-dd", columnValue);
                    }
                    aList.add(columnValue);
                    System.out.print(columnValue + " " + rsmd_inner.getColumnName(i));
                }

            }
            // logMgr.accessLog("arr_inner:" + arr_X);
            rs_inner.close();
            pstmt_inner.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conMgr.freeConnection(lPoolName, con_inner);
            } catch (Exception e) {
            }
        }
        return aList;
    }

    ArrayList arrMenu = new ArrayList();
    ArrayList arrAppl = new ArrayList();
    ArrayList TotalApplArr = new ArrayList();

    public ArrayList getColumnNames(String tableName) {
        Connection con = null;
        ArrayList lArr = new ArrayList();

        try {
            con = conMgr.getConnection(lPoolName);

            java.sql.DatabaseMetaData md = con.getMetaData();
            ResultSet rset = md.getColumns(null, null, tableName, null);

            logMgr.accessLog("your_table_name:" + tableName);
            while (rset.next()) {
                logMgr.accessLog("\t" + rset.getString(4));
                lArr.add(rset.getString(4));
            }
            rset.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conMgr.freeConnection(lPoolName, con);
            } catch (Exception e) {
            }
        }

        return lArr;
    }

    public ArrayList<LinkedHashMap<String, Object>> getAllApplicantPosts(String eMail) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        ArrayList<LinkedHashMap<String, Object>> arr = new ArrayList<LinkedHashMap<String, Object>>();
        String advt_no = new String();
        String post_no = new String();
        String net = new String();

        Connection con_inner = null;
        ResultSet rs_inner = null;
        PreparedStatement pstmt_inner = null;

        try {
            con = conMgr.getConnection(lPoolName);
            logMgr.accessLog("Inside GET getAllAdverstimentPost OPTIONS");
            con_inner = conMgr.getConnection(lPoolName);
            //	String qry = "select * from post_data where  status='active' order by advt_no,post_data";
            String qry = "select * from post_data  order by advt_no,post_data";
            logMgr.accessLog("qry in getAllAdverstimentPost :" + qry);
            pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                advt_no = rs.getString("advt_no");
                post_no = rs.getString("post_no");
                net = rs.getString("net");

                /* // ChangeId: 2025041901
                String qry_inner = "select * from post_data_details where advt_no='" + advt_no + "' and post_no='"
                        + post_no + "'";
                */
                // ChangeId: 2025041901
                String qry_inner = "select * from post_data_details where advt_no=? and post_no=?";
                // logMgr.accessLog("qry inner in getAllAdverstimentPost :" + qry_inner);
                pstmt_inner = con.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                pstmt_inner.setString(1, advt_no); // ChangeId: 2025041901
                pstmt_inner.setString(2, post_no); // ChangeId: 2025041901
                rs_inner = pstmt_inner.executeQuery();
                ArrayList<HashMap<String, String>> arr_inner = new ArrayList<HashMap<String, String>>();
                while (rs_inner.next()) {
                    HashMap<String, String> mp_inner = new HashMap<String, String>();
                    mp_inner.put("eligibility", rs_inner.getString("eligibility"));
                    mp_inner.put("essential_qualification", rs_inner.getString("essential_qualification"));
                    mp_inner.put("discipline", rs_inner.getString("discipline"));
                    mp_inner.put("specialization", rs_inner.getString("specialization"));
                    mp_inner.put("mandatory", rs_inner.getString("mandate"));

                    arr_inner.add(mp_inner);
                }
                // logMgr.accessLog("arr_inner:" + arr_inner);
                rs_inner.close();
                pstmt_inner.close();

                ArrayList<HashMap<String, String>> cat_arr = new ArrayList<HashMap<String, String>>();
                /* // ChangeId: 2025041901
                qry_inner = "select * from category_data where advt_no='" + advt_no + "' and post_no='" + post_no + "'";
                */
                // ChangeId: 2025041901
                qry_inner = "select * from category_data where advt_no=? and post_no=?";
                // logMgr.accessLog("qry category_data in getAllAdverstimentPost :" +
                // qry_inner);
                pstmt_inner = con.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                pstmt_inner.setString(1, advt_no); // ChangeId: 2025041901
                pstmt_inner.setString(2, post_no); // ChangeId: 2025041901
                rs_inner = pstmt_inner.executeQuery();
                while (rs_inner.next()) {
                    HashMap<String, String> mp_inner = new HashMap<String, String>();
                    mp_inner.put("category", rs_inner.getString("category"));
                    mp_inner.put("age_limit_lower", rs_inner.getString("age_limit_lower"));
                    mp_inner.put("age_limit_upper", rs_inner.getString("age_limit_upper"));
                    cat_arr.add(mp_inner);
                }
                // logMgr.accessLog("cat_arr:" + cat_arr.toString());
                rs_inner.close();
                pstmt_inner.close();

                String reg_id = "-";
                String preview_file = "-";
                String applied_date = "-";
                String reference_date = "-";

                /* // ChangeId: 2025041901
                qry_inner = "select registration_id,preview_file,date_of_application from personal_data where advt_no='"
                        + advt_no + "' and post_no='" + post_no + "' and lower(email)='" + eMail.toLowerCase() + "' and status='POSTED'";
                */
                // ChangeId: 2025041901
                qry_inner = "select registration_id,preview_file,date_of_application from personal_data where advt_no=? and post_no=? and lower(email)=? and status='POSTED'";
                
                // logMgr.accessLog("qry registration_id..done. in getAllAdverstimentPost :" +
                // qry_inner);
                pstmt_inner = con.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                pstmt_inner.setString(1, advt_no); // ChangeId: 2025041901
                pstmt_inner.setString(2, post_no); // ChangeId: 2025041901
                pstmt_inner.setString(3, eMail.toLowerCase()); // ChangeId: 2025041901
                rs_inner = pstmt_inner.executeQuery();
                while (rs_inner.next()) {
                    reg_id = rs_inner.getString("registration_id");
                    preview_file = rs_inner.getString("preview_file");
                    applied_date = rs_inner.getString("date_of_application");
                    applied_date = formatDate("yyyy-MM-dd", applied_date);

                }
                // logMgr.accessLog("preview_file:" + preview_file + ",reg_id:" + reg_id);
                rs_inner.close();
                pstmt_inner.close();

                /* // ChangeId: 2025041901
                qry_inner = "select reference_date from advt_data where advt_no='" + advt_no + "' and status='active'";
                */
                // ChangeId: 2025041901
                qry_inner = "select reference_date from advt_data where advt_no=? and status='active'";
                // logMgr.accessLog("qry registration_id..done. in getAllAdverstimentPost :" +
                // qry_inner);
                pstmt_inner = con.prepareStatement(qry_inner, ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                pstmt_inner.setString(1, advt_no); // ChangeId: 2025041901
                rs_inner = pstmt_inner.executeQuery();
                while (rs_inner.next()) {
                    reference_date = rs_inner.getString("reference_date");
                }
                logMgr.accessLog("reference_date:" + reference_date);
                rs_inner.close();
                pstmt_inner.close();

                String advt_date = "";
                //qry = "select valid_from from advt_data where status='active' and advt_no='" + advt_no + "'";
                /* // ChangeId: 2025041901
                qry = "select valid_from from advt_data where  advt_no='" + advt_no + "'";
                */
                // ChangeId: 2025041901
                qry = "select valid_from from advt_data where  advt_no=?";
                // logMgr.accessLog("qry in getAllAdverstimentPost date :" + qry);
                pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                pstmt.setString(1, advt_no); // ChangeId: 2025041901
                ResultSet rs_dt = pstmt.executeQuery();
                while (rs_dt.next()) {
                    advt_date = rs_dt.getString("valid_from");

                }
                rs_dt.close();

                LinkedHashMap<String, Object> mp = new LinkedHashMap<String, Object>();

                // only valid regId
                if (!reg_id.equals("-")) {
                    mp.put("advt_no", advt_no);
                    mp.put("post_no", post_no);
                    mp.put("net", net);
                    mp.put("advt_date", advt_date);
                    mp.put("post_name", rs.getString("post_name"));
                    mp.put("no_of_vacancies", rs.getString("no_of_vacancies"));
                    mp.put("status", rs.getString("status"));
                    mp.put("desirable_qual", rs.getString("desirable_qualification"));
                    mp.put("net", rs.getString("net"));// National Eligibility test
                    mp.put("Eligibility", arr_inner);
                    mp.put("Category", cat_arr);
                    mp.put("Reg_Id", reg_id);
                    mp.put("Applied_Date", applied_date);
                    mp.put("Preview_File", preview_file);
                    mp.put("Reference_Date", reference_date);

                    arr.add(mp);
                }
                logMgr.accessLog("FOR REFERENCE DATE: " + mp.toString());
            }

            // rs.close();
            // pstmt.close();
            // con.close();
            // con_inner.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conMgr.freeConnection(lPoolName, con_inner);
                conMgr.freeConnection(lPoolName, con);
            } catch (Exception e) {
            }
        }
        return arr;
    }

    public String formatDate(String pattern, String date) {
        String dtStr = date;
        try {
            // String pattern = "yyyy-MM-dd";
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            Date date1 = sdf.parse(date);
            // logMgr.accessLog("DATE IS ...." + pattern + "," + date);
            if (!date.equals("-")) {
                dtStr = new SimpleDateFormat("dd-MMM-yyyy").format(date1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtStr;
    }

    @SuppressWarnings({"rawtypes", "unused", "unchecked"})
    public ArrayList<LinkedHashMap<String, Object>> getApplicantSaveData(String email) {
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;

        ArrayList<LinkedHashMap<String, Object>> arr = new ArrayList<LinkedHashMap<String, Object>>();

        ArrayList per_Arr = new ArrayList();
        ArrayList x_Arr = new ArrayList();
        ArrayList xii_Arr = new ArrayList();
        ArrayList iti_Arr = new ArrayList();
        ArrayList higher_Arr = new ArrayList();
        ArrayList exp_Arr = new ArrayList();
        ArrayList net_Arr = new ArrayList();

        try {
            logMgr.accessLog("Inside GET get Required ....AdverstimentPost OPTIONS");
            con = conMgr.getConnection(lPoolName);

            String reqId = "NA_" + email;

            LinkedHashMap mp = new LinkedHashMap();
            logMgr.accessLog("REGISTRATION ID IS ..................................:" + reqId);

            per_Arr = getApplicantTable(reqId, "personal_data", "NA", "NA");
            x_Arr = getApplicantTable(reqId, "x_data", "NA", "NA");
            xii_Arr = getApplicantTable(reqId, "xii_data", "NA", "NA");
            iti_Arr = getApplicantTable(reqId, "iti_diploma", "NA", "NA");
            higher_Arr = getApplicantTable(reqId, "highereducation", "NA", "NA");
            exp_Arr = getApplicantTable(reqId, "experience", "NA", "NA");
            net_Arr = getApplicantTable(reqId, "nationalexamtest", "NA", "NA");

            if (!per_Arr.isEmpty()) {
                mp.put("Personal", per_Arr);
                mp.put("X", x_Arr);
                mp.put("XII", xii_Arr);
                mp.put("ITI", iti_Arr);
                mp.put("Higher", higher_Arr);
                mp.put("Experience", exp_Arr);
                mp.put("NationalExam", net_Arr);
                //logMgr.accessLog("MP VALUE IS ....:" + mp.toString());
                System.out.println("MP VALUE IS ....:" + mp.toString());
                arr.add(mp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static void main(String args[]) {
        PostOperation ps = new PostOperation("admin");
        List m = new ArrayList();

        // ps.generateExcel(m);
    }

}
