package ehiring.dao;

import java.sql.Date;

public class Experience_Data {

    String registration_id;
    String advt_no;
    String post_no;
    String experience;
    int exp_years;
    int exp_months;
    int exp_days;
    String emp_name;
    String emp_address;
    Date time_from;
    Date time_to;
    String designation;
    String emp_work;
    int emp_paydrawn; // PPEG-HRD: PayDrawn
    String emp_reason; // PPEG-HRD: ReasonForLeaving
    int slno;
    String expGovt = new String();
    String expCert = new String();
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpCert() {
        return expCert;
    }

    public void setExpCert(String expCert) {
        this.expCert = expCert;
    }

    public String getExpGovt() {
        return expGovt;
    }

    public void setExpGovt(String expGovt) {
        this.expGovt = expGovt;
    }

    public String getEmp_work() {
        return emp_work;
    }
    
    // Start PPEG-HRD: PayDrawn
    public int getEmp_paydrawn() {
            return emp_paydrawn;
    }
    // End PPEG-HRD: PayDrawn

    // Start PPEG-HRD: ReasonForLeaving
    public String getEmp_reason() {
            return emp_reason;
    }
    // End PPEG-HRD: ReasonForLeaving

    public void setEmp_work(String emp_work) {
        this.emp_work = emp_work;
    }
    
    // Start PPEG-HRD: PayDrawn
    public void setEmp_paydrawn(int emp_paydrawn) {
            this.emp_paydrawn = emp_paydrawn;
    }
    // End PPEG-HRD: PayDrawn

    // Start PPEG-HRD: ReasonForLeaving
    public void setEmp_reason(String emp_reason) {
            this.emp_reason = emp_reason;
    }
    // End PPEG-HRD: ReasonForLeaving

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getExp_years() {
        return exp_years;
    }

    public void setExp_years(int exp_years) {
        this.exp_years = exp_years;
    }

    public int getExp_months() {
        return exp_months;
    }

    public void setExp_months(int exp_months) {
        this.exp_months = exp_months;
    }

    public int getExp_days() {
        return exp_days;
    }

    public void setExp_days(int exp_days) {
        this.exp_days = exp_days;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_address() {
        return emp_address;
    }

    public void setEmp_address(String emp_address) {
        this.emp_address = emp_address;
    }

    public Date getTime_from() {
        return time_from;
    }

    public void setTime_from(Date time_from) {
        this.time_from = time_from;
    }

    public Date getTime_to() {
        return time_to;
    }

    public void setTime_to(Date time_to) {
        this.time_to = time_to;
    }
}
