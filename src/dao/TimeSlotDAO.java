package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
						+" driver_user_id, time_slot_id) VALUES (?, ?);" ;
				try {
					preparedStatement = connection.prepareStatement(sql);
					for(TimeSlot slot : timeSlotList){
						if(slot.checked){
							preparedStatement.setString(1, boyUserId);
							preparedStatement.setInt(2, slot.slotId);
							preparedStatement.addBatch();
						}
					}
					int count[] = preparedStatement.executeBatch();
					for(Integer  c : count){
						saved = true;
					}
				} finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
			
			SQL:{
				PreparedStatement preparedStatement = null;
				String sql = "INSERT INTO fapp_timeslot_driver_status_tommorrow( "
						+" driver_user_id, time_slot_id) VALUES (?, ?);" ;
				try {
					preparedStatement = connection.prepareStatement(sql);
					for(TimeSlot slot : timeSlotList){
						if(slot.checked){
							preparedStatement.setString(1, boyUserId);
							preparedStatement.setInt(2, slot.slotId);
							preparedStatement.addBatch();
						}
					}
					int count[] = preparedStatement.executeBatch();
					for(Integer  c : count){
						saved = true;
					}
				} catch (Exception e) {
					Messagebox.show("Error due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
}
