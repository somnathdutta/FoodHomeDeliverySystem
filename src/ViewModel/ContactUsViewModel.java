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

import Bean.ContactUsBean;
import service.CmsMasterService;

public class ContactUsViewModel {

	ContactUsBean contactUsBean = new ContactUsBean();
	
	private ArrayList<ContactUsBean> contactUsBeanList = new ArrayList<ContactUsBean>();
	
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
		System.out.println("zul page >> cmscontactus.zul");
	}
	
	public void onload(){
		contactUsBeanList = CmsMasterService.loadContactUs(connection);
		if(contactUsBeanList.size()>0){
			contactUsBean.setInsertDivVis(false);
		}else {
			contactUsBean.setInsertDivVis(true);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void insertContactUs(){
		int i = 0;
		if(contactUsBean.getContNo() != null){
		i = CmsMasterService.insertContactUs(connection, contactUsBean, userName);
		if(i>0){
			onload();
			contactUsBean.setContNo(null);
			Messagebox.show("Saved Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
		}else {
			Messagebox.show("Enter Contact No!", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
		
	}

	@Command
	@NotifyChange("*")
	public void updateContactUs(@BindingParam("bean") ContactUsBean bean){
		int i = 0;
		if(bean.getContNo() != null && bean.getContNo().trim().length()>0){
			i = CmsMasterService.updateContactUs(connection, bean, userName);
		}else {
			Messagebox.show("Enter Valid Contact No", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
		if(i>0){
			onload();
			Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void deleteContactUs(@BindingParam("bean") ContactUsBean bean){
		int i = 0;
		i = CmsMasterService.deleteContactUs(connection, bean, userName);
		if(i>0){
			onload();
			Messagebox.show("Deleted Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	
	
	public ContactUsBean getContactUsBean() {
		return contactUsBean;
	}

	public void setContactUsBean(ContactUsBean contactUsBean) {
		this.contactUsBean = contactUsBean;
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

	public ArrayList<ContactUsBean> getContactUsBeanList() {
		return contactUsBeanList;
	}

	public void setContactUsBeanList(ArrayList<ContactUsBean> contactUsBeanList) {
		this.contactUsBeanList = contactUsBeanList;
	}
	
}
