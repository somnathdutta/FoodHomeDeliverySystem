package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import dao.SetDAO;
import Bean.ItemBean;
import Bean.ManageCategoryBean;
import Bean.SetBean;


public class SetUpdateViewModel {
	
	private SetBean setBean = new SetBean();
	private ManageCategoryBean categoryBean = new ManageCategoryBean();
	private ItemBean itemBean = new ItemBean();
	private SetBean setValueBean;
	
	
	private ArrayList<ManageCategoryBean> categoryBeanList;
	private ArrayList<SetBean> existingItemSetList;
	private ArrayList<ItemBean> itemSetList = new ArrayList<ItemBean>();
	private HashSet<SetBean> setBeanWithItemList = new HashSet<SetBean>();
	private ArrayList<SetBean> setList;
	
	
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
		
		setValueBean = setbean;
		categoryBeanList = SetDAO.onLoadCategoryList(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectCategory(){
		
		itemSetList = SetDAO.loadItemsFromCategory(connection, categoryBean.categoryId);
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickAddItemsUpdate(){
		boolean added = false;
		for(ItemBean bean : itemSetList){
			if(bean.isChecked){
				setValueBean.getItemList().add(bean);
				added = true;
			}
		}
		System.out.println("ADDED " + added);
		System.out.println("Size  " + setValueBean.getItemList().size());
		if(added){
			itemSetList.clear();
			categoryBean.categoryName = null;
			categoryBeanList.clear();
			categoryBeanList = SetDAO.onLoadCategoryList(connection);
			Messagebox.show("Items are added!","Added",Messagebox.OK,Messagebox.INFORMATION);
		}
	
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSaveSetUpdate(){

		
		Map<String, SetBean> parentMap =  new HashMap<String, SetBean>();
		
		parentMap.put("parentObject", setValueBean);
		if(setValueBean.getItemList().size() > 0){
			Window orderDetailswindow = (Window) Executions.createComponents("setDetails.zul", null, parentMap);
			
			orderDetailswindow.doModal();
		}
		
	
	}
	
	
	
	
	

	public SetBean getSetBean() {
		return setBean;
	}

	public void setSetBean(SetBean setBean) {
		this.setBean = setBean;
	}

	public ManageCategoryBean getCategoryBean() {
		return categoryBean;
	}

	public void setCategoryBean(ManageCategoryBean categoryBean) {
		this.categoryBean = categoryBean;
	}

	public ItemBean getItemBean() {
		return itemBean;
	}

	public void setItemBean(ItemBean itemBean) {
		this.itemBean = itemBean;
	}

	public SetBean getSetValueBean() {
		return setValueBean;
	}

	public void setSetValueBean(SetBean setValueBean) {
		this.setValueBean = setValueBean;
	}

	public ArrayList<ManageCategoryBean> getCategoryBeanList() {
		return categoryBeanList;
	}

	public void setCategoryBeanList(ArrayList<ManageCategoryBean> categoryBeanList) {
		this.categoryBeanList = categoryBeanList;
	}

	public ArrayList<SetBean> getExistingItemSetList() {
		return existingItemSetList;
	}

	public void setExistingItemSetList(ArrayList<SetBean> existingItemSetList) {
		this.existingItemSetList = existingItemSetList;
	}

	public ArrayList<ItemBean> getItemSetList() {
		return itemSetList;
	}

	public void setItemSetList(ArrayList<ItemBean> itemSetList) {
		this.itemSetList = itemSetList;
	}

	public HashSet<SetBean> getSetBeanWithItemList() {
		return setBeanWithItemList;
	}

	public void setSetBeanWithItemList(HashSet<SetBean> setBeanWithItemList) {
		this.setBeanWithItemList = setBeanWithItemList;
	}

	public ArrayList<SetBean> getSetList() {
		return setList;
	}

	public void setSetList(ArrayList<SetBean> setList) {
		this.setList = setList;
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
