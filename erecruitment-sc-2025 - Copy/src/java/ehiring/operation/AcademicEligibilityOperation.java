package ehiring.operation;


import ehiring.action.CurrentDateTime;
import ehiring.dao.LogManager;
import ehiring.db.DBConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class AcademicEligibilityOperation {
	public Connection con = null;
	// public ResultSet rs = null;
	public PreparedStatement pstmt = null;
	DBConnectionManager conMgr = null;
	String lPoolName = "recruit";
	LogManager logMgr;
	
	
	public AcademicEligibilityOperation(String emailLogId) {
		conMgr = DBConnectionManager.getInstance();
		logMgr = LogManager.getInstance(emailLogId);
	}

	public ArrayList<String> loadAcadEligibOptions() {
		ArrayList<String> arr = new ArrayList<String>();

		try {
			logMgr.accessLog("Inside GET ACADEMIC ELIGIBILITY OPTIONS");
			con = conMgr.getConnection(lPoolName);
			pstmt = con.prepareStatement(
					"select eligibility_value from academic_eligibility order by eligibility_value",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				arr.add(rs.getString("eligibility_value"));
			}
			rs.close();
			pstmt.close();
			if (arr.isEmpty()) {
				logMgr.accessLog("No Data Found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conMgr.freeConnection(lPoolName,con);
			} catch (Exception e) {
			}
		}
		return arr;
	}

	public ArrayList<String> getAcadEligibOptions() {

		ArrayList<String> arr = new ArrayList<String>();
		
		try {
			logMgr.accessLog("Inside GET ACADEMIC ELIGIBILITY OPTIONS");
			con = conMgr.getConnection(lPoolName);
			pstmt = con.prepareStatement(
					"select eligibility_value from academic_eligibility where advt_no=? & post_no=?",
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				arr.add(rs.getString("eligibility_value"));
			}
			rs.close();
			if (arr.isEmpty()) {
				logMgr.accessLog("No Data Found");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// rs.close();
				pstmt.close();
				conMgr.freeConnection(lPoolName,con);
			} catch (SQLException e) {
			}
		}

		return arr;
	}

	public ArrayList<HashMap> getQualDiscipline() {
		ArrayList<HashMap> arrFinal = new ArrayList<HashMap>();
		HashMap mp = new HashMap();
		
		try {
			logMgr.accessLog("Inside GET Qualification Discipline OPTIONS");
			con = conMgr.getConnection(lPoolName);
			pstmt = con.prepareStatement("select * from essential_qualification ", ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = pstmt.executeQuery();
			Connection con_internal = null;
			ResultSet rs_internal = null;
			PreparedStatement pstmt_internal = null;
			ArrayList<String> dispList = new ArrayList<String>();
			ArrayList<String> idList = new ArrayList<String>();
			String qry = "";
			String idCombo = new String();
			String idCombo_org = new String();
			while (rs.next()) {
				String qual = rs.getString("qualification_value");
				logMgr.accessLog("qual is :" + qual);
				mp.put("Qualification", qual);
				String eId = rs.getString("essential_qualification_id");
				String elgId = rs.getString("eligibility_id");
				idCombo_org = elgId + "_" + eId;
				con_internal = conMgr.getConnection(lPoolName);
				qry = "select * from discipline where essential_qualification_id='" + eId + "' and eligibility_id='"
						+ elgId + "' ";
				//logMgr.accessLog("qry is :" + qry);
				pstmt_internal = con_internal.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE);
				rs_internal = pstmt_internal.executeQuery();
				while (rs_internal.next()) {
					dispList.add(rs_internal.getString("discipline_value"));
					idCombo = idCombo_org + "_" + rs_internal.getString("discipline_id");
					idList.add(idCombo);
				}

				rs_internal.close();
				pstmt_internal.close();
				con_internal.close();
				mp.put("Discipline", dispList);
				mp.put("id", idList);
				ArrayList specList = new ArrayList();
				if (idList.size() > 0) {
					specList = getSpecialization(idList.get(0));
				}
				mp.put("Spec", specList);
				logMgr.accessLog("mp is the values every time :" + mp.toString());
				arrFinal.add(mp);

				dispList = new ArrayList();
				mp = new HashMap();
				idList = new ArrayList();
			}
			rs.close();
			pstmt.close();
			conMgr.freeConnection(lPoolName,con);
		} catch (Exception e) {
			e.printStackTrace();
			conMgr.freeConnection(lPoolName,con);
		}

		return arrFinal;
	}

	public ArrayList<String> getSpecialization(String id) {
		ArrayList arr = new ArrayList();
		StringTokenizer st = new StringTokenizer(id, "_");
		String elgId = "", eId = "", dispId = "";
		elgId = st.nextToken();
		eId = st.nextToken();
		dispId = st.nextToken();
		

		try {
			logMgr.accessLog("Inside GET Specialization  OPTIONS");
			
			con = conMgr.getConnection(lPoolName);
			String qry = "select * from specialization where essential_qualification_id='" + eId
					+ "' and eligibility_id='" + elgId + "' and discipline_id='" + dispId
					+ "' order by specialization_value";
		//	logMgr.accessLog("qry in getSpecialization :" + qry);
			PreparedStatement pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				arr.add(rs.getString("specialization_value"));
			}
			rs.close();
			pstmt.close();
			conMgr.freeConnection(lPoolName,con);
		} catch (Exception e) {
			e.printStackTrace();
			conMgr.freeConnection(lPoolName,con);
		}

		return arr;
	}

	public String getEligibility(String elgId) {
		String elgValue = new String();
		
		try {
			logMgr.accessLog("Inside GET Specialization  OPTIONS");
			
			con = conMgr.getConnection(lPoolName);
			String qry = "select eligibility_value from academic_eligibility where eligibility_id='" + elgId + "' ";
	//		logMgr.accessLog("qry in getSpecialization :" + qry);
			PreparedStatement pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				elgValue = rs.getString("specialization_value");
			}
			rs.close();
			pstmt.close();
			conMgr.freeConnection(lPoolName,con);
		} catch (Exception e) {
			e.printStackTrace();
			conMgr.freeConnection(lPoolName,con);
		}

		return elgValue;
	}

	public String getEligibilityFromQual(String qual) {
		String elgValue = new String();
		
		try {
			logMgr.accessLog("Inside GET Specialization  OPTIONS");
			Connection con = conMgr.getConnection(lPoolName);
			String qry = "select eligibility_value from academic_eligibility where eligibility_id=(select eligibility_id from essential_qualification where qualification_value='"
					+ qual + "')";
			logMgr.accessLog("qry in getEligibilityFromQual is:" + qry);
			PreparedStatement pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				elgValue = rs.getString("eligibility_value");
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conMgr.freeConnection(lPoolName,con);
			} catch (Exception e) {
			}
		}

		return elgValue;
	}

	public ArrayList<String> getDesirableQual() {
		ArrayList arr = new ArrayList();
		
		con = conMgr.getConnection(lPoolName);
		try {
			logMgr.accessLog("Inside GET getDesirableQual OPTIONS");
			con = conMgr.getConnection(lPoolName);
			String qry = "select * from desirable_qualification";
		//	logMgr.accessLog("qry in getSpecialization :" + qry);
			pstmt = con.prepareStatement(qry, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				arr.add(rs.getString("desirable_qualification_value"));
			}
			rs.close();
			pstmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conMgr.freeConnection(lPoolName,con);
			} catch (Exception e) {
			}
		}
		return arr;
	}

	public static void main(String args[]) {
		AcademicEligibilityOperation aeo = new AcademicEligibilityOperation("admin");
		ArrayList l = aeo.getQualDiscipline();
		// logMgr.accessLog("l is:"+l.toString());
		// ArrayList l = aeo.getSpecialization("4_2_1");
		// ArrayList l = aeo.getDesirableQual();
		// ArrayList l = aeo.getAllAdverstimentPost();
//	logMgr.accessLog("l is:" + l.toString());

	}
}