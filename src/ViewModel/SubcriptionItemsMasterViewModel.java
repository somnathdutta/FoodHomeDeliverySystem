package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;

import dao.SubcriptionItemsMasterDAO;
import Bean.SubcriptionItemsMasterBean;

public class SubcriptionItemsMasterViewModel {
	
	private SubcriptionItemsMasterBean subcriptionItemsMasterBean = new SubcriptionItemsMasterBean();
	
	
	private ArrayList<SubcriptionItemsMasterBean> subcriptionItemsMasterBeanList = new ArrayList<SubcriptionItemsMasterBean>();
	
	
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
		
		subcriptionItemsMasterBeanList = SubcriptionItemsMasterDAO.loadSubItems(connection);
		
		System.out.println("zul page >> subcriptionItemsMaster.zul");
	}

	
	@Command
	@NotifyChange("*")
	public void onClickSave(){
		int i =0;
		
		if(subcriptionItemsMasterBean.getItemName() != null && subcriptionItemsMasterBean.getItemName().trim().length()>0){
			if(subcriptionItemsMasterBean.getItemImage() != null && subcriptionItemsMasterBean.getItemImage().trim().length()>0){
			i = SubcriptionItemsMasterDAO.InsertSubItems(connection, subcriptionItemsMasterBean, userName);
			if(i>0){
				Messagebox.show("Saved Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
				subcriptionItemsMasterBean.setItemName(null);
				subcriptionItemsMasterBean.setItemImage(null);
				subcriptionItemsMasterBeanList = SubcriptionItemsMasterDAO.loadSubItems(connection);
			}
			}else {
				Messagebox.show("Enter Image Url", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				subcriptionItemsMasterBean.setItemImage(null);
				
			}
		}else {
			Messagebox.show("Enter Item Name", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
			subcriptionItemsMasterBean.setItemName(null);
			subcriptionItemsMasterBean.setItemImage(null);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdate(@BindingParam("bean") SubcriptionItemsMasterBean bean){
		
		String un = userName;
		int i =0;
		i = SubcriptionItemsMasterDAO.updateSubItems(connection, bean, un);
		if(i>0){
			Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
			subcriptionItemsMasterBeanList = SubcriptionItemsMasterDAO.loadSubItems(connection);
		}
		
	}
	
	
	
	public SubcriptionItemsMasterBean getSubcriptionItemsMasterBean() {
		return subcriptionItemsMasterBean;
	}

	public void setSubcriptionItemsMasterBean(
			SubcriptionItemsMasterBean subcriptionItemsMasterBean) {
		this.subcriptionItemsMasterBean = subcriptionItemsMasterBean;
	}

	public ArrayList<SubcriptionItemsMasterBean> getSubcriptionItemsMasterBeanList() {
		return subcriptionItemsMasterBeanList;
	}

	public void setSubcriptionItemsMasterBeanList(
			ArrayList<SubcriptionItemsMasterBean> subcriptionItemsMasterBeanList) {
		this.subcriptionItemsMasterBeanList = subcriptionItemsMasterBeanList;
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
