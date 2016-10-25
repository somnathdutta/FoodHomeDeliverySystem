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
import Bean.ShareAndEarnBean;


public class ShareAndEarnViewModel {
	
	ShareAndEarnBean shareAndEarnBean = new ShareAndEarnBean();
	
	ArrayList<ShareAndEarnBean> shareAndEarnBeanList = new ArrayList<ShareAndEarnBean>();
	
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
		System.out.println("zul page >> cmsshare.zul");
	}

	public void onload(){
		
		shareAndEarnBeanList = CmsMasterService.loadShareAndEarndetails(connection);
		if(shareAndEarnBeanList.size()>0){
			shareAndEarnBean.setInsertDivVis(false);
		}else {
			shareAndEarnBean.setInsertDivVis(true);
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onclickShareAndEarn(){
		int i = 0;
		if(CmsMasterService.shareAndEarnValidation(shareAndEarnBean)){
			i = CmsMasterService.insertShareAndEarnDetails(connection, shareAndEarnBean, userName);
			if(i>0){
				clear();
				onload();
				Messagebox.show("Saved Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
			}
		}
	}

	@Command
	@NotifyChange("*")
	public void onClickUpdate(@BindingParam("bean") ShareAndEarnBean bean){
		int i = 0;
		
			i = CmsMasterService.updateShareAndEarnDetails(connection, bean, userName);
			if(i>0){
				onload();
				Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
			}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean") ShareAndEarnBean bean){
		int i = 0;
			
			i = CmsMasterService.deleteShareAndEarnDetails(connection, bean, userName);
			if(i>0){
				onload();
				Messagebox.show("Deleted Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
			}
		
		
	}
	
	public void clear(){
		shareAndEarnBean.setAmnt(null);
		shareAndEarnBean.setAppMsg(null);
		shareAndEarnBean.setInvtMsg(null);
		shareAndEarnBean.setImgUrl(null);
	}
	
	
	public ShareAndEarnBean getShareAndEarnBean() {
		return shareAndEarnBean;
	}

	public void setShareAndEarnBean(ShareAndEarnBean shareAndEarnBean) {
		this.shareAndEarnBean = shareAndEarnBean;
	}

	public ArrayList<ShareAndEarnBean> getShareAndEarnBeanList() {
		return shareAndEarnBeanList;
	}

	public void setShareAndEarnBeanList(
			ArrayList<ShareAndEarnBean> shareAndEarnBeanList) {
		this.shareAndEarnBeanList = shareAndEarnBeanList;
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
