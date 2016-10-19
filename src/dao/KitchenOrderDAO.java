package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.zkoss.zul.Messagebox;

import Bean.OrderDashBoardBean;

public class KitchenOrderDAO {
	/**
	 * Method for returnning OrderDashBoardBean LIst for any kitchen
	 * @param connection
	 * @param kitchenName
	 * @return ArrayList<OrderDashBoardBean>
	 */
	public static ArrayList<OrderDashBoardBean> onLoadQuery(Connection connection,String kitchenName){
		ArrayList<OrderDashBoardBean> kitchenOrderList = new ArrayList<OrderDashBoardBean>();
		if(kitchenOrderList.size()>0){
			kitchenOrderList.clear();
		}
		if(connection!=null){
			try {
				SQL:{
					 PreparedStatement preparedStatement = null;
					 ResultSet resultSet = null;
					 String sql = "SELECT * FROM vw_order_dashboard where kitchen_name = ?";
					 try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, kitchenName);
						resultSet = preparedStatement.executeQuery();
						String orderNo = null;
						while (resultSet.next()) {
							OrderDashBoardBean dashBoardBean = new OrderDashBoardBean();
							dashBoardBean.orderNo = resultSet.getString("order_no");
							String itemCode = resultSet.getString("item_code");
							if(itemCode!=null){
								dashBoardBean.itemCode = itemCode;
							}else{
								dashBoardBean.itemCode = "-NA-";
							}
							String tempOrderNo = dashBoardBean.orderNo;
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
							dashBoardBean.price = resultSet.getDouble("total_price");
							dashBoardBean.timeSlot = resultSet.getString("time_slot");
							dashBoardBean.orderDateValue = reformattedOrderDate;
							dashBoardBean.deliveryDateValue = reformattedDeliveryOrderDate;
							dashBoardBean.orderStatus= resultSet.getString("order_status");
							dashBoardBean.orderBy = resultSet.getString("order_by");
							dashBoardBean.mealType = resultSet.getString("meal_type");
							dashBoardBean.deliveryDate = resultSet.getDate("delivery_date");
							dashBoardBean.contactNo = resultSet.getString("contact_number");
							dashBoardBean.orderItem = resultSet.getString("category_name");
							dashBoardBean.quantity = resultSet.getInt("qty");
							dashBoardBean.kitchenName = resultSet.getString("kitchen_name");
							dashBoardBean.received = resultSet.getString("received");
							if(dashBoardBean.received.equalsIgnoreCase("Y")){
								dashBoardBean.orderReceived = true;
							}else{
								dashBoardBean.orderReceived = false;
							}
							
							dashBoardBean.notified = resultSet.getString("notify");
							if(dashBoardBean.received.equals("Y")){
								dashBoardBean.orderNotified = false;
							}else{
								dashBoardBean.orderNotified = true;
							}
							
							if(resultSet.getString("delivered")!=null){
								dashBoardBean.delivered = resultSet.getString("delivered");
							}					
							else{
								dashBoardBean.delivered = "N";
							}
							if(dashBoardBean.received.equals("Y") && dashBoardBean.notified.equals("Y")){
								dashBoardBean.orderDelivered = false;
							}else{
								dashBoardBean.orderDelivered = true;
							}
							
							dashBoardBean.picked = resultSet.getString("order_picked");
							dashBoardBean.rejected = resultSet.getString("rejected");
							String itemDesc = resultSet.getString("item_description");
							if(itemDesc!=null){
								if(itemDesc.contains(","))
									itemDesc = itemDesc.replace(",", ";");
								dashBoardBean.itemDescription = itemDesc;
							}else{
								dashBoardBean.itemDescription = "NA";
							}
							
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
								dashBoardBean.receiveVisibility = false;
								dashBoardBean.notifyVisibility = false;
								dashBoardBean.deliverVisibility = false;
							}	
							orderNo = tempOrderNo ;
							kitchenOrderList.add(dashBoardBean);
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
			return kitchenOrderList;
		}else{
			return kitchenOrderList;
		}
		
	}
}
