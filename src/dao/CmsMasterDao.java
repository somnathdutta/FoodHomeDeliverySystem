package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import org.zkoss.zul.Messagebox;

import sql.CmsMasterSql;
import utility.FappPstm;
import Bean.AboutUsBean;
import Bean.ContactUsBean;
import Bean.PrivacyPolicyBean;
import Bean.ShareAndEarnBean;
import Bean.TermsAndConditionBean;

public class CmsMasterDao {

	
	//share and earn
	
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
	
	
	//contact us
	
	public static int insertContactUs(Connection connection, ContactUsBean bean, String user){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.insertContactUsSql, Arrays.asList(bean.getContNo(), bean.getContactMessage() ,user, user));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static ArrayList<ContactUsBean> loadContactUs(Connection connection){
		ArrayList<ContactUsBean> list = new ArrayList<ContactUsBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.loadContactUsSql, null);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ContactUsBean bean = new ContactUsBean();
					bean.setContactusId(resultSet.getInt("fapp_contact_us_id"));
					bean.setContNo(resultSet.getString("contct_no"));
					bean.setContactMessage(resultSet.getString("contact_message"));
					list.add(bean);
					
				}
				
			} catch (Exception e) {
				String msg = e.getMessage();
				Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	public static int updateContactUs(Connection connection, ContactUsBean bean, String user){

		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.updateContactUsSql, Arrays.asList(bean.getContNo(), bean.getContactMessage() ,user, bean.getContactusId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	
	}
	
	public static int deleteContactUs(Connection connection, ContactUsBean bean, String user){

		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.deleteContactUsSql, Arrays.asList(user, bean.getContactusId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	
	}
	
	//about us
	
	public static int insertAboutUs(Connection connection, AboutUsBean bean, String user){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.insertAboutUsSql, Arrays.asList(bean.getAboutUs(), user, user));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static ArrayList<AboutUsBean> loadAboutUs(Connection connection){
		ArrayList<AboutUsBean> list = new ArrayList<AboutUsBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.loadAboutUsSql, null);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					AboutUsBean bean = new AboutUsBean();
					bean.setAboutUsId(resultSet.getInt("fapp_about_us_id"));
					bean.setAboutUs(resultSet.getString("about_us"));
					
					list.add(bean);
				}
				
			} catch (Exception e) {
				String msg = e.getMessage();
				Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	public static int updateAboutUs(Connection connection, AboutUsBean bean, String user){

		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.updateAboutUsSql, Arrays.asList(bean.getAboutUs(), user, bean.getAboutUsId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static int deleteAboutUs(Connection connection, AboutUsBean bean, String user){

		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.deleteAboutUsSql, Arrays.asList(user, bean.getAboutUsId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	
	}
	
	//terms and condition
	
	public static int insertTaC(Connection connection, TermsAndConditionBean bean, String user){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.inSertTermsAndC, Arrays.asList(bean.getTermsandCondition(), user, user));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static ArrayList<TermsAndConditionBean> loadTaC(Connection connection){
		ArrayList<TermsAndConditionBean> list = new ArrayList<TermsAndConditionBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.loadTermsAndCSql, null);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					TermsAndConditionBean bean = new TermsAndConditionBean();
					bean.setTermsandconditionId(resultSet.getInt("fapp_terms_and_condition_id"));
					bean.setTermsandCondition(resultSet.getString("terms_and_condition"));
					
					list.add(bean);
				}
				
			} catch (Exception e) {
				String msg = e.getMessage();
				Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	public static int updateTaC(Connection connection, TermsAndConditionBean bean, String user){

		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.updateTermsAndCSql, Arrays.asList(bean.getTermsandCondition(), user, bean.getTermsandconditionId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static int deleteTaC(Connection connection, TermsAndConditionBean bean, String user){

		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.deleteTermsAndCSql, Arrays.asList(user, bean.getTermsandconditionId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	
	}
	
	//privacy policy
	
	public static int insertPrivacyPolicy(Connection connection, PrivacyPolicyBean bean, String user){
		int i = 0;
			try {
				PreparedStatement preparedStatement = null;
				preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.insertPrivacyPolicySql, Arrays.asList(bean.getPrivacyPolicy(), user, user));
				i = preparedStatement.executeUpdate();
				
			} catch (Exception e) {
				String msg = e.getMessage();
				Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
				e.printStackTrace();
			}
			return i;
		}
	
	public static ArrayList<PrivacyPolicyBean> loadPrivacyPolicy(Connection connection){
		ArrayList<PrivacyPolicyBean> list = new ArrayList<PrivacyPolicyBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.loadPrivacyPolicySql, null);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					PrivacyPolicyBean bean = new PrivacyPolicyBean();
					bean.setPrivacyPolicyId(resultSet.getInt("fapp_privacy_policy_id"));
					bean.setPrivacyPolicy(resultSet.getString("privacy_policy"));
					
					list.add(bean);
				}
				
			} catch (Exception e) {
				String msg = e.getMessage();
				Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	
	public static int updatePrivacyPolicy(Connection connection, PrivacyPolicyBean bean, String user){

		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.updatePrivacyPolicySql, Arrays.asList(bean.getPrivacyPolicy(), user, bean.getPrivacyPolicyId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static int deletePrivacyPolicy(Connection connection, PrivacyPolicyBean bean, String user){

		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.deletePrivacyPolicySql, Arrays.asList(user, bean.getPrivacyPolicyId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	
	}
	
	// order contact us
	public static int insertOrderContactUs(Connection connection, ContactUsBean bean, String user){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.insertOrderContactUsSql, Arrays.asList(bean.getContNo(), bean.getContactMessage() ,user, user));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static ArrayList<ContactUsBean> loadOrderContactUs(Connection connection){
		ArrayList<ContactUsBean> list = new ArrayList<ContactUsBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.loadOrderContactUsSql, null);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ContactUsBean bean = new ContactUsBean();
					bean.setContactusId(resultSet.getInt("fapp_order_contact_us_id"));
					bean.setContNo(resultSet.getString("contact_no"));
					bean.setContactMessage(resultSet.getString("order_contact_message"));
					list.add(bean);
					
				}
				
			} catch (Exception e) {
				String msg = e.getMessage();
				Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public static int updateOrderContactUs(Connection connection, ContactUsBean bean, String user){

		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.updateOrderContactUsSql, Arrays.asList(bean.getContNo(), bean.getContactMessage() ,user, bean.getContactusId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	
	}
	
	public static int deleteOrderContactUs(Connection connection, ContactUsBean bean, String user){

		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, CmsMasterSql.deleteOrderContactUsSql, Arrays.asList(user, bean.getContactusId()));
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	
	}
	
	
	
	
}


