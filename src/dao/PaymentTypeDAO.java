package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import org.zkoss.zul.Messagebox;

import sql.PaymentTypeSql;
import utility.FappPstm;
import Bean.PaymentMethod;

public class PaymentTypeDAO {
	public static ArrayList<PaymentMethod> fetchAllPaymentTypes(Connection connection){
		ArrayList<PaymentMethod> paymenttypeList = new ArrayList<PaymentMethod>();
		try {
			SQL:{
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PaymentTypeSql.loadAllPaymentTypeQuery, null);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				PaymentMethod paymenttype = new PaymentMethod();
				paymenttype.setPaymentId(resultSet.getInt("paymenttype_id"));
				paymenttype.setPaymentType(resultSet.getString("paymenttype_question"));
				if(resultSet.getString("is_active").equals("Y")){
					paymenttype.setStatus("Active");
				}else{
					paymenttype.setStatus("Deactive");
				}
				paymenttypeList.add(paymenttype);
			}
		}
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
		return paymenttypeList;
	}

	public static void savePaymentType(Connection connection, PaymentMethod PaymentMethod){
		int insertCount = 0;
		try {
			SQL:{
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PaymentTypeSql.insertPaymentTypeQuery, Arrays.asList(PaymentMethod));
			preparedStatement.setString(1, PaymentMethod.getPaymentType());
			preparedStatement.setInt(2, PaymentMethod.getPaymentId());
			if(PaymentMethod.getStatus().equalsIgnoreCase("Active")){
				preparedStatement.setString(3, "Y");
			}else{
				preparedStatement.setString(3, "N");
			}
			preparedStatement.setString(4, PaymentMethod.getUserName());
			insertCount = preparedStatement.executeUpdate();
		}
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
		if(insertCount>0){
			Messagebox.show("paymenttype Saved successfully!","Success Information",Messagebox.OK,Messagebox.INFORMATION);
			//paymenttypeDAO.fetchAllpaymenttypes(connection);
		}
	}

	public static void updatePaymentType(Connection connection, PaymentMethod PaymentMethod){
		int updateCount = 0;
		try {
			SQL:{
			PreparedStatement preparedStatement = null;
			preparedStatement = FappPstm.createQuery(connection, PaymentTypeSql.updatePaymentTypeQuery, Arrays.asList(PaymentMethod));
			preparedStatement.setString(1, PaymentMethod.getPaymentType());
			preparedStatement.setInt(2, PaymentMethod.getPaymentId());
			if(PaymentMethod.getStatus().equalsIgnoreCase("Active")){
				preparedStatement.setString(3, "Y");
			}else{
				preparedStatement.setString(3, "N");
			}
			preparedStatement.setString(4, PaymentMethod.getUserName());
		//	preparedStatement.setInt(5, PaymentMethod.getpaymenttypeId());
			updateCount = preparedStatement.executeUpdate();
		}
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
		if(updateCount>0){
			Messagebox.show("paymenttype updated successfully!","Success Information",Messagebox.OK,Messagebox.INFORMATION);
			//paymenttypeDAO.fetchAllpaymenttypes(connection);
		}
	}
}
