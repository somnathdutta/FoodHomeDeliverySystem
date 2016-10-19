package ViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import pdfHandler.PdfDesignerHandler;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

import Bean.NewOrderBean;
import Bean.OrderInvoiceBean;
import Bean.OrderItemDetailsBean;

public class NewOrderViewModel {
	
	private NewOrderBean newOrderBean = null;
	
	private ArrayList<NewOrderBean> newOrderBeanList = new ArrayList<NewOrderBean>();
	
	public Boolean kitchenColumnVisibility = false;
	
	public Boolean actionColumnVisibility = true;
	
	Session session = null;
	
	private Connection connection = null;
	
	private Integer roleId = 0;
	
	private String userName ;
	
	private Date startDate;
	
	private Date endDate;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@Wire("#timer")
	Timer timer;

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		onLoadQuery();
		
		reload();
		
		
		
		
	}
	
	@Command
	@NotifyChange("*")
	public void refresh(){
		onLoadQuery();
	}
	public void reload(){
		for(int i=0;i<1;i++){
			NewOrderBean bean = new NewOrderBean();
			newOrderBeanList.add(bean);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickFind(){
		
		if(startDate!=null && endDate!=null ){
			if(newOrderBeanList!=null){
				newOrderBeanList.clear();
			}
			try {
					SQL:{
							PreparedStatement preparedStatement = null;
							ResultSet resultSet = null;
						try {
								if(roleId==1){
									preparedStatement = connection.prepareStatement(propertyfile.getPropValues("findnewOrderListAdminSql"));
									preparedStatement.setDate(1, new java.sql.Date( startDate.getTime()));
									preparedStatement.setDate(2, new java.sql.Date(endDate.getTime()));
								}else{
									preparedStatement = connection.prepareStatement(propertyfile.getPropValues("findnewOrderListKitchenSql"));
									preparedStatement.setString(1, userName);
									preparedStatement.setDate(2, new java.sql.Date( startDate.getTime()));
									preparedStatement.setDate(3, new java.sql.Date(endDate.getTime()));
								}
								resultSet = preparedStatement.executeQuery();
								Integer orderid = null;
								while(resultSet.next()){
									NewOrderBean newOrderbean = new NewOrderBean();
									newOrderbean.orderId = resultSet.getInt("order_id");
									newOrderbean.orderNo = resultSet.getString("order_no");
									newOrderbean.kitchenName = resultSet.getString("kitchen_name");
									if(roleId == 1){	
										kitchenColumnVisibility =  true;
										actionColumnVisibility = false;
									}
									newOrderbean.kitchenId = resultSet.getInt("kitchen_id");
									Integer tempOrderId = newOrderbean.orderId;
									if(newOrderbean.orderId.equals(orderid)){
										newOrderbean.orderId = null;
										newOrderbean.statusVisisbility = false;
										newOrderbean.orderIdVisibility =false;
										newOrderbean.detailsVisibility = false;
										newOrderbean.receiveVisibility = false;
										newOrderbean.rejectVisibility = false;
										
									}
									newOrderbean.cuisineName = resultSet.getString("cuisin_name");
									newOrderbean.categoryName = resultSet.getString("category_name");
									newOrderbean.quantity = resultSet.getInt("qty");
									newOrderbean.status = resultSet.getString("order_status_name");
									newOrderbean.rejectionReason = resultSet.getString("rejected_reason");
									if(newOrderbean.rejectionReason != null){
										newOrderbean.rejectedKitchenName = resultSet.getString("kitchen_name");
									}
									
									if(resultSet.getInt("order_status_id")==2 || resultSet.getInt("order_status_id")==3
											|| resultSet.getInt("order_status_id")==8){
										newOrderbean.receiveDisability = true;
									}
									newOrderbean.totalPrice = resultSet.getDouble("total_price");
									orderid =  tempOrderId;
									newOrderBeanList.add(newOrderbean);
								}
						} catch (Exception e) {
							Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
							e.printStackTrace();
						}finally{
							if(preparedStatement !=null){
								preparedStatement.close();
							}
						}
						
					}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else{
			Messagebox.show("Date required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear(){
		startDate = null;
		endDate = null;
		onLoadQuery();
	}
	
	
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		onLoadQuery();
	}
	/**
	 * 
	 * This method loads the order list from view (vw_new_order_list)
	 */
	public void onLoadQuery(){	
		if(newOrderBeanList!=null){
			newOrderBeanList.clear();
		}
		try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
					try {
							if(roleId==1){
								preparedStatement = connection.prepareStatement(propertyfile.getPropValues("newOrderListAdminSql"));
							}else{
								preparedStatement = connection.prepareStatement(propertyfile.getPropValues("newOrderListKitchenSql"));
								preparedStatement.setString(1, userName);
							}
							resultSet = preparedStatement.executeQuery();
							Integer orderid = null;
							while(resultSet.next()){
								NewOrderBean newOrderbean = new NewOrderBean();
								newOrderbean.orderId = resultSet.getInt("order_id");
								newOrderbean.orderNo = resultSet.getString("order_no");
								newOrderbean.kitchenName = resultSet.getString("kitchen_name");
								if(roleId == 1){	
									kitchenColumnVisibility =  true;
									actionColumnVisibility = false;
								}
								newOrderbean.kitchenId = resultSet.getInt("kitchen_id");
								Integer tempOrderId = newOrderbean.orderId;
								if(newOrderbean.orderId.equals(orderid)){
									newOrderbean.orderId = null;
									//newOrderbean.orderIdVisibility = false;
									newOrderbean.statusVisisbility = false;
									newOrderbean.orderIdVisibility =false;
									newOrderbean.detailsVisibility = false;
									newOrderbean.receiveVisibility = false;
									newOrderbean.rejectVisibility = false;
									//newOrderbean.kitchenNameVisisbility = false;
								}
								
								newOrderbean.cuisineName = resultSet.getString("cuisin_name");
								newOrderbean.categoryName = resultSet.getString("category_name");
								newOrderbean.quantity = resultSet.getInt("qty");
								newOrderbean.status = resultSet.getString("order_status_name");
								newOrderbean.orderDate = resultSet.getDate("order_date");
								java.util.Date currDate = new java.util.Date();
								String dbDateStr =  "",currDateStr="";
								//System.out.println("1..dbdate-->"+newOrderbean.orderDate+" curr date-->"+currDate);
								
								SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
								
								DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");

								dbDateStr = myFormat.format(newOrderbean.orderDate);
								currDateStr = myFormat.format(currDate);
								
							//	System.out.println("2..db refor-->"+dbDateStr+" curr str datre->"+currDateStr);
								
								Date currentDate , dateFromDb ;
								currentDate = format1.parse(currDateStr);
								dateFromDb = format1.parse(dbDateStr);
							//	System.out.println("3..dbDate-->"+dateFromDb+" current-->"+currentDate);
							
								if(dateFromDb.after(currentDate) ){
									newOrderbean.receiveDisability = true;
								}
								
								newOrderbean.rejectionReason = resultSet.getString("rejected_reason");
								if(newOrderbean.rejectionReason != null){
									newOrderbean.rejectedKitchenName = resultSet.getString("kitchen_name");
								}
								
								if(resultSet.getInt("order_status_id")==2 || resultSet.getInt("order_status_id")==3
										|| resultSet.getInt("order_status_id")==8){
									newOrderbean.receiveDisability = true;
								}
								newOrderbean.totalPrice = resultSet.getDouble("total_price");
								orderid =  tempOrderId;
								newOrderBeanList.add(newOrderbean);
							}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement !=null){
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
	 * On receive order button click method
	 * @throws IOException 
	 */
	@Command
	@NotifyChange("*")
	public void onClickReceiveOrder(@BindingParam("bean")NewOrderBean neworderbean) throws IOException{
		
	//	if(isOrderReceived(neworderbean)){
			//go to Line 309
			System.out.println("User qty::"+getUserQuantity(getOrderItemDetailList(neworderbean.orderId)) );
			//go to Line no 742
			System.out.println("Kitchen stock::"+getkitchenStockQuantity(neworderbean.kitchenId, getOrderItemDetailList(neworderbean.orderId)) );
			//go to Line no 789
		//	System.out.println("Updated kitchen stock::"+ updatedKitchenStock(getUserQuantity(getOrderItemDetailList(neworderbean.orderId)), getkitchenStockQuantity(neworderbean.kitchenId, getOrderItemDetailList(neworderbean.orderId)) ));
			//go to Line 809
		//	System.out.println("Kitchen stock id ::"+getkitchenStockIdList(neworderbean.kitchenId, getOrderItemDetailList(neworderbean.orderId)) );
			//go to Line 407
		//	updateStockKitchen(getkitchenStockIdList(neworderbean.kitchenId, getOrderItemDetailList(neworderbean.orderId) ),
			//	updatedKitchenStock(getUserQuantity(getOrderItemDetailList(neworderbean.orderId)),
			//			getkitchenStockQuantity(neworderbean.kitchenId, getOrderItemDetailList(neworderbean.orderId))) );
			
			receiveOrder(neworderbean);
			
			Messagebox.show("Order Received Successfully!");
			
			if(isAllKitchenReceive(neworderbean.orderId)){
				
				isOrderReceived(neworderbean);
				
				/*sendMessage(getDeviceRegId(neworderbean.orderId),neworderbean.orderNo);*/
			}else{
				System.out.println("All kitchen not receive their orders!");
			}
		//}
		/*if(neworderbean.orderId!=null){
		try {
			ArrayList<Integer> cuis =  new ArrayList<Integer>();
			ArrayList<Integer> cats =  new ArrayList<Integer>();
			ArrayList<Integer> stocks =  new ArrayList<Integer>();
			ArrayList<Integer> qtys = new ArrayList<Integer>();
			ArrayList<Integer> kitchenstocks = new ArrayList<Integer>();
			ArrayList<Integer> updatedkitchenstocks = new ArrayList<Integer>();
			
			
			
			cuis = getCuisineIdList(neworderbean.orderId);
			StringBuilder cuisidsBuilder =  new StringBuilder();
			String temp = cuis.toString();
			String fb = temp.replace("[", "(");
			String bb = fb.replace("]", ")");
			cuisidsBuilder.append(bb);
			
			
			cats = getCategoryIdList(neworderbean.orderId);
			StringBuilder catsidsBuilder =  new StringBuilder();
			String temp1 = cats.toString();
			String fb1 = temp1.replace("[", "(");
			String bb1 = fb1.replace("]", ")");
			catsidsBuilder.append(bb1);
			String cuisineids = cuisidsBuilder.toString();
			String catids = catsidsBuilder.toString();
			
			stocks = getStockIdList(cuisineids, catids, neworderbean.kitchenId);
			
			StringBuilder stockidbuilder = new StringBuilder();
			String temp2 = stocks.toString();
			String fb2 = temp2.replace("[", "(");
			String bb2 = fb2.replace("]", ")");
			stockidbuilder.append(bb2);
			String stockids = stockidbuilder.toString();
			
			qtys = getUserQuantityList(cuisineids, catids, neworderbean.orderId, neworderbean.kitchenId);
			
			
			kitchenstocks = getKitchenQuantityList(stockids);
			
			
			updatedkitchenstocks = updatedKitchenStock(qtys, kitchenstocks);
			
			System.out.println("Stock ids:"+stocks.toString());
			System.out.println("kitchen stocks qty:"+kitchenstocks.toString());
			System.out.println("User's qty:"+qtys.toString());
			System.out.println("My updated Stock List is:"+ updatedkitchenstocks);
			
			for(int i = 0 ; i< updatedkitchenstocks.size() ; i++){
				
				System.out.println("Update stock value->"+updatedkitchenstocks.get(i));
				System.out.println("Stock id->"+stocks.get(i));
				
			}
			
			SQL:{
					PreparedStatement preparedStatement = null;
				try {
					preparedStatement =  connection.prepareStatement(propertyfile.getPropValues("receiveButtonSql"));
					preparedStatement.setInt(1, neworderbean.orderId);
					int updatedRow = preparedStatement.executeUpdate();
					if(updatedRow>0){
						updateStockKitchen(stocks, updatedkitchenstocks);
						Messagebox.show("Order Received Successfully!");
						System.out.println("status::"+getOrderStatus(neworderbean.orderId));
						System.out.println("Device regid::"+getDeviceRegId(neworderbean.orderId));
						sendMessage(getDeviceRegId(neworderbean.orderId) );
						neworderbean.receiveDisability = true;
						onLoadQuery();
					}
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
				}finally{
					if(preparedStatement !=null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		
	}else{
		Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
	}*/
		
	}
	
	/** 
	 * Get user's ordered item list
	 * @param orderId
	 * @return parent call 185
	 */
	public ArrayList<OrderItemDetailsBean> getOrderItemDetailList(Integer orderId){
		ArrayList<OrderItemDetailsBean> orderItemDetailsBeanList = new ArrayList<OrderItemDetailsBean>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
					if(roleId==1){
						preparedStatement = connection.prepareStatement("SELECT * FROM vw_order_item_details_list"
								+ " WHERE order_id = ?");
						preparedStatement.setInt(1, orderId);
						
					}else{
						preparedStatement = connection.prepareStatement("SELECT * FROM vw_order_item_details_list"
								+ " WHERE order_id=? and kitchen_name=  ?");
						preparedStatement.setInt(1, orderId);
						preparedStatement.setString(2, userName);
						
					}
					//String sql = "SELECT * FROM vw_order_item_details_list WHERE order_id=? and kitchen_name=  ?";
					
					
						//preparedStatement = connection.prepareStatement(sql);
						
						resultSet = preparedStatement.executeQuery();
						while(resultSet.next()){
							OrderItemDetailsBean detailsBean =  new OrderItemDetailsBean();
							detailsBean.cuisineName = resultSet.getString("cuisin_name");
							detailsBean.cuisineId = resultSet.getInt("cuisine_id");
							detailsBean.categoryName = resultSet.getString("category_name");
							detailsBean.categoryId = resultSet.getInt("category_id");
							detailsBean.quantity = resultSet.getInt("qty");
							detailsBean.price = resultSet.getDouble("total_price");
							detailsBean.status = resultSet.getString("order_status_name");
							
							orderItemDetailsBeanList.add(detailsBean);
						}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement !=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return orderItemDetailsBeanList;
	}
	
	/**
	 * Get user quantity List
	 * @param orderItemDetailList
	 * @return parent call 187
	 */
	private ArrayList<Integer> getUserQuantity(ArrayList<OrderItemDetailsBean> orderItemDetailList){
    	ArrayList<Integer> userQuantityList = new ArrayList<Integer>();
    	for(OrderItemDetailsBean userQuantity : orderItemDetailList){
    		userQuantityList.add(userQuantity.quantity);
    	}
    	//System.out.println("User quantity list->"+userQuantityList);
    	return userQuantityList;
    }
	
	
	/**
	 * Get kitchen stock quantity List
	 * @param kitchenId
	 * @param orderItemDetailList
	 * @return parent call from 189
	 */
	private ArrayList<Integer> getkitchenStockQuantity(Integer kitchenId, ArrayList<OrderItemDetailsBean> orderItemDetailList){
    	ArrayList<Integer> stockInKitchen =  new ArrayList<Integer>();
    	try {
				SQL:{
	    				PreparedStatement preparedStatement = null;
	    				ResultSet resultSet = null;
	    				String sql= "select category_stock from fapp_kitchen_stock "
									+" where kitchen_cuisine_id =  ?"
									+" and kitchen_category_id = ?"
									+" and kitchen_id = ?";
	    				try {
	    					preparedStatement =  connection.prepareStatement(sql);
	    						for(OrderItemDetailsBean items : orderItemDetailList){
									preparedStatement.setInt(1, items.cuisineId);
									preparedStatement.setInt(2, items.categoryId);
									preparedStatement.setInt(3, kitchenId);
								
									resultSet = preparedStatement.executeQuery();
									if (resultSet.next()) {
										stockInKitchen.add(resultSet.getInt("category_stock"));
									}
								}
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("ERROR DUE TO:"+e.getMessage());
							e.printStackTrace();
						}finally{
							if(preparedStatement != null){
								preparedStatement.close();
							}
						}
	    		}
			} catch (Exception e) {
				// TODO: handle exception
			}

		//System.out.println("Kitchen Stock list::"+stockInKitchen);
    	return stockInKitchen;
    }
	
	/**
	 * Get updated stock List for kitchen
	 * @param userStockList
	 * @param kitchenStockList
	 * @return parent call 191
	 */
	public ArrayList<Integer> updatedKitchenStock(ArrayList<Integer> userStockList, ArrayList<Integer> kitchenStockList){
		ArrayList<Integer> updatedStockList = new ArrayList<Integer>();
		
		for(int i=0 ; i< kitchenStockList.size() && i<userStockList.size() ;i++){
			
			Integer upStock = kitchenStockList.get(i)- userStockList.get(i);
			updatedStockList.add(upStock);
		}
		//System.out.println("Updated Stock List->"+updatedStockList);
		return updatedStockList;
	}
	
	/**
	 * Get kitchen stock quantity List
	 * @param kitchenId
	 * @param orderItemDetailList
	 * @return go to line 193
	 */
	private ArrayList<Integer> getkitchenStockIdList(Integer kitchenId, ArrayList<OrderItemDetailsBean> orderItemDetailList){
    	ArrayList<Integer> kitchenStockIdList =  new ArrayList<Integer>();
    	try {
				SQL:{
	    				PreparedStatement preparedStatement = null;
	    				ResultSet resultSet = null;
	    				String sql= "select kitchen_stock_id from fapp_kitchen_stock "
									+" where kitchen_cuisine_id =  ?"
									+" and kitchen_category_id = ?"
									+" and kitchen_id = ?";
	    				try {
	    					preparedStatement =  connection.prepareStatement(sql);
	    						for(OrderItemDetailsBean items : orderItemDetailList){
									preparedStatement.setInt(1, items.cuisineId);
									preparedStatement.setInt(2, items.categoryId);
									preparedStatement.setInt(3, kitchenId);
								
									resultSet = preparedStatement.executeQuery();
									if (resultSet.next()) {
										kitchenStockIdList.add(resultSet.getInt("kitchen_stock_id"));
									}
								}
						} catch (Exception e) {
							// TODO: handle exception
							System.out.println("ERROR DUE TO:"+e.getMessage());
							e.printStackTrace();
						}finally{
							if(preparedStatement != null){
								preparedStatement.close();
							}
						}
	    		}
			} catch (Exception e) {
				// TODO: handle exception
			}

		//System.out.println("Kitchen Stock Id list::"+kitchenStockIdList);
    	return kitchenStockIdList;
    }
	
	/**
	 * Update kitchen stock
	 * @param stockIdList
	 * @param updateStockValueList parent call from 195
	 */
	public void updateStockKitchen(ArrayList<Integer> stockIdList, ArrayList<Integer> updateStockValueList){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_kitchen_stock "
								+" SET category_stock = ?"
							    +" WHERE kitchen_stock_id = ?";
					
					try {
						preparedStatement =  connection.prepareStatement(sql);
						
						for(int i = 0 ; i< updateStockValueList.size() ; i++){
							
							preparedStatement.setInt(1, updateStockValueList.get(i));
							preparedStatement.setInt(2, stockIdList.get(i));
							
							preparedStatement.addBatch();
						}
						int [] count = preparedStatement.executeBatch();
				    	   
				    	   for(Integer integer : count){
				    		   
				    		   System.out.println(integer);
				    		   // System.out.println("Kitchen Stock is Updated... ");  
				    	   }
					} catch (Exception e) {
						System.out.println("ERROR Due to:"+e.getMessage());
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
	 * Receive order b kitchen
	 * @param neworderbean
	 * @return parent call from 199
	 */
	private Boolean receiveOrder(NewOrderBean neworderbean){
		Boolean received = false;
		
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				String sql = "UPDATE fapp_order_tracking SET received = 'Y', received_time=current_timestamp"
						   + " WHERE order_id = ? AND kitchen_id = ?";
				
				try {
				preparedStatement =  connection.prepareStatement(sql);
				preparedStatement.setInt(1, neworderbean.orderId);
				preparedStatement.setInt(2, neworderbean.kitchenId);
				int updatedRow = preparedStatement.executeUpdate();
				if(updatedRow>0){
					
					onLoadQuery();
					received =  true;
					
				}
				} catch (Exception e) {
					System.out.println(e);
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				}
		
		
			SQL:{
						PreparedStatement preparedStatement = null;
						String sql = "UPDATE fapp_orders SET order_status_id = 8"
								   + " WHERE order_id = (SELECT order_id from fapp_orders where order_no = ?) ";
						try {
						preparedStatement =  connection.prepareStatement(sql);
						preparedStatement.setString(1, neworderbean.orderNo);
						int updatedRow = preparedStatement.executeUpdate();
						if(updatedRow>0){
							received =  true;
						}
						} catch (Exception e) {
							System.out.println(e);
							}finally{
								if(preparedStatement!=null){
									preparedStatement.close();
								}
							}
						}	
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return received;
	}
	
	/**
	 * Check if all kicthen accepts order or not
	 * @param orderid
	 * @return parent call from 203
	 */
	private Boolean isAllKitchenReceive(Integer orderid){
		Integer totalOrders = 0,totalReceivedOrders = 0 ;
		
		try {
			SQL:{  
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					
					String sql = "SELECT "
							 	+" count(ORDER_ID)AS total_order "
								+" from fapp_order_tracking "
								+" where ORDER_ID = ?";
				 try {
					preparedStatement =  connection.prepareStatement(sql);
					preparedStatement.setInt(1, orderid);
					resultSet = preparedStatement.executeQuery();
					if (resultSet.next()) {
						totalOrders = resultSet.getInt("total_order");
					}
				} catch (Exception e) {
					// TODO: handle exceptio
					e.printStackTrace();
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
				 System.out.println("total orders-->"+totalOrders);
			}
			
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					
					String sql = "SELECT "
						 	+" count(received)AS total_order_received "
							+" from fapp_order_tracking "
							+" where ORDER_ID = ? AND received = 'Y'";
					 try {
							preparedStatement =  connection.prepareStatement(sql);
							preparedStatement.setInt(1, orderid);
							resultSet = preparedStatement.executeQuery();
							if (resultSet.next()) {
								totalReceivedOrders = resultSet.getInt("total_order_received");
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
							}finally{
								if(preparedStatement!=null){
									preparedStatement.close();
								}
							}
					 System.out.println("Total received orders::"+totalReceivedOrders);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if( totalOrders!=0 && totalReceivedOrders!=0 && totalOrders == totalReceivedOrders){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Receive the order
	 * @param neworderbean parent call from 205
	 */
	private Boolean isOrderReceived(NewOrderBean neworderbean){
		Boolean receivedOrder = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					
				try {
					preparedStatement =  connection.prepareStatement(propertyfile.getPropValues("receiveButtonSql"));
					preparedStatement.setInt(1, neworderbean.orderId);
					int updatedRow = preparedStatement.executeUpdate();
					if(updatedRow>0){
					//	Messagebox.show("Order Received Successfully!");
						receivedOrder = true;
						//System.out.println("Device regid::"+getDeviceRegId(neworderbean.orderId));
						//sendMessage(getDeviceRegId(neworderbean.orderId),neworderbean.orderNo );
						sendMessageToCustomer(getCustomerMobile(neworderbean.orderNo,"REGULAR"), neworderbean.orderNo, getOrderTime(neworderbean.orderNo, "REGULAR"), 2);
						
						//sendMessageToMobile(getCustomerMobile(neworderbean.orderNo), neworderbean.orderNo, getOrderTime(neworderbean.orderNo), 2);
						neworderbean.receiveDisability = true;
						onLoadQuery();
					}
				} catch (Exception e) {
					System.out.println(e);
				}finally{
					if(preparedStatement!=null){
						preparedStatement.close();
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return receivedOrder;
	}
	
	
	
	@Command
	@NotifyChange("*")
	public void onClickDetails(@BindingParam("bean")NewOrderBean orderBean) throws Exception{
		
		if(orderBean.orderId!=null){	
			
			Map<String, Object> parentMap = new HashMap<String, Object>();
			
			parentMap.put("parentOrderObject", orderBean);
			
			Window win = (Window) Executions.createComponents("showDetails.zul", null, parentMap);
			
			win.doModal();
			
			//generateInvoicePdf(orderBean);
		}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	public void generateInvoicePdf(NewOrderBean orderBean) throws Exception{
		
		OrderInvoiceBean orderInvoiceBean = new OrderInvoiceBean();
		
		orderInvoiceBean.newOrderBean = orderBean;
		
		orderInvoiceBean.kitchenName = orderBean.kitchenName;
		
		getDeliveryAddressDetails(orderInvoiceBean);
		
		String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		
		PdfDesignerHandler pdf = new PdfDesignerHandler();
		
		String pdfNamewithPath = realPath + "test1.pdf";
		
		pdf.getDetails(pdfNamewithPath, orderInvoiceBean, getOrderItemDetailList(orderBean.orderId));
		
		FileInputStream fis = new FileInputStream(new File(pdfNamewithPath));
		
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

			final AMedia amedia = new AMedia("invoice.pdf", "pdf", "application/pdf", bios.toByteArray());

			Filedownload.save(amedia);

		} catch (Exception ex) {

			ex.printStackTrace();

		} finally {

			File xlsFile = new File(pdfNamewithPath);

			if (xlsFile.exists()) {

				FileUtils.forceDelete(xlsFile);

			}
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickRejectOrder(@BindingParam("bean")NewOrderBean orderBean){
		
		if(orderBean.orderId!=null){
			
			Map<String,Object> parentMap = new HashMap<String, Object>();
			
			parentMap.put("parentOrderObject", orderBean);
			
			Window win = (Window) Executions.createComponents("orderRejection.zul", null, parentMap);
			
			win.doModal();
			
		}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
	}

	
	/**
	 * Send notification to App
	 * @param deviceregId
	 * @throws IOException
	 */
	public static void sendMessage(String deviceregId,String orderNo) throws IOException
	{
		//String API_KEY = "AIzaSyCvaJt0tbW3Nn1pCPynPduVxo3T3l3_Yek"; //sender id got from google api console project(My)
		String API_KEY = "AIzaSyA03muwGMLqGmk2mwY3x1di5mI3jEVViqM";//(sir)
		String collpaseKey = "gcm_message"; //if messages are sent and not delivered yet to android device (as device might not be online), then only deliver latest message when device is online
		//String messageStr = "message content here"; //actual message content
		String messageStr = "Your order "+orderNo+" received sucessfully!";
		//String messageId = "APA91bGgGzVQWb88wkRkACGmHJROeJSyQbzLvh3GgP2CASK_NBsuIXH15HcnMta3e9ZXMhdPN6Z3FSD2Pezf6bhgUuM2CF74SgZbG4Zr57LA76VVaNvSi7XM7QEuAVLIiTsXnVq3QAUFDo-ynD316bF10JGT3ZOaSQ"; //gcm registration id of android device
		String messageId = deviceregId;
				
		Sender sender = new Sender(API_KEY);
		Message.Builder builder = new Message.Builder();
		
		builder.collapseKey(collpaseKey);
		builder.timeToLive(30);
		builder.delayWhileIdle(true);
		builder.addData("message", messageStr);
		
		Message message = builder.build();
		
		List<String> androidTargets = new ArrayList<String>();
		//if multiple messages needs to be deliver then add more message ids to this list
		androidTargets.add(messageId);
		
		MulticastResult result = sender.send(message, androidTargets, 1);
		System.out.println("result = "+result);
		
		if (result.getResults() != null) 
		{
			System.out.println("Status:"+messageStr+" is sent to device reg id:"+messageId);
			int canonicalRegId = result.getCanonicalIds();
			System.out.println("canonicalRegId = "+canonicalRegId);
			
			if (canonicalRegId != 0) 
			{
            }
		}
		else 
		{
			int error = result.getFailure();
			System.out.println("Broadcast failure: " + error);
		}
	}
	/**
	 * Getting the device reg id 
	 * @param orderId
	 * @return
	 */
	public String getDeviceRegId(Integer orderId ){
		String deviceregId = "";
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			SQL:{
					String sql = "SELECT device_reg_id FROM fapp_devices"
								+" WHERE email_id = (SELECT user_mail_id FROM fapp_orders"
								+ " WHERE order_id = ? )";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, orderId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							deviceregId = resultSet.getString("device_reg_id");
						}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement !=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return deviceregId;
	}
	
	
	public void getDeliveryAddressDetails(OrderInvoiceBean orderinvoicebean){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT * FROM vw_orders_delivery_address WHERE order_id=?";
					try {
						
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, orderinvoicebean.newOrderBean.orderId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							orderinvoicebean.orderBy = resultSet.getString("order_by");
							orderinvoicebean.contactNumber = resultSet.getString("contact_number");
							orderinvoicebean.emailId = resultSet.getString("user_mail_id");
							orderinvoicebean.flatNo = resultSet.getString("flat_no");
							orderinvoicebean.streetName = resultSet.getString("street_name");
							orderinvoicebean.landMark = resultSet.getString("landmark");
							orderinvoicebean.location = resultSet.getString("delivery_location");
							orderinvoicebean.city = resultSet.getString("city");
							orderinvoicebean.pincode = resultSet.getString("pincode");
						}
					}  catch (Exception e) {
						Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement !=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	
	private String getOrderTime(String orderNo,String orderType){
		String time="";
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT order_date from fapp_orders where order_id = "
							+ " (SELECT order_id from fapp_orders where order_no = ?)";
					
					String sqlSub = "SELECT subscription_date::date AS order_date from fapp_subscription where subscription_id = "
							+ " (SELECT subscription_id from fapp_subscription where subscription_no = ?)";
					try {
						if(orderType.equalsIgnoreCase("REGULAR")){
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setString(1, orderNo);
						}else{
							preparedStatement = connection.prepareStatement(sqlSub);
							preparedStatement.setString(1, orderNo);
						}
						
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							time = resultSet.getString("order_date");
						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("Order date search failed due to:"+e.getMessage());
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return time;
	}
	
	
	private void sendMessageToMobile(String recipient,String orderNo, String orderTime, Integer orderStatusId){
		try {
			//String recipient = "+917872979469";
			//String message = orderNo+" is assigned for you!";
			String message = "";
			if(orderStatusId!=2){
				message = "New Retail order "+orderNo+" is assigned to you :"+orderTime+". Eazelyf.";
			//	message = "New order "+ orderNo +" is assigned to you : "+orderTime+". Thnx & Rgds Eazelyf.";
			}
			else{
				message ="Your order "+orderNo+" has been accepted. Eazelyf.";
				//message = "Your order "+orderNo+" is under process"+". Thnx & Rgds Eazelyf.";
			}
			String username = "nextgenvision"; 
			String password = "sms@123";
			String senderId = "eazelyf";
			String requestUrl  = "http://fastsms.way2mint.com/SendSMS/sendmsg.php?" +
					 "uname=" + URLEncoder.encode(username, "UTF-8") +
					 "&pass=" + URLEncoder.encode(password, "UTF-8") +
					 "&send=" + URLEncoder.encode(senderId, "UTF-8") +
					 "&dest=" + URLEncoder.encode(recipient, "UTF-8") +
					 "&msg=" + URLEncoder.encode(message, "UTF-8") ;
			System.out.println("Message sent to mobile no::"+recipient+"::"+message);
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection)url.openConnection();
			System.out.println("Message Response:::::"+uc.getResponseMessage());
			uc.disconnect();
			} catch(Exception ex) {
			System.out.println(ex.getMessage());
			}	
	}
	
	private String getCustomerMobile(String orderNo){
		String mob="";
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT contact_number from fapp_orders where order_id = "
							+ " (SELECT order_id from fapp_orders where order_no = ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, orderNo);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							mob = resultSet.getString("contact_number");
						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("Contact number search failed due to:"+e.getMessage());
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mob;
	}
	
	private String getOrderTime(String orderNo){
		String time="";
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT order_date from fapp_orders where order_id = "
							+ " (SELECT order_id from fapp_orders where order_no = ?)";
					try {
						
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, orderNo);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							time = resultSet.getString("order_date");
						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("Order date search failed due to:"+e.getMessage());
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return time;
	}
	
	private String getCustomerMobile(String orderNo,String orderType){
		String mob="";
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT contact_number from fapp_orders where order_id = "
							+ " (SELECT order_id from fapp_orders where order_no = ?)";
					
					String sqlSub = "SELECT contact_number from fapp_subscription where subscription_id = "
							 +" (SELECT subscription_id from fapp_subscription where subscription_no = ?)";
					try {
						if(orderType.equalsIgnoreCase("REGULAR")){
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setString(1, orderNo);
						}else{
							preparedStatement = connection.prepareStatement(sqlSub);
							preparedStatement.setString(1, orderNo);
						}
						
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							mob = resultSet.getString("contact_number");
						}
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println("Contact number search failed due to:"+e.getMessage());
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mob;
	}
	
	
	private void sendMessageToCustomer(String recipient, String orderNo, String orderTime, Integer orderStatusId){
		try {
			//String recipient = "+917872979469";
			//String message = orderNo+" is assigned for you!";
			String message = "";
			if(orderStatusId==1){
				if(orderNo.startsWith("SUB")){
					message = "New Subscription order "+orderNo+" is assigned to you :  "+orderTime+". Eazelyf.";
					//message = "New Subscription order "+ orderNo +" is assigned to you : "+orderTime+". Thnx & Rgds Eazelyf.";
				}else{
					message = "New Retail order "+orderNo+" is assigned to you : "+orderTime+". Eazelyf.";
					//message = "New order "+ orderNo +" is assigned to you : "+orderTime+". Thnx & Rgds Eazelyf.";
				}	
			}else if(orderStatusId==7){
				message = orderNo+" has been successfully delivered to you. Eazelyf.";
				//message = orderNo +" has been successfully delivered to you. Thnx & Rgds Eazelyf. ";
			}else{
				if(orderNo.startsWith("REG")){
					message = "Your order "+orderNo+" has been accepted. Eazelyf.";
					//message = "Your order "+orderNo+" is under process"+". Thnx & Rgds Eazelyf.";
					//Your order "+orderNo+" has been accepted.Thank you for choosing us. Have a great day! Thnx & Rgds Eazelyf.";
				}else{
					message = "Your order "+orderNo+" for subscription is accepted. Thank you for choosing us. Have a great day!. Eazelyf.";
					//message = "Your order "+orderNo+" for subscription is accepted. "
						//	+ "Thank you for choosing us. Have a great day! Thnx & Rgds Eazelyf.";
				}
			}
			/*
			 * Delivery boy ABCD having mobile no 91XXXXXXXX is assigned to deliver your order.Thnx & Rgds Eazelyf.
				ORDER/000001 has been successfully delivered to you.Thnx & Rgds Eazelyf.
			 */
			String username = "nextgenvision"; 
			String password = "sms@123";
			String senderId = "eazelyf";
			String requestUrl  = "http://fastsms.way2mint.com/SendSMS/sendmsg.php?" +
					 "uname=" + URLEncoder.encode(username, "UTF-8") +
					 "&pass=" + URLEncoder.encode(password, "UTF-8") +
					 "&send=" + URLEncoder.encode(senderId, "UTF-8") +
					 "&dest=" + URLEncoder.encode(recipient, "UTF-8") +
					 "&msg=" + URLEncoder.encode(message, "UTF-8") ;
			System.out.println("Message sent to mobile no::"+recipient+"::"+message);
			URL url = new URL(requestUrl);
			HttpURLConnection uc = (HttpURLConnection)url.openConnection();
			System.out.println("Message Response:::::"+uc.getResponseMessage());
			uc.disconnect();
			} catch(Exception ex) {
			System.out.println(ex.getMessage());
			}	
	}
	
	
	/*
	public ArrayList<Integer> getCuisineIdList(Integer orderId){
		ArrayList<Integer> cuisineIdList = new ArrayList<Integer>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT cuisine_id FROM vw_new_orders_list WHERE order_id= ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, orderId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							cuisineIdList.add(resultSet.getInt("cuisine_id"));
						}
						
					} catch (Exception e) {
						System.out.println("ERROR DUE TO:"+e.getMessage());
					  }finally{
						  if(preparedStatement!=null){
							  preparedStatement.close();
						  }
					  }
			  }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return cuisineIdList;
	}
	
	public ArrayList<Integer> getCategoryIdList(Integer orderId){
		ArrayList<Integer> categoryIdList = new ArrayList<Integer>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT category_id FROM vw_new_orders_list WHERE order_id= ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, orderId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							categoryIdList.add(resultSet.getInt("category_id"));
						}
						
					} catch (Exception e) {
						System.out.println("ERROR DUE TO:"+e.getMessage());
					  }finally{
						  if(preparedStatement!=null){
							  preparedStatement.close();
						  }
					  }
			   }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return categoryIdList;
	}

	public ArrayList<Integer> getStockIdList(String cuisids, String catids, Integer kitchenId){
		ArrayList<Integer> stockIdList = new ArrayList<Integer>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT kitchen_stock_id from fapp_kitchen_stock " 
								+" where "
								+" kitchen_cuisine_id IN "+cuisids+""
								+" and kitchen_category_id IN "+catids+""
								+" and kitchen_id = ? ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, kitchenId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							stockIdList.add(resultSet.getInt("kitchen_stock_id"));
						}
						
					} catch (Exception e) {
						System.out.println("ERROR DUE TO:"+e.getMessage());
					  }finally{
						  if(preparedStatement!=null){
							  preparedStatement.close();
						  }
					  }
			   }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return stockIdList;
	}
	
	public ArrayList<Integer> getUserQuantityList(String cuisineids, String categoryids,Integer orderId , Integer kitchenId){
		ArrayList<Integer> userQuantityList = new ArrayList<Integer>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select qty from vw_new_orders_list "
								+" where "
								+" cuisine_id IN "+cuisineids+" "
								+" and category_id IN "+categoryids+""
								+" and order_id=? "
								+" and kitchen_id = ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, orderId);
						preparedStatement.setInt(2, kitchenId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							userQuantityList.add(resultSet.getInt("qty"));
						}
						
					} catch (Exception e) {
						System.out.println("ERROR DUE TO:"+e.getMessage());
					  }finally{
						  if(preparedStatement!=null){
							  preparedStatement.close();
						  }
					  }
			   }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return userQuantityList;
	}
	
	public ArrayList<Integer> getKitchenQuantityList(String stockids){
		ArrayList<Integer> kitchenQuantityList = new ArrayList<Integer>();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT category_stock from fapp_kitchen_stock "
								+" where "
								+" kitchen_stock_id IN "+stockids+" ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							kitchenQuantityList.add(resultSet.getInt("category_stock"));
						}
						
					} catch (Exception e) {
						System.out.println("ERROR DUE TO:"+e.getMessage());
					  }finally{
						  if(preparedStatement!=null){
							  preparedStatement.close();
						  }
					  }
			   }
		} catch (Exception e) {
			// TODO: handle exception
		}
		return kitchenQuantityList;
	}
	
	public String getOrderStatus(Integer orderId){
		String status = "";
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			SQL:{
					String sql = "SELECT order_status_name FROM fapp_order_status"
								+" WHERE order_status_id = (SELECT order_status_id FROM fapp_orders"
								+ " WHERE order_id = ? )";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, orderId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							status = resultSet.getString("order_status_name");
						}
					} catch (Exception e) {
						Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
					}finally{
						if(preparedStatement !=null){
							preparedStatement.close();
						}
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return status;
	}
	
	*/
	
	public NewOrderBean getNewOrderBean() {
		return newOrderBean;
	}

	public void setNewOrderBean(NewOrderBean newOrderBean) {
		this.newOrderBean = newOrderBean;
	}

	public ArrayList<NewOrderBean> getNewOrderBeanList() {
		return newOrderBeanList;
	}

	public void setNewOrderBeanList(ArrayList<NewOrderBean> newOrderBeanList) {
		this.newOrderBeanList = newOrderBeanList;
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

	public Boolean getKitchenColumnVisibility() {
		return kitchenColumnVisibility;
	}

	public void setKitchenColumnVisibility(Boolean kitchenColumnVisibility) {
		this.kitchenColumnVisibility = kitchenColumnVisibility;
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

	public Boolean getActionColumnVisibility() {
		return actionColumnVisibility;
	}

	public void setActionColumnVisibility(Boolean actionColumnVisibility) {
		this.actionColumnVisibility = actionColumnVisibility;
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
