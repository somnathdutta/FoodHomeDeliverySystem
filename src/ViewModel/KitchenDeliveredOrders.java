package ViewModel;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

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
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.ReceivedOrderBean;
import dao.FreshKitchenOrderDAO;
import dao.KitchenDeliverOrderDAO;

public class KitchenDeliveredOrders {
	
	public ReceivedOrderBean deliverOrderBean = new ReceivedOrderBean();
	
	private ArrayList<ReceivedOrderBean> deliverOrderBeanList = new ArrayList<ReceivedOrderBean>();
	
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
		if(deliverOrderBeanList.size()==0){
			reload();
		}
		System.out.println("zul page >> kitchenFreshorders.zul");
	}
	
	public void reload(){
		for(int i=0;i<1;i++){
			ReceivedOrderBean bean = new ReceivedOrderBean();
			deliverOrderBeanList.add(bean);
		}
	}
	/**
	 * 
	 * On load query to list of all orders which are received view (vw_received_order_list)
	 */
	public void onLoadQuery(){
		deliverOrderBeanList= KitchenDeliverOrderDAO.loadKitchenDeliveredOrders(connection, userName);
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickFind(){
		if(stdate!=null && endate!=null){
			deliverOrderBeanList =KitchenDeliverOrderDAO.showKitchenKitchenDeliveredOrdersWithinRange(connection, userName, stdate, endate);
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
	
	@Command
	@NotifyChange("*")
	public void generateExcelSheet(){

		 File f = null;
	      boolean bool = false;
	    if(deliverOrderBeanList.size()>0){
	    	try{
		         // create new file
		    	 String realPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");
		  		
		  		String reportNamewithPath = realPath + "report.csv";
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
		            w.write("ORDER NO,ITEM,ORDER DATE,DELIVERY DATE,QUANTITY,"
		            		+ "PRICE \n");
		            for(int i=0;i<deliverOrderBeanList.size();i++){
		            	w.write(deliverOrderBeanList.get(i).orderNo
		            			+ ","+ deliverOrderBeanList.get(i).itemName
		            			+ ","+ deliverOrderBeanList.get(i).orderDateValue 
		            			+","+ deliverOrderBeanList.get(i).deliveryDateValue
		            			+","+deliverOrderBeanList.get(i).quantity
		            			+","+deliverOrderBeanList.get(i).price + "\n");
		            	/*w.write(deliverOrderBeanList.get(i).orderNo+","+deliverOrderBeanList.get(i).orderDateValue
		            			+","+orderDashBoardBeanList.get(i).orderStatus+","+orderDashBoardBeanList.get(i).orderBy
		            			+","+orderDashBoardBeanList.get(i).mealType+","+orderDashBoardBeanList.get(i).deliveryDateValue
		            			+","+orderDashBoardBeanList.get(i).contactNo+","+orderDashBoardBeanList.get(i).orderItem
		            			+","+orderDashBoardBeanList.get(i).itemCode
		            			+","+orderDashBoardBeanList.get(i).itemDescription
		            			+","+orderDashBoardBeanList.get(i).quantity
		            			+","+orderDashBoardBeanList.get(i).kitchenName+","+orderDashBoardBeanList.get(i).received
		            			+","+orderDashBoardBeanList.get(i).notified
		            			+","+orderDashBoardBeanList.get(i).rejected
		            			+","+orderDashBoardBeanList.get(i).picked
		            			+","+orderDashBoardBeanList.get(i).delivered
		            			+","+orderDashBoardBeanList.get(i).driverName+","+orderDashBoardBeanList.get(i).driverNumber+"\n");*/
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

		    			final AMedia amedia = new AMedia("report", "csv", "application/csv", bios.toByteArray());

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
	
	
	/**
	 * 
	 * Clearing data after update
	 */
	public void cleardata(){
		onLoadQuery();
		quantity = null;
		itemPrice = null;
		deliverOrderBean.orderStatus ="";
		deliverOrderBean.deliveryBoyName = "";
		deliverOrderBean.deliveryBoyId = null;
	}

	public ReceivedOrderBean getDeliverOrderBean() {
		return deliverOrderBean;
	}

	public void setDeliverOrderBean(ReceivedOrderBean deliverOrderBean) {
		this.deliverOrderBean = deliverOrderBean;
	}

	public ArrayList<ReceivedOrderBean> getDeliverOrderBeanList() {
		return deliverOrderBeanList;
	}

	public void setDeliverOrderBeanList(
			ArrayList<ReceivedOrderBean> deliverOrderBeanList) {
		this.deliverOrderBeanList = deliverOrderBeanList;
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
