package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.util.Beta;
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

import dao.PackDetailsDAO;
import dao.SubscriptionSettingsDAO;
import Bean.DayBean;
import Bean.Flavour;
import Bean.ItemBean;
import Bean.ManageCategoryBean;
import Bean.ManageCuisinBean;
import Bean.MealBean;
import Bean.Pack;

public class PackSettingsViewModel {

	Pack packBean = new Pack();
	
	public Pack pack = new Pack();
	public ArrayList<Pack> packTypeList = new ArrayList<Pack>();
	
	public DayBean day = new DayBean();
	public ArrayList<DayBean> dayList = new ArrayList<DayBean>();
	
	public Flavour flavour = new Flavour();
	public ArrayList<Flavour> flavourList = new ArrayList<Flavour>();
	
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
		
	public ItemBean savedItem = new ItemBean();
	public ArrayList<ItemBean> savedItemList = new ArrayList<ItemBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
		packTypeList = SubscriptionSettingsDAO.loadPackList(connection);
		
	}

	@Command
	@NotifyChange("*")
	public void onSelectPackType(){
		flavour.setFlavourType(null);
		cuisine.cuisinName = null;
		category.categoryName = null;
		cuisineList.clear();
		categoryList.clear();
		flavourList.clear();
		itemList.clear();
		addedItemList.clear();
		day.setDay(null);
		dayList= SubscriptionSettingsDAO.loadDayList(connection);
		flavourList = SubscriptionSettingsDAO.loadFlavourList(connection);
		cuisineList = SubscriptionSettingsDAO.loadCuisineList(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectFlavour(){
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickAdd(){
		for(ItemBean itemBean : itemList){
			if(itemBean.isChecked()){
				//System.out.println(itemBean.toString());
				itemBean.dayName = day.getDay();
				itemBean.dayId = day.getDayId();
				addedItemList.add(itemBean);
			}
			
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSave(){
		SubscriptionSettingsDAO.savePack(addedItemList, pack, flavour, connection);
		loadExistingPacks();
		clear();
	}
	
	
	
	@Command
	@NotifyChange("*")
	public void onClickRemove(@BindingParam("bean")ItemBean addedItem){
		System.out.println("Before remove Added item list : "+addedItemList.size()+"----------------"+addedItemList.contains(addedItem));
		
		addedItemList.remove(addedItem);
		System.out.println("Afeter remove Added item list : "+addedItemList.size());
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectCuisine(){
		category.categoryName = null;
		itemList.clear();
		categoryList= SubscriptionSettingsDAO.loadCategoryList(connection, cuisine.cuisinId);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectCategory(){
		itemList = SubscriptionSettingsDAO.loadItemList(connection, category.categoryId);
	}
	
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		loadExistingPacks();
	}
	
	public void clear(){
		if(addedItemList.size()>0)
			addedItemList.clear();
		if(itemList.size() > 0)
			itemList.clear();
		flavour.setFlavourType(null);
		cuisine.cuisinName = null;
		category.categoryName = null;
		cuisineList.clear();
		categoryList.clear();
		flavourList.clear();
		itemList.clear();
		addedItemList.clear();
		loadFlavourList();
		loadCuisineList();
	}
	
	public void loadPackList(){
		if(packTypeList.size() > 0){
			packTypeList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from fapp_pack_types where is_delete='N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Pack pack = new Pack();
					pack.packType = resultSet.getString("pack_type");
					pack.packTypeId = resultSet.getInt("pack_type_id");
					packTypeList.add(pack);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public void loadFlavourList(){
		if(flavourList.size() > 0){
			flavourList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from fapp_pack_flavour where is_delete='N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					Flavour flavour = new Flavour();
					flavour.setFlavourType(resultSet.getString("flavour_type")); 
					flavour.setFlavourId(resultSet.getInt("flavour_type_id"));  
					flavourList.add(flavour);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void loadCuisineList(){
		if(cuisineList.size() > 0){
			cuisineList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from fapp_cuisins where is_delete='N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ManageCuisinBean cuisine = new ManageCuisinBean();
					cuisine.cuisinName = resultSet.getString("cuisin_name");
					cuisine.cuisinId = resultSet.getInt("cuisin_id");
					cuisineList.add(cuisine);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void loadCategoryList(int cuisineId){
		if(categoryList.size()>0){
			categoryList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from food_category where is_delete='N' and category_price IS NULL and cuisine_id = ?";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, cuisineId);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ManageCategoryBean category = new ManageCategoryBean();
					category.categoryName = resultSet.getString("category_name");
					category.categoryId = resultSet.getInt("category_id");
					categoryList.add(category);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public void loadItemList(int categoryId){
		if(itemList.size() > 0){
			itemList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select item_name,item_price,item_code from food_items where is_delete='N' and kitchen_id IS NULL and category_id = ?";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setInt(1, categoryId);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ItemBean item = new ItemBean();
					item.itemName = resultSet.getString("item_name");
					item.itemPrice = resultSet.getDouble("item_price");
					item.itemCode = resultSet.getString("item_code");
					item.isChecked = false;
					item.qty = 0;
					item.mealTypeList = SubscriptionSettingsDAO.getMealtypeList(connection);
					itemList.add(item);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public ArrayList<MealBean> getMealtypeList(){
		ArrayList<MealBean> mealTypeList = new ArrayList<MealBean>();
		
		if(mealTypeList.size()>0){
			mealTypeList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from fapp_pack_meal_type where is_delete='N' ";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					MealBean meal = new MealBean();
					meal.typeOfMeal = resultSet.getString("meal_type");
					meal.mealTypeId = resultSet.getInt("meal_type_id");
					mealTypeList.add(meal);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mealTypeList;
	}
	
	public void loadDayList(){
		if(dayList.size() > 0){
			dayList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select * from fapp_day where is_delete='N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					DayBean day = new DayBean();
					day.setDay(resultSet.getString("day")); 
					day.setDayId(resultSet.getInt("day_id"));
					dayList.add(day);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickExistingPack(){
		loadExistingPacks();
	}
	
	public void loadExistingPacks(){
		if(savedItemList.size() > 0)
			savedItemList.clear();
		savedItemList = SubscriptionSettingsDAO.loadExistingPacks(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickDetails(@BindingParam("bean")ItemBean bean){
		System.out.println("PACK ID:: "+bean.packId);
		Map<String , Object > parentMap = new HashMap<String, Object>();
		parentMap.put("packId", bean);
		Window window = (Window) Executions.createComponents("packdetails.zul", null, parentMap);
		window.doModal();
	}
	
	public Pack getPackBean() {
		return packBean;
	}

	public void setPackBean(Pack packBean) {
		this.packBean = packBean;
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

	public Pack getPack() {
		return pack;
	}

	public void setPack(Pack pack) {
		this.pack = pack;
	}

	public ArrayList<Pack> getPackTypeList() {
		return packTypeList;
	}

	public void setPackTypeList(ArrayList<Pack> packTypeList) {
		this.packTypeList = packTypeList;
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

	public Flavour getFlavour() {
		return flavour;
	}

	public void setFlavour(Flavour flavour) {
		this.flavour = flavour;
	}

	public ArrayList<Flavour> getFlavourList() {
		return flavourList;
	}

	public void setFlavourList(ArrayList<Flavour> flavourList) {
		this.flavourList = flavourList;
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

	public ItemBean getSavedItem() {
		return savedItem;
	}

	public void setSavedItem(ItemBean savedItem) {
		this.savedItem = savedItem;
	}

	public ArrayList<ItemBean> getSavedItemList() {
		return savedItemList;
	}

	public void setSavedItemList(ArrayList<ItemBean> savedItemList) {
		this.savedItemList = savedItemList;
	}

	public void setAddedItemList(LinkedHashSet<ItemBean> addedItemList) {
		this.addedItemList = addedItemList;
	}

	
}
