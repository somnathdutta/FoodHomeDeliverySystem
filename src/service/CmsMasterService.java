package service;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import dao.CmsMasterDao;
import Bean.AboutUsBean;
import Bean.ContactUsBean;
import Bean.PrivacyPolicyBean;
import Bean.ShareAndEarnBean;
import Bean.TermsAndConditionBean;

public class CmsMasterService {

	public static boolean shareAndEarnValidation(ShareAndEarnBean bean){
		
		//share and earn
		
		if(bean.getAppMsg() !=null){
			if(bean.getInvtMsg() != null){
				if(bean.getAmnt() != null){
					if(bean.getImgUrl() !=null){
						return true;
					}else {
						Messagebox.show("Enter Image Url!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
						return false;
					}
					
				}else {
					Messagebox.show("Enter Amount!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
					return false;
				}
				
				}else {
				Messagebox.show("Enter Invite Message!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				return false;
			}
		 }else {
			Messagebox.show("Enter App Message", "Alert!", Messagebox.OK, Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public static int insertShareAndEarnDetails(Connection connection, ShareAndEarnBean bean, String user){
		int i = 0;
		i = CmsMasterDao.insertShareAndEarnDetails(connection, bean, user);
		return i;
	}
	
	public static ArrayList<ShareAndEarnBean> loadShareAndEarndetails(Connection connection){
		ArrayList<ShareAndEarnBean> list = new ArrayList<ShareAndEarnBean>();
		list = CmsMasterDao.loadShareAndEarndetails(connection);
		return list;
		
	}
	
	public static int updateShareAndEarnDetails(Connection connection, ShareAndEarnBean bean, String user){
		int i = 0;
		i = CmsMasterDao.upDateShareAndEarnDetails(connection, bean, user);
		return i;
	}
	
	public static int deleteShareAndEarnDetails(Connection connection, ShareAndEarnBean bean, String user){
		int i = 0;
		i = CmsMasterDao.deleteShareAndEarnDetails(connection, bean, user);
		return i;
	}
	
	//contact us
	
	public static int insertContactUs(Connection connection, ContactUsBean bean, String user){
		int i = 0;
		i = CmsMasterDao.insertContactUs(connection, bean, user);
		return i;
	}
	
	public static ArrayList<ContactUsBean> loadContactUs(Connection connection){
		ArrayList<ContactUsBean> list = new ArrayList<ContactUsBean>();
		list = CmsMasterDao.loadContactUs(connection);
		return list;
	}
	
	public static int updateContactUs(Connection connection, ContactUsBean bean, String user){
		int i = 0;
		i = CmsMasterDao.updateContactUs(connection, bean, user);
		return i;
	}
	
	public static int deleteContactUs(Connection connection, ContactUsBean bean, String user){
		int i = 0;
		i = CmsMasterDao.deleteContactUs(connection, bean, user);
		return i;
	}
	
	//about us
	public static int insertAboutUs(Connection connection, AboutUsBean bean, String user){
		int i = 0;
		i = CmsMasterDao.insertAboutUs(connection, bean, user);
		return i;
	}
	
	public static ArrayList<AboutUsBean> loadAboutUs(Connection connection){
		ArrayList<AboutUsBean> list = new ArrayList<AboutUsBean>();
		list = CmsMasterDao.loadAboutUs(connection);
		return list;
	}
	
	public static int updateAboutUs(Connection connection, AboutUsBean bean, String user){
		int i = 0;
		i = CmsMasterDao.updateAboutUs(connection, bean, user);
		return i;
	}
	
	public static int deleteAboutUs(Connection connection, AboutUsBean bean, String user){
		int i = 0;
		i = CmsMasterDao.deleteAboutUs(connection, bean, user);
		return i;
	}
	
	//terms and condition
	public static int insertTaC(Connection connection, TermsAndConditionBean bean, String user){
		int i = 0;
		i = CmsMasterDao.insertTaC(connection, bean, user);
		return i;
	}
	
	public static ArrayList<TermsAndConditionBean> loadTaC(Connection connection){
		ArrayList<TermsAndConditionBean> list = new ArrayList<TermsAndConditionBean>();
		list = CmsMasterDao.loadTaC(connection);
		return list;
	}
	
	public static int updateTaC(Connection connection, TermsAndConditionBean bean, String user){
		int i = 0;
		i = CmsMasterDao.updateTaC(connection, bean, user);
		return i;
	}
	
	public static int deleteTaC(Connection connection, TermsAndConditionBean bean, String user){
		int i = 0;
		i = CmsMasterDao.deleteTaC(connection, bean, user);
		return i;
	}
	
	//privacy policy
		
	public static int insertPrivacyPolicy(Connection connection, PrivacyPolicyBean bean, String user){
		int i = 0;
		i = CmsMasterDao.insertPrivacyPolicy(connection, bean, user);
		return i;
	}
		
	public static ArrayList<PrivacyPolicyBean> loadprivacyPolicy(Connection connection){
		ArrayList<PrivacyPolicyBean> list = new ArrayList<PrivacyPolicyBean>();
		list = CmsMasterDao.loadPrivacyPolicy(connection);
		return list;
	}
		
	public static int updatePrivacyPolicy(Connection connection, PrivacyPolicyBean bean, String user){
		int i = 0;
		i = CmsMasterDao.updatePrivacyPolicy(connection, bean, user);
		return i;
	}

	public static int deletePrivacyPolicy(Connection connection, PrivacyPolicyBean bean, String user){
		int i = 0;
		i = CmsMasterDao.deletePrivacyPolicy(connection, bean, user);
		return i;
	}
		
	
}
