package utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetoStringConverter {

	
	
	public static String convertStringToDate(Date indate)
	{
	   String dateString = null;
	   SimpleDateFormat sdfr = new SimpleDateFormat("h:mm:a");
	   /*you can also use DateFormat reference instead of SimpleDateFormat 
	    * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
	    */
	   try{
		dateString = sdfr.format( indate );
	   }catch (Exception ex ){
		System.out.println(ex);
	   }
	//   System.out.println("dateString- "+dateString);
	   return dateString;
	}
	
	public static String convert24DateToString(Date indate)
	{
	   String dateString = null;
	   SimpleDateFormat sdfr = new SimpleDateFormat("HH:mm");
	   /*you can also use DateFormat reference instead of SimpleDateFormat 
	    * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
	    */
	   try{
		dateString = sdfr.format( indate );
	   }catch (Exception ex ){
		System.out.println(ex);
	   }
	   System.out.println("dateString- "+dateString);
	   return dateString;
	}
	
	public static String convert24DateToStringSlot(Date indate)
	{
	   String dateString = null;
	   SimpleDateFormat sdfr = new SimpleDateFormat("HH:mm:ss");
	   /*you can also use DateFormat reference instead of SimpleDateFormat 
	    * like this: DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
	    */
	   try{
		dateString = sdfr.format( indate );
	   }catch (Exception ex ){
		   ex.printStackTrace();
	   }
	   System.out.println("dateStringslot- "+dateString);
	   return dateString;
	}
	
	public static Date convertStringTo24DateSlot(String dateString)
	{
	    Date date = null;
	   // Date formatteddate = null;
	    DateFormat df = new SimpleDateFormat("HH:mm:ss");
	    try{
	        date = df.parse(dateString);
	    //    formatteddate = df.format(date);
	    }
	    catch ( Exception ex ){
	        ex.printStackTrace();
	    }
	 //   return formatteddate;
	    System.out.println("Formated slot string to date: "+date);
	    return date;
	}
	
	public static Date convertStringTo24Date(String dateString)
	{
	    Date date = null;
	   // Date formatteddate = null;
	    DateFormat df = new SimpleDateFormat("HH:mm");
	    try{
	        date = df.parse(dateString);
	    //    formatteddate = df.format(date);
	    }
	    catch ( Exception ex ){
	        System.out.println(ex);
	    }
	 //   return formatteddate;
	  //  System.out.println("Formated string to date: "+date);
	    return date;
	}
	
	public static Date convertStringToDate(String dateString)
	{
	    Date date = null;
	   // Date formatteddate = null;
	    DateFormat df = new SimpleDateFormat("hh:mm:a");
	    try{
	        date = df.parse(dateString);
	    //    formatteddate = df.format(date);
	    }
	    catch ( Exception ex ){
	        System.out.println(ex);
	    }
	 //   return formatteddate;
	  //  System.out.println("Formated string to date: "+date);
	    return date;
	}
	
	public static String convertDbDateToString(String dbDate){
		String dateString =  null;
		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");	
		try {
			dateString = myFormat.format(fromUser.parse(dbDate));
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return dateString;
	}
	
}
