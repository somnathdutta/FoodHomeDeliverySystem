package service;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import dao.LogisticsPaymentReportDAO;
import Bean.LogisticsPaymentReportBean;
import Bean.ManageDeliveryBoyBean;
import Bean.PaymentModeBean;

public class LogisticsPaymentReportService {

	public static ArrayList<LogisticsPaymentReportBean> onload(Connection connection){
		ArrayList<LogisticsPaymentReportBean> list = new ArrayList<LogisticsPaymentReportBean>();
		list = LogisticsPaymentReportDAO.onLoadLogisticsReport(connection);
		return list;
	}
	
	public static ArrayList<ManageDeliveryBoyBean> onLoadDriver(Connection connection){
		ArrayList<ManageDeliveryBoyBean> list = new ArrayList<ManageDeliveryBoyBean>();
		list = LogisticsPaymentReportDAO.loadBiker(connection);
		return list;
	}
	
	public static ArrayList<PaymentModeBean> onLoadPaymentMode(Connection connection){
		ArrayList<PaymentModeBean> list = new ArrayList<PaymentModeBean>();
		list = LogisticsPaymentReportDAO.loadpaymnetMode(connection);
		return list;
	}
	
	public static ArrayList<LogisticsPaymentReportBean> onloadDetailsWithDate(Connection connection, LogisticsPaymentReportBean bean1){
		ArrayList<LogisticsPaymentReportBean> list = new ArrayList<LogisticsPaymentReportBean>();
		list = LogisticsPaymentReportDAO.loadSearch(connection, bean1);
		return list;
	}
	
	
	
	
	public static boolean validation(LogisticsPaymentReportBean bean){
		if(bean.getDeliveryFormDateUtil() !=null){
			if(bean.getDeliveryToDateUtil() != null){
				if(!bean.getDeliveryToDateUtil().before(bean.getDeliveryFormDateUtil())){
					return true;
				}else {
					bean.setDeliveryToDateUtil(null);
					Messagebox.show("Delivery To Date should not less than From Date", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
					return false;
				}
			}else {
				Messagebox.show("Select Delivery To Date", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				return false;
			}
		}else {
			Messagebox.show("Select Delivery Form Date", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
			return false;
		}
	}
	
}
