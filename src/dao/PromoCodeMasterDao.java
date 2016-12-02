package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import org.zkoss.zul.Messagebox;

import Bean.PromoCodeMasterBean;
import Bean.PromoCodeTypeBean;
import sql.PromoCodeMasterSql;
import utility.DateFormatter;
import utility.FappPstm;

public class PromoCodeMasterDao {

	public static int inSertPromocode(Connection connection, PromoCodeMasterBean bean, String promoCode){
		int i = 0;
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PromoCodeMasterSql.inSertPromoCodeSql, Arrays.asList(promoCode, bean.getUser(), bean.getUser()));
			System.out.println("Prepared state " + preparedStatement);
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			if(msg.startsWith("ERROR: duplicate")){
				Messagebox.show("Promo code Already Exists", "Error", Messagebox.OK, Messagebox.ERROR);
			}else {
				Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			}
			e.printStackTrace();
		}
		return i;
	}
	
	public static ArrayList<PromoCodeMasterBean> loadPromoCodeType(Connection connection){
		ArrayList<PromoCodeMasterBean> list = new ArrayList<PromoCodeMasterBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PromoCodeMasterSql.loadPromocodeTypeSql, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				PromoCodeMasterBean bean = new PromoCodeMasterBean();
				bean.setPromoTypeId(resultSet.getInt("fapp_promo_code_type_master_id"));
				bean.setPromoType(resultSet.getString("promo_code_type"));
				
				list.add(bean);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
		}
		return list;
	}
	
	public static ArrayList<PromoCodeMasterBean> loadPromoCodeApplicationType(Connection connection){
		ArrayList<PromoCodeMasterBean> list = new ArrayList<PromoCodeMasterBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PromoCodeMasterSql.loadPromocodeApplicationTypeSql, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				PromoCodeMasterBean bean = new PromoCodeMasterBean();
				bean.setPromocodeApplyTypeId(resultSet.getInt("fapp_promo_code_application_type_master_id"));
				bean.setPromoCodeApplyType(resultSet.getString("promo_code_application_type"));
				
				list.add(bean);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<PromoCodeMasterBean> loadPromoCodeList(Connection connection){
		ArrayList<PromoCodeMasterBean> list = new ArrayList<PromoCodeMasterBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PromoCodeMasterSql.loadPromoCodeList, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				PromoCodeMasterBean bean= new PromoCodeMasterBean();
				bean.setPromoCodeId(resultSet.getInt("fapp_promo_code_master_id"));
				bean.setPromoCode(resultSet.getString("promo_code"));
				
				list.add(bean);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	public static ArrayList<PromoCodeMasterBean> loadPromoCodeDetails(Connection connection){
		ArrayList<PromoCodeMasterBean> list = new ArrayList<PromoCodeMasterBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PromoCodeMasterSql.loadPromoCodeDetailsSql, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				PromoCodeMasterBean bean = new PromoCodeMasterBean();
				bean.setPromocodeDetailsId(resultSet.getInt("fapp_promo_code_details_id"));
				bean.setPromoCodeId(resultSet.getInt("promo_code_id"));
				bean.setPromoCode(resultSet.getString("promo_code"));
				
				bean.setFromDateSql(resultSet.getDate("from_date"));
				bean.setFromDateStr(resultSet.getString("from_date"));
				if(bean.getFromDateSql() != null){
					bean.setFromDateUtil(DateFormatter.sqlToUtilDate(bean.getFromDateSql()));
				}
				
				bean.setToDateSql(resultSet.getDate("to_date"));
				bean.setToDateStr(resultSet.getString("to_date"));
				if(bean.getToDateSql() != null){
					bean.setToDateUtil(DateFormatter.sqlToUtilDate(bean.getToDateSql()));
				}
				
				bean.getPromoTypeBean().setPromoCodeTypeId(resultSet.getInt("promo_type_id"));
				bean.getPromoTypeBean().setPromocodeType(resultSet.getString("promo_code_type"));
				
				bean.setPromoValue(resultSet.getDouble("promo_value"));
				
				bean.getPromoApplyBean().setApplyTypeId(resultSet.getInt("promo_code_application_type_id"));
				bean.getPromoApplyBean().setApplyType(resultSet.getString("promo_code_application_type"));
				
				bean.setPromoTypeBeanList(loadPromoType(connection));
				bean.setApplyBeanList(loadPromoApplyList(connection));
				
				String status = resultSet.getString("promo_code_is_active");
				if(status.equalsIgnoreCase("Y")){
					bean.setStatus("Active");
				}else {
					bean.setStatus("Inactive");
				}
				
				String reusable = resultSet.getString("is_reusable");
				if(reusable.equalsIgnoreCase("Y")){
					bean.setIsreUsable("YES");
				}else {
					bean.setIsreUsable("NO");
				}
				
				bean.setVolumeQuantity(resultSet.getInt("volume_quantity"));
				
				bean.setPromocodeDescription(resultSet.getString("description"));
				
				if(bean.getPromoTypeBean().getPromoCodeTypeId() == 3){ //3 = ON VOLUME
					bean.setVolumeQuantityDis(false);
				}else {
					bean.setVolumeQuantityDis(true);
				}
				System.out.println("");
				list.add(bean);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}	
		return list;
	}
	
	public static int inSertPromocodeDetails(Connection connection, PromoCodeMasterBean bean, PromoCodeMasterBean typeBean, PromoCodeMasterBean applbean){
		int i =0;
		int j =0;
		Date fromDate = new Date(bean.getFromDateUtil().getTime());
		Date toDate = new Date(bean.getToDateUtil().getTime());
		String status = bean.getStatus();
		if(status.equalsIgnoreCase("Active")){
			status = "Y";
		}else {
			status = "N";
		}
		
		try {
			
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PromoCodeMasterSql.insertPromoCodeDetailsSql, 
					Arrays.asList(bean.getPromoCodeId(), bean.getPromoCode(), fromDate, toDate, typeBean.getPromoTypeId(), 
							applbean.getPromocodeApplyTypeId(), bean.getUser(), bean.getUser(), status, bean.getPromoValue()));
			
			i = preparedStatement.executeUpdate();
			
			if(i>0){
				PreparedStatement preparedStatement2 = null;
				preparedStatement2 = FappPstm.createQuery(connection, PromoCodeMasterSql.updatePromoCodeSql, Arrays.asList(bean.getPromoCodeId()));
				j = preparedStatement2.executeUpdate();
			}
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return i;
	}
	
	public static ArrayList<PromoCodeTypeBean> loadPromoType(Connection connection){
		ArrayList<PromoCodeTypeBean> list = new ArrayList<PromoCodeTypeBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PromoCodeMasterSql.loadPromocodeTypeSql, null);
			
			ResultSet resultSet = preparedStatement.executeQuery(); 
			while (resultSet.next()) {
				PromoCodeTypeBean bean = new PromoCodeTypeBean();
				bean.setPromoCodeTypeId(resultSet.getInt("fapp_promo_code_type_master_id"));
				bean.setPromocodeType(resultSet.getString("promo_code_type"));
				
				list.add(bean);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<PromoCodeTypeBean> loadPromoApplyList(Connection connection){
		ArrayList<PromoCodeTypeBean> list = new ArrayList<PromoCodeTypeBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PromoCodeMasterSql.loadPromocodeApplicationTypeSql, null);
		
			ResultSet resultSet = preparedStatement.executeQuery(); 
			while (resultSet.next()) {
				PromoCodeTypeBean bean = new PromoCodeTypeBean();
				bean.setApplyTypeId(resultSet.getInt("fapp_promo_code_application_type_master_id"));
				bean.setApplyType(resultSet.getString("promo_code_application_type"));
				
				list.add(bean);
			}
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return list;
	}
	
	public static int updatePromoCodeDetails(Connection connection, PromoCodeMasterBean bean){
		int i = 0;
		Date fromDate = null;
		Date toDate = null;
		if(bean.getFromDateUtil() != null){
		fromDate = new Date(bean.getFromDateUtil().getTime());
		}
		if(bean.getToDateUtil() != null){
		toDate = new Date(bean.getToDateUtil().getTime());
		}
		String status = bean.getStatus();
		if(status.equalsIgnoreCase("Active")){
			status = "Y";
		}else {
			status = "N";
		}
		
		String reusable = bean.getIsreUsable();
		if(reusable.equalsIgnoreCase("YES")){
			reusable = "Y";
		}else {
			reusable = "N";
		}
		
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = FappPstm.createQuery(connection, PromoCodeMasterSql.upDatePromoCodeDetailsSql, Arrays.asList(bean.getPromocodeDescription(), reusable, fromDate, toDate, bean.getPromoTypeBean().getPromoCodeTypeId(),
													 bean.getPromoApplyBean().getApplyTypeId(), bean.getUser(), status, bean.getPromoValue(),bean.getVolumeQuantity(), bean.getPromocodeDetailsId()));
			System.out.println("QUERY -- " + preparedStatement);
			i = preparedStatement.executeUpdate();
			
		} catch (Exception e) {
			String msg = e.getMessage();
			Messagebox.show(msg, "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
	  return i;
	}
}
