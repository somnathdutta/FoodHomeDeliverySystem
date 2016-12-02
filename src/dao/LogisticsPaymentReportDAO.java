package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.LogisticsPaymentReportBean;
import Bean.ManageDeliveryBoyBean;
import Bean.PaymentModeBean;

public class LogisticsPaymentReportDAO {

	
	public static ArrayList<PaymentModeBean> loadpaymnetMode(Connection connection){
		ArrayList<PaymentModeBean> list = new ArrayList<PaymentModeBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			String sql = "select payment_method_id,payment_method from fapp_payment_methods where is_active= 'Y' "; 
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					PaymentModeBean bean =  new PaymentModeBean();
					bean.setPaymentModeId(resultSet.getInt("payment_method_id"));
					bean.setPaymentMode(resultSet.getString("payment_method"));
					list.add(bean);
				}
				
			} finally{
				if(preparedStatement != null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}
	
	public static ArrayList<ManageDeliveryBoyBean> loadBiker(Connection connection){
		ArrayList<ManageDeliveryBoyBean> list = new ArrayList<ManageDeliveryBoyBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			String sql = "select * from vw_delivery_boy_data where kitchen_id is not null and is_active = 'Y' "; 
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ManageDeliveryBoyBean bean =  new ManageDeliveryBoyBean();
					bean.deliveryBoyUserId = resultSet.getString("delivery_boy_user_id");
					bean.name = resultSet.getString("delivery_boy_name");
					list.add(bean);
				}
				
			} finally{
				if(preparedStatement != null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}
	
	
	
	
	public static ArrayList<LogisticsPaymentReportBean> onLoadLogisticsReport(Connection connection){
		ArrayList<LogisticsPaymentReportBean> list = new ArrayList<LogisticsPaymentReportBean>();
		if(list.size()>0){
			list.clear();
		}
		
		try {
			String sql = "select order_no, delivery_date, driver_name, payment_name,final_price from vw_order_dashboard where order_status_id = 7 ";
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(sql);
				ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				LogisticsPaymentReportBean bean = new LogisticsPaymentReportBean();
				bean.setBikerName(resultSet.getString("driver_name"));
				bean.setOrderNo(resultSet.getString("order_no"));
				bean.setPaymentMode(resultSet.getString("payment_name"));
				bean.setAmount(resultSet.getDouble("final_price"));
				
				bean.setDeliveryDateSql(resultSet.getDate("delivery_date"));
				String str = resultSet.getString("delivery_date");
				if(str != null){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
					str = newFormat.format(format.parse(str));
					
					bean.setDeliveryDateStr(str);
				}
				
				list.add(bean);
				
			}
				
			}finally{
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "ERROR", Messagebox.OK, Messagebox.ERROR);
		}
		return list;
		
	}
	
	
	public static ArrayList<LogisticsPaymentReportBean> loadSearch(Connection connection, LogisticsPaymentReportBean bean1){
		ArrayList<LogisticsPaymentReportBean> list = new ArrayList<LogisticsPaymentReportBean>();
		if(list.size()>0){
			list.clear();
		}
		try {
			String sql = "select order_no, delivery_date, driver_name, payment_name,final_price from vw_order_dashboard where order_status_id = 7 and delivery_date>= ? and delivery_date <= ? ";
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setDate(1, new java.sql.Date(bean1.getDeliveryFormDateUtil().getTime()));
				preparedStatement.setDate(2, new java.sql.Date(bean1.getDeliveryToDateUtil().getTime()));
				ResultSet resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {

					LogisticsPaymentReportBean bean = new LogisticsPaymentReportBean();
					bean.setBikerName(resultSet.getString("driver_name"));
					bean.setOrderNo(resultSet.getString("order_no"));
					bean.setPaymentMode(resultSet.getString("payment_name"));
					bean.setAmount(resultSet.getDouble("final_price"));
					
					bean.setDeliveryDateSql(resultSet.getDate("delivery_date"));
					String str = resultSet.getString("delivery_date");
					if(str != null){
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
						str = newFormat.format(format.parse(str));
						
						bean.setDeliveryDateStr(str);
					}
					
					list.add(bean);
				}
			} finally{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "ERROR", Messagebox.OK, Messagebox.ERROR);
			
		}
		return list;
	}
	
	
}
