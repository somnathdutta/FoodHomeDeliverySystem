package ViewModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import Database.DataBaseHandler;


public class HomeTabIndexViewModel {
	
	Session session = null;
	
	private Connection connection = null;
	
	private String username;
	
	private Integer roleId;
	
	private String roleName;
	
	public Boolean adminSettingsVisibility = false;
	
	public Boolean manageVisibility = false;
	
	public Boolean manageServiceAreaVisibility = false;
	
	public Boolean managcategoryVisibility = false;
	
	public Boolean managecategoryitemsVisibility = false;
	
	public Boolean managesubitemsVisibility = false;
	
	public Boolean managealacarteitemsVisibility = false;
	
	public Boolean managekitchensVisibility = false;
	
	public Boolean managelogisticsVisibility = false;
	
	public Boolean managetaxesVisibility = false;
	
	public Boolean managecmsVisibility = false;
	
	public Boolean managecouponsVisibility = false;
	
	public Boolean managedealsVisibility = false;
	
	public Boolean managecuisineVisibility = false;
	
	public Boolean manageDeliveryBoyVisibility = false;
	
	public Boolean orderManagementVisibility = false;
	
	public Boolean receivedOrdersVisibility = false;
	
	public Boolean reportsVisibility = false;
	
	public Boolean suscriptionVisibility = false;
	
	public Boolean subscriptionListVisibility = false;
	
	public Boolean stockManagementVisibility = false;
	
	public Boolean newOrderVisibility = false;
	
	public Boolean assignOrderlogisticsVisibility = false;
	
	public Boolean subscriptionOrderVisibility = false;
	
	public Boolean receiveOrderVisibility = false;
	
	public Boolean assignVisibility = false;
	
	public Boolean outForDeliveryVisibility = false;
	
	public Boolean deliveredorderVisibility = false;
	
	public Boolean completedOrderVisibility = false;
	
	public Boolean orderDashboardVisibility = false;
	
	public Boolean kitchenGroupVisibility = false;
	
	public Boolean bikerManageVisibility = false;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view){

		Selectors.wireComponents(view, this, false);
		session = Sessions.getCurrent();
		if (session.getAttribute("login") != null) {
			username = "Welcome: " + session.getAttribute("login");
			roleId = (Integer) session.getAttribute("userRoleId");
			
			if(roleId==1){
				roleName = "Admin Menus";
				showAdminNodes();
			}else if(roleId==2){
				roleName = "Kitchen admin Menus";
				showKitchenNodes();
			}else{
				roleName = "Logistics admin Menus";
				showLogisticsNodes();
			}
			
		}else{	
			Executions.sendRedirect("/index.zul");
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickChangePassword(){
		/*Map<String, Connection> connectionMap = new HashMap<String, Connection>();
		connectionMap.put("connectionParent", connection);
		Window window = (Window) Executions.createComponents("forGotPassWord.zul", null, connectionMap);
		window.doModal();*/
		Map<String, String> userMap = new HashMap<String, String>();
		userMap.put("username", username);
		Window window = (Window) Executions.createComponents("forGotPassWord.zul", null, userMap);
		window.doModal();
	}
	
	
	
	@Command
	@NotifyChange("*")
	public void onClickSignOut(){
			
		if(session != null){
			System.out.println("Removing session attribute login. . .");
			session.removeAttribute("login");
			
			Connection connection = (Connection) session.getAttribute("sessionConnection");
			System.out.println("Getting session connection login. . .");
			
			try {
				System.out.println("Closing connection session. . . ");
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Exception on sign out button. . . ");
				e.printStackTrace();
			}
			System.out.println("Removing sessionConnection. . . ");
			session.removeAttribute("sessionConnection");
			
			Messagebox.show("Logged out!", "Information", Messagebox.OK, Messagebox.INFORMATION);
			
			Executions.sendRedirect("/index.zul");
		}
		
	}
	
	public void showAdminNodes(){
		
			orderDashboardVisibility = true;
		
			adminSettingsVisibility = true;
			
			manageVisibility = true;
			
			managcategoryVisibility = true;
			
			managealacarteitemsVisibility = true;
			
			managecategoryitemsVisibility = true;
			
			managecmsVisibility = true;
			
			managecouponsVisibility = true;
			
			managecuisineVisibility = true;
			
			managedealsVisibility = true;
			
			manageDeliveryBoyVisibility = true;
			
			managekitchensVisibility = true;
			
			managelogisticsVisibility = true;
			
			manageServiceAreaVisibility = true;
			
			managesubitemsVisibility = true;
			
			managetaxesVisibility = true;
			
			orderManagementVisibility = true;
			
			reportsVisibility = true;
			
			stockManagementVisibility =  true;
			
			newOrderVisibility = true;
			
			suscriptionVisibility = true;
			
			subscriptionListVisibility = true;
			
			assignOrderlogisticsVisibility = true;
			
			receiveOrderVisibility = true;
			
			assignVisibility = true;
			
			outForDeliveryVisibility = true;
				
			//deliveredorderVisibility = true;
				
		    completedOrderVisibility = true;
		    
		    kitchenGroupVisibility = true;
		    
		    bikerManageVisibility = true;
		
	}
	
	public void showKitchenNodes(){
	
			//manageVisibility = true;
			
			//managelogisticsVisibility = true;
		
			manageVisibility = true;
		
			manageDeliveryBoyVisibility = true;
		
			stockManagementVisibility = true;
		
			orderManagementVisibility = true;
						
			newOrderVisibility = true;
			
			assignOrderlogisticsVisibility = true;
			
			receivedOrdersVisibility = true;
			
			subscriptionOrderVisibility = true;
		
	}
	
	public void showLogisticsNodes(){
		
			/*manageVisibility = true;
		
		    manageDeliveryBoyVisibility = true;*/
		
			orderManagementVisibility = true;
			
			//assignOrderlogisticsVisibility = true;
			
			receiveOrderVisibility = true;
			
			assignVisibility = true;
			
			outForDeliveryVisibility = true;
				
			//deliveredorderVisibility = true;
		
			completedOrderVisibility = true;
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
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public Boolean getAdminSettingsVisibility() {
		return adminSettingsVisibility;
	}
	public void setAdminSettingsVisibility(Boolean adminSettingsVisibility) {
		this.adminSettingsVisibility = adminSettingsVisibility;
	}
	public Boolean getManageVisibility() {
		return manageVisibility;
	}
	public void setManageVisibility(Boolean manageVisibility) {
		this.manageVisibility = manageVisibility;
	}
	public Boolean getOrderManagementVisibility() {
		return orderManagementVisibility;
	}
	public void setOrderManagementVisibility(Boolean orderManagementVisibility) {
		this.orderManagementVisibility = orderManagementVisibility;
	}
	public Boolean getReportsVisibility() {
		return reportsVisibility;
	}
	public void setReportsVisibility(Boolean reportsVisibility) {
		this.reportsVisibility = reportsVisibility;
	}
	public Boolean getNewOrderVisibility() {
		return newOrderVisibility;
	}
	public void setNewOrderVisibility(Boolean newOrderVisibility) {
		this.newOrderVisibility = newOrderVisibility;
	}
	public Boolean getReceiveOrderVisibility() {
		return receiveOrderVisibility;
	}
	public void setReceiveOrderVisibility(Boolean receiveOrderVisibility) {
		this.receiveOrderVisibility = receiveOrderVisibility;
	}
	public Boolean getAssignVisibility() {
		return assignVisibility;
	}
	public void setAssignVisibility(Boolean assignVisibility) {
		this.assignVisibility = assignVisibility;
	}
	public Boolean getOutForDeliveryVisibility() {
		return outForDeliveryVisibility;
	}
	public void setOutForDeliveryVisibility(Boolean outForDeliveryVisibility) {
		this.outForDeliveryVisibility = outForDeliveryVisibility;
	}
	public Boolean getDeliveredorderVisibility() {
		return deliveredorderVisibility;
	}
	public void setDeliveredorderVisibility(Boolean deliveredorderVisibility) {
		this.deliveredorderVisibility = deliveredorderVisibility;
	}
	public Boolean getCompletedOrderVisibility() {
		return completedOrderVisibility;
	}
	public void setCompletedOrderVisibility(Boolean completedOrderVisibility) {
		this.completedOrderVisibility = completedOrderVisibility;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Boolean getAssignOrderlogisticsVisibility() {
		return assignOrderlogisticsVisibility;
	}
	public void setAssignOrderlogisticsVisibility(
			Boolean assignOrderlogisticsVisibility) {
		this.assignOrderlogisticsVisibility = assignOrderlogisticsVisibility;
	}
	public Boolean getStockManagementVisibility() {
		return stockManagementVisibility;
	}
	public void setStockManagementVisibility(Boolean stockManagementVisibility) {
		this.stockManagementVisibility = stockManagementVisibility;
	}
	public Boolean getManageDeliveryBoyVisibility() {
		return manageDeliveryBoyVisibility;
	}
	public void setManageDeliveryBoyVisibility(Boolean manageDeliveryBoyVisibility) {
		this.manageDeliveryBoyVisibility = manageDeliveryBoyVisibility;
	}
	public Boolean getManageServiceAreaVisibility() {
		return manageServiceAreaVisibility;
	}
	public void setManageServiceAreaVisibility(Boolean manageServiceAreaVisibility) {
		this.manageServiceAreaVisibility = manageServiceAreaVisibility;
	}
	public Boolean getManagcategoryVisibility() {
		return managcategoryVisibility;
	}
	public void setManagcategoryVisibility(Boolean managcategoryVisibility) {
		this.managcategoryVisibility = managcategoryVisibility;
	}
	public Boolean getManagecategoryitemsVisibility() {
		return managecategoryitemsVisibility;
	}
	public void setManagecategoryitemsVisibility(
			Boolean managecategoryitemsVisibility) {
		this.managecategoryitemsVisibility = managecategoryitemsVisibility;
	}
	public Boolean getManagekitchensVisibility() {
		return managekitchensVisibility;
	}
	public void setManagekitchensVisibility(Boolean managekitchensVisibility) {
		this.managekitchensVisibility = managekitchensVisibility;
	}
	public Boolean getManagelogisticsVisibility() {
		return managelogisticsVisibility;
	}
	public void setManagelogisticsVisibility(Boolean managelogisticsVisibility) {
		this.managelogisticsVisibility = managelogisticsVisibility;
	}
	public Boolean getManagecmsVisibility() {
		return managecmsVisibility;
	}
	public void setManagecmsVisibility(Boolean managecmsVisibility) {
		this.managecmsVisibility = managecmsVisibility;
	}
	public Boolean getManagecouponsVisibility() {
		return managecouponsVisibility;
	}
	public void setManagecouponsVisibility(Boolean managecouponsVisibility) {
		this.managecouponsVisibility = managecouponsVisibility;
	}
	public Boolean getManagedealsVisibility() {
		return managedealsVisibility;
	}
	public void setManagedealsVisibility(Boolean managedealsVisibility) {
		this.managedealsVisibility = managedealsVisibility;
	}
	public Boolean getManagecuisineVisibility() {
		return managecuisineVisibility;
	}
	public void setManagecuisineVisibility(Boolean managecuisineVisibility) {
		this.managecuisineVisibility = managecuisineVisibility;
	}
	public Boolean getManagesubitemsVisibility() {
		return managesubitemsVisibility;
	}
	public void setManagesubitemsVisibility(Boolean managesubitemsVisibility) {
		this.managesubitemsVisibility = managesubitemsVisibility;
	}
	public Boolean getManagealacarteitemsVisibility() {
		return managealacarteitemsVisibility;
	}
	public void setManagealacarteitemsVisibility(
			Boolean managealacarteitemsVisibility) {
		this.managealacarteitemsVisibility = managealacarteitemsVisibility;
	}
	public Boolean getManagetaxesVisibility() {
		return managetaxesVisibility;
	}
	public void setManagetaxesVisibility(Boolean managetaxesVisibility) {
		this.managetaxesVisibility = managetaxesVisibility;
	}
	public Boolean getSuscriptionVisibility() {
		return suscriptionVisibility;
	}
	public void setSuscriptionVisibility(Boolean suscriptionVisibility) {
		this.suscriptionVisibility = suscriptionVisibility;
	}
	public Boolean getSubscriptionListVisibility() {
		return subscriptionListVisibility;
	}
	public void setSubscriptionListVisibility(Boolean subscriptionListVisibility) {
		this.subscriptionListVisibility = subscriptionListVisibility;
	}

	public Boolean getReceivedOrdersVisibility() {
		return receivedOrdersVisibility;
	}

	public void setReceivedOrdersVisibility(Boolean receivedOrdersVisibility) {
		this.receivedOrdersVisibility = receivedOrdersVisibility;
	}

	public Boolean getSubscriptionOrderVisibility() {
		return subscriptionOrderVisibility;
	}

	public void setSubscriptionOrderVisibility(Boolean subscriptionOrderVisibility) {
		this.subscriptionOrderVisibility = subscriptionOrderVisibility;
	}

	public Boolean getOrderDashboardVisibility() {
		return orderDashboardVisibility;
	}

	public void setOrderDashboardVisibility(Boolean orderDashboardVisibility) {
		this.orderDashboardVisibility = orderDashboardVisibility;
	}

	public Boolean getKitchenGroupVisibility() {
		return kitchenGroupVisibility;
	}

	public void setKitchenGroupVisibility(Boolean kitchenGroupVisibility) {
		this.kitchenGroupVisibility = kitchenGroupVisibility;
	}

	public Boolean getBikerManageVisibility() {
		return bikerManageVisibility;
	}

	public void setBikerManageVisibility(Boolean bikerManageVisibility) {
		this.bikerManageVisibility = bikerManageVisibility;
	}
	
}
