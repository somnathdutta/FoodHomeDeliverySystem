package service;

import java.sql.Connection;
import java.util.ArrayList;

import dao.FoodItemDAO;
import Bean.ManageCategoryBean;
import Bean.ManageCuisinBean;

public class FoodItemService {

	public static ArrayList<ManageCuisinBean> fetchCuisineList(Connection connection){
		ArrayList<ManageCuisinBean> list = new ArrayList<ManageCuisinBean>();
		return list = FoodItemDAO.loadCuisineList(connection);
		
	}
	
	public static ArrayList<ManageCategoryBean> fetchCategory(Connection connection, Integer cuisineId){
		ArrayList<ManageCategoryBean> list = new ArrayList<ManageCategoryBean>();
		return list = FoodItemDAO.loadCategoryList(connection, cuisineId);
		
	}
	
	public static int updateNewUserItemStatus(Connection connection, String status, int itemId){
		int i = 0;
		return i = FoodItemDAO.updateNewUserItemStatus(connection, status, itemId);
	}
	
	
}
