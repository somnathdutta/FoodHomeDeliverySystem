package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.ManageAreaBean;
import Bean.ManageCityBean;
import Bean.ManageZipBean;

public class ManageZipCodeViewModel {

	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private ManageZipBean manageZipBEAN = new ManageZipBean();
	
	private ManageCityBean manageCityBean = new ManageCityBean();
	
	private ManageAreaBean manageAreaBean = new ManageAreaBean();
	
	private ArrayList<ManageZipBean> manageZipBeanList = new ArrayList<ManageZipBean>();
	
	private ArrayList<ManageCityBean> cityBeanList = new ArrayList<ManageCityBean>();
	
	private ArrayList<ManageAreaBean> areaBeanList = new ArrayList<ManageAreaBean>();
	
	private Boolean updateButtonVisibility = false;
	
	private Boolean saveButtonVisibility = true;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		loadCityList();
		
		onLoadQuery();
	}
	
	public void onLoadQuery(){
		if(manageZipBeanList.size()>0){
			manageZipBeanList.clear();
		}
		try {
				SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "select * from vw_zipcode";
				try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						ManageZipBean bean = new ManageZipBean();
						bean.zipId = resultSet.getInt("zip_id");
						bean.areaId = resultSet.getInt("area_id");
						bean.areaName = resultSet.getString("area_name");
						bean.cityId = resultSet.getInt("city_id");
						bean.cityName = resultSet.getString("city_name");
						bean.zipcode = resultSet.getString("zip_code");
						bean.zipAreaName = resultSet.getString("locality_name");
						if(resultSet.getString("is_active").equalsIgnoreCase("Y")){
							bean.status = "Active";
						}else{
							bean.status = "Deactive";
						}
						manageZipBeanList.add(bean);
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
	
	
	public void loadCityList(){
		if(cityBeanList.size()>0){
			cityBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT city_id,city_name from sa_city where is_active='Y'" ; 
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							ManageCityBean cityBean = new ManageCityBean();
							cityBean.cityId = resultSet.getInt("city_id");
							cityBean.cityName = resultSet.getString("city_name");
							cityBeanList.add(cityBean);
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
	
	@Command
	@NotifyChange("*")
	public void onSelectCity(){
		System.out.println("Selected city Id->"+manageCityBean.cityId); 
		manageZipBEAN.cityId = manageCityBean.cityId;
		loadAreaList(manageCityBean.cityId);
	}
	
 	public void loadAreaList(int cityId){
		if(areaBeanList.size()>0){
			areaBeanList.clear();
		}
		
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT area_id,area_name from sa_area where city_id =? and is_active='Y'" ; 
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, cityId);
						resultSet = preparedStatement.executeQuery();
						
						while (resultSet.next()) {
							ManageAreaBean areaBean = new ManageAreaBean();
							areaBean.areaId = resultSet.getInt("area_id");
							areaBean.areaName = resultSet.getString("area_name");
							areaBeanList.add(areaBean);
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

 	@Command
	@NotifyChange("*")
	public void onSelectArea(){
		System.out.println("Selected area Id->"+manageAreaBean.areaId); 
		manageZipBEAN.areaId = manageAreaBean.areaId;
	}
 	
 	@Command
	@NotifyChange("*")
	public void onClickSave(){
		if(isValid()){
			System.out.println(manageZipBEAN.cityId+" "+manageZipBEAN.areaId+" "+manageZipBEAN.zipcode+" "+manageZipBEAN.zipAreaName+" "+manageZipBEAN.status );
			if(isSave()){
				Messagebox.show("Zip code saved successfully!", "Information", Messagebox.OK, Messagebox.INFORMATION);
				clear();
				loadCityList();
				onLoadQuery();
			}
			
		}
	}
 	
 	public boolean isSave(){
 		boolean isSave = false;
 		try {
			SQL:{
 					PreparedStatement preparedStatement  = null;
 					String sql = "INSERT INTO sa_zipcode( zip_code, locality_name, city_id, area_id, is_active,  created_by)"
 							   +" VALUES (?, ?, ?, ?, ?, ?);" ;
 					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, manageZipBEAN.zipcode);
						preparedStatement.setString(2, manageZipBEAN.zipAreaName);
						preparedStatement.setInt(3, manageZipBEAN.cityId);
						preparedStatement.setInt(4, manageZipBEAN.areaId);
						if(manageZipBEAN.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(5, "Y");
						}else{
							preparedStatement.setString(5, "N");
						}
						preparedStatement.setString(6, userName);
						int count = preparedStatement.executeUpdate();
						if(count > 0){
							isSave = true;
						}
					} catch (Exception e) {
						System.out.println(e);
						String err = e.toString();
							if(err.contains("already exists")){
								Messagebox.show("Zip code"+manageZipBEAN.zipcode+" already exists..", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
							}else{
								Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
							}
						} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}	
 			}
		} catch (Exception e) {
			// TODO: handle exception
		}
 		return isSave;
 	}
 	
 	public boolean isUpdate(){
 		boolean isUpdate = false;
 		try {
			SQL:{
 					PreparedStatement preparedStatement  = null;
 					String sql = "UPDATE sa_zipcode set zip_code=?, locality_name=?, city_id=?, area_id=?, is_active=?,  updated_by=?"
 							   +" where zip_id = ?" ;
 					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, manageZipBEAN.zipcode);
						preparedStatement.setString(2, manageZipBEAN.zipAreaName);
						preparedStatement.setInt(3, manageZipBEAN.cityId);
						preparedStatement.setInt(4, manageZipBEAN.areaId);
						if(manageZipBEAN.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(5, "Y");
						}else{
							preparedStatement.setString(5, "N");
						}
						preparedStatement.setString(6, userName);
						preparedStatement.setInt(7, manageZipBEAN.zipId);
						int count = preparedStatement.executeUpdate();
						if(count > 0){
							isUpdate = true;
						}
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}	
 			}
		} catch (Exception e) {
			// TODO: handle exception
		}
 		return isUpdate;
 	}
 	
 	public boolean isDelete(int zipId){
 		boolean isDelete = false;
 		try {
			SQL:{
 					PreparedStatement preparedStatement  = null;
 					String sql = "UPDATE sa_zipcode set is_delete='Y'"
 							   +" where zip_id = ?" ;
 					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, zipId);
						int count = preparedStatement.executeUpdate();
						if(count > 0){
							isDelete = true;
						}
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}	
 			}
		} catch (Exception e) {
			// TODO: handle exception
		}
 		return isDelete;
 	}
 	
 	@Command
 	@NotifyChange("*")
 	public void onClickUpdate(){
 		if(isValid()){
 			if(isUpdate()){
 				Messagebox.show("Zip code updated successfully!", "Information", Messagebox.OK, Messagebox.INFORMATION);
				clear();
				loadCityList();
				onLoadQuery();
				updateButtonVisibility = false;
				saveButtonVisibility = true;
 			}
 		}
 	}
 	
 	@Command
 	@NotifyChange("*")
 	public void onClickEdit(@BindingParam("bean")ManageZipBean bean){
 		manageZipBEAN.areaId = bean.areaId;
 		manageZipBEAN.areaName = bean.areaName;
 		manageZipBEAN.cityId = bean.cityId;
 		manageZipBEAN.cityName = bean.cityName;
 		manageZipBEAN.zipcode = bean.zipcode;
 		manageZipBEAN.zipAreaName = bean.zipAreaName;
 		manageZipBEAN.zipId = bean.zipId;
 		manageZipBEAN.status = bean.status;
 		saveButtonVisibility = false;
 		updateButtonVisibility = true;
 	}
 	
 	@SuppressWarnings("unchecked")
	@Command
 	@NotifyChange("*")
 	public void onClickDelete(@BindingParam("bean") final ManageZipBean bean){
 		Messagebox.show("Are you sure to delete?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
			          if(isDelete(bean.zipId)){
			        	  BindUtils.postGlobalCommand(null, null, "globalReload", null);
						   Messagebox.show("Zipcode data deleted successfully!");
			          }
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
 	}
 	
 	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		onLoadQuery();
		clear();
	}
 	
 	public boolean isValid(){
 		if(manageZipBEAN.cityId!=null){
 			if(manageZipBEAN.areaId!=null){
 				if(manageZipBEAN.zipcode!=null){
 					if(manageZipBEAN.zipAreaName!=null){
 						if(manageZipBEAN.status!=null){
 							return true;
 						}else{
 							Messagebox.show("Status can not be blank!");
 							return false;
 						}	
 					}else{
						Messagebox.show("Zip code area name can not be blank!");
						return false;
					}	
 				}else{
					Messagebox.show("Zip code can not be blank!");
					return false;
				}	
 			}else{
				Messagebox.show("Area name can not be blank!");
				return false;
			}	
 		}else{
			Messagebox.show("City Name can not be blank!");
			return false;
		}	
 	}
 	
 	public void clear(){
 		manageCityBean.cityId =  null;
 		manageCityBean.cityName = null;
 		manageAreaBean.areaId = null;
 		manageAreaBean.areaName = null;
 		manageZipBEAN.cityId = null;
 		manageZipBEAN.areaId = null;
 		manageZipBEAN.zipcode = null;
 		manageZipBEAN.zipcodeId = null;
 		manageZipBEAN.status = null;
 		manageZipBEAN.zipId = null;
 		manageZipBEAN.zipAreaName = null;
 		areaBeanList.clear();
 		cityBeanList.clear();
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

	public ManageZipBean getManageZipBEAN() {
		return manageZipBEAN;
	}

	public void setManageZipBEAN(ManageZipBean manageZipBEAN) {
		this.manageZipBEAN = manageZipBEAN;
	}

	public ArrayList<ManageZipBean> getManageZipBeanList() {
		return manageZipBeanList;
	}

	public void setManageZipBeanList(ArrayList<ManageZipBean> manageZipBeanList) {
		this.manageZipBeanList = manageZipBeanList;
	}

	public ArrayList<ManageCityBean> getCityBeanList() {
		return cityBeanList;
	}

	public void setCityBeanList(ArrayList<ManageCityBean> cityBeanList) {
		this.cityBeanList = cityBeanList;
	}

	public ArrayList<ManageAreaBean> getAreaBeanList() {
		return areaBeanList;
	}

	public void setAreaBeanList(ArrayList<ManageAreaBean> areaBeanList) {
		this.areaBeanList = areaBeanList;
	}

	public ManageCityBean getManageCityBean() {
		return manageCityBean;
	}

	public void setManageCityBean(ManageCityBean manageCityBean) {
		this.manageCityBean = manageCityBean;
	}

	public ManageAreaBean getManageAreaBean() {
		return manageAreaBean;
	}

	public void setManageAreaBean(ManageAreaBean manageAreaBean) {
		this.manageAreaBean = manageAreaBean;
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
 	
 	
}
