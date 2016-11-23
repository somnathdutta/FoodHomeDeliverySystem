package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import org.zkoss.zul.Messagebox;

import sql.ItemPackMasterSql;
import utility.FappPstm;
import Bean.ItemPackMaster;

public class ItemPackMasterDAO {

	public static void saveItemPackTypeMaster(ItemPackMaster itemPackMaster, Connection connection){
		int insertCount = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					preparedStatement = FappPstm.createQuery(connection, ItemPackMasterSql.insertItemPackQuery, Arrays.asList(itemPackMaster));
					preparedStatement.setString(1, itemPackMaster.getItemPackName());
					if(itemPackMaster.getStatus().equalsIgnoreCase("Active")){
						preparedStatement.setString(2, "Y");
					}else{
						preparedStatement.setString(2, "N");
					}
					preparedStatement.setString(3, itemPackMaster.getUser());
					
					insertCount = preparedStatement.executeUpdate();
				}
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
		if(insertCount>0){
			Messagebox.show("Item Pack type Saved successfully!","Success Information",Messagebox.OK,Messagebox.INFORMATION);
			
		}
	}
	
	public static void updateItemPackTypeMaster(ItemPackMaster itemPackMaster, Connection connection){
		int insertCount = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					preparedStatement = FappPstm.createQuery(connection, ItemPackMasterSql.updateItemPackQuery, Arrays.asList(itemPackMaster));
					preparedStatement.setString(1, itemPackMaster.getItemPackName());
					if(itemPackMaster.getStatus().equalsIgnoreCase("Active")){
						preparedStatement.setString(2, "Y");
					}else{
						preparedStatement.setString(2, "N");
					}
					preparedStatement.setString(3, itemPackMaster.getUser());
					preparedStatement.setInt(4, itemPackMaster.getItemPackId());
					insertCount = preparedStatement.executeUpdate();
				}
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
		if(insertCount>0){
			Messagebox.show("Item Pack type updated successfully!","Success Information",Messagebox.OK,Messagebox.INFORMATION);
			ItemPackMasterDAO.fetchAllItemPack(connection);
		}
	}
	
	public static ArrayList<ItemPackMaster> fetchAllItemPack(Connection connection){
		ArrayList<ItemPackMaster> itemPackTypeList = new ArrayList<ItemPackMaster>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					preparedStatement = FappPstm.createQuery(connection, ItemPackMasterSql.loadAllItemPackQuery, null);
					ResultSet resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						ItemPackMaster itemPackMaster = new ItemPackMaster();
						itemPackMaster.setItemPackId(resultSet.getInt("item_pack_type_id"));
						itemPackMaster.setItemPackName(resultSet.getString("pack_type"));
						if(resultSet.getString("is_active").equals("Y")){
							itemPackMaster.setStatus("Active");
						}else{
							itemPackMaster.setStatus("Deactive");
						}
						itemPackTypeList.add(itemPackMaster);
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
		return itemPackTypeList;
}
}
