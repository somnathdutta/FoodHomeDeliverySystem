package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Set;

import org.apache.poi.util.Beta;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.impl.BinderUtil;
import org.zkoss.bind.sys.debugger.impl.info.AddBindingInfo;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;

import service.BannerService;


import Bean.BannerBean;
import Bean.UrlBean;

public class BannerViewModel {

	public BannerBean bannerBean = new BannerBean();
	public BannerBean addNewBannerBean = new BannerBean();
	public UrlBean existingBannerName = new UrlBean();
	public String urlOfImage = null;
	
	public ArrayList<UrlBean> urlList = new ArrayList<UrlBean>();
	public ArrayList<UrlBean> existingUrlList = new ArrayList<UrlBean>();
	public ArrayList<BannerBean> bannerList = new ArrayList<BannerBean>();
	public ArrayList<UrlBean> bannerNameList;
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	public boolean group1Open = false;
	
	public boolean group2Open = false;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		loadSavedBanners();
		bannerNameList = BannerService.loadBannerList(connection);
		System.out.println("zul page >> manageBanners.zul");
		
	}
	
	public void loadSavedBanners(){
		if(bannerList.size()>0){
			bannerList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select banner_id,banner_title from fapp_banner where is_active = 'Y'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							BannerBean bannerBean = new BannerBean();
							bannerBean.bannerId = resultSet.getInt("banner_id");
							bannerBean.bannertTitle = resultSet.getString("banner_title");
							bannerList.add(bannerBean);
						}
								
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	public void onClickAddNewGroup(){
		group1Open = true;
		group2Open = false;
		clear();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickShowGroup(){
		group2Open = true;
		group1Open = false;
		loadSavedBanners();
	}
	
	@Command
	@NotifyChange("*")
	public void onOkNoOfImages(){
		if(addNewBannerBean.bannertTitle !=null){
		    if(addNewBannerBean.noOfUrls != null){
		    	if(addNewBannerBean.noOfUrls==null){
					urlList.clear();
					addNewBannerBean.setUrlGridVis(false);
				}else{
					addNewBannerBean.setUrlGridVis(true);
					urlList.clear();
					for(int i = 0 ;i<addNewBannerBean.noOfUrls;i++ ){
						urlList.add(new UrlBean());
					}
				}
		    }else {
		    	Messagebox.show("Enter Url Number", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
			}
		}else {
			Messagebox.show("Enter Banner Title", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickAdd(){
		//save()
		int i = 0;
		if(addNewBannerBean.bannertTitle !=null){
		     if(addNewBannerBean.noOfUrls != null){
		    	 ArrayList<UrlBean> subUrlBeanList = new ArrayList<UrlBean>();
		    	 for(UrlBean bean : urlList){
		    		 if(bean.urlName != null){
		    			 subUrlBeanList.add(bean);
		    		 }
		    	 }
		    	if(subUrlBeanList.size()>0){ 
			    i = BannerService.insertBannerDetails(connection, addNewBannerBean, urlList);
				    if(i>0){
						urlList.clear();
						onclickExistClear();
						addNewBannerBean.bannertTitle = null;
						addNewBannerBean.noOfUrls = null;
						addNewBannerBean.setUrlGridVis(false);
						Messagebox.show("Inserted Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
					}else {
						
					}
		    	}else {
		    		Messagebox.show("Enter Image Url", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
				}
		     }else {
			    Messagebox.show("Enter Url Number", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		     }
		 }else {
			Messagebox.show("Enter Banner Title", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
		
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickAddNew(){
		if(urlOfImage != null ){
			if(bannerBean.bannerId != null){
				addNewImage();
			}else{
				Messagebox.show("Banner title required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
			}
		}else{
			Messagebox.show("Image url required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
		}	
	}
	
	public void addNewImage(){
		boolean added = false;
		try {
				SQL:{
			     PreparedStatement preparedStatement = null;
			     String sql = "INSERT INTO fapp_banner_details( "
			    		 +" banner_id,banner_image) "
			    		 +" VALUES (?, ?)";
			     try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setInt(1, bannerBean.bannerId);
					preparedStatement.setString(2, urlOfImage);
					int count = preparedStatement.executeUpdate();
					if(count>0){
						added = true;
					}
				} catch (Exception e) {
					// TODO: handle exception
					Messagebox.show("Error due to : "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
					connection.rollback();
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(added){
			Messagebox.show("New image added successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
			clear();
			loadSavedBanners();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdateTitle(){
		if(bannerBean.bannerId != null ){
			updatetitle();
		}else{
			Messagebox.show("Banner title required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
		}
	}
	
	
	public void updatetitle(){
		boolean updated = false;
		try {
				SQL:{
			     PreparedStatement preparedStatement = null;
			     String sql = "UPDATE fapp_banner set banner_title = ?  where banner_id = ?";
			     try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, bannerBean.bannertTitle);
					preparedStatement.setInt(2, bannerBean.bannerId);
					int count = preparedStatement.executeUpdate();
					if(count>0){
						updated = true;
					}
				} catch (Exception e) {
					// TODO: handle exception
					Messagebox.show("Error due to : "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
					connection.rollback();
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(updated){
			Messagebox.show("Banner title updated successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
			clear();
			loadSavedBanners();
		}
	}
	
	
	public void save(){
		boolean inserted = false,allinserted= false;
		int id=0;
		try {
			 SQL:{
				     PreparedStatement preparedStatement = null;
				     String sql = "INSERT INTO fapp_banner(banner_title) VALUES (?)";
				     try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, bannerBean.bannertTitle);
						int countInsertedRow = preparedStatement.executeUpdate();
						if(countInsertedRow>0){
							inserted = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to : "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
						connection.rollback();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		  
		    if(inserted){
		    	SQL:{
				     PreparedStatement preparedStatement = null;
				     ResultSet resultSet = null;
				     
				     String sql = "SELECT MAX(banner_id)AS banner_id from fapp_banner";
				     try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							id = resultSet.getInt("banner_id");
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to : "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		    }
			
		    if(id!=0){
		    	SQL:{
				     PreparedStatement preparedStatement = null;
				     String sql = "INSERT INTO fapp_banner_details( "
				    		 +" banner_id,banner_image) "
				    		 +" VALUES (?, ?)";
				     try {
						preparedStatement = connection.prepareStatement(sql);
						
						for(UrlBean bean : urlList){
							preparedStatement.setInt(1, id);
							preparedStatement.setString(2, bean.urlName);
							preparedStatement.addBatch();
						}
						int[] insert = preparedStatement.executeBatch();
						if(insert.length>0){
							allinserted = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("Error due to : "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
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
		if(allinserted){
			Messagebox.show("Saved successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
			clear();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickCancel(){
		clear();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickShow(){
		if(bannerBean.bannerId!=null)
			show();
		else
			Messagebox.show("Choose banner title!","Required information missing",Messagebox.OK,Messagebox.EXCLAMATION);
	}
	
	public void show(){
		if(urlList.size()>0){
			urlList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select banner_image,banner_details_id from fapp_banner_details where banner_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, bannerBean.bannerId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							urlList.add(new UrlBean(resultSet.getString("banner_image") , resultSet.getInt("banner_details_id")));
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		loadSavedBanners();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickRemove(@BindingParam("bean")final UrlBean bean){
		System.out.println(bean.urlId);
		Messagebox.show(
				"Are you sure to save?", "Confirm Dialog",
				Messagebox.OK | Messagebox.IGNORE | Messagebox.CANCEL,
				Messagebox.QUESTION, 
				new org.zkoss.zk.ui.event.EventListener() {
					@Override
					public void onEvent(Event evt) throws InterruptedException {
						if (evt.getName().equals("onOK")) {
							System.out.println("Data Saved !");
							remove(bean.urlId);
							BindUtils.postGlobalCommand(null, null, "globalReload", null);
						} else if (evt.getName().equals("onIgnore")) {
							Messagebox.show("Ignore Deletion", "Warning",
									Messagebox.OK, Messagebox.EXCLAMATION);
						} else {
							System.out.println("Save Canceled !");
						}
					}
				}
			);
	}
	
	public void remove(int urlId){
		boolean deleted= false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "DELETE from fapp_banner_details where banner_details_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, urlId);
						int deletedRows = preparedStatement.executeUpdate();
						if(deletedRows>0){
							deleted = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						Messagebox.show("ERROR DUE TO:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
		if(deleted){
			Messagebox.show("Deleted successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
			clear();
			loadSavedBanners();
		}
	}
	
	public boolean isValid(){
		if(bannerBean.bannertTitle!=null){
			if(urlList.size()>0){
				return true;
			}else{
				Messagebox.show("Url required!","Alaert",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Title required!","Alaert",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public void clear(){
		bannerBean.bannertTitle = null;
		bannerBean.noOfUrls = null;
		urlOfImage = null;
		loadSavedBanners();
		urlList.clear();
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectBannerName(){
		
		existingUrlList = BannerService.loadUrlList(connection, existingBannerName.bannerId);
		existingBannerName.divVis = true;
		if(existingUrlList.size()==0){
			Messagebox.show("Please add","Information",Messagebox.OK,Messagebox.INFORMATION);
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onclickEdit(@BindingParam("Bean") UrlBean bean){
		int i =0;
		i = BannerService.upDateBannerUrl(connection, bean.bannerId);
		if(i>0){
			Messagebox.show("Updated successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("Bean") UrlBean bean){
        String status = bean.activeStatus;
		if(status.equalsIgnoreCase("Active")){
        	status = "Y";
        }else {
			status= "N";
		}
		
		int i =0;
		i = BannerService.inActiveBannerUrl(connection, bean.bannerId, status);
		if(i>0){
			Messagebox.show("Deactive successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickNewAdd(){
		UrlBean bean = new UrlBean();
		bean.bannerName = existingBannerName.bannerName;
		bean.activeStatus = "Active";
		
		existingUrlList.add(bean);
	}
	
	@Command
	@NotifyChange("*")
	public void saveAll(){
		boolean status = false;
		status = BannerService.saveAll(connection, existingBannerName.bannerId, existingUrlList);
		if(status == true){
			existingUrlList = BannerService.loadUrlList(connection, existingBannerName.bannerId);
			Messagebox.show("Updated successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
		}
		
	}
	
	
	@Command
	@NotifyChange("*")
	public void onclickExistClear(){
		existingBannerName.bannerName = null;
		existingUrlList.clear();
		existingBannerName.divVis = false;
		bannerNameList = BannerService.loadBannerList(connection);
		
	}
	
	@Command
	@NotifyChange("*")
	public void onclickStatus(@BindingParam("bean") UrlBean bean){
		int i = 0;
		System.out.println(bean.activeStatus);
		String status = bean.activeStatus;
		if(status.equalsIgnoreCase("Active")){
        	status = "Y";
        }else {
			status= "N";
		}
		if(bean.urlId != null){
		i = BannerService.inActiveBannerUrl(connection, bean.bannerId, status);
		}
		
		if(i>0){
			existingUrlList = BannerService.loadUrlList(connection, existingBannerName.bannerId);
			Messagebox.show("Updated successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
		}
		
		
	}
	
	public BannerBean getBannerBean() {
		return bannerBean;
	}

	public void setBannerBean(BannerBean bannerBean) {
		this.bannerBean = bannerBean;
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

	public ArrayList<UrlBean> getUrlList() {
		return urlList;
	}

	public void setUrlList(ArrayList<UrlBean> urlList) {
		this.urlList = urlList;
	}

	public boolean isGroup1Open() {
		return group1Open;
	}

	public void setGroup1Open(boolean group1Open) {
		this.group1Open = group1Open;
	}

	public boolean isGroup2Open() {
		return group2Open;
	}

	public void setGroup2Open(boolean group2Open) {
		this.group2Open = group2Open;
	}

	public ArrayList<BannerBean> getBannerList() {
		return bannerList;
	}

	public void setBannerList(ArrayList<BannerBean> bannerList) {
		this.bannerList = bannerList;
	}

	public String getUrlOfImage() {
		return urlOfImage;
	}

	public void setUrlOfImage(String urlOfImage) {
		this.urlOfImage = urlOfImage;
	}

	public BannerBean getAddNewBannerBean() {
		return addNewBannerBean;
	}

	public void setAddNewBannerBean(BannerBean addNewBannerBean) {
		this.addNewBannerBean = addNewBannerBean;
	}

	public ArrayList<UrlBean> getExistingUrlList() {
		return existingUrlList;
	}

	public void setExistingUrlList(ArrayList<UrlBean> existingUrlList) {
		this.existingUrlList = existingUrlList;
	}

	public ArrayList<UrlBean> getBannerNameList() {
		return bannerNameList;
	}

	public void setBannerNameList(ArrayList<UrlBean> bannerNameList) {
		this.bannerNameList = bannerNameList;
	}

	public UrlBean getExistingBannerName() {
		return existingBannerName;
	}

	public void setExistingBannerName(UrlBean existingBannerName) {
		this.existingBannerName = existingBannerName;
	}
	
	
}
