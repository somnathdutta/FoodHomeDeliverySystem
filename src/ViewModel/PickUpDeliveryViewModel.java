package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

import dao.PickUpDeliveryDAO;
import Bean.OrderResponseBean;

public class PickUpDeliveryViewModel {
	
	Session session = null;
	
	private Connection connection = null;
	
	private String username = "";
	
	private Integer roleId = 0;

	private Boolean averageRadio = false;
	
	private Boolean highestRadio = false;
	
	private Boolean averageDeliveryRadio = false;
	
	private Boolean highestDeliveryRadio = false;
	
	public String kitchenName = null;
	
	private OrderResponseBean orderResponseBean = new OrderResponseBean();
	
	private ArrayList<OrderResponseBean> orderResponseBeanList = new ArrayList<OrderResponseBean>();
	
	private Date startDate;
	
	private Date endDate;
	
	public Boolean delayInResponseVisibility = false;
	
	public Boolean delayInDeliveryVisibility = false;
	
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
	public void onCheckFirstRadio() throws ParseException{
		//System.out.println("When first checked first-"+averageRadio+" second-"+highestRadio+" third-"+averageDeliveryRadio+" fourth-"+highestDeliveryRadio);
		
		if(highestRadio && averageDeliveryRadio && highestDeliveryRadio){
			averageRadio=true;highestRadio = false;averageDeliveryRadio=false;highestDeliveryRadio=false;
		}
		startDate = null;endDate=null;kitchenName= null;
		averageRadio=true;highestRadio = false;averageDeliveryRadio=false;highestDeliveryRadio=false;
		//System.out.println("When first checked first-"+averageRadio+" second-"+highestRadio+" third-"+averageDeliveryRadio+" fourth-"+highestDeliveryRadio);
		onLoadQuery(true, false , false, false, false);
		
	}
	
	@Command
	@NotifyChange("*")
	public void onCheckSecondRadio() throws ParseException{
		//System.out.println("When second checked first-"+averageRadio+" second-"+highestRadio+" third-"+averageDeliveryRadio+" fourth-"+highestDeliveryRadio);
		
		if(averageRadio && averageDeliveryRadio && highestDeliveryRadio ){
			highestRadio=true;averageRadio = false;averageDeliveryRadio=false;highestDeliveryRadio=false;
		}
		startDate = null;endDate=null;kitchenName= null;
		highestRadio=true;averageRadio = false;averageDeliveryRadio=false;highestDeliveryRadio=false;
		//System.out.println("When second checked first-"+averageRadio+" second-"+highestRadio+" third-"+averageDeliveryRadio+" fourth-"+highestDeliveryRadio);
		onLoadQuery(false, true , false, false, false);
		
	}
	
	@Command
	@NotifyChange("*")
	public void onCheckThirdRadio() throws ParseException{
	//	System.out.println("When third checked first-"+averageRadio+" second-"+highestRadio+" third-"+averageDeliveryRadio+" fourth-"+highestDeliveryRadio);
		
		if(averageRadio && highestRadio && highestDeliveryRadio){
			averageDeliveryRadio=true;highestRadio = false;averageRadio=false;highestDeliveryRadio=false;
		}
		startDate = null;endDate=null;kitchenName= null;
		averageDeliveryRadio=true;highestRadio = false;averageRadio=false;highestDeliveryRadio=false;
		//System.out.println("When third checked first-"+averageRadio+" second-"+highestRadio+" third-"+averageDeliveryRadio+" fourth-"+highestDeliveryRadio);
		onLoadQuery(false, false , true, false, false);
		
	}
	
	@Command
	@NotifyChange("*")
	public void onCheckFourthRadio() throws ParseException{
		//System.out.println("When fourth checked first-"+averageRadio+" second-"+highestRadio+" third-"+averageDeliveryRadio+" fourth-"+highestDeliveryRadio);
		
		if(averageRadio && highestRadio && averageDeliveryRadio){
			highestDeliveryRadio=true;averageRadio = false;highestRadio=false;averageDeliveryRadio=false;
		}
		startDate = null;endDate=null;kitchenName= null;
		highestDeliveryRadio=true;averageRadio = false;highestRadio=false;averageDeliveryRadio=false;
		//System.out.println("When fourth checked first-"+averageRadio+" second-"+highestRadio+" third-"+averageDeliveryRadio+" fourth-"+highestDeliveryRadio);
		onLoadQuery(false, false , false, true, false);
		
	}
	
	public void onLoadQuery(Boolean avg, Boolean high ,Boolean avgDelivery, Boolean highDelivery, Boolean dategiven){
		if(orderResponseBeanList.size()>0){
			orderResponseBeanList.clear();
		}
		System.out.println("Avg ->"+avg+" high->"+high+" avg delv->"+avgDelivery+" highdelv->"+highDelivery+" dategiven - >"+dategiven);
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql ="select * from vw_response_delay ";
					String sql1 ="select * from vw_response_delay where delay_in_pickup IS NOT NULL order by delay_in_pickup desc ";
					String sqlAvgWithDate = "select * from vw_response_delay where order_date>= ? and order_date<= ?";
					String sqlTopWithDate = "select * from vw_response_delay where order_date>= ? and order_date<= ?"
										+ "  and delay_in_pickup IS NOT NULL order by delay_in_pickup desc ";
					String sqlDelv = "select * from vw_response_delay  ";
					String sqlDelvWithDate = "select * from vw_response_delay  where order_date>= ? and order_date<= ?";
							
					String sqlTopDelv = "select * from vw_response_delay where delay_in_delivery IS NOT NULL order by delay_in_delivery DESC ";
					String sqlTopDelvWithDate = "select * from vw_response_delay where order_date>= ? and order_date<= ?"
							+ " and delay_in_delivery IS NOT NULL order by delay_in_delivery DESC";
					try {
						if(avg  &&  dategiven==false && high==false && avgDelivery==false &&  highDelivery==false){
							preparedStatement = connection.prepareStatement(sql);
							delayInResponseVisibility = true;
							delayInDeliveryVisibility = false;
							System.out.println("sql");
						}else if(avg && dategiven && high==false && avgDelivery==false &&  highDelivery==false ){
							delayInResponseVisibility = true;
							preparedStatement = connection.prepareStatement(sqlAvgWithDate);
							preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()) );
							preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()) );
							System.out.println("sqlAvgWithDate");
						}else if(high && dategiven  && avg==false && avgDelivery==false &&  highDelivery==false){
							delayInResponseVisibility = true;
							preparedStatement = connection.prepareStatement(sqlTopWithDate);
							preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()) );
							preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()) );
							System.out.println("sqlTopWithDate");
						}else if(high && dategiven==false && avg==false && avgDelivery==false &&  highDelivery==false){
							delayInResponseVisibility = true;
							preparedStatement = connection.prepareStatement(sql1);
							System.out.println("sql1");
						}else if(avgDelivery &&  dategiven==false && high==false && avg ==false &&  highDelivery==false){
							delayInResponseVisibility = false;
							delayInDeliveryVisibility = true;
							preparedStatement = connection.prepareStatement(sqlDelv);
							System.out.println("sqlDelv");
						}else if(avgDelivery &&  dategiven && high==false && avg ==false &&  highDelivery==false){
							delayInResponseVisibility = false;
							delayInDeliveryVisibility = true;
							preparedStatement = connection.prepareStatement(sqlDelvWithDate);
							preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()) );
							preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()) );
							System.out.println("sqlDelvWithDate");
						}else if(highDelivery &&  dategiven==false && high==false && avg ==false &&  avgDelivery==false){
							delayInResponseVisibility = false;
							delayInDeliveryVisibility = true;
							preparedStatement = connection.prepareStatement(sqlTopDelv);
							System.out.println("sqlTopDelv");
						}else if(highDelivery &&  dategiven && high==false && avg ==false &&  avgDelivery==false){
							delayInResponseVisibility = false;
							delayInDeliveryVisibility = true;
							preparedStatement = connection.prepareStatement(sqlTopDelvWithDate);
							preparedStatement.setDate(1, new java.sql.Date(startDate.getTime()) );
							preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()) );
							System.out.println("sqlTopDelvWithDate");
						}
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							OrderResponseBean orderResponseBean = new OrderResponseBean();
							orderResponseBean.orderNo = resultSet.getString("order_no");
							String orderDate="",reformattedOrderDate="";
							SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
							DateFormat dateFormat = new SimpleDateFormat(" HH:mm:ss");
							orderDate = resultSet.getString("order_date");
							java.util.Date stdate = null;
							try {
								 reformattedOrderDate = myFormat.format(fromUser.parse(orderDate));
								 stdate = myFormat.parse(reformattedOrderDate); 
							} catch (Exception e) {
								e.printStackTrace();
							}
							orderResponseBean.orderDateValue =  reformattedOrderDate;
							orderResponseBean.kitchenName = resultSet.getString("kitchen_name");
							orderResponseBean.notifyTime = resultSet.getTimestamp("notified_time");
							orderResponseBean.notifyTimeValue = dateFormat.format(orderResponseBean.notifyTime );
							/*orderResponseBean.orderResponseTime = resultSet.getTimestamp("driver_pickup_time");
							orderResponseBean.orderResponseTimeValue = dateFormat.format(orderResponseBean.orderResponseTime);*/
							if(resultSet.getTimestamp("driver_pickup_time")!=null){
								orderResponseBean.pickUpTime = resultSet.getTimestamp("driver_pickup_time");
								orderResponseBean.pickUpTimeValue = dateFormat.format(orderResponseBean.pickUpTime);
							}else{
								orderResponseBean.pickUpTimeValue = "NA";
							}
							
							if(resultSet.getTimestamp("delivery_time")!=null){
								orderResponseBean.deliveryTime = resultSet.getTimestamp("delivery_time");
								orderResponseBean.deliveryTimeValue = dateFormat.format(orderResponseBean.deliveryTime);
							}else{
								orderResponseBean.deliveryTimeValue = "NA";
							}
							if(resultSet.getString("delay_in_pickup")!=null){
								//orderResponseBean.delayInResponse = resultSet.getString("delay_in_pickup");
								//int minutes =toMins(orderResponseBean.delayInResponse);
								//orderResponseBean.delayInMinutes = minutes;
								orderResponseBean.delayInPickUp = resultSet.getString("delay_in_pickup");
							}else{
								//orderResponseBean.delayInMinutes = 0;
								orderResponseBean.delayInPickUp = "NA";
							}
							
							if(resultSet.getString("delay_in_delivery")!=null){
								orderResponseBean.delayInDelivery = resultSet.getString("delay_in_delivery");
								//int minutes =toMins(orderResponseBean.delayInDelivery);
								//orderResponseBean.delayInMinutesDelivery = minutes;
							}else{
								orderResponseBean.delayInDelivery = "NA";
							}
							orderResponseBeanList.add(orderResponseBean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("ERROR Due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
						if(resultSet!=null){
							resultSet.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private int toMins(String str) {
	    boolean space = false;int hour,mins,hoursInMins;
		if(str.contains(" ")){
			space = true;
		}
		if(space){
			String[] splitted = str.split(" ");
			String[] hourMin = splitted[2].split(":");
		    hour = Integer.parseInt(hourMin[0]);
		    mins = Integer.parseInt(hourMin[1]);
		    hoursInMins = hour * 60;
		}else{
			String[] hourMin = str.split(":");
		    hour = Integer.parseInt(hourMin[0]);
		    mins = Integer.parseInt(hourMin[1]);
		    hoursInMins = hour * 60;
		}
	    return hoursInMins + mins;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickFind(){
		if(averageRadio==false && highestRadio==false && averageDeliveryRadio==false && highestDeliveryRadio==false){
			Messagebox.show("Please choose one of the options","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}else if(startDate==null && endDate==null){
			Messagebox.show("Date range required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}else if(startDate==null && endDate!=null){
			Messagebox.show("Start date required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}else if(startDate!=null && endDate==null){
			Messagebox.show("End date required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}else{
			System.out.println("OK");
			boolean dateGiven = true;
			onLoadQuery(averageRadio, highestRadio, averageDeliveryRadio, highestDeliveryRadio, dateGiven);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		startDate = null;
		endDate = null;
		if(averageRadio){
			onLoadQuery(averageRadio, highestRadio, averageDeliveryRadio, highestDeliveryRadio, false);
		}else if(highestRadio){
			onLoadQuery(averageRadio, highestRadio, averageDeliveryRadio, highestDeliveryRadio, false);
		}else if(averageDeliveryRadio){
			onLoadQuery(averageRadio, highestRadio, averageDeliveryRadio, highestDeliveryRadio, false);
		}else{
			onLoadQuery(averageRadio, highestRadio, averageDeliveryRadio, highestDeliveryRadio, false);
		}
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickGenerateExcel(){
		if(averageRadio){
			PickUpDeliveryDAO.generateCSV(orderResponseBeanList, true, false);
		}else if(highestRadio){
			PickUpDeliveryDAO.generateCSV(orderResponseBeanList, true, false);
		}else if(averageDeliveryRadio){
			PickUpDeliveryDAO.generateCSV(orderResponseBeanList, false, true);
		}else{
			PickUpDeliveryDAO.generateCSV(orderResponseBeanList, false, true);
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

	public Boolean getAverageRadio() {
		return averageRadio;
	}

	public void setAverageRadio(Boolean averageRadio) {
		this.averageRadio = averageRadio;
	}

	public Boolean getHighestRadio() {
		return highestRadio;
	}

	public void setHighestRadio(Boolean highestRadio) {
		this.highestRadio = highestRadio;
	}

	public Boolean getAverageDeliveryRadio() {
		return averageDeliveryRadio;
	}

	public void setAverageDeliveryRadio(Boolean averageDeliveryRadio) {
		this.averageDeliveryRadio = averageDeliveryRadio;
	}

	public Boolean getHighestDeliveryRadio() {
		return highestDeliveryRadio;
	}

	public void setHighestDeliveryRadio(Boolean highestDeliveryRadio) {
		this.highestDeliveryRadio = highestDeliveryRadio;
	}

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public OrderResponseBean getOrderResponseBean() {
		return orderResponseBean;
	}

	public void setOrderResponseBean(OrderResponseBean orderResponseBean) {
		this.orderResponseBean = orderResponseBean;
	}

	public ArrayList<OrderResponseBean> getOrderResponseBeanList() {
		return orderResponseBeanList;
	}

	public void setOrderResponseBeanList(
			ArrayList<OrderResponseBean> orderResponseBeanList) {
		this.orderResponseBeanList = orderResponseBeanList;
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

	public Boolean getDelayInResponseVisibility() {
		return delayInResponseVisibility;
	}

	public void setDelayInResponseVisibility(Boolean delayInResponseVisibility) {
		this.delayInResponseVisibility = delayInResponseVisibility;
	}

	public Boolean getDelayInDeliveryVisibility() {
		return delayInDeliveryVisibility;
	}

	public void setDelayInDeliveryVisibility(Boolean delayInDeliveryVisibility) {
		this.delayInDeliveryVisibility = delayInDeliveryVisibility;
	}
	
	
}
