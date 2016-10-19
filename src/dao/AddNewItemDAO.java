package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.zkoss.zul.Messagebox;

import Bean.ItemBean;
import sql.AddNewItemSQL;
import sql.PackDetailsSQL;
import sql.SubscriptionSettingsSQL;

public class AddNewItemDAO {
	
	public static void insertNewItem(Connection connection,
			LinkedHashSet<ItemBean> itemList) {
	boolean detailsInserted = false;double totPrice = 0.0;
	int packId=0;
	for(ItemBean itemBean : itemList)
		packId = itemBean.packId;
		try {
			SQL: {
					PreparedStatement preparedStatement = null;
					try {
						preparedStatement = connection
								.prepareStatement(SubscriptionSettingsSQL.insertPackDetailsQuery);
						for (ItemBean itemBean : itemList) {
							preparedStatement.setString(1, itemBean.itemCode);
							preparedStatement.setDouble(2, itemBean.itemPrice);
							preparedStatement.setInt(3, itemBean.meal.mealTypeId);
							preparedStatement.setInt(4, itemBean.packId);
							preparedStatement.setInt(5, itemBean.dayId);
							preparedStatement.setInt(6, itemBean.qty);
							preparedStatement.addBatch();
						}
						int[] insertData = preparedStatement.executeBatch();
						for (Integer c : insertData) {
							detailsInserted = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						connection.rollback();
					} finally {
						if (preparedStatement != null) {
							preparedStatement.close();
						}
					}
			}
		
		SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				try {
					preparedStatement = connection.prepareStatement(PackDetailsSQL.totalPriceQuery);
					preparedStatement.setInt(1, packId);
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
					preparedStatement.setInt(2, packId);
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
		
		if(detailsInserted){
			Messagebox.show("New Item Added successfully!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
}
