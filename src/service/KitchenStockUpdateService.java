package service;

import java.sql.Connection;
import java.util.ArrayList;
import dao.KitchenStockUpdateDao;
import Bean.KitchenStockUpdateViewBean;

public class KitchenStockUpdateService {
	public static boolean flagInsert = false;
	
	public static ArrayList<KitchenStockUpdateViewBean> fetchKitchenStockDetails(Connection connection,String kichenName){
		ArrayList<KitchenStockUpdateViewBean> list = null;
		return list = KitchenStockUpdateDao.FetchKitchenStock(connection,kichenName);
	}

	public static boolean updateKitchenStockDetails(Connection connection,Integer stockUpdationId,Integer lunchStock, Integer dinnerStock){
		return flagInsert = KitchenStockUpdateDao.updateKichenStock(connection, stockUpdationId, lunchStock, dinnerStock);
	}
	
	/***************************************************************************************************************************************************/
	
	public static boolean isFlagInsert() {
		return flagInsert;
	}
	public static void setFlagInsert(boolean flagInsert) {
		KitchenStockUpdateService.flagInsert = flagInsert;
	}
}
