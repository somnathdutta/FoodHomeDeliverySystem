package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.Ordertimings;

public class OrderTimingsDAO {

	public static ArrayList<Ordertimings> loadMealtypes(Connection connection){
		ArrayList<Ordertimings> mealTypeList = new ArrayList<Ordertimings>();
		try {
			SQL:{
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "SELECT meal_type_id,meal_type FROM fapp_pack_meal_type where is_active='Y' and is_delete='N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Ordertimings ordertimings = new Ordertimings();
					ordertimings.setMealId(resultSet.getInt("meal_type_id"));
					ordertimings.setMealType(resultSet.getString("meal_type"));
					mealTypeList.add(ordertimings);
				}
			} catch (Exception e) {
				// TODO: handle exception
				Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mealTypeList;
	}

	public static void saveOrderTimings(Connection connection, Ordertimings ordertimings){
		try {
			SQL:{
			PreparedStatement preparedStatement = null;
			String sql = "INSERT INTO fapp_order_timings(meal_type_id, order_open_timing, order_close_timing)"
						+"  VALUES (?, ?, ?)";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, ordertimings.getMealId());
				if(ordertimings.getLunchFromTimings()!=null && ordertimings.getLunchToTimings()!=null){
					preparedStatement.setString(2, ordertimings.getLunchFromTimingsValue() );
					preparedStatement.setString(3, ordertimings.getLunchToTimingsValue());
				}else{
					preparedStatement.setString(2, ordertimings.getDinnerFromTimingsValue() );
					preparedStatement.setString(3, ordertimings.getDinnerToTimingsValue());
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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

	public static void upadateOrderTimings(Connection connection, Ordertimings ordertimings){
		try {
			SQL:{
			PreparedStatement preparedStatement = null;
			String sql = "";
			try {
				preparedStatement = connection.prepareStatement(sql);
			} catch (Exception e) {
				// TODO: handle exception
				Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
