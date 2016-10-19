package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sun.java.swing.plaf.windows.resources.windows;

import Bean.LoginBean;
import Database.DataBaseHandler;

public class WelcomeViewModel {
		
	private LoginBean loginBean = new LoginBean();
	
	Session session = null;
	
	private Connection connection=null;
	
	public ArrayList<String> departmentList =  new ArrayList<String>();
	
	PropertyFile propertyfile =  new PropertyFile();
	//private Map<String, LoginBean> loginMap = new Hashtable<String, LoginBean>();
	
	private Boolean rowVisibility = true;
	
	private String gridHeight = "95%";
	
	public WelcomeViewModel() {

		try {
				connection = DataBaseHandler.getInstance().getConnection();
			//connection = DataBaseHandler.getInstance().createConnection();///local need to changed
	
		} catch (Exception e) {
			
			Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
			
			e.printStackTrace();
		}
		System.out.println("CONNECTION: "+connection);
		
	}
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) {

		Selectors.wireComponents( view, this, false);
		session = Sessions.getCurrent();
		
		
	}
	

	public void fetchDepartmentList(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT department_name from fapp_department_master";
				try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while(resultSet.next()){
						departmentList.add(resultSet.getString(1).toUpperCase());
					}
				} catch (Exception e) {
					// TODO: handle exception
					Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectLoginType(){
		System.out.println("Login type:"+loginBean.roleName);
		if(loginBean.roleName!=null && loginBean.roleName.trim().length()>0){
			loginBean.roleId = getRoleId();
			gridHeight = "95%";
			rowVisibility = true;
			loginBean.userName=null;
			loginBean.password=null;
		}else{
			 loginBean.userName=null;
			 loginBean.password=null;
			 rowVisibility = false;
			 gridHeight = null;
			Messagebox.show("Please choose login type!","INFORMATION",Messagebox.OK,Messagebox.EXCLAMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickLogin(){
		String[] msg = null;
		if(validateFields()){
			try {
				
				sql1:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = propertyfile.getPropValues("onClickLoginClickSql");
					try {
						
						preparedStatement = connection.prepareStatement(sql);
						
						preparedStatement.setString(1, loginBean.userName);
						preparedStatement.setString(2, loginBean.password);
						
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
						msg = resultSet.getString(1).split("-");					
						System.out.println("chk login:" +resultSet.getString(1) );
						//Messagebox.show(msg, "Information", Messagebox.OK, Messagebox.INFORMATION);
						}
						session.setAttribute("login", loginBean.userName);
						session.setAttribute("sessionConnection", connection);
						session.setAttribute("userRoleId",Integer.valueOf(msg[1]));
						session.setAttribute("userId", Integer.valueOf(msg[2]));
						
						System.out.println("LOGIN ROLE ID: "+Integer.valueOf(msg[1])+" LOGIN ID: "+Integer.valueOf(msg[2]));
						
						Executions.sendRedirect("view/homeTabIndex.zul");
						//Executions.sendRedirect("view/homePage.zul");
						
					} catch (Exception e) {
						//Messagebox.show(""+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						Window win = (Window) Executions.createComponents("view/errorBox.zul", null, null);
						
						win.doModal();
						loginBean.userName=null;
						loginBean.password=null;
						//rowVisibility = false;
						//gridHeight = null;
						loginBean.departmentName="";
						
						//Executions.sendRedirect("index.zul");
						//e.printStackTrace();
						
					}finally{
						
						if(preparedStatement!= null){
							
							preparedStatement.close();
						}
						
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSignUp(){
		session.setAttribute("login", loginBean.userName);
		session.setAttribute("sessionConnection", connection);
		
		Map<String, Connection> connectionMap = new HashMap<String, Connection>();
		connectionMap.put("connectionParent", connection);
		
		Window window = (Window) Executions.createComponents("/view/signUp.zul", null, connectionMap);
		
		window.doModal();
		//Executions.sendRedirect("view/signUp.zul");
	}

	
	@Command
	@NotifyChange("*")
	public void onClickForgotPassword(){
		Map<String, Connection> connectionMap = new HashMap<String, Connection>();
		connectionMap.put("connectionParent", connection);
		
		Window window = (Window) Executions.createComponents("view/forGotPassWord.zul", null, connectionMap);
		window.doModal();
	}
	
	public Integer getRoleId(){
		Integer deptId = 0 ;
		try {
			SQL:
			{	
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "SELECT role_id from fapp_role_master where role_name=?";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, loginBean.roleName);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						deptId = resultSet.getInt(1);
					}
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return deptId;
	}
	
	public Boolean validateFields(){
		if(loginBean.userName!=null){
			if(loginBean.password!=null){
				return true;
			}else{
				Messagebox.show("Password required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("User id required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
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

	public ArrayList<String> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(ArrayList<String> departmentList) {
		this.departmentList = departmentList;
	}

	public Boolean getRowVisibility() {
		return rowVisibility;
	}

	public void setRowVisibility(Boolean rowVisibility) {
		this.rowVisibility = rowVisibility;
	}

	public String getGridHeight() {
		return gridHeight;
	}

	public void setGridHeight(String gridHeight) {
		this.gridHeight = gridHeight;
	}


	/*public Map<String, LoginBean> getLoginMap() {
		return loginMap;
	}

	public void setLoginMap(Map<String, LoginBean> loginMap) {
		this.loginMap = loginMap;
	}*/
	
	
}
