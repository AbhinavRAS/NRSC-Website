package ehiring.dao;

import java.sql.Date;
import java.util.ArrayList;

public class PostData {
	String post_no;
	String advt_no;
	String post_name;
	int no_of_vacancies;
	String elgDetails = new String();
	String elgibility_Id = new String();
	String mandate = new String();
	String eligibility;
	String essential_qualification;
	String discipline;
	String specialization;
	String desirable_qualification;
	String status;
	Date date_of_status_update;
	String net;
	String experience;
	String remarks;
	String select_process;
	
	
	public String getSelect_process() {
		return select_process;
	}

	public void setSelect_process(String select_process) {
		this.select_process = select_process;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getNet() {
		return net;
	}

	public void setNet(String net) {
		this.net = net;
	}

	public String getMandate() {
		return mandate;
	}

	public void setMandate(String mandate) {
		System.out.println("mandate is :"+mandate);
		this.mandate = mandate;
	}
	
	public String getElgibility_Id() {
		return elgibility_Id;
	}

	public void setElgibility_Id(String elgibility_Id) {
		this.elgibility_Id = elgibility_Id;
	}

	
	public String getElgDetails() {
		return elgDetails;
	}

	public void setElgDetails(String elgDetails) {
		this.elgDetails = elgDetails;
	}

	public String getPost_no() {
		return post_no;
	}

	public void setPost_no(String post_no) {
		this.post_no = post_no;
	}

	public String getAdvt_no() {
		return advt_no;
	}

	public void setAdvt_no(String advt_no) {
		this.advt_no = advt_no;
	}

	public String getPost_name() {
		return post_name;
	}

	public void setPost_name(String post_name) {
		this.post_name = post_name;
	}

	public int getNo_of_vacancies() {
		return no_of_vacancies;
	}

	public void setNo_of_vacancies(int no_of_vacancies) {
		this.no_of_vacancies = no_of_vacancies;
	}

	public String getEligibility() {
		return eligibility;
	}

	public void setEligibility(String eligibility) {
		this.eligibility = eligibility;
	}

	public String getEssential_qualification() {
		return essential_qualification;
	}

	public void setEssential_qualification(String essential_qualification) {
		this.essential_qualification = essential_qualification;
	}

	public String getDiscipline() {
		return discipline;
	}

	public void setDiscipline(String discipline) {
		System.out.println("discipline is.. :"+discipline);
		this.discipline = discipline;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getDesirable_qualification() {
		return desirable_qualification;
	}

	public void setDesirable_qualification(String desirable_qualification) {
		this.desirable_qualification = desirable_qualification;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDate_of_status_update() {
		return date_of_status_update;
	}

	public void setDate_of_status_update(Date date_of_status_update) {
		this.date_of_status_update = date_of_status_update;
	}
}

//    public void setEligibility(String[] eligibility) {
//        String tempStr = new String();
//        for (String postStr : eligibility) {
//            tempStr += postStr + ", ";
//        }
//        tempStr = tempStr.substring(0, tempStr.length() - 2);
//        System.out.println("TempStr : " + tempStr);
//        this.eligibility = tempStr;
//
//    }

//    public void setEssential_qualification(String[] essential_qualification) {
//        String tempStr = new String();
//        for (String postStr : essential_qualification) {
//            tempStr += postStr + ", ";
//        }
//        tempStr = tempStr.substring(0, tempStr.length() - 2);
//        System.out.println("TempStr : " + tempStr);
//        this.essential_qualification = tempStr;
//    }

//    public void setDiscipline(String[] discipline) {
//        String tempStr = new String();
//        for (String postStr : discipline) {
//            tempStr += postStr + ", ";
//        }
//        tempStr = tempStr.substring(0, tempStr.length() - 2);
//        System.out.println("TempStr : " + tempStr);
//        this.discipline = tempStr;
//    }

//    public void setSpecialization(String[] specialization) {
//        String tempStr = new String();
//        for (String postStr : specialization) {
//            tempStr += postStr + ", ";
//        }
//        tempStr = tempStr.substring(0, tempStr.length() - 2);
//        System.out.println("TempStr : " + tempStr);
//        this.specialization = tempStr;
//    }

//    public void setDesirable_qualification(String[] desirable_qualification) {
//        String tempStr = new String();
//        for (String postStr : desirable_qualification) {
//            tempStr += postStr + ", ";
//        }
//        tempStr = tempStr.substring(0, tempStr.length() - 2);
//        System.out.println("TempStr : " + tempStr);
//        this.desirable_qualification = tempStr;
//    }
