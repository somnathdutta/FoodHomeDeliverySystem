package ViewModel;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.ManageCMSBean;
import Bean.ManageCouponsBean;


public class ManageCouponsViewModel {
	
	private ManageCouponsBean manageCouponsBean = new ManageCouponsBean();
	
	private ArrayList<ManageCouponsBean> manageCouponsBeanList = new ArrayList<ManageCouponsBean>();
	Session session = null;
	
	private Connection connection = null;
	
	private String userName = "";
	
	private ArrayList<String> discountTypeList= new ArrayList<String>();
	
	private Boolean saveButtonVisibility = true;
	
	private Boolean updateButtonVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetUp(@ContextParam(ContextType.VIEW) Component view)throws Exception{
		
		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		connection.setAutoCommit(true);
		
		onLoadDiscountTypeList();
		
		onLoadCouponsList();
	}
	
	/**
	 * Load all saved coupons
	 */
	public void onLoadCouponsList(){
		if(manageCouponsBeanList.size()>0){
			manageCouponsBeanList.clear();
		}
		try {
			SQL:{	
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String show="";
				try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("onLoadCouponListSql"));
						resultSet = preparedStatement.executeQuery();
						
						while(resultSet.next()){
							ManageCouponsBean manageCouponsBean = new ManageCouponsBean();
							manageCouponsBean.couponId = resultSet.getInt("coupon_id");
							manageCouponsBean.couponCode = resultSet.getString("coupon_code");
							manageCouponsBean.discountAmount = resultSet.getDouble("discount_amount");
							show = resultSet.getString("is_active");
							if(show.equals("N")){
								manageCouponsBean.status = "Inactive";
							}
							else{
								manageCouponsBean.status = "Active";
							}
							
							manageCouponsBeanList.add(manageCouponsBean);
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
	
	
	/**
	 * 
	 * GlobalReload is used to reload the grid when child window is detach
	 */
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		onLoadCouponsList();
		clearData();
		updateButtonVisibility = false;
		saveButtonVisibility = true;
	}
	
	/**
	 * Load discount types
	 */
	public void onLoadDiscountTypeList(){
		
		if(discountTypeList.size()>0){
			discountTypeList.clear();
		}
		
		try {
				sql1:{
						PreparedStatement preparedStatement=null;
						try {
								preparedStatement=connection.prepareStatement(propertyfile.getPropValues("onLoadDiscountTypeListSql"));
								
								ResultSet rs=preparedStatement.executeQuery();
								
								while(rs.next()){
							
									discountTypeList.add(rs.getString(1));
									
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
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * Selecting discount type
	 */
	@Command
	@NotifyChange("*")
	public void onSelectDiscountType(){
		try {
			sql1:{
					PreparedStatement preparedStatement=null;
					try {
							preparedStatement=connection.prepareStatement(propertyfile.getPropValues("discountTypeIdWRTdiscountNameSql"));
							preparedStatement.setString(1, manageCouponsBean.discountType);
							ResultSet resultSet=preparedStatement.executeQuery();
							
							while(resultSet.next()){
								manageCouponsBean.discountTypeId = resultSet.getInt("discount_type_id");								
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
			e.printStackTrace();
			}
	}
	
	/**
	 * 
	 * on click save button
	 */
	@Command
	@NotifyChange("*")
	public void onClickSaveCoupon(){
		
		if(isFieldNotNull()){
			saveCouponData();
		}
	}
	
	/**
	 * save coupon data
	 * 
	 */
	public void saveCouponData(){
		String message="";
		Boolean inserted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
							
				try {
						preparedStatement =  connection.prepareStatement(propertyfile.getPropValues("saveCouponSql"));
						
						preparedStatement.setString(1, manageCouponsBean.couponCode);
						preparedStatement.setInt(2, manageCouponsBean.usesPerUser);
						preparedStatement.setInt(3, manageCouponsBean.discountTypeId);
						preparedStatement.setDouble(4, manageCouponsBean.discountAmount);
						preparedStatement.setDate(5, new Date(manageCouponsBean.fromDate.getTime()));
						preparedStatement.setDate(6, new Date(manageCouponsBean.toDate.getTime()));
						if(manageCouponsBean.isActiveChecked){
							preparedStatement.setString(7, "Y");
						}
						else{
							preparedStatement.setString(7, "N");
						}
						preparedStatement.setString(8, userName);
						preparedStatement.setString(9, userName);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							inserted = true;
							System.out.println("Coupon saved "+message);
						}
						if(inserted){
							Messagebox.show("Coupon saved sucessfully!!");
							clearData();
							onLoadCouponsList();
						}
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	 * 
	 * clear data 
	 */
	public void clearData(){
		manageCouponsBean.couponCode="";
		manageCouponsBean.usesPerUser=null;
		manageCouponsBean.discountType="";
		manageCouponsBean.discountAmount=null;
		manageCouponsBean.fromDate=null;
		manageCouponsBean.toDate= null;
		manageCouponsBean.isActiveChecked=false;
	}
	
	/**
	 *  
	 * Edit coupon data
	 */
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean")ManageCouponsBean manageCouponsbean){
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet =null;
				try {
					preparedStatement = connection.prepareStatement(propertyfile.getPropValues("editCouponSql"));
					preparedStatement.setInt(1,manageCouponsbean.couponId);
					manageCouponsBean.couponId = manageCouponsbean.couponId;
					resultSet = preparedStatement.executeQuery();
					while(resultSet.next()){
						
						manageCouponsBean.couponCode = resultSet.getString("coupon_code");
						manageCouponsBean.usesPerUser = resultSet.getInt("uses_per_user");
						manageCouponsBean.discountAmount = resultSet.getDouble("discount_amount");
						manageCouponsBean.fromDate = resultSet.getDate("from_date");
						manageCouponsBean.toDate = resultSet.getDate("to_date");
						manageCouponsBean.discountType = resultSet.getString("discount_type");
						manageCouponsBean.discountTypeId = resultSet.getInt("discount_type_id");
						updateButtonVisibility = true;
						saveButtonVisibility = false;
					}	
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
				}				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 
	 * on click update 
	 */
	@Command
	@NotifyChange("*")
	public void onClickUpdateCoupon(){
		if(isFieldNotNull()){
			updateCouponData();
		}
	}
	
	/**
	 * 
	 * Updating coupon data
	 */
	public void updateCouponData(){
		String message="";
		Boolean inserted = false;
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				try {
					preparedStatement = connection.prepareStatement(propertyfile.getPropValues("updateCouponSql"));
					preparedStatement.setString(1, manageCouponsBean.couponCode);
					preparedStatement.setInt(2, manageCouponsBean.usesPerUser);
					preparedStatement.setInt(3, manageCouponsBean.discountTypeId);
					preparedStatement.setDouble(4, manageCouponsBean.discountAmount);
					preparedStatement.setDate(5, new Date(manageCouponsBean.fromDate.getTime()));
					preparedStatement.setDate(6, new Date(manageCouponsBean.toDate.getTime()));
					if(manageCouponsBean.isActiveChecked){
						preparedStatement.setString(7, "Y");
					}
					else{
						preparedStatement.setString(7, "N");
					}
					preparedStatement.setString(8, userName);
					preparedStatement.setString(9, userName);
					preparedStatement.setInt(10, manageCouponsBean.couponId);
					resultSet = preparedStatement.executeQuery();
					if(resultSet.next()){
						message=resultSet.getString(1);
						inserted = true;
						System.out.println("Coupons Data Details"+message);
					}
					if(inserted){
						Messagebox.show("Coupons data Updated Successfully...");
						clearData();
						onLoadCouponsList();
					}
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	}
	
	/**
	 * 
	 * Delete confirmation box
	 */
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean")ManageCouponsBean manageCouponsbean){
		
		Map<String, Object> parentMap = new HashMap<String, Object>();
		
		parentMap.put("parentCMSObject", manageCouponsbean);
		
		Window win = (Window) Executions.createComponents("couponDeleteConfirmMessageBox.zul", null, parentMap);
		
		win.doModal();
	}
	
	/**
	 * 
	 * validating fields
	 */
	public Boolean isFieldNotNull(){
		
		if(manageCouponsBean.couponCode != null && manageCouponsBean.couponCode.length()>0){
			
			if(manageCouponsBean.usesPerUser != null){
				
				if(manageCouponsBean.discountAmount != null){
					
					if(manageCouponsBean.discountType != null && manageCouponsBean.discountType.length()>0){
						
						if(manageCouponsBean.fromDate!= null){
							
							if(manageCouponsBean.toDate!=null){
								
								return true;
							}else{
								
								Messagebox.show("To Date Cannot Be Null..", "Information", Messagebox.OK, Messagebox.INFORMATION);
								
								return false;
								
							}
						}else{
							
							Messagebox.show("From Date Cannot Be Null..", "Information", Messagebox.OK, Messagebox.INFORMATION);
							
							return false;
							
						}
					}else{
							
							Messagebox.show("Discount type Cannot Be Null..", "Information", Messagebox.OK, Messagebox.INFORMATION);
							
							return false;
							
						}
				}else{
					Messagebox.show("Discount amount Cannot Be Null..", "Information", Messagebox.OK, Messagebox.INFORMATION);
					
					return false;
					
				}
				
			}else{
				Messagebox.show("Uses per user Cannot Be Null..", "Information", Messagebox.OK, Messagebox.INFORMATION);
				
				return false;
			}
						
		}else{
			Messagebox.show("Coupon Code Cannot Be Null..", "Information", Messagebox.OK, Messagebox.INFORMATION);
			
			return false;
		}
	}
	
	public ManageCouponsBean getManageCouponsBean() {
		return manageCouponsBean;
	}

	public void setManageCouponsBean(ManageCouponsBean manageCouponsBean) {
		this.manageCouponsBean = manageCouponsBean;
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


	public ArrayList<String> getDiscountTypeList() {
		return discountTypeList;
	}


	public void setDiscountTypeList(ArrayList<String> discountTypeList) {
		this.discountTypeList = discountTypeList;
	}

	public ArrayList<ManageCouponsBean> getManageCouponsBeanList() {
		return manageCouponsBeanList;
	}

	public void setManageCouponsBeanList(
			ArrayList<ManageCouponsBean> manageCouponsBeanList) {
		this.manageCouponsBeanList = manageCouponsBeanList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Boolean getSaveButtonVisibility() {
		return saveButtonVisibility;
	}

	public void setSaveButtonVisibility(Boolean saveButtonVisibility) {
		this.saveButtonVisibility = saveButtonVisibility;
	}

	public Boolean getUpdateButtonVisibility() {
		return updateButtonVisibility;
	}

	public void setUpdateButtonVisibility(Boolean updateButtonVisibility) {
		this.updateButtonVisibility = updateButtonVisibility;
	}
	
}
