package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import service.SetMasterService;
import Bean.SetBean;

public class ItemSetViewModel {
	
Session session = null;
	
	private Connection connection = null;
	private String userName;
	
	private SetBean setTypeBean = new SetBean();
	private SetBean dayBean = new SetBean();
	private SetBean setBean = new SetBean();
	private SetBean itemCodeBean = new SetBean();
	
	private ArrayList<SetBean> dayTypeList;
	private ArrayList<SetBean> setList;
	private ArrayList<SetBean> itemCodeList;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		setList = SetMasterService.fetchSetList(connection);
	}

	@Command
	@NotifyChange("*")
	public void onSelectSet(){
		dayBean.setDayType(null);
		dayTypeList = SetMasterService.fetchDayTypeList(connection);
		
		
	}

	@Command
	@NotifyChange("*")
	public void onSelectDayType(){
		itemCodeList = SetMasterService.fetchExistingSetDetais(connection, setBean.getSetId());
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickApply(){
		int i = 0;
		if(dayBean.getDayTypeId() != null && setBean.getSetId() != null){
		i = SetMasterService.applyDayTypeUpdate(connection, dayBean.getDayTypeId(), itemCodeList, setBean.getSetId());
		}else {
			Messagebox.show("Select Set And Day Type", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
		if(i >0){
			Messagebox.show("Saved Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public SetBean getSetTypeBean() {
		return setTypeBean;
	}

	public void setSetTypeBean(SetBean setTypeBean) {
		this.setTypeBean = setTypeBean;
	}

	public SetBean getDayBean() {
		return dayBean;
	}

	public void setDayBean(SetBean dayBean) {
		this.dayBean = dayBean;
	}

	public SetBean getSetBean() {
		return setBean;
	}

	public void setSetBean(SetBean setBean) {
		this.setBean = setBean;
	}

	public ArrayList<SetBean> getDayTypeList() {
		return dayTypeList;
	}

	public void setDayTypeList(ArrayList<SetBean> dayTypeList) {
		this.dayTypeList = dayTypeList;
	}

	public ArrayList<SetBean> getSetList() {
		return setList;
	}

	public void setSetList(ArrayList<SetBean> setList) {
		this.setList = setList;
	}

	public SetBean getItemCodeBean() {
		return itemCodeBean;
	}

	public void setItemCodeBean(SetBean itemCodeBean) {
		this.itemCodeBean = itemCodeBean;
	}

	public ArrayList<SetBean> getItemCodeList() {
		return itemCodeList;
	}

	public void setItemCodeList(ArrayList<SetBean> itemCodeList) {
		this.itemCodeList = itemCodeList;
	}

	
			
	
}
