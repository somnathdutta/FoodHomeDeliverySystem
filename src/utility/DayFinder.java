package utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayFinder {


	public static String getDayName(String dateInStringddMMYYYY){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(toDate(dateInStringddMMYYYY));
		String dayName = null;
		int day = calendar.get(Calendar.DAY_OF_WEEK);

		switch (day) {
		case 1:
			dayName = "SUNDAY";
			break;
		case 2:
			dayName = "MONDAY";
			break;
		case 3:
			dayName = "TUESDAY";
			break;
		case 4:
			dayName = "WEDNESDAY";
			break;
		case 5:
			dayName = "THURSDAY";
			break;	
		case 6:
			dayName = "FRIDAY";
			break;
		case 7:
			dayName = "SATURDAY";
			break;
		}
		return dayName;
	}

	public static Date toDate(String startDateString){
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy"); 
		Date startDate = null;
		try {
			startDate = df.parse(startDateString);
			//String newDateString = df.format(startDate);
			//System.out.println(newDateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return startDate;
	}
}
