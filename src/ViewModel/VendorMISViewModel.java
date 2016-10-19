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
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zhtml.Pre;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import pdfHandler.VendorPerformanceXLS;
import Bean.VendorMisBean;

public class VendorMISViewModel {
	
	Session session = null;
	
	private Connection connection = null;
	
	private Integer roleId = 0;
	
	private String userName ;
	
	private String kitchenName = null;
	
	private VendorMisBean vendorMisBean = new VendorMisBean();
	
	private ArrayList<VendorMisBean> vendorMisBeanList = new ArrayList<VendorMisBean>();
	
	private ArrayList<String> kitchenNameList = new ArrayList<String>();
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		roleId = (Integer) session.getAttribute("userRoleId");
		
		getvendorList();
	//	onLoadQuery();
		for(int i=0;i<1;i++)
			vendorMisBeanList.add(vendorMisBean);
	}

	public void onLoadQuery() throws ParseException{
		if(vendorMisBeanList.size()>0){
			vendorMisBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					/*String sql = "SELECT distinct kitchen_name,order_no,order_assign_date,received_time,notified_time "
							+ " from vw_received_order_details";*/
					String sql = "SELECT distinct kitchen_name,order_no,order_date,order_assign_date,received_time,notified_time, "
					  +" (notified_time - order_assign_date)::text AS delay_in_delivery, "
					  +"(received_time- order_assign_date)::text AS delay_in_receive "
												+"  from vw_received_order_details";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							VendorMisBean bean = new VendorMisBean();
							bean.kitchenName = resultSet.getString("kitchen_name");
							bean.orderNo = resultSet.getString("order_no");
							bean.orderAssignTime = resultSet.getTimestamp("order_assign_date");
							DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
					        bean.orderAssignTimeValue = dateFormat.format(bean.orderAssignTime);
					       
						    bean.acceptanceTime = resultSet.getTimestamp("received_time");
						    bean.acceptanceTimeValue = dateFormat.format(bean.acceptanceTime);
						    
							bean.notifyTime = resultSet.getTimestamp("notified_time");
							bean.notifyTimeValue = dateFormat.format(bean.notifyTime);
							bean.delayInReceive = resultSet.getString("delay_in_receive");
							bean.delayInDelivery = resultSet.getString("delay_in_delivery");
							
							vendorMisBeanList.add(bean);
						}
					} catch (Exception e) {
						// TODO: handle exception
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
	/*
	@Command
	@NotifyChange("*")
	public void onClickAdd(){
		vendorMisBeanList.add(new VendorMisBean());
	}*/
	
	
	private void getvendorList(){
		/*if(kitchenNameList.size()>0){
			kitchenNameList.clear();
		}*/
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "SELECT kitchen_name FROM fapp_kitchen WHERE is_active='Y' AND is_delete='N'";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					kitchenNameList.add(resultSet.getString("kitchen_name"));
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error due to:"+e.getMessage());
			}finally{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectKitchenName(@BindingParam("string")String kitchenname){
		kitchenName = kitchenname;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickFind(){
		
		if(kitchenName!=null){
			if(vendorMisBeanList.size()>0){
				vendorMisBeanList.clear();
			}
			try {
				SQL:{
						PreparedStatement preparedStatement = null;
						ResultSet resultSet = null;
						/*String sql = "SELECT distinct kitchen_name, order_no,order_assign_date,received_time,notified_time "
								+ " from vw_received_order_details where kitchen_name=?";*/
						String sql = "SELECT distinct kitchen_name,order_no,order_assign_date,received_time,notified_time, "
								  +" (notified_time - order_assign_date)::text AS delay_in_delivery, "
								  +"(received_time- order_assign_date)::text AS delay_in_receive "
															+"  from vw_received_order_details where kitchen_name=?";
						try {
							preparedStatement = connection.prepareStatement(sql);
							preparedStatement.setString(1, kitchenName);
							resultSet = preparedStatement.executeQuery();
							while (resultSet.next()) {
								VendorMisBean bean = new VendorMisBean();
								bean.kitchenName = resultSet.getString("kitchen_name");
								bean.orderNo = resultSet.getString("order_no");
								bean.orderAssignTime = resultSet.getTimestamp("order_assign_date");
								DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
						        bean.orderAssignTimeValue = dateFormat.format(bean.orderAssignTime);
						       
							    bean.acceptanceTime = resultSet.getTimestamp("received_time");
							    bean.acceptanceTimeValue = dateFormat.format(bean.acceptanceTime);
							    
								bean.notifyTime = resultSet.getTimestamp("notified_time");
								bean.notifyTimeValue = dateFormat.format(bean.notifyTime);
								bean.delayInReceive = resultSet.getString("delay_in_receive");
								bean.delayInDelivery = resultSet.getString("delay_in_delivery");
								
								vendorMisBeanList.add(bean);
							}
						} catch (Exception e) {
							// TODO: handle exception
							Messagebox.show("ERROR Due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
			
			if(vendorMisBeanList.size()==0){
				Messagebox.show("No Data found regarding vendor "+kitchenName+"!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
			}
		}else{
			Messagebox.show("Vendor name required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickClear() throws ParseException{
		kitchenName = null;
		onLoadQuery();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickGenerate(){
		if(kitchenName!=null ){
			new VendorPerformanceXLS().addExcelHeader(vendorMisBeanList);
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

	public ArrayList<String> getKitchenNameList() {
		return kitchenNameList;
	}

	public void setKitchenNameList(ArrayList<String> kitchenNameList) {
		this.kitchenNameList = kitchenNameList;
	}

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}


	public String getKitchenName() {
		return kitchenName;
	}


	public void setKitchenName(String kitchenName) {
		this.kitchenName = kitchenName;
	}

	public VendorMisBean getVendorMisBean() {
		return vendorMisBean;
	}

	public void setVendorMisBean(VendorMisBean vendorMisBean) {
		this.vendorMisBean = vendorMisBean;
	}

	public ArrayList<VendorMisBean> getVendorMisBeanList() {
		return vendorMisBeanList;
	}

	public void setVendorMisBeanList(ArrayList<VendorMisBean> vendorMisBeanList) {
		this.vendorMisBeanList = vendorMisBeanList;
	}
	
	
}
