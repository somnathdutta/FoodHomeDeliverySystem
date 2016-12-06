package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import dao.QueryDAO;
import dao.QueryManagementDAO;
import Bean.Query;

public class QueryManagementViewModel {

	private Query queryPojo = new Query();
	
	private ArrayList<Query> userQueryList = new ArrayList<Query>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName="";
	
	private Boolean saveButtonVisibility = true;
	
	private Boolean updateButtonVisibility = false;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		userName = (String) session.getAttribute("login");
		
		queryPojo.setUserName(userName);
		
		onLoadQuery();
	}
	
	public void onLoadQuery(){
		userQueryList = QueryManagementDAO.loadAllUsersMessages(connection);
	}
	
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		onLoadQuery();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSend(@BindingParam("bean")Query queryPojo){
		
		Messagebox.show("Are you sure to send this reply message to user having email id "+queryPojo.getUserEmailId()+" ?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
		        	if(QueryManagementDAO.generateAndSendEmail(queryPojo)){
		    			QueryManagementDAO.closeQuery(queryPojo, connection);
		    			onLoadQuery();
		    		}
		          BindUtils.postGlobalCommand(null, null, "globalReload", null);
				   //Messagebox.show("Tax data deleted successfully!");
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
		
		
		
		/*if(QueryManagementDAO.generateAndSendEmail(queryPojo)){
			QueryManagementDAO.closeQuery(queryPojo, connection);
			onLoadQuery();
		}*/
	}

	public Query getQueryPojo() {
		return queryPojo;
	}

	public void setQueryPojo(Query queryPojo) {
		this.queryPojo = queryPojo;
	}

	public ArrayList<Query> getUserQueryList() {
		return userQueryList;
	}

	public void setUserQueryList(ArrayList<Query> userQueryList) {
		this.userQueryList = userQueryList;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getSaveButtonVisibility() {
		return saveButtonVisibility;
	}

	public void setSaveButtonVisibility(Boolean saveButtonVisibility) {
		this.saveButtonVisibility = saveButtonVisibility;
	}

	public Boolean getUpdateButtonVisibility() {
		return updateButtonVisibility;
	}

	public void setUpdateButtonVisibility(Boolean updateButtonVisibility) {
		this.updateButtonVisibility = updateButtonVisibility;
	}
}
