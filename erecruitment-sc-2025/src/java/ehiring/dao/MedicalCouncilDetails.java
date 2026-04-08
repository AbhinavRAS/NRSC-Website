/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ehiring.dao;

import java.sql.Date;

/**
 *
 * @author ssd
 */
public class MedicalCouncilDetails {
     String registration_id;
		String advt_no;
		String post_no;
		String councilRegNo;
                String nameCouncil;
                Date medRegDate;
                String email;
                 Date validityDate;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getMedRegDate() {
        return medRegDate;
    }

    public void setMedRegDate(Date medRegDate) {
        this.medRegDate = medRegDate;
    }
               

    public String getRegistration_id() {
        return registration_id;
    }

    public void setRegistration_id(String registration_id) {
        this.registration_id = registration_id;
    }

    public String getAdvt_no() {
        return advt_no;
    }

    public void setAdvt_no(String advt_no) {
        this.advt_no = advt_no;
    }

    public String getPost_no() {
        return post_no;
    }

    public void setPost_no(String post_no) {
        this.post_no = post_no;
    }

    public String getCouncilRegNo() {
        return councilRegNo;
    }

    public void setCouncilRegNo(String councilRegNo) {
        this.councilRegNo = councilRegNo;
    }

    public String getNameCouncil() {
        return nameCouncil;
    }

    public void setNameCouncil(String nameCouncil) {
        this.nameCouncil = nameCouncil;
    }

 
    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }
    
}
