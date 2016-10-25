package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.ManageCuisinBean;
import Bean.RoadRunnrBean;

public class CreateOrderDAO {

	public static ArrayList<RoadRunnrBean> fetchActiveLocationList(Connection connection){
		ArrayList<RoadRunnrBean> locationList = new ArrayList<RoadRunnrBean>();
		try {
			SQL:{
    				PreparedStatement preparedStatement = null;
    				ResultSet resultSet = null;
    				String sql = "select zip_code,locality_name from sa_zipcode "
    						+ "where is_delete = 'N' and is_active='Y'";
    				try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							RoadRunnrBean location = new RoadRunnrBean();
							location.locatityName = resultSet.getString("locality_name");
							location.zipCode = resultSet.getString("zip_code");
							locationList.add(location);
						}
					} catch (Exception e) {
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
		
		return locationList;
	}
	
	/**
	 * Load all cuisins
	 */
	public static ArrayList<ManageCuisinBean> onloadCuisinList(Connection connection){
		ArrayList<ManageCuisinBean> cuisineList = new ArrayList<ManageCuisinBean>();
		try {
			SQL:{
					
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT * FROM fapp_cuisins WHERE is_delete='N' AND is_active='Y'";
				try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCuisinBean cuisinBean = new ManageCuisinBean();
							cuisinBean.cuisinId = resultSet.getInt("cuisin_id");
							cuisinBean.cuisinName = resultSet.getString("cuisin_name");
							cuisineList.add(cuisinBean);
						}
				} catch (Exception e) {
					Messagebox.show("Error Due To::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
		return cuisineList;
	}
}
