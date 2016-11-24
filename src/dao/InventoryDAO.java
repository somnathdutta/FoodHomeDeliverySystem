package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import jdk.nashorn.internal.ir.SetSplitState;

import org.zkoss.zul.Messagebox;

import sql.InventorySql;
import Bean.InventoryBean;
import Bean.ItemPackMaster;
import Bean.ManageKitchens;

public class InventoryDAO {

	public static ArrayList<ManageKitchens> loadKitchen(Connection connection){
		ArrayList<ManageKitchens> list = new ArrayList<ManageKitchens>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(InventorySql.loadKitchenSql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ManageKitchens bean = new ManageKitchens();
				bean.setKitchenId(resultSet.getInt("kitchen_id"));
				bean.setKitchenName(resultSet.getString("kitchen_name"));
				
				list.add(bean);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return list;
	}
	
	
	public static ArrayList<ItemPackMaster> loadPackagingType(Connection connection, Integer kid){
		ArrayList<ItemPackMaster> list = new ArrayList<ItemPackMaster>();
		if(list.size()>0){
			list.clear();
		}
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(InventorySql.loadPackageTypeSql);
			preparedStatement.setInt(1, kid);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ItemPackMaster bean = new ItemPackMaster();
				bean.setItemPackId(resultSet.getInt("item_pack_type_id"));
				bean.setItemPackName(resultSet.getString("pack_type"));
				
				list.add(bean);
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return list;
	}
	
	public static ArrayList<InventoryBean> loadpkgItem(Connection connection, Integer kid){
		ArrayList<InventoryBean> list = new ArrayList<InventoryBean>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(InventorySql.loadkitchenPackageSql);
			preparedStatement.setInt(1, kid);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				InventoryBean bean = new InventoryBean();
				bean.setKitchenInventoryId(resultSet.getInt("fapp_kitchen_inventry_id"));
				bean.setKitchenId(resultSet.getInt("kitchen_id"));
				bean.setKitchenName(resultSet.getString("kitchen_name"));
				bean.setPackagingTypeId(resultSet.getInt("packing_type_id"));
				bean.setPackagingType(resultSet.getString("pack_type"));
				
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return list;
	}
	
	public static int saveKitchenPackage(Connection connection, ManageKitchens kbean, ItemPackMaster ipmBean, String user){
		int i = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement= connection.prepareStatement(InventorySql.saveKitchenPackeSql);
			preparedStatement.setInt(1, ipmBean.getItemPackId());
			preparedStatement.setString(2, ipmBean.getItemPackName());
			preparedStatement.setInt(3, kbean.getKitchenId());
			preparedStatement.setString(4, user);
			preparedStatement.setString(5, user);
			
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		
		return i;
	}
	
	public static ArrayList<InventoryBean> loadInventoryDetails(Connection connection, Integer kid){
		ArrayList<InventoryBean> list = new ArrayList<InventoryBean>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(InventorySql.loadinventroyDetails);
			preparedStatement.setInt(1, kid);
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				InventoryBean bean = new InventoryBean();
				bean.setStock(resultSet.getInt("stock"));
				bean.setSold(resultSet.getInt("sold"));
				bean.setKitchenInventoryId(resultSet.getInt("fapp_kitchen_inventry_id"));
				bean.setKitchenId(resultSet.getInt("kitchen_id"));
				bean.setKitchenName(resultSet.getString("kitchen_name"));
				bean.setPackagingTypeId(resultSet.getInt("packing_type_id"));
				bean.setPackagingType(resultSet.getString("pack_type"));
				
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return list;
	}
	
	public static int updateInventory(Connection connection, InventoryBean bean, String user){
		int i =0;
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement  = connection.prepareStatement(InventorySql.updateInventorySql);
			preparedStatement.setInt(1, bean.getStock());
			preparedStatement.setString(2, user);
			preparedStatement.setInt(3, bean.getKitchenInventoryId());
			
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return i;
	}
	
}
