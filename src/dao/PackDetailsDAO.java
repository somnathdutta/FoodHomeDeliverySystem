package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.zkoss.zul.Messagebox;

import sql.AddNewItemSQL;
import sql.PackDetailsSQL;
import Bean.ItemBean;

public class PackDetailsDAO {

	public static ArrayList<ItemBean> loadSavedPacks(Connection connection,  int packId){
		ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(PackDetailsSQL.loadExistingPackQuery);
						preparedStatement.setInt(1, packId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ItemBean bean = new ItemBean();
							bean.itemName = resultSet.getString("item_name");
							bean.packId = resultSet.getInt("subscription_pack_id");
							bean.qty = resultSet.getInt("item_qty");
							bean.itemPrice = resultSet.getDouble("item_price");
							bean.mealTypeList =  SubscriptionSettingsDAO.getMealtypeList(connection);
							bean.meal.mealTypeId = resultSet.getInt("meal_type_id");
							bean.meal.typeOfMeal = resultSet.getString("meal_type");
							bean.dayList = SubscriptionSettingsDAO.loadDayList(connection);
							bean.day.setDay(resultSet.getString("day"));
							bean.day.setDayId(resultSet.getInt("day_id"));
							bean.packDetailsId = resultSet.getInt("subscription_pack_details_id");
							
							itemBeanList.add(bean);
						}
					} catch (Exception e) {
						// TODO: handle exception
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
		
		return itemBeanList;
	}
	
	public static ArrayList<ItemBean> loadExistingItems(Connection connection){
		ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(PackDetailsSQL.loadExistingItemQuery);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ItemBean bean = new ItemBean();
							bean.itemName = resultSet.getString("item_name");
							bean.itemPrice = resultSet.getDouble("item_price");
							bean.itemCode = resultSet.getString("item_code");
							itemBeanList.add(bean);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return itemBeanList;
	}

	public static boolean removeItem(Connection connection,ItemBean itemBean){
		boolean deleted = false;double totPrice =0.0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
				try {
					preparedStatement = connection.prepareStatement(PackDetailsSQL.removeItemQuery);
					preparedStatement.setInt(1, itemBean.packDetailsId);
					int delete =  preparedStatement.executeUpdate();
					if(delete > 0 ){
						deleted = true;
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
		
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(PackDetailsSQL.totalPriceQuery);
						preparedStatement.setInt(1, itemBean.packId);
						System.out.println(preparedStatement);
						resultSet =  preparedStatement.executeQuery();
						if(resultSet.next() ){
							totPrice = resultSet.getDouble("total_price");
							System.out.println("Tot :: "+totPrice);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
			
			SQL:{
					PreparedStatement preparedStatement = null;
					try {
						preparedStatement = connection.prepareStatement(PackDetailsSQL.updateFinalPriceQuery);
						preparedStatement.setDouble(1, totPrice);
						preparedStatement.setInt(2, itemBean.packId);
						System.out.println(preparedStatement);
						int delete =  preparedStatement.executeUpdate();
						if(delete > 0 ){
							System.out.println("Final price updated to: "+totPrice);
						}
					} catch (Exception e) {
						// TODO: handle exception
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
		if(deleted){
			Messagebox.show("Item deleted successfully","Deletion Information",Messagebox.OK,Messagebox.INFORMATION);
		}
		return deleted;
	}

	public static void updateItem(Connection connection ,  ItemBean itemBean){
		boolean updated = false;
		double totPrice = 0.0;
		
		if(itemBean.itemPrice > 0 || itemBean.itemPrice!=null){
			if(itemBean.qty > 0 || itemBean.qty!=0){
				try {
					SQL:{
							PreparedStatement preparedStatement = null;
						try {
							preparedStatement = connection.prepareStatement(PackDetailsSQL.updateItemQuery);
							preparedStatement.setDouble(1, itemBean.itemPrice);
							preparedStatement.setInt(2, itemBean.meal.mealTypeId);
							preparedStatement.setInt(3, itemBean.day.getDayId());
							preparedStatement.setInt(4, itemBean.qty);
							preparedStatement.setInt(5, itemBean.packDetailsId);
							int delete =  preparedStatement.executeUpdate();
							if(delete > 0 ){
								updated = true;
								System.out.println("Item updated");
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}finally{
							if(preparedStatement!=null){
								preparedStatement.close();
							}
						}
					}
				 
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						try {
							preparedStatement = connection.prepareStatement(PackDetailsSQL.totalPriceQuery);
							preparedStatement.setInt(1, itemBean.packId);
							System.out.println(preparedStatement);
							resultSet =  preparedStatement.executeQuery();
							if(resultSet.next() ){
								totPrice = resultSet.getDouble("total_price");
								System.out.println("Tot :: "+totPrice);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}finally{
							if(preparedStatement!=null){
								preparedStatement.close();
							}
						}
					}
				
				SQL:{
						PreparedStatement preparedStatement = null;
						try {
							preparedStatement = connection.prepareStatement(PackDetailsSQL.updateFinalPriceQuery);
							preparedStatement.setDouble(1, totPrice);
							preparedStatement.setInt(2, itemBean.packId);
							System.out.println(preparedStatement);
							int delete =  preparedStatement.executeUpdate();
							if(delete > 0 ){
								System.out.println("Final price updated to: "+totPrice);
							}
						} catch (Exception e) {
							// TODO: handle exception
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
			}else{
				Messagebox.show("Item quantity required!","Alert Information",Messagebox.OK,Messagebox.EXCLAMATION);
			}
		}else{
			Messagebox.show("Item price required!","Alert Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}
		
		if(updated){
			Messagebox.show("Item updated successfully","Updation Information",Messagebox.OK,Messagebox.INFORMATION);
		}
	}


}
