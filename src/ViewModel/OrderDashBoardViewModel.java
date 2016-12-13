package ViewModel;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import utility.DayFinder;
import dao.OrderDashBoardDAO;
import Bean.OrderDashBoardBean;

public class OrderDashBoardViewModel {

	private OrderDashBoardBean orderDashBoardBean = new OrderDashBoardBean();
	
	private ArrayList<OrderDashBoardBean> orderDashBoardBeanList = new ArrayList<OrderDashBoardBean>();
	
	public ArrayList<String> kitchenList =  new ArrayList<String>();
	
	public String kitchenName = null;
	
	public String orderno = null;
	
	Session session = null;
	
	private Connection connection = null;
	
	private Integer roleId = 0;
	
	private String userName, statusStyle = "font-weight:bold;";
	
	private Date startDate;
	
	private Date endDate;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		onLoadKitchenList();
		
        
		onLoadQuery();
	}

	@Command
	@NotifyChange("*")
	public void refresh(){
		orderno = null;
		startDate = null;
		endDate = null;
		onLoadQuery();
	}
	
	/**
	 * Search by order no
	 */
	@Command
	@NotifyChange("*")
	public void onClickFindByOrder(){
		orderDashBoardBeanList = OrderDashBoardDAO.findOrdersByOrderNo(orderno, connection);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClearByOrder(){
		orderno = null;
		
		onLoadQuery();
	}
	
	/**
	 * 
	 * Load all kitchen list
	 */
	public void onLoadKitchenList(){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sqlKitchen = "SELECT kitchen_name FROM fapp_kitchen WHERE is_active = 'Y' ";
										
					try {
						preparedStatement = connection.prepareStatement(sqlKitchen);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							kitchenList.add(resultSet.getString(1));
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
	
	public void onLoadQuery(){
		if(orderDashBoardBeanList.size()>0){
			orderDashBoardBeanList.clear();
		}
		if(connection!=null){
		try {
			SQL:{
				 PreparedStatement preparedStatement = null;
				 ResultSet resultSet = null;
			//	 String sql = "SELECT * FROM vw_order_dashboard where order_date between current_date-2 and current_date  ;";
				 String sql = "SELECT * FROM vw_order_dashboard ";
				 try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					String orderNo = null;double itemTotalPriceValue = 0.0,finalPrice = 0.0; 
					while (resultSet.next()) {
						OrderDashBoardBean dashBoardBean = new OrderDashBoardBean();
						dashBoardBean.orderNo = resultSet.getString("order_no");
						dashBoardBean.price = resultSet.getDouble("final_price");
						dashBoardBean.itemRate = resultSet.getDouble("rate");
						dashBoardBean.totalDiscount = resultSet.getDouble("total_discount");
						dashBoardBean.creditApplied = resultSet.getString("is_credit_applied");
						dashBoardBean.itemTotalPrice = resultSet.getDouble("total_price");
						dashBoardBean.deliveryCharges = resultSet.getDouble("delivery_charges");
						//System.out.println("Discount : "+resultSet.getDouble("discount_amount"));
						//dashBoardBean.discountAmount = dashBoardBean.itemTotalPrice - dashBoardBean.price;
						dashBoardBean.discountAmount = resultSet.getDouble("discount_amount");
						dashBoardBean.userType = resultSet.getString("user_type");
						String itemCode = resultSet.getString("item_code");
						if(itemCode!=null){
							dashBoardBean.itemCode = itemCode;
						}else{
							dashBoardBean.itemCode = "-NA-";
						}
						
						String tempOrderNo = dashBoardBean.orderNo;
						double tempItemPrice = dashBoardBean.itemTotalPrice;
						double tempFinalPrice = dashBoardBean.price;
						
						String orderDate="",reformattedOrderDate="",deliveryDate="",reformattedDeliveryOrderDate="";
						SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
						orderDate = resultSet.getString("order_date");
						deliveryDate = resultSet.getString("delivery_date");
						try {
							reformattedOrderDate = myFormat.format(fromDB.parse(orderDate));
							if(deliveryDate!=null)
							reformattedDeliveryOrderDate = myFormat.format(fromDB.parse(deliveryDate));
						} catch (Exception e) {
							e.printStackTrace();
						}
						dashBoardBean.orderDateValue = reformattedOrderDate;
						dashBoardBean.deliveryDateValue = reformattedDeliveryOrderDate;
						dashBoardBean.deliveryDayName = DayFinder.getDayName(dashBoardBean.deliveryDateValue);
						dashBoardBean.orderStatus= resultSet.getString("order_status");
						int orderStatusId = resultSet.getInt("order_status_id"); 
						if(orderStatusId == 10){
							dashBoardBean.statusStyle = "font-weight:bold; color:#ff3300; ";
							dashBoardBean.rowStyle = "background-color: #ff3300";
						}
						if(orderStatusId == 7){
							dashBoardBean.statusStyle = "font-weight:bold; color:#39e600; ";
							//dashBoardBean.rowStyle = "background-color: #39e600";
						}
						if(orderStatusId != 10 && orderStatusId != 7 ){
							dashBoardBean.statusStyle = "font-weight:bold; color:#ff9900; ";
							//dashBoardBean.rowStyle = "background-color: #ff9900";
						}
						dashBoardBean.orderBy = resultSet.getString("order_by");
						dashBoardBean.paymentName = resultSet.getString("payment_name");
						String deliveryZone = resultSet.getString("delivery_zone");
						if(deliveryZone!=null){
							if(deliveryZone.contains(","))
								deliveryZone = deliveryZone.replace(",", ";");
								deliveryZone = deliveryZone.replace("\n", "").replace("\r", "");
							
							dashBoardBean.deliveryZone = deliveryZone;
						}else{
							dashBoardBean.deliveryZone = "NA";
						}
						String deliveryAddress = resultSet.getString("delivery_address");
						if(deliveryAddress!=null){
							if(deliveryAddress.contains(","))
								deliveryAddress = deliveryAddress.replace(",", ";");
							deliveryAddress = deliveryAddress.replace("\n", "").replace("\r", "");
							dashBoardBean.deliveryAddress = deliveryAddress;
						}else{
							dashBoardBean.deliveryAddress = "NA";
						}
						String deliveryInstruction = resultSet.getString("instruction");
						if(deliveryInstruction!=null){
							if(deliveryInstruction.contains(","))
								deliveryInstruction = deliveryInstruction.replace(",", ";");
								deliveryInstruction = deliveryInstruction.replace("\n", "").replace("\r", "");
							dashBoardBean.deliveryInstruction = deliveryInstruction;
						}else{
							dashBoardBean.deliveryInstruction = "NA";
						}
						dashBoardBean.mealType = resultSet.getString("meal_type");
						String riceRoti = resultSet.getString("rice_roti");
						if(riceRoti!=null){
							dashBoardBean.riceRoti = riceRoti;
						}else{
							dashBoardBean.riceRoti = "No Data";
						}
						dashBoardBean.deliveryDate = resultSet.getDate("delivery_date");
						dashBoardBean.contactNo = resultSet.getString("contact_number");
						dashBoardBean.orderItem = resultSet.getString("category_name");
						dashBoardBean.setName = resultSet.getString("set_name");
						dashBoardBean.quantity = resultSet.getInt("qty");
						dashBoardBean.kitchenName = resultSet.getString("kitchen_name");
						dashBoardBean.received = resultSet.getString("received");
						dashBoardBean.notified = resultSet.getString("notify");
						dashBoardBean.picked = resultSet.getString("order_picked");
						dashBoardBean.rejected = resultSet.getString("rejected");
						dashBoardBean.timeSlot = resultSet.getString("time_slot");
						String itemDesc = resultSet.getString("item_description");
						dashBoardBean.orderCreationTime= resultSet.getString("assigned_time");
						
						if(itemDesc!=null){
							if(itemDesc.contains(","))
								itemDesc = itemDesc.replace(",", ";");
							dashBoardBean.itemDescription = itemDesc;
						}else{
							dashBoardBean.itemDescription = "NA";
						}
						if(resultSet.getString("delivered")!=null)
							dashBoardBean.delivered = resultSet.getString("delivered");
						else
							dashBoardBean.delivered = "N";
						
						
						if(resultSet.getString("driver_name")!=null){
							dashBoardBean.driverName = resultSet.getString("driver_name");
						}else{
							dashBoardBean.driverName = "No Driver assigned!";
						}
						if(resultSet.getString("driver_number")!=null){
							dashBoardBean.driverNumber = resultSet.getString("driver_number");
						}else{
							dashBoardBean.driverNumber = "NA";
						}
						
						if(resultSet.getString("referred_by") != null){
							dashBoardBean.referredBy = resultSet.getString("referred_by");
						}else {
							dashBoardBean.referredBy = "NA";
						}
						
						if(resultSet.getString("vallet_amount") != null){
							dashBoardBean.walletAmt = resultSet.getString("vallet_amount");
						}else {
							dashBoardBean.walletAmt = "NA";
						}
						
						if(resultSet.getString("menu") != null){
							dashBoardBean.menu = resultSet.getString("menu");
						}else {
							dashBoardBean.menu = "NA";
						}
						
						if(resultSet.getString("taste") != null){
							dashBoardBean.taste = resultSet.getString("taste");
						}else {
							dashBoardBean.taste = "NA";
						}
						
						if(resultSet.getString("portion") != null){
							dashBoardBean.portion = resultSet.getString("portion");
						}else {
							dashBoardBean.portion = "NA";
						}
						
						if(resultSet.getString("packing") != null){
							dashBoardBean.packing = resultSet.getString("packing");
						}else {
							dashBoardBean.packing = "NA";
						}
						
						if(resultSet.getString("timely_delivered") != null){
							dashBoardBean.timelyDeliverd = resultSet.getString("timely_delivered");
						}else {
							dashBoardBean.timelyDeliverd = "NA";
						}
						
						if(resultSet.getString("comment") != null){
							dashBoardBean.comment = resultSet.getString("comment");
						}else {
							dashBoardBean.comment = "NA";
						}
						
						
						
						if(dashBoardBean.orderNo.equals(orderNo)){
							dashBoardBean.orderNoVisibiliity= false;
							dashBoardBean.orderDateVisibility = false;
							dashBoardBean.deliveryDateVisibility = false;
							dashBoardBean.orderByVisibility = false;
							dashBoardBean.contactNoVisibility = false;
							dashBoardBean.mealTypeVisibility = false;
							dashBoardBean.orderCreationTimeVis = false;
							dashBoardBean.timeSlotVis = false;
							dashBoardBean.price = 0.0;
							/*dashBoardBean.orderNo = "";
							dashBoardBean.orderDateValue = "";
							dashBoardBean.orderBy = "";
							dashBoardBean.orderStatus = "";
							dashBoardBean.deliveryZone  = "";
							dashBoardBean.mealType = "";
							dashBoardBean.deliveryDateValue = "";
							dashBoardBean.contactNo = "";
							dashBoardBean.paymentName ="";
							dashBoardBean.deliveryDayName = "";*/
							//System.out.println("Total price: ===> "+(itemTotalPriceValue+dashBoardBean.itemTotalPrice));
							//System.out.println("Final price : == >"+finalPrice);
							//System.out.println("Discount == > "+((itemTotalPriceValue+dashBoardBean.itemTotalPrice) - dashBoardBean.price ));
							//dashBoardBean.discountAmount = ((itemTotalPriceValue+dashBoardBean.itemTotalPrice) - dashBoardBean.price );
						}	
						orderNo = tempOrderNo ;
						itemTotalPriceValue = tempItemPrice;
						finalPrice = tempFinalPrice;
						orderDashBoardBeanList.add(dashBoardBean);
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
		}else{
			
		}
	}
	
	
	@Command
	@NotifyChange("*")
	public void onSelectKitchen(){
		if(orderDashBoardBeanList.size()>0){
			orderDashBoardBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql;
						sql = "SELECT * FROM vw_order_dashboard WHERE kitchen_name = ?";
					
					
					try {
						if(kitchenName!=null){
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setString(1, kitchenName);
						}
						
						resultSet = preparedStatement.executeQuery();
						String orderNo=null;double itemTotalPriceValue = 0.0,finalPrice = 0.0; 
						while (resultSet.next()) {
							OrderDashBoardBean dashBoardBean = new OrderDashBoardBean();
							dashBoardBean.orderNo = resultSet.getString("order_no");
							dashBoardBean.price = resultSet.getDouble("final_price");
							dashBoardBean.itemRate = resultSet.getDouble("rate");
							dashBoardBean.totalDiscount = resultSet.getDouble("total_discount");
							dashBoardBean.creditApplied = resultSet.getString("is_credit_applied");
							dashBoardBean.itemTotalPrice = resultSet.getDouble("total_price");
							dashBoardBean.deliveryCharges = resultSet.getDouble("delivery_charges");
							dashBoardBean.discountAmount = resultSet.getDouble("discount_amount");
							//dashBoardBean.discountAmount = dashBoardBean.itemTotalPrice - dashBoardBean.price;
							dashBoardBean.userType = resultSet.getString("user_type");
							String itemCode = resultSet.getString("item_code");
							if(itemCode!=null){
								dashBoardBean.itemCode = itemCode;
							}else{
								dashBoardBean.itemCode = "-NA-";
							}
							String tempOrderNo = dashBoardBean.orderNo;
							double tempItemPrice = dashBoardBean.itemTotalPrice;
							double tempFinalPrice = dashBoardBean.price;
							String orderDate="",reformattedOrderDate="",deliveryDate="",reformattedDeliveryOrderDate="";
							SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
							orderDate = resultSet.getString("order_date");
							deliveryDate = resultSet.getString("delivery_date");
							try {
								reformattedOrderDate = myFormat.format(fromDB.parse(orderDate));
								reformattedDeliveryOrderDate = myFormat.format(fromDB.parse(deliveryDate));
							} catch (Exception e) {
								e.printStackTrace();
							}
							dashBoardBean.orderDateValue = reformattedOrderDate;
							dashBoardBean.deliveryDateValue = reformattedDeliveryOrderDate;
							dashBoardBean.deliveryDayName = DayFinder.getDayName(dashBoardBean.deliveryDateValue);
							dashBoardBean.orderStatus= resultSet.getString("order_status");
							int orderStatusId = resultSet.getInt("order_status_id"); 
							if(orderStatusId == 10){
								dashBoardBean.statusStyle = "font-weight:bold; color:#ff3300; ";
								dashBoardBean.rowStyle = "background-color: #ff3300";
							}
							if(orderStatusId == 7){
								dashBoardBean.statusStyle = "font-weight:bold; color:#39e600; ";
							}
							if(orderStatusId != 10 && orderStatusId != 7 ){
								dashBoardBean.statusStyle = "font-weight:bold; color:#ff9900; ";
							}
							dashBoardBean.orderBy = resultSet.getString("order_by");
							dashBoardBean.paymentName = resultSet.getString("payment_name");
							String deliveryZone = resultSet.getString("delivery_zone");
							if(deliveryZone!=null){
								if(deliveryZone.contains(","))
									deliveryZone = deliveryZone.replace(",", ";");
									deliveryZone = deliveryZone.replace("\n", "").replace("\r", "");
								dashBoardBean.deliveryZone = deliveryZone;
							}else{
								dashBoardBean.deliveryZone = "NA";
							}
							String deliveryAddress = resultSet.getString("delivery_address");
							if(deliveryAddress!=null){
								if(deliveryAddress.contains(","))
									deliveryAddress = deliveryAddress.replace(",", ";");
									deliveryAddress = deliveryAddress.replace("\n", "").replace("\r", "");
								dashBoardBean.deliveryAddress = deliveryAddress;
							}else{
								dashBoardBean.deliveryAddress = "NA";
							}
							String deliveryInstruction = resultSet.getString("instruction");
							if(deliveryInstruction!=null){
								if(deliveryInstruction.contains(","))
									deliveryInstruction = deliveryInstruction.replace(",", ";");
									deliveryInstruction = deliveryInstruction.replace("\n", "").replace("\r", "");
								dashBoardBean.deliveryInstruction = deliveryInstruction;
							}else{
								dashBoardBean.deliveryInstruction = "NA";
							}
							dashBoardBean.mealType = resultSet.getString("meal_type");
							String riceRoti = resultSet.getString("rice_roti");
							if(riceRoti!=null){
								dashBoardBean.riceRoti = riceRoti;
							}else{
								dashBoardBean.riceRoti = "No Data";
							}
							dashBoardBean.deliveryDate = resultSet.getDate("delivery_date");
							dashBoardBean.contactNo = resultSet.getString("contact_number");
							dashBoardBean.orderItem = resultSet.getString("category_name");
							dashBoardBean.setName = resultSet.getString("set_name");
							dashBoardBean.quantity = resultSet.getInt("qty");
							dashBoardBean.kitchenName = resultSet.getString("kitchen_name");
							dashBoardBean.received = resultSet.getString("received");
							dashBoardBean.notified = resultSet.getString("notify");
							dashBoardBean.rejected = resultSet.getString("rejected");
							String itemDesc = resultSet.getString("item_description");
							dashBoardBean.timeSlot = resultSet.getString("time_slot");
							dashBoardBean.orderCreationTime= resultSet.getString("assigned_time");
						//	System.out.println("DAAAAA " + dashBoardBean.orderCreationTime);
							if(itemDesc!=null){
								if(itemDesc.contains(","))
									itemDesc = itemDesc.replace(",", ";");
								dashBoardBean.itemDescription = itemDesc;
							}else{
								dashBoardBean.itemDescription = "NA";
							}
							dashBoardBean.rejected = resultSet.getString("rejected");
							if(resultSet.getString("delivered")!=null)
								dashBoardBean.delivered = resultSet.getString("delivered");
							else
								dashBoardBean.delivered = "N";
							
							dashBoardBean.picked = resultSet.getString("order_picked");
							
							if(resultSet.getString("driver_name")!=null){
								dashBoardBean.driverName = resultSet.getString("driver_name");
							}else{
								dashBoardBean.driverName = "No Driver assigned!";
							}
							if(resultSet.getString("driver_number")!=null){
								dashBoardBean.driverNumber = resultSet.getString("driver_number");
							}else{
								dashBoardBean.driverNumber = "NA";
							}
							
							if(resultSet.getString("referred_by") != null){
								dashBoardBean.referredBy = resultSet.getString("referred_by");
							}else {
								dashBoardBean.referredBy = "NA";
							}
							
							if(resultSet.getString("vallet_amount") != null){
								dashBoardBean.walletAmt = resultSet.getString("vallet_amount");
							}else {
								dashBoardBean.walletAmt = "NA";
							}
							
							if(resultSet.getString("menu") != null){
								dashBoardBean.menu = resultSet.getString("menu");
							}else {
								dashBoardBean.menu = "NA";
							}
							
							if(resultSet.getString("taste") != null){
								dashBoardBean.taste = resultSet.getString("taste");
							}else {
								dashBoardBean.taste = "NA";
							}
							
							if(resultSet.getString("portion") != null){
								dashBoardBean.portion = resultSet.getString("portion");
							}else {
								dashBoardBean.portion = "NA";
							}
							
							if(resultSet.getString("packing") != null){
								dashBoardBean.packing = resultSet.getString("packing");
							}else {
								dashBoardBean.packing = "NA";
							}
							
							if(resultSet.getString("timely_delivered") != null){
								dashBoardBean.timelyDeliverd = resultSet.getString("timely_delivered");
							}else {
								dashBoardBean.timelyDeliverd = "NA";
							}
							
							if(resultSet.getString("comment") != null){
								dashBoardBean.comment = resultSet.getString("comment");
							}else {
								dashBoardBean.comment = "NA";
							}
							
							if(dashBoardBean.orderNo.equals(orderNo)){
								dashBoardBean.orderNoVisibiliity= false;
								dashBoardBean.orderDateVisibility = false;
								dashBoardBean.deliveryDateVisibility = false;
								dashBoardBean.orderByVisibility = false;
								dashBoardBean.contactNoVisibility = false;
								dashBoardBean.mealTypeVisibility = false;
								dashBoardBean.orderCreationTimeVis = false;
								dashBoardBean.timeSlotVis = false;
								dashBoardBean.price = 0.0;
								/*dashBoardBean.orderNo = "";
								dashBoardBean.orderDateValue = "";
								dashBoardBean.orderBy = "";
								dashBoardBean.orderStatus = "";
								dashBoardBean.deliveryZone  = "";
								dashBoardBean.mealType = "";
								dashBoardBean.deliveryDateValue = "";
								dashBoardBean.contactNo = "";
								dashBoardBean.paymentName ="";
								dashBoardBean.deliveryDayName = "";*/
								//dashBoardBean.discountAmount = ((itemTotalPriceValue+dashBoardBean.itemTotalPrice) - dashBoardBean.price );
							}	
							orderNo = tempOrderNo ;
							itemTotalPriceValue = tempItemPrice;
							finalPrice = tempFinalPrice;
							orderDashBoardBeanList.add(dashBoardBean);
						}
					}  catch (Exception e) {
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
		
		if(startDate!=null && endDate!=null ){
		if(orderDashBoardBeanList.size()>0){
			orderDashBoardBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql;
					if(kitchenName!=null){
						sql = "SELECT * FROM vw_order_dashboard WHERE order_date>= ? AND order_date <= ? and kitchen_name = ?";
					}else{
						sql = "SELECT * FROM vw_order_dashboard WHERE order_date>= ? AND order_date <= ? ";
					}
					
					try {
						if(kitchenName!=null){
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setDate(1, new java.sql.Date( startDate.getTime()));
							preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
							preparedStatement.setString(3, kitchenName);
						}else{
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setDate(1, new java.sql.Date( startDate.getTime()));
							preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
						}
						
						resultSet = preparedStatement.executeQuery();
						String orderNo=null;double itemTotalPriceValue = 0.0,finalPrice = 0.0; 
						while (resultSet.next()) {
							OrderDashBoardBean dashBoardBean = new OrderDashBoardBean();
							dashBoardBean.orderNo = resultSet.getString("order_no");
							dashBoardBean.price = resultSet.getDouble("final_price");
							dashBoardBean.itemRate = resultSet.getDouble("rate");
							dashBoardBean.totalDiscount = resultSet.getDouble("total_discount");
							dashBoardBean.creditApplied = resultSet.getString("is_credit_applied");
							dashBoardBean.itemTotalPrice = resultSet.getDouble("total_price");
							dashBoardBean.deliveryCharges = resultSet.getDouble("delivery_charges");
							dashBoardBean.discountAmount = resultSet.getDouble("discount_amount");
							//dashBoardBean.discountAmount = dashBoardBean.itemTotalPrice - dashBoardBean.price;
							dashBoardBean.userType = resultSet.getString("user_type");
							String itemCode = resultSet.getString("item_code");
							if(itemCode!=null){
								dashBoardBean.itemCode = itemCode;
							}else{
								dashBoardBean.itemCode = "-NA-";
							}
							String tempOrderNo = dashBoardBean.orderNo;
							double tempItemPrice = dashBoardBean.itemTotalPrice;
							double tempFinalPrice = dashBoardBean.price;
							
							String orderDate="",reformattedOrderDate="",deliveryDate="",reformattedDeliveryOrderDate="";
							SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
							orderDate = resultSet.getString("order_date");
							deliveryDate = resultSet.getString("delivery_date");
							try {
								reformattedOrderDate = myFormat.format(fromDB.parse(orderDate));
								reformattedDeliveryOrderDate = myFormat.format(fromDB.parse(deliveryDate));
							} catch (Exception e) {
								e.printStackTrace();
							}
							dashBoardBean.orderDateValue = reformattedOrderDate;
							dashBoardBean.deliveryDateValue = reformattedDeliveryOrderDate;
							dashBoardBean.deliveryDayName = DayFinder.getDayName(dashBoardBean.deliveryDateValue);
							dashBoardBean.orderStatus= resultSet.getString("order_status");
							int orderStatusId = resultSet.getInt("order_status_id"); 
							if(orderStatusId == 10){
								dashBoardBean.statusStyle = "font-weight:bold; color:#ff3300; ";
								dashBoardBean.rowStyle = "background-color: #ff3300";
							}
							if(orderStatusId == 7){
								dashBoardBean.statusStyle = "font-weight:bold; color:#39e600; ";
							}
							if(orderStatusId != 10 && orderStatusId != 7 ){
								dashBoardBean.statusStyle = "font-weight:bold; color:#ff9900; ";
							}
							dashBoardBean.orderBy = resultSet.getString("order_by");
							dashBoardBean.paymentName = resultSet.getString("payment_name");
							String deliveryZone = resultSet.getString("delivery_zone");
							if(deliveryZone!=null){
								if(deliveryZone.contains(","))
									deliveryZone = deliveryZone.replace(",", ";");
									deliveryZone = deliveryZone.replace("\n", "").replace("\r", "");
								dashBoardBean.deliveryZone = deliveryZone;
							}else{
								dashBoardBean.deliveryZone = "NA";
							}
							String deliveryAddress = resultSet.getString("delivery_address");
							if(deliveryAddress!=null){
								if(deliveryAddress.contains(","))
									deliveryAddress = deliveryAddress.replace(",", ";");
									deliveryAddress = deliveryAddress.replace("\n", "").replace("\r", "");
								dashBoardBean.deliveryAddress = deliveryAddress;
							}else{
								dashBoardBean.deliveryAddress = "NA";
							}
							String deliveryInstruction = resultSet.getString("instruction");
							if(deliveryInstruction!=null){
								if(deliveryInstruction.contains(","))
									deliveryInstruction = deliveryInstruction.replace(",", ";");
									deliveryInstruction = deliveryInstruction.replace("\n", "").replace("\r", "");
								dashBoardBean.deliveryInstruction = deliveryInstruction;
							}else{
								dashBoardBean.deliveryInstruction = "NA";
							}
							dashBoardBean.mealType = resultSet.getString("meal_type");
							String riceRoti = resultSet.getString("rice_roti");
							if(riceRoti!=null){
								dashBoardBean.riceRoti = riceRoti;
							}else{
								dashBoardBean.riceRoti = "No Data";
							}
							dashBoardBean.deliveryDate = resultSet.getDate("delivery_date");
							dashBoardBean.contactNo = resultSet.getString("contact_number");
							dashBoardBean.orderItem = resultSet.getString("category_name");
							dashBoardBean.setName = resultSet.getString("set_name");
							dashBoardBean.quantity = resultSet.getInt("qty");
							dashBoardBean.kitchenName = resultSet.getString("kitchen_name");
							dashBoardBean.received = resultSet.getString("received");
							dashBoardBean.notified = resultSet.getString("notify");
							dashBoardBean.rejected = resultSet.getString("rejected");
							String itemDesc = resultSet.getString("item_description");
							dashBoardBean.timeSlot = resultSet.getString("time_slot");
							dashBoardBean.orderCreationTime= resultSet.getString("assigned_time");
							
							if(itemDesc!=null){
								if(itemDesc.contains(","))
									itemDesc = itemDesc.replace(",", ";");
								dashBoardBean.itemDescription = itemDesc;
							}else{
								dashBoardBean.itemDescription = "NA";
							}
							dashBoardBean.rejected = resultSet.getString("rejected");
							if(resultSet.getString("delivered")!=null)
								dashBoardBean.delivered = resultSet.getString("delivered");
							else
								dashBoardBean.delivered = "N";
							
							dashBoardBean.picked = resultSet.getString("order_picked");
							
							if(resultSet.getString("driver_name")!=null){
								dashBoardBean.driverName = resultSet.getString("driver_name");
							}else{
								dashBoardBean.driverName = "No Driver assigned!";
							}
							if(resultSet.getString("driver_number")!=null){
								dashBoardBean.driverNumber = resultSet.getString("driver_number");
							}else{
								dashBoardBean.driverNumber = "NA";
							}
							
							if(resultSet.getString("referred_by") != null){
								dashBoardBean.referredBy = resultSet.getString("referred_by");
							}else {
								dashBoardBean.referredBy = "NA";
							}
							
							if(resultSet.getString("vallet_amount") != null){
								dashBoardBean.walletAmt = resultSet.getString("vallet_amount");
							}else {
								dashBoardBean.walletAmt = "NA";
							}
							
							if(resultSet.getString("menu") != null){
								dashBoardBean.menu = resultSet.getString("menu");
							}else {
								dashBoardBean.menu = "NA";
							}
							
							if(resultSet.getString("taste") != null){
								dashBoardBean.taste = resultSet.getString("taste");
							}else {
								dashBoardBean.taste = "NA";
							}
							
							if(resultSet.getString("portion") != null){
								dashBoardBean.portion = resultSet.getString("portion");
							}else {
								dashBoardBean.portion = "NA";
							}
							
							if(resultSet.getString("packing") != null){
								dashBoardBean.packing = resultSet.getString("packing");
							}else {
								dashBoardBean.packing = "NA";
							}
							
							if(resultSet.getString("timely_delivered") != null){
								dashBoardBean.timelyDeliverd = resultSet.getString("timely_delivered");
							}else {
								dashBoardBean.timelyDeliverd = "NA";
							}
							
							if(resultSet.getString("comment") != null){
								dashBoardBean.comment = resultSet.getString("comment");
							}else {
								dashBoardBean.comment = "NA";
							}
							
							if(dashBoardBean.orderNo.equals(orderNo)){
								dashBoardBean.orderNoVisibiliity= false;
								dashBoardBean.orderDateVisibility = false;
								dashBoardBean.deliveryDateVisibility = false;
								dashBoardBean.orderByVisibility = false;
								dashBoardBean.contactNoVisibility = false;
								dashBoardBean.mealTypeVisibility = false;
								dashBoardBean.orderCreationTimeVis = false;
								dashBoardBean.timeSlotVis = false;
								dashBoardBean.price = 0.0;
								dashBoardBean.orderNo = "";
								/*dashBoardBean.orderDateValue = "";
								dashBoardBean.orderBy = "";
								dashBoardBean.orderStatus = "";
								dashBoardBean.deliveryZone  = "";
								dashBoardBean.mealType = "";
								dashBoardBean.deliveryDateValue = "";
								dashBoardBean.contactNo = "";
								dashBoardBean.paymentName ="";
								dashBoardBean.deliveryDayName = "";*/
							//	dashBoardBean.discountAmount = ((itemTotalPriceValue+dashBoardBean.itemTotalPrice) - dashBoardBean.price );
							}	
							orderNo = tempOrderNo ;
							itemTotalPriceValue = tempItemPrice;
							finalPrice = tempFinalPrice;
							orderDashBoardBeanList.add(dashBoardBean);
						}
					}  catch (Exception e) {
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
		}else if(kitchenName!=null){
			if(orderDashBoardBeanList.size()>0){
				orderDashBoardBeanList.clear();
			}
			try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						String sql;
							sql = "SELECT * FROM vw_order_dashboard WHERE kitchen_name = ?";
						
						
						try {
							if(kitchenName!=null){
								preparedStatement = connection.prepareStatement(sql);
								preparedStatement.setString(1, kitchenName);
							}
							
							resultSet = preparedStatement.executeQuery();
							String orderNo=null;double itemTotalPriceValue = 0.0,finalPrice = 0.0; 
							while (resultSet.next()) {
								OrderDashBoardBean dashBoardBean = new OrderDashBoardBean();
								dashBoardBean.orderNo = resultSet.getString("order_no");
								dashBoardBean.price = resultSet.getDouble("final_price");
								dashBoardBean.itemRate = resultSet.getDouble("rate");
								dashBoardBean.totalDiscount = resultSet.getDouble("total_discount");
								dashBoardBean.creditApplied = resultSet.getString("is_credit_applied");
								dashBoardBean.itemTotalPrice = resultSet.getDouble("total_price");
								dashBoardBean.deliveryCharges = resultSet.getDouble("delivery_charges");
								dashBoardBean.discountAmount = resultSet.getDouble("discount_amount");
								//dashBoardBean.discountAmount = dashBoardBean.itemTotalPrice - dashBoardBean.price;
								dashBoardBean.userType = resultSet.getString("user_type");
								String itemCode = resultSet.getString("item_code");
								if(itemCode!=null){
									dashBoardBean.itemCode = itemCode;
								}else{
									dashBoardBean.itemCode = "-NA-";
								}
								String tempOrderNo = dashBoardBean.orderNo;
								double tempItemPrice = dashBoardBean.itemTotalPrice;
								double tempFinalPrice = dashBoardBean.price;
								
								String orderDate="",reformattedOrderDate="",deliveryDate="",reformattedDeliveryOrderDate="";
								SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd");
								SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
								orderDate = resultSet.getString("order_date");
								deliveryDate = resultSet.getString("delivery_date");
								try {
									reformattedOrderDate = myFormat.format(fromDB.parse(orderDate));
									reformattedDeliveryOrderDate = myFormat.format(fromDB.parse(deliveryDate));
								} catch (Exception e) {
									e.printStackTrace();
								}
								dashBoardBean.orderDateValue = reformattedOrderDate;
								dashBoardBean.deliveryDateValue = reformattedDeliveryOrderDate;
								dashBoardBean.deliveryDayName = DayFinder.getDayName(dashBoardBean.deliveryDateValue);
								dashBoardBean.orderStatus= resultSet.getString("order_status");
								int orderStatusId = resultSet.getInt("order_status_id"); 
								if(orderStatusId == 10){
									dashBoardBean.statusStyle = "font-weight:bold; color:#ff3300; ";
									dashBoardBean.rowStyle = "background-color: #ff3300";
								}
								if(orderStatusId == 7){
									dashBoardBean.statusStyle = "font-weight:bold; color:#39e600; ";
								}
								if(orderStatusId != 10 && orderStatusId != 7 ){
									dashBoardBean.statusStyle = "font-weight:bold; color:#ff9900; ";
								}
								dashBoardBean.orderBy = resultSet.getString("order_by");
								dashBoardBean.paymentName = resultSet.getString("payment_name");
								String deliveryZone = resultSet.getString("delivery_zone");
								if(deliveryZone!=null){
									if(deliveryZone.contains(","))
										deliveryZone = deliveryZone.replace(",", ";");
										deliveryZone = deliveryZone.replace("\n", "").replace("\r", "");
									dashBoardBean.deliveryZone = deliveryZone;
								}else{
									dashBoardBean.deliveryZone = "NA";
								}
								String deliveryAddress = resultSet.getString("delivery_address");
								if(deliveryAddress!=null){
									if(deliveryAddress.contains(","))
										deliveryAddress = deliveryAddress.replace(",", ";");
										deliveryAddress = deliveryAddress.replace("\n", "").replace("\r", "");
									dashBoardBean.deliveryAddress = deliveryAddress;
								}else{
									dashBoardBean.deliveryAddress = "NA";
								}
								String deliveryInstruction = resultSet.getString("instruction");
								if(deliveryInstruction!=null){
									if(deliveryInstruction.contains(","))
										deliveryInstruction = deliveryInstruction.replace(",", ";");
										deliveryInstruction = deliveryInstruction.replace("\n", "").replace("\r", "");
									dashBoardBean.deliveryInstruction = deliveryInstruction;
								}else{
									dashBoardBean.deliveryInstruction = "NA";
								}
								dashBoardBean.mealType = resultSet.getString("meal_type");
								String riceRoti = resultSet.getString("rice_roti");
								if(riceRoti!=null){
									dashBoardBean.riceRoti = riceRoti;
								}else{
									dashBoardBean.riceRoti = "No Data";
								}
								dashBoardBean.deliveryDate = resultSet.getDate("delivery_date");
								dashBoardBean.contactNo = resultSet.getString("contact_number");
								dashBoardBean.orderItem = resultSet.getString("category_name");
								dashBoardBean.setName = resultSet.getString("set_name");
								dashBoardBean.quantity = resultSet.getInt("qty");
								dashBoardBean.kitchenName = resultSet.getString("kitchen_name");
								dashBoardBean.received = resultSet.getString("received");
								dashBoardBean.notified = resultSet.getString("notify");
								dashBoardBean.rejected = resultSet.getString("rejected");
								String itemDesc = resultSet.getString("item_description");
								dashBoardBean.orderCreationTime= resultSet.getString("assigned_time");
								
								if(itemDesc!=null){
									if(itemDesc.contains(","))
										itemDesc = itemDesc.replace(",", ";");
									dashBoardBean.itemDescription = itemDesc;
								}else{
									dashBoardBean.itemDescription = "NA";
								}
								dashBoardBean.rejected = resultSet.getString("rejected");
								if(resultSet.getString("delivered")!=null)
									dashBoardBean.delivered = resultSet.getString("delivered");
								else
									dashBoardBean.delivered = "N";
								
								dashBoardBean.picked = resultSet.getString("order_picked");
								
								if(resultSet.getString("driver_name")!=null){
									dashBoardBean.driverName = resultSet.getString("driver_name");
								}else{
									dashBoardBean.driverName = "No Driver assigned!";
								}
								if(resultSet.getString("driver_number")!=null){
									dashBoardBean.driverNumber = resultSet.getString("driver_number");
								}else{
									dashBoardBean.driverNumber = "NA";
								}
								
								if(resultSet.getString("referred_by") != null){
									dashBoardBean.referredBy = resultSet.getString("referred_by");
								}else {
									dashBoardBean.referredBy = "NA";
								}
								
								if(resultSet.getString("vallet_amount") != null){
									dashBoardBean.walletAmt = resultSet.getString("vallet_amount");
								}else {
									dashBoardBean.walletAmt = "NA";
								}
								
								if(resultSet.getString("menu") != null){
									dashBoardBean.menu = resultSet.getString("menu");
								}else {
									dashBoardBean.menu = "NA";
								}
								
								if(resultSet.getString("taste") != null){
									dashBoardBean.taste = resultSet.getString("taste");
								}else {
									dashBoardBean.taste = "NA";
								}
								
								if(resultSet.getString("portion") != null){
									dashBoardBean.portion = resultSet.getString("portion");
								}else {
									dashBoardBean.portion = "NA";
								}
								
								if(resultSet.getString("packing") != null){
									dashBoardBean.packing = resultSet.getString("packing");
								}else {
									dashBoardBean.packing = "NA";
								}
								
								if(resultSet.getString("timely_delivered") != null){
									dashBoardBean.timelyDeliverd = resultSet.getString("timely_delivered");
								}else {
									dashBoardBean.timelyDeliverd = "NA";
								}
								
								if(resultSet.getString("comment") != null){
									dashBoardBean.comment = resultSet.getString("comment");
								}else {
									dashBoardBean.comment = "NA";
								}
								
								if(dashBoardBean.orderNo.equals(orderNo)){
									dashBoardBean.orderNoVisibiliity= false;
									dashBoardBean.orderDateVisibility = false;
									dashBoardBean.deliveryDateVisibility = false;
									dashBoardBean.orderByVisibility = false;
									dashBoardBean.contactNoVisibility = false;
									dashBoardBean.mealTypeVisibility = false;
									dashBoardBean.orderCreationTimeVis = false;
									dashBoardBean.timeSlotVis = false;
									dashBoardBean.price = 0.0;
									/*dashBoardBean.orderNo = "";
									dashBoardBean.orderDateValue = "";
									dashBoardBean.orderBy = "";
									dashBoardBean.orderStatus = "";
									dashBoardBean.deliveryZone  = "";
									dashBoardBean.mealType = "";
									dashBoardBean.deliveryDateValue = "";
									dashBoardBean.contactNo = "";
									dashBoardBean.paymentName ="";
									dashBoardBean.deliveryDayName = "";*/
									//dashBoardBean.discountAmount = ((itemTotalPriceValue+dashBoardBean.itemTotalPrice) - dashBoardBean.price );
								}	
								orderNo = tempOrderNo ;
								itemTotalPriceValue = tempItemPrice;
								finalPrice = tempFinalPrice;
								orderDashBoardBeanList.add(dashBoardBean);
							}
						}  catch (Exception e) {
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
		}else{
			Messagebox.show("Search value required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickGenerate(){
		 File f = null;
	      boolean bool = false;
	    if(orderDashBoardBeanList.size()>0){
	    	try{
		         // create new file
		    	 String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		  		
		  		String reportNamewithPath = realPath + "orderDashBoardReport.csv";
		  		System.out.println(reportNamewithPath);
		         //f = new File("C:/Users/somnathd/Desktop/report.csv");
		  		f = new File(reportNamewithPath);
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // prints
		         System.out.println("File created: "+bool);
		         if(f.exists())
		         // deletes file from the system
		         f.delete();
		         System.out.println("delete() method is invoked");
		         // delete() is invoked
		         f.createNewFile();
		       
		         // tries to create new file in the system
		         bool = f.createNewFile();
		         
		         // print
		         System.out.println("File created: "+bool);
		         FileOutputStream fileOutputStream = new FileOutputStream(f);
		         OutputStreamWriter osw = new OutputStreamWriter(fileOutputStream);    
		            Writer w = new BufferedWriter(osw);
		           /* w.write("ORDER NO,ORDER DATE,ORDER STATUS,ORDER BY,MEAL TYPE,DELIVERY DATE,CONTACT NO,ORDER ITEM,ITEM CODE,ITEM DESC,QUANTITY,"
		            		+ "VENDOR NAME,RECEIVED,NOTIFIED,REJECTED,PICKED,DELIVERED,DRIVER NAME,DRIVER NUMBER\n");*/
		            w.write("ORDER NO,ORDER DATE,ORDER STATUS,ORDER BY,DELIVERY ZONE,"
		            		+ "MEAL TYPE,RICE/ROTI,SLOT,DELIVERY DATE,CONTACT NO,ORDER ITEM,ITEM CODE,SET,QUANTITY,"
		            		+ "VENDOR NAME,DRIVER NAME,DRIVER NUMBER,FINAL PRICE,PAYMENT MODE,DAY,"
		            		+ "CHOICE OF MENU,FOOD TASTE,QUANTITY,PACKAGING,TIMING,"
		            		+ "RATE,VALUE,DISCOUNT,SHARE,PROMO VALUE,CREDIT APPLIED,FINAL PRICE"
		            		+ "\n");
		            for(int i=0;i<orderDashBoardBeanList.size();i++){
		            	w.write(orderDashBoardBeanList.get(i).orderNo
		            			+","+orderDashBoardBeanList.get(i).orderDateValue
		            			+","+orderDashBoardBeanList.get(i).orderStatus
		            			+","+orderDashBoardBeanList.get(i).orderBy
		            			+","+orderDashBoardBeanList.get(i).deliveryZone
		            	
		            			+","+orderDashBoardBeanList.get(i).mealType
		            			+","+orderDashBoardBeanList.get(i).riceRoti
		            			+","+orderDashBoardBeanList.get(i).timeSlot
		            			+","+orderDashBoardBeanList.get(i).deliveryDateValue
		            			+","+orderDashBoardBeanList.get(i).contactNo
		            		
		            			+","+orderDashBoardBeanList.get(i).orderItem
		            			+","+orderDashBoardBeanList.get(i).itemCode
		            			+","+orderDashBoardBeanList.get(i).setName
		            		
		            			+","+orderDashBoardBeanList.get(i).quantity
		            			+","+orderDashBoardBeanList.get(i).kitchenName
		            		
		            			+","+orderDashBoardBeanList.get(i).driverName
		            			+","+orderDashBoardBeanList.get(i).driverNumber
		            		
		            			+","+orderDashBoardBeanList.get(i).price
		            			+","+orderDashBoardBeanList.get(i).paymentName
		            			+","+orderDashBoardBeanList.get(i).deliveryDayName
		            			
		            			+","+orderDashBoardBeanList.get(i).menu
		            			+","+orderDashBoardBeanList.get(i).taste
		            			+","+orderDashBoardBeanList.get(i).portion
		            			+","+orderDashBoardBeanList.get(i).packing
		            			+","+orderDashBoardBeanList.get(i).timelyDeliverd
		            			
		            			+","+orderDashBoardBeanList.get(i).itemRate
		            			+","+orderDashBoardBeanList.get(i).itemTotalPrice
		            			+","+orderDashBoardBeanList.get(i).totalDiscount
		            			
		            			+","+orderDashBoardBeanList.get(i).walletAmt
		            			+","+orderDashBoardBeanList.get(i).discountAmount
		            			+","+orderDashBoardBeanList.get(i).creditApplied
		            			
		            			+","+orderDashBoardBeanList.get(i).price
		            		//	+","+orderDashBoardBeanList.get(i).paymentName
		            			+"\n");
		            }
		            
		            /*for(OrderDashBoardBean bean:orderDashBoardBeanList){
		            	w.write(bean.orderNo+","+bean.orderDateValue+","+bean.orderStatus+","+bean.orderBy
		            			+","+bean.mealType+","+bean.deliveryDateValue+","+bean.contactNo+","+
		            			bean.orderItem+","+bean.itemCode+","+bean.itemDescription+","+bean.quantity
		            			+","+bean.kitchenName+","+bean.received+","+bean.notified+","+bean.rejected+
		            			","+bean.picked+","+bean.delivered+","+bean.driverName+","+bean.driverNumber);
		            }*/
		            w.close();
		           // Desktop.getDesktop().open(f);
		            FileInputStream fis = new FileInputStream(new File(reportNamewithPath));
		    		
		    		byte[] ba1 = new byte[1024];

		    		int baLength;

		    		ByteArrayOutputStream bios = new ByteArrayOutputStream();

		    		try {

		    			try {

		    				while ((baLength = fis.read(ba1)) != -1) {

		    					bios.write(ba1, 0, baLength);

		    				}
		    			} catch (Exception e1) {

		    			} finally {

		    				fis.close();

		    			}
		    			
		    			final AMedia amedia = new AMedia("orderDashBoardReport", "csv", "application/csv", bios.toByteArray());

		    			Filedownload.save(amedia);
		    		}catch(Exception exception){
		    			
		    		}
		      }catch(Exception e){
		         e.printStackTrace();
		      }
	    }else{
	    	Messagebox.show("No DATA found !","Information",Messagebox.OK,Messagebox.EXCLAMATION);
	    }
	      
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		startDate = null;
		endDate = null;
		kitchenName = null;
		onLoadQuery();
	}
	
	public OrderDashBoardBean getOrderDashBoardBean() {
		return orderDashBoardBean;
	}

	public void setOrderDashBoardBean(OrderDashBoardBean orderDashBoardBean) {
		this.orderDashBoardBean = orderDashBoardBean;
	}

	/*public ArrayList<OrderDashBoardBean> getOrderDashBoardBeanList() {
		return orderDashBoardBeanList;
	}

	public void setOrderDashBoardBeanList(
			ArrayList<OrderDashBoardBean> orderDashBoardBeanList) {
		this.orderDashBoardBeanList = orderDashBoardBeanList;
	}*/

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

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
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

	public ArrayList<String> getKitchenList() {
		return kitchenList;
	}

	public void setKitchenList(ArrayList<String> kitchenList) {
		this.kitchenList = kitchenList;
	}

	public String getKitchenName() {
		return kitchenName;
	}

	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public ArrayList<OrderDashBoardBean> getOrderDashBoardBeanList() {
		return orderDashBoardBeanList;
	}

	public void setOrderDashBoardBeanList(
			ArrayList<OrderDashBoardBean> orderDashBoardBeanList) {
		this.orderDashBoardBeanList = orderDashBoardBeanList;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public String getStatusStyle() {
		return statusStyle;
	}

	public void setStatusStyle(String statusStyle) {
		this.statusStyle = statusStyle;
	}

	
}
