package ViewModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.management.loading.PrivateClassLoader;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import dao.KitchenOrderDAO;
import dao.KitchenReceivedOrders;
import Bean.ReceivedOrderBean;

public class ReceivedOrderViewModel {
	public ReceivedOrderBean receivedOrderBean = new ReceivedOrderBean();
	
	private ArrayList<ReceivedOrderBean> receivedOrderBeanList = new ArrayList<ReceivedOrderBean>();
	
	private ArrayList<String> deliveryBoyList = new ArrayList<String>();
	
	private Integer quantity ;
	
	private Double itemPrice ;
	
	Session session = null;
	
	private Connection connection = null;
	
	@Wire("#winReceivedOrder")
	private Window winReceivedOrder;
	
	PropertyFile propertyfile = new PropertyFile();
	
	private String userName ;
	
	private Date stdate;
	
	private Date endate;

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("parentObjectData")ReceivedOrderBean receivedorderbean) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		userName = (String) session.getAttribute("login");
		connection.setAutoCommit(true);
		if(receivedorderbean!=null){
			receivedOrderBean =  receivedorderbean;
		}
		
		//onLoadeliveryBoyList();
		onLoadQuery();
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
	 * On load query to list of all orders which are received view (vw_received_order_list)
	 */
	public void onLoadQuery(){
		
		receivedOrderBeanList= KitchenReceivedOrders.loadKitchenReceivedOrders(connection, userName);
	}
	
	/**
	 * 
	 * Load all delivery boy list who are not assigned for any order and active
	 */
	public void onLoadeliveryBoyList(){
		if(deliveryBoyList.size()>0){
			deliveryBoyList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					//String sqlDeliveryBoy = "SELECT * FROM fapp_delivery_boy WHERE order_assigned='N' AND delivery_boy_status_id=2";
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("freeDeliveryBoyListSql"));
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							deliveryBoyList.add(resultSet.getString("delivery_boy_name"));
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
	}
	
	/**
	 * 
	 * On selecting delivery boy save the delivery boy id of that boy from the view (vw_free_delivery_boy_list)
	 */
	@Command
	@NotifyChange("*")
	public void onSelectdeliveryBoy(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					//String sqlDeliveryBoy = "SELECT delivery_boy_id FROM fapp_delivery_boy WHERE delivery_boy_name=?";
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("deliveryBoyIdWRTdeliveryBoyNameSQl"));
						preparedStatement.setString(1, receivedOrderBean.deliveryBoyName);
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							receivedOrderBean.deliveryBoyId = resultSet.getInt("delivery_boy_id");
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
	}
	
	/**
	 * 
	 * Method for assigning delivery boy to an order
	 */
	@Command
	@NotifyChange("*")
	public void onClickAssignDeliveryBoy(){
		Boolean updateCount=false;
		String message="";
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					/*String sqlQuery = "UPDATE fapp_orders SET delivery_boy_id=?,order_status_id=3 WHERE order_id=?";
										"UPDATE fapp_delivery_boy SET order_assigned='Y' WHERE delivery_boy_id=?"*/
					//String sqlQuery = "SELECT func_assign_delivery_boy(?,3,?,'Y')";
				try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("assignDeliveryBoySql"));
						preparedStatement.setInt(1, receivedOrderBean.deliveryBoyId);
						preparedStatement.setInt(2, receivedOrderBean.orderId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message = resultSet.getString(1);
							updateCount = true;
						}
						if(updateCount){
							Messagebox.show("Delivery Boy::"+receivedOrderBean.deliveryBoyName+" is assigned to order id::"+receivedOrderBean.orderId+" sucessfully!");
							BindUtils.postGlobalCommand(null, null, "globalReload", null);
							winReceivedOrder.detach();	
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
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickFind(){
		if(stdate!=null && endate!=null){
			receivedOrderBeanList = KitchenReceivedOrders.showKitchenReceivedOrdersWithinRange(connection, userName, stdate, endate);
		}else{
			Messagebox.show("Search value required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		stdate=null;
		endate= null;
		onLoadQuery();
	}
	
	/**
	 * 
	 * Clearing data after update
	 */
	public void cleardata(){
		onLoadQuery();
		quantity = null;
		itemPrice = null;
		receivedOrderBean.orderStatus ="";
		receivedOrderBean.deliveryBoyName = "";
		receivedOrderBean.deliveryBoyId = null;
	}
	
	
	public ReceivedOrderBean getReceivedOrderBean() {
		return receivedOrderBean;
	}
	public void setReceivedOrderBean(ReceivedOrderBean receivedOrderBean) {
		this.receivedOrderBean = receivedOrderBean;
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
	public ArrayList<ReceivedOrderBean> getReceivedOrderBeanList() {
		return receivedOrderBeanList;
	}
	public void setReceivedOrderBeanList(
			ArrayList<ReceivedOrderBean> receivedOrderBeanList) {
		this.receivedOrderBeanList = receivedOrderBeanList;
	}

	public ArrayList<String> getDeliveryBoyList() {
		return deliveryBoyList;
	}

	public void setDeliveryBoyList(ArrayList<String> deliveryBoyList) {
		this.deliveryBoyList = deliveryBoyList;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Window getWinReceivedOrder() {
		return winReceivedOrder;
	}

	public void setWinReceivedOrder(Window winReceivedOrder) {
		this.winReceivedOrder = winReceivedOrder;
	}

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getStdate() {
		return stdate;
	}

	public void setStdate(Date stdate) {
		this.stdate = stdate;
	}

	public Date getEndate() {
		return endate;
	}

	public void setEndate(Date endate) {
		this.endate = endate;
	}
	
}
