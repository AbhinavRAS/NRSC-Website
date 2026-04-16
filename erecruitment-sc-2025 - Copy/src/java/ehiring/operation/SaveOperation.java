package ehiring.operation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import ehiring.action.CurrentDateTime;
import ehiring.dao.LogManager;
import ehiring.dao.Temp_Data;
import ehiring.db.DBConnectionManager;


public class SaveOperation {
	
	public DBConnectionManager conMgr = null;
	public String lPoolName="recruit";
	LogManager logMgr;
	
	
	public SaveOperation(String emailLogId){
		conMgr = DBConnectionManager.getInstance();
		logMgr = LogManager.getInstance(emailLogId);
		}
	
	public int savePersonalData(Temp_Data td) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;

		LocalDate localDate = LocalDate.now();
		Date dateToday = Date.valueOf(localDate);
		td.setDate_of_application(dateToday);
		

		int i = 0;
		try {
			logMgr.accessLog("in savePersonalData of Save operation.");
			con =  conMgr.getConnection(lPoolName);

			pstmt = con.prepareStatement(
					"insert into temp_data(advt_no,post_no,email,salutation,first_name,last_name,gender,dob,place_of_birth,nationality,"
							+ "	category,category_pwd,category_exservice,category_exservice_from,"
							+ " category_exservice_to,category_ews,category_merit_sportname,"
							+ "	category_merit_sportlevel,marital_status,house_no,locality,town,district,state,"
							+ "	pincode,p_house_no,	p_locality,	p_town,	p_district,p_state,p_pincode,contact_no,"
							+ "	alternate_contact,nearest_railway_station,date_of_application,bank_acc_no,bank_ifsc_code," // ChangeId: 2023111001
                                                        + "cgov_serv, cgov_serv_doj,category_pwd_scrbe,bank_acc_beneficiary,bank_acc_doc,disability_certificate, category_pwd_comptime)" // ChangeId: 2023111101 // ChangeId:2025041601, 2025050807
							+ " Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); // ChangeId: 2023111001,// ChangeId: 2023111101 //ChangeId:2025041601, 2025050807

			pstmt.setString(1, td.getAdvt_no());
			pstmt.setString(2, td.getPost_no());
			pstmt.setString(3, td.getEmail());
			pstmt.setString(4, td.getSalutation());
			pstmt.setString(5, td.getFirst_name());
			pstmt.setString(6, td.getLast_name());
			pstmt.setString(7, td.getGender());
			pstmt.setDate(8, td.getDob());
			pstmt.setString(9, td.getPlace_of_birth());
			pstmt.setString(10, td.getNationality());
			pstmt.setString(11, td.getCategory());
			pstmt.setString(12, td.getCategory_pwd());
			pstmt.setString(13, td.getCategory_exservice());
			pstmt.setDate(14, td.getCategory_exservice_from());
			pstmt.setDate(15, td.getCategory_exservice_to());
			pstmt.setString(16, td.getCategory_ews());
			pstmt.setString(17, td.getCategory_merit_sportlevel());
			pstmt.setString(18, td.getCategory_merit_sportname());
			pstmt.setString(19, td.getMarital_status());
			pstmt.setString(20, td.getHouse_no());
			pstmt.setString(21, td.getLocality());
			pstmt.setString(22, td.getTown());
			pstmt.setString(23, td.getDistrict());
			pstmt.setString(24, td.getState());
			pstmt.setInt(25, td.getPincode());
			pstmt.setString(26, td.getP_house_no());
			pstmt.setString(27, td.getP_locality());
			pstmt.setString(28, td.getP_town());
			pstmt.setString(29, td.getP_district());
			pstmt.setString(30, td.getP_state());
			pstmt.setInt(31, td.getP_pincode());
			pstmt.setLong(32, td.getContact_no());
			pstmt.setLong(33, td.getAlternate_contact());
			pstmt.setString(34, td.getNearest_railway_station());
			pstmt.setDate(35, td.getDate_of_application());
                        pstmt.setString(36, td.getBank_Acc_No()); // ChangeId: 2023111001
			pstmt.setString(37, td.getBank_IFSC_Code()); // ChangeId: 2023111001
                        pstmt.setString(38, td.getCGov_Serv()); // ChangeId: 2023111101
			pstmt.setDate(39, td.getCGov_Serv_DOJ()); // ChangeId: 2023111101
                        pstmt.setString(40, td.getCategoryPwdScribe()); // ChangeId:2025041501
                        pstmt.setString(41, td.getBank_Acc_Beneficiary()); //ChangeId:2025041601
                        pstmt.setString(42, td.getBank_Acc_Doc()); //ChangeId:2025041601
                        pstmt.setString(43, td.getCategoryPwdCertificate()); //ChangeId:2025041501
                        pstmt.setString(44, td.getCategoryPwdCompTime()); //ChangeId:2025050807
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			con.close();
		}
		finally {
			try {
				
				pstmt.close();
				conMgr.freeConnection(lPoolName,con);
			} catch (SQLException e) {
			}
		}
		return i;
	}

	public int saveEducationData(Temp_Data td) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			logMgr.accessLog("in saveEducationData of Save operation.");
			con = conMgr.getConnection(lPoolName);
			pstmt = con.prepareStatement("insert into temp_data(x_edu_board,x_school,x_year_of_passing,"
					+ "x_division,x_percentage_or_cgpa,x_percentage,x_cgpa_obt,x_cgpa_max,xii_edu_board,"
					+ "xii_school,xii_specialization,xii_year_of_passing,xii_division,xii_percentage_or_cgpa,"
					+ "xii_percentage,xii_cgpa_obt,xii_cgpa_max,ug_university,ug_college,ug_qualification,"
					+ "ug_discipline,ug_year_of_passing,ug_division,ug_percentage_or_cgpa,ug_percentage,"
					+ "ug_cgpa_obt,ug_cgpa_max,pg_university,pg_college,pg_qualification,pg_discipline,"
					+ "pg_year_of_passing,pg_division,pg_percentage_or_cgpa,pg_percentage,pg_cgpa_obt,"
					+ "pg_cgpa_max,phd_university,phd_college,phd_qualification,phd_discipline,"
					+ "phd_year_of_passing,phd_division,phd_percentage_or_cgpa,phd_percentage,"
					+ "phd_cgpa_obt,phd_cgpa_max,pd_university,pd_college,pd_qualification,"
					+ "pd_discipline,pd_year_of_passing,pd_division,pd_percentage_or_cgpa,"
					+ "pd_percentage,pd_cgpa_obt,pd_cgpa_max) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
					+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			pstmt.setString(1, td.getX_edu_board());
			pstmt.setString(2, td.getX_school());
			pstmt.setInt(3, td.getX_year_of_passing());
			pstmt.setString(4, td.getX_division());
			pstmt.setString(5, td.getX_percentage_cgpa());
			pstmt.setFloat(6, td.getX_percentage());
			pstmt.setFloat(7, td.getX_cgpa_obt());
			pstmt.setString(8, td.getX_cgpa_max());
			pstmt.setString(9, td.getXii_edu_board());
			pstmt.setString(10, td.getXii_school());
			pstmt.setString(11, td.getXii_specialization());
			pstmt.setInt(12, td.getXii_year_of_passing());
			pstmt.setString(13, td.getXii_division());
			pstmt.setString(14, td.getXii_percentage_cgpa());
			pstmt.setFloat(15, td.getXii_percentage());
			pstmt.setFloat(16, td.getXii_cgpa_obt());
			pstmt.setString(17, td.getXii_cgpa_max());
			pstmt.setString(18, td.getUg_university());
			pstmt.setString(19, td.getUg_college());
			pstmt.setString(20, td.getUg_qualification());
			pstmt.setString(21, td.getUg_discipline());
			pstmt.setInt(22, td.getUg_year_of_passing());
			pstmt.setString(23, td.getUg_division());
			pstmt.setString(24, td.getUg_percentage_cgpa());
			pstmt.setFloat(25, td.getUg_percentage());
			pstmt.setFloat(26, td.getUg_cgpa_obt());
			pstmt.setString(27, td.getUg_cgpa_max());
			pstmt.setString(28, td.getPg_university());
			pstmt.setString(23, td.getPg_college());
			pstmt.setString(30, td.getPg_qualification());
			pstmt.setString(31, td.getPg_discipline());
			pstmt.setInt(32, td.getPg_year_of_passing());
			pstmt.setString(33, td.getPg_division());
			pstmt.setString(34, td.getPg_percentage_cgpa());
			pstmt.setFloat(35, td.getPg_percentage());
			pstmt.setFloat(36, td.getPg_cgpa_obt());
			pstmt.setString(37, td.getPg_cgpa_max());
			pstmt.setString(38, td.getPd_university());
			pstmt.setString(39, td.getPd_college());
			pstmt.setString(40, td.getPd_qualification());
			pstmt.setString(41, td.getPd_discipline());
			pstmt.setInt(42, td.getPd_year_of_passing());
			pstmt.setString(43, td.getPd_division());
			pstmt.setString(44, td.getPd_percentage_cgpa());
			pstmt.setFloat(45, td.getPd_percentage());
			pstmt.setFloat(46, td.getPd_cgpa_obt());
			pstmt.setString(47, td.getPd_cgpa_max());
			logMgr.accessLog(pstmt.toString());

			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			con.close();
		}
		return i;
	}

	public int saveExperienceData(Temp_Data td) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		int i = 0;
		try {
			logMgr.accessLog("in saveExperienceData of Save operation.");
			con = conMgr.getConnection(lPoolName);
			pstmt = con.prepareStatement(
					"insert into temp_data(experience,exp_years,exp_months,emp_name,emp_address,time_from,time_to) Values(?,?,?,?,?,?,?)");

			pstmt.setString(1, td.getExperience());
			pstmt.setInt(2, td.getExp_years());
			pstmt.setInt(3, td.getExp_months());
			pstmt.setString(4, td.getEmp_name());
			pstmt.setString(5, td.getEmp_address());
			pstmt.setDate(6, td.getTime_from());
			pstmt.setDate(7, td.getTime_to());
			logMgr.accessLog(pstmt.toString());
			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			con.close();
		}
		return i;
	}
}