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
						if(resultSet.getString("is_single_order_biker").equals("Y")){
							manageDeliveryBoybean.isSingleOrderBoy = "YES";
						}else{
							manageDeliveryBoybean.isSingleOrderBoy = "NO";
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
					            + "kitchen_id,is_pickji_boy,is_single_order_biker,is_active, created_by)"
							    +"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"	;
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
						if(manageDeliveryBoyBean.isSingleOrderBoy.equals("YES")){
							preparedStatement.setString(9, "Y");
						}else{
							preparedStatement.setString(9, "N");
						}
						if(manageDeliveryBoyBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(10, "Y");
						}
						else{
							preparedStatement.setString(10, "N");
						}
					
						preparedStatement.setString(11, manageDeliveryBoyBean.userName);
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
							+ " delivery_boy_vehicle_reg_no=?,is_active=?,delivery_boy_status_id=?,kitchen_id=?,is_pickji_boy=?, updated_by=?,"
							+ " is_single_order_biker=? "
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
						if(manageDeliveryBoyBean.isSingleOrderBoy.equals("YES")){
							preparedStatement.setString(10, "Y");
						}else{
							preparedStatement.setString(10, "N");
						}
						preparedStatement.setInt(11, manageDeliveryBoyBean.deliveryBoyId);
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
	
	public static int saveBikerCapacity(Connection connection, ManageDeliveryBoyBean deliveryBoyBean, String userName ){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			String sql = "insert into fapp_biker_capacity (biker_capacity, serving_location_per_slot,max_cart_capacity, created_by, updated_by) values(?,?,?,?,?)";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, deliveryBoyBean.getBikerCapacity());
				preparedStatement.setInt(2, deliveryBoyBean.getServingLocationPerSlot());
				preparedStatement.setInt(3, deliveryBoyBean.getMaximumCartCapacity());
				preparedStatement.setString(4, userName);
				preparedStatement.setString(5, userName);
				
				i = preparedStatement.executeUpdate();
				
			} catch (Exception e) {
				Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public static ArrayList<ManageDeliveryBoyBean> loadBikerCapacity(Connection connection){
		ArrayList<ManageDeliveryBoyBean> list = new ArrayList<ManageDeliveryBoyBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			String sql = "select fapp_biker_capacity_id,biker_capacity, serving_location_per_slot,max_cart_capacity from fapp_biker_capacity where is_delete = 'N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ManageDeliveryBoyBean bean = new ManageDeliveryBoyBean();
					
					bean.setBikerCapacityId(resultSet.getInt("fapp_biker_capacity_id"));
					bean.setBikerCapacity(resultSet.getInt("biker_capacity"));
					bean.setServingLocationPerSlot(resultSet.getInt("serving_location_per_slot"));
					bean.setMaximumCartCapacity(resultSet.getInt("max_cart_capacity"));
					
					list.add(bean);
				}
				
				
			} catch (Exception e) {
				Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static int updateBikerCapacity(Connection connection, ManageDeliveryBoyBean deliveryBoyBean, String userName ){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			String sql = "update fapp_biker_capacity set biker_capacity = ? , serving_location_per_slot = ?, max_cart_capacity = ?, updated_by = ? where fapp_biker_capacity_id = ?";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, deliveryBoyBean.getBikerCapacity());
				preparedStatement.setInt(2, deliveryBoyBean.getServingLocationPerSlot());
				preparedStatement.setInt(3, deliveryBoyBean.getMaximumCartCapacity());
				preparedStatement.setString(4, userName);
				preparedStatement.setInt(5, deliveryBoyBean.getBikerCapacityId());
				
				i = preparedStatement.executeUpdate();
				
			} catch (Exception e) {
				Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public static int deleteBikerCapacity(Connection connection, ManageDeliveryBoyBean deliveryBoyBean, String userName ){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			String sql = "update fapp_biker_capacity set is_delete = 'N', updated_by = ? where fapp_biker_capacity_id = ?";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, userName);
				preparedStatement.setInt(2, deliveryBoyBean.getBikerCapacityId());
				
				i = preparedStatement.executeUpdate();
				
			} catch (Exception e) {
				Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	
}
