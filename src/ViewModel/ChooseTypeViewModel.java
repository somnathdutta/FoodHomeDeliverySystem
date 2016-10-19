package ViewModel;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

public class ChooseTypeViewModel {

	private String type;
	
	Session session = null;
	
	private Connection connection = null;
	
	private String username;
	
	private String newdayName ="";
	
	private Integer roleId;
	
	@Wire("#winChooseType")
	private Window winChooseType;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view
			,@ExecutionArgParam("day")String dayname) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		username = (String) session.getAttribute("login");
		roleId = (Integer) session.getAttribute("userRoleId");
		connection.setAutoCommit(true);
		newdayName = dayname;
	}

	@Command
	@NotifyChange("*")
	public void onClicksave(){
		if(isValidateField()){
			String dayWithType = newdayName+"$"+type;
			Map<String, Object> dayData = new HashMap<String, Object>();
			dayData.put("day", dayWithType);
			BindUtils.postGlobalCommand(null, null, "onOkData", dayData);
			winChooseType.detach();
		}
		
	}
	
	private Boolean isValidateField(){
		if(type!=null){
			return true;
		}else{
			Messagebox.show("Meal type required!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
			return false;
		}
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}
