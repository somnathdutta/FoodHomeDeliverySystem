package ViewModel;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.DeliveredOrderBean;
import Bean.ManageDeliveryBoyBean;

public class DeliveredOrderViewModel {

	private DeliveredOrderBean deliveredOrderBean = null;
	
	private ArrayList<DeliveredOrderBean> deliveredOrderBeanList = new ArrayList<DeliveredOrderBean>();
 	
	private ManageDeliveryBoyBean deliveryBoyBean = new ManageDeliveryBoyBean();
	
	private Integer quantity ;
	
	private Double itemPrice ;
	
	private boolean podfileuploaded = false;
	
	private String podImagefilePath;

	private AImage podImage;
	
	Session session = null;
	
	private Connection connection = null;
	
	@Wire("#winDeliverOrder")
	private Window winDeliverOrder;


	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,@ExecutionArgParam("parentObjectData")DeliveredOrderBean deliveredorderbean) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		connection.setAutoCommit(true);
	     deliveredOrderBean =  deliveredorderbean ;
		onLoadQuery();
		
	}
	
	public void reload(){
		for(int i=0;i<1;i++){
			deliveredOrderBean = new DeliveredOrderBean();
			deliveredOrderBeanList.add(deliveredOrderBean);
		}
	}
	/**
	 * 
	 * On load query to list of all orders which are received view (vw_received_order_list)
	 */
	public void onLoadQuery(){
		if(deliveredOrderBeanList.size()>0){
			deliveredOrderBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "SELECT * FROM vw_delivered_order_details where order_id=?";
				 try {
					 preparedStatement = connection.prepareStatement(sql);
					 preparedStatement.setInt(1, deliveredOrderBean.getOrderId());
					 resultSet = preparedStatement.executeQuery();
					 String itemName = null;
					 while(resultSet.next()){
						 DeliveredOrderBean deliveredorderbean = new DeliveredOrderBean();
						 deliveredorderbean.setOrderId(resultSet.getInt("order_id")); 
						 deliveredorderbean.setItemName(resultSet.getString("item_name")); 
						 String tepmItemName = deliveredorderbean.getItemName();
							if(deliveredorderbean.getItemName().equals(itemName)){
								deliveredorderbean.setItemName("");
							}
						 quantity = resultSet.getInt("qty");
						 itemPrice = resultSet.getDouble("final_price");
						 deliveredorderbean.setOrderStatus(resultSet.getString("order_status_name"));
						 deliveryBoyBean.deliveryBoyId = resultSet.getInt("delivery_boy_id");
						 deliveryBoyBean.name = resultSet.getString("delivery_boy_name");
						 deliveryBoyBean.phoneNo = resultSet.getString("delivery_boy_phn_number");
						 itemName=tepmItemName;
						 deliveredOrderBeanList.add(deliveredorderbean);
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
	}
	
	/**
	 * onUploadpodImage is used to upload pod
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onUploadPODImage(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx) throws IOException {

		UploadEvent upEvent = null;
		
		Object objUploadEvent = ctx.getTriggerEvent();
		
		if (objUploadEvent != null && (objUploadEvent instanceof UploadEvent)) {
			upEvent = (UploadEvent) objUploadEvent;
		}

		if (upEvent != null) {

			Media media = upEvent.getMedia();
			
			String yearPath = "Images_POD_Images" + "\\"+new SimpleDateFormat("dd-MMM-yyyy").format(new java.util.Date())+ "\\";

			podImagefilePath = "C:\\FooApp_Project_Images\\" + yearPath;
			File baseDir = new File(podImagefilePath);
			if (!baseDir.exists()) {
				baseDir.mkdirs();
			}

			Files.copy(new File(podImagefilePath + media.getName()),media.getStreamData());

			podfileuploaded = true;
			podImagefilePath = podImagefilePath + media.getName();
		
			deliveredOrderBean.setPodPicturePath(podImagefilePath); 

			podImage = (AImage) media;
		}
	}

	/**
	 * 
	 * Method to save POD
	 */
	@Command
	@NotifyChange("*")
	public void onClickSavePOD(){
		if(validateFields()){
			Boolean updated = false;
			try {
				SQL:{	
					 PreparedStatement preparedStatement = null;
					 ResultSet resultSet = null;
					// String sqlQuery = "UPDATE fapp_orders SET order_status_id = 8,delivery_date_time=?,proof_of_delivery=? WHERE order_id=?";
					 String sqlQuery = "SELECT func_free_delivery_boy(8,?,?,?,'N',?)";
					try {
						preparedStatement = connection.prepareStatement(sqlQuery);
						preparedStatement.setString(1, deliveredOrderBean.getUploadingProofdate().toString());
						preparedStatement.setString(2, deliveredOrderBean.getPodPicturePath());
						preparedStatement.setInt(3, deliveredOrderBean.getOrderId());
						preparedStatement.setInt(4, deliveryBoyBean.deliveryBoyId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							updated = true;
						}
						if(updated){
							Messagebox.show("Order sucessfully completed by "+deliveryBoyBean.name);
							BindUtils.postGlobalCommand(null, null, "globalReload", null);
							winDeliverOrder.detach();
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
			
		}
	}
	
	/**
	 * 
	 * Validating empty fields
	 */
	public Boolean validateFields(){
		if(deliveredOrderBean.getPodPicturePath()!=null){
			if(deliveredOrderBean.getUploadingProofdate()!=null){
				return true;
			}else{
				Messagebox.show("Proof of delivery date required!");
				return false;
			}
		}else{
			Messagebox.show("Proof of delivery required!");
			return false;
		}
	}
	
	/**
	 * 
	 * clearing data and on load query
	 */
	public void clearData(){	
		onLoadQuery(); 
		reload();
		quantity = null;
		 itemPrice = null;
		 deliveredOrderBean.setOrderStatus("");
		 deliveryBoyBean.name = "";
		 deliveryBoyBean.phoneNo = "";
		 podImage =null;
		 deliveredOrderBean.setUploadingProofdate(null);
	}
	
	public DeliveredOrderBean getDeliveredOrderBean() {
		return deliveredOrderBean;
	}

	public void setDeliveredOrderBean(DeliveredOrderBean deliveredOrderBean) {
		this.deliveredOrderBean = deliveredOrderBean;
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

	public ManageDeliveryBoyBean getDeliveryBoyBean() {
		return deliveryBoyBean;
	}

	public void setDeliveryBoyBean(ManageDeliveryBoyBean deliveryBoyBean) {
		this.deliveryBoyBean = deliveryBoyBean;
	}

	public ArrayList<DeliveredOrderBean> getDeliveredOrderBeanList() {
		return deliveredOrderBeanList;
	}

	public void setDeliveredOrderBeanList(
			ArrayList<DeliveredOrderBean> deliveredOrderBeanList) {
		this.deliveredOrderBeanList = deliveredOrderBeanList;
	}

	public AImage getPodImage() {
		return podImage;
	}

	public void setPodImage(AImage podImage) {
		this.podImage = podImage;
	}

	public boolean isPodfileuploaded() {
		return podfileuploaded;
	}

	public void setPodfileuploaded(boolean podfileuploaded) {
		this.podfileuploaded = podfileuploaded;
	}

	public String getPodImagefilePath() {
		return podImagefilePath;
	}

	public void setPodImagefilePath(String podImagefilePath) {
		this.podImagefilePath = podImagefilePath;
	}
}
