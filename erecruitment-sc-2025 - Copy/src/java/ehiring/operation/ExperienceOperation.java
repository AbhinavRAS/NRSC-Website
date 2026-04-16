package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ehiring.action.CurrentDateTime;
import ehiring.dao.Experience_Data;
import ehiring.dao.LogManager;
import ehiring.action.CurrentDateTime;

public class ExperienceOperation {
	
	LogManager logMgr;
	
	public ExperienceOperation(String emailLogId)
	{
		logMgr = LogManager.getInstance(emailLogId);
	}
	
	
	public int expData(Experience_Data exp, Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			pstmt = con.prepareStatement(
					"insert into experience(registration_id,advt_no,post_no,experience,exp_years,exp_months,"
							+ "	emp_name,emp_address,time_from,time_to,designation,slno,nature_of_work,exp_govt,exp_cert,exp_days,reason_for_leaving,pay_drawn,email) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                                                                // PPEG-HRD: ReasonForLeaving, PayDrawn: field added in prepareStatement
			pstmt.setString(1, exp.getRegistration_id());
			pstmt.setString(2, exp.getAdvt_no());
			pstmt.setString(3, exp.getPost_no());
			pstmt.setString(4, exp.getExperience());
			pstmt.setInt(5, exp.getExp_years());
			pstmt.setInt(6, exp.getExp_months());
			pstmt.setString(7, exp.getEmp_name());
			pstmt.setString(8, exp.getEmp_address());
			pstmt.setDate(9, exp.getTime_from());
			pstmt.setDate(10, exp.getTime_to());
			pstmt.setString(11, exp.getDesignation());
			pstmt.setInt(12, exp.getSlno());
			pstmt.setString(13, exp.getEmp_work());
			pstmt.setString(14, exp.getExpGovt());
			pstmt.setString(15, exp.getExpCert());
			pstmt.setInt(16, exp.getExp_days());
                        pstmt.setString(17, exp.getEmp_reason()); // PPEG-HRD: ReasonForLeaving 
                        pstmt.setInt(18, exp.getEmp_paydrawn()); // PPEG-HRD: PayDrawn
                        pstmt.setString(19, exp.getEmail());
			
			//logMgr.accessLog(pstmt.toString());
			logMgr.accessLog("Experience :"+pstmt.toString());

			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
}