package ViewModel;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.SeekableByteChannel;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

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
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.ManageCouponsBean;
import Bean.ManageDealBean;


public class ManageDealViewModel {
	
	private ManageDealBean manageDealBean = new ManageDealBean();
	
	private ArrayList<ManageDealBean> manageDealBeanList = new ArrayList<ManageDealBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private ArrayList<String> cityList= new ArrayList<String>();
	
	private ArrayList<String> areaList= new ArrayList<String>();
	
	private boolean dealBannerFileuploaded = false;
	
	private String dealBannerImagefilePath;
	
	private String filePath;

	private AImage dealPageBannerImage;
	
	public Boolean saveButtonVisibility = true;
	
	public Boolean updateButtonVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile();
	
	public boolean iFrameVisibility = false;
	
	public boolean imageContentVisibility = false;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		connection.setAutoCommit(true);
		
		onLoadCityList();
		
		onSelectcityName();
		onLoadDealList();
		//String fromDb = "C:\Joget-v4-Enterprise\apache-tomcat-7.0.62/webapps/FoodHomeDeliverySystem/view/images/Images_Deal_PageBanner/28-Feb-2016/11-640x483.png";
			
		/*System.out.println("\nApp Deployed Directory path: " + Sessions.getCurrent().getWebApp().getServletContext().getRealPath("WEB-INF/../"));
		  System.out.println("getContextPath(): " + Sessions.getCurrent().getWebApp().getServletContext().getContextPath());
		  String contextPath = Sessions.getCurrent().getWebApp().getServletContext().getContextPath();
		  System.out.println("Cataline Home:"+System.getProperty("catalina.home"));
		  String imagespath = System.getProperty("catalina.home")+File.separator+"webapps"+File.separator+contextPath.replace("/", "")+File.separator+"view"+File.separator+"images";
 		  System.out.println("Apache Tomcat Server: " + Sessions.getCurrent().getWebApp().getServletContext().getServerInfo());
		  System.out.println("Servlet API version: " + Sessions.getCurrent().getWebApp().getServletContext().getMajorVersion() + "." +Sessions.getCurrent().getWebApp().getServletContext().getMinorVersion());
		  System.out.println("Tomcat Project Name: " + Sessions.getCurrent().getWebApp().getServletContext().getServletContextName());
*/	}
	
	
	/*
	 * 
	 * GlobalReload is used to reload the grid when child window is detach
	 */
	
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		onLoadDealList();
		clearData();
		updateButtonVisibility = false;
		saveButtonVisibility = true;
	}
	
	/**
	 * 
	 * onLoadDealList method is used to load all the deal details from database
	 */
	public void onLoadDealList(){
		if(manageDealBeanList.size()>0){
			manageDealBeanList.clear();
		}
		try {
			SQL:{	
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String show="";
				try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("onLoadDealListSql"));
						resultSet = preparedStatement.executeQuery();
						
						while(resultSet.next()){
							ManageDealBean manageDealBean = new ManageDealBean();
							manageDealBean.dealId = resultSet.getInt("deal_id");
							manageDealBean.title = resultSet.getString("deal_title");
							manageDealBean.dealBannerPicturePath = resultSet.getString("deal_banner");
							manageDealBean.cityName = resultSet.getString("city_name");
							manageDealBean.areaName = resultSet.getString("area_name");
							manageDealBean.areaId = resultSet.getInt("area_id");
							/*String dbDeal = manageDealBean.dealBannerPicturePath;
							System.out.println("dbDeal->"+dbDeal);
							System.out.println("After replace:->"+dbDeal.replace("C:\\apache-tomcat-7.0.62/webapps/", "appsquad.cloudapp.net:8080/"));*/
							manageDealBean.fromDate = resultSet.getDate("deal_from_date");
							manageDealBean.toDate = resultSet.getDate("deal_to_date");
							try {
								manageDealBean.databaseBannerImage = new AImage(manageDealBean.dealBannerPicturePath);
							} catch (Exception e) {
								// TODO: handle exception
								manageDealBean.databaseBannerImage = null;
							}
							
							manageDealBeanList.add(manageDealBean);
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
								preparedStatement=connection.prepareStatement(propertyfile.getPropValues("sqlcitylist"));
								
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
		manageDealBean.areaName="";
		try {
			sql1:{
					PreparedStatement preparedStatement=null;
					try {
							preparedStatement=connection.prepareStatement(propertyfile.getPropValues("areaIdWRTcityNameSql"));
							preparedStatement.setString(1, manageDealBean.cityName);
							ResultSet resultSet=preparedStatement.executeQuery();
							
							while(resultSet.next()){
								areaList.add(resultSet.getString("area_name"));	
								manageDealBean.areaId = resultSet.getInt("area_id");
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
							preparedStatement.setString(1, manageDealBean.areaName);
							ResultSet resultSet=preparedStatement.executeQuery();
							
							while(resultSet.next()){
								manageDealBean.areaId = resultSet.getInt("area_id");
								System.out.println(manageDealBean.areaId+","+manageDealBean.areaName+","+manageDealBean.cityName);
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
	 *onUploadDealBanerImage method is used to upload deal banner image 
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onUploadDealBanerImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			String yearPath = "/"+"Images_Deal_PageBanner" + "/" +new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "/";
			
			
			String contextPath = Sessions.getCurrent().getWebApp().getServletContext().getContextPath();
			System.out.println("Context path-"+contextPath);
			String imagespath = System.getProperty("catalina.home")+"/"+"webapps"+"/"+contextPath.replace("/", "")+"/"+"view"+"/"+"images";
	 		  System.out.println("imagespath->"+imagespath);
			//filePath = Sessions.getCurrent().getWebApp().getServletContext().getContextPath();
	        filePath = imagespath; 
			System.out.println("File path:"+filePath);
	         filePath = filePath + yearPath;
	         System.out.println("New file path::"+filePath);
	         
	         File baserDir = new File(filePath);
	         if(!baserDir.exists()){
	        	 baserDir.mkdirs();
	         }
	         
			/*String yearPath = "Images_Deal_PageBanner" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";

			dealBannerImagefilePath = "C:\\FooApp_Project_Images\\" + yearPath;
			File baseDir = new File(dealBannerImagefilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}
			Files.copy(new File(dealBannerImagefilePath + media.getName()),media.getStreamData());*/

			Files.copy(new File(filePath + media.getName()),media.getStreamData());

			
			 Messagebox.show("Image Sucessfully uploaded!");
	             //    + filePath + " ]");
			
			dealBannerFileuploaded = true;
			
			filePath = filePath + media.getName() ; 
			////dealBannerImagefilePath = dealBannerImagefilePath + media.getName();
		
			//manageDealBean.dealBannerPicturePath = dealBannerImagefilePath;

			manageDealBean.dealBannerPicturePath = filePath;
			dealPageBannerImage = (AImage) media;
			
		}
	}

	/**
	 * 
	 * onClickSaveDeal method is used to save the deal data to the database with validation 
	 */
	@Command
	@NotifyChange("*")
	public void onClickSaveDeal(){
		if(validateFields()){
			saveDealData();
		}
	}
	
	/**
	 * saveDealData method contains the insertion query  
	 * 
	 */
	public void saveDealData(){
		String message ="";
		Boolean inserted=false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
				try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("saveDealSql"));
						System.out.println(manageDealBean.areaId);
						preparedStatement.setInt(1, manageDealBean.areaId);
						preparedStatement.setString(2, manageDealBean.title);
						preparedStatement.setString(3, manageDealBean.dealBannerPicturePath);
						preparedStatement.setDate(4, new Date( manageDealBean.fromDate.getTime()));
						preparedStatement.setDate(5, new Date(manageDealBean.toDate.getTime()));
						if(manageDealBean.isActiveChecked){
							preparedStatement.setString(6, "Y");
						}else{
							preparedStatement.setString(6, "N");
						}
						preparedStatement.setString(7, userName);
						preparedStatement.setString(8, userName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							inserted = true;
							System.out.println("Deal save message =" + message);
						}
						if (inserted) {
							Messagebox.show("Saved Deal Successfully...");
							clearData();
							onLoadDealList();
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
	 * clear data method is used to clear the data from all the page components
	 * 
	 */
	public void clearData(){
		manageDealBean.title = "";
		manageDealBean.areaName = "";
		manageDealBean.cityName="";
		manageDealBean.dealBannerPicturePath="";
		manageDealBean.fromDate=null;
		manageDealBean.toDate=null;
		dealPageBannerImage=null;
		manageDealBean.isActiveChecked = false;
	}
	
	/**
	 * validateFields is used to validate the fields
	 * 
	 */
	public Boolean validateFields(){
		if(manageDealBean.cityName!=null){
			if(manageDealBean.areaName!=null){
				if(manageDealBean.title!=null){
					if(manageDealBean.dealBannerPicturePath!=null){
						if(manageDealBean.fromDate!=null){
							if(manageDealBean.toDate!=null){
								return true;
							}else{
								Messagebox.show("Please give end date!");
								return false;
							}
						}else{
							Messagebox.show("Please give start date!");
							return false;
						}		
					}else{
						Messagebox.show("Please upload image!");
						return false;
					}
				}else{
					Messagebox.show("Please give deal title!");
					return false;
				}
			}else{
				Messagebox.show("Please give area name!");
				return false;
			}
		}else{
			Messagebox.show("Please give city name!");
			return false;
		}
	}
	
	/**
	 * 
	 * edit deal data
	 */
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean") ManageDealBean manageDealbean){
				
		manageDealBean.cityName = manageDealbean.cityName;
		manageDealBean.areaName = manageDealbean.areaName;
		manageDealBean.areaId = manageDealbean.areaId;
		manageDealBean.dealId = manageDealbean.dealId;
		manageDealBean.title = manageDealbean.title;
		manageDealBean.dealBannerPicturePath = manageDealbean.dealBannerPicturePath;
		manageDealBean.fromDate = manageDealbean.fromDate;
		manageDealBean.toDate = manageDealbean.toDate;
		if(manageDealbean.dealBannerPicturePath.startsWith("C:")){
			iFrameVisibility = false;
			imageContentVisibility = true;
		}else{
			iFrameVisibility = true;
			imageContentVisibility = false;
		}
		try {
			dealPageBannerImage = new AImage(manageDealBean.dealBannerPicturePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dealPageBannerImage = null;
		}
		
		updateButtonVisibility = true;
		saveButtonVisibility = false;
					
	}
	
	/**
	 * onClickUpdateDeal method is used to update deal data in the database
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickUpdateDeal(){
		if(validateFields()){
			updateDealData();
		}
	}
	
	/**
	 * 
	 * Update deal data
	 */
	public void updateDealData(){
		String message = "";
		boolean updated = false;
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				try {
					preparedStatement = connection.prepareStatement(propertyfile.getPropValues("updateDealSql"));
					preparedStatement.setInt(1, manageDealBean.areaId);
					preparedStatement.setString(2, manageDealBean.title);
					preparedStatement.setString(3, manageDealBean.dealBannerPicturePath);
					preparedStatement.setDate(4, new Date(manageDealBean.fromDate.getTime()));
					preparedStatement.setDate(5, new Date(manageDealBean.toDate.getTime()));
					if(manageDealBean.isActiveChecked){
						preparedStatement.setString(6, "Y");
					}
					else{
						preparedStatement.setString(6, "N");
					}
					preparedStatement.setString(7, userName);
					preparedStatement.setString(8, userName);
					preparedStatement.setInt(9, manageDealBean.dealId);
					resultSet = preparedStatement.executeQuery();
					if(resultSet.next()){
						message=resultSet.getString(1);
						updated = true;
						System.out.println("Deal save message =" + message);
					}
					if(updated){
						Messagebox.show("Deal data Updated Successfully...");
						clearData();
						onLoadDealList();
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
	
	/**
	 * 
	 * Deal data delete confirmation box
	 */
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean")ManageDealBean manageDealbean){
		
		Map<String, Object> parentMap = new HashMap<String, Object>();
		
		parentMap.put("parentDealObject", manageDealbean);
		
		Window win = (Window) Executions.createComponents("dealDeleteConfirmationMessageBox.zul", null, parentMap);
		
		win.doModal();
	}
	
	public ManageDealBean getManageDealBean() {
		return manageDealBean;
	}

	public void setManageDealBean(ManageDealBean manageDealBean) {
		this.manageDealBean = manageDealBean;
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

	public boolean isDealBannerFileuploaded() {
		return dealBannerFileuploaded;
	}

	public void setDealBannerFileuploaded(boolean dealBannerFileuploaded) {
		this.dealBannerFileuploaded = dealBannerFileuploaded;
	}

	public String getDealBannerImagefilePath() {
		return dealBannerImagefilePath;
	}

	public void setDealBannerImagefilePath(String dealBannerImagefilePath) {
		this.dealBannerImagefilePath = dealBannerImagefilePath;
	}

	public AImage getDealPageBannerImage() {
		return dealPageBannerImage;
	}

	public void setDealPageBannerImage(AImage dealPageBannerImage) {
		this.dealPageBannerImage = dealPageBannerImage;
	}

	public ArrayList<ManageDealBean> getManageDealBeanList() {
		return manageDealBeanList;
	}

	public void setManageDealBeanList(ArrayList<ManageDealBean> manageDealBeanList) {
		this.manageDealBeanList = manageDealBeanList;
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


	public boolean isiFrameVisibility() {
		return iFrameVisibility;
	}


	public void setiFrameVisibility(boolean iFrameVisibility) {
		this.iFrameVisibility = iFrameVisibility;
	}


	public boolean isImageContentVisibility() {
		return imageContentVisibility;
	}


	public void setImageContentVisibility(boolean imageContentVisibility) {
		this.imageContentVisibility = imageContentVisibility;
	}

}
