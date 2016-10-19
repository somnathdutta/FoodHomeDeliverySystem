package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import Bean.AssignedOrderBean;
import Bean.CompletedOrderBean;
import Bean.DeliveredOrderBean;
import Bean.OrderItemDetailsBean;
import Bean.OutForDeliveryBean;
import Bean.ReceivedOrderBean;

public class OrderItemDetailsViewModel {

	private OrderItemDetailsBean orderItemDetailsBean = new OrderItemDetailsBean();
	
	private ArrayList<OrderItemDetailsBean> orderItemDetailsBeanList = new ArrayList<OrderItemDetailsBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("parentObjectData")ReceivedOrderBean orderBean,
			@ExecutionArgParam("parentObjectDataOutForDelivery")OutForDeliveryBean deliveryBean,
			@ExecutionArgParam("parentObjectDataAssignedOrder")AssignedOrderBean assignedorderBean,
			@ExecutionArgParam("parentObjectDataDeliveredOrder")DeliveredOrderBean deliveredorderBean,
			@ExecutionArgParam("parentObjectDataCompletedOrder")CompletedOrderBean completedOrderBean) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		if(orderBean!=null){
			
			orderItemDetailsBean.orderId = orderBean.orderId;
		}
		
		if(deliveryBean!= null){
			
			orderItemDetailsBean.orderId = deliveryBean.orderId;
			
		}
		
		if(assignedorderBean != null){
			
			orderItemDetailsBean.orderId = assignedorderBean.orderId;
		}
		
		if(deliveredorderBean !=null){
			orderItemDetailsBean.orderId = deliveredorderBean.orderId;
		}
		
		if(completedOrderBean != null){
			orderItemDetailsBean.orderId = completedOrderBean.orderId;
		}
		onLoadQuery();
	}

	public void onLoadQuery(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT * FROM vw_order_item_details_list WHERE order_id=?";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, orderItemDetailsBean.orderId);
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							OrderItemDetailsBean detailsBean =  new OrderItemDetailsBean();
							detailsBean.cuisineName = resultSet.getString("cuisin_name");
							detailsBean.categoryName = resultSet.getString("category_name");
							detailsBean.itemCode = resultSet.getString("item_code");
							detailsBean.itemName = resultSet.getString("item_name");
							detailsBean.itemDescription = resultSet.getString("item_description");
							detailsBean.quantity = resultSet.getInt("qty");
							detailsBean.price = resultSet.getDouble("total_price");
							detailsBean.status = resultSet.getString("order_status_name");
							detailsBean.kitchenName = resultSet.getString("kitchen_name");
							
							orderItemDetailsBeanList.add(detailsBean);
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
	
	public OrderItemDetailsBean getOrderItemDetailsBean() {
		return orderItemDetailsBean;
	}

	public void setOrderItemDetailsBean(OrderItemDetailsBean orderItemDetailsBean) {
		this.orderItemDetailsBean = orderItemDetailsBean;
	}

	public ArrayList<OrderItemDetailsBean> getOrderItemDetailsBeanList() {
		return orderItemDetailsBeanList;
	}

	public void setOrderItemDetailsBeanList(
			ArrayList<OrderItemDetailsBean> orderItemDetailsBeanList) {
		this.orderItemDetailsBeanList = orderItemDetailsBeanList;
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
}
