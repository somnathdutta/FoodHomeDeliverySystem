package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import service.SetMasterService;
import dao.SetDAO;
import Bean.CompletedOrderBean;
import Bean.ItemBean;
import Bean.ManageCategoryBean;
import Bean.SetBean;

public class SetViewModel {
	
	private SetBean setBean = new SetBean();
	private ManageCategoryBean categoryBean = new ManageCategoryBean();
	private ItemBean itemBean = new ItemBean();
	private SetBean setValueBean;
	
	private ArrayList<SetBean> existingItemSetList;
	private ArrayList<ItemBean> itemSetList = new ArrayList<ItemBean>();
	private HashSet<SetBean> setBeanWithItemList = new HashSet<SetBean>();
	private ArrayList<SetBean> setList;
	
	
	private ArrayList<ManageCategoryBean> categoryBeanList = new ArrayList<ManageCategoryBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		setList = SetMasterService.fetchSetList(connection);
		onLoadQuery();
	}
	
	public void onLoadQuery(){
		categoryBeanList = SetDAO.onLoadCategoryList(connection);
		
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectCategoryName(){
		System.out.println("Category Name " + categoryBean.categoryName);
		
		itemSetList = SetDAO.loadItemsFromCategory(connection, categoryBean.categoryId);
		
	}

	@Command
	@NotifyChange("*")
	public void onClickAddItems(){
		boolean added = false;
		for(ItemBean bean : itemSetList){
			if(bean.isChecked){
				setBean.getItemList().add(bean);
				added = true;
			}
		}
		if(added){
			itemSetList.clear();
			categoryBean.categoryName = null;
			categoryBeanList.clear();
			onLoadQuery();
			Messagebox.show("Items are added!","Added",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSaveSet(){
		
		Map<String, SetBean> parentMap =  new HashMap<String, SetBean>();
		
		parentMap.put("parentObject", setBean);
		if(setBean.getItemList().size() > 0){
			Window orderDetailswindow = (Window) Executions.createComponents("setDetails.zul", null, parentMap);
			
			orderDetailswindow.doModal();
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectSet(){
		existingItemSetList = SetMasterService.fetchExistingSetDetais(connection, setValueBean.getSetId());
		
		if(existingItemSetList.size()==0){
			Messagebox.show("No Item Added", "Alert", Messagebox.OK,Messagebox.EXCLAMATION);
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectStatus(){
		System.out.println("Status " + setBean.getItemBean().getStatus());
	}
	
	@GlobalCommand
	@NotifyChange("*")
	public void setDetailsGlobalUpdate(){
		
	setBean.setSetName(null);
	categoryBeanList.clear();
	categoryBean.categoryName= null;
	setBean.getItemBean().categoryName=null;
	categoryBeanList = SetDAO.onLoadCategoryList(connection);
	setList = SetMasterService.fetchSetList(connection);	
	}
	
	
	
	
	
	
	public ArrayList<ItemBean> getItemSetList() {
		return itemSetList;
	}

	public void setItemSetList(ArrayList<ItemBean> itemSetList) {
		this.itemSetList = itemSetList;
	}

	public ArrayList<ManageCategoryBean> getCategoryBeanList() {
		return categoryBeanList;
	}

	public void setCategoryBeanList(ArrayList<ManageCategoryBean> categoryBeanList) {
		this.categoryBeanList = categoryBeanList;
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

	public ManageCategoryBean getCategoryBean() {
		return categoryBean;
	}

	public void setCategoryBean(ManageCategoryBean categoryBean) {
		this.categoryBean = categoryBean;
	}

	public HashSet<SetBean> getSetBeanWithItemList() {
		return setBeanWithItemList;
	}

	public void setSetBeanWithItemList(HashSet<SetBean> setBeanWithItemList) {
		this.setBeanWithItemList = setBeanWithItemList;
	}

	public ArrayList<SetBean> getExistingItemSetList() {
		return existingItemSetList;
	}

	public void setExistingItemSetList(ArrayList<SetBean> existingItemSetList) {
		this.existingItemSetList = existingItemSetList;
	}

	public ArrayList<SetBean> getSetList() {
		return setList;
	}

	public void setSetList(ArrayList<SetBean> setList) {
		this.setList = setList;
	}

	public SetBean getSetValueBean() {
		return setValueBean;
	}

	public void setSetValueBean(SetBean setValueBean) {
		this.setValueBean = setValueBean;
	}
}
