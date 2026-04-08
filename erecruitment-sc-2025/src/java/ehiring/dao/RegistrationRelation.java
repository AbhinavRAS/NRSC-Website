package ehiring.dao;

public class RegistrationRelation
{
	String email;
	String registration_id;
        String advt_no;

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
        String post_no;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRegistration_id() {
		return registration_id;
	}
	public void setRegistration_id(String registration_id) {
		this.registration_id = registration_id;
	}
}