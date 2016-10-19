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
import Bean.ReceivedOrderBean;

public class AssignedOrderViewModel {
	
	private AssignedOrderBean assignedOrderBean = null;
	
	private ArrayList<AssignedOrderBean> assignedOrderBeanList = new ArrayList<AssignedOrderBean>();

	Session session = null;
	
	private Connection connection = null;
	
	private String username = "";
	
	private Integer roleId = 0;

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
			AssignedOrderBean bean = new AssignedOrderBean();
			assignedOrderBeanList.add(bean);
		}
	}
	public void onLoadQuery(){
		if(assignedOrderBeanList.size()>0){
			assignedOrderBeanList.clear();
		}
		try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
					try {
						
							if(roleId==1){
								preparedStatement = connection.prepareStatement(propertyfile.getPropValues("assignedOrderListSqlAdmin"));
							}else{
								preparedStatement = connection.prepareStatement(propertyfile.getPropValues("assignedOrderListSqlOthers"));
								preparedStatement.setString(1, username);
							}
														
							resultSet = preparedStatement.executeQuery();
							while(resultSet.next()){
								AssignedOrderBean assignedOrderbean = new AssignedOrderBean();
								assignedOrderbean.orderId = resultSet.getInt("order_id");
								assignedOrderbean.status = resultSet.getString("order_status_name");
								assignedOrderbean.deliveryBoy = resultSet.getString("delivery_boy_name");
								assignedOrderbean.deliveryBoyContactNumber = resultSet.getString("delivery_boy_phn_number");
								assignedOrderBeanList.add(assignedOrderbean);
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
	public void onClickOrderDetails(@BindingParam("bean")AssignedOrderBean assignedOrderbean){
		if(assignedOrderbean.orderId!=null){
			Map<String, AssignedOrderBean> parentMap =  new HashMap<String, AssignedOrderBean>();
			parentMap.put("parentObjectDataAssignedOrder", assignedOrderbean);
			Window orderDetailswindow = (Window) Executions.createComponents("orderdetails.zul", null, parentMap);
			orderDetailswindow.doModal();
			
		}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
			//winReceiveOrderList.detach();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickDeliveryDetails(@BindingParam("bean")AssignedOrderBean assignedOrderbean){
		//if(assignedOrderbean.orderId!=null){
			Map<String, AssignedOrderBean> parentMap =  new HashMap<String, AssignedOrderBean>();
			parentMap.put("parentObjectDataAssignedOrder", assignedOrderbean);
			Window orderDetailswindow = (Window) Executions.createComponents("deliverydetails.zul", null, parentMap);
			orderDetailswindow.doModal();
			
		/*}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
			//winReceiveOrderList.detach();
		}*/
	}
	
	/**
	 * 
	 * On receive order button click method
	 */
	@Command
	@NotifyChange("*")
	public void onClickOrderPickedUp(@BindingParam("bean")AssignedOrderBean assignedorderbean){
		if(assignedorderbean.orderId!=null){
			try {
				SQL:{
						PreparedStatement preparedStatement = null;
					try {
						preparedStatement =  connection.prepareStatement(propertyfile.getPropValues("orderPickedUpSql"));
						preparedStatement.setInt(1, assignedorderbean.orderId);
						int updatedRow = preparedStatement.executeUpdate();
						if(updatedRow>0){
							Messagebox.show("Order Picked up Successfully!");
							sendMessage(getDeviceRegId(assignedorderbean.orderId), assignedorderbean.deliveryBoy);
							onLoadQuery();
							reload();
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
		}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
		
	}
	private String getDeviceRegId(Integer orderId ){
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
	
	private void sendMessage(String deviceregId,String deliveryBoy) throws IOException
	{
		System.out.println("Reg id::"+deviceregId);
		//String API_KEY = "AIzaSyCvaJt0tbW3Nn1pCPynPduVxo3T3l3_Yek"; //sender id got from google api console project(My)
		String API_KEY = "AIzaSyA03muwGMLqGmk2mwY3x1di5mI3jEVViqM";//(sir)
		String collpaseKey = "gcm_message"; //if messages are sent and not delivered yet to android device (as device might not be online), then only deliver latest message when device is online
		//String messageStr = "message content here"; //actual message content
		System.out.println("Reg id::"+deviceregId);
		String messageStr = "Your Order picked up by delivery boy: "+deliveryBoy ;
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
	
	public AssignedOrderBean getAssignedOrderBean() {
		return assignedOrderBean;
	}

	public void setAssignedOrderBean(AssignedOrderBean assignedOrderBean) {
		this.assignedOrderBean = assignedOrderBean;
	}

	public ArrayList<AssignedOrderBean> getAssignedOrderBeanList() {
		return assignedOrderBeanList;
	}

	public void setAssignedOrderBeanList(
			ArrayList<AssignedOrderBean> assignedOrderBeanList) {
		this.assignedOrderBeanList = assignedOrderBeanList;
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
