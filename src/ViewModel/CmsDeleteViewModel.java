package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

import Bean.ManageCMSBean;

public class CmsDeleteViewModel {
	
	private ManageCMSBean manageCMSBEAN = null;
	
	private Connection connection = null;
	
	private Session sessions = null;
	
	private String userName ;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@Wire("#winCMSDelete")
	private Window winCMSDelete;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("parentCMSObject") ManageCMSBean manageCMSbean)
			throws Exception {

		Selectors.wireComponents(view, this, false);
		
		sessions = Sessions.getCurrent();
		
		connection = (Connection) sessions.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
		userName = (String) sessions.getAttribute("login");
		
		manageCMSBEAN = manageCMSbean;
			
	}
	
	@Command
	@NotifyChange("*")
	public void onClickYes(){
		
		Integer noOfUpdate = 0;
	//	saveCMSPageData();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					try {
						
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("deleteCmsSql"));
				
						preparedStatement.setInt(1, manageCMSBEAN.pageId);
						
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
				
				Messagebox.show("CMS Deleted Successfully...");
				
				BindUtils.postGlobalCommand(null, null, "globalReload", null);
				winCMSDelete.detach();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickNo(){
		
		BindUtils.postGlobalCommand(null, null, "globalReload", null);
		winCMSDelete.detach();
	}

	
	public void saveCMSPageData(){
		Boolean inserted =false;
	
		try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						
					try {
							preparedStatement = connection.prepareStatement("select deleteddataschema.func_save_cms(?,?,?,?,?,?,?,?,?)");
							preparedStatement.setInt(1, manageCMSBEAN.areaId);
							preparedStatement.setString(2, manageCMSBEAN.pageTitle);
							preparedStatement.setString(3, manageCMSBEAN.pageContent);
							preparedStatement.setString(4, manageCMSBEAN.pageBanner);
							preparedStatement.setString(5, manageCMSBEAN.pageBannerPicturePath);
							if(manageCMSBEAN.showInAppChecked){
								preparedStatement.setString(6, "Y");
							}
							else{
								preparedStatement.setString(6, "N");
							}
							preparedStatement.setString(7, userName);
							preparedStatement.setString(8, userName);
							preparedStatement.setInt(9, manageCMSBEAN.pageId);								
							resultSet = preparedStatement.executeQuery();
							if (resultSet.next()) {
								inserted=true;
							}
							if (inserted) {
								Messagebox.show("CMS data deleted Successfully...");	
							}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
					
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public ManageCMSBean getManageCMSBean() {
		return manageCMSBEAN;
	}

	public void setManageCMSBean(ManageCMSBean manageCMSBean) {
		this.manageCMSBEAN = manageCMSBean;
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

	public Window getWinCMSDelete() {
		return winCMSDelete;
	}

	public void setWinCMSDelete(Window winCMSDelete) {
		this.winCMSDelete = winCMSDelete;
	}
}
