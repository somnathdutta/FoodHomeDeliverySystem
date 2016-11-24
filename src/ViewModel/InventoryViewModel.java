package ViewModel;

import java.sql.Connection;
import java.util.ArrayList;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import service.InventoryService;
import Bean.InventoryBean;
import Bean.ItemPackMaster;
import Bean.ManageKitchens;

public class InventoryViewModel {
	
	public InventoryBean inventoryBean = new InventoryBean();
	public InventoryBean inventoryAllocationBean = new InventoryBean();
	public ManageKitchens kitchenBean = new ManageKitchens();
	public ManageKitchens kitchenAllocationBean = new ManageKitchens();
	public ItemPackMaster itemPackMasterBean = new ItemPackMaster();
	
	
	private ArrayList<ItemPackMaster> itemPackMasterBeanList = new ArrayList<ItemPackMaster>();
	private ArrayList<InventoryBean> inventoryBeanList = new ArrayList<InventoryBean>();
	private ArrayList<ManageKitchens> kitchenAllocationList = new ArrayList<ManageKitchens>();
	private ArrayList<InventoryBean> inventoryAllocationBeanList = new ArrayList<InventoryBean>();
	private ArrayList<ManageKitchens> kitchenList = new ArrayList<ManageKitchens>();
	
	
	
	
	
	
	
	Session session = null;
	private Connection connection = null;
	private String userName;
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		kitchenList = InventoryService.loadKitchen(connection);
		kitchenAllocationList = InventoryService.loadKitchen(connection);
		
		System.out.println("zul page >> inventory.zul");
	}
	
	@Command
	@NotifyChange("*")
	public void onSelctKitchenAllocate(){
		itemPackMasterBeanList = InventoryService.loadPacking(connection, kitchenAllocationBean.getKitchenId());
		inventoryAllocationBeanList = InventoryService.loadKitchenPacking(connection, kitchenAllocationBean.getKitchenId());
	}
	
	@Command
	@NotifyChange("*")
	public void onClickSavePackAllocation(){
		int i =0;
		if(kitchenAllocationBean.getKitchenId() != null){
		   if(itemPackMasterBean.getItemPackName()!= null){
			  i = InventoryService.saveKitchenPackage(connection,kitchenAllocationBean, itemPackMasterBean, userName);
		   }else {
			Messagebox.show("Select Packing Type", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		   }
		}else {
			Messagebox.show("Select Kitchen", "Alert", Messagebox.OK, Messagebox.EXCLAMATION);
		}
		if(i>0){
			itemPackMasterBean.setItemPackName(null);
			
			itemPackMasterBeanList = InventoryService.loadPacking(connection, kitchenAllocationBean.getKitchenId());
			inventoryAllocationBeanList = InventoryService.loadKitchenPacking(connection, kitchenAllocationBean.getKitchenId());
			Messagebox.show("Saved Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickPackAllocationClear(){
		kitchenAllocationBean.kitchenName = null;
		kitchenAllocationBean.kitchenId = null;
		kitchenAllocationList.clear();
		itemPackMasterBeanList.clear();
		itemPackMasterBean.setItemPackName(null);
		kitchenAllocationList = InventoryService.loadKitchen(connection);		
		inventoryAllocationBeanList.clear();
	}
	
	@Command
	@NotifyChange("*")
	public void onSelectKitchen(){
		
		inventoryBeanList = InventoryService.loadInventoryDetails(connection, kitchenBean.getKitchenId());
		if(inventoryBeanList.size() ==0){
			Messagebox.show("No Data Found", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}	
	}
	
	
	@Command
	@NotifyChange("*")
	public void onClickUpdate(@BindingParam("bean")InventoryBean bean){
		int i=0;
		i = InventoryService.updateInventory(connection, bean, userName);
		if(i>0){
			Messagebox.show("Updated Successfully", "Information", Messagebox.OK, Messagebox.INFORMATION);
		}
	}


	public InventoryBean getInventoryBean() {
		return inventoryBean;
	}


	public void setInventoryBean(InventoryBean inventoryBean) {
		this.inventoryBean = inventoryBean;
	}


	public InventoryBean getInventoryAllocationBean() {
		return inventoryAllocationBean;
	}


	public void setInventoryAllocationBean(InventoryBean inventoryAllocationBean) {
		this.inventoryAllocationBean = inventoryAllocationBean;
	}


	public ManageKitchens getKitchenBean() {
		return kitchenBean;
	}


	public void setKitchenBean(ManageKitchens kitchenBean) {
		this.kitchenBean = kitchenBean;
	}


	public ManageKitchens getKitchenAllocationBean() {
		return kitchenAllocationBean;
	}


	public void setKitchenAllocationBean(ManageKitchens kitchenAllocationBean) {
		this.kitchenAllocationBean = kitchenAllocationBean;
	}


	public ArrayList<InventoryBean> getInventoryBeanList() {
		return inventoryBeanList;
	}


	public void setInventoryBeanList(ArrayList<InventoryBean> inventoryBeanList) {
		this.inventoryBeanList = inventoryBeanList;
	}


	public ArrayList<ManageKitchens> getKitchenAllocationList() {
		return kitchenAllocationList;
	}


	public void setKitchenAllocationList(
			ArrayList<ManageKitchens> kitchenAllocationList) {
		this.kitchenAllocationList = kitchenAllocationList;
	}


	public ArrayList<ManageKitchens> getKitchenList() {
		return kitchenList;
	}


	public void setKitchenList(ArrayList<ManageKitchens> kitchenList) {
		this.kitchenList = kitchenList;
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


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ItemPackMaster getItemPackMasterBean() {
		return itemPackMasterBean;
	}

	public void setItemPackMasterBean(ItemPackMaster itemPackMasterBean) {
		this.itemPackMasterBean = itemPackMasterBean;
	}

	public ArrayList<ItemPackMaster> getItemPackMasterBeanList() {
		return itemPackMasterBeanList;
	}

	public void setItemPackMasterBeanList(
			ArrayList<ItemPackMaster> itemPackMasterBeanList) {
		this.itemPackMasterBeanList = itemPackMasterBeanList;
	}

	public ArrayList<InventoryBean> getInventoryAllocationBeanList() {
		return inventoryAllocationBeanList;
	}

	public void setInventoryAllocationBeanList(
			ArrayList<InventoryBean> inventoryAllocationBeanList) {
		this.inventoryAllocationBeanList = inventoryAllocationBeanList;
	}
	
	

	
	
}
