package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.DeliveredOrderBean;
import Bean.OutForDeliveryBean;
import Bean.ReceivedOrderBean;

public class DeliveredOrderListViewModel {
	private DeliveredOrderBean deliveredOrderBean = null;
	
	private ArrayList<DeliveredOrderBean> deliveredOrderBeanList = new ArrayList<DeliveredOrderBean>();

	Session session = null;
	
	private Connection connection = null;
	
	private Integer roleId = 0;
	
	private String username = ""; 
	
	PropertyFile propertyfile = new PropertyFile();

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		username = (String) session.getAttribute("login");
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		connection.setAutoCommit(true);
		
		onLoadQuery();
		
		reload();
	}
	
	public void reload(){
		for(int i=0;i<1;i++){
			DeliveredOrderBean bean = new DeliveredOrderBean();
			deliveredOrderBeanList.add(bean);
		}
	}
	
	/**
	 * 
	 * GlobalReload is used to reload the grid when child window is detach
	 */
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		onLoadQuery();
		reload();
	}
	
	/**
	 * 
	 * This method loads the order list from view (vw_new_order_list)
	 */
	public void onLoadQuery(){	
		if(deliveredOrderBeanList!=null){
			deliveredOrderBeanList.clear();
		}
		try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
					try {
							if(roleId==1){
								preparedStatement = connection.prepareStatement(new PropertyFile().getPropValues("deliveredOrderListSqlAdmin"));
							}else{
								preparedStatement = connection.prepareStatement(new PropertyFile().getPropValues("deliveredOrderListSqlOthers"));
								preparedStatement.setString(1, username);
							}
							
							resultSet = preparedStatement.executeQuery();
							while(resultSet.next()){
								DeliveredOrderBean deliveredorderbean = new DeliveredOrderBean(); 
								deliveredorderbean.orderId = resultSet.getInt("order_id");
								deliveredorderbean.orderBy = resultSet.getString("order_by");
								deliveredorderbean.emailId = resultSet.getString("user_mail_id");
								deliveredorderbean.contactNumber = resultSet.getString("contact_number");
								String flatNo = resultSet.getString("flat_no");
								String streetName = resultSet.getString("street_name");
								String landmark = resultSet.getString("landmark");
								String deliveryLocation = resultSet.getString("delivery_location");
								String city = resultSet.getString("city");
								String pincode = resultSet.getString("pincode");
								deliveredorderbean.deliveryAddress = flatNo+", "+streetName+", "+landmark+", "+deliveryLocation+", "+city+", "+pincode;
								deliveredorderbean.deliveryBoy = resultSet.getString("delivery_boy_name");
								deliveredorderbean.deliveryBoyPhone = resultSet.getString("delivery_boy_phn_number");
								deliveredorderbean.orderStatus = resultSet.getString("order_status_name");
								
								deliveredOrderBeanList.add(deliveredorderbean);
							}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement !=null){
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
	public void onClickOrderDetails(@BindingParam("bean")DeliveredOrderBean deliveredorderbean){
		
		if(deliveredorderbean.orderId!=null){
			
			Map<String, DeliveredOrderBean> parentMap =  new HashMap<String, DeliveredOrderBean>();
			
			parentMap.put("parentObjectDataDeliveredOrder", deliveredorderbean);
			
			Window orderDetailswindow = (Window) Executions.createComponents("orderdetails.zul", null, parentMap);
			
			orderDetailswindow.doModal();
		}else{
			Messagebox.show("Data not available!");
		}
		
	}
	
	/**
	 * 
	 * On receive order button click method
	 */
	@Command
	@NotifyChange("*")
	public void onClickUploadPOD(@BindingParam("bean")DeliveredOrderBean deliveredorderbean){
		if(deliveredorderbean.getOrderId()!=null){
			Map<String, DeliveredOrderBean> parentMap =  new HashMap<String, DeliveredOrderBean>();
			parentMap.put("parentObjectData", deliveredorderbean);
			Window window = (Window) Executions.createComponents("deliveredOrder.zul", null, parentMap);
			window.doModal();
		}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	public DeliveredOrderBean getDeliveredOrderBean() {
		return deliveredOrderBean;
	}

	public void setDeliveredOrderBean(DeliveredOrderBean deliveredOrderBean) {
		this.deliveredOrderBean = deliveredOrderBean;
	}

	public ArrayList<DeliveredOrderBean> getDeliveredOrderBeanList() {
		return deliveredOrderBeanList;
	}

	public void setDeliveredOrderBeanList(
			ArrayList<DeliveredOrderBean> deliveredOrderBeanList) {
		this.deliveredOrderBeanList = deliveredOrderBeanList;
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
}
