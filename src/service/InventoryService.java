package service;

import java.sql.Connection;
import java.util.ArrayList;

import dao.InventoryDAO;
import Bean.InventoryBean;
import Bean.ItemPackMaster;
import Bean.ManageKitchens;

public class InventoryService {

	public static ArrayList<ManageKitchens> loadKitchen(Connection connection){
		ArrayList<ManageKitchens> list = new ArrayList<ManageKitchens>();
		list = InventoryDAO.loadKitchen(connection);
		return list;
	}
	
	public static ArrayList<ItemPackMaster> loadPacking(Connection connection, Integer kid){
		ArrayList<ItemPackMaster> list = new ArrayList<ItemPackMaster>();
		list = InventoryDAO.loadPackagingType(connection, kid);
		return list;
	}
	
	public static ArrayList<InventoryBean> loadKitchenPacking(Connection connection, Integer kid){
		ArrayList<InventoryBean> list = new ArrayList<InventoryBean>();
		list = InventoryDAO.loadpkgItem(connection, kid);
		return list;
	}
	
	public static int saveKitchenPackage(Connection connection, ManageKitchens kbean, ItemPackMaster ipmBean,  String user){
		int i = 0;
		i = InventoryDAO.saveKitchenPackage(connection, kbean,ipmBean, user);
		return i;
	}
	
	public static ArrayList<InventoryBean> loadInventoryDetails(Connection connection, Integer kid){
		ArrayList<InventoryBean> list = new ArrayList<InventoryBean>();
		list = InventoryDAO.loadInventoryDetails(connection, kid);
		return list;
	}
	
	public static int updateInventory(Connection connection, InventoryBean bean, String user){
		int i =0;
		i = InventoryDAO.updateInventory(connection, bean, user);
		return i;
	}
	
}
