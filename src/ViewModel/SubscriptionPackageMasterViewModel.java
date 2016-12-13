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

import dao.SubscriptionpackageMasterDAO;
import Bean.SubscriptionpackageMasterBean;

public class SubscriptionPackageMasterViewModel {



	private SubscriptionpackageMasterBean subscriptionpackageMasterBean = new SubscriptionpackageMasterBean();
	private SubscriptionpackageMasterBean mealTypeMasterBean = new SubscriptionpackageMasterBean();
	
	
	
	
	private ArrayList<SubscriptionpackageMasterBean> mealTypeMasterBeanList = new ArrayList<SubscriptionpackageMasterBean>();
	private ArrayList<SubscriptionpackageMasterBean> subscriptionpackageMasterBeanList = new ArrayList<SubscriptionpackageMasterBean>(); 
			
	
	
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
		
		subscriptionpackageMasterBeanList = SubscriptionpackageMasterDAO.loadpackagemaster(connection);
		
		mealTypeMasterBeanList = SubscriptionpackageMasterDAO.loadMealType(connection);
		
		System.out.println("zul page >> subscriptionpackageMaster.zul");
	}

	
	@Command
	@NotifyChange("*")
	public void onClickSavePackage(){
		int i =0;
		if(subscriptionpackageMasterBean.getPackageName() != null && subscriptionpackageMasterBean.getPackageName().trim().length() >0){
			if(subscriptionpackageMasterBean.getNoOfDays() != null){
				if(subscriptionpackageMasterBean.getButtonName() != null && subscriptionpackageMasterBean.getButtonName().trim().length() >0){
					if(mealTypeMasterBean.getMealTypeId() != null){
					
					i = SubscriptionpackageMasterDAO.insertPackageMaster(connection, userName, subscriptionpackageMasterBean);
					if(i>0){
						subscriptionpackageMasterBean.setPackageId(i);
						System.out.println("IIIIIIIIIIII >>> >> > " + subscriptionpackageMasterBean.getPackageId());
						subscriptionpackageMasterBean.setPackageName(null);
						subscriptionpackageMasterBean.setNoOfDays(null);
						subscriptionpackageMasterBean.setButtonName(null);
						subscriptionpackageMasterBeanList = SubscriptionpackageMasterDAO.loadpackagemaster(connection);
					}
				  }else {
					  Messagebox.show("Select Meal Type", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				}
				}else {
					Messagebox.show("Enter Button Name", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				}
			}else {
				Messagebox.show("Enter no of days", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		}else {
			Messagebox.show("Enter Package Name", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickPckgUpdate(@BindingParam("bean") SubscriptionpackageMasterBean bean){
		int i =0;
		String un = userName;
		i = SubscriptionpackageMasterDAO.updatePackageMaster(connection, un, bean);
		if(i>0){
			subscriptionpackageMasterBeanList = SubscriptionpackageMasterDAO.loadpackagemaster(connection);
			Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
		
		
	}
	
	
	
	
	
	public SubscriptionpackageMasterBean getSubscriptionpackageMasterBean() {
		return subscriptionpackageMasterBean;
	}

	public void setSubscriptionpackageMasterBean(
			SubscriptionpackageMasterBean subscriptionpackageMasterBean) {
		this.subscriptionpackageMasterBean = subscriptionpackageMasterBean;
	}

	public ArrayList<SubscriptionpackageMasterBean> getSubscriptionpackageMasterBeanList() {
		return subscriptionpackageMasterBeanList;
	}

	public void setSubscriptionpackageMasterBeanList(
			ArrayList<SubscriptionpackageMasterBean> subscriptionpackageMasterBeanList) {
		this.subscriptionpackageMasterBeanList = subscriptionpackageMasterBeanList;
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


	public SubscriptionpackageMasterBean getMealTypeMasterBean() {
		return mealTypeMasterBean;
	}


	public void setMealTypeMasterBean(
			SubscriptionpackageMasterBean mealTypeMasterBean) {
		this.mealTypeMasterBean = mealTypeMasterBean;
	}


	public ArrayList<SubscriptionpackageMasterBean> getMealTypeMasterBeanList() {
		return mealTypeMasterBeanList;
	}


	public void setMealTypeMasterBeanList(
			ArrayList<SubscriptionpackageMasterBean> mealTypeMasterBeanList) {
		this.mealTypeMasterBeanList = mealTypeMasterBeanList;
	}

}
