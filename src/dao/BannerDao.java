package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.zkoss.zul.Messagebox;

import sql.BannerSql;
import utility.FappPstm;
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
				preparedStatement.setString(1, bean.bannertTitle.trim());
				
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
			if(msg.startsWith("ERROR: duplicate")){
				Messagebox.show("Already Exists","Error", Messagebox.OK, Messagebox.ERROR);
			}else {
				Messagebox.show(msg,"Error", Messagebox.OK, Messagebox.ERROR);
			}
			
			
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
	
	public static ArrayList<UrlBean> loadBannerList(Connection connection){
		ArrayList<UrlBean> list = new ArrayList<UrlBean>();
		if(list.size()>0){
			list.clear();
		}
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, BannerSql.loadBannerNameQuery, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				UrlBean bean = new UrlBean();
				bean.bannerName = resultSet.getString("banner_title");
				bean.bannerId = resultSet.getInt("banner_id");
				
				list.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return list;
		
	}
	
	
	public static ArrayList<UrlBean> loadUrlList(Connection connection, int bannerId){
		ArrayList<UrlBean> urlBeanList = new ArrayList<UrlBean>();
		if(urlBeanList.size()>0){
			urlBeanList.clear();
		}
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, BannerSql.loadBannerUrlListQuery, Arrays.asList(bannerId));
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				UrlBean bean = new UrlBean();
				bean.urlId = resultSet.getInt("banner_details_id");
				bean.urlName = resultSet.getString("banner_image");
				bean.bannerName = resultSet.getString("banner_title");
				bean.bannerId = resultSet.getInt("banner_id");
				String status = resultSet.getString("is_active");
				if(status.equalsIgnoreCase("Y")){
					bean.activeStatus = "Active";
				}else {
					bean.activeStatus = "Inactive";
				}
				
				
				urlBeanList.add(bean);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return urlBeanList;
	}
	
	public static int bannerEdit(Connection connection, int bannerDetailsId){
		int i =0;
		PreparedStatement preparedStatement;
		try {
			preparedStatement = FappPstm.createQuery(connection, BannerSql.updateBannerUrl, Arrays.asList(bannerDetailsId));
			i = preparedStatement.executeUpdate();
			
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg,"Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static int inActivebanner(Connection connection, int bannerDetailsId, String status){
		int i =0;
		PreparedStatement preparedStatement;
		try {
			preparedStatement = FappPstm.createQuery(connection, BannerSql.inActiveBannerUrl, Arrays.asList(status, bannerDetailsId));
			i = preparedStatement.executeUpdate();
			
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg,"Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static boolean saveAll(Connection connection, int bannerId, ArrayList<UrlBean> list){
		boolean status = false;
		int i = 0, j = 0;
		
		try {
			
			for(UrlBean bean : list){
				
				if(bean.urlId !=null && bean.urlName != null){
					PreparedStatement preparedStatement = null;
					preparedStatement = FappPstm.createQuery(connection, BannerSql.updateBannerUrl, Arrays.asList(bean.urlName, bannerId));
					
					i = preparedStatement.executeUpdate();
					
				}if(bean.urlId ==null && bean.urlName != null) {
					PreparedStatement preparedStatement = null;
					preparedStatement = FappPstm.createQuery(connection, BannerSql.insertBannerListQuery, Arrays.asList(bannerId, bean.urlName));
					
					j =preparedStatement.executeUpdate();
				}
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg,"Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		if(i> 0 || j> 0){
			status = true;
		}
		return status;
	}
	
	
	
}