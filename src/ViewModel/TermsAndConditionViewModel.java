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
import Bean.TermsAndConditionBean;

public class TermsAndConditionViewModel {

	TermsAndConditionBean termsAndConditionBean = new TermsAndConditionBean();
	ArrayList<TermsAndConditionBean> termsAndConditionBeanList = new ArrayList<TermsAndConditionBean>();
	
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
		System.out.println("zul page >> cmsaboutus.zul");
	}
	
	
	public void onload(){
		termsAndConditionBeanList = CmsMasterService.loadTaC(connection);
		if(termsAndConditionBeanList.size()>0){
			termsAndConditionBean.setInsertDivVis(false);
		}else {
			termsAndConditionBean.setInsertDivVis(true);
		}
	}

	@Command
	@NotifyChange("*")
	public void onClickTandc(){
		if(termsAndConditionBean.getTermsandCondition() != null){
			int i = 0;
			i = CmsMasterService.insertTaC(connection, termsAndConditionBean, userName);
			if(i>0){
				onload();
				termsAndConditionBean.setTermsandCondition(null);
				Messagebox.show("Saved Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
			}
		}
	}
	
	@Command
	@NotifyChange("*")
	public void updateTandc(@BindingParam("bean") TermsAndConditionBean bean){
		int i = 0;
		if(bean.getTermsandCondition() !=null && bean.getTermsandCondition().trim().length()>0){
		  i = CmsMasterService.updateTaC(connection, bean, userName);
		}else {
			Messagebox.show("Enter T&C Text", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
		if(i>0){
			onload();
			Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void deleteTandc(@BindingParam("bean") TermsAndConditionBean bean){
		int i = 0;
		i = CmsMasterService.deleteTaC(connection, bean, userName);
		if(i>0){
			onload();
			Messagebox.show("Deleted Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	
	
	public TermsAndConditionBean getTermsAndConditionBean() {
		return termsAndConditionBean;
	}

	public void setTermsAndConditionBean(TermsAndConditionBean termsAndConditionBean) {
		this.termsAndConditionBean = termsAndConditionBean;
	}

	public ArrayList<TermsAndConditionBean> getTermsAndConditionBeanList() {
		return termsAndConditionBeanList;
	}

	public void setTermsAndConditionBeanList(
			ArrayList<TermsAndConditionBean> termsAndConditionBeanList) {
		this.termsAndConditionBeanList = termsAndConditionBeanList;
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
