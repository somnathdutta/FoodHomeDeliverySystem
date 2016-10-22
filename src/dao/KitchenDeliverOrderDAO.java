package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.zkoss.zul.Messagebox;

import Bean.ReceivedOrderBean;

public class KitchenDeliverOrderDAO {

	public static ArrayList<ReceivedOrderBean> loadKitchenDeliveredOrders(Connection connection, String kitchenName){
		ArrayList<ReceivedOrderBean> receivedOrderBeanList = new ArrayList<ReceivedOrderBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_kitchen_delivered_orders where kitchen_id= "
							+ " (select kitchen_id from fapp_kitchen where kitchen_name = ?) and order_no IS NOT NULL";
					try {
					 preparedStatement = connection.prepareStatement(sql);
					 preparedStatement.setString(1, kitchenName);
					 resultSet = preparedStatement.executeQuery();
					 String orderno = "",status="";
					 while(resultSet.next()){
						 ReceivedOrderBean receivedorderbean = new ReceivedOrderBean();
						 receivedorderbean.orderId = resultSet.getInt("order_id");
						// receivedOrderBean.orderId = receivedorderbean.orderId;
						 receivedorderbean.orderNo = resultSet.getString("order_no");
						// receivedorderbean.status = resultSet.getString("order_status_name");
						 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						 java.sql.Date date = resultSet.getDate("order_date");
						 receivedorderbean.orderDateValue =  sdf.format(date);
						 
						 receivedorderbean.deliveryDateSql = resultSet.getDate("delivery_date");
						 receivedorderbean.deliveryDateValue = sdf.format(receivedorderbean.deliveryDateSql);
						 
						 String tempOrderNo = "";
						 if(orderno!=null){
							 tempOrderNo = receivedorderbean.orderNo;
							 if(receivedorderbean.orderNo.equals(orderno)){
								 receivedorderbean.orderNo = "";
								 receivedorderbean.status = "";
								 receivedorderbean.orderDateValue="";
								 receivedorderbean.deliveryDateValue="";
							 }
						 }
						
						
						 //receivedorderbean.cuisineName = resultSet.getString("cuisin_name");
						 //receivedorderbean.categoryName = resultSet.getString("category_name");
						 receivedorderbean.itemName = resultSet.getString("item_name");
						receivedorderbean.itemDescription = resultSet.getString("item_description");
						// String tepmItemName = receivedorderbean.itemName;
							//if(receivedorderbean.itemName.equals(itemName)){
								//receivedorderbean.itemName="";
							//	receivedorderbean.itemVisibility = false;
						//	}
						 receivedorderbean.quantity = resultSet.getInt("qty");
						 receivedorderbean.price = resultSet.getDouble("category_price");
						// receivedorderbean.orderStatus = resultSet.getString("order_status_name");
						
						//receivedOrderBean.orderStatus = receivedorderbean.orderStatus;
						 //itemName=tepmItemName;
						 orderno = tempOrderNo;
						 //status = tempStatus;
						 receivedOrderBeanList.add(receivedorderbean);
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
		return receivedOrderBeanList;
	}
	
	public static ArrayList<ReceivedOrderBean> showKitchenKitchenDeliveredOrdersWithinRange(Connection connection, String kitchenName,
			Date startDate, Date endDate){
		ArrayList<ReceivedOrderBean> receivedOrderBeanList = new ArrayList<ReceivedOrderBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select * from vw_kitchen_delivered_orders where kitchen_id= "
							+ " (select kitchen_id from fapp_kitchen where kitchen_name = ?) and "
							+ " order_date >= ? and order_date <= ?"
							+ " and order_no IS NOT NULL";
					try {
					 preparedStatement = connection.prepareStatement(sql);
					 preparedStatement.setString(1, kitchenName);
					 preparedStatement.setDate(2, new java.sql.Date( startDate.getTime()));
						preparedStatement.setDate(3, new java.sql.Date(endDate.getTime()));
					 resultSet = preparedStatement.executeQuery();
					 String orderno = "",status="";
					 while(resultSet.next()){
						 ReceivedOrderBean receivedorderbean = new ReceivedOrderBean();
						 receivedorderbean.orderId = resultSet.getInt("order_id");
						// receivedOrderBean.orderId = receivedorderbean.orderId;
						 receivedorderbean.orderNo = resultSet.getString("order_no");
						// receivedorderbean.status = resultSet.getString("order_status_name");
						 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
						 java.sql.Date date = resultSet.getDate("order_date");
						 receivedorderbean.orderDateValue =  sdf.format(date);
						 
						 receivedorderbean.deliveryDateSql = resultSet.getDate("delivery_date");
						 receivedorderbean.deliveryDateValue = sdf.format(receivedorderbean.deliveryDateSql);
						 
						 String tempOrderNo = "";
						 if(orderno!=null){
							 tempOrderNo = receivedorderbean.orderNo;
							 if(receivedorderbean.orderNo.equals(orderno)){
								 receivedorderbean.orderNo = "";
								 receivedorderbean.status = "";
								 receivedorderbean.orderDateValue="";
								 receivedorderbean.deliveryDateValue = null;
							 }
						 }
						
						
						 //receivedorderbean.cuisineName = resultSet.getString("cuisin_name");
						 //receivedorderbean.categoryName = resultSet.getString("category_name");
						 receivedorderbean.itemName = resultSet.getString("item_name");
						receivedorderbean.itemDescription = resultSet.getString("item_description");
						// String tepmItemName = receivedorderbean.itemName;
							//if(receivedorderbean.itemName.equals(itemName)){
								//receivedorderbean.itemName="";
							//	receivedorderbean.itemVisibility = false;
						//	}
						 receivedorderbean.quantity = resultSet.getInt("qty");
						 receivedorderbean.price = resultSet.getDouble("category_price");
						// receivedorderbean.orderStatus = resultSet.getString("order_status_name");
						
						//receivedOrderBean.orderStatus = receivedorderbean.orderStatus;
						 //itemName=tepmItemName;
						 orderno = tempOrderNo;
						 //status = tempStatus;
						 receivedOrderBeanList.add(receivedorderbean);
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
		return receivedOrderBeanList;
	}
}
