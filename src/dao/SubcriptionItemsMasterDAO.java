package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zhtml.Messagebox;

import com.google.android.gcm.server.Message;

import Bean.SubcriptionItemsMasterBean;
import sql.SubcriptionItemsMasterSql;

public class SubcriptionItemsMasterDAO {

	public static ArrayList<SubcriptionItemsMasterBean> loadSubItems(Connection connection){
		ArrayList<SubcriptionItemsMasterBean> list = new ArrayList<SubcriptionItemsMasterBean>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(SubcriptionItemsMasterSql.loadItemsQry);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				SubcriptionItemsMasterBean bean = new SubcriptionItemsMasterBean();
				bean.setItemId(resultSet.getInt("items_master_item_id"));
				bean.setItemName(resultSet.getString("item_name"));
				bean.setItemImage(resultSet.getString("item_image"));
				
				list.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "ERROR", Messagebox.OK, Messagebox.ERROR);
		}
		return list;
	}
	
	public static int InsertSubItems(Connection connection, SubcriptionItemsMasterBean bean, String username){
		int i =0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(SubcriptionItemsMasterSql.insertItemsQry);
			preparedStatement.setString(1, bean.getItemName());
			preparedStatement.setString(2, bean.getItemImage());
			preparedStatement.setString(3, username);
			preparedStatement.setString(4, username);
			
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().startsWith("ERROR: duplicate key")){
				Messagebox.show(bean.getItemName() + " Already Exists", "ERROR", Messagebox.OK, Messagebox.ERROR);
				bean.setItemName(null);
				bean.setItemImage(null);
			}else {
				Messagebox.show(e.getMessage(), "ERROR", Messagebox.OK, Messagebox.ERROR);
			}
			
		}
		return i;
	}
	
	public static int updateSubItems(Connection connection, SubcriptionItemsMasterBean bean, String username){
		
		int i =0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(SubcriptionItemsMasterSql.updateItemsQry);
			preparedStatement.setString(1, bean.getItemName());
			preparedStatement.setString(2, bean.getItemImage());
			preparedStatement.setString(3, username);
			preparedStatement.setInt(4, bean.getItemId());
			
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "ERROR", Messagebox.OK, Messagebox.ERROR);
		}
		return i;
	}
	
	
}
