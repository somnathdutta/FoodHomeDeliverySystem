package ViewModel;

import java.sql.Connection;
import java.sql.SQLException;
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

import service.ItemPackMasterService;
import dao.ItemPackMasterDAO;
import Bean.ItemPackMaster;

public class ItemPackMasterViewModel {

	private ItemPackMaster itemPackMaster = new ItemPackMaster();
	
	private ArrayList<ItemPackMaster> itemPackMasterList = new ArrayList<ItemPackMaster>();
	
	Session session = null;
	
	private Connection connection = null;
	
	private String userName;

	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws SQLException{

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		itemPackMaster.setUser(userName);
		
		connection.setAutoCommit(true);
				
		onLoad();
	}
	
	public void onLoad(){
		itemPackMasterList = ItemPackMasterDAO.fetchAllItemPack(connection);
	}
	
	public void saveItemPack(){
		if(ItemPackMasterService.isValid(itemPackMaster)){
			ItemPackMasterDAO.saveItemPackTypeMaster(itemPackMaster, connection);
			onLoad();
		}
	}
	
	public void updateItemPack(ItemPackMaster itempackmaster){
		if(ItemPackMasterService.isValid(itempackmaster)){
			ItemPackMasterDAO.updateItemPackTypeMaster(itempackmaster, connection);
			onLoad();
		}
	}

	@Command
	@NotifyChange("*")
	public void onClickSave(){
		saveItemPack();
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdate(@BindingParam("bean")ItemPackMaster itempackmaster){
		itempackmaster.setUser(userName);
		updateItemPack(itempackmaster);
	}
	
	public ItemPackMaster getItemPackMaster() {
		return itemPackMaster;
	}

	public void setItemPackMaster(ItemPackMaster itemPackMaster) {
		this.itemPackMaster = itemPackMaster;
	}

	public ArrayList<ItemPackMaster> getItemPackMasterList() {
		return itemPackMasterList;
	}

	public void setItemPackMasterList(ArrayList<ItemPackMaster> itemPackMasterList) {
		this.itemPackMasterList = itemPackMasterList;
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
	
	
}
