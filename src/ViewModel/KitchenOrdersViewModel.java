package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import dao.KitchenOrderDAO;
import Bean.OrderDashBoardBean;

public class KitchenOrdersViewModel {

private OrderDashBoardBean kitchenOrderBean = new OrderDashBoardBean();
	
	private ArrayList<OrderDashBoardBean> kitchenOrderBeanList = new ArrayList<OrderDashBoardBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private Integer roleId = 0;
	
	private String userName ;
	
	private Date startDate;
	
	private Date endDate;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		onLoadQuery();
		
		/*for(OrderDashBoardBean bean: kitchenOrderBeanList){
			System.out.println("Order no :: "+bean.orderNo);
		}*/
	}

	/**
	 * Load all orders for a kitchen
	 */
	public void onLoadQuery(){
		kitchenOrderBeanList = KitchenOrderDAO.onLoadQuery(connection, userName);
	}
	
	/**
	 * After confirmation call this method
	 */
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		onLoadQuery();
	}

	
	/**
	 * Method for order notify
	 * @param kitchenBean
	 */
	@Command
	@NotifyChange("*")
	public void onClickDetails(@BindingParam("bean")OrderDashBoardBean kitchenBean){
		Map<String, OrderDashBoardBean> parentMap = new HashMap<String, OrderDashBoardBean>();
		parentMap.put("parentBean", kitchenBean);
		Window window = (Window) Executions.createComponents("kitchenOrderItems.zul", null, null);
		window.doModal();
	}	
	
	/**
	 * Method for order receive
	 * @param kitchenBean
	 */
	@Command
	@NotifyChange("*")
	public void onClickReceive(@BindingParam("bean")OrderDashBoardBean kitchenBean){
	
		Messagebox.show("Are you sure to Receive?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
		          //write code here
		        	BindUtils.postGlobalCommand(null, null, "globalReload", null);
				   Messagebox.show("Order received successfully!","Success Information",Messagebox.OK,Messagebox.INFORMATION);
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
	}
	
	/**
	 * Method for order notify
	 * @param kitchenBean
	 */
	@Command
	@NotifyChange("*")
	public void onClickNotify(@BindingParam("bean")OrderDashBoardBean kitchenBean){
		
		Messagebox.show("Are you sure to Notify?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
		          //write code here
		        	BindUtils.postGlobalCommand(null, null, "globalReload", null);
				   Messagebox.show("Order notified successfully!","Success Information",Messagebox.OK,Messagebox.INFORMATION);
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
	}
	
	/**
	 * Method for order deliver
	 * @param kitchenBean
	 */
	@Command
	@NotifyChange("*")
	public void onClickDeliver(@BindingParam("bean")OrderDashBoardBean kitchenBean){
		
		Messagebox.show("Are you sure to Deliver?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
		          //write code here
		        	BindUtils.postGlobalCommand(null, null, "globalReload", null);
				   Messagebox.show("Order deliver to biker successfully!","Success Information",Messagebox.OK,Messagebox.INFORMATION);
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
	}
	
	public OrderDashBoardBean getKitchenOrderBean() {
		return kitchenOrderBean;
	}

	public void setKitchenOrderBean(OrderDashBoardBean kitchenOrderBean) {
		this.kitchenOrderBean = kitchenOrderBean;
	}

	public ArrayList<OrderDashBoardBean> getKitchenOrderBeanList() {
		return kitchenOrderBeanList;
	}

	public void setKitchenOrderBeanList(
			ArrayList<OrderDashBoardBean> kitchenOrderBeanList) {
		this.kitchenOrderBeanList = kitchenOrderBeanList;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
