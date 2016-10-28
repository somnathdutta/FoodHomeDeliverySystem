package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.shape.Cylinder;
import jdk.nashorn.internal.ir.SetSplitState;

import org.zkoss.zul.Messagebox;

import com.itextpdf.text.Phrase;

import service.SetMasterService;
import sql.SetMasterSql;
import utility.FappPstm;
import Bean.ItemBean;
import Bean.ManageCategoryBean;
import Bean.SetBean;

public class SetDAO {

	/**
	 * Load all categories
	 */
	public static ArrayList<ManageCategoryBean> onLoadCategoryList(Connection connection){
		ArrayList<ManageCategoryBean> categoryBeanList = new ArrayList<ManageCategoryBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					String sql = "SELECT distinct category_name,category_id FROM vw_food_item_details order by category_id  ";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean categoryBean = new ManageCategoryBean();
							categoryBean.categoryId = resultSet.getInt("category_id");
							categoryBean.categoryName = resultSet.getString("category_name");
							categoryBeanList.add(categoryBean);
						}
						
					}catch (Exception e) {
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
		return categoryBeanList;
	}
	
	public static ArrayList<ItemBean> loadItemsFromCategory(Connection  connection, int categoryId){
		ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		try {
			SQL:{
			 PreparedStatement preparedStatement = null;
			 ResultSet resultSet = null;
			 String sql = "select item_id,item_code,item_name,item_description from vw_food_item_details where category_id = ?";
			 try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, categoryId);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ItemBean item = new ItemBean();
					item.itemId = resultSet.getInt("item_id");
					item.itemCode = resultSet.getString("item_code");
					item.itemName = resultSet.getString("item_name");
					item.itemDescription = resultSet.getString("item_description");
					itemBeanList.add(item);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Messagebox.show("ERROR Due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return itemBeanList;
	}
	
	
	public static ArrayList<SetBean> fetchSetList(Connection connection){
		ArrayList<SetBean> list = new ArrayList<SetBean>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, SetMasterSql.selectSetListQuery, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				SetBean bean = new SetBean();
				
				bean.setSetId(resultSet.getInt("set_id"));
				bean.setSetName(resultSet.getString("set_name"));
				
				list.add(bean);
				
			}
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}finally{
			if(preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		return list;
		
	}
	
	
	public static ArrayList<SetBean> fetchExistingSetItems(Connection connection, Integer setId){
		ArrayList<SetBean> list = new ArrayList<SetBean>();
		if(list.size()>0){
			list.clear();
		}
		String setName = "%%";
		PreparedStatement preparedStatement = null;
		try {
			
			preparedStatement = FappPstm.createQuery(connection, SetMasterSql.selctExsistingSetQuery,Arrays.asList(setId));
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				SetBean bean = new SetBean();
				
				bean.setSetId(resultSet.getInt("set_id"));
				bean.setSetName(resultSet.getString("set_name"));
				if(bean.getSetName().equals(setName)){
					bean.setSetNameVis(false);
				}else {
					bean.setSetNameVis(true);
				}
				
				setName = resultSet.getString("set_name");
				bean.getItemBean().setItemId(resultSet.getInt("item_id"));
				bean.getItemBean().setItemCode(resultSet.getString("item_code"));
				bean.getItemBean().setItemName(resultSet.getString("item_name"));
				bean.getItemBean().setItemDescription(resultSet.getString("item_description"));
				String status = resultSet.getString("item_active");
				if(status.equals("Y")){
					bean.getItemBean().setStatus("Active");
				}else {
					bean.getItemBean().setStatus("Inactive");
				}
				bean.setSetItemId(resultSet.getInt("set_item_id"));
				list.add(bean);
				
			}
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}finally{
			if(preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}
	
	public static ArrayList<SetBean> fetchDayTypeList(Connection connection){
	  ArrayList<SetBean> list = new ArrayList<SetBean>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, SetMasterSql.SelectDayTypeQuery, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				SetBean bean = new SetBean();
				
				bean.setDayTypeId(resultSet.getInt("order_type_id"));
				bean.setDayType(resultSet.getString("order_type"));
				
				list.add(bean);
				
			}
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}finally{
			if(preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		return list;
		
	}
	
	public static int applyDayType(Connection connection, Integer dayTypeId, ArrayList<SetBean> itemCodeList, int setId){
	  int insertCount = 0;
	  int count = 0;
	  String sql = null;
		PreparedStatement preparedStatement = null;
		if(dayTypeId == 1){
			sql = SetMasterSql.updateTodayStatusQuery;
		}
		if(dayTypeId == 2){
			sql = SetMasterSql.updateTomorrowStatusQuery;
		}
		try {
			connection.setAutoCommit(false);
		try {
			
			for(SetBean bean : itemCodeList){
				
				preparedStatement = FappPstm.createQuery(connection, sql, Arrays.asList(bean.getItemBean().itemCode));
				insertCount = preparedStatement.executeUpdate();
			}
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}finally{
			if(preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		if(insertCount> 0){
			PreparedStatement preparedStatement2 = null;
			try {
				preparedStatement2 = FappPstm.createQuery(connection, SetMasterSql.upDateSetMasterQuery, Arrays.asList(dayTypeId, setId));
				count = preparedStatement2.executeUpdate();
			
			} catch (Exception e) {
				String msg = e.getMessage();
				Messagebox.show(msg, "Error", Messagebox.OK,Messagebox.ERROR);
				e.printStackTrace();
			}finally{
				if(preparedStatement2 != null){
					try {
						preparedStatement2.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		connection.commit();
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		return count;
	}
	
	public static int statusUpdate(Connection connection,String status,  int setItemId){
		int i = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, SetMasterSql.updateSetItemStatus, Arrays.asList(status, setItemId));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
			e.printStackTrace();
		}
		return i;
	}
	
	public static int todayTomorrowStatusUpdate(Connection connection,String status, String itemCode){
		int i = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, SetMasterSql.updateKitchenItemSetStatus, Arrays.asList(status,status,itemCode));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
			e.printStackTrace();
		}
		return i;
	}
	
	
	
	
	public static ArrayList<String> fetchExistingSetItemCode(Connection connection, Integer setId){
		ArrayList<String> codeList = new ArrayList<String>();
		if(codeList.size()>0){
			codeList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = FappPstm.createQuery(connection, SetMasterSql.fetchExistingSetItemQuery, Arrays.asList(setId));
				ResultSet resultSet = preparedStatement.executeQuery();
				
				while (resultSet.next()) {
					codeList.add(resultSet.getString("item_code"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return codeList;
		
	}
	
	public static ArrayList<ItemBean> loadItemsFromCategoryUpdate(Connection  connection, int categoryId, String codeString){
		ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		if(itemBeanList.size()>0){
			itemBeanList.clear();
		}
		System.out.println("Codes - " + codeString);
		try {
			SQL:{
			 PreparedStatement preparedStatement = null;
			 ResultSet resultSet = null;
			 String sql = "select item_id,item_code,item_name,item_description "
			 		+ " from vw_food_item_details where category_id = ? and item_code not IN "+codeString;
			 
			 try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, categoryId);
				
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ItemBean item = new ItemBean();
					item.itemId = resultSet.getInt("item_id");
					item.itemCode = resultSet.getString("item_code");
					item.itemName = resultSet.getString("item_name");
					item.itemDescription = resultSet.getString("item_description");
					itemBeanList.add(item);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Messagebox.show("ERROR Due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return itemBeanList;
	}
	
}
