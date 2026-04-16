package ehiring.dao;

import java.sql.Date;

public class PersonalData {
	String registration_id;
	String advt_no;
	String post_no;
	String salutation;
	String first_name;
	String middle_name;
	String last_name;
	String gender;
	Date dob;
	String place_of_birth;
	String nationality;
	String category;
	String category_pwd;
        String category_pwd_scribe; /*ChangeId:2025041501*/
        String category_pwd_comptime; /*ChangeId:2025050807*/
	String category_ews;
	String category_exservice;
	Date category_exservice_from;
	Date category_exservice_to;
        String category_merit; // ChangeId:2023083002 newly added
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
        String bank_acc_beneficiary; // ChangeId:2025041601
        String bank_acc_no; //ChangeId: 2023111001
        String bank_ifsc_code; //ChangeId: 2023111001
        String bank_acc_doc; //ChangeId: 2025041601
	String nearest_railway_station;
	Date date_of_application;
	String preview_file;
	String eMail;
	String remarks;
	String photo;
	String signature;
	String actualEmail;
	String motherName;
	String fatherName;
	String zone;
	String status;
	String reservation_cert;
	String ews_cert;
	String disability_cert;
	String serviceman_cert;
	String totalExp;
	
	public String getTotalExp() {
		return totalExp;
	}

	public void setTotalExp(String totalExp) {
		this.totalExp = totalExp;
	}
	
	public String getReservation_cert() {
		return reservation_cert;
	}

	public void setReservation_cert(String reservation_cert) {
		this.reservation_cert = reservation_cert;
	}

	public String getEws_cert() {
		return ews_cert;
	}

	public void setEws_cert(String ews_cert) {
		this.ews_cert = ews_cert;
	}

	public String getDisability_cert() {
		return disability_cert;
	}

	public void setDisability_cert(String disability_cert) {
		this.disability_cert = disability_cert;
	}

	public String getServiceman_cert() {
		return serviceman_cert;
	}

	public void setServiceman_cert(String serviceman_cert) {
		this.serviceman_cert = serviceman_cert;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
	
	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}
	
	public String getActualEmail() {
		return actualEmail;
	}

	public void setActualEmail(String actualEmail) {
		this.actualEmail = actualEmail;
	}

	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getPreview_file() {
		return preview_file;
	}

	public void setPreview_file(String preview_file) {
		this.preview_file = preview_file;
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
        
        /*Start: ChangeId:2025041501*/
        public String getCategoryPwdScribe() {
		return category_pwd_scribe;
	}
        public void setCategoryPwdScribe(String category_pwd_scribe) {
		this.category_pwd_scribe = category_pwd_scribe;
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
        
	public void setCategory_pwd(String category_pwd) {
		this.category_pwd = category_pwd;
	}
	
	public String getCategory_ews() {
		return category_ews;
	}

	public void setCategory_ews(String category_ews) {
		this.category_ews = category_ews;
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
        
        // Start: ChangeId:2023083002 newly added
        public String getCategory_merit() {
		return category_merit;
	}
        public void setCategory_merit(String category_merit) {
		this.category_merit = category_merit;
	}
        // End: ChangeId:2023083002 newly added
     
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
	public String getMiddle_name() {
		return middle_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
	}

}