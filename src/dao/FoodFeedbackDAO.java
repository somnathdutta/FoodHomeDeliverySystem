package dao;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Messagebox;

import utility.DatetoStringConverter;
import Bean.FeedBackFoodBean;

public class FoodFeedbackDAO {

	public static ArrayList<FeedBackFoodBean> getAllFeedBacks(Connection connection, Date startDate, Date endDate){
		ArrayList<FeedBackFoodBean> feedBackFoodBeanList = new ArrayList<FeedBackFoodBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "";
					if(startDate==null && endDate==null){
						 sql = "select * from vw_order_feedback order by order_id desc";
						 preparedStatement = connection.prepareStatement(sql);
					}else{
						 sql = "select * from vw_order_feedback where order_date >= ? and order_date <= ? order by order_id desc";
						 preparedStatement = connection.prepareStatement(sql);
						 preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()));
						 preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
					}
					
					try {
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							FeedBackFoodBean foodFeedBack = new FeedBackFoodBean();
							foodFeedBack.orderNo = resultSet.getString("order_no");
							foodFeedBack.choiceOfMenu = Double.valueOf(resultSet.getString("menu")) ;
							foodFeedBack.foodTaste = Double.valueOf(resultSet.getString("taste"));
							foodFeedBack.qnty = Double.valueOf(resultSet.getString("portion"));
							foodFeedBack.packaging = Double.valueOf(resultSet.getString("packing"));
							foodFeedBack.timing = Double.valueOf(resultSet.getString("timely_delivered"));
							foodFeedBack.orderDateValue = DatetoStringConverter.convertDbDateToString(resultSet.getString("order_date"));
							feedBackFoodBeanList.add(foodFeedBack);
						}
								
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return feedBackFoodBeanList;
	}
	
	public static void generateCSV(ArrayList<FeedBackFoodBean> feedBackFoodBeanList){
		 File f = null;
	      boolean bool = false;
	    if(feedBackFoodBeanList.size()>0){
	    	try{
		         // create new file
		    	 String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		  		
		  		String reportNamewithPath = realPath + "feedbackonfoodreport.csv";
		  		System.out.println(reportNamewithPath);
		         //f = new File("C:/Users/somnathd/Desktop/report.csv");
		  		f = new File(reportNamewithPath);
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // prints
		         System.out.println("File created: "+bool);
		         if(f.exists())
		         // deletes file from the system
		         f.delete();
		         System.out.println("delete() method is invoked");
		         // delete() is invoked
		         f.createNewFile();
		       
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // print
		         System.out.println("File created: "+bool);
		         FileOutputStream fileOutputStream = new FileOutputStream(f);
		         OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);    
		            Writer w = new BufferedWriter(osw);
		            w.write("ORDER DATE,ORDER NO,CHOICE OF MENU,FOOD TASTE,QUANTITY,PACKAGING,TIMING\n");
		            for(int i=0;i<feedBackFoodBeanList.size();i++){
		            	w.write(feedBackFoodBeanList.get(i).orderDateValue
		            			+","+feedBackFoodBeanList.get(i).orderNo
		            			+","+feedBackFoodBeanList.get(i).choiceOfMenu
		            			+","+feedBackFoodBeanList.get(i).foodTaste
		            			+","+feedBackFoodBeanList.get(i).qnty
		            			+","+feedBackFoodBeanList.get(i).packaging
		            			+","+feedBackFoodBeanList.get(i).timing+"\n");
		            }
		            w.close();
		           Desktop.getDesktop().open(f);
		           
		            FileInputStream fis = new FileInputStream(new File(reportNamewithPath));
		    		byte[] ba1 = new byte[1024];
		    		int baLength;
		    		ByteArrayOutputStream bios = new ByteArrayOutputStream();
		    		try {
		    			try {
		    				while ((baLength = fis.read(ba1)) != -1) {
		    					bios.write(ba1, 0, baLength);
		    				}
		    			} catch (Exception e1) {
		    			} finally {
		    				fis.close();
		    			}
		    			final AMedia amedia = new AMedia("feedbackonfoodreport", "csv", "application/csv", bios.toByteArray());
		    			Filedownload.save(amedia);
		    		}catch(Exception exception){		
		    		}
		      }catch(Exception e){
		         e.printStackTrace();
		      }
	    }else{
	    	Messagebox.show("No DATA found !","Information",Messagebox.OK,Messagebox.EXCLAMATION);
	    }     
	}
	
	public static void generateCSVForStars(ArrayList<FeedBackFoodBean> feedBackStarsList){
		 File f = null;
	      boolean bool = false;
	    if(feedBackStarsList.size()>0){
	    	try{
		         // create new file
		    	 String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		  		
		  		String reportNamewithPath = realPath + "starsReport.csv";
		  		System.out.println(reportNamewithPath);
		         //f = new File("C:/Users/somnathd/Desktop/report.csv");
		  		f = new File(reportNamewithPath);
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // prints
		         System.out.println("File created: "+bool);
		         if(f.exists())
		         // deletes file from the system
		         f.delete();
		         System.out.println("delete() method is invoked");
		         // delete() is invoked
		         f.createNewFile();
		       
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // print
		         System.out.println("File created: "+bool);
		         FileOutputStream fileOutputStream = new FileOutputStream(f);
		         OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);    
		            Writer w = new BufferedWriter(osw);
		            w.write("STAR,CHOICE OF MENU,FOOD TASTE,QUANTITY,PACKAGING,TIMING\n");
		            for(int i=0;i<feedBackStarsList.size();i++){
		            	w.write(i+1
		            			+","+feedBackStarsList.get(i).choiceOfMenu
		            			+","+feedBackStarsList.get(i).foodTaste
		            			+","+feedBackStarsList.get(i).qnty
		            			+","+feedBackStarsList.get(i).packaging
		            			+","+feedBackStarsList.get(i).timing+"\n");
		            }
		            w.close();
		           Desktop.getDesktop().open(f);
		           
		            FileInputStream fis = new FileInputStream(new File(reportNamewithPath));
		    		byte[] ba1 = new byte[1024];
		    		int baLength;
		    		ByteArrayOutputStream bios = new ByteArrayOutputStream();
		    		try {
		    			try {
		    				while ((baLength = fis.read(ba1)) != -1) {
		    					bios.write(ba1, 0, baLength);
		    				}
		    			} catch (Exception e1) {
		    			} finally {
		    				fis.close();
		    			}
		    			final AMedia amedia = new AMedia("starsReport", "csv", "application/csv", bios.toByteArray());
		    			Filedownload.save(amedia);
		    		}catch(Exception exception){		
		    		}
		      }catch(Exception e){
		         e.printStackTrace();
		      }
	    }else{
	    	Messagebox.show("No DATA found !","Information",Messagebox.OK,Messagebox.EXCLAMATION);
	    }     
	}

	public static int getNoOfStars(String fieldName,String stars,Connection connection){
		int noOfStars = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;					
					String sql = "select count("+fieldName+")As stars from vw_order_feedback where "+fieldName+" LIKE ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, stars+"%");
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							noOfStars = resultSet.getInt("stars");
						}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return noOfStars;
	}
	
	public static int getNoOfStarsWithinRange(String fieldName,String stars,Connection connection, Date startDatePer, Date endDatePer){
		int noOfStars = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;					
					String sql = "select count("+fieldName+")As stars from vw_order_feedback where "+fieldName+" LIKE ?"
							+ " and order_date >= ? and order_date <= ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, stars+"%");
						preparedStatement.setDate(2, new java.sql.Date(startDatePer.getTime()));
						preparedStatement.setDate(3, new java.sql.Date(endDatePer.getTime()));
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							noOfStars = resultSet.getInt("stars");
						}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return noOfStars;
	}
	
	public static ArrayList<FeedBackFoodBean> setAllStars(Connection connection){
		ArrayList<FeedBackFoodBean> feedBackStarsList = new ArrayList<FeedBackFoodBean>();
		for(int i=1;i<5;i++){
			FeedBackFoodBean foodStars = new FeedBackFoodBean();
			foodStars.stars = i;
			foodStars.choiceOfMenu = FoodFeedbackDAO.getNoOfStars("menu", String.valueOf(i), connection);
			foodStars.foodTaste = FoodFeedbackDAO.getNoOfStars("taste", String.valueOf(i), connection);
			foodStars.qnty = FoodFeedbackDAO.getNoOfStars("portion", String.valueOf(i), connection);
			foodStars.packaging = FoodFeedbackDAO.getNoOfStars("packing", String.valueOf(i), connection);
			foodStars.timing = FoodFeedbackDAO.getNoOfStars("timely_delivered", String.valueOf(i), connection);
			
			feedBackStarsList.add(foodStars);
		}
		return feedBackStarsList;		
	}
	
	public static ArrayList<FeedBackFoodBean> setAllStarsWithinRange(Connection connection, Date startDatePer, Date endDatePer){
		ArrayList<FeedBackFoodBean> feedBackStarsList = new ArrayList<FeedBackFoodBean>();
		for(int i=1;i<5;i++){
			FeedBackFoodBean foodStars = new FeedBackFoodBean();
			foodStars.stars = i;
			foodStars.choiceOfMenu = FoodFeedbackDAO.getNoOfStarsWithinRange("menu", String.valueOf(i), connection,startDatePer, endDatePer);
			foodStars.foodTaste = FoodFeedbackDAO.getNoOfStarsWithinRange("taste", String.valueOf(i), connection,startDatePer, endDatePer);
			foodStars.qnty = FoodFeedbackDAO.getNoOfStarsWithinRange("portion", String.valueOf(i), connection,startDatePer, endDatePer);
			foodStars.packaging = FoodFeedbackDAO.getNoOfStarsWithinRange("packing", String.valueOf(i), connection,startDatePer, endDatePer);
			foodStars.timing = FoodFeedbackDAO.getNoOfStarsWithinRange("timely_delivered", String.valueOf(i), connection,startDatePer, endDatePer);
			
			feedBackStarsList.add(foodStars);
		}
		return feedBackStarsList;		
	}
}
