package ViewModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.util.Beta;
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

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import Bean.ClusterBean;
import Bean.ManageDeliveryBoyBean;

public class ManageClusterViewModel {

	Session session = null;
	
	private Connection connection = null;
	
	private String userName;
	
	private Integer roleId;
	
	private String roleName;
	
	private String groupName ;
	
	private ClusterBean clusterBEAN = new ClusterBean();
	
	private Set<ClusterBean> clusterBeanList = new HashSet<ClusterBean>();
	
	private Set<ClusterBean> clusterBeanSet = new HashSet<ClusterBean>();
	
	private ArrayList<ClusterBean> groupList = new ArrayList<ClusterBean>();
	
	private ArrayList<ClusterBean> selectedGroupList = new ArrayList<ClusterBean>();
	
	private ArrayList<ManageDeliveryBoyBean> driverList = new ArrayList<ManageDeliveryBoyBean>();
	
	private ManageDeliveryBoyBean driverBean = new ManageDeliveryBoyBean();
	
	@AfterCompose
	public void initSetup(@ContextParam(ContextType.VIEW) Component view) throws Exception {

		Selectors.wireComponents(view, this, false);
		
		session= Sessions.getCurrent();
		
		connection=(Connection) session.getAttribute("sessionConnection");
		
		userName = (String) session.getAttribute("login");
		
		connection.setAutoCommit(true);
		
		roleId = (Integer) session.getAttribute("userRoleId");
	
		onLoadQuery();
		
		loadGroups();
		
		loadDriverList();
	}
	
	public void onLoadQuery(){
		if(clusterBeanList.size()>0){
			clusterBeanList.clear();
		}
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "select kitchen_id,kitchen_name,pincode from vw_kitchens_details order by kitchen_id asc";
				try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						ClusterBean bean = new ClusterBean();
						bean.kichenId = resultSet.getInt("kitchen_id");
						bean.kitchenName = resultSet.getString("kitchen_name");
						bean.pincode = resultSet.getString("pincode");
						
						clusterBeanList.add(bean);
					}
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
					connection.rollback();
				}finally{
					if(preparedStatement !=null){
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
	public void onClickItem(@BindingParam("bean")ClusterBean bean){
		bean.isSelected =true;
	
	}

	@Command
	@NotifyChange("*")
	public void onClickRemoveItem(@BindingParam("bean")ClusterBean bean){
		bean.isDeSelected = true;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickAddOne(){
		Iterator<ClusterBean> iter = clusterBeanList.iterator();

		while (iter.hasNext()) {
		    ClusterBean bean = iter.next();
		    	
		    if(bean.isSelected){
		    	bean.isSelected = false;
		        iter.remove();
		        clusterBeanSet.add(bean);
		    }
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickAddAll(){
		clusterBeanSet.addAll(clusterBeanList);
		clusterBeanList.removeAll(clusterBeanList);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickRemoveOne(){
		Iterator<ClusterBean> iter = clusterBeanSet.iterator();

		while (iter.hasNext()) {
		    ClusterBean bean = iter.next();
		   if(bean.isDeSelected){
		    	 iter.remove();
		        clusterBeanList.add(bean);
		      
		    }
		}
		
	}
	
	@Command
	@NotifyChange("*")
	public void onClickRemoveAll(){
		clusterBeanList.addAll(clusterBeanSet);
		clusterBeanSet.removeAll(clusterBeanSet);
	}
	
	@Command
	@NotifyChange("*")
	public void onClickCreate(){
		int groupId;
		if(isValid()){
			groupId = addGroupName();
			if(groupId>0){
				saveGroup(groupId);
			}
		}
	}
	
	public int addGroupName() {
		boolean added = false;
		int newInsertedId = 0;
		try {
			SQL: {
			PreparedStatement preparedStatement = null;
			String sql = "INSERT INTO fapp_kitchen_group(group_name) VALUES(?) ";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.setString(1, groupName.toUpperCase());
				int count = preparedStatement.executeUpdate();
				if (count > 0) {
					added = true;
					System.out.println("GROUP CREATED. . .");
				} else {
					added = false;
					System.out.println("GROUP CREATION FAILED. . .");
				}
			} catch (Exception e) {
				if(e.getMessage().contains("duplicate key value")){
					Messagebox.show("GROUP NAME ALREADY TAKEN !","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
				}else{
					Messagebox.show(
							"GROUP CREATION FAILED DUE TO ::" + e.getMessage(),
							"ERROR", Messagebox.OK, Messagebox.ERROR);
					e.printStackTrace();
				}
				connection.rollback();
				added = false;
			} finally {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}
		}

		if (added) {
			SQL: {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "SELECT group_id FROM fapp_kitchen_group ORDER BY group_id DESC LIMIT 1";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					newInsertedId = resultSet.getInt("group_id");
				}
			} catch (Exception e) {
				Messagebox
				.show("GROUP ID Retrieval DUE TO ::"
						+ e.getMessage(), "ERROR",
						Messagebox.OK, Messagebox.ERROR);
				e.printStackTrace();
				connection.rollback();
				newInsertedId = 0;
			} finally {
				if (preparedStatement != null) {
					preparedStatement.close();
				}
			}
		}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return newInsertedId;
	}
	
	public boolean saveGroup(int groupId){
		boolean isSaved = false;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql ="INSERT INTO fapp_kitchen_group_details(kitchen_id, kitchen_name, pincode, group_id)"
							   + "VALUES ( ?, ?, ?, ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						for(ClusterBean bean : clusterBeanSet){
							preparedStatement.setInt(1, bean.kichenId);
							preparedStatement.setString(2, bean.kitchenName);
							preparedStatement.setString(3, bean.pincode);
							preparedStatement.setInt(4, groupId);
							preparedStatement.addBatch();
						}
						int[] count = preparedStatement.executeBatch();
						if(count.length>0){
							isSaved = true;
							Messagebox.show("GROUP CREATED SUCCESSFULLY!","INFORMATION",Messagebox.OK,Messagebox.INFORMATION);
							clear();
						}
					} catch (Exception e) {
						if(e.getMessage().contains("duplicate key value")){
							Messagebox.show("The Kitchen with group is already assigned!","INFORMATION",Messagebox.OK,Messagebox.EXCLAMATION);
						}else{
							Messagebox.show(
									"GROUP CREATION FAILED DUE TO ::" + e.getMessage(),
									"ERROR", Messagebox.OK, Messagebox.ERROR);
							e.printStackTrace();
						}
						connection.rollback();
						
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isSaved;
	}
	
	public void loadGroups(){
		if(groupList.size()>0){
			groupList.clear();
		}
		try {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			String sql = "Select group_id,group_name from fapp_kitchen_group";
			try {
				preparedStatement = connection.prepareStatement(sql);
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					ClusterBean bean = new ClusterBean();
					bean.groupId = resultSet.getInt("group_id");
					bean.groupName = resultSet.getString("group_name");
					groupList.add(bean);
				}
			} catch (Exception e) {
				Messagebox.show("ERROR DUE TO::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
				e.printStackTrace();
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
	public void onSelectGroup(@BindingParam("bean")ClusterBean bean){
		loadSelectedGroupDetails(bean.groupId);
		groupName = bean.groupName;
		//driverBean.deliveryBoyUserId = getDriverUserId(bean.groupId);
	}
	
	public void loadSelectedGroupDetails(int groupId){
		if(selectedGroupList.size()>0){
			selectedGroupList.clear();
		}
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "select kitchen_id,kitchen_name,pincode from fapp_kitchen_group_details"
						+ " where group_id = ?  order by kitchen_id asc";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setInt(1, groupId);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						ClusterBean bean = new ClusterBean();
						bean.kichenId = resultSet.getInt("kitchen_id");
						bean.kitchenName = resultSet.getString("kitchen_name");
						bean.pincode = resultSet.getString("pincode");
						
						selectedGroupList.add(bean);
					}
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
					connection.rollback();
				}finally{
					if(preparedStatement !=null){
						preparedStatement.close();
					}
				}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	
	
	public void loadDriverList(){
		if(driverList.size()>0){
			driverList.clear();
		}
		try {
			SQL:{
				PreparedStatement preparedStatement = null;
				ResultSet resultSet = null;
				String sql = "select delivery_boy_id,delivery_boy_name,delivery_boy_user_id,delivery_boy_phn_number"
							+ " from fapp_delivery_boy"
							+ " where kitchen_id IS NULL";
				try {
					preparedStatement = connection.prepareStatement(sql);
					resultSet = preparedStatement.executeQuery();
					while (resultSet.next()) {
						ManageDeliveryBoyBean bean = new ManageDeliveryBoyBean();
						bean.deliveryBoyUserId = resultSet.getString("delivery_boy_user_id");
						bean.deliveryBoyId = resultSet.getInt("delivery_boy_id");
						bean.phoneNo = resultSet.getString("delivery_boy_phn_number");
						bean.name = resultSet.getString("delivery_boy_name");
						driverList.add(bean);
					}
				} catch (Exception e) {
					Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
					e.printStackTrace();
					connection.rollback();
				}finally{
					if(preparedStatement !=null){
						preparedStatement.close();
					}
				}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
	System.out.println("SIZE::  "+selectedGroupList.size());
	}
	
	@Command
	@NotifyChange("*")
	public void onClickAssignDriver(){
		if(isValidate())
		assignDriver(driverBean.deliveryBoyId, clusterBEAN.groupId);
	}
	
	public void assignDriver(int driverId, int groupId){
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_group_driver(group_id, driver_id)VALUES ( ?, ?)";
					try {
						preparedStatement =connection.prepareStatement(sql);
						preparedStatement.setInt(1, groupId);
						preparedStatement.setInt(2, driverId);
						int count = preparedStatement.executeUpdate();
						if(count>0){
							Messagebox.show("DRIVER MAPPED WITH GROUP SUCCESSFULLY!","Information",Messagebox.OK,Messagebox.INFORMATION);
							loadDriverList();
						}
					} catch (Exception e) {
						if(e.getMessage().contains("duplicate key value")){
							Messagebox.show("This driver is already assigned for this group!","INFORMATION",Messagebox.OK,Messagebox.EXCLAMATION);
						}else{
							Messagebox.show(
									"DRIVER MAPPING FAILED DUE TO ::" + e.getMessage(),
									"ERROR", Messagebox.OK, Messagebox.ERROR);
							e.printStackTrace();
						}
						connection.rollback();
					}finally{
						if(preparedStatement !=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public String getDriverUserId(int groupId){
		String boyUserId = null ;
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					ResultSet resultSet = null;
					String sql = "select delivery_boy_user_id from fapp_delivery_boy where delivery_boy_id= "
								+"(select driver_id from fapp_kitchen_group where group_id = ?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.setInt(1, groupId);
						resultSet = preparedStatement.executeQuery();
						while (resultSet.next()) {
							boyUserId = resultSet.getString("delivery_boy_user_id");
						}
					}  catch (Exception e) {
						Messagebox.show("ERROR DUE TO ::"+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						e.printStackTrace();
						connection.rollback();
					}finally{
						if(preparedStatement !=null){
							preparedStatement.close();
						}
					}
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return boyUserId;
	}
	
	@Command
	@NotifyChange("*")
	public void onClickAdd(){
		System.out.println("GRoup name : "+clusterBEAN.groupName);
		ArrayList<ClusterBean> clusterBeans = new ArrayList<ClusterBean>();
		if(clusterBEAN.groupName != null){
			Iterator<ClusterBean> iter = clusterBeanSet.iterator();
			while (iter.hasNext()) {
			    ClusterBean bean = iter.next();
			     
			    if(bean.isDeSelected){
			    	bean.groupId = clusterBEAN.groupId;
			    	clusterBeans.add(bean);
			    }
			}
		}else{
    		Messagebox.show("Group Name required!","Alert",Messagebox.OK,Messagebox.EXCLAMATION);
    	}
		if(clusterBeanList.size()>0){
			saveNewKitchenToGroup(clusterBeans);
		}
	}
	
	public void saveNewKitchenToGroup(ArrayList<ClusterBean> clusterBeanList){
		boolean saved = false;
		System.out.println("size :"+clusterBeanList.size());
		try {
			SQL:{
					PreparedStatement preparedStatement = null;
					String sql = "INSERT INTO fapp_kitchen_group_details(kitchen_name,kitchen_id,pincode,group_id) "
							+ " values(?,?,?,?)";
					try {
						preparedStatement = connection.prepareStatement(sql);
						for(ClusterBean bean : clusterBeanList){
							if(bean.isDeSelected){
								preparedStatement.setString(1, bean.kitchenName);
								preparedStatement.setInt(2, bean.kichenId);
								preparedStatement.setString(3, bean.pincode);
								preparedStatement.setInt(4, bean.groupId);
								
								preparedStatement.addBatch();
							}
						}
						int[] count = preparedStatement.executeBatch();
						for(Integer c : count){
							saved = true;
						}
					} catch (Exception e) {
						// TODO: handle exception
						connection.rollback();
						e.printStackTrace();
						if(e.getMessage().startsWith("Batch entry")){
							Messagebox.show("Kitchens Already added!","Error",Messagebox.OK,Messagebox.ERROR);
						}else{
							Messagebox.show("Addition to group faied due to: "+e.getMessage(),"ERROR",Messagebox.OK,Messagebox.ERROR);
						}
						
					}finally{
						if(preparedStatement!=null){
							preparedStatement.close();
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(saved){
			Messagebox.show("Added to group!","Information",Messagebox.OK,Messagebox.INFORMATION);
			clusterBEAN.groupName = null;
			groupName = null;
			clusterBeanSet.clear();
			clusterBeanList.clear();
			loadGroups();
		}
	}
	
	public boolean isValid(){
		if(clusterBeanSet.size()>0){
			if(groupName!=null){
				return true;
			}else{
				Messagebox.show("Group Name required!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("No Kitchens added!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public boolean isValidate(){
		if(clusterBEAN.groupName!=null){
			if(driverBean.deliveryBoyId!=null){
				return true;
			}else{
				Messagebox.show("Driver Name required!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
				return false;
			}
		}else{
			Messagebox.show("Group Name required!","ALERT",Messagebox.OK,Messagebox.EXCLAMATION);
			return false;
		}
	}
	
	public void clear(){
		clusterBeanSet.clear();
		groupName = null;
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

	public ClusterBean getClusterBEAN() {
		return clusterBEAN;
	}

	public void setClusterBEAN(ClusterBean clusterBEAN) {
		this.clusterBEAN = clusterBEAN;
	}

	
	public Set<ClusterBean> getClusterBeanSet() {
		return clusterBeanSet;
	}

	public void setClusterBeanSet(Set<ClusterBean> clusterBeanSet) {
		this.clusterBeanSet = clusterBeanSet;
	}

	public Set<ClusterBean> getClusterBeanList() {
		return clusterBeanList;
	}

	public void setClusterBeanList(Set<ClusterBean> clusterBeanList) {
		this.clusterBeanList = clusterBeanList;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ArrayList<ClusterBean> getGroupList() {
		return groupList;
	}

	public void setGroupList(ArrayList<ClusterBean> groupList) {
		this.groupList = groupList;
	}

	public ArrayList<ClusterBean> getSelectedGroupList() {
		return selectedGroupList;
	}

	public void setSelectedGroupList(ArrayList<ClusterBean> selectedGroupList) {
		this.selectedGroupList = selectedGroupList;
	}

	public ArrayList<ManageDeliveryBoyBean> getDriverList() {
		return driverList;
	}

	public void setDriverList(ArrayList<ManageDeliveryBoyBean> driverList) {
		this.driverList = driverList;
	}

	public ManageDeliveryBoyBean getDriverBean() {
		return driverBean;
	}

	public void setDriverBean(ManageDeliveryBoyBean driverBean) {
		this.driverBean = driverBean;
	}

	
	
}
