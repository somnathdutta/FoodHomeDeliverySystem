package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.ManageCityBean;


public class ManageCityViewModel {
	
	private ManageCityBean manageCityBean = new ManageCityBean();
	
	private ArrayList<ManageCityBean> manageCityBeanList = new ArrayList<ManageCityBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName="";
	
	private Boolean saveButtonVisibility = true;
	
	private Boolean updateButtonVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		userName = (String) session.getAttribute("login");
		connection.setAutoCommit(true);
		onLoadCityList();
	
	}
	
	/**
	 * 
	 * This method is used to load all city list from view (vw_city_data)
	 */
	public void onLoadCityList(){
		if(manageCityBeanList.size()>0){
			manageCityBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					
					try {
							preparedStatement = connection.prepareStatement(propertyfile.getPropValues("onLoadCityListSql"));
							resultSet = preparedStatement.executeQuery();
							while(resultSet.next()){
								
								ManageCityBean manageCitybean = new ManageCityBean();
								manageCitybean.cityId = resultSet.getInt("city_id");
								manageCityBean.cityId = manageCitybean.cityId;
								manageCitybean.cityName = resultSet.getString("city_name");
								
								if(resultSet.getString("is_active").equals("Y")){
									manageCitybean.status = "Active";
								}
								else{
									manageCitybean.status = "Inactive";
								}
								manageCityBeanList.add(manageCitybean);
							}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	public void onClickSaveCity(){

		if(validateFields()){
			saveCityData();
		}
	}
	
	public void saveCityData(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					Boolean inserted = false;
							
				try {
						preparedStatement =  connection.prepareStatement(propertyfile.getPropValues("saveCitySql"));
						
						preparedStatement.setString(1, manageCityBean.cityName.toUpperCase());
						
						/*if(manageCityBean.isActiveChecked){
							preparedStatement.setString(2, "Y");
						}
						else{
							preparedStatement.setString(2, "N");
						}*/
						if(manageCityBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(2, "Y");
						}else{
							preparedStatement.setString(2, "N");
						}
						preparedStatement.setString(3, userName);
						preparedStatement.setString(4, userName);
						
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							inserted = true;
							System.out.println(resultSet.getString(1));
						}
						if(inserted){
							Messagebox.show("City saved sucessfully!!","SAVE INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
							clearData();
							onLoadCityList();
						}		
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	 * onClickEdit is used to edit data
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean")ManageCityBean manageCitybean){
		
		manageCityBean.cityId = manageCitybean.cityId;
		manageCityBean.cityName = manageCitybean.cityName;
		if(manageCitybean.status.equals("Active")){
			//manageCityBean.isActiveChecked = true;
			manageCityBean.status = "Active";
			updateButtonVisibility = true;
			saveButtonVisibility = false;
		}	
		else{
			//manageCityBean.isActiveChecked=false;
			manageCityBean.status = "Inactive";
			updateButtonVisibility = true;
			saveButtonVisibility = false;
		}
	}
	
	/*
	 * onClickpdateCity method is used to update city data in the database
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickpdateCity(){
		 
		if(validateFields()){
			updateCityData();
		}
	}
	
	/**
	 * 
	 * Method to update city data 
	 */
	public void updateCityData(){
		Boolean updated = false;
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				try {
					preparedStatement = connection.prepareStatement(propertyfile.getPropValues("updateCitySql"));
					preparedStatement.setString(1, manageCityBean.cityName.toUpperCase());
					/*if(manageCityBean.isActiveChecked){
						preparedStatement.setString(2, "Y");
					}
					else{
						preparedStatement.setString(2, "N");
					}*/
					if(manageCityBean.status.equalsIgnoreCase("Active")){
						preparedStatement.setString(2, "Y");
					}else{
						preparedStatement.setString(2, "N");
					}
					
					preparedStatement.setString(3, userName);
					preparedStatement.setString(4, userName);
					preparedStatement.setInt(5, manageCityBean.cityId);
					resultSet = preparedStatement.executeQuery();
					while(resultSet.next()){
						updated = true;
						System.out.println(resultSet.getString(1));
					}
					if(updated){
						Messagebox.show("City data Updated Successfully...");
						clearData();
						onLoadCityList();
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
	
	/*
	 * 
	 * Method to clear data from fields
	 */
	public void clearData(){
		manageCityBean.cityName=null;
		manageCityBean.isActiveChecked=false;
		manageCityBean.status = null;
	}
	
	/*
	 * 
	 * Method to check fields are not empty
	 */
	public Boolean validateFields(){
		if(manageCityBean.cityName!=null){
			if(manageCityBean.status!=null){
				return true;
			}else{
				Messagebox.show("City will active or not!!");
				return false;
			}
		}else{
			Messagebox.show("City name required!!");
			return false;
		}
	}
	public ManageCityBean getManageCityBean() {
		return manageCityBean;
	}

	
	
	public void setManageCityBean(ManageCityBean manageCityBean) {
		this.manageCityBean = manageCityBean;
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

	public ArrayList<ManageCityBean> getManageCityBeanList() {
		return manageCityBeanList;
	}

	public void setManageCityBeanList(ArrayList<ManageCityBean> manageCityBeanList) {
		this.manageCityBeanList = manageCityBeanList;
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
	
}
