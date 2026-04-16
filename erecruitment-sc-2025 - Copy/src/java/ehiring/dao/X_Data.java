package ehiring.dao;

public class X_Data {

    String registration_id;
    String advt_no;
    String post_no;
    String x_edu_board;
    String x_school;
    int x_year_of_passing;
    String x_division;
    String x_percentage_cgpa;
    float x_percentage;
    float x_cgpa_to_perc; // ChangeId: 2023121901
    float x_cgpa_obt;
    String x_cgpa_max;
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

    public String getX_edu_board() {
        return x_edu_board;
    }

    public void setX_edu_board(String x_edu_board) {
        this.x_edu_board = x_edu_board;
    }

    public String getX_school() {
        return x_school;
    }

    public void setX_school(String x_school) {
        this.x_school = x_school;
    }

    public int getX_year_of_passing() {
        return x_year_of_passing;
    }

    public void setX_year_of_passing(int x_year_of_passing) {
        this.x_year_of_passing = x_year_of_passing;
    }

    public String getX_division() {
        return x_division;
    }

    public void setX_division(String x_division) {
        this.x_division = x_division;
    }

    public String getX_percentage_cgpa() {
        return x_percentage_cgpa;
    }

    public void setX_percentage_cgpa(String x_percentage_cgpa) {
        this.x_percentage_cgpa = x_percentage_cgpa;
    }

    public float getX_percentage() {
        return x_percentage;
    }

    public void setX_percentage(float x_percentage) {
        this.x_percentage = x_percentage;
    }
    
    // Start: ChangeId: 2023121901
    public float getX_cgpa_to_perc() {
        return x_cgpa_to_perc;
    }

    public void setX_cgpa_to_perc(float x_cgpa_to_perc) {
        this.x_cgpa_to_perc = x_cgpa_to_perc;
    }
    // End: ChangeId: 2023121901

    public float getX_cgpa_obt() {
        return x_cgpa_obt;
    }

    public void setX_cgpa_obt(float x_cgpa_obt) {
        this.x_cgpa_obt = x_cgpa_obt;
    }

    public String getX_cgpa_max() {
        return x_cgpa_max;
    }

    public void setX_cgpa_max(String x_cgpa_max) {
        this.x_cgpa_max = x_cgpa_max;
    }
}
