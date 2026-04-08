package ehiring.dao;

import java.sql.Date;

public class AdvertisementData {

	String advt_id;
	String advt_no;
	Date advt_upload_date;
	Date valid_from;
	Date valid_till;
	Date ref_date;
	String remarks;
	Date date_of_status_update;

	/*
	 * status => active ; inactive ; delete
	 */
	String status;
	
	public Date getRef_date() {
		return ref_date;
	}

	public void setRef_date(Date ref_date) {
		this.ref_date = ref_date;
	}

	public String getAdvt_id() {
		return advt_id;
	}

	public void setAdvt_id(String advt_id) {
		this.advt_id = advt_id;
	}

	public String getAdvt_no() {
		return advt_no;
	}

	public void setAdvt_no(String advt_no) {
		this.advt_no = advt_no;
	}

	public Date getAdvt_upload_date() {
		return advt_upload_date;
	}

	public void setAdvt_upload_date(Date advt_upload_date) {
		this.advt_upload_date = advt_upload_date;
	}

	public Date getValid_from() {
		return valid_from;
	}

	public void setValid_from(Date valid_from) {
		this.valid_from = valid_from;
	}

	public Date getValid_till() {
		return valid_till;
	}

	public void setValid_till(Date valid_till) {
		this.valid_till = valid_till;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
