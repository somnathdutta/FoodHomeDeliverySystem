package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import sql.StockSQL;
import Bean.StockCategoryBean;

public class StockDAO {

	public  static ArrayList<StockCategoryBean> fetchStockOfKitchen(String kitchenName, Connection connection){
		ArrayList<StockCategoryBean> stockList = new ArrayList<StockCategoryBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(StockSQL.loadStocksQuery);
						preparedStatement.setString(1, kitchenName);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							StockCategoryBean bean = new StockCategoryBean();
							bean.itemName = resultSet.getString("item_name");
							bean.costPrice = resultSet.getDouble("item_price");
							bean.stock = resultSet.getInt("stock");
							bean.kitchenName = kitchenName;
							stockList.add(bean);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return stockList;
	}

	public static void updateStock(StockCategoryBean stockbean,Connection connection){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_kitchen_items SET stock = ? WHERE"
							   + " kitchen_id=(SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name= ? ) and item_code = ? ";
					try {
						preparedStatement  = connection.prepareStatement(sql);
						if(stockbean.stock!=null){
							preparedStatement.setInt(1, stockbean.stock);
						}else{
							preparedStatement.setInt(1, 0);	
						}
						
						preparedStatement.setString(2, stockbean.kitchenName);
						preparedStatement.setString(3, stockbean.itemCode);
						int update = preparedStatement.executeUpdate();
						if(update>0){
							//Messagebox.show("Stock updated!");
							stockbean.stockDisability = true;
							stockbean.editVisibility = true;
							stockbean.priceDisability = true;
							stockbean.updateVisibility = false;
							Messagebox.show("Updated successfully!","Update Information",Messagebox.OK,Messagebox.INFORMATION);
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
	}
}
