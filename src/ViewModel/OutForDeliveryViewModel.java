package ViewModel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

import Bean.AssignedOrderBean;
import Bean.NewOrderBean;
import Bean.OutForDeliveryBean;
import Bean.ReceivedOrderBean;

public class OutForDeliveryViewModel {
	private OutForDeliveryBean outForDeliveryBean = null;
	
	private ArrayList<OutForDeliveryBean> outForDeliveryBeanList = new ArrayList<OutForDeliveryBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private Integer roleId = 0;
	
	private String username = ""; 
	
	private String orderNo = "";
	
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
			OutForDeliveryBean bean = new OutForDeliveryBean();
			outForDeliveryBeanList.add(bean);
		}
	}
	public void onLoadQuery(){
		if(outForDeliveryBeanList.size()>0){
			outForDeliveryBeanList.clear();
		}
		try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
					try {
						
						/*if(roleId==1){
							preparedStatement = connection.prepareStatement(propertyfile.getPropValues("outForDeliveryOrderListSqlAdmin"));
						}else{
							preparedStatement = connection.prepareStatement(propertyfile.getPropValues("outForDeliveryOrderListSql"));
							preparedStatement.setString(1, username);
						}*/
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("outForDeliveryOrderListSqlAdmin"));
							
							resultSet = preparedStatement.executeQuery();
							while(resultSet.next()){
								OutForDeliveryBean outForDeliveryBean = new OutForDeliveryBean();
								outForDeliveryBean.orderId = resultSet.getInt("order_id");
								outForDeliveryBean.orderNo = resultSet.getString("order_no");
								outForDeliveryBean.status = resultSet.getString("order_status_name");
								outForDeliveryBean.orderBy = resultSet.getString("order_by");
								outForDeliveryBean.emailId = resultSet.getString("user_mail_id");
								outForDeliveryBean.contactNumber = resultSet.getString("contact_number");
								String flatNo = resultSet.getString("flat_no");
								String streetName = resultSet.getString("street_name");
								String landmark = resultSet.getString("landmark");
								String deliveryLocation = resultSet.getString("delivery_location");
								String city = resultSet.getString("city");
								String pincode = resultSet.getString("pincode");
								outForDeliveryBean.deliveryAddress = flatNo+", "+streetName+", "+landmark+", "+deliveryLocation+", "+city+", "+pincode;
								//outForDeliveryBean.deliveryBoy = resultSet.getString("delivery_boy_name");
								//outForDeliveryBean.deliveryBoyPhone = resultSet.getString("delivery_boy_phn_number");
								outForDeliveryBeanList.add(outForDeliveryBean);
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
	public void onClickOrderDetails(@BindingParam("bean")OutForDeliveryBean deliveryBean){
		
		if(deliveryBean.orderId!=null){
			Map<String, OutForDeliveryBean> parentMap =  new HashMap<String, OutForDeliveryBean>();
			
			parentMap.put("parentObjectDataOutForDelivery", deliveryBean);
			
			Window orderDetailswindow = (Window) Executions.createComponents("orderdetails.zul", null, parentMap);
			
			orderDetailswindow.doModal();
		}else{
			Messagebox.show("Data not available!");
		}
		
	}
	
	/**
	 * 
	 * Method for marking as delivered
	 */
	@Command
	@NotifyChange("*")
	public void onClickMarkAsDelivered(@BindingParam("bean")OutForDeliveryBean outfordeliverybean){
		if(outfordeliverybean.orderId!=null){
			try {
				SQL:{
						PreparedStatement preparedStatement = null;
					try {
							preparedStatement = connection.prepareStatement(propertyfile.getPropValues("markedAsDeliveredSql"));
							preparedStatement.setInt(1, outfordeliverybean.orderId);
							int updateCount = preparedStatement.executeUpdate();
							if(updateCount>0){
								Messagebox.show("Order Marked as delivered succesfully ");
								sendMessage(getDeviceRegId(outfordeliverybean.orderId));
								onLoadQuery();
								reload();
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
			Messagebox.show("Data not available!","CALL INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickCall(@BindingParam("bean")OutForDeliveryBean outfordeliverybean){
		if(outfordeliverybean.orderId!=null){
			Messagebox.show("Please call to delivery boy "+outfordeliverybean.deliveryBoy+"'s phone number ::"+outfordeliverybean.deliveryBoyPhone,"CALL INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}else{
			Messagebox.show("Data not available!","CALL INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onChangeOrderNo(){
	//System.out.println("On chanege order no");
		if(outForDeliveryBeanList.size()>0){
			outForDeliveryBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_complete_the_order where order_no ILIKE ? ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						if(orderNo!=null && orderNo.trim().length()>0){
							preparedStatement.setString(1, (orderNo+"%"));
						}	
						else
							preparedStatement.setString(1, "%%");
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							OutForDeliveryBean outForDeliveryBean = new OutForDeliveryBean();
							outForDeliveryBean.orderId = resultSet.getInt("order_id");
							outForDeliveryBean.orderNo = resultSet.getString("order_no");
							outForDeliveryBean.status = resultSet.getString("order_status_name");
							outForDeliveryBean.orderBy = resultSet.getString("order_by");
							outForDeliveryBean.emailId = resultSet.getString("user_mail_id");
							outForDeliveryBean.contactNumber = resultSet.getString("contact_number");
							String flatNo = resultSet.getString("flat_no");
							String streetName = resultSet.getString("street_name");
							String landmark = resultSet.getString("landmark");
							String deliveryLocation = resultSet.getString("delivery_location");
							String city = resultSet.getString("city");
							String pincode = resultSet.getString("pincode");
							outForDeliveryBean.deliveryAddress = flatNo+", "+streetName+", "+landmark+", "+deliveryLocation+", "+city+", "+pincode;
							//outForDeliveryBean.deliveryBoy = resultSet.getString("delivery_boy_name");
							//outForDeliveryBean.deliveryBoyPhone = resultSet.getString("delivery_boy_phn_number");
							outForDeliveryBeanList.add(outForDeliveryBean);
						}
						
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
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
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		orderNo= null;
		onLoadQuery();
		
	}
	/**
	 * Send notification to App
	 * @param deviceregId
	 * @throws IOException
	 */
	public static void sendMessage(String deviceregId) throws IOException
	{
		//String API_KEY = "AIzaSyCvaJt0tbW3Nn1pCPynPduVxo3T3l3_Yek"; //sender id got from google api console project(My)
		String API_KEY = "AIzaSyA03muwGMLqGmk2mwY3x1di5mI3jEVViqM";//(sir)
		String collpaseKey = "gcm_message"; //if messages are sent and not delivered yet to android device (as device might not be online), then only deliver latest message when device is online
		//String messageStr = "message content here"; //actual message content
		String messageStr = "Your order is delivered sucessfully!";
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
	
	public OutForDeliveryBean getOutForDeliveryBean() {
		return outForDeliveryBean;
	}

	public void setOutForDeliveryBean(OutForDeliveryBean outForDeliveryBean) {
		this.outForDeliveryBean = outForDeliveryBean;
	}

	public ArrayList<OutForDeliveryBean> getOutForDeliveryBeanList() {
		return outForDeliveryBeanList;
	}

	public void setOutForDeliveryBeanList(
			ArrayList<OutForDeliveryBean> outForDeliveryBeanList) {
		this.outForDeliveryBeanList = outForDeliveryBeanList;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}
}
