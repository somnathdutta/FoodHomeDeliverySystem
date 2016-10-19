package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import dao.KitchenItemsDAO;
import Bean.ItemBean;
import Bean.ManageCategoryBean;
import Bean.ManageCuisinBean;
import Bean.ManageKitchens;

public class ManageKitchenItemsViewModel {
	
	public ManageKitchens kitchenBean = new ManageKitchens();
	
	public ArrayList<ManageKitchens> kitchenList = new ArrayList<ManageKitchens>();
	
	ManageCategoryBean categoryBean = new ManageCategoryBean();
	
	private ArrayList<ManageCategoryBean> categoryBeanList = new ArrayList<ManageCategoryBean>();
	
	ManageCuisinBean cuisinBean = new ManageCuisinBean();
	
	private ArrayList<ManageCuisinBean> cuisineBeanList =  new ArrayList<ManageCuisinBean>();
	
	ItemBean item = new ItemBean();
	
	private ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
	
	private ArrayList<ItemBean> kitchenItemBeanList = new ArrayList<ItemBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	public Boolean saveButtonVisibility = true;
	
	public Boolean updateButtonVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile(); 
	
	private int cuisineid,categoryId;
	
	public boolean isAllChecked = false;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		loadKitchenList();
	}
	
	public void loadKitchenList(){
		if(kitchenList.size()>0){
			kitchenList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet= null;
					String sql = "select kitchen_name,kitchen_id from vw_kitchens_details";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageKitchens kitchen = new ManageKitchens();
							kitchen.kitchenName = resultSet.getString("kitchen_name");
							kitchen.kitchenId = resultSet.getInt("kitchen_id");
							kitchenList.add(kitchen);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Command
	@NotifyChange("*")
	public void onAllCheck(){
		if(itemBeanList.size()>0){
			for(ItemBean bean : itemBeanList){
				bean.isChecked = true;
			}
		}
	}
	
	
	@Command
	@NotifyChange("*")
	public void onSelectKitchenName(){
		kitchenBean.cuisineName =null;
		kitchenBean.categoryName=null;
		loadCuisineForKitchen(kitchenBean.kitchenId);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectKitchen(){
		
		kitchenItemBeanList = KitchenItemsDAO.loadItemOfKitchen(connection, kitchenBean.kitchenId);
	}
	
	public void loadCuisineForKitchen(int kitchenId){
		if(cuisineBeanList.size()>0){
			cuisineBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql ="select * from vw_kitchen_cuisins_details where kitchen_id =?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchenId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCuisinBean cuisinBean = new ManageCuisinBean();
							cuisinBean.cuisinId = resultSet.getInt("cuisin_id");
							cuisinBean.cuisinName =resultSet.getString("cuisine_name");
							cuisineBeanList.add(cuisinBean);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectCuisineName(){
		System.out.println("cuid::" +cuisinBean.cuisinId);
		loadCategoryForKitchen(cuisinBean.cuisinId);
	}
	
	public void loadCategoryForKitchen(int cuisineId){
		if(categoryBeanList.size()>0){
			categoryBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql ="select category_id,category_name from vw_category_details where cuisin_id =?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, cuisineId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean category = new ManageCategoryBean();
							category.categoryId = resultSet.getInt("category_id");
							category.categoryName =resultSet.getString("category_name");
							categoryBeanList.add(category);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	@Command
	@NotifyChange("*")
	public void onSelectCategoryName(){
		kitchenBean.cuisineId = cuisinBean.cuisinId;
		kitchenBean.categoryId = categoryBean.categoryId;
		isAllChecked = false;
		System.out.println(kitchenBean.kitchenId+"  "+kitchenBean.cuisineId+" "+kitchenBean.categoryId);
		loadItemDetails(kitchenBean.categoryId);
	}
	
	public void loadItemDetails(int categoryId){
		if(itemBeanList.size()>0){
			itemBeanList.clear();
		}
		try {
			SQL:{
				 PreparedStatement preparedStatement = null;
				 ResultSet resultSet = null;
				 String sql = "select item_id,item_code,item_name,item_description,item_price from vw_food_item_details where category_id = ?";
				 try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setInt(1, categoryId);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						int itemId = resultSet.getInt("item_id");
						String itemCode = resultSet.getString("item_code");
						String itemName = resultSet.getString("item_name");
						String itemDescription = resultSet.getString("item_description");
						Double itemPrice = resultSet.getDouble("item_price");
						itemBeanList.add(new ItemBean(itemName, itemCode, itemDescription, itemId, itemPrice));
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					Messagebox.show("ERROR Due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSave(){
		if(isValid()){
			saveKitchenItemData();
		}
		
	}
	
	
	public boolean saveKitchenItemData(){
		boolean isInserted=false;
		boolean categorySaved =false;
		try {
			if(isCategoryAlreadySaved(kitchenBean)){
				categorySaved = true;
			}else{
				System.out.println("New category inserting. . . ");
				SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_kitchen_stock( kitchen_cuisine_id,kitchen_category_id,category_stock,kitchen_id)"
							+ " VALUES(?,?,?,?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchenBean.cuisineId);
						preparedStatement.setInt(2, kitchenBean.categoryId);
						preparedStatement.setInt(3, kitchenBean.capacity);
						preparedStatement.setInt(4, kitchenBean.kitchenId);
						int count = preparedStatement.executeUpdate();
						if(count > 0){
							categorySaved = true;
						}
					} catch (Exception e) {
						if(e.getMessage().startsWith("Batch entry")){
							Messagebox.show("This category already saved.","ERROR",Messagebox.OK,Messagebox.ERROR);
						}else{
							Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						}
						connection.rollback();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}	
			}
			
		    if(categorySaved){
		    	SQL:{
			    	PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_kitchen_items( item_code,item_id,kitchen_id )"
							+ " VALUES(?,?,?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						for(ItemBean itemBean : itemBeanList){
							if(itemBean.isChecked){
								preparedStatement.setString(1, itemBean.itemCode);
								preparedStatement.setInt(2, itemBean.itemId);
								preparedStatement.setInt(3, kitchenBean.kitchenId);
								preparedStatement.addBatch();
							}
							
						}
						int[] count = preparedStatement.executeBatch();
						for(Integer integer : count){
				    		   System.out.println(integer);  
				    		   isInserted = true;
				    	   }
					} catch (Exception e) {
						if(e.getMessage().startsWith("Batch entry")){
							Messagebox.show("This item already saved.","ERROR",Messagebox.OK,Messagebox.ERROR);
						}else{
							Messagebox.show("Error due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						}
						connection.rollback();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		    }
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(isInserted){
			Messagebox.show("Item saved with kitchen successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
			refresh();
		}
		return isInserted;
	}
	
	public boolean isCategoryAlreadySaved(ManageKitchens kitchen){
		boolean isExists =false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select kitchen_cuisine_id,kitchen_category_id,kitchen_id from fapp_kitchen_stock"
							+ " where kitchen_cuisine_id=? and kitchen_category_id=? and kitchen_id=?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchen.cuisineId);
						preparedStatement.setInt(2, kitchen.categoryId);
						preparedStatement.setInt(3, kitchen.kitchenId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							isExists = true;
							System.out.println("Category already saved . . .");
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return isExists ;
	}
	
	public boolean isValid(){
		if(kitchenBean.kitchenId!=null){
			if(kitchenBean.cuisineId!=null){
				if(kitchenBean.categoryId!=null){
					if(kitchenBean.capacity!=null){
						//if(itemBeanList.size()>0){
						if(isItemChecked()){
							return true;
						}else{
							Messagebox.show("Items required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
							return false;
						}
					}else{
						Messagebox.show("Capacity required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
						return false;
					}
				}else{
					Messagebox.show("Category required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
					return false;
				}
			}else{
				Messagebox.show("Cuisine required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Kitchen required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public void refresh(){
		itemBeanList.clear();
		kitchenBean.kitchenId = null;
		kitchenBean.kitchenName = null;
		kitchenBean.cuisineId =  null;
		kitchenBean.cuisineName = null;
		kitchenBean.capacity = null;
		kitchenBean.categoryId = null;
		kitchenBean.categoryName = null;
		cuisinBean.cuisinName = null;
		categoryBean.categoryName = null;
		isAllChecked = false;
		loadKitchenList();
	}
	
	public boolean isItemChecked(){
		boolean isChecked =false;
		for(ItemBean bean : itemBeanList){
			if(bean.isChecked){
				isChecked = true;
			}
		}
		return isChecked;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickShow(){
		if(itemBeanList.size()>0){
			itemBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT fki.item_id,fki.item_code,fi.item_name,fi.item_description,fi.item_price "
								+" from fapp_kitchen_items fki "
								+" JOIN food_items fi "
								+" ON fi.item_id  = fki.item_id "
								+" where fki.kitchen_id = ? order by item_code";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchenBean.kitchenId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							int itemId = resultSet.getInt("item_id");
							String itemName = resultSet.getString("item_name");
							String itemCode = resultSet.getString("item_code");
							String itemDescription = resultSet.getString("item_description");
							Double itemPrice = resultSet.getDouble("item_price"); 
								
							itemBeanList.add(new ItemBean(itemName, itemCode, itemDescription, itemId, itemPrice)) ;
						}
					} catch (Exception e) {
					e.printStackTrace();
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public ManageKitchens getKitchenBean() {
		return kitchenBean;
	}

	public void setKitchenBean(ManageKitchens kitchenBean) {
		this.kitchenBean = kitchenBean;
	}

	public ArrayList<ManageKitchens> getKitchenList() {
		return kitchenList;
	}

	public void setKitchenList(ArrayList<ManageKitchens> kitchenList) {
		this.kitchenList = kitchenList;
	}

	public ManageCategoryBean getCategoryBean() {
		return categoryBean;
	}

	public void setCategoryBean(ManageCategoryBean categoryBean) {
		this.categoryBean = categoryBean;
	}

	public ManageCuisinBean getCuisinBean() {
		return cuisinBean;
	}

	public void setCuisinBean(ManageCuisinBean cuisinBean) {
		this.cuisinBean = cuisinBean;
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

	public ArrayList<ManageCuisinBean> getCuisineBeanList() {
		return cuisineBeanList;
	}

	public void setCuisineBeanList(ArrayList<ManageCuisinBean> cuisineBeanList) {
		this.cuisineBeanList = cuisineBeanList;
	}

	public Boolean getSaveButtonVisibility() {
		return saveButtonVisibility;
	}

	public void setSaveButtonVisibility(Boolean saveButtonVisibility) {
		this.saveButtonVisibility = saveButtonVisibility;
	}

	public Boolean getUpdateButtonVisibility() {
		return updateButtonVisibility;
	}

	public void setUpdateButtonVisibility(Boolean updateButtonVisibility) {
		this.updateButtonVisibility = updateButtonVisibility;
	}

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}

	public int getCuisineid() {
		return cuisineid;
	}

	public void setCuisineid(int cuisineid) {
		this.cuisineid = cuisineid;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public ItemBean getItem() {
		return item;
	}

	public void setItem(ItemBean item) {
		this.item = item;
	}

	public ArrayList<ItemBean> getItemBeanList() {
		return itemBeanList;
	}

	public void setItemBeanList(ArrayList<ItemBean> itemBeanList) {
		this.itemBeanList = itemBeanList;
	}

	public boolean isAllChecked() {
		return isAllChecked;
	}

	public void setAllChecked(boolean isAllChecked) {
		this.isAllChecked = isAllChecked;
	}

	public ArrayList<ItemBean> getKitchenItemBeanList() {
		return kitchenItemBeanList;
	}

	public void setKitchenItemBeanList(ArrayList<ItemBean> kitchenItemBeanList) {
		this.kitchenItemBeanList = kitchenItemBeanList;
	}
}
