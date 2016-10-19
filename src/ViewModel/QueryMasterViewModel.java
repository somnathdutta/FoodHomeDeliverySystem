package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;

import dao.QueryDAO;
import Bean.Query;

public class QueryMasterViewModel {

	private Query queryPojo = new Query();
	
	private ArrayList<Query> queryTypeList = new ArrayList<Query>();
	
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
		queryTypeList = QueryDAO.showAllMasterQueryTypes(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSaveQueryType(){
		saveQuery();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdateQueryType(){
		updateQuery();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickEditQueryType(@BindingParam("bean") Query queryPOJO){
		queryPojo.setQueryID(queryPOJO.getQueryID());
		queryPojo.setQueryName(queryPOJO.getQueryName());
		queryPojo.setStatus(queryPOJO.getStatus());
		updateButtonVisibility = true;
		saveButtonVisibility = false;
	}
	
	public void saveQuery(){
		queryPojo.setUserName(userName);
		if(QueryDAO.isValid(queryPojo)){
			QueryDAO.addNewQuery(connection, queryPojo);
			clearData();
			onLoadQuery();
		}
		
	}
	
	public void updateQuery(){
		queryPojo.setUserName(userName);
		if(QueryDAO.isValid(queryPojo)){
			QueryDAO.updateQuery(connection, queryPojo);
			updateButtonVisibility = false;
			saveButtonVisibility = true;
			clearData();
			onLoadQuery();
		}
	}
	
	public void deleteQuery(){
		queryPojo.setUserName(userName);
		QueryDAO.deleteQuery(connection, queryPojo);
	}

	
	public void clearData(){
		queryPojo.setQueryName(null);
		queryPojo.setQueryID(0);
		queryPojo.setStatus(null);
		queryPojo.setUserName(null);
	}
	
	public Query getQueryPojo() {
		return queryPojo;
	}

	public void setQueryPojo(Query queryPojo) {
		this.queryPojo = queryPojo;
	}

	public ArrayList<Query> getQueryTypeList() {
		return queryTypeList;
	}

	public void setQueryTypeList(ArrayList<Query> queryTypeList) {
		this.queryTypeList = queryTypeList;
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
