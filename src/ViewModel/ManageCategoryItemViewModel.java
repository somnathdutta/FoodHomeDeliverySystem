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
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.ManageCategoryBean;
import Bean.ManageCategoryItemBean;
import Bean.SubitemBean;


public class ManageCategoryItemViewModel {
	
	private ManageCategoryItemBean manageCategoryItemBean = new ManageCategoryItemBean();
	
	private ArrayList<ManageCategoryItemBean> manageCategoryItemBeanList = new ArrayList<ManageCategoryItemBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private ArrayList<String> cityList= new ArrayList<String>();
	
	private ArrayList<String> areaList= new ArrayList<String>();
	
	private ArrayList<String> categoryList= new ArrayList<String>();
	
	private AImage itemImage;
	
	private String itemImageFilePath;
	
	private boolean itemImageFileuploaded = false;
	
	private AImage itemBanner;
	
	private String itemBannerFilePath;
	
	private boolean itemBannerFileuploaded = false;
	
	private AImage subitemImage;
	
	private String subitemImageFilePath;
	
	private boolean subitemImageFileuploaded = false;
	
	private Boolean imageBoxVisibility = false;
	
	private Boolean updateButtonVisibility = false;
	
	private Boolean saveButtonVisibility = true;
	
	public ArrayList<String> cuisineList = new ArrayList<String>();
	
	public ArrayList<String> kitchenList = new ArrayList<String>();
	
	public String cuisineName =  "";
	
	public String kitchenName = "";
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		onLoadKitchenList();
		//onLoadCuisineList();
		
		onLoadItemList();
		
		//onLoadCategoryList();
		
	}
	
	public void onLoadKitchenList(){
		if(kitchenList.size()>0)
			kitchenList.clear();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					String sql = "SELECT kitchen_name FROM fapp_kitchen WHERE is_delete = 'N' AND is_active='Y'";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							kitchenList.add(resultSet.getString(1));
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
	public void onSelectKitchenName(){
		cuisineName = null;
		manageCategoryItemBean.categoryName = null;
		onLoadCuisineList(kitchenName);
		if(categoryList.size()>0)
			categoryList.clear();
		
	}
	
	public void onLoadCuisineList(String kitchenName){
		if(cuisineList.size()>0)
			cuisineList.clear();
		
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					//String sql = "SELECT cuisin_name FROM fapp_cuisins WHERE is_delete = 'N'";
					String sql="select distinct fks.kitchen_cuisine_id,"
							+" (select cuisin_name from fapp_cuisins where cuisin_id = fks.kitchen_cuisine_id)AS cuisine_name"
							+" from fapp_kitchen_stock fks "
							+" where fks.kitchen_id = "
							+" (select kitchen_id from fapp_kitchen where kitchen_name ILIKE ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, kitchenName);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							cuisineList.add(resultSet.getString("cuisine_name"));
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
		if(categoryList.size()>0){
			categoryList.clear();
			manageCategoryItemBean.categoryName= null;
		}
			
		onLoadCategoryList(kitchenName,cuisineName);
	}
	
	/**
	 * Load all categories
	 */
	public void onLoadCategoryList(String kitchen,String cuisineName){
		
		if(categoryList.size()>0)
			categoryList.clear();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					//String sql = "SELECT category_name FROM food_category WHERE area_id IS NULL AND is_delete = 'N' AND "
					//		+ "  cuisine_id = (select cuisin_id from fapp_cuisins where cuisin_name = ?)";
					String sql = "select distinct fks.kitchen_category_id,(select category_name from food_category "
							 +"  where category_id = fks.kitchen_category_id) "
							 +"  from fapp_kitchen_stock fks "
							 +"   where "
							 +"  fks.kitchen_cuisine_id = (select cuisin_id from fapp_cuisins where cuisin_name = ?)" 
							 +"  and "
							 +" fks.kitchen_id =  "
							 +" (select kitchen_id from fapp_kitchen where kitchen_name ILIKE ?)";
					
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, cuisineName);
						preparedStatement.setString(2, kitchen);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							categoryList.add(resultSet.getString(2));
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
	/**
	 * 
	 * This function used to load all saved items from database to the grid
	 */
	public void onLoadItemList(){
		if(manageCategoryItemBeanList.size()>0){
			manageCategoryItemBeanList.clear();
		}
		try {
			SQL:{
				PreparedStatement preparedStatement =  null;
				ResultSet resultSet = null;
				try {
					preparedStatement = connection.prepareStatement(propertyfile.getPropValues("onLoadCategoryItemListSql"));
					resultSet = preparedStatement.executeQuery();
					String cityname = null;
					String areaname = null;
					String categoryname = null;
					while(resultSet.next()){
						ManageCategoryItemBean manageCategoryItembean =  new ManageCategoryItemBean();
						manageCategoryItembean.kitchenId = resultSet.getInt("kitchen_id");
						manageCategoryItembean.kitchenName = resultSet.getString("kitchen_name");
						manageCategoryItembean.categoryId = resultSet.getInt("category_id");
					//	manageCategoryItemBean.categoryId = manageCategoryItembean.categoryId;
						manageCategoryItembean.itemId = resultSet.getInt("item_id");
					//	manageCategoryItemBean.itemId = manageCategoryItembean.itemId;
						/*manageCategoryItembean.cityName = resultSet.getString("city_name");
						String tempCityname = manageCategoryItembean.cityName;
						if(manageCategoryItembean.cityName.equals(cityname)){
							manageCategoryItembean.cityName="";
							manageCategoryItembean.citynamevisibility=false;
						}
						manageCategoryItembean.areaName = resultSet.getString("area_name");
						String tempAreaname = manageCategoryItembean.areaName;
						if(manageCategoryItembean.areaName.equals(areaname)){
							manageCategoryItembean.areaName="";
							manageCategoryItembean.areanamevisibility=false;
						}*/
						manageCategoryItembean.categoryName = resultSet.getString("category_name");
						manageCategoryItembean.cuisineName = resultSet.getString("cuisine_name");
						/*String tempCategoryname = manageCategoryItembean.categoryName;
						if(manageCategoryItembean.categoryName.equals(categoryname)){
							manageCategoryItembean.categoryName="";
							manageCategoryItembean.categorynamevisibility=false;
						}*/
						manageCategoryItembean.itemName = resultSet.getString("item_name");
						/*manageCategoryItembean.itemImageFilePath = resultSet.getString("item_image");
						if(manageCategoryItembean.itemImageFilePath!=null){
							manageCategoryItembean.itemImage = new AImage(manageCategoryItembean.itemImageFilePath);
						}else{
							manageCategoryItembean.itemImage = new AImage("");
						}*/
						manageCategoryItembean.description = resultSet.getString("item_description");
						manageCategoryItembean.price = resultSet.getDouble("item_price");
						manageCategoryItembean.inStockQty = resultSet.getInt("item_stock_qty");
						if(resultSet.getString("is_active").equalsIgnoreCase("Y")){
							manageCategoryItembean.status = "Active";
						}else{
							manageCategoryItembean.status = "Deactive";
						}
						/*cityname = tempCityname;
						areaname = tempAreaname;
						categoryname = tempCategoryname;*/
						manageCategoryItemBeanList.add(manageCategoryItembean);
					}
				} catch (Exception e) {
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
	
	public Integer getKitchenId(){
		
		Integer kictchenId = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select kitchen_id from fapp_kitchen where kitchen_name = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, kitchenName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							kictchenId = resultSet.getInt(1);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return kictchenId;
	}
	
	
	@Command
	@NotifyChange("*")
	public void onSelectCategoryName(){
		try {
			sql1:{
					PreparedStatement preparedStatement=null;
					try {
							preparedStatement=connection.prepareStatement(propertyfile.getPropValues("categoryIdWRTcategorynameSql"));
							preparedStatement.setString(1, manageCategoryItemBean.categoryName);
							preparedStatement.setString(2, cuisineName);
							ResultSet resultSet=preparedStatement.executeQuery();
							
							while(resultSet.next()){
								manageCategoryItemBean.categoryId = resultSet.getInt("category_id");
								
							}
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						e.printStackTrace();
						} finally{
							if(preparedStatement!=null){
								preparedStatement.close();
								}
							}					
				}
		} catch (Exception e) {
			e.printStackTrace();
			}
		manageCategoryItemBean.kitchenId = getKitchenId();
		System.out.println("category id-->"+manageCategoryItemBean.categoryId);
		System.out.println(manageCategoryItemBean.kitchenId);
	}
	

	@Command
	@NotifyChange("*")
	public void onUploadItemBanner(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			
			String yearPath = "/"+"Images_Category_Item_Banner" + "/" +new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "/";
			
			//String yearPath = "Images_Category_Item_Banner" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";

			String contextPath = Sessions.getCurrent().getWebApp().getServletContext().getContextPath();
			
			String imagespath = System.getProperty("catalina.home")+"/"+"webapps"+"/"+contextPath.replace("/", "")+"/"+"view"+"/"+"images";
	 		
			//itemBannerFilePath = "C:\\FooApp_Project_Images\\" + yearPath;
			

			itemBannerFilePath = imagespath;
			
			itemBannerFilePath = itemBannerFilePath + yearPath;
			
			File baseDir = new File(itemBannerFilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			Files.copy(new File(itemBannerFilePath + media.getName()),media.getStreamData());

			itemBannerFileuploaded = true;
			
			itemBannerFilePath = itemBannerFilePath + media.getName();
		
			manageCategoryItemBean.itemBannerFilePath = itemBannerFilePath;

			itemBanner = (AImage) media;
		}
	}

	@Command
	@NotifyChange("*")
	public void onUploadItemImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			
			String yearPath = "/"+"Images_Category_Item_Image" + "/" +new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "/";
			//String yearPath = "Images_Category_Item_Image" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";

			String contextPath = Sessions.getCurrent().getWebApp().getServletContext().getContextPath();
			
			String imagespath = System.getProperty("catalina.home")+"/"+"webapps"+"/"+contextPath.replace("/", "")+"/"+"view"+"/"+"images";
			
			//itemImageFilePath = "C:\\FooApp_Project_Images\\" + yearPath;
			
			itemImageFilePath = imagespath;
			
			itemImageFilePath = imagespath + yearPath;
			
			File baseDir = new File(itemImageFilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			Files.copy(new File(itemImageFilePath + media.getName()),media.getStreamData());

			itemImageFileuploaded = true;
			
			Messagebox.show("Image Sucessfully uploaded!");
			
			itemImageFilePath = itemImageFilePath + media.getName();
		
			manageCategoryItemBean.itemImageFilePath = itemImageFilePath;

			itemImage = (AImage) media;
		}
	}
	
	
	/**
	 * 
	 * This is onClick function for add new item
	 */
	@Command
	@NotifyChange("*")
	public void onClickSaveCategoryItem(){
		if(validateFields()){
			saveCategoryItemData();	
		}
	}
	
	
	
	/**
	 * 
	 * This function saves the category item data to the database(func_save_category_item)
	 */
	public void saveCategoryItemData(){
		String message ="";
		Boolean inserted=false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
				try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("saveCategoryItemSql"));
						
						preparedStatement.setInt(1, manageCategoryItemBean.categoryId);
						if(manageCategoryItemBean.itemName!=null){
							preparedStatement.setString(2, manageCategoryItemBean.itemName);
						}else{
							preparedStatement.setNull(2, Types.NULL);
						}
						
						
						if(manageCategoryItemBean.itemImageFilePath!=null){
							preparedStatement.setString(3, manageCategoryItemBean.itemImageFilePath);
						}else{
							preparedStatement.setNull(3, Types.NULL);
						}
						
						if(manageCategoryItemBean.itemBannerFilePath!=null){
							preparedStatement.setString(4, manageCategoryItemBean.itemBannerFilePath);
						}else{
							preparedStatement.setNull(4, Types.NULL);
						}
						if(manageCategoryItemBean.price!=null){
							preparedStatement.setDouble(5, manageCategoryItemBean.price);
						}else{
							preparedStatement.setDouble(5, Types.DOUBLE);
						}
						
						if(manageCategoryItemBean.inStockQty!=null){
							preparedStatement.setInt(6, manageCategoryItemBean.inStockQty);
						}else{
							preparedStatement.setInt(6, Types.NULL);
						}
						
						if(manageCategoryItemBean.description!=null){
							preparedStatement.setString(7, manageCategoryItemBean.description);
						}
						else{
							preparedStatement.setString(7, "");
						}
						
						if(manageCategoryItemBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(8, "Y");
						}
						else{
							preparedStatement.setString(8, "N");
						}
						
						preparedStatement.setString(9, userName);
						preparedStatement.setString(10, userName);
						preparedStatement.setInt(11, manageCategoryItemBean.kitchenId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							inserted = true;
							System.out.println("Category Item save message =" + message);
						}
						if (inserted) {
							Messagebox.show("Category item Saved Successfully...");
							clearData();
							onLoadItemList();
						}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement != null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
		
	/**
	 * onClickEdit is used to edit data
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean")ManageCategoryItemBean manageCategoryItembean){
	/*	try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet =null;
				try {
					preparedStatement = connection.prepareStatement(propertyfile.getPropValues("editCategoryItemSql"));
					preparedStatement.setInt(1,manageCategoryItembean.itemId);
					manageCategoryItemBean.itemId = manageCategoryItembean.itemId;
					resultSet = preparedStatement.executeQuery();
					while(resultSet.next()){
						manageCategoryItemBean.categoryId = resultSet.getInt("category_id");
						manageCategoryItemBean.categoryName = resultSet.getString("category_name");
						manageCategoryItemBean.itemName = resultSet.getString("item_name");
						manageCategoryItemBean.itemImageFilePath = resultSet.getString("item_image");
						manageCategoryItemBean.itemBannerFilePath = resultSet.getString("item_banner");
						itemImage = new AImage(manageCategoryItemBean.itemImageFilePath);
						itemBanner = new AImage(manageCategoryItemBean.itemBannerFilePath);
						manageCategoryItemBean.price = resultSet.getDouble("item_price");
						manageCategoryItemBean.inStockQty = resultSet.getInt("item_stock_qty");
						manageCategoryItemBean.description = resultSet.getString("item_description");
						if(resultSet.getString("is_active").equals("Y")){
							manageCategoryItemBean.status = "Active";
						}else{
							manageCategoryItemBean.status = "Deactive";
						}
						
						updateButtonVisibility = true;
						saveButtonVisibility = false;
					}	
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
				}	
			}
		} catch (Exception e) {
			// TODO: handle exception
		}*/
		kitchenName = manageCategoryItembean.kitchenName;
		manageCategoryItemBean.kitchenId = manageCategoryItembean.kitchenId;
		cuisineName = manageCategoryItembean.cuisineName;
		manageCategoryItemBean.categoryName = manageCategoryItembean.categoryName;
		manageCategoryItemBean.categoryId = manageCategoryItembean.categoryId;
		manageCategoryItemBean.itemName = manageCategoryItembean.itemName;
		manageCategoryItemBean.itemId = manageCategoryItembean.itemId;
		manageCategoryItemBean.price = manageCategoryItembean.price;
		manageCategoryItemBean.inStockQty = manageCategoryItembean.inStockQty;
		manageCategoryItemBean.description = manageCategoryItembean.description;
		manageCategoryItemBean.status = manageCategoryItembean.status;
		updateButtonVisibility = true;
		saveButtonVisibility = false;
	}
	
	/**
	 * onClickUpdateCategoryItem method is used to update category item data in the database
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickUpdateCategoryItem(){
		String message="";
		Boolean updated= false;
		if(validateFields()){
			try {
				SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("updateCategoryItemSql"));
						preparedStatement.setInt(1, manageCategoryItemBean.categoryId);
						preparedStatement.setString(2, manageCategoryItemBean.itemName);
						
						if(manageCategoryItemBean.itemImageFilePath!=null){
							preparedStatement.setString(3, manageCategoryItemBean.itemImageFilePath);
						}else{
							preparedStatement.setNull(3, Types.NULL);
						}
						
						if(manageCategoryItemBean.itemBannerFilePath!=null){
							preparedStatement.setString(4, manageCategoryItemBean.itemBannerFilePath);
						}else{
							preparedStatement.setNull(4, Types.NULL);
						}
						
						
						/*preparedStatement.setString(3, manageCategoryItemBean.itemImageFilePath);
						preparedStatement.setString(4, manageCategoryItemBean.itemBannerFilePath);*/
						preparedStatement.setDouble(5, manageCategoryItemBean.price);
						preparedStatement.setInt(6, manageCategoryItemBean.inStockQty);
						preparedStatement.setString(7, manageCategoryItemBean.description);
						if(manageCategoryItemBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(8, "Y");
						}
						else{
							preparedStatement.setString(8, "N");
						}
						preparedStatement.setString(9, userName);
						preparedStatement.setString(10, userName);
						preparedStatement.setInt(11, manageCategoryItemBean.kitchenId);
						preparedStatement.setInt(12, manageCategoryItemBean.itemId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message = resultSet.getString(1);
							updated = true;
							System.out.println(message);
						}
						if(updated){
							Messagebox.show("Category item data Updated Successfully...");
							clearData();
							onLoadItemList();
							saveButtonVisibility=true;
							updateButtonVisibility=false;
						}
						
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement != null){
							preparedStatement.close();
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	/**
	 * 
	 * This function validates all the field not empty
	 */
	public Boolean validateFields(){
				if(manageCategoryItemBean.categoryName!=null){
					//if(manageCategoryItemBean.itemName!=null){
					if(manageCategoryItemBean.description!=null){
						//if(manageCategoryItemBean.itemImageFilePath!=null){
						//	if(manageCategoryItemBean.itemBannerFilePath!=null){
								//if(manageCategoryItemBean.price!=null){
									//if(manageCategoryItemBean.inStockQty!=null){
										if(manageCategoryItemBean.status!=null){
											return true;
										}else{
											Messagebox.show("Item will active or not!");
											return false;
										}
									/*}else{
										Messagebox.show("In stock quantity can not be blank!");
										return false;
									}
								}else{
									Messagebox.show("Price can not be blank!");
									return false;
								}*/
							/*}else{
								Messagebox.show("Please upload item banner image!");
								return false;
							}
						}else{
							Messagebox.show("Please upload item image!");
							return false;
						}*/
					/*}else{
						Messagebox.show("Item name can not be blank!");
						return false;
					}*/
					}else{
						Messagebox.show("Description can not be blank!");
						return false;
					}					
				}else{
					Messagebox.show("Category name can not be blank!");
					return false;
				}
			
	}
	
	/**
	 * 
	 * This function is for clearing the data from the fields
	 */
	public void clearData(){
		manageCategoryItemBean.categoryName = "";
		manageCategoryItemBean.categoryName= null;
		manageCategoryItemBean.categoryId = null;
		itemBanner=null;
		itemImage=null;
		manageCategoryItemBean.itemName=null;
		manageCategoryItemBean.price=null;
		manageCategoryItemBean.inStockQty=null;
		manageCategoryItemBean.description=null;
		manageCategoryItemBean.status= null;
		kitchenName = null;
		cuisineName = null;
		if(categoryList.size()>0){
			categoryList.clear();
		}
	}
	
	public ManageCategoryItemBean getManageCategoryItemBean() {
		return manageCategoryItemBean;
	}

	public void setManageCategoryItemBean(
			ManageCategoryItemBean manageCategoryItemBean) {
		this.manageCategoryItemBean = manageCategoryItemBean;
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

	public ArrayList<String> getCityList() {
		return cityList;
	}

	public void setCityList(ArrayList<String> cityList) {
		this.cityList = cityList;
	}

	public ArrayList<String> getAreaList() {
		return areaList;
	}

	public void setAreaList(ArrayList<String> areaList) {
		this.areaList = areaList;
	}

	public ArrayList<String> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(ArrayList<String> categoryList) {
		this.categoryList = categoryList;
	}

	public AImage getItemImage() {
		return itemImage;
	}

	public void setItemImage(AImage itemImage) {
		this.itemImage = itemImage;
	}

	public String getItemImageFilePath() {
		return itemImageFilePath;
	}

	public void setItemImageFilePath(String itemImageFilePath) {
		this.itemImageFilePath = itemImageFilePath;
	}

	public boolean isItemImageFileuploaded() {
		return itemImageFileuploaded;
	}

	public void setItemImageFileuploaded(boolean itemImageFileuploaded) {
		this.itemImageFileuploaded = itemImageFileuploaded;
	}

	public AImage getItemBanner() {
		return itemBanner;
	}

	public void setItemBanner(AImage itemBanner) {
		this.itemBanner = itemBanner;
	}

	public String getItemBannerFilePath() {
		return itemBannerFilePath;
	}

	public void setItemBannerFilePath(String itemBannerFilePath) {
		this.itemBannerFilePath = itemBannerFilePath;
	}

	public boolean isItemBannerFileuploaded() {
		return itemBannerFileuploaded;
	}

	public void setItemBannerFileuploaded(boolean itemBannerFileuploaded) {
		this.itemBannerFileuploaded = itemBannerFileuploaded;
	}

	public AImage getSubitemImage() {
		return subitemImage;
	}

	public void setSubitemImage(AImage subitemImage) {
		this.subitemImage = subitemImage;
	}

	public String getSubitemImageFilePath() {
		return subitemImageFilePath;
	}

	public void setSubitemImageFilePath(String subitemImageFilePath) {
		this.subitemImageFilePath = subitemImageFilePath;
	}

	public boolean isSubitemImageFileuploaded() {
		return subitemImageFileuploaded;
	}

	public void setSubitemImageFileuploaded(boolean subitemImageFileuploaded) {
		this.subitemImageFileuploaded = subitemImageFileuploaded;
	}

	public Boolean getImageBoxVisibility() {
		return imageBoxVisibility;
	}

	public void setImageBoxVisibility(Boolean imageBoxVisibility) {
		this.imageBoxVisibility = imageBoxVisibility;
	}

	public ArrayList<ManageCategoryItemBean> getManageCategoryItemBeanList() {
		return manageCategoryItemBeanList;
	}

	public void setManageCategoryItemBeanList(
			ArrayList<ManageCategoryItemBean> manageCategoryItemBeanList) {
		this.manageCategoryItemBeanList = manageCategoryItemBeanList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getUpdateButtonVisibility() {
		return updateButtonVisibility;
	}

	public void setUpdateButtonVisibility(Boolean updateButtonVisibility) {
		this.updateButtonVisibility = updateButtonVisibility;
	}

	public Boolean getSaveButtonVisibility() {
		return saveButtonVisibility;
	}

	public void setSaveButtonVisibility(Boolean saveButtonVisibility) {
		this.saveButtonVisibility = saveButtonVisibility;
	}


	public ArrayList<String> getCuisineList() {
		return cuisineList;
	}


	public void setCuisineList(ArrayList<String> cuisineList) {
		this.cuisineList = cuisineList;
	}


	public PropertyFile getPropertyfile() {
		return propertyfile;
	}


	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}

	public String getCuisineName() {
		return cuisineName;
	}

	public void setCuisineName(String cuisineName) {
		this.cuisineName = cuisineName;
	}

	public ArrayList<String> getKitchenList() {
		return kitchenList;
	}

	public void setKitchenList(ArrayList<String> kitchenList) {
		this.kitchenList = kitchenList;
	}

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}
	
	
}
