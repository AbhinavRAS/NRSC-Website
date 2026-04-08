package ehiring.dao;

import java.sql.Date;

public class Temp_Data {

	String advt_no;
	String post_no;
	String name;
	String email;
	String salutation;
	String first_name;
	String last_name;
	String gender;
	Date dob;
	String place_of_birth;
	String nationality;
	String category;
	String category_pwd;
        String category_pwd_scribe; /*End: ChangeId:2025041501*/
        String category_pwd_comptime; /*End: ChangeId:2025050807*/
        String category_pwd_certificate; /*ChangeId:2025041501*/
	String category_exservice;
	Date category_exservice_from;
	Date category_exservice_to;
	String category_ews;
	String category_merit_sportname;
	String category_merit_sportlevel;
        String cgov_serv; // ChangeId:2023111101
	Date cgov_serv_doj; // ChangeId:2023111101
	String marital_status;
	String house_no;
	String locality;
	String town;
	String district;
	String state;
	int pincode;
	String p_house_no;
	String p_locality;
	String p_town;
	String p_district;
	String p_state;
	int p_pincode;
	long contact_no;
        long aadhaar; // PPEG-HRD AADHAAR
	long alternate_contact;
        String bank_acc_beneficiary; //ChangeId:2025041601
        String bank_acc_no; //ChangeId: 2023111001
        String bank_ifsc_code; //ChangeId: 2023111001
        String bank_acc_doc; //ChangeId: 2025041601
	String nearest_railway_station;
	Date date_of_application;
	String x_edu_board;
	String x_school;
	int x_year_of_passing;
	String x_division;
	String x_percentage_cgpa;
	float x_percentage;
        float x_cgpa_to_perc; // ChangeId: 2023121901
	float x_cgpa_obt;
	String x_cgpa_max;
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
	String ug_university;
	String ug_college;
	String ug_qualification;
	String ug_discipline;
	int ug_year_of_passing;
	String ug_division;
	String ug_percentage_cgpa;
	float ug_percentage;
	float ug_cgpa_obt;
	String ug_cgpa_max;
	String pg_university;
	String pg_college;
	String pg_qualification;
	String pg_discipline;
	int pg_year_of_passing;
	String pg_division;
	String pg_percentage_cgpa;
	float pg_percentage;
	float pg_cgpa_obt;
	String pg_cgpa_max;
	String phd_university;
	String phd_college;
	String phd_qualification;
	String phd_discipline;
	int phd_year_of_passing;
	String phd_division;
	String phd_percentage_cgpa;
	float phd_percentage;
	float phd_cgpa_obt;
	String phd_cgpa_max;
	String pd_university;
	String pd_college;
	String pd_qualification;
	String pd_discipline;
	int pd_year_of_passing;
	String pd_division;
	String pd_percentage_cgpa;
	float pd_percentage;
	float pd_cgpa_obt;
	String pd_cgpa_max;
	String experience;
	int exp_years;
	int exp_months;
	String emp_name;
	String emp_address;
	Date time_from;
	Date time_to;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPlace_of_birth() {
		return place_of_birth;
	}

	public void setPlace_of_birth(String place_of_birth) {
		this.place_of_birth = place_of_birth;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory_pwd() {
		return category_pwd;
	}

	public void setCategory_pwd(String category_pwd) {
		this.category_pwd = category_pwd;
	}
        
        /*Start: ChangeId:2025041501*/
        public String getCategoryPwdScribe() {
		return category_pwd_scribe;
	}

	public void setCategoryPwdScribe(String category_pwd_scribe) {
		this.category_pwd_scribe = category_pwd_scribe;
	}
        
        public String getCategoryPwdCertificate() {
		return category_pwd_certificate;
	}
        public void setCategoryPwdCertificate(String category_pwd_certificate) {
		this.category_pwd_certificate = category_pwd_certificate;
	}
        /*End: ChangeId:2025041501*/
        
        /*Start: ChangeId:2025050807*/
        public String getCategoryPwdCompTime() {
		return category_pwd_comptime;
	}

	public void setCategoryPwdCompTime(String category_pwd_comptime) {
		this.category_pwd_comptime = category_pwd_comptime;
	}
        /*End: ChangeId:2025050807*/

	public String getCategory_exservice() {
		return category_exservice;
	}

	public void setCategory_exservice(String category_exservice) {
		this.category_exservice = category_exservice;
	}

	public Date getCategory_exservice_from() {
		return category_exservice_from;
	}

	public void setCategory_exservice_from(Date category_exservice_from) {
		this.category_exservice_from = category_exservice_from;
	}

	public Date getCategory_exservice_to() {
		return category_exservice_to;
	}

	public void setCategory_exservice_to(Date category_exservice_to) {
		this.category_exservice_to = category_exservice_to;
	}
        
        // Start: ChangeId:2023111101
        public String getCGov_Serv() {
		return cgov_serv;
	}

	public void setCGov_Serv(String cgov_serv) {
		this.cgov_serv = cgov_serv;
	}
        
        public Date getCGov_Serv_DOJ() {
		return cgov_serv_doj;
	}

	public void setCGov_Serv_DOJ(Date cgov_serv_doj) {
		this.cgov_serv_doj = cgov_serv_doj;
	}
        // End: ChangeId:2023111101

	public String getCategory_ews() {
		return category_ews;
	}

	public void setCategory_ews(String category_ews) {
		this.category_ews = category_ews;
	}

	public String getCategory_merit_sportname() {
		return category_merit_sportname;
	}

	public void setCategory_merit_sportname(String category_merit_sportname) {
		this.category_merit_sportname = category_merit_sportname;
	}

	public String getCategory_merit_sportlevel() {
		return category_merit_sportlevel;
	}

	public void setCategory_merit_sportlevel(String category_merit_sportlevel) {
		this.category_merit_sportlevel = category_merit_sportlevel;
	}

	public String getMarital_status() {
		return marital_status;
	}

	public void setMarital_status(String marital_status) {
		this.marital_status = marital_status;
	}

	public String getHouse_no() {
		return house_no;
	}

	public void setHouse_no(String house_no) {
		this.house_no = house_no;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
	}

	public String getP_house_no() {
		return p_house_no;
	}

	public void setP_house_no(String p_house_no) {
		this.p_house_no = p_house_no;
	}

	public String getP_locality() {
		return p_locality;
	}

	public void setP_locality(String p_locality) {
		this.p_locality = p_locality;
	}

	public String getP_town() {
		return p_town;
	}

	public void setP_town(String p_town) {
		this.p_town = p_town;
	}

	public String getP_district() {
		return p_district;
	}

	public void setP_district(String p_district) {
		this.p_district = p_district;
	}

	public String getP_state() {
		return p_state;
	}

	public void setP_state(String p_state) {
		this.p_state = p_state;
	}

	public int getP_pincode() {
		return p_pincode;
	}

	public void setP_pincode(int p_pincode) {
		this.p_pincode = p_pincode;
	}

	public long getContact_no() {
		return contact_no;
	}

        // PPEG-HRD: Start AADHAAR
        public long getAadhaar() {
		return aadhaar;
	}
        // PPEG-HRD: End AADHAAR
        
	public void setContact_no(long contact_no) {
		this.contact_no = contact_no;
	}
        
        // PPEG-HRD: Start AADHAAR
        public void setAadhaar(long aadhaar) {
		this.aadhaar = aadhaar;
	}
        // PPEG-HRD: End AADHAAR
        
	public long getAlternate_contact() {
		return alternate_contact;
	}

	public void setAlternate_contact(long alternate_contact) {
		this.alternate_contact = alternate_contact;
	}

	public String getNearest_railway_station() {
		return nearest_railway_station;
	}

	public void setNearest_railway_station(String nearest_railway_station) {
		this.nearest_railway_station = nearest_railway_station;
	}
        
        //Start: ChangeId:2025041601
        public String getBank_Acc_Beneficiary() {
		return bank_acc_beneficiary;
	}

	public void setBank_Acc_Beneficiary(String bank_acc_beneficiary) {
		this.bank_acc_beneficiary = bank_acc_beneficiary;
	}
        public String getBank_Acc_Doc() {
		return bank_acc_doc;
	}

	public void setBank_Acc_Doc(String bank_acc_doc) {
		this.bank_acc_doc = bank_acc_doc;
	}
        //End: ChangeId:2025041601
        
        // Start: ChangeId: 2023111001
        public String getBank_Acc_No() {
		return bank_acc_no;
	}

	public void setBank_Acc_No(String bank_acc_no) {
		this.bank_acc_no = bank_acc_no;
	}
        public String getBank_IFSC_Code() {
		return bank_ifsc_code;
	}

	public void setBank_IFSC_Code(String bank_ifsc_code) {
		this.bank_ifsc_code = bank_ifsc_code;
	}
        // End: ChangeId: 2023111001

	public Date getDate_of_application() {
		return date_of_application;
	}

	public void setDate_of_application(Date date_of_application) {
		this.date_of_application = date_of_application;
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
        
        // Start: ChangeId: 2023121901
        public float getX_cgpa_to_perc() {
		return x_cgpa_to_perc;
	}

	public void setX_cgpa_to_perc(float x_cgpa_to_perc) {
		this.x_cgpa_to_perc = x_cgpa_to_perc;
	}
        // End: ChangeId: 2023121901
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
        
        // Start: ChangeId: 2023122001
        public float getXii_cgpa_to_perc() {
		return xii_cgpa_to_perc;
	}

	public void setXii_cgpa_to_perc(float xii_cgpa_to_perc) {
		this.xii_cgpa_to_perc = xii_cgpa_to_perc;
	}
        // End: ChangeId: 2023122001

	public String getUg_university() {
		return ug_university;
	}

	public void setUg_university(String ug_university) {
		this.ug_university = ug_university;
	}

	public String getUg_college() {
		return ug_college;
	}

	public void setUg_college(String ug_college) {
		this.ug_college = ug_college;
	}

	public String getUg_qualification() {
		return ug_qualification;
	}

	public void setUg_qualification(String ug_qualification) {
		this.ug_qualification = ug_qualification;
	}

	public String getUg_discipline() {
		return ug_discipline;
	}

	public void setUg_discipline(String ug_discipline) {
		this.ug_discipline = ug_discipline;
	}

	public int getUg_year_of_passing() {
		return ug_year_of_passing;
	}

	public void setUg_year_of_passing(int ug_year_of_passing) {
		this.ug_year_of_passing = ug_year_of_passing;
	}

	public String getUg_division() {
		return ug_division;
	}

	public void setUg_division(String ug_division) {
		this.ug_division = ug_division;
	}

	public String getUg_percentage_cgpa() {
		return ug_percentage_cgpa;
	}

	public void setUg_percentage_cgpa(String ug_percentage_cgpa) {
		this.ug_percentage_cgpa = ug_percentage_cgpa;
	}

	public float getUg_percentage() {
		return ug_percentage;
	}

	public void setUg_percentage(float ug_percentage) {
		this.ug_percentage = ug_percentage;
	}

	public float getUg_cgpa_obt() {
		return ug_cgpa_obt;
	}

	public void setUg_cgpa_obt(float ug_cgpa_obt) {
		this.ug_cgpa_obt = ug_cgpa_obt;
	}

	public String getUg_cgpa_max() {
		return ug_cgpa_max;
	}

	public void setUg_cgpa_max(String ug_cgpa_max) {
		this.ug_cgpa_max = ug_cgpa_max;
	}

	public String getPg_university() {
		return pg_university;
	}

	public void setPg_university(String pg_university) {
		this.pg_university = pg_university;
	}

	public String getPg_college() {
		return pg_college;
	}

	public void setPg_college(String pg_college) {
		this.pg_college = pg_college;
	}

	public String getPg_qualification() {
		return pg_qualification;
	}

	public void setPg_qualification(String pg_qualification) {
		this.pg_qualification = pg_qualification;
	}

	public String getPg_discipline() {
		return pg_discipline;
	}

	public void setPg_discipline(String pg_discipline) {
		this.pg_discipline = pg_discipline;
	}

	public int getPg_year_of_passing() {
		return pg_year_of_passing;
	}

	public void setPg_year_of_passing(int pg_year_of_passing) {
		this.pg_year_of_passing = pg_year_of_passing;
	}

	public String getPg_division() {
		return pg_division;
	}

	public void setPg_division(String pg_division) {
		this.pg_division = pg_division;
	}

	public String getPg_percentage_cgpa() {
		return pg_percentage_cgpa;
	}

	public void setPg_percentage_cgpa(String pg_percentage_cgpa) {
		this.pg_percentage_cgpa = pg_percentage_cgpa;
	}

	public float getPg_percentage() {
		return pg_percentage;
	}

	public void setPg_percentage(float pg_percentage) {
		this.pg_percentage = pg_percentage;
	}

	public float getPg_cgpa_obt() {
		return pg_cgpa_obt;
	}

	public void setPg_cgpa_obt(float pg_cgpa_obt) {
		this.pg_cgpa_obt = pg_cgpa_obt;
	}

	public String getPg_cgpa_max() {
		return pg_cgpa_max;
	}

	public void setPg_cgpa_max(String pg_cgpa_max) {
		this.pg_cgpa_max = pg_cgpa_max;
	}

	public String getPhd_university() {
		return phd_university;
	}

	public void setPhd_university(String phd_university) {
		this.phd_university = phd_university;
	}

	public String getPhd_college() {
		return phd_college;
	}

	public void setPhd_college(String phd_college) {
		this.phd_college = phd_college;
	}

	public String getPhd_qualification() {
		return phd_qualification;
	}

	public void setPhd_qualification(String phd_qualification) {
		this.phd_qualification = phd_qualification;
	}

	public String getPhd_discipline() {
		return phd_discipline;
	}

	public void setPhd_discipline(String phd_discipline) {
		this.phd_discipline = phd_discipline;
	}

	public int getPhd_year_of_passing() {
		return phd_year_of_passing;
	}

	public void setPhd_year_of_passing(int phd_year_of_passing) {
		this.phd_year_of_passing = phd_year_of_passing;
	}

	public String getPhd_division() {
		return phd_division;
	}

	public void setPhd_division(String phd_division) {
		this.phd_division = phd_division;
	}

	public String getPhd_percentage_cgpa() {
		return phd_percentage_cgpa;
	}

	public void setPhd_percentage_cgpa(String phd_percentage_cgpa) {
		this.phd_percentage_cgpa = phd_percentage_cgpa;
	}

	public float getPhd_percentage() {
		return phd_percentage;
	}

	public void setPhd_percentage(float phd_percentage) {
		this.phd_percentage = phd_percentage;
	}

	public float getPhd_cgpa_obt() {
		return phd_cgpa_obt;
	}

	public void setPhd_cgpa_obt(float phd_cgpa_obt) {
		this.phd_cgpa_obt = phd_cgpa_obt;
	}

	public String getPhd_cgpa_max() {
		return phd_cgpa_max;
	}

	public void setPhd_cgpa_max(String phd_cgpa_max) {
		this.phd_cgpa_max = phd_cgpa_max;
	}

	public String getPd_university() {
		return pd_university;
	}

	public void setPd_university(String pd_university) {
		this.pd_university = pd_university;
	}

	public String getPd_college() {
		return pd_college;
	}

	public void setPd_college(String pd_college) {
		this.pd_college = pd_college;
	}

	public String getPd_qualification() {
		return pd_qualification;
	}

	public void setPd_qualification(String pd_qualification) {
		this.pd_qualification = pd_qualification;
	}

	public String getPd_discipline() {
		return pd_discipline;
	}

	public void setPd_discipline(String pd_discipline) {
		this.pd_discipline = pd_discipline;
	}

	public int getPd_year_of_passing() {
		return pd_year_of_passing;
	}

	public void setPd_year_of_passing(int pd_year_of_passing) {
		this.pd_year_of_passing = pd_year_of_passing;
	}

	public String getPd_division() {
		return pd_division;
	}

	public void setPd_division(String pd_division) {
		this.pd_division = pd_division;
	}

	public String getPd_percentage_cgpa() {
		return pd_percentage_cgpa;
	}

	public void setPd_percentage_cgpa(String pd_percentage_cgpa) {
		this.pd_percentage_cgpa = pd_percentage_cgpa;
	}

	public float getPd_percentage() {
		return pd_percentage;
	}

	public void setPd_percentage(float pd_percentage) {
		this.pd_percentage = pd_percentage;
	}

	public float getPd_cgpa_obt() {
		return pd_cgpa_obt;
	}

	public void setPd_cgpa_obt(float pd_cgpa_obt) {
		this.pd_cgpa_obt = pd_cgpa_obt;
	}

	public String getPd_cgpa_max() {
		return pd_cgpa_max;
	}

	public void setPd_cgpa_max(String pd_cgpa_max) {
		this.pd_cgpa_max = pd_cgpa_max;
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