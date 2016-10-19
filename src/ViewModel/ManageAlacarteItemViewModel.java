package ViewModel;

import java.sql.Connection;
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
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;









import org.zkoss.zul.Window;

import Bean.ManageAlacarteItemBean;
import Bean.ManageCategoryItemBean;

public class ManageAlacarteItemViewModel {
	
	public ManageAlacarteItemBean manageAlacarteItemBEAN = new ManageAlacarteItemBean();
	
	private ArrayList<ManageAlacarteItemBean> alacarteItemBeanList = new ArrayList<ManageAlacarteItemBean>();
	
	private Connection connection = null;
	
	Session session = null;
	
	private String userName = "";
	
	PropertyFile propertyfile = new PropertyFile();
	
	public Boolean saveButtonVisibility = true;
	
	public Boolean updateButtonVisibility = false;
		
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		loadAllAlacarteItems();
	}
	
	/**
	 * 
	 * GlobalReload is used to reload the grid when child window is detach
	 */
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		loadAllAlacarteItems();
		refresh();
		updateButtonVisibility = false;
		saveButtonVisibility = true;
	}
	
	/**
	 * This method is useful to load saved alacarte item to the front page
	 */
	public void loadAllAlacarteItems(){
		if(alacarteItemBeanList.size()>0){
			alacarteItemBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement =  null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("onLoadAlacarteItemListSql"));
						resultSet = preparedStatement.executeQuery();
						
						while(resultSet.next()){
							
							ManageAlacarteItemBean manageAlacarteItemBean = new ManageAlacarteItemBean();
							manageAlacarteItemBean.alacarteitemId = resultSet.getInt("alacarte_item_id");
							manageAlacarteItemBean.alacarteItem = resultSet.getString("alacarte_item_name");
							manageAlacarteItemBean.price = resultSet.getDouble("alacarte_item_price");
							if( resultSet.getString("is_active").equalsIgnoreCase("Y")){
								manageAlacarteItemBean.status = "Active";
							}else{
								manageAlacarteItemBean.status = "Deactive";
							}
							alacarteItemBeanList.add(manageAlacarteItemBean);
							
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
	 * This method is useful for the functionality of save button
	 */
	@Command
	@NotifyChange("*")
	public void onClickSave(){
		if(fieldsIsEmpty()){
			saveAlaCarteItem();
		}
	}
	
	/**
	 * This method is useful to save the alacarte item to database
	 */
	public void saveAlaCarteItem(){
		System.out.println(manageAlacarteItemBEAN.alacarteItem+" "+manageAlacarteItemBEAN.price+" "+manageAlacarteItemBEAN.status);
		String message ="";
		Boolean inserted=false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
				try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("saveAlacarteItemSql"));
						
						preparedStatement.setString(1, manageAlacarteItemBEAN.alacarteItem);
						preparedStatement.setDouble(2, manageAlacarteItemBEAN.price);
						
						if(manageAlacarteItemBEAN.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(3, "Y");
						}else{
							preparedStatement.setString(3, "N");
						}
						
						preparedStatement.setString(4, userName);
						preparedStatement.setString(5, userName);
						
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message=resultSet.getString(1);
							inserted = true;
							System.out.println("Alacarte Item save message =" + message);
						}
						if (inserted) {
							Messagebox.show("Alacarte item Saved Successfully...");
							loadAllAlacarteItems();
						}
				} catch (Exception e) {
					Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
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
		refresh();
	}
	
	/**
	 * onClickEdit is used to edit data
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean")ManageAlacarteItemBean manageAlacarteItemBean){
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet =null;
				try {
					preparedStatement = connection.prepareStatement(propertyfile.getPropValues("editAlacarteItemSql"));
					preparedStatement.setInt(1,manageAlacarteItemBean.alacarteitemId);
					manageAlacarteItemBEAN.alacarteitemId = manageAlacarteItemBean.alacarteitemId;
					resultSet = preparedStatement.executeQuery();
					while(resultSet.next()){
						manageAlacarteItemBEAN.alacarteitemId = resultSet.getInt("alacarte_item_id");
						manageAlacarteItemBEAN.alacarteItem = resultSet.getString("alacarte_item_name");
						manageAlacarteItemBEAN.price = resultSet.getDouble("alacarte_item_price"); 
						
						if(resultSet.getString("is_active").equals("Y")){
							manageAlacarteItemBEAN.status = "Active";
						}
						if(resultSet.getString("is_active").equals("N")){
							manageAlacarteItemBEAN.status = "Deactive";
						}
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
	 * onClickUpdateCategoryItem method is used to update category item data in the database
	 * 
	 */
	@Command
	@NotifyChange("*")
	public void onClickUpdate(){
		String message="";
		Boolean updated= false;
		if(fieldsIsEmpty()){
			try {
				SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					try {
						preparedStatement = connection.prepareStatement(propertyfile.getPropValues("updateAlacarteItemSql"));
						
						preparedStatement.setString(1, manageAlacarteItemBEAN.alacarteItem);
						preparedStatement.setDouble(2, manageAlacarteItemBEAN.price);
						
						if(manageAlacarteItemBEAN.status.equalsIgnoreCase("Active")){
							preparedStatement.setString(3, "Y");
						}
						else{
							preparedStatement.setString(3, "N");
						}
						preparedStatement.setString(4, userName);
						preparedStatement.setString(5, userName);
						preparedStatement.setInt(6,manageAlacarteItemBEAN.alacarteitemId);
						resultSet = preparedStatement.executeQuery();
						if(resultSet.next()){
							message = resultSet.getString(1);
							updated = true;
							System.out.println(message);
						}
						if(updated){
							Messagebox.show("Alacarte item data Updated Successfully...");
							refresh();
							loadAllAlacarteItems();
							saveButtonVisibility=true;
							updateButtonVisibility=false;
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
	}
	
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean")ManageAlacarteItemBean manageAlacarteItemBean){

		Map<String, Object> parentMap = new HashMap<String, Object>();
		
		parentMap.put("parentAlacarteObject", manageAlacarteItemBean);
		
		Window win = (Window) Executions.createComponents("alacarteItemDeleteConfirmMessageBox.zul", null, parentMap);
		
		win.doModal();
	}
	/**
	 * This method is useful to validate empty fields
	 */
	public Boolean fieldsIsEmpty(){
		if(manageAlacarteItemBEAN.alacarteItem!=null){
			if(manageAlacarteItemBEAN.price!=null){
				if(manageAlacarteItemBEAN.status!=null){
					return true;
				}else{
					Messagebox.show("Status required!");
					return false;
				}
			}else{
				Messagebox.show("Price required!");
				return false;
			}
		}else{
			Messagebox.show("Item name required!");
			return false;
		}
	}
	
	/**
	 * This method is useful to clear data after being saved
	 */
	public void refresh(){
		manageAlacarteItemBEAN.alacarteItem =null;
		manageAlacarteItemBEAN.price = null;
		manageAlacarteItemBEAN.status =null;
	}
/************************************* Getters and Setters *****************************************************/
	public ManageAlacarteItemBean getManageAlacarteItemBEAN() {
		return manageAlacarteItemBEAN;
	}

	public void setManageAlacarteItemBEAN(
			ManageAlacarteItemBean manageAlacarteItemBEAN) {
		this.manageAlacarteItemBEAN = manageAlacarteItemBEAN;
	}

	public ArrayList<ManageAlacarteItemBean> getAlacarteItemBeanList() {
		return alacarteItemBeanList;
	}

	public void setAlacarteItemBeanList(
			ArrayList<ManageAlacarteItemBean> alacarteItemBeanList) {
		this.alacarteItemBeanList = alacarteItemBeanList;
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
