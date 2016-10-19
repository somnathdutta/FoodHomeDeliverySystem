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
import org.zkoss.zhtml.Pre;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.ManageKitchens;
import Bean.ManageLogistics;

public class ManageLogisticsViewModel {

	private ManageLogistics managelogisticsBean = new ManageLogistics();
	
	private ArrayList<ManageLogistics> manageLogisticsBeanList = new ArrayList<ManageLogistics>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private Integer roleId = 0;
	
	private ArrayList<String> kitchenList= new ArrayList<String>();
	
	private Boolean kitchenComboBoxvisibility = false;
	
	private Boolean saveButtonVisibility = true;
	
	private Boolean updateButtonVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile();

	@AfterCompose
	public void initSetUp(@ContextParam(ContextType.VIEW) Component view)throws Exception{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		connection.setAutoCommit(true);
		
		if(roleId==1){
			kitchenComboBoxvisibility = true;
			onLoadKitchenList();
		}
		
		loadAllLogisticsList();
		
		//onLoadKitchenList();
	}
	
	
	/**
	 * This method is useful to load saved Logistics to the front page
	 */
	public void loadAllLogisticsList(){
		if(manageLogisticsBeanList.size()>0){
			manageLogisticsBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement =  null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("onLoadLogisticsSql"));
						resultSet = preparedStatement.executeQuery();
						
						while(resultSet.next()){
							
							ManageLogistics manageLogistics = new ManageLogistics();
							manageLogistics.kitchenName = resultSet.getString("kitchen_name");
							manageLogistics.logisticsName = resultSet.getString("logistics_name");
							if( resultSet.getString("is_active").equalsIgnoreCase("Y")){
								manageLogistics.status = "Active";
							}else{
								manageLogistics.status = "Deactive";
							}
							manageLogisticsBeanList.add(manageLogistics);
							
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
	
	public void onLoadKitchenList(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "Select kitchen_name from fapp_kitchen";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							kitchenList.add(resultSet.getString(1));
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
	public void onSelectKitchenName(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "Select kitchen_id from fapp_kitchen where kitchen_name=?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, managelogisticsBean.kitchenName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							managelogisticsBean.kitchenId =  resultSet.getInt(1);
							System.out.println("kit id "+managelogisticsBean.kitchenId);
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
	public void onClickSave(){
		if(validateFields()){
			saveLogisticsData();
		}
	}
	
	public void saveLogisticsData(){
		String message ="";
		Boolean inserted=false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("saveLogisticsSql"));
						preparedStatement.setString(1, managelogisticsBean.logisticsName);
						preparedStatement.setString(2, managelogisticsBean.password);
						if(roleId ==1){
							preparedStatement.setInt(3, managelogisticsBean.kitchenId);
						}else{
							preparedStatement.setInt(3, getKitchenId());
						}
						
						if(managelogisticsBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(4, "Y");
						}else{
							preparedStatement.setString(4, "N");
						}
						preparedStatement.setString(5, userName);
						preparedStatement.setString(6, userName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							inserted = true;
							System.out.println("Logistics save message =" + message);
						}
						if (inserted) {
							Messagebox.show("Logistics Saved Successfully...");
							refresh();
							loadAllLogisticsList();
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
	
	
	public Integer getKitchenId(){
		Integer kitchenId = 0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, userName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							kitchenId = resultSet.getInt(1);
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
		return kitchenId;
	}
	
	@Command
	@NotifyChange("*")
	public void onOKLogisticsName(){
		if(isLogisticsNameExists()){
			Messagebox.show("Logistics name already exists!","ALERT",Messagebox.OK,Messagebox.INFORMATION);
			managelogisticsBean.logisticsName = null;
		}else{
			
		}
	}
	
	private Boolean isLogisticsNameExists(){
		Boolean notExists = false;
		ArrayList<String> logisticsList = new ArrayList<String>();
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "SELECT logistics_name FROM fapp_logistics";
				try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						logisticsList.add(resultSet.getString(1));
					}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement != null){
						preparedStatement.close();
					}
				}
				if(logisticsList.size()>0){
					for(String logistics : logisticsList){
						if(logistics.equals(managelogisticsBean.logisticsName)){	
							notExists = true;	
						}	
					}
				}else{
					notExists = true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return notExists;
	}
	
	
	public Boolean validateFields(){
		
		//if(roleId==1 && managelogisticsBean.kitchenId!=null){
			
			if(managelogisticsBean.logisticsName!=null){
				if(managelogisticsBean.password!=null){
					if(managelogisticsBean.status!=null){
						return true;
					}else{
						Messagebox.show("Logistics will active or not?");
						return false;
					}
				}else{
					Messagebox.show("Password required!");
					return false;
				}
			}else{
				Messagebox.show("Logistics User name required!");
				return false;
			}
		
		/*}else{
			Messagebox.show("Kitchen name required!");
			return false;
		}*/
	}
	
	public void refresh(){
		managelogisticsBean.kitchenName=null;
		managelogisticsBean.logisticsName = null;
		managelogisticsBean.password = null;
		managelogisticsBean.status = null;
	}
	public ManageLogistics getManagelogisticsBean() {
		return managelogisticsBean;
	}

	public void setManagelogisticsBean(ManageLogistics managelogisticsBean) {
		this.managelogisticsBean = managelogisticsBean;
	}

	public ArrayList<ManageLogistics> getManageLogisticsBeanList() {
		return manageLogisticsBeanList;
	}

	public void setManageLogisticsBeanList(
			ArrayList<ManageLogistics> manageLogisticsBeanList) {
		this.manageLogisticsBeanList = manageLogisticsBeanList;
	}

	public ArrayList<String> getKitchenList() {
		return kitchenList;
	}

	public void setKitchenList(ArrayList<String> kitchenList) {
		this.kitchenList = kitchenList;
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


	public Boolean getKitchenComboBoxvisibility() {
		return kitchenComboBoxvisibility;
	}


	public void setKitchenComboBoxvisibility(Boolean kitchenComboBoxvisibility) {
		this.kitchenComboBoxvisibility = kitchenComboBoxvisibility;
	}
	
	
}
