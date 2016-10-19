package ViewModel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

import Bean.NewOrderBean;
import Bean.ReceivedOrderBean;

public class ReceivedOrderListViewModel {
	
	private ReceivedOrderBean receivedOrderBean = null;
	
	private ArrayList<ReceivedOrderBean> receivedOrderBeanList = new ArrayList<ReceivedOrderBean>();

	Session session = null;
	
	private Connection connection = null;
	
	private Integer roleId = 0;
	
	private String username = ""; 
	
	private ArrayList<String> deliveryBoyList = new ArrayList<String>();
	
	PropertyFile propertyfile = new PropertyFile();

	@Wire("#winReceiveOrderList")
	private Window winReceiveOrderList;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		username = (String) session.getAttribute("login");
		roleId = (Integer) session.getAttribute("userRoleId");
		connection.setAutoCommit(true);
		
		onLoadQuery();
		fetchDeliveryBoyList();
		reload();
	}
	
	public void reload(){
		for(int i=0;i<1;i++){
			ReceivedOrderBean bean = new ReceivedOrderBean();
			receivedOrderBeanList.add(bean);
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
	 * Fetch delivery boy order list
	 */
	public void fetchDeliveryBoyList(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sqlAdmin = "SELECT delivery_boy_name FROM fapp_delivery_boy ";
					String sqlOthers = "SELECT delivery_boy_name FROM fapp_delivery_boy "
						    + "WHERE logistics_id=(SELECT logistics_id FROM fapp_logistics WHERE logistics_name=?)";
					try {
						if(roleId==1){
							preparedStatement = connection.prepareStatement(sqlAdmin);
						}else{
							preparedStatement = connection.prepareStatement(sqlOthers);
							preparedStatement.setString(1, username);
						}
						
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							deliveryBoyList.add(resultSet.getString(1));
						}
						
					}  catch (Exception e) {
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
	
	@Command
	@NotifyChange("*")
	public void onSelectDeliveryBoy(@BindingParam("bean")ReceivedOrderBean receivedOrderBean){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT delivery_boy_id FROM fapp_delivery_boy WHERE delivery_boy_name = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1,receivedOrderBean.deliveryBoyName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							receivedOrderBean.deliveryBoyId = resultSet.getInt("delivery_boy_id");
						}
					System.out.println("del boy id="+receivedOrderBean.deliveryBoyId);
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
	/**
	 * 
	 * This method loads the order list from view (vw_received_order_list)
	 */
	public void onLoadQuery(){	
		if(receivedOrderBeanList!=null){
			receivedOrderBeanList.clear();
		}
		try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						String sqlAdmin = "SELECT * FROM vw_logistics_order_list";
						String sqlLogistics = "SELECT * FROM vw_logistics_order_list "
								+ "WHERE logistics_id=(SELECT logistics_id FROM fapp_logistics WHERE logistics_name=?)";
					try {
							if(roleId==1){
								preparedStatement = connection.prepareStatement(sqlAdmin); 
							}else{
								preparedStatement = connection.prepareStatement(sqlLogistics);
								preparedStatement.setString(1, username);
							}
							resultSet = preparedStatement.executeQuery();
							while(resultSet.next()){
								ReceivedOrderBean receivedOrderBean = new ReceivedOrderBean();
								receivedOrderBean.orderId = resultSet.getInt("order_id");
								receivedOrderBean.statusId = resultSet.getInt("order_status_id");
								receivedOrderBean.orderStatus = resultSet.getString("order_status_name");
								receivedOrderBean.orderNo = resultSet.getString("order_no");
								receivedOrderBean.orderBy = resultSet.getString("order_by");
								receivedOrderBean.contactNumber = resultSet.getString("contact_number");
								receivedOrderBean.emailId = resultSet.getString("user_mail_id");
								String flatNo = resultSet.getString("flat_no");
								String streetName = resultSet.getString("street_name");
								String landmark = resultSet.getString("landmark");
								String deliveryLocation = resultSet.getString("delivery_location");
								String city = resultSet.getString("city");
								String pincode = resultSet.getString("pincode");
								receivedOrderBean.deliveryAddress = flatNo+", "+streetName+", "+landmark+", "+deliveryLocation+", "+city+", "+pincode;
								
								
								receivedOrderBeanList.add(receivedOrderBean);
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
	public void	onClickAssignDeliveryBoy(@BindingParam("bean")ReceivedOrderBean receivedorderbean){
		if(receivedorderbean.orderId!=null ){
			if(receivedorderbean.deliveryBoyId!=null){
				if(receivedorderbean.statusId==9){
					Boolean updateCount=false;
					String message="";
					try {
						SQL:{
								PreparedStatement preparedStatement = null;
								ResultSet resultSet = null;
								String sqlQuery = "SELECT func_assign_delivery_boy(?,4,?,'Y')";
								
								try {
									preparedStatement = connection.prepareStatement(sqlQuery);
									preparedStatement.setInt(1, receivedorderbean.deliveryBoyId);
									preparedStatement.setInt(2, receivedorderbean.orderId);
									resultSet = preparedStatement.executeQuery();
									if(resultSet.next()){
										message = resultSet.getString(1);
										updateCount = true;
									}
									if(updateCount){
										Messagebox.show("Delivery Boy::"+receivedorderbean.deliveryBoyName+" is assigned to order id::"+receivedorderbean.orderId+" sucessfully!");
										sendMessage(getDeviceRegId(receivedorderbean.orderId), receivedorderbean.deliveryBoyName);
										onLoadQuery();
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
				}else{
					Messagebox.show("Order not ready!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
				}
			}else{
				Messagebox.show("Please choose delivery boy!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
				//winReceiveOrderList.detach();
			}
		}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
			//winReceiveOrderList.detach();
		}
		
	}

	@Command
	@NotifyChange("*")
	public void onClickOrderDetails(@BindingParam("bean")ReceivedOrderBean receivedorderbean){
		if(receivedorderbean.orderId!=null){
			Map<String, ReceivedOrderBean> parentMap =  new HashMap<String, ReceivedOrderBean>();
			parentMap.put("parentObjectData", receivedorderbean);
			Window orderDetailswindow = (Window) Executions.createComponents("orderdetails.zul", null, parentMap);
			orderDetailswindow.doModal();
			
		}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
			//winReceiveOrderList.detach();
		}
	}
	
	public String getDeviceRegId(Integer orderId ){
		String deviceregId = "";
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			SQL:{
					String sql = "SELECT device_reg_id FROM fapp_devices"
								+" WHERE email_id = (SELECT user_mail_id FROM fapp_orders"
								+ " WHERE order_id = ? )";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, orderId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							deviceregId = resultSet.getString("device_reg_id");
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
		return deviceregId;
	}
	
	public static void sendMessage(String deviceregId,String deliveryBoy) throws IOException
	{
		System.out.println("Reg id::"+deviceregId);
		//String API_KEY = "AIzaSyCvaJt0tbW3Nn1pCPynPduVxo3T3l3_Yek"; //sender id got from google api console project(My)
		String API_KEY = "AIzaSyA03muwGMLqGmk2mwY3x1di5mI3jEVViqM";//(sir)
		String collpaseKey = "gcm_message"; //if messages are sent and not delivered yet to android device (as device might not be online), then only deliver latest message when device is online
		//String messageStr = "message content here"; //actual message content
		System.out.println("Reg id::"+deviceregId);
		String messageStr = "Delivery boy "+deliveryBoy +" is assigned for delivery!";
		//String messageId = "APA91bGgGzVQWb88wkRkACGmHJROeJSyQbzLvh3GgP2CASK_NBsuIXH15HcnMta3e9ZXMhdPN6Z3FSD2Pezf6bhgUuM2CF74SgZbG4Zr57LA76VVaNvSi7XM7QEuAVLIiTsXnVq3QAUFDo-ynD316bF10JGT3ZOaSQ"; //gcm registration id of android device
		String messageId = deviceregId;
				
		Sender sender = new Sender(API_KEY);
		Message.Builder builder = new Message.Builder();
		
		builder.collapseKey(collpaseKey);
		builder.timeToLive(30);
		builder.delayWhileIdle(true);
		builder.addData("message", messageStr);
		
		Message message = builder.build();
		
		List<String> androidTargets = new ArrayList<String>();
		//if multiple messages needs to be deliver then add more message ids to this list
		androidTargets.add(messageId);
		
		MulticastResult result = sender.send(message, androidTargets, 1);
		System.out.println("result = "+result);
		
		if (result.getResults() != null) 
		{
			System.out.println("Status:"+messageStr+" is sent to device reg id:"+messageId);
			int canonicalRegId = result.getCanonicalIds();
			System.out.println("canonicalRegId = "+canonicalRegId);
			
			if (canonicalRegId != 0) 
			{
            }
		}
		else 
		{
			int error = result.getFailure();
			System.out.println("Broadcast failure: " + error);
		}
	}
	
	
	public ReceivedOrderBean getReceivedOrderBean() {
		return receivedOrderBean;
	}

	public void setReceivedOrderBean(ReceivedOrderBean receivedOrderBean) {
		this.receivedOrderBean = receivedOrderBean;
	}

	public ArrayList<ReceivedOrderBean> getReceivedOrderBeanList() {
		return receivedOrderBeanList;
	}

	public void setReceivedOrderBeanList(
			ArrayList<ReceivedOrderBean> receivedOrderBeanList) {
		this.receivedOrderBeanList = receivedOrderBeanList;
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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ArrayList<String> getDeliveryBoyList() {
		return deliveryBoyList;
	}

	public void setDeliveryBoyList(ArrayList<String> deliveryBoyList) {
		this.deliveryBoyList = deliveryBoyList;
	}

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}

	public Window getWinReceiveOrderList() {
		return winReceiveOrderList;
	}

	public void setWinReceiveOrderList(Window winReceiveOrderList) {
		this.winReceiveOrderList = winReceiveOrderList;
	}
}
