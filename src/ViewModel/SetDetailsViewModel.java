package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;

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
import org.zkoss.zul.Messagebox;

import dao.SetDetailsSaveDao;
import Bean.ItemBean;
import Bean.SetBean;

public class SetDetailsViewModel {

	private SetBean setBean = null;
	
	private ItemBean itemBean = new ItemBean();
	
	private ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("parentObject")SetBean setbean) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		setBean = setbean;
		
		
		for(ItemBean bean : setBean.getItemList()){
			itemBeanList.add(bean);
		}
			
	}
	
	@Command
	@NotifyChange("*")
	public void onclickFinalSave(){
		int i = SetDetailsSaveDao.setDetailsSave(connection, setBean.getSetName(), userName, setBean);
		if(i>0){
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

	public SetBean getSetBean() {
		return setBean;
	}

	public void setSetBean(SetBean setBean) {
		this.setBean = setBean;
	}

	public ItemBean getItemBean() {
		return itemBean;
	}

	public void setItemBean(ItemBean itemBean) {
		this.itemBean = itemBean;
	}

	public ArrayList<ItemBean> getItemBeanList() {
		return itemBeanList;
	}

	public void setItemBeanList(ArrayList<ItemBean> itemBeanList) {
		this.itemBeanList = itemBeanList;
	}
	
	
}
