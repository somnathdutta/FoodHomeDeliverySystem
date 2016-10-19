package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.zkoss.zul.Messagebox;

import sql.SubscriptionSettingsSQL;
import Bean.DayBean;
import Bean.Flavour;
import Bean.ItemBean;
import Bean.ManageCategoryBean;
import Bean.ManageCuisinBean;
import Bean.MealBean;
import Bean.Pack;

public class SubscriptionSettingsDAO {

	public static void savePack(LinkedHashSet<ItemBean> itemList, Pack pack, Flavour flavour, Connection connection){
		boolean detailsInserted= false;
		double price = 0.0;
		for(ItemBean item : itemList){
			price += item.itemPrice;
		}
		if(itemList.size()>0){
			try {
				boolean packCreated = false;
				int packID = 0 ;
				/************ PACK CREATION **************/
				SQL:{
					PreparedStatement preparedStatement = null;
					try {
						preparedStatement = connection.prepareStatement(SubscriptionSettingsSQL.insertPackQuery);
						preparedStatement.setInt(1, pack.packTypeId);
						preparedStatement.setInt(2, flavour.getFlavourId());
						preparedStatement.setDouble(3, price);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							System.out.println("Pack created!");
							packCreated = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						connection.rollback();
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
				}
				/************ PACK MAX ID CREATION **************/
				if(packCreated){
					SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						try {
							preparedStatement = connection.prepareStatement(SubscriptionSettingsSQL.getMaxPackIdQuery);
							System.out.println(preparedStatement);
							resultSet = preparedStatement.executeQuery();
							if(resultSet.next()){
								packID = resultSet.getInt("id");
								System.out.println("pck id- > "+packID);
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							connection.rollback();
						}finally{
							if(preparedStatement != null){
								preparedStatement.close();
							}
						}	
					}
				}
				/************ PACK details CREATION **************/
				if(packID > 0){
					SQL:{
					PreparedStatement preparedStatement = null;
					try {
						preparedStatement = connection.prepareStatement(SubscriptionSettingsSQL.insertPackDetailsQuery);
						for(ItemBean itemBean : itemList){
							preparedStatement.setString(1, itemBean.itemCode);
							preparedStatement.setDouble(2, itemBean.itemPrice);
							preparedStatement.setInt(3, itemBean.meal.mealTypeId);
							preparedStatement.setInt(4, packID);
							preparedStatement.setInt(5, itemBean.dayId);
							preparedStatement.setInt(6, itemBean.qty);
							preparedStatement.addBatch();
						}
						int[] insertData = preparedStatement.executeBatch();
						for(Integer c: insertData){
							detailsInserted = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						connection.rollback();
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}	
				}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			if(detailsInserted){
				Messagebox.show("PACK SAVED SUCCESFULLY!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
			}
		}else{
			Messagebox.show("NO ITEM ADDED!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}	
	}
	
	public static void updateNewItems(Set<ItemBean> itemList, int packId, Connection connection){
		
	}
	
	public static ArrayList<Pack> loadPackList(Connection connection){
		ArrayList<Pack> packTypeList = new ArrayList<Pack>();
		if(packTypeList.size() > 0){
			packTypeList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from fapp_pack_types where is_delete='N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Pack pack = new Pack();
					pack.packType = resultSet.getString("pack_type");
					pack.packTypeId = resultSet.getInt("pack_type_id");
					packTypeList.add(pack);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return packTypeList;
	}
	
	public static ArrayList<Flavour> loadFlavourList(Connection connection){
		ArrayList<Flavour> flavourList = new ArrayList<Flavour>();
		if(flavourList.size() > 0){
			flavourList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from fapp_pack_flavour where is_delete='N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Flavour flavour = new Flavour();
					flavour.setFlavourType(resultSet.getString("flavour_type")); 
					flavour.setFlavourId(resultSet.getInt("flavour_type_id"));  
					flavourList.add(flavour);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return flavourList;
	}
	
	public static ArrayList<ItemBean> loadExistingPacks(Connection connection){
		ArrayList<ItemBean> existingPacks = new ArrayList<ItemBean>();
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				try {
					preparedStatement = connection.prepareStatement(SubscriptionSettingsSQL.existingPackQuery);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						ItemBean bean = new ItemBean();
						bean.pack.packType = resultSet.getString("pack_type");
						bean.itemPrice = resultSet.getDouble("pack_price");
						bean.flavour.setFlavourType(resultSet.getString("flavour_type"));
						bean.packId = resultSet.getInt("subscription_pack_id");
						existingPacks.add(bean);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return existingPacks;
	}
		
	public static ArrayList<MealBean> getMealtypeList(Connection connection){
		ArrayList<MealBean> mealTypeList = new ArrayList<MealBean>();
		
		if(mealTypeList.size()>0){
			mealTypeList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from fapp_pack_meal_type where is_delete='N' ";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					MealBean meal = new MealBean();
					meal.typeOfMeal = resultSet.getString("meal_type");
					meal.mealTypeId = resultSet.getInt("meal_type_id");
					mealTypeList.add(meal);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mealTypeList;
	}

	public static ArrayList<DayBean> loadDayList(Connection connection){
		 ArrayList<DayBean> dayList = new ArrayList<DayBean>();
		if(dayList.size() > 0){
			dayList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from fapp_day where is_delete='N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					DayBean day = new DayBean();
					day.setDay(resultSet.getString("day")); 
					day.setDayId(resultSet.getInt("day_id"));
					dayList.add(day);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return dayList;
	}

	public static ArrayList<ManageCuisinBean> loadCuisineList(Connection connection){
		ArrayList<ManageCuisinBean> cuisineList = new ArrayList<ManageCuisinBean>();
		if(cuisineList.size() > 0){
			cuisineList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from fapp_cuisins where is_delete='N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ManageCuisinBean cuisine = new ManageCuisinBean();
					cuisine.cuisinName = resultSet.getString("cuisin_name");
					cuisine.cuisinId = resultSet.getInt("cuisin_id");
					cuisineList.add(cuisine);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return cuisineList;
	}

	public static ArrayList<ManageCategoryBean> loadCategoryList(Connection connection, int cuisineId){
		ArrayList<ManageCategoryBean> categoryList = new ArrayList<ManageCategoryBean>();
		if(categoryList.size()>0){
			categoryList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from food_category where is_delete='N' and category_price IS NULL and cuisine_id = ?";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, cuisineId);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ManageCategoryBean category = new ManageCategoryBean();
					category.categoryName = resultSet.getString("category_name");
					category.categoryId = resultSet.getInt("category_id");
					categoryList.add(category);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return categoryList;
	}

	public static ArrayList<ItemBean> loadItemList(Connection connection ,int categoryId){
		ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();
		if(itemList.size() > 0){
			itemList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select item_name,item_price,item_code from food_items where is_delete='N' and kitchen_id IS NULL and category_id = ?";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, categoryId);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ItemBean item = new ItemBean();
					item.itemName = resultSet.getString("item_name");
					item.itemPrice = resultSet.getDouble("item_price");
					item.itemCode = resultSet.getString("item_code");
					item.isChecked = false;
					item.qty = 0;
					item.mealTypeList = SubscriptionSettingsDAO.getMealtypeList(connection);
					itemList.add(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return itemList;
	}
}
