package ViewModel;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.ReceivedOrderBean;
import dao.FreshKitchenOrderDAO;

public class KitchenFreshOrderViewModel {
	public ReceivedOrderBean freshOrderBean = new ReceivedOrderBean();
	
	private ArrayList<ReceivedOrderBean> freshOrderBeanList = new ArrayList<ReceivedOrderBean>();
	
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
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		userName = (String) session.getAttribute("login");
		connection.setAutoCommit(true);
		onLoadQuery();
		reload();
		System.out.println("zul page >> kitchenFreshorders.zul");
	}
	
	public void reload(){
		for(int i=0;i<1;i++){
			ReceivedOrderBean bean = new ReceivedOrderBean();
			freshOrderBeanList.add(bean);
		}
	}
	/**
	 * 
	 * On load query to list of all orders which are received view (vw_received_order_list)
	 */
	public void onLoadQuery(){
		freshOrderBeanList= FreshKitchenOrderDAO.loadKitchenFreshOrders(connection, userName);
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickFind(){
		if(stdate!=null && endate!=null){
			freshOrderBeanList =FreshKitchenOrderDAO.showKitchenFreshOrdersWithinRange(connection, userName, stdate, endate);
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
		freshOrderBean.orderStatus ="";
		freshOrderBean.deliveryBoyName = "";
		freshOrderBean.deliveryBoyId = null;
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

	public ReceivedOrderBean getFreshOrderBean() {
		return freshOrderBean;
	}

	public void setFreshOrderBean(ReceivedOrderBean freshOrderBean) {
		this.freshOrderBean = freshOrderBean;
	}

	public ArrayList<ReceivedOrderBean> getFreshOrderBeanList() {
		return freshOrderBeanList;
	}

	public void setFreshOrderBeanList(
			ArrayList<ReceivedOrderBean> freshOrderBeanList) {
		this.freshOrderBeanList = freshOrderBeanList;
	}
	
}
