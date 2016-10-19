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
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.CompletedOrderBean;
import Bean.OutForDeliveryBean;

public class CompletedOrderViewModel {

	private CompletedOrderBean completedOrderBean = new CompletedOrderBean();
	
	private ArrayList<CompletedOrderBean> completedOrderBeanList = new ArrayList<CompletedOrderBean>();

	Session session = null;
	
	private Connection connection = null;

	PropertyFile propertyfile = new PropertyFile();
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		//connection = DbConnection.getConnection();
		connection.setAutoCommit(true);
		
		onLoadQuery();
		reload();
	}
	
	public void reload(){
		for(int i=0;i<1;i++){
			CompletedOrderBean bean = new CompletedOrderBean();
			completedOrderBeanList.add(bean);
		}
	}
	
	public void onLoadQuery(){
		
		try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
					try {
							preparedStatement = connection.prepareStatement(propertyfile.getPropValues("completedOrderListSql"));
							
							resultSet = preparedStatement.executeQuery();
							while(resultSet.next()){
								CompletedOrderBean completedorderbean = new CompletedOrderBean();
								completedorderbean.orderId = resultSet.getInt("order_id");
								completedorderbean.orderNo = resultSet.getString("order_no");
								completedorderbean.orderBy = resultSet.getString("order_by");
								completedorderbean.emailId = resultSet.getString("user_mail_id");
								completedorderbean.contactNumber = resultSet.getString("contact_number");
								String address = resultSet.getString("delivery_address");
								String zone = resultSet.getString("delivery_zone");
								String landmark = resultSet.getString("instruction");
								String pincode = resultSet.getString("pincode");
								completedorderbean.deliveryAddress = address+", "+zone+", "+landmark+","+pincode;
								//completedorderbean.deliveryBoy = resultSet.getString("delivery_boy_name");
								//completedorderbean.deliveryBoyPhone = resultSet.getString("delivery_boy_phn_number");
								completedorderbean.orderStatus = resultSet.getString("order_status_name");
								
								completedOrderBeanList.add(completedorderbean);
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
	public void onClickOrderDetails(@BindingParam("bean")CompletedOrderBean completedorderbean){
		
		if(completedorderbean.orderId!=null){
			Map<String, CompletedOrderBean> parentMap =  new HashMap<String, CompletedOrderBean>();
			
			parentMap.put("parentObjectDataCompletedOrder", completedorderbean);
			Window orderDetailswindow = (Window) Executions.createComponents("orderdetails.zul", null, parentMap);
			
			orderDetailswindow.doModal();
		}else{
			Messagebox.show("Data not available!");
		}
		
	}
	public CompletedOrderBean getCompletedOrderBean() {
		return completedOrderBean;
	}

	public void setCompletedOrderBean(CompletedOrderBean completedOrderBean) {
		this.completedOrderBean = completedOrderBean;
	}

	public ArrayList<CompletedOrderBean> getCompletedOrderBeanList() {
		return completedOrderBeanList;
	}

	public void setCompletedOrderBeanList(
			ArrayList<CompletedOrderBean> completedOrderBeanList) {
		this.completedOrderBeanList = completedOrderBeanList;
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
