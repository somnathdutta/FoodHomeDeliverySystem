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
import org.zkoss.zul.Messagebox;

import service.CmsMasterService;
import Bean.PrivacyPolicyBean;

public class PrivecyPolicyViewModel {
	
	PrivacyPolicyBean privacyPolicyBean = new PrivacyPolicyBean();
	ArrayList<PrivacyPolicyBean> privacyPolicyBeanList = new ArrayList<PrivacyPolicyBean>();
	
	Session session = null;
	private Connection connection = null;
	private String userName;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		onload();
		System.out.println("zul page >> cmsprivecypolicy.zul");
	}
	
	public void onload(){
		privacyPolicyBeanList = CmsMasterService.loadprivacyPolicy(connection);
		if(privacyPolicyBeanList.size()>0){
			privacyPolicyBean.setInsertDivVis(false);
		}else {
			privacyPolicyBean.setInsertDivVis(true);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickPivacyPolicy(){

		if(privacyPolicyBean.getPrivacyPolicy() != null){
			int i = 0;
			i = CmsMasterService.insertPrivacyPolicy(connection, privacyPolicyBean, userName);
			if(i>0){
				privacyPolicyBean.setPrivacyPolicy(null);
				onload();
				Messagebox.show("Saved Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
			}
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onclcikUpdate(@BindingParam("bean") PrivacyPolicyBean bean){
		int i = 0;
		i = CmsMasterService.updatePrivacyPolicy(connection, bean, userName);
		if(i>0){
			onload();
			Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onclcikDelete(@BindingParam("bean") PrivacyPolicyBean bean){
		int i = 0;
		i = CmsMasterService.deletePrivacyPolicy(connection, bean, userName);
		if(i>0){
			onload();
			Messagebox.show("Deleted Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}

	public PrivacyPolicyBean getPrivacyPolicyBean() {
		return privacyPolicyBean;
	}

	public void setPrivacyPolicyBean(PrivacyPolicyBean privacyPolicyBean) {
		this.privacyPolicyBean = privacyPolicyBean;
	}

	public ArrayList<PrivacyPolicyBean> getPrivacyPolicyBeanList() {
		return privacyPolicyBeanList;
	}

	public void setPrivacyPolicyBeanList(
			ArrayList<PrivacyPolicyBean> privacyPolicyBeanList) {
		this.privacyPolicyBeanList = privacyPolicyBeanList;
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
	
	

}
