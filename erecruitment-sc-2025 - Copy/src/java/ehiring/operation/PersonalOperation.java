package ehiring.operation;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import ehiring.action.CurrentDateTime;
import ehiring.dao.LogManager;
import ehiring.dao.PersonalData;

public class PersonalOperation {
	public int personalData(PersonalData pd, Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		LocalDate localDate = LocalDate.now();
		Date dateToday = Date.valueOf(localDate);

		pd.setDate_of_application(dateToday);
		LogManager logMgr = LogManager.getInstance(pd.getActualEmail());

		int i = 0;
		try {
			logMgr.accessLog("Inside Personal data method");

			pstmt = con.prepareStatement("insert into personal_data(registration_id,advt_no,post_no,"
					+ " salutation,first_name,last_name,gender,dob,place_of_birth,nationality,"
					+ " category,category_pwd,category_ews,category_exservice,category_exservice_from,"
					+ " category_exservice_to,category_merit_sportname,"
					+ "	category_merit_sportlevel,marital_status,house_no,locality,town,district,state,"
					+ " pincode,p_house_no, p_locality, p_town,p_district,p_state,p_pincode,contact_no,"
					+ " alternate_contact,nearest_railway_station,date_of_application,preview_file,email,"
                                        + "remarks,photo,signature,middle_name,status,mother_name,father_name,zone,reservation_certificate,"
                                        + "ews_certificate,disability_certificate,serviceman_certificate,totalExperience,aadhaar,category_merit,"
                                        + "bank_acc_no,bank_ifsc_code,cgov_serv,cgov_serv_doj,category_pwd_scribe,bank_acc_beneficiary,bank_acc_doc,category_pwd_comptime)" // ChangeId: 2023111001, ChangeId:2023111101, 2025050807
					+ " Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"); // ChangeId: 2023111001, ChangeId:2023111101, 2025050807
                                            // PPEG-HRD: AADHAAR // ChangeId:2023083002 Category_merit newly added // ChangeId:2025041501 // ChangeId:2025041601
                                            
			pstmt.setString(1, pd.getRegistration_id());
			pstmt.setString(2, pd.getAdvt_no());
			pstmt.setString(3, pd.getPost_no());
			pstmt.setString(4, pd.getSalutation());
			pstmt.setString(5, pd.getFirst_name().toUpperCase()); // ChangeId:2023110703
			pstmt.setString(6, pd.getLast_name());
			pstmt.setString(7, pd.getGender());
			pstmt.setDate(8, pd.getDob());
			pstmt.setString(9, pd.getPlace_of_birth());
			pstmt.setString(10, pd.getNationality());
			pstmt.setString(11, pd.getCategory());
			pstmt.setString(12, pd.getCategory_pwd());
			pstmt.setString(13, pd.getCategory_ews());
			pstmt.setString(14, pd.getCategory_exservice());
			pstmt.setDate(15, pd.getCategory_exservice_from());
			pstmt.setDate(16, pd.getCategory_exservice_to());
			pstmt.setString(17, pd.getCategory_merit_sportlevel());
			pstmt.setString(18, pd.getCategory_merit_sportname());
			pstmt.setString(19, pd.getMarital_status());
			pstmt.setString(20, pd.getHouse_no());
			pstmt.setString(21, pd.getLocality());
			pstmt.setString(22, pd.getTown());
			pstmt.setString(23, pd.getDistrict());
			pstmt.setString(24, pd.getState());
			pstmt.setInt(25, pd.getPincode());
			pstmt.setString(26, pd.getP_house_no());
			pstmt.setString(27, pd.getP_locality());
			pstmt.setString(28, pd.getP_town());
			pstmt.setString(29, pd.getP_district());
			pstmt.setString(30, pd.getP_state());
			pstmt.setInt(31, pd.getP_pincode());
			pstmt.setLong(32, pd.getContact_no());
			pstmt.setLong(33, pd.getAlternate_contact());
			pstmt.setString(34, pd.getNearest_railway_station());
			pstmt.setDate(35, pd.getDate_of_application());
			pstmt.setString(36, pd.getPreview_file());
			pstmt.setString(37, pd.geteMail());
			pstmt.setString(38, pd.getRemarks());
			pstmt.setString(39, pd.getPhoto());
			pstmt.setString(40, pd.getSignature());
			pstmt.setString(41, pd.getMiddle_name());
			pstmt.setString(42,pd.getStatus());
			pstmt.setString(43,pd.getMotherName());
			pstmt.setString(44,pd.getFatherName());
			pstmt.setString(45,pd.getZone());
			pstmt.setString(46,pd.getReservation_cert());
			pstmt.setString(47,pd.getEws_cert());
			pstmt.setString(48,pd.getDisability_cert());
			pstmt.setString(49,pd.getServiceman_cert());
			pstmt.setString(50,pd.getTotalExp());
                        pstmt.setLong(51,pd.getAadhaar()); // PPEG-HRD: AADHAAR
			pstmt.setString(52,pd.getCategory_merit()); // ChangeId:2023083002 newly added
                        pstmt.setString(53,pd.getBank_Acc_No()); // ChangeId: 2023111001
                        pstmt.setString(54,pd.getBank_IFSC_Code()); // ChangeId: 2023111001
                        pstmt.setString(55,pd.getCGov_Serv()); // ChangeId: 2023111101
                        pstmt.setDate(56,pd.getCGov_Serv_DOJ()); // ChangeId: 2023111101
                        pstmt.setString(57, pd.getCategoryPwdScribe()); // ChangeId:2025041501
                        pstmt.setString(58, pd.getBank_Acc_Beneficiary()); // ChangeId:2025041501
                        pstmt.setString(59, pd.getBank_Acc_Doc()); // ChangeId:2025041501
                        pstmt.setString(60, pd.getCategoryPwdCompTime()); // ChangeId:2025050807
			logMgr.accessLog("Personal_data :"+pstmt.toString());
                        logMgr.accessLog("HRD-DEBUG :"+pd.getCGov_Serv_DOJ());

			i = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
}