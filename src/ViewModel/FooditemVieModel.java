package ViewModel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import service.FoodItemService;
import dao.FoodItemDAO;
import Bean.ItemBean;
import Bean.ManageCategoryBean;
import Bean.ManageCuisinBean;

public class FooditemVieModel {
	
	private ItemBean itemBean = new ItemBean();
	
	private ArrayList<ItemBean> itemBeanList = new ArrayList<ItemBean>();
	
	private ItemBean alaCarteTypeBean = new ItemBean();
	
	private ArrayList<ItemBean> alaCarteTypeList = new ArrayList<ItemBean>();
	
	private ItemBean itemTypeBean = new ItemBean();
	
	private ArrayList<ItemBean> ItemTypeList = new ArrayList<ItemBean>();
	
	private ItemBean itemPackTypeBean = new ItemBean();
	
	private ArrayList<ItemBean> itemPackTypeList = new ArrayList<ItemBean>();
	
	ManageCategoryBean categoryBean = new ManageCategoryBean();
	
	ManageCuisinBean cuisinBean = new ManageCuisinBean();
	
	private ArrayList<ManageCategoryBean> categoryBeanList = new ArrayList<ManageCategoryBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private ArrayList<ManageCuisinBean> cuisineBeanList =  new ArrayList<ManageCuisinBean>();
	
	ManageCuisinBean manageCuisinBean = new ManageCuisinBean();
	ManageCategoryBean manageCategoryBean = new ManageCategoryBean();
	ItemBean newUserItemBean = new ItemBean();
	
	
	private ArrayList<ManageCuisinBean> newUserCuisineBeanList;
	private ArrayList<ManageCategoryBean> NewUserCategoryBeanList;
	private ArrayList<ItemBean> newUserItemBeanList;
	
	public Boolean saveButtonVisibility = true, typeVisibility = false;
	
	public Boolean updateButtonVisibility = false, typeComboBoxVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile(); 
	
	private AImage itemImage;
	
	private String itemImagefilePath;
	
	private boolean itemImageFileuploaded = false;
	
	private int cuisineid,categoryId;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		System.out.println("zul page >> fooditems.zul");
		onPageLoad();
	}
	
	public void onPageLoad(){
		//loadQuery();
		onLoadCuisineList();
		ItemTypeList = FoodItemDAO.loadAlacarteTypeNameList(connection);
		itemPackTypeList = FoodItemDAO.loadItemPackingTypeList(connection);
		newUserCuisineBeanList = FoodItemService.fetchCuisineList(connection);
		itemBean.itemCode = FoodItemDAO.getLastItemCode(connection);
	}
	
	public void loadQuery(){
		if(itemBeanList.size()>0){
			itemBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_food_item_details";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ItemBean bean = new ItemBean();
							bean.cusineName = resultSet.getString("cuisin_name");
							bean.categoryName = resultSet.getString("category_name");
							bean.categoryId = resultSet.getInt("category_id");
							bean.itemName = resultSet.getString("item_name");
							bean.itemCode = resultSet.getString("item_code");
							bean.itemPrice = resultSet.getDouble("item_price");
							bean.itemDescription = resultSet.getString("item_description");
							bean.itemId = resultSet.getInt("item_id");
							if(resultSet.getString("item_image") != null){
								bean.itemmagePath = resultSet.getString("item_image");
							}else{
								bean.itemmagePath = null;
							}
							if(bean.itemmagePath != null){
								try {
									bean.itemImage = new AImage(bean.itemmagePath);
								} catch (Exception e) {
									bean.itemImage = null;
								}
							}else{
								bean.itemImage = null;
							}
							bean.itemTypeId = resultSet.getInt("item_type_id");
							if(resultSet.getString("type_name") != null){
								bean.itemTypeName = resultSet.getString("type_name");
							}else{
								bean.itemTypeName = "";
							}
							if(resultSet.getString("is_active").equalsIgnoreCase("Y")){
								bean.status = "Active";
							}else{
								bean.status = "Deactive";
							}
							bean.packingId = resultSet.getInt("packing_type_id");
							if(resultSet.getString("pack_type") != null){
								bean.packingName = resultSet.getString("pack_type");
							}else{
								bean.packingName = "";
							}
							
							itemBeanList.add(bean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void onLoadCuisineList(){
		if(cuisineBeanList.size()>0)
			cuisineBeanList.clear();
		
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					String sql = "SELECT cuisin_name,cuisin_id FROM fapp_cuisins WHERE is_delete = 'N' order by cuisin_id";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCuisinBean cuisinBean = new ManageCuisinBean();
							cuisinBean.cuisinName =  resultSet.getString("cuisin_name");
							cuisinBean.cuisinId = resultSet.getInt("cuisin_id");
								
							cuisineBeanList.add(cuisinBean);
						}
						
					}catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
						} finally{
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
	public void onSelectCuisineName(){
		if(categoryBeanList.size()>0){
			categoryBeanList.clear();
			categoryBean.categoryName = null;
			itemBean.categoryName = null;
		}
		cuisineid = cuisinBean.cuisinId;
		itemBean.cuisineId = cuisineid;
		onLoadCategoryList(itemBean.cuisineId);
		if(categoryBeanList.size()==0){
			categoryId =0;
		}
		
		itemBeanList = FoodItemDAO.loadFoodItems(connection, cuisineid, 0);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectCategoryName(){
		categoryId = categoryBean.categoryId;
		itemBean.categoryId = categoryId;
		if(itemBean.categoryId==78 || itemBean.categoryId==79){
			typeVisibility = true;
			typeComboBoxVisibility = true;
			alaCarteTypeList = FoodItemDAO.loadAlacarteTypeNameList(connection);	
		}else{
			typeVisibility = false;
			typeComboBoxVisibility = false;
		}
		itemBeanList = FoodItemDAO.loadFoodItems(connection, cuisineid, categoryId);
	}
	
	
	/**
	 * Load all categories
	 */
	public void onLoadCategoryList(int cuisineId){
		
		if(categoryBeanList.size()>0)
			categoryBeanList.clear();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					String sql = "SELECT category_name,category_id FROM food_category WHERE area_id IS NULL AND is_delete = 'N' "
							+ "AND cuisine_id = ?";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, cuisineId);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCategoryBean categoryBean = new ManageCategoryBean();
							categoryBean.categoryId = resultSet.getInt("category_id");
							categoryBean.categoryName = resultSet.getString("category_name");
							categoryBeanList.add(categoryBean);
						}
						
					}catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
						} finally{
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
	public void onClickCancel(){
		clear();
		onLoadCuisineList();
		itemBean.itemCode = FoodItemDAO.getLastItemCode(connection);
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickSaveCategoryItem(){		
		if(validate()){
			
			System.out.println("Cat : "+categoryId);
			System.out.println("item type id: "+itemTypeBean.itemTypeId);
			saveItem();
			clear();
			loadAllSavedItems();
			loadQuery();
			onLoadCuisineList();
			itemBean.itemCode = FoodItemDAO.getLastItemCode(connection);
			System.out.println("Cat : "+categoryId);
		}	
	}
	
	public void saveItem(){
		boolean isInserted = false;
		
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO food_items(category_id, item_name, item_image, item_price, "
					           +" item_description, is_active, item_code,item_type_id,created_by,packing_type_id) "
					           +" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, categoryId);
						if(itemBean.itemName!=null){
							preparedStatement.setString(2, itemBean.itemName.trim());
						}else{
							preparedStatement.setNull(2, Types.INTEGER);
						}
						if(itemBean.itemmagePath!=null){
							preparedStatement.setString(3, itemBean.itemmagePath);
						}else{
							preparedStatement.setNull(3, Types.INTEGER);
						}
						if(itemBean.itemPrice != null){
							preparedStatement.setDouble(4,itemBean.itemPrice);
						}else{
							preparedStatement.setNull(4,Types.NULL);
						}
						if(itemBean.itemDescription!=null){
							preparedStatement.setString(5, itemBean.itemDescription);
						}else{
							preparedStatement.setNull(5, Types.INTEGER);
						}
						if(itemBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(6, "Y");
						}else{
							preparedStatement.setString(6, "N");
						}
						if(itemBean.itemCode!=null){
							preparedStatement.setString(7, itemBean.itemCode);
						}else{
							preparedStatement.setNull(7, Types.INTEGER);
						}
					//	if(categoryId==78 || categoryId==79)
					//		preparedStatement.setInt(8, alaCarteTypeBean.itemTypeId);
					//	else
							preparedStatement.setInt(8, itemTypeBean.itemTypeId);
						preparedStatement.setString(9, userName);
						preparedStatement.setInt(10, itemPackTypeBean.packingId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							isInserted = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("ERROR Due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}	
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(isInserted){
			Messagebox.show("Item Saved successfully!","Infromation",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean")ItemBean bean){
		categoryId = bean.categoryId;
		itemBean.cusineName = bean.cusineName;
		itemBean.categoryName = bean.categoryName;
		itemBean.itemName = bean.itemName;
		itemBean.itemCode = bean.itemCode;
		itemBean.itemDescription = bean.itemDescription;
		itemBean.itemId = bean.itemId;
		itemBean.itemPrice = bean.itemPrice;
		itemBean.itemmagePath = bean.itemmagePath;
		itemImage=bean.itemImage;
		itemBean.status = bean.status;
		itemBean.packingId = bean.packingId;
		itemBean.packingName = bean.packingName;
		saveButtonVisibility = false;
		updateButtonVisibility = true;
		itemPackTypeBean.packingId = bean.packingId;
		itemPackTypeBean.packingName = bean.packingName;
		if(itemBean.categoryId==78 || itemBean.categoryId==79){
			//typeVisibility = true;
			//typeComboBoxVisibility = true;
			ItemTypeList = FoodItemDAO.loadAlacarteTypeNameList(connection);	
			itemTypeBean.itemTypeName = bean.itemTypeName;
			itemTypeBean.itemTypeId = bean.itemTypeId;
			alaCarteTypeBean.itemTypeId = bean.itemTypeId;
			alaCarteTypeBean.itemTypeName = bean.itemTypeName;
			alaCarteTypeList = FoodItemDAO.loadAlacarteTypeNameList(connection);	
		}else{
			typeVisibility = false;
			typeComboBoxVisibility = false;
			ItemTypeList = FoodItemDAO.loadAlacarteTypeNameList(connection);	
			itemTypeBean.itemTypeName = bean.itemTypeName;
			itemTypeBean.itemTypeId = bean.itemTypeId;
		}
		itemPackTypeList = FoodItemDAO.loadItemPackingTypeList(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdateCategoryItem(){
		if(validate()){
			updateItem();
			saveButtonVisibility = true;
			updateButtonVisibility = false;
			typeVisibility = false;
			typeComboBoxVisibility = false;
			clear();
			onLoadCuisineList();
			
			//loadAllSavedItems();
			loadQuery();
		}	
	}
	
	public void updateItem(){
		boolean isUpdated = false;
		System.out.println(itemTypeBean.packingId);
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE food_items set category_id=?, item_name=?, item_image=?, item_price=?, "
					           +" item_description=?, is_active=?, item_code=?,item_type_id=?,updated_by=?,packing_type_id = ? where item_id = ? ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, categoryId);
						if(itemBean.itemName!=null){
							preparedStatement.setString(2, itemBean.itemName.trim());
						}else{
							preparedStatement.setNull(2, Types.INTEGER);
						}
						if(itemBean.itemmagePath!=null){
							preparedStatement.setString(3, itemBean.itemmagePath);
						}else{
							preparedStatement.setNull(3, Types.INTEGER);
						}
						if(itemBean.itemPrice != null){
							preparedStatement.setDouble(4,itemBean.itemPrice);
						}else{
							preparedStatement.setNull(4,Types.NULL);
						}
						if(itemBean.itemDescription!=null){
							preparedStatement.setString(5, itemBean.itemDescription);
						}else{
							preparedStatement.setNull(5, Types.INTEGER);
						}
						if(itemBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(6, "Y");
						}else{
							preparedStatement.setString(6, "N");
						}
						if(itemBean.itemCode!=null){
							preparedStatement.setString(7, itemBean.itemCode);
						}else{
							preparedStatement.setNull(7, Types.INTEGER);
						}
						
					//	if(categoryId==78 || categoryId==79)
					//		preparedStatement.setInt(8, alaCarteTypeBean.itemTypeId);
					//	else
							preparedStatement.setInt(8, itemTypeBean.itemTypeId);
						preparedStatement.setString(9, userName);
						preparedStatement.setInt(10, itemPackTypeBean.packingId);
						preparedStatement.setInt(11, itemBean.itemId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							isUpdated = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("ERROR Due to:: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}	
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(isUpdated){
			Messagebox.show("Item Updated successfully!","Infromation",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		//loadQuery();
		loadAllSavedItems();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean") final ItemBean bean){
		Messagebox.show(
				"Are you sure to delete?", "Confirm Dialog",
				Messagebox.OK | Messagebox.IGNORE | Messagebox.CANCEL,
				Messagebox.QUESTION, 
				new org.zkoss.zk.ui.event.EventListener() {
					@Override
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							//System.out.println("Data Saved !");
							deleteItem(bean);
							BindUtils.postGlobalCommand(null, null, "globalReload", null);
						} else if (evt.getName().equals("onIgnore")) {
							Messagebox.show("Ignore Save", "Warning",
									Messagebox.OK, Messagebox.EXCLAMATION);
						} else {
							System.out.println("Save Canceled !");
						}
					}
				}
			);
	}
	
	public void deleteItem(ItemBean bean){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE food_items set is_delete='Y' where item_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, bean.itemId);
						int count = preparedStatement.executeUpdate();
						if(count > 0){
							System.out.println("Item deleted.");
							Messagebox.show("Item deleted successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Deletion failed due to: "+e.getMessage(),"ERROR" , Messagebox.OK,Messagebox.ERROR);
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
	public void onUploadItemImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException{
		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			
			String yearPath = "/"+"Images_IemImages" + "/" +new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "/";
						
			String contextPath = Sessions.getCurrent().getWebApp().getServletContext().getContextPath();
			
			String imagespath = System.getProperty("catalina.home")+"/"+"webapps"+"/"+contextPath.replace("/", "")+"/"+"view"+"/"+"images";
	 		
			itemImagefilePath = imagespath;
			
			itemImagefilePath = itemImagefilePath + yearPath;
		        
			File baseDir = new File(itemImagefilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			Files.copy(new File(itemImagefilePath + media.getName()),media.getStreamData());

			Messagebox.show("Image Sucessfully uploaded!","Information",Messagebox.OK,Messagebox.INFORMATION);
			
			itemImageFileuploaded = true;
			itemImagefilePath = itemImagefilePath + media.getName();
		
			itemBean.itemmagePath = itemImagefilePath;

			itemImage = (AImage) media;
		}
	}
	
	public boolean validate(){
		if(itemBean.cusineName != null){
			if(categoryId!=0){
				if(itemBean.itemName!=null){
					if(itemBean.itemCode!=null){
						if(itemBean.itemPrice!=null){
							if(itemBean.itemDescription!=null){
								if(itemBean.status!=null){
									if(itemTypeBean.itemTypeId!=0){
										return true;
									}else{
										Messagebox.show("Item Type required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
										return false;
									}
								}else{
									Messagebox.show("Status required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
									return false;
								}
							}else{
								Messagebox.show("Description required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
								return false;
							}
						}else{
							Messagebox.show("Price required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
							return false;
						}
					}else{
						Messagebox.show("Item code required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
						return false;
					}
				}else{
					Messagebox.show("Item Name required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
					return false;
				}
			}else{
				Messagebox.show("Please choose category!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Please choose cuisne!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectNewUserFetchCuisine(){
		
		NewUserCategoryBeanList = FoodItemService.fetchCategory(connection, manageCuisinBean.cuisinId);
		newUserItemBeanList = FoodItemDAO.loadFoodItems(connection, manageCuisinBean.cuisinId, 0);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectNewUserCategory(){
		newUserItemBeanList = FoodItemDAO.loadFoodItems(connection, manageCuisinBean.cuisinId, manageCategoryBean.categoryId);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelctNewUserStatus(@BindingParam("bean") ItemBean bean ){
		
		int i = 0;
		String localStatus = bean.status;
		if(localStatus.equals("Active")){
			localStatus = "Y";
		}else {
			localStatus = "N";
		}
		i = FoodItemService.updateNewUserItemStatus(connection, localStatus, bean.itemId);
		if(i>0){
			Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	
	
	public void clear(){
		categoryId = 0;
		cuisineid = 0;
		//categoryBean.categoryName = null;
		//cuisinBean.cuisinName = null;
		itemBean.itemName = null;
		itemBean.cusineName = null;
		itemBean.categoryName = null;
		itemBean.status = null;
		//itemBean.itemCode = null;
		itemBean.itemDescription = null;
		itemBean.itemId = 0;
		itemImage = null;
		itemBean.itemmagePath = null;
		//itemBean.itemCode = null;
		itemBean.itemPrice =null;
		categoryBeanList.clear();
		alaCarteTypeList.clear();
		alaCarteTypeBean.itemTypeName = null;
		ItemTypeList.clear();
		itemTypeBean.itemTypeName = null;
		itemPackTypeBean.packingName = null;
	}
	
	public void loadAllSavedItems(){
		itemBeanList = FoodItemDAO.loadFoodItems(connection, 0, 0);
	}
	
	public ItemBean getItemBean() {
		return itemBean;
	}

	public void setItemBean(ItemBean itemBean) {
		this.itemBean = itemBean;
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

	public ArrayList<ItemBean> getItemBeanList() {
		return itemBeanList;
	}

	public void setItemBeanList(ArrayList<ItemBean> itemBeanList) {
		this.itemBeanList = itemBeanList;
	}

	public AImage getItemImage() {
		return itemImage;
	}

	public void setItemImage(AImage itemImage) {
		this.itemImage = itemImage;
	}

	public String getItemImagefilePath() {
		return itemImagefilePath;
	}

	public void setItemImagefilePath(String itemImagefilePath) {
		this.itemImagefilePath = itemImagefilePath;
	}

	public boolean isItemImageFileuploaded() {
		return itemImageFileuploaded;
	}

	public void setItemImageFileuploaded(boolean itemImageFileuploaded) {
		this.itemImageFileuploaded = itemImageFileuploaded;
	}

	public Boolean getTypeVisibility() {
		return typeVisibility;
	}

	public void setTypeVisibility(Boolean typeVisibility) {
		this.typeVisibility = typeVisibility;
	}

	public Boolean getTypeComboBoxVisibility() {
		return typeComboBoxVisibility;
	}

	public void setTypeComboBoxVisibility(Boolean typeComboBoxVisibility) {
		this.typeComboBoxVisibility = typeComboBoxVisibility;
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

	public ArrayList<ItemBean> getAlaCarteTypeList() {
		return alaCarteTypeList;
	}

	public void setAlaCarteTypeList(ArrayList<ItemBean> alaCarteTypeList) {
		this.alaCarteTypeList = alaCarteTypeList;
	}

	public ItemBean getAlaCarteTypeBean() {
		return alaCarteTypeBean;
	}

	public void setAlaCarteTypeBean(ItemBean alaCarteTypeBean) {
		this.alaCarteTypeBean = alaCarteTypeBean;
	}

	public ManageCuisinBean getManageCuisinBean() {
		return manageCuisinBean;
	}

	public void setManageCuisinBean(ManageCuisinBean manageCuisinBean) {
		this.manageCuisinBean = manageCuisinBean;
	}

	public ManageCategoryBean getManageCategoryBean() {
		return manageCategoryBean;
	}

	public void setManageCategoryBean(ManageCategoryBean manageCategoryBean) {
		this.manageCategoryBean = manageCategoryBean;
	}

	public ArrayList<ManageCuisinBean> getNewUserCuisineBeanList() {
		return newUserCuisineBeanList;
	}

	public void setNewUserCuisineBeanList(
			ArrayList<ManageCuisinBean> newUserCuisineBeanList) {
		this.newUserCuisineBeanList = newUserCuisineBeanList;
	}

	public ArrayList<ManageCategoryBean> getNewUserCategoryBeanList() {
		return NewUserCategoryBeanList;
	}

	public void setNewUserCategoryBeanList(
			ArrayList<ManageCategoryBean> newUserCategoryBeanList) {
		NewUserCategoryBeanList = newUserCategoryBeanList;
	}

	public ItemBean getNewUserItemBean() {
		return newUserItemBean;
	}

	public void setNewUserItemBean(ItemBean newUserItemBean) {
		this.newUserItemBean = newUserItemBean;
	}

	public ArrayList<ItemBean> getNewUserItemBeanList() {
		return newUserItemBeanList;
	}

	public void setNewUserItemBeanList(ArrayList<ItemBean> newUserItemBeanList) {
		this.newUserItemBeanList = newUserItemBeanList;
	}

	public ItemBean getItemTypeBean() {
		return itemTypeBean;
	}

	public void setItemTypeBean(ItemBean itemTypeBean) {
		this.itemTypeBean = itemTypeBean;
	}

	public ArrayList<ItemBean> getItemTypeList() {
		return ItemTypeList;
	}

	public void setItemTypeList(ArrayList<ItemBean> itemTypeList) {
		ItemTypeList = itemTypeList;
	}

	public ItemBean getItemPackTypeBean() {
		return itemPackTypeBean;
	}

	public void setItemPackTypeBean(ItemBean itemPackTypeBean) {
		this.itemPackTypeBean = itemPackTypeBean;
	}

	public ArrayList<ItemBean> getItemPackTypeList() {
		return itemPackTypeList;
	}

	public void setItemPackTypeList(ArrayList<ItemBean> itemPackTypeList) {
		this.itemPackTypeList = itemPackTypeList;
	}

	
	
}
