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
import dao.CmsMasterDao;
import Bean.AboutUsBean;

public class AboutUsViewModel {

	AboutUsBean aboutUsBean = new AboutUsBean();
	ArrayList<AboutUsBean> aboutUsBeanList = new ArrayList<AboutUsBean>();
	
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
		aboutUsBeanList = CmsMasterService.loadAboutUs(connection);
		if(aboutUsBeanList.size()>0){
			aboutUsBean.setInsertDivVis(false);
		}else {
			aboutUsBean.setInsertDivVis(true);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void insertAboutUs(){
		int i = 0;
		if(aboutUsBean.getAboutUs() != null){
		i = CmsMasterDao.insertAboutUs(connection, aboutUsBean, userName);
		}
		if(i>0){
			Messagebox.show("Saved Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void updateAboutUs(@BindingParam("bean") AboutUsBean bean){
		int i = 0;
		i = CmsMasterService.updateAboutUs(connection, aboutUsBean, userName);
		if(i>0){
			onload();
			Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void deleteAboutUs(@BindingParam("bean") AboutUsBean bean){
		int i = 0;
		i = CmsMasterService.deleteAboutUs(connection, aboutUsBean, userName);
		if(i>0){
			onload();
			Messagebox.show("Deleted Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
		
	}
	
	
	
	
	public AboutUsBean getAboutUsBean() {
		return aboutUsBean;
	}

	public void setAboutUsBean(AboutUsBean aboutUsBean) {
		this.aboutUsBean = aboutUsBean;
	}

	public ArrayList<AboutUsBean> getAboutUsBeanList() {
		return aboutUsBeanList;
	}

	public void setAboutUsBeanList(ArrayList<AboutUsBean> aboutUsBeanList) {
		this.aboutUsBeanList = aboutUsBeanList;
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
