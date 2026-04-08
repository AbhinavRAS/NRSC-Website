package ehiring.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class CategoryData {
	@SuppressWarnings("rawtypes")
	LinkedHashMap<String, ArrayList> categoryMap = new LinkedHashMap<>();
	String status;
	Date date_of_status_update;

	@SuppressWarnings("rawtypes")
	public LinkedHashMap<String, ArrayList> getCategoryMap() {
		return categoryMap;
	}

	@SuppressWarnings("rawtypes")
	public void setCategoryMap(LinkedHashMap<String, ArrayList> categoryMap) {
		this.categoryMap = categoryMap;
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
