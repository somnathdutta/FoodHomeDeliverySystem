package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import org.zkoss.zul.Messagebox;

import sql.FaqSql;
import utility.FappPstm;
import Bean.FaqBean;

public class FaqDAO {

	public static ArrayList<FaqBean> fetchAllFaqs(Connection connection){
			ArrayList<FaqBean> faqList = new ArrayList<FaqBean>();
			try {
				SQL:{
						PreparedStatement preparedStatement = null;
						preparedStatement = FappPstm.createQuery(connection, FaqSql.loadAllFaqsQuery, null);
						ResultSet resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							FaqBean faq = new FaqBean();
							faq.setFaqId(resultSet.getInt("faq_id"));
							faq.setFaqQuestion(resultSet.getString("faq_question"));
							faq.setFaqAnswer(resultSet.getString("faq_answer"));
							if(resultSet.getString("is_active").equals("Y")){
								faq.setStatus("Active");
							}else{
								faq.setStatus("Deactive");
							}
							faqList.add(faq);
						}
					}
			} catch (Exception e) {
				// TODO: handle exception
				Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				e.printStackTrace();
			}
			return faqList;
	}
	
	public static void saveNewFaq(Connection connection, FaqBean faqBean){
		int insertCount = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					preparedStatement = FappPstm.createQuery(connection, FaqSql.saveFaqQuery, Arrays.asList(faqBean));
					preparedStatement.setString(1, faqBean.getFaqQuestion());
					preparedStatement.setString(2, faqBean.getFaqAnswer());
					if(faqBean.getStatus().equalsIgnoreCase("Active")){
						preparedStatement.setString(3, "Y");
					}else{
						preparedStatement.setString(3, "N");
					}
					preparedStatement.setString(4, faqBean.getUserName());
					insertCount = preparedStatement.executeUpdate();
				}
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
		if(insertCount>0){
			Messagebox.show("Faq Saved successfully!","Success Information",Messagebox.OK,Messagebox.INFORMATION);
			FaqDAO.fetchAllFaqs(connection);
		}
	}
	
	public static void updateNewFaq(Connection connection, FaqBean faqBean){
		int updateCount = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					preparedStatement = FappPstm.createQuery(connection, FaqSql.updateFaqQuery, Arrays.asList(faqBean));
					preparedStatement.setString(1, faqBean.getFaqQuestion());
					preparedStatement.setString(2, faqBean.getFaqAnswer());
					if(faqBean.getStatus().equalsIgnoreCase("Active")){
						preparedStatement.setString(3, "Y");
					}else{
						preparedStatement.setString(3, "N");
					}
					preparedStatement.setString(4, faqBean.getUserName());
					preparedStatement.setInt(5, faqBean.getFaqId());
					updateCount = preparedStatement.executeUpdate();
				}
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
		if(updateCount>0){
			Messagebox.show("Faq updated successfully!","Success Information",Messagebox.OK,Messagebox.INFORMATION);
			FaqDAO.fetchAllFaqs(connection);
		}
	}
	
	public static void deleteFaq(Connection connection, FaqBean faqBean){
		int deleteCount = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					preparedStatement = FappPstm.createQuery(connection, FaqSql.deleteFaqQuery, Arrays.asList(faqBean));
					preparedStatement.setString(1, faqBean.getUserName());
					preparedStatement.setInt(2, faqBean.getFaqId());
					deleteCount = preparedStatement.executeUpdate();
				}
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
		if(deleteCount>0){
			Messagebox.show("Faq deleted successfully!","Success Information",Messagebox.OK,Messagebox.INFORMATION);
			FaqDAO.fetchAllFaqs(connection);
		}
	}
}
