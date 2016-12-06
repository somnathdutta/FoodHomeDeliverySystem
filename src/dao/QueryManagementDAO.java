package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.zkoss.zul.Messagebox;

import Bean.Query;


public class QueryManagementDAO {

	
	public static ArrayList<Query> loadAllUsersMessages(Connection connection){
		ArrayList<Query> queryList = new ArrayList<Query>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_fapp_query_users "
							+ " order by query_user_id ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							Query query = new Query();
							query.setQueryID(resultSet.getInt("query_user_id"));
							query.setQueryName(resultSet.getString("query_type"));
							query.setUserName(resultSet.getString("user_name"));
							query.setUserEmailId(resultSet.getString("user_email"));
							query.setUserMessage(resultSet.getString("user_message"));
							query.setQueryTime(resultSet.getTimestamp("created_date"));
							String replyMessage = resultSet.getString("eazelyf_message");
							if(replyMessage!=null){
								query.setReplyMessage(replyMessage);
							}else{
								query.setReplyMessage("");
							}
							
							String staus = resultSet.getString("is_active");
							if(staus.equalsIgnoreCase("Y")){
								query.setStatus("Active");
							}else{
								query.setStatus("Deactive");
							}
							
							queryList.add(query);
						}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return queryList;
	}
	
	public static void closeQuery(Query queryPojo, Connection connection){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql ="UPDATE fapp_query_from_user SET is_active ='N',eazelyf_message = ? where query_user_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, queryPojo.getReplyMessage());
						preparedStatement.setInt(2, queryPojo.getQueryID());
						System.out.println(preparedStatement);
						int count = preparedStatement.executeUpdate();
						if(count > 0 ){
							System.out.println("Closed!");
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * Generate invoice for completed order
	 * @param user
	 * @param order
	 * @param orderItemList
	 * @return
	 */
	 public static Boolean generateAndSendEmail(Query querypojo) { 
    	System.out.println("Email generation starting . . .!");
		 Properties mailServerProperties;
    	 Session getMailSession;
    	 MimeMessage generateMailMessage;
    	 String senderEmailID = "eazelyf@gmail.com";// change this to eazelyf's own mail id
     	String senderPassword = "eazelyf1234";
    	// Step1
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.host", "smtp.gmail.com");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.user", "eazelyf@gmail.com");
		mailServerProperties.put("mail.smtp.password", "eazelyf1234");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");
 
		// Step2
		System.out.println("\n 2nd ===> get Mail Session..");
		Authenticator auth = new utility.SMTPAuthenticator(senderEmailID, senderPassword);
		//getMailSession = Session.getDefaultInstance(mailServerProperties, auth);
		getMailSession = Session.getInstance(mailServerProperties, auth);
		generateMailMessage = new MimeMessage(getMailSession);
		try {
			generateMailMessage.setFrom(new InternetAddress("somnathdutta048@gmail.com"));
			generateMailMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(querypojo.getUserEmailId()) );
			generateMailMessage.setSubject("Query replied from EazeLyf");
			String emailBody = querypojo.getReplyMessage();
			generateMailMessage.setContent(emailBody, "text/html");
			System.out.println("Mail Session has been created successfully..");
			// Step3
			System.out.println("\n 4th ===> Get Session and Send mail");
			Transport transport = getMailSession.getTransport("smtp");
	 
			// Enter your correct gmail UserID and Password
			// if you have 2FA enabled then provide App Specific Password
			//transport.connect("smtp.gmail.com", "<----- Your GMAIL ID ----->", "<----- Your GMAIL PASSWORD ----->");
			transport.connect("eazelyf@gmail.com", "eazelyf1234");
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
			return true;
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			Messagebox.show("ERROR DUE TO1:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			Messagebox.show("ERROR DUE TO2:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			e.printStackTrace();
		}
		return false;
	}
	 
	 /*public static Boolean generateAndSendEmail(String toAddress) { 
    	 Properties mailServerProperties;
    	 Session getMailSession;
    	 MimeMessage generateMailMessage;
    	// Step1
		System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.host", "smtp.gmail.com");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.user", "eazelyf@gmail.com");
		mailServerProperties.put("mail.smtp.password", "eazelyf1234");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("Mail Server Properties have been setup successfully..");
 
		// Step2
		System.out.println("\n\n 2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties);
		generateMailMessage = new MimeMessage(getMailSession);
		try {
			generateMailMessage.setFrom(new InternetAddress("somnathdutta048@gmail.com"));
			generateMailMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(toAddress) );
			generateMailMessage.setSubject("Order delivered");
			//String emailBody = buildBody();
			String emailBody = "Hi";
			generateMailMessage.setContent(emailBody, "text/html");
			System.out.println("Mail Session has been created successfully..");
			// Step3
			System.out.println("\n\n 3rd ===> Get Session and Send mail");
			Transport transport = getMailSession.getTransport("smtp");
	 
			// Enter your correct gmail UserID and Password
			// if you have 2FA enabled then provide App Specific Password
			//transport.connect("smtp.gmail.com", "<----- Your GMAIL ID ----->", "<----- Your GMAIL PASSWORD ----->");
			transport.connect("eazelyf@gmail.com", "eazelyf1234");
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
			return true;
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}*/
}
