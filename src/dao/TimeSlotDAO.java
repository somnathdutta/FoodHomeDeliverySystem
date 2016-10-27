package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zhtml.Pre;
import org.zkoss.zul.Messagebox;

import Bean.ManageDeliveryBoyBean;
import Bean.TimeSlot;

public class TimeSlotDAO {

	public static ArrayList<TimeSlot> fetchTimeSlotList(Connection connection){
		ArrayList<TimeSlot> timeSlotList = new ArrayList<TimeSlot>();
		try {
			SQL:{
				 PreparedStatement preparedStatement = null;
				 ResultSet resultSet = null;
				 String sql= "SELECT time_slot_id, time_slot,is_active "
						 	+ " FROM fapp_timeslot where  is_delete='N'  ";
				 try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						TimeSlot slot = new TimeSlot();
						slot.slotId =  resultSet.getInt("time_slot_id");
						slot.timeSlot = resultSet.getString("time_slot");
						if(resultSet.getString("is_active").equals("Y")){
							slot.status = "Active";
						}else{
							slot.status = "Inactive";
						}
						timeSlotList.add(slot);		
					}
				} catch (Exception e) {
					Messagebox.show("Error due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return timeSlotList;
	}
	
	
	public static ArrayList<ManageDeliveryBoyBean> fetchBikerList(Connection connection){
		ArrayList<ManageDeliveryBoyBean> bikerList = new ArrayList<ManageDeliveryBoyBean>();
		try {
			SQL:{
				 PreparedStatement preparedStatement = null;
				 ResultSet resultSet = null;
				 String sql= "SELECT delivery_boy_name, delivery_boy_user_id  " 
							  +"  FROM fapp_delivery_boy "
							  +" where is_active='Y' and  is_delete='N' "
							  +" order by delivery_boy_user_id ;  ";
				 try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						ManageDeliveryBoyBean bikerBoy = new ManageDeliveryBoyBean();
						bikerBoy.name = resultSet.getString("delivery_boy_name");
						bikerBoy.deliveryBoyUserId = resultSet.getString("delivery_boy_user_id");
						//if(!TimeSlotDAO.isBikerSlotAssigned(bikerBoy.deliveryBoyUserId, connection))
						bikerList.add(bikerBoy);		
					}
				} catch (Exception e) {
					Messagebox.show("Error due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bikerList;
	}
	
	public static void saveTimeSlotForBiker(String boyUserId, ArrayList<TimeSlot> timeSlotList, 
			Connection connection){
		boolean saved = false;
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				String sql = "INSERT INTO fapp_timeslot_driver_status( "
						+" driver_user_id, time_slot_id,is_lunch,is_dinner) VALUES (?, ?, ?, ?);" ;
				try {
					preparedStatement = connection.prepareStatement(sql);
					for(TimeSlot slot : timeSlotList){
						if(slot.checked){
							preparedStatement.setString(1, boyUserId);
							preparedStatement.setInt(2, slot.slotId);
							String slotType = getSlotType(slot.slotId, connection);
							if(slotType.equalsIgnoreCase("LUNCH")){
								preparedStatement.setString(3, "Y");
								preparedStatement.setString(4, "N");
							}else{
								preparedStatement.setString(3, "N");
								preparedStatement.setString(4, "Y");
							}
							preparedStatement.addBatch();
						}
					}
					int count[] = preparedStatement.executeBatch();
					for(Integer  c : count){
						saved = true;
					}
				}catch(Exception e){
					String err = e.getMessage();
					e.printStackTrace();
					if(err.contains("duplicate")){
						Messagebox.show("Time slot already added!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
					}
				}
				
				finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
			
			SQL:{
				PreparedStatement preparedStatement = null;
				String sql = "INSERT INTO fapp_timeslot_driver_status_tommorrow( "
						+" driver_user_id, time_slot_id,is_lunch,is_dinner) VALUES (?, ?, ?, ?);" ;
				try {
					preparedStatement = connection.prepareStatement(sql);
					for(TimeSlot slot : timeSlotList){
						if(slot.checked){
							
							preparedStatement.setString(1, boyUserId);
							preparedStatement.setInt(2, slot.slotId);
							String slotType = getSlotType(slot.slotId, connection);
							if(slotType.equalsIgnoreCase("Y")){
								preparedStatement.setString(3, "Y");
								preparedStatement.setString(4, "N");
							}else{
								preparedStatement.setString(3, "N");
								preparedStatement.setString(4, "Y");
							}
							preparedStatement.addBatch();
						}
					}
					int count[] = preparedStatement.executeBatch();
					for(Integer  c : count){
						saved = true;
					}
				} catch (Exception e) {
					//Messagebox.show("Error due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(saved){
			Messagebox.show("Slot assigned successfully!","Successful information",Messagebox.OK,Messagebox.INFORMATION);
			
		}
	}
	
	public static void loadAll(){
		
	}
	
	public static String getSlotType(int slotId, Connection connection){
		String slotType = null;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select is_lunch from fapp_timeslot where time_slot_id = ? ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, slotId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							slotType = resultSet.getString("is_lunch");
						}
					} catch (Exception e) {
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
		return slotType;
	}
	
	public static boolean isBikerSlotAssigned(String bikerUserId,Connection connection){
		boolean isBikerAssigned = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select count(driver_user_id)AS biker from fapp_timeslot_driver_status where driver_user_id = ? ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, bikerUserId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							int count = resultSet.getInt("biker");
							if(count>0)
								isBikerAssigned = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
					}if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isBikerAssigned;
	}
	
	public static void updateBikerSlots(TimeSlot timeSlot, Connection connection){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_timeslot_driver_status set is_lunch = ?,is_dinner =? ,is_slot_active = ? "
							+ " where time_slot_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						if(timeSlot.getLunchStatus().equalsIgnoreCase("Active")){
							preparedStatement.setString(1, "Y");
							preparedStatement.setString(2, "N");
						}else{
							preparedStatement.setString(1, "N");
							preparedStatement.setString(2, "Y");
						}
						if(timeSlot.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(3, "Y");
						}else{
							preparedStatement.setString(3, "N");
						}
						preparedStatement.setInt(4, timeSlot.getSlotId());
						int count = preparedStatement.executeUpdate();
						if(count>0){
							System.out.println(count+ " rows updated in fapp_timeslot_driver_status");
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
					 
				}
			SQL1:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_timeslot_driver_status_tommorrow set is_lunch = ?,is_dinner =? ,is_slot_active = ? "
							+ " where time_slot_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						if(timeSlot.getLunchStatus().equalsIgnoreCase("Active")){
							preparedStatement.setString(1, "Y");
							preparedStatement.setString(2, "N");
						}else{
							preparedStatement.setString(1, "N");
							preparedStatement.setString(2, "Y");
						}
						if(timeSlot.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(3, "Y");
						}else{
							preparedStatement.setString(3, "N");
						}
						preparedStatement.setInt(4, timeSlot.getSlotId());
						int count = preparedStatement.executeUpdate();
						if(count>0){
							System.out.println(count+ " rows updated in fapp_timeslot_driver_status_tommorrow");
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	}
}
