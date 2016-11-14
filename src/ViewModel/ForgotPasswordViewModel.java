package ViewModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.UUID;

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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.LoginBean;

public class ForgotPasswordViewModel {
	
	Session session = null;
	
	@Wire("#winForgotPassword")
	private Window winForgotPassword;
	
	private Connection connection = null;
	
	private String username;
	
	private LoginBean loginBean = new LoginBean();
	
	private Integer loginCount = 0;
	
	private String password;
	
	private Boolean okBtnDisability = false;
	private String label;
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view, 
				@ExecutionArgParam("hashcode") String hascode) {

		Selectors.wireComponents(view, this, false);
		session = Sessions.getCurrent();
		
		System.out.println("zul page >> forGotPassWord.zul");
		loginBean.newPasswordDis = true;
		loginBean.confirmPasswordDis = true;
		loginBean.saveDis = true;
		
		loginBean.hashcode = hascode;
		loginBean.contactNoDis = true;
		loadEmailId(hascode);
		label = "Reset password for the email "+ loginBean.email;
	}
	
	public void loadEmailId(String hc){
		PreparedStatement preparedStatement = null;
		Connection conn = null;
		
		String sql = "select email, mobile_no from fapp_accounts where hash_code = ? ";
		
		try {
			
		
		try {
			Class.forName("org.postgresql.Driver"); 
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/foodhomedelivery", "postgres", "password"); 
			preparedStatement  = conn.prepareStatement(sql);
			
			preparedStatement.setString(1, hc);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				loginBean.email = resultSet.getString("email");
				loginBean.contactNo = resultSet.getString("mobile_no");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(preparedStatement != null){
				preparedStatement.close();
			}if(conn != null){
				conn.close();
			}
		}
		

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Command
	@NotifyChange("*")
	public void onChangeOldPassword(){
		if(isValidPassword(loginBean.oldPassword, loginBean.userName)){
			
		}else{
			Messagebox.show("Old Password is wrong!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
			loginBean.oldPassword = null;
		}
	}
	
	
	 private Boolean isValidPassword(String oldPassword, String userName){
	    	Boolean isValid = false;
	    	System.out.println(oldPassword+ "  "+userName);
	    	try {
				SQL:{
	    				PreparedStatement preparedStatement = null;
	    				ResultSet resultSet = null;
	    				String sql = "SELECT * FROM fapp_accounts WHERE password = ? AND username = ?";
	    				try {
	    					//connection.setAutoCommit(false);
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setString(1, oldPassword);
							preparedStatement.setString(2, userName);
							
							resultSet = preparedStatement.executeQuery();
							while (resultSet.next()) {
								isValid = true;
								System.out.println(isValid);
								//connection.commit();
							}
						} catch (Exception e) {
							connection.rollback();
							Messagebox.show("Error due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
							e.printStackTrace();
						}finally{
							if(preparedStatement!=null ){
								preparedStatement.close();
							}
						}
	    		}
			} catch (Exception e) {
				// TODO: handle exception
				
			}
	    	return isValid;
	    }
	 
	 
	 private Boolean isValidate() {
		  if(loginBean.contactNo!=null){
			  if(loginBean.oldPassword!=null){
				  if(loginBean.newPassword!=null){
					  return true;
				  }else{
					  
					  Messagebox.show("New password required!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
					  return false;
				  }
			  }else{
				  
				  Messagebox.show("Old password required!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				  return false;
			  }
		  }else{
			  
			  Messagebox.show("User name required!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
			  return false;
		  }
	  }
	 
	 @Command
	 @NotifyChange("*")
	 public void onChangeContactNo(){
		 boolean isContainsSpace = false;
		 /*if(loginBean.contactNo.contains(" ")){
			 isContainsSpace = true;
			 loginBean.newPasswordDis = true;
			 loginBean.confirmPasswordDis = true;
			 Messagebox.show("Space Not Allowed!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
			 
		 }else {
			 if(!isContainsSpace){
				 if(loginBean.contactNo.length() != 10){
					 Messagebox.show("Shoud Enter Ten Digit Contact No", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				 }
				 
			 }
			 loginBean.newPasswordDis = false;
		}*/
		 
		 if(!loginBean.contactNo.contains(" ") && loginBean.contactNo.length()==10){
			 loginBean.newPasswordDis = false;
		 }else {
			 loginBean.newPasswordDis = true;
			 loginBean.confirmPasswordDis = true;
			 Messagebox.show("Enter Proper Mobile Number!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
		 
	 }
	 
	 @Command
	 @NotifyChange("*")
	 public void onChangeNewPassword(){
		 if(!loginBean.newPassword.contains(" ")){
			 loginBean.confirmPasswordDis = false; 
		 }else {
			 loginBean.confirmPasswordDis = true;
			 Messagebox.show("Enter Proper Mobile Number!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	 }
	 
	 @Command
	 @NotifyChange("*")
	 public void onChangeConfirmPassword(){
		 if(!loginBean.newPassword.equals(loginBean.confirmPassword)){
			 Messagebox.show("New Password and Confirm Password Not Matched!", "Error", Messagebox.OK, Messagebox.ERROR);
			 loginBean.saveDis = true;
		 }else {
			loginBean.saveDis = false;
		}
	 }
	 
	 
	 
	 
	/*@Command
	@NotifyChange("*")
	public void onChangeUserName(){
		
		try {
			
			sql1:{
			
				PreparedStatement preparedStatementSQL1 = null;
				
				try {
					
					preparedStatementSQL1 = connection.prepareStatement("SELECT COUNT(*) "
																		+ "FROM fapp_accounts "
																		+ "WHERE username = ? ");
					
					preparedStatementSQL1.setString(1, loginBean.userName);
					
					ResultSet resultSetSQL1 = preparedStatementSQL1.executeQuery();
					
					if(resultSetSQL1.next()){
						
						loginCount = resultSetSQL1.getInt(1);
					}
					
					
				} catch (Exception e) {
					
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
					
				}finally{
					
					if(preparedStatementSQL1!= null){
						
						preparedStatementSQL1.close();
					}
					
				}
			}
		
			if(loginCount>0){
				
				password = UUID.randomUUID().toString().replace("-", "");
				
				loginBean.password = generateRandomString();
				
				okBtnDisability = false;
				
			}else{
				Messagebox.show("User Id Not Found..", "Information", Messagebox.OK, Messagebox.INFORMATION);
				okBtnDisability = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	 @Command
	 @NotifyChange("*")
	 public void onClickResetPassword(){
		 int i = 0;
		 if(validation()){
		 	 
			 String sql = "UPDATE fapp_accounts SET password = ? WHERE mobile_no = ? ";
		 
		 		try {
		 		Connection con = null;
		 		PreparedStatement preparedStatement = null;
			    try {
					Class.forName("org.postgresql.Driver"); 
					con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/foodhomedelivery", "postgres", "password"); 
					preparedStatement = con.prepareStatement(sql); 
					preparedStatement = con.prepareStatement(sql);
					preparedStatement.setString(1, loginBean.confirmPassword);
					preparedStatement.setString(2, loginBean.contactNo);
					
					i = preparedStatement.executeUpdate();
				if(i>0){
					Messagebox.show("Password Successfully Updated \n close the window", "Information", Messagebox.OK, Messagebox.INFORMATION);
					
					winForgotPassword.detach();
					
					
				}else {
					
					Messagebox.show("Enter Proper Data", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				
			 } catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
			}finally{
				if(preparedStatement != null){
					preparedStatement.close();
				}
				if(con != null){
					con.close();
				}
			}
			 

			} catch (Exception e) {
				e.printStackTrace();
			} 	
		 	
		}
	 }
	 
	 
	 public boolean validation(){
		 if(loginBean.newPassword != null){
			 if(loginBean.confirmPassword != null){
				 if(loginBean.newPassword.matches("^[^\\d\\s]+$")){
					 if(loginBean.confirmPassword.matches("^[^\\d\\s]+$")){
						 if(loginBean.newPassword.equals(loginBean.confirmPassword)){
							 return true;
						 }else {
							 loginBean.confirmPassword = null;
							 loginBean.newPassword  = null;
							 Messagebox.show("Confirm PassWord And New Password Not Matched", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
							 return false;
						}
						 
					 }else {
						 loginBean.confirmPassword = null;
						 loginBean.newPassword  = null;
						 Messagebox.show("Entered Confirm Password Fields Should Not Cotains any space ", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
						 return false;
					}
				 }else {
					 loginBean.confirmPassword = null;
					 loginBean.newPassword  = null;
					 Messagebox.show("Entered New Password Fields Should Not Cotains any space ", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
					 return false;
				}
			 }else {
				 Messagebox.show("Enter Confirm Password Fields", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				 return false;
			}
		 }else {
			 Messagebox.show("Enter New Password Fields", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
			 return false;
		}
		 
		 
	 }
	 
	 
	 
	 
	@Command
	@NotifyChange("*")
	public void onClickOkButton(){
		if(isValidate()){
			try {
				
				sql1:{
				
					PreparedStatement preparedStatementSQL1 = null;
					
					try {
						
						//connection.setAutoCommit(false);
					
						preparedStatementSQL1 = connection.prepareStatement("UPDATE fapp_accounts "
																			+ "SET password = ? "
																			+ "WHERE "
																			+ "username = ? ");
						
						preparedStatementSQL1.setString(1, loginBean.newPassword);
						preparedStatementSQL1.setString(2, loginBean.userName);
						
						int rowCount = preparedStatementSQL1.executeUpdate();
						
						if(rowCount>0){

							Messagebox.show("Password Successfully Updated", "Information", Messagebox.OK, Messagebox.INFORMATION);
							connection.commit();
							
							winForgotPassword.detach();
						}
						
					} catch (Exception e) {
						
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						//connection.rollback();
						e.printStackTrace();
						
					}finally{
						
						if(preparedStatementSQL1!= null){
							
							preparedStatementSQL1.close();
						}
						
					}
				
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public String generateRandomString(){
        
        StringBuffer randStr = new StringBuffer();
        
        for(int i=0; i<10; i++){
        
        	int number = getRandomNumber();
           
        	char ch = password.charAt(number);
            
        	randStr.append(ch);
        
        }

        return randStr.toString();
    }
     
   
    private int getRandomNumber() {
    
    	int randomInt = 0;
    	
        Random randomGenerator = new Random();
        
        randomInt = randomGenerator.nextInt(password.length());
        
        if (randomInt - 1 == -1) {
        	
            return randomInt;
        
        } else {
        
        	return randomInt - 1;
        
        }
    }

	public Window getWinForgotPassword() {
		return winForgotPassword;
	}

	public void setWinForgotPassword(Window winForgotPassword) {
		this.winForgotPassword = winForgotPassword;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}



	public Integer getLoginCount() {
		return loginCount;
	}



	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Boolean getOkBtnDisability() {
		return okBtnDisability;
	}



	public void setOkBtnDisability(Boolean okBtnDisability) {
		this.okBtnDisability = okBtnDisability;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	
	
}
