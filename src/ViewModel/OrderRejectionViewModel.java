package ViewModel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Pre;
import org.zkoss.zk.ui.Component;
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
import Bean.OrderRejectionBean;

public class OrderRejectionViewModel {

	public OrderRejectionBean orderRejectionBean = new OrderRejectionBean();
	
	Session session = null;
	
	private Connection connection = null;
	
	@Wire("#winRejection")
	private Window winRejection;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW)Component view,
			@ExecutionArgParam("parentOrderObject")NewOrderBean newOrderBean) throws SQLException{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
		orderRejectionBean.orderId = newOrderBean.orderId  ;
		
		orderRejectionBean.kitchenId = newOrderBean.kitchenId;
		
		orderRejectionBean.orderNo = newOrderBean.orderNo;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickConfirm(){
		
		if(validateFields()){
			try {
				SQL:{
						PreparedStatement preparedStatement = null;
						String sql ="UPDATE fapp_order_tracking set rejected='Y',reject_reason=?,reject_date=? WHERE order_id=? AND kitchen_id=?";
						try {
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setString(1, orderRejectionBean.rejectReason);
							preparedStatement.setDate(2, new java.sql.Date(orderRejectionBean.rejectiondate.getTime()));
							preparedStatement.setInt(3, orderRejectionBean.orderId);
							preparedStatement.setInt(4, orderRejectionBean.kitchenId);
							
							int row = preparedStatement.executeUpdate();
							if(row > 0){
								System.out.println("Order id "+orderRejectionBean.orderId+" is rejected by kitchen id "+orderRejectionBean.kitchenId);
								Messagebox.show("Order Rejected!","Inforamtion",Messagebox.OK,Messagebox.INFORMATION);
								sendMessage(getDeviceRegId(orderRejectionBean.orderId), orderRejectionBean.orderNo);
								//getNearestKitchenId(orderRejectionBean.kitchenId);
								
								//orderAssignToKitchen(orderRejectionBean.orderId, getNearestKitchenId(orderRejectionBean.kitchenId));
								//Map<String, OrderRejectionBean> rejectionData = new HashMap<String, OrderRejectionBean>();
								//rejectionData.put("rejectedData", orderRejectionBean);
								BindUtils.postGlobalCommand(null, null, "globalReload", null);
								//BindUtils.postGlobalCommand(null, null, "globalReload", rejectionData);
								winRejection.detach();
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
	}

	public Integer getNearestKitchenId(Integer rejectionKitchenId){
    	Integer kitchenId = 0;
    	Double destlat = 0d, destlong = 0d;
    	try {
    		
    		SQL:{
		    		PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "Select latitude,longitude from fapp_kitchen where kitchen_id=?";
					try {
						preparedStatement=connection.prepareStatement(sql);
						preparedStatement.setInt(1, rejectionKitchenId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							destlat = resultSet.getDouble("latitude");
							destlong = resultSet.getDouble("longitude");
						}
					}catch (Exception e) {
						e.printStackTrace();
					} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}	
    		}
    	
    	
    		SQL:{
    				PreparedStatement preparedStatement = null;
    				ResultSet resultSet = null;
    				String sql ="SELECT kitchen_name,kitchen_id," 
								+" 3956 * 2 * "
								+" ASIN(SQRT( "
								+" POWER( SIN((? - abs(dest.latitude)) * pi()/180 / 2),2) "
								+" + COS(? * pi()/180 )  "
								+" * COS(abs(dest.latitude) * pi()/180)  "
								+" * POWER(SIN((? - abs(dest.longitude)) * pi()/180 / 2), 2) )) "
								+"  as distance "
								+" FROM fapp_kitchen dest "
								+" order by distance asc LIMIT 2";
    				try {
    					preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setDouble(1, destlat);
						preparedStatement.setDouble(2, destlat);
						preparedStatement.setDouble(3, destlong);
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							kitchenId = resultSet.getInt("kitchen_id");
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}	
    				System.out.println("New assigned kid->"+kitchenId);
    		}
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return kitchenId;
    }
	
	public Boolean orderAssignToKitchen(Integer orderId, Integer kitchenId){
    	Boolean kitchenAssigned = false;
    	try {
    		
			SQL:{
    				PreparedStatement preparedStatement= null;
    				String sql = "INSERT INTO fapp_order_tracking"
    						     +" (order_id, kitchen_id)"
    						     +" VALUES (?, ? );";
    				try {
						preparedStatement =connection.prepareStatement(sql);
						preparedStatement.setInt(1, orderId);
						preparedStatement.setInt(2, kitchenId);
    					int count = preparedStatement.executeUpdate();
    					if(count > 0){
    						kitchenAssigned = true;
    						System.out.println("Order "+orderId+"is assigned to nearest kitchen "+kitchenId+" :"+kitchenAssigned);
    					}
					} catch (Exception e) {
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
    	
    	return kitchenAssigned ; 
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
	
	/**
	 * Send notification to App
	 * @param deviceregId
	 * @throws IOException
	 */
	public static void sendMessage(String deviceregId,String orderNo) throws IOException
	{
		//String API_KEY = "AIzaSyCvaJt0tbW3Nn1pCPynPduVxo3T3l3_Yek"; //sender id got from google api console project(My)
		String API_KEY = "AIzaSyA03muwGMLqGmk2mwY3x1di5mI3jEVViqM";//(sir)
		String collpaseKey = "gcm_message"; //if messages are sent and not delivered yet to android device (as device might not be online), then only deliver latest message when device is online
		//String messageStr = "message content here"; //actual message content
		String messageStr = "Sorry! Your order "+orderNo+" rejected !";
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
	
	public Boolean validateFields(){
		if(orderRejectionBean.rejectReason!=null){
			return true;
		}else{
			Messagebox.show("Please Give reason for rejection of this order","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public OrderRejectionBean getOrderRejectionBean() {
		return orderRejectionBean;
	}

	public void setOrderRejectionBean(OrderRejectionBean orderRejectionBean) {
		this.orderRejectionBean = orderRejectionBean;
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
