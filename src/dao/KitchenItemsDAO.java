package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.ItemBean;

public class KitchenItemsDAO {

	public static ArrayList<ItemBean> loadItemOfKitchen(Connection  connection, int kitchenId){
		ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		try {
			SQL:{
			 PreparedStatement preparedStatement = null;
			 ResultSet resultSet = null;
			 //String sql = "select item_id,item_code,item_name,item_description,item_price from vw_category_item_details_from_kitchen where kitchen_id = ?";
			 
			 String sql = "SELECT fki.item_id,fki.item_code,fi.item_name,fi.item_description,fi.item_price, fki.item_type_id "
						+" from fapp_kitchen_items fki "
						+" JOIN food_items fi "
						+" ON fi.item_id  = fki.item_id "
						+" where fki.kitchen_id = ? order by item_code";
			 try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, kitchenId);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					int itemId = resultSet.getInt("item_id");
					String itemCode = resultSet.getString("item_code");
					String itemName = resultSet.getString("item_name");
					String itemDescription = resultSet.getString("item_description");
					Double itemPrice = resultSet.getDouble("item_price");
					int itemTypeId = resultSet.getInt("item_type_id");
					itemBeanList.add(new ItemBean(itemName, itemCode, itemDescription, itemId, itemPrice,itemTypeId));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Messagebox.show("ERROR Due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return itemBeanList;
	}
}
