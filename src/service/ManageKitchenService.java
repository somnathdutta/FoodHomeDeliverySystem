package service;

import java.sql.Connection;
import java.util.ArrayList;

import dao.ManageKitchenDAO;
import Bean.ItemTypeBean;
import Bean.ManageKitchens;

public class ManageKitchenService {

	public static ArrayList<ItemTypeBean> loadItemType(Connection connection){
		ArrayList<ItemTypeBean> list = new ArrayList<ItemTypeBean>();
		list = ManageKitchenDAO.loadItemTypes(connection);
		return list;
	}
	
	public static int updateItemType(Connection connection, ItemTypeBean bean, String userName){
		int i = 0;
		i = ManageKitchenDAO.updateItemType(connection, bean, userName);
		return i;
	}
	
	public static int saveItemType(Connection connection, ItemTypeBean bean, String userName){
		int i = 0;
		i = ManageKitchenDAO.SaveItemType(connection, bean, userName);
		return i;
	}
	
	public static ArrayList<ItemTypeBean> loadKitchenItemType(Connection connection, Integer kitchenId){
		ArrayList<ItemTypeBean> list= new ArrayList<ItemTypeBean>();
		list = ManageKitchenDAO.loadItemTypeKitchen(connection, kitchenId);
		return list;
		
	}
	
	public static ArrayList<ItemTypeBean> loadKitItTyNotInK(Connection connection, Integer kitchenId){
		ArrayList<ItemTypeBean> list= new ArrayList<ItemTypeBean>();
		list = ManageKitchenDAO.loadKitchenItemTypeNotInKitchen(connection, kitchenId);
		return list;
	}
	
	public static int insertItemTypeKitchenQty(Connection connection,Integer kId, ItemTypeBean bean){
		int i = 0;
		i = ManageKitchenDAO.AddNewItemTypeToKitchen(connection, kId, bean);
		return i;
	}
	
	public static int updateKitchenItemType(Connection connection, ItemTypeBean bean){
		int i = 0;
		i = ManageKitchenDAO.updateKitchenItemTypeStock(connection, bean);
		return i;
		
	}
	
	public static int updateKitchenSingleOrder(Connection connection, ManageKitchens kitchens){
		int i = 0;
		i = ManageKitchenDAO.updateKitchenSingleOrder(connection, kitchens);
		return i;
	}
	
	public static ArrayList<ManageKitchens> loadKitchenSingleOrder(Connection connection){
		ArrayList<ManageKitchens> list = new ArrayList<ManageKitchens>();
		list = ManageKitchenDAO.loadKitchenSingleOrder(connection);
		return list;
	}
	
}
