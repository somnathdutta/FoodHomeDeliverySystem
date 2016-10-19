package dao;

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

import Bean.User;

public class UserDAO {

	public static ArrayList<User> showUsers(Connection connection){
		ArrayList<User> newUserList = new ArrayList<User>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "Select * from vw_users";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							User user = new User();
							user.setUserName(resultSet.getString("username"));
							user.setEmailId(resultSet.getString("email"));
							user.setMobileNo(resultSet.getString("mobile_no"));
							user.setPassword(resultSet.getString("password"));
							newUserList.add(user);
						}
					} catch (Exception e) {
						Messagebox.show("Error Due to:"+e.getMessage(),"Error",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
						// TODO: handle exception
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return newUserList;
	}
	
	public static ArrayList<User> showUsersBetweenDates(Connection connection,Date startDate, Date endDate){
		ArrayList<User> newUserList = new ArrayList<User>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "Select * from vw_users where creation_date >= ? and creation_date <= ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setDate(1, new java.sql.Date( startDate.getTime()));
						preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							User user = new User();
							user.setUserName(resultSet.getString("username"));
							user.setEmailId(resultSet.getString("email"));
							user.setMobileNo(resultSet.getString("mobile_no"));
							user.setPassword(resultSet.getString("password"));
							newUserList.add(user);
						}
					} catch (Exception e) {
						Messagebox.show("Error Due to:"+e.getMessage(),"Error",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
						// TODO: handle exception
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return newUserList;
	}
	
	public static ArrayList<User> showSpecificUser(Connection connection, String mobileNo){
		System.out.println("On chenge:::::::::::"+mobileNo);
		ArrayList<User> newUserList = new ArrayList<User>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "Select * from vw_users where mobile_no LIKE ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, mobileNo+"%");
						System.out.println(preparedStatement);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							User user = new User();
							user.setUserName(resultSet.getString("username"));
							user.setEmailId(resultSet.getString("email"));
							user.setMobileNo(resultSet.getString("mobile_no"));
							user.setPassword(resultSet.getString("password"));
							newUserList.add(user);
						}
					} catch (Exception e) {
						Messagebox.show("Error Due to:"+e.getMessage(),"Error",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
						// TODO: handle exception
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return newUserList;
	}
	
	public static void generateExcel(ArrayList<User> userList){
		File f = null;
	      boolean bool = false;
	    if(userList.size()>0){
	    	try{
		         // create new file
		    	 String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		  		
		  		String reportNamewithPath = realPath + "newUsersReport.csv";
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
		           /* w.write("ORDER NO,ORDER DATE,ORDER STATUS,ORDER BY,MEAL TYPE,DELIVERY DATE,CONTACT NO,ORDER ITEM,ITEM CODE,ITEM DESC,QUANTITY,"
		            		+ "VENDOR NAME,RECEIVED,NOTIFIED,REJECTED,PICKED,DELIVERED,DRIVER NAME,DRIVER NUMBER\n");*/
		            w.write("USER NAME,MOBILE NUMBER,EMAIL ID\n");
		            for(int i=0;i<userList.size();i++){
		            	w.write(userList.get(i).getUserName()+","+userList.get(i).getMobileNo()
		            			+","+userList.get(i).getEmailId()+"\n");
		            }
		           
		            w.close();
		           // Desktop.getDesktop().open(f);
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

		    			final AMedia amedia = new AMedia("newUsersReport", "csv", "application/csv", bios.toByteArray());

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
	
	
}
