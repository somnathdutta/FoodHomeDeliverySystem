package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.scene.shape.Cylinder;

import org.zkoss.zul.Messagebox;

import Bean.ItemBean;
import Bean.ManageCategoryBean;

public class SetDAO {

	/**
	 * Load all categories
	 */
	public static ArrayList<ManageCategoryBean> onLoadCategoryList(Connection connection){
		ArrayList<ManageCategoryBean> categoryBeanList = new ArrayList<ManageCategoryBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					String sql = "SELECT distinct category_name,category_id FROM vw_food_item_details order by category_id  ";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean categoryBean = new ManageCategoryBean();
							categoryBean.categoryId = resultSet.getInt("category_id");
							categoryBean.categoryName = resultSet.getString("category_name");
							categoryBeanList.add(categoryBean);
						}
						
					}catch (Exception e) {
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
		return categoryBeanList;
	}
	
	public static ArrayList<ItemBean> loadItemsFromCategory(Connection  connection, int categoryId){
		ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		try {
			SQL:{
			 PreparedStatement preparedStatement = null;
			 ResultSet resultSet = null;
			 String sql = "select item_id,item_code,item_name,item_description from vw_food_item_details where category_id = ?";
			 try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, categoryId);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ItemBean item = new ItemBean();
					item.itemId = resultSet.getInt("item_id");
					item.itemCode = resultSet.getString("item_code");
					item.itemName = resultSet.getString("item_name");
					item.itemDescription = resultSet.getString("item_description");
					itemBeanList.add(item);
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
