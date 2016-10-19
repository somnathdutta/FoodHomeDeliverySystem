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

import Bean.ManageDeliveryBoyBean;

public class GetListDeliveryBoyViewModel {
	private ManageDeliveryBoyBean manageDeliveryBoyBean = null;
	
	private ArrayList<ManageDeliveryBoyBean> manageDeliveryBoyBeanList = new ArrayList<ManageDeliveryBoyBean>();

	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	@AfterCompose
	public void initSetUp(@ContextParam(ContextType.VIEW) Component view,@ExecutionArgParam("parentData")ManageDeliveryBoyBean managedeliveryboybean)throws Exception{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		manageDeliveryBoyBean = managedeliveryboybean;
		connection.setAutoCommit(true);
		onloadAllDetails();
	}
	
	public void onloadAllDetails(){
		if(manageDeliveryBoyBeanList.size()>0){
			manageDeliveryBoyBeanList.clear();
		}
		try {
			SQL:{
				 PreparedStatement preparedStatement = null;
				 ResultSet resultSet = null;
				 String sql = "SELECT * FROM vw_order_list_delivery_boy WHERE delivery_boy_id=?";
				 try {
					 preparedStatement =connection.prepareStatement(sql);
					 System.out.println(manageDeliveryBoyBean.deliveryBoyId);
					 preparedStatement.setInt(1, manageDeliveryBoyBean.deliveryBoyId);
					 resultSet = preparedStatement.executeQuery();
					 while(resultSet.next()){
						 ManageDeliveryBoyBean manageDeliveryBoybean =  new ManageDeliveryBoyBean();
						 manageDeliveryBoybean.orderId = resultSet.getInt("order_id");
						 manageDeliveryBoybean.status = resultSet.getString("order_status_name");
						 manageDeliveryBoybean.deliveryDateTime = resultSet.getString("delivery_date_time");
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
		} catch (Exception e) {
			// TODO: handle exception
		}
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
	
}
