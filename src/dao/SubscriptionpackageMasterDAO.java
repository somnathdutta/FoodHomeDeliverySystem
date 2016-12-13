package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.zkoss.zhtml.Messagebox;

import sql.SubcriptionItemsMasterSql;
import Bean.SubscriptionpackageMasterBean;

public class SubscriptionpackageMasterDAO {

	
	public static ArrayList<SubscriptionpackageMasterBean> loadpackagemaster(Connection connection){
		ArrayList<SubscriptionpackageMasterBean> list = new ArrayList<SubscriptionpackageMasterBean>();
		if(list.size()>0){
			list.clear();
		}
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(SubcriptionItemsMasterSql.loadPackageMasterQry);
			System.out.println("QRY " + preparedStatement);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				SubscriptionpackageMasterBean bean = new SubscriptionpackageMasterBean();
				bean.setPackageId(resultSet.getInt("package_master_id"));
				bean.setPackageName(resultSet.getString("package_name"));
				bean.setNoOfDays(resultSet.getInt("no_of_days"));
				bean.setButtonName(resultSet.getString("button_name"));
				
				list.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "ERROR", Messagebox.OK, Messagebox.ERROR);
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
	
	public static int insertPackageMaster(Connection connection, String un, SubscriptionpackageMasterBean bean){
		int i =0;
		int packageId = 0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(SubcriptionItemsMasterSql.insertPackageMasterQry);
			preparedStatement.setString(1, bean.getPackageName().trim());
			preparedStatement.setInt(2, bean.getNoOfDays());
			preparedStatement.setString(3, bean.getButtonName().trim());
			preparedStatement.setString(4, un);
			preparedStatement.setString(5, un);
			
			i = preparedStatement.executeUpdate();
			if(i>0){
				String sql = "select max(package_master_id) as id from fapp_subs_package_master";
				PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement2.executeQuery();
				while (resultSet.next()) {
					packageId = resultSet.getInt("id");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().startsWith("ERROR: duplicate key")){
				Messagebox.show(bean.getPackageName() + " Already Exists", "ERROR", Messagebox.OK, Messagebox.ERROR);
				bean.setPackageName(null);
				bean.setNoOfDays(null);
				bean.setButtonName(null);
				
			}else {
				Messagebox.show(e.getMessage(), "ERROR", Messagebox.OK, Messagebox.ERROR);
			}
		}finally{
			if(preparedStatement != null){
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return packageId;
	}
	
	
	public static int updatePackageMaster(Connection connection, String un, SubscriptionpackageMasterBean bean){
		int i =0;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(SubcriptionItemsMasterSql.updatePackageMasterQry);
			preparedStatement.setString(1, bean.getPackageName());
			preparedStatement.setInt(2, bean.getNoOfDays());
			preparedStatement.setString(3, bean.getButtonName());
			preparedStatement.setString(4, un);
			preparedStatement.setInt(5, bean.getPackageId());
			
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "ERROR", Messagebox.OK, Messagebox.ERROR);
		}
		return i;
	}
	
	
	public static ArrayList<SubscriptionpackageMasterBean> loadMealType(Connection connection){
		ArrayList<SubscriptionpackageMasterBean> list = new ArrayList<SubscriptionpackageMasterBean>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		
		try {
			preparedStatement = connection.prepareStatement(SubcriptionItemsMasterSql.loadMealTypeQry);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				SubscriptionpackageMasterBean bean = new SubscriptionpackageMasterBean();
				bean.setMealTypeId(resultSet.getInt("meal_type_master_id"));
				bean.setMealType(resultSet.getString("meal_type"));
				
				list.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
}
