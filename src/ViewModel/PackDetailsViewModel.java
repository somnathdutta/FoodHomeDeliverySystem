package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Window;

import dao.PackDetailsDAO;
import dao.SubscriptionSettingsDAO;
import Bean.DayBean;
import Bean.ItemBean;
import Bean.MealBean;

public class PackDetailsViewModel {

	int packId = 0;
	
	public ItemBean itemBean = new ItemBean(); 
	
	public ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
	
	public ItemBean allItemBean = new ItemBean(); 
	
	public ArrayList<ItemBean> allItemBeanList = new ArrayList<ItemBean>();
	
	public MealBean mealtype = new MealBean();
	public ArrayList<MealBean> mealTypeList = new ArrayList<MealBean>();
	
	public DayBean day = new DayBean();
	public ArrayList<DayBean> dayList = new ArrayList<DayBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("packId")ItemBean bean) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
		if(bean != null){
			packId = bean.packId;
		}
		itemBeanList = PackDetailsDAO.loadSavedPacks(connection, packId); 
		for(ItemBean itemBean : itemBeanList)
			itemBean.packId = packId;
		//allItemBeanList = PackDetailsDAO.loadExistingItems(connection);
	}

	
	@Command
	@NotifyChange("*")
	public void onClickRemove(@BindingParam("bean")ItemBean bean){
		System.out.println("Pack detal id : "+bean.packDetailsId);
		System.out.println(" meal id:"+bean.meal.mealTypeId);
		System.out.println(" day : "+bean.day.getDayId());
		System.out.println(" pack id: "+bean.packId);
		PackDetailsDAO.removeItem(connection, bean);
		BindUtils.postGlobalCommand(null, null, "globalReload", null);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdate(@BindingParam("bean")ItemBean bean){
		bean.packId = packId;
		PackDetailsDAO.updateItem(connection,  bean);
		BindUtils.postGlobalCommand(null, null, "globalReload", null);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickAddNewItem(){
		Map<String, Integer> parentMap = new HashMap<String, Integer>();
		parentMap.put("packId", packId);
		Window window = (Window) Executions.createComponents("addnewitem.zul", null, parentMap);
		window.doModal();
	}
	

	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		itemBeanList = PackDetailsDAO.loadSavedPacks(connection, packId); 
	}
	
	public int getPackId() {
		return packId;
	}

	public void setPackId(int packId) {
		this.packId = packId;
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

	public ItemBean getAllItemBean() {
		return allItemBean;
	}

	public void setAllItemBean(ItemBean allItemBean) {
		this.allItemBean = allItemBean;
	}

	public ArrayList<ItemBean> getAllItemBeanList() {
		return allItemBeanList;
	}

	public void setAllItemBeanList(ArrayList<ItemBean> allItemBeanList) {
		this.allItemBeanList = allItemBeanList;
	}

	public MealBean getMealtype() {
		return mealtype;
	}

	public void setMealtype(MealBean mealtype) {
		this.mealtype = mealtype;
	}

	public ArrayList<MealBean> getMealTypeList() {
		return mealTypeList;
	}

	public void setMealTypeList(ArrayList<MealBean> mealTypeList) {
		this.mealTypeList = mealTypeList;
	}

	public DayBean getDay() {
		return day;
	}

	public void setDay(DayBean day) {
		this.day = day;
	}

	public ArrayList<DayBean> getDayList() {
		return dayList;
	}

	public void setDayList(ArrayList<DayBean> dayList) {
		this.dayList = dayList;
	}
}
