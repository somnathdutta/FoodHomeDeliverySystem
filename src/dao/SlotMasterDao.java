package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import org.zkoss.zul.Messagebox;

import utility.FappPstm;

import com.google.android.gcm.server.Message;

import Bean.TimeSlot;

public class SlotMasterDao {

	public static ArrayList<TimeSlot> loadTimeSlot(Connection connection){
		ArrayList<TimeSlot> list = new ArrayList<TimeSlot>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		String sql = "select time_slot_id, is_active, time_slot, is_lunch, is_dinner from fapp_timeslot ";
		try {
			 preparedStatement = connection.prepareStatement(sql);
			 ResultSet resultSet = preparedStatement.executeQuery();
			 while (resultSet.next()) {
				TimeSlot bean = new TimeSlot();
				bean.slotId = resultSet.getInt("time_slot_id");
				bean.timeSlot= resultSet.getString("time_slot");
				String isActive = resultSet.getString("is_active");
				if(isActive.equalsIgnoreCase("Y")){
					bean.status = "Active";
				}else {
					bean.status = "Inactive";
				}
				
				String islunch = resultSet.getString("is_lunch");
				if(islunch.equalsIgnoreCase("Y")){
					bean.setLunchStatus("Active");
				}else {
					bean.setLunchStatus("Inactive");
				}
				
				String isDinner = resultSet.getString("is_dinner");
				if(isDinner.equalsIgnoreCase("Y")){
					bean.setDinnerStatus("Active");
				}else {
					bean.setDinnerStatus("Inactive");
				}
				
				list.add(bean);
				
			}
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return list;
	}
	
	public static int slotUpdate(Connection connection, TimeSlot bean){
		int i = 0;
		String status = bean.status;
		if(status.equalsIgnoreCase("Active")){
			status = "Y";
		}else {
			status = "N";
		}
		String lunchStatus = bean.getLunchStatus();
		String dinnerStatus = null;
		if(lunchStatus.equalsIgnoreCase("Active")){
			lunchStatus = "Y";
			dinnerStatus = "N";
		}else {
			lunchStatus = "N";
			dinnerStatus = "Y";
		}
		
		PreparedStatement preparedStatement = null;
		String sql = "update fapp_timeslot set time_slot = ?, is_active = ?, is_lunch = ?, is_dinner = ? where time_slot_id = ? ";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement = FappPstm.createQuery(connection, sql, Arrays.asList(bean.timeSlot, status,lunchStatus, dinnerStatus, bean.slotId));
			i = preparedStatement.executeUpdate();
			
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
		
	}
	
	
}
