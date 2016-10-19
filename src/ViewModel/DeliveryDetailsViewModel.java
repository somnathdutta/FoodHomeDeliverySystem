package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
import Bean.DeliveryDetailsBean;

public class DeliveryDetailsViewModel {

	public DeliveryDetailsBean deliveryDetailsBean = new DeliveryDetailsBean();
	
	private Session session = null;
	
	private Connection connection = null;
	
	
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("parentObjectDataAssignedOrder")AssignedOrderBean assignedOrderBean) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		connection.setAutoCommit(true);
		
		if(assignedOrderBean.orderId != null ){
			
			deliveryDetailsBean.orderId = assignedOrderBean.orderId;
			
		}
		
		onLoadQuery();
	}
	
	public void onLoadQuery(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					
					try {
						
						preparedStatement = connection.prepareStatement(new PropertyFile().getPropValues("deliveryOrderDetailsSql"));
						preparedStatement.setInt(1, deliveryDetailsBean.orderId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							deliveryDetailsBean.orderBy = resultSet.getString("order_by");
							deliveryDetailsBean.contactNumber = resultSet.getString("contact_number");
							deliveryDetailsBean.emailId = resultSet.getString("user_mail_id");
							String flatNo = resultSet.getString("flat_no");
							String streetName = resultSet.getString("street_name");
							String landmark = resultSet.getString("landmark");
							String deliveryLocation = resultSet.getString("delivery_location");
							String city = resultSet.getString("city");
							String pincode = resultSet.getString("pincode");
							deliveryDetailsBean.deliveryAddress = flatNo+", "+streetName+", "+landmark+", "+deliveryLocation+", "+city+", "+pincode;
							
						}
					}  catch (Exception e) {
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

	public DeliveryDetailsBean getDeliveryDetailsBean() {
		return deliveryDetailsBean;
	}

	public void setDeliveryDetailsBean(DeliveryDetailsBean deliveryDetailsBean) {
		this.deliveryDetailsBean = deliveryDetailsBean;
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
