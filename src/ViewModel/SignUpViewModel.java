package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.SignUpBean;

public class SignUpViewModel {
	
	private SignUpBean signUpBean = new SignUpBean();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String emailTextBoxStyle = "";
	
	private String mobileNumberTextBoxStyle = "";
	
	PropertyFile propertyFile = new PropertyFile();
	private ArrayList<String> cityList= new ArrayList<String>();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("connectionParent") Connection connectionParent) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection = connectionParent;
		//connection=(Connection) session.getAttribute("sessionConnection");
		
		//connection = DbConnection.getConnection();
		
		connection.setAutoCommit(true);
		
		onLoadCityList();
	}
	
	public void onLoadCityList(){
		
		if(cityList!=null){
			cityList.clear();
		}
		
		try {
				sql1:{
						PreparedStatement preparedStatement=null;
						try {
						
								String sql= "SELECT city_name,city_id "+
											"FROM sa_city ";																		 
								
								preparedStatement=connection.prepareStatement(sql);
								
								ResultSet rs=preparedStatement.executeQuery();
								
								while(rs.next()){
							
									cityList.add(rs.getString(1));
									
								}
						
						} catch (Exception e) {
							Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
							e.printStackTrace();
							} finally{
						
								if(preparedStatement!=null){
									preparedStatement.close();
									}
								}					
					}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectcityName(){
		try {
			sql1:{
					PreparedStatement preparedStatement=null;
					try {
					
							String sql= "SELECT city_id "+
										"FROM sa_city where city_name=?";																		 
							
							preparedStatement=connection.prepareStatement(sql);
							preparedStatement.setString(1, signUpBean.cityName);
							ResultSet resultSet=preparedStatement.executeQuery();
							
							while(resultSet.next()){
								signUpBean.cityId = resultSet.getInt("city_id");								
							}
					
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
						} finally{
					
							if(preparedStatement!=null){
								preparedStatement.close();
								}
							}					
				}
		} catch (Exception e) {
			e.printStackTrace();
			}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickRegister(){
			if(validateFields()){
				registerNewUser();
			}
	}
	
	public void registerNewUser(){
		
		String message = "";
		
		try {
			 SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
							preparedStatement = connection.prepareStatement(propertyFile.getPropValues("registerNewUserSql"));
							System.out.println(preparedStatement);
								preparedStatement.setString(1, signUpBean.userName);
								preparedStatement.setString(2, signUpBean.password);
								preparedStatement.setString(3, signUpBean.email);
								preparedStatement.setString(4, signUpBean.firstName);
								
								if(signUpBean.middleName!=null){
									preparedStatement.setString(5, signUpBean.middleName);
								}
								else{
									preparedStatement.setString(5, "");
								}
								
								preparedStatement.setString(6, signUpBean.lastName);
								
								if(signUpBean.addressLine1!=null){
									preparedStatement.setString(7, signUpBean.addressLine1);
								}
								else{
									preparedStatement.setString(7, "");
								}
								
								if(signUpBean.addressLine2!=null){
									preparedStatement.setString(8, signUpBean.addressLine2);
								}
								else{
									preparedStatement.setString(8,"");
								}
								
								if(signUpBean.cityId!=null){
									preparedStatement.setInt(9, signUpBean.cityId);
								}
								else{
									preparedStatement.setNull(9, Types.INTEGER);
								}
								
								if(signUpBean.pinCode!=null){
									preparedStatement.setInt(10, signUpBean.pinCode);
								}
								else{
									preparedStatement.setNull(10, Types.INTEGER);
								}
								
								preparedStatement.setString(11, signUpBean.mobileNo);
								
								if(signUpBean.isActiveChecked){
									preparedStatement.setString(12, "Y");
								}
								else{
									preparedStatement.setString(12, "N");
								}
								if(signUpBean.isAdminChecked){
									preparedStatement.setString(13, "Y");
								}
								else{
									preparedStatement.setString(13, "N");
								}
								
							resultSet = preparedStatement.executeQuery();
							
							if(resultSet.next()){
								
								message = resultSet.getString(1);
								System.out.println("Register message =" + message);
							}
					} catch (Exception e) {
						Messagebox.show("Error Due To: " + e.getMessage(), "Error",Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
					}finally {
						if (preparedStatement != null) {
							preparedStatement.close();
						}	
					}
					if (message.contains("Successfully")) {
						Messagebox.show("Registered Successfully...");
						clearData();
						connection.close();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public Boolean validateMobileNumber(){
		if(signUpBean.mobileNo.matches("[0-9]+") == true){
			return true;
		}
		else{
			return false;
		}
	}
	
	public Boolean validateEmailId(){
		String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	      String email = signUpBean.email;
	      Boolean valid = email.matches(EMAIL_REGEX);
	      return valid;
	}
	
	public Boolean validateFields(){
		if(signUpBean.userName!=null){
			if(signUpBean.password!=null){
					if(signUpBean.firstName!=null){
						if(signUpBean.lastName!=null){
							if(signUpBean.mobileNo!=null){
								if(validateMobileNumber()){
									if(signUpBean.email!=null){
										if(validateEmailId()){
											return true;
										}else{
											Messagebox.show("Please give valid email as john@domainname!","INVALID",Messagebox.OK,Messagebox.ERROR);
											return false;
										}
									}else {
										Messagebox.show("Email required!");
										return false;
									}
								}else{
									Messagebox.show("Please give valid mobile number as 9988877777!","INVALID",Messagebox.OK,Messagebox.ERROR);
									return false;
								}
							}else {
								Messagebox.show("Phone number required!");
								return false;
							}
						}else {
							Messagebox.show("Last Name required!");
							return false;
						}
					}else {
						Messagebox.show("First Name required!");
						return false;
					}
			}else {
				Messagebox.show("Password required!");
				return false;
			}
		}else {
			Messagebox.show("User Name required!");
			return false;
		}
	}
	public void clearData(){
		signUpBean.userName = "";
		signUpBean.password = "";
		signUpBean.email = "";
		signUpBean.firstName = "" ;
		signUpBean.middleName = "";
		signUpBean.lastName = "";
		signUpBean.addressLine1 = "";
		signUpBean.addressLine2 = "";
		signUpBean.cityName = "";
		signUpBean.pinCode = null;
		signUpBean.mobileNo = "";
		signUpBean.isActiveChecked = false;
		signUpBean.isAdminChecked = false;
	}
	
	public SignUpBean getSignUpBean() {
		return signUpBean;
	}

	public void setSignUpBean(SignUpBean signUpBean) {
		this.signUpBean = signUpBean;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}


	public Session getSession() {
		return session;
	}


	public void setSession(Session session) {
		this.session = session;
	}


	public String getEmailTextBoxStyle() {
		return emailTextBoxStyle;
	}


	public void setEmailTextBoxStyle(String emailTextBoxStyle) {
		this.emailTextBoxStyle = emailTextBoxStyle;
	}


	public String getMobileNumberTextBoxStyle() {
		return mobileNumberTextBoxStyle;
	}


	public void setMobileNumberTextBoxStyle(String mobileNumberTextBoxStyle) {
		this.mobileNumberTextBoxStyle = mobileNumberTextBoxStyle;
	}


	public ArrayList<String> getCityList() {
		return cityList;
	}


	public void setCityList(ArrayList<String> cityList) {
		this.cityList = cityList;
	}
	
}
