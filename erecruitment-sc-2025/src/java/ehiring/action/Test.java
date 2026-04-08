package ehiring.action;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class Test {
	public static void main(String args[])
	{
		/*String b="F:\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\eRecruitment_NRSC\\Recruitment_data\\applicant\\saikalpana_t_nrsc_gov_in\\abcd1234\\44\\signature_saikalpana_t_nrsc_gov_in";
		String a ="F:\\eclipse-workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\eRecruitment_NRSC\\Recruitment_data\\admin\\abcd1234\\44"; 
		
		String d=b.substring(a.indexOf("admin")+10, a.length());
		System.out.println(d);
		*/
		try
		{
		
		LocalDate l = LocalDate.of(1985, 06, 12); //specify year, month, date directly
		  LocalDate now = LocalDate.now(); //gets localDate
		  Period diff = Period.between(l, now); //difference between the dates is calculated
		  System.out.println(diff.getYears() + "years" + diff.getMonths() + "months" + diff.getDays() + "days");

		  //using Calendar Object
		  String s = "1985-06-12";
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		  Date d = sdf.parse(s);
		  Calendar c = Calendar.getInstance();
		  c.setTime(d);
		  int year = c.get(Calendar.YEAR);
		  int month = c.get(Calendar.MONTH) + 1;
		  int date = c.get(Calendar.DATE);
		  LocalDate l1 = LocalDate.of(year, month, date);
		  LocalDate now1 = LocalDate.now();
		  Period diff1 = Period.between(l1, now1);
		  System.out.println("age:" + diff1.getYears() + "years");
		 }
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}
