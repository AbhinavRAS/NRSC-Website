package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ehiring.action.CurrentDateTime;
import ehiring.dao.IntCourse_Data;
import ehiring.dao.LogManager;

public class IntCourseOperation {

    LogManager logMgr;

    public IntCourseOperation(String emailLogId) {
        logMgr = LogManager.getInstance(emailLogId);
    }

    public int IntgData(IntCourse_Data igd, Connection con) throws SQLException {
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            pstmt = con.prepareStatement("insert into highereducation(registration_id,advt_no,post_no,sno,specialization,university,college,"
                    + "qualification,discipline,year_of_passing,division,percentage_cgpa,percentage,cgpa_obt,cgpa_max,qualification_type,marksheet,degree_certificate,phdtopic,phdabstract,cgpa_univ_flag,email) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pstmt.setString(1, igd.getRegistration_id());
            pstmt.setString(2, igd.getAdvt_no());
            pstmt.setString(3, igd.getPost_no());
            pstmt.setInt(4, igd.getsNo());
            pstmt.setString(5, igd.getSpecialization());
            pstmt.setString(6, igd.getUniversity());
            pstmt.setString(7, igd.getCollege());
            pstmt.setString(8, igd.getQualification());
            pstmt.setString(9, igd.getDiscipline());
            pstmt.setInt(10, igd.getYear_of_passing());
            pstmt.setString(11, igd.getDivision());
            pstmt.setString(12, igd.getPercentage_cgpa());
            pstmt.setFloat(13, igd.getPercentage());
            pstmt.setFloat(14, igd.getCgpa_obt());
            pstmt.setString(15, igd.getCgpa_max());
            pstmt.setString(16, igd.getQualType());
            pstmt.setString(17, igd.getMarksheet());
            pstmt.setString(18, igd.getDegCert());
            pstmt.setString(19, igd.getPhdTopic());
            pstmt.setString(20, igd.getPhdAbstract());
            pstmt.setString(21, igd.getCgpa_univ_flag());
            pstmt.setString(22, igd.getEmail());

            logMgr.accessLog("THE QUERY:" + pstmt.toString());

            i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

}
