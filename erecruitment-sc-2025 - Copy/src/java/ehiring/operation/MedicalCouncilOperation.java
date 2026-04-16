/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehiring.operation;

import ehiring.dao.Experience_Data;
import ehiring.dao.LogManager;
import ehiring.dao.MedicalCouncilDetails;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author ssd
 */
public class MedicalCouncilOperation {

    LogManager logMgr;

    public MedicalCouncilOperation(String emailLogId) {
        logMgr = LogManager.getInstance(emailLogId);
    }

    public int medData(MedicalCouncilDetails med, Connection con) throws SQLException {
        PreparedStatement pstmt = null;
        int i = 0;
        try {

            pstmt = con.prepareStatement(
                    "insert into medicalcouncildetails(registration_id,advt_no,post_no,councilregno,councilname,councilreg_date,council_validity_date,email) values ( ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?)");

            pstmt.setString(1, med.getRegistration_id());
            pstmt.setString(2, med.getAdvt_no());
            pstmt.setString(3, med.getPost_no());
            pstmt.setString(4, med.getCouncilRegNo());
            pstmt.setString(5, med.getNameCouncil());
            pstmt.setDate(6, med.getMedRegDate());
            pstmt.setDate(7, med.getValidityDate());
            pstmt.setString(8, med.getEmail());

            //logMgr.accessLog(pstmt.toString());
            logMgr.accessLog("Medical details :" + pstmt.toString());
            System.out.println("Medical details :" + pstmt.toString());

            i = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

}
