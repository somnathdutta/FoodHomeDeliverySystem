package service;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import dao.CmsMasterDao;
import Bean.ShareAndEarnBean;

public class CmsMasterService {

	public static boolean shareAndEarnValidation(ShareAndEarnBean bean){
		
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
		i = CmsMasterDao.insertShareAndEarnDetails(connection, bean, user);
		return i;
	}
	
	public static int deleteShareAndEarnDetails(Connection connection, ShareAndEarnBean bean, String user){
		int i = 0;
		i = CmsMasterDao.insertShareAndEarnDetails(connection, bean, user);
		return i;
	}
	
	
}
