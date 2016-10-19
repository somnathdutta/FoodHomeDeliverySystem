package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.AdminSettingsBean;
import Bean.ManageCategoryBean;

public class LunchDinnerTimingsDAO {

	public static void saveLD(AdminSettingsBean lunchDinnerTimingsbean, Connection connection) {
		boolean savedLD = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_timings(lunch_from, lunch_to, dinner_from, dinner_to) "
							   +" VALUES (?, ?, ?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, lunchDinnerTimingsbean.lunchFromTimingsValue);
						preparedStatement.setString(2, lunchDinnerTimingsbean.lunchToTimingsValue);
						preparedStatement.setString(3, lunchDinnerTimingsbean.dinnerFromTimingsValue);
						preparedStatement.setString(4, lunchDinnerTimingsbean.dinnerToTimingsValue);
						int update = preparedStatement.executeUpdate();
						if(update>0){
							savedLD = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(savedLD){
			Messagebox.show("Timings saved!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	public static void updateLD(AdminSettingsBean lunchDinnerTimingsbean, Connection connection) {
		boolean savedLD = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_timings SET lunch_from=?, lunch_to=?, dinner_from=?, dinner_to=? "
							+ " where timings_id = ?";
							  
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, lunchDinnerTimingsbean.lunchFromTimingsValue);
						preparedStatement.setString(2, lunchDinnerTimingsbean.lunchToTimingsValue);
						preparedStatement.setString(3, lunchDinnerTimingsbean.dinnerFromTimingsValue);
						preparedStatement.setString(4, lunchDinnerTimingsbean.dinnerToTimingsValue);
						preparedStatement.setInt(5, lunchDinnerTimingsbean.lunchDinnerTimingId);
						
						int update = preparedStatement.executeUpdate();
						if(update>0){
							savedLD = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(savedLD){
			Messagebox.show("Timings updated!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	public static ArrayList<ManageCategoryBean> loadLunchCategories(Connection connection){
		ArrayList<ManageCategoryBean> earlyBirdLunchCategoryList = new ArrayList<ManageCategoryBean>();
		if(earlyBirdLunchCategoryList.size()>0){
			earlyBirdLunchCategoryList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select category_id,category_name from food_category where category_price IS NULL ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean categoryBean = new ManageCategoryBean();
							categoryBean.categoryId = resultSet.getInt("category_id");
							categoryBean.categoryName = resultSet.getString("category_name");
							categoryBean.chkCategory = false;
							earlyBirdLunchCategoryList.add(categoryBean);		
						}
								
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}		 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return earlyBirdLunchCategoryList;
	}
	
	public static ArrayList<ManageCategoryBean> loadDinnerCategories(Connection connection){
		ArrayList<ManageCategoryBean> earlyBirdDinnerCategoryList = new ArrayList<ManageCategoryBean>();
		if( earlyBirdDinnerCategoryList.size()>0){
			earlyBirdDinnerCategoryList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select category_id,category_name from food_category where category_price IS NULL ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean categoryBean = new ManageCategoryBean();
							categoryBean.categoryId = resultSet.getInt("category_id");
							categoryBean.categoryName = resultSet.getString("category_name");
							categoryBean.chkCategory = false;
							earlyBirdDinnerCategoryList.add(categoryBean);		
						}
								
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}		 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return earlyBirdDinnerCategoryList;
	}

	public static ArrayList<AdminSettingsBean> loadSavedTimings(Connection connection){
		 ArrayList<AdminSettingsBean> lunchDinnerTimingsbeanList = new ArrayList<AdminSettingsBean>();
		if(lunchDinnerTimingsbeanList.size()>0){
			lunchDinnerTimingsbeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select lunch_from,lunch_to,dinner_from,dinner_to,timings_id from fapp_timings";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							AdminSettingsBean lunchDinnerBean = new AdminSettingsBean();
							lunchDinnerBean.lunchFromTimingsValue = resultSet.getString("lunch_from");
							lunchDinnerBean.lunchToTimingsValue = resultSet.getString("lunch_to");
							lunchDinnerBean.dinnerFromTimingsValue = resultSet.getString("dinner_from");
							lunchDinnerBean.dinnerToTimingsValue = resultSet.getString("dinner_to");
							lunchDinnerBean.lunchDinnerTimingId = resultSet.getInt("timings_id");
							lunchDinnerTimingsbeanList.add(lunchDinnerBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return lunchDinnerTimingsbeanList;
	}
	
	public static ArrayList<AdminSettingsBean> loadSavedCredits(Connection connection){
		ArrayList<AdminSettingsBean> creditbeanList = new ArrayList<AdminSettingsBean>();
		if(creditbeanList.size()>0){
			creditbeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select sign_up_credit,order_credit,credits_id from fapp_credits order by credits_id desc limit 1";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							AdminSettingsBean lunchDinnerBean = new AdminSettingsBean();
							lunchDinnerBean.signUpCredit = resultSet.getDouble("sign_up_credit");
							lunchDinnerBean.orderCredit = resultSet.getDouble("order_credit");
							creditbeanList.add(lunchDinnerBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return creditbeanList;
	}

	public static boolean isEBLunchValid(AdminSettingsBean  normalSpecialTimingsbean,Connection connection){
		boolean isChked = false;
		for(ManageCategoryBean bean: normalSpecialTimingsbean.earlyBirdLunchCategoryList){
			if(bean.chkCategory){
				isChked = true;
			}
		}		
		if(normalSpecialTimingsbean.ebLunchFromTimings != null){
			if(normalSpecialTimingsbean.ebLunchToTimings != null ){
				if(isChked){
					return true;
				}else{
					Messagebox.show("Category not chosen!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
					return false;
				}
			}else{
				Messagebox.show("Lunch time(TO) not given!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Lunch time(FROM) not given!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}

	public static boolean isEBDinnerValid(AdminSettingsBean  normalSpecialTimingsbean,Connection connection){
		boolean isChked = false;
		for(ManageCategoryBean bean: normalSpecialTimingsbean.earlyBirdDinnerCategoryList){
			if(bean.chkCategory){
				isChked = true;
			}
		}		
		if(normalSpecialTimingsbean.dinnerFromTimings != null){
			if(normalSpecialTimingsbean.dinnerToTimings != null ){
				if(isChked){
					return true;
				}else{
					Messagebox.show("Category not chosen!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
					return false;
				}
			}else{
				Messagebox.show("Dinner time(TO) not given!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Dinner time(FROM) not given!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}

	public static void saveEBLunch(Connection connection,AdminSettingsBean ebLunchTimingsbean) {
		boolean savedLD = false, saveLunch=false;
		int earlyBirdId = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_early_bird(early_bird_lunch_from, early_bird_lunch_to) "
							   +" VALUES (?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, ebLunchTimingsbean.ebLunchFromTimingsValue);
						preparedStatement.setString(2, ebLunchTimingsbean.ebLunchToTimingsValue);
						int update = preparedStatement.executeUpdate();
						if(update>0){
							savedLD = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
			}
		
		
			if(savedLD){
				SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select MAX(early_bird_id)AS early_bird_id from fapp_early_bird ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							earlyBirdId = resultSet.getInt("early_bird_id");
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
				}
			}
		
			if(earlyBirdId>0){
				SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_early_bird_lunch_details(early_bird_id, early_bird_category_id) "
							   +" VALUES (?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						for(ManageCategoryBean categoryBean : ebLunchTimingsbean.earlyBirdLunchCategoryList){
							if(categoryBean.chkCategory){
								preparedStatement.setInt(1, earlyBirdId);
								preparedStatement.setInt(2, categoryBean.categoryId);
								preparedStatement.addBatch();
							}
						}
						int[] countRows = preparedStatement.executeBatch();
						if(countRows.length>0){
							saveLunch = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
		if(saveLunch){
			Messagebox.show("Early bird lunch Timings saved!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
			//clearEbLunchDetails();
		}
	}
	
	public static void saveEBDinner(Connection connection,AdminSettingsBean ebLunchTimingsbean) {
		boolean savedLD = false, saveLunch=false;
		int earlyBirdId = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_early_bird(early_bird_dinner_from, early_bird_dinner_to) "
							   +" VALUES (?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, ebLunchTimingsbean.dinnerFromTimingsValue);
						preparedStatement.setString(2, ebLunchTimingsbean.dinnerToTimingsValue);
						int update = preparedStatement.executeUpdate();
						if(update>0){
							savedLD = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
			}
		
		
			if(savedLD){
				SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select MAX(early_bird_id)AS early_bird_id from fapp_early_bird where"
							+ " early_bird_lunch_from is null and early_bird_lunch_to is null";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							earlyBirdId = resultSet.getInt("early_bird_id");
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
				}
			}
		
			if(earlyBirdId>0){
				SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_early_bird_dinner_details(early_bird_id, early_bird_category_id) "
							   +" VALUES (?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						for(ManageCategoryBean categoryBean : ebLunchTimingsbean.earlyBirdDinnerCategoryList){
							if(categoryBean.chkCategory){
								preparedStatement.setInt(1, earlyBirdId);
								preparedStatement.setInt(2, categoryBean.categoryId);
								preparedStatement.addBatch();
							}
						}
						int[] countRows = preparedStatement.executeBatch();
						if(countRows.length>0){
							saveLunch = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
		if(saveLunch){
			Messagebox.show("Early bird Dinner Timings saved!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
			//clearEbLunchDetails();
		}
	}

	public static ArrayList<AdminSettingsBean> loadSavedLunchTimings(Connection connection){
		ArrayList<AdminSettingsBean> ebLunchTimingsbeanList = new ArrayList<AdminSettingsBean>();
		if(ebLunchTimingsbeanList.size()>0){
			ebLunchTimingsbeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select early_bird_lunch_from,early_bird_lunch_to,early_bird_id from fapp_early_bird "
							+ " where early_bird_dinner_from is null and early_bird_dinner_to is null ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							AdminSettingsBean lunchBean = new AdminSettingsBean();
							lunchBean.ebLunchFromTimingsValue = resultSet.getString("early_bird_lunch_from");
							lunchBean.ebLunchToTimingsValue = resultSet.getString("early_bird_lunch_to");
							lunchBean.ebLunchId = resultSet.getInt("early_bird_id");
							ebLunchTimingsbeanList.add(lunchBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ebLunchTimingsbeanList;
	}
	
	public static ArrayList<AdminSettingsBean> loadSavedDinnerTimings(Connection connection){
		ArrayList<AdminSettingsBean> ebDinnerTimingsbeanList = new ArrayList<AdminSettingsBean>();
		if(ebDinnerTimingsbeanList.size()>0){
			ebDinnerTimingsbeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select early_bird_dinner_from,early_bird_dinner_to,early_bird_id from fapp_early_bird"
							+ " where early_bird_lunch_from is null and early_bird_lunch_to is null ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							AdminSettingsBean lunchBean = new AdminSettingsBean();
							lunchBean.specialItemFromSettingsValue = resultSet.getString("early_bird_dinner_from");
							lunchBean.specialItemToSettingsValue = resultSet.getString("early_bird_dinner_to");
							lunchBean.ebLunchId = resultSet.getInt("early_bird_id");
							ebDinnerTimingsbeanList.add(lunchBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ebDinnerTimingsbeanList;
	}
	
	public static void saveCredit(Connection connection,AdminSettingsBean creditbean) {
		boolean savedLD = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_credits(sign_up_credit, order_credit) "
							   +" VALUES (?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setDouble(1, creditbean.signUpCredit);
						preparedStatement.setDouble(2, creditbean.orderCredit);
						
						int update = preparedStatement.executeUpdate();
						if(update>0){
							savedLD = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(savedLD){
			Messagebox.show("Credits saved!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
}
