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

import Bean.ManageCouponsBean;
import Bean.ManageDeliveryBoyBean;

public class SuspendBoyViewModel {
	
	private ManageDeliveryBoyBean managedeliveryboyBEAN = null;
	
	private Connection connection = null;
	
	private Session sessions = null;
	
	private String userName ;
	
	@Wire("#winSuspendBoy")
	private Window winSuspendBoy;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("parentCMSObject") ManageDeliveryBoyBean managedeliveryboybean)
			throws Exception {

		Selectors.wireComponents(view, this, false);
		
		sessions = Sessions.getCurrent();
		
		connection = (Connection) sessions.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
		userName = (String) sessions.getAttribute("login");
		
		managedeliveryboyBEAN = managedeliveryboybean;
			
	}
	
	@Command
	@NotifyChange("*")
	public void onClickYes(){
		
		if(managedeliveryboyBEAN.boyStatus.equals("Active")){
			if(managedeliveryboyBEAN.orderAssigned.equals("N")){
				try {
					SQL:{	
							PreparedStatement preparedStatement = null;
							String sql = "UPDATE fapp_delivery_boy SET delivery_boy_status_id=3 WHERE delivery_boy_id=? ";
						try {
								preparedStatement = connection.prepareStatement(sql);
								preparedStatement.setInt(1, managedeliveryboyBEAN.deliveryBoyId);
								int updaterow = preparedStatement.executeUpdate();
								if(updaterow>0){
									Messagebox.show("Delivery boy suspended!");
									managedeliveryboyBEAN.activeVisibility = true;
									managedeliveryboyBEAN.suspendVisibility = false;
									BindUtils.postGlobalCommand(null, null, "globalReload", null);
									winSuspendBoy.detach();
								}
								
						} catch (Exception e) {
							Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
							e.printStackTrace();
						}finally{
							if(preparedStatement!=null){
								preparedStatement.close();
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}else{
				Messagebox.show("Delivery boy assigned to some order,can not be suspended now!","SUSPENSION FAILED",Messagebox.OK,Messagebox.EXCLAMATION);
				winSuspendBoy.detach();
			}	
		}else{
			Messagebox.show("Delivery boy already suspended!","SUSPENSION FAILED",Messagebox.OK,Messagebox.EXCLAMATION);
			winSuspendBoy.detach();
		}
		
	
				/*BindUtils.postGlobalCommand(null, null, "globalReload", null);
				winSuspendBoy.detach();*/
			
			
	}
	
	@Command
	@NotifyChange("*")
	public void onClickNo(){
		BindUtils.postGlobalCommand(null, null, "globalReload", null);
		winSuspendBoy.detach();
	}

	public ManageDeliveryBoyBean getManagedeliveryboyBEAN() {
		return managedeliveryboyBEAN;
	}

	public void setManagedeliveryboyBEAN(ManageDeliveryBoyBean managedeliveryboyBEAN) {
		this.managedeliveryboyBEAN = managedeliveryboyBEAN;
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

	public Window getWinSuspendBoy() {
		return winSuspendBoy;
	}

	public void setWinSuspendBoy(Window winSuspendBoy) {
		this.winSuspendBoy = winSuspendBoy;
	}

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}

}
