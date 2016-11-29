package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.zkoss.zul.Messagebox;
import Bean.KitchenStockUpdateViewBean;

public class KitchenStockUpdateDao {
	
	public static ArrayList<KitchenStockUpdateViewBean> FetchKitchenStock(Connection connection, String kitchenName){
		ArrayList<KitchenStockUpdateViewBean> list = new ArrayList<KitchenStockUpdateViewBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_kitchen_stock_details where kitchen_name like ? ";
					try {
					 preparedStatement = connection.prepareStatement(sql);
					 preparedStatement.setString(1, "%"+kitchenName+"%");
					 resultSet = preparedStatement.executeQuery();
					 while(resultSet.next()){
						 KitchenStockUpdateViewBean updateViewBean = new KitchenStockUpdateViewBean();
						 updateViewBean.setKitchenId(resultSet.getInt("kitchen_id"));
						 updateViewBean.setKitchenName(resultSet.getString("kitchen_name"));
						 updateViewBean.setItemTypeId(resultSet.getInt("item_type_id"));
						 updateViewBean.setItemTypeName(resultSet.getString("type_name"));
						 updateViewBean.setLunchStock(resultSet.getInt("lunch_stock"));
						 updateViewBean.setDinnerStock(resultSet.getInt("dinner_stock"));
						 updateViewBean.setStockUpdationId(resultSet.getInt("stock_updation_id"));
						 
						 list.add(updateViewBean);
					 }
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<KitchenStockUpdateViewBean> loadKitchenName(Connection connection){
		ArrayList<KitchenStockUpdateViewBean> kitchenNameList = new ArrayList<KitchenStockUpdateViewBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select distinct kitchen_name,kitchen_id from vw_kitchen_stock_details ";
					try {
					 preparedStatement = connection.prepareStatement(sql);
					 resultSet = preparedStatement.executeQuery();
					 while(resultSet.next()){
						 KitchenStockUpdateViewBean updateViewBean = new KitchenStockUpdateViewBean();
						 updateViewBean.setKitchenName(resultSet.getString("kitchen_name"));
						 updateViewBean.setKitchenId(resultSet.getInt("kitchen_id"));
						 
						 kitchenNameList.add(updateViewBean);
					 }
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kitchenNameList;
	}
	
	public static boolean updateKichenStock(Connection connection,Integer stockUpdationId,Integer lunchStock, Integer dinnerStock){
		boolean inserted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "update stock_updation set lunch_stock = ?, dinner_stock = ? where stock_updation_id = ? ";
				try {
						preparedStatement =  connection.prepareStatement(sql);
						preparedStatement.setInt(1, lunchStock);
						preparedStatement.setInt(2, dinnerStock);
						preparedStatement.setInt(3, stockUpdationId);
						System.out.println(preparedStatement);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							inserted = true;
						}
						if(inserted){
							Messagebox.show("Kitchen Stock Updated Successfully.");
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
			e.printStackTrace();
		}
		return inserted;	
	}
	
}
