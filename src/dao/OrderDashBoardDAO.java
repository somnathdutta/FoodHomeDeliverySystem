package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.OrderDashBoardBean;

public class OrderDashBoardDAO {

	public static ArrayList<OrderDashBoardBean> findOrdersByOrderNo(String orderno, Connection connection){
		ArrayList<OrderDashBoardBean> orderDashBoardBeanList = new ArrayList<OrderDashBoardBean>();
		if(orderDashBoardBeanList.size()>0){
			orderDashBoardBeanList.clear();
		}
		if(connection!=null){
		try {
			SQL:{
				 PreparedStatement preparedStatement = null;
				 ResultSet resultSet = null;
				 String sql = "SELECT * FROM vw_order_dashboard where order_no like ?";
				 try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, "%"+orderno);
					resultSet = preparedStatement.executeQuery();
					String orderNo = null;double itemTotalPriceValue = 0.0,finalPrice = 0.0; 
					while (resultSet.next()) {
						OrderDashBoardBean dashBoardBean = new OrderDashBoardBean();
						dashBoardBean.orderNo = resultSet.getString("order_no");
						dashBoardBean.price = resultSet.getDouble("final_price");
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
							if(deliveryDate!=null)
							reformattedDeliveryOrderDate = myFormat.format(fromDB.parse(deliveryDate));
						} catch (Exception e) {
							e.printStackTrace();
						}
						dashBoardBean.orderDateValue = reformattedOrderDate;
						dashBoardBean.deliveryDateValue = reformattedDeliveryOrderDate;
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
						dashBoardBean.deliveryDate = resultSet.getDate("delivery_date");
						dashBoardBean.contactNo = resultSet.getString("contact_number");
						dashBoardBean.orderItem = resultSet.getString("category_name");
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
						
						if(dashBoardBean.orderNo.equals(orderNo)){
							dashBoardBean.orderNoVisibiliity= false;
							dashBoardBean.orderDateVisibility = false;
							dashBoardBean.deliveryDateVisibility = false;
							dashBoardBean.orderByVisibility = false;
							dashBoardBean.contactNoVisibility = false;
							dashBoardBean.mealTypeVisibility = false;
							dashBoardBean.orderCreationTimeVis = false;
							dashBoardBean.timeSlotVis = false;
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
		if(orderDashBoardBeanList.size()==0){
			Messagebox.show("Oops! No order found!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
		}
		return orderDashBoardBeanList;
	}
}
