package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.image.AImage;
import org.zkoss.zhtml.Pre;
import org.zkoss.zul.Messagebox;

import Bean.ItemBean;

public class FoodItemDAO {

	public static ArrayList<ItemBean> loadFoodItems(Connection connection, int cuisineId, int categoryId){
		ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "";
					if(cuisineId >0 && categoryId >0){
						sql = "select * from vw_food_item_details where cuisine_id = ? and category_id = ?";
					}else if(cuisineId > 0){
						sql = "select * from vw_food_item_details where cuisine_id = ?";
					}else if(categoryId >0) {
						sql = "select * from vw_food_item_details where category_id = ?";
					}else{
						sql = "select * from vw_food_item_details ";
					}
					try {
						if(cuisineId >0 && categoryId >0){
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setInt(1, cuisineId);
							preparedStatement.setInt(2, categoryId);
							preparedStatement.setInt(1, cuisineId);
						}else if(cuisineId >0 ) {
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setInt(1, cuisineId);
						}else if(categoryId > 0)  {
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setInt(1, categoryId);
						}else{
							preparedStatement = connection.prepareStatement(sql);
						}
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ItemBean bean = new ItemBean();
							bean.cusineName = resultSet.getString("cuisin_name");
							bean.categoryName = resultSet.getString("category_name");
							bean.categoryId = resultSet.getInt("category_id");
							bean.itemName = resultSet.getString("item_name");
							bean.itemCode = resultSet.getString("item_code");
							bean.itemPrice = resultSet.getDouble("item_price");
							bean.itemDescription = resultSet.getString("item_description");
							bean.itemId = resultSet.getInt("item_id");
							if(resultSet.getString("item_image") != null){
								bean.itemmagePath = resultSet.getString("item_image");
							}else{
								bean.itemmagePath = null;
							}
							if(bean.itemmagePath != null){
								try {
									bean.itemImage = new AImage(bean.itemmagePath);
								} catch (Exception e) {
									bean.itemImage = null;
								}
							}else{
								bean.itemImage = null;
							}
							
							if(resultSet.getString("is_active").equalsIgnoreCase("Y")){
								bean.status = "Active";
							}else{
								bean.status = "Deactive";
							}
							bean.itemTypeId = resultSet.getInt("item_type_id");
							if(resultSet.getString("type_name") != null){
								bean.itemTypeName = resultSet.getString("type_name");
							}else{
								bean.itemTypeName = "";
							}
							itemBeanList.add(bean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return itemBeanList;		
	}
	
	public static ArrayList<ItemBean> loadAlacarteTypeNameList(Connection connection){
		ArrayList<ItemBean> alaCarteTypeList = new ArrayList<ItemBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select item_type_id,type_name from fapp_alacarte_item_type_master where "
							+ " is_active ='Y'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next() ) {
							ItemBean alaType = new ItemBean();
							alaType.itemTypeId = resultSet.getInt("item_type_id");
							alaType.itemTypeName = resultSet.getString("type_name");
									
							alaCarteTypeList.add(alaType);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("ERROR DUE TO : "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return alaCarteTypeList;
	}
}
