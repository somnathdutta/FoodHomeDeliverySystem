package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.ItemBean;
import Bean.ManageKitchens;

public class ManageKitchenDAO {

	public static ArrayList<ManageKitchens> fetchKitchens(Connection connection){
		ArrayList<ManageKitchens> kitchenList = new ArrayList<ManageKitchens>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_kitchens_details where is_active ='Y' and is_delete = 'N'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageKitchens kitchen = new ManageKitchens();
							kitchen.kitchenId = resultSet.getInt("kitchen_id");
							kitchen.kitchenName = resultSet.getString("kitchen_name");
							kitchenList.add(kitchen);
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error Due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
		return kitchenList;
	}
	
	public static ArrayList<ItemBean> fetchKitchenItems(int kitchenId, Connection connection){
		ArrayList<ItemBean> kitchenItemList = new ArrayList<ItemBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_all_kitchen_items where kitchen_id = ? order by item_code";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchenId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ItemBean item = new ItemBean();
							item.itemCode = resultSet.getString("item_code");
							item.itemName = resultSet.getString("item_name");
							item.itemDescription = resultSet.getString("item_description");
							item.categoryId = resultSet.getInt("category_id");
							item.categoryName = resultSet.getString("category_name");
							if(resultSet.getString("is_active").equals("Y")){
								item.status = "Active";
							}else{
								item.status = "Deactive";
							}
							item.lunchStock = resultSet.getInt("stock");
							item.dinnerStock = resultSet.getInt("dinner_stock");
							item.tommorrowLunchStock = resultSet.getInt("stock_tomorrow");
							item.tomorrowDinnerStock = resultSet.getInt("dinner_stock_tomorrow");
							kitchenItemList.add(item);
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error Due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
		return kitchenItemList;
	}
	
	public static boolean updateItem(Connection  connection,String status, int kicthenId, 
			String itemCode, boolean isAlaCarte){
		boolean updated  = false;
		
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "";
					if(isAlaCarte){
						sql = "UPDATE fapp_kitchen_items "
								   +" SET is_active=?,is_alacarte ='Y' "
								   +" WHERE item_code=? and  kitchen_id=? ";
					}else{
						sql = "UPDATE fapp_kitchen_items "
								   +" SET is_active=? "
								   +" WHERE item_code=? and  kitchen_id=? ";
					}
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						if(status.equalsIgnoreCase("Active")){
							preparedStatement.setString(1, "Y");
						}else{
							preparedStatement.setString(1, "N");
						}
						preparedStatement.setString(2, itemCode);
						preparedStatement.setInt(3, kicthenId);
						int updateCount = preparedStatement.executeUpdate();
						if(updateCount > 0){
							updated = true;
						}
					} catch (Exception e) {
						Messagebox.show("Error Due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
		if(updated){
			Messagebox.show("Updated Successfully!","Suucess",Messagebox.OK,Messagebox.INFORMATION);
			return true;
		}else{
			return false;
		}
	}
	
	public static void updateStock(ManageKitchens kitchen, Connection connection){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "Update fapp_kitchen_items set stock = ?,dinner_stock = ?,stock_tomorrow=?, dinner_stock_tomorrow = ?"
							+ " where kitchen_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchen.lunchStock);
						preparedStatement.setInt(2, kitchen.dinnerStock);
						preparedStatement.setInt(3, kitchen.lunchStock);
						preparedStatement.setInt(4, kitchen.dinnerStock);
						preparedStatement.setInt(5, kitchen.kitchenId);
						int count = preparedStatement.executeUpdate();
						if(count>0)
							System.out.println("Stock updated!");
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(e);
						e.printStackTrace();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
