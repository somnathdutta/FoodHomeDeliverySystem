package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import org.zkoss.zul.Messagebox;

import sql.CmsMasterSql;
import utility.FappPstm;
import Bean.ShareAndEarnBean;

public class CmsMasterDao {

	public static int insertShareAndEarnDetails(Connection connection, ShareAndEarnBean bean, String user){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.inSertShareAndEarnDetailsSql, 
													 Arrays.asList(bean.getAppMsg(), bean.getInvtMsg(), bean.getAmnt(), user, user, bean.getImgUrl()));
			i = preparedStatement.executeUpdate();
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}

	public static ArrayList<ShareAndEarnBean> loadShareAndEarndetails(Connection connection){
		ArrayList<ShareAndEarnBean> list = new ArrayList<ShareAndEarnBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.loadShareAndEarnDetailsSql, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				ShareAndEarnBean bean = new ShareAndEarnBean();
				bean.setShareAndEarnId(resultSet.getInt("fapp_share_and_earn_id"));
				bean.setAppMsg(resultSet.getString("app_msg"));
				bean.setInvtMsg(resultSet.getString("inviting_text"));
				bean.setAmnt(resultSet.getDouble("amount"));
				bean.setImgUrl(resultSet.getString("img_url"));
				
				list.add(bean);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return list;
	}
	
	public static int upDateShareAndEarnDetails(Connection connection, ShareAndEarnBean bean, String user){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.updateShareAndEarnDetailsSql, 
													 Arrays.asList(bean.getAppMsg(), bean.getInvtMsg(), bean.getAmnt(), user,bean.getImgUrl(), bean.getShareAndEarnId()));
			i = preparedStatement.executeUpdate();
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static int deleteShareAndEarnDetails(Connection connection, ShareAndEarnBean bean, String user){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.deleteShareAndEarnDetailsSql, 
													 Arrays.asList(user, bean.getShareAndEarnId()));
			i = preparedStatement.executeUpdate();
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	
	
	
}


