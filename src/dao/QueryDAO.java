package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.Query;

public class QueryDAO {

	public static void addNewQuery(Connection connection, Query queryPojo){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_query_type_master( "
							+" query_type, is_active,created_by, updated_by) "
							+" VALUES (?, ?, ?, ?);";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, queryPojo.getQueryName());
						String status = queryPojo.getStatus();
						if(status.equalsIgnoreCase("Active")){
							preparedStatement.setString(2, "Y");
						}else{
							preparedStatement.setString(2, "N");
						}
						
						preparedStatement.setString(3, queryPojo.getUserName());
						preparedStatement.setString(4, queryPojo.getUserName());
						int count = preparedStatement.executeUpdate();
						if(count>0){
							Messagebox.show("Query saved successfully!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
						}
								
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static  ArrayList<Query> showAllMasterQueryTypes(Connection connection){
		ArrayList<Query> queryTypeList = new ArrayList<Query>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT * FROM fapp_query_type_master where is_delete='N'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							Query query = new Query();
							query.setQueryID(resultSet.getInt("query_id"));
							query.setQueryName(resultSet.getString("query_type"));
							String status = resultSet.getString("is_active");
							if(status.equals("Y")){
								query.setStatus("Active");
							}else{
								query.setStatus("Inactive");
							}
							queryTypeList.add(query);
						}
						
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return queryTypeList;
	}
	
	public static  void updateQuery(Connection connection, Query queryPojo){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_query_type_master SET query_type=?, is_active=?,updated_by=? "
							+" WHERE query_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, queryPojo.getQueryName());
						String status = queryPojo.getStatus();
						if(status.equalsIgnoreCase("Active")){
							preparedStatement.setString(2, "Y");
						}else{
							preparedStatement.setString(2, "N");
						}
						
						preparedStatement.setString(3, queryPojo.getUserName());
						preparedStatement.setInt(4, queryPojo.getQueryID());
						int count = preparedStatement.executeUpdate();
						if(count>0){
							Messagebox.show("Query updated successfully!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
						}
								
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void deleteQuery(Connection connection, Query queryPojo){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_query_type_master SET is_delete='Y',updated_by=? "
							+" WHERE query_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						
						preparedStatement.setString(1, queryPojo.getUserName());
						preparedStatement.setInt(2, queryPojo.getQueryID());
						int count = preparedStatement.executeUpdate();
						if(count>0){
							Messagebox.show("Query deleted successfully!","Successful Information",Messagebox.OK,Messagebox.INFORMATION);
						}
								
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static boolean isValid(Query queryPojo){
		if(queryPojo.getQueryName()!=null){
			if(queryPojo.getStatus()!=null){
				return true;
			}else{
				Messagebox.show("Status required!","Alert Information",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Query Type required!","Alert Information",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
}
