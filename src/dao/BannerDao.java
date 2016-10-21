package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import sql.BannerSql;
import Bean.BannerBean;
import Bean.UrlBean;

public class BannerDao {
	
	public static int insertBanner(Connection connection, BannerBean bean, ArrayList<UrlBean> urlList){
		int i = 0;
		int generatedKey = 0;
		int insertedUrlNo = 0;
		try {
			connection.setAutoCommit(false);
			PreparedStatement preparedStatement = null;
			
			SQL:{
				preparedStatement = connection.prepareStatement(BannerSql.insertBannerIdQuery);
				preparedStatement.setString(1, bean.bannertTitle);
				
			    i = preparedStatement.executeUpdate();
			    
			}
			
			SQL:{
				if(i>0){
				 preparedStatement = connection.prepareStatement(BannerSql.selectMaxBannerIdQuery);
				 ResultSet resultSet = preparedStatement.executeQuery();
				 while (resultSet.next()) {
					generatedKey = resultSet.getInt("m_id");
				}
				}
			}
			SQL:{
				
					for(UrlBean ub : urlList){
						preparedStatement = connection.prepareStatement(BannerSql.insertBannerListQuery);
						preparedStatement.setInt(1, generatedKey);
						preparedStatement.setString(2, ub.urlName);
						
						insertedUrlNo = preparedStatement.executeUpdate();
					}
				
			}
			connection.commit();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg,"Error", Messagebox.OK, Messagebox.ERROR);
			
			try {
				connection.rollback();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			
			
		}
		return insertedUrlNo;
		
	}
	
	
	
	
}