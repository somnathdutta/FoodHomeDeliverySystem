package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

import dao.AddNewItemDAO;
import dao.SubscriptionSettingsDAO;
import Bean.DayBean;
import Bean.Flavour;
import Bean.ItemBean;
import Bean.ManageCategoryBean;
import Bean.ManageCuisinBean;
import Bean.MealBean;

public class AddNewItemViewModel {

	public DayBean day = new DayBean();
	public ArrayList<DayBean> dayList = new ArrayList<DayBean>();
	
	public ManageCuisinBean cuisine = new ManageCuisinBean();	
	public ArrayList<ManageCuisinBean> cuisineList = new ArrayList<ManageCuisinBean>();
	
	public ManageCategoryBean category = new ManageCategoryBean();	
	public ArrayList<ManageCategoryBean> categoryList = new ArrayList<ManageCategoryBean>();
	
	public ItemBean item = new ItemBean();
	public ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();
	
	public ItemBean addedItem = new ItemBean();
	public LinkedHashSet<ItemBean> addedItemList = new LinkedHashSet<ItemBean>();
	
	public MealBean mealtype = new MealBean();
	public ArrayList<MealBean> mealTypeList = new ArrayList<MealBean>();
	
	int packID = 0;
		
	Session session = null;
	
	private Connection connection = null;
	
	@Wire("#addNew")
	Window addNew;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("packId")Integer packId) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
		if(packId != null){
			packID = packId;
			System.out.println("PAck ID in Add new Item : "+packID);
		}
		
		loadInitials();
	}
	
	public void loadInitials(){
		dayList = SubscriptionSettingsDAO.loadDayList(connection);
		
	}

	
	@Command
	@NotifyChange("*")
	public void onSelectDay(){
		cuisine.cuisinName = null;
		category.categoryName = null;
		itemList.clear();
		cuisineList = SubscriptionSettingsDAO.loadCuisineList(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectCuisine(){
		System.out.println(cuisine.cuisinId);
		category.categoryName = null;
		itemList.clear();
		categoryList = SubscriptionSettingsDAO.loadCategoryList(connection, cuisine.cuisinId);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectCategory(){
		itemList = SubscriptionSettingsDAO.loadItemList(connection, category.categoryId);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickAdd(){
		for(ItemBean itemBean : itemList){
			if(itemBean.isChecked()){
				System.out.println(itemBean.toString());
				itemBean.dayName = day.getDay();
				itemBean.dayId = day.getDayId();
				itemBean.packId = packID;
				addedItemList.add(itemBean);
			}
			
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickRemove(@BindingParam("bean")ItemBean addedItem){
		addedItemList.remove(addedItem);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickFinalAdd(){
		AddNewItemDAO.insertNewItem(connection, addedItemList);
		clear();
		BindUtils.postGlobalCommand(null, null, "globalReload", null);
		addNew.detach();
	}
	
	public void clear(){
		if(addedItemList.size()>0)
			addedItemList.clear();
		if(itemList.size() > 0)
			itemList.clear();
		cuisine.cuisinName = null;
		category.categoryName = null;
		cuisineList.clear();
		categoryList.clear();
		itemList.clear();
		addedItemList.clear();
		
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

	public ManageCuisinBean getCuisine() {
		return cuisine;
	}

	public void setCuisine(ManageCuisinBean cuisine) {
		this.cuisine = cuisine;
	}

	public ArrayList<ManageCuisinBean> getCuisineList() {
		return cuisineList;
	}

	public void setCuisineList(ArrayList<ManageCuisinBean> cuisineList) {
		this.cuisineList = cuisineList;
	}

	public ManageCategoryBean getCategory() {
		return category;
	}

	public void setCategory(ManageCategoryBean category) {
		this.category = category;
	}

	public ArrayList<ManageCategoryBean> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(ArrayList<ManageCategoryBean> categoryList) {
		this.categoryList = categoryList;
	}

	public ItemBean getItem() {
		return item;
	}

	public void setItem(ItemBean item) {
		this.item = item;
	}

	public ArrayList<ItemBean> getItemList() {
		return itemList;
	}

	public void setItemList(ArrayList<ItemBean> itemList) {
		this.itemList = itemList;
	}

	public ItemBean getAddedItem() {
		return addedItem;
	}

	public void setAddedItem(ItemBean addedItem) {
		this.addedItem = addedItem;
	}

	public Set<ItemBean> getAddedItemList() {
		return addedItemList;
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

	public void setAddedItemList(LinkedHashSet<ItemBean> addedItemList) {
		this.addedItemList = addedItemList;
	}
	
	
}
