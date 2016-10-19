package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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
import org.zkoss.zul.Messagebox;

import Bean.OrderRejectionBean;

public class OrderRejectionMisViewModel {

	public OrderRejectionBean orderRejectionBean = new OrderRejectionBean();
	
	public ArrayList<OrderRejectionBean> orderRejectionBeanList = new ArrayList<OrderRejectionBean>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String username = "";
	
	private Integer roleId = 0;

	private Boolean totalRejectionDivVisibility = false;
	
	private Boolean firstRejectionDivVisibility = false;
	
	private Boolean totalRejectionRadio = false;
	
	private Boolean firstRejectionRadio = false;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		username = (String) session.getAttribute("login");
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		connection.setAutoCommit(true);
		
	}

	@Command
	@NotifyChange("*")
	public void onCheckFirstRadio(){
		if(firstRejectionRadio){
			firstRejectionRadio = false;
			
		}
		System.out.println("When first checked first-"+totalRejectionRadio+" second-"+firstRejectionRadio);
		if(totalRejectionRadio){
			totalRejectionDivVisibility = true;
			firstRejectionDivVisibility= false;
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onCheckSecondRadio(){
		if(totalRejectionRadio){
			totalRejectionRadio = false;
		}
		System.out.println("When second checked first-"+totalRejectionRadio+" second-"+firstRejectionRadio);
		if(firstRejectionRadio){
			firstRejectionDivVisibility= true;
			totalRejectionDivVisibility = false;
		}
	}

	public void loadRejection(boolean total , boolean firstrejection){
		if(orderRejectionBeanList.size()>0){
			orderRejectionBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT * from vw_order_rejection_mis";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							OrderRejectionBean orderRejectionbean = new OrderRejectionBean();
							orderRejectionbean.orderNo = resultSet.getString("order_no");
							String orderDate="",reformattedOrderDate="";
							SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");		
							orderDate = resultSet.getString("order_date");
							java.util.Date stdate = null;
							try {
								reformattedOrderDate = myFormat.format(fromUser.parse(orderDate));
								 stdate = myFormat.parse(reformattedOrderDate);	 
							} catch (Exception e) {
								e.printStackTrace();
							}	
							orderRejectionbean.orderDateValue =  reformattedOrderDate;
							
							
							orderRejectionBeanList.add(orderRejectionbean);
						}
					} catch (Exception e) {
					System.out.println(e);
						Messagebox.show("ERROR Due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Boolean getTotalRejectionDivVisibility() {
		return totalRejectionDivVisibility;
	}

	public void setTotalRejectionDivVisibility(Boolean totalRejectionDivVisibility) {
		this.totalRejectionDivVisibility = totalRejectionDivVisibility;
	}

	public Boolean getFirstRejectionDivVisibility() {
		return firstRejectionDivVisibility;
	}

	public void setFirstRejectionDivVisibility(Boolean firstRejectionDivVisibility) {
		this.firstRejectionDivVisibility = firstRejectionDivVisibility;
	}

	public Boolean getTotalRejectionRadio() {
		return totalRejectionRadio;
	}

	public void setTotalRejectionRadio(Boolean totalRejectionRadio) {
		this.totalRejectionRadio = totalRejectionRadio;
	}

	public Boolean getFirstRejectionRadio() {
		return firstRejectionRadio;
	}

	public void setFirstRejectionRadio(Boolean firstRejectionRadio) {
		this.firstRejectionRadio = firstRejectionRadio;
	}

	public OrderRejectionBean getOrderRejectionBean() {
		return orderRejectionBean;
	}

	public void setOrderRejectionBean(OrderRejectionBean orderRejectionBean) {
		this.orderRejectionBean = orderRejectionBean;
	}

	public ArrayList<OrderRejectionBean> getOrderRejectionBeanList() {
		return orderRejectionBeanList;
	}

	public void setOrderRejectionBeanList(
			ArrayList<OrderRejectionBean> orderRejectionBeanList) {
		this.orderRejectionBeanList = orderRejectionBeanList;
	}
}
