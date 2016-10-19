package ViewModel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.print.DocFlavor.STRING;

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
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sun.org.apache.regexp.internal.recompile;

import Bean.ManageCMSBean;
import Bean.ManageCategoryBean;
import Bean.ManageCuisinBean;
import Bean.ManageDealBean;
import Bean.TaxesBean;


public class ManageCategoryViewModel {
	
	private ManageCategoryBean manageCategoryBean = new ManageCategoryBean();
	
	private ArrayList<ManageCategoryBean> manageCategoryBeanList = new ArrayList<ManageCategoryBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private AImage categoryeBannerImage;
	
	private String categoryBannerImagefilePath;
	
	private boolean categoryBannerFileuploaded = false;
		
	private AImage categoryImage;
	
	private String categoryImagefilePath;
	
	private boolean categoryImageFileuploaded = false;
	
	private ArrayList<String> cuisinList =  new ArrayList<String>();
	
	public Boolean saveButtonVisibility = true;
	
	public Boolean updateButtonVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile(); 
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		onLoadCategoryList();
		onloadCuisinList();
		
	}
	
	/**
	 * 
	 * GlobalReload is used to reload the grid when child window is detach
	 */
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		onLoadCategoryList();
		clearData();
		updateButtonVisibility = false;
		saveButtonVisibility = true;
	}
	
	/**
	 * 
	 * Load saved category list
	 */
	public void onLoadCategoryList(){
		if(manageCategoryBeanList.size()>0){
			manageCategoryBeanList.clear();
		}
		try {
			SQL:{	
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
				try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("onLoadCategoryListSql"));
						resultSet = preparedStatement.executeQuery();
						//String cityname = null;
						while(resultSet.next()){
							ManageCategoryBean manageCategoryBean = new ManageCategoryBean();
							manageCategoryBean.categoryId =  resultSet.getInt("category_id");
							manageCategoryBean.cuisinName = resultSet.getString("cuisin_name");
							manageCategoryBean.cuisinId = resultSet.getInt("cuisin_id");
						/*	manageCategoryBean.cityName = resultSet.getString("city_name");
							String tempCityname = manageCategoryBean.cityName;
							if(manageCategoryBean.cityName.equals(cityname)){
								manageCategoryBean.cityName="";
								manageCategoryBean.citynamevisibility=false;
							}
							manageCategoryBean.areaName = resultSet.getString("area_name");
							manageCategoryBean.areaId = resultSet.getInt("area_id");*/
							manageCategoryBean.categoryName = resultSet.getString("category_name");
							manageCategoryBean.categoryImagePath = resultSet.getString("category_image");
						//	manageCategoryBean.categoryImage = new AImage(manageCategoryBean.categoryImagePath);
							manageCategoryBean.categoryBannerImagePath = resultSet.getString("category_banner");
							try {
								manageCategoryBean.categoryBannerImage = new AImage(manageCategoryBean.categoryBannerImagePath);
							} catch (Exception e) {
								manageCategoryBean.categoryBannerImage = null;
							}
							
							if(resultSet.getString("is_active").equalsIgnoreCase("Y")){
								manageCategoryBean.status = "Active";
							}else{
								manageCategoryBean.status = "Deactive";
							}
							//cityname = tempCityname;
							manageCategoryBeanList.add(manageCategoryBean);
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
	
	/**
	 * Load all cuisins
	 */
	public void onloadCuisinList(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT * FROM fapp_cuisins WHERE is_delete='N' AND is_active='Y'";
				try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							
							cuisinList.add( resultSet.getString("cuisin_name"));
						}
				} catch (Exception e) {
					Messagebox.show("Error Due To::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	
	/**
	 * On select cuisine name
	 */
	@Command
	@NotifyChange("*")
	public void onSelectCuisine(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql ="SELECT cuisin_id from fapp_cuisins WHERE cuisin_name=?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, manageCategoryBean.cuisinName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							manageCategoryBean.cuisinId = resultSet.getInt(1);
						}
					} catch (Exception e) {
						Messagebox.show("Error Due To::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	
	/**
	 * 
	 *Upload category banner image
	 */
	@Command
	@NotifyChange("*")
	public void onUploadCategoryBannerImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			
						
			String yearPath = "Images_Category_Banner_images" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";

			categoryBannerImagefilePath = "C:\\FooApp_Project_Images\\" + yearPath;
			File baseDir = new File(categoryBannerImagefilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			Files.copy(new File(categoryBannerImagefilePath + media.getName()),media.getStreamData());

			categoryBannerFileuploaded = true;
			categoryBannerImagefilePath = categoryBannerImagefilePath + media.getName();
		
			manageCategoryBean.categoryBannerImagePath = categoryBannerImagefilePath;

			categoryeBannerImage = (AImage) media;
			
		}
	}
	
	/**
	 * 
	 *Upload category image
	 */
	@Command
	@NotifyChange("*")
	public void onUploadCategoryImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			
			String yearPath = "/"+"Images_Categoryimages" + "/" +new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "/";
						
			//String yearPath = "Images_Categoryimages" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";
			
			String contextPath = Sessions.getCurrent().getWebApp().getServletContext().getContextPath();
			
			String imagespath = System.getProperty("catalina.home")+"/"+"webapps"+"/"+contextPath.replace("/", "")+"/"+"view"+"/"+"images";
	 		
			categoryImagefilePath = imagespath;
			
			//categoryImagefilePath = "C:\\FooApp_Project_Images\\" + yearPath;
			categoryImagefilePath = categoryImagefilePath + yearPath;
		        
			File baseDir = new File(categoryImagefilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			Files.copy(new File(categoryImagefilePath + media.getName()),media.getStreamData());

			Messagebox.show("Image Sucessfully uploaded!");
			
			categoryImageFileuploaded = true;
			categoryImagefilePath = categoryImagefilePath + media.getName();
		
			manageCategoryBean.categoryImagePath = categoryImagefilePath;

			categoryImage = (AImage) media;
		}
	}
	
	/**
	 * 
	 * on click Save button 
	 */
	@Command
	@NotifyChange("*")
	public void	onClickSaveCategory(){
		if(validateFields()){
			saveCategoryData();
		}
	}
	
	/**
	 * 
	 * Saving category data 
	 */
	public void saveCategoryData(){
		String message="";
		Boolean inserted=false;
		try {
			SQL:{	
					PreparedStatement prepareStatement= null;
					ResultSet resultSet = null;
				try {
						prepareStatement=connection.prepareStatement(propertyfile.getPropValues("saveCategorySql"));
						
						prepareStatement.setInt(1, manageCategoryBean.cuisinId);
						prepareStatement.setString(2, manageCategoryBean.categoryName);
						prepareStatement.setString(3, manageCategoryBean.categoryImagePath);
						prepareStatement.setString(4, manageCategoryBean.categoryBannerImagePath);
						if(manageCategoryBean.status.equalsIgnoreCase("Active")){
							prepareStatement.setString(5, "Y");
						}
						else{
							prepareStatement.setString(5, "N");
						}
						prepareStatement.setString(6, userName);
						prepareStatement.setString(7, userName);
						
						resultSet = prepareStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							inserted=true;
							System.out.println(message);
						}
						if(inserted){
							Messagebox.show("Category data saved successfully!!");
							clearData();
							onLoadCategoryList();
						}
				} catch (Exception e) {
					Messagebox.show("Error Due To::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(prepareStatement!=null){
						prepareStatement.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	

	/**
	 * onClickEdit is used to edit data
	 * @throws IOException 
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean")ManageCategoryBean manageCategorybean) throws IOException{
		manageCategoryBean.cuisinName = manageCategorybean.cuisinName;
		manageCategoryBean.cuisinId = manageCategorybean.cuisinId;
		manageCategoryBean.categoryName = manageCategorybean.categoryName;
		manageCategoryBean.categoryId = manageCategorybean.categoryId;
		manageCategoryBean.categoryImage = manageCategorybean.categoryImage ;
		manageCategoryBean.categoryImagePath = manageCategorybean.categoryImagePath;
		categoryImage = manageCategoryBean.categoryImage;
		manageCategoryBean.categoryBannerImage = manageCategorybean.categoryBannerImage ;
		manageCategoryBean.categoryBannerImagePath = manageCategorybean.categoryBannerImagePath;
		categoryeBannerImage = manageCategoryBean.categoryBannerImage;
		manageCategoryBean.status = manageCategorybean.status;
		updateButtonVisibility = true;
		saveButtonVisibility = false;
	}
	
	/**
	 * onClickUpdateDeal method is used to update deal data in the database
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickUpdateCategory(){
		String message="";
		Boolean updated= false;
		//if(validateFields()){
			try {
				SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("updateCategorySql"));
						preparedStatement.setInt(1, manageCategoryBean.cuisinId);
						preparedStatement.setString(2, manageCategoryBean.categoryName);
						preparedStatement.setString(3, manageCategoryBean.categoryImagePath);
						preparedStatement.setString(4, manageCategoryBean.categoryBannerImagePath);
						if(manageCategoryBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(5, "Y");
						}
						else{
							preparedStatement.setString(5, "N");
						}
						preparedStatement.setString(6, userName);
						preparedStatement.setString(7, userName);
						preparedStatement.setInt(8, manageCategoryBean.categoryId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							updated = true;
							System.out.println(message);
						}
						if(updated){
							Messagebox.show("Category data Updated Successfully...");
							clearData();
							onLoadCategoryList();
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
		//}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean") final ManageCategoryBean managecategorybean){
		Messagebox.show("Are you sure to delete?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
		          deleteCategory(managecategorybean);
		          BindUtils.postGlobalCommand(null, null, "globalReload", null);
				   Messagebox.show("Category deleted sucessfully!");
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
	}
	
	public void deleteCategory(ManageCategoryBean managecategorybean){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE food_category"
				                +" SET is_delete='Y'"
				                +" WHERE category_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, managecategorybean.categoryId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							System.out.println("Category deleted sucessfully!");
						}
								
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	
	/**
	 * 
	 * clear data
	 */
	public void clearData(){
		manageCategoryBean.cuisinName = null;
		manageCategoryBean.categoryName= null;
		categoryeBannerImage=null;
		categoryImage=null;
		manageCategoryBean.status = null;
	}
	
	
	public Boolean validateFields(){
		if(manageCategoryBean.cuisinName!=null){
			if(manageCategoryBean.categoryName!=null){
				/*if(manageCategoryBean.categoryImagePath!=null){
					if(manageCategoryBean.categoryBannerImagePath!=null){*/
						if(manageCategoryBean.status!=null){
							return true;
						}else{
							Messagebox.show("Category will active or not!");
							return false;
						}
						
					/*}else{
						Messagebox.show("Please upload category banner image!");
						return false;
					}
				}else{
					Messagebox.show("Please upload category image!");
					return false;
				}*/
			}else{
				Messagebox.show("Please fill category name!");
				return false;
			}
		}else{
			Messagebox.show("Please choose cuisine!");
			return false;
		}
	}
	
	/**
	 * 
	 * delete category message box confirmation
	 *//*
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean")ManageCategoryBean manageCategorybean){
		
		Map<String, Object> parentMap = new HashMap<String, Object>();
		
		parentMap.put("parentCategoryObject", manageCategorybean);
		
		Window win = (Window) Executions.createComponents("categoryDeleteConfirmationMessageBox.zul", null, parentMap);
		
		win.doModal();
	}*/
	
	public ManageCategoryBean getManageCategoryBean() {
		return manageCategoryBean;
	}

	public void setManageCategoryBean(ManageCategoryBean manageCategoryBean) {
		this.manageCategoryBean = manageCategoryBean;
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

	public AImage getCategoryeBannerImage() {
		return categoryeBannerImage;
	}

	public void setCategoryeBannerImage(AImage categoryeBannerImage) {
		this.categoryeBannerImage = categoryeBannerImage;
	}

	public String getCategoryBannerImagefilePath() {
		return categoryBannerImagefilePath;
	}

	public void setCategoryBannerImagefilePath(String categoryBannerImagefilePath) {
		this.categoryBannerImagefilePath = categoryBannerImagefilePath;
	}

	public boolean isCategoryBannerFileuploaded() {
		return categoryBannerFileuploaded;
	}

	public void setCategoryBannerFileuploaded(boolean categoryBannerFileuploaded) {
		this.categoryBannerFileuploaded = categoryBannerFileuploaded;
	}

	public AImage getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(AImage categoryImage) {
		this.categoryImage = categoryImage;
	}

	public String getCategoryImagefilePath() {
		return categoryImagefilePath;
	}

	public void setCategoryImagefilePath(String categoryImagefilePath) {
		this.categoryImagefilePath = categoryImagefilePath;
	}

	public boolean isCategoryImageFileuploaded() {
		return categoryImageFileuploaded;
	}

	public void setCategoryImageFileuploaded(boolean categoryImageFileuploaded) {
		this.categoryImageFileuploaded = categoryImageFileuploaded;
	}

	public ArrayList<ManageCategoryBean> getManageCategoryBeanList() {
		return manageCategoryBeanList;
	}

	public void setManageCategoryBeanList(
			ArrayList<ManageCategoryBean> manageCategoryBeanList) {
		this.manageCategoryBeanList = manageCategoryBeanList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public ArrayList<String> getCuisinList() {
		return cuisinList;
	}

	public void setCuisinList(ArrayList<String> cuisinList) {
		this.cuisinList = cuisinList;
	}

	

}
