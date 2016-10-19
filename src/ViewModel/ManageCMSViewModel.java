package ViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.zkoss.bind.BindContext;
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
import org.zkoss.zhtml.Pre;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.ManageCMSBean;
import ViewModel.PropertyFile;


public class ManageCMSViewModel {

		private ManageCMSBean manageCMSBean = new ManageCMSBean();
		
		private ArrayList<ManageCMSBean> manageCMSBeanList = new ArrayList<ManageCMSBean>();
		
		Session session = null;
		
		private Connection connection = null;
		
		private String userName = "";
		
		private boolean bannerfileuploaded = false;
		
		private String bannerImagefilePath;

		private AImage pageBannerImage;
			
		private Boolean saveButtonVisibility = true;
		
		private Boolean updateButtonVisibility = false;
		
		private ArrayList<String> cityList= new ArrayList<String>();
		
		private ArrayList<String> areaList= new ArrayList<String>();
		
		PropertyFile propertyfile = new PropertyFile();
		
		@AfterCompose
		public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

			Selectors.wireComponents(view, this, false);
			
			session= Sessions.getCurrent();
			
			connection=(Connection) session.getAttribute("sessionConnection");
			//connection = DbConnection.getConnection();
			userName = (String) session.getAttribute("login");
		
			connection.setAutoCommit(true);
			
			onLoadCMSList();
			//onLoadCityList();
		}
		
		/**
		 * 
		 * onLoadCMSList is used to reload the grid view "vw_cms_data"
		 * 
		 */
		public void onLoadCMSList(){
			
			if(manageCMSBeanList.size()>0){
				manageCMSBeanList.clear();
			}
			try {
				SQL:{	
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						String sql = propertyfile.getPropValues("sqlcmsonload");
					try {
							preparedStatement = connection.prepareStatement(sql);
							resultSet = preparedStatement.executeQuery();
							
							while(resultSet.next()){
								
								ManageCMSBean manageCMSBean = new ManageCMSBean();
								manageCMSBean.areaId = resultSet.getInt("area_id");
								manageCMSBean.pageId = resultSet.getInt("page_id");
								manageCMSBean.pageTitle = resultSet.getString("page_title");
								manageCMSBean.bannerImage = new AImage(resultSet.getString("page_background_image"));
							
								if(resultSet.getString("show_in_app_menu").equals("N")){
									manageCMSBean.inAppMenu = "No";
								}
								else{
									manageCMSBean.inAppMenu = "Yes";
								}
								
								manageCMSBeanList.add(manageCMSBean);
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
		
		/**
		 * 
		 * GlobalReload is used to reload the grid when child window is detach
		 */
		@GlobalCommand
		@NotifyChange("*")
		public void globalReload(){
			onLoadCMSList();
			clearData();
			updateButtonVisibility = false;
			saveButtonVisibility = true;
		}
		
	
		/**
		 * onLoadCityList method is used to load all the city name list from database
		 * 
		 */
		public void onLoadCityList(){
			
			if(cityList!=null){
				cityList.clear();
			}
			try {
					sql1:{
							PreparedStatement preparedStatement=null;
							try {
							
									String sql= propertyfile.getPropValues("sqlcitylist");																	 
									
									preparedStatement=connection.prepareStatement(sql);
									
									ResultSet rs=preparedStatement.executeQuery();
									
									while(rs.next()){
								
										cityList.add(rs.getString(1));
										
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
		}
		
		/**
		 * onSelectcityName method is used to load the area name by selecting the city name from dropdown
		 * 
		 */
		@Command
		@NotifyChange("*")
		public void onSelectcityName(){
			if(areaList.size()>0){
				areaList.clear();
			}
			manageCMSBean.areaName="";
			try {
				sql1:{
						PreparedStatement preparedStatement=null;
						try {
						
								preparedStatement=connection.prepareStatement(propertyfile.getPropValues("areaIdWRTcityNameSql"));
								preparedStatement.setString(1, manageCMSBean.cityName);
								ResultSet resultSet=preparedStatement.executeQuery();
								
								while(resultSet.next()){
									areaList.add(resultSet.getString("area_name"));	
									manageCMSBean.areaId = resultSet.getInt("area_id");
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
		}
		
		/**
		 * onSelectAreaName method is used to load the area name by selecting the city name from dropdown
		 * 
		 */
		@Command
		@NotifyChange("*")
		public void onSelectAreaName(){
			try {
				sql1:{
						PreparedStatement preparedStatement=null;
						try {
						
								preparedStatement=connection.prepareStatement(propertyfile.getPropValues("areaIdWRTareaNameSql"));
								preparedStatement.setString(1, manageCMSBean.areaName);
								ResultSet resultSet=preparedStatement.executeQuery();
								
								while(resultSet.next()){
									manageCMSBean.areaId = resultSet.getInt("area_id");
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
		}
		
		/**
		 * onUploadBanerImage is used to upload banner image
		 * 
		 */
		@Command
		@NotifyChange("*")
		public void onUploadBanerImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

			UploadEvent upEvent = null;
			
			Object objUploadEvent = ctx.getTriggerEvent();
			
			if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
				upEvent = (UploadEvent) objUploadEvent;
			}

			if (upEvent != null) {

				Media media = upEvent.getMedia();
				
				String yearPath = "Images_PageBanner" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";

				bannerImagefilePath = "C:\\FooApp_Project_Images\\" + yearPath;
				File baseDir = new File(bannerImagefilePath);
				if (!baseDir.exists()) {
					baseDir.mkdirs();
				}

				Files.copy(new File(bannerImagefilePath + media.getName()),media.getStreamData());

				bannerfileuploaded = true;
				bannerImagefilePath = bannerImagefilePath + media.getName();
			
				manageCMSBean.pageBannerPicturePath = bannerImagefilePath;

				pageBannerImage = (AImage) media;
			}
		}

		/**
		 * 
		 * onClickSaveCMSPage is method for click on the save button
		 */
		@Command
		@NotifyChange("*")
		public void onClickSaveCMSPage(){
			if(validateFields()){
				saveCMSPageData();
			}
		}


		/**
		 * 
		 * saveCMSPageData is used to save the data to database
		 */
		public void saveCMSPageData(){
			
			String message="";
			Boolean inserted =false;
		
			try {
					SQL:{
							PreparedStatement preparedStatement = null;
							ResultSet resultSet = null;
							
						try {
								preparedStatement = connection.prepareStatement(propertyfile.getPropValues("saveCmsSql"));
								if(manageCMSBean.areaId!=null){
									preparedStatement.setInt(1, manageCMSBean.areaId);
								}else{
									preparedStatement.setNull(1, Types.NULL);
								}
								
								preparedStatement.setString(2, manageCMSBean.pageTitle);
								preparedStatement.setString(3, manageCMSBean.pageContent);
								preparedStatement.setString(4, manageCMSBean.pageBanner);
								preparedStatement.setString(5, manageCMSBean.pageBannerPicturePath);
								if(manageCMSBean.status.equalsIgnoreCase("Active")){
									preparedStatement.setString(6, "Y");
								}else{
									preparedStatement.setString(6, "N");
								}
								/*if(manageCMSBean.showInAppChecked){
									preparedStatement.setString(6, "Y");
								}
								else{
									preparedStatement.setString(6, "N");
								}*/
								preparedStatement.setString(7, userName);
								preparedStatement.setString(8, userName);
																
								resultSet = preparedStatement.executeQuery();
								if (resultSet.next()) {
									message = resultSet.getString(1);
									inserted=true;
									System.out.println("CMS save message =" + message);
								}
								if (inserted) {
									Messagebox.show("CMS data saved Successfully...");
									clearData();
									onLoadCMSList();
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
		
		/**
		 * 
		 * clearData is used to clear all textbox values
		 */
		public void clearData(){
			manageCMSBean.cityName = null;
			manageCMSBean.areaName = null;
			manageCMSBean.pageTitle = null;
			manageCMSBean.pageContent = null;
			manageCMSBean.pageBanner = null;
			pageBannerImage = null;
			manageCMSBean.showInAppChecked = false;
			manageCMSBean.status = null;
		}
		
		/**
		 * onClickEdit is used to edit data
		 * 
		 */
		@Command
		@NotifyChange("*")
		public void onClickEdit(@BindingParam("bean")ManageCMSBean manageCMSbean){
		manageCMSBean.pageTitle = manageCMSbean.pageTitle;
		manageCMSBean.pageContent = manageCMSbean.pageContent;
		manageCMSBean.pageBanner = manageCMSbean.pageBanner;
		manageCMSBean.pageBannerPicturePath = manageCMSbean.pageBannerPicturePath;
		try {
			pageBannerImage = new AImage(manageCMSBean.pageBannerPicturePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			/*try {
				manageCMSBean = new ManageCMSBean();
				
				SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet =null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("editCmsSql"));
						preparedStatement.setInt(1,manageCMSbean.pageId);
						manageCMSBean.pageId = manageCMSbean.pageId;
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							manageCMSBean.cityName = resultSet.getString("city_name");
							manageCMSBean.areaName = resultSet.getString("area_name");
							manageCMSBean.areaId = resultSet.getInt("area_id");
							manageCMSBean.pageTitle = resultSet.getString("page_title");
							manageCMSBean.pageBanner = resultSet.getString("page_banner");
							manageCMSBean.pageContent = resultSet.getString("page_content");
							manageCMSBean.pageBannerPicturePath = resultSet.getString("page_background_image");
							pageBannerImage = new AImage(manageCMSBean.pageBannerPicturePath);
							
							if(resultSet.getString("show_in_app_menu").equals("Y")){
								manageCMSBean.showInAppChecked = true;
								System.out.println("true part-----------"+manageCMSBean.showInAppChecked);
							}else{
								manageCMSBean.showInAppChecked = false;
								System.out.println("false part-----------"+manageCMSBean.showInAppChecked);
							}
							if(resultSet.getString("show_in_app_menu").equals("Y")){
								manageCMSBean.status = "Active";
							}else{
								manageCMSBean.status = "Inactive";
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
		}
		
		/**
		 * onClickUpdateCMSPage is used to update and save to database
		 * 
		 */	
		@Command
		@NotifyChange("*")
		public void onClickUpdateCMSPage(){
			String message="";
			Boolean inserted = false;
			if(validateFields()){
				try {
					SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						try {
							preparedStatement = connection.prepareStatement(propertyfile.getPropValues("updateCmsSql"));
							System.out.println(preparedStatement);
							if(manageCMSBean.areaId!=null){
								preparedStatement.setInt(1, manageCMSBean.areaId);
							}else{
								preparedStatement.setNull(1, Types.NULL);
							}
							
							preparedStatement.setString(2, manageCMSBean.pageTitle);
							preparedStatement.setString(3, manageCMSBean.pageBanner);
							preparedStatement.setString(4, manageCMSBean.pageContent);
							preparedStatement.setString(5, manageCMSBean.pageBannerPicturePath);
							/*if(manageCMSBean.showInAppChecked){
								preparedStatement.setString(6, "Y");
							}
							else{
								preparedStatement.setString(6, "N");
							}*/
							if(manageCMSBean.status.equalsIgnoreCase("Active")){
								preparedStatement.setString(6, "Y");
							}else{
								preparedStatement.setString(6, "N");
							}
							preparedStatement.setString(7, userName);
							preparedStatement.setString(8, userName);
							preparedStatement.setInt(9, manageCMSBean.pageId);
							
							resultSet = preparedStatement.executeQuery();
							if(resultSet.next()){
								message=resultSet.getString(1);
								inserted = true;
								System.out.println("CMS Data Details"+message);
							}
							if(inserted){
								Messagebox.show("CMS data Updated Successfully...");
								clearData();
								onLoadCMSList();
								updateButtonVisibility = false;
								saveButtonVisibility = true;
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
		 * onClickDelete is used to delete the data after confirmation box
		 */
		@Command
		@NotifyChange("*")
		public void onClickDelete(@BindingParam("bean")ManageCMSBean manageCMSbean){
			System.out.println(manageCMSbean.areaId+","+manageCMSbean.pageTitle);
			Map<String, Object> parentMap = new HashMap<String, Object>();
			
			parentMap.put("parentCMSObject", manageCMSbean);
			
			Window win = (Window) Executions.createComponents("cmsDeleteConfirmMessageBox.zul", null, parentMap);
			win.doModal();
		}
		
	
		/**
		 * validateFields is used to validate the fields
		 * 
		 */
		public Boolean validateFields(){
			if(manageCMSBean.pageTitle!=null){
				if(manageCMSBean.pageBanner!=null){
					if(manageCMSBean.pageContent!=null){
					//	if(pageBannerImage!=null){
							if(manageCMSBean.status!=null){
								return true;
							}else{
								Messagebox.show("Shown in app menu status required!");
								return false;
							}
						/*}else{
							Messagebox.show("Please upload image!");
							return false;
						}*/
					}else{
						Messagebox.show("Please give page content!");
						return false;
					}
				}else{
					Messagebox.show("Please give banner name!");
					return false;
				}
			}else{
				Messagebox.show("Please give page title!");
				return false;
			}
		}
		
		public ManageCMSBean getManageCMSBean() {
			return manageCMSBean;
		}

		public void setManageCMSBean(ManageCMSBean manageCMSBean) {
			this.manageCMSBean = manageCMSBean;
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

		public boolean isBannerfileuploaded() {
			return bannerfileuploaded;
		}

		public void setBannerfileuploaded(boolean bannerfileuploaded) {
			this.bannerfileuploaded = bannerfileuploaded;
		}

		public String getBannerImagefilePath() {
			return bannerImagefilePath;
		}

		public void setBannerImagefilePath(String bannerImagefilePath) {
			this.bannerImagefilePath = bannerImagefilePath;
		}

		public AImage getPageBannerImage() {
			return pageBannerImage;
		}

		public void setPageBannerImage(AImage pageBannerImage) {
			this.pageBannerImage = pageBannerImage;
		}

		public ArrayList<ManageCMSBean> getManageCMSBeanList() {
			return manageCMSBeanList;
		}

		public void setManageCMSBeanList(ArrayList<ManageCMSBean> manageCMSBeanList) {
			this.manageCMSBeanList = manageCMSBeanList;
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

		
}
