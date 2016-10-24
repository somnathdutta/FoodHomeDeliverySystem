package utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

	public static void main(String[] args) {
		
	}
	public static String formatdate(Date date) {
		
		if (date != null) {
			
			return new SimpleDateFormat("dd-MMM-yyyy").format(date);
		
		} else {
		
			return "";
		}
	}
	
	public static String toStringDate(String yyyyMMDD){
		  String reformattedDate = "";
		  SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
		  SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
		  
		   try { 
		       reformattedDate = myFormat.format(fromUser.parse(yyyyMMDD));
		   } catch (ParseException e) {
		       e.printStackTrace();
		   }
		   return reformattedDate;
		 }
	/************* current date *****************/
	
	public static Date date(){
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY");
		Date date = new Date();
		
		System.out.println(dateFormat.format(date));
		
		return date;
	}
	
	public static Date strToUtilDate(String prmdate) throws ParseException{
		
		DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
		Date date = formatter.parse(prmdate);
		
		return date;
		
	}
	
	public static DateFormatter getInstance() {
		return new DateFormatter();
	}
	
}
