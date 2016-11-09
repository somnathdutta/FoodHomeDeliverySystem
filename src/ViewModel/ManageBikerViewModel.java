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

import dao.ManageBikerDAO;
import Bean.BoyStatus;
import Bean.ManageDeliveryBoyBean;
import Bean.ManageKitchens;

public class ManageBikerViewModel {
	
	private ManageDeliveryBoyBean manageDeliveryBoyBean =  new ManageDeliveryBoyBean();
	
	private BoyStatus boyStatusBean = new BoyStatus();
	
	private ArrayList<BoyStatus> boyStatusList = new ArrayList<BoyStatus>();
	
	private ManageKitchens kicthenBean = new ManageKitchens();
	
	private ArrayList<ManageKitchens> kitchenList = new ArrayList<ManageKitchens>();
	
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
		
		onLoadKitchens();
		
		loadAllBiker();
		
		boyStatusList =  loadAllStatus();
	}

	public void onLoadKitchens(){
		kitchenList = ManageBikerDAO.loadKitchenList(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectKitchen(){
		manageDeliveryBoyBean.kitchenId = kicthenBean.kitchenId;	
	}
	
	public void loadAllBiker(){
		manageDeliveryBoyBeanList = ManageBikerDAO.loadDeliveryBoyList(connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickCancel(){
		clearData();
		onLoadKitchens();
		loadAllBiker();
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
			manageDeliveryBoyBean.userName = userName;
			ManageBikerDAO.saveDeliveryBoyData(connection, manageDeliveryBoyBean);
			clearData();
			loadAllBiker();
			onLoadKitchens();
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
		kicthenBean.kitchenName = managedeliveryboybean.kitchenName;
		kicthenBean.kitchenId = managedeliveryboybean.kitchenId;
		manageDeliveryBoyBean.kitchenId = managedeliveryboybean.kitchenId;
		manageDeliveryBoyBean.kitchenName = managedeliveryboybean.kitchenName;
		manageDeliveryBoyBean.isPickJiBoy = managedeliveryboybean.isPickJiBoy;
		manageDeliveryBoyBean.isSingleOrderBoy = managedeliveryboybean.isSingleOrderBoy;
	}
	
	/**
	 * on click update button functionality 	
	 */
	@Command
	@NotifyChange("*")
	public void onClickUpdateDeliveryBoy(){
		if(validateFields()){
			manageDeliveryBoyBean.userName = userName;
			ManageBikerDAO.updateDeliveryBoyData(connection, manageDeliveryBoyBean);
			clearData();
			loadAllBiker();
			onLoadKitchens();
			saveButtonVisibility = true;
			updateButtonVisibility = false;
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
		manageDeliveryBoyBean.isPickJiBoy = null;
		manageDeliveryBoyBean.isSingleOrderBoy = null;
		kitchenList.clear();
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

	public ArrayList<ManageKitchens> getKitchenList() {
		return kitchenList;
	}

	public void setKitchenList(ArrayList<ManageKitchens> kitchenList) {
		this.kitchenList = kitchenList;
	}

	public ManageKitchens getKicthenBean() {
		return kicthenBean;
	}

	public void setKicthenBean(ManageKitchens kicthenBean) {
		this.kicthenBean = kicthenBean;
	}

	
}
