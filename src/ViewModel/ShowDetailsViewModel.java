package ViewModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.NewOrderBean;
import Bean.OrderUserDetailsBean;
import Bean.SubscriptionBean;

public class ShowDetailsViewModel {


	private OrderUserDetailsBean orderUserDetailsBean = new OrderUserDetailsBean();
	
	private NewOrderBean newOrderBean = new NewOrderBean();
	
	private ArrayList<OrderUserDetailsBean> orderUserDetailsBeanList = new ArrayList<OrderUserDetailsBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	PropertyFile propertyfile = new PropertyFile();

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view ,
			@ExecutionArgParam("parentOrderObject")NewOrderBean bean,
			@ExecutionArgParam("parentSubscriptionOrderObject")SubscriptionBean subscriptionBean) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		connection.setAutoCommit(true);
		
		if(bean!=null){
			orderUserDetailsBean.orderId = bean.orderId  ;
			onLoadQuery();
		}
		
		
		if(subscriptionBean!=null){
			orderUserDetailsBean.orderId = subscriptionBean.subscriptionId;
			onLoadQueryForSubscription();
		}
		//reload();	
	}
	
	public void onLoadQuery(){
		if(orderUserDetailsBeanList.size()>0){
			orderUserDetailsBeanList.clear();
		}
		try {
			
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					/*String sql ="SELECT foud.order_by,"
								+" fo.user_mail_id,"
								+" fo.contact_number,"
								+" fo.order_date,"
								+" foud.foud.pincode" 
								+" FROM fapp_orders fo,"
								+" fapp_order_user_details foud"
								+" WHERE "
								+" fo.order_id = foud.order_id and"
								+" fo.order_id=?";*/
					String sql= "select * from vw_orders_delivery_address where order_id= ?";
					//String subsql = "select * from vw_subscribed_order_list where subscription_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1,orderUserDetailsBean.orderId );
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							orderUserDetailsBean.name = resultSet.getString("order_by");
							orderUserDetailsBean.emailId = resultSet.getString("user_mail_id");
							orderUserDetailsBean.contactNumber = resultSet.getString("contact_number");
							orderUserDetailsBean.deliveryZone = resultSet.getString("delivery_zone");
							orderUserDetailsBean.deliveryAddress = resultSet.getString("delivery_address");
							orderUserDetailsBean.deliveryPincode = resultSet.getString("pincode");
							orderUserDetailsBean.instruction = resultSet.getString("instruction");
							String orderDate="",reformattedOrderDate="";
							SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
							
							orderDate = resultSet.getString("order_date");
							java.util.Date stdate = null;
							try {
								reformattedOrderDate = myFormat.format(fromUser.parse(orderDate));
								System.out.println("refor--"+reformattedOrderDate);
								 stdate = myFormat.parse(reformattedOrderDate);
								 System.out.println("st1="+stdate);
								 
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							
							orderUserDetailsBean.orderedDateValue =  reformattedOrderDate;
							/*String flatNo = resultSet.getString("flat_no");
							String streetName = resultSet.getString("street_name");
							String landmark = resultSet.getString("landmark");
							String deliveryLocation = resultSet.getString("delivery_location");
							String city = resultSet.getString("city");
							String pincode = resultSet.getString("pincode");
							orderUserDetailsBean.deliveryAddress = flatNo+", "+streetName+", "+landmark+", "+deliveryLocation+", "+city+", "+pincode;*/
							orderUserDetailsBeanList.add(orderUserDetailsBean);
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

	public void onLoadQueryForSubscription(){
		if(orderUserDetailsBeanList.size()>0){
			orderUserDetailsBeanList.clear();
		}
		try {
			
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_subscribed_order_list where subscription_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1,orderUserDetailsBean.orderId );
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							orderUserDetailsBean.name = resultSet.getString("subscribed_by");
							orderUserDetailsBean.emailId = resultSet.getString("user_mail_id");
							orderUserDetailsBean.contactNumber = resultSet.getString("contact_number");
							orderUserDetailsBean.deliveryZone = resultSet.getString("delivery_zone");
							orderUserDetailsBean.deliveryAddress = resultSet.getString("delivery_address");
							orderUserDetailsBean.deliveryPincode = resultSet.getString("pincode");
							orderUserDetailsBean.instruction = resultSet.getString("instruction");
							String orderDate="",reformattedOrderDate="";
							SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
							
							orderDate = resultSet.getString("subscription_date");
							java.util.Date stdate = null;
							try {
								reformattedOrderDate = myFormat.format(fromUser.parse(orderDate));
								System.out.println("refor--"+reformattedOrderDate);
								 stdate = myFormat.parse(reformattedOrderDate);
								 System.out.println("st1="+stdate);
								 
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
							
							orderUserDetailsBean.orderedDateValue =  reformattedOrderDate;
							/*String flatNo = resultSet.getString("flat_no");
							String streetName = resultSet.getString("street_name");
							String landmark = resultSet.getString("landmark");
							String deliveryLocation = resultSet.getString("delivery_location");
							String city = resultSet.getString("city");
							String pincode = resultSet.getString("pincode");
							orderUserDetailsBean.deliveryAddress = flatNo+", "+streetName+", "+landmark+", "+deliveryLocation+", "+city+", "+pincode;*/
							orderUserDetailsBeanList.add(orderUserDetailsBean);
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

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}

	public OrderUserDetailsBean getOrderUserDetailsBean() {
		return orderUserDetailsBean;
	}

	public void setOrderUserDetailsBean(OrderUserDetailsBean orderUserDetailsBean) {
		this.orderUserDetailsBean = orderUserDetailsBean;
	}

	public NewOrderBean getNewOrderBean() {
		return newOrderBean;
	}

	public void setNewOrderBean(NewOrderBean newOrderBean) {
		this.newOrderBean = newOrderBean;
	}

	public ArrayList<OrderUserDetailsBean> getOrderUserDetailsBeanList() {
		return orderUserDetailsBeanList;
	}

	public void setOrderUserDetailsBeanList(
			ArrayList<OrderUserDetailsBean> orderUserDetailsBeanList) {
		this.orderUserDetailsBeanList = orderUserDetailsBeanList;
	}
}
