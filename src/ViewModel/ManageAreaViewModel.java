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

import Bean.ManageAreaBean;
import Bean.ManageCityBean;


public class ManageAreaViewModel {
	
	private ManageAreaBean manageAreaBean = new ManageAreaBean();
	
	private ArrayList<ManageAreaBean> manageAreaBeanList = new ArrayList<ManageAreaBean>();
	Session session = null;
	
	private Connection connection = null;
	
	private String userName="";
	
	private Boolean saveButtonVisibility = true;
	
	private Boolean updateButtonVisibility = false;

	private ArrayList<String>  cityList = new ArrayList<String>();
	
	PropertyFile propertyfile = new PropertyFile();
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		userName = (String) session.getAttribute("login");
		//connection = DbConnection.getConnection();	
		connection.setAutoCommit(true);
		onLoadAreaList();
		onLoadCityList();
		
	}
	
	public void onLoadAreaList(){
		if(manageAreaBeanList.size()>0){
			manageAreaBeanList.clear();
		}	
		try {
			SQL:{
				 PreparedStatement preparedStatement = null;
				 ResultSet resultSet = null;
				 String status="";
				 try {
					 	preparedStatement = connection.prepareStatement(propertyfile.getPropValues("onLoadAreaListSql"));
					 	resultSet = preparedStatement.executeQuery();
					 	String cityName=null;
					 	while(resultSet.next()){
					 		ManageAreaBean manageAreabean =  new ManageAreaBean();
					 		manageAreabean.cityName = resultSet.getString("city_name");
					 		manageAreabean.cityId =  resultSet.getInt("city_id");
					 		manageAreabean.areaId = resultSet.getInt("area_id");
					 		String tempCityName=manageAreabean.cityName;
					 		if(manageAreabean.cityName.equals(cityName)){
					 			manageAreabean.cityName="";
					 			manageAreabean.cityNameVisibility=false;
					 		}
					 		manageAreabean.areaName = resultSet.getString("area_name");
					 		status = resultSet.getString("is_active");
					 		if(status.equals("Y")){
					 			manageAreabean.status = "Active";
					 		}
					 		else{
					 			manageAreabean.status = "Inactive";
					 		}
					 		cityName=tempCityName;
					 		
					 		manageAreaBeanList.add(manageAreabean);
					 	}
					 	
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	
	@Command
	@NotifyChange("*")
	public void onSelectcityName(){
		try {
			sql1:{
					PreparedStatement preparedStatement=null;
					try {
							preparedStatement=connection.prepareStatement(propertyfile.getPropValues("cityIdWRTcityNameSql"));
							preparedStatement.setString(1, manageAreaBean.cityName);
							ResultSet resultSet=preparedStatement.executeQuery();
							
							while(resultSet.next()){
								manageAreaBean.cityId = resultSet.getInt("city_id");								
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
	
	@Command
	@NotifyChange("*")
	public void onClickSaveArea(){
		if(validateFields()){
			saveAreaData();
		}
	}
	
	public void saveAreaData(){
		String message="";
		Boolean inserted=false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
							
				try {
						preparedStatement =  connection.prepareStatement(propertyfile.getPropValues("saveAreaSql"));
						preparedStatement.setString(1, manageAreaBean.areaName);
						preparedStatement.setInt(2, manageAreaBean.cityId);
						/*if(manageAreaBean.isActiveChecked){
							preparedStatement.setString(3, "Y");
						}
						else{
							preparedStatement.setString(3, "N");
						}*/
						if(manageAreaBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(3, "Y");
						}else{
							preparedStatement.setString(3, "N");
						}
						preparedStatement.setString(4, userName);
						preparedStatement.setString(5, userName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							inserted=true;
							System.out.println("Area saved "+message);
						}
						if(inserted){
							Messagebox.show("Area saved sucessfully!!");
							clearData();
							onLoadAreaList();
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
	public void onClickEdit(@BindingParam("bean")ManageAreaBean manageAreabean){
		cityList.clear();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet =  null;
					String sql = "select * from vw_area_data where area_name=?";
					try {
						 preparedStatement = connection.prepareStatement(sql);
						 preparedStatement.setString(1, manageAreabean.areaName);
						 manageAreaBean.areaName= manageAreabean.areaName;
						 manageAreaBean.cityId = manageAreabean.cityId;
						 manageAreaBean.areaId = manageAreabean.areaId;
						 resultSet = preparedStatement.executeQuery();
						 while(resultSet.next()){
							manageAreaBean.cityName = resultSet.getString("city_name");	
							if(resultSet.getString("is_active").equals("Y")){
								manageAreaBean.isActiveChecked =true;
								manageAreaBean.status = "Active";
							}
							else{
								manageAreaBean.isActiveChecked = false;
								manageAreaBean.status = "Inactive";
							}
							updateButtonVisibility = true;
							saveButtonVisibility = false;
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
	 * onClickpdateCity method is used to update city data in the database
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickUpdateArea(){
		 if(validateFields()){
			updateAreaData();
		}
	}
	
	/**
	 * 
	 * Method to update city data 
	 */
	public void updateAreaData(){
		Boolean updated = false;
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				try {
					preparedStatement = connection.prepareStatement(propertyfile.getPropValues("updateAreaSql"));
					preparedStatement.setString(1, manageAreaBean.areaName);
					preparedStatement.setInt(2, manageAreaBean.cityId);
					/*if(manageAreaBean.isActiveChecked){
						preparedStatement.setString(3, "Y");
					}
					else{
						preparedStatement.setString(3, "N");
					}*/
					if(manageAreaBean.status.equalsIgnoreCase("Active")){
						preparedStatement.setString(3, "Y");
					}else{
						preparedStatement.setString(3, "N");
					}
					preparedStatement.setString(4, userName);
					preparedStatement.setString(5, userName);
					preparedStatement.setInt(6, manageAreaBean.areaId);
					resultSet = preparedStatement.executeQuery();
					while(resultSet.next()){
						updated = true;
						System.out.println(resultSet.getString(1));
					}
					if(updated){
						Messagebox.show("Area data Updated Successfully...");
						clearData();
						onLoadAreaList();
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
	
	public Boolean validateFields(){
		if(manageAreaBean.cityName!=null){
			if(manageAreaBean.areaName!=null){
				if(manageAreaBean.status!=null){
					return true;
				}else{
					Messagebox.show("Area will active or not?");
					return false;
				}
			}else{
				Messagebox.show("Area name required!");
				return false;
			}
		}else{
			Messagebox.show("City name required!");
			return false;
		}
	}
	
	public void clearData(){
		manageAreaBean.cityName=null;
		manageAreaBean.areaName=null;
		manageAreaBean.status = null;
		manageAreaBean.isActiveChecked=false;
	}
	
	
	public ManageAreaBean getManageAreaBean() {
		return manageAreaBean;
	}

	public void setManageAreaBean(ManageAreaBean manageAreaBean) {
		this.manageAreaBean = manageAreaBean;
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

	public ArrayList<ManageAreaBean> getManageAreaBeanList() {
		return manageAreaBeanList;
	}

	public void setManageAreaBeanList(ArrayList<ManageAreaBean> manageAreaBeanList) {
		this.manageAreaBeanList = manageAreaBeanList;
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
