package ViewModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Pre;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.sun.org.apache.regexp.internal.recompile;

import Bean.ManageDeliveryBoyBean;
import Bean.TaxesBean;

public class ManageDeliveryBoyViewModel {
	
	private ManageDeliveryBoyBean manageDeliveryBoyBean =  new ManageDeliveryBoyBean();
	
	private ArrayList<ManageDeliveryBoyBean> manageDeliveryBoyBeanList = new ArrayList<ManageDeliveryBoyBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private Integer roleId = 0;
	
	private ArrayList<String> logisticsList = new ArrayList<String>();
	
	private ArrayList<String> kitchenList = new ArrayList<String>();
	
	private ArrayList<String> cityList= new ArrayList<String>();
	
	private ArrayList<String> areaList= new ArrayList<String>();
	
	private Boolean saveButtonVisibility = true;
	
	private Boolean updateButtonVisibility = false;
	
	private Boolean logisticsComboBoxvisibility = false;
	
	private Boolean kitchenComboBoxvisibility = false;
	
	private Boolean assigngridVisibility = false;
	
	private String includeSrc="";
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetUp(@ContextParam(ContextType.VIEW) Component view)throws Exception{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		if(roleId==1){
			/*logisticsComboBoxvisibility = true;
			loadLogisticsList();*/
			kitchenComboBoxvisibility = true;
			loadKitchenList();
		}
		
		onLoadDeliveryBoyList();
		
	}
	
	
	/**
	 * Load all logistics List
	 */
	public void loadLogisticsList(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT logistics_name FROM fapp_logistics";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							logisticsList.add(resultSet.getString("logistics_name"));
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
	public void onSelectLogistics(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT logistics_id FROM fapp_logistics WHERE logistics_name = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, manageDeliveryBoyBean.logisticsName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							manageDeliveryBoyBean.logisticsId = resultSet.getInt("logistics_id");
							System.out.println("Logistics id-"+manageDeliveryBoyBean.logisticsId);
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
	 * Load all logistics List
	 */
	public void loadKitchenList(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT kitchen_name FROM fapp_kitchen WHERE is_active='Y' AND is_delete='N'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							kitchenList.add(resultSet.getString("kitchen_name"));
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
	public void onSelectKitchen(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT kitchen_id FROM fapp_kitchen WHERE kitchen_name = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, manageDeliveryBoyBean.kitchenName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							manageDeliveryBoyBean.kitchenId = resultSet.getInt("kitchen_id");
							System.out.println("Kitchen id-"+manageDeliveryBoyBean.kitchenId);
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
				 try {
					 if(roleId==1){
						 preparedStatement =connection.prepareStatement(propertyfile.getPropValues("onLoadDeliveryBoyListSqlAdmin"));
					 }else{
						 preparedStatement =connection.prepareStatement(propertyfile.getPropValues("onLoadDeliveryBoyListSql"));
						 preparedStatement.setString(1, userName);
					 }
					
					 resultSet = preparedStatement.executeQuery();
					 while(resultSet.next()){
						 ManageDeliveryBoyBean manageDeliveryBoybean =  new ManageDeliveryBoyBean();
						 manageDeliveryBoybean.deliveryBoyUserId = resultSet.getString("delivery_boy_user_id");
						 manageDeliveryBoybean.kitchenId = resultSet.getInt("kitchen_id");
						 manageDeliveryBoybean.deliveryBoyId = resultSet.getInt("delivery_boy_id");
						 manageDeliveryBoybean.name = resultSet.getString("delivery_boy_name");
						 manageDeliveryBoybean.phoneNo = resultSet.getString("delivery_boy_phn_number");
						 manageDeliveryBoybean.address = resultSet.getString("delivery_boy_address");
						 manageDeliveryBoybean.password = resultSet.getString("password");
						 manageDeliveryBoybean.totalDelivery = resultSet.getInt("total_delivery");
						 if(resultSet.getString("is_active").equals("Y")){
							 manageDeliveryBoybean.status = "Active";
						 }else{
							 manageDeliveryBoybean.status = "Deactive";
						 }
						 manageDeliveryBoybean.vehicleRegNo = resultSet.getString("delivery_boy_vehicle_reg_no");
						 manageDeliveryBoybean.orderAssigned = resultSet.getString("order_assigned");
						 manageDeliveryBoybean.boyStatus = resultSet.getString("delivery_boy_status");
						 if(manageDeliveryBoybean.boyStatus.equals("Active")){
							 manageDeliveryBoybean.activeVisibility = false;
							 manageDeliveryBoybean.suspendVisibility = true;
						 }else{
							 manageDeliveryBoybean.activeVisibility = true;
							 manageDeliveryBoybean.suspendVisibility = false;
						 }
						 
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
			// String sql = "SELECT COUNT(*) FROM fapp_orders WHERE order_status_id = 8 AND delivery_boy_id=?";
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
		onLoadDeliveryBoyList();
		clearData();
		/*manageDeliveryBoyBean.activeVisibility = true;
		manageDeliveryBoyBean.suspendVisibility = false;*/
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
					ResultSet resultSet = null;
							
				try {
						preparedStatement =  connection.prepareStatement(propertyfile.getPropValues("saveDeliveryBoySql"));
						if(roleId==1){
							preparedStatement.setInt(1, manageDeliveryBoyBean.kitchenId);
						}else{
							preparedStatement.setInt(1, getKitchenId());
						}
						preparedStatement.setString(2, manageDeliveryBoyBean.deliveryBoyUserId);
						preparedStatement.setString(3, manageDeliveryBoyBean.name);
						preparedStatement.setString(4, manageDeliveryBoyBean.phoneNo);
						preparedStatement.setString(5, manageDeliveryBoyBean.password);
						if(manageDeliveryBoyBean.address!=null){
							preparedStatement.setString(6, manageDeliveryBoyBean.address);
						}else{
							preparedStatement.setNull(6,Types.NULL);
						}
						
						if(manageDeliveryBoyBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(7, "Y");
						}
						else{
							preparedStatement.setString(7, "N");
						}
						preparedStatement.setString(8, manageDeliveryBoyBean.vehicleRegNo);
						preparedStatement.setString(9, userName);
						preparedStatement.setString(10, userName);
						
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
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
	
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean") ManageDeliveryBoyBean managedeliveryboybean){
		//manageDeliveryBoyBean.logisticsId = managedeliveryboybean.logisticsId;
		manageDeliveryBoyBean.kitchenId = managedeliveryboybean.kitchenId;
		manageDeliveryBoyBean.name = managedeliveryboybean.name;
		manageDeliveryBoyBean.phoneNo = managedeliveryboybean.phoneNo;
		manageDeliveryBoyBean.deliveryBoyUserId = managedeliveryboybean.deliveryBoyUserId;
		manageDeliveryBoyBean.password = managedeliveryboybean.password;
		manageDeliveryBoyBean.address = managedeliveryboybean.address;
		manageDeliveryBoyBean.status = managedeliveryboybean.status;
		manageDeliveryBoyBean.deliveryBoyId = managedeliveryboybean.deliveryBoyId;
		manageDeliveryBoyBean.vehicleRegNo = managedeliveryboybean.vehicleRegNo;
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
					ResultSet resultSet = null;
							
				try {
						preparedStatement =  connection.prepareStatement(propertyfile.getPropValues("updateDeliveryBoySql"));
						if(roleId==1){
							preparedStatement.setInt(1, manageDeliveryBoyBean.kitchenId);
						}else{
							preparedStatement.setInt(1, getKitchenId());
						}
						preparedStatement.setString(2, manageDeliveryBoyBean.deliveryBoyUserId);
						preparedStatement.setString(3, manageDeliveryBoyBean.name);
						preparedStatement.setString(4, manageDeliveryBoyBean.phoneNo);
						preparedStatement.setString(5, manageDeliveryBoyBean.password);
						if(manageDeliveryBoyBean.address!=null){
							preparedStatement.setString(6, manageDeliveryBoyBean.address);
						}else{
							preparedStatement.setNull(6,Types.NULL);
						}
						
						
						if(manageDeliveryBoyBean.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(7, "Y");
						}
						else{
							preparedStatement.setString(7, "N");
						}
						preparedStatement.setString(8, manageDeliveryBoyBean.vehicleRegNo);
						preparedStatement.setString(9, userName);
						preparedStatement.setString(10, userName);
						preparedStatement.setInt(11, manageDeliveryBoyBean.deliveryBoyId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
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
	
	/**
	 * 
	 * Method to get list by the delivery boy
	 */
	@Command
	@NotifyChange("*")
	public void onClickGetList(@BindingParam("bean")ManageDeliveryBoyBean managedeliveryboybean){
		Map<String, ManageDeliveryBoyBean> parentMap =  new HashMap<String, ManageDeliveryBoyBean>();
		parentMap.put("parentData", managedeliveryboybean);
		Window window = (Window) Executions.createComponents("getDeliveryList.zul", null, parentMap);
		window.doModal();
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickAssignDelivery(@BindingParam("bean")ManageDeliveryBoyBean managedeliveryboybean){
		if(managedeliveryboybean.boyStatus.equals("Active")){
			if(managedeliveryboybean.orderAssigned.equals("N")){
				Window window = (Window) Executions.createComponents("receivedOrderList.zul", null, null);
				window.doModal();
			}else{
				Messagebox.show("Delivery boy already assigned to some order !","ASSIGN FAILED",Messagebox.OK,Messagebox.EXCLAMATION);	
			}
		}else{
			Messagebox.show("Delivery boy suspended,can not be assigned !","ASSIGN FAILED",Messagebox.OK,Messagebox.EXCLAMATION);	
		}
		
		/*Boolean result=false;
		try {
			SQL:{	
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT * FROM fapp_delivery_boy WHERE order_assigned='N' AND delivery_boy_status_id=2 AND delivery_boy_id=? ";
				try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, managedeliveryboybean.deliveryBoyId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							result= true;
							Window window = (Window) Executions.createComponents("receivedOrderList.zul", null, null);
							window.doModal();
						}
						if(!result){
							Messagebox.show("Can not be assigned!","ASSIGN FAILED",Messagebox.OK,Messagebox.EXCLAMATION);
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
		}*/
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickSuspend(@BindingParam("bean")ManageDeliveryBoyBean managedeliveryboybean){
		
		Map<String, Object> parentMap = new HashMap<String, Object>();
		
		parentMap.put("parentCMSObject", managedeliveryboybean);
		
		Window win = (Window) Executions.createComponents("suspendConfirmationBox.zul", null, parentMap);
		
		win.doModal();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickActive(@BindingParam("bean")ManageDeliveryBoyBean managedeliveryboybean){
		if(managedeliveryboybean.boyStatus.equals("Suspend")){
			if(managedeliveryboybean.orderAssigned.equals("N")){
				try {
					SQL:{	
							PreparedStatement preparedStatement = null;
							String sql = "UPDATE fapp_delivery_boy SET delivery_boy_status_id=2 WHERE delivery_boy_id=? ";
						try {
								preparedStatement = connection.prepareStatement(sql);
								preparedStatement.setInt(1, managedeliveryboybean.deliveryBoyId);
								int updaterow = preparedStatement.executeUpdate();
								if(updaterow>0){
									Messagebox.show("Delivery boy activated sucessfully!");
									onLoadDeliveryBoyList();
									managedeliveryboybean.activeVisibility = false;
									managedeliveryboybean.suspendVisibility = true;
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
			}else{
				Messagebox.show("Delivery boy assigned to some order,can not be activated now!","ACTIVATIION FAILED",Messagebox.OK,Messagebox.EXCLAMATION);
			}	
		}else{
			Messagebox.show("Delivery boy already activated!","ACTIVATIION FAILED",Messagebox.OK,Messagebox.EXCLAMATION);
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
	
	/**
	 * Get Logistics id
	 */
	public Integer getLogisticsId(){
		Integer logisticsid = 0;
		try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						String sql = "SELECT logistics_id from fapp_logistics WHERE logistics_name = ?";
						
						try {
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setString(1, userName);
							resultSet = preparedStatement.executeQuery();
							if(resultSet.next()){
								logisticsid = resultSet.getInt(1);
							}
						} catch (Exception e) {
							Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						}finally{
							if(preparedStatement!=null){
								preparedStatement.close();
							}
						}
					}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return logisticsid;
	}
	
	/**
	 * Get Logistics id
	 */
	public Integer getKitchenId(){
		Integer kitchenid = 0;
		try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						String sql = "SELECT kitchen_id from fapp_kitchen WHERE kitchen_name = ?";
						
						try {
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setString(1, userName);
							resultSet = preparedStatement.executeQuery();
							if(resultSet.next()){
								kitchenid = resultSet.getInt(1);
							}
						} catch (Exception e) {
							Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						}finally{
							if(preparedStatement!=null){
								preparedStatement.close();
							}
						}
					}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return kitchenid;
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
	}
	/*
	 * 
	 * Method used for field validation
	 */
	public Boolean validateFields(){
			if(manageDeliveryBoyBean.name!=null){
				if(manageDeliveryBoyBean.phoneNo!=null){
					if(manageDeliveryBoyBean.password!=null){
					//	if(manageDeliveryBoyBean.address!=null){
							if(manageDeliveryBoyBean.status!=null){
								return true;
							}else{
								Messagebox.show("Status required!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
								return false;
							}
						/*}else{
							Messagebox.show("Address required!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
							return false;
						}*/
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
	
	
	
	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean")final ManageDeliveryBoyBean boyBean){
		Messagebox.show("Are you sure to delete?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
		        	deleteBoy(boyBean);
		          /*refresh();
				   onLoad();*/
		          BindUtils.postGlobalCommand(null, null, "globalReload", null);
				   Messagebox.show("Delivery boy data deleted successfully!");
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
	}
	
	public void deleteBoy(ManageDeliveryBoyBean boybean){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_delivery_boy"
				                +" SET is_delete='Y'"
				                +" WHERE delivery_boy_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, boybean.deliveryBoyId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							System.out.println("Delivery boy deleted sucessfully!");
						}
								
					} catch (Exception e) {
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
	/**
	 * 
	 * Mobile number validation method
	 */
	public Boolean validateMobileNumber(){
		if(manageDeliveryBoyBean.phoneNo.matches("[0-9]+") && manageDeliveryBoyBean.phoneNo.length()==10){
			return true;
		}
		else{
			return false;
		}
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

	public String getIncludeSrc() {
		return includeSrc;
	}

	public void setIncludeSrc(String includeSrc) {
		this.includeSrc = includeSrc;
	}

	

	public Boolean getAssigngridVisibility() {
		return assigngridVisibility;
	}

	public void setAssigngridVisibility(Boolean assigngridVisibility) {
		this.assigngridVisibility = assigngridVisibility;
	}

	public Boolean getLogisticsComboBoxvisibility() {
		return logisticsComboBoxvisibility;
	}

	public void setLogisticsComboBoxvisibility(Boolean logisticsComboBoxvisibility) {
		this.logisticsComboBoxvisibility = logisticsComboBoxvisibility;
	}

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}

	public ArrayList<String> getLogisticsList() {
		return logisticsList;
	}

	public void setLogisticsList(ArrayList<String> logisticsList) {
		this.logisticsList = logisticsList;
	}


	public Integer getRoleId() {
		return roleId;
	}


	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}


	public ArrayList<String> getKitchenList() {
		return kitchenList;
	}


	public void setKitchenList(ArrayList<String> kitchenList) {
		this.kitchenList = kitchenList;
	}


	public Boolean getKitchenComboBoxvisibility() {
		return kitchenComboBoxvisibility;
	}


	public void setKitchenComboBoxvisibility(Boolean kitchenComboBoxvisibility) {
		this.kitchenComboBoxvisibility = kitchenComboBoxvisibility;
	}
	
	
}
