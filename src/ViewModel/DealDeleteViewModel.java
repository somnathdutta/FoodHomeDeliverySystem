package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.zkoss.bind.BindUtils;
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

import Bean.ManageDealBean;

public class DealDeleteViewModel {

	private ManageDealBean manageDealBEAN = null;
	
	private Connection connection = null;
	
	private Session sessions = null;
	
	private String userName ;
	
	@Wire("#winDealDelete")
	private Window winDealDelete;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("parentDealObject") ManageDealBean manageDealbean)
			throws Exception {

		Selectors.wireComponents(view, this, false);
		
		sessions = Sessions.getCurrent();
		
		connection = (Connection) sessions.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
		userName = (String) sessions.getAttribute("login");
		
		manageDealBEAN = manageDealbean;
			
	}
	
	@Command
	@NotifyChange("*")
	public void onClickYes(){
		Integer noOfUpdate = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					try {
						
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("deleteDealSql"));
				
						preparedStatement.setInt(1, manageDealBEAN.dealId);
						
						noOfUpdate = preparedStatement.executeUpdate();
						
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
			}
		
			if(noOfUpdate>0){
				
				Messagebox.show("Deal data Deleted Successfully...");
				
				BindUtils.postGlobalCommand(null, null, "globalReload", null);
				winDealDelete.detach();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickNo(){
		
		BindUtils.postGlobalCommand(null, null, "globalReload", null);
		winDealDelete.detach();
	}

	public ManageDealBean getManageDealBEAN() {
		return manageDealBEAN;
	}

	public void setManageDealBEAN(ManageDealBean manageDealBEAN) {
		this.manageDealBEAN = manageDealBEAN;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Session getSessions() {
		return sessions;
	}

	public void setSessions(Session sessions) {
		this.sessions = sessions;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Window getWinDealDelete() {
		return winDealDelete;
	}

	public void setWinDealDelete(Window winDealDelete) {
		this.winDealDelete = winDealDelete;
	}
}
