package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Bean.NewOrderBean;
import Bean.SubscriptionBean;

public class SubscribedOrdersViewModel {

	private Connection connection = null;
	
	Session session = null;
	
	private String username;
	
	private Integer roleId;
	
	private SubscriptionBean subscriptionBean = new SubscriptionBean();
	
	private ArrayList<SubscriptionBean> subscriptionBeanList = new ArrayList<SubscriptionBean>();
	
	private ArrayList<SubscriptionBean> subscriptionOrderBeanList = new ArrayList<SubscriptionBean>();
	
	PropertyFile propertyfile = new PropertyFile();
	
	private String kitchenName ;
	
	private ArrayList<String> kitchenNameList = new ArrayList<String>();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view,
			@ExecutionArgParam("parentOrderObject")SubscriptionBean subscriptionbean) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		username = (String) session.getAttribute("login");
		roleId = (Integer) session.getAttribute("userRoleId");
		connection.setAutoCommit(true);
		subscriptionBean = subscriptionbean;
	
		if(roleId==1){
			loadSubscribedOrders();
		//	onLoadKitchenList();
		}else{
			loadSubscribedOrdersForKitchen();
		}
		
	
		
	}
	
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload1(){
		loadSubscribedOrders();
	}
	
	private void loadSubscribedOrders(){
		if(subscriptionBeanList.size()>0){
			subscriptionBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
				//	String sql = "select * from vw_subscribed_orders where subscription_id=? and quantity <> 0 order by day_name";
					String sql = "select * from vw_subscribed_order_list where subscription_id=? and quantity <> 0 order by day_name";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, subscriptionBean.subscriptionId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							SubscriptionBean bean = new SubscriptionBean();
							bean.day = resultSet.getString("day_name");
							bean.mealType = resultSet.getString("meal_type");
							bean.cuisineName = resultSet.getString("cuisine_name");
							bean.categoryName = resultSet.getString("category_name");
							bean.price = resultSet.getDouble("meal_price");
							bean.quantity = resultSet.getInt("quantity");
							bean.totalPrice = bean.price * bean.quantity;
							bean.subscriptionMealDetailId = resultSet.getInt("subscription_meal_detail_id");
							bean.kitchenName = resultSet.getString("kitchen_name");
							subscriptionBeanList.add(bean);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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

	private void loadSubscribedOrdersForKitchen(){
		if(subscriptionOrderBeanList.size()>0){
			subscriptionOrderBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					//String sql = "select * from vw_subscribed_orders where kitchen_name = ? and quantity <> 0 order by subscription_no";
				//	String sql = "select * from vw_subscribed_order_list where kitchen_name = ? and quantity <> 0 order by subscription_no";
					
					try {
						preparedStatement = connection.prepareStatement(new PropertyFile().getPropValues("loadSubscribedOrdersForKitchen"));
						
						preparedStatement.setString(1, username);
						System.out.println("User:: "+preparedStatement);
						resultSet = preparedStatement.executeQuery();
						String subno = null;
						while (resultSet.next()) {
							SubscriptionBean bean = new SubscriptionBean();
							bean.subscriptionNo = resultSet.getString("subscription_no");
							bean.subscriptionId = resultSet.getInt("subscription_id");
							bean.day = resultSet.getString("day_name");
							bean.mealType = resultSet.getString("meal_type");
							bean.cuisineName = resultSet.getString("cuisine_name");
							bean.categoryName = resultSet.getString("category_name");
							bean.price = resultSet.getDouble("meal_price");
							bean.quantity = resultSet.getInt("quantity");
							bean.totalPrice = bean.price * bean.quantity;
							bean.startDate = resultSet.getDate("start_date");
							SimpleDateFormat simpDate = new SimpleDateFormat("dd-MM-yyyy");
							bean.startDateValue = simpDate.format(bean.startDate);
							bean.endDate = resultSet.getDate("end_date");
							bean.endDateValue =simpDate.format(bean.endDate);
							bean.kitchenName = resultSet.getString("kitchen_name");
							String tempsubno = bean.subscriptionNo;
							if(bean.subscriptionNo.equals(subno)){
								bean.subscriptionNo = null;
								bean.startDateValue = null;
								bean.endDateValue= null;
								bean.detailsVisibility = false;
							}
							subno =  tempsubno;
							subscriptionOrderBeanList.add(bean);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	
	
	private void onLoadKitchenList(){
		if(kitchenNameList.size()>0)
			kitchenNameList.clear();
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet  resultSet = null;
					String sql = "SELECT kitchen_name FROM fapp_kitchen WHERE is_delete = 'N' AND is_active='Y'"
							+ " AND area_id = ?";
					
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, subscriptionBean.areaId);
						resultSet =  preparedStatement.executeQuery();
						while (resultSet.next()) {
							kitchenNameList.add(resultSet.getString(1));
						}
						
					}catch (Exception e) {
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Command
	@NotifyChange("*")
	public void onClickAssignKitchen(@BindingParam("bean") final SubscriptionBean bean){
		if(bean.kitchenName!=null){
			Messagebox.show("Are you sure to assign?", "Confirm Dialog", 
					Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
			    public void onEvent(Event evt) throws InterruptedException {
			        if (evt.getName().equals("onOK")) {
			          update(bean);
			          BindUtils.postGlobalCommand(null, null, "globalReload1", null);
			        } else if (evt.getName().equals("onIgnore")) {
			            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
			        } else {
			           
			        }
			    }
			});
		}else{
			Messagebox.show("Kitchen name required!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	private void update(SubscriptionBean bean){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_subscription_meals_details SET kitchen_id = "
							+ " (SELECT kitchen_id from fapp_kitchen WHERE kitchen_name=?) WHERE subscription_meal_detail_id= ?";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, bean.kitchenName);
						preparedStatement.setInt(2, bean.subscriptionMealDetailId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							Messagebox.show("Kitchen Assigned successfully!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						Messagebox.show("Error due to:"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	public void onClickDetails(@BindingParam("bean")SubscriptionBean subsorderBean) throws Exception{
		
		if(subsorderBean.subscriptionId!=null){	
			
			Map<String, Object> parentMap = new HashMap<String, Object>();
			
			parentMap.put("parentSubscriptionOrderObject", subsorderBean);
			
			Window win = (Window) Executions.createComponents("showDetails.zul", null, parentMap);
			
			win.doModal();
			
		}else{
			Messagebox.show("Data not available!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
		}
	}
	
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
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

	public SubscriptionBean getSubscriptionBean() {
		return subscriptionBean;
	}

	public void setSubscriptionBean(SubscriptionBean subscriptionBean) {
		this.subscriptionBean = subscriptionBean;
	}

	public ArrayList<SubscriptionBean> getSubscriptionBeanList() {
		return subscriptionBeanList;
	}

	public void setSubscriptionBeanList(
			ArrayList<SubscriptionBean> subscriptionBeanList) {
		this.subscriptionBeanList = subscriptionBeanList;
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

	public ArrayList<String> getKitchenNameList() {
		return kitchenNameList;
	}

	public void setKitchenNameList(ArrayList<String> kitchenNameList) {
		this.kitchenNameList = kitchenNameList;
	}

	public ArrayList<SubscriptionBean> getSubscriptionOrderBeanList() {
		return subscriptionOrderBeanList;
	}

	public void setSubscriptionOrderBeanList(
			ArrayList<SubscriptionBean> subscriptionOrderBeanList) {
		this.subscriptionOrderBeanList = subscriptionOrderBeanList;
	}
}
