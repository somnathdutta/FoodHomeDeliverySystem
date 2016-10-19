package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.poi.util.Beta;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zul.Messagebox;

import Bean.RoadRunnrBean;

public class RoadRunnrViewModel {

	Session session = null;
	
	private Connection connection = null;
	
	private RoadRunnrBean roadRunnrBean = new RoadRunnrBean();
	
	private ArrayList<RoadRunnrBean> roadRunnrBeanList = new ArrayList<RoadRunnrBean>();

	private String username;
	
	private Integer roleId;
	
	private boolean saveButtonVisibility = true;
	
	private boolean updateButtonVisibility = false;
	
	PropertyFile propertyfile = new PropertyFile();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		connection=(Connection) session.getAttribute("sessionConnection");
		username = (String) session.getAttribute("login");
		roleId = (Integer) session.getAttribute("userRoleId");
		connection.setAutoCommit(true);
		onLoadQuery();
	}

	public void onLoadQuery(){
		if(roadRunnrBeanList.size()>0){
			roadRunnrBeanList.clear();
		}
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "Select * from fapp_runnr where is_delete='N'";
					try {
						preparedStatement = connection.prepareStatement(sql);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							RoadRunnrBean bean = new RoadRunnrBean();
							bean.id = resultSet.getInt("loc_id");
							bean.localityID = resultSet.getString("locality_id");
							bean.locatityName = resultSet.getString("locality_name");
							bean.zipCode = resultSet.getString("tag_zipcodes");
							if(resultSet.getString("is_active").equals("Y")){
								bean.status = "Active";
							}else{
								bean.status = "Deactive";
							}
							roadRunnrBeanList.add(bean);
						}
					} catch (Exception e) {
						e.printStackTrace();
						Messagebox.show("Error due to :"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
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
	public void onClickSave(){
		if(isValid()){
			if(save()){
				Messagebox.show("Saved successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
			}
		}
	}
	
	@Command
	@NotifyChange("*")
	public void onClickEdit(@BindingParam("bean") RoadRunnrBean bean){
		roadRunnrBean.id = bean.id;
		roadRunnrBean.localityID = bean.localityID;
		roadRunnrBean.locatityName = bean.locatityName;
		roadRunnrBean.status = bean.status;
		roadRunnrBean.zipCode = bean.zipCode;
		saveButtonVisibility = false;
		updateButtonVisibility = true;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickUpdate(){
		if(isValid()){
			if(update()){
				Messagebox.show("Updated successfully!","Information",Messagebox.OK,Messagebox.INFORMATION);
			}
		}
	}
	
	@GlobalCommand
	@NotifyChange("*")
	public void globalReload(){
		onLoadQuery();
		clear();
	}
	
	public boolean save(){
		boolean inserted = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_runnr(tag_zipcodes, locality_name, locality_id,is_active,created_by) VALUES (?, ?, ?,?,?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, roadRunnrBean.zipCode);
						preparedStatement.setString(2, roadRunnrBean.locatityName);
						preparedStatement.setString(3, roadRunnrBean.localityID);
						if(roadRunnrBean.status.equals("Active")){
							preparedStatement.setString(4, "Y");
						}else{
							preparedStatement.setString(4, "N");
						}
						preparedStatement.setString(5, username);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							inserted = true;
							onLoadQuery();
							clear();
						}
					} catch (Exception e) {
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
		return inserted; 
	}
		
	public boolean update(){
		boolean updated = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "UPDATE fapp_runnr set tag_zipcodes=?, locality_name=?, locality_id=? ,is_active = ?,updated_by=?,updated_date=now() where loc_id = ? ";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setString(1, roadRunnrBean.zipCode);
						preparedStatement.setString(2, roadRunnrBean.locatityName);
						preparedStatement.setString(3, roadRunnrBean.localityID);
						if(roadRunnrBean.status.equals("Active")){
							preparedStatement.setString(4, "Y");
						}else{
							preparedStatement.setString(4, "N");
						}
						preparedStatement.setString(5, username);
						preparedStatement.setInt(6, roadRunnrBean.id);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							updated = true;
							onLoadQuery();
							clear();
						}
					} catch (Exception e) {
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
		return updated; 
	}
	
	@SuppressWarnings("unchecked")
	@Command
	@NotifyChange("*")
	public void onClickDelete(@BindingParam("bean") final RoadRunnrBean bean){
		Messagebox.show("Are you sure to delete?", "Confirm Dialog", 
				Messagebox.OK |  Messagebox.CANCEL, Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
		    public void onEvent(Event evt) throws InterruptedException {
		        if (evt.getName().equals("onOK")) {
			          if(isDelete(bean.id)){
			        	  BindUtils.postGlobalCommand(null, null, "globalReload", null);
						   Messagebox.show("Runnr data deleted successfully!");
			          }
		        } else if (evt.getName().equals("onIgnore")) {
		            Messagebox.show("Ignore Save", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		        } else {
		           
		        }
		    }
		});
	}
	
	public boolean isDelete(int locId){
 		boolean isDelete = false;
 		try {
			SQL:{
 					PreparedStatement preparedStatement  = null;
 					String sql = "UPDATE fapp_runnr set is_delete='Y'"
 							   +" where loc_id = ?" ;
 					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, locId);
						int count = preparedStatement.executeUpdate();
						if(count > 0){
							isDelete = true;
						}
					} catch (Exception e) {
						Messagebox.show("Error Due To: "+e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
						} finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}	
 			}
		} catch (Exception e) {
			// TODO: handle exception
		}
 		return isDelete;
 	}
	
	public boolean isValid(){
		if(roadRunnrBean.localityID != null){
			if(roadRunnrBean.locatityName != null){
				if(roadRunnrBean.zipCode != null){
					if(roadRunnrBean.status!=null){
						return true;
					}else{
						Messagebox.show("Status required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
						return false;
					}
				}else{
					Messagebox.show("Zip code required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
					return false;
				}
			}else{
				Messagebox.show("Locality Name required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Locality ID required!","Information",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public void clear(){
		roadRunnrBean.id= null;
		roadRunnrBean.localityID = null;
		roadRunnrBean.locatityName = null;
		roadRunnrBean.status = null;
		roadRunnrBean.zipCode = null;
		
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

	public RoadRunnrBean getRoadRunnrBean() {
		return roadRunnrBean;
	}

	public void setRoadRunnrBean(RoadRunnrBean roadRunnrBean) {
		this.roadRunnrBean = roadRunnrBean;
	}

	public ArrayList<RoadRunnrBean> getRoadRunnrBeanList() {
		return roadRunnrBeanList;
	}

	public void setRoadRunnrBeanList(ArrayList<RoadRunnrBean> roadRunnrBeanList) {
		this.roadRunnrBeanList = roadRunnrBeanList;
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

	public PropertyFile getPropertyfile() {
		return propertyfile;
	}

	public void setPropertyfile(PropertyFile propertyfile) {
		this.propertyfile = propertyfile;
	}

	public boolean isSaveButtonVisibility() {
		return saveButtonVisibility;
	}

	public void setSaveButtonVisibility(boolean saveButtonVisibility) {
		this.saveButtonVisibility = saveButtonVisibility;
	}

	public boolean isUpdateButtonVisibility() {
		return updateButtonVisibility;
	}

	public void setUpdateButtonVisibility(boolean updateButtonVisibility) {
		this.updateButtonVisibility = updateButtonVisibility;
	}
	
	
}
