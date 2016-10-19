package ViewModel;

import java.sql.Connection;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;

public class OrderAcceptanceViewModel {

	Session session = null;
	
	private Connection connection = null;
	
	private String username = "";
	
	private Integer roleId = 0;

	private Boolean averageDivVisibility = false;
	
	private Boolean followUpDivVisibility = false;
	
	private Boolean averageRadio = false;
	
	private Boolean followupRadio = false;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		username = (String) session.getAttribute("login");
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		connection.setAutoCommit(true);
		
	}

	@Command
	@NotifyChange("*")
	public void onCheckFirstRadio(){
		if(followupRadio){
			followupRadio = false;
		}
		System.out.println("When first checked first-"+averageRadio+" second-"+followupRadio);
		if(averageRadio){
			averageDivVisibility = true;
			followUpDivVisibility= false;
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onCheckSecondRadio(){
		if(averageRadio){
			averageRadio = false;
		}
		System.out.println("When second checked first-"+averageRadio+" second-"+followupRadio);
		if(followupRadio){
			followUpDivVisibility= true;
			averageDivVisibility = false;
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

	public Boolean getAverageDivVisibility() {
		return averageDivVisibility;
	}

	public void setAverageDivVisibility(Boolean averageDivVisibility) {
		this.averageDivVisibility = averageDivVisibility;
	}

	public Boolean getFollowUpDivVisibility() {
		return followUpDivVisibility;
	}

	public void setFollowUpDivVisibility(Boolean followUpDivVisibility) {
		this.followUpDivVisibility = followUpDivVisibility;
	}

	public void setAverageRadio(Boolean averageRadio) {
		this.averageRadio = averageRadio;
	}

	public void setFollowupRadio(Boolean followupRadio) {
		this.followupRadio = followupRadio;
	}

	public Boolean getAverageRadio() {
		return averageRadio;
	}

	public Boolean getFollowupRadio() {
		return followupRadio;
	}
}
