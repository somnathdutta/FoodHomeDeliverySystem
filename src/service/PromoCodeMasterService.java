package service;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.PromoCodeMasterBean;
import Bean.PromoCodeTypeBean;
import dao.PromoCodeMasterDao;

public class PromoCodeMasterService {

	public static int insertPromocode(Connection connection,PromoCodeMasterBean bean, String promoCode){
		int i = 0;
		i = PromoCodeMasterDao.inSertPromocode(connection, bean, promoCode);
		return i;
	}
	
	public static ArrayList<PromoCodeMasterBean> loadPromoCodeType(Connection connection){
		ArrayList<PromoCodeMasterBean> list = new ArrayList<PromoCodeMasterBean>();
		list = PromoCodeMasterDao.loadPromoCodeType(connection);
		return list;
	}
	
	public static ArrayList<PromoCodeMasterBean> loadPromoCodeApplicationType(Connection connection){
		ArrayList<PromoCodeMasterBean> list = new ArrayList<PromoCodeMasterBean>();
		list = PromoCodeMasterDao.loadPromoCodeApplicationType(connection);
		return list;
	}
	
	public static ArrayList<PromoCodeMasterBean> loadPromoCodeDetails(Connection connection){
		ArrayList<PromoCodeMasterBean> list = new ArrayList<PromoCodeMasterBean>();
		list = PromoCodeMasterDao.loadPromoCodeDetails(connection);
		return list;
	}
	
	public static int insertPromoCodeDetails(Connection connection, PromoCodeMasterBean bean, PromoCodeMasterBean typeBean, PromoCodeMasterBean applbean){
		int i = 0;
		i = PromoCodeMasterDao.inSertPromocodeDetails(connection, bean, typeBean, applbean);
		return i;
	}
	
	public static ArrayList<PromoCodeMasterBean> loadPromoCodeList(Connection connection){
		ArrayList<PromoCodeMasterBean> list = new ArrayList<PromoCodeMasterBean>();
		list = PromoCodeMasterDao.loadPromoCodeList(connection);
		return list;
	}
	
	public static ArrayList<PromoCodeTypeBean> loadTypeList(Connection connection){
		ArrayList<PromoCodeTypeBean> list = new ArrayList<PromoCodeTypeBean>();
		list = PromoCodeMasterDao.loadPromoType(connection);
		return list;
		
	}
	
	public static ArrayList<PromoCodeTypeBean> loadApplyTypeList(Connection connection){
		ArrayList<PromoCodeTypeBean> list = new ArrayList<PromoCodeTypeBean>();
		list = PromoCodeMasterDao.loadPromoApplyList(connection);
		return list;
		
	}
	
	
	
	
	
	
	public static boolean isValidate(PromoCodeMasterBean promoCodeMasterBean, PromoCodeMasterBean typeBean, PromoCodeMasterBean applyTypeBean){
		
		if(promoCodeMasterBean.getFromDateUtil() != null){
			if(promoCodeMasterBean.getToDateUtil() != null){
				if(typeBean.getPromoTypeId() !=null){
					if(promoCodeMasterBean.getPromoValue() !=null){
						if(applyTypeBean.getPromocodeApplyTypeId() != null){
							if(promoCodeMasterBean.getStatus() !=null){
								return true;
							}else {
								Messagebox.show("Select Status", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
								return false;
							}
						}else {
							Messagebox.show("Select Promo code Application Type", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
							return false;
						}
						
					}else {
						Messagebox.show("Enter Promo Value", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
						return false;
					}
				}else {
					Messagebox.show("Select Promo Type", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
					return false;
				}
			}else {
				
				Messagebox.show("Select To Date", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				return false;
			}
		}else {
			Messagebox.show("Select From Date", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
			return false;
		}
		
	}
	
}
