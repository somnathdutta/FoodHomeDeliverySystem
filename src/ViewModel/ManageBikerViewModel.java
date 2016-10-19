package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
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

import Bean.BoyStatus;
import Bean.ManageDeliveryBoyBean;

public class ManageBikerViewModel {
	
	private ManageDeliveryBoyBean manageDeliveryBoyBean =  new ManageDeliveryBoyBean();
	
	private BoyStatus boyStatusBean = new BoyStatus();
	private ArrayList<BoyStatus> boyStatusList = new ArrayList<BoyStatus>();
	
	private ArrayList<ManageDeliveryBoyBean> manageDeliveryBoyBeanList = new ArrayList<ManageDeliveryBoyBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private Integer roleId = 0;
	
	private Boolean saveButtonVisibility = true;
	
	private Boolean updateButtonVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetUp(@ContextParam(ContextType.VIEW) Component view)throws Exception{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		onLoadDeliveryBoyList();
		
		boyStatusList =  loadAllStatus();
	}

	/**
	 * 
	 * Method to load delivery boy list from view "vw_delivery_boy_list"
	 */
	public void onLoadDeliveryBoyList(){
		if(manageDeliveryBoyBeanList.size()>0){
			manageDeliveryBoyBeanList.clear();
		}
		try {
			SQL:{
				 PreparedStatement preparedStatement = null;
				 ResultSet resultSet = null;
				// String sql = "select * from fapp_delivery_boy where is_delete='N' and kitchen_id IS NULL";
				 String sql =  "select * from vw_delivery_boy_data ";
				 try {
					  preparedStatement =connection.prepareStatement(sql);
					
					 resultSet = preparedStatement.executeQuery();
					 while(resultSet.next()){
						 ManageDeliveryBoyBean manageDeliveryBoybean =  new ManageDeliveryBoyBean();
						 manageDeliveryBoybean.deliveryBoyUserId = resultSet.getString("delivery_boy_user_id");
						 manageDeliveryBoybean.deliveryBoyId = resultSet.getInt("delivery_boy_id");
						 manageDeliveryBoybean.name = resultSet.getString("delivery_boy_name");
						 manageDeliveryBoybean.phoneNo = resultSet.getString("delivery_boy_phn_number");
						 manageDeliveryBoybean.address = resultSet.getString("delivery_boy_address");
						 manageDeliveryBoybean.password = resultSet.getString("password");
						 manageDeliveryBoybean.boyStatus = resultSet.getString("delivery_boy_status");
						 manageDeliveryBoybean.boyStatusId = resultSet.getInt("delivery_boy_status_id");
						if(resultSet.getString("is_active").equals("Y")){
							 manageDeliveryBoybean.status = "Active";
						 }else{
							 manageDeliveryBoybean.status = "Deactive";
						 }
						 manageDeliveryBoybean.vehicleRegNo = resultSet.getString("delivery_boy_vehicle_reg_no");
						 manageDeliveryBoybean.orderAssigned = resultSet.getString("order_assigned");
						 
						 manageDeliveryBoyBeanList.add(manageDeliveryBoybean);
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
	
	
	
	public ArrayList<BoyStatus> loadAllStatus(){
		ArrayList<BoyStatus> boyStatusList = new ArrayList<BoyStatus>();
		if(boyStatusList.size() > 0 ){
			boyStatusList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select delivery_boy_status_id,delivery_boy_status from fapp_delivery_boy_status "
							+ " WHERE is_active = 'Y' order by delivery_boy_status_id";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							BoyStatus status = new BoyStatus();
							status.statusId = resultSet.getInt("delivery_boy_status_id");
							status.statusName = resultSet.getString("delivery_boy_status");
							boyStatusList.add(status);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return boyStatusList;
	}
	/**
	 * on click save button functionality
	 */
	@Command
	@NotifyChange("*")
	public void onClickSaveDeliveryBoy(){
		if(validateFields() ){
			saveDeliveryBoyData();
		}
	}
	
	/**
	 * 
	 * Method for saving delivery boy details to database
	 */
	public void saveDeliveryBoyData(){
		String message="";
		Boolean inserted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_delivery_boy( "
					            +"delivery_boy_name, delivery_boy_phn_number, " 
					            +"delivery_boy_user_id, password,delivery_boy_vehicle_reg_no,delivery_boy_status_id,"
					            + "is_active, created_by)"
							    +"VALUES (?, ?, ?, ?, ?, ?, ?, ?);"	;
				try {
						preparedStatement =  connection.prepareStatement(sql);
						preparedStatement.setString(1, manageDeliveryBoyBean.name);
						preparedStatement.setString(2, manageDeliveryBoyBean.phoneNo);
						preparedStatement.setString(3, manageDeliveryBoyBean.deliveryBoyUserId);
						preparedStatement.setString(4, manageDeliveryBoyBean.password);
						if(manageDeliveryBoyBean.vehicleRegNo!=null){
							preparedStatement.setString(5, manageDeliveryBoyBean.vehicleRegNo);
						}else{
							preparedStatement.setNull(5,Types.INTEGER);
						}
						preparedStatement.setInt(6, 1);
						if(manageDeliveryBoyBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(7, "Y");
						}
						else{
							preparedStatement.setString(7, "N");
						}
					
						preparedStatement.setString(8, userName);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							inserted = true;
							System.out.println("Delivery boy Details saved "+message);
						}
						if(inserted){
							Messagebox.show("Delivery boy details saved sucessfully!!");
							clearData();
							onLoadDeliveryBoyList();
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
	 * On change name
	 */
	@Command
	@NotifyChange("*")
	public void onChangeName(){
		manageDeliveryBoyBean.name = PropertyFile.toTitleCase(manageDeliveryBoyBean.name);
		manageDeliveryBoyBean.deliveryBoyUserId = getdeliveryBoyUserId();
	}
	
	
	public String getdeliveryBoyUserId(){
		String userId = "";
		Integer serialid=0;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT Count(delivery_boy_id) FROM fapp_delivery_boy";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							serialid = resultSet.getInt(1);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		userId =  "DLV"+String.format("%04d", serialid+1);
		System.out.println("Del boy id->"+userId);
		return userId;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean") ManageDeliveryBoyBean managedeliveryboybean){
		manageDeliveryBoyBean.kitchenId = managedeliveryboybean.kitchenId;
		manageDeliveryBoyBean.name = managedeliveryboybean.name;
		manageDeliveryBoyBean.phoneNo = managedeliveryboybean.phoneNo;
		manageDeliveryBoyBean.deliveryBoyUserId = managedeliveryboybean.deliveryBoyUserId;
		manageDeliveryBoyBean.password = managedeliveryboybean.password;
		manageDeliveryBoyBean.address = managedeliveryboybean.address;
		manageDeliveryBoyBean.status = managedeliveryboybean.status;
		manageDeliveryBoyBean.deliveryBoyId = managedeliveryboybean.deliveryBoyId;
		manageDeliveryBoyBean.vehicleRegNo = managedeliveryboybean.vehicleRegNo;
		manageDeliveryBoyBean.boyStatusBean.statusId = managedeliveryboybean.boyStatusId;
		manageDeliveryBoyBean.boyStatusBean.statusName = managedeliveryboybean.boyStatus;
		updateButtonVisibility = true;
		saveButtonVisibility = false;
	}
	
	/**
	 * on click update button functionality 	
	 */
	@Command
	@NotifyChange("*")
	public void onClickUpdateDeliveryBoy(){
		if(validateFields()){
			updateDeliveryBoyData();
		}
	}
	
	/**
	 * 
	 * Method for saving delivery boy details to database
	 */
	public void updateDeliveryBoyData(){
		String message="";
		Boolean inserted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql ="UPDATE fapp_delivery_boy SET delivery_boy_name=?, delivery_boy_phn_number=?,password=?,"
							+ " delivery_boy_vehicle_reg_no=?,is_active=?,delivery_boy_status_id=?, updated_by=?"
						      +" WHERE delivery_boy_id=?";

				try {
						preparedStatement =  connection.prepareStatement(sql);
						preparedStatement.setString(1, manageDeliveryBoyBean.name);
						preparedStatement.setString(2, manageDeliveryBoyBean.phoneNo);
						preparedStatement.setString(3, manageDeliveryBoyBean.password);
						if(manageDeliveryBoyBean.vehicleRegNo!=null){
							preparedStatement.setString(4, manageDeliveryBoyBean.vehicleRegNo);
						}else{
							preparedStatement.setNull(4,Types.NULL);
						}

						if(manageDeliveryBoyBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(5, "Y");
						}
						else{
							preparedStatement.setString(5, "N");
						}
						preparedStatement.setInt(6, manageDeliveryBoyBean.boyStatusBean.statusId);
						preparedStatement.setString(7, userName);
						preparedStatement.setInt(8, manageDeliveryBoyBean.deliveryBoyId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							inserted = true;
							System.out.println("Delivery boy Details saved "+message);
						}
						if(inserted){
							Messagebox.show("Delivery boy details updated sucessfully!!");
							clearData();
							onLoadDeliveryBoyList();
							saveButtonVisibility = true;
							updateButtonVisibility = false;
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
	
	/*
	 * 
	 * Method used for field validation
	 */
	public Boolean validateFields(){
			if(manageDeliveryBoyBean.name!=null){
				if(manageDeliveryBoyBean.phoneNo!=null){
					if(manageDeliveryBoyBean.password!=null){
							if(manageDeliveryBoyBean.status!=null){
								return true;
							}else{
								Messagebox.show("Status required!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
								return false;
							}
					}else{
						Messagebox.show("Password required!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
						return false;
					}
				}else{
					Messagebox.show("Mobile number required!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
					return false;
				}
			}else{
				Messagebox.show("Name required!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
	}
	
	/**
	 * 
	 * Method for clearing data after save
	 */
	public void clearData(){
		manageDeliveryBoyBean.name = null;
		manageDeliveryBoyBean.phoneNo = null;
		manageDeliveryBoyBean.address = null;
		manageDeliveryBoyBean.password = null;
		manageDeliveryBoyBean.deliveryBoyUserId = null;
		manageDeliveryBoyBean.status = null;
		manageDeliveryBoyBean.logisticsName= null;
		manageDeliveryBoyBean.kitchenName = null;
		manageDeliveryBoyBean.vehicleRegNo = null;
		manageDeliveryBoyBean.boyStatusBean.statusName = null;
		boyStatusList = loadAllStatus();
		
	}
	
	public ManageDeliveryBoyBean getManageDeliveryBoyBean() {
		return manageDeliveryBoyBean;
	}

	public void setManageDeliveryBoyBean(ManageDeliveryBoyBean manageDeliveryBoyBean) {
		this.manageDeliveryBoyBean = manageDeliveryBoyBean;
	}

	public ArrayList<ManageDeliveryBoyBean> getManageDeliveryBoyBeanList() {
		return manageDeliveryBoyBeanList;
	}

	public void setManageDeliveryBoyBeanList(
			ArrayList<ManageDeliveryBoyBean> manageDeliveryBoyBeanList) {
		this.manageDeliveryBoyBeanList = manageDeliveryBoyBeanList;
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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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

	public BoyStatus getBoyStatusBean() {
		return boyStatusBean;
	}

	public void setBoyStatusBean(BoyStatus boyStatusBean) {
		this.boyStatusBean = boyStatusBean;
	}

	public ArrayList<BoyStatus> getBoyStatusList() {
		return boyStatusList;
	}

	public void setBoyStatusList(ArrayList<BoyStatus> boyStatusList) {
		this.boyStatusList = boyStatusList;
	}

	
}
