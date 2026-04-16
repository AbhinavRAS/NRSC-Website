package ehiring.dao;

public class XII_Data {

    String registration_id;
    String advt_no;
    String post_no;
    String xii_edu_board;
    String xii_school;
    String xii_specialization;
    int xii_year_of_passing;
    String xii_division;
    String xii_percentage_cgpa;
    float xii_percentage;
    float xii_cgpa_to_perc; // ChangeId: 2023122001
    float xii_cgpa_obt;
    String xii_cgpa_max;
    String marksheet;
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMarksheet() {
        return marksheet;
    }

    public void setMarksheet(String marksheet) {
        this.marksheet = marksheet;
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

    public String getXii_edu_board() {
        return xii_edu_board;
    }

    public void setXii_edu_board(String xii_edu_board) {
        this.xii_edu_board = xii_edu_board;
    }

    public String getXii_school() {
        return xii_school;
    }

    public void setXii_school(String xii_school) {
        this.xii_school = xii_school;
    }

    public String getXii_specialization() {
        return xii_specialization;
    }

    public void setXii_specialization(String xii_specialization) {
        this.xii_specialization = xii_specialization;
    }

    public int getXii_year_of_passing() {
        return xii_year_of_passing;
    }

    public void setXii_year_of_passing(int xii_year_of_passing) {
        this.xii_year_of_passing = xii_year_of_passing;
    }

    public String getXii_division() {
        return xii_division;
    }

    public void setXii_division(String xii_division) {
        this.xii_division = xii_division;
    }

    public String getXii_percentage_cgpa() {
        return xii_percentage_cgpa;
    }

    public void setXii_percentage_cgpa(String xii_percentage_cgpa) {
        this.xii_percentage_cgpa = xii_percentage_cgpa;
    }

    public float getXii_percentage() {
        return xii_percentage;
    }

    public void setXii_percentage(float xii_percentage) {
        this.xii_percentage = xii_percentage;
    }
    
    // Start: ChangeId: 2023122001
    public float getXii_cgpa_to_perc() {
        return xii_cgpa_to_perc;
    }

    public void setXii_cgpa_to_perc(float xii_cgpa_to_perc) {
        this.xii_cgpa_to_perc = xii_cgpa_to_perc;
    }
    // End: ChangeId: 2023122001
    
    public float getXii_cgpa_obt() {
        return xii_cgpa_obt;
    }

    public void setXii_cgpa_obt(float xii_cgpa_obt) {
        this.xii_cgpa_obt = xii_cgpa_obt;
    }

    public String getXii_cgpa_max() {
        return xii_cgpa_max;
    }

    public void setXii_cgpa_max(String xii_cgpa_max) {
        this.xii_cgpa_max = xii_cgpa_max;
    }
}
