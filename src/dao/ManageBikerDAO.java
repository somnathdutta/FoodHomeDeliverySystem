package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.ManageDeliveryBoyBean;
import Bean.ManageKitchens;

public class ManageBikerDAO {

	/**
	 * Load all logistics List
	 */
	public static ArrayList<ManageKitchens> loadKitchenList(Connection connection){
		ArrayList<ManageKitchens> kitchenList = new ArrayList<ManageKitchens>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT kitchen_name,kitchen_id FROM fapp_kitchen WHERE is_active='Y' AND is_delete='N'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							ManageKitchens kitchen = new ManageKitchens();
							kitchen.kitchenId = resultSet.getInt("kitchen_id");
							kitchen.kitchenName = resultSet.getString("kitchen_name");
							kitchenList.add(kitchen);
						}
						
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
						} finally{
							if(preparedStatement!=null){
								preparedStatement.close();
							}
						}		
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return kitchenList;
	}
	
	/**
	 * 
	 * Method to load delivery boy list from view "vw_delivery_boy_list"
	 */
	public static ArrayList<ManageDeliveryBoyBean> loadDeliveryBoyList(Connection connection){
		ArrayList<ManageDeliveryBoyBean> manageDeliveryBoyBeanList = new ArrayList<ManageDeliveryBoyBean>();
		try {
			SQL:{
				 PreparedStatement preparedStatement = null;
				 ResultSet resultSet = null;
				// String sql = "select * from fapp_delivery_boy where is_delete='N' and kitchen_id IS NULL";
				 String sql =  "select * from vw_delivery_boy_data ";
				 try {
					  preparedStatement =connection.prepareStatement(sql);
					
					 resultSet = preparedStatement.executeQuery();
					 while(resultSet.next()){
						 ManageDeliveryBoyBean manageDeliveryBoybean =  new ManageDeliveryBoyBean();
						 manageDeliveryBoybean.deliveryBoyUserId = resultSet.getString("delivery_boy_user_id");
						 manageDeliveryBoybean.deliveryBoyId = resultSet.getInt("delivery_boy_id");
						 manageDeliveryBoybean.name = resultSet.getString("delivery_boy_name");
						 manageDeliveryBoybean.phoneNo = resultSet.getString("delivery_boy_phn_number");
						 manageDeliveryBoybean.address = resultSet.getString("delivery_boy_address");
						 manageDeliveryBoybean.password = resultSet.getString("password");
						 manageDeliveryBoybean.boyStatus = resultSet.getString("delivery_boy_status");
						 manageDeliveryBoybean.boyStatusId = resultSet.getInt("delivery_boy_status_id");
						if(resultSet.getString("is_active").equals("Y")){
							 manageDeliveryBoybean.status = "Active";
						 }else{
							 manageDeliveryBoybean.status = "Deactive";
						 }
						if(resultSet.getString("is_pickji_boy").equals("Y")){
							manageDeliveryBoybean.isPickJiBoy = "YES";
						}else{
							manageDeliveryBoybean.isPickJiBoy = "NO";
						}
						 manageDeliveryBoybean.vehicleRegNo = resultSet.getString("delivery_boy_vehicle_reg_no");
						 manageDeliveryBoybean.orderAssigned = resultSet.getString("order_assigned");
						 manageDeliveryBoybean.kitchenId = resultSet.getInt("kitchen_id");
						 manageDeliveryBoybean.kitchenName = resultSet.getString("kitchen_name");
						 manageDeliveryBoyBeanList.add(manageDeliveryBoybean);
					 }
				} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
				} finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}		
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return manageDeliveryBoyBeanList;
	}
	
	/**
	 * 
	 * Method for saving delivery boy details to database
	 */
	public static void saveDeliveryBoyData(Connection connection,ManageDeliveryBoyBean manageDeliveryBoyBean){
		String message="";
		Boolean inserted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_delivery_boy( "
					            +"delivery_boy_name, delivery_boy_phn_number, " 
					            +"delivery_boy_user_id, password,delivery_boy_vehicle_reg_no,delivery_boy_status_id,"
					            + "kitchen_id,is_pickji_boy,is_active, created_by)"
							    +"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"	;
				try {
						preparedStatement =  connection.prepareStatement(sql);
						preparedStatement.setString(1, manageDeliveryBoyBean.name);
						preparedStatement.setString(2, manageDeliveryBoyBean.phoneNo);
						preparedStatement.setString(3, manageDeliveryBoyBean.deliveryBoyUserId);
						preparedStatement.setString(4, manageDeliveryBoyBean.password);
						if(manageDeliveryBoyBean.vehicleRegNo!=null){
							preparedStatement.setString(5, manageDeliveryBoyBean.vehicleRegNo);
						}else{
							preparedStatement.setNull(5,Types.INTEGER);
						}
						preparedStatement.setInt(6, 1);
						preparedStatement.setInt(7, manageDeliveryBoyBean.kitchenId);
						if(manageDeliveryBoyBean.isPickJiBoy.equals("YES")){
							preparedStatement.setString(8, "Y");
						}else{
							preparedStatement.setString(8, "N");
						}
						if(manageDeliveryBoyBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(9, "Y");
						}
						else{
							preparedStatement.setString(9, "N");
						}
					
						preparedStatement.setString(10, manageDeliveryBoyBean.userName);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							inserted = true;
							System.out.println("Delivery boy Details saved "+message);
						}
						if(inserted){
							Messagebox.show("Delivery boy details saved sucessfully!!");
							
						}
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	
	/**
	 * 
	 * Method for saving delivery boy details to database
	 */
	public static void updateDeliveryBoyData(Connection connection, ManageDeliveryBoyBean manageDeliveryBoyBean){
		String message="";
		Boolean inserted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql ="UPDATE fapp_delivery_boy SET delivery_boy_name=?, delivery_boy_phn_number=?,password=?,"
							+ " delivery_boy_vehicle_reg_no=?,is_active=?,delivery_boy_status_id=?,kitchen_id=?,is_pickji_boy=?, updated_by=?"
						      +" WHERE delivery_boy_id=?";

				try {
						preparedStatement =  connection.prepareStatement(sql);
						preparedStatement.setString(1, manageDeliveryBoyBean.name);
						preparedStatement.setString(2, manageDeliveryBoyBean.phoneNo);
						preparedStatement.setString(3, manageDeliveryBoyBean.password);
						if(manageDeliveryBoyBean.vehicleRegNo!=null){
							preparedStatement.setString(4, manageDeliveryBoyBean.vehicleRegNo);
						}else{
							preparedStatement.setNull(4,Types.NULL);
						}

						if(manageDeliveryBoyBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(5, "Y");
						}
						else{
							preparedStatement.setString(5, "N");
						}
						preparedStatement.setInt(6, manageDeliveryBoyBean.boyStatusBean.statusId);
						preparedStatement.setInt(7, manageDeliveryBoyBean.kitchenId);
						if(manageDeliveryBoyBean.isPickJiBoy.equals("YES")){
							preparedStatement.setString(8, "Y");
						}else{
							preparedStatement.setString(8, "N");
						}
						preparedStatement.setString(9, manageDeliveryBoyBean.userName);
						preparedStatement.setInt(10, manageDeliveryBoyBean.deliveryBoyId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							inserted = true;
							System.out.println("Delivery boy Details saved "+message);
						}
						if(inserted){
							Messagebox.show("Delivery boy details updated sucessfully!!");
							
						}
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
