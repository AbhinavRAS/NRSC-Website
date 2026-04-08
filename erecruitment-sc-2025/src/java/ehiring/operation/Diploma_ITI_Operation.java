package ehiring.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ehiring.dao.LogManager;
import ehiring.dao.diploma_iti_data;

public class Diploma_ITI_Operation {

    LogManager logMgr;

    public Diploma_ITI_Operation(String emailLogId) {
        logMgr = LogManager.getInstance(emailLogId);
    }

    public int IntgData(diploma_iti_data igd, Connection con) throws SQLException {
        PreparedStatement pstmt = null;
        int i = 0;
        try {
            pstmt = con
                    .prepareStatement("insert into ITI_Diploma (registration_id,advt_no,post_no,specialization,college,"
                            + "year_of_passing,division,percentage_cgpa,percentage,cgpa_obt,cgpa_max,qualification_type,degree_certificate,email) Values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            pstmt.setString(1, igd.getRegistration_id());
            pstmt.setString(2, igd.getAdvt_no());
            pstmt.setString(3, igd.getPost_no());
            pstmt.setString(4, igd.getSpecialization());
            pstmt.setString(5, igd.getCollege());
            pstmt.setInt(6, igd.getYear_of_passing());
            pstmt.setString(7, igd.getDivision());
            pstmt.setString(8, igd.getPercentage_cgpa());
            pstmt.setFloat(9, igd.getPercentage());
            pstmt.setFloat(10, igd.getCgpa_obt());
            pstmt.setString(11, igd.getCgpa_max());
            pstmt.setString(12, igd.getQualType());
            pstmt.setString(13, igd.getDegCert());
            pstmt.setString(14, igd.getEmail());

            logMgr.accessLog("THE QUERY:" + pstmt.toString());

            i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

}
